package com.xiaoxu.music.community.im.applib.model;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.util.EasyUtils;
import com.xiaoxu.music.community.im.applib.controller.HXSDKHelper;

/**
 * 新消息提醒class
 * 2.1.8把新消息提示相关的api移除出sdk，方便开发者自由修改
 * 开发者也可以继承此类实现相关的接口
 * 这个类被继承和实现相关的api
 */
public class HXNotifier {
    Ringtone ringtone = null;

    protected final static String[] msg_ch ={ "发来一条消息", "发来一张图片", "发来一段语音","发来一个文件","发来位置信息","%1个联系人发来%2条消息"};

    protected static int notifyID = 0525; // start notification id
    protected static int foregroundNotifyID = 0555;

    protected NotificationManager notificationManager = null;

    protected HashSet<String> fromUsers = new HashSet<String>();
    protected int notificationNum = 0;

    protected Context appContext;
    protected String packageName;
    protected String[] msgs;
    protected long lastNotifiyTime;
    protected AudioManager audioManager;
    protected Vibrator vibrator;
    protected HXNotificationInfoProvider notificationInfoProvider;

    public HXNotifier() {
    }
    
    /**
     * 开发者可以重载此函数
     * 这个函数可以被覆盖
     * @param context
     * @return
     */
    public HXNotifier init(Context context){
        appContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        packageName = appContext.getApplicationInfo().packageName;
        if (Locale.getDefault().getLanguage().equals("zh")) {
            msgs = msg_ch;
        } 

        audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
        vibrator = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);
        return this;
    }
    
    /**
     * 开发者可以重载此函数
     * this function can be override
     */
    public void reset(){
        resetNotificationCount();
        cancelNotificaton();
    }

    void resetNotificationCount() {
        notificationNum = 0;
        fromUsers.clear();
    }
    
    void cancelNotificaton() {
        if (notificationManager != null)
            notificationManager.cancel(notifyID);
    }

    /**
     * 处理新收到的消息，然后发送通知
     * 开发者可以重载此函数
     * 这个函数可以被覆盖
     * @param message
     */
    public synchronized void onNewMsg(EMMessage message) {
        if(EMChatManager.getInstance().isSlientMessage(message)){
            return;
        }
        
        // 判断app是否在后台
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            sendNotification(message, false);
        } else {
            sendNotification(message, true);
        }
        
        viberateAndPlayTone(message);
    }
    
    public synchronized void onNewMesg(List<EMMessage> messages) {
        if(EMChatManager.getInstance().isSlientMessage(messages.get(messages.size()-1))){
            return;
        }
        // 判断app是否在后台
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            sendNotification(messages, false);
        } else {
            sendNotification(messages, true);
        }
        viberateAndPlayTone(messages.get(messages.size()-1));
    }

    /**
     * 发送通知栏提示
     * 这可以被子类覆盖提供客户实现
     * @param messages
     * @param isForeground
     */
    protected void sendNotification (List<EMMessage> messages, boolean isForeground){
        for(EMMessage message : messages){
            if(!isForeground){
                notificationNum++;
                fromUsers.add(message.getStringAttribute("user_nick", ""));
            }
        }
        sendNotification(messages.get(messages.size()-1), isForeground, false);
    }
    
    protected void sendNotification (EMMessage message, boolean isForeground){
        sendNotification(message, isForeground, true);
    }
    
    /**
     * 发送通知栏提示
     * 这可以被子类覆盖提供客户实现
     * @param message
     */
    @SuppressWarnings("incomplete-switch")
	protected void sendNotification(EMMessage message, boolean isForeground, boolean numIncrease) {
        String username = message.getStringAttribute("user_nick", "");
        try {
            String notifyText = username + " ";
            switch (message.getType()) {
            case TXT:
                notifyText += msgs[0];
                break;
            case IMAGE:
                notifyText += msgs[1];
                break;
            case VOICE:
                notifyText += msgs[2];
                break;
            case FILE:
                notifyText += msgs[3];
                break;
            case LOCATION:
            	notifyText += msgs[4];
            	break;
            }
            
            PackageManager packageManager = appContext.getPackageManager();
            String appname = (String) packageManager.getApplicationLabel(appContext.getApplicationInfo());
            
            // 通知titile
            String contentTitle = appname;
            if (notificationInfoProvider != null) {
                String customNotifyText = notificationInfoProvider.getDisplayedText(message);
                String customCotentTitle = notificationInfoProvider.getTitle(message);
                if (customNotifyText != null){
                    // 设置自定义的状态栏提示内容
                    notifyText = customNotifyText;
                }
                    
                if (customCotentTitle != null){
                    // 设置自定义的通知栏标题
                    contentTitle = customCotentTitle;
                }   
            }

            // 创建和发送notificaiton
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                                                                        .setSmallIcon(appContext.getApplicationInfo().icon)
                                                                        .setWhen(System.currentTimeMillis())
                                                                        .setAutoCancel(true);

            Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
            if (notificationInfoProvider != null) {
                // 设置自定义的notification点击跳转intent
                msgIntent = notificationInfoProvider.getLaunchIntent(message);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(appContext, notifyID, msgIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            if(numIncrease){
                // prepare latest event info section
                if(!isForeground){
                    notificationNum++;
                    fromUsers.add(message.getStringAttribute("user_nick", ""));
                }
            }

            int fromUsersNum = fromUsers.size();
            String summaryBody = msgs[5].replaceFirst("%1", Integer.toString(fromUsersNum)).replaceFirst("%2",Integer.toString(notificationNum));
            
            if (notificationInfoProvider != null) {
                // lastest text
                String customSummaryBody = notificationInfoProvider.getLatestText(message, fromUsersNum,notificationNum);
                if (customSummaryBody != null){
                    summaryBody = customSummaryBody;
                }
                
                // small icon
                int smallIcon = notificationInfoProvider.getSmallIcon(message);
                if (smallIcon != 0){
                    mBuilder.setSmallIcon(smallIcon);
                }
            }

            mBuilder.setContentTitle(contentTitle);
            mBuilder.setTicker(notifyText);
            mBuilder.setContentText(summaryBody);
            mBuilder.setContentIntent(pendingIntent);
            // mBuilder.setNumber(notificationNum);
            Notification notification = mBuilder.build();

            if (isForeground) {
                notificationManager.notify(foregroundNotifyID, notification);
                notificationManager.cancel(foregroundNotifyID);
            } else {
                notificationManager.notify(notifyID, notification);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手机震动和声音提示
     */
    public void viberateAndPlayTone(EMMessage message) {
        if(message != null){
            if(EMChatManager.getInstance().isSlientMessage(message)){
                return;
            } 
        }
        
        HXSDKModel model = HXSDKHelper.getInstance().getModel();
        if(!model.getSettingMsgNotification()){
            return;
        }
        
        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            // 2秒内收到新消息,跳过播放铃声
            return;
        }
        
        try {
            lastNotifiyTime = System.currentTimeMillis();
            
            // 判断是否处于静音模式
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                return;
            }
            
            if(model.getSettingMsgVibrate()){
                long[] pattern = new long[] { 0, 180, 80, 120 };
                vibrator.vibrate(pattern, -1);
            }

            if(model.getSettingMsgSound()){
                if (ringtone == null) {
                    Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    ringtone = RingtoneManager.getRingtone(appContext, notificationUri);
                    if (ringtone == null) {
                        return;
                    }
                }
                
                if (!ringtone.isPlaying()) {
                    String vendor = Build.MANUFACTURER;
                    
                    ringtone.play();
                    // 对于三星S3,我们遇到一个错误的电话继续铃声没有停止
                    //所以下面添加特殊处理程序停止后3s需要
                    if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                        Thread ctlThread = new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    if (ringtone.isPlaying()) {
                                        ringtone.stop();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        };
                        ctlThread.run();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置NotificationInfoProvider
     * @param provider
     */
    public void setNotificationInfoProvider(HXNotificationInfoProvider provider) {
        notificationInfoProvider = provider;
    }

    public interface HXNotificationInfoProvider {
        /**
         * 设置发送notification时状态栏提示新消息的内容(比如Xxx发来了一条图片消息)
         * @param message 接收到的消息
         * @return null为使用默认
         */
        String getDisplayedText(EMMessage message);

        /**
         * 设置notification持续显示的新消息提示(比如2个联系人发来了5条消息)
         * @param message 接收到的消息
         * @param fromUsersNum 发送人的数量
         * @param messageNum 消息数量
         * @return null为使用默认
         */
        String getLatestText(EMMessage message, int fromUsersNum, int messageNum);

        /**
         * 设置notification标题
         * @param message
         * @return null为使用默认
         */
        String getTitle(EMMessage message);

        /**
         * 设置小图标
         * @param message
         * @return 0使用默认图标
         */
        int getSmallIcon(EMMessage message);

        /**
         * 设置notification点击时的跳转intent
         * 显示在notification上最近的一条消息
         * @return null为使用默认
         */
        Intent getLaunchIntent(EMMessage message);
    }
}
