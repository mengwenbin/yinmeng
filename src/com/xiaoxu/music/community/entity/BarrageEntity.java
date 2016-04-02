package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import java.util.List;

public class BarrageEntity implements Serializable {

	private String list_size;
	private List<BarrageInfoEntity> list_barrage;

	public String getList_size() {
		return list_size;
	}

	public void setList_size(String list_size) {
		this.list_size = list_size;
	}

	public List<BarrageInfoEntity> getList_barrage() {
		return list_barrage;
	}

	public void setList_barrage(List<BarrageInfoEntity> list_barrage) {
		this.list_barrage = list_barrage;
	}

	public BarrageEntity(String list_size, List<BarrageInfoEntity> list_barrage) {
		super();
		this.list_size = list_size;
		this.list_barrage = list_barrage;
	}

	public BarrageEntity() {
		super();
	}

	@Override
	public String toString() {
		return "BarrageEntity [list_size=" + list_size + ", list_barrage="
				+ list_barrage + "]";
	}
}
