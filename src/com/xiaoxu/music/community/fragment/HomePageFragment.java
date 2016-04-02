package com.xiaoxu.music.community.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;
import com.xiaoxu.music.community.BaseLazyLoadFragment;
import com.xiaoxu.music.community.ExitAppliation;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.PlayActivity;
import com.xiaoxu.music.community.activity.SearchActivity;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.view.CustomViewPager;

public class HomePageFragment extends BaseLazyLoadFragment implements OnClickListener{
	
	private CustomViewPager viewpager;
	private TextView btn_recommend, btn_music, btn_sortlist;
	private ImageButton btn_play,btn_search;
	
	private MusicLibFragment frg_musiclib;
	private RecommendFragment frg_recommend;
	private SortListFragment frg_sort;
	private ViewPagerAdapter adapter;
	
	private final  String mPageName = "HomePageFragment";
	
	@Override
	public int setLayoutId() {
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_homepage;
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
		getImageButton(view, R.id.title_left_btn).setOnClickListener(this);;
		btn_play = getImageButton(view, R.id.title_right_btn);
		initAnimation(btn_play);
		viewpager = (CustomViewPager) getViewPager(view, R.id.viewpager_home);
		viewpager.setOnPageChangeListener(listener);
		viewpager.setScanScroll(true);
		viewpager.setOffscreenPageLimit(3);
		
		btn_recommend = getTextView(view, R.id.btn_home_recommend);
		btn_music = getTextView(view, R.id.btn_home_music);
		btn_sortlist = getTextView(view, R.id.btn_home_sort);
		
		btn_recommend.setOnClickListener(this);
		btn_music.setOnClickListener(this);
		btn_sortlist.setOnClickListener(this);
		
		btn_play.setOnClickListener(this);
	}
	
	@Override
	public void setModel() {
		//友盟版本更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		String upgrade_mode = MobclickAgent.getConfigParams(getActivity(),"yinmeng");
		if (TextUtils.isEmpty(upgrade_mode)) {
			return;
		}
		String[] upgrade_mode_array = upgrade_mode.split(",");
		UmengUpdateAgent.update(getActivity());
		UmengUpdateAgent.forceUpdate(getActivity());// 这行如果是强制更新就一定加上
		for (String mode : upgrade_mode_array) {
			String versionName = ActivityUtil.getVersionName(getActivity());
			versionName = versionName+"f";
			if (mode.equals(versionName)) {
				UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
		            @Override
		            public void onClick(int status) {
		            	switch (status) {
		            	case UpdateStatus.Update: 
		            		break;
		                default:  
		                  ExitAppliation.getInstance().exit();
		                }
		             }
	            });
				break;
             }
		}
		lazyLoad();
	}
	
	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		if(!isVisible || !isPrepared || mHasLoadOnce)
			return;
		handler.sendEmptyMessageDelayed(1, 10);
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			frg_musiclib = new MusicLibFragment();
			frg_recommend = new RecommendFragment();
			frg_sort = new SortListFragment();
			adapter = new ViewPagerAdapter(getChildFragmentManager());
			viewpager.setAdapter(adapter);
			mHasLoadOnce = true;
		};
	};
	
	public void initTabButton(){
		btn_recommend.setTextColor(getResources().getColor(R.color.title_text_normal));
		btn_music.setTextColor(getResources().getColor(R.color.title_text_normal));
		btn_sortlist.setTextColor(getResources().getColor(R.color.title_text_normal));
		btn_recommend.setTextSize(18);
		btn_music.setTextSize(18);
		btn_sortlist.setTextSize(18);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_left_btn:
			startActivity(new Intent(context, SearchActivity.class));
			break;
		case R.id.btn_home_recommend:
			initTabButton();
			viewpager.setCurrentItem(0);
			break;
		case R.id.btn_home_music:
			initTabButton();
			viewpager.setCurrentItem(1);
			break;
		case R.id.btn_home_sort:
			initTabButton();
			viewpager.setCurrentItem(2);
			break;
		case R.id.title_right_btn:
			startActivity(new Intent(context, PlayActivity.class));
			break;
		default:
			break;
		}
	}
	
	public class ViewPagerAdapter extends FragmentStatePagerAdapter {
		
		private Fragment fragment;
		
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case 0:
				fragment = frg_recommend;
				break;
			case 1:
				fragment = frg_musiclib;
				break;
			case 2:
				fragment = frg_sort;
				break;
			default:
				break;
			}
			return fragment;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
		}
	}
	
	OnPageChangeListener listener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if (arg0 == 0) {
				initTabButton();
				btn_recommend.setTextColor(getResources().getColor(R.color.white));
				btn_recommend.setTextSize(19);
			} else if (arg0 == 1) {
				initTabButton();
				btn_music.setTextColor(getResources().getColor(R.color.white));
				btn_music.setTextSize(19);
				if(null!=frg_musiclib){
					frg_musiclib.loading();
				}
			} else if (arg0 == 2) {
				initTabButton();
				btn_sortlist.setTextColor(getResources().getColor(R.color.white));
				btn_sortlist.setTextSize(19);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}
	};
	
}
