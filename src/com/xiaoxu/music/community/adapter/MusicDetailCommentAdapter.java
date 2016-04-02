package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.MusicUserSongsAdapter.ViewHolder;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.MusicCommentInfoEntity;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.custom_shape_imageview.CircleImageView;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicDetailCommentAdapter extends BaseAdapter {
	
	private Context context;
	private List<MusicCommentInfoEntity> list;
	private BitmapUtils bitmapUtils;
	
	public MusicDetailCommentAdapter(Context context) {
		super();
		this.context = context;
		this.bitmapUtils = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		this.bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		this.bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_head_bg_blue);// 默认背景图片
		this.bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_head_bg_blue);// 加载失败图片
	}
	
	public List<MusicCommentInfoEntity> getList() {
		return list;
	}
	
	public void setList(List<MusicCommentInfoEntity> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_music_comment_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.author_name);
			holder.replease_time = (TextView) convertView.findViewById(R.id.release_time);
			holder.tv_body = (TextView) convertView.findViewById(R.id.post_body);
			holder.head = (CircleImageView) convertView.findViewById(R.id.author_head);
			holder.iv_sex =  (ImageView) convertView.findViewById(R.id.author_sex);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		bitmapUtils.display(holder.head, StringUtil.replaceImagePath(list.get(position).getUser().getUser_img(), 100));
		holder.name.setText(list.get(position).getUser().getUser_nick());
		String release_time = TimeUtil.timeToString(list.get(position).getTime_create());
		holder.replease_time.setText(release_time);
		holder.tv_body.setText(list.get(position).getComment_content()+"");
		int sex = Integer.parseInt(list.get(position).getUser().getUser_sex());
		switch (sex) {
			case 0: holder.iv_sex.setImageResource(R.drawable.sex_martian); break;
			case 1: holder.iv_sex.setImageResource(R.drawable.sex_man); break;
			case 2: holder.iv_sex.setImageResource(R.drawable.sex_woman); break;
			default: break;
		}
		return convertView;
	}
	
	class ViewHolder{
		CircleImageView head;
		TextView name, replease_time, tv_body;
		ImageView iv_sex;
	}

}
