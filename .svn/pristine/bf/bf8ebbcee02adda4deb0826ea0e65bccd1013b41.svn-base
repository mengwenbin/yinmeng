package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.entity.FilePathEntity;
import com.xiaoxu.music.community.entity.ReleseMusicEntity;
import com.xiaoxu.music.community.model.impl.BaseUploadTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class ReleseMusicTask extends BaseUploadTask{
	
	/**
	 * 发布音乐
	 * @param context
	 * @param filePath
	 * @param callback
	 */
	public ReleseMusicTask(Context context, ReleseMusicEntity entity,FilePathEntity pathEntity,RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("MusicUser[music_name]",entity.getMusic_name());
		params.addBodyParameter("MusicUser[music_category]",entity.getMusic_category());
		params.addBodyParameter("MusicUser[music_lyricser]",entity.getMusic_lyricser());
		params.addBodyParameter("MusicUser[music_composer]",entity.getMusic_composer());
		params.addBodyParameter("MusicUser[music_singing0]",entity.getMusic_singing0());
		params.addBodyParameter("MusicUser[music_singing]",entity.getMusic_singing());
		params.addBodyParameter("MusicUser[music_arranger]",entity.getMusic_arranger());
		params.addBodyParameter("MusicUser[music_mixed]",entity.getMusic_mixed());
		params.addBodyParameter("MusicUser[music_tag_int]",entity.getMusic_tag());
		params.addBodyParameter("MusicUser[music_coverimg]",pathEntity.getImagefile_path());
		params.addBodyParameter("MusicUser[music_url]",pathEntity.getMusicfile_path());
		params.addBodyParameter("MusicUser[music_lyric]",entity.getMusic_lyric());
		url = APIUrl.BASE_URL + APIUrl.RELESE_MUSIC;
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
