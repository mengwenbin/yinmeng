package com.xiaoxu.music.community.activity;


import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.ResetPwdTask;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.view.ClearEditText;
import com.xiaoxu.music.community.view.CustomProgressDialog;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ResetPasswordActivity extends BaseNewActivity {

	private ImageButton left_btn;
	private TextView tv_title,right_text;
	private EditText edit_code;
	private ClearEditText edit_name, edit_pwd, edit_pwd_affirm;
	private Button btn_code, btn_regist;
	
	private MyCount myCount;
	private String phone, code, pwd, pwd_affirm;
	
	private final String mPageName = "ResetPasswordActivity";

	@Override
	public void setContent() {
		setContentView(R.layout.activity_reset_password);
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
		
		left_btn = (ImageButton) findViewById(R.id.left_btn);
		tv_title = (TextView) findViewById(R.id.title_center_content);
		right_text = getTextView(R.id.right_text);
		right_text.setVisibility(View.GONE);
		tv_title.setText(getString(R.string.reset_password));
		edit_name = (ClearEditText) findViewById(R.id.regist_name);
		edit_pwd = (ClearEditText) findViewById(R.id.regist_pwd);
		edit_pwd_affirm = (ClearEditText) findViewById(R.id.regist_pwd_affirm);
		edit_code = (EditText) findViewById(R.id.regist_code);// 验证码
		btn_code = (Button) findViewById(R.id.regist_get_code);
		btn_regist = (Button) findViewById(R.id.regist_btn);
		myCount = new MyCount(60000, 1000);// 验证吗时间
		initListener();
		
	}

	private void initListener() {
		
		edit_pwd.addTextChangedListener(new TextChangedListener(edit_pwd));
		edit_pwd_affirm.addTextChangedListener(new TextChangedListener(edit_pwd_affirm));
		btn_code.setOnClickListener(listener);// 获取验证码
		btn_regist.setOnClickListener(listener);
		left_btn.setOnClickListener(listener);
	}

	@Override
	public void setModel() {
		initSMSSDK();
	}
	
	public void initSMSSDK() {
		SMSSDK.initSDK(context, Constant.SMSSDK_APPKEY, Constant.SMSSDK_APPSECRET);
		EventHandler eh = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		SMSSDK.registerEventHandler(eh);
	}

	OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.regist_get_code:// 获取验证码
				phone = edit_name.getText().toString();
				if (TextUtils.isEmpty(phone)) {
					ActivityUtil.showShortToast(context, getString(R.string.start_please_phone));
					return;
				}
				SMSSDK.getVerificationCode("86", phone);// 发送验证码
				myCount.start();
				break;
			case R.id.regist_btn:
				reset();
				break;
			case R.id.left_btn:
				finish();
				break;
			}
		}
	};
	
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

	public void reset() {
		phone = edit_name.getText().toString();
		pwd = edit_pwd.getText().toString();
		code = edit_code.getText().toString();
		pwd_affirm = edit_pwd_affirm.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			ActivityUtil.showShortToast(context, getString(R.string.start_phone_number_can_not_be_empty));
			return;
		}
		if (TextUtils.isEmpty(code)) {
			ActivityUtil.showShortToast(context, getString(R.string.start_verification_code_can_not_be_empty));
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			ActivityUtil.showShortToast(context, getString(R.string.start_password_can_not_be_empty));
			return;
		}
		
		if(pwd.length() < 6 || pwd.length() > 18){
			ActivityUtil.showShortToast(context, "密码必须在6-18位之间");
			return;
		}
		if(pwd_affirm.length() < 6 || pwd_affirm.length() > 18){
			ActivityUtil.showShortToast(context, "密码必须在6-18位之间");
			return;
		}
		if (!pwd.equals(pwd_affirm)) {
			ActivityUtil.showShortToast(context, getString(R.string.start_two_passwords_differ));
			return;
		}
		if(haveNetwork()){
			SMSSDK.submitVerificationCode("86", phone, code);// 发送验证码
		}
	}
	
	// 验证码
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			if (result == SMSSDK.RESULT_COMPLETE) {
				// 短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					//验证码输入正确
					ActivityUtil.showShortToast(context, getString(R.string.start_verificationCode_isRight));
					resetPasswordTask();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					//验证码已发送
					ActivityUtil.showShortToast(context, getString(R.string.start_verificationCode_isSend));
				} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {// 返回支持发送验证码的国家列表
					//获取国家列表成功
					ActivityUtil.showShortToast(context, getString(R.string.start_get_country_list_success));
				}
			} else {
				((Throwable) data).printStackTrace();
				//验证码错误
				ActivityUtil.showShortToast(context, getString(R.string.start_verificationCode_isError));
			}
		}
	};

	public void resetPasswordTask() {
		if(haveNetwork()){
			
			CustomProgressDialog.show(context, true, null).setCanceledOnTouchOutside(false);
			ResetPwdTask task = new ResetPwdTask(context, phone, pwd, callBack);
			task.excute();
		}
	}
	
	BaseRequestCallBack callBack = new BaseRequestCallBack() {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			CustomProgressDialog.disMiss();
			ActivityUtil.showShortToast(context, alert+"重置密码成功 ");
			if(code != Constant.SUCCESS_REQUEST){
				return;	//請求失敗
			}
			finish();
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			// TODO Auto-generated method stub
			CustomProgressDialog.disMiss();
		}
	};

	// 验证码定时
	private class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onTick(long millisUntilFinished) {
			btn_code.setText(millisUntilFinished / 1000 + "秒");
			btn_code.setClickable(false);
			btn_code.setEnabled(false);
		}

		@Override
		public void onFinish() {
			btn_code.setText(getString(R.string.start_regist_getcode));
			btn_code.setClickable(true);
			btn_code.setEnabled(true);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}

}
