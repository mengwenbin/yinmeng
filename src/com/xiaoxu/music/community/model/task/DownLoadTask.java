package com.xiaoxu.music.community.model.task;

import java.io.File;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.model.impl.BaseDownLoadTask;

public class DownLoadTask extends BaseDownLoadTask {
	
	/**
	 * 下载音乐
	 * @param context
	 * @param requestPath
	 * @param storagePath
	 * @param callback
	 */
	public DownLoadTask(Context context, String requestPath,
			String storagePath, RequestCallBack<File> callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
		url = requestPath;
		savaUrl = storagePath;
	}
	
	@Override
	public void excute() {
		// TODO Auto-generated method stub
		/**
		 * 1.请求路径 ；2.保存路径 ； 3.如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
		 * 4.如果从请求返回信息中获取到文件名，下载完成后自动重命名。5.回调
		 */
		httpUtils = new HttpUtils();
		httpHandler = httpUtils.download(url, savaUrl, true, false, callback);
	}
	
	// 停止下载
	public void cancelTask() {
		httpHandler.cancel(httpHandler.isCancelled());
	}

}
