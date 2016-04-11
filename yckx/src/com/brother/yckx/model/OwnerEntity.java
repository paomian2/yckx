package com.brother.yckx.model;

import java.io.Serializable;

public class OwnerEntity implements Serializable{
	
	private String id;
	private String userPhone;
	private String userImageUrl;
	private String userRegistTime;
	private String userRole;
	private String userOpStatus;
	private String userOrderCount;
	private String userTotalScore;
	private String userCommentCount;
	private String wowo;
	
	
	public OwnerEntity(){
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUserPhone() {
		return userPhone;
	}


	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}


	public String getUserImageUrl() {
		return userImageUrl;
	}


	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}


	public String getUserRegistTime() {
		return userRegistTime;
	}


	public void setUserRegistTime(String userRegistTime) {
		this.userRegistTime = userRegistTime;
	}


	public String getUserRole() {
		return userRole;
	}


	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}


	public String getUserOpStatus() {
		return userOpStatus;
	}


	public void setUserOpStatus(String userOpStatus) {
		this.userOpStatus = userOpStatus;
	}


	public String getUserOrderCount() {
		return userOrderCount;
	}


	public void setUserOrderCount(String userOrderCount) {
		this.userOrderCount = userOrderCount;
	}


	public String getUserTotalScore() {
		return userTotalScore;
	}


	public void setUserTotalScore(String userTotalScore) {
		this.userTotalScore = userTotalScore;
	}


	public String getUserCommentCount() {
		return userCommentCount;
	}


	public void setUserCommentCount(String userCommentCount) {
		this.userCommentCount = userCommentCount;
	}


	public String getWowo() {
		return wowo;
	}


	public void setWowo(String wowo) {
		this.wowo = wowo;
	}


	public OwnerEntity(String id, String userPhone, String userImageUrl,
			String userRegistTime, String userRole, String userOpStatus,
			String userOrderCount, String userTotalScore,
			String userCommentCount, String wowo) {
		super();
		this.id = id;
		this.userPhone = userPhone;
		this.userImageUrl = userImageUrl;
		this.userRegistTime = userRegistTime;
		this.userRole = userRole;
		this.userOpStatus = userOpStatus;
		this.userOrderCount = userOrderCount;
		this.userTotalScore = userTotalScore;
		this.userCommentCount = userCommentCount;
		this.wowo = wowo;
	}
	
	

}
