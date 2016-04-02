package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class MusicByCategoryTask extends BaseRequestTask {

	/**
	 * 
	 * @param context
	 * @param music_category 
	 * 					51=原创    52=翻唱
	 * @param pageNum
	 * @param callback
	 */
	public MusicByCategoryTask(Context context, String music_category, int pageNum,
			RequestCallBack<String> callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("music_category", music_category);
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		url = APIUrl.BASE_URL + APIUrl.MUSICLIB_MUSIC_BYCATEGORY;
	}

}
