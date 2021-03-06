package com.xiaoxu.music.community.activity;

import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.ExitAppliation;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.fragment.CircleFragment;
import com.xiaoxu.music.community.fragment.HomePageFragment;
import com.xiaoxu.music.community.fragment.MineFragment;
import com.xiaoxu.music.community.fragment.MusicUserFragment;
import com.xiaoxu.music.community.im.applib.controller.HXSDKHelper;
import com.xiaoxu.music.community.im.applib.controller.YMHXSDKHelper;
import com.xiaoxu.music.community.service.MediaBinder;
import com.xiaoxu.music.community.service.MediaBinder.OnPlayStatusListener;
import com.xiaoxu.music.community.service.MediaPlayerService;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.view.CustomViewPager;

public class MainTabActivity extends BaseNewActivity implements EMEventListener {
	
	private static boolean isExit = false;
	
	private CustomViewPager viewpager;
	private ViewPagerAdapter adapter;
	private Button btn_home, btn_music_man, btn_upload, btn_circle, btn_mine;
	
	private HomePageFragment fragment_homepage;
	private MusicUserFragment fragment_music_man;
	private CircleFragment fragment_circle;
	private MineFragment fragment_mine;
	private Intent in;
	private MediaBinder binder;
	
	//消息未读数量
	private int count;
	// 账号在别处登录
	public static boolean isConflict = false;
	// 未读消息textview
	private TextView unreadLabel; 
	private Intent actionintent;
	private boolean isConflictDialogShow;
	private AlertDialog.Builder conflictBuilder;
	private MyConnectionListener connectionListener = null;
	private String Login_Tag = "com.xiaoxu.music.community.activity.MainTabActivity";
	
	@Override
	public void setContent() {
		
		setContentView(R.layout.activity_main_tab);
		in = new Intent(getApplicationContext(), MediaPlayerService.class);
		startService(in);
		if (getIntent().getBooleanExtra("conflict", false)&& !isConflictDialogShow) {
			showConflictDialog();
		} 
		actionintent = new Intent(Constant.REFRESH_MESSAGE);
		
	}
	
	@Override
	public void setupView() {
		
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		viewpager = (CustomViewPager) getViewPager(R.id.viewpager_tab);
		btn_home = getButton(R.id.tab_btn_homepage);
		btn_music_man = getButton(R.id.tab_btn_musicman);
		btn_upload = getButton(R.id.tab_btn_upload);
		btn_circle = getButton(R.id.tab_btn_circle);
		btn_mine = getButton(R.id.tab_btn_mine);
		
		// 注册一个监听连接状态的listener
		connectionListener = new MyConnectionListener();
		EMChatManager.getInstance().addConnectionListener(connectionListener);
	}

	@Override
	public void setModel() {
		
		fragment_homepage = new HomePageFragment();
		fragment_music_man = new MusicUserFragment();
		fragment_circle = new CircleFragment();
		fragment_mine = new MineFragment();
		
		adapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(listener);
		viewpager.setOffscreenPageLimit(4);
	}
	
	OnPlayStatusListener onPlayStatusListener = new OnPlayStatusListener() {
		
		@Override
		public void onPlay() {
			// TODO Auto-generated method stub
			fragment_homepage.on_off_anim();
			fragment_music_man.on_off_anim();
			fragment_circle.on_off_anim();
			fragment_mine.on_off_anim();
		}
		
		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			fragment_homepage.on_off_anim();
			fragment_music_man.on_off_anim();
			fragment_circle.on_off_anim();
			fragment_mine.on_off_anim();
		}
	};
	
	
	public void initTabButton(){
		// Init Tab-Button
		btn_home.setTextColor(getResources().getColor(R.color.tab_text_normal));
		btn_music_man.setTextColor(getResources().getColor(R.color.tab_text_normal));
		btn_upload.setTextColor(getResources().getColor(R.color.tab_text_normal));
		btn_circle.setTextColor(getResources().getColor(R.color.tab_text_normal));
		btn_mine.setTextColor(getResources().getColor(R.color.tab_text_normal));
		btn_home.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_homepage_normal, 0, 0);
		btn_music_man.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_musicman_normal, 0, 0);
		btn_circle.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_circle_normal, 0, 0);
		btn_mine.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_mine_normal, 0, 0);
	}
	
	public void onTabClick(View v){
		// Tab-Button onClick  this.getResources().getColor(R.color.red)
		switch (v.getId()) {
		case R.id.tab_btn_homepage:
			initTabButton();
			btn_home.setTextColor(getResources().getColor(R.color.tab_text_select));
			btn_home.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_homepage_pressed, 0, 0);
			viewpager.setCurrentItem(0);
			break;
		case R.id.tab_btn_musicman:
			initTabButton();
			btn_music_man.setTextColor(getResources().getColor(R.color.tab_text_select));
			btn_music_man.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_musicman_pressed, 0, 0);
			viewpager.setCurrentItem(1);
			break;
		case R.id.tab_btn_upload:
			initTabButton();
			btn_upload.setTextColor(getResources().getColor(R.color.tab_text_select));
			startActivity(new Intent(context, UploadMusicActivity.class));
			break;
		case R.id.tab_btn_circle:
			initTabButton();
			btn_circle.setTextColor(getResources().getColor(R.color.tab_text_select));
			btn_circle.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_circle_pressed, 0, 0);
			viewpager.setCurrentItem(2);
			fragment_circle.loading();
			break;
		case R.id.tab_btn_mine:
			initTabButton();
			btn_mine.setTextColor(getResources().getColor(R.color.tab_text_select));
			btn_mine.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_mine_pressed, 0, 0);
			viewpager.setCurrentItem(3);
			break;
		default:
			break;
		}
	}
	
	public class ViewPagerAdapter extends FragmentStatePagerAdapter {

		private Fragment fragment;

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				fragment = fragment_homepage;
				break;
			case 1:
				fragment = fragment_music_man;
				break;
			case 2:
				fragment = fragment_circle;
				break;
			case 3:
				fragment = fragment_mine;
				break;
			default:
				break;
			}
			return fragment;
		}
		
		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}
	}
	
	OnPageChangeListener listener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				initTabButton();
				btn_home.setTextColor(getResources().getColor(R.color.tab_text_select));
				btn_home.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_homepage_pressed, 0, 0);
				break;
			case 1:
				initTabButton();
				btn_music_man.setTextColor(getResources().getColor(R.color.tab_text_select));
				btn_music_man.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_musicman_pressed, 0, 0);
				break;
			case 2:
				initTabButton();
				btn_circle.setTextColor(getResources().getColor(R.color.tab_text_select));
				btn_circle.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_circle_pressed, 0, 0);
				break;
			case 3:
				initTabButton();
				btn_mine.setTextColor(getResources().getColor(R.color.tab_text_select));
				btn_mine.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.tab_mine_pressed, 0, 0);
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	
	@Override
	protected void onDestroy() {
		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}
		sendBroadcast(new Intent(MediaPlayerService.SAVE_HISTORY));
		super.onDestroy();
	}
	
	/**
	 * 获取未读消息数
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		//未读消息数量
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		for (EMConversation conversation : EMChatManager.getInstance()
				.getAllConversations().values()) {
			if (conversation.getType() == EMConversationType.ChatRoom)
				chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal - chatroomUnreadMsgCount;
	}
	
	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		YmApplication.getInstance().logout(null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!MainTabActivity.this.isFinishing()) {
			try {
				if (conflictBuilder == null)
				conflictBuilder = new android.app.AlertDialog.Builder(context);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								conflictBuilder = null;
								AccountInfoUtils.getInstance(context).setAccountInfo("", "0", "", false);
								Intent in = new Intent(Constant.ACTION_MINE_UPDATE_USERINFO);
								in.putExtra("isLogin", true);
								context.sendBroadcast(in);
								Intent intent = new Intent(context, StartActivity.class);
								intent.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
								startActivity(intent);
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
			}
		}
	}
	
	
	
	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		binder = app.getBinder();
		binder.setOnPlayStatusListener(onPlayStatusListener);
		if (!isConflict) {
			updateUnreadLabel();
			EMChatManager.getInstance().activityResumed();
		}
		YMHXSDKHelper sdkHelper = (YMHXSDKHelper) YMHXSDKHelper.getInstance();
		sdkHelper.pushActivity(this);
		// 当进入前台注册事件侦听器
		EMChatManager.getInstance().registerEventListener(this,
		new EMNotifierEvent.Event[] { 
		EMNotifierEvent.Event.EventNewMessage,
		EMNotifierEvent.Event.EventOfflineMessage,
		EMNotifierEvent.Event.EventConversationListChanged});

	}
	
	@Override
	protected void onStop() {
		
		// 当这个活动进入注销这个事件侦听器后台
		EMChatManager.getInstance().unregisterEventListener(this);
		YMHXSDKHelper sdkHelper = (YMHXSDKHelper) YMHXSDKHelper.getInstance();
		sdkHelper.popActivity(this);
		super.onStop();
	}

	/**
	 * 连接监听listener
	 * 
	 */
	public class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {

			// 通知sdk我们准备接收的事件
			new Thread() {
				@Override
				public void run() {
					HXSDKHelper.getInstance().notifyForRecevingEvents();
				}
			}.start();
			actionintent.putExtra("isShow", false);
			context.sendBroadcast(actionintent);
		}

		@Override
		public void onDisconnected(final int error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆dialog
						showConflictDialog();
					}else{
						actionintent.putExtra("isShow", true);
						context.sendBroadcast(actionintent);
					}
				}
			});
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		
			// 新消息通知事件
			case EventNewMessage: {
				EMMessage message = (EMMessage) event.getData();
				// 提示新消息
				HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
				YmApplication.getInstance().SaveStrangerInfo(message);
				refreshUI();
			}
			break;
			case EventOfflineMessage:{
				YmApplication.getInstance().SaveStranger((List<EMMessage>) event.getData());
				refreshUI();
			}
				break;
			case EventConversationListChanged:{
				refreshUI();
			}
			break;
		}
	}
	
	private void refreshUI() {
		
		runOnUiThread(new Runnable() {
			public void run() {
				// 刷新消息未读数
				updateUnreadLabel();
				if(fragment_mine!=null){
					fragment_mine.updateUnreadLabel();
				}
			}
		});
	}
	
	// 退出应用
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	private void exit() {
		if (!isExit) {
			isExit = true;
			ActivityUtil.showShortToast(context, getString(R.string.exit_app));
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			ExitAppliation.getInstance().exit();
		}
	}
	
	
}
