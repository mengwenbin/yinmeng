package com.xiaoxu.music.community.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.SongDetailsAdapter;
import com.xiaoxu.music.community.adapter.SongDetailsAdapter.OnChangeListSize;
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

/*
 * 歌单详情
 */
public class SongMenuDetailsActivity extends BaseNewActivity implements OnXListViewListener,OnClickListener, OnChangeListSize{

	private ImageButton left_btn;
	private ImageButton right_btn;
	private TextView center_title;
	
	private XListView listview;
	private SongDetailsAdapter adapter;
	private ArrayList<SongInfoEntity> list,oldlist;
	
	private Intent intent;
	private int position;
	private String musiclist_id;
	private String musiclist_name;
	private SongMenuInfoEntity musicInfoEntity;
	
	private int pageNum = 1;
	private Activity activity;
	private String list_size;
	private String latelyRefreshTime; //最近刷新时间
	
	private MusicByMusicListTask task;
	private final String mPageName = "SongMenuDetailsActivity";
	
	@Override
	public void setContent() {
		setContentView(R.layout.activity_songdetails);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		activity = this;
		intent = getIntent();
		musicInfoEntity = (SongMenuInfoEntity) intent.getSerializableExtra("musicInfoEntity");
		musiclist_id = musicInfoEntity.getMusiclist_id();
		musiclist_name = musicInfoEntity.getMusiclist_name();
		position = intent.getIntExtra("position", 0);
	}
	
	@Override
	public void setupView() {
		
		rotateLoading = getRotateLoading(R.id.loading);
		left_btn = getImageButton(R.id.title_left_btn);
		right_btn = getImageButton(R.id.title_right_btn);
		center_title = getTextView(R.id.title_center_content);
		initAnimation(right_btn);
		center_title.setText(musiclist_name);
		
		
		listview = (XListView) findViewById(R.id.list_view);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		listview.setXListViewListener(this);
		left_btn.setOnClickListener(this);
		right_btn.setOnClickListener(this);
		listview.setFooterReady(true);
		adapter = new SongDetailsAdapter(context, activity, app, musiclist_id, pageNum, bitmapUtils);
		listview.setAdapter(adapter);
		adapter.setChangeListSize(this);

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
	public void setModel() {
		loadingStart(rotateLoading);
		list = new ArrayList<SongInfoEntity>();
		task();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_btn:
			
			Intent intent = new Intent(context, MineSongActivity.class);
			musicInfoEntity.setC_music(list_size);
			intent.putExtra("musicInfoEntity", musicInfoEntity);
			intent.putExtra("position", position);
			setResult(1, intent);
			finish();
			break;

		case R.id.title_right_btn:
			startActivity(new Intent(context,PlayActivity.class));
			break;
		}
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
				musicInfoEntity = musiclist;
			}
			ArrayList<SongInfoEntity> lists = (ArrayList<SongInfoEntity>) detail.getList_music();
			if(null!=lists&&lists.size() > 0){
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
			adapter.setSongmenu(musicInfoEntity);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 1:
			if(data == null){
				return;
			}
			String size = data.getStringExtra("list_size");
			if(size == null){
				return;
			}
			if(size.equals(list_size)){
				list_size = "0";
				adapter.setList(null);
				adapter.notifyDataSetChanged();
			}else{
				pageNum = 1;
				task();
			}
			break;

		case 2:
			if(resultCode == 4){
				if(data != null){
					
					center_title.setText(data.getStringExtra("musiclist_name"));
					if(adapter.getList().size() > 1){
						adapter.setList(null);
					}
					onRefresh();
				}
			}
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(context, MineSongActivity.class);
			musicInfoEntity.setC_music(list_size);
			intent.putExtra("musicInfoEntity", musicInfoEntity);
			intent.putExtra("position", position);
			setResult(1, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void setListSize(String listsize) {
		list_size = listsize;
	}

}
