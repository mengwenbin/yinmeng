package com.xiaoxu.music.community.activity;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.fragment.LoginFragment;
import com.xiaoxu.music.community.fragment.RegistFragment;
import com.xiaoxu.music.community.util.AccountInfoUtils;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class StartActivity extends BaseNewActivity {

	//fragment
	private LoginFragment frag_login;// 登录注册的Fragment
	private RegistFragment frag_regist;
	
	//view
	private ViewPager viewpager;
	private ViewPagerAdapter adapter;
	private TextView tv_select_login, tv_select_regist;// 登录，注册按钮
	private ImageView select_login, select_regist;// 两个指示箭头
	private ImageButton btn_back;
	
	//跳转到那个Activity
	public static String toActivity = "";//要跳转到的Activity
	public static String LOGIN_KEY = "From_Activity";
	private Intent in;
	
	private final String mPageName = "StartActivity";
	
	@Override
	public void setContent() {
		
		//是否登录
		if (AccountInfoUtils.getInstance(context).isLogin()) {
			Intent i = new Intent(context, MainTabActivity.class);
			startActivity(i);
			finish();
		}
		setContentView(R.layout.activity_start);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		//跳转到那个Activity
		in = getIntent();
		String fromActivity = in.getStringExtra(LOGIN_KEY);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setVisibility(View.GONE);
		if (TextUtils.isEmpty(fromActivity)) {
			toActivity = "";
		} else {
			btn_back.setVisibility(View.VISIBLE);
			toActivity = fromActivity;
		}
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
	public void setupView() {
		viewpager = (ViewPager) findViewById(R.id.start_viewpager);
		select_login = (ImageView) findViewById(R.id.start_select_login);
		select_regist = (ImageView) findViewById(R.id.start_select_regist);
		
		tv_select_login = (TextView) findViewById(R.id.start_login_title);
		tv_select_regist = (TextView) findViewById(R.id.start_regist_title);
		tv_select_login.setOnClickListener(l);
		tv_select_regist.setOnClickListener(l);
		btn_back.setOnClickListener(l);
	}
	
	@Override
	public void setModel() {
		frag_login = new LoginFragment();
		frag_regist = new RegistFragment();
		
		adapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(listener);
	}
	
	// 还原 指示箭头
	public void initSelect() {
		 select_login.setImageBitmap(null);
		 select_regist.setImageBitmap(null);
	}
	
	
	/* 切换登录，注册界面 */
	OnClickListener l = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.start_login_title:
				changeFragment(0);
				break;
			case R.id.start_regist_title:
				changeFragment(1);
				break;
			default:
				break;
			}
		}
	};
	
	// 切换Fragment
	public void changeFragment(int cur) {
		switch (cur) {
		case 0:
			viewpager.setCurrentItem(0);
			break;
		case 1:
			viewpager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}
	
	public void changeLoginFragment(String num){
		frag_login.authEdit(num);
	}

	OnPageChangeListener listener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			if (arg0 == 0) {
				initSelect();
				select_login.setImageResource(R.drawable.indicator_up_white);
			} else if (arg0 == 1) {
				initSelect();
				select_regist.setImageResource(R.drawable.indicator_up_white);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	public class ViewPagerAdapter extends FragmentPagerAdapter {

		private Fragment fragment;

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}
		
		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				fragment = frag_login;
				break;
			case 1:
				fragment = frag_regist;
				break;
			default:
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
