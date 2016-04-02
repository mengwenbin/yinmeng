package com.xiaoxu.music.community.activity;

import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
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
import com.xiaoxu.music.community.adapter.MineAttentionAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.AttentionListEntity;
import com.xiaoxu.music.community.entity.MusicUserInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.MineAttentionTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.RotateLoading;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

/*
 * 我的关注模块
 */
public class MineAttentionActivity extends BaseNewActivity implements 
OnXListViewListener, OnClickListener,OnItemClickListener{

	private TextView title_center;
	private ImageButton left_btn;
	private ImageButton right_btn;
	private XListView List_View;
	private String latelyRefreshTime;// 最近刷新时间

	private int pageNum = 1;
	private MineAttentionTask task;
	private MineAttentionAdapter adapter;
	private List<MusicUserInfoEntity> list, oldlist;
	
	private final String mPageName = "MineAttentionActivity";
	private String Login_Tag = "com.xiaoxu.music.community.activity.MineAttentionActivity";

	@Override
	public void setupView() {
		
		left_btn = (ImageButton) findViewById(R.id.title_left_btn);
		right_btn = (ImageButton) findViewById(R.id.title_right_btn);
		initAnimation(right_btn);
		
		List_View = (XListView) findViewById(R.id.list_view);
		title_center = (TextView) findViewById(R.id.title_center_content);
		rotateLoading = (RotateLoading) findViewById(R.id.loading);
		List_View.setFooterReady(true);
		List_View.setPullLoadEnable(XListView.FOOTER_HIDE);
		List_View.setXListViewListener(this);
		initListener();

		adapter = new MineAttentionAdapter(context,list);
		List_View.setAdapter(adapter);

	}

	/*
	 * 初始化监听
	 */
	private void initListener() {

		left_btn.setOnClickListener(this);
		right_btn.setOnClickListener(this);
		List_View.setOnItemClickListener(this);
	}

	@Override
	public void setContent() {
		setContentView(R.layout.activity_mineattention);
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
	public void setModel() {
		
		if(!AccountInfoUtils.getInstance(context).isLogin()){
			Intent intent = new Intent(context, StartActivity.class);
			intent.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
			startActivityForResult(intent, 1);
		}
		title_center.setText("我的关注");
		task();
	}

	/*
	 * 请求数据task
	 */
	public void task() {
		if (haveNetwork()) {
			loadingStart(rotateLoading);
			task = new MineAttentionTask(context, pageNum,callback);
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
        			List_View.stopLoadMore();
        			List_View.stopRefresh();
        			List_View.setPullLoadEnable(XListView.FOOTER_HIDE);
                }
            }, Constant.DELAYMILLIS);
		}
	}

	BaseRequestCallBack callback = new BaseRequestCallBack(AttentionListEntity.class) {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			
			loadingCancle(rotateLoading);
			List_View.stopLoadMore();
			List_View.stopRefresh();
			if (code != Constant.SUCCESS_REQUEST) {
				return;
			}
			AttentionListEntity attentionEntity = (AttentionListEntity) obj;
			List<MusicUserInfoEntity> lists = attentionEntity.getList_attention();
			if (null != lists && lists.size() > 0) {
				list = lists;
				updateUI();
			}else {
				List_View.setPullLoadEnable(XListView.FOOTER_HIDE);
			}
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			loadingCancle(rotateLoading);
			List_View.stopLoadMore();
			List_View.stopRefresh();
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
		if (list.size() >= 10) {
			List_View.setPullLoadEnable(XListView.FOOTER_WAIT);
		} else {
			List_View.setPullLoadEnable(XListView.FOOTER_HIDE);
		}
	}

	@Override
	public void onRefresh() {
		List_View.setPullLoadEnable(XListView.FOOTER_HIDE);
		List_View.setRefreshTime(latelyRefreshTime);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		oldlist = adapter.getList();
		Intent intent = new Intent(context, MusicUserDetailActivity.class);
		intent.putExtra("user_id", oldlist.get(position-1).getUser_id());
		intent.putExtra("position", position-1);
		startActivityForResult(intent, 2);
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(task != null){
			task.cancelTask();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1){
			if(!AccountInfoUtils.getInstance(context).isLogin()){
				finish();
			}
			pageNum = 1;
			task();
		}
		if(requestCode == 2){
			if(data == null ){
				return;
			}
			int position = data.getIntExtra("position", 0);
			MusicUserInfoEntity infoEntity = (MusicUserInfoEntity) data.getSerializableExtra("MusicUserInfoEntity");
			if(infoEntity!=null&&infoEntity.getIs_attention().equals("0")&&adapter!=null){
				List<MusicUserInfoEntity> list = adapter.getList();
				if(list!=null&&list.size()>0){
					list.remove(position);
					adapter.notifyDataSetChanged();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
