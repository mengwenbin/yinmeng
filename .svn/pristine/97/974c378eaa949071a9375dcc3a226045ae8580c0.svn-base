package com.xiaoxu.music.community.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.EditMineSongTask;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.FileUtil;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.view.SelectPicPopupWindow;

/*
 * 编辑歌单简介详情
 */
public class EditSongInfoActivity extends BaseNewActivity implements OnClickListener {

	private Intent intent;
	private ImageButton left_btn;
	private ImageView song_cover;
	private TextView save_edit;
	private TextView center_title;
	private EditText edit_songname;
	private EditText edit_songintroduce;
	
	private TextView tv_progress;
	private EditMineSongTask task;
	
	private String imagepath = "";
	private String cropPath = "";
	private String savaPath = "";// 拍照图片保存路径
	private SelectPicPopupWindow menuWindow;
	
	private int progress = 0;
	public boolean isUploading = false;
	
	private int pageNum;
	private String musiclist_id;
	private String musiclist_img;
	private String musiclist_name;
	private String musiclist_summary;
	private String musiclist_namevalue;
	private String musiclist_summaryvalue;
	
	private final String mPageName = "EditSongInfoActivity";
	
	@Override
	public void setupView() {
		
		rotateLoading = getRotateLoading(R.id.loading);
		tv_progress = getTextView(R.id.progress);
		left_btn = getImageButton(R.id.title_left_btn);
		save_edit = getTextView(R.id.save_edit);
		song_cover = getImageView(R.id.song_cover);
		center_title = getTextView(R.id.title_center_content);
		center_title.setText("编辑歌单信息");
		edit_songname = getEditText(R.id.song_music);
		edit_songintroduce = getEditText(R.id.song_introduce);
		
		edit_songname.setText(musiclist_name+"");
		edit_songintroduce.setText(musiclist_summary+"");
		
		
		left_btn.setOnClickListener(this);
		song_cover.setOnClickListener(this);
		save_edit.setOnClickListener(this);
		edit_songname.addTextChangedListener(new TextChangedListener(edit_songname));
	}

	@Override
	public void setContent() {
		setContentView(R.layout.activity_editsonginfo);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		intent = getIntent();
		musiclist_id = intent.getStringExtra("musiclist_id");
		pageNum = intent.getIntExtra("pageNum",0);
		musiclist_name = intent.getStringExtra("musiclist_name");
		musiclist_summary = intent.getStringExtra("musiclist_summary");
		musiclist_img = intent.getStringExtra("musiclist_img");
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
		
		bitmapUtils.configDefaultLoadingImage(R.drawable.default_convering);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_convering);// 加载失败图片
		bitmapUtils.display(song_cover, StringUtil.replaceImagePath(musiclist_img, 300));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.title_left_btn:
			finish();
			break;
		case R.id.song_cover:
			upDown();
			break;

		case R.id.save_edit:
			
			musiclist_namevalue = edit_songname.getText().toString().trim();
			musiclist_summaryvalue = edit_songintroduce.getText().toString().trim();
			
			if(musiclist_namevalue.equals(musiclist_name) && musiclist_summaryvalue.equals(musiclist_summary) && cropPath.equals("")){
				finish();
			}else{
				
				if(musiclist_namevalue.equals("")){
					musiclist_namevalue = musiclist_name;
				}
				if(musiclist_summaryvalue.equals("")){
					musiclist_summaryvalue = musiclist_summary;
				}
				if(haveNetwork()){
					task = new EditMineSongTask(context, musiclist_id,musiclist_namevalue,musiclist_summaryvalue, cropPath, pageNum, UpdateCallback);
					task.excute();
				}else{
					ActivityUtil.showShortToast(context, "请链接网络");
				}
			}
			
			break;
		}
	}
	
	/**
	 * 回调照片
	 * @param menuWindow
	 */
	private void upDown() {
		menuWindow = new SelectPicPopupWindow(EditSongInfoActivity.this,itemsOnClick);
		menuWindow.showAtLocation(EditSongInfoActivity.this.findViewById(R.id.main_ll),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	// 【相机，图库】为弹出窗口实现监听类
		private OnClickListener itemsOnClick = new OnClickListener() {
			public void onClick(View v) {
				menuWindow.dismiss();
				switch (v.getId()) {
				case R.id.btn_take_photo:// 相机
					savaPath = createPhotoFile() + File.separator+ getCurrentTime() + ".jpg";
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
					Uri uri = Uri.fromFile(new File(savaPath));
					// 获取拍照后未压缩的原图片，并保存在uri路径中
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(intent, 1);
					break;
				case R.id.btn_pick_photo:// 图库
					Intent intent1 = new Intent(Intent.ACTION_PICK);
					intent1.setType("image/*");// 相片类型
					startActivityForResult(intent1, 2);
					break;
				default:
					break;
				}
			}
		};
		
		/**
		 * 获取图片存储 （文件夹）路径
		 * 
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
		 * 
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
		 * 
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

		public void cutPic(String imagePath) {
			cropPath = cropPhotoFile() + File.separator + getCurrentTime()+ imagePath.substring(imagePath.indexOf("."));
			Intent in = getCropImageIntent(Uri.fromFile(new File(imagePath)),Uri.fromFile(new File(cropPath)));
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
			intent.putExtra("outputX", 400);
			intent.putExtra("outputY", 400);
			intent.putExtra("return-data", false);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, toPhotoUri);
			return intent;
		}
		
		/*
		 * 修改信息回调
		 */
		BaseRequestCallBack UpdateCallback = new BaseRequestCallBack() {

			@Override
			public void onLoading(long total, long current, boolean isuploading) {
				super.onLoading(total, current, isUploading);
				progress = (int) (current * 100 / total);
				tv_progress.setText(progress+"%");
				isUploading = isuploading;
			}
			
			@Override
			public void onStart() {
				super.onStart();
				isUploading = true;
				tv_progress.setText(0+"%");
				rotateLoading.start();
			}
			@Override
			public void onResult(String result, int code, String alert, Object obj) {
				
				isUploading = false;
				tv_progress.setText("");
				loadingCancle(rotateLoading);
				if (code != Constant.SUCCESS_REQUEST) {
					ActivityUtil.showShortToast(context, "修改失败");
					return;
				}
					ActivityUtil.showShortToast(context, alert);
					Intent intent = new Intent();
					intent.putExtra("musiclist_name", musiclist_namevalue);
					setResult(4, intent);
					finish();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				isUploading = false;
				tv_progress.setText("");
				loadingCancle(rotateLoading);
			}
		};
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			
			if (requestCode == 1) {
				imagepath = savaPath;
				if (!FileUtil.fileIsExists(savaPath)) {
					return;
				}
				cutPic(imagepath);// 裁剪图片
			} else if (requestCode == 2) {
				if (data != null) {
					ContentResolver resolver = context.getContentResolver();
					// 照片的原始资源地址
					Uri originalUri = data.getData();
					if (originalUri != null) {
						String[] imgs = { MediaStore.Images.Media.DATA };// 将图片URI转换成存储路径
						Cursor cursor = resolver.query(originalUri, imgs, null,
								null, null);
						if (cursor != null) {
							cursor.moveToFirst();
							int index = cursor
									.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
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
			} else if (requestCode == 3) {
				if (data == null) {
					return;
				}
				
				if (FileUtil.fileIsExists(cropPath)) {
					bitmapUtils.display(song_cover, cropPath);
				}
			}
		}
		
		private class TextChangedListener implements TextWatcher{
			
			private EditText edit_text;
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			public TextChangedListener(EditText edit_text) {
				super();
				this.edit_text = edit_text;
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				selectionStart = edit_text.getSelectionStart();
				selectionEnd = edit_text.getSelectionEnd();
				if (temp.length() > 15) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					edit_text.setText(s);
					edit_text.setSelection(tempSelection);
				}
			}
		}

}
