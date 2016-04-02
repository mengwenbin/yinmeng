
package com.xiaoxu.music.community.activity;

import java.util.ArrayList;
import java.util.List;

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
import com.xiaoxu.music.community.adapter.MusicListAdapter2;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.entity.SongMenuDetailEntity;
import com.xiaoxu.music.community.entity.SongMenuInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.MusicByMusicListTask;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class MusicListActivity extends BaseNewActivity implements OnClickListener,OnXListViewListener {
	
	private ImageButton btn_back,btn_play,btn_right;
	private TextView title;
	private XListView listview;
	private MusicListAdapter2 adapter;
	private String latelyRefreshTime; //最近刷新时间
	private int pageNum = 1;
	private Intent intent;
	
	private MusicByMusicListTask task;
	
	private String musiclist_id;
	private String musiclist_name;
	private SongMenuInfoEntity songMenuInfo;
	private String list_size;
	private List<SongInfoEntity> list,oldlist;
	
	private final String mPageName = "MusicListActivity";
	public String Login_Tag = "com.xiaoxu.music.community.activity.MusicListActivity";
	
	@Override
	public void setContent() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_musiclist_songs);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		intent = getIntent();
		musiclist_id = intent.getStringExtra("musiclist_id");
		musiclist_name = intent.getStringExtra("musiclist_name");
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
		// TODO Auto-generated method stub
		getImageButton(R.id.title_left_btn).setOnClickListener(this);
		btn_right = getImageButton(R.id.title_right_btn);
		btn_right.setOnClickListener(this);
		initAnimation(btn_right);
		
		rotateLoading = getRotateLoading(R.id.loading);
		title = getTextView(R.id.title_center_content);
		title.setText("歌单");
		listview = (XListView) findViewById(R.id.listview_songs);
		listview.setFooterReady(true);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		listview.setXListViewListener(this);
		adapter = new MusicListAdapter2(context, this, app, musiclist_id, pageNum);
		listview.setAdapter(adapter);
		adapter.setListview(listview);
	}
	
	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		loadingStart(rotateLoading);
		list = new ArrayList<SongInfoEntity>();
		task();
	}
	
	public void login(){
		Intent i = new Intent(context, StartActivity.class);
		i.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
		startActivity(i);
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
		default:
			break;
		}
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
	    listview.setRefreshTime(latelyRefreshTime);
		pageNum = 1;
		task();
	}

	@Override
	public void onLoadMore() {
		task();
	}
	
	public void task(){
		if(haveNetwork()){
			task = new MusicByMusicListTask(context, musiclist_id, pageNum, callback);
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
	
	BaseRequestCallBack callback = new BaseRequestCallBack(SongMenuDetailEntity.class) {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			loadingCancle(rotateLoading);
			listview.stopLoadMore();
			listview.stopRefresh();
			if(code != Constant.SUCCESS_REQUEST) {
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			
			SongMenuDetailEntity detail = (SongMenuDetailEntity) obj;
			list_size = detail.getList_size();
			SongMenuInfoEntity musiclist = detail.getMusic_list();
			if(pageNum == 1 && null != musiclist){
				songMenuInfo = musiclist;
			}
			List<SongInfoEntity> lists = detail.getList_music();
			if(null!=lists&&lists.size()>0){
				list = lists;
				updateUI();
			}else {
				listview.setPullLoadEnable(XListView.FOOTER_HIDE);
				if(pageNum==1){
					updateUI();
				}
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
			if(adapter.getCount()==0){
				list.add(0, new SongInfoEntity("-1"));
			}else if(!"-1".equals(list.get(0).getMusic_id())){
				list.add(0, new SongInfoEntity("-1"));
			}
			adapter.setList(list);
			adapter.setList_size(list_size);
			adapter.setSongmenu(songMenuInfo);
			pageNum = 2;
		} else if (pageNum >= 2) {
			oldlist = adapter.getList();
			if (oldlist != null && oldlist.size() > 0) {
				for (int i = 0; i < list.size(); i++) { // 新数据
					for (int j = oldlist.size() - 1; j >= 0; j--) { // 旧数据
						int old_id = Integer.parseInt(oldlist.get(j).getMusic_id());
						int new_id = Integer.parseInt(list.get(i).getMusic_id());
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
		if(null!=list && list.size()>=10){
			listview.setPullLoadEnable(XListView.FOOTER_WAIT);
		} else {
			listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		}
	}

}
