package com.xiaoxu.music.community.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.MusicUserAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.MusicUserEntity;
import com.xiaoxu.music.community.entity.MusicUserInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.RecommendMusicUserTask;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.RotateLoading;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class RecommendMusicUserActivity extends BaseNewActivity implements OnClickListener,
						OnXListViewListener, OnItemClickListener{
	
	private XListView listview;
	private String latelyRefreshTime; //最近刷新时间
	private int pageNum = 1;
	
	private RecommendMusicUserTask task;
	private List<MusicUserInfoEntity> list,oldlist;
	private MusicUserAdapter adapter;
	public RotateLoading rotateLoading;
	private ImageButton btn_play;
	
	private MineBroadcastReceiver receiver;
	private final String mPageName = "RecommendMusicUserActivity";
	
	@Override
	public void setContent() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_recommend_music_user);
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
		getTextView(R.id.title_center_content).setText(getString(R.string.recomment_music_user));
		getImageButton(R.id.title_left_btn).setOnClickListener(this);
		btn_play = getImageButton(R.id.title_right_btn);
		btn_play.setOnClickListener(this);
		initAnimation(btn_play);
		
		listview = (XListView) findViewById(R.id.listview_music_man);
		listview.setFooterReady(true);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		listview.setXListViewListener(this);
		adapter = new MusicUserAdapter(context,list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		
		// 注册广播 (action:修改用户信息)
		receiver = new MineBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_MINE_UPDATE_USERINFO);
		context.registerReceiver(receiver, filter);
	}

	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		loadingStart(rotateLoading);
		task();
	}
	
	public void task(){
		if(haveNetwork()){
			task = new RecommendMusicUserTask(context, pageNum, callback);
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
	
	
	BaseRequestCallBack callback = new BaseRequestCallBack(MusicUserEntity.class) {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			loadingCancle(rotateLoading);
			listview.stopLoadMore();
			listview.stopRefresh();
			if(code!=Constant.SUCCESS_REQUEST){
				return;
			}
			MusicUserEntity entity = (MusicUserEntity) obj;
			List<MusicUserInfoEntity> lists = entity.getList_user();
			if(null!=lists && lists.size()>0){
				list = lists;
				updateUI();
			}
		}
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			// TODO Auto-generated method stub
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
						long old_id = Long.parseLong(oldlist.get(j).getUser_id());
						long new_id = Long.parseLong(list.get(i).getUser_id());
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
		// TODO Auto-generated method stub
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
	    listview.setRefreshTime(latelyRefreshTime);
		pageNum = 1;
		setModel();
	}
	
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		setModel();
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			oldlist = adapter.getList();
			Intent in = new Intent(context, MusicUserDetailActivity.class);
			in.putExtra("MusicUserInfoEntity", oldlist.get(position - 1));
			in.putExtra("position", position - 1);
			startActivityForResult(in, 1);
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1 && data != null){
			int position = data.getIntExtra("position", -1);
			MusicUserInfoEntity infoEntity = (MusicUserInfoEntity) data.getSerializableExtra("MusicUserInfoEntity");
			if(position>=0 && infoEntity!=null && adapter!=null){
				oldlist = adapter.getList();
				if(oldlist!=null&&oldlist.size()>position){
					oldlist.set(position, infoEntity);
				}
				adapter.setList(oldlist);
				adapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public class MineBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null){
				onRefresh();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		context.unregisterReceiver(receiver);
	}

}
