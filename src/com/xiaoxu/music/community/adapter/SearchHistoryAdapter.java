package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.SearchActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class SearchHistoryAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> list;
	private SearchActivity act;
	
	public SearchHistoryAdapter(SearchActivity act,List<String> list) {
		super();
		this.context = act;
		this.act = act;
		this.list = list;
	}
	
	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
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
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_search_history, null);
			viewHolder.tv_history = (TextView) convertView.findViewById(R.id.tv_history);
			viewHolder.btn_delete = (ImageButton) convertView.findViewById(R.id.btn_delete);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_history.setText(list.get(position));
		viewHolder.tv_history.setTag(list.get(position));
		viewHolder.tv_history.setOnClickListener(l);
		viewHolder.btn_delete.setTag(position);
		viewHolder.btn_delete.setOnClickListener(l);
		return convertView;
	}
	
	OnClickListener l = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tv_history:
				act.Search((String)v.getTag());
				break;
			case R.id.btn_delete:
				int position = (Integer) v.getTag();
				list.remove(position);
				act.setHistory(list);
				break;
			default:
				break;
			}
		}
	};
	
	class ViewHolder {
		TextView tv_history;
		ImageButton btn_delete;
	}
	
}
