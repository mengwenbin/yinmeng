package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import java.util.List;

public class InfromEntity implements Serializable {

	private String list_size;
	private List<InfromInfoEntity> list_message;

	public String getList_size() {
		return list_size;
	}

	public void setList_size(String list_size) {
		this.list_size = list_size;
	}

	public List<InfromInfoEntity> getList_message() {
		return list_message;
	}

	public void setList_message(List<InfromInfoEntity> list_message) {
		this.list_message = list_message;
	}

	public InfromEntity() {
		super();
	}

	@Override
	public String toString() {
		return "InfromEntity [list_size=" + list_size + ", list_message="
				+ list_message + "]";
	}

}
