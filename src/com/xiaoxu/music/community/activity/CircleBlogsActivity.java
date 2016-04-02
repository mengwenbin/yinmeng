package com.xiaoxu.music.community.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.CircleBlogsAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.BlogEntity;
import com.xiaoxu.music.community.entity.BlogsInfoEntity;
import com.xiaoxu.music.community.entity.CategorysInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.CircleBlogsTask;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.LoadingHelper;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class CircleBlogsActivity extends BaseNewActivity implements OnClickListener, OnXListViewListener{
	
	private ImageButton btn_back,btn_play,btn_release_post;
	private TextView title;
	private XListView listview;
	private String latelyRefreshTime; //最近刷新时间
	private int pageNum = 1;
	
	private Intent intent;
	private CategorysInfoEntity category;
	
	private CircleBlogsTask task;
	private BlogEntity blog;
	private List<BlogsInfoEntity> list,oldlist;
	
	private CircleBlogsAdapter adapter;
	
	private final String mPageName = "CircleBlogsActivity"; 
	public String Login_Tag = "com.xiaoxu.music.community.activity.CircleBlogsActivity";
	
	@Override
	public void setContent() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_circle_blogs);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		intent = getIntent();
		category = (CategorysInfoEntity) intent.getSerializableExtra("category");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
		loadingCancle(rotateLoading);
		if(task!=null){
			task.cancelTask();
		}
	}
	
	@Override
	public void setupView() {
		// TODO Auto-generated method stub
		rotateLoading = getRotateLoading(R.id.loading);
		btn_back = getImageButton(R.id.title_left_btn);
		btn_play = getImageButton(R.id.title_right_btn);
		initAnimation(btn_play);
		
		btn_release_post = getImageButton(R.id.btn_release_blog);
		title = getTextView(R.id.title_center_content);
		title.setText(category.getCategory_name());
		
		listview = (XListView) findViewById(R.id.listview_circle);
		listview.setFooterReady(true);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		listview.setXListViewListener(this);
		btn_back.setOnClickListener(this);
		btn_play.setOnClickListener(this);
		btn_release_post.setOnClickListener(this);
		
		adapter = new CircleBlogsAdapter(this, category);
		listview.setAdapter(adapter);
	}
	
	@Override
	public void setModel() {
		LoadingHelper.showDialogForLoading(context, null, true);
		task();
	}
	
	public void login(){
		Intent i = new Intent(context, StartActivity.class);
		i.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
		startActivity(i);
	}
	
	public void task(){
		if(haveNetwork()){
			task = new CircleBlogsTask(context,category.getCategory_id(), pageNum, callback);
			task.excute();
		}else{
			LoadingHelper.hideDialogForLoading();
			if(adapter != null){
				adapter.setList(null);
				adapter.notifyDataSetChanged();
				pageNum = 1;
				showNoNet(this);
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
	
	BaseRequestCallBack callback = new BaseRequestCallBack(BlogEntity.class) {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			LoadingHelper.hideDialogForLoading();
			listview.stopLoadMore();
			listview.stopRefresh();
			if(code!=Constant.SUCCESS_REQUEST){
				return;
			}
			blog = (BlogEntity) obj;
			List<BlogsInfoEntity> lists = blog.getList_blogThread();
			if(null != lists && lists.size()>0){
				list = lists;
				updateUI();
			}else{
				listview.setPullLoadEnable(XListView.FOOTER_HIDE);
			}
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			// TODO Auto-generated method stub
			LoadingHelper.hideDialogForLoading();
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
		task();
	}
	
	public void updateUI() {
		if (pageNum == 1) {
			if(blog!=null){
				list.addAll(0, blog.getList_blogThread_top());
				adapter.setTop_count(blog.getList_blogThread_top()==null?0:blog.getList_blogThread_top().size());
			}
			list.add(0, new BlogsInfoEntity("-1"));
			adapter.setList(list);
			pageNum = 2;
		} else if (pageNum >= 2) {
			oldlist = adapter.getList();
			if (oldlist != null && oldlist.size() > 0) {
				for (int i = 0; i < list.size(); i++) { // 新数据
					for (int j = oldlist.size() - 1; j >= 0; j--) { // 旧数据
						long old_id = Long.parseLong(oldlist.get(j).getTid());
						long new_id = Long.parseLong(list.get(i).getTid());
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
		switch (v.getId()) {
		case R.id.title_left_btn:
			finish();
			break;
		case R.id.btn_release_blog:
			Intent in = new Intent(context, ReleaseBlogActivity.class);
			in.putExtra("category_id", category.getCategory_id());
			startActivityForResult(in, 0x2);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 0x1 && data != null){//detail
			int position = data.getIntExtra("position", -1);
			BlogsInfoEntity blog_info = (BlogsInfoEntity) data.getSerializableExtra("blog_info");
			List<BlogsInfoEntity> list = adapter.getList();
			if(position!=-1&&blog_info!=null&&list!=null){
				list.set(position, blog_info);
				adapter.setList(list);
				adapter.notifyDataSetChanged();
			}
		}else if(resultCode == 0x2){//release blog
			onRefresh();
		}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
