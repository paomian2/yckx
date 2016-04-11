package com.brother.yckx.control.activity.rongyun.model;

public class RongIMUser {
	
	private String userId;
	private String userNickName;
	private String userPortrait;//头像
	private RongIMFriend userFriend;//聊过天的朋友
	
	public void setUserid(String userId){
		this.userId=userId;
	}
	
	public String getuserId(){
		return userId;
	}
	public void setUserName(String userNickName){
		this.userNickName=userNickName;
	}
	
	public String getUserName(){
		return userNickName;
	}
	
	public void setUserPorTrait(String userPortrait){
		this.userPortrait=userPortrait;
	}
	
	public String getUserPorTrait(){
		return userPortrait;
	}
	
	public void setUserFriend(RongIMFriend userFriend){
		this.userFriend=userFriend;
	}
	
	public RongIMFriend getUserFriend(){
		return userFriend;
	}

}
