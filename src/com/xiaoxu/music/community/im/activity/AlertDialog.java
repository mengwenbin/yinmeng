package com.xiaoxu.music.community.im.activity;

import java.io.File;

import com.easemob.util.ImageUtils;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.im.applib.task.DownloadImageTask;
import com.xiaoxu.music.community.im.applib.utils.ImageCache;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertDialog extends BaseNewActivity {
	private TextView mTextView;
	private Button mButton;
	private int position;
	private ImageView imageView;
	private EditText editText;
	private boolean isEditextShow;
	
	
	@Override
	public void setContent() {
		setContentView(R.layout.alert_dialog);
	}
	
	@Override
	public void setupView() {
		
		mTextView = (TextView) findViewById(R.id.title);
		mButton = (Button) findViewById(R.id.btn_cancel);
		imageView = (ImageView) findViewById(R.id.image);
		editText = (EditText) findViewById(R.id.edit);
		//提示内容
		String msg = getIntent().getStringExtra("msg");
		//提示标题
		String title = getIntent().getStringExtra("title");
		position = getIntent().getIntExtra("position", -1);
		//是否显示取消标题
		boolean isCanceTitle=getIntent().getBooleanExtra("titleIsCancel", false);
		//是否显示取消按钮
		boolean isCanceShow = getIntent().getBooleanExtra("cancel", false);
		//是否显示文本编辑
		isEditextShow = getIntent().getBooleanExtra("editTextShow",false);
		//转发复制的图片的path
		String path = getIntent().getStringExtra("forwardImage");
		//
		String edit_text = getIntent().getStringExtra("edit_text");
		
		if(msg != null)
		    ((TextView)findViewById(R.id.alert_message)).setText(msg);
		if(title != null)
			mTextView.setText(title);
		if(isCanceTitle){
			mTextView.setVisibility(View.GONE);
		}
		if(isCanceShow)
			mButton.setVisibility(View.VISIBLE);
		if(path != null){
			 //优先拿大图，没有去取缩略图
			if(!new File(path).exists())
				path = DownloadImageTask.getThumbnailImagePath(path);
		    imageView.setVisibility(View.VISIBLE);
		    ((TextView)findViewById(R.id.alert_message)).setVisibility(View.GONE);
		    if(ImageCache.getInstance().get(path) != null){
		        imageView.setImageBitmap(ImageCache.getInstance().get(path));
		    }else{
		        Bitmap bm = ImageUtils.decodeScaleImage(path, 150, 150);
		        imageView.setImageBitmap(bm);
		        ImageCache.getInstance().put(path, bm);
		    }
		    
		}
		if(isEditextShow){
			editText.setVisibility(View.VISIBLE);
			editText.setText(edit_text);
		}
		
	}

	@Override
	public void setModel() {
		
	}
	
	public void ok(View view){
		Intent intent = new Intent();
		intent.putExtra("position", position);
		intent.putExtra("edittext", editText.getText().toString());
		setResult(RESULT_OK,intent);
		if(position != -1)
		ChatActivity.resendPos = position;
		finish();
		
	}
	
	public void cancel(View view){
		finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	
}
