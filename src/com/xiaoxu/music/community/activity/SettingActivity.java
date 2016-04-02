package com.xiaoxu.music.community.activity;

import java.io.File;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.easemob.EMCallBack;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.UpdateInfoEntity;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.FileUtil;
import com.xiaoxu.music.community.util.PushSettingUtils;
import com.xiaoxu.music.community.util.SharedPreUtils;

public class SettingActivity extends BaseNewActivity implements OnClickListener{
	
	private TextView tv_new;
	private Button btn_exit;
	private TextView dw_loadtv;
	private ImageButton left_btn;
	private ImageButton right_btn;
	private RelativeLayout layout_exit;
	private TextView title_content,cache_size,version_name;
	
	private Dialog dialog;
	private UpdateInfoEntity entity;
	private int versionCode = -1;
	private boolean onlyWifi;
	private boolean pushState = false;
	private ImageButton switch_push,switch_wifi;
	
	private final String mPageName = "SettingActivity";
	
	@Override
	public void setContent() {
		setContentView(R.layout.activity_setting);
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		pushState = PushSettingUtils.getPushState(context);
		onlyWifi = SharedPreUtils.isOnlyWifi(context);
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
		
		dialog = new Dialog(context, R.style.Custom_Dialog);
		left_btn = getImageButton(R.id.title_left_btn);
		right_btn = getImageButton(R.id.title_right_btn);
		right_btn.setVisibility(View.GONE);
		title_content = getTextView(R.id.title_center_content);
		title_content.setText(getString(R.string.setting));
		layout_exit = getRelativeLayout(R.id.setting_layout_exit);
		btn_exit = getButton(R.id.btn_exit);//退出
		cache_size = getTextView(R.id.cache_size);//缓存大小
		version_name = getTextView(R.id.version_name);
		tv_new = getTextView(R.id.new_tv);
		tv_new.setVisibility(View.GONE);
		checkVersion();
//		dw_loadtv = getTextView(R.id.dw_loadtv);
		
		switch_push = getImageButton(R.id.btn_switch_push);
		switch_wifi = getImageButton(R.id.btn_switch_wifi);
		
		switch_push.setOnClickListener(this);
		switch_wifi.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		left_btn.setOnClickListener(this);
		
		getRelativeLayout(R.id.setting_layout_update_pwd).setOnClickListener(this);
		getRelativeLayout(R.id.setting_layout_spitslot).setOnClickListener(this);
//		getRelativeLayout(R.id.setting_layout_dw_load).setOnClickListener(this);
		getRelativeLayout(R.id.setting_layout_about_app).setOnClickListener(this);
		getRelativeLayout(R.id.setting_layout_attention_app).setOnClickListener(this);
		getRelativeLayout(R.id.setting_layout_share_app).setOnClickListener(this);
		getRelativeLayout(R.id.setting_layout_version_change).setOnClickListener(this);
		getRelativeLayout(R.id.setting_layout_clear_cache).setOnClickListener(this);
	}
	
	@Override
	public void setModel() {
		
		layout_exit.setVisibility(View.GONE);
		if(AccountInfoUtils.getInstance(context).isLogin()){
			layout_exit.setVisibility(View.VISIBLE);
		}
		
		if(pushState) {
			switch_push.setBackgroundResource(R.drawable.btn_switch_on);
		} else {
			switch_push.setBackgroundResource(R.drawable.btn_switch_off);
		}
		
		if(onlyWifi) {
			switch_wifi.setBackgroundResource(R.drawable.btn_switch_on);
		}else {
			switch_wifi.setBackgroundResource(R.drawable.btn_switch_off);
		}
		cache_size.setText(FileUtil.getFormatSize(FileUtil.getFolderSize(new File(Constant.DISK_CACHE_PATH))));
		version_name.setText(ActivityUtil.getVersionName(context));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_layout_update_pwd:
			startActivity(new Intent(context, UpdatePwdActivity.class));
			break;
		case R.id.setting_layout_spitslot:
			startActivity(new Intent(context, SpitslotActivity.class));
			break;
//		case R.id.setting_layout_dw_load:
//			showAddMenu();
//			break;
		case R.id.setting_layout_about_app://关于
			startActivity(new Intent(context, AboutAppActivity.class));
			break;
		case R.id.setting_layout_attention_app://关注
			startActivity(new Intent(context, AttentionActivity.class));
			break;
		case R.id.setting_layout_share_app://分享
			startActivity(new Intent(context, ShareAppActivity.class));
			break;
		case R.id.setting_layout_version_change://版本更新
			if(versionCode == 0){
				showUpdateDialog(entity);
			} else if(versionCode == 1){ 
				ActivityUtil.showShortToast(context, "已是最新版本");
			}
			break;
		case R.id.setting_layout_clear_cache://清除缓存
			FileUtil.deleteFilesByDirectory(new File(Constant.DISK_CACHE_PATH));
			cache_size.setText(FileUtil.getFormatSize(FileUtil.getFolderSize(new File(Constant.DISK_CACHE_PATH))));
			break;
		case R.id.btn_switch_wifi://开关-wifi
			if (onlyWifi) { // true
				onlyWifi = false;
//				ActivityUtil.showShortToast(context, "关闭仅在WIFI");
				SharedPreUtils.setOnlyWifi(context, false);
				switch_wifi.setBackgroundResource(R.drawable.btn_switch_off);
			} else {
				onlyWifi = true;
//				ActivityUtil.showShortToast(context, "开启仅在WIFI");
				SharedPreUtils.setOnlyWifi(context, true);
				switch_wifi.setBackgroundResource(R.drawable.btn_switch_on);
			}
			break;
		case R.id.btn_switch_push: //开关-推送
			if (pushState) { // true
				pushState = false;
				if(!JPushInterface.isPushStopped(getApplicationContext())){
					JPushInterface.stopPush(getApplicationContext());
				}
				PushSettingUtils.savePushState(context, pushState);
//				ActivityUtil.showShortToast(context, "推送关闭");
				switch_push.setBackgroundResource(R.drawable.btn_switch_off);
			} else {
				pushState = true;
				if(JPushInterface.isPushStopped(getApplicationContext())){
					JPushInterface.resumePush(getApplicationContext());
				}
				PushSettingUtils.savePushState(context, pushState);
//				ActivityUtil.showShortToast(context, "推送开启");
				switch_push.setBackgroundResource(R.drawable.btn_switch_on);
			}
			break;
		case R.id.btn_exit://退出
			exitDialog();
			break;
		case R.id.title_left_btn://返回
			finish();
			break;
		}
	}
	
	 /** 版本检测 */
    private void checkVersion() {
    	
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
            	
            	versionCode = updateStatus;
            	switch (updateStatus) {
        		case 0:
        			if(updateInfo != null){
        				tv_new.setVisibility(View.VISIBLE);
        				entity = new UpdateInfoEntity();
        				entity.setPath(updateInfo.path);
        				entity.setVersion(updateInfo.version);
        				float target_size = Float.valueOf(updateInfo.target_size)/1024/1024;
        		    	DecimalFormat fnum = new DecimalFormat( "##0.00 ");
        		    	String  app_size = "最新版本大小："+fnum.format(target_size) + "M";
        				entity.setTarget_size(app_size);
        				entity.setUpdatelog(updateInfo.updateLog);
        			}
        			break;
        		case 1:
        			tv_new.setVisibility(View.GONE);
        			break;
        		}
            }
        });
        UmengUpdateAgent.update(context);

    }

    private View view;
    private TextView msg;
    private TextView btn_ok,btn_cancel;
    private TextView tv_title,tv_app_version,tv_app_size,update_text;
//    private CheckBox built_in_box;
//    private CheckBox built_out_box;
//    private TextView built_inpath,built_outpath,built_insurplus_size,built_outsurplus_size;
	
    private void showUpdateDialog(final UpdateInfoEntity entity) {
    	
    	view = getLayoutInflater().inflate(R.layout.del_invitation_dialog, null);
    	tv_title = (TextView) view.findViewById(R.id.title);
    	tv_app_version = (TextView) view.findViewById(R.id.app_version);
    	tv_app_size = (TextView) view.findViewById(R.id.app_size);
    	update_text = (TextView) view.findViewById(R.id.text);
    	tv_app_version.setVisibility(View.VISIBLE);
    	tv_app_size.setVisibility(View.VISIBLE);
    	update_text.setVisibility(View.VISIBLE);
    	tv_app_version.setText("最新版本："+entity.getVersion());
    	tv_app_size.setText(entity.getTarget_size());
    	tv_title.setText("发现新版本");
		msg = (TextView) view.findViewById(R.id.msg);
		msg.setText(entity.getUpdatelog());
		btn_ok = (Button) view.findViewById(R.id.ok);
		btn_cancel =  (Button) view.findViewById(R.id.cancel);
		btn_ok.setText("立即更新");
		btn_cancel.setText("以后再说");
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
                try {
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entity.getPath())));
                } catch (Exception ex) {}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);// 按返回键是否取消
		// 设置居中
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (ActivityUtil.getScreenWidth(context) * 0.8);
		dialog.getWindow().setAttributes(lp);
		lp.dimAmount = 0.5f;// 设置背景层透明度
		dialog.show();
    }
	
//	public void showdialog(){
//		
//		view = getLayoutInflater().inflate(R.layout.select_sdcord_dialog, null);
//		built_in_box = (CheckBox) view.findViewById(R.id.built_in_box);
//		built_out_box = (CheckBox) view.findViewById(R.id.built_out_box);
//		built_inpath = (TextView) view.findViewById(R.id.built_inpath);
//		built_outpath = (TextView) view.findViewById(R.id.built_outpath);
//		built_inpath.setText(getInnerSDCardPath());
//		built_outpath.setText(getSDPath());
//		built_insurplus_size = (TextView) view.findViewById(R.id.built_insurplus_size);
//		built_outsurplus_size = (TextView) view.findViewById(R.id.built_outsurplus_size);
//		built_insurplus_size.setText(getMemFree()+getRomAvailableSize());
//		built_outsurplus_size.setText(getSdFree()+getSDAvailableSize());
//		built_in_box.setOnCheckedChangeListener(new CheckedChangeListener());
//		built_out_box.setOnCheckedChangeListener(new CheckedChangeListener());
//		btn_ok = (TextView) view.findViewById(R.id.ok);
//		btn_cancel =  (TextView) view.findViewById(R.id.cancel);
//		btn_ok.setOnClickListener(dialogclick);
//		btn_cancel.setOnClickListener(dialogclick);
//		dialog = new Dialog(context, R.style.Custom_Dialog);
//		dialog.setContentView(view);
//		dialog.setCanceledOnTouchOutside(true);
//		dialog.setCancelable(true);// 按返回键是否取消
//		// 设置居中
//		Window dialogWindow = dialog.getWindow();
//		dialogWindow.setGravity(Gravity.CENTER);
//		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//		lp.dimAmount = 0.5f;// 设置背景层透明度
//		dialog.show();
//	}
	
//	OnClickListener dialogclick = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//				case R.id.ok:
//					dialog.dismiss();
//					break;
//				case R.id.cancel: 
//					dialog.dismiss(); 
//					break;
//			}
//		}
//	};
	
//	 private class CheckedChangeListener implements OnCheckedChangeListener{
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
//				switch (buttonView.getId()) {
//				case R.id.built_in_box:
//					if(isChecked){
//						built_in_box.setChecked(isChecked);
//						dw_loadtv.setText("内置SD卡");
//					}
//					break;
//
//				case R.id.built_out_box:
//					if(isChecked){
//						built_out_box.setChecked(isChecked);
//						dw_loadtv.setText("外置SD卡");
//					}
//					break;
//				}
//			}
//	    	
//	    }
	
	/**
     * 获取内置SD卡路径
     * @return
     */
    public String getInnerSDCardPath() {  
        return Environment.getExternalStorageDirectory().getPath();  
    }
    
    /**
     * 获取外置SD卡路径
     * @return
     */
    public String getSDPath(){
    	  File sdDir = null;
    	  boolean sdCardExist = Environment.getExternalStorageState()
    	  .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
    	  if (sdCardExist){
    		  sdDir = Environment.getExternalStorageDirectory();//获取跟目录
    	  }
    	  return sdDir.toString();
    	 }

    /** 
     *  获得机身内存总大小
     * @return 
     */ 
    @SuppressLint("NewApi")
	private String getMemFree(){
      StatFs stat = new StatFs(Environment.getRootDirectory().getPath());
      //获取block的SIZE
      long blocSize = stat.getBlockSize();
      //获取BLOCK数量
      long totalBlocks = stat.getBlockCount();
      return "共"+Formatter.formatFileSize(context, blocSize * totalBlocks )+",";
    }
    
    /** 
     * 获得机身可用内存 
     * @return 
     */  
    @SuppressLint("NewApi")
	private String getRomAvailableSize() {  
    	
        File path = Environment.getRootDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        //获取block的SIZE
        long blocSize = stat.getBlockSize();
        //空闲的Block的数量
        long availaBlock = stat.getAvailableBlocks();
        return "剩余"+Formatter.formatFileSize(context,blocSize * availaBlock);  
    }  
    
    /** 得到SD可用内存 **/
    @SuppressLint("NewApi")
    private String getSdFree(){
      if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){ 
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        //获取block的SIZE
        long blocSize = stat.getBlockSize();
        //获取BLOCK数量
        long totalBlocks = stat.getBlockCount();
        return "共"+Formatter.formatFileSize(context, blocSize * totalBlocks)+",";
      }
      return "未装载SD卡";
    }
    
    /** 
     * 获得sd卡剩余容量，即可用大小 
     * @return 
     */  
    @SuppressLint("NewApi")
	private String getSDAvailableSize() {  
        File path = Environment.getExternalStorageDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        //获取block的SIZE
        long blocSize = stat.getBlockSize();
        //空闲的Block的数量 
        long availaBlock = stat.getAvailableBlocks();
        return "剩余"+Formatter.formatFileSize(context, blocSize * availaBlock);  
    }  
    
	
    private Dialog exitdialog;
	// 退出应用dialog
	public void exitDialog() {
		View view = getLayoutInflater().inflate(R.layout.del_invitation_dialog, null);
		tv_title = (TextView) view.findViewById(R.id.title);
		tv_title.setText("退出当前帐号");
		msg = (TextView) view.findViewById(R.id.msg);
		msg.setText("退出后音萌的某些重要功能你将无法使用，确定退出？");
		btn_ok = (Button) view.findViewById(R.id.ok);
		btn_cancel =  (Button) view.findViewById(R.id.cancel);
		btn_ok.setText("退出");
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HXlogout();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				exitdialog.dismiss();
			}
		});
		exitdialog = new Dialog(context, R.style.Custom_Dialog);
		exitdialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		exitdialog.setCancelable(true);// 按返回键是否取消
		// 设置居中
		Window dialogWindow = exitdialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (ActivityUtil.getScreenWidth(context) * 0.8);
		lp.dimAmount = 0.5f;// 设置背景层透明度
		exitdialog.show();
	}
	
	private void HXlogout() {

		YmApplication.getInstance().logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						AccountInfoUtils.getInstance(context).setAccountInfo("", "0", "", false);
						Intent intent = new Intent(Constant.ACTION_MINE_UPDATE_USERINFO);
						intent.putExtra("isLogin", true);
						context.sendBroadcast(intent);
						finish();
						exitdialog.dismiss();
					}
				});
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {

			}
		});
	}
	
	
	OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			} else {
				return false;
			}
		}
	};

}
