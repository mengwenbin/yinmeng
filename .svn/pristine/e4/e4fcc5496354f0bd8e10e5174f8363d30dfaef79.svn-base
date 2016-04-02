package com.xiaoxu.music.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.MediaController;
import android.widget.VideoView;
import com.xiaoxu.music.community.R;

public class PlayVideoActivity extends Activity {
	
	private VideoView vv;
	private String uriStr;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_video);
		intent = getIntent();
		uriStr = intent.getStringExtra("video_url");
		vv = (VideoView)findViewById(R.id.video_view);
		MediaController mc = new MediaController(this);
		vv.setMediaController(mc);
		if(!TextUtils.isEmpty(uriStr)){
			vv.setVideoPath(uriStr);   //vv.setVideoURI(Uri.parse(uriStr));
			vv.requestFocus();         //获取焦点
			vv.start();
		}
	}

	/**
	 * 改变屏幕方向、弹出软件 盘和隐藏软键盘时，不再去执行onCreate()方法，而是直接执行onConfigurationChanged()。 
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

}
