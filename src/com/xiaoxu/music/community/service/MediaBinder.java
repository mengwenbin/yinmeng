package com.xiaoxu.music.community.service;

import com.xiaoxu.music.community.entity.SongInfoEntity;

import android.os.Binder;

public class MediaBinder extends Binder {
	
	private OnClientBinderListener onClientBinderListener;
	private OnServiceBinderListener onServiceBinderListener;
	private OnPlayStatusListener onPlayStatusListener;
	
	protected void setOnServiceBinderListener(OnServiceBinderListener onServiceBinderListener) {
		this.onServiceBinderListener = onServiceBinderListener;
	}
	
	public void setOnClientBinderListener(OnClientBinderListener onClientBinderListener) {
		this.onClientBinderListener = onClientBinderListener;
	}
	
	public void setOnPlayStatusListener(OnPlayStatusListener onPlayStatusListener) {
		this.onPlayStatusListener = onPlayStatusListener;
	}
	
	/* 
	 *************client 回调 ************/
	
	protected void playStart(SongInfoEntity info,int currentIndex) {
		if (onClientBinderListener != null) {
			onClientBinderListener.onPlayStart(info,currentIndex);
		}
		if(onPlayStatusListener != null){
			onPlayStatusListener.onPlay();
		}
	}
	
	protected void playResume() {
		if (onClientBinderListener != null) {
			onClientBinderListener.onPlayResume();
		}
		if(onPlayStatusListener != null){
			onPlayStatusListener.onPlay();
		}
	}
	
	protected void playProgress(long currentProgress,long durationTime) {
		if (onClientBinderListener != null) {
			onClientBinderListener.onPlayProgress(currentProgress,durationTime);
		}
	}
	
	protected void playBufferProgress(int buffer) {
		if (onClientBinderListener != null) {
			onClientBinderListener.onPlayBufferProgress(buffer);
		}
	}
	
	protected void playPause() {
		if (onClientBinderListener != null) {
			onClientBinderListener.onPlayPause();
		}
		if(onPlayStatusListener != null){
			onPlayStatusListener.onPause();
		}
	}

	protected void playComplete() {
		if (onClientBinderListener != null) {
			onClientBinderListener.onPlayComplete();
		}
		if(onPlayStatusListener != null){
			onPlayStatusListener.onPause();
		}
	}

	protected void playError() {
		if (onClientBinderListener != null) {
			onClientBinderListener.onPlayError();
		}
		if(onPlayStatusListener != null){
			onPlayStatusListener.onPause();
		}
	}
	
	protected void playMode(int mode) {
		if (onClientBinderListener != null) {
			onClientBinderListener.onPlayMode(mode);
		}
	}
	
	/* 
	 **************Service 回调 ****************/
	
	/**
	 * 离开SeekBar时响应
	 * @param progress
	 *            当前进度
	 */
	public void seekBarStopTrackingTouch(int progress) {
		if (onServiceBinderListener != null) {
			onServiceBinderListener.seekBarStopTrackingTouch(progress);
		}
	}
	
	/**
	 * 设置歌词视图
	 * 
	 * @param lrcView
	 *            歌词视图
	 * @param isKLOK
	 *            是否属于卡拉OK模式
	 */
//	public void setLyricView(LyricView lrcView, boolean isKLOK) {
//		if (onServiceBinderListener != null) {
//			onServiceBinderListener.lrc(lrcView, isKLOK);
//		}
//	}

	/**
	 * 设置控制命令
	 * @param command
	 *            控制命令
	 */
	public void setControlCommand(int command) {
		if (onServiceBinderListener != null) {
			onServiceBinderListener.control(command);
		}
	}
	
	public void setPlayQueue(int command) {
		if (onServiceBinderListener != null) {
			onServiceBinderListener.control(command);
		}
	}
	
	/* 接口
	 ********************************************************************************************/
	
	public interface OnClientBinderListener {
		/**
		 * 开始播放
		 * @param info 歌曲详细信息
		 */
		public void onPlayStart(SongInfoEntity info, int currentIndex);
		
		/**
		 * 继续播放
		 * @param info
		 */
		public void onPlayResume();
		
		/**
		 * 正在播放
		 * @param current
		 *            当前播放时间(String类型)
		 */
		public void onPlayProgress(long currentTime,long durationTime);
		/**
		 * 缓存进度
		 * @param buffer
		 */
		public void onPlayBufferProgress(int buffer);
		/**
		 * 暂停播放
		 */
		public void onPlayPause();
		/**
		 * 播放完成
		 */
		public void onPlayComplete();
		/** 
		 * 播放出错
		 */
		public void onPlayError();
		/**
		 * 播放模式
		 */
		public void onPlayMode(int mode);

	}
	
	/**
	 * 回调接口，只允许service使用
	 */
	public interface OnServiceBinderListener {
		/**
		 * 离开SeekBar时响应
		 * @param progress当前进度
		 */
		void seekBarStopTrackingTouch(int progress);
		/**
		 * 播放控制(播放、暂停、上一首、下一首、播放模式切换)
		 * @param command 控制命令
		 */
		void control(int command);
		
		/**
		 * 设置歌词
		 * 
		 * @param lyricView
		 *            歌词视图
		 * @param isKLOK
		 *            是否属于卡拉OK模式
		 */
//		void lrc(LyricView lyricView, boolean isKLOK);
	}
	
	/**
	 * 播放状态的监听（动画的改变）
	 */
	public interface OnPlayStatusListener{
		public void onPause();
		public void onPlay();
	}

}
