package com.xiaoxu.music.community.activity;

import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.MineSongAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.MusicEntity;
import com.xiaoxu.music.community.entity.SongMenuInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.MoreRecommendSongMenuTask;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.RotateLoading;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class RecommendSongMenuActivity extends BaseNewActivity implements 
	OnXListViewListener, OnClickListener,OnItemClickListener{
	
	private TextView title_center;
	private ImageButton right_btn;
	private XListView list_View;
	private String latelyRefreshTime;// 最近刷新时间
	
	private int pageNum = 1;
	private MineSongAdapter adapter;
	private List<SongMenuInfoEntity> list, oldlist;
	
	private MoreRecommendSongMenuTask task;
	
	private final String mPageName = "RecommendSongMenuActivity";

	@Override
	public void setContent() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_mineattention);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
	}
	
	@Override
	public void setupView() {
		// TODO Auto-generated method stub
		getImageButton(R.id.title_left_btn).setOnClickListener(this);
		right_btn = (ImageButton) findViewById(R.id.title_right_btn);
		right_btn.setOnClickListener(this);
		initAnimation(right_btn);
		title_center = (TextView) findViewById(R.id.title_center_content);
		rotateLoading = (RotateLoading) findViewById(R.id.loading);
		
		list_View = (XListView) findViewById(R.id.list_view);
		list_View.setPullLoadEnable(XListView.FOOTER_HIDE);
		list_View.setFooterReady(true);
		list_View.setXListViewListener(this);
		list_View.setOnItemClickListener(this);
		adapter = new MineSongAdapter(context,list);
		list_View.setAdapter(adapter);
	}

	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		title_center.setText("专题推荐");
		task();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(mPageName);//友盟统计
		MobclickAgent.onResume(this);//友盟统计
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}
	
	public void task() {
		if (haveNetwork()) {
			loadingStart(rotateLoading);
			task = new MoreRecommendSongMenuTask(context, pageNum, callback);
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
                	list_View.setPullLoadEnable(XListView.FOOTER_HIDE);
                	list_View.stopLoadMore();
                	list_View.stopRefresh();
                }
            }, Constant.DELAYMILLIS);
		}
	}
	
	BaseRequestCallBack callback = new BaseRequestCallBack(MusicEntity.class) {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			loadingCancle(rotateLoading);
			list_View.stopLoadMore();
			list_View.stopRefresh();
			if (code != Constant.SUCCESS_REQUEST) {
				return;
			}
			MusicEntity musicEntity = (MusicEntity) obj;
			List<SongMenuInfoEntity> lists = musicEntity.getList_musiclist();
			if (null != lists && lists.size() > 0) {
				list = lists;
				updateUI();
			}else{
				list_View.setPullLoadEnable(XListView.FOOTER_HIDE);
			}
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			
			loadingCancle(rotateLoading);
			list_View.stopLoadMore();
			list_View.stopRefresh();
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
						long old_id = Long.parseLong(oldlist.get(j).getMusiclist_id());
						long new_id = Long.parseLong(list.get(i).getMusiclist_id());
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
			list_View.setPullLoadEnable(XListView.FOOTER_WAIT);
		} else {
			list_View.setPullLoadEnable(XListView.FOOTER_HIDE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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
	public void onRefresh() {
		// TODO Auto-generated method stub
		list_View.setPullLoadEnable(XListView.FOOTER_HIDE);
		list_View.setRefreshTime(latelyRefreshTime);
		pageNum = 1;
		task();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		task();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		oldlist = adapter.getList();
		Intent in = new Intent(context, MusicListActivity.class);
		in.putExtra("musiclist_id", oldlist.get(position-1).getMusiclist_id());
		in.putExtra("musiclist_name", oldlist.get(position-1).getMusiclist_name());
		context.startActivity(in);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(task != null){
			task.cancelTask();
		}
	}

}
