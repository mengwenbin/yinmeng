package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.MusicDetailActivity;
import com.xiaoxu.music.community.adapter.SortListAdapter.ViewHolder;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.view.custom_shape_imageview.CircleImageView;
import com.xiaoxu.music.community.view.custom_shape_imageview.RectangleImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecommendMusicAdapter extends BaseAdapter {
	
	private Context context;
	private List<SongInfoEntity> list;
	private BitmapUtils bitUtil;
	
	public RecommendMusicAdapter(Context context, BitmapUtils bitUtil) { 
		super();
		this.context = context;
		this.bitUtil = bitUtil;
		this.bitUtil.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		this.bitUtil.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
	}
	
	public List<SongInfoEntity> getList() {
		return list;
	}

	public void setList(List<SongInfoEntity> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list != null ? list.size() : 0;
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_recommend_music_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.author = (TextView) convertView.findViewById(R.id.tv_author);
			holder.covering = (RectangleImageView) convertView.findViewById(R.id.iv_covering);
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout_item);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getMusic_name()+" ");
		holder.author.setText(list.get(position).getUser().getUser_nick()+" ");
		bitUtil.display(holder.covering, StringUtil.replaceImagePath(list.get(position).getMusic_coverimg(), 300));
		holder.layout.setOnClickListener(new Click(position));
		return convertView;
	}
	
	class ViewHolder {
		RelativeLayout layout;
		TextView name,author;
		RectangleImageView covering;
	}
	
	class Click implements OnClickListener{
		
		private int position;
		
		public Click(int position) {
			super();
			this.position = position;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent in = new Intent(context, MusicDetailActivity.class);
			in.putExtra("position", position);
			in.putExtra("SongInfoEntity", list.get(position));
			context.startActivity(in);
		}
	}

}
