package com.xiaoxu.music.community.model.task;

import android.content.Context;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class MusicRecommendTask extends BaseRequestTask{

	public MusicRecommendTask(Context context,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		url = APIUrl.BASE_URL + APIUrl.MUSIC_RECOMMEND_LIST;
	}
	
}
