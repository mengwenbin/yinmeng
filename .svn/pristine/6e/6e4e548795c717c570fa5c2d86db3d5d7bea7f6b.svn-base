package com.xiaoxu.music.community;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.SimpleUserEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.CountMusicTask;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.NetUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.BaseAdapter;

public abstract class BaseNewAdapter extends BaseAdapter {
	
	public Activity activity;
	public Context context;
	
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private String share_content = "音萌，二次元造梦空间：http://www.inmoe.cn";
	private String share_path = "http://www.inmoe.cn";
	
	public BaseNewAdapter(Context context, Activity activity) {
		super();
		this.activity = activity;
		this.context = context;
		configPlatforms();
	}
	
	public boolean haveNetwork(){
		if (NetUtils.getAPNType(context) == -1) { //判断网络状态
			ActivityUtil.showShortToast(context, context.getString(R.string.no_network));
			return false;
		}else{
			return true;
		}
	}
	
	
	/**
	 * @param title 标题
	 * @param thumb 缩略图
	 * @param path 音乐路径
	 * @param author 作者
	 */
	public void shareSongMenu(String title,String thumb,String author) {
		mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
		UMImage image;
		if(!TextUtils.isEmpty(thumb)){
			image = new UMImage(activity,thumb);
		}else{
			image = new UMImage(activity, BitmapFactory
					.decodeResource(activity.getResources(), R.drawable.logo));
		}
		//设置QQ分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(share_content);
		qqShareContent.setShareImage(image);
		qqShareContent.setTargetUrl(share_path);
		mController.setShareMedia(qqShareContent);
		
		//设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(share_content);
		qzone.setShareImage(image);
		qzone.setTargetUrl(share_path);
		mController.setShareMedia(qzone);
		
		//设置新浪微博分享内容
		SinaShareContent sina = new SinaShareContent();
		if(!TextUtils.isEmpty(title)){
			sina.setTitle(title);
		}else{
			sina.setTitle("音萌");
		}
		sina.setShareContent(share_content);
		sina.setShareImage(image);
		sina.setTargetUrl(share_path);
		mController.setShareMedia(sina);
		
		//设置微信分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(share_content);
		weixinContent.setShareImage(image);
		weixinContent.setTargetUrl(share_path);
		mController.setShareMedia(weixinContent);
		
		//设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		//设置朋友圈title
		if(!TextUtils.isEmpty(title)){
			circleMedia.setTitle(title);
		}else{
			circleMedia.setTitle("音萌");
		}
		circleMedia.setShareContent(share_content);
		circleMedia.setShareImage(image);
		circleMedia.setTargetUrl(share_path);
		mController.setShareMedia(circleMedia);
		/**
		 * 弹出窗口
		 */
		mController.openShare(activity, false);
	}
	
	public void shareMusic(SongInfoEntity info) {
		if(info==null){
			return;
		}
		final String music_id = info.getMusic_id();
		String title = info.getMusic_name();
		String thumb = info.getMusic_coverimg();
		String path = info.getMusic_url();
		SimpleUserEntity user = info.getUser();
		String author = "";
		if(user!=null){
			author = user.getUser_nick();
		}
		String share_url = info.getShare_url();
		if(TextUtils.isEmpty(share_url)){
			share_url = share_path;
		}
		
		mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
		mController.getConfig().registerListener(new SocializeListeners.SnsPostListener() {
            
			@Override
            public void onStart() {
				CountMusicTask task = new CountMusicTask(context, music_id, 2, new BaseRequestCallBack() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {}
					
					@Override
					public void onResult(String result, int code, String alert, Object obj) {
					}
				});
				task.excute();
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,SocializeEntity arg2) {
				
			}
        });
		
		// 设置分享音乐
		UMusic uMusic = new UMusic(path);
		if(!TextUtils.isEmpty(author)){
			uMusic.setAuthor(author);
		}
		if(!TextUtils.isEmpty(title)){
			uMusic.setTitle(title);
		}else{
			uMusic.setTitle("音萌");
		}
		// 设置音乐缩略图
		if(!TextUtils.isEmpty(thumb)){
			uMusic.setThumb(thumb);
		}
		mController.setShareMedia(uMusic);
		
		//设置QQ分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareMedia(uMusic);
		qqShareContent.setTargetUrl(share_url);
		mController.setShareMedia(qqShareContent);
		
		//设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareMedia(uMusic);
		qzone.setTargetUrl(share_url);
		mController.setShareMedia(qzone);
		
		//设置新浪微博分享内容
		SinaShareContent sina = new SinaShareContent();
		if(!TextUtils.isEmpty(title)){
			sina.setTitle(title);
		}else{
			sina.setTitle("音萌");
		}
		sina.setShareContent("我正在听"+author+"的歌曲《"+title+"》,很喜欢，推荐给大家！"
				+ "（来自音萌APP，更多精彩戳这里→（"+share_url+"））");
		sina.setTargetUrl(share_url);
		mController.setShareMedia(sina);
		
		//设置微信分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareMedia(uMusic);
		weixinContent.setTargetUrl(share_url);
		mController.setShareMedia(weixinContent);
		
		//设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		//设置朋友圈title
		if(!TextUtils.isEmpty(title)){
			circleMedia.setTitle(title);
		}else{
			circleMedia.setTitle("音萌");
		}
		circleMedia.setShareContent("我正在听"+author+"的歌曲《"+title+"》,很喜欢，推荐给大家！"
					+ "（来自音萌APP，更多精彩戳这里→（"+share_url+"））");
		circleMedia.setTargetUrl(share_url);
		mController.setShareMedia(circleMedia);
		/**
		 * 弹出窗口
		 */
		mController.openShare(activity, false);
	}
	
	/**
	 * 配置分享平台参数</br>
	 */
	private void configPlatforms() {
		mController.getConfig().setSsoHandler(new SinaSsoHandler());// 添加新浪SSO授权
		addQQZonePlatform();// 添加QQ、QZone平台
		addWXPlatform();// 添加微信、微信朋友圈平台
		mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
	}

	private void addQQZonePlatform() {
		String appId = "1104749644";
		String appKey = "F19Aijc6m4n6AikM";
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, appId,appKey);// 添加QQ支持,并且设置QQ分享内容的target url
		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
		qqSsoHandler.addToSocialSDK();
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, appId,appKey);// 添加QZone平台
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		String appId = "wxa6b6ab6636ed733c";
		String appSecret = "0b23145d0f7b20329e29741d1ac43d9b";
		UMWXHandler wxHandler = new UMWXHandler(activity, appId, appSecret);// 添加微信平台
		wxHandler.addToSocialSDK();
		UMWXHandler wxCircleHandler = new UMWXHandler(activity, appId,appSecret);// 支持微信朋友圈
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

}
