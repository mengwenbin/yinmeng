package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import java.util.List;

public class SongMenuListEntity implements Serializable{
	
	private String list_size;
	private List<SongMenuInfoEntity> list_musiclist;
	
	public List<SongMenuInfoEntity> getList_musiclist() {
		return list_musiclist;
	}
	
	public void setList_musiclist(List<SongMenuInfoEntity> list_musiclist) {
		this.list_musiclist = list_musiclist;
	}

	public String getList_size() {
		return list_size;
	}

	public void setList_size(String list_size) {
		this.list_size = list_size;
	}

	@Override
	public String toString() {
		return "SongMenuListEntity [list_size=" + list_size
				+ ", list_musiclist=" + list_musiclist + "]";
	}
	
	
	
}
