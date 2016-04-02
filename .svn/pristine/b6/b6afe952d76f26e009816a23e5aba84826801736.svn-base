package com.xiaoxu.music.community.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewFragment;
import com.xiaoxu.music.community.R;

public class MusicDetailLyricFragment extends BaseNewFragment {
	
	private String content;
	private TextView tv_lyric;
	private final  String mPageName = "MusicDetailLyricFragment";
	
	public void setContent(String content) {
		this.content = content;
		setModel();
	}
	
	public MusicDetailLyricFragment() {
		super();
	}
	
	public MusicDetailLyricFragment(String content) {
		super();
		this.content = content;
	}
	

	@Override
	public int setLayoutId() {
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_music_detail_lyric;
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
		tv_lyric = (TextView) view.findViewById(R.id.tv_lyric);
	}
	
	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(content)){
			tv_lyric.setText(content);
		}else{
			tv_lyric.setText("无歌词");
		}
	}

}
