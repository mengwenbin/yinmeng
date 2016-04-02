package com.xiaoxu.music.community.model.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.parser.ParseUtil;

/**
 * 构造方法 传入 Class ，解析result数据
 * @author ice
 */
public abstract class BaseRequestCallBack extends RequestCallBack<String> {
	
	protected Class classOfT = null;
	protected Object obj = null;
	
	public BaseRequestCallBack() {
		super();
	}
	
	public BaseRequestCallBack(Class classOfT) {
		super();
		this.classOfT = classOfT;
	}
	
	public BaseRequestCallBack setClassOfT(Class classOfT) {
		this.classOfT = classOfT;
		return this;
	}

	@Override
	public void onSuccess(ResponseInfo<String> arg0) {
		// TODO Auto-generated method stub
		String result = arg0.result;
		int code = ParseUtil.parseCode(result);
		String alert = ParseUtil.parseAlert(result);
		if(classOfT!=null && code==Constant.SUCCESS_REQUEST){
			obj = ParseUtil.parseResultObj(result, classOfT);
		}
		onResult(result, code, alert, obj);
	}
	
	public abstract void onResult(String result, int code, String alert, Object obj);
	
}
