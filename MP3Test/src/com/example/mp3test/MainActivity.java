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
    	 * 页面切换布局
    	 * addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)负责在切换到activity是更新activity
    	 * 或者是在目标activty中将方法放入OnResume()也能更新Activity
    	 */
    	final TabHost tabHost = getTabHost();
    	TabSpec remotespec = tabHost.newTabSpec("远程").setIndicator("远程",this.getResources().getDrawable(android.R.drawable.stat_sys_download)).setContent(new Intent(this,RemoteMp3ListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    	TabSpec localspec = tabHost.newTabSpec("本地").setIndicator("本地",this.getResources().getDrawable(android.R.drawable.stat_sys_download)).setContent(new Intent(this,LocalMp3ListAcitivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(remotespec);
        tabHost.addTab(localspec);

    }
}
