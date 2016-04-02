package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.socialize.utils.Log;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class ReplyPostTask extends BaseRequestTask {
	/**
	 * 回帖子
	 * @param context
	 * @param token			token
	 * @param user_id		用户id
	 * @param parent_tid	被回帖的id
	 * @param username		用户名
	 * @param content		回帖内容
	 * @param imageIds		【非必传】回帖图片（id用‘ 逗号 ’ 隔开）
	 * @param callback
	 */
	public ReplyPostTask(Context context, String parent_tid,String username,
			String content, String imageIds, RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("BlogThread[parent_tid]", parent_tid);
		params.addBodyParameter("BlogThread[username]", username);
		params.addBodyParameter("BlogThread[blog_message]", content);
		params.addBodyParameter("BlogThread[a_id]", imageIds);
		url = APIUrl.BASE_URL+APIUrl.CIRCLE_REPLY_POST;
	}

}
