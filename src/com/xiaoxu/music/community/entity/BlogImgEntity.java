package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class BlogImgEntity implements Serializable{
	
	private String info;
	private ImageEntity attachment;
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public ImageEntity getAttachment() {
		return attachment;
	}
	public void setAttachment(ImageEntity attachment) {
		this.attachment = attachment;
	}
	
	@Override
	public String toString() {
		return "PostImageCallEntity [info=" + info + ", attachment="
				+ attachment + "]";
	}
	
	

}
