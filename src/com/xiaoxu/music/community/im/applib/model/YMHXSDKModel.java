package com.xiaoxu.music.community.im.applib.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.xiaoxu.music.community.im.applib.db.DBManager;
import com.xiaoxu.music.community.im.applib.db.UserDao;
import com.xiaoxu.music.community.im.applib.utils.HXPreferenceUtils;
import android.content.Context;

/**
 * HuanXin default SDK Model implementation
 * @author easemob
 *
 */
public class YMHXSDKModel extends HXSDKModel{
	
    UserDao dao = null;
    private Context context = null;
    protected Map<Key,Object> valueCache = new HashMap<Key,Object>();
    
    public YMHXSDKModel(Context ctx){
        context = ctx;
        HXPreferenceUtils.init(context);
    }
    
    @Override
    public void setSettingMsgNotification(boolean paramBoolean) {
        HXPreferenceUtils.getInstance().setSettingMsgNotification(paramBoolean);
        valueCache.put(Key.VibrateAndPlayToneOn, paramBoolean);
    }

    @Override
    public boolean getSettingMsgNotification() {
        Object val = valueCache.get(Key.VibrateAndPlayToneOn);

        if(val == null){
            val = HXPreferenceUtils.getInstance().getSettingMsgNotification();
            valueCache.put(Key.VibrateAndPlayToneOn, val);
        }
       
        return (Boolean) (val != null?val:true);
    }

    @Override
    public void setSettingMsgSound(boolean paramBoolean) {
        HXPreferenceUtils.getInstance().setSettingMsgSound(paramBoolean);
        valueCache.put(Key.PlayToneOn, paramBoolean);
    }

    @Override
    public boolean getSettingMsgSound() {
        Object val = valueCache.get(Key.PlayToneOn);

        if(val == null){
            val = HXPreferenceUtils.getInstance().getSettingMsgSound();
            valueCache.put(Key.PlayToneOn, val);
        }
       
        return (Boolean) (val != null?val:true);
    }

    @Override
    public void setSettingMsgVibrate(boolean paramBoolean) {
        HXPreferenceUtils.getInstance().setSettingMsgVibrate(paramBoolean);
        valueCache.put(Key.VibrateOn, paramBoolean);
    }

    @Override
    public boolean getSettingMsgVibrate() {
        Object val = valueCache.get(Key.VibrateOn);

        if(val == null){
            val = HXPreferenceUtils.getInstance().getSettingMsgVibrate();
            valueCache.put(Key.VibrateOn, val);
        }
       
        return (Boolean) (val != null?val:true);
    }

    @Override
    public void setSettingMsgSpeaker(boolean paramBoolean) {
        HXPreferenceUtils.getInstance().setSettingMsgSpeaker(paramBoolean);
        valueCache.put(Key.SpakerOn, paramBoolean);
    }

    @Override
    public boolean getSettingMsgSpeaker() {        
        Object val = valueCache.get(Key.SpakerOn);

        if(val == null){
            val = HXPreferenceUtils.getInstance().getSettingMsgSpeaker();
            valueCache.put(Key.SpakerOn, val);
        }
       
        return (Boolean) (val != null?val:true);
    }

    @Override
    public boolean getUseHXRoster() {
        return false;
    }

    public void setDisabledIds(List<String> ids){
        if(dao == null){
            dao = new UserDao(context);
        }
        
        dao.setDisabledIds(ids);
        valueCache.put(Key.DisabledIds, ids);
    }
    
    public List<String> getDisabledIds(){
        Object val = valueCache.get(Key.DisabledIds);
        
        if(dao == null){
            dao = new UserDao(context);
        }

        if(val == null){
            val = dao.getDisabledIds();
            valueCache.put(Key.DisabledIds, val);
        }
       
        return (List<String>) val;
    }
   
    public void allowChatroomOwnerLeave(boolean value){
        HXPreferenceUtils.getInstance().setSettingAllowChatroomOwnerLeave(value);
    }
    
    public boolean isChatroomOwnerLeaveAllowed(){
        return HXPreferenceUtils.getInstance().getSettingAllowChatroomOwnerLeave();
    }
    
    // 演示将打开调试模式
    public boolean isDebugMode(){
        return true;
    }
    
    public void closeDB() {
        DBManager.getInstance().closeDB();
    }
    
    @Override
    public String getAppProcessName() {
        return context.getPackageName();
    }
    
//    public void setContactSynced(boolean synced){
//        HXPreferenceUtils.getInstance().setContactSynced(synced);
//    }
    
//    public boolean isContactSynced(){
//        return HXPreferenceUtils.getInstance().isContactSynced();
//    }
    
//    public void setBlacklistSynced(boolean synced){
//        HXPreferenceUtils.getInstance().setBlacklistSynced(synced);
//    }
    
//    public boolean isBacklistSynced(){
//        return HXPreferenceUtils.getInstance().isBacklistSynced();
//    }
        
    
//    public boolean saveContactList(List<User> contactList) {
//        UserDao dao = new UserDao(context);
//        dao.saveContactList(contactList);
//        return true;
//    }

//    public Map<String, User> getContactList() {
//        UserDao dao = new UserDao(context);
//        return dao.getContactList();
//    }
    
    enum Key{
        VibrateAndPlayToneOn,
        VibrateOn,
        PlayToneOn,
        SpakerOn,
        DisabledGroups,
        DisabledIds
    }
}
