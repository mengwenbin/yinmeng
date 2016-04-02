package com.xiaoxu.music.community.service;

import java.io.File;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.dao.DownLoadDB;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.model.task.DownLoadTask;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.FileUtil;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class DownLoadService extends IntentService {

	private DownLoadTask task;
	private Context context;
	private SongInfoEntity info;
	private DownLoadDB db;
	
	public static String KEY_DOWNLOAD = "musicdownload";
	
	public DownLoadService() {
		super("DownLoadService");
	}
	
	public DownLoadService(String name) {
		super("DownLoadService");
		// TODO Auto-generated constructor stub
	}
	
	// 1服务创建
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		db = new DownLoadDB(context);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		info = (SongInfoEntity) intent.getSerializableExtra(KEY_DOWNLOAD);
		if(info==null){
			return;
		}
		if(info.getUser() != null ){
			info.setUser_nick(info.getUser().getUser_nick());
			info.setUser_sex(info.getUser().getUser_sex());
			info.setUser_img(info.getUser().getUser_img());
			info.setIs_attention(info.getUser().getIs_attention());
			info.setUser_summary(info.getUser().getUser_summary());
		}
		String url = info.getMusic_url();
		String path = FileUtil.getDownLoadMusicSavaPath(context, info.getMusic_name());
		info.setSavePath(path);
		if(db.getMusicInfoById2(info.getMusic_id())==null){
			db.add(info);
		}
		downLoadTask(url, path);
	}
	/**
	 * 请求路径，保存路径
	 * @param url
	 * @param path
	 */
	public void downLoadTask(String url, String path) {
		task = new DownLoadTask(context, url, path, callback);
		task.excute();
	}

	RequestCallBack<File> callback = new RequestCallBack<File>() {
		
		int progress = 0;
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(ResponseInfo<File> arg0) {
			File file = arg0.result;
			String savapath = file.getAbsolutePath();
			info.setIsfinish(true);
			info.setSavePath(savapath);
			db.updateById(info);
			ActivityUtil.showShortToast(context, info.getMusic_name()+"下载完成");
		}
		
		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			// TODO Auto-generated method stub
			super.onLoading(total, current, isUploading);
			progress = (int) (current * 100 / total);
		}
		
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			if (FileUtil.fileIsExists(info.getSavePath())) {
				if (info.getMusic_size().equals(String.valueOf(FileUtil.getFileSizes(new File(info.getSavePath()))))) { 
					ActivityUtil.showShortToast(context, "已下载过该歌曲");
					task.cancelTask();
					info.setIsfinish(true);
					db.updateById(info);
				}else{
					ActivityUtil.showShortToast(context, info.getMusic_name()+"开始下载");
				}
			}else{
				ActivityUtil.showShortToast(context, info.getMusic_name()+"开始下载");
			}
		}

		@Override
		public void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
