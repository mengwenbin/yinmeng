package com.xiaoxu.music.community.activity;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.xiaoxu.music.community.ExitAppliation;
import com.xiaoxu.music.community.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class ShareAppActivity extends Activity implements OnClickListener {

	private Context context;
	private ImageButton share_qq, share_qzone, share_sina, share_wx,
			share_wxcircle;
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	SHARE_MEDIA platform;
	
	private ImageButton title_left_btn;
	
	private String share_title = "音萌";
	private String share_content = "音萌，二次元造梦空间：http://www.inmoe.cn";
	private String share_url = "http://www.inmoe.cn";
	private final String mPageName = "ShareAppActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_app);
		
		// 打开测试模式
		MobclickAgent.setDebugMode(true);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		ExitAppliation.getInstance().addActivity(this);
		context = this;
		init();
		configPlatforms();
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

	public void init() {
		title_left_btn = (ImageButton) findViewById(R.id.title_left_btn);
		
		share_qq = (ImageButton) findViewById(R.id.share_qq);
		share_qzone = (ImageButton) findViewById(R.id.share_qzone);
		share_sina = (ImageButton) findViewById(R.id.share_sina);
		share_wx = (ImageButton) findViewById(R.id.share_weixin);
		share_wxcircle = (ImageButton) findViewById(R.id.share_wxcircle);
		
		share_qq.setOnClickListener(this);
		share_qzone.setOnClickListener(this);
		share_sina.setOnClickListener(this);
		share_wx.setOnClickListener(this);
		share_wxcircle.setOnClickListener(this);
		
		title_left_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.share_qq:
			platform = SHARE_MEDIA.QQ;
			QQShareContent qqShareContent = new QQShareContent();
			// 设置分享title
			qqShareContent.setTitle(share_title);
			// 设置点击分享内容的跳转链接
			qqShareContent.setShareContent(share_content);
			qqShareContent.setTargetUrl(share_url);
			mController.setShareMedia(qqShareContent);
			share(false);
			break;
		case R.id.share_qzone:
			platform = SHARE_MEDIA.QZONE;
			QZoneShareContent qzone = new QZoneShareContent();
			qzone.setTitle(share_title);
			qzone.setShareContent(share_content);
			qzone.setTargetUrl(share_url);
			mController.setShareMedia(qzone);
			share(false);
			break;
		case R.id.share_sina:
			platform = SHARE_MEDIA.SINA;
			SinaShareContent sina = new SinaShareContent();
			sina.setTitle(share_title);
			sina.setShareContent(share_content);
			sina.setTargetUrl(share_url);
			mController.setShareMedia(sina);
			auth();
			break;
		case R.id.share_weixin:
			platform = SHARE_MEDIA.WEIXIN;
			// 设置微信好友分享内容
			WeiXinShareContent weixinContent = new WeiXinShareContent();
			// 设置title
			weixinContent.setTitle(share_title);
			weixinContent.setShareContent(share_content);
			// 设置分享内容跳转URL
			weixinContent.setTargetUrl(share_url);
			mController.setShareMedia(weixinContent);
			share(false);
			break;
		case R.id.share_wxcircle:
			platform = SHARE_MEDIA.WEIXIN_CIRCLE;
			// 设置微信朋友圈分享内容
			CircleShareContent circleMedia = new CircleShareContent();
			circleMedia.setTitle(share_title);
			circleMedia.setShareContent(share_content);
			circleMedia.setTargetUrl(share_url);
			mController.setShareMedia(circleMedia);
			share(false);
			break;
		default:
			break;
		}
	}

	/**
	 * 判读是否获取授权
	 */
	public void auth() {
		if (!OauthHelper.isAuthenticated(context, platform)) {
			mController.doOauthVerify(context, platform, authlistener);
		} else {
			share(false);
		}
	}

	/**
	 * share
	 * @param isDirectShare
	 *            是否调用直接分享
	 */
	public void share(boolean isDirectShare) {
		if (isDirectShare) {
			mController.directShare(context, platform, mShareListener);
		} else {
			// 调用直接分享, 但是在分享前用户可以编辑要分享的内容
			mController.postShare(context, platform, mShareListener);
		}
	}

	UMAuthListener authlistener = new UMAuthListener() {

		@Override
		public void onStart(SHARE_MEDIA platform) {
//			Toast.makeText(context, "授权开始", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onError(SocializeException e, SHARE_MEDIA platform) {
//			Toast.makeText(context, "授权错误", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(Bundle value, SHARE_MEDIA platform) {
//			Toast.makeText(context, "授权完成", Toast.LENGTH_SHORT).show();
			// 获取相关授权信息或者跳转到自定义的分享编辑页面
			String uid = value.getString("uid");
			share(false);
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
//			Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * 分享监听器
	 */
	SnsPostListener mShareListener = new SnsPostListener() {
		
		@Override
		public void onStart() {
//			Toast.makeText(context, "开始分享.", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onComplete(SHARE_MEDIA platform, int stCode,
				SocializeEntity entity) {
			if (stCode == 200) {
				Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
			} else {
				String eMsg = "";
				if (stCode == -101) {
					eMsg = "没有授权";
					Toast.makeText(context, eMsg,Toast.LENGTH_SHORT).show();
					auth();
					return;
				}
//				Toast.makeText(context, "分享失败 : error code : " + stCode,Toast.LENGTH_SHORT).show();
			}
			
		}
	};

	/**
	 * 配置分享平台参数</br>
	 */
	private void configPlatforms() {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加QQ、QZone平台
		addQQZonePlatform();
		// 添加微信、微信朋友圈平台
		addWXPlatform();
		mController.getConfig().closeToast();
	}

	private void addQQZonePlatform() {
		String appId = "1104749644";
		String appKey = "F19Aijc6m4n6AikM";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ShareAppActivity.this, appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(ShareAppActivity.this, appId,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wxa6b6ab6636ed733c";
		String appSecret = "0b23145d0f7b20329e29741d1ac43d9b";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(ShareAppActivity.this, appId, appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(ShareAppActivity.this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig()
				.getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
