package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class LyricObject implements Serializable {
	public int begintime; // 开始时间
	public int endtime; // 结束时间
	public int timeline; // 单句歌词用时
	public String lrc; // 单句歌词

	public int getBegintime() {
		return begintime;
	}

	public void setBegintime(int begintime) {
		this.begintime = begintime;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public int getTimeline() {
		return timeline;
	}

	public void setTimeline(int timeline) {
		this.timeline = timeline;
	}

	public String getLrc() {
		return lrc;
	}

	public void setLrc(String lrc) {
		this.lrc = lrc;
	}

	@Override
	public String toString() {
		return "LyricObject [begintime=" + begintime + ", endtime=" + endtime
				+ ", timeline=" + timeline + ", lrc=" + lrc + "]";
	}

}
