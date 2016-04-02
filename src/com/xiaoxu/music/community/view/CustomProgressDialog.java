package com.xiaoxu.music.community.view;

import com.xiaoxu.music.community.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomProgressDialog extends Dialog {
	
	public final int DELAY_TIME = 5000;
	public static RotateLoading rotateLoading;

	public CustomProgressDialog(Context context) {
		super(context);
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}
	
	/**
	 * 当窗口焦点改变时调用
	 */
	public void onWindowFocusChanged(boolean hasFocus) {
		rotateLoading = (RotateLoading) findViewById(R.id.loading);
		rotateLoading.start();
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
	static CustomProgressDialog dialog;

	public static CustomProgressDialog show(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		dialog = new CustomProgressDialog(context, R.style.Custom_Progress);
		dialog.setTitle("");
		dialog.setContentView(R.layout.dialog_loading_progress);
		// 按返回键是否取消
		dialog.setCancelable(cancelable);
		// 监听返回键处理
		dialog.setOnCancelListener(cancelListener);
		// 设置居中
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// 设置背景层透明度
		lp.dimAmount = 0.1f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				disMiss();
			}
		}, 5000);
		return dialog;
	}

	public static void disMiss() {
		if (dialog != null) {
			dialog.cancel();
		}
	}
}
