package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;

public class ResetPwdTask extends BaseRequestTask {
	
	/**
	 * 重置密码
	 * @param context
	 * @param phone
	 * @param pwd
	 * @param callback
	 */

	public ResetPwdTask(Context context, String phone, String pwd,
			RequestCallBack<String> callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("User[user_mobile]", phone);
		params.addBodyParameter("User[password]", pwd);
		url = APIUrl.BASE_URL + APIUrl.USER_RESETPWD;
	}

}
