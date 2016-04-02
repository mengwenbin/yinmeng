package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class BarrageInfoEntity implements Serializable {

	private String barrage_id;
	private String barrage_body;
	private String time_current;
	private String ip_create;
	private BarrageUserEntity user;

	public String getBarrage_id() {
		return barrage_id;
	}

	public void setBarrage_id(String barrage_id) {
		this.barrage_id = barrage_id;
	}

	public String getBarrage_body() {
		return barrage_body;
	}

	public void setBarrage_body(String barrage_body) {
		this.barrage_body = barrage_body;
	}

	public String getTime_current() {
		return time_current;
	}

	public void setTime_current(String time_current) {
		this.time_current = time_current;
	}

	public String getIp_create() {
		return ip_create;
	}

	public void setIp_create(String ip_create) {
		this.ip_create = ip_create;
	}

	public BarrageUserEntity getUser() {
		return user;
	}

	public void setUser(BarrageUserEntity user) {
		this.user = user;
	}

	public BarrageInfoEntity() {
		super();
	}

	@Override
	public String toString() {
		return "BarrageInfoEntity [barrage_id=" + barrage_id
				+ ", barrage_body=" + barrage_body + ", time_current="
				+ time_current + ", ip_create=" + ip_create + ", user=" + user
				+ "]";
	}

}
