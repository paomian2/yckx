package com.brother.yckx.model;

import android.graphics.drawable.Drawable;

public class NoticeEntity {
	
	private Drawable drawable;
	private String noticeType;
	private String time;
	private String noticeTxt;
	public Drawable getDrawable() {
		return drawable;
	}
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	public String getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getNoticeTxt() {
		return noticeTxt;
	}
	public void setNoticeTxt(String noticeTxt) {
		this.noticeTxt = noticeTxt;
	}
	public NoticeEntity(Drawable drawable, String noticeType, String time,
			String noticeTxt) {
		super();
		this.drawable = drawable;
		this.noticeType = noticeType;
		this.time = time;
		this.noticeTxt = noticeTxt;
	}
	
	

}
