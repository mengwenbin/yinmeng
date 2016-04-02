package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class ReleasePostTask extends BaseRequestTask {
	/**
	 * 
	 * @param context
	 * @param token
	 * @param user_id 用户id
	 * @param blog_category  帖子类别
	 * @param username   用户名
	 * @param blog_message   帖子文本
	 * @param imageIds       帖子图片的id
	 * @param callback       回调
	 */
	public ReleasePostTask(Context context, String blog_category, String username, 
			String blog_message, String imageIds, RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("BlogThread[blog_category]", blog_category);
		params.addBodyParameter("BlogThread[username]", username);
		params.addBodyParameter("BlogThread[blog_message]", blog_message);
		params.addBodyParameter("BlogThread[a_id]", imageIds);
		url = APIUrl.BASE_URL+APIUrl.CIRCLE_RELEASE_POST;
	}
}
