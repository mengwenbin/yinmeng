package com.xiaoxu.music.community.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.MusicUserInfoEntity;
import com.xiaoxu.music.community.util.StringUtil;

public class MineFansAdapter extends BaseAdapter {

	private Context context;
	private ViewHolder viewholder = null;
	private List<MusicUserInfoEntity> list;
	private BitmapUtils bitmapUtils;

	private void setBitmapUtils() {

		bitmapUtils = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片

	}

	public MineFansAdapter(Context context,List<MusicUserInfoEntity> list) {
		super();
		this.context = context;
		this.list = list;
		setBitmapUtils();
	}

	public List<MusicUserInfoEntity> getList() {
		return list;
	}

	public void setList(List<MusicUserInfoEntity> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			
			viewholder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_mineattention, null);
			viewholder.head_bg = (ImageView) convertView.findViewById(R.id.head_bg);
			viewholder.image_view = (ImageView) convertView.findViewById(R.id.author_head);
			viewholder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		viewholder.head_bg.setVisibility(View.GONE);
		bitmapUtils.display(viewholder.image_view, StringUtil.replaceImagePath(list.get(position).getUser_img(), 100));
		viewholder.tv_name.setText(list.get(position).getUser_nick());
		return convertView;
	}

	class ViewHolder {
		TextView tv_name;
		ImageView head_bg;
		ImageView image_view;
	}
}
