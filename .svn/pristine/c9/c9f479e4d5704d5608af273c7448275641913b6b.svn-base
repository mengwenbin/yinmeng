package com.xiaoxu.music.community.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap.Config;
import android.os.IBinder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.adapter.PlayHistoryAdapter;
import com.xiaoxu.music.community.adapter.PlayQueueAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.BarrageEntity;
import com.xiaoxu.music.community.entity.BarrageInfoEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.entity.SongMenuDetailEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.BarrageTask;
import com.xiaoxu.music.community.model.task.CollectMusicTask;
import com.xiaoxu.music.community.model.task.MusicByCategoryTask;
import com.xiaoxu.music.community.model.task.MusicByMusicListTask;
import com.xiaoxu.music.community.service.DownLoadService;
import com.xiaoxu.music.community.service.MediaBinder;
import com.xiaoxu.music.community.service.MediaPlayerService;
import com.xiaoxu.music.community.service.MediaBinder.OnClientBinderListener;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.NetUtils;
import com.xiaoxu.music.community.util.SharedPreUtils;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.CustomViewPager;
import com.xiaoxu.music.community.view.DanmuView;
import com.xiaoxu.music.community.view.custom_shape_imageview.CircleImageView;
import com.xiaoxu.music.community.view.lrc.DefaultLrcParser;
import com.xiaoxu.music.community.view.lrc.LrcRow;
import com.xiaoxu.music.community.view.lrc.LrcView;
import com.xiaoxu.music.community.view.lrc.LrcView.OnSeekToListener;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class PlayActivity extends BaseNewActivity implements OnClickListener,OnXListViewListener, OnSeekBarChangeListener{
	
	private YmApplication app;
	private String[] titleStr = {"播放队列","歌词"};
	
	private ImageButton indicat_left,indicat_center,indicat_right,btn_play,btn_collect,btn_mode;
	private TextView title_content;
	private CircleImageView iv_author_head;
	private CustomViewPager viewpager;
	
	private ViewPagerAdapter adapter_viewpager;
	private PlayQueueAdapter adapter_play_queue;
	
	private View layout_queue, layout_cover, layout_lyric;
	private XListView xlist;
	private ImageView iv_cover;
	private LrcView lyricView;
	private LrcView lrcView;
	private TextView lyric_view;
	private ScrollView scroll_View;
	
	private List<View> views;
	private List<SongInfoEntity> play_queue,list;//播放队列
	private List<SongInfoEntity> play_history;//历史记录
	private SongInfoEntity currentSong;//当前播放的歌曲
	private int currentIndex = -1;
	private int play_mode = MediaPlayerService.MODE_NORMAL;
	
	private Intent playIntent;
	private MediaBinder binder;
	
	private int CurrentTime;
	//seekbar
	private String currentTime,durationTime;
	private TextView c_time,d_time;
	private SeekBar seekbar;
	private double width, fDensity;
	private DisplayMetrics displaysMetrics;
	private LayoutParams layoutparams;
	
	//播放界面的
	private MusicByMusicListTask task_1;
	private MusicByCategoryTask task_2;
	
	private int duration_time = 0;
	
	private BitmapUtils bitUtil;
	
	private int progress;
	private EditText edit_content;
	private Button send_barrage;
	private boolean isTouch = false;
	private final String mPageName = "PlayActivity";
	public String Login_Tag = "com.xiaoxu.music.community.activity.PlayActivity";
	
	@Override
	public void setContent() {
		setContentView(R.layout.activity_playing);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		views = new ArrayList<View>();
		app = (YmApplication) getApplication();
		play_queue = new ArrayList<SongInfoEntity>();
		play_history = new ArrayList<SongInfoEntity>();
		playIntent = new Intent(context, MediaPlayerService.class);
		
		bitUtil = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		bitUtil.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitUtil.configDefaultLoadingImage(R.drawable.test_default_head_bg_blue);// 默认背景图片
		bitUtil.configDefaultLoadFailedImage(R.drawable.test_default_head_bg_blue);// 加载失败图片
	}
	
	ServiceConnection connection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			binder = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MediaBinder) service;
			if(binder != null){
				binder.setOnClientBinderListener(binderListener);
			}
		}
	};
	
	@Override
	public void setupView() {
		// TODO Auto-generated method stub
		title_content = getTextView(R.id.title_center_content);
		title_content.setText(titleStr[0]);
		iv_author_head = (CircleImageView) getImageView(R.id.title_author_head);
		iv_author_head.setOnClickListener(this);
		indicat_left = getImageButton(R.id.indicat_1);
		indicat_center = getImageButton(R.id.indicat_2);
		indicat_right = getImageButton(R.id.indicat_3);
		btn_play = getImageButton(R.id.btn_play);
		btn_play.setOnClickListener(this);
		btn_collect = getImageButton(R.id.btn_collect);
		btn_collect.setOnClickListener(this);
		btn_mode = getImageButton(R.id.btn_mode);
		
		//弹幕
		rel_barrage = (RelativeLayout) findViewById(R.id.rel_barrage);
		edit_content = (EditText) findViewById(R.id.barrage_content);
		send_barrage = (Button) findViewById(R.id.send_barrage);
		
		send_barrage.setOnClickListener(this);
		btn_mode.setOnClickListener(this);
		getImageButton(R.id.left_btn).setOnClickListener(this);
		getImageButton(R.id.btn_pre).setOnClickListener(this);
		getImageButton(R.id.btn_next).setOnClickListener(this);
		getImageButton(R.id.btn_download).setOnClickListener(this);
		getImageButton(R.id.btn_share).setOnClickListener(this);
		getImageButton(R.id.btn_queue).setOnClickListener(this);
		if(MediaPlayerService.isPlaying){
			btn_play.setBackgroundResource(R.drawable.btn_style_pause);
		} else {
			btn_play.setBackgroundResource(R.drawable.btn_style_play);
		}
		initSeekBar();
		initViewPager();
		bindService(playIntent, connection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		play_queue = app.getPlay_queue();
		currentIndex = app.getCurrent_index();
		currentSong = app.getCurrentMusic();
		play_mode = SharedPreUtils.currentPlayMode(context);
		updateModeUI(play_mode);
		updateQueueUI_1();
		updateMusicUI();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
		MobclickAgent.onResume(this);//友盟统计
		if(binder!=null){
			binder.setOnClientBinderListener(binderListener);
		}
		if(btn_play==null){
			return;
		}
		if(MediaPlayerService.isPlaying){
			btn_play.setBackgroundResource(R.drawable.btn_style_pause);
		} else {
			btn_play.setBackgroundResource(R.drawable.btn_style_play);
		}
	}
	
	public void updateQueueUI_1(){
		if(null!=play_queue && play_queue.size()>0){
			adapter_play_queue.setList(play_queue);
			adapter_play_queue.setCurrent(app.seekPosInListById(play_queue, currentSong.getMusic_id()));
			adapter_play_queue.notifyDataSetChanged();
		}
	}
	public void updateMusicUI(){
		if(null!=currentSong){
			
			if(MediaPlayerService.isPlaying){
				barrage_task = new BarrageTask(context, currentSong.getMusic_id(), barrage_callback);
				barrage_task.excute();
			}
			if(viewpager!=null&&viewpager.getCurrentItem()==1){
				title_content.setText(currentSong.getMusic_name());
			}
			if(currentSong.getUser()!=null){
				iv_author_head.setVisibility(View.VISIBLE);
				bitUtil.display(iv_author_head, StringUtil.replaceImagePath(currentSong.getUser().getUser_img(), 100));
			}
			bitmapUtils.configDefaultLoadingImage(R.drawable.default_convering);// 默认背景图片
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_convering);// 加载失败图片
			bitmapUtils.display(iv_cover, StringUtil.replaceImagePath(currentSong.getMusic_coverimg(), 600));
			btn_collect.setBackgroundResource(R.drawable.btn_collect_play_normal);
			if(currentSong.getFavorite() ==null ){
				btn_collect.setBackgroundResource(R.drawable.btn_collect_play_pressed);
			}else{
				if (Integer.parseInt(currentSong.getFavorite())==1) {
					btn_collect.setBackgroundResource(R.drawable.btn_collect_play_pressed);
				}
			}
			lrcView.reset();
			lyricView.reset();
			if(!TextUtils.isEmpty(currentSong.getIs_timelyric())&&"1".equals(currentSong.getIs_timelyric())){
				lyricView.setVisibility(View.VISIBLE);
				scroll_View.setVisibility(View.GONE);
				lyricView.setLrcRows(getLrcRows(currentSong.getMusic_lyric()));
				lrcView.setLrcRows(getLrcRows(currentSong.getMusic_lyric()));
				lrcView.setmCurPadding(ActivityUtil.sp2px(context, 35));
			}else{
				lyricView.setVisibility(View.GONE);
				scroll_View.setVisibility(View.VISIBLE);
				lyric_view.setText(currentSong.getMusic_lyric());
			}
			canPlay();
		}else if(iv_author_head!=null){
			iv_author_head.setVisibility(View.INVISIBLE);
		}
	}
	
	public void updateModeUI(int mode){
		switch (mode) {
		case MediaPlayerService.MODE_NORMAL:
			btn_mode.setBackgroundResource(R.drawable.btn_mode_order);
			break;
		case MediaPlayerService.MODE_REPEAT_ALL:
			btn_mode.setBackgroundResource(R.drawable.btn_mode_repeat_all);
			break;
		case MediaPlayerService.MODE_REPEAT_ONE:
			btn_mode.setBackgroundResource(R.drawable.btn_mode_repeat_one);
			break;
		case MediaPlayerService.MODE_RANDOM:
			btn_mode.setBackgroundResource(R.drawable.btn_mode_random);
			break;
		default:
			break;
		}
	}
	
	public void login(){
		Intent i = new Intent(context, StartActivity.class);
		i.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
		startActivity(i);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.btn_play:
			if(currentSong == null)
				return;
			if(MediaPlayerService.isPlaying){
				if (danmuView.isWorking()) {
					barrage_control.setImageResource(R.drawable.barrage_icon);
					danmuView.stop();
				}
				binder.setControlCommand(MediaPlayerService.CONTROL_PAUSE);
			}else{
				barrage_control.setImageResource(R.drawable.barrage_close_icon);
				danmuView.start();
				binder.setControlCommand(MediaPlayerService.CONTROL_PLAY);
				if("1".equals(currentSong.getIs_timelyric()==null?"0":currentSong.getIs_timelyric())){
					if(currentTime.equals(durationTime)){
						lrcView.setLrcRows(getLrcRows(currentSong.getMusic_lyric()));
						lyricView.setLrcRows(getLrcRows(currentSong.getMusic_lyric()));
					}else{
						lrcView.seekTo(progress, true, isTouch);
						lyricView.seekTo(progress, true, isTouch);
					}
				}
				canPlay();
			}
			break;
		case R.id.btn_pre:
			if(ActivityUtil.isFastDoubleClick(v.getId()))//不允许一个按钮连续快速的点击
				return;
			binder.setControlCommand(MediaPlayerService.CONTROL_PREV);
			break;
		case R.id.btn_next:
			if(ActivityUtil.isFastDoubleClick(v.getId()))//不允许一个按钮连续快速的点击
				return;
			binder.setControlCommand(MediaPlayerService.CONTROL_NEXT);
			break;
		case R.id.btn_collect:
			if(ActivityUtil.isFastDoubleClick(v.getId()))//不允许一个按钮连续快速的点击
				return;
			if(currentSong == null)
				return;
			if(AccountInfoUtils.getInstance(context).isLogin()){
				if(Integer.parseInt(currentSong.getFavorite()) == 0 && haveNetwork()){
					CollectMusicTask task = new CollectMusicTask(context, currentSong.getMusic_id(), callback_collect);
					task.excute();
				}
			}else{
				login();
			}
			break;
		case R.id.btn_mode:
			binder.setControlCommand(MediaPlayerService.CONTROL_MODE);
			break;
		case R.id.mode:
			binder.setControlCommand(MediaPlayerService.CONTROL_MODE);
			break;
		case R.id.btn_download:
			if(ActivityUtil.isFastDoubleClick(v.getId()))//不允许一个按钮连续快速的点击
				return;
			if(currentSong == null)
				return;
			if(AccountInfoUtils.getInstance(context).isLogin()){
				Intent intent = new Intent(context, DownLoadService.class);
				intent.putExtra(DownLoadService.KEY_DOWNLOAD, currentSong);
				context.startService(intent);
			}else{
				login();
			}
			break;
		case R.id.btn_share:
			if(ActivityUtil.isFastDoubleClick(v.getId()))//不允许一个按钮连续快速的点击
				return;
			if(currentSong == null)
				return;
			shareMusic(currentSong);
			break;
		case R.id.btn_queue:
			if(ActivityUtil.isFastDoubleClick(v.getId()))//不允许一个按钮连续快速的点击
				return;
			showMenu();
			break;
		case R.id.clear:
			if(ActivityUtil.isFastDoubleClick(v.getId()))//不允许一个按钮连续快速的点击
				return;
			app.setPlay_history(null);
			dialog.dismiss();
			break;
		case R.id.title_author_head:
			if(currentSong == null)
				return;
			Intent in = new Intent(context, MusicDetailActivity.class);
			in.putExtra("SongInfoEntity", currentSong);
			in.putExtra("position", currentIndex);
			startActivityForResult(in, 1);
			break;
		case R.id.barrage_control:
			
			if(MediaPlayerService.isPlaying){
				if (danmuView.isWorking()) {
					barrage_control.setImageResource(R.drawable.barrage_icon);
					danmuView.stop();
                } else {
                	barrage_control.setImageResource(R.drawable.barrage_close_icon);
                    danmuView.start();
                }
			}
			break;
		case R.id.barrage_edit://弹幕
			if(AccountInfoUtils.getInstance(context).isLogin()){
				if(rel_barrage.getVisibility() == View.GONE){
					rel_barrage.setVisibility(View.VISIBLE);
				}else if(rel_barrage.getVisibility() == View.VISIBLE){
					rel_barrage.setVisibility(View.GONE);
				}
			}else{
				login();
			}
			break;
		case R.id.send_barrage://弹幕
			
			String content = edit_content.getText().toString().trim();
			if(haveNetwork()){
				BarrageTask barrageTask = new BarrageTask(context, currentSong.getMusic_id(), content, CurrentTime, sendBarrage);
				barrageTask.excute();
			}
			break;
		}
	}
	
	@Override
	public void onRefresh() {
	}
	
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		switch (app.by_task) {
		case 1:
			if(haveNetwork()){
				task_1 = new MusicByMusicListTask(context, app.by_params, app.pageNum, callback);
				task_1.excute();
			}
			break;
		case 2:
			if(haveNetwork()){
				task_2 = new MusicByCategoryTask(context, app.by_params, app.pageNum, callback);
				task_2.excute();
			}
			break;
		default:
			break;
		}
	}
	
	BaseRequestCallBack callback_collect = new BaseRequestCallBack(){
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			if(code != Constant.SUCCESS_REQUEST){
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			ActivityUtil.showCollectToast(context,alert);
			btn_collect.setBackgroundResource(R.drawable.btn_collect_play_pressed);
			play_queue = app.getPlay_queue();
			if(play_queue!=null&&play_queue.size()>0&&currentIndex<=play_queue.size()-1){
				play_queue.get(app.seekPosInListById(play_queue, currentSong.getMusic_id())).setFavorite("1");
				app.setSongMenu(play_queue);
			}
		}
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			// TODO Auto-generated method stub
		}
	};
	
	BaseRequestCallBack callback = new BaseRequestCallBack(SongMenuDetailEntity.class) {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			xlist.stopLoadMore();
			xlist.stopRefresh();
			if(code != Constant.SUCCESS_REQUEST) {
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			SongMenuDetailEntity detail = (SongMenuDetailEntity) obj;
			List<SongInfoEntity> list = detail.getList_music();
			if(null!=list&&list.size()>0){
				updateQueueUI_2();
			}
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			// TODO Auto-generated method stub
			xlist.stopLoadMore();
			xlist.stopRefresh();
		}
	};
	
	public void updateQueueUI_2() {
		if (app.pageNum >= 2) {
			play_queue = adapter_play_queue.getList();
			if (play_queue != null && play_queue.size() > 0) {
				for (int i = 0; i < list.size(); i++) { // 新数据
					for (int j = play_queue.size() - 1; j >= 0; j--) { // 旧数据
						int old_id = Integer.parseInt(play_queue.get(j).getMusic_id());
						int new_id = Integer.parseInt(list.get(i).getMusic_id());
						if (old_id == new_id) {
							play_queue.remove(j);
							break;
						}
					}
				}
				play_queue.addAll(list);
			}
			adapter_play_queue.setList(play_queue);
			app.pageNum++;
		}
		adapter_play_queue.notifyDataSetChanged();
		if(list.size()>=10){
			xlist.setPullLoadEnable(XListView.FOOTER_WAIT);
		} else {
			xlist.setPullLoadEnable(XListView.FOOTER_HIDE);
		}
		app.setSongMenu(play_queue);//更新播放队列
	}
	
	OnClientBinderListener binderListener = new OnClientBinderListener() {
		
		@Override
		public void onPlayStart(SongInfoEntity info,int current_index) {
			// TODO Auto-generated method stub
			if(info!=null){
				btn_play.setBackgroundResource(R.drawable.btn_style_pause);
				currentSong = info;
				currentIndex = app.seekPosInListById(adapter_play_queue.getList(), currentSong.getMusic_id());
				adapter_play_queue.setCurrent(currentIndex);
				adapter_play_queue.notifyDataSetChanged();
				updateMusicUI();
			}
		}
		
		@Override
		public void onPlayResume() {
			// TODO Auto-generated method stub
			btn_play.setBackgroundResource(R.drawable.btn_style_pause);
		}
		
		@Override
		public void onPlayProgress(long current, long duration) {
			// TODO Auto-generated method stub
			if(duration > 0 && null != seekbar){ //必须判断，歌曲切换的瞬间duration很可能==0
				duration_time = (int) duration;
				long pos = (seekbar.getMax() * current / duration);
				currentTime = TimeUtil.formatTime(current);
				CurrentTime = (int) (duration / 1000);
				durationTime = TimeUtil.formatTime(duration);
				if(seekbar!=null){
					seekbar.setProgress((int) pos);
				}
				if(c_time!=null){
					c_time.setText(currentTime);
				}
			}
		}
		
		@Override
		public void onPlayPause() {
			// TODO Auto-generated method stub
			btn_play.setBackgroundResource(R.drawable.btn_style_play);
		}
		
		@Override
		public void onPlayError() {
			btn_play.setBackgroundResource(R.drawable.btn_style_play);
		}
		
		@Override
		public void onPlayComplete() {
			btn_play.setBackgroundResource(R.drawable.btn_style_play);
			danmuView.hide();
			Map<String, String> map_value = new HashMap<String, String>();
			map_value.put("type", "popular");
			map_value.put("artist", "JJLin");	
			MobclickAgent.onEventValue(PlayActivity.this, "music", map_value, CurrentTime);
		}

		@Override
		public void onPlayBufferProgress(int buffer) {
			// TODO Auto-generated method stub
			seekbar.setSecondaryProgress(buffer*2);
		}

		@Override
		public void onPlayMode(int mode) {
			// TODO Auto-generated method stub
			play_mode = mode;
			updateModeUI(play_mode);
			if(dialog!=null&&dialog.isShowing()){
				updateHisMode(play_mode);
			}
		}
	};
	
	/* SeekBar 
	 * ******************************************************************************************/
	public void initSeekBar(){
		displaysMetrics = getResources().getDisplayMetrics();
		width = displaysMetrics.widthPixels;
		seekbar = getSeekBar(R.id.seekbar);
//		fDensity = (width - ActivityUtil.dip2px(this, 40)) / 100;
		fDensity = (width - ActivityUtil.dip2px(this, 40)) / seekbar.getMax();
		c_time = getTextView(R.id.tv_currentTime);
		d_time = getTextView(R.id.tv_durationTime);
		layoutparams = new LayoutParams(ActivityUtil.dip2px(this, 40),ActivityUtil.dip2px(this, 18));
		seekbar.setOnSeekBarChangeListener(this);
	}
	private DanmuView danmuView;
	private ImageView barrage_eidt;
	private ImageView barrage_control;
	private RelativeLayout rel_barrage;
	private List<BarrageInfoEntity> barrageList;
	
	private BarrageTask barrage_task;
	/* ViewPager 
	 * ******************************************************************************************/
	public void initViewPager(){
		viewpager = (CustomViewPager) getViewPager(R.id.viewpager_play);
		viewpager.setScanScroll(true);
		layout_queue = getLayoutInflater().inflate(R.layout.fragment_play_queue, null);
		layout_cover = getLayoutInflater().inflate(R.layout.fragment_play_cover, null);
		layout_lyric = getLayoutInflater().inflate(R.layout.fragment_play_lyric, null);
		views.add(layout_queue);
		views.add(layout_cover);
		views.add(layout_lyric);
		
		iv_cover = (ImageView) layout_cover.findViewById(R.id.imageview_cover);
		lrcView = (LrcView) layout_cover.findViewById(R.id.lyric);
		
		barrage_control = (ImageView) layout_cover.findViewById(R.id.barrage_control);
		barrage_eidt = (ImageView) layout_cover.findViewById(R.id.barrage_edit);
		danmuView = (DanmuView) layout_cover.findViewById(R.id.danmu);
		
		barrage_control.setOnClickListener(this);
		barrage_eidt.setOnClickListener(this);
		lrcView.setOnSeekToListener(onSeekToListener);
		LayoutParams params = (LayoutParams) iv_cover.getLayoutParams();
		params.height = ActivityUtil.getHeight(iv_cover);
		iv_cover.setLayoutParams(params);
		
		scroll_View = (ScrollView) layout_lyric.findViewById(R.id.scroll_View);
		lyric_view = (TextView) layout_lyric.findViewById(R.id.textview_lyric1);
		lyricView = (LrcView) layout_lyric.findViewById(R.id.textview_lyric);
		lyricView.setOnSeekToListener(onSeekToListener);
		
		xlist = (XListView) layout_queue.findViewById(R.id.listview_queue);
		xlist.setPullRefreshEnable(false);
		xlist.setXListViewListener(this);
		xlist.setPullLoadEnable(XListView.FOOTER_HIDE);
		adapter_play_queue = new PlayQueueAdapter(context, app);
		xlist.setAdapter(adapter_play_queue);
		
		adapter_viewpager = new ViewPagerAdapter();
		viewpager.setAdapter(adapter_viewpager);
		viewpager.setOnPageChangeListener(listener);
	}
	
	/*
	 * 请求弹幕数据
	*/
	BaseRequestCallBack barrage_callback = new BaseRequestCallBack(BarrageEntity.class) {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			if(code != Constant.SUCCESS_REQUEST) {
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			BarrageEntity barrageEntity = (BarrageEntity) obj;
			if(!barrageEntity.getList_size().equals("0")){
				barrageList = barrageEntity.getList_barrage();
				danmuView.initDanmuItemViews(barrageList);
			}
		}
		
		@Override
		public void onFailure(HttpException exception, String error) {
		}
	};
	
	/*
	 * 发送弹幕数据
	*/
	BaseRequestCallBack sendBarrage = new BaseRequestCallBack() {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			if(code != Constant.SUCCESS_REQUEST) {
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			ActivityUtil.showShortToast(context, alert);
			edit_content.setText("");
		}
		
		@Override
		public void onFailure(HttpException exception, String error) {
		}
	};

	
	OnPageChangeListener listener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				initIndictor();
				title_content.setText(titleStr[0]);
				indicat_left.setBackgroundResource(R.drawable.bg_indicator_white);
				break;
			case 1:
				initIndictor();
				if(currentSong!=null){
					title_content.setText(currentSong.getMusic_name());
				}else{
					title_content.setText(" ");
				}
				indicat_center.setBackgroundResource(R.drawable.bg_indicator_white);
				break;
			case 2:
				initIndictor();
				title_content.setText(titleStr[1]);
				indicat_right.setBackgroundResource(R.drawable.bg_indicator_white);
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
	
	class ViewPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return views == null ? 0 : views.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			view.removeView(views.get(position));
		}
		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			view.addView(views.get(position));
			return views.get(position);
		}
	}
	
	public void initIndictor(){
		indicat_left.setBackgroundResource(R.drawable.bg_indicator_black);
		indicat_center.setBackgroundResource(R.drawable.bg_indicator_black);
		indicat_right.setBackgroundResource(R.drawable.bg_indicator_black);
	}
	
	Dialog dialog;
	View menuView;
	ImageButton btn_mode_his;
	TextView tv_num,tv_clear;
	ListView listview_history;
	PlayHistoryAdapter history_adapter;
	public void showMenu(){
		//dialog
		dialog = new Dialog(context, R.style.Custom_Progress);
		play_history = app.getPlay_history();
		//dialog view
		menuView = getLayoutInflater().inflate(R.layout.dialog_play_history, null);
		tv_num = (TextView) menuView.findViewById(R.id.num);
		tv_clear =  (TextView) menuView.findViewById(R.id.clear);
		btn_mode_his = (ImageButton) menuView.findViewById(R.id.mode);
		updateHisMode(play_mode);
		btn_mode_his.setOnClickListener(this);
		tv_clear.setOnClickListener(this);
		listview_history = (ListView) menuView.findViewById(R.id.listview_history);
		history_adapter = new PlayHistoryAdapter(context, app, dialog, this);
		history_adapter.setList(play_history);
		if(play_history!=null&&play_history.size()>0&&currentSong!=null){
			history_adapter.setCurrent(app.seekPosInListById(play_history, currentSong.getMusic_id()));
			tv_num.setText("历史播放"+"("+play_history.size()+")");
		}
		listview_history.setAdapter(history_adapter);
		
		dialog.setContentView(menuView);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);// 按返回键是否取消
		// 设置居中
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = getScreenWidth();
		lp.dimAmount = 0.5f;// 设置背景层透明度
		dialog.show();
	}
	
	public void updateHisMode(int mode){
		switch (mode) {
		case MediaPlayerService.MODE_NORMAL:
			btn_mode_his.setBackgroundResource(R.drawable.btn_mode_his_order);
			break;
		case MediaPlayerService.MODE_REPEAT_ALL:
			btn_mode_his.setBackgroundResource(R.drawable.btn_mode_his_repeat_all);
			break;
		case MediaPlayerService.MODE_REPEAT_ONE:
			btn_mode_his.setBackgroundResource(R.drawable.btn_mode_his_repeat_one);
			break;
		case MediaPlayerService.MODE_RANDOM:
			btn_mode_his.setBackgroundResource(R.drawable.btn_mode_his_random);
			break;
		default:
			break;
		}
	}
	
	public void canPlay(){
		String playPath = app.getPlayPath();
		if(!TextUtils.isEmpty(playPath)&&!StringUtil.isAbsolutePath(playPath)){
			return;
		}
		
		int i = NetUtils.getAPNType(context);
		switch (i) {
		case -1://No NetWork
			if(binder!=null){
				binder.setControlCommand(MediaPlayerService.CONTROL_PAUSE);
			}
			ActivityUtil.showShortToast(context, context.getString(R.string.please_check_network));
			break;
		case 1://WIFI
			break;
		default://流量WIFI
			if(!SharedPreUtils.isOnlyWifi(context)){
				break;
			}
			if(binder != null){
				binder.setControlCommand(MediaPlayerService.CONTROL_PAUSE);
				showDialogWifi();
			}
			break;
		}
	}
	
	Dialog dialog_wifi;
	public void showDialogWifi(){
		dialog_wifi = new Dialog(context, R.style.Custom_Progress);
		View menuView = LayoutInflater.from(context).inflate(R.layout.dialog_only_wifi, null);
		Button btn_ok = (Button) menuView.findViewById(R.id.ok);
		Button btn_cancle = (Button) menuView.findViewById(R.id.cancle);
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				binder.setControlCommand(MediaPlayerService.CONTROL_PLAY);
				SharedPreUtils.setOnlyWifi(context, false);
				dialog_wifi.dismiss();
			}
		});
		btn_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_wifi.dismiss();
			}
		});
		dialog_wifi.setContentView(menuView);
		dialog_wifi.setCancelable(true);
		dialog_wifi.getWindow().getAttributes().gravity = Gravity.CENTER;
		dialog_wifi.show();
	}
	
	@SuppressWarnings("deprecation")
	public int getScreenWidth(){
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		return d.getWidth();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		if(currentSong!=null&&!TextUtils.isEmpty(currentSong.getMusic_coverimg())){
			bitmapUtils.clearMemoryCache(StringUtil.replaceImagePath(currentSong.getMusic_coverimg(), 600));
		}
		lrcView.reset();
		lyricView.reset();
		unbindService(connection);
		super.onDestroy();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null && requestCode == 1){
			int position = data.getIntExtra("position", -1);
			SongInfoEntity infoEntity = (SongInfoEntity) data.getSerializableExtra("info");
			if(infoEntity==null)
				return;
			if(play_queue!=null&&play_queue.size()>0){
				int index = app.seekPosInListById(play_queue, infoEntity.getMusic_id());
				if(index>=0){
					play_queue.set(index, infoEntity);
				}
			}
			if(play_history!=null&&play_history.size()>0){
				int index = app.seekPosInListById(play_history, infoEntity.getMusic_id());
				if(index>=0){
					play_history.set(index, infoEntity);
				}
			}
			currentSong = infoEntity;
		}
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
	
	/**
	 * 将一个字符串转化为输入流
	 */
	public InputStream getStringStream(String sInputString) {
		if (sInputString != null && !sInputString.trim().equals("")) {
			try {
				ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
				return tInputStringStream;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	OnSeekToListener onSeekToListener = new OnSeekToListener() {
		@Override
		public void onSeekTo(int progress) {
			binder.seekBarStopTrackingTouch(progress);
		}
	};
		
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		binder.seekBarStopTrackingTouch(progress);
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		this.progress = progress * duration_time / seekBar.getMax();
		isTouch = fromUser;
		lrcView.seekTo(this.progress, true,fromUser);
		lyricView.seekTo(this.progress, true,fromUser);
		layoutparams.leftMargin = (int) (progress * fDensity);
		c_time.setLayoutParams(layoutparams);
		c_time.setText(currentTime);
		d_time.setText(durationTime);
	}
	
}
