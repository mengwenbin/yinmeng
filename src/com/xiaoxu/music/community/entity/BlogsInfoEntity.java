package com.xiaoxu.music.community.entity;

import java.io.Serializable;
import java.util.List;

public class BlogsInfoEntity implements Serializable {
	
	private String tid;
	private String gid;
	private String parent_tid;
	private String user_id;
	private String blog_author;
	private String blog_message;
	private String blog_category;
	private String time_create;
	private String time_last;
	private String digest;
	private String recommend_add;
	private String heats;
	private String replies;
	private String praise;
	private List<ImageEntity> img_attachment;
	private BlogAuthorEntity author_info;
	private List<BlogsInfoEntity> list_reply2;
	
	public BlogsInfoEntity(String tid) {
		super();
		this.tid = tid;
	}

	public BlogsInfoEntity(String tid, String gid, String parent_tid,
			String user_id, String blog_author, String blog_message,
			String blog_category, String time_create, String time_last,
			String digest, String recommend_add, String heats, String replies,
			String praise, List<ImageEntity> img_attachment,
			BlogAuthorEntity author_info, List<BlogsInfoEntity> list_reply2) {
		super();
		this.tid = tid;
		this.gid = gid;
		this.parent_tid = parent_tid;
		this.user_id = user_id;
		this.blog_author = blog_author;
		this.blog_message = blog_message;
		this.blog_category = blog_category;
		this.time_create = time_create;
		this.time_last = time_last;
		this.digest = digest;
		this.recommend_add = recommend_add;
		this.heats = heats;
		this.replies = replies;
		this.praise = praise;
		this.img_attachment = img_attachment;
		this.author_info = author_info;
		this.list_reply2 = list_reply2;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getParent_tid() {
		return parent_tid;
	}

	public void setParent_tid(String parent_tid) {
		this.parent_tid = parent_tid;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBlog_author() {
		return blog_author;
	}

	public void setBlog_author(String blog_author) {
		this.blog_author = blog_author;
	}

	public String getBlog_message() {
		return blog_message;
	}

	public void setBlog_message(String blog_message) {
		this.blog_message = blog_message;
	}

	public String getBlog_category() {
		return blog_category;
	}

	public void setBlog_category(String blog_category) {
		this.blog_category = blog_category;
	}

	public String getTime_create() {
		return time_create;
	}

	public void setTime_create(String time_create) {
		this.time_create = time_create;
	}

	public String getTime_last() {
		return time_last;
	}

	public void setTime_last(String time_last) {
		this.time_last = time_last;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getRecommend_add() {
		return recommend_add;
	}

	public void setRecommend_add(String recommend_add) {
		this.recommend_add = recommend_add;
	}

	public String getHeats() {
		return heats;
	}

	public void setHeats(String heats) {
		this.heats = heats;
	}

	public String getReplies() {
		return replies;
	}

	public void setReplies(String replies) {
		this.replies = replies;
	}

	public String getPraise() {
		return praise;
	}

	public void setPraise(String praise) {
		this.praise = praise;
	}

	public List<ImageEntity> getImg_attachment() {
		return img_attachment;
	}

	public void setImg_attachment(List<ImageEntity> img_attachment) {
		this.img_attachment = img_attachment;
	}

	public BlogAuthorEntity getAuthor_info() {
		return author_info;
	}

	public void setAuthor_info(BlogAuthorEntity author_info) {
		this.author_info = author_info;
	}

	public List<BlogsInfoEntity> getList_reply2() {
		return list_reply2;
	}

	public void setList_reply2(List<BlogsInfoEntity> list_reply2) {
		this.list_reply2 = list_reply2;
	}

	@Override
	public String toString() {
		return "BlogsInfoEntity [tid=" + tid + ", gid=" + gid + ", parent_tid="
				+ parent_tid + ", user_id=" + user_id + ", blog_author="
				+ blog_author + ", blog_message=" + blog_message
				+ ", blog_category=" + blog_category + ", time_create="
				+ time_create + ", time_last=" + time_last + ", digest="
				+ digest + ", recommend_add=" + recommend_add + ", heats="
				+ heats + ", replies=" + replies + ", praise=" + praise
				+ ", img_attachment=" + img_attachment + ", author_info="
				+ author_info + ", list_reply2=" + list_reply2 + "]";
	}

}
