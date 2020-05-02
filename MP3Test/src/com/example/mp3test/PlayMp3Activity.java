package com.example.mp3test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lrcprocess.LrcProcess;
import com.example.model.XmlInfoModel;
import com.example.services.PlayMp3Service;

public class PlayMp3Activity extends Activity {
	ImageButton begin = null;
	ImageButton pause = null;
	ImageButton over = null;
	TextView textView = null;
	XmlInfoModel xm = null;

	public static final int BEGIN_MSG = 1;
	public static final int PAUSE_MSG = 2;
	public static final int OVER_MSG = 3;

	private BroadcastReceiver receiver = null;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playmp3);
		Intent in = getIntent();
		xm = (XmlInfoModel) in.getSerializableExtra("xm");
		begin = (ImageButton) findViewById(R.id.begin);
		pause = (ImageButton) findViewById(R.id.pause);
		over = (ImageButton) findViewById(R.id.over);
		begin.setOnClickListener(new BeginOnclickListener());
		pause.setOnClickListener(new PauseOnclickListerner());
		over.setOnClickListener(new OverOnclickListerner());
		textView = (TextView) findViewById(R.id.lrc);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	receiver = new LrcMessageBroadCastReceiver();
    	IntentFilter intentFilter = new IntentFilter();
    	intentFilter.addAction("lrc.action");
    	registerReceiver(receiver,intentFilter);
    }
    /**
     * 接受service发送过来的歌词广播，并更新UI
     * @author Administrator
     *
     */
    class LrcMessageBroadCastReceiver extends BroadcastReceiver {
    	
    	public void onReceive(Context arg0, Intent arg1) {
    	       	// TODO Auto-generated method stub
    		String lrc = arg1.getStringExtra("lrc");
    		textView.setText(lrc);
    	}
    }
     
	class BeginOnclickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.putExtra("xm", xm);
			intent.putExtra("MSG", BEGIN_MSG);
			intent.setClass(PlayMp3Activity.this, PlayMp3Service.class);
			startService(intent);
		}
	}

	class PauseOnclickListerner implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.putExtra("xm", xm);
			intent.putExtra("MSG", PAUSE_MSG);
			intent.setClass(PlayMp3Activity.this, PlayMp3Service.class);
			startService(intent);

		}
	}

	class OverOnclickListerner implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.putExtra("xm", xm);
			intent.putExtra("MSG", OVER_MSG);
			intent.setClass(PlayMp3Activity.this, PlayMp3Service.class);
			startService(intent);
		}
	}
}
