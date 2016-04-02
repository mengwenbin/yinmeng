package com.xiaoxu.music.community.im.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.util.ImageUtils;
import com.easemob.util.PathUtil;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.im.applib.task.LoadLocalBigImgTask;
import com.xiaoxu.music.community.im.applib.utils.ImageCache;
import com.xiaoxu.music.community.view.photoview.PhotoView;

/**
 * 下载显示大图
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ShowBigImage extends BaseNewActivity {
	
	private Intent intent;
	private PhotoView image;
	private String localFilePath;
	private boolean isDownloaded;
	private TextView tv_progress;
	private ProgressBar loadLocalPb;
	private Bitmap bitmap;
	private int default_res = R.drawable.test_default_wait_img;
	
	private Uri uri;
	private String secret;
	private String remotepath;
	
	@Override
	public void setContent() {
	
		setContentView(R.layout.activity_show_big_image);
		intent = getIntent();
		uri = intent.getParcelableExtra("uri");
		remotepath = intent.getStringExtra("remotepath");
		secret = intent.getExtras().getString("secret");
		
	}
	
	@Override
	public void setupView() {
		
		image = (PhotoView) findViewById(R.id.image);
		tv_progress = getTextView(R.id.tv_progress);
		loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
		default_res = intent.getIntExtra("default_image", R.drawable.signin_local_gallry);

		//本地存在，直接显示本地的图片
		if (uri != null && new File(uri.getPath()).exists()) {
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			bitmap = ImageCache.getInstance().get(uri.getPath());
			if (bitmap == null) {
				LoadLocalBigImgTask task = new LoadLocalBigImgTask(this, uri.getPath(), image, loadLocalPb, ImageUtils.SCALE_IMAGE_WIDTH,
						ImageUtils.SCALE_IMAGE_HEIGHT);
				if (android.os.Build.VERSION.SDK_INT > 10) {
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					task.execute();
				}
			} else {
				image.setImageBitmap(bitmap);
			}
		} else if (remotepath != null) { 
			//去服务器下载图片
			Map<String, String> maps = new HashMap<String, String>();
			if (!TextUtils.isEmpty(secret)) {
				maps.put("share-secret", secret);
			}
			downloadImage(remotepath, maps);
		} else {
			image.setImageResource(default_res);
		}
	}

	@Override
	public void setModel() {
	}
	
	/**
	 * 通过远程URL，确定下本地下载后的localurl
	 * @param remoteUrl
	 * @return
	 */
	public String getLocalFilePath(String remoteUrl){
		String localPath;
		if (remoteUrl.contains("/")){
			localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/"
					+ remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
		}else{
			localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/" + remoteUrl;
		}
		return localPath;
	}
	
	/**
	 * 下载图片
	 * @param remoteFilePath
	 */
	private void downloadImage(final String remoteFilePath, final Map<String, String> headers) {
		
		loadLocalPb.setVisibility(View.VISIBLE);
		localFilePath = getLocalFilePath(remoteFilePath);
		final EMCallBack callback = new EMCallBack() {
			public void onSuccess() {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(metrics);
						int screenWidth = metrics.widthPixels;
						int screenHeight = metrics.heightPixels;

						loadLocalPb.setVisibility(View.GONE);
						tv_progress.setVisibility(View.GONE);
						bitmap = ImageUtils.decodeScaleImage(localFilePath, screenWidth, screenHeight);
						if (bitmap == null) {
							image.setImageResource(default_res);
						} else {
							image.setImageBitmap(bitmap);
							ImageCache.getInstance().put(localFilePath, bitmap);
							isDownloaded = true;
						}
						
					}
				});
			}

			public void onError(int error, String msg) {
				File file = new File(localFilePath);
				if (file.exists()&&file.isFile()) {
					file.delete();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						loadLocalPb.setVisibility(View.GONE);
						tv_progress.setVisibility(View.GONE);
						image.setImageResource(default_res);
					}
				});
			}

			public void onProgress(final int progress, String status) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv_progress.setText(getString(R.string.Download_the_pictures_new) + progress + "%");
					}
				});
			}
		};

	    EMChatManager.getInstance().downloadFile(remoteFilePath, localFilePath, headers, callback);

	}

	@Override
	public void onBackPressed() {
		if (isDownloaded)
			setResult(RESULT_OK);
		finish();
	}

}
