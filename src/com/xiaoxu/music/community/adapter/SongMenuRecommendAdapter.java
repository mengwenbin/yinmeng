package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.MusicListActivity;
import com.xiaoxu.music.community.adapter.MusicLibCategoryAdapter.MyViewHolder;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.SongMenuInfoEntity;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.view.custom_shape_imageview.RectangleImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SongMenuRecommendAdapter extends RecyclerView.Adapter<SongMenuRecommendAdapter.MyViewHolder>{
	
	private Context context;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;
	private List<SongMenuInfoEntity> list;
	
	public SongMenuRecommendAdapter(Context context,
			List<SongMenuInfoEntity> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		bitmapUtils = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
	}
	
	public List<SongMenuInfoEntity> getList() {
		return list;
	}

	public void setList(List<SongMenuInfoEntity> list) {
		this.list = list;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return list != null ? list.size() : 0;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.adapter_recommend_songmenu_item, arg0, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}
	
	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		// TODO Auto-generated method stub
		bitmapUtils.display(holder.image, StringUtil.replaceImagePath(list.get(position).getMusiclist_img(), 300));
		holder.tv.setText(list.get(position).getMusiclist_name());
		holder.layout.setTag(position);
		holder.layout.setOnClickListener(l);
	}
	
	OnClickListener l = new OnClickListener() {
		int position;
		Intent in;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			position = (Integer) v.getTag();
			in = new Intent(context, MusicListActivity.class);
			in.putExtra("musiclist_id", list.get(position).getMusiclist_id());
			in.putExtra("musiclist_name", list.get(position).getMusiclist_name());
			context.startActivity(in);
		}
	};
	
	/**
	 * 
	 */
	
	//自定义的ViewHolder，持有每个Item的的所有界面元素
	class MyViewHolder extends RecyclerView.ViewHolder {
		private RelativeLayout layout;
		private RectangleImageView image;
		private TextView tv;
		public MyViewHolder(View view) {
			super(view);
			layout = (RelativeLayout) view.findViewById(R.id.rel_recommend);
			image = (RectangleImageView) view.findViewById(R.id.image_view);
			tv = (TextView) view.findViewById(R.id.tv_summary);
			image.setRoundedCorner(true);
			image.setRoundPx(ActivityUtil.dip2px(context, 6));
		}
	}
	
}
