package com.example.mp3test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.renderscript.Int3;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.download.HttpDownloader;
import com.example.model.XmlInfoModel;
import com.example.parsexml.MyContentHandler;
import com.example.services.DownLoadService;

public class RemoteMp3ListActivity extends ListActivity {
	private static final int UPDATE = 1;
	private static final int UABOUT = 2;
	private List<XmlInfoModel> ls = null;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mp3list);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		showToListView();
	}

	/**
	 * ���û����menu�˵����󣬺���ø÷��������ǿ�����������������Լ��İ�ť�ؼ�
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, 1, 1, R.string.update);
		menu.add(0, 2, 2, R.string.about);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == UPDATE) {// �û�����˸����б�ť
			showToListView();
		} else if (item.getItemId() == UABOUT) {// �û�����˹��ڰ�ť

		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ���ط������˵�xml�ļ�
	 */
	private String downloadXml(String urlString) {
		HttpDownloader httpDownloader = new HttpDownloader();
		String result = httpDownloader.download(urlString);
		return result;
	}

	/**
	 * ������������xml�ļ�
	 */
	private List<XmlInfoModel> parse(String xmlstr) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<XmlInfoModel> list = new ArrayList<XmlInfoModel>();
		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();
			MyContentHandler mHandler = new MyContentHandler(list);
			xmlReader.setContentHandler(mHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlstr)));
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				XmlInfoModel xmlInfoModel = (XmlInfoModel) iterator.next();
				//System.out.println(xmlInfoModel);
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * ����XML�ļ�����XML�ļ��������µ��б��������ʾ��ListView��ȥ
	 */
	private void showToListView() {
		String xml = downloadXml("http://localhost:8080/mp3/resources.xml");
		ls = parse(xml);
		List<HashMap<String, String>> ls2 = new ArrayList<HashMap<String, String>>();
		for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
			XmlInfoModel xif = (XmlInfoModel) iterator.next();
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("name", xif.getMp3Name());// ע�������key�����벼���ļ���idһ��
			hm.put("size", xif.getMp3size());
			ls2.add(hm);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, ls2,
				R.layout.listview_content, new String[] { "name", "size" },
				new int[] { R.id.name, R.id.size });
		setListAdapter(simpleAdapter);
	}

	/**
	 * �û����ListView�е�Item�¼����� ����service�����ļ�(�ص�)
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (ls != null) {
			XmlInfoModel xModel = ls.get(position);
			Intent intent = new Intent();
			intent.putExtra("xModel", xModel);
			intent.setClass(this, DownLoadService.class);
			startService(intent);
			super.onListItemClick(l,v,position,id);
		}
	}
}
