package com.xiaoxu.music.community.view;

import com.xiaoxu.music.community.R;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
public class LoadingHelper {
	
	/** 加载数据对话框 */
	private static Dialog mLoadingDialog;
	
	/**
	 * 显示加载对话框
	 * @param context 上下文
	 * @param msg 对话框显示内容
	 * @param cancelable 对话框是否可以取消
	 */
	public static void showDialogForLoading(Context context, String msg, boolean cancelable) {
		View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
		TextView loadingText = (TextView)view.findViewById(R.id.id_tv_loading_dialog_text);
		if(TextUtils.isEmpty(msg)){
			msg = "正在加载...";
		}
		loadingText.setText(msg);
		mLoadingDialog = new Dialog(context, R.style.loading_dialog_style);
		mLoadingDialog.setCancelable(cancelable);
		mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		mLoadingDialog.show();		
	}
	
	/**
	 * 关闭加载对话框
	 */
	public static void hideDialogForLoading() {
		if(mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.cancel();
		}
	}


}
