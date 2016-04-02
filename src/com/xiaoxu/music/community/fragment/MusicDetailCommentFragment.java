package com.xiaoxu.music.community.fragment;

import java.util.List;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewFragment;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.MusicDetailActivity;
import com.xiaoxu.music.community.adapter.MusicDetailCommentAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.MusicCommentEntity;
import com.xiaoxu.music.community.entity.MusicCommentInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.CommentMusicListTask;
import com.xiaoxu.music.community.model.task.CommentMusicTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class MusicDetailCommentFragment extends BaseNewFragment  implements OnXListViewListener, OnClickListener{

	private String music_id;
	private XListView xlist;
	private MusicDetailCommentAdapter adapter;
	
	private int pageNum = 1;
	private List<MusicCommentInfoEntity> list,oldlist;
	private String latelyRefreshTime;//最近刷新时间
	
	//replay_comment
	private LinearLayout view_comment;
	private EditText reply_edit;
	
	private MusicDetailActivity act;
	private final  String mPageName = "MusicDetailCommentFragment";
	
	public MusicDetailCommentFragment() {
		super();
	}
	
	public MusicDetailCommentFragment(String music_id) {
		super();
		this.music_id = music_id;
	}

	@Override
	public int setLayoutId() {
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_music_detail_comment;
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
		rotateLoading = getRotateLoading(view, R.id.loading);
		no_network = getImageButton(view, R.id.no_network);
		
		reply_edit = getEditText(view,R.id.reply_edit);
		getImageButton(view,R.id.reply_send).setOnClickListener(this);
		
		xlist = (XListView) view.findViewById(R.id.listview_queue);
		xlist.setPullLoadEnable(XListView.FOOTER_HIDE);
		xlist.setXListViewListener(this);
		adapter = new MusicDetailCommentAdapter(context);
		xlist.setAdapter(adapter);
	}
	
	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		pageNum = 1;
		loadingStart(rotateLoading);
		task();
	}
	
	public void task(){
		if(haveNetwork(context)){
			CommentMusicListTask task = new CommentMusicListTask(context, music_id, pageNum, callback_list);
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
                	xlist.stopLoadMore();
        			xlist.stopRefresh();
        			xlist.setPullLoadEnable(XListView.FOOTER_HIDE);
                }
            }, Constant.DELAYMILLIS);
		}
	}
	
	BaseRequestCallBack callback_list = new BaseRequestCallBack(MusicCommentEntity.class) {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			loadingCancle(rotateLoading);
			xlist.stopLoadMore();
			xlist.stopRefresh();
			if(code!=Constant.SUCCESS_REQUEST){
				return;
			}
			MusicCommentEntity entity = (MusicCommentEntity) obj;
			List<MusicCommentInfoEntity> lists = entity.getList_comment();
			if(lists!=null && lists.size()>0){
				list = lists;
				updateCommentUI();
			}else{
				xlist.setPullLoadEnable(XListView.FOOTER_HIDE);
			}
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			// TODO Auto-generated method stub
			loadingCancle(rotateLoading);
			xlist.stopLoadMore();
			xlist.stopRefresh();
		}
	};
	
	public void updateCommentUI() {
		if (pageNum == 1) {
			adapter.setList(list);
			pageNum = 2;
		} else if (pageNum >= 2) {
			oldlist = adapter.getList();
			if (list!=null && oldlist != null && oldlist.size() > 0) {
				for (int i = 0; i < list.size(); i++) { // 新数据
					for (int j = oldlist.size() - 1; j >= 0; j--) { // 旧数据
						long old_id = Long.parseLong(oldlist.get(j).getComment_id());
						long new_id = Long.parseLong(list.get(i).getComment_id());
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
			xlist.setPullLoadEnable(XListView.FOOTER_SHOW);
		}else{
			xlist.setPullLoadEnable(XListView.FOOTER_HIDE);
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xlist.setPullLoadEnable(XListView.FOOTER_HIDE);
	    xlist.setRefreshTime(latelyRefreshTime);
		pageNum = 1;
		task();
	}
	
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		task();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.no_network:
			hintNoNet();
			task();
			break;
		case R.id.reply_send:
			exitSoft();
			String comment = reply_edit.getText().toString();
			reply_edit.setText("");
			if (!AccountInfoUtils.getInstance(context).isLogin()) {
				act = (MusicDetailActivity) activity;
				act.login();
				return;
			}
			if(!TextUtils.isEmpty(comment)){
				CommentMusicTask task = new CommentMusicTask(context, music_id, comment, new BaseRequestCallBack() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						loadingCancle(rotateLoading);
					}
					@Override
					public void onResult(String result, int code, String alert, Object obj) {
						// TODO Auto-generated method stub
						loadingCancle(rotateLoading);
						if(code!=Constant.SUCCESS_REQUEST)
							return;
						onRefresh();
					}
				});
				task.excute();
				loadingStart(rotateLoading);
			}
		default:
			break;
		}
		
	}
	
}
