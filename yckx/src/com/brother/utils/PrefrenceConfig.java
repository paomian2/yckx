package com.brother.utils;

import com.brother.yckx.model.AdressEntity;
import com.brother.yckx.model.CardEntity;
import com.brother.yckx.model.OrderRegisterEntity;
import com.brother.yckx.model.UserEntity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefrenceConfig {

	public static final String USE_STATE="use_state";
	public static final String USER="user";
	public static final String CAR="car";
	public static final String ADRESS="adress";
	/**判断是否已经使用过该软件*/
	public static boolean isFistUse(Context context){
		SharedPreferences preferences=context.getSharedPreferences(USE_STATE, Context.MODE_PRIVATE);
		boolean isFist=preferences.getBoolean(USE_STATE, true);
		return isFist;
	}
	/**设置当前使用状态*/
	public static void setUseState(Context context,boolean isFirst){
		SharedPreferences preferences=context.getSharedPreferences(USE_STATE, Context.MODE_PRIVATE);
		Editor editor=preferences.edit();
		editor.putBoolean(USE_STATE, isFirst);
		editor.commit();
	}

	/**获取用户信息，进行登录*/
	public static UserEntity getUserMessage(Context context){
		SharedPreferences preferences=context.getSharedPreferences(USER, Context.MODE_PRIVATE);
		String userId=preferences.getString("userId", "");
		String userName=preferences.getString("userName", "");
		String userIamgeUrl=preferences.getString("userIamgeUrl", "");
		String userPhone=preferences.getString("userPhone", "");
		String userRole=preferences.getString("userRole", "");
		String userToken=preferences.getString("userToken", "");

		String userTotalScore_mhs=preferences.getString("userTotalScore_mhs", "0");
		String userCommentCount_mhs=preferences.getString("userCommentCount_mhs", "0");
		String userOpStatus_mhs=preferences.getString("userOpStatus_mhs", "");
		String userOrderCount_mhs=preferences.getString("userOrderCount_mhs", "0");
		boolean inWork_mhs=preferences.getBoolean("inWork_mhs", false);

		String userAdress_user=preferences.getString("userAdress_user", "");
		String userCard_user=preferences.getString("userCard_user", "");
		String userBalance_user=preferences.getString("userBalance_user", "0");
		String userCardTimes_user=preferences.getString("userCardTimes_user", "0");
		String userNotice_user=preferences.getString("userNotice_user", "1");
		UserEntity entity=new UserEntity(userId, userName, userIamgeUrl, userPhone, userRole, userToken, userTotalScore_mhs, userCommentCount_mhs, userOpStatus_mhs, userOrderCount_mhs, inWork_mhs, userAdress_user, userCard_user, userBalance_user, userCardTimes_user, userNotice_user);
		return entity;
	}

	public static void setLoginUserMessage(Context contet,UserEntity entity){
		SharedPreferences preferences=contet.getSharedPreferences(USER, Context.MODE_PRIVATE);
		Editor editor=preferences.edit();
		if(entity==null){
			return;
		}
		editor.putString("userId", entity.getUserId());
		editor.putString("userName", entity.getUserName());
		editor.putString("userIamgeUrl", entity.getUserIamgeUrl());
		editor.putString("userPhone", entity.getUserPhone());
		editor.putString("userRole", entity.getUserRole());
		editor.putString("userToken", entity.getUserToken());
		//美护师
		editor.putString("userTotalScore_mhs", entity.getUserTotalScore_mhs());
		editor.putString("userCommentCount_mhs", entity.getUserCommentCount_mhs());
		editor.putString("userOpStatus_mhs", entity.getUserOpStatus_mhs());
		editor.putString("userOrderCount_mhs", entity.getUserOrderCount_mhs());
		editor.putBoolean("inWork_mhs", entity.isInWork_mhs());
		//普通用户
		editor.putString("userAdress_user", entity.getUserAdress_user());
		editor.putString("userCard_user", entity.getUserCard_user());
		editor.putString("userBalance_user", entity.getUserBalance_user());
		editor.putString("userCardTimes_user", entity.getUserCardTimes_user());
		editor.putString("userNotice_user", entity.getUserNotice_user());
		editor.commit();
	}


	//设置默认车辆信息
	/**获取用户信息，进行登录*/
	public static CardEntity getCarMessage(Context context){
		SharedPreferences preferences=context.getSharedPreferences(CAR, Context.MODE_PRIVATE);
		String id=preferences.getString("id", "");
		String carNum=preferences.getString("carNum", "");
		String province=preferences.getString("province", "");
		String carBrand=preferences.getString("carBrand", "");
		String carColor=preferences.getString("carColor", "");
		String carImage=preferences.getString("carImage", "");
		boolean isDefault=preferences.getBoolean("isDefault", false);
		CardEntity entity=new CardEntity(id, carNum, province, carBrand, carColor, carImage, isDefault);
		return entity;
	}

	/**设置用户信息*/
	public static void setCarMessage(Context context,CardEntity entity){
		if(entity==null){
			return;
		}
		SharedPreferences preferences=context.getSharedPreferences(CAR, Context.MODE_PRIVATE);
		Editor editor=preferences.edit();
		editor.putString("id", entity.getId());
		editor.putString("carNum", entity.getCarNum());
		editor.putString("province", entity.getProvince());
		editor.putString("carBrand", entity.getCarBrand());
		editor.putString("carColor", entity.getCarColor());
		editor.putString("carImage", entity.getCarImage());
		editor.putBoolean("isDefault", entity.isDefault());
		editor.commit();
	}
	
	
	/**获取默认地址*/
	public static  AdressEntity getAdressDefalut(Context context){
		SharedPreferences preferences=context.getSharedPreferences(CAR, Context.MODE_PRIVATE);
		String id=preferences.getString("id", "");
		String phone=preferences.getString("phone", "");
		String provice=preferences.getString("provice", "");
		String carAdress=preferences.getString("carAdress", "");
		boolean isDefault=preferences.getBoolean("isDefault", false);
		AdressEntity entity=new AdressEntity(id,phone, provice, carAdress, isDefault);
		return entity;
	}
	
	/**设置默认地址信息*/
	public static void setAdressDefalut(Context context,AdressEntity entity){
		if(entity==null){
			return;
		}
		SharedPreferences preferences=context.getSharedPreferences(CAR, Context.MODE_PRIVATE);
		Editor editor=preferences.edit();
		editor.putString("id", entity.getId());
		editor.putString("phone", entity.getPhone());
		editor.putString("provice", entity.getProvince());
		editor.putString("carAdress", entity.getLocation());
		editor.putBoolean("isDefault", entity.isDefalut());
		editor.commit();
	}
	
	private static final String ORDER_REGISTERENTITY="ORDER_REGISTERENTITY";
	/**商家申请注册订单*/
	public static void setRegisterCompanyMessage(Context context,OrderRegisterEntity orderRegisterEntity){
		if(orderRegisterEntity==null){
			return;
		}
		SharedPreferences preferences=context.getSharedPreferences(ORDER_REGISTERENTITY, Context.MODE_PRIVATE);
		Editor editor=preferences.edit();
		editor.putString("companyName", orderRegisterEntity.getCompanyName());
		editor.putString("companyAdress", orderRegisterEntity.getCompanyAdress());
		editor.putString("companyPone", orderRegisterEntity.getCompanyPone());
		editor.putString("companyHangye", orderRegisterEntity.getCompanyHangye());
		editor.putString("companyServiceMain", orderRegisterEntity.getCompanyServiceMain());
		editor.putString("principal", orderRegisterEntity.getPrincipal());
		editor.putString("principalPhone", orderRegisterEntity.getPrincipalPhone());
		editor.commit();
	}
	public static OrderRegisterEntity getRegisterCompanyMessage(Context context){
		SharedPreferences preferences=context.getSharedPreferences(ORDER_REGISTERENTITY, Context.MODE_PRIVATE);
		String companyName=preferences.getString("companyName", "");
		String companyAdress=preferences.getString("companyAdress", "");
		String companyPone=preferences.getString("companyPone", "");
		String companyHangye=preferences.getString("companyHangye", "");
		String companyServiceMain=preferences.getString("companyServiceMain", "");
		String principal=preferences.getString("principal", "");
		String principalPhone=preferences.getString("principalPhone", "");
		OrderRegisterEntity orderEntity=new OrderRegisterEntity(companyName, companyAdress, companyPone, companyHangye, companyServiceMain, principal, principalPhone);
		return orderEntity;
	}
}
