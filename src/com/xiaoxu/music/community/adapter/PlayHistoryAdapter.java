package com.xiaoxu.music.community.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.activity.PlayActivity;
import com.xiaoxu.music.community.entity.SongInfoEntity;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayHistoryAdapter extends BaseAdapter {

	private List<SongInfoEntity> list;
	private Context context;
	private int current = -1;
	private YmApplication app;
	private Dialog dialog;
	private PlayActivity act;
	
	public PlayHistoryAdapter(Context context,YmApplication app,Dialog dialog, PlayActivity act) {
		super();
		this.context = context;
		this.app = app;
		this.dialog = dialog;
		this.act = act;
	}
	
	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public List<SongInfoEntity> getList() {
		return list;
	}

	public void setList(List<SongInfoEntity> list) {
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
		ViewHolderMusic holderMusicList = null;
		if(null == convertView){
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_play_history, null);
			holderMusicList = new ViewHolderMusic();
			holderMusicList.layout_play = (RelativeLayout) convertView.findViewById(R.id.layout_play);
			holderMusicList.music_num = (TextView) convertView.findViewById(R.id.tv_num);
			holderMusicList.music_name = (TextView) convertView.findViewById(R.id.tv_music_name);
			holderMusicList.music_author =  (TextView) convertView.findViewById(R.id.tv_music_author);
			holderMusicList.btn_delete = (ImageButton) convertView.findViewById(R.id.btn_delete);
			convertView.setTag(holderMusicList);
		}else{
			holderMusicList = (ViewHolderMusic) convertView.getTag();
		}
		holderMusicList.music_num.setText(position+"");
		if(position<10){
			holderMusicList.music_num.setText("0"+position);
		}
		holderMusicList.music_name.setText(list.get(position).getMusic_name());
		if(list.get(position).getUser()!=null)
			holderMusicList.music_author.setText("——"+list.get(position).getUser().getUser_nick());
		holderMusicList.btn_delete.setOnClickListener(new OnClick(0, position));
		holderMusicList.layout_play.setOnClickListener(new OnClick(1, position));
		
		holderMusicList.music_num.setTextColor(context.getResources().getColor(R.color.black));
		holderMusicList.music_name.setTextColor(context.getResources().getColor(R.color.black));
		holderMusicList.music_author.setTextColor(context.getResources().getColor(R.color.Gainsboro));
		if(position == current){
			holderMusicList.music_num.setTextColor(context.getResources().getColor(R.color.music_list_song_name_pressed));
			holderMusicList.music_name.setTextColor(context.getResources().getColor(R.color.music_list_song_name_pressed));
			holderMusicList.music_author.setTextColor(context.getResources().getColor(R.color.music_list_song_author_pressed));
		}
		return convertView;
	}
	
	
	class ViewHolderMusic {
		TextView music_num,music_name, music_author;
		ImageButton btn_delete;
		RelativeLayout layout_play;
	}
	
	public class OnClick implements OnClickListener {
		
		private int type = 0;
		private int position = 0;
		
		public OnClick(int type, int position) {
			super();
			this.type = type;
			this.position = position;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(type == 0){
				list.remove(position);
				if (current == position) {// 如果刚才删除的是(正在播放)的数据
					if (position == getCount()) {// 如果刚才删除的是最后一条(正在播放)的数据[注：删除最后一条后，删除的索引==现在数据的条数]
						current = current - 1;// 播放索引上移
					}
					app.playStart(current);
				} else if (position < current) {
					current = current - 1;
					app.setCurrent_index(current);
				}
				app.setSongMenu(list);
				notifyDataSetChanged();
			}else if(type == 1){
				dialog.dismiss();
				List<SongInfoEntity> list_his = new ArrayList<SongInfoEntity>();
				list_his.addAll(list);
				app.setSongMenu(list_his, -1, 2, "-1");
				app.playStart(position);
			}
		}
	}
	
}
