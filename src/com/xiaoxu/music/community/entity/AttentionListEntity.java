package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import java.util.List;

public class AttentionListEntity implements Serializable {

	private String list_size;
	private List<MusicUserInfoEntity> list_attention;

	public String getList_size() {
		return list_size;
	}

	public void setList_size(String list_size) {
		this.list_size = list_size;
	}

	public List<MusicUserInfoEntity> getList_attention() {
		return list_attention;
	}

	public void setList_attention(List<MusicUserInfoEntity> list_attention) {
		this.list_attention = list_attention;
	}

	public AttentionListEntity() {
		super();
	}

	@Override
	public String toString() {
		return "AttentionListEntity [list_size=" + list_size
				+ ", list_attention=" + list_attention + "]";
	}

}
