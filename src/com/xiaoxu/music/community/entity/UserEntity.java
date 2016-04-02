package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class UserEntity implements Serializable{
	
	private UserInfoEntity user;
	
	public UserEntity(UserInfoEntity user) {
		super();
		this.user = user;
	}

	public UserInfoEntity getUser() {
		return user;
	}

	public void setUser(UserInfoEntity user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "UserEntity [user=" + user + "]";
	}
	
}
