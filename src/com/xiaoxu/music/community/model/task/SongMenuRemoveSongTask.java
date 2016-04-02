package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

public class SongMenuRemoveSongTask extends BaseRequestTask {

	public SongMenuRemoveSongTask(Context context,String music_id,String songmenu_id,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("MusicUser[music_id]", music_id);
		params.addBodyParameter("MusicUser[musiclist_id]",songmenu_id);
		url = APIUrl.BASE_URL + APIUrl.SONG_MENU_REMOVE;
	}

}
