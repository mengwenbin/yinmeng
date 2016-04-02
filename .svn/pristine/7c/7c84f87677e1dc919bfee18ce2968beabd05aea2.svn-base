package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.CircleBlogDetailActivity;
import com.xiaoxu.music.community.entity.BlogsInfoEntity;
import com.xiaoxu.music.community.util.TimeUtil;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CircleBlogDetailChildAdapter extends BaseAdapter {
	
	private Context context;
	private List<BlogsInfoEntity> list;
	private CircleBlogDetailActivity act;
	private String parent_Tid;
	private boolean isMore = false;
	
	public CircleBlogDetailChildAdapter(Context context, CircleBlogDetailActivity act,
			List<BlogsInfoEntity> list, String parent_Tid) {
		super();
		this.context = context;
		this.list = list;
		this.act = act;
		this.parent_Tid = parent_Tid;
	}

	public void setMore(boolean isMore) {
		this.isMore = isMore;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list!=null&&list.size()>3 && !isMore){
			return 3;
		}
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
			convertView = LayoutInflater.from(context).inflate(R.layout.circle_blog_detail_child_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_content.setTag(position);
		holder.tv_content.setOnClickListener(listener);
		
		String release_time = "("+TimeUtil.getStandardDate(list.get(position).getTime_create())+")";
		String content = list.get(position).getBlog_author()+":"+list.get(position).getBlog_message()+release_time;
		
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.act_blog_detail_author_name));
		ForegroundColorSpan graySpan = new ForegroundColorSpan(context.getResources().getColor(R.color.act_blog_detail_release_time));
		builder.setSpan(blueSpan, 0, list.get(position).getBlog_author().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(graySpan, content.length()-release_time.length(), content.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		holder.tv_content.setText(builder);
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_content;
		public ViewHolder(View convertView) {
			super();
			tv_content = (TextView) convertView.findViewById(R.id.tv_blog);
		}
	}
	
	OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = (Integer) v.getTag();
			act.replyComment(parent_Tid,list.get(position).getTid(),list.get(position).getBlog_author(),list.get(position).getUser_id());
		}
	};
	

}
