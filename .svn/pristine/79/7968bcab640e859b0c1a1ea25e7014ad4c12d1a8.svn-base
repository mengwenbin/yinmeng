package com.xiaoxu.music.community.model.task;

import android.content.Context;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;

/**
 * 读取  ---  搜索标签
 * @author ice
 */
public class SearchTagTask extends BaseRequestTask{

	public SearchTagTask(Context context, String type, RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("access_token", APIUrl.ACCESS_TOKEN);
		params.addBodyParameter("k", type);
		url = APIUrl.BASE_URL + APIUrl.SEARCH_TAG;
	}

}
