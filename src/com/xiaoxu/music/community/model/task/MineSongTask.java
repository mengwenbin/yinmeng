package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class MineSongTask extends BaseRequestTask{

	/*
	 * 我的歌单列表
	 */
	public MineSongTask(Context context,int pageNum,RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		url = APIUrl.BASE_URL + APIUrl.MINE_SONG;
	}
	
	/*
	 * 删除我的歌单
	 */
	public MineSongTask(Context context,String musiclist_id,String music_id,RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("MusicUser[music_id]", music_id);
		params.addBodyParameter("MusicUser[musiclist_id]", musiclist_id);
		url = APIUrl.BASE_URL + APIUrl.DELETE_MINE_SONG;
	}
}
