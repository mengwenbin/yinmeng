package com.xiaoxu.music.community.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.util.ImageUtils;
import com.xiaoxu.music.community.view.CustomProgressDialog;

public class SongMenuSummaryActivity extends BaseNewActivity implements OnClickListener {

	private Intent intent;
	private ImageView close_iv;
	private ImageView song_cover;
	private TextView tv_musiclist_name;
	private TextView tv_musiclist_summary;
	
	private MyAsyncTask asyncTask;
	private String musiclist_img;
	private String musiclist_name;
	private String musiclist_summary;
	
	private final String mPageName = "SongMenuSummaryActivity";
	@Override
	public void setupView() {
		
		bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
		close_iv = getImageView(R.id.close);
		song_cover = getImageView(R.id.song_cover);
		tv_musiclist_name = getTextView(R.id.musiclist_name);
		tv_musiclist_summary = getTextView(R.id.musiclist_summary);
		
		close_iv.setOnClickListener(this);
	}

	@Override
	public void setContent() {
		setContentView(R.layout.activity_songsummary);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		intent = getIntent();
		musiclist_img = intent.getStringExtra("musiclist_img");
		musiclist_name = intent.getStringExtra("musiclist_name");
		musiclist_summary = intent.getStringExtra("musiclist_summary");
		
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
		
		tv_musiclist_name.setText(musiclist_name);
		tv_musiclist_name.setText(musiclist_name+"");
		tv_musiclist_summary.setText("简介： "+musiclist_summary+"");
		
		if(!musiclist_img.equals("")){
			asyncTask = new MyAsyncTask();
			asyncTask.execute(musiclist_img);
		}else{
			
			Bitmap bitmap = ((BitmapDrawable) song_cover.getDrawable()).getBitmap();
			Bitmap Reflectionbitmap = ImageUtils.getReflectionImageWithOrigin(bitmap);
			song_cover.setImageBitmap(Reflectionbitmap);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.close:
			finish();
			break;
		}
	}
	
	private class MyAsyncTask extends AsyncTask<String, Void, Bitmap>{

		protected void onPreExecute() {
			CustomProgressDialog.show(context, true, null).setCanceledOnTouchOutside(false);;
		};
		@Override
		protected Bitmap doInBackground(String... params) {
			
			URL url;
			Bitmap bitmap = null;
			try {
				//URL对象  
				url = new URL(params[0]);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接  
				connection.setRequestMethod("GET"); //使用get请求  
				connection.setDoInput(true);  
				connection.connect();  
				connection.setConnectTimeout(5000);
				int code = connection.getResponseCode();
				if(code == 200){
					InputStream is = connection.getInputStream();  
					bitmap = BitmapFactory.decodeStream(is);  
					is.close();  
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		
		protected void onPostExecute(Bitmap result) {
			CustomProgressDialog.disMiss();
			Bitmap Reflectionbitmap = ImageUtils.getReflectionImageWithOrigin(result);
			song_cover.setImageBitmap(Reflectionbitmap);
		};
		
		
	}

}
