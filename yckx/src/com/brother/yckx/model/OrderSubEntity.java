package com.brother.yckx.model;

import java.io.Serializable;

public class OrderSubEntity implements Serializable{
	
	private String id;
	private String subStatus;
	private String arrivedTime;
	private String checkedTime;
	private String washTime;
	private String washedTime;
	private String img1Url;
	private String img1Ur2;
	private String img1Ur3;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}
	public String getArrivedTime() {
		return arrivedTime;
	}
	public void setArrivedTime(String arrivedTime) {
		this.arrivedTime = arrivedTime;
	}
	public String getCheckedTime() {
		return checkedTime;
	}
	public void setCheckedTime(String checkedTime) {
		this.checkedTime = checkedTime;
	}
	public String getWashTime() {
		return washTime;
	}
	public void setWashTime(String washTime) {
		this.washTime = washTime;
	}
	public String getWashedTime() {
		return washedTime;
	}
	public void setWashedTime(String washedTime) {
		this.washedTime = washedTime;
	}
	public String getImg1Url() {
		return img1Url;
	}
	public void setImg1Url(String img1Url) {
		this.img1Url = img1Url;
	}
	public String getImg1Ur2() {
		return img1Ur2;
	}
	public void setImg1Ur2(String img1Ur2) {
		this.img1Ur2 = img1Ur2;
	}
	public String getImg1Ur3() {
		return img1Ur3;
	}
	public void setImg1Ur3(String img1Ur3) {
		this.img1Ur3 = img1Ur3;
	}
	public OrderSubEntity(String id, String subStatus, String arrivedTime,
			String checkedTime, String washTime, String washedTime,
			String img1Url, String img1Ur2, String img1Ur3) {
		super();
		this.id = id;
		this.subStatus = subStatus;
		this.arrivedTime = arrivedTime;
		this.checkedTime = checkedTime;
		this.washTime = washTime;
		this.washedTime = washedTime;
		this.img1Url = img1Url;
		this.img1Ur2 = img1Ur2;
		this.img1Ur3 = img1Ur3;
	}
	
	public OrderSubEntity(){}
	
}
