package com.xiaoxu.music.community.adapter;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class YasiteAdapter extends BaseAdapter {
	
	protected int layoutId;
	protected Context context;
	protected BitmapUtils bitmapUtils;
	
	
	public YasiteAdapter(Context context){
		this.context = context;
	}
	
	protected void setBitmapUtils(){
		
		bitmapUtils = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder mViewHolder;
		if(null == convertView){
			mViewHolder = setHolder();
			setLayoutResource(position);
			convertView = LayoutInflater.from(context).inflate(layoutId, null);
			this.setupChildViews(convertView, mViewHolder);
			convertView.setTag(mViewHolder);
		} else{
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		setChildViewData(mViewHolder, position, getItem(position));
		return convertView;
	}
	
	protected abstract void setupChildViews(View convertView,ViewHolder holder);
	
	protected abstract void setChildViewData(ViewHolder holder,int position,Object obj);
	
	protected abstract class ViewHolder{};
	
	protected abstract ViewHolder setHolder();
	
	protected abstract void setLayoutResource(int position);

}