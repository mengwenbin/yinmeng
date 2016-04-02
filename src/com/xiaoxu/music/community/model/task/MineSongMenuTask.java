package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class MineSongMenuTask extends BaseRequestTask{
	
	/**
	 * 我的收藏歌曲
	 * @author
	 */
	public MineSongMenuTask(Context context,int pageNum,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("pageNum",String.valueOf(pageNum));
		url = APIUrl.BASE_URL + APIUrl.MINE_SONGMENU;
	}	
	/**
	 * 删除收藏歌单
	 */
	public MineSongMenuTask(Context context,int pageNum,String favorite_id,String favorite_category,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("Favorite[favorite_id]",favorite_id);
		params.addBodyParameter("Favorite[favorite_category]",favorite_category);
		url = APIUrl.BASE_URL + APIUrl.DELETE_SONGMENU;
	}	

}
