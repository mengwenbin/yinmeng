package com.xiaoxu.music.community.activity;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.fragment.MoreRecommendHotFragment;
import com.xiaoxu.music.community.fragment.MoreRecommendNewFragment;
import com.xiaoxu.music.community.fragment.MoreRecommendPopularFragment;
import com.xiaoxu.music.community.fragment.MoreRecommendPublicityFragment;
import com.xiaoxu.music.community.view.CustomViewPager;

public class MoreRecommendActivity extends BaseNewActivity implements OnClickListener{
	
	private ImageButton btn_right;
	private TextView tv_title;
	
	private RelativeLayout layout_indicator_original, layout_indicator_cover;
	private TextView tv_indicator_original, tv_indicator_cover;
	private View line_indicator_new, line_indicator_hot, line_indicator_popular, line_indicator_publicity;
	private Button btn_indicator_new, btn_indicator_hot, btn_indicator_popular, btn_indicator_publicity;
	
	private CustomViewPager viewpager;
	private ViewPagerAdapter adapter;
	
	private MoreRecommendNewFragment frag_new;
	private MoreRecommendHotFragment frag_hot;
	private MoreRecommendPopularFragment frag_popular;
	private MoreRecommendPublicityFragment frag_publicity;
	
	private final String mPageName = "MoreRecommendActivity";
	private int music_category = 51;
	
	public int getMusic_category() {
		return music_category;
	}
	
	@Override
	public void setContent() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_recommend_more);
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
	public void setupView() {
		// TODO Auto-generated method stub
		btn_right = getImageButton(R.id.title_right_btn);
		btn_right.setOnClickListener(this);
		initAnimation(btn_right);
		
		getImageButton(R.id.title_left_btn).setOnClickListener(this);
		tv_title =  getTextView(R.id.title_center_content);
		tv_title.setText("更多推荐");
		//原创翻唱指示器
		layout_indicator_original = getRelativeLayout(R.id.layout_indicator_original);
		layout_indicator_cover = getRelativeLayout(R.id.layout_indicator_cover);
		tv_indicator_original = getTextView(R.id.tv_indicator_original);
		tv_indicator_cover = getTextView(R.id.tv_indicator_cover);
		layout_indicator_original.setOnClickListener(this);
		layout_indicator_cover.setOnClickListener(this);
		//热门，周榜，月榜，总榜
		btn_indicator_new = getButton(R.id.btn_indicator_new);
		btn_indicator_hot = getButton(R.id.btn_indicator_hot);
		btn_indicator_popular = getButton(R.id.btn_indicator_popular);
		btn_indicator_publicity = getButton(R.id.btn_indicator_publicity);
		line_indicator_new = findViewById(R.id.indicator_new);
		line_indicator_hot = findViewById(R.id.indicator_hot);
		line_indicator_popular = findViewById(R.id.indicator_popular);
		line_indicator_publicity = findViewById(R.id.indicator_publicity);
		btn_indicator_new.setOnClickListener(this);
		btn_indicator_hot.setOnClickListener(this);
		btn_indicator_popular.setOnClickListener(this);
		btn_indicator_publicity.setOnClickListener(this);
		
		frag_new = new MoreRecommendNewFragment();
		frag_hot = new MoreRecommendHotFragment();
		frag_popular = new MoreRecommendPopularFragment();
		frag_publicity = new MoreRecommendPublicityFragment();
		
		viewpager = (CustomViewPager) findViewById(R.id.viewpager_song);
		viewpager.setScanScroll(true);
		viewpager.setOnPageChangeListener(listener);
		adapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
	}
	
	@Override
	public void setModel() {
		// TODO Auto-generated method stub
	}
	
	//初始化歌曲类型指示器
	public void initIndicatorSong(){
		layout_indicator_original.setBackgroundResource(R.drawable.bg_shape_original_normal);
		layout_indicator_cover.setBackgroundResource(R.drawable.bg_shape_cover_normal);
		tv_indicator_original.setTextColor(getResources().getColor(R.color.cyan8));
		tv_indicator_cover.setTextColor(getResources().getColor(R.color.cyan8));
	}
	
	//初始化歌曲类型指示器
	public void initIndicatorSort(){
		btn_indicator_new.setTextColor(getResources().getColor(R.color.tab_text_normal));
		btn_indicator_hot.setTextColor(getResources().getColor(R.color.tab_text_normal));
		btn_indicator_popular.setTextColor(getResources().getColor(R.color.tab_text_normal));
		btn_indicator_publicity.setTextColor(getResources().getColor(R.color.tab_text_normal));
		line_indicator_new.setVisibility(View.GONE);
		line_indicator_hot.setVisibility(View.GONE);
		line_indicator_popular.setVisibility(View.GONE);
		line_indicator_publicity.setVisibility(View.GONE);
	}
	
	public void changeData(){
		frag_new.setMusic_category(music_category);
		frag_hot.setMusic_category(music_category);
		frag_popular.setMusic_category(music_category);
		frag_publicity.setMusic_category(music_category);
		frag_new.loading();
		frag_hot.loading();
		frag_popular.loading();
		frag_publicity.loading();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_left_btn:
			finish();
			break;
		case R.id.title_right_btn:
			startActivity(new Intent(context, PlayActivity.class));
			break;
		case R.id.layout_indicator_original:
			initIndicatorSong();
			layout_indicator_original.setBackgroundResource(R.drawable.bg_shape_original_press);
			tv_indicator_original.setTextColor(getResources().getColor(R.color.white));
			if(music_category == 52){
				music_category = 51;
				changeData();
			}
			break;
		case R.id.layout_indicator_cover:
			initIndicatorSong();
			layout_indicator_cover.setBackgroundResource(R.drawable.bg_shape_cover_press);
			tv_indicator_cover.setTextColor(getResources().getColor(R.color.white));
			if(music_category == 51){
				music_category = 52;
				changeData();
			}
			break;
		case R.id.btn_indicator_new:
			viewpager.setCurrentItem(0);
			break;
		case R.id.btn_indicator_hot:
			viewpager.setCurrentItem(1);
			break;
		case R.id.btn_indicator_popular:
			viewpager.setCurrentItem(2);
			break;
		case R.id.btn_indicator_publicity:
			viewpager.setCurrentItem(3);
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
				fragment = frag_new;
				break;
			case 1:
				fragment = frag_hot;
				break;
			case 2:
				fragment = frag_popular;
				break;
			case 3:
				fragment = frag_publicity;
				break;
			default:
				break;
			}
			return fragment;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
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
				initIndicatorSort();
				btn_indicator_new.setTextColor(getResources().getColor(R.color.tab_text_select));
				line_indicator_new.setVisibility(View.VISIBLE);
			} else if (arg0 == 1) {
				initIndicatorSort();
				btn_indicator_hot.setTextColor(getResources().getColor(R.color.tab_text_select));
				line_indicator_hot.setVisibility(View.VISIBLE);
			} else if (arg0 == 2) {
				initIndicatorSort();
				btn_indicator_popular.setTextColor(getResources().getColor(R.color.tab_text_select));
				line_indicator_popular.setVisibility(View.VISIBLE);
			} else if (arg0 == 3) {
				initIndicatorSort();
				btn_indicator_publicity.setTextColor(getResources().getColor(R.color.tab_text_select));
				line_indicator_publicity.setVisibility(View.VISIBLE);
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
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if(data!=null){
//			SongInfoEntity info = (SongInfoEntity) data.getSerializableExtra("info");
//			int position = data.getIntExtra("position", -1);
//			if(info!=null&&position!=-1){
//				switch (requestCode) {
//				case 2:
//					frag_hot.changeData(position, info);
//					break;
//				case 3:
//					frag_new.changeData(position, info);
//					break;
//				case 4:
//					frag_popular.changeData(position, info);
//					break;
//				case 5:
//					frag_publicity.changeData(position, info);
//					break;
//				default:
//					break;
//				}
//			}
//		}
//	}
	
}