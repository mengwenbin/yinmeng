package com.xiaoxu.music.community.activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.view.RotateLoading;

public class WebViewActivity extends BaseNewActivity implements OnClickListener{

	//请求路径
	private String URL;
	private Intent intent;
	private WebView web_View;
	private ImageButton left_btn; 
	private ImageButton right_btn; 
	private TextView center_content;
	
	private final String mPageName = "WebViewActivity";
	@Override
	public void setupView() {

		intent = getIntent();
		URL = intent.getStringExtra("url");
		rotateLoading = (RotateLoading) findViewById(R.id.loading);
		web_View = (WebView) findViewById(R.id.web_view);
		center_content = (TextView) findViewById(R.id.title_center_content);
		String content = intent.getStringExtra("content");
		if(!content.equals("") && content != null){
			center_content.setText(content);
		}else{
			center_content.setText("");
		}
		left_btn = (ImageButton) findViewById(R.id.title_left_btn);
		right_btn = (ImageButton) findViewById(R.id.title_right_btn);
		right_btn.setVisibility(View.GONE);
		left_btn.setOnClickListener(this);
		WebSettings wSet = web_View.getSettings();
		
		wSet.setUseWideViewPort(true);//让网页自适应手机屏幕
		wSet.setJavaScriptEnabled(true);
		wSet.setLoadWithOverviewMode(true);
		wSet.setLoadsImagesAutomatically(true);// 设置可以自动加载图片  
		//设置可以放大缩小网页
		wSet.setSupportZoom(true);
		wSet.setBuiltInZoomControls(true);
	}

	@Override
	public void setContent() {
		setContentView(R.layout.huodong_details);
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
		loadWebView();
	}
	
	private void loadWebView(){
		
		if(haveNetwork()){
			web_View.loadUrl(URL); 
			web_View.setWebViewClient(new WebViewClient(){
				
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
					loadingStart(rotateLoading);
				}
				
				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
					loadingCancle(rotateLoading);
				}
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url){
                	
                    view.loadUrl(url);
                    return true;
                }
                
        });
		}else{
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
	
}
