package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class MusicUserDetailEntity implements Serializable {
	
	private MusicUserInfoEntity user;
	
	public MusicUserDetailEntity() {
		super();
	}

	public MusicUserDetailEntity(MusicUserInfoEntity user) {
		super();
		this.user = user;
	}

	public MusicUserInfoEntity getUser() {
		return user;
	}

	public void setUser(MusicUserInfoEntity user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "MusicUserDetailEntity [user=" + user + "]";
	}
	
	
}
