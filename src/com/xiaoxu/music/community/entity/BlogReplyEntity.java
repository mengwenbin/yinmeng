package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class BlogReplyEntity implements Serializable {
	
	private BlogsInfoEntity blogThread;

	public BlogReplyEntity(BlogsInfoEntity blogThread) {
		super();
		this.blogThread = blogThread;
	}

	public BlogReplyEntity() {
		super();
	}

	public BlogsInfoEntity getBlogThread() {
		return blogThread;
	}

	public void setBlogThread(BlogsInfoEntity blogThread) {
		this.blogThread = blogThread;
	}

	@Override
	public String toString() {
		return "BlogReplyEntity [blogThread=" + blogThread + "]";
	}
	
	

}
