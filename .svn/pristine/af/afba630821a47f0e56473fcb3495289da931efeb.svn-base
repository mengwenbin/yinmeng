package com.xiaoxu.music.community.model.task;

import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class UpdateUserInfoTask extends BaseRequestTask {
	
	/**
	 * 修改用户信息
	 * @param context
	 * @param token
	 * @param user_id
	 * @param map
	 * @param callback
	 */
	public UpdateUserInfoTask(Context context,Map<String, String> map, RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		for (Entry<String, String> entry : map.entrySet()) {
			params.addBodyParameter(entry.getKey(), entry.getValue());
		}
		url = APIUrl.BASE_URL + APIUrl.USER_UPDATE;
	}
	
	
	public UpdateUserInfoTask(Context context,String statu, RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("User[status]", statu);
		
		url = APIUrl.BASE_URL + APIUrl.USER_UPDATE;
	}

}
