package com.xiaoxu.music.community.fragment;

import java.util.List;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;
import com.xiaoxu.music.community.BaseNewFragment;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.SearchActivity;
import com.xiaoxu.music.community.adapter.MusicListAdapter;
import com.xiaoxu.music.community.adapter.MusicUserAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.MusicListEntity;
import com.xiaoxu.music.community.entity.MusicUserInfoEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.SearchTask;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.RotateLoading;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class SearchMusicFragment extends BaseNewFragment  implements OnClickListener,
				OnXListViewListener{
	
	private String key = "";
	private XListView listview;
	private String latelyRefreshTime; // 最近刷新时间
	private int pageNum = 1;
	
	private SearchTask task_music;
	private List<SongInfoEntity> list, oldlist;
	private MusicListAdapter adapter;
	public RotateLoading rotateLoading;
	
	private final String mPageName = "SearchMusicFragment";
	
	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_search_music;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(mPageName);//友盟统计
		MobclickAgent.onResume(activity);//友盟统计
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(activity);
	}

	@Override
	public void setupView(View view) {
		// TODO Auto-generated method stub
		rotateLoading = getRotateLoading(view, R.id.loading);
		no_network = getImageButton(view, R.id.no_network);
		
		listview = (XListView) view.findViewById(R.id.listview_search);
		listview.setFooterReady(true);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		listview.setXListViewListener(this);
		adapter = new MusicListAdapter(context, getActivity(), app, pageNum);
		listview.setAdapter(adapter);
	}

	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		
	}
	
	public void search(String search){
		key = search;
		pageNum = 1;
		task_music();
	}
	
	public void task_music(){
		if (haveNetwork(context)) {
			loadingStart(rotateLoading);
			task_music = new SearchTask(context, Constant.SEARCH_TYPE_MUSIC, key, pageNum, callback_music);
			task_music.excute();
		} else {
			if (adapter != null) {
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
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		listview.setRefreshTime(latelyRefreshTime);
		pageNum = 1;
		task_music();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		task_music();
	}
	
	BaseRequestCallBack callback_music = new BaseRequestCallBack(MusicListEntity.class) {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			loadingCancle(rotateLoading);
			listview.stopLoadMore();
			listview.stopRefresh();
			if (code != Constant.SUCCESS_REQUEST) {
				return;
			}
			MusicListEntity entity = (MusicListEntity) obj;
			List<SongInfoEntity> lists = entity.getList_music();
			if (null != lists && lists.size() > 0) {
				list = lists;
				updateUI();
			}else{
				listview.setPullLoadEnable(XListView.FOOTER_HIDE);
			}
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			// TODO Auto-generated method stub
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
						long old_id = Long.parseLong(oldlist.get(j).getMusic_id());
						long new_id = Long.parseLong(list.get(i).getMusic_id());
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
			listview.setPullLoadEnable(XListView.FOOTER_WAIT);
		} else {
			listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.no_network:
			hintNoNet();
			task_music();
			break;
		}
	}

}
