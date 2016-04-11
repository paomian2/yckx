package com.brother.yckx.model;

import java.io.Serializable;


/**
 * 订单详情页面使用
 * 
 * */
public class OrderDetailEntity implements Serializable{
	
	//美护师处理订单信息
	private OrderSubEntity orderSubEntity;
	private CommentEntity commentEntity;//评论
	private String id;
	private String orderNum;
	private String orderStatus;
	private String orderPrice;
	private String orderUpdateTime;
	private String orderCreateTime;//下单时间
	private String orderPayedTime;//支付完成时间
	private String orderRobbedTime;//美护师接单时间
	private String orderFiniahTime;//洗车完成时间
	private String serviceTime;//服务时间
	private String payType;//支付方式(洗护师的历史订单没有此内容,为空)
	private String payTradeNo;//支付流水号(洗护师有可能为空)
	private String doorService;//是否上门服务
	//美护师信息
	private  WasherEntity washerEntity;
	//商家信息
	private CompanyEntity companyEntity;
	//产品信息
	private ProductEntity productEntity;
    //服务车辆信息
	private CardEntity carEntity;
	//车主信息
	private OwnerEntity ownerEntity;
	//车主电话
	private String phone;
	//车主地点
	private String address;
	//车主留言
	private String remark;
	

	public OwnerEntity getOwnerEntity() {
		return ownerEntity;
	}

	public void setOwnerEntity(OwnerEntity ownerEntity) {
		this.ownerEntity = ownerEntity;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getOrderFiniahTime() {
		return orderFiniahTime;
	}

	public void setOrderFiniahTime(String orderFiniahTime) {
		this.orderFiniahTime = orderFiniahTime;
	}

	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
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
	public String getDoorService() {
		return doorService;
	}

	public void setDoorService(String doorService) {
		this.doorService = doorService;
	}

	public WasherEntity getWasherEntity() {
		return washerEntity;
	}
	public void setWasherEntity(WasherEntity washerEntity) {
		this.washerEntity = washerEntity;
	}

	public CompanyEntity getCompanyEntity() {
		return companyEntity;
	}

	public void setCompanyEntity(CompanyEntity companyEntity) {
		this.companyEntity = companyEntity;
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
	public OrderDetailEntity(OrderSubEntity orderSubEntity,
			CommentEntity commentEntity, String id, String orderNum,
			String orderStatus, String orderPrice, String orderUpdateTime,
			String orderCreateTime, String orderPayedTime,
			String orderRobbedTime, String orderFiniahTime, String serviceTime,
			String payType, String payTradeNo, String doorService,
			WasherEntity washerEntity, CompanyEntity companyEntity,
			ProductEntity productEntity, CardEntity carEntity,
			OwnerEntity ownerEntity, String phone, String address, String remark) {
		super();
		this.orderSubEntity = orderSubEntity;
		this.commentEntity = commentEntity;
		this.id = id;
		this.orderNum = orderNum;
		this.orderStatus = orderStatus;
		this.orderPrice = orderPrice;
		this.orderUpdateTime = orderUpdateTime;
		this.orderCreateTime = orderCreateTime;
		this.orderPayedTime = orderPayedTime;
		this.orderRobbedTime = orderRobbedTime;
		this.orderFiniahTime = orderFiniahTime;
		this.serviceTime = serviceTime;
		this.payType = payType;
		this.payTradeNo = payTradeNo;
		this.doorService = doorService;
		this.washerEntity = washerEntity;
		this.companyEntity = companyEntity;
		this.productEntity = productEntity;
		this.carEntity = carEntity;
		this.ownerEntity = ownerEntity;
		this.phone = phone;
		this.address = address;
		this.remark = remark;
	}
	
	

	@Override
	public String toString() {
		return "OrderDetailEntity [orderSubEntity=" + orderSubEntity
				+ ", commentEntity=" + commentEntity + ", id=" + id
				+ ", orderNum=" + orderNum + ", orderStatus=" + orderStatus
				+ ", orderPrice=" + orderPrice + ", orderUpdateTime="
				+ orderUpdateTime + ", orderCreateTime=" + orderCreateTime
				+ ", orderPayedTime=" + orderPayedTime + ", orderRobbedTime="
				+ orderRobbedTime + ", orderFiniahTime=" + orderFiniahTime
				+ ", serviceTime=" + serviceTime + ", payType=" + payType
				+ ", payTradeNo=" + payTradeNo + ", doorService=" + doorService
				+ ", washerEntity=" + washerEntity + ", companyEntity="
				+ companyEntity + ", productEntity=" + productEntity
				+ ", carEntity=" + carEntity + ", ownerEntity=" + ownerEntity
				+ ", phone=" + phone + ", address=" + address + ", remark="
				+ remark + "]";
	}

	public OrderDetailEntity(){}
	
}
