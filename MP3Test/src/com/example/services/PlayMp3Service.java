package com.example.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;

import com.example.lrcprocess.LrcProcess;
import com.example.model.XmlInfoModel;
import com.example.mp3test.PlayMp3Activity;

/**
 * Service中播放音乐文件
 * @author Administrator
 *
 */
public class PlayMp3Service extends Service {
	MediaPlayer mediaPlayer = null;
	private boolean isplaying = false;
	private boolean ispause = false;
	private boolean isrelease = false;
	private ArrayList<Queue> list = null;
	private String lrcmsg = null;
	private Handler handler = new Handler();
	private UpdateTimeCallBack updatetimecallback = null;
	private long begintime = 0;
	private long nexttime = 0;
	private long currenttime = 0;
	private String message = null;
	private long pausetime = 0;
	public int onStartCommand(Intent intent, int flags, int startId) {
		XmlInfoModel xModel = (XmlInfoModel)intent
				.getSerializableExtra("xm");
		int MSG = intent.getIntExtra("MSG",0);
		if (xModel != null) {
			switch (MSG) {
			case PlayMp3Activity.BEGIN_MSG:
				 begin(xModel); 
				 prepareLrc(xModel.getLrcName());
				 begintime = System.currentTimeMillis();
				 handler.postDelayed(updatetimecallback,5);
				break;
			case PlayMp3Activity.PAUSE_MSG:
				if (isplaying) {
					 handler.removeCallbacks(updatetimecallback);
					 pausetime = System.currentTimeMillis();
				}else {
					handler.postDelayed(updatetimecallback,5);
					begintime = System.currentTimeMillis()-pausetime+begintime;
				}
				pause();
				break;
			case PlayMp3Activity.OVER_MSG:
				 over();
				 handler.removeCallbacks(updatetimecallback);
				break;
			default:
				break;
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
    /**
     * 准备歌词文件
     */
	private void prepareLrc(String lrcName){
		try {
			InputStream inputStream = new FileInputStream(new File(Environment.getExternalStorageDirectory()+File.separator+"lrc/"+lrcName));
			LrcProcess lrcProcess = new LrcProcess();
			list = lrcProcess.processlrc(inputStream);
			System.out.println(list);
			updatetimecallback = new UpdateTimeCallBack(list);
			currenttime = 0;
			nexttime = 0;
		} catch (Exception e) {	
		}
	}
	/**
	 * 不断更新歌词
	 */
	class UpdateTimeCallBack implements Runnable{
		Queue time = null;
		Queue lrc = null;
		public UpdateTimeCallBack(ArrayList<Queue> list){
			time = list.get(0);
			lrc = list.get(1);
		}
		public void run() {
             //计算偏移量
			long offset = System.currentTimeMillis() - begintime;
			System.out.println("偏移量"+offset);
			if (currenttime == 0) {//UpdateTimeCallBack第一次执行的时候
				nexttime = (Long) time.poll();
				lrcmsg = (String) lrc.poll();
				Intent intent = new Intent();
				intent.setAction("lrc.action");
				intent.putExtra("lrc",lrcmsg);
				sendBroadcast(intent);
			}
			if (offset >= nexttime) {//当前时间等于偏移量的时候(大于的原因是考虑存在误差)，显示歌词，并取出下个内容
				Intent intent = new Intent();
				intent.setAction("lrc.action");
				intent.putExtra("lrc",lrcmsg);
				sendBroadcast(intent);
				if ((time.peek() != null) && (lrc.peek() != null)) {
					nexttime = (Long) time.poll();
					lrcmsg = (String) lrc.poll();
				}
			}
			currenttime = currenttime + 10;//为了不再执行上方的if (currenttime == 0)语句，因为那个仅仅是第一次执行这个类的该方法时候的才走的程序
		    handler.postDelayed(updatetimecallback,10);//每隔10毫秒执行一次 查一查看offset是否等于nexttime	
		} 
	}

	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
    public void begin(XmlInfoModel xm){
		if (isplaying == false) {
			mediaPlayer = MediaPlayer.create(
					this,
					Uri.parse("file://"
							+ (Environment.getExternalStorageDirectory()
									+ File.separator + "mp3"
									+ File.separator + xm.getMp3Name())));
			mediaPlayer.setLooping(false);// 不让它循环播放
			mediaPlayer.start();
			isplaying = true;
			isrelease = false;
			ispause = false;
		}
    }
    public void pause(){
		if (mediaPlayer != null) {
			if (isrelease == false) {
				if (ispause == false) {
					mediaPlayer.pause();
					ispause = true;
					isplaying = false;
				} else {
					mediaPlayer.start();
					ispause = false;
					isplaying = true;
				}
			}
		}
    }
    public void over(){
		if (mediaPlayer != null) {
			if (isplaying == true) {
				if (isrelease == false) {
					mediaPlayer.stop();
					mediaPlayer.release();
					isrelease = true;
					isplaying = false;
					ispause = false;
				}
			}
		}
    }
}
