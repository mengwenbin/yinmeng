package com.xiaoxu.music.community.model.impl;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public abstract class BaseUploadTask {

	protected Context context;
	protected RequestCallBack<String> callback;
	protected String url;
	protected String file_path;
	protected RequestParams params;
	protected HttpUtils httpUtils;
	protected HttpHandler<String> httpHandler;

	public BaseUploadTask(Context context, RequestCallBack<String> callback) {
		this.context = context;
		this.callback = callback;
		this.params = new RequestParams();
	}

	public abstract void excute();

}
