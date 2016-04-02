package com.xiaoxu.music.community.view;

import java.util.ArrayList;
import java.util.List;
import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.WebViewActivity;
import com.xiaoxu.music.community.entity.AdvertisementInfoEntity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @步骤1 直接在XML中添加此控件（自定义相对布局），然后再代码中找到控件先调用init方法
 * @步骤2 选择调用setAutoNext方法，设置是否自动滚动，默认不滚动
 * @步骤3 必须调用setAdapter(5);
 */
public class MyViewPager extends RelativeLayout implements
		OnPageChangeListener, Runnable {
	
	private int count;
	private int size = 16;
	private int margin = 5;
	private Context context;
	private ViewPager viewPager;
	private int marginButtom = 20;
	private int autoNextTime = 5000;
	private Handler hd = new Handler();
	private boolean isAutoNext = false;
	private LinearLayout tagImageLayout;
	private int tagImageId_seleced, tagImageId_nomorl;
	private List<ImageView> imageList = new ArrayList<ImageView>();
	
	/** 当前viewpager显示第几�? ***/
	private int currentItem = 0;
	private List<String> list;
	private BitmapUtils bitmapUtils;
	private List<AdvertisementInfoEntity> entityList;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public MyViewPager(Context context) {
		super(context);
		this.context = context;

	}

	/**
	 * 
	 * @param id1
	 *            小圆选中后图标资源ID
	 * @param id2
	 *            小圆没选中图标资源ID
	 */
	public void init(int id1, int id2) {
		this.tagImageId_seleced = id1;
		this.tagImageId_nomorl = id2;
		tagImageLayout = new LinearLayout(context);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.setMargins(0, 0, 15, marginButtom);
		tagImageLayout.setLayoutParams(params);
		this.addView(tagImageLayout);
		tagImageLayout.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
		tagImageLayout.setBottom(marginButtom);
		viewPager = new ViewPager(context);
		viewPager.setOnPageChangeListener(this);
		viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		this.addView(viewPager);
	}

	/**
	 * 
	 * @param id1
	 *            小圆选中后图标资源ID
	 * @param id2
	 *            小圆没选中中图标资源ID
	 * @param size
	 *            小图标大小
	 * @param imageMargin
	 *            小图标间距
	 * @param gravity
	 *            小图标位置 1代表viewPager上面，2代表viewPager下面;
	 * @param layoutMargin
	 *            小图标距离父控件margin,如果在上面则代表距离上边框距离，如果在下面则代表距离下边框的距离
	 */
	public void init(int id1, int id2, int size, int imageMargin, int gravity,
			int layoutMargin) {
		this.tagImageId_seleced = id1;
		this.tagImageId_nomorl = id2;
		this.margin = imageMargin;
		this.size = size;
		this.marginButtom = layoutMargin;
		tagImageLayout = new LinearLayout(context);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		if (gravity == 2) {
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params.setMargins(0, 0, marginButtom, marginButtom);
		} else {
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.setMargins(0, marginButtom, 0, 0);
		}
		tagImageLayout.setLayoutParams(params);
		this.addView(tagImageLayout);
		viewPager = new ViewPager(context);
		viewPager.setOnPageChangeListener(this);
		viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		this.addView(viewPager);
	}

	/**
	 * @param adapter
	 *            viewpager适配器
	 * @param count
	 *            viewpager实际item数量,一般是list.size();
	 */
	public void setAdapter(List<AdvertisementInfoEntity> list,BitmapUtils bitmapUtils) {

		this.bitmapUtils = bitmapUtils;
		bitmapUtils.configDefaultLoadingImage(R.drawable.banner_default);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.banner_default);// 加载失败图片
		this.entityList = list;
		initData(list);
		this.count = list.size();
		viewPager.setAdapter(new MyPagerAdapter());
		initTagImage(this.count);
		if (isAutoNext) {
			hd.postDelayed(this, autoNextTime);
		}
	}
	
	private void initData(List<AdvertisementInfoEntity> Item) {

		list = new ArrayList<String>();
		for (int i = 0; i < Item.size(); i++) {
			list.add(Item.get(i).getRes_img());
		}
		
	}

	/**
	 * 
	 * @param count
	 *            viewpager实际数据数量
	 */
	public void notifyChanged(int count) {
		
		viewPager.getAdapter().notifyDataSetChanged();
		initTagImage(count);
	}

	/**
	 * 
	 * @param count2
	 *            viewpager实际数据数量
	 */
	private void initTagImage(int count2) {

		tagImageLayout.removeAllViews();
		imageList.clear();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size,
				size);
		params.setMargins(margin, 0, margin, 0);
		for (int i = 0; i < count2; i++) {
			ImageView view1 = new ImageView(context);
			view1.setLayoutParams(params);
			if (i == 0) {
				view1.setImageResource(tagImageId_seleced);
			} else {
				view1.setImageResource(tagImageId_nomorl);
			}
			view1.setScaleType(ScaleType.FIT_XY);
			imageList.add(view1);
			tagImageLayout.addView(view1);
		}
		tagImageLayout.bringToFront();
	}

	/**
	 * 
	 * @param isAutoNext
	 *            是否自动循环,默认为否
	 * @param time
	 *            间隔时间 ，默认5000毫秒
	 */
	public void setAutoNext(boolean isAutoNext, int time) {
		this.isAutoNext = isAutoNext;
		this.autoNextTime = time;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(final int position) {

		if (imageList.isEmpty()) {
			return;
		}
		if (!isAutoNext) {
			for (int i = 0; i < imageList.size(); i++) {
				if (i == position) {
					imageList.get(position).setImageResource(tagImageId_seleced);
				} else
					imageList.get(i).setImageResource(tagImageId_nomorl);
			}
		} else {
			imageList.get(currentItem).setImageResource(tagImageId_nomorl);
			currentItem = position % this.count;
			imageList.get(currentItem).setImageResource(tagImageId_seleced);
			
			hd.removeCallbacks(this);
			hd.postDelayed(this, autoNextTime);
		}

	}

	@Override
	public void run() {

		int count = viewPager.getCurrentItem();
		imageList.get(currentItem).setImageResource(tagImageId_nomorl);
		viewPager.setCurrentItem(++count);
		currentItem = viewPager.getCurrentItem() % this.count;
		imageList.get(currentItem).setImageResource(tagImageId_seleced);
		hd.postDelayed(this, autoNextTime);
	}

	public class MyPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			ImageView iv = new ImageView(context);
			String path = list.get(position % count);
			if (!TextUtils.isEmpty(path)) {
				bitmapUtils.display(iv, path);
			}
			
			iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(context, WebViewActivity.class);
					intent.putExtra("url", entityList.get(position % count).getRes_url());
					intent.putExtra("content", entityList.get(position % count).getRes_title());
					context.startActivity(intent);
				}
			});
			container.addView(iv);
			return iv;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {

			if (isAutoNext && count > 1) {
				return Integer.MAX_VALUE;
			} else
				return count;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
			object = null;
		}

	}

}
