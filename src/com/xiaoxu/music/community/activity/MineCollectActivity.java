package com.xiaoxu.music.community.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.fragment.SongFragment;
import com.xiaoxu.music.community.fragment.SongMenuFragment;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.view.CustomViewPager;

/*
 * 我的收藏模块
 */
public class MineCollectActivity extends BaseNewActivity implements OnClickListener{

	private TextView all_check;
	private TextView complete;
	private TextView or_tv;
	private TextView cover_tv;
	private ImageView left_btn;
	private ImageButton right_btn;
	private CustomViewPager viewPager;
	private RelativeLayout or_layout;
	private RelativeLayout cover_layout;
	private List<Fragment> fragments;
	//歌曲
	private SongFragment songFragment;
	//歌单
	private SongMenuFragment songMenuFragment;
	private final String mPageName = "MineCollectActivity";
	private String Login_Tag = "com.xiaoxu.music.community.activity.MineCollectActivity";
	
	@Override
	public void setupView() {
		
		all_check = (TextView) findViewById(R.id.all_check_tv);
		complete = (TextView) findViewById(R.id.complete_tv);
		left_btn = (ImageView) findViewById(R.id.left_btn);
		right_btn = (ImageButton) findViewById(R.id.right_btn);
		initAnimation(right_btn);
		
		or_tv = (TextView) findViewById(R.id.or_tv);
		cover_tv = (TextView) findViewById(R.id.cover_tv);
		or_layout = (RelativeLayout) findViewById(R.id.or_layout);
		cover_layout = (RelativeLayout) findViewById(R.id.cover_layout);
		viewPager = (CustomViewPager) findViewById(R.id.view_pager);
		viewPager.setScanScroll(true);
		initListener();
		
	}

	@Override
	public void setContent() {
		setContentView(R.layout.activity_minecollect);
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
		
		if(!AccountInfoUtils.getInstance(context).isLogin()){
			Intent intent = new Intent(context, StartActivity.class);
			intent.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
			startActivityForResult(intent, 1);
		}
		songFragment = new SongFragment();
		songMenuFragment = new SongMenuFragment();
		initFragment();
		viewPager.setCurrentItem(0);

	}
	
	/*
	 * 初始化监听
	 */
	private void initListener() {

		left_btn.setOnClickListener(this);
		right_btn.setOnClickListener(this);
		or_layout.setOnClickListener(this);
		cover_layout.setOnClickListener(this);
		all_check.setOnClickListener(this);
		complete.setOnClickListener(this);
		viewPager.setOnPageChangeListener(listener);
	}

	/*
	 * 初始化Fragment
	 */
	private void initFragment() {
		
		fragments = new ArrayList<Fragment>();
		fragments.add(songFragment);
		fragments.add(songMenuFragment);
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		
	}

	
	private class ViewPagerAdapter extends FragmentPagerAdapter{

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.right_btn:
			startActivity(new Intent(context, PlayActivity.class));
			break;
		case R.id.or_layout:
			viewPager.setCurrentItem(0);
			songFragment.loading();
			break;
		case R.id.cover_layout:
			viewPager.setCurrentItem(1);
			songMenuFragment.loading();
			break;
			
		case R.id.all_check_tv:
			if(songFragment.isVisible()){
				songFragment.all_check();
			}
			if(songMenuFragment.isVisible()){
				songMenuFragment.all_check();
			}
			break;
		case R.id.complete_tv:
			if(songFragment.isVisible()){
				songFragment.complete();
			}
			if(songFragment.isVisible()){
				songMenuFragment.complete();
			}
			break;
		}
	}
	
	/*
	 * viewPager监听
	 */
	OnPageChangeListener listener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {

			switch (position) {
			case 0:
				viewPager.setCurrentItem(0);
				or_layout.setBackgroundResource(R.drawable.bg_songpress);
				cover_layout.setBackgroundResource(R.drawable.bg_songmenunormal);
				or_tv.setTextColor(getResources().getColor(R.color.white));
				cover_tv.setTextColor(getResources().getColor(R.color.cyan8));
				break;

			case 1:
				viewPager.setCurrentItem(1);
				or_layout.setBackgroundResource(R.drawable.bg_songnormal);
				cover_layout.setBackgroundResource(R.drawable.bg_songmenupress);
				cover_tv.setTextColor(getResources().getColor(R.color.white));
				or_tv.setTextColor(getResources().getColor(R.color.cyan8));
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 1){
			if(!AccountInfoUtils.getInstance(context).isLogin()){
				finish();
			}
		}
	}

}
