package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class MusicDetailEntity implements Serializable {
	
	private SongInfoEntity music;

	public MusicDetailEntity(SongInfoEntity music) {
		super();
		this.music = music;
	}

	public MusicDetailEntity() {
		super();
	}

	public SongInfoEntity getMusic() {
		return music;
	}

	public void setMusic(SongInfoEntity music) {
		this.music = music;
	}

	@Override
	public String toString() {
		return "MusicDetailEntity [music=" + music + "]";
	}
	
	

}
