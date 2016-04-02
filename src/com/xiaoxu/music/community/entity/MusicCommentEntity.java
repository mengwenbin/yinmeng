package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import java.util.List;

public class MusicCommentEntity implements Serializable {
	
	private String list_size;
	private List<MusicCommentInfoEntity> list_comment;
	
	public MusicCommentEntity() {
		super();
	}
	
	public MusicCommentEntity(String list_size,
			List<MusicCommentInfoEntity> list_comment) {
		super();
		this.list_size = list_size;
		this.list_comment = list_comment;
	}
	
	public String getList_size() {
		return list_size;
	}
	public void setList_size(String list_size) {
		this.list_size = list_size;
	}
	public List<MusicCommentInfoEntity> getList_comment() {
		return list_comment;
	}
	public void setList_comment(List<MusicCommentInfoEntity> list_comment) {
		this.list_comment = list_comment;
	}
	
	@Override
	public String toString() {
		return "MusicCommentEntity [list_size=" + list_size + ", list_comment="
				+ list_comment + "]";
	}
	
	

}
