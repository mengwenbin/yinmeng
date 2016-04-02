package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

/**
 * 更多推荐
 * @author ice
 *
 */
public class MoreRecommendTask extends BaseRequestTask {
	
	/**
	 * 更多推荐
	 * @param context
	 * @param music_category
	 * @param pageNum
	 * @param s_url 
	 * 				0：最新     1：热门	2：人气	3：传播
	 * @param callback
	 */
	public MoreRecommendTask(Context context, int music_category, int pageNum, int s_url, 
			RequestCallBack<String> callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("music_category", String.valueOf(music_category));
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		switch (s_url) {
			case 0: url = APIUrl.BASE_URL + APIUrl.MORE_RECOMMEND_NEW; break;
			case 1: url = APIUrl.BASE_URL + APIUrl.MORE_RECOMMEND_HOT; break;
			case 2: url = APIUrl.BASE_URL + APIUrl.MORE_RECOMMEND_POPULAR; break;
			case 3: url = APIUrl.BASE_URL + APIUrl.MORE_RECOMMEND_PUBLICITY; break;
			default: url = APIUrl.BASE_URL + APIUrl.MORE_RECOMMEND_NEW; break;
		}
	}

}
