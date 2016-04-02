package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;

public class LoginTask extends BaseRequestTask {
	/**
	 * 登录
	 * @param context
	 * @param name
	 * @param pwd
	 * @param callback
	 */
	public LoginTask(Context context, String name, String pwd,
			RequestCallBack<String> callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
		params.addBodyParameter("LoginForm[username]", name);
		params.addBodyParameter("LoginForm[password]", pwd);
		url = APIUrl.BASE_URL + APIUrl.USER_LOGIN;
	}
}
