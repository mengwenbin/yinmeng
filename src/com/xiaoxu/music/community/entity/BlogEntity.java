package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import java.util.List;

public class BlogEntity implements Serializable {
	
	private List<BlogsInfoEntity> list_blogThread;
	private List<BlogsInfoEntity> list_blogThread_top;
	private String count_blogThread;
	
	public BlogEntity() {
		super();
	}

	public BlogEntity(List<BlogsInfoEntity> list_blogThread,
			List<BlogsInfoEntity> list_blogThread_top, String count_blogThread) {
		super();
		this.list_blogThread = list_blogThread;
		this.list_blogThread_top = list_blogThread_top;
		this.count_blogThread = count_blogThread;
	}

	public List<BlogsInfoEntity> getList_blogThread() {
		return list_blogThread;
	}

	public void setList_blogThread(List<BlogsInfoEntity> list_blogThread) {
		this.list_blogThread = list_blogThread;
	}

	public List<BlogsInfoEntity> getList_blogThread_top() {
		return list_blogThread_top;
	}

	public void setList_blogThread_top(List<BlogsInfoEntity> list_blogThread_top) {
		this.list_blogThread_top = list_blogThread_top;
	}

	public String getCount_blogThread() {
		return count_blogThread;
	}

	public void setCount_blogThread(String count_blogThread) {
		this.count_blogThread = count_blogThread;
	}

	@Override
	public String toString() {
		return "BlogEntity [list_blogThread=" + list_blogThread
				+ ", list_blogThread_top=" + list_blogThread_top
				+ ", count_blogThread=" + count_blogThread + "]";
	}
	
}
