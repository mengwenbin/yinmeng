package com.xiaoxu.music.community.fragment;

import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewFragment;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.entity.SongInfoEntity;

public class MusicDetailAuthorFragment extends BaseNewFragment {
	
	private SongInfoEntity info;
	private TextView tv_singer,tv_writer,tv_composer,tv_arranger,tv_mixuser;
	
	private final  String mPageName = "MusicDetailAuthorFragment";

	public void setInfo(SongInfoEntity info) {
		this.info = info;
		setModel();
	}
	
	public MusicDetailAuthorFragment() {
		super();
	}

	public MusicDetailAuthorFragment(SongInfoEntity info) {
		super();
		this.info = info;
	}

	@Override
	public int setLayoutId() {
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_music_detail_author;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
		MobclickAgent.onResume(activity);//友盟统计
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(activity);
	}

	@Override
	public void setupView(View view) {
		// TODO Auto-generated method stub
		tv_singer = getTextView(view, R.id.tv_singer);//演唱
		tv_writer = getTextView(view, R.id.tv_writer);//作词
		tv_composer = getTextView(view, R.id.tv_composer);//作曲
		tv_arranger = getTextView(view, R.id.tv_arranger);//编曲
		tv_mixuser = getTextView(view, R.id.tv_mixuser);//混缩
	}

	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		if(info!=null){
			tv_singer.setText("演唱:"+info.getMusic_singing());
			tv_writer.setText("作词:"+info.getMusic_lyricser());
			tv_composer.setText("作曲:"+info.getMusic_composer());
			tv_arranger.setText("编曲:"+info.getMusic_arranger());
			tv_mixuser.setText("混缩:"+info.getMusic_mixed());
		}
	}

}
