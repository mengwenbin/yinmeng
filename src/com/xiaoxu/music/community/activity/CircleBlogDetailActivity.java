package com.xiaoxu.music.community.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.CircleBlogDetailAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.BlogEntity;
import com.xiaoxu.music.community.entity.BlogReplyEntity;
import com.xiaoxu.music.community.entity.BlogsInfoEntity;
import com.xiaoxu.music.community.entity.UserInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.CircleBlogsTask;
import com.xiaoxu.music.community.model.task.PraisePostTask;
import com.xiaoxu.music.community.model.task.ReplyPostListTask;
import com.xiaoxu.music.community.model.task.ReplyPostTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.LoadingHelper;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

public class CircleBlogDetailActivity extends BaseNewActivity implements OnClickListener, OnXListViewListener{
	
	private XListView listview;
	private String latelyRefreshTime;//最近刷新时间
	private int pageNum = 1;
	private CircleBlogDetailAdapter adapter;
	
	private ReplyPostListTask task;
	private Intent intent;
	private BlogEntity blog;
	private int position;
	private BlogsInfoEntity blog_info;
	private List<BlogsInfoEntity> list,oldlist;
	private UserInfoEntity user_info;
	
	private EditText reply_eidt;
	private ImageButton reply_praise,btn_right;
	
	private final String mPageName = "CircleBlogDetailActivity";
	public String Login_Tag = "com.xiaoxu.music.community.activity.CircleBlogDetailActivity";
	
	private String parent_tid,t_id,user_id,parent_tname;
	
	@Override
	public void setContent() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_circle_blogs_detail);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		intent = getIntent();
		blog_info = (BlogsInfoEntity) intent.getSerializableExtra("BlogsInfoEntity");
		position = intent.getIntExtra("position", 0);
		user_info = AccountInfoUtils.getInstance(context).getUserInfo();//用户信息
		list = new ArrayList<BlogsInfoEntity>();
		list.add(0,blog_info);
		parent_tid = blog_info.getTid();
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
		btn_right = getImageButton(R.id.title_right_btn);
		btn_right.setOnClickListener(this);
		initAnimation(btn_right);
		getTextView(R.id.title_center_content).setText(getString(R.string.blog_detail));
		getImageButton(R.id.title_left_btn).setOnClickListener(this);
		getImageButton(R.id.reply_send).setOnClickListener(this);
		reply_eidt = getEditText(R.id.reply_edit);
		reply_praise = getImageButton(R.id.reply_praise);
		reply_praise.setOnClickListener(this);
		
		reply_praise.setBackgroundResource(R.drawable.btn_praise_normal);
		if (Integer.parseInt(blog_info.getPraise()==null?"0":blog_info.getPraise()) == 1){
			reply_praise.setBackgroundResource(R.drawable.btn_praise_pressed);
		}
		listview = (XListView) findViewById(R.id.listview_circle);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		listview.setXListViewListener(this);
		adapter = new CircleBlogDetailAdapter(this, list, bitmapUtils);
		listview.setAdapter(adapter);
	}
	
	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		LoadingHelper.showDialogForLoading(context, null, true);
		onRefresh();
	}
	
	public void login(){
		Intent i = new Intent(context, StartActivity.class);
		i.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
		startActivityForResult(i, 1);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == 1){
			user_info = AccountInfoUtils.getInstance(context).getUserInfo();//用户信息
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
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
		loadingStart(rotateLoading);
		task(); 
	}
	
	public void task(){
		if(haveNetwork()){
			task = new ReplyPostListTask(context, blog_info.getTid(), pageNum, callback);
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
			if(lists!=null&&lists.size()>0){
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
	
	public void updateUI() {
		if (pageNum == 1) {
			list.add(0, blog_info);
			adapter.setList(list);
			pageNum = 2;
		} else if (pageNum >= 2) {
			oldlist = adapter.getList();
			if (list!=null && oldlist != null && oldlist.size() > 0) {
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
			listview.setPullLoadEnable(XListView.FOOTER_SHOW);
		}else{
			listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		}
	}

	/**
	 * 回复回帖人
	 * @param parent_tid 帖子id
	 * @param parent_tname 发帖人name
	 */
	public void replyComment(String parent_tid,String t_id, String parent_tname,String user_id){
		this.parent_tid = parent_tid;
		this.parent_tname = parent_tname;
		this.user_id = user_id;
		this.t_id = t_id;
		showReplyDialog();
	}
	
	Dialog dialog;
	View menuView;
	public void showReplyDialog(){
		dialog = new Dialog(context, R.style.Custom_Progress);
		if(AccountInfoUtils.getInstance(context).getAccountId().equals(user_id)){
			menuView = LayoutInflater.from(context).inflate(R.layout.dialog_circle_blog_reply2, null);
		}else{
			menuView = LayoutInflater.from(context).inflate(R.layout.dialog_circle_blog_reply, null);
		}
		Button btn_reply = (Button) menuView.findViewById(R.id.btn_reply);
		Button btn_cancle = (Button) menuView.findViewById(R.id.btn_cancel);
		Button btn_delete = (Button) menuView.findViewById(R.id.btn_delete);
		btn_reply.setOnClickListener(l);
		btn_cancle.setOnClickListener(l);
		btn_delete.setOnClickListener(l);
		
		dialog.setContentView(menuView);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);// 按返回键是否取消
		// 设置居中
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = getScreenWidth();
		lp.dimAmount = 0.5f;// 设置背景层透明度
		dialog.show();
	}
	
	OnClickListener l = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_reply:
				dialog.dismiss();
				reply_eidt.setHint("@"+parent_tname+" (按返回键取消@)");
				reply_eidt.setFocusable(true);
				reply_eidt.setFocusableInTouchMode(true);
				InputMethodManager inputManager = (InputMethodManager)reply_eidt.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(reply_eidt, 0);
				break;
			case R.id.btn_cancel: 
				parent_tid = blog_info.getTid();
				reply_eidt.setHint("唠叨两句呗~");
				reply_eidt.setText(null);
				dialog.dismiss();
				break;
			case R.id.btn_delete:
				parent_tid = blog_info.getTid();
				reply_eidt.setHint("唠叨两句呗~");
				reply_eidt.setText(null);
				dialog.dismiss();
				new CircleBlogsTask(context, t_id, new BaseRequestCallBack() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						ActivityUtil.showShortToast(context, "删除失败");
					}
					@Override
					public void onResult(String result, int code, String alert, Object obj) {
						// TODO Auto-generated method stub
						ActivityUtil.showShortToast(context, alert);
						if(code!=Constant.SUCCESS_REQUEST){
							return;
						}
						onRefresh();
					}
				}).excute();
				break;
			default: break;
			}
		}
	};
	
	@SuppressWarnings("deprecation")
	public int getScreenWidth(){
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		return d.getWidth();
	}
	
	public void task_send(String reply,String parent_tid) {
		exitSoft();
		if(!AccountInfoUtils.getInstance(context).isLogin()){
			login();
			return;
		}
		loadingStart(rotateLoading);
		ReplyPostTask task_reply = new ReplyPostTask(context,parent_tid, user_info.getUsername(), reply, null,callback_reply);
		task_reply.excute();
	}
	
	BaseRequestCallBack callback_reply = new BaseRequestCallBack(BlogReplyEntity.class) {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			loadingCancle(rotateLoading);
			if(code!=Constant.SUCCESS_REQUEST){
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			parent_tid = blog_info.getTid();
			reply_eidt.setHint("唠叨两句呗~");
			reply_eidt.setText(null);
			onRefresh();
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
		}
	};
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(ActivityUtil.isFastDoubleClick(v.getId()))
			return;
		switch (v.getId()) {
		case R.id.title_left_btn://back
			if(!parent_tid.equals(blog_info.getTid())){
				parent_tid = blog_info.getTid();
				reply_eidt.setHint("唠叨两句呗~");
				reply_eidt.setText(null);
			}else{
				Intent intent = new Intent(context, CircleBlogsActivity.class);
				intent.putExtra("blog_info", blog_info);
				intent.putExtra("position", position);
				setResult(1, intent);
				finish();
			}
			break;
		case R.id.title_right_btn://playing
			startActivity(new Intent(context, PlayActivity.class));
			break;
		case R.id.reply_praise://赞
			if(!AccountInfoUtils.getInstance(context).isLogin()){
				login();
				return;
			}
			if (Integer.parseInt(blog_info.getPraise()) == 1)// 点过赞了
				return;
			PraisePostTask task = new PraisePostTask(context, blog_info.getTid(), callback_praise);
			task.excute();
			break;
		case R.id.reply_send://回帖
			String reply = reply_eidt.getText().toString();
			if (TextUtils.isEmpty(reply))
				return;
			if(!parent_tid.equals(blog_info.getTid())){
				reply = "回复@"+parent_tname+":"+reply;
			}
			task_send(reply,parent_tid);
			break;
		case R.id.no_network:
			hintNoNet();
			onRefresh();
			break;
		default:
			break;
		}
	}
	
	BaseRequestCallBack callback_praise = new BaseRequestCallBack() {
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			ActivityUtil.showShortToast(context, alert);
			if(code!=Constant.SUCCESS_REQUEST){
				return;
			}
			blog_info.setHeats(String.valueOf(Integer.parseInt(blog_info.getHeats())+1));
			blog_info.setPraise(String.valueOf(1));
			reply_praise.setBackgroundResource(R.drawable.btn_praise_pressed);
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			// TODO Auto-generated method stub
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(!parent_tid.equals(blog_info.getTid())){
				parent_tid = blog_info.getTid();
				reply_eidt.setHint("唠叨两句呗~");
				reply_eidt.setText(null);
			}else{
				Intent intent = new Intent(context, CircleBlogsActivity.class);
				intent.putExtra("blog_info", blog_info);
				intent.putExtra("position", position);
				setResult(1, intent);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}	
