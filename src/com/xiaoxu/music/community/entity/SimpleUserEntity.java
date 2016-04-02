package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class SimpleUserEntity implements Serializable{
	
	private String user_nick;
	private String user_sex;
	private String user_img;
	private String is_attention;
	private String user_summary;
	
	public SimpleUserEntity(String user_nick, String user_sex, String user_img,
			String is_attention, String user_summary) {
		super();
		this.user_nick = user_nick;
		this.user_sex = user_sex;
		this.user_img = user_img;
		this.is_attention = is_attention;
		this.user_summary = user_summary;
	}
	
	public String getUser_nick() {
		return user_nick;
	}
	public void setUser_nick(String user_nick) {
		this.user_nick = user_nick;
	}
	public String getUser_sex() {
		return user_sex;
	}
	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}
	public String getUser_img() {
		return user_img;
	}
	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}
	public String getIs_attention() {
		return is_attention;
	}
	public void setIs_attention(String is_attention) {
		this.is_attention = is_attention;
	}

	public String getUser_summary() {
		return user_summary;
	}

	public void setUser_summary(String user_summary) {
		this.user_summary = user_summary;
	}

	@Override
	public String toString() {
		return "SimpleUserEntity [user_nick=" + user_nick + ", user_sex="
				+ user_sex + ", user_img=" + user_img + ", is_attention="
				+ is_attention + ", user_summary=" + user_summary + "]";
	}

}
