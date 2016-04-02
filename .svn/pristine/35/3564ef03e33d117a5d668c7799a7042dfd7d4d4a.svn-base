package com.xiaoxu.music.community.im.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.PlayActivity;
import com.xiaoxu.music.community.activity.StartActivity;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.UserInfo;
import com.xiaoxu.music.community.im.adapter.ChatAllHistoryAdapter;
import com.xiaoxu.music.community.im.applib.db.UserDao;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 */

public class ChatAllHistoryActivity extends BaseNewActivity implements OnClickListener, OnItemClickListener, OnXListViewListener{

	private UserDao dao;
	private List<UserInfo> list;
	private TextView center_title;
	private ImageButton left_btn;
	private ImageButton right_btn;
	private TextView errorText;
	private XListView listView;
	private RelativeLayout errorItem;
	private ChatAllHistoryAdapter adapter;
	private InputMethodManager inputMethodManager;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();
	
	private String Login_Tag = "com.xiaoxu.music.community.im.activity.ChatAllHistoryActivity";

	private IntentFilter filter;
	private MyBroadcastReceiver receiver;
	@Override
	public void setContent() {
		setContentView(R.layout.fragment_conversation_history);
		inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		receiver = new MyBroadcastReceiver();
		filter = new IntentFilter(Constant.REFRESH_MESSAGE);
	}
	
	void hideSoftKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	@Override
	public void setupView() {
		
		dao = new UserDao(context);
		list = dao.getStrangerList();
		rotateLoading = getRotateLoading(R.id.loading);
		center_title = getTextView(R.id.title_center_content);
		center_title.setText("我的消息");
		left_btn = getImageButton(R.id.title_left_btn);
		right_btn = getImageButton(R.id.title_right_btn);
		initAnimation(right_btn);
		errorItem = (RelativeLayout) findViewById(R.id.rl_error_item);
		errorText = (TextView) findViewById(R.id.tv_connect_errormsg);
		listView = (XListView) findViewById(R.id.listview);
		listView.setFooterReady(true);
		listView.setPullLoadEnable(XListView.FOOTER_HIDE);
		
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				hideSoftKeyboard();
				return false;
			}

		});
		
		initListener();
	}

	private void initListener() {
		
		right_btn.setOnClickListener(this);
		left_btn.setOnClickListener(this);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
		// 注册上下文菜单
		registerForContextMenu(listView);
		
	}

	@Override
	public void setModel() {
		
		if(!AccountInfoUtils.getInstance(context).isLogin()){
			Intent intent = new Intent(context, StartActivity.class);
			intent.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
			startActivityForResult(intent, 10);
		}
		conversationList.addAll(loadConversationsWithRecentChat());
		loadingCancle(rotateLoading);
		adapter = new ChatAllHistoryAdapter(context,list,conversationList);
		// 设置adapter
		listView.setAdapter(adapter);
	}

	/**
	 * 获取所有会话
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation
							.getLastMessage().getMsgTime(), conversation));
				}
			}
		}
		try {
			// 内部是TimSort算法,错误
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		listView.stopRefresh();
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			
			List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList,
				new Comparator<Pair<Long, EMConversation>>() {
					@Override
					public int compare(final Pair<Long, EMConversation> con1,
							final Pair<Long, EMConversation> con2) {

						if (con1.first == con2.first) {
							return 0;
						} else if (con2.first > con1.first) {
							return 1;
						} else {
							return -1;
						}
					}

				});
		
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(receiver, filter);
	}

	
	/**
	 * 刷新页面
	 */
	public void refresh() {
		
		conversationList.clear();
		list = dao.getStrangerList();
		conversationList.addAll(loadConversationsWithRecentChat());
		if(conversationList.size() > 0){
			if (adapter != null){
				adapter.setList(list);
				adapter.setConversationList(conversationList);
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(receiver != null){
			unregisterReceiver(receiver);
		}
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
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 10){
			if(!AccountInfoUtils.getInstance(context).isLogin()){
				finish();
			}
			refresh();
		}
		if(requestCode == 1){
			refresh();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		 // 进入聊天页面
		 Intent intent = new Intent(context, ChatActivity.class);
		 UserInfo user = adapter.getList().get(position-1);
		 intent.putExtra("userNick", user.getUsernick());
		 intent.putExtra("userName", user.getUsername());
		 intent.putExtra("userimg", user.getAvatar());
		 startActivityForResult(intent, 1);
	}
	
	private class MyBroadcastReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null){
				
				if(intent.getBooleanExtra("isShow", false)){
					errorItem.setVisibility(View.VISIBLE);
					if(ActivityUtil.isNetworkAvailable(context)){
						errorText.setText(getString(R.string.can_not_connect_chat_server_connection));
					}else{
						errorText.setText(getString(R.string.the_current_network));
					}
				}else{
					errorItem.setVisibility(View.GONE);
				}
				if(intent.getBooleanExtra("refresh", false)){
					refresh();
				}
			}
		}
	}

	@Override
	public void onRefresh() {
		loadingStart(rotateLoading);
		refresh();
		loadingCancle(rotateLoading);
	}

	@Override
	public void onLoadMore() {
		
	};
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.delete_message, menu); 
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		boolean handled = false;
		boolean deleteMessage = false;
			if (item.getItemId() == R.id.delete_conversation) {
			deleteMessage = false;
			handled = true;
		}
		int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position - 1;
		adapter.getList().remove(position);
		UserDao dao = new UserDao(context);
		EMConversation tobeDeleteCons = adapter.getConversationList().get(position);
		dao.delStrangerUser(tobeDeleteCons.getUserName());
		adapter.getConversationList().remove(position);
		// 删除此会话
		EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), deleteMessage);
		adapter.notifyDataSetChanged();
		// 更新消息未读数
		return handled ? true : super.onContextItemSelected(item);
	}

}