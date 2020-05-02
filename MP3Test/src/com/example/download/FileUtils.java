package com.example.download;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.example.model.XmlInfoModel;

public class FileUtils {
	private String SDPATH;

	public String getSDPATH() {
		return SDPATH;
	}

	public FileUtils() {
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 在SD卡上创建文件
	 */
	public File createFileOnSD(String filename) throws Exception {
		File file = new File(SDPATH + filename);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 */
	public File createDirOnSD(String dirname) {
		if (dirname.contains("/")) {
			dirname = dirname.substring(0, dirname.lastIndexOf("/"));
		}
		File dir = new File(SDPATH + dirname);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上文件是否存在
	 */
	public boolean isFileExist(String filename) {
		return new File(SDPATH + filename).exists();
	}

	/**
	 * 将Mp3文件写到SD卡中去(字节流)
	 */
	public File writeMp3toSD(String path, String filename, InputStream input) {
		File file = null;
		FileOutputStream out = null;
		try {
			createDirOnSD(path);
			file = createFileOnSD(path + filename);
			out = new FileOutputStream(file);
			byte[] b = new byte[512];
			int len = 0;
			while ((len = input.read(b)) != -1) {
				out.write(b, 0, len);
				out.flush();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 将歌词文件写入到SD卡中去(字符流)
	 */
	public File writeLrctoSD(String path, String filename, InputStream input) {
		File file = null;
		BufferedReader reader = null;
		BufferedWriter bWriter = null;
		OutputStreamWriter outputStreamWriter = null;
		try {
			// 创建目录和文件名字
			createDirOnSD(path);
			file = createFileOnSD(path + filename);
			reader = new BufferedReader(new InputStreamReader(input));
			outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
			bWriter = new BufferedWriter(outputStreamWriter);
			String temp = null;
			while ((temp=reader.readLine())!= null) {
				bWriter.write(temp+"\r");
				bWriter.flush();
				System.out.println(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				bWriter.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 获取SD卡中文件内容和大小
	 */
	public List<XmlInfoModel> getMp3Info(String path) {
		List<XmlInfoModel> list = new ArrayList<XmlInfoModel>();
		// 读取目录文件
		File f = new File(SDPATH + "mp3");
		File f2 = new File(SDPATH + "lrc");
		File[] files = f.listFiles();
		File[] files2 = f2.listFiles();
		if (files != null && files2 != null) {
			for (int i = 0; (i < files.length) && (i < files2.length); i++) {
				XmlInfoModel xmlInfoModel = new XmlInfoModel();
				xmlInfoModel.setMp3Name(files[i].getName());
				xmlInfoModel.setMp3size(files[i].length() + "");
				xmlInfoModel.setLrcName(files2[i].getName());
				xmlInfoModel.setLrcSize(files2[i].length() + "");
				list.add(xmlInfoModel);
			}
		}
		return list;
	}

}
