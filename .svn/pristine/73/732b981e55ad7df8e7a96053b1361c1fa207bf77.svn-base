package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;
/**
 * 删除歌单
 * @author ice
 */
public class DeleteSongMenuTask extends BaseRequestTask {
	
	public DeleteSongMenuTask(Context context, String songmenu_id,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("MusicList[musiclist_id]", songmenu_id);
		url = APIUrl.BASE_URL + APIUrl.SONG_MENU_DELETE;
	}

}
