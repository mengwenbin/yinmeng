package com.xiaoxu.music.community.im.adapter;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Handler;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Direct;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.FileMessageBody;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DensityUtil;
import com.easemob.util.FileUtils;
import com.easemob.util.TextFormater;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.MusicUserDetailActivity;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.im.activity.AlertDialog;
import com.xiaoxu.music.community.im.activity.ChatActivity;
import com.xiaoxu.music.community.im.activity.ContextMenu;
import com.xiaoxu.music.community.im.activity.ShowBigImage;
import com.xiaoxu.music.community.im.activity.ShowNormalFileActivity;
import com.xiaoxu.music.community.im.applib.controller.YMHXSDKHelper;
import com.xiaoxu.music.community.im.applib.controller.HXSDKHelper;
import com.xiaoxu.music.community.im.applib.db.UserDao;
import com.xiaoxu.music.community.im.applib.task.LoadImageTask;
import com.xiaoxu.music.community.im.applib.utils.DateUtils;
import com.xiaoxu.music.community.im.applib.utils.ImageCache;
import com.xiaoxu.music.community.im.applib.utils.ImageUtils;
import com.xiaoxu.music.community.im.applib.utils.SmileUtils;
import com.xiaoxu.music.community.im.applib.utils.UserUtils;
import com.xiaoxu.music.community.util.ActivityUtil;

public class MessageAdapter extends BaseAdapter{

	private static final int MESSAGE_TYPE_RECV_TXT = 0;
	private static final int MESSAGE_TYPE_SENT_TXT = 1;
	private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
	private static final int MESSAGE_TYPE_RECV_IMAGE = 3;
	private static final int MESSAGE_TYPE_SENT_VOICE = 4;
	private static final int MESSAGE_TYPE_RECV_VOICE = 5;
	private static final int MESSAGE_TYPE_SENT_ROBOT_MENU = 6;
	private static final int MESSAGE_TYPE_RECV_ROBOT_MENU = 7;
	private static final int MESSAGE_TYPE_SENT_FILE = 8;
	private static final int MESSAGE_TYPE_RECV_FILE = 9;
//	private static final int MESSAGE_TYPE_SENT_LOCATION = 10;
//	private static final int MESSAGE_TYPE_RECV_LOCATION = 11;

	public static final String IMAGE_DIR = "chat/image/";
	public static final String VOICE_DIR = "chat/audio/";
	
	private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
	private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
	private static final int HANDLER_MESSAGE_SEEK_TO = 2;

	// 引用在chatsdk谈话对象
	private EMConversation conversation;
	EMMessage[] messages = null;

	private Context context;
	private String username;
	private String usernick;
	private Activity activity;
	private LayoutInflater inflater;
	private Map<String, Timer> timers = new Hashtable<String, Timer>();

	public MessageAdapter(Context context, String username,int chatType) {
		this.context = context;
		this.username = username;
		inflater = LayoutInflater.from(context);
		activity = (Activity) context;
		this.conversation = EMChatManager.getInstance().getConversation(username);
	}
	
	Handler handler = new Handler() {
		private void refreshList() {
			// UI线程不能直接使用conversation.getAllMessages()
			// 否则在UI刷新过程中，如果收到新的消息，会导致并发问题
			messages = (EMMessage[]) conversation.getAllMessages().toArray(new EMMessage[conversation.getAllMessages().size()]);
			for (int i = 0; i < messages.length; i++) {
				// getMessage will set message as read status
				conversation.getMessage(i);
				
			}
			notifyDataSetChanged();
		}
		
		@Override
		public void handleMessage(android.os.Message message) {
			switch (message.what) {
			case HANDLER_MESSAGE_REFRESH_LIST:
				refreshList();
				break;
			case HANDLER_MESSAGE_SELECT_LAST:
				if (activity instanceof ChatActivity) {
					ListView listView = ((ChatActivity)activity).getListView();
					if (messages.length > 0) {
						listView.setSelection(messages.length - 1);
					}
				}
				break;
			case HANDLER_MESSAGE_SEEK_TO:
				int position = message.arg1;
				if (activity instanceof ChatActivity) {
					ListView listView = ((ChatActivity)activity).getListView();
					listView.setSelection(position);
				}
				break;
			}
		}
	};


	/**
	 * 获取item�?
	 */
	public int getCount() {
		return messages == null ? 0 : messages.length;
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
			return;
		}
		android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
		handler.sendMessage(msg);
	}
	
	/**
	 * 刷新页面,选择最后一条消息
	 */
	public void refreshSelectLast() {
		handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
		handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_SELECT_LAST));
	}
	
	/**
	 * 刷新页面, 选择Position
	 */
	public void refreshSeekTo(int position) {
		handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
		android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
		msg.arg1 = position;
		handler.sendMessage(msg);
	}

	public EMMessage getItem(int position) {
		if (messages != null && position < messages.length) {
			return messages[position];
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * 获取item类型
	 */
	public int getViewTypeCount() {
        return 10;
    }

	/**
	 * 获取item类型
	 */
	public int getItemViewType(int position) {
		EMMessage message = getItem(position); 
		if (message == null) {
			return -1;
		}else if (message.getType() == EMMessage.Type.TXT) {
			if(((YMHXSDKHelper)HXSDKHelper.getInstance()).isRobotMenuMessage(message))
				return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_ROBOT_MENU : MESSAGE_TYPE_SENT_ROBOT_MENU;
			else
				return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
		}else if (message.getType() == EMMessage.Type.IMAGE) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

		}
//		else if (message.getType() == EMMessage.Type.LOCATION) {
//			
//			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
//		}
		else if (message.getType() == EMMessage.Type.VOICE) {
			
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
		}else if (message.getType() == EMMessage.Type.FILE) {
			
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
		}

		return -1;// invalid
	}


	private View createViewByMessage(EMMessage message, int position) {
		switch (message.getType()) {
		
//		case LOCATION:
//			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_location, null) : inflater.inflate(
//					R.layout.row_sent_location, null);
		case IMAGE:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_picture, null) : inflater.inflate(
					R.layout.row_sent_picture, null);

		case VOICE:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_voice, null) : inflater.inflate(
					R.layout.row_sent_voice, null);
		case FILE:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_file, null) : inflater.inflate(
					R.layout.row_sent_file, null);
		default:
			// 含有菜单的消息	
			if (((YMHXSDKHelper)HXSDKHelper.getInstance()).isRobotMenuMessage(message))
				return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_menu, null)
						: inflater.inflate(R.layout.row_sent_message, null);
			else
				return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_message,
						null) : inflater.inflate(R.layout.row_sent_message, null);
		}
	}

	@SuppressWarnings("incomplete-switch")
	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		final EMMessage message = getItem(position);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = createViewByMessage(message, position);
			
			switch (message.getType()) {
			
			//图片消息
			case IMAGE:
				try{
				holder.iv = ((ImageView) convertView.findViewById(R.id.iv_sendPicture));
				holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
				holder.tv = (TextView) convertView.findViewById(R.id.percentage);
				holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
				holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
				holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
				}catch (Exception e) {}
				
				break;
			//文本消息
			case TXT:
				try{
				holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
				holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
				holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
				holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
				holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
				
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
				holder.tvList = (LinearLayout) convertView.findViewById(R.id.ll_layout);
				} catch (Exception e) {}
				break;
//			case LOCATION:
//				try {
//					holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
//					holder.tv = (TextView) convertView.findViewById(R.id.tv_location);
//					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
//					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
//					holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
//				} catch (Exception e) {}
//				break;
			//语音消息
			case VOICE:
				try{
				holder.iv = ((ImageView) convertView.findViewById(R.id.iv_voice));
				holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
				holder.tv = (TextView) convertView.findViewById(R.id.tv_length);
				holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
				holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
				holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
				holder.iv_read_status = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
				}catch (Exception e) {}
				break;
			case FILE:
				try {
					holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
					holder.tv_file_name = (TextView) convertView.findViewById(R.id.tv_file_name);
					holder.tv_file_size = (TextView) convertView.findViewById(R.id.tv_file_size);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.tv_file_download_state = (TextView) convertView.findViewById(R.id.tv_file_state);
					holder.ll_container = (LinearLayout) convertView.findViewById(R.id.ll_file_container);
					// 这里是进度值
					holder.tv = (TextView) convertView.findViewById(R.id.percentage);
					} catch (Exception e) {}
					try {
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
					} catch (Exception e) {}
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		

		switch (message.direct) {
		case RECEIVE:
			if(message.getChatType() == ChatType.Chat){
				holder.tv_usernick.setVisibility(View.GONE);
			}else{
				usernick = new UserDao(context).getStrangerUser(username).getUsernick();
				if(!usernick.equals("")){
					holder.tv_usernick.setText(usernick);
				}else{
					holder.tv_usernick.setText(message.getFrom());
				}
			}
			break;

		case SEND:
			if (message.direct == EMMessage.Direct.SEND) {
				holder.tv_ack = (TextView) convertView.findViewById(R.id.tv_ack);
				holder.tv_delivered = (TextView) convertView.findViewById(R.id.tv_delivered);
				if (holder.tv_ack != null) {
					if (message.isAcked) {
						if (holder.tv_delivered != null) {
							holder.tv_delivered.setVisibility(View.INVISIBLE);
						}
						holder.tv_ack.setVisibility(View.VISIBLE);
					} else {
						holder.tv_ack.setVisibility(View.INVISIBLE);

						// �?查和显示味精ack交付状�??
						if (holder.tv_delivered != null) {
							if (message.isDelivered) {
								holder.tv_delivered.setVisibility(View.VISIBLE);
							} else {
								holder.tv_delivered.setVisibility(View.INVISIBLE);
							}
						}
					}
				}
			} else {
				// 如果是文本,显示的时候给对方发送已读回执
				if (message.getType() == Type.TXT ) {

					try {
							EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
							// 发送已读回执
							message.isAcked = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}
			break;
		}
		//设置用户头像
		setUserAvatar(message, holder.iv_avatar);
		switch (message.getType()) {
		// 根据消息type显示item
		case IMAGE: // 图片
			handleImageMessage(message, holder, position, convertView);
			break;
		case TXT: // 文本
			if(((YMHXSDKHelper)HXSDKHelper.getInstance()).isRobotMenuMessage(message))
				//含有列表的消息
				handleRobotMenuMessage(message, holder, position);
			else
			    handleTextMessage(message, holder, position);
			break;
//		case LOCATION: // 位置
//			handleLocationMessage(message, holder, position, convertView);
//			break;
		case VOICE: // 语音
			handleVoiceMessage(message, holder, position, convertView);
			break;
		case FILE:
			
			break;
		}

		if (message.direct == EMMessage.Direct.SEND) {
			View statusView = convertView.findViewById(R.id.msg_status);
			// 重发按钮点击事件
			statusView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// 显示重发消息的自定义alertdialog
					Intent intent = new Intent(activity, AlertDialog.class);
					intent.putExtra("msg", activity.getString(R.string.confirm_resend));
					intent.putExtra("title", activity.getString(R.string.resend));
					intent.putExtra("cancel", true);
					intent.putExtra("position", position);
					if (message.getType() == EMMessage.Type.TXT)
						activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_TEXT);
					else if (message.getType() == EMMessage.Type.VOICE)
						activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_VOICE);
					else if (message.getType() == EMMessage.Type.IMAGE)
						activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_PICTURE);

				}
			});

		} 

		TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);

		if (position == 0) {
			timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
			timestamp.setVisibility(View.VISIBLE);
		} else {
			// 两条消息时间离得如果稍长，显示时间
			EMMessage prevMessage = getItem(position - 1);
			if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
				timestamp.setVisibility(View.GONE);
			} else {
				timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
				timestamp.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}
	
	
	/**
	 * 显示用户头像
	 * @param message
	 * @param imageView
	 */
	private void setUserAvatar(final EMMessage message, ImageView imageView){
		  if(message.direct == Direct.SEND){
		        //显示自己头像
		        UserUtils.setUserAvatar(context, EMChatManager.getInstance().getCurrentUser(), imageView);
		    }else{
		    	UserUtils.setUserAvatar(context, message, imageView);
		    	imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						Intent intent = new Intent(context, MusicUserDetailActivity.class);
						String user_id = message.getFrom();
						intent.putExtra("user_id", user_id.split("ym_",2)[1]);	
						context.startActivity(intent);
					}
				});
		    }
	    
	}

	/**
	 * 文本消息
	 * @param message
	 * @param holder
	 * @param position
	 */
	private void handleTextMessage(EMMessage message, ViewHolder holder, final int position) {
		TextMessageBody txtBody = (TextMessageBody) message.getBody();
		Spannable span = SmileUtils.getSmiledText(context, txtBody.getMessage());
		// 设置内容
		holder.tv.setText(span, BufferType.SPANNABLE);
		// 设置长按事件监听
		holder.tv.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				activity.startActivityForResult(
						(new Intent(activity, ContextMenu.class)).putExtra("position", position).putExtra("type",
								EMMessage.Type.TXT.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
				return true;
			}
		});

		if (message.direct == EMMessage.Direct.SEND) {
			switch (message.status) {
			case SUCCESS: // 发送成功
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			case FAIL: // 发送失败
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				break;
			case INPROGRESS: // 发送中
				holder.pb.setVisibility(View.VISIBLE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			default:
				// 发送消息取消
				sendMsgInBackground(message, holder);
			}
		}
	}
	
	private void setRobotMenuMessageLayout(LinearLayout parentView,JSONArray jsonArr){
		try {
			parentView.removeAllViews();
			for (int i = 0; i < jsonArr.length(); i++) {
				final String itemStr = jsonArr.getString(i);
				final TextView textView = new TextView(context);
				textView.setText(itemStr);
				textView.setTextSize(15);
				try {
					XmlPullParser xrp = context.getResources().getXml(R.drawable.menu_msg_text_color);
					textView.setTextColor(ColorStateList.createFromXml(context.getResources(), xrp));
				} catch (Exception e) {
					e.printStackTrace();
				}
				textView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						((ChatActivity)context).sendText(itemStr);
					}
				});
				LinearLayout.LayoutParams llLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
				llLp.bottomMargin = DensityUtil.dip2px(context, 3);
				llLp.topMargin = DensityUtil.dip2px(context, 3);
				parentView.addView(textView, llLp);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}
	private void handleRobotMenuMessage(EMMessage message, ViewHolder holder, final int postion){
		try {
			JSONObject jsonObj = message.getJSONObjectAttribute(Constant.MESSAGE_ATTR_ROBOT_MSGTYPE);
			if(jsonObj.has("choice")){
				JSONObject jsonChoice = jsonObj.getJSONObject("choice");
				String title = jsonChoice.getString("title");
				holder.tvTitle.setText(title);
				setRobotMenuMessageLayout(holder.tvList, jsonChoice.getJSONArray("list"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (message.direct == EMMessage.Direct.SEND) {
			switch (message.status) {
			case SUCCESS: // 发送成功
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			case FAIL: // 发送失败
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				break;
			case INPROGRESS: // 发送中
				holder.pb.setVisibility(View.VISIBLE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			default:
				// 发送取消
				sendMsgInBackground(message, holder);
			}
		}
	}


	/**
	 * 图片消息
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleImageMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {
		holder.pb.setTag(position);
		holder.iv.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(activity, ContextMenu.class);
				intent.putExtra("position", position);
				intent.putExtra("type",EMMessage.Type.IMAGE.ordinal());
				activity.startActivityForResult(intent,ChatActivity.REQUEST_CODE_CONTEXT_MENU);
				return true;
			}
		});

		if (message.direct == EMMessage.Direct.RECEIVE) {
			if (message.status == EMMessage.Status.INPROGRESS) {
				holder.iv.setImageResource(R.drawable.default_image);
				showDownloadImageProgress(message, holder);
			} else {
				holder.pb.setVisibility(View.GONE);
				holder.tv.setVisibility(View.GONE);
				holder.iv.setImageResource(R.drawable.default_image);
				ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
				
				//本地图片，语音等文件的url
				if (imgBody.getLocalUrl() != null) {
					//远程图片，声音等的url
					String remotePath = imgBody.getRemoteUrl();
					//得到这个文件名字
					String filePath = ImageUtils.getImagePath(remotePath);
					//获取 缩略图的url
					String thumbRemoteUrl = imgBody.getThumbnailUrl();
					if(TextUtils.isEmpty(thumbRemoteUrl)&&!TextUtils.isEmpty(remotePath)){
						thumbRemoteUrl = remotePath;
					}
					String thumbnailPath = ImageUtils.getThumbnailImagePath(thumbRemoteUrl);
					showImageView(thumbnailPath, holder.iv, filePath, imgBody.getRemoteUrl(), message);
				}
			}
			return;
		}

		// 发送的消息
		// process send message
		ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
		String filePath = imgBody.getLocalUrl();
		if (filePath != null && new File(filePath).exists()) {
			showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, null, message);
		} else {
			showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, IMAGE_DIR, message);
		}

		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.GONE);
			holder.tv.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.GONE);
			holder.tv.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			holder.staus_iv.setVisibility(View.GONE);
			holder.pb.setVisibility(View.VISIBLE);
			holder.tv.setVisibility(View.VISIBLE);
			if (timers.containsKey(message.getMsgId()))
				return;
			// set a timer
			final Timer timer = new Timer();
			timers.put(message.getMsgId(), timer);
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					activity.runOnUiThread(new Runnable() {
						public void run() {
							holder.pb.setVisibility(View.VISIBLE);
							holder.tv.setVisibility(View.VISIBLE);
							holder.tv.setText(message.progress + "%");
							if (message.status == EMMessage.Status.SUCCESS) {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								timer.cancel();
							} else if (message.status == EMMessage.Status.FAIL) {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								holder.staus_iv.setVisibility(View.VISIBLE);
								ActivityUtil.showShortToast(context, activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast));
								timer.cancel();
							}

						}
					});

				}
			}, 0, 500);
			break;
		default:
			sendPictureMessage(message, holder);
		}
	}
	
	/**
	 * 处理位置消息
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
//	private void handleLocationMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {
//		TextView locationView = ((TextView) convertView.findViewById(R.id.tv_location));
//		LocationMessageBody locBody = (LocationMessageBody) message.getBody();
//		locationView.setText(locBody.getAddress());
//		LatLng loc = new LatLng(locBody.getLatitude(), locBody.getLongitude());
//		locationView.setOnClickListener(new MapClickListener(loc, locBody.getAddress()));
//		locationView.setOnLongClickListener(new OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				Intent intent = new Intent(activity, ContextMenu.class);
//				intent.putExtra("position", position);
//				intent.putExtra("type",EMMessage.Type.LOCATION.ordinal());
//				activity.startActivityForResult(intent,ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//				return false;
//			}
//		});
//
//		if (message.direct == EMMessage.Direct.RECEIVE) {
//			return;
//		}
//		// deal with send message
//		switch (message.status) {
//		case SUCCESS:
//			holder.pb.setVisibility(View.GONE);
//			holder.staus_iv.setVisibility(View.GONE);
//			break;
//		case FAIL:
//			holder.pb.setVisibility(View.GONE);
//			holder.staus_iv.setVisibility(View.VISIBLE);
//			break;
//		case INPROGRESS:
//			holder.pb.setVisibility(View.VISIBLE);
//			break;
//		default:
//			sendMsgInBackground(message, holder);
//		}
//	}
	
	/*
	 * 点击地图消息listener
	 */
//	class MapClickListener implements View.OnClickListener {
//
//		LatLng location;
//		String address;
//
//		public MapClickListener(LatLng loc, String address) {
//			location = loc;
//			this.address = address;
//
//		}
//		@Override
//		public void onClick(View v) {
//			Intent intent;
//			intent = new Intent(context, BaiduMapActivity.class);
//			intent.putExtra("latitude", location.latitude);
//			intent.putExtra("longitude", location.longitude);
//			intent.putExtra("address", address);
//			activity.startActivity(intent);
//		}
//
//	}

	/**
	 * 语音消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleVoiceMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {
		VoiceMessageBody voiceBody = (VoiceMessageBody) message.getBody();
		int len = voiceBody.getLength();
		if(len>0){
			holder.tv.setText(voiceBody.getLength() + "\"");
			holder.tv.setVisibility(View.VISIBLE);
		}else{
			holder.tv.setVisibility(View.INVISIBLE);
		}
		holder.iv.setOnClickListener(new VoicePlayClickListener(message, holder.iv, holder.iv_read_status, this, activity, username));
		holder.iv.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				activity.startActivityForResult(
						(new Intent(activity, ContextMenu.class)).putExtra("position", position).putExtra("type",
								EMMessage.Type.VOICE.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
				return true;
			}
		});
		if (((ChatActivity)activity).playMsgId != null
				&& ((ChatActivity)activity).playMsgId.equals(message
						.getMsgId())&&VoicePlayClickListener.isPlaying) {
			AnimationDrawable voiceAnimation;
			if (message.direct == EMMessage.Direct.RECEIVE) {
				holder.iv.setImageResource(R.anim.voice_from_icon);
			} else {
				holder.iv.setImageResource(R.anim.voice_to_icon);
			}
			voiceAnimation = (AnimationDrawable) holder.iv.getDrawable();
			voiceAnimation.start();
		} else {
			if (message.direct == EMMessage.Direct.RECEIVE) {
				holder.iv.setImageResource(R.drawable.chatfrom_voice_playing);
			} else {
				holder.iv.setImageResource(R.drawable.chatto_voice_playing);
			}
		}
		
		
		if (message.direct == EMMessage.Direct.RECEIVE) {
			if (message.isListened()) {
				// 隐藏语音未听标志
				holder.iv_read_status.setVisibility(View.INVISIBLE);
			} else {
				holder.iv_read_status.setVisibility(View.VISIBLE);
			}
			if (message.status == EMMessage.Status.INPROGRESS) {
				holder.pb.setVisibility(View.VISIBLE);
				((FileMessageBody) message.getBody()).setDownloadCallback(new EMCallBack() {

					@Override
					public void onSuccess() {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								holder.pb.setVisibility(View.INVISIBLE);
								notifyDataSetChanged();
							}
						});

					}

					@Override
					public void onProgress(int progress, String status) {
					}

					@Override
					public void onError(int code, String message) {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								holder.pb.setVisibility(View.INVISIBLE);
							}
						});

					}
				});

			} else {
				holder.pb.setVisibility(View.INVISIBLE);

			}
			return;
		}

		// 直到这里,处理发�?�声音味�?
		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			holder.pb.setVisibility(View.VISIBLE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		default:
			sendMsgInBackground(message, holder);
		}
	}
	
	/**
	 * 文件消息
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleFileMessage(final EMMessage message, final ViewHolder holder, int position, View convertView) {
		final NormalFileMessageBody fileMessageBody = (NormalFileMessageBody) message.getBody();
		final String filePath = fileMessageBody.getLocalUrl();
		holder.tv_file_name.setText(fileMessageBody.getFileName());
		holder.tv_file_size.setText(TextFormater.getDataSize(fileMessageBody.getFileSize()));
		holder.ll_container.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				File file = new File(filePath);
				if (file != null && file.exists()) {
					// 文件存在，直接打开
					FileUtils.openFile(file, (Activity) context);
				} else {
					// 下载
					Intent intent = new Intent(context, ShowNormalFileActivity.class);
					intent.putExtra("msgbody", fileMessageBody);
					context.startActivity(intent);
				}
				if (message.direct == EMMessage.Direct.RECEIVE && !message.isAcked && message.getChatType() != ChatType.GroupChat && message.getChatType() != ChatType.ChatRoom) {
					try {
						EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
						message.isAcked = true;
					} catch (EaseMobException e) {
						e.printStackTrace();
					}
				}
			}
		});
		String st1 = context.getResources().getString(R.string.Have_downloaded);
		String st2 = context.getResources().getString(R.string.Did_not_download);
		if (message.direct == EMMessage.Direct.RECEIVE) { // 接收的消息
			File file = new File(filePath);
			if (file != null && file.exists()) {
				holder.tv_file_download_state.setText(st1);
			} else {
				holder.tv_file_download_state.setText(st2);
			}
			return;
		}

		// 
		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.INVISIBLE);
			holder.tv.setVisibility(View.INVISIBLE);
			holder.staus_iv.setVisibility(View.INVISIBLE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.INVISIBLE);
			holder.tv.setVisibility(View.INVISIBLE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			if (timers.containsKey(message.getMsgId()))
				return;
			// 设置一个定时器
			final Timer timer = new Timer();
			timers.put(message.getMsgId(), timer);
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							holder.pb.setVisibility(View.VISIBLE);
							holder.tv.setVisibility(View.VISIBLE);
							holder.tv.setText(message.progress + "%");
							if (message.status == EMMessage.Status.SUCCESS) {
								holder.pb.setVisibility(View.INVISIBLE);
								holder.tv.setVisibility(View.INVISIBLE);
								timer.cancel();
							} else if (message.status == EMMessage.Status.FAIL) {
								holder.pb.setVisibility(View.INVISIBLE);
								holder.tv.setVisibility(View.INVISIBLE);
								holder.staus_iv.setVisibility(View.VISIBLE);
								ActivityUtil.showShortToast(context, activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast));
								timer.cancel();
							}

						}
					});

				}
			}, 0, 500);
			break;
		default:
			// 发送消息
			sendMsgInBackground(message, holder);
		}

	}

	/**
	 * 发送消息
	 * @param message
	 * @param holder
	 * @param position
	 */
	public void sendMsgInBackground(final EMMessage message, final ViewHolder holder) {
		holder.staus_iv.setVisibility(View.GONE);
		holder.pb.setVisibility(View.VISIBLE);

		// 调用sdk异步发送方
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onSuccess() {
				
				updateSendedView(message, holder);
			}

			@Override
			public void onError(int code, String error) {
				
				updateSendedView(message, holder);
			}

			@Override
			public void onProgress(int progress, String status) {
			}

		});

	}

	/*
	 * 聊天sdk将自动下载缩略图图像的图像信息,我们需要注册回调显示下载进度
	 */
	private void showDownloadImageProgress(final EMMessage message, final ViewHolder holder) {
		
		final ImageMessageBody msgbody = (ImageMessageBody)message.getBody();
		if(holder.pb!=null)
		holder.pb.setVisibility(View.VISIBLE);
		if(holder.tv!=null)
		holder.tv.setVisibility(View.VISIBLE);

		msgbody.setDownloadCallback(new EMCallBack() {

			@Override
			public void onSuccess() {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (message.getType() == EMMessage.Type.IMAGE) {
							holder.pb.setVisibility(View.GONE);
							holder.tv.setVisibility(View.GONE);
						}
						notifyDataSetChanged();
					}
				});
			}

			@Override
			public void onError(int code, String message) {

			}

			@Override
			public void onProgress(final int progress, String status) {
				if (message.getType() == EMMessage.Type.IMAGE) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							holder.tv.setText(progress + "%");
						}
					});
				}

			}

		});
	}

	/*
	 * 发送信息新的sdk
	 */
	private void sendPictureMessage(final EMMessage message, final ViewHolder holder) {
		try {
			// before send, update ui
			holder.staus_iv.setVisibility(View.GONE);
			holder.pb.setVisibility(View.VISIBLE);
			holder.tv.setVisibility(View.VISIBLE);
			holder.tv.setText("0%");
			
			EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

				@Override
				public void onSuccess() {
					activity.runOnUiThread(new Runnable() {
						public void run() {
							holder.pb.setVisibility(View.GONE);
							holder.tv.setVisibility(View.GONE);
						}
					});
				}

				@Override
				public void onError(int code, String error) {
					
					activity.runOnUiThread(new Runnable() {
						public void run() {
							holder.pb.setVisibility(View.GONE);
							holder.tv.setVisibility(View.GONE);
							holder.staus_iv.setVisibility(View.VISIBLE);
							ActivityUtil.showShortToast(context, activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast));
						}
					});
				}

				@Override
				public void onProgress(final int progress, String status) {
					activity.runOnUiThread(new Runnable() {
						public void run() {
							holder.tv.setText(progress + "%");
						}
					});
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新ui上消息发送状态
	 * @param message
	 * @param holder
	 */
	private void updateSendedView(final EMMessage message, final ViewHolder holder) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (message.status == EMMessage.Status.SUCCESS) {
					 if (message.getType() == EMMessage.Type.FILE) {
					 holder.pb.setVisibility(View.INVISIBLE);
					 holder.staus_iv.setVisibility(View.INVISIBLE);
					 } else {
					 holder.pb.setVisibility(View.GONE);
					 holder.staus_iv.setVisibility(View.GONE);
					 }

				} else if (message.status == EMMessage.Status.FAIL) {
					 if (message.getType() == EMMessage.Type.FILE) {
					 holder.pb.setVisibility(View.INVISIBLE);
					 } else {
					 holder.pb.setVisibility(View.GONE);
					 }
					 holder.staus_iv.setVisibility(View.VISIBLE);
				    
				    if(message.getError() == EMError.MESSAGE_SEND_INVALID_CONTENT){
				        ActivityUtil.showShortToast(context, activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast));
				    }else{
				    	ActivityUtil.showShortToast(context, activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast));
				    }
				}

				notifyDataSetChanged();
			}
		});
	}

	/**
	 * 图像加载到形象的看法
	 * @param thumbernailPath 缩略图的url
	 * @param localFullSizePath 文件名字
	 * @param remoteDir 远程图片，声音等的url
	 * @return the image exists or not
	 */
	private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath, String remoteDir,
			final EMMessage message) {
		
		final String remote = remoteDir;
		// 首先检查如果缩略图已经加载到缓存中
		Bitmap bitmap = ImageCache.getInstance().get(thumbernailPath);
		if (bitmap != null) {
			// 缩略图图像已经加载,重用bitmap
			iv.setImageBitmap(bitmap);
			iv.setClickable(true); 
			iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity, ShowBigImage.class);
					File file = new File(localFullSizePath);
					if (file.exists()) {
						Uri uri = Uri.fromFile(file);
						intent.putExtra("uri", uri);
					} else {
						// 当地的全尺寸图片不存在ShowBigImage首先要从服务器下载它
						ImageMessageBody body = (ImageMessageBody) message.getBody();
						intent.putExtra("secret", body.getSecret());
						intent.putExtra("remotepath", remote);
					}
					if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked) {
						try {
							EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
							message.isAcked = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					activity.startActivity(intent);
				}
			});
			return true;
		} else {

			new LoadImageTask().execute(thumbernailPath, localFullSizePath, remote, message.getChatType(), iv, activity, message);
			return true;
		}

	}

	public static class ViewHolder {
		
		ImageView iv;
		TextView tv;
		ProgressBar pb;
		ImageView staus_iv;
		ImageView iv_avatar;
		TextView tv_usernick;
		ImageView playBtn;
		TextView timeLength;
		TextView size;
		LinearLayout container_status_btn;
		LinearLayout ll_container;
		ImageView iv_read_status;
		// 显示已读回执状态
		TextView tv_ack;
		// 显示送达回执状态
		TextView tv_delivered;

		TextView tv_file_name;
		TextView tv_file_size;
		TextView tv_file_download_state;
		
		TextView tvTitle;
		LinearLayout tvList;
	}

}