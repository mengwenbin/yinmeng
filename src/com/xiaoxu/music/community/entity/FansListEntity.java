package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import java.util.List;

public class FansListEntity implements Serializable {
	private String list_size;
	private List<MusicUserInfoEntity> list_fans;

	public String getList_size() {
		return list_size;
	}

	public void setList_size(String list_size) {
		this.list_size = list_size;
	}

	public List<MusicUserInfoEntity> getList_fans() {
		return list_fans;
	}

	public void setList_fans(List<MusicUserInfoEntity> list_fans) {
		this.list_fans = list_fans;
	}

	public FansListEntity(String list_size, List<MusicUserInfoEntity> list_fans) {
		super();
		this.list_size = list_size;
		this.list_fans = list_fans;
	}

	@Override
	public String toString() {
		return "FansListEntity [list_size=" + list_size + "]";
	}

}
