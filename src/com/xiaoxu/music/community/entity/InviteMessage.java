package com.xiaoxu.music.community.entity;

public class InviteMessage {
	
	private int id;
	private String from;
	//时间
	private long time;
	//添加理由
	private String reason;
	//未验证，已同意等状�??
	private InviteMesageStatus status;
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public InviteMesageStatus getStatus() {
		return status;
	}

	public void setStatus(InviteMesageStatus status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public enum InviteMesageStatus{
		/**被邀�?*/
		BEINVITEED,
		/**被拒�?*/
		BEREFUSED,
		/**对方同意*/
		BEAGREED,
		/**对方申请*/
		BEAPPLYED,
		/**我同意了对方的请�?*/
		AGREED,
		/**我拒绝了对方的请�?*/
		REFUSED
		
	}
	
}



