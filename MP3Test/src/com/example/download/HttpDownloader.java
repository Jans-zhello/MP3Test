package com.example.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpDownloader {
	private URL url = null;

	/**
	 * 
	 * ����URL�����ļ���ǰ��������ļ��������ı��������ķ���ֵ��������ı�������
	 * 
	 * @param urlString
	 * @return
	 */

	public String download(String urlString) {
		StringBuffer sb = new StringBuffer();
		BufferedReader buffer = null;
		String line = null;
		try {  
			/**
			 * Http�ļ����ع̶���ʽ
			 */
			   url = new URL(urlString);
			  if (url.getProtocol().toLowerCase().equals("http")){
				
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
			 /**
			  *   IO��������
			  */
				buffer = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				while ((line = buffer.readLine()) != null) {
					sb.append(line);
				}
			}else {
				HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
				buffer = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				while ((line = buffer.readLine()) != null) {
					sb.append(line);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				buffer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * �ú����������� -1�����ļ�����; 0 �����ļ����سɹ�;1�����ļ��Ѵ���
	 */

	public int downFile(String urlString, String path, String filename) {
		InputStream inputStream = null;
		FileUtils fileUtils = new FileUtils();
		if (fileUtils.isFileExist(path + filename)) {
			return 1;
		} else {
			try {
				/**
				 * Http�ļ����ع̶���ʽ
				 */
				url = new URL(urlString);
				if (url.getProtocol().toLowerCase().equals("http")){
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					inputStream = urlConnection.getInputStream();
				/**
				 * ���ز�д��SD��	
				 */
					if (filename.endsWith("mp3")) {//д��Mp3�ļ�
						File resultFile = fileUtils.writeMp3toSD(path, filename,
								inputStream);
						if (resultFile == null) {
							return -1;
						}
					}else{
						File resultFile = fileUtils.writeLrctoSD(path, filename, inputStream);
						if (resultFile == null) {
							return -1;
						}
					}
				}else {
					HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
					inputStream = urlConnection.getInputStream();
					File resultFile = fileUtils.writeMp3toSD(path, filename,
							inputStream);
					if (resultFile == null) {
						return -1;
					}
				}
			
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

}
