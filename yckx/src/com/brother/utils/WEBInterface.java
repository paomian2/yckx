package com.brother.utils;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

public class WEBInterface {
	
	public static final String INDEXURL="http://112.74.18.34:80/wc";
	public static final String INDEXIMGURL="http://112.74.18.34:12002/";
	//http://112.74.18.34:12002//2016/01/10/72bcb757e32f14fc2927099e951d9d91_yckx_temp.png
	//http://112.74.18.34:12002/2016/01/14/8c8312316c3398147d62a49c6c294bca_yckx_temp.png
    
	//�����̼�
	public static final String MAP_STORELIST_URL=INDEXURL+"/company/lbs";
	//http://112.74.18.34:80/wc/company/lbs?lng=108.397434&lat=22.81906
	//http://112.74.18.34:80/wc/company/lbs?lng=110.282539&lat=25.261227
	//25.261227---110.282539(����վ)
	
	//�̼�����/company/types
	public static final String STORE_TYPE=INDEXURL+"/company/types";
	//http://112.74.18.34:80/wc/company/types
	
	//�̼�����/company/detail/companyid
	public static final String STORE_DETAIL=INDEXURL+"/company/detail/";
	//http://112.74.18.34:80/wc/company/detail/companyid=12
	//id�ôӸ����̼��л�ȡ�������еõ�
	
	//ע��/register
	public static final String REGISTERURL=INDEXURL+"/register";
	//http://112.74.18.34:80/wc/register

	//��¼/auth      //new StringEntity(new JSONObject(hasmp).toString(),"UTF-8")
	public static final String LOGINURL=INDEXURL+"/auth";
	//http://112.74.18.34:80/wc/auth
	
	//������֤��/register/checkcode/{phone}
	public static final String SENDCODEURL=INDEXURL+"/register/checkcode/";
	//http://112.74.18.34:80/wc/register/checkcode/15011428785
	
	//�޸�����/register/modifyPasswd/{phone}/{checkCode}/{passwd}
	public static final String MODIFYPWDURL=INDEXURL+"/register/modifyPasswd";
	//http://112.74.18.34:80/wc/register/modifyPasswd/15678467855/12345/12345678
	
	//�����޸�����/user/resetPasswd/{oldPasswd}/{newPasswd}
	public static final String MODIFYPWDONLINE_URL=INDEXURL+"/user/resetPasswd/";
	
	//�ϴ�ͷ��/user/header
	public static final String UPLOADHEADURL=INDEXURL+"/user/header";
	//http://112.74.18.34:80/wc/user/header/123456.png
	
	
	//�ļ��ϴ�  ��ͷ��/file/upload
	public static final String UPLOADURL=INDEXURL+"/file/upload";
	//http://112.74.18.34:80/wc/upload/file=
	
	//��Ϣ�б�/message/list/{page}/{size}
	public static final String MESSAGE_URL=INDEXURL+"/message/list/";
	
	//��ӳ�����Ϣ/usercar
	public static final String ADD_OR_MODIFY_CAR_URL=INDEXURL+"/usercar";
	//http://112.74.18.34:80/wc/usercar
	
	//������ѯ/usercar/list/{ownerId}
	public static final String SEARCH_CARLIST=INDEXURL+"/usercar/list/";
	
	//ɾ������/usercar/del/{id}
	public static final String DELETECAR=INDEXURL+"/usercar/del/";
	
	//�����ӿ�
	public static final String ORDERURL=INDEXURL+"/order";
	
	//֧����Ԥ֧��/order/aliPrePay/{orderId}
	public static final String ALIPREPAY_URL=INDEXURL+"/order/aliPrePay/";
	
	//������֧���ӿ�/order/unionPayOrder/{orderId}
	public static final String UPPAY_URL=INDEXURL+"/order/unionPayOrder/";
	
	//����֧����ɻص��ӿ�/order/finishPay/{orderId}
	//public static final String UPPAY_END_URL=INDEXURL+"/order/finishPay/";����
	public static final String UPPAY_END_URL=INDEXURL+"/order/unionPayNotify/";
	//http://112.74.18.34:80/wc/order/unionPayNotify/
	
	//΢��Ԥ֧��/order/wxprePayOrder//{orderId}
	public static final String WXPAY_URL=INDEXURL+"/order/wxprePayOrder/";
	
	//�ҵĶ����ӿ�,�����б�/order/list/{page}/{size}?status=xxxx
	public static final String MYORDERLISTURL=INDEXURL+"/order/list";
	//http://112.74.18.34:80/wc/order/list/1/3?status=δ֧��
	//http://112.74.18.34:80/wc/order/list/1/3?status=δ֧��
	
	//ȡ�������ӿ�/order/cancel/{orderId}
	public static final String ORDER_CANCEL_URL=INDEXURL+"/order/cancel/";
	
	//��������/order/{orderId}
	public static final String ORDERDETAIL_URL=INDEXURL+"/order/";
	
	//��ӵ�ַ/user_address
	public static final String ADDADRESS_URL=INDEXURL+"/user_address";
	
	//��ַ�б��ѯ
	public static final String ADRESSLIST_URL=INDEXURL+"/user_address/list";
	
	//ɾ����ַ/user_address/{id}
	public static final String DELETEADRESS_URL=INDEXURL+"/user_address/";
	
    //�����б�/order/payed/list/{page}/{size}
	public static final String QIANGDAN_LIST_URL=INDEXURL+"/order/payed/list/";
	
	//�Ѿ������Ķ����б�/order/robbed/list/{page}/{size}
	public static final String ROBBEDLIST_URL=INDEXURL+"/order/robbed/list/";
	
	//ϴ��ʦ�����ӿ�/order/rob/{orderId}
	public static final String ROBBURL=INDEXURL+"/order/rob/";
	
	//��ʷ����/comment/list/{page}/{size}/{targetOwnerId}
	public static final String HISTORYEVOLOTION_URL=INDEXURL+"/comment/list/";
	
	//��˾����/comment/company/{page}/{size}/{companyId}
	public static final String COMPANYHISTORY_COMMENT_URL=INDEXURL+"/comment/company/";
	
	//ϴ��ʦ���°�
	public static final String WASHERWORK=INDEXURL+"/washer/work";
	
	//��λ����/company/parkingSpaceInfo
	public static final String UPDATEPARK=INDEXURL+"/company/parkingSpaceInfo";
	
	//�����̼ҳ�λ����/company/space/{companyId}/{num}
	public static final String SETTINGPARKCOUNT=INDEXURL+"/company/space/";
	
	//֪ͨ�û����Ѿ�����/order/arrived/{orderId}
	public static final String NOTICEUSER_ARRIVE=INDEXURL+"/order/arrived/";
	
	//֪ͨ�û����ڼ��/order/checked/{orderId}
	public  static final String NOTICECHECKINGCAR_URL=INDEXURL+"/order/checked/";
	
	//֪ͨ�û�ϴ����
	public static final String NOTICEWASHERINGCAR_URL=INDEXURL+"/order/washing/";
	
	//֪ͨ�û���ϴ��/order/washed/{orderId}
	public static final String NOTICEWAHERED_URL=INDEXURL+"/order/washed/";
	
	//ȡ������/order/cancel/{orderId}
	public static final String GREETOCANCELORDER_URL=INDEXURL+"/order/cancel/";
	
	//�û����۶����ӿ�
	public static final String USERCOMMENT=INDEXURL+"/comment";
	
	//ϴ��ʦ�ظ�����/comment/reply/{commentId}
	public static final String WASHERCOMMENTREPLY=INDEXURL+"/comment/reply/";
	
	//���ͻ��豸token��������/xg/token
	public static final String SENDTOKEN2SERVICE_URL=INDEXURL+"/xg/token";
	
	//���ܵ����ͺ�֪ͨ���������Ѿ����ܵ�����/message/hook/{messageId}
	public static final String SENDG_I_GET_NOTICE_FROMSERVICE_URL=INDEXURL+"/message/hook/";
	
	//��ȡ����token�ӿ�/gr/getGrTk
	public static final String RONGYUN_TOKEN_URL=INDEXURL+"/gr/getGrTk";
	
	//����ӿ�/settle/total
	public static final String SETTLEURL=INDEXURL+"/settle/total";
	//http://112.74.18.34:80/wc/settle/total
	//http://112.74.18.34:80/wc/jiesuan.html?token=
	public static final String SETTLEURL2=INDEXURL+"/static/jiesuan.html?token=";
    //http://112.74.18.34:80/wcs/tatic/jiesuan.html?token=9F1dt1N8dQsNdFZsZRx
	
	//�������
	public static final String FEEDBACK_URL=INDEXURL+"/advice";
	//http://112.74.18.34:80/wc/advice
	//��������
	public static final String ABOUTUS_RUL="http://youchekuaixi.com/about.html";
	//�ų���ϴʹ��Э��
	public static final String YCKX_USE_AGREEMENT="http://youchekuaixi.com/protocol.html";
	
	//����ͷ.�ų���ϴ��Ӧ�ñ����ص�url
	public static final String YCKX_APK_DOWNLOAD_URL="http://android.myapp.com/myapp/detail.htm?apkName=com.my.kyxc";
	
}
