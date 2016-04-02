package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import java.util.List;

public class MusicRecommendEntity implements Serializable {
	
	private List<SongInfoEntity> list_music51;
	private List<SongInfoEntity> list_music52;
	private List<SongMenuInfoEntity> list_musiclist;
	
	public MusicRecommendEntity() {
		super();
	}

	public MusicRecommendEntity(List<SongInfoEntity> list_music51,
			List<SongInfoEntity> list_music52,
			List<SongMenuInfoEntity> list_musiclist) {
		super();
		this.list_music51 = list_music51;
		this.list_music52 = list_music52;
		this.list_musiclist = list_musiclist;
	}

	public List<SongInfoEntity> getList_music51() {
		return list_music51;
	}

	public void setList_music51(List<SongInfoEntity> list_music51) {
		this.list_music51 = list_music51;
	}

	public List<SongInfoEntity> getList_music52() {
		return list_music52;
	}

	public void setList_music52(List<SongInfoEntity> list_music52) {
		this.list_music52 = list_music52;
	}

	public List<SongMenuInfoEntity> getList_musiclist() {
		return list_musiclist;
	}

	public void setList_musiclist(List<SongMenuInfoEntity> list_musiclist) {
		this.list_musiclist = list_musiclist;
	}

	@Override
	public String toString() {
		return "MusicRecommendEntity [list_music51=" + list_music51
				+ ", list_music52=" + list_music52 + ", list_musiclist="
				+ list_musiclist + "]";
	}

}
