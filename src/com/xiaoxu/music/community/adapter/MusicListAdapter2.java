package com.xiaoxu.music.community.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.xiaoxu.music.community.BaseNewAdapter;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.activity.EditUserInfoActivity;
import com.xiaoxu.music.community.activity.MusicDetailActivity;
import com.xiaoxu.music.community.activity.MusicListActivity;
import com.xiaoxu.music.community.activity.MusicUserDetailActivity;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.SimpleUserEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.entity.SongMenuInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.AttentionPersonTask;
import com.xiaoxu.music.community.model.task.CollectMusicTask;
import com.xiaoxu.music.community.model.task.CollectSongMenuTask;
import com.xiaoxu.music.community.service.DownLoadService;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.NetUtils;
import com.xiaoxu.music.community.util.SharedPreUtils;
import com.xiaoxu.music.community.view.AddSongMenuPop;
import com.xiaoxu.music.community.view.CustomOnlyWifiDialog;

public class MusicListAdapter2 extends BaseNewAdapter {

	public final int SONG_MENU = 0;
	public final int MUSIC_LIST = 1;
	private SongMenuInfoEntity songmenu;
	private List<SongInfoEntity> list;
	private String list_size;
	private BitmapUtils bitmapUtils1,bitmapUtils2;
	private YmApplication app;
	private int current = -1;
	private String musiclist_id;
	private int pageNum;
	private MusicListActivity act;
	
	private ListView listview;
	private int currentBar = -1;//显示功能栏的那一条
	private ViewHolderMusic holder;
	private boolean isShow = false;
	
	public MusicListAdapter2(Context context,MusicListActivity activity, YmApplication app, String musiclist_id,int pageNum) {
		super(context, activity);
		this.app = app;
		this.musiclist_id = musiclist_id;
		this.pageNum = pageNum;
		this.act = activity;
		bitmapUtils1 = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		bitmapUtils2 = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		bitmapUtils1.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitmapUtils2.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitmapUtils1.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		bitmapUtils1.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
		bitmapUtils2.configDefaultLoadingImage(R.drawable.test_default_head_bg_blue);// 默认背景图片
		bitmapUtils2.configDefaultLoadFailedImage(R.drawable.test_default_head_bg_blue);// 加载失败图片
	}
	
	public void setListview(ListView listview) {
		this.listview = listview;
	}
	
	public SongMenuInfoEntity getSongmenu() {
		return songmenu;
	}
	
	public void setSongmenu(SongMenuInfoEntity songmenu) {
		this.songmenu = songmenu;
	}

	public List<SongInfoEntity> getList() {
		return list;
	}

	public void setList(List<SongInfoEntity> list) {
		this.list = list;
	}
	
	public String getList_size() {
		return list_size;
	}
	
	public void setList_size(String list_size) {
		this.list_size = list_size;
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
			return SONG_MENU;
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
		ViewHolderSongMenu holderSongMemu = null;
		ViewHolderMusic holderMusicList = null;
		int type = getItemViewType(position);
		if(null == convertView){
			switch (type) {
			case SONG_MENU:
				convertView = LayoutInflater.from(context).inflate(R.layout.adapter_musiclib_songmenu2, null);
				holderSongMemu = new ViewHolderSongMenu();
				holderSongMemu.songmenu_img = (ImageView) convertView.findViewById(R.id.song_cover);
				holderSongMemu.songmenu_tv = (TextView) convertView.findViewById(R.id.songmenu_name);
				holderSongMemu.btn_collect = (ImageButton) convertView.findViewById(R.id.collect_songmenu);
				holderSongMemu.btn_share = (ImageButton) convertView.findViewById(R.id.share_songmenu);
				
				holderSongMemu.rel_author = (RelativeLayout) convertView.findViewById(R.id.rel_user);
				holderSongMemu.author_head = (ImageView) convertView.findViewById(R.id.author_head);
				holderSongMemu.author_name = (TextView) convertView.findViewById(R.id.author_name);
				
				holderSongMemu.btn_playAll = (ImageButton) convertView.findViewById(R.id.btn_playall);
				holderSongMemu.songs_num = (TextView) convertView.findViewById(R.id.songs_num);
				convertView.setTag(holderSongMemu);
				break;
			case MUSIC_LIST:
				convertView = LayoutInflater.from(context).inflate(R.layout.adapter_musiclib_songs, null);
				holderMusicList = new ViewHolderMusic();
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
		} else {
			switch (type) {
			case SONG_MENU:
				holderSongMemu = (ViewHolderSongMenu) convertView.getTag();
				break;
			case MUSIC_LIST:
				holderMusicList = (ViewHolderMusic) convertView.getTag();
				break;
			default:
				break;
			}
		}
		switch (type) {
		case SONG_MENU:
			bitmapUtils1.display(holderSongMemu.songmenu_img,songmenu.getMusiclist_img()+"");
			holderSongMemu.songmenu_tv.setText(songmenu.getMusiclist_name()+"");
			//collect
			holderSongMemu.btn_collect.setOnClickListener(new OnClick(0x101));
			holderSongMemu.btn_collect.setBackgroundResource(R.drawable.btn_collect_middle_normal);
			if(Integer.parseInt(songmenu.getFavorite())==1){
				holderSongMemu.btn_collect.setBackgroundResource(R.drawable.btn_collect_middle_pressed);
			}
			//share songmenu
			holderSongMemu.btn_share.setOnClickListener(new OnClick(0x102));
			
			//head name
			SimpleUserEntity user = songmenu.getUser();
			bitmapUtils2.display(holderSongMemu.author_head, user.getUser_img()+"");
			holderSongMemu.author_name.setText(user.getUser_nick()+"");
			holderSongMemu.rel_author.setOnClickListener(new OnClick(0x104));
			// playall number
			holderSongMemu.btn_playAll.setOnClickListener(new OnClick(0x103));
			holderSongMemu.songs_num.setText("播放全部(共"+list_size+"首)");
			break;
		case MUSIC_LIST:
			holderMusicList.music_name.setText(list.get(position).getMusic_name()+"");
			SimpleUserEntity author = list.get(position).getUser();
			if(author!=null){
				holderMusicList.music_author.setText(list.get(position).getUser().getUser_nick()+"");
			}
			holderMusicList.layout_play.setOnClickListener(new onClickPlay(position));
			
			holderMusicList.vline.setBackgroundColor(context.getResources().getColor(R.color.white));
			holderMusicList.music_name.setTextColor(context.getResources().getColor(R.color.music_list_song_name));
			holderMusicList.music_author.setTextColor(context.getResources().getColor(R.color.music_list_song_author));
			if(position == current){
				holderMusicList.vline.setBackgroundColor(context.getResources().getColor(R.color.music_list_song_name_pressed));
				holderMusicList.music_name.setTextColor(context.getResources().getColor(R.color.music_list_song_name_pressed));
				holderMusicList.music_author.setTextColor(context.getResources().getColor(R.color.music_list_song_author_pressed));
			}
			//功能栏
			holderMusicList.btn_more.setBackgroundResource(R.drawable.btn_down);
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
				holderMusicList.btn_more.setBackgroundResource(R.drawable.btn_up);
				holderMusicList.layout_menu.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
		return convertView;
	}
	
	class ViewHolderSongMenu {
		ImageView songmenu_img, author_head;
		TextView songmenu_tv, author_name, songs_num;
		ImageButton btn_collect, btn_share, btn_playAll;
		RelativeLayout rel_author;
	}

	class ViewHolderMusic {
		View vline;
		TextView music_name, music_author;
		ImageButton btn_more;
		RelativeLayout layout_play, rel_collect, rel_share, rel_download, rel_add;
		ImageView iv_collect;
		LinearLayout layout_menu;
	}
	
	
	
	public class OnClick implements OnClickListener {
		private int type;//
		private int position = 0;
		private ViewHolderMusic holderMusic;
		public OnClick(int type) {
			super();
			this.type = type;
		}
		
		public OnClick(int type, int position) {
			super();
			this.type = type;
			this.position = position;
		}
		
		public OnClick(int type, int position, ViewHolderMusic holderMusic){
			super();
			this.type = type;
			this.position = position;
			this.holderMusic = holderMusic;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (type) {
			case 0x101://歌单收藏
				if(AccountInfoUtils.getInstance(context).isLogin()){
					if(haveNetwork()){
						CollectSongMenuTask task = new CollectSongMenuTask(context, songmenu.getMusiclist_id(),
								new CallBack(2,(ImageButton)v));
						task.excute();
					}
				}else{
					act.login();
				}
				break;
			case 0x102://share songmenu
				shareSongMenu(songmenu.getMusiclist_name(), songmenu.getMusiclist_img(), songmenu.getUser().getUser_nick());
				break;
			case 0x103://播放全部
				canPlay(0);
				break;
			case 0x104://音乐人
				Intent in = new Intent(context, MusicUserDetailActivity.class);
				in.putExtra("user_id", songmenu.getUser_id());
				context.startActivity(in);
				break;
			case 0x201://菜单栏控制
				if(position == currentBar&&isShow){
					currentBar = -1;
					isShow = false;
					holderMusic.layout_menu.setVisibility(View.GONE);
					holderMusic.btn_more.setBackgroundResource(R.drawable.btn_down);
					holder = null;
				}else{
					currentBar = position;
					isShow = true;
					if(holder!=null){
						holder.layout_menu.setVisibility(View.GONE);
						holder.btn_more.setBackgroundResource(R.drawable.btn_down);
					}
					holderMusic.layout_menu.setVisibility(View.VISIBLE);
					holderMusic.btn_more.setBackgroundResource(R.drawable.btn_up);
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
					AddSongMenuPop pop = new AddSongMenuPop(act, list.get(position).getMusic_id(), songmenu.getMusiclist_id());
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
		private int type = 1;
		private View btn;
		
		public CallBack(int position,int type) {
			super();
			this.position = position;
			this.type = type;
		}
		
		public CallBack(int position,int type,View btn) {
			super();
			this.position = position;
			this.type = type;
			this.btn = btn;
		}
		
		public CallBack(int type,ImageButton btn) {
			super();
			this.type = type;
			this.btn = btn;
		}
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			if(code != Constant.SUCCESS_REQUEST){
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			ActivityUtil.showCollectToast(context,alert);
			if(type == 1){//收藏歌曲
				list.get(position).setFavorite("1");
				notifyDataSetChanged();
			}else if(type == 2){//收藏歌单
				btn.setBackgroundResource(R.drawable.btn_collect_middle_pressed);
				songmenu.setFavorite("1");
			}
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
				app.addSingleMusic(list.get(current),musiclist_id);
				app.playStart(list.get(current));
			}else{
				List<SongInfoEntity> lists = new ArrayList<SongInfoEntity>(); 
				lists.addAll(list);
				lists.remove(0);
				app.setSongMenu(lists, 1, pageNum, musiclist_id);
				app.playStart(0);
			}
			break;
		default://流量WIFI
			if(!SharedPreUtils.isOnlyWifi(context.getApplicationContext())){
				if(type == 1){
					app.addSingleMusic(list.get(current),musiclist_id);
					app.playStart(list.get(current));
				}else{
					List<SongInfoEntity> lists = new ArrayList<SongInfoEntity>(); 
					lists.addAll(list);
					lists.remove(0);
					app.setSongMenu(lists, 1, pageNum, musiclist_id);
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
					app.addSingleMusic(list.get(current),musiclist_id);
					app.playStart(list.get(current));
				}else{
					List<SongInfoEntity> lists = new ArrayList<SongInfoEntity>(); 
					lists.addAll(list);
					lists.remove(0);
					app.setSongMenu(lists, 1, pageNum, musiclist_id);
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
