package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import java.util.List;

public class MusicEntity implements Serializable {

	private String list_size;
	private List<SongMenuInfoEntity> list_musiclist;

	public String getList_size() {
		return list_size;
	}

	public void setList_size(String list_size) {
		this.list_size = list_size;
	}

	public List<SongMenuInfoEntity> getList_musiclist() {
		return list_musiclist;
	}

	public void setList_musiclist(List<SongMenuInfoEntity> list_musiclist) {
		this.list_musiclist = list_musiclist;
	}

	
	public MusicEntity(String list_size, List<SongMenuInfoEntity> list_musiclist) {
		super();
		this.list_size = list_size;
		this.list_musiclist = list_musiclist;
	}

	public MusicEntity() {
		super();
	}

	@Override
	public String toString() {
		return "MusicEntity [list_size=" + list_size + ", list_musiclist="
				+ list_musiclist + "]";
	}

}
