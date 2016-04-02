package com.xiaoxu.music.community.parser;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xiaoxu.music.community.entity.VersionEntity;
import com.xiaoxu.music.community.util.ActivityUtil;

public class ParseUtil {
	/**
	 * Code返回码
	 * @param json
	 * @return code
	 */
	public static int parseCode(String json) {// 获取返回状态码code
		try {
			JSONObject js = new JSONObject(json);
			return js.getInt("code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * alert
	 * @param json
	 * @return code
	 */
	public static String parseAlert(String json) {// 获取返回状态码code
		try {
			JSONObject js = new JSONObject(json);
			return js.getString("alert");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Message
	 * @param json
	 * @return code
	 */
	public static String parseMessage(String json) {// 获取返回状态码code
		try {
			JSONObject js = new JSONObject(json);
			return js.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Object
	 * @param json
	 * @return code
	 */
	public static Object parseResultObj(String json,Class classOfT) {// 获取返回状态码code
		try {
			if(classOfT == null){
				return null;
			}
			JSONObject js = new JSONObject(json);
			String result = js.getString("result");
			if(TextUtils.isEmpty(result))
				return null;
			Gson gson = new Gson();
			return gson.fromJson(result, classOfT);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



}
