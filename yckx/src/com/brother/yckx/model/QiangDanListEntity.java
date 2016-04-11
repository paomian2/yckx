package com.brother.yckx.model;

import java.io.Serializable;

public class QiangDanListEntity implements Serializable{
	
	private String orderId;
	private String orderNum;
	private String orderStatus;
	private String phone;
	private String createTime;//下单时间
	private String receiveTime;//接单时间
	private String completeTime;//完成时间
	private String serviceTime;//服务时间
	private String orderPrice;
	private String payType;
	private String address;
	private String doorService;
	private String say;//待补充
	
	private String companyId;
	private String companyName;
	
	private String productId;
	private String productName;
	
	private String carId;
	private String carBrand;
	private String carColor;
	private String province;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}
	public String getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getDoorService() {
		return doorService;
	}
	
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public void setDoorService(String doorService) {
		this.doorService = doorService;
	}
	public String getSay() {
		return say;
	}
	public void setSay(String say) {
		this.say = say;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getCarBrand() {
		return carBrand;
	}
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	public String getCarColor() {
		return carColor;
	}
	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public QiangDanListEntity(String orderId, String orderNum, String orderStatus,String phone,String createTime,String receiveTime,
			String completeTime,
			String serviceTime,String orderPrice, String payType,String address, String doorService, String say,
			String companyId, String companyName, String productId,
			String productName, String carId, String carBrand, String carColor,
			String province) {
		super();
		this.orderId = orderId;
		this.orderNum = orderNum;
		this.orderStatus=orderStatus;
		this.phone = phone;
		this.createTime=createTime;
		this.receiveTime=receiveTime;
		this.completeTime=completeTime;
		this.serviceTime = serviceTime;
		this.orderPrice=orderPrice;
		this.payType=payType;
		this.address = address;
		this.doorService = doorService;
		this.say = say;
		this.companyId = companyId;
		this.companyName = companyName;
		this.productId = productId;
		this.productName = productName;
		this.carId = carId;
		this.carBrand = carBrand;
		this.carColor = carColor;
		this.province = province;
	}
	
	public QiangDanListEntity(){}

}
