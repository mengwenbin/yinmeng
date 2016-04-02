package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class CountMusicTask extends BaseRequestTask {
	/**
	 * 统计歌曲的   播放和分享   次数
	 * @param context
	 * @param music_id
	 * @param type
	 * 			 1:播放  2：分享
	 * @param callback
	 */
	public CountMusicTask(Context context, String music_id, int type, RequestCallBack<String> callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("music_id", music_id);
		if(type == 1){
			url = APIUrl.BASE_URL + APIUrl.MUSIC_COUNT_PLAY;
		}else{
			url = APIUrl.BASE_URL + APIUrl.MUSIC_COUNT_SHARE;
		}
	}

}
