package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;

/**
 * 贴子一级分类
 * @author ice
 *
 */
public class CircleFirstClassTask extends BaseRequestTask {

	public CircleFirstClassTask(Context context,
			RequestCallBack<String> callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		url = APIUrl.BASE_URL+APIUrl.CIRCLE_FIRST_CLASS;
	}

}
