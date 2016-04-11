package com.brother.yckx.model;

import java.io.Serializable;

public class ProductEntity implements Serializable{
	
	private String id;
	private String productName;
	private String productPrice;
	private String productDesc;
	private String productImage;
	private String h5URL;
	private String productStatus;
	private String productCreateTime;
	private String remark;
	private String doorService;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public String getH5URL() {
		return h5URL;
	}
	public void setH5URL(String h5url) {
		h5URL = h5url;
	}
	public String getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	public String getProductCreateTime() {
		return productCreateTime;
	}
	public void setProductCreateTime(String productCreateTime) {
		this.productCreateTime = productCreateTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDoorService() {
		return doorService;
	}
	public void setDoorService(String doorService) {
		this.doorService = doorService;
	}
	public ProductEntity(String id, String productName, String productPrice,
			String productDesc, String productImage, String h5url,
			String productStatus, String productCreateTime, String remark,
			String doorService) {
		super();
		this.id = id;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productDesc = productDesc;
		this.productImage = productImage;
		h5URL = h5url;
		this.productStatus = productStatus;
		this.productCreateTime = productCreateTime;
		this.remark = remark;
		this.doorService = doorService;
	}
	@Override
	public String toString() {
		return "ProductEntity [id=" + id + ", productName=" + productName
				+ ", productPrice=" + productPrice + ", productDesc="
				+ productDesc + ", productImage=" + productImage + ", h5URL="
				+ h5URL + ", productStatus=" + productStatus
				+ ", productCreateTime=" + productCreateTime + ", remark="
				+ remark + ", doorService=" + doorService + "]";
	}
	
	

}
