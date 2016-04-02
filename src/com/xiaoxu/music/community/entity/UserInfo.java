package com.xiaoxu.music.community.entity;


public class UserInfo {

	private String header;
	private String avatar;
	private String usernick;
	private String username;
	private String lastmsg;
	private long time;

	public UserInfo() {
	}

	public String getLastmsg() {
		return lastmsg;
	}

	public void setLastmsg(String lastmsg) {
		this.lastmsg = lastmsg;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public UserInfo(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsernick() {
		return usernick;
	}

	public void setUsernick(String usernick) {
		this.usernick = usernick;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserInfo(String avatar,String usernick, String username, String lastmsg) {
		super();
		this.avatar = avatar;
		this.usernick = usernick;
		this.username = username;
		this.lastmsg = lastmsg;
	}

	@Override
	public String toString() {
		return "User [avatar=" + avatar
				+ ", usernick=" + usernick + ", username=" + username
				+ ", lastmsg=" + lastmsg + ", time=" + time + "]";
	}

}
