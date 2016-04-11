package com.brother.yckx.model;

import java.io.Serializable;

public class CompanyEntity implements Serializable{
	
	private String id;
	private String companyName;
	private String companyAddr;
	private String companyDesc;
	private String companyImg;
	private String companyMapImg;
	private String companyPhone;
	private String companySpaceCount;
	private String companyTotalCount;
	private String spacePrice;
	private String freeTime;
	private String saleMsg;
	private String lbsId;
	private String longitude;
	private String latitude;
	private String status;

	public CompanyEntity(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getCompanyImg() {
		return companyImg;
	}

	public void setCompanyImg(String companyImg) {
		this.companyImg = companyImg;
	}

	public String getCompanyMapImg() {
		return companyMapImg;
	}

	public void setCompanyMapImg(String companyMapImg) {
		this.companyMapImg = companyMapImg;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCompanySpaceCount() {
		return companySpaceCount;
	}

	public void setCompanySpaceCount(String companySpaceCount) {
		this.companySpaceCount = companySpaceCount;
	}

	public String getCompanyTotalCount() {
		return companyTotalCount;
	}

	public void setCompanyTotalCount(String companyTotalCount) {
		this.companyTotalCount = companyTotalCount;
	}

	public String getSpacePrice() {
		return spacePrice;
	}

	public void setSpacePrice(String spacePrice) {
		this.spacePrice = spacePrice;
	}

	public String getFreeTime() {
		return freeTime;
	}

	public void setFreeTime(String freeTime) {
		this.freeTime = freeTime;
	}

	public String getSaleMsg() {
		return saleMsg;
	}

	public void setSaleMsg(String saleMsg) {
		this.saleMsg = saleMsg;
	}

	public String getLbsId() {
		return lbsId;
	}

	public void setLbsId(String lbsId) {
		this.lbsId = lbsId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CompanyEntity(String id, String companyName, String companyAddr,
			String companyDesc, String companyImg, String companyMapImg,
			String companyPhone, String companySpaceCount,
			String companyTotalCount, String spacePrice, String freeTime,
			String saleMsg, String lbsId, String longitude, String latitude,
			String status) {
		super();
		this.id = id;
		this.companyName = companyName;
		this.companyAddr = companyAddr;
		this.companyDesc = companyDesc;
		this.companyImg = companyImg;
		this.companyMapImg = companyMapImg;
		this.companyPhone = companyPhone;
		this.companySpaceCount = companySpaceCount;
		this.companyTotalCount = companyTotalCount;
		this.spacePrice = spacePrice;
		this.freeTime = freeTime;
		this.saleMsg = saleMsg;
		this.lbsId = lbsId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.status = status;
	}
	
}
