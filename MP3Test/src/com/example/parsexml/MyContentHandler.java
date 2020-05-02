package com.example.parsexml;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.example.model.XmlInfoModel;

/**
 * ����xml�ļ�׼������(�̳�ContentHandler��) (����̳�DefaultHandler����һ��������ģʽ������ѡ�����д���з���)
 * 
 * @author Administrator
 * 
 */
public class MyContentHandler extends DefaultHandler {

	private List<XmlInfoModel> list = null;
	private XmlInfoModel xim = null;
	private String tagName = null;

	public MyContentHandler(List<XmlInfoModel> list) {
		super();
		this.list = list;
	}
    
	public List<XmlInfoModel> getList() {
		return list;
	}

	public void setList(List<XmlInfoModel> list) {
		this.list = list;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String temp = new String(ch, start, length);
		if (tagName.equals("id")) {
			xim.setId(temp);
		} else if (tagName.equals("mp3.name")) {
			xim.setMp3Name(temp);
		} else if (tagName.equals("mp3.size")) {
			xim.setMp3size(temp);
		} else if (tagName.equals("lrc.name")) {
			xim.setLrcName(temp);
		} else if (tagName.equals("lrc.size")) {
			xim.setLrcSize(temp);
		}
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("resource")) {
			list.add(xim);
		}
		tagName = "";
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		this.tagName = localName;
		if (tagName.equals("resource")) {
			xim = new XmlInfoModel();
		}
	}

}
