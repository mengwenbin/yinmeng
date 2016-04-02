package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class FansUserListTask extends BaseRequestTask {

	public FansUserListTask(Context context,String user_id, int pageNum,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", user_id);
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		url = APIUrl.BASE_URL + APIUrl.FANS_PERSON_LIST;
	}

}
