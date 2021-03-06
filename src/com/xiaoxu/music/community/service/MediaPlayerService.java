package com.xiaoxu.music.community.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Service;

import com.lidroid.xutils.exception.HttpException;
import com.xiaoxu.music.community.ExitAppliation;
import com.xiaoxu.music.community.HandlerHelp;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.activity.PlayActivity;
import com.xiaoxu.music.community.dao.DownLoadDB;
import com.xiaoxu.music.community.entity.SimpleUserEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.CountMusicTask;
import com.xiaoxu.music.community.service.MediaBinder.OnServiceBinderListener;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.FileUtil;
import com.xiaoxu.music.community.util.SharedPreUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

public class MediaPlayerService extends Service implements IMediaConstant, OnBufferingUpdateListener,
							OnPreparedListener, OnErrorListener, OnCompletionListener {
	
	private Context context;
	private YmApplication app;
	
	private MediaPlayer mediaPlayer;		    //媒体播放器 
	private int playMode = MODE_NORMAL;	       //播放的模式（顺序）
	public static boolean isPlaying = false;  //当前播放状态（播放，暂停）
	private boolean isPrepared = true;       //是否准备好了
	
	private List<SongInfoEntity> play_history,old_history;
	private int maxHistory = 100;
	
	private List<SongInfoEntity> play_queue;
	private SongInfoEntity currentSong;
	private int currentIndex;    //当前索引
	private String songPath;    // mp3文件路径
	private long currentTime,durationTime;   // 当前播放进度, 播放长度
	private Intent intent;
	private MediaBinder binder;
	private PlayReceiver receiver;
	
	private DownLoadDB db_download;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		syncMusicData();
		return binder;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		app = (YmApplication) getApplication();
		binder = app.getBinder();
		binder.setOnServiceBinderListener(binderListener);//播放控制的监听（客户端控制）
		
		db_download = new DownLoadDB(context);
		//播放的循环模式
		playMode = SharedPreUtils.currentPlayMode(context);
		//获取播放历史
		play_history = app.getPlay_history();
		new Async().execute();
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置媒体流类型
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this); // 播放完成监听
		
		//广播用于监听 （更新播放队列，播放控制，保存播放历史）
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_QUEUE);
		filter.addAction(PLAY_CONTROL);
		filter.addAction(SAVE_HISTORY);
		receiver = new PlayReceiver();
		registerReceiver(receiver, filter);
		
		initNotification();//初始化通知栏通知
	}
	
	//广播用于监听 （更新播放队列，播放控制，保存播放历史）
	class PlayReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(UPDATE_QUEUE)) {
				syncMusicData();//同步播放队列
			}else if(intent.getAction().equals(PLAY_CONTROL)) {//YmApplication PlayStart发送广播
				int control = intent.getIntExtra(KEY_CONTROL, -1);
				int position = intent.getIntExtra(KEY_CURRENT_INDEX, 0);
				if(getSize()>0 && control==CONTROL_START){//播放指定歌曲
					currentIndex = position;
					updateCurrentMusic();
				}
			}else if(intent.getAction().equals(SAVE_HISTORY)){
				savaHistory();//保存播放历史
			}
		}
	}
	
	//服务端控制监听，用于监听客户端发出的控制指令
	OnServiceBinderListener binderListener = new OnServiceBinderListener() {
		
		//进度改变监听
		@Override
		public void seekBarStopTrackingTouch(int progress) {
			// TODO Auto-generated method stub
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.seekTo(progress);//跳转到指定进度
			}
		}
		
		//控制指令监听
		@Override
		public void control(int command) {
			// TODO Auto-generated method stub
			switch (command) {
			case CONTROL_START: //开始播放
				if(getSize()>0){
					currentIndex = app.getCurrent_index();
					updateCurrentMusic();
				}
				break;
			case CONTROL_PAUSE://暂停
				pause();
				break;
			case CONTROL_PLAY://继续播放
				resume();
				break;
			case CONTROL_STOP://停止播放
				stop();
				break;
			case CONTROL_PREV:// 上一首
				previous();
				break;
			case CONTROL_NEXT:// 下一首
				next();
				break;
			case CONTROL_MODE://播放循环方式的改变
				changeMode();
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 改变播放循环模式
	 */
	public void changeMode(){
		playMode ++;
		if(playMode>4){
			playMode =1;
		}
		binder.playMode(playMode);//通知客户端，改变UI
		SharedPreUtils.setPlayMode(context, playMode);
	}
	
	/**
	 * 同步播放队列
	 */
	public void syncMusicData(){
		playMode = SharedPreUtils.currentPlayMode(context);
		play_queue = app.getPlay_queue();
		currentIndex = app.getCurrent_index();
	}
	
	/**
	 * 获取播放队列的数量
	 * @return
	 */
	public int getSize(){
		if(null != play_queue && play_queue.size()>0){
			return play_queue.size();
		}else{
			return 0;
		}
	}
	
	/**
	 * 更新当前播放的音乐信息
	 */
	public void updateCurrentMusic(){
		if(getSize()>0){
			currentSong = play_queue.get(currentIndex);
			app.setCurrent_index(currentIndex);
			app.setCurrentMusic(currentSong);
			songPath = currentSong.getMusic_url();
			//判断数据库（下载）中是否有该音乐，有的话替换播放路径
			SongInfoEntity info = db_download.getMusicInfoByUrl(currentSong.getMusic_url());
			if(info!=null){
				songPath = info.getSavePath();
			}
			app.setPlayPath(songPath);
		} else {
			ActivityUtil.showShortToast(context, "播放队列为空");
			return;
		}
		play();
	}
	
	
	/**
	 * 播放音乐
	 * @param position
	 */
	Thread t;
	private void play() {
		try {
			if(mediaPlayer.isPlaying())
				mediaPlayer.stop();
			isPlaying = true;
			updateNotification();
			t = new Thread(new Runnable() {
				@Override
				public void run() {
					playUrl();
				}
			});
			t.start();
			binder.playStart(currentSong,currentIndex);
			isPrepared = false;
			handler.sendEmptyMessage(1);
			addHistory(currentSong);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void playUrl(){
		try {
			mediaPlayer.reset(); //把各项参数恢复到初始状态
			mediaPlayer.setDataSource(songPath);
			mediaPlayer.prepareAsync(); // prepare自动播放
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 暂停音乐
	 */
	private void pause() {
		if (mediaPlayer != null) {
			isPlaying = false;
			mediaPlayer.pause();
			binder.playPause();
			updateNotification();
		}
	}
	
	/**
	 * 继续播放
	 */
	private void resume() {
		if (!isPlaying) { // 如果是暂停状态
			isPlaying = true;
			mediaPlayer.start();
			binder.playResume();
			updateNotification();
		}
	}

	/** 
     * 停止音乐 
     */  
    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); 
            try {
            	isPlaying = false;
            	binder.playPause();
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数  
            } catch (Exception e) {  
                e.printStackTrace();  
            }
        }
    }
    
    /**
   	 * 上一首
   	 */
   	private void previous() {
   		if(getSize()==0)
   			return;
   		if(currentIndex == 0){
   			currentIndex = getSize()-1;
   			updateCurrentMusic();
   		}else{
   			if(currentIndex-1>=0&&currentIndex-1<=getSize()-1){
   	   			currentIndex --;
   	   			updateCurrentMusic();
   	   		}
   		}
   	}
       
   	/**
   	 * 下一首
   	 */
   	private void next() {
   		if(getSize()==0)
   			return;
   		if(currentIndex == getSize()-1){
   			currentIndex = 0;
   			updateCurrentMusic();
   		}else{
   			if(currentIndex+1<=getSize()-1){
   	   			currentIndex ++;
   	   			updateCurrentMusic();
   	   		}
   		}
   	}
    
   	/**
   	 * 单曲播放结束监听
   	 */
   	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
   		if(isPrepared){
   			if(currentSong!=null){
   				task_count(currentSong.getMusic_id());
   			}
			isPlaying = false;
	   		binder.playComplete();
	   		updateNotification();
			if(getSize()==0)
				return;
			switch (playMode) {
			case MODE_NORMAL://顺序播放
				if (currentIndex+1 <= getSize() - 1) {
					currentIndex ++; // 下一首位置
					updateCurrentMusic();
				}
				break;
			case MODE_REPEAT_ALL://循环播放
				if(currentIndex+1 <= getSize() - 1){
					currentIndex ++; // 下一首位置
					updateCurrentMusic();
				}else{
					currentIndex = 0;
					updateCurrentMusic();
				}
				break;
			case MODE_REPEAT_ONE://单曲循环
				resume();
				break;
			case MODE_RANDOM://随机播放
				currentIndex = getRandomIndex(getSize()-1);
				updateCurrentMusic();
				break;
			default: break;
			}
		}
	}
	
   	/**
	 * 当完成prepareAsync方法时，将调用onPrepared方法，表明音频准备播放。
	 */
	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		isPrepared = true;
		mediaPlayer.start();// 准备播放
		if(!isPlaying){
			pause();
		}
	}
	
	/**
	 * MediaPlayer.prepare(),当MediaPlayer正在缓冲时，将调用该Activity的onBufferingUpdate方法。 */
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		binder.playBufferProgress(percent);
	}
	
	/**
	 * 获取随机播放位置索引
	 */
	protected int getRandomIndex(int end) {
		int index = (int) (Math.random() * end);
		return index;
	}
	
	/** 
     * handler 更新  progress 播放进度
     */
    Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1) {
                if(null != mediaPlayer) {
                    currentTime =  mediaPlayer.getCurrentPosition();//获取当前播放时间
                    durationTime = mediaPlayer.getDuration();//获取总播放时间
                    binder.playProgress(currentTime,durationTime);//通知客户端更新UI seekbar
                    handler.removeMessages(1);
                    handler.sendEmptyMessageDelayed(1, 300);
                }
            }
		}
    };
	
    
    /**
     * 播放出错监听
     */
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		mp.reset();
		currentIndex = 0;
		File file = new File(songPath);
		if (file.exists()) {
			ActivityUtil.showShortToast(context, "播放出错");
		} else {
			ActivityUtil.showShortToast(context, "文件不存在");
		}
		songPath = null;
		binder.playError();
		binder.playPause();
		return true;
	}
	
	//获取本地保存的历史记录
	class Async extends HandlerHelp {
		@Override
		public void doTask() {
			// TODO Auto-generated method stub
			old_history = (List<SongInfoEntity>) FileUtil.readObject(historyPath());//播放历史
			if(old_history != null&& old_history.size()>0){
				app.setPlay_history(old_history);
			}
		}
		@Override
		public void handler() {
			// TODO Auto-generated method stub
			play_history = app.getPlay_history();
		}
	}
	
	/**
	 * 添加到播放历史
	 * @param info
	 */
	public void addHistory(SongInfoEntity info){
		if (play_history != null && play_history.size() > 0) {
			for (int j = play_history.size() - 1; j >= 0; j--) {// 旧数据
				int old_id = Integer.parseInt(play_history.get(j).getMusic_id());
				int new_id = Integer.parseInt(info.getMusic_id());
				if (old_id == new_id) {
					play_history.remove(j);
					break;
				}
			}
		}
		play_history.add(0, info);
		removeHistory();
		app.setPlay_history(play_history);
	}
	/**
	 * 播放历史去重
	 */
	public void removeHistory(){
		if(play_history.size()>maxHistory){
			play_history.remove(play_history.size()-1);
			removeHistory();
		}
	}
	
	public String historyPath(){
		return context.getFilesDir().getAbsolutePath()+ "/play_history";
	}
	
	
	//通知栏---------------------start------------------------------------------------
	
	private NotificationManager mNotificationManager;
	private int NOTIFICATION_ID = 0x1;
	private RemoteViews rv;
	private ControlBroadcast mConrolBroadcast;
	
	private static final String BROADCAST_PAUSE = "com.xiaoxu.music.community.broadcast.pause";
	private static final String BROADCAST_NEXT = "com.xiaoxu.music.community.broadcast.next";
	private static final String BROADCAST_EXIT = "com.xiaoxu.music.community.broadcast.exit";
	
	//初始化notification，注册广播
	public void initNotification(){
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mConrolBroadcast = new ControlBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BROADCAST_PAUSE);
		filter.addAction(BROADCAST_NEXT);
		filter.addAction(BROADCAST_EXIT);
		registerReceiver(mConrolBroadcast, filter);
	}
	
	//更新通知栏更新UI(notification)
	public void updateNotification(){
		if(currentSong==null)
			return;
		SimpleUserEntity user = currentSong.getUser();
		String author = user!=null?user.getUser_nick():"未知艺术家";
		
		Notification notification = new Notification();
		
		Intent intent = new Intent(getApplicationContext(),PlayActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		rv = new RemoteViews(this.getPackageName(), R.layout.music_notification);
		
		notification.icon = R.drawable.logo;
		notification.contentIntent = pi;
		notification.contentView = rv;
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		rv.setTextViewText(R.id.music_name, currentSong.getMusic_name());
		rv.setTextViewText(R.id.music_writer, author);
		
		if(isPlaying){
			rv.setImageViewResource(R.id.btn_play, R.drawable.btn_style_pause);
		}else{
			rv.setImageViewResource(R.id.btn_play, R.drawable.btn_style_play);
		}
		
		//此处action不能是一样的 如果一样的 接受的flag参数只是第一个设置的值
		Intent pauseIntent = new Intent(BROADCAST_PAUSE);//播放暂停
		pauseIntent.putExtra("FLAG", CONTROL_PAUSE);
		PendingIntent pausePIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
		rv.setOnClickPendingIntent(R.id.btn_play, pausePIntent);
		
		Intent nextIntent = new Intent(BROADCAST_NEXT);//下一首
		nextIntent.putExtra("FLAG", CONTROL_NEXT);
		PendingIntent nextPIntent = PendingIntent.getBroadcast(this, 0, nextIntent, 0);
		rv.setOnClickPendingIntent(R.id.btn_next, nextPIntent);
		
		Intent exitIntent = new Intent(BROADCAST_EXIT);//关闭应用
		exitIntent.putExtra("FLAG", CONTROL_EXIT);
		PendingIntent exitPIntent = PendingIntent.getBroadcast(this, 0, exitIntent, 0);
		rv.setOnClickPendingIntent(R.id.btn_exit, exitPIntent);
		
		startForeground(NOTIFICATION_ID, notification);
	}
	
	//关闭notification通知
	private void cancelNotification() {
		stopForeground(true);
		mNotificationManager.cancel(NOTIFICATION_ID);
	}
	
	//播放控制广播，通知栏（notification）控制播放
	class ControlBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int flag = intent.getIntExtra("FLAG", -1);
			switch(flag) {
			case CONTROL_PAUSE:
				if(isPlaying){
					pause();
				}else{
					resume();
				}
				updateNotification();
				break;
			case CONTROL_NEXT:
				next();
				break;
			case CONTROL_EXIT:
				cancelNotification();
				stopService(new Intent(getApplicationContext(), MediaPlayerService.class));
				ExitAppliation.getInstance().exit();
				break;
			}
		}
	}
	
	//通知栏---------------------end------------------------------------------------
	
	/**
	 * 保存播放历史记录
	 */
	public void savaHistory(){
		SharedPreUtils.setOnlyWifi(context, true);
		play_history = app.getPlay_history();
		FileUtil.writeObject(app.getPlay_history(), historyPath());
	}
	
	/**
	 * 统计歌曲的   播放  次数
	 * @param id
	 */
	public void task_count(String id){
		CountMusicTask task = new CountMusicTask(context, id, 1, new BaseRequestCallBack() {
			@Override
			public void onResult(String result, int code, String alert, Object obj) {}
			@Override
			public void onFailure(HttpException arg0, String arg1) {}
		});
		task.excute();
	}
	
	@Override
	public void onDestroy() {
		savaHistory();
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		System.exit(0);
	}

}
