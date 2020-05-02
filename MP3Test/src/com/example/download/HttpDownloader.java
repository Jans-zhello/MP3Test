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
	 * 根据URL下载文件，前提是这个文件内容是文本，函数的返回值即是这个文本的内容
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
			 * Http文件下载固定格式
			 */
			   url = new URL(urlString);
			  if (url.getProtocol().toLowerCase().equals("http")){
				
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
			 /**
			  *   IO常见操作
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
	 * 该函数返回整形 -1代表文件出错; 0 代表文件下载成功;1代表文件已存在
	 */

	public int downFile(String urlString, String path, String filename) {
		InputStream inputStream = null;
		FileUtils fileUtils = new FileUtils();
		if (fileUtils.isFileExist(path + filename)) {
			return 1;
		} else {
			try {
				/**
				 * Http文件下载固定格式
				 */
				url = new URL(urlString);
				if (url.getProtocol().toLowerCase().equals("http")){
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					inputStream = urlConnection.getInputStream();
				/**
				 * 下载并写入SD卡	
				 */
					if (filename.endsWith("mp3")) {//写入Mp3文件
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
