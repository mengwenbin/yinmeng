package com.xiaoxu.music.community.activity;

import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.VideoListAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.AdvertisementInfoEntity;
import com.xiaoxu.music.community.entity.VideoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.VideoTask;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class VideoActivity extends BaseNewActivity implements OnClickListener,OnXListViewListener{

	private TextView title;
	private XListView listview;
	private int pageNum = 1;
	private VideoTask task;
	private VideoListAdapter adapter;
	private String latelyRefreshTime; //最近刷新时间
	
	private ImageButton btn_back;
	private ImageButton btn_play;
	private List<AdvertisementInfoEntity> list,oldlist;
	
	private final String mPageName = "VideoActivity";
	
	@Override
	public void setContent() { 
		
		setContentView(R.layout.activity_recommend_music);
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
		
		rotateLoading = getRotateLoading(R.id.loading);
		btn_back = getImageButton(R.id.title_left_btn);
		btn_play = getImageButton(R.id.title_right_btn);
		initAnimation(btn_play);
		
		title = getTextView(R.id.title_center_content);
		listview = (XListView) findViewById(R.id.listview_music);
		listview.setFooterReady(true);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		listview.setXListViewListener(this);
		listview.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, true, true));
		adapter = new VideoListAdapter(context,list);
		listview.setAdapter(adapter);
		
		btn_back.setOnClickListener(this);
		btn_play.setOnClickListener(this);
	}
	
	@Override
	public void setModel() {
		
		title.setText("牛人视频");
		loadingStart(rotateLoading);
		task();
	}
	
	public void task(){
		if(haveNetwork()){
			task = new VideoTask(context,pageNum, callback);
			task.excute();
		}else{
			if(adapter != null){
				adapter.setList(null);
				adapter.notifyDataSetChanged();
				pageNum = 1;
				showNoNet(this);
			}
			loadingCancle(rotateLoading);
			new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                	listview.stopLoadMore();
        			listview.stopRefresh();
        			listview.setPullLoadEnable(XListView.FOOTER_HIDE);
                }
            }, Constant.DELAYMILLIS);
		}
	}
	
	BaseRequestCallBack callback = new BaseRequestCallBack(VideoEntity.class) {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			loadingCancle(rotateLoading);
			listview.stopLoadMore();
			listview.stopRefresh();
			if(code!=Constant.SUCCESS_REQUEST){
				return;
			}
			VideoEntity entity = (VideoEntity) obj;
			List<AdvertisementInfoEntity> lists = entity.getList_res();
			if(null!=lists && lists.size()>0){
				list = lists;
				updateUI();
			}else{
				listview.setPullLoadEnable(XListView.FOOTER_HIDE);
			}
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			loadingCancle(rotateLoading);
			listview.stopLoadMore();
			listview.stopRefresh();
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
						int old_id = Integer.parseInt(oldlist.get(j).getRes_id());
						int new_id = Integer.parseInt(list.get(i).getRes_id());
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
		if(list.size()>=10){
			listview.setPullLoadEnable(XListView.FOOTER_WAIT);
		} else {
			listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		}
	}


	@Override
	public void onRefresh() {
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
	    listview.setRefreshTime(latelyRefreshTime);
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
	
}
