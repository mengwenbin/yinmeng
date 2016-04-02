package com.xiaoxu.music.community.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.umeng.socialize.utils.Log;

import android.content.Context;
import android.text.format.DateUtils;
import android.text.format.Time;

public class TimeUtil {
	
	/**
	 * 根据日期计算属于第几周
	 * @param ms 时间戳（单位ms）
	 * @throws ParseException
	 */
	public static int getWeekOfYear(long ms){
	    try {
	        Calendar cal = Calendar.getInstance();
	        cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
	        //cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
	        cal.setMinimalDaysInFirstWeek(1); // 设置每周最少为1天
	        cal.setTime(new Date(ms));
	        return cal.get(Calendar.WEEK_OF_YEAR);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return 0;
	}
	
	/**
	 * 获取当前的时间
	 * @return
	 */
	public static long getCurrentTime(){
		long time = System.currentTimeMillis();
		return time;
	}
	
	/**
	 * 获取当前的 Year or Month
	 * @param type 
	 * 			  0:year  1:month
	 * @return
	 */
	public static int getSystemTime(int type){
		System.currentTimeMillis();
		String date = new SimpleDateFormat("yyyy-MM").format(new Date(System.currentTimeMillis()));
		String[] a = date.split("-");
	    int currentTime = 1;
	    if(type == 0){
        	currentTime = Integer.parseInt(a[0]);
        }else if(type == 1) {
        	currentTime = Integer.parseInt(a[1]);
        }
        return currentTime;
	}
	
	/**
	 * 获取系统时间
	 * @return
	 */
	public static String getSystemTimer(Context context){
    	String currentTime = DateUtils.formatDateTime(context,System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_ABBREV_ALL);
	    return currentTime;
	}
	
	/**
	 * 判断本地时间是否小于服务器时间(时间戳)
	 */
	public static boolean datePk(long localDate, long serverDate) {
		return localDate < serverDate;
	}
	
	/**
	 * 时间转换（播放时间转换）
	 * @param int毫秒
	 * @return
	 */
	public static String formatTime(long ms) {
		long minutes = (ms % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (ms % (1000 * 60)) / 1000;
		return minutes + ":" + seconds;
	}
	
	/**
	 * 时间戳转换成字符串
	 * @param time 秒
	 * @return
	 */
	public static String timeToString(String s) {
	    String re_StrTime = null;
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    long lcc_time = Long.valueOf(s);
	    re_StrTime = sdf.format(new Date(lcc_time * 1000L));// lcc_time * 1000L 秒转换成毫秒
	    return re_StrTime;
	}
	
	/**
	 * 时间戳转换成字符串
	 * @param time 毫秒
	 * @return
	 */
	public static String timeToString2(String ms) {
	    String re_StrTime = null;
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    long lcc_time = Long.valueOf(ms);
	    re_StrTime = sdf.format(new Date(lcc_time));
	    return re_StrTime;
	}
	
	/** 
	 * 将时间戳转为代表"距现在多久之前"的字符串 
	 * @param timeStr   时间戳 
	 * @return 
	 */  
	public static String getStandardDate(String timeStr) {  
	    StringBuffer sb = new StringBuffer();
	    long t = Long.parseLong(timeStr);
	    long time = System.currentTimeMillis() - (t*1000);
	    long mill = (long) Math.ceil(time /1000);//秒前  Math.ceil()将小数部分一律向整数部分进位。 
	    
	    long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前  
	  
	    long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时  
	    
	    long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前  
	  
	    if (day - 1 > 0) {  
	        sb.append(day + "天");  
	    } else if (hour - 1 > 0) {  
	        if (hour >= 24) {  
	            sb.append("1天");  
	        } else {  
	            sb.append(hour + "小时");  
	        }  
	    } else if (minute - 1 > 0) {  
	        if (minute == 60) {  
	            sb.append("1小时");  
	        } else {  
	            sb.append(minute + "分钟");  
	        }  
	    } else if (mill - 1 > 0) {  
	        if (mill == 60) {
	            sb.append("1分钟");  
	        } else {  
	            sb.append(mill + "秒");  
	        }  
	    } else {  
	        sb.append("刚刚");  
	    }
	    if (!sb.toString().equals("刚刚")) {
	        sb.append("前");  
	    } 
	    return sb.toString();  
	}

}
