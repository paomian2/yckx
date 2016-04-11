package com.brother.yckx.model;

public class CarParkEntity {
	
	private String companyName;
	private String companySpaceCount;
	private String companyTotalCount;
	private String companyId;
	private String companyImageUrl;
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyImageUrl() {
		return companyImageUrl;
	}
	public void setCompanyImageUrl(String companyImageUrl) {
		this.companyImageUrl = companyImageUrl;
	}
	public CarParkEntity(String companyName, String companySpaceCount,
			String companyTotalCount, String companyId, String companyImageUrl) {
		super();
		this.companyName = companyName;
		this.companySpaceCount = companySpaceCount;
		this.companyTotalCount = companyTotalCount;
		this.companyId = companyId;
		this.companyImageUrl = companyImageUrl;
	}
	
	

}
