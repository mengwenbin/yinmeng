package com.xiaoxu.music.community.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PushSettingUtils {

	public static int startHour = 0, endHour = 23, startMin = 00, endMin = 59;// 免打扰时间
	
	// 保存提示音的状态（开启|关闭）
	public static void savePushState(Context context,boolean state) {
		SharedPreferences sp = context.getSharedPreferences("pushState",context.MODE_PRIVATE);
		Editor edit = sp.edit(); // 创建一个编辑器
		edit.putBoolean("state", state);
		edit.commit(); // 提交要存入的数据
	}
	
	// 得到提示音的状态（开启|关闭）
	public static boolean getPushState(Context context) {
		SharedPreferences sp = context.getSharedPreferences("pushState",context.MODE_PRIVATE);
		return sp.getBoolean("state", true);// 得到数据
	}

}
