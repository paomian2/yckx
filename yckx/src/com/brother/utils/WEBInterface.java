package com.brother.utils;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

public class WEBInterface {
	
	public static final String INDEXURL="http://112.74.18.34:80/wc";
	public static final String INDEXIMGURL="http://112.74.18.34:12002/";
	//http://112.74.18.34:12002//2016/01/10/72bcb757e32f14fc2927099e951d9d91_yckx_temp.png
	//http://112.74.18.34:12002/2016/01/14/8c8312316c3398147d62a49c6c294bca_yckx_temp.png
    
	//附件商家
	public static final String MAP_STORELIST_URL=INDEXURL+"/company/lbs";
	//http://112.74.18.34:80/wc/company/lbs?lng=108.397434&lat=22.81906
	//http://112.74.18.34:80/wc/company/lbs?lng=110.282539&lat=25.261227
	//25.261227---110.282539(桂林站)
	
	//商家类型/company/types
	public static final String STORE_TYPE=INDEXURL+"/company/types";
	//http://112.74.18.34:80/wc/company/types
	
	//商家详情/company/detail/companyid
	public static final String STORE_DETAIL=INDEXURL+"/company/detail/";
	//http://112.74.18.34:80/wc/company/detail/companyid=12
	//id好从附件商家中获取的数据中得到
	
	//注册/register
	public static final String REGISTERURL=INDEXURL+"/register";
	//http://112.74.18.34:80/wc/register

	//登录/auth      //new StringEntity(new JSONObject(hasmp).toString(),"UTF-8")
	public static final String LOGINURL=INDEXURL+"/auth";
	//http://112.74.18.34:80/wc/auth
	
	//发送验证码/register/checkcode/{phone}
	public static final String SENDCODEURL=INDEXURL+"/register/checkcode/";
	//http://112.74.18.34:80/wc/register/checkcode/15011428785
	
	//修改密码/register/modifyPasswd/{phone}/{checkCode}/{passwd}
	public static final String MODIFYPWDURL=INDEXURL+"/register/modifyPasswd";
	//http://112.74.18.34:80/wc/register/modifyPasswd/15678467855/12345/12345678
	
	//在线修改密码/user/resetPasswd/{oldPasswd}/{newPasswd}
	public static final String MODIFYPWDONLINE_URL=INDEXURL+"/user/resetPasswd/";
	
	//上传头像/user/header
	public static final String UPLOADHEADURL=INDEXURL+"/user/header";
	//http://112.74.18.34:80/wc/user/header/123456.png
	
	
	//文件上传  如头像/file/upload
	public static final String UPLOADURL=INDEXURL+"/file/upload";
	//http://112.74.18.34:80/wc/upload/file=
	
	//消息列表/message/list/{page}/{size}
	public static final String MESSAGE_URL=INDEXURL+"/message/list/";
	
	//添加车辆信息/usercar
	public static final String ADD_OR_MODIFY_CAR_URL=INDEXURL+"/usercar";
	//http://112.74.18.34:80/wc/usercar
	
	//车辆查询/usercar/list/{ownerId}
	public static final String SEARCH_CARLIST=INDEXURL+"/usercar/list/";
	
	//删除车辆/usercar/del/{id}
	public static final String DELETECAR=INDEXURL+"/usercar/del/";
	
	//订单接口
	public static final String ORDERURL=INDEXURL+"/order";
	
	//支付宝预支付/order/aliPrePay/{orderId}
	public static final String ALIPREPAY_URL=INDEXURL+"/order/aliPrePay/";
	
	//银联与支付接口/order/unionPayOrder/{orderId}
	public static final String UPPAY_URL=INDEXURL+"/order/unionPayOrder/";
	
	//银联支付完成回调接口/order/finishPay/{orderId}
	//public static final String UPPAY_END_URL=INDEXURL+"/order/finishPay/";不用
	public static final String UPPAY_END_URL=INDEXURL+"/order/unionPayNotify/";
	//http://112.74.18.34:80/wc/order/unionPayNotify/
	
	//微信预支付/order/wxprePayOrder//{orderId}
	public static final String WXPAY_URL=INDEXURL+"/order/wxprePayOrder/";
	
	//我的订单接口,订单列表/order/list/{page}/{size}?status=xxxx
	public static final String MYORDERLISTURL=INDEXURL+"/order/list";
	//http://112.74.18.34:80/wc/order/list/1/3?status=未支付
	//http://112.74.18.34:80/wc/order/list/1/3?status=未支付
	
	//取消订单接口/order/cancel/{orderId}
	public static final String ORDER_CANCEL_URL=INDEXURL+"/order/cancel/";
	
	//订单详情/order/{orderId}
	public static final String ORDERDETAIL_URL=INDEXURL+"/order/";
	
	//添加地址/user_address
	public static final String ADDADRESS_URL=INDEXURL+"/user_address";
	
	//地址列表查询
	public static final String ADRESSLIST_URL=INDEXURL+"/user_address/list";
	
	//删除地址/user_address/{id}
	public static final String DELETEADRESS_URL=INDEXURL+"/user_address/";
	
    //抢单列表/order/payed/list/{page}/{size}
	public static final String QIANGDAN_LIST_URL=INDEXURL+"/order/payed/list/";
	
	//已经抢到的订单列表/order/robbed/list/{page}/{size}
	public static final String ROBBEDLIST_URL=INDEXURL+"/order/robbed/list/";
	
	//洗护师抢单接口/order/rob/{orderId}
	public static final String ROBBURL=INDEXURL+"/order/rob/";
	
	//历史评论/comment/list/{page}/{size}/{targetOwnerId}
	public static final String HISTORYEVOLOTION_URL=INDEXURL+"/comment/list/";
	
	//公司评论/comment/company/{page}/{size}/{companyId}
	public static final String COMPANYHISTORY_COMMENT_URL=INDEXURL+"/comment/company/";
	
	//洗护师上下班
	public static final String WASHERWORK=INDEXURL+"/washer/work";
	
	//车位更新/company/parkingSpaceInfo
	public static final String UPDATEPARK=INDEXURL+"/company/parkingSpaceInfo";
	
	//设置商家车位数据/company/space/{companyId}/{num}
	public static final String SETTINGPARKCOUNT=INDEXURL+"/company/space/";
	
	//通知用户我已经到达/order/arrived/{orderId}
	public static final String NOTICEUSER_ARRIVE=INDEXURL+"/order/arrived/";
	
	//通知用户正在检查/order/checked/{orderId}
	public  static final String NOTICECHECKINGCAR_URL=INDEXURL+"/order/checked/";
	
	//通知用户洗车中
	public static final String NOTICEWASHERINGCAR_URL=INDEXURL+"/order/washing/";
	
	//通知用户清洗完/order/washed/{orderId}
	public static final String NOTICEWAHERED_URL=INDEXURL+"/order/washed/";
	
	//取消订单/order/cancel/{orderId}
	public static final String GREETOCANCELORDER_URL=INDEXURL+"/order/cancel/";
	
	//用户评论订单接口
	public static final String USERCOMMENT=INDEXURL+"/comment";
	
	//洗护师回复评论/comment/reply/{commentId}
	public static final String WASHERCOMMENTREPLY=INDEXURL+"/comment/reply/";
	
	//发送获设备token到服务器/xg/token
	public static final String SENDTOKEN2SERVICE_URL=INDEXURL+"/xg/token";
	
	//接受到推送后通知服务器我已经接受到推送/message/hook/{messageId}
	public static final String SENDG_I_GET_NOTICE_FROMSERVICE_URL=INDEXURL+"/message/hook/";
	
	//获取荣云token接口/gr/getGrTk
	public static final String RONGYUN_TOKEN_URL=INDEXURL+"/gr/getGrTk";
	
	//结算接口/settle/total
	public static final String SETTLEURL=INDEXURL+"/settle/total";
	//http://112.74.18.34:80/wc/settle/total
	//http://112.74.18.34:80/wc/jiesuan.html?token=
	public static final String SETTLEURL2=INDEXURL+"/static/jiesuan.html?token=";
    //http://112.74.18.34:80/wcs/tatic/jiesuan.html?token=9F1dt1N8dQsNdFZsZRx
	
	//意见反馈
	public static final String FEEDBACK_URL=INDEXURL+"/advice";
	//http://112.74.18.34:80/wc/advice
	//关于我们
	public static final String ABOUTUS_RUL="http://youchekuaixi.com/about.html";
	//优车快洗使用协议
	public static final String YCKX_USE_AGREEMENT="http://youchekuaixi.com/protocol.html";
	
	//窝窝头.优车快洗在应用宝下载的url
	public static final String YCKX_APK_DOWNLOAD_URL="http://android.myapp.com/myapp/detail.htm?apkName=com.my.kyxc";
	
}
