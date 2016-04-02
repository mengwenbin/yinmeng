package com.xiaoxu.music.community.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;

/*
 * 编辑信息界面
 */
public class EditContentActivity extends BaseNewActivity implements OnClickListener{
	
	private ImageView btn_back;
	private EditText edit_content;
	private TextView title_center,btn_sava;
	private Intent intent;
	private String type;
	
	private final String mPageName = "EditContentActivity";
	@Override
	public void setContent() {
		setContentView(R.layout.activity_edit_content);
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
		title_center = getTextView(R.id.title_center_content);
		btn_sava = getTextView(R.id.right_text);
		edit_content = getEditText(R.id.edit_content);
		btn_back.setOnClickListener(this);
		btn_sava.setOnClickListener(this);
	}

	@Override
	public void setModel() {
		
		intent = getIntent();
		type = intent.getStringExtra("type");
		
		if(type == null || type.equals("")){
			return;
		}
		if(type.equals("nick")){
			title_center.setText(getString(R.string.modify_nick));
			String nick = intent.getStringExtra("key");
			edit_content.addTextChangedListener(new TextChangedListener(edit_content));
			if(!TextUtils.isEmpty(nick)){
				edit_content.setText(nick);
			}
		} else if(type.equals("local")){
			title_center.setText(getString(R.string.modify_local));
			String location  = intent.getStringExtra("key");
			edit_content.addTextChangedListener(new TextChangedListener(edit_content));
			if(!TextUtils.isEmpty(location)){
				edit_content.setText(location);
			}
		} else if(type.equals("summary")){
			title_center.setText(getString(R.string.modify_summary));
			String summary  = intent.getStringExtra("key");
			if(!TextUtils.isEmpty(summary)){
				edit_content.setText(summary);
			}
		} else if(type.equals("wish")){
			title_center.setText(getString(R.string.modify_wish));
			String wish  = intent.getStringExtra("key");
			if(!TextUtils.isEmpty(wish)){
				edit_content.setText(wish);
			}
		} else if(type.equals("life")){
			title_center.setText(getString(R.string.modify_life));
			String life  = intent.getStringExtra("key");
			if(!TextUtils.isEmpty(life)){
				edit_content.setText(life);
			}
		} else if(type.equals("twitter")){
			title_center.setText(getString(R.string.modify_twitter));
			String twitter  = intent.getStringExtra("key");
			edit_content.addTextChangedListener(new TextChangedListener(edit_content));
			if(!TextUtils.isEmpty(twitter)){
				edit_content.setText(twitter);
			}
		} else if(type.equals("qq")){
			title_center.setText(getString(R.string.modify_qq));
			String qq  = intent.getStringExtra("key");
			edit_content.addTextChangedListener(new TextChangedListener(edit_content));
			if(!TextUtils.isEmpty(qq)){
				edit_content.setText(qq);
			}
		} else if(type.equals("wechat")){
			title_center.setText(getString(R.string.modify_wechat));
			String wechat  = intent.getStringExtra("key");
			edit_content.addTextChangedListener(new TextChangedListener(edit_content));
			if(!TextUtils.isEmpty(wechat)){
				edit_content.setText(wechat);
			}
		} else if(type.equals("mailbox")){
			title_center.setText(getString(R.string.modify_mailbox));
			String mailbox  = intent.getStringExtra("key");
			edit_content.addTextChangedListener(new TextChangedListener(edit_content));
			if(!TextUtils.isEmpty(mailbox)){
				edit_content.setText(mailbox);
			}
		}
	}
	
	private class TextChangedListener implements TextWatcher{
		
		private EditText edit_text;
		private CharSequence temp;
		private int selectionStart;
		private int selectionEnd;

		public TextChangedListener(EditText edit_text) {
			super();
			this.edit_text = edit_text;
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			selectionStart = edit_text.getSelectionStart();
			selectionEnd = edit_text.getSelectionEnd();
			if (temp.length() > 25) {
				s.delete(selectionStart - 1, selectionEnd);
				int tempSelection = selectionStart;
				edit_text.setText(s);
				edit_text.setSelection(tempSelection);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.right_text:
			Intent intent = new Intent(context, EditUserInfoActivity.class);
			intent.putExtra("userinfo", edit_content.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
	}

}
