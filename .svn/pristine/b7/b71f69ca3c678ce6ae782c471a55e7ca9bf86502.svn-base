package com.xiaoxu.music.community.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.SongMenuEntity;
import com.xiaoxu.music.community.entity.SongMenuInfoEntity;
import com.xiaoxu.music.community.util.TimeUtil;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SongMenuAdapter extends BaseAdapter {

	private int state = 0;
	private Context context;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;
	private List<SongMenuEntity> list;

	public SongMenuAdapter(Context context, List<SongMenuEntity> list) {
		this.list = list;
		this.context = context;
		setBitmapUtils();
		inflater = LayoutInflater.from(context);
	}
	
	private void setBitmapUtils(){
		
		bitmapUtils = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
		
	}


	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<SongMenuEntity> getList() {
		return list;
	}

	public void setList(List<SongMenuEntity> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return (SongMenuEntity) list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder menuHolder = null;
		if(convertView == null){
			
			menuHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_songmenu_item, null);
			menuHolder.music_image = (ImageView) convertView.findViewById(R.id.music_image);
			menuHolder.music_name = (TextView) convertView.findViewById(R.id.music_name);
			menuHolder.music_date = (TextView) convertView.findViewById(R.id.music_date);
			menuHolder.right_btn = (ImageView) convertView.findViewById(R.id.right_btn);
			menuHolder.check_box = (CheckBox) convertView.findViewById(R.id.check_box);
			convertView.setTag(menuHolder);
		}else{
			menuHolder = (ViewHolder) convertView.getTag();
		}
		
		SongMenuEntity menuEntity = list.get(position);
		if(menuEntity.getMusiclist_info() != null){
			bitmapUtils.display(menuHolder.music_image, menuEntity.getMusiclist_info().getMusiclist_img());
			SongMenuInfoEntity entity = menuEntity.getMusiclist_info();
			if(entity != null){
				if(!TextUtils.isEmpty(entity.getMusiclist_name())){
					menuHolder.music_name.setText(entity.getMusiclist_name());
				}else{
					menuHolder.music_name.setText("该歌单已经失效");
				}
			}
		}
		String time_create = list.get(position).getTime_create();
		menuHolder.music_date.setText(TimeUtil.timeToString(time_create));
		
		// checkbox
		if (state == 1) {
			menuHolder.check_box.setVisibility(View.VISIBLE);
			menuHolder.check_box.setTag(position);
			menuHolder.check_box.setChecked(list.get(position).isCheck());
			menuHolder.check_box.setOnCheckedChangeListener(check_listener);
		} else if(state == 0){
			menuHolder.check_box.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	class ViewHolder {
		TextView music_name, music_date;
		ImageView music_image;
		ImageView right_btn;
		CheckBox check_box;
	}
	
	// 取消全选
		public void setCheckNO() {
			for (SongMenuEntity entity : list) {
				entity.setCheck(false);;// 编辑取消状态
			}
			notifyDataSetChanged();
		}
				
		// 全选
		public void setCheckAll() {
			for (SongMenuEntity entity : list) {
				entity.setCheck(true);// 编辑取消状态
			}
			notifyDataSetChanged();
		}
		
		OnCheckedChangeListener check_listener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int position = (Integer) buttonView.getTag();
				if(position>list.size()-1){
					return;
				}
				list.get(position).setCheck(isChecked);
				notifyDataSetChanged();
			}
		};
		
		// 得到要删除音乐
		public List<SongMenuEntity> getDeleteIDs() {
			List<SongMenuEntity> delList = new ArrayList<SongMenuEntity>();
			for (SongMenuEntity entity : list) {
				if (entity.isCheck()) {
					delList.add(entity);
				}
			}
			return delList;
		}
		
		public void delete(){
			
			List<SongMenuEntity> Item = getDeleteIDs();
			for (int i = 0; i < Item.size(); i++) {
				list.remove(Item.get(i));
			}
			notifyDataSetChanged();
		}
	
}
