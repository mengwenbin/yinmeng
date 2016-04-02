package com.xiaoxu.music.community.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.fragment.CoverVersionFragment;
import com.xiaoxu.music.community.fragment.OriginalFragment;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.view.CustomViewPager;

/*
 * 我的作品模块
 */
public class MineWorksActivity extends BaseNewActivity implements OnClickListener{

	private TextView or_tv;
	private TextView cover_tv;
	private ImageButton left_btn;
	private ImageButton right_btn;
	public CustomViewPager viewPager;
	private RelativeLayout or_layout;
	private RelativeLayout cover_layout;
	private List<Fragment> fragments;
	//原创
	private OriginalFragment originalFragment;
	//翻唱
	private CoverVersionFragment coverVersionFragment;
	
	private final String mPageName = "MineWorksActivity";
	private String Login_Tag = "com.xiaoxu.music.community.activity.MineWorksActivity";
	
	@Override
	public void setupView() {
		
		left_btn = (ImageButton) findViewById(R.id.title_left_btn);
		right_btn = (ImageButton) findViewById(R.id.title_right_btn);
		initAnimation(right_btn);
		or_tv = (TextView) findViewById(R.id.or_tv);
		cover_tv = (TextView) findViewById(R.id.cover_tv);
		or_layout = (RelativeLayout) findViewById(R.id.or_layout);
		cover_layout = (RelativeLayout) findViewById(R.id.cover_layout);
		viewPager = (CustomViewPager) findViewById(R.id.view_pager);
		viewPager.setScanScroll(true);
		
	}

	@Override
	public void setContent() {
		setContentView(R.layout.activity_mine_works);
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
		originalFragment = new OriginalFragment();
		coverVersionFragment = new CoverVersionFragment();
		initFragment();
		viewPager.setCurrentItem(0);
		initListener();

	}
	
	/*
	 * 初始化监听
	 */
	private void initListener() {

		left_btn.setOnClickListener(this);
		right_btn.setOnClickListener(this);
		or_layout.setOnClickListener(this);
		cover_layout.setOnClickListener(this);
		viewPager.setOnPageChangeListener(listener);
	}

	/*
	 * 初始化fragment
	 */
	private void initFragment() {
		
		fragments = new ArrayList<Fragment>();
		fragments.add(originalFragment);
		fragments.add(coverVersionFragment);
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
		case R.id.title_left_btn:
			finish();
			break;
		case R.id.title_right_btn:
			startActivity(new Intent(context, PlayActivity.class));
			break;
		case R.id.or_layout:
			viewPager.setCurrentItem(0);
			originalFragment.loading();
			break;
		case R.id.cover_layout:
			
			viewPager.setCurrentItem(1);
			coverVersionFragment.loading();

			break;
		}
	}
	
	/*
	 * viewpager滑动监听
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
		
		switch (requestCode) {
		case 0:
			originalFragment.pageNum = 1;
			originalFragment.task();
			break;
		case 1:
			if(!AccountInfoUtils.getInstance(context).isLogin()){
				finish();
			}
			break;

		case 2:
			coverVersionFragment.pageNum = 1;
			coverVersionFragment.task();
			break;
		}
		
	}

}