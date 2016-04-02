package com.xiaoxu.music.community.util;

import java.util.Map;

import com.xiaoxu.music.community.service.MediaPlayerService;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreUtils {

	public static String WIFI = "wifi";//是否仅在WIFI
	public static String PLAY_MODE = "mode";//播放循环模式
	public static String SEARCH_HISTORY = "search_history";//搜索的历史记录
	
	public static void setSearchHistory(Context context, String history){
		SharedPreferences sp = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE);
		Editor edit = sp.edit(); 
		edit.putString("search_history", history);
		edit.commit();
	}
	
	public static String getSearchHistory(Context context){
		SharedPreferences sp = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE);
		return sp.getString("search_history", "");
	}
	
	// set 仅在WIFI
	public static void setOnlyWifi(Context context, boolean onlyWifi){
		SharedPreferences sp = context.getSharedPreferences(WIFI, Context.MODE_PRIVATE);
		Editor edit = sp.edit(); 
		edit.putBoolean("wifi", onlyWifi);
		edit.commit();
	}
	
	// is 仅在WIFI
	public static boolean isOnlyWifi(Context context){
		SharedPreferences sp = context.getSharedPreferences(WIFI, Context.MODE_PRIVATE);
		return sp.getBoolean("wifi", true);
	}
	
	// set 播放循环模式
	public static void setPlayMode(Context context, int mode){
		SharedPreferences sp = context.getSharedPreferences(PLAY_MODE, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("playmode", mode);
		edit.commit();
	}
	
	//获取当前播放的循环模式
	public static int currentPlayMode(Context context){
		SharedPreferences sp = context.getSharedPreferences(PLAY_MODE, Context.MODE_PRIVATE);
		return sp.getInt("playmode", MediaPlayerService.MODE_NORMAL);
	}
	
	
	/*
	 * 保存单条String类型数据
	 */
	public void saveData(Context context, String name, int mode, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor edit = sp.edit(); // 创建一个编辑器
		edit.putString(key, value); // 编辑数据
		edit.commit(); // 提交要存入的数据
	}
	
	/*
	 * 保存多条 任意类型 的数据
	 */
	public void saveAll(Context context, String name, int mode, Map<String, Object> value) {
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor edit = sp.edit();
		/** 【 1 】 */
		for (Map.Entry<String, Object> map : value.entrySet()) {
			String key = map.getKey();
			Object val = map.getValue();
			if (val instanceof String) {
				edit.putString(key, (String) val);
			} else if (val instanceof Integer) {
				edit.putInt(key, (Integer) val);
			} else if (val instanceof Float) {
				edit.putFloat(key, (Float) val);
			} else if (val instanceof Boolean) {
				edit.putBoolean(key, (Boolean) val);
			}
		}
		edit.commit();
	}

	/**
	 * 得到单条String类型数据
	 */
	public String getData(Context context, String name, int mode, String key) {
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getString(key, null); // 得到数据
	}
	
	/**
	 * 得到所有数据Map集合
	 */
	public Map<String, ?> getAllDate(Context context, String name, int mode) {
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getAll();
	}

}
