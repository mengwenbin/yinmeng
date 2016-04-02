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
import com.xiaoxu.music.community.activity.MusicUserDetailActivity;
import com.xiaoxu.music.community.adapter.AttentionUserAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.AttentionUserEntity;
import com.xiaoxu.music.community.entity.MusicUserInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.AttentionUserListTask;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class MusicUserAttentionFragment extends BaseNewFragment implements OnXListViewListener,OnClickListener{

	private MusicUserDetailActivity act;
	
	private XListView listview;
	private AttentionUserListTask task;
	private String user_id;
	private int pageNum = 1;
	private String latelyRefreshTime; //最近刷新时间
	private AttentionUserAdapter adapter;
	
	private List<MusicUserInfoEntity> list,oldlist;
	private final  String mPageName = "MusicUserAttentionFragment";
	
	@Override
	public int setLayoutId() {
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_music_user_attention;
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
		listview = (XListView) view.findViewById(R.id.listview_attention);
		listview.setXListViewListener(this);
		listview.setFooterReady(true);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		adapter = new AttentionUserAdapter(context);
		listview.setAdapter(adapter);
	}

	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		act = (MusicUserDetailActivity) activity;
		user_id = act.getUser_id();
		loadingStart(rotateLoading);
		task();
	}
	
	public void loading(){
		pageNum = 1;
		task();
	}
	
	public void task(){
		if(context==null){
			return;
		}
		if(haveNetwork(context)){
			task = new AttentionUserListTask(context, user_id, pageNum, callback);
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
	
	BaseRequestCallBack callback = new BaseRequestCallBack(AttentionUserEntity.class) {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			loadingCancle(rotateLoading);
			listview.stopLoadMore();
			listview.stopRefresh();
			if(code!=Constant.SUCCESS_REQUEST){
				return;
			}
			if(obj==null)
				return;
			AttentionUserEntity entity = (AttentionUserEntity) obj;
			List<MusicUserInfoEntity> lists = entity.getList_attention();
			if(null!=lists&&lists.size()>0){
				list = lists;
				updateUI();
			}else {
				listview.setPullLoadEnable(XListView.FOOTER_HIDE);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		hintNoNet();
		task();
	}

}