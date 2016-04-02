package com.xiaoxu.music.community.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.xiaoxu.music.community.BaseNewAdapter;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.activity.MusicUserDetailActivity;
import com.xiaoxu.music.community.activity.StartActivity;
import com.xiaoxu.music.community.adapter.MusicListAdapter2.CallBack;
import com.xiaoxu.music.community.adapter.MusicListAdapter2.OnClick;
import com.xiaoxu.music.community.adapter.MusicListAdapter2.OnlyWifi;
import com.xiaoxu.music.community.adapter.MusicListAdapter2.ViewHolderMusic;
import com.xiaoxu.music.community.adapter.MusicListAdapter2.ViewHolderSongMenu;
import com.xiaoxu.music.community.adapter.MusicListAdapter2.onClickPlay;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.SimpleUserEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.CollectMusicTask;
import com.xiaoxu.music.community.model.task.CollectSongMenuTask;
import com.xiaoxu.music.community.service.DownLoadService;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.NetUtils;
import com.xiaoxu.music.community.util.SharedPreUtils;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.AddSongMenuPop;
import com.xiaoxu.music.community.view.CustomOnlyWifiDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MusicUserSongsAdapter extends BaseNewAdapter{
	
	public final int PLAY_ALL = 0;
	public final int MUSIC_LIST = 1;
	private String list_size = "0";
	private Context context;
	private List<SongInfoEntity> list;
	//播放
	private int current = -1;
	private YmApplication app;
	//菜单栏
	private int currentBar = -1;//显示功能栏的那一条
	private boolean isShow = false;
	private ViewHolder holder;
	private MusicUserDetailActivity act;
	
	public MusicUserSongsAdapter(Context context, MusicUserDetailActivity activity, YmApplication app, List<SongInfoEntity> list) {
		super(context, activity);
		this.app = app;
		this.act = activity;
		this.context = context;
		this.list = list;
	}
	
	public String getList_size() {
		return list_size;
	}

	public void setList_size(String list_size) {
		this.list_size = list_size;
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
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return PLAY_ALL;
		} else {
			return MUSIC_LIST;
		}
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolderPlayAll holderPlayAll = null;
		ViewHolder holderMusicList = null;
		int type = getItemViewType(position);
		if(null == convertView){
			switch (type) {
			case PLAY_ALL:
				convertView = LayoutInflater.from(context).inflate(R.layout.adapter_musiclib_playall, null);
				holderPlayAll = new ViewHolderPlayAll();
				holderPlayAll.btn_playAll = (ImageButton) convertView.findViewById(R.id.btn_playall);
				holderPlayAll.songs_num = (TextView) convertView.findViewById(R.id.songs_num);
				convertView.setTag(holderPlayAll);
				break;
			case MUSIC_LIST:
				convertView = LayoutInflater.from(context).inflate(R.layout.adapter_musiclib_songs, null);
				holderMusicList = new ViewHolder();
				holderMusicList.layout_play = (RelativeLayout) convertView.findViewById(R.id.layout_play);
				holderMusicList.vline = convertView.findViewById(R.id.left_line);
				holderMusicList.music_name = (TextView) convertView.findViewById(R.id.tv_music_name);
				holderMusicList.music_author =  (TextView) convertView.findViewById(R.id.tv_music_author);
				holderMusicList.btn_more = (ImageButton) convertView.findViewById(R.id.btn_more);
				//功能栏
				holderMusicList.layout_menu = (LinearLayout) convertView.findViewById(R.id.layout_menu);
				holderMusicList.rel_collect = (RelativeLayout) convertView.findViewById(R.id.rel_collect);
				holderMusicList.rel_share = (RelativeLayout) convertView.findViewById(R.id.rel_share);
				holderMusicList.rel_download = (RelativeLayout) convertView.findViewById(R.id.rel_down);
				holderMusicList.rel_add = (RelativeLayout) convertView.findViewById(R.id.add_song);
				holderMusicList.iv_collect = (ImageView) convertView.findViewById(R.id.iv_collect);
				convertView.setTag(holderMusicList);
				break;
			default:
				break;
			}
		}else{
			switch (type) {
			case PLAY_ALL:
				holderPlayAll = (ViewHolderPlayAll) convertView.getTag();
				break;
			case MUSIC_LIST:
				holderMusicList = (ViewHolder) convertView.getTag();
				break;
			default:
				break;
			}
		}
		switch (type) {
		case PLAY_ALL:
			// playall number
			holderPlayAll.btn_playAll.setOnClickListener(new OnClick(0x103));
			list_size = list_size!=null?list_size:"0";
			holderPlayAll.songs_num.setText("播放全部(共"+ list_size +"首)");
			break;
		case MUSIC_LIST:
			//歌曲信息
			holderMusicList.music_name.setText(list.get(position).getMusic_name()+"");
			SimpleUserEntity author = list.get(position).getUser();
			if(author!=null){
				holderMusicList.music_author.setText(list.get(position).getUser().getUser_nick()+"");
			}
			//点击播放单曲
			holderMusicList.layout_play.setOnClickListener(new onClickPlay(position));
			//变色
			holderMusicList.vline.setBackgroundColor(context.getResources().getColor(R.color.white));
			holderMusicList.music_name.setTextColor(context.getResources().getColor(R.color.music_list_song_name));
			holderMusicList.music_author.setTextColor(context.getResources().getColor(R.color.music_list_song_author));
			if(position == current){
				holderMusicList.vline.setBackgroundColor(context.getResources().getColor(R.color.music_list_song_name_pressed));
				holderMusicList.music_name.setTextColor(context.getResources().getColor(R.color.music_list_song_name_pressed));
				holderMusicList.music_author.setTextColor(context.getResources().getColor(R.color.music_list_song_author_pressed));
			}
			//功能栏
			holderMusicList.btn_more.setBackgroundResource(R.drawable.btn_up);
			holderMusicList.btn_more.setOnClickListener(new OnClick(0x201, position, holderMusicList));
			holderMusicList.layout_menu.setVisibility(View.GONE);
			holderMusicList.iv_collect.setBackgroundResource(R.drawable.down_collectnormal);
			
			holderMusicList.rel_collect.setOnClickListener(new OnClick(0x202, position));
			holderMusicList.rel_share.setOnClickListener(new OnClick(0x203, position));
			holderMusicList.rel_download.setOnClickListener(new OnClick(0x204, position));
			holderMusicList.rel_add.setOnClickListener(new OnClick(0x205, position));
			
			String favorite = list.get(position).getFavorite();
			if(favorite!=null && Integer.parseInt(favorite) == 1){
				holderMusicList.iv_collect.setBackgroundResource(R.drawable.down_collect_press);
			}
			
			if(position == currentBar){
				holderMusicList.btn_more.setBackgroundResource(R.drawable.btn_down);
				holderMusicList.layout_menu.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
		
		return convertView;
	}
	
	class ViewHolderPlayAll {
		ImageButton btn_playAll;
		TextView songs_num;
	}
	
	class ViewHolder {
		RelativeLayout layout_play;
		View vline;
		TextView music_name,music_author;
		ImageButton btn_more;
		//功能栏
		LinearLayout layout_menu;
		RelativeLayout rel_collect,rel_share,rel_download,rel_add;
		ImageView iv_collect; 
	}
	
	public class OnClick implements OnClickListener {
		private int type;//
		private int position = 0;
		private ViewHolder holderMusic;
		
		public OnClick(int type) {
			super();
			this.type = type;
		}
		
		public OnClick(int type, int position) {
			super();
			this.type = type;
			this.position = position;
		}
		
		public OnClick(int type, int position, ViewHolder holderMusic){
			super();
			this.type = type;
			this.position = position;
			this.holderMusic = holderMusic;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (type) {
			case 0x103://播放全部
				canPlay(0);
				break;
			case 0x201://菜单栏控制
				if(position == currentBar&&isShow){
					currentBar = -1;
					isShow = false;
					holderMusic.layout_menu.setVisibility(View.GONE);
					holderMusic.btn_more.setBackgroundResource(R.drawable.btn_up);
					holder = null;
				}else{
					currentBar = position;
					isShow = true;
					if(holder!=null){
						holder.layout_menu.setVisibility(View.GONE);
						holder.btn_more.setBackgroundResource(R.drawable.btn_up);
					}
					holderMusic.layout_menu.setVisibility(View.VISIBLE);
					holderMusic.btn_more.setBackgroundResource(R.drawable.btn_down);
					holder = holderMusic;
				}
				break;
			case 0x202://collect
				if(AccountInfoUtils.getInstance(context).isLogin()){
					if(haveNetwork()){
						CollectMusicTask task = new CollectMusicTask(context, 
								list.get(position).getMusic_id(),new CallBack(position, 1, v));
						task.excute();
					}
				}else{
					act.login();
				}
				break;
			case 0x203://share
				shareMusic(list.get(position));
				break;
			case 0x204://download
				if(AccountInfoUtils.getInstance(context).isLogin()){
					Intent intent = new Intent(context, DownLoadService.class);
					intent.putExtra(DownLoadService.KEY_DOWNLOAD, list.get(position));
					context.startService(intent);
				}else{
					act.login();
				}
				break;
			case 0x205://add
				if(AccountInfoUtils.getInstance(context).isLogin()){
					AddSongMenuPop pop = new AddSongMenuPop(act, list.get(position).getMusic_id(), "");
					pop.showAtLocation(act.findViewById(R.id.main),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				}else{
					act.login();
				}
				break;
			default:
				break;
			}
		}
	}
	
	class CallBack extends BaseRequestCallBack {
		
		private int position;
		private View btn;
		
		public CallBack(int position,int type) {
			super();
			this.position = position;
		}
		
		public CallBack(int position,int type,View btn) {
			super();
			this.position = position;
			this.btn = btn;
		}
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			if(code != Constant.SUCCESS_REQUEST){
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			ActivityUtil.showCollectToast(context,alert);
			list.get(position).setFavorite("1");//收藏歌曲
			notifyDataSetChanged();
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {}
		
	}
	
	/*
	 * -------------------------- 播放 ----------------------------------------
	 */
	public class onClickPlay implements OnClickListener {
		private int position;
		public onClickPlay(int position) {
			super();
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			current = position;
			notifyDataSetChanged();
			canPlay(1);
		}
	}
	CustomOnlyWifiDialog dialog_only_wifi;
	public void canPlay(int type){//type == 1:单曲播放  type==0：播放全部
		int i = NetUtils.getAPNType(context);
		switch (i) {
		case -1://No NetWork
			ActivityUtil.showShortToast(context, context.getString(R.string.please_check_network));
			break;
		case 1://WIFI
			if(type == 1){
				app.addSingleMusic(list.get(current));
				app.playStart(list.get(current));
			}else{
				List<SongInfoEntity> lists = new ArrayList<SongInfoEntity>(); 
				lists.addAll(list);
				lists.remove(0);
				app.setSongMenu(lists);
				app.playStart(0);
			}
			break;
		default://流量WIFI
			if(!SharedPreUtils.isOnlyWifi(context.getApplicationContext())){
				if(type == 1){
					app.addSingleMusic(list.get(current));
					app.playStart(list.get(current));
				}else{
					List<SongInfoEntity> lists = new ArrayList<SongInfoEntity>(); 
					lists.addAll(list);
					lists.remove(0);
					app.setSongMenu(lists);
					app.playStart(0);
				}
				break;
			}
			dialog_only_wifi = CustomOnlyWifiDialog.show(context, new OnlyWifi(type));
			break;
		}
	}
	
	public class OnlyWifi implements OnClickListener {
		int type = 1;
		public OnlyWifi(int type) {
			super();
			this.type = type;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.ok:
				if(type == 1){
					app.addSingleMusic(list.get(current));
					app.playStart(list.get(current));
				}else{
					List<SongInfoEntity> lists = new ArrayList<SongInfoEntity>(); 
					lists.addAll(list);
					lists.remove(0);
					app.setSongMenu(lists);
					app.playStart(0);
				}
				SharedPreUtils.setOnlyWifi(context, false);
				dialog_only_wifi.disMiss(dialog_only_wifi);
				break;
			case R.id.cancle:
				dialog_only_wifi.disMiss(dialog_only_wifi);
				break;
			default:
				break;
			}
		}
	}

}
