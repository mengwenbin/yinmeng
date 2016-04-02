package com.xiaoxu.music.community.view;

import com.xiaoxu.music.community.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

public class CustomLoadingDialog extends Dialog {
	
	public final int DELAY_TIME = 5000;
	public RotateLoading rotateLoading;
	public TextView loading_progress;
	public Context context;
	
	public CustomLoadingDialog(Context context) {
		super(context);
		this.context = context;
	}
	
	public CustomLoadingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}
	
	/**
	 * 当窗口焦点改变时调用
	 */
	public void onWindowFocusChanged(boolean hasFocus) {
		rotateLoading = (RotateLoading) findViewById(R.id.loading);
		loading_progress = (TextView) findViewById(R.id.loading_progress);
		rotateLoading.start();
	}
	
	public void changeProgress(int progress){
		if(loading_progress!=null){
			loading_progress.setText(progress+"%");
		}
	}
	
	/**
	 * 弹出自定义ProgressDialog
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            提示
	 * @param cancelable
	 *            是否按返回键取消
	 * @param cancelListener
	 *            按下返回键监听
	 * @return
	 */
	CustomProgressDialog dialog;
	public CustomProgressDialog showDialog(boolean cancelable, OnCancelListener cancelListener) {
		dialog = new CustomProgressDialog(context, R.style.Custom_Progress);
		dialog.setTitle("");
		dialog.setContentView(R.layout.dialog_upload_progress);
		// 按返回键是否取消
		dialog.setCancelable(cancelable);
		// 监听返回键处理
		dialog.setOnCancelListener(cancelListener);
		// 设置居中
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// 设置背景层透明度
		lp.dimAmount = 0.5f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
		return dialog;
	}

	public void disMiss() {
		if (dialog != null) {
			dialog.cancel();
		}
	}

}
