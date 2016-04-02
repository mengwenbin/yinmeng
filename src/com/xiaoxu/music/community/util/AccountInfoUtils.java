package com.xiaoxu.music.community.util;

import com.google.gson.Gson;
import com.xiaoxu.music.community.entity.UserEntity;
import com.xiaoxu.music.community.entity.UserInfoEntity;
import com.xiaoxu.music.community.parser.ParseUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

public class AccountInfoUtils {
	
	private Context context;
	private Editor editor;
	private SharedPreferences sp;
	private static AccountInfoUtils accountInfoUtils;
	private static final String TAG = "AccountUtils";
	private static final String PREF_OID = "user_id";
	private static final String PREF_TOKEN = "token";
	private static final String PREF_ISFIRST = "is_first";
	
	private AccountInfoUtils(Context context){
		this.context = context;
		sp = context.getSharedPreferences(PREF_TOKEN,Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	public static AccountInfoUtils getInstance(Context context){
		
		if(accountInfoUtils == null){
			accountInfoUtils = new AccountInfoUtils(context);
		}
		return accountInfoUtils;
		
	}
	
	public void setAccountInfo(String token, String id,String json, boolean isLogin) {
		editor.putString("token", token);
		editor.putString("user_id", id);
		editor.putString("json", json);
		editor.putBoolean("isLogin", isLogin);
		editor.commit();
	}
	
	public void setAccountJson(String json) {
		editor.putString("json", json);
		editor.commit();
	}
	
	public void setIsLogin(boolean isLogin) {
		editor.putBoolean("isLogin", isLogin);
		editor.commit();
	}
	
	public void setHXid(String hxid){
		editor.putString("hxid", hxid);
		editor.commit();
	}
	
	public void setHXPwd(String hxpwd){
		editor.putString("hxpwd", hxpwd);
		editor.commit();
	}
	
	public String getHXid(){
		return sp.getString("hxid", "");
	}
	
	public String getHXpwd(){
		return sp.getString("hxpwd", "");
	}
	
	public void setNick(String nick){
		editor.putString("nick", nick);
		editor.commit();
	}
	
	public String getNick(){
		return sp.getString("nick", "");
	}
	
	public String getAccountToken() {
		return sp.getString("token", "");
	}
	
	public String getAccountId() {
		return sp.getString("user_id", "0");
	}
	
	public UserInfoEntity getUserInfo() {
		String json = sp.getString("json", "");
		if(TextUtils.isEmpty(json)){
			return null;
		}
		UserEntity entity = (UserEntity) ParseUtil.parseResultObj(json, UserEntity.class);
		return entity.getUser();
	}
	
	public boolean isLogin() {
		return sp.getBoolean("isLogin", false);
	}
	
	
	public void setAccountNumber(String accountNumber){
		editor.putString("accountNumber", accountNumber);
		editor.commit();
	}
	
	public String getAccountNumber(){
		return sp.getString("accountNumber", "");
	}
	
	
	public void userToJson(UserInfoEntity data){
		Gson gson = new Gson();
		String json = gson.toJson(new UserInfo(200, "success", "操作成功", new UserEntity(data)));
		Log.e("----json-----", json);
		Log.e("----UserInfoEntity-----", ParseUtil.parseResultObj(json, UserEntity.class).toString());
	}
	
	class UserInfo {
		int code;
		String message;
		String alert;
		UserEntity result;
		public UserInfo(int code, String message, String alert,
				UserEntity result) {
			super();
			this.code = code;
			this.message = message;
			this.alert = alert;
			this.result = result;
		}
		@Override
		public String toString() {
			return "UserInfo [code=" + code + ", message=" + message
					+ ", alert=" + alert + ", result=" + result + "]";
		}
	}
	
}
