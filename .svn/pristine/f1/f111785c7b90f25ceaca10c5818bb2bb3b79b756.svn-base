package com.xiaoxu.music.community.activity;

import java.util.List;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.InformAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.InfromEntity;
import com.xiaoxu.music.community.entity.InfromInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.InformTask;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.RotateLoading;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class SysteminformActivity extends BaseNewActivity implements OnClickListener, OnXListViewListener {

	private XListView listView;
	private TextView center_title;
	private ImageButton left_btn;
	private ImageButton right_btn;
	private InformTask task;
	private List<InfromInfoEntity> list;
	private InformAdapter adapter;
	
	private String latelyRefreshTime;// 最近刷新时间
	private final String mPageName = "SysteminformActivity";
	
	@Override
	public void setupView() {
		
		rotateLoading = (RotateLoading) findViewById(R.id.loading);
		center_title = (TextView) findViewById(R.id.title_center_content);
		left_btn = (ImageButton) findViewById(R.id.title_left_btn);
		right_btn = (ImageButton) findViewById(R.id.title_right_btn);
		right_btn.setVisibility(View.GONE);
		center_title.setText("系统通知");
		listView = (XListView) findViewById(R.id.listview);
		left_btn.setOnClickListener(this);
		listView.setPullLoadEnable(XListView.FOOTER_HIDE);
		listView.setFooterReady(true);
		listView.setXListViewListener(this);
		
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
	public void setContent() {
		setContentView(R.layout.activity_systeminform);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
	}

	@Override
	public void setModel() {
		
		adapter = new InformAdapter(context, list);
		listView.setAdapter(adapter);
		task();
	}

	/*
	 * 请求数据task
	 */
	public void task() {
		if (haveNetwork()) {
			loadingStart(rotateLoading);
			task = new InformTask(context,callback);
			task.excute();
		} else {
			if(adapter != null){
				adapter.setList(null);
				adapter.notifyDataSetChanged();
				showNoNet(this);
			}
			new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                	listView.setPullLoadEnable(XListView.FOOTER_HIDE);
        			listView.stopLoadMore();
        			listView.stopRefresh();
                }
            }, Constant.DELAYMILLIS);
		}
	}

	BaseRequestCallBack callback = new BaseRequestCallBack(InfromEntity.class) {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			
			loadingCancle(rotateLoading);
			listView.stopLoadMore();
			listView.stopRefresh();
			if (code != Constant.SUCCESS_REQUEST) {
				return;
			}
			InfromEntity infromEntity = (InfromEntity) obj;
			list = infromEntity.getList_message();
			adapter.setList(list);
			adapter.notifyDataSetChanged();
			latelyRefreshTime = TimeUtil.getSystemTimer(context);
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			loadingCancle(rotateLoading);
			listView.stopLoadMore();
			listView.stopRefresh();
		}
	};
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.title_left_btn:
			finish();
			break;
		case R.id.no_network:
			hintNoNet();
			task();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(task != null){
			task.cancelTask();
		}
	}

	@Override
	public void onRefresh() {
		listView.setPullLoadEnable(XListView.FOOTER_HIDE);
		listView.setRefreshTime(latelyRefreshTime);
		task();
	}

	@Override
	public void onLoadMore() {
		task();
	}

}
