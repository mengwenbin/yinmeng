package com.xiaoxu.music.community;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import cn.jpush.android.api.JPushInterface;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMConversation.EMConversationType;
import com.umeng.update.UmengUpdateAgent;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.entity.UserInfo;
import com.xiaoxu.music.community.im.applib.controller.YMHXSDKHelper;
import com.xiaoxu.music.community.im.applib.db.UserDao;
import com.xiaoxu.music.community.im.applib.utils.UserUtils;
import com.xiaoxu.music.community.service.MediaBinder;
import com.xiaoxu.music.community.service.MediaPlayerService;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.PushSettingUtils;

public class YmApplication extends Application {
	
	public Context app_context;
	private List<SongInfoEntity> play_queue,play_history;//播放队列
	private SongInfoEntity currentMusic;//播放队列
	private int current_index = 0;//当前播放的索引
	
	//歌单加载更多
	public int by_task = -1;// -1:非播放全部   1:通过歌单   2：通过类别
	public String by_params = "-1";//请求的参数：歌单id || 类别id
	public int pageNum = 2;
	private MediaBinder binder = new MediaBinder();
	
	private String playPath;//播放的路径
	
	private static YmApplication instance;
	public static YMHXSDKHelper hxSDKHelper = new YMHXSDKHelper();
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		instance = this;
		app_context = getApplicationContext();
		JPushInterface.init(this);// 初始化 JPush
		if(!PushSettingUtils.getPushState(this)){
			JPushInterface.stopPush(getApplicationContext());
		}
		
		//设置增量更新
		UmengUpdateAgent.setDeltaUpdate(true);
		UmengUpdateAgent.setUpdateAutoPopup(true);

        /**
         * 该函数将初始化HuanXin SDK
         */
        hxSDKHelper.onInit(app_context);
		play_queue = new ArrayList<SongInfoEntity>();
		play_history = new ArrayList<SongInfoEntity>();
	}
	
	public String getPlayPath() {
		return playPath;
	}
	
	public void setPlayPath(String playPath) {
		this.playPath = playPath;
	}
	
	public MediaBinder getBinder() {
		return binder;
	}

	public List<SongInfoEntity> getPlay_history() {
		return play_history;
	}
	public void setPlay_history(List<SongInfoEntity> play_history) {
		this.play_history = play_history;
	}

	public Context getApp_context() {
		return app_context;
	}

	public int getCurrent_index() {
		return current_index;
	}

	public void setCurrent_index(int current_index) {
		this.current_index = current_index;
	}
	
	public SongInfoEntity getCurrentMusic() {
		return currentMusic;
	}
	
	public void setCurrentMusic(SongInfoEntity currentMusic) {
		this.currentMusic = currentMusic;
	}

	public List<SongInfoEntity> getPlay_queue() {
		return play_queue;
	}
	
	/**
	 * 添加歌单
	 * @param play_queue
	 */
	public void setSongMenu(List<SongInfoEntity> play_queue) {
		this.play_queue = play_queue;
		updatePlayQueue();
	}
	
	public void setSongMenu(List<SongInfoEntity> play_queue, int type,int pageNum,String id) {
		this.play_queue = play_queue;
		this.by_task = type;
		this.by_params = id;
		this.pageNum = pageNum;
		updatePlayQueue();
	}
	
	/**
	 * 
	 * @param songInfo
	 * @param id
	 */
	public void addSingleMusic(SongInfoEntity songInfo, String songmenu_id){
		if(!by_params.equals(songmenu_id)){
			play_queue = null;
			play_queue = new ArrayList<SongInfoEntity>();
		}
		this.by_params = songmenu_id;
		if (play_queue != null && play_queue.size() > 0) {
			for (int j = play_queue.size() - 1; j >= 0; j--) {// 旧数据
				int old_id = Integer.parseInt(play_queue.get(j).getMusic_id());
				int new_id = Integer.parseInt(songInfo.getMusic_id());
				if (old_id == new_id) {
					play_queue.remove(j);
					break;
				}
			}
		}
		play_queue.add(songInfo);
		updatePlayQueue();
	}
	
	/**
	 * 添加单曲
	 * @param songInfo
	 */
	public void addSingleMusic(SongInfoEntity songInfo){
		if (play_queue != null && play_queue.size() > 0) {
			for (int j = play_queue.size() - 1; j >= 0; j--) {// 旧数据
				int old_id = Integer.parseInt(play_queue.get(j).getMusic_id());
				int new_id = Integer.parseInt(songInfo.getMusic_id());
				if (old_id == new_id) {
					play_queue.remove(j);
					break;
				}
			}
		}
		play_queue.add(songInfo);
		updatePlayQueue();
	}
	
	/**
	 * 移除歌曲 【通过SongInfoEntity对象】
	 * @param songInfo
	 */
	public void removeMusicByObj(SongInfoEntity songInfo){
		play_queue.remove(songInfo);
		updatePlayQueue();
	}
	
	/**
	 * 移除歌曲 【通过index 】
	 * @param int
	 */
	public void removeMusicByIndex(int index){
		play_queue.remove(index);
		updatePlayQueue();
	}
	
	/**
	 * 修改播放队列中的某一首歌
	 * @param songInfo
	 */
	public void updateSingleMusicFromPlayQueue(SongInfoEntity songInfo){
		if (play_queue != null && play_queue.size() > 0) {
			for (int j = play_queue.size() - 1; j >= 0; j--) {// 旧数据
				int old_id = Integer.parseInt(play_queue.get(j).getMusic_id());
				int new_id = Integer.parseInt(songInfo.getMusic_id());
				if (old_id == new_id) {
					play_queue.set(j, songInfo);
					if(currentMusic!=null && currentMusic.getMusic_id()
							.equals(songInfo.getMusic_id())){
						setCurrentMusic(songInfo);
					}
					break;
				}
			}
		}
		updatePlayQueue();
	}
	
	/* **********************  sendBroadCast  ********************* */
	
	/**
	 * 通知更新播放队列
	 */
	public void updatePlayQueue(){
		Intent intent = new Intent(MediaPlayerService.UPDATE_QUEUE);
		sendBroadcast(intent);
	}
	
	/**
	 * 播放歌曲
	 */
	public void playStart(int current_index){
		if(current_index<0){
			current_index = 0;
		}
		setCurrent_index(current_index);
		Intent in = new Intent(MediaPlayerService.PLAY_CONTROL);
		in.putExtra(MediaPlayerService.KEY_CONTROL, MediaPlayerService.CONTROL_START);
		in.putExtra(MediaPlayerService.KEY_CURRENT_INDEX, current_index);
		sendBroadcast(in);
	}
	
	/**
	 * 播放歌曲
	 */
	public void playStart(SongInfoEntity info){
		Intent in = new Intent(MediaPlayerService.PLAY_CONTROL);
		in.putExtra(MediaPlayerService.KEY_CONTROL, MediaPlayerService.CONTROL_START);
		in.putExtra(MediaPlayerService.KEY_CURRENT_INDEX, seekPosInListById(play_queue,info.getMusic_id()));
		sendBroadcast(in);
	}
	
	/**
	 * 根据歌曲的ID，寻找出歌曲在当前播放列表中的位置
	 * @param list
	 * @param id
	 * @return
	 */
	public int seekPosInListById(List<SongInfoEntity> play_queue, String music_id) {
		int id = Integer.parseInt(music_id);
		if(id == -1)
			return -1;
		int result = -1;
		if (play_queue != null) {
			for (int i = play_queue.size()-1; i >= 0 ; i--) {
				if (id == Integer.parseInt(play_queue.get(i).getMusic_id())) {
					result = i;
					break;
				}
			}
		}
		return result;
	}
	/* **************************************/

	public void setBy_task(int by_task) {
		this.by_task = by_task;
	}

	public void setBy_params(String by_params) {
		this.by_params = by_params;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	
	
	public static YmApplication getInstance() {
		return instance;
	}
 
	/**
	 * 获取内存中好友user list
	 */
//	public Map<String, User> getContactList() {
//	    return hxSDKHelper.getContactList();
//	}

	/**
	 * 设置好友user list到内存中
	 */
//	public void setContactList(Map<String, User> contactList) {
//	    hxSDKHelper.setContactList(contactList);
//	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
	    hxSDKHelper.logout(emCallBack);
	}
	
	public boolean isLogined(){
		return EMChat.getInstance().isLoggedIn();
	}
	
	
	public void SaveStranger(List<EMMessage> messages){
		
		for (int i = 0; i < messages.size(); i++) {
			EMMessage message = messages.get(i);
			String username = message.getFrom();
//			User user = new UserDao(app_context).getStrangerUser(username);
//			if(user != null){
//				continue;
//			}
			EMConversation conversation = EMChatManager.getInstance().getConversationByType(username,EMConversationType.Chat);
			String lastmsg = ActivityUtil.getMessageDigest(conversation.getLastMessage(),app_context);
			String usernick = message.getStringAttribute("user_nick", "");
			String user_img = message.getStringAttribute("user_img", "");
			UserUtils.SaveUserInfo(app_context, new UserInfo(user_img, usernick, username, lastmsg));
		}
	}
	
	public void SaveStrangerInfo(EMMessage message){
		
		String username = message.getFrom();
//		User user = new UserDao(app_context).getStrangerUser(username);
//		if(user != null){
//			return;
//		}
		EMConversation conversation = EMChatManager.getInstance().getConversationByType(username,EMConversationType.Chat);
		String lastmsg = ActivityUtil.getMessageDigest(conversation.getLastMessage(),app_context);
		String usernick = message.getStringAttribute("user_nick", "");
		String user_img = message.getStringAttribute("user_img", "");
		UserUtils.SaveUserInfo(app_context, new UserInfo(user_img, usernick, username, lastmsg));
	}
	
}
