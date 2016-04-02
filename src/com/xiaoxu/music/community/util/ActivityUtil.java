package com.xiaoxu.music.community.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.xiaoxu.music.community.R;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityUtil {
	
	public static Toast toast;
	public static long lastClickTime = 0;//button最后点击的时间(防止按钮连续点击)
	public static int lastButtonId = 0;//button最后点击的时间(防止按钮连续点击)
	
	/**
	 * 判断按钮是否连续点击
	 * @param btn_id
	 * @return
	 */
	public static boolean isFastDoubleClick(int btn_id) {
        long time = System.currentTimeMillis(); 
        long timeD = time - lastClickTime;
        //如果快速点击的是同一个按钮并且点击时间<500毫秒，视为快速点击
        if (lastButtonId==btn_id && 0<timeD && timeD<500) {
            return true;
        }
        lastClickTime = time;
        lastButtonId = btn_id;
        return false;
    }
	
	/**
	 * 获取屏幕的宽
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}
	
	/**
	 * 获取屏幕的高
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}
	
	/**
	 * 短暂显示Toast消息
	 * @param context
	 * @param message
	 */
	public static void showShortToast(Context context, String message) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(message);
		if(toast==null){
			toast = new Toast(context);
		}
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 300);
		toast.setView(view);
		toast.show();
	}
	
	public static void showCollectToast(Context context,String alert){
		
		   View layout = LayoutInflater.from(context).inflate(R.layout.collect_toast_layout,null);
		   RelativeLayout rel_toast = (RelativeLayout) layout.findViewById(R.id.rel_toast);
		   TextView collect_tv = (TextView) rel_toast.findViewById(R.id.collect_tv);
		   collect_tv.setText(alert);
		   int width = ActivityUtil.getScreenWidth(context);
		   int height = ActivityUtil.getScreenHeight(context);
		   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (width * 0.4), height/6);
		   rel_toast.setLayoutParams(params);
		   Toast toast = new Toast(context);
		   toast.setGravity(Gravity.CENTER, 0, 0);
		   toast.setDuration(Toast.LENGTH_SHORT);
		   toast.setView(layout);
		   toast.show();
	}
	
	public static Toast showAttentionToast(Context context,String attention){
		
		View layout = LayoutInflater.from(context).inflate(R.layout.attention_toast_layout,null);
		RelativeLayout rel_toast = (RelativeLayout) layout.findViewById(R.id.rel_toast);
		TextView is_attention = (TextView) rel_toast.findViewById(R.id.attention);
		is_attention.setText(attention);
		int width = ActivityUtil.getScreenWidth(context);
		int height = ActivityUtil.getScreenHeight(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width/4 , height/15);
		rel_toast.setLayoutParams(params);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		return toast;
	}
	
	/**
	 * 获取软件版本号
	 * @param context
	 * @return
	 */
	public static int getOldVersion(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int version = packInfo.versionCode;
		return version;
	}
	/**
	 * 获取软件版本号
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		return version;
	}
	
	/**
     * 创建平移动画
     */
    public static Animation createTranslateAnim(Context context, int fromX, int toX) {
        TranslateAnimation tlAnim = new TranslateAnimation(fromX, toX, 0, 0);
        //自动计算时间
        long duration = (long) (Math.abs(toX - fromX) * 1.0f / getScreenWidth(context) * 4000);
        tlAnim.setDuration(duration);
        tlAnim.setInterpolator(new DecelerateAccelerateInterpolator());
        tlAnim.setFillAfter(true);

        return tlAnim;
    }
	
	
	/**
	 * 获取组件高度
	 * @param view
	 * @return
	 */
	public static int getHeight(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return view.getMeasuredHeight();
	}
	
	/**
	 * 获取组件宽度
	 * @param view
	 * @return
	 */
	public static int getWidth(View view){
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return view.getMeasuredWidth();
	}
	
	/**
	 * 重新绘制控件的宽高
	 * @param view
	 * @param width
	 * @param height
	 */
	public static void redrawViewSize(View view, int width, int height){ 
		LayoutParams params = view.getLayoutParams();
		if(params != null){
			params.width = width;
			params.height = height;
			view.setLayoutParams(params);
		}
	}
	
	/**
	 * 判断是否是wifi
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 创建日期及时间选择对话框
	 */
	public static Dialog dateDialog(Context context, final TextView et) {
		Dialog dialog = null;
		Calendar c = Calendar.getInstance();
		String time = et.getTag().toString();
		if (time != null && !"".equals(time)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date d = format.parse(time);
				c.setTime(d);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
				et.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
				Calendar cs = Calendar.getInstance();
				cs.set(Calendar.YEAR, year);
				cs.set(Calendar.MONTH, month);
				cs.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				et.setTag(format.format(cs.getTime()));
			}
		}, c.get(Calendar.YEAR), // 传入年份
				c.get(Calendar.MONTH), // 传入月份
				c.get(Calendar.DAY_OF_MONTH) // 传入天数
		);
		return dialog;
	}
	
	
	/**
	 * 获取手机信息
	 * type 1: 设备唯一标识
	 * type 2: 系统版本号
	 * type 3: 设备型号
	 * type 4: 应用程序版本号
	 * 权限 ：<uses-permission android:name="android.permission.READ_PHONE_STATE" />  
	 */
	public static String getPhoneInfo(Context context, int type) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (type) {
		case 1:// 设备唯一标识
			return telephonyManager.getDeviceId();
		case 2:// 系统版本号
			return android.os.Build.VERSION.RELEASE;
		case 3:// 设备型号
			return android.os.Build.MODEL;
		case 4:// 应用程序版本号
			try {
				return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		default:
			return "";
		}
	}
	
	
	/**
	 * 手机号码Pattern
	 */
	public static final Pattern MOBILE_NUMBER_PATTERN = Pattern
			.compile("^1[3|4|5|7|8]\\d{9}$");// /^1[3|4|5|8]\d{9}$/

	/**
	 * 手机号码否正确
	 * @param s
	 * @return
	 */
	public static boolean isMobileNumber(String s) {
		Matcher m = MOBILE_NUMBER_PATTERN.matcher(s);
		return m.matches();
	}
	

	/**
	 * 验证邮箱格式
	 * */
	public static boolean isEmail(String email) {
		String str = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 验证手机号码格式
	 * */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|8[025-9])\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	public static boolean isQQ(String qq){
		Pattern p = Pattern.compile("^[0-9]{4,11}$");
		Matcher m = p.matcher(qq);
		return m.matches();
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		if (str.length() > 9) {
			Pattern p1 = null;
			Matcher m = null;
			p1 = Pattern.compile("^0(10|2[0-5789]|\\d{3})\\d{7,8}$"); // 验证带区号的
			m = p1.matcher(str);
			return m.matches();
		} else {
			return false;
		}
	}

	/**
	 * 判断电话卡状态
	 * 
	 * @param context
	 * @return
	 */
	public static String readSIMCard(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (tm.getSimState()) { // getSimState()取得sim的状态 有下面6中状态
		case TelephonyManager.SIM_STATE_ABSENT:
			return "无卡";
		case TelephonyManager.SIM_STATE_UNKNOWN:
			return "未知状态";
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			return "需要NetworkPIN解锁";
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			return "需要PIN解锁";
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			return "需要PUK解锁";
		}
		return "正常";
	}
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 */
	public static int px2sp(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 */
	public static int sp2px(Context context, float spValue){
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * scale + 0.5f);
	}
	
	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
	

	/**
     * 根据消息内容和消息类型获取消息内容提示
     * 
     * @param message
     * @param context
     * @return
     */
    public static String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
        case IMAGE: // 图片消息
            digest = getString(context, R.string.picture);
            break;
        case VOICE:// 语音消息
            digest = getString(context, R.string.voice);
            break;
        case TXT: // 文本消息
        	TextMessageBody txtBody = (TextMessageBody) message.getBody();
            digest = txtBody.getMessage();
            break;
        case LOCATION:
        	digest = getString(context, R.string.attach_location);
        	break;
        default:
            return "";
        }
        return digest;
    }
    
    static String getString(Context context, int resId){
        return context.getResources().getString(resId);
    }
	
	
	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}
	
	/**
	 * 检测网络状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			showShortToast(context, "请连接网络");
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		showShortToast(context, "请连接网络");
		return false;
	}

}
