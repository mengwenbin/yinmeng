package com.xiaoxu.music.community.model.task;

import java.io.File;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseUploadTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class UploadMusicTask extends BaseUploadTask {
	/**
	 * 上传音乐文件
	 * @param context
	 * @param filePath
	 * @param callback
	 */
	public UploadMusicTask(Context context, String filePath,String musicname,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("Attachment[filename]",musicname);
		params.addBodyParameter("Attachment[attachment]", new File(filePath));
		url = APIUrl.BASE_URL + APIUrl.UPLOAD_MUSIC;
	}

	@Override
	public void excute() {
		httpUtils = new HttpUtils();
		httpHandler = httpUtils.send(HttpMethod.POST, url, params, callback);
	}

	public void cancelTask() {
		httpUtils = null;
		httpHandler.cancel(httpHandler.isCancelled());
	}
}
