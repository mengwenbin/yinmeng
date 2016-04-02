package com.xiaoxu.music.community.im.applib.db;

import java.util.List;
import java.util.Map;

import com.xiaoxu.music.community.entity.UserInfo;

import android.content.Context;

public class UserDao {
	public static final String TABLE_NAME = "user";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_DATE = "time";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_AVATAR = "avatar";
	public static final String COLUMN_NAME_MSG = "msg";
	
	public static final String PREF_TABLE_NAME = "pref";
	public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";
	
	public UserDao(Context context) {
	    DBManager.getInstance().onInit(context);
	}

	/**
	 * 保存好友list
	 * @param contactList
	 */
	public void saveContactList(List<UserInfo> contactList) {
	    DBManager.getInstance().saveContactList(contactList);
	}

	/**
	 * 获取好友list
	 * @return
	 */
	public Map<String, UserInfo> getContactList() {
		
	    return DBManager.getInstance().getContactList();
	}
	
	/**
	 * 删除一个联系人
	 * @param username
	 */
	public void deleteContact(String username){
	    DBManager.getInstance().deleteContact(username);
	}
	
	/**
	 * 保存一个陌生人
	 * @param user
	 */
	public void saveStranger(UserInfo user){
	    DBManager.getInstance().saveStranger(user);
	}
	
	
	/*
	 * 得到陌生人list
	 * @param username
	 */
	public List<UserInfo> getStrangerList(){
		return DBManager.getInstance().getStrangerList();
	}
	
	/*
     * 得到一个陌生人
     * 
     */
	public UserInfo getStrangerUser(String username){
		return DBManager.getInstance().getStrangerUser(username);
	}
	
	/*
	 * 得到一个陌生人
	 * 
	 */
	public int delStrangerUser(String username){
		return DBManager.getInstance().delStrangerUser(username);
	}
    
    public void setDisabledIds(List<String> ids){
        DBManager.getInstance().setDisabledIds(ids);
    }
    
    public List<String> getDisabledIds(){
        return DBManager.getInstance().getDisabledIds();
    }
    
}
