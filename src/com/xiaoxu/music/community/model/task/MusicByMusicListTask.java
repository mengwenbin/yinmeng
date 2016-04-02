package com.xiaoxu.music.community.model.task;

import android.content.Context;
import android.text.TextUtils;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

/**
 * 通过歌单请求歌曲
 * @author ice
 */
public class MusicByMusicListTask extends BaseRequestTask {

	public MusicByMusicListTask(Context context,String musiclist_id,int pageNum,
			RequestCallBack<String> callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
		String user_id = AccountInfoUtils.getInstance(context).getAccountId();
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", TextUtils.isEmpty(user_id)?"0":user_id);
		params.addBodyParameter("musiclist_id", musiclist_id);
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		url = APIUrl.BASE_URL + APIUrl.MUSICLIB_MUSIC_BYMUSICLIST;
	}
	
}
