package com.xiaoxu.music.community.im.adapter;

import java.util.Date;
import java.util.List;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.entity.UserInfo;
import com.xiaoxu.music.community.im.applib.controller.HXSDKHelper;
import com.xiaoxu.music.community.im.applib.controller.YMHXSDKHelper;
import com.xiaoxu.music.community.im.applib.utils.DateUtils;
import com.xiaoxu.music.community.im.applib.utils.SmileUtils;
import com.xiaoxu.music.community.im.applib.utils.UserUtils;

/**
 * 显示所有聊天记录adpater
 */
public class ChatAllHistoryAdapter extends BaseAdapter {

	private Context context;
	private List<UserInfo> list;
	private LayoutInflater inflater;
	private List<EMConversation> conversationList;

	public ChatAllHistoryAdapter(Context context, List<UserInfo> list,
			List<EMConversation> objects) {
		this.context = context;
		this.list = list;
		this.conversationList = objects;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}
	
	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	public void setList(List<UserInfo> list) {
		this.list = list;
	}
	
	public List<UserInfo> getList(){
		return list;
	}
	
	public List<EMConversation> getConversationList() {
		return conversationList;
	}

	public void setConversationList(List<EMConversation> conversationList) {
		this.conversationList = conversationList;
	}


	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_chat_history, parent,false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.msgState = convertView.findViewById(R.id.msg_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		EMConversation conversation = conversationList.get(position);
		EMMessage lastMessage = conversation.getLastMessage();
		if (conversation.getUnreadMsgCount() > 0) {
			// 显示与此用户的消息未读数
			String unread = String.valueOf(conversation.getUnreadMsgCount());
			holder.unreadLabel.setText(unread);
			holder.unreadLabel.setVisibility(View.VISIBLE);
		} else {
			holder.unreadLabel.setVisibility(View.GONE);
		}
			UserInfo user = list.get(position);
			if(user != null && lastMessage != null){
				UserUtils.setUserAvatar(context, user.getUsername(),holder.avatar);
				holder.name.setText(user.getUsernick());
				// 把最后一条消息的内容作为item的message内容
				if (!TextUtils.isEmpty(user.getLastmsg())) {
					holder.message.setText(SmileUtils.getSmiledText(context,user.getLastmsg()), BufferType.SPANNABLE);
				} 
				holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
				if (lastMessage.direct == EMMessage.Direct.SEND&& lastMessage.status == EMMessage.Status.FAIL) {
					holder.msgState.setVisibility(View.VISIBLE);
				} else {
					holder.msgState.setVisibility(View.GONE);
				}
			}

		return convertView;
	}

	private class ViewHolder {

		/** 和谁的聊天记录 */
		TextView name;
		/** 消息未读数 */
		TextView unreadLabel;
		/** 最后一条消息的内容 */
		TextView message;
		/** 最后一条消息的时间 */
		TextView time;
		/** 用户头像 */
		ImageView avatar;
		/** 最后一条消息的发送状态 */
		View msgState;
		/** 整个list中每一行总布局 */

	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			digest = getString(context, R.string.picture)
					+ imageBody.getFileName();
			break;
		case VOICE:// 语音消息
			digest = getString(context, R.string.voice);
			break;
		case TXT: // 文本消息

			if (((YMHXSDKHelper) HXSDKHelper.getInstance())
					.isRobotMenuMessage(message)) {
				digest = ((YMHXSDKHelper) HXSDKHelper.getInstance())
						.getRobotMenuMessageDigest(message);
			} else {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			}
			break;
		default:
			return "";
		}

		return digest;
	}

	String getString(Context context, int resId) {
		return context.getResources().getString(resId);
	}
}
