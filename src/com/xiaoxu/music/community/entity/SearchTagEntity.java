package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class SearchTagEntity implements Serializable{
	
	private String k;
	private String v;
	
	public SearchTagEntity(String k, String v) {
		super();
		this.k = k;
		this.v = v;
	}
	
	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	
	@Override
	public String toString() {
		return "SearchTagEntity [k=" + k + ", v=" + v + "]";
	}
	
}
