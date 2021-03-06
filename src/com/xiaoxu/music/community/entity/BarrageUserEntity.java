package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class BarrageUserEntity implements Serializable {

	private String user_nick;
	private String user_sex;
	private String user_img;

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

	public BarrageUserEntity() {
		super();
	}

	public BarrageUserEntity(String user_nick, String user_sex, String user_img) {
		super();
		this.user_nick = user_nick;
		this.user_sex = user_sex;
		this.user_img = user_img;
	}

	@Override
	public String toString() {
		return "BarrageUserEntity [user_nick=" + user_nick + ", user_sex="
				+ user_sex + ", user_img=" + user_img + "]";
	}

}
