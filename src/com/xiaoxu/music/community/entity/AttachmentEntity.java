package com.xiaoxu.music.community.entity;

import java.io.Serializable;

public class AttachmentEntity implements Serializable {

	private AttachmentInfoEntity attachment;

	public AttachmentInfoEntity getAttachment() {
		return attachment;
	}

	public void setAttachment(AttachmentInfoEntity attachment) {
		this.attachment = attachment;
	}

	public AttachmentEntity() {
		super();
	}

	@Override
	public String toString() {
		return "AttachmentEntity [attachment=" + attachment + "]";
	}
}
