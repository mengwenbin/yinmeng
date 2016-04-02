package com.xiaoxu.music.community.dao;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Unique;

/**
 * old
 * @author ice
 *
 */
public class CacheInfo {
	
	@Id(column = "cache_id")
	@NoAutoIncrement
	private int cache_id;
	
	@Column(column = "cache_name")
	private String cache_name;
	
	@Column(column = "cache_data")
	private String cache_data;// 保存路径
	
	@Column(column = "cache_time")
	private long cache_time;// 保存路径
	
	public CacheInfo() {
		super();
	}

	public CacheInfo(int cache_id, String cache_name, String cache_data,
			long cache_time) {
		super();
		this.cache_id = cache_id;
		this.cache_name = cache_name;
		this.cache_data = cache_data;
		this.cache_time = cache_time;
	}



	public int getCache_id() {
		return cache_id;
	}

	public void setCache_id(int cache_id) {
		this.cache_id = cache_id;
	}

	public String getCache_name() {
		return cache_name;
	}

	public void setCache_name(String cache_name) {
		this.cache_name = cache_name;
	}

	public String getCache_data() {
		return cache_data;
	}

	public void setCache_data(String cache_data) {
		this.cache_data = cache_data;
	}

	public long getCache_time() {
		return cache_time;
	}

	public void setCache_time(long cache_time) {
		this.cache_time = cache_time;
	}

	@Override
	public String toString() {
		return "CacheInfo [cache_id=" + cache_id + ", cache_name=" + cache_name
				+ ", cache_data=" + cache_data + ", cache_time=" + cache_time
				+ "]";
	}
	
	
	
}
