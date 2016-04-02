package com.xiaoxu.music.community.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.ReleasePostImageAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.ImageEntity;
import com.xiaoxu.music.community.entity.BlogImgEntity;
import com.xiaoxu.music.community.entity.UserInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.ReleasePostTask;
import com.xiaoxu.music.community.model.task.UploadPostImageTask;
import com.xiaoxu.music.community.parser.ParseUtil;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.FileUtil;
import com.xiaoxu.music.community.view.SelectPicPopupWindow;

public class ReleaseBlogActivity extends BaseNewActivity implements OnClickListener, OnItemClickListener{
	
	private ImageButton btn_back,btn_release_blog;
	private EditText eidt_text;
	private GridView gv_image;
	
	private Intent intent;
	private UserInfoEntity user_info;
	private String category_id;
	private List<ImageEntity> list;
	private ReleasePostImageAdapter adapter;
	
	private SelectPicPopupWindow menuWindow;
	private String imagepath;
	private String savaPath;// 拍照的路径
	private Bitmap bitmap;
	
	private UploadPostImageTask upload_task;
	
	private TextView tv_progress;
	private int progress_num = 0;
	public boolean isUploading = false;
	
	private final String mPageName = "ReleaseBlogActivity";
	private String Login_Tag = "com.xiaoxu.music.community.activity.ReleaseBlogActivity";
	
	@Override
	public void setContent() {
		setContentView(R.layout.activity_circle_blogs_release);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		intent = getIntent();
		category_id = intent.getStringExtra("category_id");
		user_info = AccountInfoUtils.getInstance(context).getUserInfo();
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
	public void setupView() {
		tv_progress = getTextView(R.id.progress);
		rotateLoading = getRotateLoading(R.id.loading);
		getImageButton(R.id.title_left_btn).setOnClickListener(this);
		getImageButton(R.id.title_right_btn).setOnClickListener(this);
		eidt_text = getEditText(R.id.edit_post_content);
		gv_image = getGridView(R.id.gridview_image);
		gv_image.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gv_image.setOnItemClickListener(this);
	}
	
	@Override
	public void setModel() {
		list = new ArrayList<ImageEntity>();
		list.add(new ImageEntity("-1", ""));
		adapter = new ReleasePostImageAdapter(context, bitmapUtils, list);
		gv_image.setAdapter(adapter);
		if(!AccountInfoUtils.getInstance(context).isLogin()){
			Intent in = new Intent(context, StartActivity.class);
			in.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
			startActivityForResult(in, 3);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ImageEntity entity = list.get(position);
		if (Integer.parseInt(entity.getA_id()) == -1 && isUploading==false) {// 选择图片
			upDown();
		} else {
			return;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:
			finish();
			break;
		case R.id.title_right_btn://release blog
			exitSoft();
			task();
			break;
		default:
			break;
		}
	}
	
	public String getIds() {
		StringBuffer buffer = new StringBuffer();
		list = adapter.getList();
		if(null == list || list.size() == 0)
			return null;
		for (ImageEntity entity : list) {
			if (Integer.parseInt(entity.getA_id()) != -1) {
				buffer.append(entity.getA_id() + ",");
			}
		}
		if (buffer.length() == 0) {
			return null;
		}
		buffer = buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}
	
	public void disposeImage(String path){
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = 8;
		//获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
		File file = new File(path);
		int degree = readPictureDegree(file.getAbsolutePath());
		Bitmap cameraBitmap = BitmapFactory.decodeFile(path, bitmapOptions);  
		//把图片旋转为正的方向 
		bitmap = rotaingImageView(degree, cameraBitmap);
		FileUtil.savaBitmap(path, bitmap);
	}
	
	 /** 
     * 旋转图片 
     * @param angle 
     * @param bitmap 
     * @return Bitmap 
     */  
    public Bitmap rotaingImageView(int angle, Bitmap bitmap) {  
        //旋转图片 动作  
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
        return resizedBitmap;
    }
	
	/** 
	 * 读取图片属性：旋转的角度 
	 * @param path 图片绝对路径 
	 * @return degree旋转的角度 
	 */  
	public int readPictureDegree(String path) {  
       int degree  = 0;  
       try {
           ExifInterface exifInterface = new ExifInterface(path);  
           int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
           switch (orientation) {
           case ExifInterface.ORIENTATION_ROTATE_90:
        	   degree = 90;
               break;
           case ExifInterface.ORIENTATION_ROTATE_180: 
               degree = 180;
               break;
           case ExifInterface.ORIENTATION_ROTATE_270:
               degree = 270;
               break;
           }
       } catch (IOException e) {
               e.printStackTrace();
       }
       return degree;  
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			imagepath = savaPath;
			if (!FileUtil.fileIsExists(imagepath)) {
				return;
			}
			disposeImage(imagepath);
			upload_task();
			break;
		case 2:
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
						} else {
							ActivityUtil.showShortToast(context, "无权限访问选定的图片");
						}
					} else {
						ActivityUtil.showShortToast(context, "无权限访问选定的图片");
					}
					cursor.close();
					if (FileUtil.fileIsExists(imagepath)) {
						upload_task();
					}
				}
			}
			break;
		case 3:
			if(!AccountInfoUtils.getInstance(context).isLogin()){
				finish();
			}
			user_info = AccountInfoUtils.getInstance(context).getUserInfo();
			break;
		default:
			break;
		}
	}
	
	public void task(){
		String message = eidt_text.getText().toString();
		if (TextUtils.isEmpty(message)) {
			ActivityUtil.showShortToast(context, "请输入贴子内容");
			return;
		}else if(message.length()<5){
			ActivityUtil.showShortToast(context, "请输入最少五个字");
		}
		String imageIds = getIds();
		if(haveNetwork()){
			loadingStart(rotateLoading);
			ReleasePostTask task = new ReleasePostTask(context, category_id, user_info.getUsername(),
					message, imageIds,callback);
			task.excute();
		}
	}
	
	public void upload_task() {
		if(haveNetwork()){
			upload_task = new UploadPostImageTask(context, imagepath, "", "", callback_upload);
			upload_task.excute();
		}
	}
	
	BaseRequestCallBack callback = new BaseRequestCallBack() {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			loadingCancle(rotateLoading);
			ActivityUtil.showShortToast(context, alert);
			if (code != Constant.SUCCESS_REQUEST) 
				return;
			setResult(2);
			finish();
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			loadingCancle(rotateLoading);
		}
	};
	
	
	RequestCallBack<String> callback_upload = new RequestCallBack<String>() {
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			isUploading = false;
			tv_progress.setText("");
			loadingCancle(rotateLoading);
		}
		
		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			isUploading = false;
			tv_progress.setText("");
			loadingCancle(rotateLoading);
			int code = ParseUtil.parseCode(arg0.result);
			String alert = ParseUtil.parseAlert(arg0.result);
			if (code != Constant.SUCCESS_REQUEST) {
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			BlogImgEntity entity = (BlogImgEntity) ParseUtil.parseResultObj(arg0.result,BlogImgEntity.class);
			if (entity != null) {
				list.add(0,new ImageEntity(entity.getAttachment().getA_id(), imagepath));
				adapter.notifyDataSetChanged();
			}
		}

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
	};
	
	
	//******************************************
	private void upDown() {
		// TODO Auto-generated method stub
		exitSoft();
		menuWindow = new SelectPicPopupWindow(ReleaseBlogActivity.this, itemsOnClick);
		menuWindow.showAtLocation(ReleaseBlogActivity.this.findViewById(R.id.main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				savaPath = createPhotoFile() + File.separator + getCurrentTime() + ".jpg";
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
				Uri uri = Uri.fromFile(new File(savaPath));
				// 获取拍照后未压缩的原图片，并保存在uri路径中
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, 1);
				break;
			case R.id.btn_pick_photo:
				Intent intent1 = new Intent(Intent.ACTION_PICK);
				intent1.setType("image/*");// 相片类型
				startActivityForResult(intent1, 2);
				break;
			default:
				break;
			}
		}
	};
	
	
	public String getCurrentTime() {
		// 获取时间String
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HHmmss");
		return format.format(date);
	}
	
	/**
	 * 获取原图片存储路径
	 * @return
	 */
	private String createPhotoFile() {
		File file = new File(Constant.PHOTO_PATH);
		if (!file.exists()) {
			file.mkdirs(); // 创建文件夹
		}
		return Constant.PHOTO_PATH;
	}
	
	// 退出应用
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && null != rotateLoading && 
				rotateLoading.isStart() && null != upload_task) {
			upload_task.cancelTask();
			tv_progress.setText("");
			loadingCancle(rotateLoading);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
