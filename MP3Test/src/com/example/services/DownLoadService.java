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
 * Service�п����߳������ļ�
 * @author Administrator
 *
 */
public class DownLoadService extends Service {
	private  int result;//mp3�ļ�
	private int result2;//lrc�ļ�
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
				msg = "���سɹ�";
			}else if (result == -1 || result2 == -1) {
				msg = "����ʧ��";
			}else {
				msg = "�ļ��Ѵ���";
			}
	        //�õ�NotificationManager�Ķ�������ʵ�ַ���Notification
	        NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
	        //�õ�Notification����
	        Notification notification = new Notification();
	        //������Ϣ��ʱ��ʾ����Ϣ
	        notification.tickerText = "����Ϣ";
	        //������Ϣ��ʱ��ʾͼ��
	        notification.icon = R.drawable.ic_launcher;
	        //�����Ƿ����ʧ
	        notification.flags =Notification.FLAG_AUTO_CANCEL;
	        //������Ϣ��ʱ��
	         notification.defaults =Notification.DEFAULT_VIBRATE;
	        //���õ�ǰʱ��
	        long when = System.currentTimeMillis();
	        notification.when = when;
	        //֪ͨ����ת�¼�
	        Intent intent = new Intent(getApplication(), RemoteMp3ListActivity.class);
	        /**Intentһ��������Activity��Sercvice��BroadcastReceiver֮�䴫�����ݣ�
	                      ��Pendingintent��һ������ Notification�ϣ�
	                    �������Ϊ�ӳ�ִ�е�intent��PendingIntent�Ƕ�Intentһ����װ��*/
	        //������1�������� 2�������� 3������������intent 4���¿�����Activity������ģʽ
	        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
	        //����1�������� 2������֪ͨ�� ��ʾ�ı��� 3������  4��PendingIntent����
	        notification.setLatestEventInfo(this, "������Ϣ", msg, pendingIntent);
	        //����Notification
	        manager.notify(0, notification);
	        //ȡ��֪ͨ
	        //manager.cancelAll();
	    }
}
