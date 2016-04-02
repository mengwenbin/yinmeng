package com.xiaoxu.music.community.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;

public class WelcomeActivity extends BaseNewActivity {

	private ViewPager pager;
	private PagerAdapter adapter;
	private ImageView into_homepage;
	private List<ImageView> views = new ArrayList<ImageView>();
	
	private final String mPageName = "WelcomeActivity";
	private int[] imgIds = { R.drawable.guidance1, R.drawable.guidance2,R.drawable.guidance3, R.drawable.guidance4 };

	@Override
	public void setupView() {

		pager = (ViewPager) findViewById(R.id.pager);
		into_homepage = (ImageView) findViewById(R.id.into_homepage);
	}

	@Override
	public void setContent() {
		setContentView(R.layout.activity_welcome);
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

		initData();
		initAdapter();
		pager.setOnPageChangeListener(listener);

	}

	private OnPageChangeListener listener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 3:
				into_homepage.setVisibility(View.VISIBLE);
				into_homepage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startActivity(new Intent(context, StartActivity.class));
						overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
						WelcomeActivity.this.finish();
					}
				});
				break;

			default:
				into_homepage.setVisibility(View.GONE);
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

	private void initData() {
		
		views.clear();
		for (int i = 0; i <imgIds.length; i++) {
			ImageView view = new ImageView(context);
			view.setScaleType(ScaleType.CENTER_CROP);
			view.setImageResource(imgIds[i]);
			views.add(view);
		}
	}
	
	private void initAdapter(){
		
		adapter = new PagerAdapter() {

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(views.get(position));
				return views.get(position);
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
		};
		pager.setAdapter(adapter);
	}
}
