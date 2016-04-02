package com.xiaoxu.music.community.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.adapter.AddSongMenuAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.MusicDetailEntity;
import com.xiaoxu.music.community.entity.SimpleUserEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.entity.SongMenuInfoEntity;
import com.xiaoxu.music.community.entity.SongMenuListEntity;
import com.xiaoxu.music.community.fragment.MusicDetailAuthorFragment;
import com.xiaoxu.music.community.fragment.MusicDetailLyricFragment;
import com.xiaoxu.music.community.fragment.MusicDetailCommentFragment;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.AttentionPersonTask;
import com.xiaoxu.music.community.model.task.CancleAttentionTask;
import com.xiaoxu.music.community.model.task.CollectMusicTask;
import com.xiaoxu.music.community.model.task.MusicDetailTask;
import com.xiaoxu.music.community.model.task.MySongMenuTask;
import com.xiaoxu.music.community.model.task.NewSongMenuTask;
import com.xiaoxu.music.community.model.task.SongMenuAddSongTask;
import com.xiaoxu.music.community.parser.ParseUtil;
import com.xiaoxu.music.community.service.DownLoadService;
import com.xiaoxu.music.community.service.MediaBinder;
import com.xiaoxu.music.community.service.MediaPlayerService;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.NetUtils;
import com.xiaoxu.music.community.util.SharedPreUtils;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.view.CustomOnlyWifiDialog;
import com.xiaoxu.music.community.view.CustomViewPager;
import com.xiaoxu.music.community.view.custom_shape_imageview.CircleImageView;
import com.xiaoxu.music.community.view.lrc.DefaultLrcParser;
import com.xiaoxu.music.community.view.lrc.LrcRow;

public class MusicDetailActivity extends BaseNewActivity implements OnClickListener, OnItemClickListener {
	
	private YmApplication app;
	private boolean isStart = true;
	private MediaBinder binder;
	
	private SongInfoEntity currentSong;
	
	private Intent intent;
	private SongInfoEntity info;
	private SimpleUserEntity userInfo;
	private String music_id;
	
	private Button btn_edit;
	private ImageButton btn_attention, btn_collect,btn_play,btn_right;
	private TextView tv_title , tv_nick, tv_summary;
	private CircleImageView iv_head;
	private ImageView iv_sex;
	
	//ViewPager
	private Button btn_author,btn_lyric,btn_comment;
	private View indicate_author,indicate_lyric,indicate_comment;
	private CustomViewPager view_pager;
	private ViewPagerAdapter adapter_viewpager;
	private MusicDetailAuthorFragment frg_author;
	private MusicDetailLyricFragment frg_lyric;
	private MusicDetailCommentFragment frg_comment;
	
	private int position;
	private final String mPageName = "MusicDetailActivity";
	public String Login_Tag = "com.xiaoxu.music.community.activity.MusicDetailActivity";
	
	@Override
	public void setContent() {
		setContentView(R.layout.activity_music_detail);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_head_bg_blue);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_head_bg_blue);// 加载失败图片
		app = (YmApplication) getApplication();
		currentSong = app.getCurrentMusic();
		
		intent = getIntent();
		position = intent.getIntExtra("position", 0);
		info = (SongInfoEntity) intent.getSerializableExtra("SongInfoEntity");
		if(info==null)
			return;
		music_id = info.getMusic_id();
		Intent playIntent = new Intent(context, MediaPlayerService.class);
		bindService(playIntent, connection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}
	
	//音乐播放Service
	ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MediaBinder) service;
		}
		public void onServiceDisconnected(ComponentName name) {}
	};
	
	//-------------------------init view----------------------------------
	@Override
	public void setupView() {
		rotateLoading = getRotateLoading(R.id.loading);
		btn_right = getImageButton(R.id.title_right_btn);
		btn_right.setOnClickListener(this);
		initAnimation(btn_right);
		
		getImageButton(R.id.title_left_btn).setOnClickListener(this);
		tv_title =  getTextView(R.id.title_center_content);
		
		//user
		iv_head = (CircleImageView) getImageView(R.id.iv_head);
		tv_nick = getTextView(R.id.tv_nick);
		iv_sex = getImageView(R.id.iv_sex);
		tv_summary = getTextView(R.id.tv_summary);
		
		btn_edit = getButton(R.id.btn_edit);
		btn_attention = getImageButton(R.id.btn_attention);
		btn_attention.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		
		if(AccountInfoUtils.getInstance(context).getAccountId().equals(info.getUser_id()==null?"":info.getUser_id())){
			btn_edit.setVisibility(View.VISIBLE);
			btn_attention.setVisibility(View.GONE);
		}else{
			btn_attention.setVisibility(View.VISIBLE);
			btn_edit.setVisibility(View.GONE);
		}
		
		// function
		btn_collect = getImageButton(R.id.btn_collect);
		btn_play = getImageButton(R.id.btn_play);
		btn_collect.setOnClickListener(this);
		btn_play.setOnClickListener(this);
		
		if("1".equals(info.getFavorite()==null?"0":info.getFavorite())){
			btn_collect.setBackgroundResource(R.drawable.btn_collect_play_pressed);
		}else{
			btn_collect.setBackgroundResource(R.drawable.btn_collect_play_normal);
		}
		getImageButton(R.id.btn_share).setOnClickListener(this);
		getImageButton(R.id.btn_download).setOnClickListener(this);
		getImageButton(R.id.btn_add).setOnClickListener(this);
		
		// indicates
		btn_author = getButton(R.id.btn_author);
		btn_lyric = getButton(R.id.btn_lyric);
		btn_comment = getButton(R.id.btn_comment);
		btn_author.setOnClickListener(this);
		btn_lyric.setOnClickListener(this);
		btn_comment.setOnClickListener(this);
		indicate_author = findViewById(R.id.indicate_author);
		indicate_lyric = findViewById(R.id.indicate_lyric);
		indicate_comment = findViewById(R.id.indicate_comment);
		
		frg_author = new MusicDetailAuthorFragment(info);
		if(!TextUtils.isEmpty(info.getIs_timelyric())&&info.getIs_timelyric().equals("1")){
			frg_lyric = new MusicDetailLyricFragment(getLrcStr(info.getMusic_lyric()));
		}else{
			frg_lyric = new MusicDetailLyricFragment(TextUtils.isEmpty(info.getMusic_lyric())?"无歌词":info.getMusic_lyric());
		}
		frg_comment = new MusicDetailCommentFragment(music_id);
		
		view_pager = (CustomViewPager) findViewById(R.id.view_pager);
		view_pager.setScanScroll(true);
		adapter_viewpager = new ViewPagerAdapter(getSupportFragmentManager());
		view_pager.setAdapter(adapter_viewpager);
		view_pager.setOnPageChangeListener(listener);
	}
	
	@Override
	public void setModel() {
		updatePlayState();
		updateMusicUI();
		task_songMenu();
		taskMusic();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
		MobclickAgent.onResume(this);//友盟统计
		updatePlayState();
	}
	
	public void taskMusic(){
		MusicDetailTask task  = new MusicDetailTask(context, music_id, new BaseRequestCallBack(MusicDetailEntity.class) {
			
			@Override
			public void onResult(String result, int code, String alert, Object obj) {
				if(code != Constant.SUCCESS_REQUEST){
					ActivityUtil.showShortToast(context, alert);
					return;
				}
				if(null != obj){
					MusicDetailEntity entity = (MusicDetailEntity) obj;
					info = entity.getMusic();
					if(frg_author!=null){
						frg_author.setInfo(info);
					}
					if(info.getIs_timelyric().equals("1")){
						frg_lyric.setContent(getLrcStr(info.getMusic_lyric()));
					}else{
						frg_lyric.setContent(TextUtils.isEmpty(info.getMusic_lyric())?"无歌词":info.getMusic_lyric());
					}
					frg_lyric.setModel();
					updateMusicUI();
				}
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
		});
		task.excute();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1){
			task_songMenu();
			return;
		}
		if(requestCode == 2){
			taskMusic();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void login(){
		Intent i = new Intent(context, StartActivity.class);
		i.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
		startActivityForResult(i, 1);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:
			
			intent = new Intent();
			intent.putExtra("info", info);
			intent.putExtra("position", position);
			setResult(1, intent);
			finish();
			break;
		case R.id.title_right_btn:
			startActivity(new Intent(context, PlayActivity.class));
			break;
		case R.id.btn_attention:
			if(AccountInfoUtils.getInstance(context).isLogin()){
				if(haveNetwork()){
					btn_attention.setClickable(false);
					if(userInfo.getIs_attention().equals("0")){
						AttentionPersonTask attention_task = new AttentionPersonTask(context, info.getUser_id(), attention_callback);
						attention_task.excute();
					}else if(userInfo.getIs_attention().equals("1")){
						CancleAttentionTask cancleattention_task = new CancleAttentionTask(context, info.getUser_id(), cancleattention_callback);
						cancleattention_task.excute();
					}
				}
			}else{
				login();
			}
			break;
		case R.id.btn_collect:
			if(AccountInfoUtils.getInstance(context).isLogin()){
				if(haveNetwork()){
					CollectMusicTask task = new CollectMusicTask(context, info.getMusic_id(),callback_collect);
					task.excute();
				}
			}else{
				login();
			}
			break;
		case R.id.btn_share:
			shareMusic(info);
			break;
		case R.id.btn_download:
			if(AccountInfoUtils.getInstance(context).isLogin()){
				Intent intent = new Intent(context, DownLoadService.class);
				intent.putExtra(DownLoadService.KEY_DOWNLOAD, info);
				context.startService(intent);
			}else{
				login();
			}
			break;
		case R.id.btn_play:
			if(isStart){
				canPlay();
				return;
			}
			if(MediaPlayerService.isPlaying){
				binder.setControlCommand(MediaPlayerService.CONTROL_PAUSE);
				btn_play.setBackgroundResource(R.drawable.btn_style_play);
			}else{
				binder.setControlCommand(MediaPlayerService.CONTROL_PLAY);
				btn_play.setBackgroundResource(R.drawable.btn_style_pause);
			}
			break;
		case R.id.btn_add:
			if(AccountInfoUtils.getInstance(context).isLogin()){
				showSongMenu();
			}else{
				login();
			}
			break;
		case R.id.btn_author:
			view_pager.setCurrentItem(0);
			break;
		case R.id.btn_lyric:
			view_pager.setCurrentItem(1);
			break;
		case R.id.btn_comment:
			view_pager.setCurrentItem(2);
			break;
		case R.id.btn_edit:
			
			Intent intent = new Intent(context, EditMusicInfoActivity.class);
			intent.putExtra("SongInfo", info);
			startActivityForResult(intent, 2);
			break;
		}
	}
	
	public void playStart(){
		app.addSingleMusic(info);
		isStart = false;
		app.playStart(info);
		btn_play.setBackgroundResource(R.drawable.btn_style_pause);
	}
	
	CustomOnlyWifiDialog dialog_only_wifi;
	public void canPlay(){
		
		
		int i = NetUtils.getAPNType(context);
		switch (i) {
		case -1://No NetWork
			ActivityUtil.showShortToast(context, context.getString(R.string.please_check_network));
			break;
		case 1://WIFI
			playStart();
			break;
		default://流量WIFI
			if(!SharedPreUtils.isOnlyWifi(context)){
				playStart();
				break;
			}
			dialog_only_wifi = CustomOnlyWifiDialog.show(context, lis_only_wifi);
			break;
		}
	}
	
	OnClickListener lis_only_wifi = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.ok:
				playStart();
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
	};
	
	BaseRequestCallBack callback_collect = new BaseRequestCallBack(){
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			if(code != Constant.SUCCESS_REQUEST){
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			ActivityUtil.showCollectToast(context,alert);
			info.setFavorite(1+"");
			btn_collect.setBackgroundResource(R.drawable.btn_collect_play_pressed);
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
		}
	};
	
	/*
	 * 关注他人
	 */
	BaseRequestCallBack attention_callback = new BaseRequestCallBack() {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			if(code != Constant.SUCCESS_REQUEST){
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			ActivityUtil.showAttentionToast(context, "关注成功").show();
			btn_attention.setBackgroundResource(R.drawable.btn_attention_white_select);
			info.getUser().setIs_attention("1");
			Intent intent = new Intent(Constant.ACTION_MINE_UPDATE_USERINFO);
			intent.putExtra("isAttention", true);
			context.sendBroadcast(intent);
			btn_attention.setClickable(true);
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
		}
	};
	
	/*
	 * 取消关注
	 */
	BaseRequestCallBack cancleattention_callback = new BaseRequestCallBack() {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			if(code != Constant.SUCCESS_REQUEST){
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			ActivityUtil.showAttentionToast(context, "取消成功").show();
			btn_attention.setBackgroundResource(R.drawable.btn_attention_white_normal);
			userInfo.setIs_attention("0");
			Intent intent = new Intent(Constant.ACTION_MINE_UPDATE_USERINFO);
			intent.putExtra("isAttention", true);
			context.sendBroadcast(intent);
			btn_attention.setClickable(true);
			
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
		}
	};
	
	public void updatePlayState(){
		currentSong = app.getCurrentMusic();
		btn_play.setBackgroundResource(R.drawable.btn_style_play);
		if(currentSong!=null && currentSong.getMusic_id().equals(music_id)){
			isStart = false;
			if(MediaPlayerService.isPlaying){
				btn_play.setBackgroundResource(R.drawable.btn_style_pause);
			}
		}
	}
	
	public void updateMusicUI(){
		if(info != null){
			
			tv_title.setText(info.getMusic_name());
			userInfo = info.getUser();
			if(userInfo!=null){
				tv_nick.setText(userInfo.getUser_nick());
				int sex = Integer.parseInt(userInfo.getUser_sex());
				switch (sex) { //性别
					case 0: iv_sex.setImageResource(R.drawable.sex_martian); break;
					case 1: iv_sex.setImageResource(R.drawable.sex_man); break;
					case 2: iv_sex.setImageResource(R.drawable.sex_woman); break;
					default: break;
				}
				if(!TextUtils.isEmpty(userInfo.getUser_summary())){
					tv_summary.setText("简介："+userInfo.getUser_summary());
				}else{
					tv_summary.setText("简介：这个主人有点懒，什么都没写");
				}
				btn_attention.setBackgroundResource(R.drawable.btn_attention_white_normal);
				if(Integer.parseInt(userInfo.getIs_attention()!=null?userInfo.getIs_attention():"0")==1){
					btn_attention.setBackgroundResource(R.drawable.btn_attention_white_select);
				}
				bitmapUtils.display(iv_head, StringUtil.replaceImagePath(info.getUser().getUser_img(), 100));
			}
			btn_collect.setBackgroundResource(R.drawable.btn_collect_play_normal);
			if(Integer.parseInt(info.getFavorite()!=null?info.getFavorite():"0")==1){
				btn_collect.setBackgroundResource(R.drawable.btn_collect_play_pressed);
			}
			
			if(info.getUser_id().equals(AccountInfoUtils.getInstance(context).getAccountId())){
				btn_edit.setVisibility(View.VISIBLE);
				btn_attention.setVisibility(View.GONE);
			}else{
				btn_attention.setVisibility(View.VISIBLE);
				btn_edit.setVisibility(View.GONE);
			}
			
		}
	}
	
	
	
	
	/* ************************ ViewPager  **********************************************/
	
	public class ViewPagerAdapter extends FragmentStatePagerAdapter {

		private Fragment fragment;

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				fragment = frg_author;
				break;
			case 1:
				fragment = frg_lyric;
				break;
			case 2:
				fragment = frg_comment;
				break;
			default:
				break;
			}
			return fragment;
		}
		
		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}
	}
	
	public void initIndictor(){
		btn_author.setTextColor(getResources().getColor(R.color.tab_text_normal));
		btn_lyric.setTextColor(getResources().getColor(R.color.tab_text_normal));
		btn_comment.setTextColor(getResources().getColor(R.color.tab_text_normal));
		indicate_author.setBackgroundResource(R.color.white);
		indicate_lyric.setBackgroundResource(R.color.white);
		indicate_comment.setBackgroundResource(R.color.white);
	}
	
	OnPageChangeListener listener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				initIndictor();
				btn_author.setTextColor(getResources().getColor(R.color.tab_text_select));
				indicate_author.setBackgroundResource(R.color.main_color);
				break;
			case 1:
				initIndictor();
				btn_lyric.setTextColor(getResources().getColor(R.color.tab_text_select));
				indicate_lyric.setBackgroundResource(R.color.main_color);
				break;
			case 2:
				initIndictor();
				btn_comment.setTextColor(getResources().getColor(R.color.tab_text_select));
				indicate_comment.setBackgroundResource(R.color.main_color);
				break;
			default:
				break;
			}
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}
	};
	
	/* ********************  歌 单   **********************************************/
	//我的歌单
	public void task_songMenu(){
		MySongMenuTask task = new MySongMenuTask(app, pageNum, new BaseRequestCallBack(SongMenuListEntity.class) {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
			@Override
			public void onResult(String result, int code, String alert, Object obj) {
				// TODO Auto-generated method stub
				if(code!=Constant.SUCCESS_REQUEST)
					return;
				SongMenuListEntity entity = (SongMenuListEntity) obj;
				List<SongMenuInfoEntity> menus = entity.getList_musiclist();
				if(Integer.parseInt(entity.getList_size())>0){
					songmenus = entity.getList_musiclist();
					songmenus.add(0, new SongMenuInfoEntity("-1", "新建歌单"));
				}
			}
		});
		task.excute();
	}
	//新建歌单
	public void task_newSongMenu(String name){
		
		NewSongMenuTask task = new NewSongMenuTask(context, name, new BaseRequestCallBack(SongMenuListEntity.class) {
			@Override
			public void onFailure(HttpException arg0, String arg1) {loadingCancle(rotateLoading);}
			@Override
			public void onResult(String result, int code, String alert, Object obj) {
				// TODO Auto-generated method stub
				loadingCancle(rotateLoading);
				if(code!=Constant.SUCCESS_REQUEST)
					return;
				SongMenuListEntity entity = (SongMenuListEntity) obj;
				List<SongMenuInfoEntity> menus = entity.getList_musiclist();
				if(Integer.parseInt(entity.getList_size())>0){
					songmenus = entity.getList_musiclist();
					songmenus.add(0, new SongMenuInfoEntity("-1", "新建歌单"));
				}
			}
		});
		task.excute();
		loadingStart(rotateLoading);
	}
	//添加歌曲到歌单
	public void task_addSongMenu(final int position, String songmenu_id){
		SongMenuAddSongTask task = new SongMenuAddSongTask(context, music_id, songmenu_id, new BaseRequestCallBack() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {loadingCancle(rotateLoading);}
			@Override
			public void onResult(String result, int code, String alert, Object obj) {
				// TODO Auto-generated method stub
				loadingCancle(rotateLoading);
				ActivityUtil.showShortToast(context, alert);
				if(code!=Constant.SUCCESS_REQUEST)
					return;
				obj = ParseUtil.parseResultObj(result, SongMenuListEntity.class);
				SongMenuListEntity entity = (SongMenuListEntity) obj;
				List<SongMenuInfoEntity> menus = entity.getList_musiclist();
				if(Integer.parseInt(entity.getList_size())>0){
					songmenus = entity.getList_musiclist();
					songmenus.add(0, new SongMenuInfoEntity("-1", "新建歌单"));
				}
			}
		});
		task.excute();
		loadingStart(rotateLoading);
	}
	
	Dialog dialog;
	View menuView;
	ListView lv_songmenu;
	int pageNum = 1;
	AddSongMenuAdapter adapter_songmenu;
	List<SongMenuInfoEntity> songmenus = new ArrayList<SongMenuInfoEntity>();
	public void showSongMenu(){
		if(songmenus!=null&&songmenus.size()>0){
			if(!songmenus.get(0).getMusiclist_id().equals("-1")){
				songmenus.add(0, new SongMenuInfoEntity("-1", "新建歌单"));
			}
		}else{
			songmenus.add(0, new SongMenuInfoEntity("-1", "新建歌单"));
		}
		menuView = getLayoutInflater().inflate(R.layout.dialog_songmenu_list, null);
		lv_songmenu = (ListView) menuView.findViewById(R.id.list_songmenu);
		lv_songmenu.setOnItemClickListener(this);
		adapter_songmenu = new AddSongMenuAdapter(context, songmenus);
		lv_songmenu.setAdapter(adapter_songmenu);
		
		dialog = new Dialog(context, R.style.Custom_Dialog);
		dialog.setContentView(menuView);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);// 按返回键是否取消
		// 设置居中
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = ActivityUtil.getScreenWidth(context);
		lp.dimAmount = 0.5f;// 设置背景层透明度
		dialog.show();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		dialog.dismiss();
		if(position==0){
			showAddMenu();
			return;
		}
		task_addSongMenu(position,songmenus.get(position).getMusiclist_id());
	}
	
	View view;
	EditText edit_name;
	Button btn_ok,btn_cancel;
	Dialog dialog2;
	public void showAddMenu(){
		view = getLayoutInflater().inflate(R.layout.dialog_new_song_menu, null);
		edit_name = (EditText) view.findViewById(R.id.edit_name);
		btn_ok = (Button) view.findViewById(R.id.ok);
		btn_cancel =  (Button) view.findViewById(R.id.cancel);
		btn_ok.setOnClickListener(l);
		btn_cancel.setOnClickListener(l);
		dialog2 = new Dialog(context, R.style.Custom_Dialog);
		dialog2 = new Dialog(context, R.style.Custom_Dialog);
		dialog2.setContentView(view);
		dialog2.setCanceledOnTouchOutside(true);
		dialog2.setCancelable(true);// 按返回键是否取消
		// 设置居中
		Window dialogWindow = dialog2.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialog2.getWindow().getAttributes();
		lp.dimAmount = 0.5f;// 设置背景层透明度
		dialog2.show();
	}
	
	OnClickListener l = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ok:
					String name = edit_name.getText().toString();
					if(!TextUtils.isEmpty(name))
						task_newSongMenu(name);
					dialog2.dismiss();
					break;
				case R.id.cancel: dialog2.dismiss(); break;
				default: break;
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(connection);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			intent = new Intent();
			intent.putExtra("info", info);
			intent.putExtra("position", position);
			setResult(1, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 获取歌词List集合
	 * @return
	 */
	private List<LrcRow> getLrcRows(String lrcStr){
		List<LrcRow> rows = null;
		InputStream is = getStringStream(lrcStr);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line ;
		StringBuffer sb = new StringBuffer();
		try {
			while((line = br.readLine()) != null){
				sb.append(line+"\n");
			}
			System.out.println(sb.toString());
			rows = DefaultLrcParser.getIstance().getLrcRows(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;
	}
	
	private String getLrcStr(String lrcStr){
		
		if(TextUtils.isEmpty(lrcStr)){
			return "无歌词";
		}
		List<LrcRow> list = getLrcRows(lrcStr);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			LrcRow lrcRow = list.get(i);
			sb.append(lrcRow.getContent()+"\n");
		}
		return sb.toString();
	}
	
	/**
	 * 将一个字符串转化为输入流
	 */
	public InputStream getStringStream(String sInputString) {
		ByteArrayInputStream tInputStringStream = null;
		try {
			tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return tInputStringStream;
	}
	
}
