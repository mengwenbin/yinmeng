package com.xiaoxu.music.community.model.impl;


import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Context;

public abstract class BaseTask {

	protected Context context;
	protected RequestCallBack<String> callback;
	protected String url;
	protected RequestParams params;
	protected HttpUtils httpUtils;
	protected HttpHandler<String> httpHandler;
	
	public BaseTask(Context context, RequestCallBack<String> callback) {
		this.context = context;
		this.callback = callback;
		this.params = new RequestParams();
	}
	
	public abstract void excute();
	
}
