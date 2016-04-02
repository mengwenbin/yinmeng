package com.xiaoxu.music.community.im.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.entity.MusicLocalInfoEntity;
import com.xiaoxu.music.community.im.adapter.MusicFileAdapter;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.CustomProgressDialog;

public class SelectFileActivity extends BaseNewActivity implements OnClickListener{

	private Intent intent;
	private String username;
	private TextView confirm;
	private ListView listView;
	private ImageButton left_btn;
	private TextView center_content;
	private MusicFileAdapter adapter;
	private SwipeRefreshLayout mSwipeLayout;
	private List<MusicLocalInfoEntity> SongList;

	@Override
	public void setupView() {

		left_btn = (ImageButton) findViewById(R.id.left_btn);
		confirm = (TextView) findViewById(R.id.right_text);
		center_content = (TextView) findViewById(R.id.title_center_content);
		center_content.setText("选择文件");
		confirm.setText("确定");
		listView = (ListView) findViewById(R.id.music_lv);
		mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light, android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		
		left_btn.setOnClickListener(this);
		confirm.setOnClickListener(this);
		
	}

	@Override
	public void setContent() {
		setContentView(R.layout.activity_selectfile);
		intent = getIntent();
		username = intent.getStringExtra("username");
	}

	@Override
	public void setModel() {
		
		adapter = new MusicFileAdapter(SelectFileActivity.this, SongList,username);
		listView.setAdapter(adapter);
		initAdapter();
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
				CustomProgressDialog.show(context, true, null)
						.setCanceledOnTouchOutside(false);
			}

			@Override
			protected void onPostExecute(List<MusicLocalInfoEntity> result) {
				super.onPostExecute(result);

				CustomProgressDialog.disMiss();
				adapter.setList(result);
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
	/**
	 * 读取sd卡中的音乐文件
	 * @return
	 * @throws Exception
	 */
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
			String musicAlbumKey = cursor.getString(5);
			String[] argArr = {musicAlbumKey};
			ContentResolver albumResolver = context.getContentResolver();
			Cursor albumCursor = albumResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
					MediaStore.Audio.AudioColumns.ALBUM_KEY + " = ?", argArr,null);
			if (null != albumCursor && albumCursor.getCount() > 0) {
				albumCursor.moveToFirst();
				int albumArtIndex = albumCursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART);
				String musicAlbumArtPath = albumCursor.getString(albumArtIndex);
				if (null != musicAlbumArtPath && !"".equals(musicAlbumArtPath)) {
					song.setMusicurl(musicAlbumArtPath);
				} else {
					song.setMusicurl("");
				}
			} else {
				song.setMusicurl("");
			}
			song.setAlbum(cursor.getString(6));// 专辑名
			if (cursor.getString(7) != null) {// 年代
				song.setYear(cursor.getString(7));
			} else {
				song.setYear("未知");
			}
			if ("audio/mpeg".equals(cursor.getString(8).trim())) {// 歌曲格式
				song.setType("mp3");
			} else if ("audio/x-ms-wma".equals(cursor.getString(8).trim())) {
				song.setType("wma");
			}
			if (cursor.getString(9) != null) {// 文件大小
				float temp = cursor.getInt(9) / 1024f / 1024f;
				String sizeStr = (temp + "").substring(0, 4);
				song.setSize(sizeStr + "M");
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
		case R.id.right_text:
			
			break;
		}
	}
	
}
