package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

/**
 * 专题推荐-更多推荐
 * @author ice
 */
public class MoreRecommendSongMenuTask extends BaseRequestTask{

	public MoreRecommendSongMenuTask(Context context, int pageNum, RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		url = APIUrl.BASE_URL + APIUrl.SONG_MENU_MORE;
	}
	
}
