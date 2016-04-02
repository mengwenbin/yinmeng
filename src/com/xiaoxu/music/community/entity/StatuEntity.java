package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class StatuEntity implements Serializable {

	private String statu;

	public String getStatu() {
		return statu;
	}

	public void setStatu(String statu) {
		this.statu = statu;
	}

	public StatuEntity() {
		super();
	}

	@Override
	public String toString() {
		return "StatuEntity [statu=" + statu + "]";
	}

}
