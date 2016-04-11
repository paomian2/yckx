package com.brother.yckx.model;

public class UserEntity {
	private String userId;
	private String userName;
	private String userIamgeUrl;
	private String userPhone;
	private String userRole;
	private String userToken;
	
	private String userTotalScore_mhs;//美护师独有,美护师总分
	private String userCommentCount_mhs;//美护师独有,评价数量
	private String userOpStatus_mhs;//美护师独有，上下班状态
	private String userOrderCount_mhs;
	private boolean inWork_mhs;//美护师是否上班状态
	
	private String userAdress_user;//用户独有，常在位置
	private String userCard_user;//用户默认车辆
	private String userBalance_user;//余额
	private String userCardTimes_user;//卡次
	private String userNotice_user;//推送的消息
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserIamgeUrl() {
		return userIamgeUrl;
	}
	public void setUserIamgeUrl(String userIamgeUrl) {
		this.userIamgeUrl = userIamgeUrl;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getUserTotalScore_mhs() {
		return userTotalScore_mhs;
	}
	public void setUserTotalScore_mhs(String userTotalScore_mhs) {
		this.userTotalScore_mhs = userTotalScore_mhs;
	}
	public String getUserCommentCount_mhs() {
		return userCommentCount_mhs;
	}
	public void setUserCommentCount_mhs(String userCommentCount_mhs) {
		this.userCommentCount_mhs = userCommentCount_mhs;
	}
	public String getUserOpStatus_mhs() {
		return userOpStatus_mhs;
	}
	public void setUserOpStatus_mhs(String userOpStatus_mhs) {
		this.userOpStatus_mhs = userOpStatus_mhs;
	}
	public String getUserOrderCount_mhs() {
		return userOrderCount_mhs;
	}
	public void setUserOrderCount_mhs(String userOrderCount_mhs) {
		this.userOrderCount_mhs = userOrderCount_mhs;
	}
	public boolean isInWork_mhs() {
		return inWork_mhs;
	}
	public void setInWork_mhs(boolean inWork_mhs) {
		this.inWork_mhs = inWork_mhs;
	}
	public String getUserAdress_user() {
		return userAdress_user;
	}
	public void setUserAdress_user(String userAdress_user) {
		this.userAdress_user = userAdress_user;
	}
	public String getUserCard_user() {
		return userCard_user;
	}
	public void setUserCard_user(String userCard_user) {
		this.userCard_user = userCard_user;
	}
	public String getUserBalance_user() {
		return userBalance_user;
	}
	public void setUserBalance_user(String userBalance_user) {
		this.userBalance_user = userBalance_user;
	}
	public String getUserCardTimes_user() {
		return userCardTimes_user;
	}
	public void setUserCardTimes_user(String userCardTimes_user) {
		this.userCardTimes_user = userCardTimes_user;
	}
	public String getUserNotice_user() {
		return userNotice_user;
	}
	public void setUserNotice_user(String userNotice_user) {
		this.userNotice_user = userNotice_user;
	}
	public UserEntity(String userId, String userName, String userIamgeUrl,
			String userPhone, String userRole, String userToken,
			String userTotalScore_mhs, String userCommentCount_mhs,
			String userOpStatus_mhs, String userOrderCount_mhs,
			boolean inWork_mhs, String userAdress_user, String userCard_user,
			String userBalance_user, String userCardTimes_user,
			String userNotice_user) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userIamgeUrl = userIamgeUrl;
		this.userPhone = userPhone;
		this.userRole = userRole;
		this.userToken = userToken;
		this.userTotalScore_mhs = userTotalScore_mhs;
		this.userCommentCount_mhs = userCommentCount_mhs;
		this.userOpStatus_mhs = userOpStatus_mhs;
		this.userOrderCount_mhs = userOrderCount_mhs;
		this.inWork_mhs = inWork_mhs;
		this.userAdress_user = userAdress_user;
		this.userCard_user = userCard_user;
		this.userBalance_user = userBalance_user;
		this.userCardTimes_user = userCardTimes_user;
		this.userNotice_user = userNotice_user;
	}
	@Override
	public String toString() {
		return "UserEntity [userId=" + userId + ", userName=" + userName
				+ ", userIamgeUrl=" + userIamgeUrl + ", userPhone=" + userPhone
				+ ", userRole=" + userRole + ", userToken=" + userToken
				+ ", userTotalScore_mhs=" + userTotalScore_mhs
				+ ", userCommentCount_mhs=" + userCommentCount_mhs
				+ ", userOpStatus_mhs=" + userOpStatus_mhs
				+ ", userOrderCount_mhs=" + userOrderCount_mhs
				+ ", inWork_mhs=" + inWork_mhs + ", userAdress_user="
				+ userAdress_user + ", userCard_user=" + userCard_user
				+ ", userBalance_user=" + userBalance_user
				+ ", userCardTimes_user=" + userCardTimes_user
				+ ", userNotice_user=" + userNotice_user + "]";
	}
	
	
	public UserEntity(){}
	
	

}
