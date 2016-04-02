package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;

/**
 * 贴子二级分类
 * @author ice
 */
public class CircleSecondClassTask extends BaseRequestTask {

	public CircleSecondClassTask(Context context,String category, int pageNum,
			RequestCallBack<String> callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("category", category);
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		url = APIUrl.BASE_URL+APIUrl.CIRCLE_SECOND_CLASS;
	}

}
