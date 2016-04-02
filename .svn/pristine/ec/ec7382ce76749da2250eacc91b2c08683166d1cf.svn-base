package com.xiaoxu.music.community.im.adapter;

import java.util.List;

import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.entity.MusicLocalInfoEntity;
import com.xiaoxu.music.community.im.activity.ChatActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicFileAdapter extends BaseAdapter {

	private String username;
	private Activity context;
	private LayoutInflater inflater;
	private List<MusicLocalInfoEntity> list;
	
	
	public MusicFileAdapter(Activity context, List<MusicLocalInfoEntity> list,String username) {
		super();
		this.context = context;
		this.list = list;
		this.username = username;
		inflater = LayoutInflater.from(context);
	}
	
	public List<MusicLocalInfoEntity> getList() {
		return list;
	}


	public void setList(List<MusicLocalInfoEntity> list) {
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
		
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_musicfile, null);
			holder.musicname = (TextView) convertView.findViewById(R.id.music_name);
			holder.music_author = (TextView) convertView.findViewById(R.id.singer);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.musicname.setText(list.get(position).getMusicname());
		holder.music_author.setText(list.get(position).getSinger());
		convertView.setOnClickListener(new ClickListener(position));
		return convertView;
	}
	
	class ViewHolder{
		TextView musicname,music_author;
	}
	
	private class ClickListener implements OnClickListener{

		private int position;
		
		public ClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, ChatActivity.class);
			intent.putExtra("userName", username);
			intent.putExtra("filepath", list.get(position).getFileurl());
			context.setResult(1, intent);
			context.finish();
		}
		
	}
}
