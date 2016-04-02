package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class SongMenuEntity implements Serializable {

	private boolean isCheck = false;
	private String favorite_id;
	private String favorite_category;
	private String favorite_name;
	private String time_create;
	private SongMenuInfoEntity musiclist_info;
	
	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getFavorite_id() {
		return favorite_id;
	}

	public void setFavorite_id(String favorite_id) {
		this.favorite_id = favorite_id;
	}

	public String getFavorite_category() {
		return favorite_category;
	}

	public void setFavorite_category(String favorite_category) {
		this.favorite_category = favorite_category;
	}

	public String getFavorite_name() {
		return favorite_name;
	}

	public void setFavorite_name(String favorite_name) {
		this.favorite_name = favorite_name;
	}

	public String getTime_create() {
		return time_create;
	}

	public void setTime_create(String time_create) {
		this.time_create = time_create;
	}

	public SongMenuInfoEntity getMusiclist_info() {
		return musiclist_info;
	}

	public void setMusiclist_info(SongMenuInfoEntity musiclist_info) {
		this.musiclist_info = musiclist_info;
	}

	public SongMenuEntity() {
		super();
	}

	@Override
	public String toString() {
		return "SongMenuEntity [favorite_id=" + favorite_id
				+ ", favorite_category=" + favorite_category
				+ ", favorite_name=" + favorite_name + ", time_create="
				+ time_create + ", musiclist_info=" + musiclist_info + "]";
	}

}
