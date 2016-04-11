package com.brother.yckx.model;

import java.io.Serializable;

public class WasherEntity implements Serializable{
	
	private String waherId;
	private String washerName;
	private String userPhone;
	private String userImageUrl;
	private String userRegistTime;
	private String userRole;
	private String washerRole;
	private String userOpStatus;
	private String userOrderCount;
	private String userTotalScore;
	private String userCommentCount;
	private String userAuthority;
	private String wowo;
	public String getWaherId() {
		return waherId;
	}
	public void setWaherId(String waherId) {
		this.waherId = waherId;
	}
	public String getWasherName() {
		return washerName;
	}
	public void setWasherName(String washerName) {
		this.washerName = washerName;
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
	public String getWasherRole() {
		return washerRole;
	}
	public void setWasherRole(String washerRole) {
		this.washerRole = washerRole;
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
	public String getUserAuthority() {
		return userAuthority;
	}
	public void setUserAuthority(String userAuthority) {
		this.userAuthority = userAuthority;
	}
	public String getWowo() {
		return wowo;
	}
	public void setWowo(String wowo) {
		this.wowo = wowo;
	}
	public WasherEntity(String waherId, String washerName, String userPhone,
			String userImageUrl, String userRegistTime, String userRole,
			String washerRole, String userOpStatus, String userOrderCount,
			String userTotalScore, String userCommentCount,
			String userAuthority, String wowo) {
		super();
		this.waherId = waherId;
		this.washerName = washerName;
		this.userPhone = userPhone;
		this.userImageUrl = userImageUrl;
		this.userRegistTime = userRegistTime;
		this.userRole = userRole;
		this.washerRole = washerRole;
		this.userOpStatus = userOpStatus;
		this.userOrderCount = userOrderCount;
		this.userTotalScore = userTotalScore;
		this.userCommentCount = userCommentCount;
		this.userAuthority = userAuthority;
		this.wowo = wowo;
	}
	
	public WasherEntity(){}
	
	

}
