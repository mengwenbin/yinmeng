package com.xiaoxu.music.community.im.applib.model;


/**
 * HX SDK app model which will manage the user data and preferences
 * @author easemob
 *
 */
public abstract class HXSDKModel {
	
    public abstract void setSettingMsgNotification(boolean paramBoolean);
    
    // 震动和声音总开关，来消息时，是否允许此开关打开
    public abstract boolean getSettingMsgNotification();

    public abstract void setSettingMsgSound(boolean paramBoolean);
    
    // 是否打开声音
    public abstract boolean getSettingMsgSound();

    public abstract void setSettingMsgVibrate(boolean paramBoolean);
    
    // 是否打开震动
    public abstract boolean getSettingMsgVibrate();

    public abstract void setSettingMsgSpeaker(boolean paramBoolean);
    
    // 是否打开扬声器
    public abstract boolean getSettingMsgSpeaker();
    
    /**
     * 返回application所在的process name,默认是包名
     * @return
     */
    public abstract String getAppProcessName();
    /**
     * 是否总是接收好友邀请
     * @return
     */
    public boolean getAcceptInvitationAlways(){
        return false;
    }
    
    /**
     * 是否需要环信好友关系，默认是false
     * @return
     */
    public boolean getUseHXRoster(){
        return false;
    }
    
    /**
     * 是否需要已读回执
     * @return
     */
    public boolean getRequireReadAck(){
        return true;
    }
    
    /**
     * 是否需要已送达回执
     * @return
     */
    public boolean getRequireDeliveryAck(){
        return false;
    }
    
    /**
     * 是否运行在sandbox测试环境. 默认是关掉的
     * 设置sandbox 测试环境
     * 建议开发者开发时设置此模式
     */
    public boolean isSandboxMode(){
        return false;
    }
    
    /**
     * 是否设置debug模式
     * @return
     */
    public boolean isDebugMode(){
        return true;
    }
    
    public void setGroupsSynced(boolean synced){
    }
    
    public boolean isGroupsSynced(){
        return false;
    }
    
    public void setContactSynced(boolean synced){
    }
    
    public boolean isContactSynced(){
        return false;
    }
    
    public void setBlacklistSynced(boolean synced){
    }
    
    public boolean isBacklistSynced(){
        return false;
    }
}
