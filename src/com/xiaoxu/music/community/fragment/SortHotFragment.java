package com.xiaoxu.music.community.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewFragment;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.MusicDetailActivity;
import com.xiaoxu.music.community.adapter.SortListAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.MusicListEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.SortListTask;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class SortHotFragment extends BaseNewFragment implements
			OnXListViewListener, OnClickListener, OnItemClickListener{
	
	private XListView listview;
	private String latelyRefreshTime; //最近刷新时间
	private int pageNum = 1;
	private SortListAdapter adapter;
	private int music_category = 51;
	private List<SongInfoEntity> list,oldlist;
	
	private final  String mPageName = "SortHotFragment";
	
	public int getMusic_category() {
		return music_category;
	}
	
	public void setMusic_category(int music_category) {
		this.music_category = music_category;
	}
	
	@Override
	public int setLayoutId() {
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_sort_hot;
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
		// TODO Auto-generated method stub
		no_network = getImageButton(view, R.id.no_network);
		rotateLoading = getRotateLoading(view, R.id.loading);
		listview = (XListView) view.findViewById(R.id.listview_sort_hot);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		listview.setFooterReady(true);
		listview.setXListViewListener(this);
		listview.setOnItemClickListener(this);
		adapter = new SortListAdapter(context, list);
		listview.setAdapter(adapter);
	}
	
	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		loadingStart(rotateLoading);
		onRefresh();
	}
	
	public void loading(){
		if(listview!=null){
			listview.refresh(this);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.no_network:
			if (haveNetwork(context)) {
				hintNoNet();
				loading();
			}
			break;
		default:
			break;
		}
	}
	
	public void task(){
		if (haveNetwork(context)) {
			SortListTask task = new SortListTask(context, String.valueOf(music_category), callback);
			task.excute();
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
			// TODO Auto-generated method stub
			loadingCancle(rotateLoading);
			listview.stopLoadMore();
			listview.stopRefresh();
			if(code != Constant.SUCCESS_REQUEST) {
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			MusicListEntity entity = (MusicListEntity) obj;
			list = entity.getList_music();
			updateUI();
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
		adapter.setList(list);
		adapter.notifyDataSetChanged();
		latelyRefreshTime = TimeUtil.getSystemTimer(context);
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
		// TODO Auto-generated method stub
		task();
	}
	
	public void changeData(int position,SongInfoEntity info){
		if(info!=null&&position!=-1){
			List<SongInfoEntity> list = adapter.getList();
			if(list!=null&&list.size()>0){
				list.set(position, info);
				adapter.setList(list);
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		oldlist = adapter.getList();
		Intent in = new Intent(context, MusicDetailActivity.class);
		in.putExtra("SongInfoEntity", oldlist.get(position - 1));
		in.putExtra("position", position - 1);
		getParentFragment().getParentFragment().startActivityForResult(in, 11);
	}
}
