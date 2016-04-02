package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.SongMenuInfoEntity;
import com.xiaoxu.music.community.view.custom_shape_imageview.RectangleImageView;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddSongMenuAdapter extends BaseAdapter {
	
	private Context context;
	private BitmapUtils bitmapUtils;
	private List<SongMenuInfoEntity> list;
	
	public AddSongMenuAdapter(Context context,List<SongMenuInfoEntity> list) {
		super();
		this.context = context;
		this.list = list;
		this.bitmapUtils = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		this.bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
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
		ViewHolderMusic holder = null;
		if(null == convertView){
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_songmenu_dialog_item, null);
			holder = new ViewHolderMusic();
			holder.iv = (RectangleImageView) convertView.findViewById(R.id.sm_img);
			holder.tv_name = (TextView) convertView.findViewById(R.id.sm_name);
			holder.tv_num =  (TextView) convertView.findViewById(R.id.sm_size);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolderMusic) convertView.getTag();
		}
		holder.tv_num.setVisibility(View.VISIBLE);
		
		SongMenuInfoEntity songMenuInfoEntity = list.get(position);
		bitmapUtils.display(holder.iv, songMenuInfoEntity.getMusiclist_img());
		holder.tv_name.setText(songMenuInfoEntity.getMusiclist_name());
		String num = songMenuInfoEntity.getC_music();
		holder.tv_num.setText((num!=null?num:"0")+"首");
		if(position == 0){
			holder.iv.setImageResource(R.drawable.bg_add_song_menu);
			holder.tv_num.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	class ViewHolderMusic {
		RectangleImageView iv;
		TextView tv_name,tv_num;
	}

}
