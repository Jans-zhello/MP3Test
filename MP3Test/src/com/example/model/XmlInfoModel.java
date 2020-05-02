package com.example.model;

import java.io.Serializable;

public class XmlInfoModel implements Serializable {
	/**
	 * 实现序列化以后即:使它从内存中变成字节码存放到硬盘当中去 或者实现以后可以通过网络发送出去
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String mp3Name;
	private String mp3size;
	private String lrcName;
	private String lrcSize;

	public XmlInfoModel() {
		super();
	}

	public XmlInfoModel(String id, String mp3Name, String mp3size,
			String lrcName, String lrcSize) {
		super();
		this.id = id;
		this.mp3Name = mp3Name;
		this.mp3size = mp3size;
		this.lrcName = lrcName;
		this.lrcSize = lrcSize;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMp3Name() {
		return mp3Name;
	}

	public void setMp3Name(String mp3Name) {
		this.mp3Name = mp3Name;
	}

	public String getMp3size() {
		return mp3size;
	}

	public void setMp3size(String mp3size) {
		this.mp3size = mp3size;
	}

	public String getLrcName() {
		return lrcName;
	}

	public void setLrcName(String lrcName) {
		this.lrcName = lrcName;
	}

	public String getLrcSize() {
		return lrcSize;
	}

	public void setLrcSize(String lrcSize) {
		this.lrcSize = lrcSize;
	}

	@Override
	public String toString() {
		return "XmlInfoModel [id=" + id + ", mp3Name=" + mp3Name + ", mp3size="
				+ mp3size + ", lrcName=" + lrcName + ", lrcSize=" + lrcSize
				+ "]";
	}

}
