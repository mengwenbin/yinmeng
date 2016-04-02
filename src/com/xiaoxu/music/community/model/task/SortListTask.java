package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

/**
 * 榜单
 * @author ice
 * 
 */
public class SortListTask extends BaseRequestTask {
	
	/**
	 * 热榜
	 * @param context
	 * @param music_category
	 * @param callback
	 */
	public SortListTask(Context context, String music_category,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("music_category", music_category);
		url = APIUrl.BASE_URL + APIUrl.SORT_LIST_HOT;
	}
	
	/**
	 * 周榜
	 * @param context
	 * @param music_category
	 * @param nYear
	 * @param nWeek
	 * @param callback
	 */
	public SortListTask(Context context, String music_category, String nYear, String nWeek,
			RequestCallBack<String> callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("music_category", music_category);
		params.addBodyParameter("nYear", nYear);
		params.addBodyParameter("nWeek", nWeek);
		url = APIUrl.BASE_URL + APIUrl.SORT_LIST_WEEK;
	}
	
	/**
	 * 月榜
	 * @param context
	 * @param music_category
	 * @param nYear
	 * @param nMonth
	 * @param callback
	 */
	public SortListTask(Context context, String music_category, int nYear, int nMonth,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("music_category", music_category);
		params.addBodyParameter("nYear", String.valueOf(nYear));
		params.addBodyParameter("nMonth", String.valueOf(nMonth));
		url = APIUrl.BASE_URL + APIUrl.SORT_LIST_MONTH;
	}
	
	/**
	 * 总榜
	 * @param context
	 * @param music_category
	 * @param callback
	 */
	public SortListTask(Context context, int music_category,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("music_category", String.valueOf(music_category));
		url = APIUrl.BASE_URL + APIUrl.SORT_LIST_ALL;
	}
	
}
