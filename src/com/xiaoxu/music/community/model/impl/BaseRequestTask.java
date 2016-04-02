package com.xiaoxu.music.community.model.impl;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.utils.Log;

public class BaseRequestTask extends BaseTask {

	public BaseRequestTask(Context context, RequestCallBack<String> callback) {
		super(context, callback);
	}
	
	@Override
	public void excute() {
		// TODO Auto-generated method stub
		httpUtils = new HttpUtils();
		httpHandler = httpUtils.send(HttpMethod.POST, url, params, callback);
	}

	public void cancelTask() {
		httpUtils = null;
		httpHandler.cancel(httpHandler.isCancelled());
	}

}
