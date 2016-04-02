package com.xiaoxu.music.community.activity;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.entity.BlogsInfoEntity;
import com.xiaoxu.music.community.entity.ImageEntity;
import com.xiaoxu.music.community.view.CustomViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageActivity extends BaseNewActivity {

	private CustomViewPager mViewPager;
	private List<ImageEntity> list_images;
	private TextView center_content;
	private ImageView left_btn;
	private Intent intent;
	private RelativeLayout layout_title;
	private final String mPageName = "ImageActivity";

	@Override
	public void setContent() {
		setContentView(R.layout.activity_image);
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
		bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
		
		// 获取数据
		intent = getIntent();
		BlogsInfoEntity entity = (BlogsInfoEntity) intent.getSerializableExtra("entity");
		list_images = entity.getImg_attachment();
		int position = intent.getIntExtra("position", 0);
		left_btn = (ImageView) findViewById(R.id.left_btn);
		left_btn.setOnClickListener(l);
		center_content = (TextView) findViewById(R.id.center_content);
		center_content.setText(String.valueOf(position + 1) + "/"+ String.valueOf(list_images.size()));
		mViewPager = (CustomViewPager) findViewById(R.id.pager);
		mViewPager.setScanScroll(true);
		SamplePagerAdapter adapter = new SamplePagerAdapter();// 声明适配器
		mViewPager.setAdapter(adapter);// 绑定
		mViewPager.setCurrentItem(position);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			// 页面更变
			@Override
			public void onPageSelected(int position) {
				center_content.setText(String.valueOf(position + 1) + "/"+ String.valueOf(list_images.size()));
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}
		});
	}

	@Override
	public void setModel() {
		// TODO Auto-generated method stub
	}
	
	OnClickListener l = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	class SamplePagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;

		SamplePagerAdapter() {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return list_images.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View imageLayout = inflater.inflate(R.layout.adapter_image_item,container, false);
			PhotoView photoView = (PhotoView) imageLayout
					.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);
			String imgUrl = list_images.get(position).getAttachment();
			bitmapUtils.display(photoView, imgUrl,new BitmapLoadCallBack<PhotoView>() {
						
				@Override
				public void onLoadCompleted(PhotoView arg0,
						String arg1, Bitmap arg2,
						BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
					// TODO Auto-generated method stub
					spinner.setVisibility(View.GONE);
					arg0.setImageBitmap(arg2);
				}

				@Override
				public void onLoadFailed(PhotoView arg0, String arg1,
						Drawable arg2) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onLoadStarted(PhotoView container,
						String uri, BitmapDisplayConfig config) {
					// TODO Auto-generated method stub
					spinner.setVisibility(View.VISIBLE);
					super.onLoadStarted(container, uri, config);
				}

			});
			((ViewPager) container).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(null!=list_images&&list_images.size()>0){
			for(ImageEntity entity:list_images){
				bitmapUtils.clearMemoryCache(entity.getAttachment());
			}
		}
		super.onDestroy();
	}

}
