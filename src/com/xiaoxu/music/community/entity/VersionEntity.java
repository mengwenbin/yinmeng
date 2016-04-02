package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class VersionEntity implements Serializable {
	
	private String versionName;
	private String versionCode;
	private String updateMust;
	private String url;
	private String app_name;
	
	public VersionEntity(String versionName, String versionCode,
			String updateMust, String url, String app_name) {
		super();
		this.versionName = versionName;
		this.versionCode = versionCode;
		this.updateMust = updateMust;
		this.url = url;
		this.app_name = app_name;
	}
	
	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getUpdateMust() {
		return updateMust;
	}

	public void setUpdateMust(String updateMust) {
		this.updateMust = updateMust;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	@Override
	public String toString() {
		return "VersionEntity [versionName=" + versionName + ", versionCode="
				+ versionCode + ", updateMust=" + updateMust + ", url=" + url
				+ ", app_name=" + app_name + "]";
	}
	
}
