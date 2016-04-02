package com.xiaoxu.music.community.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.SpitslotTask;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.view.CustomProgressDialog;

public class SpitslotActivity extends BaseNewActivity implements OnClickListener{

	private ImageView btn_back;
	private TextView btn_sava,center_content;
	private EditText edit_content;
	private final String alert = "亲，你还没有吐槽 ou！";
	private final String mPageName = "SpitslotActivity";
	
	@Override
	public void setContent() {
		setContentView(R.layout.activity_spitslot);
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
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
		btn_back = getImageView(R.id.left_btn);
		btn_sava = getTextView(R.id.right_text);
		center_content = getTextView(R.id.title_center_content);
		btn_sava.setText("发送");
		edit_content = getEditText(R.id.edit_content);
		center_content.setText("我要吐槽");
		btn_back.setOnClickListener(this);
		btn_sava.setOnClickListener(this);
	}
	
	@Override
	public void setModel() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.right_text:
			String str = edit_content.getText().toString();
			if(!TextUtils.isEmpty(str)){
				task(str);
			}else{
				ActivityUtil.showShortToast(context, alert);
			}
			break;
		}
	}
	
	public void task(String content) {
		if(haveNetwork()){
			CustomProgressDialog.show(context, true, null).setCanceledOnTouchOutside(false);
			SpitslotTask task = new SpitslotTask(context, content, callback);
			task.excute();
		}
	}
	
	BaseRequestCallBack callback = new BaseRequestCallBack() {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			CustomProgressDialog.disMiss();
			ActivityUtil.showShortToast(context, alert);
			if(code != Constant.SUCCESS_REQUEST){
				return;
			}
			finish();
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			CustomProgressDialog.disMiss();
		}
	};

}
