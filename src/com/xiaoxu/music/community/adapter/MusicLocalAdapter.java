package com.xiaoxu.music.community.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.entity.MusicLocalInfoEntity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MusicLocalAdapter extends YasiteAdapter {

	private List<MusicLocalInfoEntity> Item = new ArrayList<MusicLocalInfoEntity>();

	public MusicLocalAdapter(Context context) {
		super(context);
		setBitmapUtils();
	}

	public MusicLocalAdapter(Context context, List<MusicLocalInfoEntity> item) {
		super(context);
		Item = item;
		setBitmapUtils();
	}

	public List<MusicLocalInfoEntity> getItem() {
		return Item;
	}

	public void setItem(List<MusicLocalInfoEntity> item) {
		Item = item;
	}

	@Override
	public int getCount() {
		return Item != null ? Item.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return (MusicLocalInfoEntity) Item.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	protected void setupChildViews(View convertView, ViewHolder holder) {
		LocalMusicHolder musicHolder = (LocalMusicHolder) holder;
		musicHolder.image = (ImageView) convertView.findViewById(R.id.music_image);
		musicHolder.music_name = (TextView) convertView.findViewById(R.id.music_name);
		musicHolder.singer = (TextView) convertView.findViewById(R.id.singer);
		musicHolder.duration = (TextView) convertView.findViewById(R.id.duration);
		musicHolder.size = (TextView) convertView.findViewById(R.id.size);
		musicHolder.view = convertView.findViewById(R.id.view);
		musicHolder.rel_item = (RelativeLayout) convertView.findViewById(R.id.rel_item);
	}

	@Override
	protected void setChildViewData(ViewHolder holder, int position, Object obj) {

		final LocalMusicHolder musicHolder = (LocalMusicHolder) holder;
		if (obj instanceof MusicLocalInfoEntity) {
			MusicLocalInfoEntity entity = (MusicLocalInfoEntity) obj;
//			bitmapUtils.display(musicHolder.image, entity.getMusicurl());
			musicHolder.music_name.setText(entity.getFilename());
			musicHolder.singer.setText(entity.getSinger());
			musicHolder.duration.setText("时长:"+entity.getDuration());
			musicHolder.size.setText("大小:"+entity.getSize());
		}
	}

	@Override
	protected ViewHolder setHolder() {
		return new LocalMusicHolder();
	}

	class LocalMusicHolder extends ViewHolder {
		RelativeLayout rel_item;
		ImageView image;
		TextView music_name;
		TextView singer;
		TextView duration;
		TextView size;
		View view;
	}

	@Override
	protected void setLayoutResource(int position) {
		layoutId = R.layout.adapter_local_item;
	}

}
