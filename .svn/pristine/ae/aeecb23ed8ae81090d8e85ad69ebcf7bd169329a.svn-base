package com.xiaoxu.music.community.model.impl;

import java.io.File;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;

public abstract class BaseDownLoadTask {

	protected Context context;
	protected RequestCallBack<File> callback;
	protected String url;// 请求路径
	protected String savaUrl;// 保存路径
	protected RequestParams params;
	protected HttpUtils httpUtils;
	protected HttpHandler httpHandler;

	public BaseDownLoadTask(Context context, RequestCallBack<File> callback) {
		this.context = context;
		this.callback = callback;
		this.params = new RequestParams();
	}
	
	public abstract void excute();
	
}
