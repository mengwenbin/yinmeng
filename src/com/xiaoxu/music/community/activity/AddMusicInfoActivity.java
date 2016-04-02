package com.xiaoxu.music.community.activity;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.AttachmentEntity;
import com.xiaoxu.music.community.entity.FilePathEntity;
import com.xiaoxu.music.community.entity.MusicCategoryEntity;
import com.xiaoxu.music.community.entity.MusicLocalInfoEntity;
import com.xiaoxu.music.community.entity.ReleseMusicEntity;
import com.xiaoxu.music.community.entity.TagInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.MusicCategoryTask;
import com.xiaoxu.music.community.model.task.ReleseMusicTask;
import com.xiaoxu.music.community.model.task.UploadHeadTask;
import com.xiaoxu.music.community.model.task.UploadMusicTask;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.FileUtil;
import com.xiaoxu.music.community.view.AnimaScrollView;
import com.xiaoxu.music.community.view.SelectPicPopupWindow;

public class AddMusicInfoActivity extends BaseNewActivity implements
		OnClickListener{

	private EditText edit_name;
	private EditText edit_words;
	private EditText edit_compose;
	private EditText edit_sing;
	private EditText edit_arrangement;
	private EditText edit_shuffling;
	private EditText edit_lyric;
	private TextView text_yc;
	private TextView text_fan;

	private ImageView left_btn;
	private TextView title_center;
	private Button relese_btn;
	private ImageView or_iv;
	private ImageView fan_iv;
	private ImageView add_image;
	private TextView or_tv;
	private TextView fan_tv;
	private TextView music_name;
	private TextView tv_progress;
	
	//标签容器
	private LinearLayout tag_layout;
	//原创RelativeLayout
	private RelativeLayout rel_or;
	//翻唱
	private RelativeLayout fan_rel;
	
	private String imagepath;
	private String cropPath;
	private String picName;// 图片名字
	private String fileUrl;//文件路径
	private String fileName;//文件名称
	private String savaPath;// 拍照图片保存路径
	private SelectPicPopupWindow menuWindow;
	private MusicLocalInfoEntity musiclocalEntity;

	private Intent intent;
	private int progress = 0;
	private int progress_num = 0;
	private ProgressBar progressBar;
	public boolean isUploading = false;

	private UploadHeadTask uploadHeadTask;// 封面图上传
	private UploadMusicTask uploadMusicTask;// 上传音乐
	private MusicCategoryTask musicCategoryTask;// 请求歌曲系统标签+歌曲分类

	private ReleseMusicEntity entity;//发布音乐实体
	private FilePathEntity filepathEntity;
	private AttachmentEntity attachmentEntity;
	private MusicCategoryEntity musicCategoryEntity;//标签类别实体
	
	private String tag_Str = "";
	private RelativeLayout rel_pregress;
	private final String mPageName = "AddMusicInfoActivity";
	private AnimaScrollView scroll_view;
	
	@Override
	public void setContent() {
		
		setContentView(R.layout.activity_addmusic_info);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		intent = getIntent();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}

	@Override
	public void setupView() {
		scroll_view = (AnimaScrollView) findViewById(R.id.scroll_view);
		musiclocalEntity = (MusicLocalInfoEntity) intent.getSerializableExtra("song");
		left_btn = (ImageView) findViewById(R.id.left_btn);
		title_center = (TextView) findViewById(R.id.centent_title);
		title_center.setText("补充歌曲信息");
		music_name = (TextView) findViewById(R.id.music_name);
		fileName = musiclocalEntity.getMusicname();
		music_name.setText(fileName);
		fileUrl = musiclocalEntity.getFileurl();
		tv_progress = getTextView(R.id.progress);
		rotateLoading = getRotateLoading(R.id.loading);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		relese_btn = (Button) findViewById(R.id.relese_btn);
		or_iv = (ImageView) findViewById(R.id.or_iv);
		fan_iv = (ImageView) findViewById(R.id.fan_iv);
		or_tv = (TextView) findViewById(R.id.or_tv);
		fan_tv = (TextView) findViewById(R.id.fan_tv);
		add_image = (ImageView) findViewById(R.id.add_image);
		
		rel_or = (RelativeLayout) findViewById(R.id.rel_or);
		fan_rel = (RelativeLayout) findViewById(R.id.fan_rel);
		tag_layout = (LinearLayout) findViewById(R.id.tag_layout);
		rel_pregress = (RelativeLayout) findViewById(R.id.rel_pregress);

		edit_name = (EditText) findViewById(R.id.edit_name);
		edit_name.setText(fileName);
		edit_words = (EditText) findViewById(R.id.edit_words);
		edit_compose = (EditText) findViewById(R.id.edit_compose);
		edit_sing = (EditText) findViewById(R.id.edit_sing);
		edit_arrangement = (EditText) findViewById(R.id.edit_arrangement);
		edit_shuffling = (EditText) findViewById(R.id.edit_shuffling);
		edit_lyric = (EditText) findViewById(R.id.edit_lyric);
		text_yc = (TextView) findViewById(R.id.text_yc);
		text_fan = (TextView) findViewById(R.id.text_fan);
		text_yc.setText("演唱:");
		text_fan.setText("编曲:");
		initEditValue();
		
		initListener();

	}

	/*
	 * 初始化编辑框
	 */
	private void initEditValue() {

		edit_name.setText("");
		edit_words.setText("");
		edit_compose.setText("");
		edit_sing.setText("");
		edit_arrangement.setText("");
		edit_shuffling.setText("");

	}


	@Override
	public void setModel() {

		filepathEntity = new FilePathEntity();
		entity = new ReleseMusicEntity();
		entity.setMusic_category(51+"");
		if (haveNetwork()) {
			UploadMusic();
		}else{
			scroll_view.setVisibility(View.INVISIBLE);
			showNoNet(this);
		}
		or_tv.setTextColor(getResources().getColor(R.color.cyan8));

	}
	
	private void UploadMusic(){
		
		musicCategoryTask = new MusicCategoryTask(context, musicCategory);
		musicCategoryTask.excute();
		uploadMusicTask = new UploadMusicTask(context, fileUrl, fileName,uploadMusicFile);
		uploadMusicTask.excute();
		
	}

	/*
	 * 初始化监听
	 */
	private void initListener() {

		left_btn.setOnClickListener(this);
		relese_btn.setOnClickListener(this);
		rel_or.setOnClickListener(this);
		fan_rel.setOnClickListener(this);
		add_image.setOnClickListener(this);
		rel_pregress.setOnClickListener(this);
		
		//限制输入框的长度的监听
		edit_name.addTextChangedListener(new TextChangedListener(edit_name));
		edit_words.addTextChangedListener(new TextChangedListener(edit_words));
		edit_compose.addTextChangedListener(new TextChangedListener(edit_compose));
		edit_sing.addTextChangedListener(new TextChangedListener(edit_sing));
		edit_arrangement.addTextChangedListener(new TextChangedListener(edit_arrangement));
		edit_shuffling.addTextChangedListener(new TextChangedListener(edit_shuffling));

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
			if (temp.length() > 20) {
				ActivityUtil.showShortToast(context, "长度超过了20");
				s.delete(selectionStart - 1, selectionEnd);
				int tempSelection = selectionStart;
				edit_text.setText(s);
				edit_text.setSelection(tempSelection);
			}
		}
	}
	
	/*
	 * 添加标签View
	 */
	private void addTagView(List<TagInfoEntity> list){
		
		tag_layout.removeAllViews();
		int width = ActivityUtil.getScreenHeight(context);
		int height = ActivityUtil.getScreenHeight(context);
		
		for (int i = 0; i < list.size(); i++) {
			
			TextView tag_btn = new TextView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(width * 0.07),(int)(height * 0.038));
			params.setMargins(10, 0, 10, 0);
			TagInfoEntity tagInfoEntity = list.get(i);
			tag_btn.setText(tagInfoEntity.getTag_name());
			tag_btn.setClickable(true);
			tag_btn.setGravity(Gravity.CENTER);
			tag_btn.setBackgroundResource(R.drawable.bg_tag_background);
			tag_btn.setTextColor(getResources().getColor(R.color.white));
			tag_btn.setTextSize(13);
			tag_btn.setLayoutParams(params);
			tag_btn.setTag(false);
			tag_btn.setOnClickListener(new ClickListener(tag_btn,tagInfoEntity.getTag_name()));
			tag_layout.addView(tag_btn);
			
		}
		
	}

	private class ClickListener implements OnClickListener{

		private String tag_name;
		private TextView tag_btn;
		
		public ClickListener(TextView tag_btn,String tag_name) {
			super();
			this.tag_btn = tag_btn;
			this.tag_name = tag_name;
		}

		@Override
		public void onClick(View v) {
			boolean isSelect = (Boolean) tag_btn.getTag();
			if(isSelect){
				isSelect = false;
				tag_btn.setTextColor(getResources().getColor(R.color.white));
				tag_Str = tag_Str.replace(tag_name + ",", "");
			}else{
				isSelect = true;
				tag_btn.setTextColor(getResources().getColor(R.color.black));
				tag_Str += tag_name + ",";
			}
			tag_btn.setTag(isSelect);
		}
		
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.no_network:
			if (haveNetwork()) {
				hintNoNet();
				scroll_view.setVisibility(View.VISIBLE);
				UploadMusic();
			}else{
				scroll_view.setVisibility(View.INVISIBLE);
				showNoNet(this);
			}
			break;
		case R.id.relese_btn:
			
			if(edit_name.getText().toString().trim().equals("")
			||edit_words.getText().toString().trim().equals("")
			||edit_compose.getText().toString().trim().equals("")
			||edit_sing.getText().toString().trim().equals("")
			||edit_arrangement.getText().toString().trim().equals("")
			||edit_shuffling.getText().toString().trim().equals("")){
				ActivityUtil.showShortToast(context, "请补充歌曲信息");
				return;
			}
			if(filepathEntity.getImagefile_path() == null || filepathEntity.getImagefile_path().equals("")){
				ActivityUtil.showShortToast(context, "请上传封面图");
				return;
			}
			
			if(tag_Str.equals("")){
				ActivityUtil.showShortToast(context, "请选择标签");
				return;
			}
			if(edit_lyric.getText().toString().trim().equals("")){
				ActivityUtil.showShortToast(context, "请编辑歌词");
				return;
			}
			
			entity.setMusic_name(edit_name.getText().toString().trim());
			entity.setMusic_lyricser(edit_words.getText().toString().trim());
			entity.setMusic_composer(edit_compose.getText().toString().trim());
			entity.setMusic_singing0("");
			entity.setMusic_singing(edit_sing.getText().toString().trim());
			entity.setMusic_arranger(edit_arrangement.getText().toString().trim());
			entity.setMusic_mixed(edit_shuffling.getText().toString().trim());
			entity.setMusic_lyric(edit_lyric.getText().toString().trim());
			entity.setMusic_tag(tag_Str.substring(0, tag_Str.length()-1));
				
				if (haveNetwork()) {
					ReleseMusicTask releseMusicTask = new ReleseMusicTask(context,entity, filepathEntity,releseMusic);
					releseMusicTask.excute();
				}

			break;
		case R.id.rel_or:

			initEditValue();
			text_yc.setText("演唱:");
			text_fan.setText("编曲:");
			entity.setMusic_name(edit_name.getText().toString().trim());
			entity.setMusic_category(musicCategoryEntity.getList_musicCategory().get(0).getCategory_id()+"");
			entity.setMusic_lyricser(edit_words.getText().toString().trim());
			entity.setMusic_composer(edit_compose.getText().toString().trim());
			entity.setMusic_singing0("");
			entity.setMusic_singing(edit_sing.getText().toString().trim());
			entity.setMusic_arranger(edit_arrangement.getText().toString().trim());
			entity.setMusic_mixed(edit_shuffling.getText().toString().trim());
			entity.setMusic_lyric(edit_lyric.getText().toString().trim());
			fan_tv.setTextColor(getResources().getColor(R.color.tab_text_normal));
			or_tv.setTextColor(getResources().getColor(R.color.cyan8));
			or_iv.setVisibility(View.VISIBLE);
			fan_iv.setVisibility(View.INVISIBLE);
			break;
		case R.id.fan_rel:

			initEditValue();
			text_yc.setText("原唱:");
			text_fan.setText("翻唱:");
			entity.setMusic_name(edit_name.getText().toString().trim());
			entity.setMusic_category(musicCategoryEntity.getList_musicCategory().get(1).getCategory_id()+"");
			entity.setMusic_lyricser(edit_words.getText().toString().trim());
			entity.setMusic_composer(edit_compose.getText().toString().trim());
			entity.setMusic_singing0(edit_sing.getText().toString().trim());
			entity.setMusic_singing(edit_arrangement.getText().toString().trim());
			entity.setMusic_arranger("");
			entity.setMusic_mixed(edit_shuffling.getText().toString().trim());
			or_tv.setTextColor(getResources().getColor(R.color.tab_text_normal));
			fan_tv.setTextColor(getResources().getColor(R.color.cyan8));
			fan_iv.setVisibility(View.VISIBLE);
			or_iv.setVisibility(View.INVISIBLE);
			
			break;
		case R.id.add_image:
			upDown();
			break;
		case R.id.rel_pregress:
			rel_pregress.setClickable(false);
			UploadMusic();
			break;

		}
	}

	/**
	 * 上传照片
	 * @param menuWindow
	 */
	private void upDown() {
		menuWindow = new SelectPicPopupWindow(AddMusicInfoActivity.this,
				itemsOnClick);
		menuWindow.showAtLocation(AddMusicInfoActivity.this.findViewById(R.id.main_ll),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	// 【相机，图库】为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:// 相机
				savaPath = createPhotoFile() + File.separator
						+ getCurrentTime() + ".jpg";
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
		picName = getCurrentTime() + "封面图"
				+ imagePath.substring(imagePath.indexOf("."));
		cropPath = cropPhotoFile() + File.separator + getCurrentTime()
				+ imagePath.substring(imagePath.indexOf("."));
		Intent in = getCropImageIntent(Uri.fromFile(new File(imagePath)),
				Uri.fromFile(new File(cropPath)));
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
			String savaPath = cropPath;
			if (FileUtil.fileIsExists(savaPath) && haveNetwork()) {
				bitmapUtils.display(add_image, savaPath);
				uploadHeadTask = new UploadHeadTask(context, savaPath, picName,
						callback_uploadCover);
				uploadHeadTask.excute();
			}
		}
	}

	/*
	 * 上传封面图回调
	 */
	BaseRequestCallBack callback_uploadCover = new BaseRequestCallBack(
			AttachmentEntity.class) {

		@Override
		public void onLoading(long total, long current, boolean isuploading) {
			super.onLoading(total, current, isUploading);
			progress_num = (int) (current * 100 / total);
			tv_progress.setText(progress_num+"%");
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
				ActivityUtil.showShortToast(context, "上传失败");
				return;
			}

			attachmentEntity = (AttachmentEntity) obj;
			filepathEntity.setImagefile_path(attachmentEntity.getAttachment().getAttachment());
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			isUploading = false;
			tv_progress.setText("");
			loadingCancle(rotateLoading);
		}
	};

	/*
	 * 上传音乐回调
	 */
	BaseRequestCallBack uploadMusicFile = new BaseRequestCallBack(AttachmentEntity.class) {

		@Override
		public void onStart() {
			super.onStart();
			progressBar.setProgress(progress);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			super.onLoading(total, current, isUploading);
			progress = (int) (current * 100 / total);
			progressBar.setProgress(progress);
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			rel_pregress.setClickable(true);
			progressBar.setProgress(0);
		}

		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			if (code != Constant.SUCCESS_REQUEST) {
				rel_pregress.setClickable(true);
				progressBar.setProgress(0);
				ActivityUtil.showShortToast(context, "上传失败");
				return;
			}

			attachmentEntity = (AttachmentEntity) obj;
			filepathEntity.setMusicfile_path(attachmentEntity.getAttachment().getAttachment());
			ActivityUtil.showShortToast(context, "上传成功");
			relese_btn.setEnabled(true);
		}
	};

	/*
	 * 请求标签、音乐类别回调
	 */
	BaseRequestCallBack musicCategory = new BaseRequestCallBack(
			MusicCategoryEntity.class) {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
		}

		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			if (code == Constant.SUCCESS_REQUEST) {
				musicCategoryEntity = (MusicCategoryEntity) obj;
				or_tv.setText(musicCategoryEntity.getList_musicCategory().get(0).getCategory_name());
				fan_tv.setText(musicCategoryEntity.getList_musicCategory().get(1).getCategory_name());
				addTagView(musicCategoryEntity.getList_tag());
			}
		}
	};

	BaseRequestCallBack releseMusic = new BaseRequestCallBack() {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
		}

		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			if (code != Constant.SUCCESS_REQUEST) {
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			Intent intent = new Intent(Constant.ACTION_MINE_UPDATE_USERINFO);
			intent.putExtra("isrelese", true);
			context.sendBroadcast(intent);
			ActivityUtil.showShortToast(context, alert);
			AddMusicInfoActivity.this.finish();
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(uploadMusicTask!= null){
			uploadMusicTask.cancelTask();
		}
		if(musicCategoryTask != null){
			musicCategoryTask.cancelTask();
		}
	}

}
