package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class AdvertisementInfoEntity implements Serializable {
	
	private String res_id;
	private String res_title;
	private String res_url;
	private String res_img;
	private String slistby;
	private String width_height;
	
	public AdvertisementInfoEntity() {
		super();
	}

	public AdvertisementInfoEntity(String res_id, String res_title,
			String res_url, String res_img, String slistby) {
		super();
		this.res_id = res_id;
		this.res_title = res_title;
		this.res_url = res_url;
		this.res_img = res_img;
		this.slistby = slistby;
	}
	
	public String getWidth_height() {
		return width_height;
	}
	
	public void setWidth_height(String width_height) {
		this.width_height = width_height;
	}
	
	public String getRes_id() {
		return res_id;
	}
	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}
	public String getRes_title() {
		return res_title;
	}
	public void setRes_title(String res_title) {
		this.res_title = res_title;
	}
	public String getRes_url() {
		return res_url;
	}
	public void setRes_url(String res_url) {
		this.res_url = res_url;
	}
	public String getRes_img() {
		return res_img;
	}
	public void setRes_img(String res_img) {
		this.res_img = res_img;
	}
	public String getSlistby() {
		return slistby;
	}
	public void setSlistby(String slistby) {
		this.slistby = slistby;
	}
	
	@Override
	public String toString() {
		return "AdvertisementInfoEntity [res_id=" + res_id + ", res_title="
				+ res_title + ", res_url=" + res_url + ", res_img=" + res_img
				+ ", slistby=" + slistby + "]";
	}
	
}
