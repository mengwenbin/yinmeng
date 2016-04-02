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
import com.xiaoxu.music.community.entity.SongMenuInfoEntity;
import com.xiaoxu.music.community.util.StringUtil;

public class MineSongAdapter extends BaseAdapter {

	private Context context;
	private List<SongMenuInfoEntity> list;
	private BitmapUtils bitmapUtils;

	private void setBitmapUtils() {

		bitmapUtils = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitmapUtils.configDefaultLoadingImage(R.drawable.default_convering);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_convering);// 加载失败图片

	}

	public MineSongAdapter(Context context,List<SongMenuInfoEntity> list) {
		super();
		this.context = context;
		this.list = list;
		setBitmapUtils();
	}

	public List<SongMenuInfoEntity> getList() {
		return list;
	}

	public void setList(List<SongMenuInfoEntity> list) {
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

		ViewHolder viewholder = null;
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_minesong, null);
			viewholder.image_view = (ImageView) convertView.findViewById(R.id.author_head);
			viewholder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			viewholder.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}

		bitmapUtils.display(viewholder.image_view, StringUtil.replaceImagePath(list.get(position).getMusiclist_img(), 300));
		viewholder.tv_name.setText(list.get(position).getMusiclist_name());
		viewholder.tv_sum.setText(list.get(position).getC_music()+"首");
		return convertView;
	}

	class ViewHolder {
		ImageView image_view;
		TextView tv_name;
		TextView tv_sum;
	}

}
