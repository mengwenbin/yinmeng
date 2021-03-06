package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.MusicDetailActivity;
import com.xiaoxu.music.community.adapter.MusicUserSongsAdapter.ViewHolder;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.custom_shape_imageview.RectangleImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SortListAdapter extends BaseAdapter {
	
	private Context context;
	private List<SongInfoEntity> list;
	private BitmapUtils bitUtil;
	public SortListAdapter(Context context, List<SongInfoEntity> list) {
		super();
		this.context = context;
		this.list = list;
		this.bitUtil = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		this.bitUtil.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		this.bitUtil.configDefaultLoadingImage(R.drawable.default_convering);// 默认背景图片
		this.bitUtil.configDefaultLoadFailedImage(R.drawable.default_convering);// 加载失败图片
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
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_sort_list, null);
			holder.num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.author = (TextView) convertView.findViewById(R.id.tv_author);
			holder.time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.covering = (RectangleImageView) convertView.findViewById(R.id.iv_covering);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.num.setText(String.valueOf((position+1)));
		holder.num.setTextColor(context.getResources().getColor(R.color.sort_list_adapter_tv_name));
		if(position<9){
			holder.num.setText("0"+(position+1));
			if(position<3){
				holder.num.setTextColor(context.getResources().getColor(R.color.sort_list_adapter_tv_num));
			}
		}
		holder.name.setText(list.get(position).getMusic_name()+" ");
		holder.author.setText(list.get(position).getUser().getUser_nick()+" ");
		String time = list.get(position).getC_play();
		if(!TextUtils.isEmpty(time)){
			holder.time.setText("播放 "+list.get(position).getC_play()+"次");
		}
		bitUtil.display(holder.covering, StringUtil.replaceImagePath(list.get(position).getMusic_coverimg(), 300));
		return convertView;
	}
	
	class ViewHolder {
		TextView num,name,author,time;
		RectangleImageView covering;
	}
}
