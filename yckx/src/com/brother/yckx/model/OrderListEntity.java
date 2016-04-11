package com.brother.yckx.model;

import java.io.Serializable;

public class OrderListEntity implements Serializable{
	//订单信息
    private String id;
    private String orderNum;
    private String orderStatus;
    private String orderPrice;
    private String orderUpdateTime;
    private String orderCreateTime;
    private String orderFinishTime;
    private String orderPayedTime;
    private String orderRobbedTime;
    
    private String payType;
    private String payTradeNo;
    private String phone;
    private String doorService;
    private String serviceTime;
    private String address;
    private String remark;
    
  //美护师处理订单信息
  	private OrderSubEntity orderSubEntity;
  	private CommentEntity commentEntity;//评论
    
    private OwnerEntity ownerEntity;
    private WasherEntity washerEntity;
    private CompanyEntity compannyEntity;
    
    
    private ProductEntity productEntity;
    private CardEntity carEntity;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getOrderUpdateTime() {
		return orderUpdateTime;
	}
	public void setOrderUpdateTime(String orderUpdateTime) {
		this.orderUpdateTime = orderUpdateTime;
	}
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public String getOrderFinishTime() {
		return orderFinishTime;
	}
	public void setOrderFinishTime(String orderFinishTime) {
		this.orderFinishTime = orderFinishTime;
	}
	public String getOrderPayedTime() {
		return orderPayedTime;
	}
	public void setOrderPayedTime(String orderPayedTime) {
		this.orderPayedTime = orderPayedTime;
	}
	public String getOrderRobbedTime() {
		return orderRobbedTime;
	}
	public void setOrderRobbedTime(String orderRobbedTime) {
		this.orderRobbedTime = orderRobbedTime;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayTradeNo() {
		return payTradeNo;
	}
	public void setPayTradeNo(String payTradeNo) {
		this.payTradeNo = payTradeNo;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public OrderSubEntity getOrderSubEntity() {
		return orderSubEntity;
	}
	public void setOrderSubEntity(OrderSubEntity orderSubEntity) {
		this.orderSubEntity = orderSubEntity;
	}
	public CommentEntity getCommentEntity() {
		return commentEntity;
	}
	public void setCommentEntity(CommentEntity commentEntity) {
		this.commentEntity = commentEntity;
	}
	public OwnerEntity getOwnerEntity() {
		return ownerEntity;
	}
	public void setOwnerEntity(OwnerEntity ownerEntity) {
		this.ownerEntity = ownerEntity;
	}
	public WasherEntity getWasherEntity() {
		return washerEntity;
	}
	public void setWasherEntity(WasherEntity washerEntity) {
		this.washerEntity = washerEntity;
	}
	public CompanyEntity getCompannyEntity() {
		return compannyEntity;
	}
	public void setCompannyEntity(CompanyEntity compannyEntity) {
		this.compannyEntity = compannyEntity;
	}
	public ProductEntity getProductEntity() {
		return productEntity;
	}
	public void setProductEntity(ProductEntity productEntity) {
		this.productEntity = productEntity;
	}
	public CardEntity getCarEntity() {
		return carEntity;
	}
	public void setCarEntity(CardEntity carEntity) {
		this.carEntity = carEntity;
	}
	public OrderListEntity(String id, String orderNum, String orderStatus,
			String orderPrice, String orderUpdateTime, String orderCreateTime,
			String orderFinishTime, String orderPayedTime,
			String orderRobbedTime, String payType, String payTradeNo,
			String phone, String doorService, String serviceTime,
			String address, String remark, OrderSubEntity orderSubEntity,
			CommentEntity commentEntity,
			OwnerEntity ownerEntity,
			WasherEntity washerEntity, CompanyEntity compannyEntity,
			ProductEntity productEntity, CardEntity carEntity) {
		super();
		this.id = id;
		this.orderNum = orderNum;
		this.orderStatus = orderStatus;
		this.orderPrice = orderPrice;
		this.orderUpdateTime = orderUpdateTime;
		this.orderCreateTime = orderCreateTime;
		this.orderFinishTime = orderFinishTime;
		this.orderPayedTime = orderPayedTime;
		this.orderRobbedTime = orderRobbedTime;
		this.payType = payType;
		this.payTradeNo = payTradeNo;
		this.phone = phone;
		this.doorService = doorService;
		this.serviceTime = serviceTime;
		this.address = address;
		this.remark = remark;
		this.orderSubEntity=orderSubEntity;
		this.commentEntity=commentEntity;
		this.ownerEntity = ownerEntity;
		this.washerEntity = washerEntity;
		this.compannyEntity = compannyEntity;
		this.productEntity = productEntity;
		this.carEntity = carEntity;
	}
    
    public OrderListEntity(){}
	@Override
	public String toString() {
		return "OrderListEntity [id=" + id + ", orderNum=" + orderNum
				+ ", orderStatus=" + orderStatus + ", orderPrice=" + orderPrice
				+ ", orderUpdateTime=" + orderUpdateTime + ", orderCreateTime="
				+ orderCreateTime + ", orderFinishTime=" + orderFinishTime
				+ ", orderPayedTime=" + orderPayedTime + ", orderRobbedTime="
				+ orderRobbedTime + ", payType=" + payType + ", payTradeNo="
				+ payTradeNo + ", phone=" + phone + ", doorService="
				+ doorService + ", serviceTime=" + serviceTime + ", address="
				+ address + ", remark=" + remark + ", ownerEntity="
				+ ownerEntity + ", washerEntity=" + washerEntity
				+ ", compannyEntity=" + compannyEntity + ", productEntity="
				+ productEntity + ", carEntity=" + carEntity + "]";
	}
    
	
	
	
	

}
