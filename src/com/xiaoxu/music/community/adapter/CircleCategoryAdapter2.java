package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.CircleBlogsActivity;
import com.xiaoxu.music.community.activity.MusicListActivity;
import com.xiaoxu.music.community.entity.CategorysInfoEntity;
import com.xiaoxu.music.community.util.ActivityUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CircleCategoryAdapter2 extends BaseAdapter {
	
	private Context context;
	private List<CategorysInfoEntity> list;
	private BitmapUtils bitmapUtils;
	
	public CircleCategoryAdapter2(Context context,BitmapUtils bitmapUtils) {
		super();
		this.context = context;
		this.bitmapUtils = bitmapUtils;
		this.bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		this.bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		this.bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
	}
	
	public List<CategorysInfoEntity> getList() {
		return list;
	}

	public void setList(List<CategorysInfoEntity> list) {
		if(list!=null && list.size()>0 && !"-1".equals(list.get(list.size()-1).getCategory_id())){
			list.add(list.size(), new CategorysInfoEntity("-1"));
		}
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list != null ? list.size() / 2 + list.size() % 2 : 0;
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
		int left = 2 * position;
		int right = left + 1;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_circle_category_second, null);
			holder = new ViewHolder();
			holder.left_img = (ImageView) convertView.findViewById(R.id.left_image);
			holder.right_img = (ImageView) convertView.findViewById(R.id.right_image);
			holder.left_name = (TextView) convertView.findViewById(R.id.left_name);
			holder.right_name = (TextView) convertView.findViewById(R.id.right_name);
			holder.left_blog_num = (TextView) convertView.findViewById(R.id.left_blog_num);
			holder.right_blog_num = (TextView) convertView.findViewById(R.id.right_blog_num);
			holder.left_reply_num = (TextView) convertView.findViewById(R.id.left_reply_num);
			holder.right_reply_num = (TextView) convertView.findViewById(R.id.right_reply_num);
			holder.left_layout = (RelativeLayout) convertView.findViewById(R.id.left);
			holder.right_layout = (RelativeLayout) convertView.findViewById(R.id.right);
			//重新绘制imageview的宽高
			int width = ActivityUtil.getWidth(holder.left_img);
			ActivityUtil.redrawViewSize(holder.left_img, width, width);
			ActivityUtil.redrawViewSize(holder.right_img, width, width);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.left_blog_num.setVisibility(View.VISIBLE);
		holder.left_reply_num.setVisibility(View.VISIBLE);
		holder.right_blog_num.setVisibility(View.VISIBLE);
		holder.right_reply_num.setVisibility(View.VISIBLE);
		
		if(!"-1".equals(list.get(left).getCategory_id())){
			bitmapUtils.display(holder.left_img, list.get(left).getCategory_img());
			holder.left_name.setText(list.get(left).getCategory_name()+"");
			holder.left_blog_num.setText(list.get(left).getCount_all()+"");
			holder.left_reply_num.setText(list.get(left).getCount_reply()+"");
			holder.right_layout.setVisibility(View.VISIBLE);
		}else{
			holder.left_img.setImageResource(R.drawable.bg_circle_more_content);
			holder.left_blog_num.setVisibility(View.INVISIBLE);
			holder.left_reply_num.setVisibility(View.INVISIBLE);
		}
		if (right <= list.size() - 1) {
			if(!"-1".equals(list.get(right).getCategory_id())){
				bitmapUtils.display(holder.right_img, list.get(right).getCategory_img());
				holder.right_name.setText(list.get(right).getCategory_name()+"");
				holder.right_blog_num.setText(list.get(right).getCount_all()+"");
				holder.right_reply_num.setText(list.get(right).getCount_reply()+"");
			}else{
				holder.right_img.setImageResource(R.drawable.bg_circle_more_content);
				holder.right_blog_num.setVisibility(View.INVISIBLE);
				holder.right_reply_num.setVisibility(View.INVISIBLE);
			}
		}else{
			holder.right_layout.setVisibility(View.INVISIBLE);
		}
		holder.left_layout.setOnClickListener(new Click(left));
		holder.right_layout.setOnClickListener(new Click(right));
		return convertView;
	}

	class ViewHolder{
		ImageView left_img,right_img;
		TextView left_name,right_name;
		TextView left_blog_num,right_blog_num;
		TextView left_reply_num,right_reply_num;
		RelativeLayout left_layout,right_layout;
	}
	
	public class Click implements OnClickListener {
		
		private int position;
		private Intent in;
		
		public Click(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!"-1".equals(list.get(position).getCategory_id())){
				in = new Intent(context, CircleBlogsActivity.class);
				in.putExtra("category", list.get(position));
				context.startActivity(in);
			}
		}
	}

	
}
