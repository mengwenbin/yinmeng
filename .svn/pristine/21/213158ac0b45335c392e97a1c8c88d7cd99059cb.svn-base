package com.xiaoxu.music.community.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewFragment;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.MusicUserDetailActivity;
import com.xiaoxu.music.community.adapter.MusicUserSongsAdapter;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.view.CustomViewPager;

public class MusicUserWorksFragment extends BaseNewFragment implements OnClickListener{
	
	private MusicUserDetailActivity act;
	private String user_id;
	private MusicUserOriginalFragment f_original;
	private MusicUserCoverFragment f_cover;
	
	private TextView tv_original,tv_cover;
	private CustomViewPager viewpager;
	private ViewPagerAdapter adapter;
	private final  String mPageName = "MusicUserWorksFragment";
	
	@Override
	public int setLayoutId() {
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_music_user_works;
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
		tv_original = getTextView(view, R.id.indicator_original);
		tv_cover = getTextView(view, R.id.indicator_conver);
		tv_original.setOnClickListener(this);
		tv_cover.setOnClickListener(this);
		viewpager = (CustomViewPager) view.findViewById(R.id.viewpager);
		viewpager.setOnPageChangeListener(listener);
		viewpager.setScanScroll(true);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.indicator_original:
			initTabButton();
			tv_original.setTextColor(getResources().getColor(R.color.tab_text_select));
			viewpager.setCurrentItem(0);
			f_original.loading();
			break;
		case R.id.indicator_conver:
			initTabButton();
			tv_cover.setTextColor(getResources().getColor(R.color.tab_text_select));
			viewpager.setCurrentItem(1);
			f_cover.loading();
			break;
		default:
			break;
		}
	}

	@Override
	public void setModel() {
		act = (MusicUserDetailActivity) activity;
		user_id = act.getUser_id();
		f_original = new MusicUserOriginalFragment(user_id,this);
		f_cover = new MusicUserCoverFragment(user_id,this);
		adapter = new ViewPagerAdapter(getChildFragmentManager());
		viewpager.setAdapter(adapter);
	}
	
	public void initTabButton(){
		tv_original.setTextColor(getResources().getColor(R.color.tab_text_normal));
		tv_cover.setTextColor(getResources().getColor(R.color.tab_text_normal));
	}
	
	public class ViewPagerAdapter extends FragmentStatePagerAdapter {
		
		private Fragment fragment;
		
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				fragment = f_original;
				break;
			case 1:
				fragment = f_cover;
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
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}
	}
	
	OnPageChangeListener listener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			if (arg0 == 0) {
				initTabButton();
				tv_original.setTextColor(getResources().getColor(R.color.tab_text_select));
			} else if (arg0 == 1) {
				initTabButton();
				tv_cover.setTextColor(getResources().getColor(R.color.tab_text_select));
			}
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {}
		
	};
	
}
