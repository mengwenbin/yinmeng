package com.xiaoxu.music.community.model.task;

import java.io.File;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseUploadTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class UploadPostImageTask extends BaseUploadTask {
	/**
	 * 上传帖子图片
	 * @param context
	 * @param filePath  图片路径
	 * @param filename  图片名*（非必传）
	 * @param description 图片描述*（非必传）
	 * @param user_id   用户id
	 * @param token     用户token
	 * @param callback  回调
	 */
	public UploadPostImageTask(Context context, String filePath,String filename,
			String description,RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("Attachment[attachment]",new File(filePath));
		params.addBodyParameter("Attachment[filename]", filename);
		params.addBodyParameter("Attachment[description]",description);
		url = APIUrl.BASE_URL + APIUrl.UPLOAD_POSTIMAGE;
	}

	@Override
	public void excute() {
		httpUtils = new HttpUtils();
		httpHandler = httpUtils.send(HttpMethod.POST, url, params, callback);
	}
	
	public void cancelTask() {
		httpHandler.cancel(httpHandler.isCancelled());
		httpUtils = null;
	}

}
