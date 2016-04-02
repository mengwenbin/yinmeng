package com.xiaoxu.music.community.model.task;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.entity.FilePathEntity;
import com.xiaoxu.music.community.entity.ReleseMusicEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

/**
 * 音乐人的歌曲（音乐人的歌曲）
 * @author ice
 */
public class MusicUserSongsTask extends BaseRequestTask {
	
	/**
	 * 其他人的歌曲（音乐人的歌曲）
	 * @author ice
	 */
	public MusicUserSongsTask(Context context, String other_user_id, String music_category,int pageNum,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("other_user_id", other_user_id);
		params.addBodyParameter("music_category",music_category);
		params.addBodyParameter("pageNum",String.valueOf(pageNum));
		url = APIUrl.BASE_URL + APIUrl.MUSIC_USER_MUSIC;
	}
	
	/**
	 * 我的歌曲（音乐人的歌曲）
	 * @author ice
	 */
	public MusicUserSongsTask(Context context, String music_category,int pageNum,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("other_user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("music_category",music_category);
		params.addBodyParameter("pageNum",String.valueOf(pageNum));
		url = APIUrl.BASE_URL + APIUrl.MINE_SINGLE;
	}
	/**
	 * 修改我的歌曲（音乐人的歌曲）
	 * @author ice
	 */
	public MusicUserSongsTask(Context context, SongInfoEntity entity,RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("MusicUser[music_id]",entity.getMusic_id());
		params.addBodyParameter("MusicUser[music_name]",entity.getMusic_name());
		params.addBodyParameter("MusicUser[music_category]",entity.getMusic_category());
		params.addBodyParameter("MusicUser[music_lyricser]",entity.getMusic_lyricser());
		params.addBodyParameter("MusicUser[music_composer]",entity.getMusic_composer());
		params.addBodyParameter("MusicUser[music_singing0]",entity.getMusic_singing0());
		params.addBodyParameter("MusicUser[music_singing]",entity.getMusic_singing());
		params.addBodyParameter("MusicUser[music_mixed]",entity.getMusic_mixed());
		params.addBodyParameter("MusicUser[music_tag_int]",entity.getMusic_tag_int());
		params.addBodyParameter("MusicUser[music_coverimg]",entity.getMusic_coverimg());
		params.addBodyParameter("MusicUser[music_url]",entity.getMusic_url());
		params.addBodyParameter("MusicUser[music_lyric]",entity.getMusic_lyric());
		url = APIUrl.BASE_URL + APIUrl.UPDATE_MINE_SINGLE;
	}
	
	/**
	 * 删除我的歌曲（音乐人的歌曲）
	 * @author ice
	 */
	public MusicUserSongsTask(Context context, String music_id,RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("MusicUser[music_id]",music_id);
		url = APIUrl.BASE_URL + APIUrl.DELETE_MINE_SINGLE;
	}

	/**
	 * 我的收藏单曲
	 * @author ice
	 */
	public MusicUserSongsTask(Context context,int pageNum,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token",AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("pageNum",String.valueOf(pageNum));
		url = APIUrl.BASE_URL + APIUrl.MINE_FAVORITE;
	}	

}
