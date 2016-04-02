package com.xiaoxu.music.community.util;

import java.text.DecimalFormat;

import android.text.TextUtils;

public class StringUtil {

	/**
	 * bt-->MB 保留两位小数
	 * @param KB
	 * @return
	 */
	public static String formatSize(String kb) {
		int k = Integer.parseInt(kb);
		double m = k / 1024.00/1024.00;
		DecimalFormat df = new DecimalFormat("######0.00");
		String str = df.format(m);
		return str;
	}
	
	/**
	 * 缩略图 路径替换替换String 
	 * @param path
	 * @param replacement
	 * @return
	 */
	public static String replaceImagePath(String path, int size){
		if(!TextUtils.isEmpty(path)){
			if(path.contains(".")){
				String lastString = path.substring(path.lastIndexOf('.'));
				String newString = path.replace(lastString, "_"+size+lastString);
				return newString;
			}
		}
		return "";
	}
	
	public static String cutLastString(String path){
		String lastString = "wma";
		if(!TextUtils.isEmpty(path)){
			if(path.contains(".")){
				lastString = path.substring(path.lastIndexOf('.'));
			}
		}
		return lastString;
	}
	
	
	/**
	 * 判断是否是绝对路径
	 * @param url
	 * @return
	 */
	public static boolean isAbsolutePath(String url){
		return url.contains("://");
	}

}
