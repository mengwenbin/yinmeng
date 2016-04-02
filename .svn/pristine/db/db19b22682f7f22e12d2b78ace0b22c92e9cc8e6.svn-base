package com.xiaoxu.music.community.fragment;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.StartActivity;
import com.xiaoxu.music.community.activity.WebViewActivity;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.RegistTask;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.NetUtils;
import com.xiaoxu.music.community.view.ClearEditText;
import com.xiaoxu.music.community.view.CustomProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class RegistFragment extends Fragment {

	private EditText edit_code;
	private ClearEditText edit_name, edit_pwd, edit_pwd_affirm;
	private Button btn_getCode, btn_regist;
	private CheckBox btn_agree;
	private TextView read_item;// 条款
	private MyCount myCount;

	private Context context;// title_center_content
	private String phone, code, pwd, pwd_affirm;
	private RegistTask registTask;
	
	private StartActivity activity;
	private final  String mPageName = "RegistFragment";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.activity = (StartActivity) getActivity();
		this.context = getActivity();
		View contentView = inflater.inflate(R.layout.fragment_regist,container, false);
		
		// 友盟统计打开测试模式
		MobclickAgent.setDebugMode(true);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，来禁止默认的Activity页面统计方式。避免重复统计。
		MobclickAgent.openActivityDurationTrack(false);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		edit_name = (ClearEditText) contentView.findViewById(R.id.regist_name);
		edit_pwd = (ClearEditText) contentView.findViewById(R.id.regist_pwd);
		edit_code = (EditText) contentView.findViewById(R.id.regist_code);// 验证码
		edit_pwd_affirm = (ClearEditText) contentView.findViewById(R.id.regist_pwd_affirm);
		btn_getCode = (Button) contentView.findViewById(R.id.regist_get_code);
		btn_agree = (CheckBox) contentView.findViewById(R.id.regist_agree_item);
		read_item = (TextView) contentView.findViewById(R.id.regist_user_item);
		btn_regist = (Button) contentView.findViewById(R.id.regist_btn);
		
		myCount = new MyCount(60000, 1000);// 验证吗时间
		initListener();
		initSMSSDK();
		return contentView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
		MobclickAgent.onResume(activity);//友盟统计
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(activity);
	}

	private void initListener() {
		
		edit_pwd.addTextChangedListener(new TextChangedListener(edit_pwd));
		edit_pwd_affirm.addTextChangedListener(new TextChangedListener(edit_pwd_affirm));
		read_item.setOnClickListener(listener);//
		btn_getCode.setOnClickListener(listener); // 获取验证码
		btn_regist.setOnClickListener(listener);
		btn_agree.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				btn_agree.setChecked(isChecked);// 条款是否同意
			}
		});
		
	}

	public void initSMSSDK() {
		SMSSDK.initSDK(context, Constant.SMSSDK_APPKEY, Constant.SMSSDK_APPSECRET);
		EventHandler event = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				smssdkHandler.sendMessage(msg);
			}
		};
		SMSSDK.registerEventHandler(event);
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.regist_get_code:// 获取验证码
				phone = edit_name.getText().toString();
				if (TextUtils.isEmpty(phone)) {
					ActivityUtil.showShortToast(context, getString(R.string.start_please_phone));
					return;
				}
				if (NetUtils.getAPNType(context) == -1) {//无网络
					ActivityUtil.showShortToast(context, getString(R.string.no_network));
					return;
				}
				SMSSDK.getVerificationCode("86", phone);// 发送验证码
				myCount.start();
				break;
			case R.id.regist_btn:
				regist();
				break;
			case R.id.regist_user_item:
				
				Intent intent = new Intent(context, WebViewActivity.class);
				intent.putExtra("url", "file:///android_asset/law.html");
				intent.putExtra("content", "使用条款");
				startActivity(intent);
				break;
			}
		}
	};
	
	public void regist() {
		
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
		if (!btn_agree.isChecked()) {
			ActivityUtil.showShortToast(context, getString(R.string.start_please_read_the_terms));
			return;
		}
		SMSSDK.submitVerificationCode("86", phone, code);// 发送验证码
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

	public void registTask() {
		if (NetUtils.getAPNType(context) == -1) {//当前网络不可用
			ActivityUtil.showShortToast(context, getString(R.string.no_network));
			return;
		}
		CustomProgressDialog.show(context, true, null).setCanceledOnTouchOutside(false);
		registTask = new RegistTask(context, phone, pwd, code, callback);
		registTask.excute();
	}
	
	BaseRequestCallBack callback = new BaseRequestCallBack() {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			CustomProgressDialog.disMiss();
			ActivityUtil.showShortToast(context, alert+" ");
			if(code != Constant.SUCCESS_REQUEST){
				return;	//請求失敗
			}
			activity.changeFragment(0); //切换到登录界面
			activity.changeLoginFragment(phone);
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
		}
	};

	// 验证码定时
	private class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			btn_getCode.setText(millisUntilFinished / 1000 + "秒");
			btn_getCode.setClickable(false);
			btn_getCode.setEnabled(false);
		}

		@Override
		public void onFinish() {
			btn_getCode.setText(getString(R.string.start_regist_getcode));
			btn_getCode.setClickable(true);
			btn_getCode.setEnabled(true);
		}
	}

	// 验证码
	Handler smssdkHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			if (result == SMSSDK.RESULT_COMPLETE) {
				// 短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					//验证码输入正确
					ActivityUtil.showShortToast(context, getString(R.string.start_verificationCode_isRight));
					registTask();// 注册
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

	@Override
	public void onDestroy() {
		myCount.cancel();
		SMSSDK.unregisterAllEventHandler();
		super.onDestroy();
	}

}
