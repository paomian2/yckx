package com.brother.yckx.model;

import java.io.Serializable;

/**����ʵ��*/
public class OrderEntity implements Serializable{
	private String orderId;//Ϊ��ʱ��ʾ��û��������ʽ����
	private String companyId;
	private String userCarId;
	private String productId;
	private String phone;//��ϵ�绰	
	private String doorService;//�����ŷ����ǵ������
	private String address;//�����ص�
	private String serviceTime;//����ʱ��
	private String remark;//����
	private String washerPhone;
	private boolean isFinihs;
	public String getOrderId(){
		return orderId;
	}
	public void setOrderId(String orderId){
		this.orderId=orderId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getUserCarId() {
		return userCarId;
	}
	public void setUserCarId(String userCarId) {
		this.userCarId = userCarId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDoorService() {
		return doorService;
	}
	public void setDoorService(String doorService) {
		this.doorService = doorService;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getWasherPhone() {
		return washerPhone;
	}
	public void setWasherPhone(String washerPhone) {
		this.washerPhone = washerPhone;
	}
	public boolean isFinihs() {
		return isFinihs;
	}
	public void setFinihs(boolean isFinihs) {
		this.isFinihs = isFinihs;
	}
	public OrderEntity(String orderId,String companyId, String userCarId, String productId,
			String phone, String doorService, String address,
			String serviceTime, String remark, String washerPhone,
			boolean isFinihs) {
		super();
		this.orderId=orderId;
		this.companyId = companyId;
		this.userCarId = userCarId;
		this.productId = productId;
		this.phone = phone;
		this.doorService = doorService;
		this.address = address;
		this.serviceTime = serviceTime;
		this.remark = remark;
		this.washerPhone = washerPhone;
		this.isFinihs = isFinihs;
	}
	
	public OrderEntity(){}
	

}
