package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.PlayVideoActivity;
import com.xiaoxu.music.community.activity.VideoPlayActivity;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.AdvertisementInfoEntity;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.view.custom_shape_imageview.RectangleImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.NrViewHolder>{
	
	private Context context;
	private BitmapUtils bitmapUtils;
	private LayoutInflater inflater;
	private List<AdvertisementInfoEntity> list;
    
	public List<AdvertisementInfoEntity> getList() {
		return list;
	}

	public void setList(List<AdvertisementInfoEntity> list) {
		this.list = list;
	}
	
	
	public VideoAdapter(Context context,List<AdvertisementInfoEntity> list) {
		
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		setBitmapUtils();
	}
	
	private void setBitmapUtils(){
		
		bitmapUtils = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
		
	}
	

	@Override
	public int getItemCount() {
		
		return list != null ? list.size() : 0;
	}


	@Override
	public void onBindViewHolder(NrViewHolder holder, int position) {
		
		AdvertisementInfoEntity advertisementInfoEntity = list.get(position);
		bitmapUtils.display(holder.image_iv,advertisementInfoEntity.getRes_img());
		holder.video_name.setText(advertisementInfoEntity.getRes_title());
		//将创建的View注册点击事件
		holder.rel_video.setOnClickListener(new ClickListener(position));
	}


	@Override
	public NrViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		
		 View view = inflater.inflate(R.layout.adapter_videoitem, viewGroup, false);
		 NrViewHolder holder = new NrViewHolder(view);
		 holder.rel_video = (RelativeLayout) view.findViewById(R.id.rel_video);
		 holder.image_iv = (RectangleImageView) view.findViewById(R.id.image_view);
		 holder.video_name = (TextView) view.findViewById(R.id.video_name);
		 holder.image_iv.setRoundedCorner(true);
		 holder.image_iv.setRoundPx(ActivityUtil.dip2px(context, 6));
	     return holder;
	}
	//自定义的ViewHolder，持有每个Item的的所有界面元素
	class NrViewHolder extends RecyclerView.ViewHolder {
		TextView video_name;
		RectangleImageView image_iv;
		RelativeLayout rel_video;
		public NrViewHolder(View view){
			super(view);
		}
	}
	
	class ClickListener implements OnClickListener{
		
		private int position;
		
		public ClickListener(int position){
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			Intent intent;
			AdvertisementInfoEntity entity = list.get(position);
			if(entity != null) {
//				if(entity.getRes_url().endsWith(".mp4")) {
//					intent = new Intent(context, PlayVideoActivity.class);
//					intent.putExtra("video_url", entity.getRes_url());
//					context.startActivity(intent);
//				}else{
					intent = new Intent(context, VideoPlayActivity.class);
					intent.putExtra("content", entity.getRes_title());
					intent.putExtra("url", entity.getRes_url());
					context.startActivity(intent);
//				}
			}
		}
	}

}
