package com.xiaoxu.music.community.im.activity;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.activity.PlayActivity;
import com.xiaoxu.music.community.entity.UserInfo;
import com.xiaoxu.music.community.im.adapter.ExpressionAdapter;
import com.xiaoxu.music.community.im.adapter.ExpressionPagerAdapter;
import com.xiaoxu.music.community.im.adapter.MessageAdapter;
import com.xiaoxu.music.community.im.adapter.VoicePlayClickListener;
import com.xiaoxu.music.community.im.applib.controller.YMHXSDKHelper;
import com.xiaoxu.music.community.im.applib.controller.HXSDKHelper;
import com.xiaoxu.music.community.im.applib.utils.ImageUtils;
import com.xiaoxu.music.community.im.applib.utils.SmileUtils;
import com.xiaoxu.music.community.im.applib.utils.UserUtils;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.view.ExpandGridView;
import com.xiaoxu.music.community.view.PasteEditText;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 聊天页面
 */
public class ChatActivity extends BaseNewActivity implements OnClickListener, EMEventListener,OnRefreshListener{
	
	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	public static final int REQUEST_CODE_TEXT = 4;
	public static final int REQUEST_CODE_VOICE = 5;
	public static final int REQUEST_CODE_PICTURE = 6;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 7;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 8;
	public static final int REQUEST_CODE_CAMERA = 9;
	public static final int REQUEST_CODE_LOCAL = 10;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 11;
	public static final int REQUEST_CODE_FILE = 12;
	public static final int REQUEST_CODE_SELECT_FILE = 13;
	private static final int REQUEST_CODE_MAP = 14;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int CHATTYPE_SINGLE = 1;

	public static final String COPY_IMAGE = "EASEMOBIMG";
	
	private ImageView micImage;
	private TextView recordingHint;
	
	private View more;
	private View buttonSend;
	private View recordingContainer;
	private View buttonPressToSpeak;
	private View buttonSetModeVoice;
	private View buttonSetModeKeyboard;
	
	private File cameraFile;
	private VoiceRecorder voiceRecorder;
	
	private List<String> reslist;
	private Drawable[] micImages;
	
	private final int pagesize = 20;
	private ViewPager expressionViewpager;
	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;
	private LinearLayout btnContainer;
	private PasteEditText mEditTextContent;
	private RelativeLayout edittext_layout;
	private LinearLayout emojiIconContainer;
	
	private Button btnMore;
	public String playMsgId;
	private boolean isloading;
	private ProgressBar loadmorePB;
	private boolean haveMoreData = true;

	private ListView listView;
	private MessageAdapter adapter;
	private SwipeRefreshLayout swipeRefreshLayout;

	private int chatType;
	static int resendPos;
	// 给谁发消息
	private String toChatUsername;
	private String toChatUsernick;
	private String toChatUserimg;
	private EMConversation conversation;
	private ClipboardManager clipboard;
	private InputMethodManager manager;
	public static ChatActivity activityInstance = null;
	
	private Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// 切换msg切换图片
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};
	
	@Override
	public void setContent() {
		
		setContentView(R.layout.activity_chat);
		activityInstance = this;
		Intent intent = getIntent();
		if(intent != null){
			chatType = intent.getIntExtra("chatType", CHATTYPE_SINGLE);
			if (chatType == CHATTYPE_SINGLE) { // 单聊
				toChatUsernick = intent.getStringExtra("userNick");
				toChatUsername = intent.getStringExtra("userName");
				toChatUserimg = intent.getStringExtra("userimg");
//				if(toChatUsernick!= null && !toChatUsernick.equals("")){
					((TextView) findViewById(R.id.name)).setText(toChatUsernick);
//				}else{
//					((TextView) findViewById(R.id.name)).setText(toChatUsername);
//				}
			} 
		}
		
		initSpeakDrawable();
	}
	
	private void initSpeakDrawable() {
		
		// 动画资源文件,用于录制语音
		micImages = new Drawable[] { getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10),
				getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12),
				getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14)};
		
	}

	@Override
	public void setupView() {
		
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
		recordingContainer = findViewById(R.id.recording_container);
		micImage = (ImageView) findViewById(R.id.mic_image);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		listView = (ListView) findViewById(R.id.list);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
		buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
		buttonSend = findViewById(R.id.btn_send);
		buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
		btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
		btnMore = (Button) findViewById(R.id.btn_more);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		more = findViewById(R.id.more);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
		swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

		// 表情list
		reslist = getExpressionRes(35);
		// 初始化表情viewpager
		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		views.add(gv1);
		views.add(gv2);
		expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
		edittext_layout.requestFocus();
		voiceRecorder = new VoiceRecorder(micImageHandler);
		buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
		initListener();
	}

	private void initListener() {
		
		mEditTextContent.setOnClickListener(new ClickListener());
		mEditTextContent.addTextChangedListener(new TextChangedListener());
		swipeRefreshLayout.setOnRefreshListener(this);
		iv_emoticons_normal.setOnClickListener(this);
		iv_emoticons_checked.setOnClickListener(this);
		
	}

	@Override
	public void setModel() {
		onConversationInit();
		onListViewCreation();
	}
	
	/*
	 * 输入框点击事件
	 */
	private class ClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
			btnContainer.setVisibility(View.GONE);
		}
		
	}
	
	/*
	 *  监听文字长度变化
	 */
	private class TextChangedListener implements TextWatcher{

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (!TextUtils.isEmpty(s)) {
				btnMore.setVisibility(View.GONE);
				buttonSend.setVisibility(View.VISIBLE);
			} else {
				btnMore.setVisibility(View.VISIBLE);
				buttonSend.setVisibility(View.GONE);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
		
	}

	protected void onConversationInit(){
		
	    if(chatType == CHATTYPE_SINGLE){
	    	if(!toChatUsername.equals("")){
	    		conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.Chat);
	    	}
	    }
	     
        // 把此会话的未读数置为0
        conversation.markAllMessagesAsRead();
        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
     	// 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            if (chatType == CHATTYPE_SINGLE) {
                conversation.loadMoreMsgFromDB(msgId, pagesize);
            } 
        }
        
        /*
         * 监听好友是否从聊天室离开
         */
        EMChatManager.getInstance().addChatRoomChangeListener(new EMChatRoomChangeListener(){

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if(roomId.equals(toChatUsername)){
                    finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {                
            }

            @Override
            public void onMemberExited(String roomId, String roomName,
                    String participant) {
                
            }

            @Override
            public void onMemberKicked(String roomId, String roomName,
                    String participant) {
                if(roomId.equals(toChatUsername)){
                    String curUser = EMChatManager.getInstance().getCurrentUser();
                    if(curUser.equals(participant)){
                        EMChatManager.getInstance().leaveChatRoom(toChatUsername);
                        finish();
                    }
                }
            }
            
        });
	}
	
	protected void onListViewCreation(){
		
        adapter = new MessageAdapter(context, toChatUsername,chatType);
        // 显示消息
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new ListScrollListener());
        adapter.refreshSelectLast();

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                return false;
            }
        });
	}
	
	/**
	 * listview滑动监听listener
	 */
	private class ListScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData && conversation.getAllMessages().size() != 0) {
					isloading = true;
					loadmorePB.setVisibility(View.VISIBLE);
					// sdk初始化加载的聊天记录为20条，到顶时去db里获取更多					
					List<EMMessage> messages;
					EMMessage firstMsg = conversation.getAllMessages().get(0);
					try {
						// 获取更多messges，调用此方法的时候从db获取的messages
						// sdk会自动存入到此conversation中
						if (chatType == CHATTYPE_SINGLE)
							messages = conversation.loadMoreMsgFromDB(firstMsg.getMsgId(), pagesize);
						else
							messages = conversation.loadMoreGroupMsgFromDB(firstMsg.getMsgId(), pagesize);
					} catch (Exception e1) {
						loadmorePB.setVisibility(View.GONE);
						return;
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					if (messages.size() != 0) {
						// 刷新ui
						if (messages.size() > 0) {
							adapter.refreshSeekTo(messages.size() - 1);
						}
						
						if (messages.size() != pagesize)
							haveMoreData = false;
					} else {
						haveMoreData = false;
					}
					loadmorePB.setVisibility(View.GONE);
					isloading = false;

				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}

	}
	
	/**
	 * onActivityResult
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
			switch (resultCode) {
			case RESULT_CODE_COPY: // 复制消息
				EMMessage copyMsg = ((EMMessage) adapter.getItem(data.getIntExtra("position", -1)));
				clipboard.setText(((TextMessageBody) copyMsg.getBody()).getMessage());
				break;
			case RESULT_CODE_DELETE: // 删除消息
				EMMessage deleteMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
				conversation.removeMessage(deleteMsg.getMsgId());
				adapter.refreshSeekTo(data.getIntExtra("position", adapter.getCount()) - 1);
				break;
			}
		}
		if (resultCode == RESULT_OK) { // 清空消息
			if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
				// 清空会话
				EMChatManager.getInstance().clearConversation(toChatUsername);
				adapter.refresh();
			} else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
				if (cameraFile != null && cameraFile.exists())
					sendPicture(cameraFile.getAbsolutePath());
			} else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图库相册
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null) {
						sendPicByUri(selectedImage);
					}
				}
			}else if (requestCode == REQUEST_CODE_MAP) { // 地图
//				double latitude = data.getDoubleExtra("latitude", 0);
//				double longitude = data.getDoubleExtra("longitude", 0);
//				String locationAddress = data.getStringExtra("address");
//				if (locationAddress != null && !locationAddress.equals("")) {
//				    toggleMore(more);
//					sendLocationMsg(latitude, longitude, "", locationAddress);
//				} else {
//					String st = getResources().getString(R.string.unable_to_get_loaction);
//					Toast.makeText(this, st, 0).show();
//				}
				// 重发消息
			}else if (requestCode == REQUEST_CODE_TEXT || requestCode == REQUEST_CODE_VOICE
					|| requestCode == REQUEST_CODE_PICTURE || requestCode == REQUEST_CODE_FILE) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
				// 粘贴
				if (!TextUtils.isEmpty(clipboard.getText())) {
					String pasteText = clipboard.getText().toString();
					if (pasteText.startsWith(COPY_IMAGE)) {
						// 把图片前�?去掉，还原成正常的path
						sendPicture(pasteText.replace(COPY_IMAGE, ""));
					}

				}
			} else if (conversation.getMsgCount() > 0) {
				adapter.refresh();
				setResult(RESULT_OK);
			} 
		} 
		if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
			if (data != null) {
				String filepath = data.getStringExtra("filepath");
				if (filepath == null) {
					return;
				}
				sendFile(filepath);
			}
		}
	}

	/**
	 * 消息图标点击事件
	 * @param view
	 */
	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.btn_send:
			// 点击发送(发文字和表情)
			String s = mEditTextContent.getText().toString();
			sendText(s);
			break;
		case R.id.btn_take_picture:
			selectPicFromCamera();// 点击照相图标
			break;
		case R.id.btn_picture:
			selectPicFromLocal(); // 点击图片图标
			break;
		case R.id.iv_emoticons_normal:
			
			// 点击显示表情
			more.setVisibility(View.VISIBLE);
			iv_emoticons_normal.setVisibility(View.INVISIBLE);
			iv_emoticons_checked.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.GONE);
			emojiIconContainer.setVisibility(View.VISIBLE);
			hideKeyboard();
			break;
		case R.id.iv_emoticons_checked:
			
			// 点击隐藏表情
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
			break;
		/*case R.id.btn_location:
			// 位置
			startActivityForResult(new Intent(this, BaiduMapActivity.class), REQUEST_CODE_MAP);
			break;*/
		}
	}

	/**
	 * 事件监听 see {@link EMNotifierEvent}
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		
		switch (event.getEvent()) {
			case EventNewMessage: {
				// 获取到message
				EMMessage message = (EMMessage) event.getData();

				String username = null;
				// 单聊消息
				username = message.getFrom();
				// 如果是当前会话的消息，刷新聊天页面
				if (username.equals(getToChatUsername())) {
					refreshUIWithNewMessage();
					// 声音和震动提示有新消息
					HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(message);
				} else {
					// 如果消息不是和当前聊天ID的消息
					YmApplication.getInstance().SaveStrangerInfo(message);
					HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
				}

			}
			break;
			case EventDeliveryAck: {
				refreshUI();
			}
			break;
			case EventReadAck: {
				// 获取到message
				refreshUI();
			}
			break;
		}

	}
	
	private void refreshUIWithNewMessage(){
	    if(adapter == null){
	        return;
	    }
	    runOnUiThread(new Runnable() {
            public void run() {
                adapter.refreshSelectLast();
            }
        });
	}

	private void refreshUI() {
	    if(adapter == null){
            return;
        }
		runOnUiThread(new Runnable() {
			public void run() {
				adapter.refresh();
			}
		});
	}
	
	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public String getCurrentTime() {
		// 获取时间String
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		return format.format(date);
	}

	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		if (!ActivityUtil.isExitsSdcard()) {
			String message = getResources().getString(R.string.sd_card_does_not_exist);
			ActivityUtil.showShortToast(context, message);
			return;
		}

		cameraFile = new File(PathUtil.getInstance().getImagePath(), 
				
		AccountInfoUtils.getInstance(context).getUserInfo().getImEaseMob_username()+getCurrentTime() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
		startActivityForResult(intent,REQUEST_CODE_CAMERA);
	}


	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * 发送文本消息
	 * @param content message content
	 * @param isResend boolean resend
	 */
	public void sendText(String content) {

		if (content.length() > 0) {
			
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			TextMessageBody txtBody = new TextMessageBody(content);
			// 设置消息body
			message.addBody(txtBody);
			message.setAttribute("user_nick", AccountInfoUtils.getInstance(context).getUserInfo().getUser_nick());
			message.setAttribute("user_img", AccountInfoUtils.getInstance(context).getUserInfo().getUser_img());
			// 设置要发给谁,用户usernick
			message.setReceipt(toChatUsername);
			// 把messgage加到conversation
			conversation.addMessage(message);
			// 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方�?
			adapter.refreshSelectLast();
			mEditTextContent.setText("");
			setResult(RESULT_OK);

		}
	}

	/**
	 * 发语音
	 * @param filePath
	 * @param fileName
	 * @param length
	 * @param isResend
	 */
	private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
			message.setAttribute("user_nick", AccountInfoUtils.getInstance(context).getUserInfo().getUser_nick());
			message.setAttribute("user_img", AccountInfoUtils.getInstance(context).getUserInfo().getUser_img());
			message.setReceipt(toChatUsername);
			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
			message.addBody(body);
			conversation.addMessage(message);
			adapter.refreshSelectLast();
			setResult(RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发图片
	 * @param filePath
	 */
	private void sendPicture(final String filePath) {
		String to = toChatUsername;
		// 在视图创建和添加图像的信息
		final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
		message.setAttribute("user_nick", AccountInfoUtils.getInstance(context).getUserInfo().getUser_nick());
		message.setAttribute("user_img", AccountInfoUtils.getInstance(context).getUserInfo().getUser_img());
		message.setReceipt(to);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
		// body.setSendOriginalImage(true);
		message.addBody(body);
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		setResult(RESULT_OK);
	}

	/**
	 * 根据图库图片uri发送图片
	 * @param selectedImage
	 */
	private void sendPicByUri(Uri selectedImage) {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
		String message = getResources().getString(R.string.cant_find_pictures);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			sendPicture(picturePath);
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;

			}
			sendPicture(file.getAbsolutePath());
		}

	}
	
	/**
	 * 发送位置信息
	 * @param latitude
	 * @param longitude
	 * @param imagePath
	 * @param locationAddress
	 */
//	private void sendLocationMsg(double latitude, double longitude, String imagePath, String locationAddress) {
//		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
//		LocationMessageBody locBody = new LocationMessageBody(locationAddress, latitude, longitude);
//		message.addBody(locBody);
//		message.setAttribute("user_nick", AccountInfoUtils.getInstance(context).getUserInfo().getUser_nick());
//		message.setAttribute("user_img", AccountInfoUtils.getInstance(context).getUserInfo().getUser_img());
//		message.setReceipt(toChatUsername);
//		conversation.addMessage(message);
//		listView.setAdapter(adapter);
//		adapter.refreshSelectLast();
//		setResult(RESULT_OK);
//	}
	
	/**
	 * 发送文件
	 * @param uri
	 */
	private void sendFile(String filePath) {
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			ActivityUtil.showShortToast(context, getResources().getString(R.string.File_does_not_exist));
			return;
		}
		if (file.length() > 10 * 1024 * 1024) {
			ActivityUtil.showShortToast(context, getResources().getString(R.string.The_file_is_not_greater_than_10_m));
			return;
		}

		// 创建一个文件消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
		message.setAttribute("user_nick", AccountInfoUtils.getInstance(context).getUserInfo().getUser_nick());
		message.setAttribute("user_img", AccountInfoUtils.getInstance(context).getUserInfo().getUser_img());
		message.setReceipt(toChatUsername);
		// 添加消息体
		NormalFileMessageBody body = new NormalFileMessageBody(new File(filePath));
		message.addBody(body);
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
	}
	

	/**
	 * 重发消息
	 */
	private void resendMessage() {
		EMMessage msg = null;
		msg = conversation.getMessage(resendPos);
		// msg.setBackSend(true);
		msg.status = EMMessage.Status.CREATE;
		adapter.refreshSeekTo(resendPos);
	}

	/**
	 * 显示语音图标按钮
	 * @param view
	 */
	public void setModeVoice(View view) {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		btnContainer.setVisibility(View.VISIBLE);
		emojiIconContainer.setVisibility(View.GONE);

	}

	/**
	 * 显示键盘图标
	 * @param view
	 */
	public void setModeKeyboard(View view) {
		
		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		buttonPressToSpeak.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) {
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} else {
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 点击清空聊天记录
	 * @param view
	 */
	public void emptyHistory(View view) {
		startActivity(new Intent(context, PlayActivity.class));
//		String st5 = getResources().getString(R.string.Whether_to_empty_all_chats);
//		Intent intent = new Intent(context, AlertDialog.class);
//		intent.putExtra("titleIsCancel", true);
//		intent.putExtra("msg", st5);
//		intent.putExtra("cancel", true);
//		startActivityForResult(intent, REQUEST_CODE_EMPTY_HISTORY);
	}
	/**
	 * 显示或隐藏图标按钮页
	 * @param view
	 */
	public void toggleMore(View view) {
		if (more.getVisibility() == View.GONE) {
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
		} else {
			if (emojiIconContainer.getVisibility() == View.VISIBLE) {
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.VISIBLE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
			} else {
				more.setVisibility(View.GONE);
			}

		}

	}

	/**
	 * 点击文字输入�?
	 * @param v
	 */
	public void editClick(View v) {
		listView.setSelection(listView.getCount() - 1);
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		}

	}

	private PowerManager.WakeLock wakeLock;

	/**
	 * 按住说话listener
	 */
	class PressToSpeakListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!ActivityUtil.isExitsSdcard()) {
					String message = getResources().getString(R.string.Send_voice_need_sdcard_support);
					ActivityUtil.showShortToast(context, message);
					return false;
				}
				try {
					v.setPressed(true);
					wakeLock.acquire();
					if (VoicePlayClickListener.isPlaying)
						VoicePlayClickListener.currentPlayListener.stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, toChatUsername, getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					if (wakeLock.isHeld())
						wakeLock.release();
					if (voiceRecorder != null)
						voiceRecorder.discardRecording();
					recordingContainer.setVisibility(View.INVISIBLE);
					ActivityUtil.showShortToast(context, getResources().getString(R.string.recoding_fail));
					return false;
				}

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint.setText(getString(R.string.release_to_cancel));
					recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (event.getY() < 0) {
					// 丢弃记录音频。
					voiceRecorder.discardRecording();

				} else {
					// 停止记录和发送声音文件
					String st1 = getResources().getString(R.string.Recording_without_permission);
					String st2 = getResources().getString(R.string.The_recording_time_is_too_short);
					String st3 = getResources().getString(R.string.send_failure_please);
					try {
						int length = voiceRecorder.stopRecoding();
						if (length > 0) {
							sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername),
									Integer.toString(length), false);
						} else if (length == EMError.INVALID_FILE) {
							ActivityUtil.showShortToast(context, st1);
						} else {
							ActivityUtil.showShortToast(context, st2);
						}
					} catch (Exception e) {
						e.printStackTrace();
						ActivityUtil.showShortToast(context, st3);
					}

				}
				return true;
			default:
				recordingContainer.setVisibility(View.INVISIBLE);
				if (voiceRecorder != null)
					voiceRecorder.discardRecording();
				return false;
			}
		}
	}

	/**
	 * 获取表情的gridview的子view
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(context, R.layout.expression_gridview, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filename = expressionAdapter.getItem(position);
				
				try {
					// 文字输入框可见时，才可输入表情
					// 按住说话可见，不让输入表情
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "delete_expression") { // 不是删除键，显示表情
							// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
							Class clz = Class.forName("com.xiaoxu.music.community.im.applib.utils.SmileUtils");
							Field field = clz.getField(filename);
							mEditTextContent.append(SmileUtils.getSmiledText(context,
									(String) field.get(null)));
						} else { // 删除文字或者表情
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
								if (selectionStart > 0) {
									String body = mEditTextContent.getText().toString();
									String tempStr = body.substring(0, selectionStart);
									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
									if (i != -1) {
										CharSequence cs = tempStr.substring(i, selectionStart);
										if (SmileUtils.containsKey(cs.toString()))
											mEditTextContent.getEditableText().delete(i, selectionStart);
										else
											mEditTextContent.getEditableText().delete(selectionStart - 1,
													selectionStart);
									} else {
										mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
									}
								}
							}

						}
					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}

	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;
			reslist.add(filename);
		}
		return reslist;

	}

	@Override
	protected void onResume() {
		super.onResume();

		 if(adapter != null){
		     adapter.refresh();
	     }

		YMHXSDKHelper sdkHelper = (YMHXSDKHelper) YMHXSDKHelper.getInstance();
		sdkHelper.pushActivity(this);
		// 当进入前台注册事件侦听器
		EMChatManager.getInstance().registerEventListener(this,
				new EMNotifierEvent.Event[] { 
				EMNotifierEvent.Event.EventNewMessage,
				EMNotifierEvent.Event.EventDeliveryAck, 
				EMNotifierEvent.Event.EventReadAck });
	}

	@Override
	protected void onStop() {
		// 当这个活动进入注销这个事件侦听器后台
		EMChatManager.getInstance().unregisterEventListener(this);
		YMHXSDKHelper sdkHelper = (YMHXSDKHelper) YMHXSDKHelper.getInstance();
		// 把此activity 从foreground activity 列表里移除
		sdkHelper.popActivity(this);
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (wakeLock.isHeld())
			wakeLock.release();
		if (VoicePlayClickListener.isPlaying && VoicePlayClickListener.currentPlayListener != null) {
			// 停止语音播放
			VoicePlayClickListener.currentPlayListener.stopPlayVoice();
		}

		try {
			// 停止录音
			if (voiceRecorder.isRecording()) {
				voiceRecorder.discardRecording();
				recordingContainer.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 返回
	 * @param view
	 */
	public void back(View view) {
		EMChatManager.getInstance().unregisterEventListener(this);
		if(conversation.getAllMessages() != null && conversation.getAllMessages().size() > 0){
			String lastmsg = ActivityUtil.getMessageDigest(conversation.getLastMessage(),context);
			UserUtils.SaveUserInfo(context, new UserInfo(toChatUserimg, toChatUsernick, toChatUsername, lastmsg));
		}
		finish();
	}

	/**
	 * 覆盖手机返回键
	 */
	@Override
	public void onBackPressed() {
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			EMChatManager.getInstance().unregisterEventListener(this);
			if(conversation.getAllMessages() != null && conversation.getAllMessages().size() > 0){
				String lastmsg = ActivityUtil.getMessageDigest(conversation.getLastMessage(),context);
				UserUtils.SaveUserInfo(context, new UserInfo(toChatUserimg, toChatUsernick, toChatUsername, lastmsg));
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onNewIntent(Intent intent) {
		// 点击notification bar进入聊天页面，保证只有一个聊天页面
		String username = intent.getStringExtra("userName");
		if (toChatUsername.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}

	/**
	 * 转发消息
	 * @param forward_msg_id
	 */
	protected void forwardMessage(String forward_msg_id) {
		
		final EMMessage forward_msg = EMChatManager.getInstance().getMessage(forward_msg_id);
		EMMessage.Type type = forward_msg.getType();
		switch (type) {
		case TXT:
			// 获取消息内容，发送消息
			String content = ((TextMessageBody) forward_msg.getBody()).getMessage();
			sendText(content);
			break;
		case IMAGE:
			// 发送图片
			String filePath = ((ImageMessageBody) forward_msg.getBody()).getLocalUrl();
			if (filePath != null) {
				File file = new File(filePath);
				if (!file.exists()) {
					// 不存在大图发送缩略图
					filePath = ImageUtils.getThumbnailImagePath(filePath);
				}
				sendPicture(filePath);
			}
			break;
		}
		
		if(forward_msg.getChatType() == EMMessage.ChatType.ChatRoom){
			EMChatManager.getInstance().leaveChatRoom(forward_msg.getTo());
		}
	}

	public String getToChatUsername() {
		return toChatUsername;
	}

	public ListView getListView() {
		return listView;
	}

	@Override
    public void onRefresh() {
            new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                            if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                                    List<EMMessage> messages;
                                    try {
                                        if (chatType == CHATTYPE_SINGLE){
                                            messages = conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
                                        }
                                        else{
                                            messages = conversation.loadMoreGroupMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
                                        }
                                    } catch (Exception e1) {
                                        swipeRefreshLayout.setRefreshing(false);
                                        return;
                                    }
                                    
                                    if (messages.size() > 0) {
                                        adapter.notifyDataSetChanged();
                                        adapter.refreshSeekTo(messages.size() - 1);
                                        if (messages.size() != pagesize){
                                            haveMoreData = false;
                                        }
                                    } else {
                                        haveMoreData = false;
                                    }
                                    
                                    isloading = false;

                            }else{
                           	 ActivityUtil.showShortToast(context, getResources().getString(R.string.no_more_messages));
                            }
                            swipeRefreshLayout.setRefreshing(false);
                    }
            }, 1000);
    }

}
