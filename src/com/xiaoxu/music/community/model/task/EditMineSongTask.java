package com.xiaoxu.music.community.model.task;

import java.io.File;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseUploadTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class EditMineSongTask extends BaseUploadTask {
	/**
	 * 修改歌单信息
	 * @param context
	 * @param filePath
	 * @param callback
	 */
	public EditMineSongTask(Context context,String musiclist_id,String musiclist_name,
			String musiclist_summary,String musiclist_img,int pageNum,RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("MusicList[musiclist_id]", musiclist_id);
		params.addBodyParameter("MusicList[musiclist_name]", musiclist_name);
		params.addBodyParameter("MusicList[musiclist_summary]", musiclist_summary);
		if(!musiclist_img.equals("")){
			params.addBodyParameter("MusicList[musiclist_img]", new File(musiclist_img));
		}
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		url = APIUrl.BASE_URL + APIUrl.UPDATE_MINE_SONG;
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
