package com.xiaoxu.music.community;

import android.os.Handler;
import android.os.Message;

public abstract class HandlerHelp {
	
	RequestThread t = new RequestThread();
	
	public void execute() {
        t.start();
	}
	
	class RequestThread extends Thread{
		
		@Override
		public void run() {
			try {
				doTask();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				tHandler.sendEmptyMessage(1);
			}
		}
	}
	
	Handler tHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				handler();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				tHandler.removeMessages(msg.what);
				if(t != null){
					tHandler.removeCallbacks(t);
				}
			}
		}
	};
	
	/**
	 * handler
	 * @param 更新UI
	 */
	public abstract void handler();
	
	/**
	 * 数据处理
	 * @param msg
	 */
	public abstract void doTask();
}
