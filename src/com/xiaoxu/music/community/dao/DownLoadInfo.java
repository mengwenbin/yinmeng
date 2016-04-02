package com.xiaoxu.music.community.dao;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "download_music")
public class DownLoadInfo implements Serializable{
	
	@Id(column = "id")
	private int id;
	
	@Column(column = "music_id")
	private String music_id;
	
	@Column(column = "music_name")
	private String music_name;
	
	@Column(column = "music_category")
	private String music_category;// 类别
	
	@Column(column = "music_url")
	private String music_url;// 访问路径

	@Column(column = "music_path")
	private String music_path;// 保存路径
	
	@Column(column = "music_size")
	private String music_size;//大小
	
	@Column(column = "music_author")
	private String music_author;//作者

	@Column(column = "music_coverimg")
	private String music_coverimg;//封面图

	@Column(column = "music_lengthtime")
	private String music_lengthtime;// 时长度
	
	@Column(column = "music_singing")
	private String music_singing;//演唱
	
	@Column(column = "music_composer")
	private String music_composer;//作曲
	
	@Column(column = "music_lyricser")
	private String music_lyricser;//作词
	
	@Column(column = "music_lyrics")
	private String music_lyrics;//歌词
	
	@Column(column = "music_md5")
	private String music_md5;
	
	@Column(column = "music_progress")
	private String music_progress;//下载进度
	
	@Column(column = "isfinish")
	private boolean isfinish;//是否下载完毕
	
	private boolean checked = false;
	
	public DownLoadInfo() {
		super();
	}

	public DownLoadInfo(String music_id, String music_name,
			String music_category, String music_url, String music_path,
			String music_size, String music_author, String music_coverimg,
			String music_lengthtime, String music_singing,
			String music_composer, String music_lyricser, String music_lyrics,
			String music_md5, String music_progress, boolean isfinish) {
		super();
		this.music_id = music_id;
		this.music_name = music_name;
		this.music_category = music_category;
		this.music_url = music_url;
		this.music_path = music_path;
		this.music_size = music_size;
		this.music_author = music_author;
		this.music_coverimg = music_coverimg;
		this.music_lengthtime = music_lengthtime;
		this.music_singing = music_singing;
		this.music_composer = music_composer;
		this.music_lyricser = music_lyricser;
		this.music_lyrics = music_lyrics;
		this.music_md5 = music_md5;
		this.music_progress = music_progress;
		this.isfinish = isfinish;
	}
	
	public String getMusic_md5() {
		return music_md5;
	}
	
	public void setMusic_md5(String music_md5) {
		this.music_md5 = music_md5;
	}

	public String getMusic_progress() {
		return music_progress;
	}

	public void setMusic_progress(String music_progress) {
		this.music_progress = music_progress;
	}

	public String getMusic_id() {
		return music_id;
	}

	public void setMusic_id(String music_id) {
		this.music_id = music_id;
	}

	public String getMusic_name() {
		return music_name;
	}

	public void setMusic_name(String music_name) {
		this.music_name = music_name;
	}

	public String getMusic_url() {
		return music_url;
	}

	public void setMusic_url(String music_url) {
		this.music_url = music_url;
	}

	public String getMusic_path() {
		return music_path;
	}

	public void setMusic_path(String music_path) {
		this.music_path = music_path;
	}

	public String getMusic_size() {
		return music_size;
	}

	public void setMusic_size(String music_size) {
		this.music_size = music_size;
	}

	public String getMusic_author() {
		return music_author;
	}

	public void setMusic_author(String music_author) {
		this.music_author = music_author;
	}

	public String getMusic_coverimg() {
		return music_coverimg;
	}

	public void setMusic_coverimg(String music_coverimg) {
		this.music_coverimg = music_coverimg;
	}

	public boolean isIsfinish() {
		return isfinish;
	}

	public void setIsfinish(boolean isfinish) {
		this.isfinish = isfinish;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
}
