package com.brother.yckx.model.db;

public class CarClassInfo {
	
	private String zimu;
	private String cid;
	private String name;
	public CarClassInfo(String zimu, String cid, String name) {
		super();
		this.zimu = zimu;
		this.cid = cid;
		this.name = name;
	}
	public String getZimu() {
		return zimu;
	}
	public void setZimu(String zimu) {
		this.zimu = zimu;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
