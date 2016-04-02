package com.xiaoxu.music.community.model.task;

import android.content.Context;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class CircleBlogsTask extends BaseRequestTask {
	/**
	 * 帖子列表
	 * @param context
	 * @param token
	 * @param user_id		     用户id
	 * @param blog_category   帖子所属分类
	 * @param pageNum		     页数
	 * @param callback
	 */
	public CircleBlogsTask(Context context, String blog_category,int pageNum,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("BlogThread[blog_category]", blog_category);
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		url = APIUrl.BASE_URL+APIUrl.CIRCLE_POSTS;
	}
	
	/*
	 * 我的帖子列表
	 */
	public CircleBlogsTask(Context context,int pageNum,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		url = APIUrl.BASE_URL+APIUrl.MINE_INVITATION;
	}
	
	/*
	 * 删除帖子
	 */
	public CircleBlogsTask(Context context,String praise_id,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("BlogThread[tid]", praise_id);
		url = APIUrl.BASE_URL+APIUrl.DELETE_INVITATION;
	}

}
