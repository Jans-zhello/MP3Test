package com.example.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.download.HttpDownloader;
import com.example.model.XmlInfoModel;
import com.example.mp3test.R;
import com.example.mp3test.RemoteMp3ListActivity;
/**
 * Service中开启线程下载文件
 * @author Administrator
 *
 */
public class DownLoadService extends Service {
	private  int result;//mp3文件
	private int result2;//lrc文件
	public int onStartCommand(Intent intent, int flags, int startId) {
		XmlInfoModel xModel = (XmlInfoModel) intent
				.getSerializableExtra("xModel");
          new Thread(new DownloadThread(xModel)).start();
		return super.onStartCommand(intent, flags, startId);
	}

	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	class DownloadThread implements Runnable{
		private XmlInfoModel xModel = null; 
		public DownloadThread(XmlInfoModel xModel ){
			 this.xModel = xModel;
		 }
		public void run() {
			String mp3Url = "http://10.102.137.72:8080/mp3/"+xModel.getMp3Name();
			String lrcUrl = "http://10.102.137.72:8080/mp3/"+xModel.getLrcName();
			HttpDownloader downloader = new HttpDownloader();
			result = downloader.downFile(mp3Url,"mp3/",xModel.getMp3Name());
			result2 = downloader.downFile(lrcUrl,"lrc/",xModel.getLrcName());
			getNotification();
 		}
	}
	 public void getNotification() {
		    String msg = null;
		    if (result == 0 && result2 ==0) {
				msg = "下载成功";
			}else if (result == -1 || result2 == -1) {
				msg = "下载失败";
			}else {
				msg = "文件已存在";
			}
	        //得到NotificationManager的对象，用来实现发送Notification
	        NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
	        //得到Notification对象
	        Notification notification = new Notification();
	        //设置消息来时显示的消息
	        notification.tickerText = "新消息";
	        //设置消息来时显示图标
	        notification.icon = R.drawable.ic_launcher;
	        //设置是否会消失
	        notification.flags =Notification.FLAG_AUTO_CANCEL;
	        //设置消息来时震动
	         notification.defaults =Notification.DEFAULT_VIBRATE;
	        //设置当前时间
	        long when = System.currentTimeMillis();
	        notification.when = when;
	        //通知的跳转事件
	        Intent intent = new Intent(getApplication(), RemoteMp3ListActivity.class);
	        /**Intent一般是用作Activity、Sercvice、BroadcastReceiver之间传递数据，
	                      而Pendingintent，一般用在 Notification上，
	                    可以理解为延迟执行的intent，PendingIntent是对Intent一个包装。*/
	        //参数：1、上下文 2、请求码 3、用于启动的intent 4、新开启的Activity的启动模式
	        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
	        //参数1、上下文 2、下拉通知栏 显示的标题 3、内容  4、PendingIntent对象
	        notification.setLatestEventInfo(this, "下载消息", msg, pendingIntent);
	        //启动Notification
	        manager.notify(0, notification);
	        //取消通知
	        //manager.cancelAll();
	    }
}
