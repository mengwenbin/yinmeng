package com.xiaoxu.music.community.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.MusicLocalAdapter;
import com.xiaoxu.music.community.entity.MusicLocalInfoEntity;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.CustomProgressDialog;

/*
 * 读取本地音乐文件模块
 */
public class UploadMusicActivity extends BaseNewActivity implements
		OnItemClickListener, OnClickListener{

	private ListView listView;
	private ImageButton left_btn;
	private ImageButton right_btn;
	private TextView center_content;
	private MusicLocalAdapter adapter;
	private SwipeRefreshLayout mSwipeLayout;
	private List<MusicLocalInfoEntity> SongList;
	
	private final String mPageName = "UploadMusicActivity";
	private String Login_Tag = "com.xiaoxu.music.community.activity.UploadMusicActivity";

	@Override
	public void setupView() {
		left_btn = (ImageButton) findViewById(R.id.title_left_btn);
		right_btn = (ImageButton) findViewById(R.id.title_right_btn);
		center_content = (TextView) findViewById(R.id.title_center_content);
		center_content.setText("上传歌曲列表");
		right_btn.setVisibility(View.GONE);
		listView = (ListView) findViewById(R.id.music_lv);
		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light, android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
	}

	@Override
	public void setContent() {
		overridePendingTransition(R.anim.push_up_out,R.anim.push_up_in);
		setContentView(R.layout.activity_local_music);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
		MobclickAgent.onResume(this);//友盟统计
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}

	@Override
	public void setModel() {
		if(!AccountInfoUtils.getInstance(context).isLogin()){
			Intent intent = new Intent(context, StartActivity.class);
			intent.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
			startActivityForResult(intent, 1);
		}
		adapter = new MusicLocalAdapter(context, SongList);
		listView.setAdapter(adapter);
		initAdapter();
		left_btn.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				initAdapter();
			}
		});
	}

	private void initAdapter(){
		
		new AsyncTask<String, Void, List<MusicLocalInfoEntity>>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				CustomProgressDialog.show(context, true, null).setCanceledOnTouchOutside(false);
			}

			@Override
			protected void onPostExecute(List<MusicLocalInfoEntity> result) {
				super.onPostExecute(result);
				CustomProgressDialog.disMiss();
				adapter.setItem(result);
				adapter.notifyDataSetChanged();
			}

			@Override
			protected List<MusicLocalInfoEntity> doInBackground(
					String... params) {
				SongList = readMusicFromSD();
				return SongList;
			}
		}.execute("");
		mSwipeLayout.setRefreshing(false);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(context, AddMusicInfoActivity.class);
		intent.putExtra("song", SongList.get(position));
		startActivity(intent);
		finish();
	}
	
	private List<MusicLocalInfoEntity> readMusicFromSD() {
		Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { 
				MediaStore.Audio.Media._ID,				//0  歌曲ID
				MediaStore.Audio.Media.DISPLAY_NAME,	//1  文件名
				MediaStore.Audio.Media.TITLE,			//2	歌曲名
				MediaStore.Audio.Media.DURATION,		//3	总播放时长
				MediaStore.Audio.Media.ARTIST,			//4	歌手名	
				MediaStore.Audio.AudioColumns.ALBUM_KEY,//5
				MediaStore.Audio.Media.ALBUM,			//6	专辑名
				MediaStore.Audio.Media.YEAR,			//7
				MediaStore.Audio.Media.MIME_TYPE,		//8
				MediaStore.Audio.Media.SIZE,			//9	文件的大小
				MediaStore.Audio.Media.DATA },			//	文件的路径
				null,null, null);
		MusicLocalInfoEntity song = null;
		List<MusicLocalInfoEntity> musicList = new ArrayList<MusicLocalInfoEntity>();
		while (cursor.moveToNext()) {
			song = new MusicLocalInfoEntity();
			song.setFilename(cursor.getString(1));// 文件名
			song.setMusicname(cursor.getString(2));// 歌曲名
			song.setDuration(TimeUtil.formatTime(cursor.getInt(3)));// 时长
			song.setSinger(cursor.getString(4));// 歌手名
			// 取得歌曲对应的专辑对应的Key
			if (cursor.getString(7) != null) {// 年代
				song.setYear(cursor.getString(7));
			} else {
				song.setYear("未知");
			}
			if (cursor.getString(9) != null) {// 文件大小
				float temp = cursor.getInt(9) / 1024f / 1024f;
				song.setSize(String.format("%.2f", temp) + "M");
			} else {
				song.setSize("未知");
			}
			if (cursor.getString(10) != null) {// 文件路径
				song.setFileurl(cursor.getString(10));
			}
			if(".mp3".equals(StringUtil.cutLastString(song.getFilename()))){
				musicList.add(song);
			}
		}
		cursor.close();
		return musicList;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:
			finish();
			break;

		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1){
			if(!AccountInfoUtils.getInstance(context).isLogin()){
				finish();
			}
		}
	}

}
