package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class FilePathEntity implements Serializable {

	private String musicfile_path;
	private String imagefile_path;

	public String getMusicfile_path() {
		return musicfile_path;
	}

	public void setMusicfile_path(String musicfile_path) {
		this.musicfile_path = musicfile_path;
	}

	public String getImagefile_path() {
		return imagefile_path;
	}

	public void setImagefile_path(String imagefile_path) {
		this.imagefile_path = imagefile_path;
	}

	public FilePathEntity() {
		super();
	}

	@Override
	public String toString() {
		return "FilePathEntity [musicfile_path=" + musicfile_path
				+ ", imagefile_path=" + imagefile_path + "]";
	}

}
