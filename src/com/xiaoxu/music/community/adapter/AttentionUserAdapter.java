package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.MusicUserDetailActivity;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.MusicUserInfoEntity;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.view.custom_shape_imageview.CircleImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AttentionUserAdapter extends BaseAdapter {
	
	private Context context;
	private List<MusicUserInfoEntity> list;
	private BitmapUtils bitUtil;
	
	public AttentionUserAdapter(Context context) {
		super();
		this.context = context;
		this.bitUtil = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		this.bitUtil.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		this.bitUtil.configDefaultLoadingImage(R.drawable.test_default_head_bg_blue);// 默认背景图片
		this.bitUtil.configDefaultLoadFailedImage(R.drawable.test_default_head_bg_blue);// 加载失败图片
	}

	public List<MusicUserInfoEntity> getList() {
		return list;
	}

	public void setList(List<MusicUserInfoEntity> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list!=null?list.size():0;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_attention_user_item, null);
			holder = new ViewHolder();
			holder.head = (CircleImageView) convertView.findViewById(R.id.head_img);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		bitUtil.display(holder.head,StringUtil.replaceImagePath(list.get(position).getUser_img(),100));
		holder.tv_name.setText(list.get(position).getUser_nick()+"");
		convertView.setOnClickListener(new Click(position));
		return convertView;
	}
	
	class ViewHolder{
		CircleImageView head;
		TextView tv_name;
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
			Intent in = new Intent(context, MusicUserDetailActivity.class);
			in.putExtra("MusicUserInfoEntity", list.get(position));
			context.startActivity(in);
		}
	}

}