package com.xiaoxu.music.community.im.activity;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import com.easemob.chat.EMMessage;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;


public class ContextMenu extends BaseNewActivity {

	private int position;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void copy(View view){
		setResult(ChatActivity.RESULT_CODE_COPY, new Intent().putExtra("position", position));
		finish();
	}
	public void delete(View view){
		setResult(ChatActivity.RESULT_CODE_DELETE, new Intent().putExtra("position", position));
		finish();
	}
	public void forward(View view){
		setResult(ChatActivity.RESULT_CODE_FORWARD, new Intent().putExtra("position", position));
		finish();
	}

	@Override
	public void setupView() {
		
	}

	@Override
	public void setContent() {
		int type = getIntent().getIntExtra("type", -1);
		if (type == EMMessage.Type.TXT.ordinal()) {
		    setContentView(R.layout.context_menu_for_text);
		} else if (type == EMMessage.Type.IMAGE.ordinal()) {
		    setContentView(R.layout.context_menu_for_image);
		} else if (type == EMMessage.Type.VOICE.ordinal()) {
		    setContentView(R.layout.context_menu_for_voice);
		}
		    
		position = getIntent().getIntExtra("position", -1);
	}

	@Override
	public void setModel() {
		
	}
	
}
