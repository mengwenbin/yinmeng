package com.xiaoxu.music.community.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;

public class SplashActivity extends BaseNewActivity {

	private Animation animation;
	private ImageView image_top;
	private ImageView image_buttom;
	private Intent intent = null;
	private SharedPreferences sp;
	
	private final String mPageName = "SplashActivity";
	
	@Override
	public void setupView() {
		
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		image_top = (ImageView) findViewById(R.id.splash_title);
		image_buttom = (ImageView) findViewById(R.id.splash_button);
		animation = AnimationUtils.loadAnimation(context, R.anim.scale_translate_rotate);
		image_top.setAnimation(AnimationUtils.loadAnimation(context, R.anim.image_anim));
		image_buttom.setAnimation(animation);
	}

	@Override
	public void setContent() {
		setContentView(R.layout.activity_splash);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
		MobclickAgent.onResume(this);//友盟统计
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}

	@Override
	public void setModel() {
		
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				if(sp.getBoolean("isfirst", true)){
					Editor edit = sp.edit();
					edit.putBoolean("isfirst", false);
					edit.commit();
					intent = new Intent(context, WelcomeActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
					
				}else{
					intent = new Intent(context, StartActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
				}
			}
		});
	}

}
