package com.example.lrcprocess;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcProcess {
	public ArrayList<Queue> processlrc(InputStream inputStream) {
		Queue<Long> time = new LinkedBlockingQueue<Long>();
		Queue<String> lrc = new LinkedBlockingQueue<String>();
		ArrayList<Queue> list = new ArrayList<Queue>();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);
			BufferedReader br = new BufferedReader(inputStreamReader);
			String temp = null;
			Pattern p = Pattern.compile("\\[([^\\]]+)\\]");
			while ((temp = br.readLine()) != null) {
				Matcher matcher = p.matcher(temp);
				if (matcher.find()) {//读取的一行找到匹配内容
					Long timelong = ChangeToLongTime(matcher.group().substring(1,matcher.group().length()-1));
					time.add(timelong);//入队
					String msg = temp.substring(10);
				    lrc.add(msg+"\n");
				}
			}
			list.add(time);
			list.add(lrc);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	/**
	 * 时间转换Long
	 * 
	 * 注意以.为切割时 要记得转义
	 * @param string
	 * @return
	 */
	public Long ChangeToLongTime(String string){
	
		String[] s = string.split(":");
		String[] s2 = s[1].split("\\.");
		return Integer.parseInt(s[0])*60*1000 + Integer.parseInt(s2[0])*1000+Integer.parseInt(s2[1])*10L;
	}
}
