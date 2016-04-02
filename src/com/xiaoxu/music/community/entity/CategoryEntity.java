package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import java.util.List;

public class CategoryEntity implements Serializable{
	
	private List<CategorysInfoEntity> category_list;
	
	public CategoryEntity(List<CategorysInfoEntity> category_list) {
		super();
		this.category_list = category_list;
	}
	
	public List<CategorysInfoEntity> getCategory_list() {
		return category_list;
	}

	public void setCategory_list(List<CategorysInfoEntity> category_list) {
		this.category_list = category_list;
	}

	@Override
	public String toString() {
		return "CategoryEntity [category_list=" + category_list + "]";
	}
	
}
