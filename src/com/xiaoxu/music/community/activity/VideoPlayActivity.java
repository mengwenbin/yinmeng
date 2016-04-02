package com.xiaoxu.music.community.activity;


import java.lang.reflect.InvocationTargetException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.view.RotateLoading;

public class VideoPlayActivity extends BaseNewActivity implements OnClickListener {

	// 请求路径
	private String URL;
	private Intent intent;
	private View xCustomView;
	private WebView web_View;
	private ImageButton left_btn;
	private TextView center_content;
	private FrameLayout video_fullView;// 全屏时视频加载view
	
	private CustomViewCallback xCustomViewCallback;
	private MyWebChromeClient chromeclient;
	
	private final String mPageName = "VideoPlayActivity";

	@Override
	public void setupView() {

		intent = getIntent();
		URL = intent.getStringExtra("url");
		rotateLoading = (RotateLoading) findViewById(R.id.loading);
		web_View = (WebView) findViewById(R.id.web_view);
		video_fullView = (FrameLayout) findViewById(R.id.video_fullView);
		center_content = (TextView) findViewById(R.id.title_center_content);
		String content = intent.getStringExtra("content");
		if (!content.equals("") && content != null) {
			center_content.setText(content);
		} else {
			center_content.setText("");
		}
		left_btn = (ImageButton) findViewById(R.id.title_left_btn);
		left_btn.setOnClickListener(this);
	}

	@Override
	public void setContent() {
		setContentView(R.layout.activity_playvideo);
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
		
		WebSettings wSet = web_View.getSettings();
		wSet.setUseWideViewPort(true);// 让网页自适应手机屏幕
		wSet.setJavaScriptEnabled(true);
		wSet.setLoadWithOverviewMode(true);
		wSet.setLoadsImagesAutomatically(true);// 设置可以自动加载图片
		wSet.setSavePassword(true);
		wSet.setSaveFormData(true);// 保存表单数据
		wSet.setGeolocationEnabled(true);// 启用地理定位
		wSet.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
		wSet.setDomStorageEnabled(true);
		// 设置可以放大缩小网页
		wSet.setSupportZoom(true);
		wSet.setBuiltInZoomControls(true);
		chromeclient = new MyWebChromeClient();
		web_View.setWebChromeClient(chromeclient);
		loadWebView();
	}

	private void loadWebView() {

		if (haveNetwork()) {
			web_View.loadUrl(URL);
			web_View.setWebViewClient(new WebViewClient() {

				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
					loadingStart(rotateLoading);
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
					loadingCancle(rotateLoading);
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {

					view.loadUrl(url);
					return true;
				}

			});
		} else {
			showNoNet(this);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.title_left_btn:
			finish();
			break;

		case R.id.no_network:
			hintNoNet();
			loadWebView();
			break;
		}
	}

	@Override
	protected void onStop() {

		super.onStop();
		try {
			web_View.getClass().getMethod("onPause").invoke(web_View, (Object[]) null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public class MyWebChromeClient extends WebChromeClient {
		private View xprogressvideo;

		// 播放网络视频时全屏会被调用的方法
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			web_View.setVisibility(View.INVISIBLE);
			// 如果一个视图已经存在，那么立刻终止并新建一个
			if (xCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			xCustomView = view;
			xCustomViewCallback = callback;
			video_fullView.addView(view);
			video_fullView.setVisibility(View.VISIBLE);
		}

		// 视频播放退出全屏会被调用的
		@Override
		public void onHideCustomView() {
			if (xCustomView == null)// 不是全屏播放状态
				return;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			xCustomView.setVisibility(View.GONE);
			video_fullView.removeView(xCustomView);
			xCustomView = null;
			video_fullView.setVisibility(View.GONE);
			xCustomViewCallback.onCustomViewHidden();
			web_View.setVisibility(View.VISIBLE);
		}

		// 视频加载时进程loading
		@Override
		public View getVideoLoadingProgressView() {
			if (xprogressvideo == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
			}
			return xprogressvideo;
		}
	}

	/**
	 * 判断是否是全屏
	 *
	 * @return
	 */
	public boolean inCustomView() {
		return (xCustomView != null);
	}

	/**
	 * 全屏时按返加键执行退出全屏方法
	 */
	public void hideCustomView() {
		chromeclient.onHideCustomView();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (inCustomView()) {
				hideCustomView();
				return true;
			} else {
				web_View.loadUrl("about:blank");
				finish();
			}
		}
		return false;
	}

}
