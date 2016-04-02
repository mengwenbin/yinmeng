package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class UserInfoEntity implements Serializable {

	private String token;
	private String user_id;
	private String username;
	private String user_nick;
	private String user_qq;
	private String user_star;
	private String user_wish;
	private String user_micblog;
	private String user_birthday;
	private String user_mobile;
	private String user_weixin;
	private String user_mail;
	private String user_sex;
	private String user_local;
	private String user_local2;
	private String user_img;
	private String user_glass;
	private String user_summary;
	private String time_reg;
	private String c_login;
	private String status;
	private String c_fans;
	private String c_thread;
	private String c_music;
	private String c_attention;
	private String imEaseMob_username;
	private String imEaseMob_password;

	public String getImEaseMob_username() {
		return imEaseMob_username;
	}

	public void setImEaseMob_username(String imEaseMob_username) {
		this.imEaseMob_username = imEaseMob_username;
	}

	public String getImEaseMob_password() {
		return imEaseMob_password;
	}

	public void setImEaseMob_password(String imEaseMob_password) {
		this.imEaseMob_password = imEaseMob_password;
	}

	public String getC_attention() {
		return c_attention;
	}

	public void setC_attention(String c_attention) {
		this.c_attention = c_attention;
	}

	public String getC_fans() {
		return c_fans;
	}

	public void setC_fans(String c_fans) {
		this.c_fans = c_fans;
	}

	public String getC_thread() {
		return c_thread;
	}

	public void setC_thread(String c_thread) {
		this.c_thread = c_thread;
	}

	public String getC_music() {
		return c_music;
	}

	public void setC_music(String c_music) {
		this.c_music = c_music;
	}

	public UserInfoEntity() {
		super();
	}

	public UserInfoEntity(String token, String user_id, String username,
			String user_nick, String user_qq, String user_star,
			String user_wish, String user_micblog, String user_birthday,
			String user_mobile, String user_weixin, String user_mail,
			String user_sex, String user_local, String user_local2,
			String user_img, String user_glass, String user_summary,
			String time_reg, String c_login, String status, String c_fans,
			String c_thread, String c_music, String c_attention,
			String imEaseMob_username, String imEaseMob_password) {
		super();
		this.token = token;
		this.user_id = user_id;
		this.username = username;
		this.user_nick = user_nick;
		this.user_qq = user_qq;
		this.user_star = user_star;
		this.user_wish = user_wish;
		this.user_micblog = user_micblog;
		this.user_birthday = user_birthday;
		this.user_mobile = user_mobile;
		this.user_weixin = user_weixin;
		this.user_mail = user_mail;
		this.user_sex = user_sex;
		this.user_local = user_local;
		this.user_local2 = user_local2;
		this.user_img = user_img;
		this.user_glass = user_glass;
		this.user_summary = user_summary;
		this.time_reg = time_reg;
		this.c_login = c_login;
		this.status = status;
		this.c_fans = c_fans;
		this.c_thread = c_thread;
		this.c_music = c_music;
		this.c_attention = c_attention;
		this.imEaseMob_username = imEaseMob_username;
		this.imEaseMob_password = imEaseMob_password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUser_nick() {
		return user_nick;
	}

	public void setUser_nick(String user_nick) {
		this.user_nick = user_nick;
	}

	public String getUser_qq() {
		return user_qq;
	}

	public void setUser_qq(String user_qq) {
		this.user_qq = user_qq;
	}

	public String getUser_star() {
		return user_star;
	}

	public void setUser_star(String user_star) {
		this.user_star = user_star;
	}

	public String getUser_wish() {
		return user_wish;
	}

	public void setUser_wish(String user_wish) {
		this.user_wish = user_wish;
	}

	public String getUser_micblog() {
		return user_micblog;
	}

	public void setUser_micblog(String user_micblog) {
		this.user_micblog = user_micblog;
	}

	public String getUser_birthday() {
		return user_birthday;
	}

	public void setUser_birthday(String user_birthday) {
		this.user_birthday = user_birthday;
	}

	public String getUser_mobile() {
		return user_mobile;
	}

	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}

	public String getUser_weixin() {
		return user_weixin;
	}

	public void setUser_weixin(String user_weixin) {
		this.user_weixin = user_weixin;
	}

	public String getUser_mail() {
		return user_mail;
	}

	public void setUser_mail(String user_mail) {
		this.user_mail = user_mail;
	}

	public String getUser_sex() {
		return user_sex;
	}

	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}

	public String getUser_local() {
		return user_local;
	}

	public void setUser_local(String user_local) {
		this.user_local = user_local;
	}

	public String getUser_local2() {
		return user_local2;
	}

	public void setUser_local2(String user_local2) {
		this.user_local2 = user_local2;
	}

	public String getUser_img() {
		return user_img;
	}

	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}

	public String getUser_glass() {
		return user_glass;
	}

	public void setUser_glass(String user_glass) {
		this.user_glass = user_glass;
	}

	public String getUser_summary() {
		return user_summary;
	}

	public void setUser_summary(String user_summary) {
		this.user_summary = user_summary;
	}

	public String getTime_reg() {
		return time_reg;
	}

	public void setTime_reg(String time_reg) {
		this.time_reg = time_reg;
	}

	public String getC_login() {
		return c_login;
	}

	public void setC_login(String c_login) {
		this.c_login = c_login;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserInfoEntity [token=" + token + ", user_id=" + user_id
				+ ", username=" + username + ", user_nick=" + user_nick
				+ ", user_qq=" + user_qq + ", user_star=" + user_star
				+ ", user_wish=" + user_wish + ", user_micblog=" + user_micblog
				+ ", user_birthday=" + user_birthday + ", user_mobile="
				+ user_mobile + ", user_weixin=" + user_weixin + ", user_mail="
				+ user_mail + ", user_sex=" + user_sex + ", user_local="
				+ user_local + ", user_local2=" + user_local2 + ", user_img="
				+ user_img + ", user_glass=" + user_glass + ", user_summary="
				+ user_summary + ", time_reg=" + time_reg + ", c_login="
				+ c_login + ", status=" + status + ", c_fans=" + c_fans
				+ ", c_thread=" + c_thread + ", c_music=" + c_music
				+ ", c_attention=" + c_attention + ", imEaseMob_username="
				+ imEaseMob_username + ", imEaseMob_password="
				+ imEaseMob_password + "]";
	}

}
