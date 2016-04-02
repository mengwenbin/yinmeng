package com.xiaoxu.music.community.adapter;

import java.util.ArrayList;
import java.util.List;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.entity.SongInfoEntity;
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

public class EditSongAdapter extends BaseAdapter {

	private ArrayList<SongInfoEntity> list;
	private LayoutInflater layoutInflater;

	public EditSongAdapter(Context context,ArrayList<SongInfoEntity> list) {
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
	}

	public ArrayList<SongInfoEntity> getList() {
		return list;
	}

	public void setList(ArrayList<SongInfoEntity> list) {
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
			viewHolder.music_author = (TextView) convertView.findViewById(R.id.music_date);
			viewHolder.right_btn = (ImageView) convertView.findViewById(R.id.right_btn);
			viewHolder.check_box = (CheckBox) convertView.findViewById(R.id.check_box);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

			viewHolder.check_box.setVisibility(View.VISIBLE);
			viewHolder.check_box.setTag(position);
			viewHolder.check_box.setChecked(list.get(position).isChecked());
			viewHolder.check_box.setOnCheckedChangeListener(check_listener);
			viewHolder.right_btn.setBackgroundResource(R.drawable.sort_icon);
			SongInfoEntity infoEntity = list.get(position);
			if(infoEntity.getFavorite() == null){
				infoEntity.setFavorite("0");
			}
			viewHolder.music_name.setText(infoEntity.getMusic_name());
			viewHolder.music_author.setText(list.get(position).getMusic_singing());

		return convertView;
	}
	
	class ViewHolder {
		TextView music_name, 
		music_author;
		CheckBox check_box;
		ImageView right_btn;
	}
	
	OnCheckedChangeListener check_listener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			int position = (Integer) buttonView.getTag();
			if(position>list.size()-1){
				return;
			}
			list.get(position).setChecked(isChecked);
			notifyDataSetChanged();
		}
	};
	
	// 取消全选
		public void setCheckNO() {
			for (SongInfoEntity entity : list) {
				entity.setChecked(false);;// 编辑取消状态
			}
			notifyDataSetChanged();
		}
				
	// 全选
	public void setCheckAll() {
		for (SongInfoEntity entity : list) {
			entity.setChecked(true);// 编辑取消状态
		}
		notifyDataSetChanged();
	}
	
	// 得到要删除音乐
	public List<SongInfoEntity> getDeleteIDs() {
		
		List<SongInfoEntity> delList = new ArrayList<SongInfoEntity>();
		for (SongInfoEntity entity : list) {
			if (entity.isChecked()) {
				delList.add(entity);
			}
		}
		return delList;
	}
	
	public void delete(){
		
		List<SongInfoEntity> Item = getDeleteIDs();
		for (int i = 0; i < Item.size(); i++) {
			list.remove(Item.get(i));
		}
		notifyDataSetChanged();
		
	}

}
