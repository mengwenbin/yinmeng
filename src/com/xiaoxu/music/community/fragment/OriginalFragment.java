package com.xiaoxu.music.community.fragment;

import java.util.List;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewFragment;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.OriginalAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.MusicListEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.MusicUserSongsTask;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class OriginalFragment extends BaseNewFragment implements
		OnXListViewListener{

	private XListView listview;
	private OriginalAdapter adapter;
	private List<SongInfoEntity> list,oldlist;
	private String latelyRefreshTime;//最近刷新时间
	
	public int pageNum = 1;
	private String list_size;
	private MusicUserSongsTask task;
	private String music_category = "51";
	
	private static final int REQUEST_CODE = 0;
	private final  String mPageName = "OriginalFragment";
	
	
	@Override
	public int setLayoutId() {
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_cover_version;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
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
		
		activity = getActivity();
		rotateLoading = getRotateLoading(view, R.id.loading);
		no_network = getImageButton(view, R.id.no_network);
		listview = (XListView) view.findViewById(R.id.listview);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		listview.setFooterReady(true);
		listview.setXListViewListener(this);
		adapter = new OriginalAdapter(activity, app,list,REQUEST_CODE);
		listview.setAdapter(adapter);
	}
	
	@Override
	public void setModel() {
		task();
	}
	
	public void loading(){
		if(adapter!=null&&adapter.getCount()==0){
			loadingStart(rotateLoading);
			onRefresh();
		}
	}
	
	public void task(){
		if(haveNetwork(context)){
			loadingStart(rotateLoading);
			task = new MusicUserSongsTask(context,music_category, pageNum, callback);
			task.excute();
		}else{
			if(adapter != null){
				adapter.setList(null);
				adapter.notifyDataSetChanged();
				pageNum = 1;
				showNoNet(new ClickListener());
			}
			new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                	listview.setPullLoadEnable(XListView.FOOTER_HIDE);
        			listview.stopLoadMore();
        			listview.stopRefresh();
                }
            }, Constant.DELAYMILLIS);
		}
	}
	
	BaseRequestCallBack callback = new BaseRequestCallBack(MusicListEntity.class) {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			loadingCancle(rotateLoading);
			listview.stopLoadMore();
			listview.stopRefresh();
			if(code!=Constant.SUCCESS_REQUEST){
				return;
			}
			MusicListEntity data = (MusicListEntity) obj;
			List<SongInfoEntity> lists = data.getList_music();
			if(lists!=null&&lists.size()>0){
				list = lists;
				list_size = data.getList_size();
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
			list.add(0,new SongInfoEntity("-1"));
			adapter.setList(list);
			pageNum = 2;
		} else if (pageNum >= 2) {
			oldlist = adapter.getList();
			if (list!=null && oldlist != null && oldlist.size() > 0) {
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
		adapter.setList_size(list_size);
		adapter.notifyDataSetChanged();
		latelyRefreshTime = TimeUtil.getSystemTimer(context);
		if(list.size()>=10){
			listview.setPullLoadEnable(XListView.FOOTER_WAIT);
		}else{
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

	private class ClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.no_network:
				
				hintNoNet();
				task();
				break;
			}
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task != null){
			task.cancelTask();
		}
	}
	
}
