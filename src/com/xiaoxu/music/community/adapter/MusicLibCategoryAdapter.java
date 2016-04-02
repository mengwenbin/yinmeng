package com.xiaoxu.music.community.adapter;

import java.util.List;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.MusicListActivity;
import com.xiaoxu.music.community.entity.SongMenuInfoEntity;
import com.xiaoxu.music.community.util.ImageUtils;
import com.xiaoxu.music.community.util.StringUtil;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MusicLibCategoryAdapter extends RecyclerView.Adapter<MusicLibCategoryAdapter.MyViewHolder>{
	
	private Context context;
	private List<SongMenuInfoEntity> list;
	private BitmapUtils bitmapUtils;
	private LayoutInflater inflater;
	
	public MusicLibCategoryAdapter(Context context,
			List<SongMenuInfoEntity> list, BitmapUtils bitmapUtils) {
		super();
		this.context = context;
		this.list = list;
		this.bitmapUtils = bitmapUtils;
		inflater = LayoutInflater.from(context);
		this.bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		this.bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
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
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int viewType) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.adapter_music_category_item, arg0, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		// TODO Auto-generated method stub
		holder.iv.setClickable(true);
		int size = list.size()-1;
		if(position == 0){
			holder.iv.setImageResource(R.drawable.bg_music_category);
			holder.iv.setClickable(false);
		}else if(position == size){
			holder.iv.setImageResource(R.drawable.bg_music_category_botton);
			holder.iv.setClickable(false);
		}else{
			bitmapUtils.display(holder.iv, StringUtil.replaceImagePath(list.get(position).getMusiclist_img(), 300),new BitmapLoadCallBack<ImageView>() {
				@Override
				public void onLoadCompleted(ImageView arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
					// TODO Auto-generated method stub
					arg0.setImageBitmap(ImageUtils.getRoundedCornerBitmap(arg2, 8));
				}
				@Override
				public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
					arg0.setImageResource(R.drawable.test_default_wait_img);
				}
			});
			holder.iv.setOnClickListener(new Click(position));
		}
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView iv;
		public MyViewHolder(View view) {
			super(view);
			iv = (ImageView) view.findViewById(R.id.music_lib_category_image);
		}
	}
	
	public class Click implements OnClickListener {
		private int position;
		private Intent in;
		public Click(int position) {
			super();
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(position>0&&position<list.size()){
				in = new Intent(context, MusicListActivity.class);
				in.putExtra("musiclist_id", list.get(position).getMusiclist_id());
				in.putExtra("musiclist_name", list.get(position).getMusiclist_name());
				context.startActivity(in);
			}
		}
	}

}
