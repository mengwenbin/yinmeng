package com.xiaoxu.music.community.model.task;

import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.UserDataHandler;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class RegistTask extends BaseRequestTask {
	
	/**
	 * 注册
	 * @param context
	 * @param name
	 * @param password
	 * @param authCode
	 * @param callback
	 */
	public RegistTask(Context context, String name, String password,
			String authCode, RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("User[user_mobile]", name);
		params.addBodyParameter("User[password]", password);
		url = APIUrl.BASE_URL + APIUrl.USER_REGIST;
	}

}
