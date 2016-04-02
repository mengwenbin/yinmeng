package com.xiaoxu.music.community.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.UpdatePwdTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.view.ClearEditText;
import com.xiaoxu.music.community.view.CustomProgressDialog;

public class UpdatePwdActivity extends BaseNewActivity implements OnClickListener{

	private ImageButton btn_back;
	private Button btn_update;
	private TextView title_content,right_text;
	private ClearEditText edit_old_pwd, edit_new_pwd, edit_new_pwd_too;
	private String mobile;
	
	private final String mPageName = "UpdatePwdActivity";
	public String Login_Tag = "com.xiaoxu.music.community.activity.UpdatePwdActivity";
	
	@Override
	public void setContent() { 
		setContentView(R.layout.activity_update_pwd);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
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
		// TODO Auto-generated method stub
		btn_back = getImageButton(R.id.left_btn);
		right_text = getTextView(R.id.right_text);
		right_text.setVisibility(View.GONE);
		title_content = getTextView(R.id.title_center_content);
		title_content.setText(getString(R.string.update_pwd));
		
		btn_update = getButton(R.id.btn_update);
		
		edit_old_pwd = (ClearEditText) getEditText(R.id.edit_old_pwd);
		edit_new_pwd = (ClearEditText) getEditText(R.id.edit_new_pwd);
		edit_new_pwd_too = (ClearEditText) getEditText(R.id.edit_new_pwd2);
		
		initListener();
	}

	private void initListener() {
		
		edit_new_pwd.addTextChangedListener(new TextChangedListener(edit_new_pwd));
		edit_new_pwd_too.addTextChangedListener(new TextChangedListener(edit_new_pwd_too));
		btn_back.setOnClickListener(this);
		btn_update.setOnClickListener(this);
	}

	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		if(!AccountInfoUtils.getInstance(context).isLogin()){	//判断是否登录
			Intent i = new Intent(context, StartActivity.class);
			i.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
			startActivityForResult(i, 1);
			return;
		}
		mobile = AccountInfoUtils.getInstance(context).getUserInfo().getUser_mobile();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.btn_update:
			update();
			break;
		default:
			break;
		}
	}
	
	public void update(){
		String oldpwd = edit_old_pwd.getText().toString();
		String Newpwd = edit_new_pwd.getText().toString();
		String Newpwd_affirm = edit_new_pwd_too.getText().toString();
		
		if (TextUtils.isEmpty(oldpwd)) {
			ActivityUtil.showShortToast(context, "请输入原密码");
			return;
		}
		if (TextUtils.isEmpty(Newpwd)) {
			ActivityUtil.showShortToast(context, "请输入新密码");
			return;
		}
		if (TextUtils.isEmpty(Newpwd_affirm)) {
			ActivityUtil.showShortToast(context, "请再次输入新密码");
			return;
		}
		
		if(Newpwd.length() < 6 || Newpwd.length() > 18){
			ActivityUtil.showShortToast(context, "密码必须在6-18位之间");
			return;
		}
		if(Newpwd_affirm.length() < 6 || Newpwd_affirm.length() > 18){
			ActivityUtil.showShortToast(context, "密码必须在6-18位之间");
			return;
		}
		if (!Newpwd.equals(Newpwd_affirm)) {
			ActivityUtil.showShortToast(context, "再次输入密码");
			return;
		}
		if(haveNetwork()){
			CustomProgressDialog.show(context, true, null).setCanceledOnTouchOutside(false);
			UpdatePwdTask task = new UpdatePwdTask(context, mobile, oldpwd, Newpwd, callback);
			task.excute();
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
			if (temp.length() > 18) {
				s.delete(selectionStart - 1, selectionEnd);
				int tempSelection = selectionStart;
				edit_text.setText(s);
				edit_text.setSelection(tempSelection);
			}
		}
	}
	
	BaseRequestCallBack callback = new BaseRequestCallBack() {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			
			CustomProgressDialog.disMiss();
			ActivityUtil.showShortToast(context, alert);
			if(code!=Constant.SUCCESS_REQUEST){
				return;
			}
			finish();
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			CustomProgressDialog.disMiss();
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if(!AccountInfoUtils.getInstance(context).isLogin()){
				finish();
			}
			mobile = AccountInfoUtils.getInstance(context).getUserInfo().getUser_mobile();
		}
	}

}
