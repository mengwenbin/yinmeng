package com.xiaoxu.music.community.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.activity.MusicDetailActivity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.util.TimeUtil;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OriginalAdapter extends BaseAdapter {

	private int REQUEST_CODE;
	private String list_size;
	private Activity activity;
	private YmApplication app;
	private List<SongInfoEntity> list;
	private LayoutInflater layoutInflater;

	public OriginalAdapter(Activity activity, YmApplication app,
			List<SongInfoEntity> list,int requestcode) {
		this.app = app;
		this.list = list;
		this.activity = activity;
		this.REQUEST_CODE = requestcode;
		layoutInflater = LayoutInflater.from(activity);
	}

	public String getList_size() {
		return list_size;
	}

	public void setList_size(String list_size) {
		this.list_size = list_size;
	}

	public List<SongInfoEntity> getList() {
		return list;
	}

	public void setList(List<SongInfoEntity> list) {
		this.list = list;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return (SongInfoEntity) list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		TextView music_name, music_date, musci_sum;
		ImageView play_all;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int type = getItemViewType(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {

			viewHolder = new ViewHolder();
			switch (type) {

			case 0:

				convertView = layoutInflater.inflate(R.layout.adapter_palyall_item, null);
				viewHolder.musci_sum = (TextView) convertView.findViewById(R.id.songs_num);
				viewHolder.play_all = (ImageView) convertView.findViewById(R.id.playall);

				break;
			case 1:

				convertView = layoutInflater.inflate(R.layout.adapter_original_item, null);
				viewHolder.music_name = (TextView) convertView.findViewById(R.id.music_name);
				viewHolder.music_date = (TextView) convertView.findViewById(R.id.music_date);

				break;

			}
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		switch (type) {
		case 0:
			viewHolder.musci_sum.setText("播放全部（共" + list_size + "首）");
			viewHolder.play_all.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						List<SongInfoEntity> lists = new ArrayList<SongInfoEntity>();
						if(list.size() > 0){
							lists.addAll(list);
						}
						lists.remove(0);
						app.setSongMenu(lists);
						app.playStart(0);
					}
				});
			break;

		case 1:

			viewHolder.music_name.setText(list.get(position).getMusic_name());
			viewHolder.music_date.setText(TimeUtil.timeToString(list.get(position).getTime_create()));
			convertView.setOnClickListener(new ClickListener(position));

			break;
		}
		return convertView;
	}
	
	private class ClickListener implements OnClickListener{

		private int position;
		
		public ClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			
			Intent intent = new Intent(activity, MusicDetailActivity.class);
			intent.putExtra("SongInfoEntity", list.get(position));
			activity.startActivityForResult(intent, REQUEST_CODE);
		}
		
	}

}
