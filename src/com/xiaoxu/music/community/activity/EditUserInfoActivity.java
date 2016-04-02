package com.xiaoxu.music.community.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.ApplyEntity;
import com.xiaoxu.music.community.entity.UserEntity;
import com.xiaoxu.music.community.entity.UserInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.UpdateUserInfoTask;
import com.xiaoxu.music.community.model.task.UploadHeadTask;
import com.xiaoxu.music.community.parser.ParseUtil;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.FileUtil;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.view.CustomProgressDialog;
import com.xiaoxu.music.community.view.DateTimePickDialogUtil;
import com.xiaoxu.music.community.view.SelectPicPopupWindow;
import com.xiaoxu.music.community.view.custom_shape_imageview.CircleImageView;

/*
 * 修改用户信息
 */
public class EditUserInfoActivity extends BaseNewActivity{
	
	private Activity activity;
	private TextView apply_tv;
	private ImageButton right_btn;
	private ImageButton btn_back;
	private ImageView iv_sex;
	private CircleImageView iv_head;
	private TextView title_content, tv_name,tv_birthday,tv_wish,
	tv_life,tv_twitter,tv_qq,tv_wechat,tv_mailbox,tv_local,tv_summary;
	private RelativeLayout layout_nick, layout_sex,layout_birthday,
	layout_wish,layout_life,layout_twitter,layout_qq,layout_wechat,
	layout_mailbox,layout_local, layout_summary, layout_attestation;
	
	private UserInfoEntity user_info;
	private SelectPicPopupWindow menuWindow;
	private String savaPath; // 拍照图片保存路径
	private String imagepath;
	private String cropPath;
	private UploadHeadTask task_upload;//头像上传
	private int sexinfo = 0;// 性别
	private Map<String, String> updateMap;
	private UpdateUserInfoTask task_update;
	
	private String birth;
	private String birthday;
	private DateTimePickDialogUtil dateTimePicKDialog;
	private final String mPageName = "EditUserInfoActivity";
	public String Login_Tag = "com.xiaoxu.music.community.activity.EditUserInfoActivity";
	
	@Override
	public void setContent() {
		setContentView(R.layout.activity_edit_userinfo);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		activity = this;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
		MobclickAgent.onResume(activity);//友盟统计
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(activity);
	}
	
	@Override
	public void setupView() {
		
		bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_head_bg_blue);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_head_bg_blue);// 加载失败图片
		updateMap = new HashMap<String, String>();
		
		btn_back = getImageButton(R.id.title_left_btn);
		iv_head = (CircleImageView) getImageView(R.id.modify_head);
		right_btn = getImageButton(R.id.title_right_btn);
		initAnimation(right_btn);
		
		initTextView();
		initRelativeLayout();
		
		btn_back.setOnClickListener(onclick);
		right_btn.setOnClickListener(onclick);
	}
	
	/*
	 * 初始化TextView
	 */
	private void initTextView() {

		title_content = getTextView(R.id.title_center_content);
		title_content.setText(getString(R.string.edituser_title));
		tv_name = getTextView(R.id.modify_nick);
		iv_sex = getImageView(R.id.modify_sex);
		tv_local = getTextView(R.id.modify_local);
		tv_summary = getTextView(R.id.modify_summary);
		tv_birthday = getTextView(R.id.modify_birthday);
		tv_wish = getTextView(R.id.modify_wish);
		tv_life = getTextView(R.id.modify_life);
		tv_twitter = getTextView(R.id.modify_twitter);
		tv_qq = getTextView(R.id.modify_qq);
		tv_wechat = getTextView(R.id.modify_wechat);
		tv_mailbox = getTextView(R.id.modify_mailbox);
		apply_tv = getTextView(R.id.apply_tv);
		
	}

	/*
	 * 初始化RelativeLayout
	 */
	private void initRelativeLayout(){
		
		layout_nick = getRelativeLayout(R.id.edituser_layout_nick);
		layout_sex = getRelativeLayout(R.id.edituser_layout_sex);
		layout_local = getRelativeLayout(R.id.edituser_layout_local);
		layout_summary = getRelativeLayout(R.id.edituser_layout_summary);
		layout_birthday = getRelativeLayout(R.id.edituser_layout_birthday);
		layout_wish = getRelativeLayout(R.id.edituser_layout_wish);
		layout_life = getRelativeLayout(R.id.edituser_layout_life);
		layout_twitter = getRelativeLayout(R.id.edituser_layout_twitter);
		layout_qq = getRelativeLayout(R.id.edituser_layout_qq);
		layout_wechat = getRelativeLayout(R.id.edituser_layout_wechat);
		layout_mailbox = getRelativeLayout(R.id.edituser_layout_mailbox);
		layout_attestation = getRelativeLayout(R.id.edituser_layout_attestation);
	}
	
	@Override
	public void setModel() {
		if(!AccountInfoUtils.getInstance(activity).isLogin()){	//判断是否登录
			Intent i = new Intent(context, StartActivity.class);
			i.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
			startActivityForResult(i, 7);
			return;
		}
		user_info = AccountInfoUtils.getInstance(context).getUserInfo();
		updateUI();
	}
	
	@SuppressWarnings("deprecation")
	public void updateUI(){
		// Update-UI
		if(user_info==null){
			return;
		}
		
		String name = user_info.getUser_nick();
		String summary = user_info.getUser_summary();
		birthday = user_info.getUser_birthday();
		String wish = user_info.getUser_wish();
		String life = user_info.getUser_star();
		String micblog = user_info.getUser_micblog();
		String qq = user_info.getUser_qq();
		String wechat = user_info.getUser_weixin();
		String mailbox = user_info.getUser_mail();
		String local = user_info.getUser_local2();
		
		sexinfo = Integer.parseInt(user_info.getUser_sex());
		
		if(!TextUtils.isEmpty(name))
			tv_name.setText(name);//昵称
		
		if(!TextUtils.isEmpty(summary))//简介
			tv_summary.setText(summary.replace("简介：", ""));
		
		if(!TextUtils.isEmpty(birthday))//生日
			birthday = new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(birthday) * 1000).toString();
			tv_birthday.setText(birthday);
		
		if(!TextUtils.isEmpty(wish))//心愿
			tv_wish.setText(wish);
		
		if(!TextUtils.isEmpty(life))//本命
			tv_life.setText(life);
		
		if(!TextUtils.isEmpty(micblog))//微博昵称
			tv_twitter.setText(micblog);
		
		if(!TextUtils.isEmpty(qq))//QQ
			tv_qq.setText(qq);
		
		if(!TextUtils.isEmpty(wechat))//微信
			tv_wechat.setText(wechat);
		
		if(!TextUtils.isEmpty(mailbox))//邮箱
			tv_mailbox.setText(mailbox);
		
		if(!TextUtils.isEmpty(local))
			tv_local.setText(local);//所在地
		
		if(user_info.getStatus().equals("1")){
			apply_tv.setTextColor(getResources().getColor(R.color.red));
			apply_tv.setText("申请认证成功");
		}else if(user_info.getStatus().equals("-1")){
			apply_tv.setText("申请认证中...");
		}else if(user_info.getStatus().equals("0")){
			apply_tv.setText("申请认证");
		}
		
		updateSex();
		bitmapUtils.display(iv_head, StringUtil.replaceImagePath(user_info.getUser_img(), 100));
	}
	
	public void updateSex() {
		switch (sexinfo) { //性别
			case 0: iv_sex.setImageResource(R.drawable.sex_martian); break;
			case 1: iv_sex.setImageResource(R.drawable.sex_man); break;
			case 2: iv_sex.setImageResource(R.drawable.sex_woman); break;
			default: break;
		}
	}
	
	private Intent intent;
	public void onEditClick(View v){
		switch (v.getId()) {
		case R.id.edituser_layout_head:
			upDown();
			break;
		case R.id.edituser_layout_nick:
			intent = new Intent(context, EditContentActivity.class);
			intent.putExtra("type", "nick");
			intent.putExtra("key", tv_name.getText().toString());
			startActivityForResult(intent, 4);
			break;
		case R.id.edituser_layout_sex:
			initPopuptWindow(sexinfo);
			break;
		case R.id.edituser_layout_local:
			intent = new Intent(context, EditContentActivity.class);
			intent.putExtra("type", "local");
			intent.putExtra("key", tv_local.getText().toString());
			startActivityForResult(intent, 5);
			break;
		case R.id.edituser_layout_summary:
			intent = new Intent(context, EditContentActivity.class);
			intent.putExtra("type", "summary");
			intent.putExtra("key", tv_summary.getText().toString());
			startActivityForResult(intent, 6);
			break;
		case R.id.edituser_layout_attestation:
			
			String statu = user_info.getStatus();
			if(statu.equals("0")){
				
				if(haveNetwork()){
					task_update = new UpdateUserInfoTask(context, "-1", applycallback);
					task_update.excute();
				}
				
			}
			break;
		case R.id.edituser_layout_birthday:
			
			dateTimePicKDialog = new DateTimePickDialogUtil(activity, new SimpleDateFormat("yyyy年MM月dd日").format(Long.valueOf(user_info.getUser_birthday()) * 1000));
			tv_birthday = dateTimePicKDialog.dateTimePicKDialog(tv_birthday,new ClickListener());
			break;
		case R.id.edituser_layout_wish:
			
			intent = new Intent(context, EditContentActivity.class);
			intent.putExtra("type", "wish");
			intent.putExtra("key", tv_wish.getText().toString());
			startActivityForResult(intent, 8);
			
			break;
		case R.id.edituser_layout_life:
			
			intent = new Intent(context, EditContentActivity.class);
			intent.putExtra("type", "life");
			intent.putExtra("key", tv_life.getText().toString());
			startActivityForResult(intent, 9);
			
			break;
		case R.id.edituser_layout_twitter:
			
			intent = new Intent(context, EditContentActivity.class);
			intent.putExtra("type", "twitter");
			intent.putExtra("key", tv_twitter.getText().toString());
			startActivityForResult(intent, 10);
			
			break;
		case R.id.edituser_layout_qq:
			
			intent = new Intent(context, EditContentActivity.class);
			intent.putExtra("type", "qq");
			intent.putExtra("key", tv_qq.getText().toString());
			startActivityForResult(intent, 11);
			
			break;
		case R.id.edituser_layout_wechat:
			
			intent = new Intent(context, EditContentActivity.class);
			intent.putExtra("type", "wechat");
			intent.putExtra("key", tv_wechat.getText().toString());
			startActivityForResult(intent, 12);
			
			break;
		case R.id.edituser_layout_mailbox:
			
			intent = new Intent(context, EditContentActivity.class);
			intent.putExtra("type", "mailbox");
			intent.putExtra("key", tv_mailbox.getText().toString());
			startActivityForResult(intent, 13);
			
			break;
		}
	}
	
	private class ClickListener implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ok:
				birth = tv_birthday.getText().toString().trim();
				if(!birth.equals(birthday)){
					task();
				}
				dateTimePicKDialog.dialog.dismiss();
				break;

			case R.id.cancel:
				tv_birthday.setText(new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(user_info.getUser_birthday()) * 1000));
				dateTimePicKDialog.dialog.dismiss();
				break;
			}
		}
		
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 1:
			imagepath = savaPath;
			if (!FileUtil.fileIsExists(savaPath)) {
				return;
			}
			cutPic(imagepath);// 裁剪图片
			break;

		case 2:
			
			if (data != null) {
				ContentResolver resolver = context.getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				if (originalUri != null) {
					String[] imgs = { MediaStore.Images.Media.DATA };// 将图片URI转换成存储路径
					Cursor cursor = resolver.query(originalUri, imgs, null,null, null);
					if (cursor != null) {
						cursor.moveToFirst();
						int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						if (cursor.getString(index) != null) {
							imagepath = cursor.getString(index);
							cursor.close();
							if (FileUtil.fileIsExists(imagepath)) {
								cutPic(imagepath);// 裁剪图片
							}
						} else {
							ActivityUtil.showShortToast(context, "无权限访问选定的图片");
						}
					} else {
						ActivityUtil.showShortToast(context, "无权限访问选定的图片");
					}
				}
			}
			break;
		case 3:
			if (data == null) {
				return;
			}
			String savaPath = cropPath;
			if (FileUtil.fileIsExists(savaPath)&&haveNetwork()) {
				bitmapUtils.display(iv_head, savaPath);
				task_upload = new UploadHeadTask(context, savaPath, callback_upload);// 头像上传
				task_upload.excute();
			}
			break;
		case 4:
			if (data == null)
				return;
			String nick = data.getStringExtra("userinfo");
			if(nick.equals(user_info.getUser_nick())){
				return;
			}
			tv_name.setText(nick);
			task();
			break;
		case 5:
			if (data == null)
				return;
			String location = data.getStringExtra("userinfo");
			if(location.equals(user_info.getUser_local())){
				return;
			}
			tv_local.setText(location);
			task();
			break;
		case 6:
			if (data == null)
				return;
			String summary = data.getStringExtra("userinfo");
			if(summary.equals(user_info.getUser_summary())){
				return;
			}
			tv_summary.setText(summary);
			task();
			break;
		case 7:
			if(!AccountInfoUtils.getInstance(context).isLogin()){
				finish();
			}
			user_info = AccountInfoUtils.getInstance(context).getUserInfo();
			updateUI();
			break;
		case 8:
			if (data == null)
				return;
			String wish = data.getStringExtra("userinfo");
			if(wish.equals(user_info.getUser_wish())){
				return;
			}
			tv_wish.setText(wish);
			task();
			break;
		case 9:
			if (data == null)
				return;
			String life = data.getStringExtra("userinfo");
			if(life.equals(user_info.getUser_star())){
				return;
			}
			tv_life.setText(life);
			task();
			break;
		case 10:
			if (data == null)
				return;
			String micblog = data.getStringExtra("userinfo");
			if(micblog.equals(user_info.getUser_micblog())){
				return;
			}
			tv_twitter.setText(micblog);
			task();
			break;
		case 11:
			if (data == null)
				return;
			String qq = data.getStringExtra("userinfo");
			if(qq.equals(user_info.getUser_qq())){
				return;
			}
			if(ActivityUtil.isQQ(qq)){
				tv_qq.setText(qq);
				task();
			}else{
				ActivityUtil.showShortToast(context, "qq号码格式不正确！");
				tv_qq.setText(user_info.getUser_qq());
			}
			break;
		case 12:
			if (data == null)
				return;
			String wechat = data.getStringExtra("userinfo");
			if(wechat.equals(user_info.getUser_weixin())){
				return;
			}
			tv_wechat.setText(wechat);
			task();
			break;
		case 13:
			if (data == null)
				return;
			String mailbox = data.getStringExtra("userinfo");
			if(mailbox.equals(user_info.getUser_mail())){
				return;
			}
			if(!ActivityUtil.isEmail(mailbox)){
				ActivityUtil.showShortToast(context, "必须为电子邮箱");
				return;
			}
			tv_mailbox.setText(mailbox);
			task();
			break;
		}
	}
	
	
	Dialog dialog;
	protected void initPopuptWindow(int sex) {
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View view = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_popup_window_select_sex, null, false);
		RelativeLayout layout_man = (RelativeLayout) view.findViewById(R.id.popup_layout_man);
		RelativeLayout layout_woman = (RelativeLayout) view.findViewById(R.id.popup_layout_woman);
		RelativeLayout layout_martian = (RelativeLayout) view.findViewById(R.id.popup_layout_ladyboy);
		
		layout_man.setOnClickListener(onclick);
		layout_woman.setOnClickListener(onclick);
		layout_martian.setOnClickListener(onclick);
		
		ImageView check_man = (ImageView) view.findViewById(R.id.check_sex_man);
		ImageView check_woman = (ImageView) view.findViewById(R.id.check_sex_woman);
		ImageView check_martian = (ImageView) view.findViewById(R.id.check_sex_ladyboy);
		
		check_man.setVisibility(View.GONE);
		check_woman.setVisibility(View.GONE);
		check_martian.setVisibility(View.GONE);
		switch (sex) {
			case 0: check_martian.setVisibility(View.VISIBLE); break;
			case 1: check_man.setVisibility(View.VISIBLE); break;
			case 2: check_woman.setVisibility(View.VISIBLE); break;
			default:break;
		}
		dialog = new Dialog(context, R.style.Custom_Progress);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);// 按返回键是否取消
		dialog.show();
	}
	
	OnClickListener onclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.popup_layout_man:
				dialog.dismiss();
				sexinfo = 1;
				updateSex();
				task();
				break;
			case R.id.popup_layout_woman:
				dialog.dismiss();
				sexinfo = 2;
				updateSex();
				task();
				break;
			case R.id.popup_layout_ladyboy:
				dialog.dismiss();
				sexinfo = 0;
				updateSex();
				task();
				break;
			case R.id.title_left_btn:
				finish();
				break;
			case R.id.title_right_btn:
				startActivity(new Intent(context, PlayActivity.class));
				break;
			}
		}
	};
	
	
	/**
	 * -----发送修改的请求-----
	 */
	public void task() {
		
		String nick = tv_name.getText().toString().trim();
		String sex = sexinfo + "";
		String local = tv_local.getText().toString().trim();
		String summary = tv_summary.getText().toString().trim();
		String wish = tv_wish.getText().toString().trim();
		String life = tv_life.getText().toString().trim();
		String twitter = tv_twitter.getText().toString().trim();
		String qq = tv_qq.getText().toString().trim();
		String wechat = tv_wechat.getText().toString().trim();
		String mailbox = tv_mailbox.getText().toString().trim();
		
		if (!TextUtils.isEmpty(nick)) {
			if (String.valueOf(nick.charAt(0)).equals("1")) {
				ActivityUtil.showShortToast(context, "第一个字不能为1");
				return;
			}
			updateMap.put("User[user_nick]", nick);
		}
		if (!TextUtils.isEmpty(sex)) {
			updateMap.put("User[user_sex]", sex);
		}
		if (!TextUtils.isEmpty(local)) {
			updateMap.put("User[user_local2]", local);
		}
		if (!TextUtils.isEmpty(summary)) {
			updateMap.put("User[user_summary]", summary);
		}
		if(!TextUtils.isEmpty(birth)){
			updateMap.put("User[user_birthday]", birth);
		}
		if(!TextUtils.isEmpty(wish)){
			updateMap.put("User[user_wish]", wish);
		}
		if(!TextUtils.isEmpty(life)){
			updateMap.put("User[user_star]", life);
		}
		if(!TextUtils.isEmpty(twitter)){
			updateMap.put("User[user_micblog]", twitter);
		}
		if(!TextUtils.isEmpty(qq)){
			updateMap.put("User[user_qq]", qq);
		}
		if(!TextUtils.isEmpty(wechat)){
			updateMap.put("User[user_weixin]", wechat);
		}
		if(!TextUtils.isEmpty(mailbox)){
			updateMap.put("User[user_mail]", mailbox);
		}
		
		if(haveNetwork()){
			CustomProgressDialog.show(context, true, null).setCanceledOnTouchOutside(false);
			task_update = new UpdateUserInfoTask(context, updateMap, callback);
			task_update.excute();
		}
	}
	
	BaseRequestCallBack applycallback = new BaseRequestCallBack(ApplyEntity.class) {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			if(code != Constant.SUCCESS_REQUEST){
				return;
			}
			apply_tv.setText("申请认证中...");
			ActivityUtil.showShortToast(context, alert+" ");
			AccountInfoUtils.getInstance(context).setAccountJson(result);
			sendBroadcast(new Intent(Constant.ACTION_MINE_UPDATE_USERINFO));
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
		}
	};
	
	BaseRequestCallBack callback = new BaseRequestCallBack(UserEntity.class) {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			
			CustomProgressDialog.disMiss();
			ActivityUtil.showShortToast(context, alert+" ");
			if(code != Constant.SUCCESS_REQUEST){
				String name = user_info.getUser_nick();
				if(!TextUtils.isEmpty(name))
					tv_name.setText(name);//昵称
				return;
			}
			UserEntity user_entity = (UserEntity) obj;
			user_info = user_entity.getUser();
			AccountInfoUtils.getInstance(context).setAccountJson(result);
			Intent intent = new Intent(Constant.ACTION_MINE_UPDATE_USERINFO);
			intent.putExtra("isLogin", true);
			sendBroadcast(intent);
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			CustomProgressDialog.disMiss();
		}
	};
	
	
	
	
	/**
	 * 修改头像
	 */
	private void upDown() {
		menuWindow = new SelectPicPopupWindow(EditUserInfoActivity.this,itemsOnClick);
		menuWindow.showAtLocation(EditUserInfoActivity.this.findViewById(R.id.main),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	/*
	 * 【相机，图库】为弹出窗口实现监听类
	 */
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo://相机
				savaPath = createPhotoFile() + File.separator+ getCurrentTime() + ".jpg";
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
				Uri uri = Uri.fromFile(new File(savaPath));
				// 获取拍照后未压缩的原图片，并保存在uri路径中
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, 1);
				break;
			case R.id.btn_pick_photo://图库
				Intent intent1 = new Intent(Intent.ACTION_PICK);
				intent1.setType("image/*");// 相片类型
				startActivityForResult(intent1, 2);
				break;
			default:
				break;
			}
		}
	};
	
	public void cutPic(String imagePath) {
		cropPath = cropPhotoFile() + File.separator + getCurrentTime() + imagePath.substring(imagePath.lastIndexOf("."));
		Intent in = getCropImageIntent(Uri.fromFile(new File(imagePath)), Uri.fromFile(new File(cropPath)));
		startActivityForResult(in, 3);
	}
	
	private Intent getCropImageIntent(Uri fromPhotoUri, Uri toPhotoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(fromPhotoUri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, toPhotoUri);
		return intent;
	}
	
	/**
	 * 获取图片存储 （文件夹）路径
	 * @return path
	 */
	private String createPhotoFile() {
		// 文件夹路径
		String pathUrl = Constant.PHOTO_PATH;
		File file = new File(pathUrl);
		if (!file.exists()) {
			file.mkdirs();// 创建文件夹
		}
		return pathUrl;
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public String getCurrentTime() {
		// 获取时间String
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		return format.format(date);
	}
	
	/**
	 * 获取图片crop存储（文件夹）路径
	 * @return path
	 */
	private String cropPhotoFile() {
		// 文件夹路径
		String pathUrl = Constant.CROPIMG_PATH;
		File file = new File(pathUrl);
		if (!file.exists()) {
			file.mkdirs();// 创建文件夹
		}
		return pathUrl;
	}
	
	private ProgressDialog progress_dialog;
	private int progress = 0;
	
	RequestCallBack<String> callback_upload = new RequestCallBack<String>() {
		
		@Override
		public void onStart() {
			super.onStart();
			progress_dialog = new ProgressDialog(context);
			progress_dialog.setTitle("头像上传中");
			progress_dialog.setMessage("请稍候...");
			progress_dialog.setProgress(progress);
			progress_dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progress_dialog.show();
		}
		
		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			super.onLoading(total, current, isUploading);
			progress = (int) (current * 100 / total);
			progress_dialog.setProgress(progress);
			if (progress == 100) {
				progress_dialog.dismiss();
			}
		}
		
		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			progress_dialog.dismiss();
			String json = arg0.result;
			int code = ParseUtil.parseCode(json);
			if (code != 200) {
				ActivityUtil.showShortToast(context, "上传失败");
				return;
			}
			ActivityUtil.showShortToast(context, "上传成功");
			AccountInfoUtils.getInstance(context).setAccountJson(json);
			Intent intent =  new Intent(Constant.ACTION_MINE_UPDATE_USERINFO);
			intent.putExtra("isLogin", true);
			sendBroadcast(intent);
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			progress_dialog.dismiss();
		}
	};

}
