package com.brother.yckx.model;

import java.io.Serializable;


/**
 * ��������ҳ��ʹ��
 * 
 * */
public class OrderDetailEntity implements Serializable{
	
	//����ʦ��������Ϣ
	private OrderSubEntity orderSubEntity;
	private CommentEntity commentEntity;//����
	private String id;
	private String orderNum;
	private String orderStatus;
	private String orderPrice;
	private String orderUpdateTime;
	private String orderCreateTime;//�µ�ʱ��
	private String orderPayedTime;//֧�����ʱ��
	private String orderRobbedTime;//����ʦ�ӵ�ʱ��
	private String orderFiniahTime;//ϴ�����ʱ��
	private String serviceTime;//����ʱ��
	private String payType;//֧����ʽ(ϴ��ʦ����ʷ����û�д�����,Ϊ��)
	private String payTradeNo;//֧����ˮ��(ϴ��ʦ�п���Ϊ��)
	private String doorService;//�Ƿ����ŷ���
	//����ʦ��Ϣ
	private  WasherEntity washerEntity;
	//�̼���Ϣ
	private CompanyEntity companyEntity;
	//��Ʒ��Ϣ
	private ProductEntity productEntity;
    //��������Ϣ
	private CardEntity carEntity;
	//������Ϣ
	private OwnerEntity ownerEntity;
	//�����绰
	private String phone;
	//�����ص�
	private String address;
	//��������
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
