package com.xiaoxu.music.community.adapter;

import com.xiaoxu.music.community.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SortListSelectTimeAdapter extends BaseAdapter {
	
	private Context context;
	private String suffix = "周";
	private int nowTime;
	private int startTime;
	private int select = -1;
	
	public SortListSelectTimeAdapter(Context context, int nowTime, int startTime, String suffix) {
		super();
		this.context = context;
		this.nowTime = nowTime;
		this.startTime = startTime;
		this.suffix = suffix;
	}
	
	public int getSelect() {
		return select;
	}
	
	public void setSelect(int select) {
		this.select = select;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Math.abs(nowTime-startTime)+1;
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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
		if(null == convertView){
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_sortlist_select_time, null);
			holder = new ViewHolder();
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		int time = nowTime-position;
		holder.tv_time.setText(time+suffix);
		holder.tv_time.setBackgroundColor(context.getResources().getColor(R.color.white));
		if(time == select){
			holder.tv_time.setBackgroundColor(context.getResources().getColor(R.color.fm_mine_bg));
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_time;
	}

}
