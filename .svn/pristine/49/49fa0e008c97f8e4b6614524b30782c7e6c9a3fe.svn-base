package com.xiaoxu.music.community.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.entity.SingleEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.util.TimeUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SingleAdapter extends BaseAdapter {

	private int state = 0;
	private List<SingleEntity> list;
	private LayoutInflater layoutInflater;

	public SingleAdapter(Context context,List<SingleEntity> list) {
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<SingleEntity> getList() {
		return list;
	}

	public void setList(List<SingleEntity> list) {
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

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.adapter_song, null);
			viewHolder.music_name = (TextView) convertView.findViewById(R.id.music_name);
			viewHolder.music_date = (TextView) convertView.findViewById(R.id.music_date);
			viewHolder.check_box = (CheckBox) convertView.findViewById(R.id.check_box);
			viewHolder.btn_right = (ImageView) convertView.findViewById(R.id.right_btn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

			viewHolder.btn_right.setBackgroundResource(R.drawable.btn_right_gray);
			SongInfoEntity infoEntity = list.get(position).getMusic_info();
			if (infoEntity != null) {
				if(infoEntity.getFavorite() == null){
					infoEntity.setFavorite("0");
				}
				String music_name = infoEntity.getMusic_name();
				viewHolder.music_name.setText(music_name);
			} else {
				viewHolder.music_name.setText("该歌曲已经失效");
			}
		String time_create = list.get(position).getTime_create();
		if (time_create != null && !time_create.equals("")) {
			viewHolder.music_date.setText(TimeUtil.timeToString(time_create));
		} else {
			viewHolder.music_date.setText("<unknown>");
		}

		if (state == 1) {
			viewHolder.check_box.setVisibility(View.VISIBLE);
			viewHolder.check_box.setTag(position);
			viewHolder.check_box.setChecked(list.get(position).isCheck());
			viewHolder.check_box.setOnCheckedChangeListener(check_listener);
		} else if (state == 0) {
			viewHolder.check_box.setVisibility(View.GONE);
		}

		return convertView;
	}
	
	class ViewHolder {
		
		ImageView btn_right;
		TextView music_name, 
		music_date, musci_sum;
		CheckBox check_box;
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
	
	// 取消全选
		public void setCheckNO() {
			for (SingleEntity entity : list) {
				if(entity != null){
					entity.setCheck(false);;// 编辑取消状态
				}
			}
			notifyDataSetChanged();
		}
				
	// 全选
	public void setCheckAll() {
		for (SingleEntity entity : list) {
			entity.setCheck(true);// 编辑取消状态
		}
		notifyDataSetChanged();
	}
	
	// 得到要删除音乐
	public List<SingleEntity> getDeleteIDs() {
		
		List<SingleEntity> delList = new ArrayList<SingleEntity>();
		for (SingleEntity entity : list) {
			if (entity.isCheck()) {
				delList.add(entity);
			}
		}
		return delList;
	}
	
	public void delete(){
		
		List<SingleEntity> Item = getDeleteIDs();
		for (int i = 0; i < Item.size(); i++) {
			list.remove(Item.get(i));
		}
		notifyDataSetChanged();
		
	}
}
