package com.xiaoxu.music.community.im.applib.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.easemob.util.HanziToPinyin;
import com.xiaoxu.music.community.entity.InviteMessage;
import com.xiaoxu.music.community.entity.InviteMessage.InviteMesageStatus;
import com.xiaoxu.music.community.entity.UserInfo;

public class DBManager {
	
    private DbOpenHelper dbHelper;
    static private DBManager dbMgr = new DBManager();
    
    void onInit(Context context){
        dbHelper = DbOpenHelper.getInstance(context);
    }
    
    public static synchronized DBManager getInstance(){
        return dbMgr;
    }
    
    /**
     * 保存好友list
     * @param contactList
     */
    synchronized public void saveContactList(List<UserInfo> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserDao.TABLE_NAME, null, null);
            for (UserInfo user : contactList) {
                ContentValues values = new ContentValues();
                values.put(UserDao.COLUMN_NAME_ID, user.getUsername());
                if(user.getUsernick() != null)
                    values.put(UserDao.COLUMN_NAME_NICK, user.getUsernick());
                if(user.getAvatar() != null)
                    values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
                db.replace(UserDao.TABLE_NAME, null, values);
            }
        }
    }

    /**
     * 获取好友list
     * @return
     */
    synchronized public Map<String, UserInfo> getContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, UserInfo> users = new HashMap<String, UserInfo>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UserDao.TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_NICK));
                String avatar = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_AVATAR));
                UserInfo user = new UserInfo();
                user.setUsername(username);
                user.setUsernick(nick);
                user.setAvatar(avatar);
                String headerName = null;
                if (!TextUtils.isEmpty(user.getUsernick())) {
                    headerName = user.getUsername();
                } else {
                    headerName = user.getUsername();
                }
                
                if (Character.isDigit(headerName.charAt(0))) {
                    user.setHeader("#");
                } else {
                    user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1))
                            .get(0).target.substring(0, 1).toUpperCase());
                    char header = user.getHeader().toLowerCase().charAt(0);
                    if (header < 'a' || header > 'z') {
                        user.setHeader("#");
                    }
                }
                users.put(username, user);
            }
            cursor.close();
        }
        return users;
    }
    
    /**
     * 删除一个联系人
     * @param username
     */
    synchronized public void deleteContact(String username){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete(UserDao.TABLE_NAME, UserDao.COLUMN_NAME_ID + " = ?", new String[]{username});
        }
    }
    
    /**
     * 保存一个陌生人
     * @param user
     */
    synchronized public void saveStranger(UserInfo user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.COLUMN_NAME_ID, user.getUsername());
        values.put(UserDao.COLUMN_NAME_NICK, user.getUsernick());
        values.put(UserDao.COLUMN_DATE, user.getTime());
        values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
        values.put(UserDao.COLUMN_NAME_MSG, user.getLastmsg());
        if(db.isOpen()){
            db.replace(UserDao.TABLE_NAME, null, values);
        }
    }
    
    /*
     * 得到陌生人列表的信息
     */
    public List<UserInfo> getStrangerList(){
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	List<UserInfo> users = new ArrayList<UserInfo>();
    	if(db.isOpen()){
    		Cursor cursor = db.rawQuery("select * from " + UserDao.TABLE_NAME +" order by " + UserDao.COLUMN_DATE +" desc", null);
    		while (cursor.moveToNext()) {
    			String username = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_ID));
    			String usernick = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_NICK));
    			String avatar = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_AVATAR));
    			String msg = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_MSG));
    			users.add(new UserInfo(avatar, usernick, username, msg));
			}
    		cursor.close();
    	}
		return users;
    }
    
    /*
     * 得到一个陌生人
     */
    public UserInfo getStrangerUser(String name){
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	UserInfo user = null;
    	if(db.isOpen()){
    		Cursor cursor = db.rawQuery("select * from user where username=?",new String[]{name});
    		while(cursor.moveToNext()){ 
    			String username = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_ID));
    			String usernick = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_NICK));
    			String avatar = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_AVATAR));
    			String msg = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_MSG));
    			user = new UserInfo(avatar, usernick, username,msg);
    		}
    		cursor.close();
    	}
		return user;
    }
    
    /*
     * 删除一个陌生人
     */
    public int delStrangerUser(String name){
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
    	int row = db.delete(UserDao.TABLE_NAME, UserDao.COLUMN_NAME_ID + " = ?", new String[]{name});
    	return row;
    }
    
//    public void setDisabledGroups(List<String> groups){
//        setList(UserDao.COLUMN_NAME_DISABLED_GROUPS, groups);
//    }
//    
//    public List<String>  getDisabledGroups(){       
//        return getList(UserDao.COLUMN_NAME_DISABLED_GROUPS);
//    }
    
    public void setDisabledIds(List<String> ids){
        setList(UserDao.COLUMN_NAME_DISABLED_IDS, ids);
    }
    
    public List<String> getDisabledIds(){
        return getList(UserDao.COLUMN_NAME_DISABLED_IDS);
    }
    
    synchronized private void setList(String column, List<String> strList){
        StringBuilder strBuilder = new StringBuilder();
        
        for(String hxid:strList){
            strBuilder.append(hxid).append("$");
        }
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(column, strBuilder.toString());

            db.update(UserDao.PREF_TABLE_NAME, values, null,null);
        }
    }
    
    synchronized private List<String> getList(String column){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + column + " from " + UserDao.PREF_TABLE_NAME,null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String strVal = cursor.getString(0);
        if (strVal == null || strVal.equals("")) {
            return null;
        }
        
        cursor.close();
        
        String[] array = strVal.split("$");
        
        if(array != null && array.length > 0){
            List<String> list = new ArrayList<String>();
            for(String str:array){
                list.add(str);
            }
            
            return list;
        }
        
        return null;
    }
    
    /**
     * 保存message
     * @param message
     * @return  返回这条messaged在db中的id
     */
    public synchronized Integer saveMessage(InviteMessage message){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int id = -1;
        if(db.isOpen()){
            ContentValues values = new ContentValues();
            values.put(InviteMessgeDao.COLUMN_NAME_FROM, message.getFrom());
//            values.put(InviteMessgeDao.COLUMN_NAME_GROUP_ID, message.getGroupId());
//            values.put(InviteMessgeDao.COLUMN_NAME_GROUP_Name, message.getGroupName());
            values.put(InviteMessgeDao.COLUMN_NAME_REASON, message.getReason());
            values.put(InviteMessgeDao.COLUMN_NAME_TIME, message.getTime());
            values.put(InviteMessgeDao.COLUMN_NAME_STATUS, message.getStatus().ordinal());
            db.insert(InviteMessgeDao.TABLE_NAME, null, values);
            
            Cursor cursor = db.rawQuery("select last_insert_rowid() from " + InviteMessgeDao.TABLE_NAME,null); 
            if(cursor.moveToFirst()){
                id = cursor.getInt(0);
            }
            
            cursor.close();
        }
        return id;
    }
    
    /**
     * 更新message
     * @param msgId
     * @param values
     */
    synchronized public void updateMessage(int msgId,ContentValues values){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.update(InviteMessgeDao.TABLE_NAME, values, InviteMessgeDao.COLUMN_NAME_ID + " = ?", new String[]{String.valueOf(msgId)});
        }
    }
    
    /**
     * 获取messges
     * @return
     */
    synchronized public List<InviteMessage> getMessagesList(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<InviteMessage> msgs = new ArrayList<InviteMessage>();
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select * from " + InviteMessgeDao.TABLE_NAME + " desc",null);
            while(cursor.moveToNext()){
                InviteMessage msg = new InviteMessage();
                int id = cursor.getInt(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_ID));
                String from = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_FROM));
//                String groupid = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_ID));
//                String groupname = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_Name));
                String reason = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_REASON));
                long time = cursor.getLong(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_TIME));
                int status = cursor.getInt(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_STATUS));
                
                msg.setId(id);
                msg.setFrom(from);
//                msg.setGroupId(groupid);
//                msg.setGroupName(groupname);
                msg.setReason(reason);
                msg.setTime(time);
                if(status == InviteMesageStatus.BEINVITEED.ordinal())
                    msg.setStatus(InviteMesageStatus.BEINVITEED);
                else if(status == InviteMesageStatus.BEAGREED.ordinal())
                    msg.setStatus(InviteMesageStatus.BEAGREED);
                else if(status == InviteMesageStatus.BEREFUSED.ordinal())
                    msg.setStatus(InviteMesageStatus.BEREFUSED);
                else if(status == InviteMesageStatus.AGREED.ordinal())
                    msg.setStatus(InviteMesageStatus.AGREED);
                else if(status == InviteMesageStatus.REFUSED.ordinal())
                    msg.setStatus(InviteMesageStatus.REFUSED);
                else if(status == InviteMesageStatus.BEAPPLYED.ordinal()){
                    msg.setStatus(InviteMesageStatus.BEAPPLYED);
                }
                msgs.add(msg);
            }
            cursor.close();
        }
        return msgs;
    }
    
    synchronized public void deleteMessage(String from){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete(InviteMessgeDao.TABLE_NAME, InviteMessgeDao.COLUMN_NAME_FROM + " = ?", new String[]{from});
        }
    }
    
    synchronized public void closeDB(){
        if(dbHelper != null){
            dbHelper.closeDB();
        }
    }
}