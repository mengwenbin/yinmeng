package com.xiaoxu.music.community.service;

public interface IMediaConstant {
	
	// 客户端要发送的一些Action(服务端接收)
	public static final String PLAY_CONTROL = "action.play_control"; // 控制播放器
	public static final String UPDATE_QUEUE = "action.play_queue"; // 更新播放队列
	public static final String SAVE_HISTORY = "action.save_history"; // 保存播放历史
	
	public static final String KEY_CONTROL = "control"; //控制命令
	public static final String KEY_CURRENT_INDEX = "currentIndex";//当前播放index
	
	public static final int CONTROL_EXIT = 0x0;// 上一首
	
	public static final int CONTROL_START = 0x1;// 开始播放音乐
	public static final int CONTROL_PAUSE = 0x2;// 暂停
	public static final int CONTROL_PLAY = 0x3;// 继续播放
	public static final int CONTROL_STOP = 0x4; // 停止
	public static final int CONTROL_PREV = 0x5;// 上一首
	public static final int CONTROL_NEXT = 0x6;// 下一首
	public static final int CONTROL_MODE = 0x7;// 下一首
	
	public static final int MODE_NORMAL = 1;	 //顺序播放，放到最后一首停止
	public static final int MODE_REPEAT_ALL = 2; //全部循环
	public static final int MODE_REPEAT_ONE = 3; //单曲循环
	public static final int MODE_RANDOM = 4;	 //随即播放
}
