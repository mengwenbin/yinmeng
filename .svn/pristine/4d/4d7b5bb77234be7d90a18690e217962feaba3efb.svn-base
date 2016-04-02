package com.xiaoxu.music.community.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseLazyLoadFragment;
import com.xiaoxu.music.community.HandlerHelp;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.CircleCategoryActivity;
import com.xiaoxu.music.community.activity.PlayActivity;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.dao.CacheDB;
import com.xiaoxu.music.community.dao.CacheData;
import com.xiaoxu.music.community.entity.CategoryEntity;
import com.xiaoxu.music.community.entity.CategorysInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.CircleFirstClassTask;
import com.xiaoxu.music.community.parser.ParseUtil;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.CircleIndicator;

public class CircleFragment extends BaseLazyLoadFragment implements OnClickListener{
	
	private ImageButton btn_play;
	private CircleFirstClassTask task;
	private CategoryEntity entity;
	private List<CategorysInfoEntity> list;
	
	private ViewPager mViewPager;
	private CircleIndicator indicator;
	private ViewPagerAdapter adapter;
	private LinearLayout container;
	
	//缓存
	private CacheDB db;
	private CacheData cacheinfo;
	private String cache_name = "circle";
	
	private final  String mPageName = "CircleFragment";
	
	@Override
	public int setLayoutId() {
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_circle2;
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
		rotateLoading = getRotateLoading(view, R.id.loading);
		no_network = getImageButton(view, R.id.no_network);
		
		getImageButton(view, R.id.title_left_btn).setVisibility(View.GONE);
		getTextView(view, R.id.title_center_content).setText(getString(R.string.tab_footbar_circle));
		btn_play = getImageButton(view, R.id.title_right_btn);
		btn_play.setOnClickListener(this);
		initAnimation(btn_play);
		
		container = (LinearLayout) view.findViewById(R.id.container);
		mViewPager = (ViewPager) view.findViewById(R.id.circle_pager);
		indicator = (CircleIndicator) view.findViewById(R.id.indicator);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setPageMargin(ActivityUtil.dip2px(context, 30));
		container.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				 return mViewPager.dispatchTouchEvent(event);  
			}
        });
		adapter = new ViewPagerAdapter();
		mViewPager.setAdapter(adapter);
		
	}
	
	@Override
	public void setModel() {
		// TODO Auto-generated method stub
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
			db = new CacheDB(context);
			loadingStart(rotateLoading);
			loading();
		};
	};
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.no_network:
			hintNoNet();
			task();
			break;
		case R.id.title_right_btn:
			startActivity(new Intent(context, PlayActivity.class));
			break;
		default:
			break;
		}
	}
	
	
	public void loading(){
		if(context==null)
			return;
		if(adapter!=null&&adapter.getCount()>0)
			return;
		new Async().execute();
	}
	
	class Async extends HandlerHelp {
		
		@Override
		public void doTask() {
			// TODO Auto-generated method stub
			cacheinfo = db.getCacheDataByName(cache_name);
		}
		
		@Override
		public void handler() {
			// TODO Auto-generated method stub
			if(cacheinfo!=null && TimeUtil.datePk(TimeUtil.getCurrentTime(), cacheinfo.getCache_time())){
				String result = cacheinfo.getCache_data();
				CategoryEntity entity = (CategoryEntity) ParseUtil.parseResultObj(result, CategoryEntity.class);
				List<CategorysInfoEntity> list_cache = entity.getCategory_list();
				if (list_cache != null && list_cache.size() > 0) {
					list = list_cache;
					updateUI();
					loadingCancle(rotateLoading);
					mHasLoadOnce = true;
				}else{
					task();
				}
			} else {
				task();
			}
		}
	}
	
	public void task(){
		if(haveNetwork(context)){
			task = new CircleFirstClassTask(context, callback);
			task.excute();
		}else{
			showNoNet(this);
			loadingCancle(rotateLoading);
		}
	} 
	
	BaseRequestCallBack callback = new BaseRequestCallBack(CategoryEntity.class) {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			loadingCancle(rotateLoading);
			if(code!=Constant.SUCCESS_REQUEST){
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			entity = (CategoryEntity) obj;
			list = entity.getCategory_list();
			updateUI();
			//缓存到数据库
			cacheinfo = db.getCacheDataByName(cache_name);
			if(cacheinfo==null){
				cacheinfo = new CacheData(cache_name, result, TimeUtil.getCurrentTime()+ CacheDB.time_circle);
				db.add(cacheinfo);
			}else{
				cacheinfo.setCache_data(result);
				cacheinfo.setCache_time(TimeUtil.getCurrentTime()+ CacheDB.time_circle);
				db.update(cacheinfo);
			}
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			// TODO Auto-generated method stub
			loadingCancle(rotateLoading);
		}
	};
	
	public void updateUI() {
		adapter.notifyDataSetChanged();
		if(adapter!=null&&adapter.getCount()>0){
			mHasLoadOnce = true;
			indicator.setViewPager(mViewPager);
		}
	}

	class ViewPagerAdapter extends PagerAdapter {
		
		private LayoutInflater inflater;
		
		public ViewPagerAdapter() {
			inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View layout = inflater.inflate(R.layout.adapter_circle_category,container, false);
			TextView tv_name = (TextView) layout.findViewById(R.id.category_name);
			TextView tv_new = (TextView) layout.findViewById(R.id.category_new_num);
			TextView tv_all = (TextView) layout.findViewById(R.id.category_all_num);
			ImageView imageView = (ImageView) layout.findViewById(R.id.category_image);
			
			String imgUrl = list.get(position).getCategory_img();
			tv_name.setText(list.get(position).getCategory_name());
			tv_new.setText("今天新帖数量："+list.get(position).getCount_today());
			tv_all.setText("总帖数量："+list.get(position).getCount_all());
			bitmapUtils.display(imageView, StringUtil.replaceImagePath(imgUrl, 600));
			layout.setId(position);
			layout.setOnClickListener(l);
			((ViewPager) container).addView(layout, 0);
			return layout;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	OnClickListener l = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent in = new Intent(context, CircleCategoryActivity.class);
			in.putExtra("category", list.get(v.getId()));
			startActivity(in);
		}
	};

}
