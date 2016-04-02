package com.xiaoxu.music.community.im.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ProgressBar;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.FileMessageBody;
import com.easemob.util.FileUtils;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.util.ActivityUtil;

public class ShowNormalFileActivity extends BaseNewActivity {
	
	private File file;
	private Intent intent;
	private ProgressBar progressBar;
	private FileMessageBody messageBody;
	
	@Override
	public void setContent() {
		
		setContentView(R.layout.activity_show_file);
		intent = getIntent();
		messageBody = intent.getParcelableExtra("msgbody");
	}

	@Override
	public void setupView() {
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	@Override
	public void setModel() {
		
		file = new File(messageBody.getLocalUrl());
		final Map<String, String> maps = new HashMap<String, String>();
		if (!TextUtils.isEmpty(messageBody.getSecret())) {
			maps.put("share-secret", messageBody.getSecret());
		}
		//下载文件
		EMChatManager.getInstance().downloadFile(messageBody.getRemoteUrl(), messageBody.getLocalUrl(), maps,
                new EMCallBack() {
                    
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                FileUtils.openFile(file, ShowNormalFileActivity.this);
                                finish();
                            }
                        });
                    }
                    
                    @Override
                    public void onProgress(final int progress,String status) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progress);
                            }
                        });
                    }
                    
                    @Override
                    public void onError(int error, final String msg) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if(file != null && file.exists()&&file.isFile())
                                    file.delete();
                                ActivityUtil.showShortToast(context, 
                                getResources().getString(R.string.Failed_to_download_file));
                                finish();
                            }
                        });
                    }
                });
		
	}
}
