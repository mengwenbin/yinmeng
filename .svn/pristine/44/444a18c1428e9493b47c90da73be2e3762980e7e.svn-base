package com.xiaoxu.music.community.model.task;

import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xiaoxu.music.community.constant.APIUrl;
import com.xiaoxu.music.community.model.impl.BaseRequestTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * 吐槽接口
 * @author ice
 *
 */
public class SpitslotTask extends BaseRequestTask {

	public SpitslotTask(Context context, String content,
			RequestCallBack<String> callback) {
		super(context, callback);
		params.addBodyParameter("Faq[faq_content]", content);
		params.addBodyParameter("user_id",AccountInfoUtils.getInstance(context).getAccountId());
		params.addBodyParameter("Faq[faq_title]", "");
		url = APIUrl.BASE_URL+APIUrl.FEEDBACK_IDEA;
	}
	
}
