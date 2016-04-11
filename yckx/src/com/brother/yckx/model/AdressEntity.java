package com.brother.yckx.model;

public class AdressEntity {
	
	private String id;
	private String phone;
	private String  province;
	private String location;
	private boolean isDefalut;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public boolean isDefalut() {
		return isDefalut;
	}
	public void setDefalut(boolean isDefalut) {
		this.isDefalut = isDefalut;
	}
	public AdressEntity(String id,String phone, String province, String location,
			boolean isDefalut) {
		super();
		this.id=id;
		this.phone = phone;
		this.province = province;
		this.location = location;
		this.isDefalut = isDefalut;
	}
	
	public AdressEntity(){}

}
