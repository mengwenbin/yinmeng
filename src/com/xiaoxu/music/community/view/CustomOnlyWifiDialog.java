package com.xiaoxu.music.community.view;

import com.xiaoxu.music.community.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class CustomOnlyWifiDialog extends Dialog {
	
	private Context context;
	private View menuView;
	
	public CustomOnlyWifiDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public static CustomOnlyWifiDialog show(Context context,View.OnClickListener listener) {
		View menuView = LayoutInflater.from(context).inflate(R.layout.dialog_only_wifi, null);
		Button btn_ok = (Button) menuView.findViewById(R.id.ok);
		Button btn_cancle = (Button) menuView.findViewById(R.id.cancle);
		btn_ok.setOnClickListener(listener);
		btn_cancle.setOnClickListener(listener);
		
		CustomOnlyWifiDialog dialog = new CustomOnlyWifiDialog(context, R.style.Custom_Progress);
		dialog.setContentView(menuView);
		dialog.setCancelable(true);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		dialog.show();
		return dialog;
	}
	
	public void disMiss(CustomOnlyWifiDialog dialog) {
		if (dialog != null) {
			dialog.cancel();
		}
	}

}
