package com.example.mp3test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.download.FileUtils;
import com.example.model.XmlInfoModel;

public class LocalMp3ListAcitivity extends ListActivity {
   private List<XmlInfoModel> list = null;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mp3list);
		show();
	}
	/**
	 * 展示下载好的列表到activity
	 */
	public void show() {
		FileUtils fileUtils = new FileUtils();
		list = fileUtils.getMp3Info("mp3");
		if (list != null) {
			List<HashMap<String, String>> ls2 = new ArrayList<HashMap<String, String>>();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				XmlInfoModel xif = (XmlInfoModel) iterator.next();
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("name", xif.getMp3Name());// 注意这里的key必须与布局文件的id一样
				hm.put("size", xif.getMp3size());
				ls2.add(hm);
			}
			SimpleAdapter simpleAdapter = new SimpleAdapter(this, ls2,
					R.layout.listview_content, new String[] { "name", "size" },
					new int[] { R.id.name, R.id.size });
			setListAdapter(simpleAdapter);
		}
	}
	@Override
		protected void onListItemClick(ListView l, View v, int position, long id) {
               if (list != null) {
				  XmlInfoModel xm = list.get(position);
				  Intent intent = new Intent();
				  intent.putExtra("xm",xm);
				  intent.setClass(LocalMp3ListAcitivity.this,PlayMp3Activity.class);
				  startActivity(intent);
			}   
		}
}
