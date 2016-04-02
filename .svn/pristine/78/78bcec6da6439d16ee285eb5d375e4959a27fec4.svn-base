package com.xiaoxu.music.community.im.applib.controller;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.activity.MainTabActivity;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.im.activity.ChatActivity;
import com.xiaoxu.music.community.im.applib.model.YMHXSDKModel;
import com.xiaoxu.music.community.im.applib.model.HXNotifier;
import com.xiaoxu.music.community.im.applib.model.HXNotifier.HXNotificationInfoProvider;
import com.xiaoxu.music.community.im.applib.model.HXSDKModel;
import com.xiaoxu.music.community.util.ActivityUtil;

/**
 * 演示UI HX SDK HXSDKHelper助手类的子类
 * @author easemob
 *
 */
public class YMHXSDKHelper extends HXSDKHelper{

    private static final String TAG = "HXSDKHelper";
    //EMEventListener
    protected EMEventListener eventListener = null;

    //contact list in cache
//    private Map<String, User> contactList;
    
    /**
     * 用来记录foreground Activity
     */
    private List<Activity> activityList = new ArrayList<Activity>();
    
    public void pushActivity(Activity activity){
        if(!activityList.contains(activity)){
            activityList.add(0,activity); 
        }
    }
    
    public void popActivity(Activity activity){
        activityList.remove(activity);
    }
    
    @Override
    protected void initHXOptions(){
        super.initHXOptions();

        // 你也可以EMChatOptions SDK期权相关的设置
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());  
    }

    @Override
    protected void initListener(){
        super.initListener();
        //注册消息事件监听
        initEventListener();
    }
    
    /**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    protected void initEventListener() {
        eventListener = new EMEventListener() {
            private BroadcastReceiver broadCastReceiver = null;
            
            @Override
            public void onEvent(EMNotifierEvent event) {
                EMMessage message = null;
                switch (event.getEvent()) {
                case EventNewMessage:{
                    //应用在后台，不需要刷新UI,通知栏提示新消息
                    if(activityList.size() <= 0){
                    	message = (EMMessage) event.getData();
                    	YmApplication.getInstance().SaveStrangerInfo(message);
                    	Intent intent = new Intent(Constant.REFRESH_MESSAGE);
                    	intent.putExtra("refresh", true);
                    	appContext.sendBroadcast(intent);
                        HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                    }
                }
                    break;
                case EventOfflineMessage:{
    				List<EMMessage> messages = (List<EMMessage>) event.getData();
    				YmApplication.getInstance().SaveStranger(messages);
    			}
    				break;
                // 下面是给一个例子来展示一个cmd吐司,应用程序不应该遵循这个
                // 所以要小心
                case EventNewCMDMessage:{
                    
                    EMLog.d(TAG, "收到透传消息");
                    //获取消息body
                    CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action;//获取自定义action
                    
                    //获取扩展属性 此处省略
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action,message.toString()));
                    final String str = appContext.getString(R.string.receive_the_passthrough);
                    
                    final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
                    IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);
                    
                    if(broadCastReceiver == null){
                        broadCastReceiver = new BroadcastReceiver(){

                            @Override
                            public void onReceive(Context context, Intent intent) {
                                ActivityUtil.showShortToast(appContext, intent.getStringExtra("cmd_value"));
                            }
                        };
                        
                      //注册广播接收者
                        appContext.registerReceiver(broadCastReceiver,cmdFilter);
                    }

                    Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
                    broadcastIntent.putExtra("cmd_value", str+action);
                    appContext.sendBroadcast(broadcastIntent, null);
                    
                    break;
                }
             }
          }
      };
        
        EMChatManager.getInstance().registerEventListener(
        		eventListener,
        		new EMNotifierEvent.Event[] { 
        		EMNotifierEvent.Event.EventNewMessage,
        		EMNotifierEvent.Event.EventOfflineMessage});
    }
    /**
     * 自定义通知栏提示内容
     * @return
     */
    @Override
    protected HXNotificationInfoProvider getNotificationListener() {
        //可以覆盖默认的设置
        return new HXNotificationInfoProvider() {
            
            @Override
            public String getTitle(EMMessage message) {
              //修改标题,这里使用默认
                return message.getStringAttribute("user_nick", "");
            }
            
            @Override
            public int getSmallIcon(EMMessage message) {
              //设置小图标，这里为默认
                return 0;
            }
            
            @SuppressWarnings("incomplete-switch")
			@Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String digest = ActivityUtil.getMessageDigest(message, appContext);
                switch (message.getType()) {
        		case IMAGE: // 图片消息
        			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
        			digest = appContext.getResources().getString(R.string.picture)+ imageBody.getFileName();
        			break;
        		case VOICE:// 语音消息
        			digest = appContext.getResources().getString(R.string.voice);
        			break;
        		case TXT: // 文本消息
//        			TextMessageBody txtBody = (TextMessageBody) message.getBody();
//        			digest = txtBody.getMessage();
        			digest = digest.replaceAll("\\[.{2,3}\\]", "[表情]");
        			break;
                }
    			return message.getStringAttribute("user_nick", "") + ": " + digest;
            }
            
            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return fromUsersNum + "个好友，发来了" + messageNum + "条消息";
            }
            
            @Override
            public Intent getLaunchIntent(EMMessage message) {
                //设置点击通知栏跳转事件
                Intent intent = new Intent(appContext, ChatActivity.class);
                    ChatType chatType = message.getChatType();
                    // 单聊信息
                    if (chatType == ChatType.Chat) { 
                        intent.putExtra("userName", message.getFrom());
                        intent.putExtra("userNick", message.getStringAttribute("user_nick", ""));
                        intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                    } 
                    
                return intent;
            }
        };
    }
    
    
    
    @Override
    protected void onConnectionConflict(){
        Intent intent = new Intent(appContext, MainTabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("conflict", true);
        appContext.startActivity(intent);
    }
    
    @Override
    protected void onCurrentAccountRemoved(){
    	Intent intent = new Intent(appContext, MainTabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ACCOUNT_REMOVED, true);
        appContext.startActivity(intent);
    }
    

    @Override
    protected HXSDKModel createModel() {
        return new YMHXSDKModel(appContext);
    }
    
    @Override
    public HXNotifier createNotifier(){
        return new HXNotifier(){
            public synchronized void onNewMsg(final EMMessage message) {
            	//判断是否是免打扰的消息
                if(EMChatManager.getInstance().isSlientMessage(message)){
                    return;
                }
                
                String chatUsename = null;
                List<String> notNotifyIds = null;
                // 获取设置的不提示新消息的用户或者群组ids
                   chatUsename = message.getTo();
                   notNotifyIds = ((YMHXSDKModel) hxModel).getDisabledIds();

                if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
                    // 判断app是否在后台
                    if (!EasyUtils.isAppRunningForeground(appContext)) {
                        EMLog.d(TAG, "应用程序运行在后台");
                        sendNotification(message, false);
                    } else {
                        sendNotification(message, true);
                    }
                    
                    viberateAndPlayTone(message);
                }
            }
        };
    }
    
    /**
     * get demo HX SDK Model
     */
    public YMHXSDKModel getModel(){
        return (YMHXSDKModel) hxModel;
    }
    
	public boolean isRobotMenuMessage(EMMessage message) {

		try {
			JSONObject jsonObj = message.getJSONObjectAttribute(Constant.MESSAGE_ATTR_ROBOT_MSGTYPE);
			if (jsonObj.has("choice")) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	public String getRobotMenuMessageDigest(EMMessage message) {
		String title = "";
		try {
			JSONObject jsonObj = message.getJSONObjectAttribute(Constant.MESSAGE_ATTR_ROBOT_MSGTYPE);
			if (jsonObj.has("choice")) {
				JSONObject jsonChoice = jsonObj.getJSONObject("choice");
				title = jsonChoice.getString("title");
			}
		} catch (Exception e) {
		}
		return title;
	}
	
	  /**
     * 获取内存中好友user list
     * @return
     */
//    public Map<String, User> getContactList() {
//        if (contactList == null) {
//            contactList = ((YMHXSDKModel) getModel()).getContactList();
//        }
//        
//        return contactList;
//    }
	
    /**
     * 设置好友user list到内存中
     * @param contactList
     */
//    public void setContactList(Map<String, User> contactList) {
//        this.contactList = contactList;
//    }
    
    @Override
    public void logout(final EMCallBack callback){
        endCall();
        super.logout(new EMCallBack(){

            @Override
            public void onSuccess() {
//                setContactList(null);
                getModel().closeDB();
                if(callback != null){
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(int code, String message) {
            }

            @Override
            public void onProgress(int progress, String status) {
                if(callback != null){
                    callback.onProgress(progress, status);
                }
            }
            
        });
    }   
    
    void endCall(){
        try {
            EMChatManager.getInstance().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
