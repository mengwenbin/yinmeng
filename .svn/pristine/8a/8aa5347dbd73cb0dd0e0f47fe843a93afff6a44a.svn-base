package com.xiaoxu.music.community.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.activity.MainTabActivity;
import com.xiaoxu.music.community.activity.ResetPasswordActivity;
import com.xiaoxu.music.community.activity.StartActivity;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.UserEntity;
import com.xiaoxu.music.community.entity.UserInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.LoginTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.NetUtils;
import com.xiaoxu.music.community.view.ClearEditText;
import com.xiaoxu.music.community.view.CustomProgressDialog;
import com.xiaoxu.music.community.view.RotateLoading;

public class LoginFragment extends Fragment {

	public static String savaUserInfoURL;// 保存用户信息的路径
	
	private Context context;
//	private TextView loading_text;
//	private RotateLoading rotateLoading;
	private ClearEditText edit_name, edit_pwd;// 帐号密码输入框
	private Button btn_login;// 登录
	private TextView forget_pwd;// 忘记密码
	private ImageButton btn_informal;// 随便逛逛按钮
	
	private String user_name, user_pwd;
	private LoginTask task;// 登录的请求task
	
	private final  String mPageName = "LoginFragment";
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contentView = inflater.inflate(R.layout.fragment_login, container,false);
		
		// 友盟统计打开测试模式
		MobclickAgent.setDebugMode(true);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，来禁止默认的Activity页面统计方式。避免重复统计。
		MobclickAgent.openActivityDurationTrack(false);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(getActivity());
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		edit_name = (ClearEditText) contentView.findViewById(R.id.login_name);
		edit_pwd = (ClearEditText) contentView.findViewById(R.id.login_pwd);
		btn_login = (Button) contentView.findViewById(R.id.start_btn_login);
		forget_pwd = (TextView) contentView.findViewById(R.id.start_tv_forget_pwd);
		btn_informal = (ImageButton) contentView.findViewById(R.id.start_imagebutton_informal);
//		loading_text = (TextView) contentView.findViewById(R.id.loading_text);
//		rotateLoading = (RotateLoading) contentView.findViewById(R.id.loading);
		btn_login.setOnClickListener(l);// 登录
		btn_informal.setOnClickListener(l);// 随便逛逛
		forget_pwd.setOnClickListener(l);// 忘记密码
//		loading_text.setVisibility(View.GONE);
		
		String account = AccountInfoUtils.getInstance(context).getAccountNumber();
		if(!TextUtils.isEmpty(account)){
			edit_name.setText(account);
		}
		return contentView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
		MobclickAgent.onResume(getActivity());//友盟统计
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(getActivity());
	}
	
	public void authEdit(String num){
		edit_name.setText(num);
	}
	
	OnClickListener l = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.start_btn_login:// 登录
				login();
				break;
			case R.id.start_imagebutton_informal: //随便逛逛
				// 判断跳转到那个Activity
				if(TextUtils.isEmpty(StartActivity.toActivity)) {
					Intent in = new Intent(context, MainTabActivity.class);
					startActivity(in);
				}
				getActivity().finish();
				break;
			case R.id.start_tv_forget_pwd: // 忘记密码
				Intent in2 = new Intent(context, ResetPasswordActivity.class);
				startActivity(in2);
				break;
			}
		}
	};

	public void login() {
		
		user_name = edit_name.getText().toString() + "";
		user_pwd = edit_pwd.getText().toString() + "";
		if (TextUtils.isEmpty(user_name)) {
			ActivityUtil.showShortToast(context, getString(R.string.start_username_can_not_be_empty));
			return;
		}
		
		if (TextUtils.isEmpty(user_pwd)) {
			ActivityUtil.showShortToast(context, getString(R.string.start_password_can_not_be_empty));
			return;
		}
		// 判断网络状态
		if (NetUtils.getAPNType(context) == -1) {
			ActivityUtil.showShortToast(context, getString(R.string.no_network));
			return;
		}
		CustomProgressDialog.show(context, true, null).setCanceledOnTouchOutside(false);
//		loadingStart(rotateLoading);
		task = new LoginTask(context, user_name, user_pwd, callback);
		task.excute();
	}
	
	/**
	 * 登录环信
	 */
	public void loginHX(final String username,final String password) {

		//如果是第一次登陆环信退出环信
		YmApplication.getInstance().logout(new EMCallBack() {
			@Override
			public void onSuccess() {}
			@Override
			public void onProgress(int arg0, String arg1) {}
			@Override
			public void onError(int arg0, String arg1) {}
		});
		if (TextUtils.isEmpty(username)) {
			ActivityUtil.showShortToast(context, getString(R.string.User_name_cannot_be_empty));
			return;
		}
		if (TextUtils.isEmpty(password)) {
			ActivityUtil.showShortToast(context, getString(R.string.Password_cannot_be_empty));
			return;
		}

		// 调用sdk登陆方法登陆聊天服务
		EMChatManager.getInstance().login(username, password, new EMCallBack() {

			@Override
			public void onSuccess() {
				
				try {
					CustomProgressDialog.disMiss();
					// ** 第一次登录或者之前logout后再登录，加载所有本地会话
					EMChatManager.getInstance().loadAllConversations();
					Intent in = null;
					if(!TextUtils.isEmpty(StartActivity.toActivity)){
						in = new Intent(getActivity(), Class.forName(StartActivity.toActivity));
						getActivity().setResult(Activity.RESULT_OK, in);
					}else{
						in = new Intent(context, MainTabActivity.class);
						startActivity(in);
					}
					getActivity().finish();
				} catch (Exception e) {
					e.printStackTrace();
					// 获取好友失败，不让进入首页
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							
							CustomProgressDialog.disMiss();
							ActivityUtil.showShortToast(context, getString(R.string.login_failure_failed));
							Intent in = null;
							if(!TextUtils.isEmpty(StartActivity.toActivity)){
								try {
									in = new Intent(getActivity(), Class.forName(StartActivity.toActivity));
									getActivity().setResult(Activity.RESULT_OK, in);
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
							}else{
								in = new Intent(context, MainTabActivity.class);
								startActivity(in);
							}
							getActivity().finish();
						}
					});
					return;
				}
			}

			@Override
			public void onError(final int code, final String message) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						CustomProgressDialog.disMiss();
						ActivityUtil.showShortToast(context, getString(R.string.login_failure_failed)+message);
						Intent in = null;
						if(!TextUtils.isEmpty(StartActivity.toActivity)){
							try {
								in = new Intent(getActivity(), Class.forName(StartActivity.toActivity));
								getActivity().setResult(Activity.RESULT_OK, in);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}else{
							in = new Intent(context, MainTabActivity.class);
							startActivity(in);
						}
						getActivity().finish();
					}
				});
			}

			@Override
			public void onProgress(int arg0, String arg1) {
				
			}
		});
	}
	BaseRequestCallBack callback = new BaseRequestCallBack(UserEntity.class) {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			AccountInfoUtils.getInstance(context).setAccountNumber(user_name);
			if(code != Constant.SUCCESS_REQUEST){
				CustomProgressDialog.disMiss();
				ActivityUtil.showShortToast(context, alert+" ");
				return;	// 登录失敗
			}
			
			UserEntity user_entity = (UserEntity) obj;
			UserInfoEntity user_info = user_entity.getUser();
			// SharedPreferences 保存到用户信息
			savaUserInfoURL = context.getFilesDir().getAbsolutePath()+"/user";
			
			// 发广播:提示MineFragment更新用户信息
			Intent changeintent = new Intent(Constant.ACTION_MINE_UPDATE_USERINFO);
			changeintent.putExtra("isLogin", true);
			context.sendBroadcast(changeintent);
			
			AccountInfoUtils.getInstance(context).setAccountInfo(user_info.getToken(),user_info.getUser_id(),result,true); 
			// 判断跳转到那个Activity
			loginHX(user_info.getImEaseMob_username(), user_info.getImEaseMob_password());
//			if(!TextUtils.isEmpty(StartActivity.toActivity)) { 
//				loginHX(user_info.getImEaseMob_username(), user_info.getImEaseMob_password());
//			} else {
//			}
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			CustomProgressDialog.disMiss();
		}
		
	};
	
//	private void loadingStart(RotateLoading loading){
//		rotateLoading = loading;
//		if(rotateLoading != null)
//			loading_text.setVisibility(View.VISIBLE);
//			rotateLoading.start();
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				loadingCancle(rotateLoading);
//			}
//		}, 5000);
//	}
	
//	private void loadingCancle(RotateLoading rotateLoading){
//		if(rotateLoading!=null&&rotateLoading.isStart()){
//			loading_text.setVisibility(View.GONE);
//			rotateLoading.stop();
//		}
//	}

}
