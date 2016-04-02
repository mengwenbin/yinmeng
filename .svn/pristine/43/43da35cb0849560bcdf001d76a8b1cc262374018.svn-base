package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

/*
 * 取消关注
 */
public class CancleAttentionTask extends BaseRequestTask {

	public CancleAttentionTask(Context context, String attention_user_id, RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("attention_user_id", attention_user_id);
		url = APIUrl.BASE_URL + APIUrl.ATTENTION_CANCEL;
	}

}
