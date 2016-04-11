package com.brother.yckx.model.b2b;

public class B2BUser {
	private String id;
	private String name;
	private String imgUrl;
	private String sex;
	private String userType;//是不是商家
	private String userBusinessIdentify;//是商家的前提下是否已经经过认证
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserBusinessIdentify() {
		return userBusinessIdentify;
	}
	public void setUserBusinessIdentify(String userBusinessIdentify) {
		this.userBusinessIdentify = userBusinessIdentify;
	}
	public B2BUser(String id, String name, String imgUrl, String sex,
			String userType, String userBusinessIdentify) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
		this.sex = sex;
		this.userType = userType;
		this.userBusinessIdentify = userBusinessIdentify;
	}
	@Override
	public String toString() {
		return "B2BUser [id=" + id + ", name=" + name + ", imgUrl=" + imgUrl
				+ ", sex=" + sex + ", userType=" + userType
				+ ", userBusinessIdentify=" + userBusinessIdentify + "]";
	}
	
	
	
	
	
}
