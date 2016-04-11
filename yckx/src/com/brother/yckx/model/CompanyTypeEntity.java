package com.brother.yckx.model;

import java.io.Serializable;

public class CompanyTypeEntity implements Serializable{
	
	private String id;
	private String storeName;
	private String storeImgUrl;
	private String amapImgUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreImgUrl() {
		return storeImgUrl;
	}
	public void setStoreImgUrl(String storeImgUrl) {
		this.storeImgUrl = storeImgUrl;
	}
	public String getAmapImgUrl() {
		return amapImgUrl;
	}
	public void setAmapImgUrl(String amapImgUrl) {
		this.amapImgUrl = amapImgUrl;
	}
	public CompanyTypeEntity(String id, String storeName, String storeImgUrl,
			String amapImgUrl) {
		super();
		this.id = id;
		this.storeName = storeName;
		this.storeImgUrl = storeImgUrl;
		this.amapImgUrl = amapImgUrl;
	}
	@Override
	public String toString() {
		return "BusinessTypeEntity [id=" + id + ", storeName=" + storeName
				+ ", storeImgUrl=" + storeImgUrl + ", amapImgUrl=" + amapImgUrl
				+ "]";
	}
	
	

}
