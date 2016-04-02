package com.xiaoxu.music.community.model.task;

import android.content.Context;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;


public class BarrageTask extends BaseRequestTask{

	/*
	 * 请求弹幕列表
	 */
	public BarrageTask(Context context,String music_id,RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("Barrage[item_id]", music_id);
		url = APIUrl.BASE_URL+APIUrl.BARRAGE_LIST;
	}
	
	/*
	 * 发送弹幕
	 */
	public BarrageTask(Context context,String music_id,String content,int current_time,RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("token", AccountInfoUtils.getInstance(context).getAccountToken());
		params.addBodyParameter("user_id", AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("Barrage[item_id]", music_id);
		params.addBodyParameter("Barrage[barrage_body]", content);
		params.addBodyParameter("Barrage[time_current]", current_time + "");
		url = APIUrl.BASE_URL+APIUrl.SEND_BARRAGE;
	}
	
	
}
