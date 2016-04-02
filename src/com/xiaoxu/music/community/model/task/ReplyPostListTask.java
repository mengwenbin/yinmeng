package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class ReplyPostListTask extends BaseRequestTask{
	
	/**
	 * 回帖列表
	 * @param context
	 * @param parent_tid	回复的帖子ID
	 * @param pageNum		页数
	 * @param callback
	 */
	public ReplyPostListTask(Context context, String parent_tid,int pageNum, RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("BlogThread[parent_tid]", parent_tid);
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		url = APIUrl.BASE_URL+APIUrl.CIRCLE_REPLY_POSTS;
	}

}
