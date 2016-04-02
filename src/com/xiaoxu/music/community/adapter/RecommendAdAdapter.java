package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.AdvertisementInfoEntity;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.StringUtil;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class RecommendAdAdapter extends BaseAdapter{
	
	private Context context;
	private List<AdvertisementInfoEntity> list;
	private BitmapUtils bitUtil;
	private int screenWidth = 1080;
	
	public RecommendAdAdapter(Context context) {
		super();
		this.context = context;
		this.bitUtil = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		this.bitUtil.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		this.bitUtil.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		this.bitUtil.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
		this.screenWidth = ActivityUtil.getScreenWidth(context);
	}

	public List<AdvertisementInfoEntity> getList() {
		return list;
	}

	public void setList(List<AdvertisementInfoEntity> list) {
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
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_recommend_ad, null);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_head);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		bitUtil.display(holder.iv,list.get(position).getRes_img());
		return convertView;
	}
	
	class ViewHolder{
		ImageView iv;
	}
	
}
