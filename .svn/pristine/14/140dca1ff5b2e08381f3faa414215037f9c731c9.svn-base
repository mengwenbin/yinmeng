package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

/**
 * 收藏
 * @author ice
 */
public class CollectSongMenuTask extends BaseRequestTask {
	
	/*
	 * 收藏歌单
	 */
	public CollectSongMenuTask(Context context, String music_id,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("Favorite[item_id]", music_id);
		url = APIUrl.BASE_URL + APIUrl.COLLECT_SONGMENU;
	}
	
	/*
	 * 收藏单曲
	 */
	public CollectSongMenuTask(Context context, String music_id,String user_id, RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", user_id);
		params.addBodyParameter("Favorite[item_id]", music_id);
		url = APIUrl.BASE_URL + APIUrl.COLLECT_MUSIC;
	}

}
