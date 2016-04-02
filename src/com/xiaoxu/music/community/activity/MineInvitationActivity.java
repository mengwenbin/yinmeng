package com.xiaoxu.music.community.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
import com.xiaoxu.music.community.adapter.MineInvitationAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.BlogEntity;
import com.xiaoxu.music.community.entity.BlogsInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.CircleBlogsTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.RotateLoading;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class MineInvitationActivity extends BaseNewActivity implements
		OnXListViewListener, OnClickListener{

	private Activity activity;
	private TextView title_center;
	private ImageButton left_btn;
	private ImageButton right_btn;
	private XListView List_View;
	private String latelyRefreshTime;// 最近刷新时间

	private int pageNum = 1;
	private BlogEntity blog;
	private CircleBlogsTask task;
	private MineInvitationAdapter adapter;
	private List<BlogsInfoEntity> list, oldlist;
	
	private final String mPageName = "MineInvitationActivity";
	private String Login_Tag = "com.xiaoxu.music.community.activity.MineInvitationActivity";
	
	@Override
	public void setupView() {
		
		left_btn = (ImageButton) findViewById(R.id.title_left_btn);
		right_btn = (ImageButton) findViewById(R.id.title_right_btn);
		initAnimation(right_btn);
		List_View = (XListView) findViewById(R.id.list_view);
		title_center = (TextView) findViewById(R.id.title_center_content);
		rotateLoading = (RotateLoading) findViewById(R.id.loading);
		List_View.setPullLoadEnable(XListView.FOOTER_HIDE);
		List_View.setFooterReady(true);
		List_View.setXListViewListener(this);
		initListener();
		
		adapter = new MineInvitationAdapter(activity,list);
		List_View.setAdapter(adapter);

	}
	
	private void initListener() {

		left_btn.setOnClickListener(this);
		right_btn.setOnClickListener(this);
	}

	@Override
	public void setContent() {
		setContentView(R.layout.activity_mineinvitation);
		activity = this;
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
		MobclickAgent.onResume(activity);//友盟统计
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(activity);
	}

	@Override
	public void setModel() {
		
		if(!AccountInfoUtils.getInstance(context).isLogin()){
			Intent intent = new Intent(context, StartActivity.class);
			intent.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
			startActivityForResult(intent, 1);
		}
		title_center.setText("我的帖子");
		task();
	}

	public void task() {
		if (haveNetwork()) {
			loadingStart(rotateLoading);
			task = new CircleBlogsTask(context, pageNum, callback);
			task.excute();
		} else {
			if(adapter != null){
				adapter.setList(null);
				adapter.notifyDataSetChanged();
				pageNum = 1;
				showNoNet(this);
			}
			new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                	List_View.setPullLoadEnable(XListView.FOOTER_HIDE);
        			List_View.stopLoadMore();
        			List_View.stopRefresh();
                }
            }, Constant.DELAYMILLIS);
		}
	}

	BaseRequestCallBack callback = new BaseRequestCallBack(BlogEntity.class) {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			loadingCancle(rotateLoading);
			List_View.stopLoadMore();
			List_View.stopRefresh();
			if (code != Constant.SUCCESS_REQUEST) {
				return;
			}
			blog = (BlogEntity) obj;
			List<BlogsInfoEntity> lists = blog.getList_blogThread();
			if (null != lists && lists.size() > 0) {
				list = lists;
				updateUI();
			}else{
				List_View.setPullLoadEnable(XListView.FOOTER_HIDE);
			}
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			loadingCancle(rotateLoading);
			List_View.stopLoadMore();
			List_View.stopRefresh();
		}
	};

	public void updateUI() {
		if (pageNum == 1) {
			adapter.setList(list);
			pageNum = 2;
		} else if (pageNum >= 2) {
			oldlist = adapter.getList();
			if (oldlist != null && oldlist.size() > 0) {
				for (int i = 0; i < list.size(); i++) { // 新数据
					for (int j = oldlist.size() - 1; j >= 0; j--) { // 旧数据
						long old_id = Long.parseLong(oldlist.get(j).getTid());
						long new_id = Long.parseLong(list.get(i).getTid());
						if (old_id == new_id) {
							oldlist.remove(j);
							break;
						}
					}
				}
				oldlist.addAll(list);
			}
			adapter.setList(oldlist);
			pageNum++;
		}
		adapter.notifyDataSetChanged();
		latelyRefreshTime = TimeUtil.getSystemTimer(context);
		if (list.size() >= 10) {
			List_View.setPullLoadEnable(XListView.FOOTER_WAIT);
		} else {
			List_View.setPullLoadEnable(XListView.FOOTER_HIDE);
		}
	}
	@Override
	public void onRefresh() {
		List_View.setPullLoadEnable(XListView.FOOTER_HIDE);
		List_View.setRefreshTime(latelyRefreshTime);
		pageNum = 1;
		task();
	}

	@Override
	public void onLoadMore() {
		task();
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.title_left_btn:
			finish();
			break;

		case R.id.title_right_btn:
			startActivity(new Intent(context, PlayActivity.class));
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 1){
			if(!AccountInfoUtils.getInstance(context).isLogin()){
				finish();
			}
			
			pageNum = 1;
			task();
		}
		if(resultCode == 1){
			
			if(data != null){
				int position = data.getIntExtra("position", 0);
				BlogsInfoEntity blog_info = (BlogsInfoEntity) data.getSerializableExtra("blog_info");
				List<BlogsInfoEntity> list = adapter.getList();
				list.set(position, blog_info);
				adapter.setList(list);
				adapter.notifyDataSetChanged();
			}
		}
	}
	
}
