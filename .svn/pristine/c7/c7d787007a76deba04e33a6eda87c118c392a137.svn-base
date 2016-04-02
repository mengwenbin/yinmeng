package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
/**
 * 首页广告
 * @author ice
 */
public class HomePageAdTask extends BaseRequestTask{
	
	public HomePageAdTask(Context context, RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		url = APIUrl.BASE_URL+APIUrl.HOMEPAGE_AD;
	}

}
