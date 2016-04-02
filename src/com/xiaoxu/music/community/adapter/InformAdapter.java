package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.InfromInfoEntity;
import com.xiaoxu.music.community.util.TimeUtil;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InformAdapter extends BaseAdapter {

	private Context context;
	private List<InfromInfoEntity> list;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;
	
	public InformAdapter(Context context, List<InfromInfoEntity> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		setBitmapUtils();
	}
	
	private void setBitmapUtils(){
		
		bitmapUtils = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.logo);// 加载失败图片
		
	}
	
	public List<InfromInfoEntity> getList() {
		return list;
	}

	public void setList(List<InfromInfoEntity> list) {
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
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_infrom, null);
			viewHolder.iv = (ImageView) convertView.findViewById(R.id.circle_iv);
			viewHolder.version_name = (TextView) convertView.findViewById(R.id.version_name);
			viewHolder.version_suggest = (TextView) convertView.findViewById(R.id.version_suggest);
			viewHolder.version_date = (TextView) convertView.findViewById(R.id.version_date);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		bitmapUtils.display(viewHolder.iv, list.get(position).getM_from());
		viewHolder.version_name.setText(list.get(position).getM_subject());
		viewHolder.version_suggest.setText(list.get(position).getM_message());
		viewHolder.version_date.setText(TimeUtil.timeToString(list.get(position).getTime_create()));
		return convertView;
	}
	
	class ViewHolder {
		ImageView iv;
		TextView version_name;
		TextView version_date;
		TextView version_suggest;
	}

}
