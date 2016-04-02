package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;

/**
 * 修改密码
 * @author ice
 *
 */
public class UpdatePwdTask extends BaseRequestTask {

	public UpdatePwdTask(Context context, String username,String password,String newpassword,
			RequestCallBack<String> callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("User[username]", username);
		params.addBodyParameter("User[password]", password);
		params.addBodyParameter("User[password_new]", newpassword);
		url = APIUrl.BASE_URL+APIUrl.USER_UPDATE_PWD;
	}

}
