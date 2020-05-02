package com.example.mp3test;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	/**
    	 * ҳ���л�����
    	 * addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)�������л���activity�Ǹ���activity
    	 * ��������Ŀ��activty�н���������OnResume()Ҳ�ܸ���Activity
    	 */
    	final TabHost tabHost = getTabHost();
    	TabSpec remotespec = tabHost.newTabSpec("Զ��").setIndicator("Զ��",this.getResources().getDrawable(android.R.drawable.stat_sys_download)).setContent(new Intent(this,RemoteMp3ListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    	TabSpec localspec = tabHost.newTabSpec("����").setIndicator("����",this.getResources().getDrawable(android.R.drawable.stat_sys_download)).setContent(new Intent(this,LocalMp3ListAcitivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(remotespec);
        tabHost.addTab(localspec);

    }
}
