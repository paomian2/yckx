package com.brother.yckx.model;

import java.io.Serializable;

public class MessageEntity implements Serializable{
	
	private String id;
	private String tittle;
	private String msg;
	private String type;
	private String status;
	private String linkId;
	private SenderEntity senderEntity;
	private String createTime;
    private String updateTime;
    private String needPush;
    private String retryTimes;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTittle() {
		return tittle;
	}
	public void setTittle(String tittle) {
		this.tittle = tittle;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public SenderEntity getSenderEntity() {
		return senderEntity;
	}
	public void setSenderEntity(SenderEntity senderEntity) {
		this.senderEntity = senderEntity;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getNeedPush() {
		return needPush;
	}
	public void setNeedPush(String needPush) {
		this.needPush = needPush;
	}
	public String getRetryTimes() {
		return retryTimes;
	}
	public void setRetryTimes(String retryTimes) {
		this.retryTimes = retryTimes;
	}
	public MessageEntity(String id, String tittle, String msg, String type,
			String status, String linkId, SenderEntity senderEntity,
			String createTime, String updateTime, String needPush,
			String retryTimes) {
		super();
		this.id = id;
		this.tittle = tittle;
		this.msg = msg;
		this.type = type;
		this.status = status;
		this.linkId = linkId;
		this.senderEntity = senderEntity;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.needPush = needPush;
		this.retryTimes = retryTimes;
	}
	@Override
	public String toString() {
		return "MessageEntity [id=" + id + ", tittle=" + tittle + ", msg="
				+ msg + ", type=" + type + ", status=" + status + ", linkId="
				+ linkId + ", senderEntity=" + senderEntity + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", needPush="
				+ needPush + ", retryTimes=" + retryTimes + "]";
	}
    
    public MessageEntity(){}
    
}
