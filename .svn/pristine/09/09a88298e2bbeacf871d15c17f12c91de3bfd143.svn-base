package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.entity.ImageEntity;
import com.xiaoxu.music.community.util.StringUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ReleasePostImageAdapter extends BaseAdapter {

	private Context context;
	private List<ImageEntity> list;
	private BitmapUtils bitUtils;

	public ReleasePostImageAdapter(Context context,BitmapUtils bitUtils, List<ImageEntity> list) {
		super();
		this.context = context;
		this.bitUtils = bitUtils;
		this.list = list;
	}

	public List<ImageEntity> getList() {
		return list;
	}

	public void setList(List<ImageEntity> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list != null ? list.size() : 1;
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.adapter_release_post_image, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image_view);
			holder.btn = (ImageButton) convertView.findViewById(R.id.btn_cancle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.btn.setOnClickListener(new OnClick(position,holder.btn));
		if(Integer.parseInt(list.get(position).getA_id()) == -1){
			holder.btn.setVisibility(View.GONE);
			holder.image.setImageResource(R.drawable.bg_add_image);
		}else{
			holder.btn.setVisibility(View.VISIBLE);
			// 加载本地图片(路径以/开头， 绝对路径)
			bitUtils.display(holder.image, list.get(position).getAttachment());
		}
		return convertView;
	}

	class OnClick implements OnClickListener {

		private int position;
		private ImageButton btn;
		
		public OnClick(int position, ImageButton btn) {
			super();
			this.position = position;
			this.btn = btn;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			list.remove(position);
			notifyDataSetChanged();
		}
	}
	
	class ViewHolder {
		ImageView image;
		ImageButton btn;
	}

}
