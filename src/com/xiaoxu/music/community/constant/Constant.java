package com.xiaoxu.music.community.constant;

import java.io.File;

import android.os.Environment;

public class Constant {
	
	//bitmap的缓存路径 和 bitmap的缓存大小
	public static String DISK_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator+ "yinmeng" + File.separator + "cacheImage" + File.separator;
	public static int MAX_MEMORY_SIZE = (int) (Runtime.getRuntime().maxMemory() / 12);
	
	//相机拍照保存路径
	public static String PHOTO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() 
			+ File.separator+ "yinmeng" + File.separator+ "caremaImage";
	//裁剪图片保存路径
	public static String CROPIMG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() 
			+ File.separator + "yinmeng" + File.separator + "cropImage";
	
	//请求成功Code
	public static final int SUCCESS_REQUEST = 200;
	
	// 短信验证 ShareSDK
	public static final String SMSSDK_APPKEY = "8572ff86377c";
	public static final String SMSSDK_APPSECRET = "dd65ad03a234be8d6e679003eaaae85a";
	
	// BroadCastReceiver
	public static final String ACTION_MINE_UPDATE_USERINFO = "update_user_info";//修改用户的信息
	public static final String UPDATE_MUSICINFO = "update_music_info";//修改音乐的信息
	public static final String ACTION_MUSIC_COLLECT = "action_collect";
	
	public static final String AppUrl = "http://www.inmoe.cn/mobile";
	
	/*
	 * 缓存首页数据的名字信息
	 */
	public static final String HomePageAdName = "advertisement";
	public static final String MusicUserRecommend = "musicuserrecommend";
	public static final String MusicRecommend = "musicrecommend";
	
	
	/*
	 * 榜单 初始 年，月 ，周
	 */
	public static final int INIT_START_YEAR = 2015;
	public static final int INIT_MONTH = 7;
	public static final int INIT_WEEK = 36;
	
	public static final String ACCOUNT_REMOVED = "account_removed";
	public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
	public static final String NEW_FRIENDS_USERNAME = "new_friends_username";
	public static final String REFRESH_MESSAGE = "refresh_message";
	
	/*
	 * listview.stopRefresh()取消的时间
	 */
	public static final int DELAYMILLIS = 500;
	
	/*
	 * Search
	 */
	public static final int SEARCH_TYPE_USER = 0x1;
	public static final int SEARCH_TYPE_MUSIC = 0x2;
	
}
