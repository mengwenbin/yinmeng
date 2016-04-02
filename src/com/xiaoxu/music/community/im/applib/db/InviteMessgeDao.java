package com.xiaoxu.music.community.im.applib.db;


import java.util.List;

import com.xiaoxu.music.community.entity.InviteMessage;

import android.content.ContentValues;
import android.content.Context;

public class InviteMessgeDao {
	public static final String TABLE_NAME = "new_friends_msgs";
	public static final String COLUMN_NAME_ID = "id";
	public static final String COLUMN_NAME_FROM = "username";
	
	public static final String COLUMN_NAME_TIME = "time";
	public static final String COLUMN_NAME_REASON = "reason";
	public static final String COLUMN_NAME_STATUS = "status";
	public static final String COLUMN_NAME_ISINVITEFROMME = "isInviteFromMe";
		
	public InviteMessgeDao(Context context){
	    DBManager.getInstance().onInit(context);
	}
	
	/**
	 * 保存message
	 * @param message
	 * @return  返回这条messaged在db中的id
	 */
	public Integer saveMessage(InviteMessage message){
		return DBManager.getInstance().saveMessage(message);
	}
	
	/**
	 * 更新message
	 * @param msgId
	 * @param values
	 */
	public void updateMessage(int msgId,ContentValues values){
	    DBManager.getInstance().updateMessage(msgId, values);
	}
	
	/**
	 * 获取messges
	 * @return
	 */
	public List<InviteMessage> getMessagesList(){
		return DBManager.getInstance().getMessagesList();
	}
	
	public void deleteMessage(String from){
	    DBManager.getInstance().deleteMessage(from);
	}
}
