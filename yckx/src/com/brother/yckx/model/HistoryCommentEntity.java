package com.brother.yckx.model;

public class HistoryCommentEntity {
	
	private String commentId;
	private String creatUserId;
	private String createUserPhone;
	private String orderId;
	private String commentContent;
	private String commentCreateTime;
	private String commentScore;
	private String replyContent;//有可能为空
	private String replyTime;//有可能为空
	private String washerId;
	private String washerName;
	private String img1;//有可能没有
	private String img2;//有可能没有
	private String img3;//有可能没有
	private String productName;
	private String orderTime;
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getCreatUserId() {
		return creatUserId;
	}
	public void setCreatUserId(String creatUserId) {
		this.creatUserId = creatUserId;
	}
	public String getCreateUserPhone() {
		return createUserPhone;
	}
	public void setCreateUserPhone(String createUserPhone) {
		this.createUserPhone = createUserPhone;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getCommentCreateTime() {
		return commentCreateTime;
	}
	public void setCommentCreateTime(String commentCreateTime) {
		this.commentCreateTime = commentCreateTime;
	}
	public String getCommentScore() {
		return commentScore;
	}
	public void setCommentScore(String commentScore) {
		this.commentScore = commentScore;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getWasherId() {
		return washerId;
	}
	public void setWasherId(String washerId) {
		this.washerId = washerId;
	}
	public String getWasherName() {
		return washerName;
	}
	public void setWasherName(String washerName) {
		this.washerName = washerName;
	}
	public String getImg1() {
		return img1;
	}
	public void setImg1(String img1) {
		this.img1 = img1;
	}
	public String getImg2() {
		return img2;
	}
	public void setImg2(String img2) {
		this.img2 = img2;
	}
	
	public String getImg3() {
		return img3;
	}
	public void setImg3(String img3) {
		this.img3 = img3;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public HistoryCommentEntity(String commentId, String creatUserId,
			String createUserPhone, String orderId, String commentContent,
			String commentCreateTime, String commentScore, String replyContent,
			String replyTime, String washerId, String washerName, String img1,
			String img2,String img3, String productName, String orderTime) {
		super();
		this.commentId = commentId;
		this.creatUserId = creatUserId;
		this.createUserPhone = createUserPhone;
		this.orderId = orderId;
		this.commentContent = commentContent;
		this.commentCreateTime = commentCreateTime;
		this.commentScore = commentScore;
		this.replyContent = replyContent;
		this.replyTime = replyTime;
		this.washerId = washerId;
		this.washerName = washerName;
		this.img1 = img1;
		this.img2 = img2;
		this.img3=img3;
		this.productName = productName;
		this.orderTime = orderTime;
	}
	

}
