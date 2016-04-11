package com.brother.yckx.model;

import com.brother.yckx.control.activity.rongyun.model.RongIMUser;

public class YCKXUserEntity {
	
	private String yckxId;
	private String yckxNickName;//姓名
	private String yckxPhone;//电话
	private String yckxToken;//融云token
	private String yckxSex;//性别
	private String yckxYears;//90后、80后...
	private String yckxProfession;//职业
	private UserEntity yckxUser;//洗车实体
	private CardEntity yckxCard;//车辆实体
    private RongIMUser yckxRongIMuser;//融云会员实体
}
