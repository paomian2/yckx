package com.brother.utils.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {


	/**把字符串转变为时间戳*/
	public static String getTime(String user_time) {  
		String re_time = null;  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日H点mm分");  
		Date d;  
		try {  
			d = sdf.parse(user_time);  
			long l = d.getTime();  
			String str = String.valueOf(l);  
			re_time = str.substring(0, 10);
			//转为13为时间戳
			re_time=re_time+"000";
		} catch (ParseException e) {  
			e.printStackTrace();  
		}  
		return re_time;  
	}  



	/**将时间戳转为字符串  */
	public static String getStrTime(String cc_time) {  
		String re_StrTime = null;  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日H点mm分");  
		// 例如：cc_time=1291778220  
		long lcc_time = Long.valueOf(cc_time);  
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));  
		return re_StrTime; 
	}
	
	/**根据时间字符串获取long型时间戳*/
	public static long betweenTime(String startTime,String endTime){
		SimpleDateFormat dateformat_todate = new SimpleDateFormat("yyyy年MM月dd日H点mm分");
		Date startDate=null;
		Date endDate=null;
		long betweenTime=-9999;
		try {
			 startDate=dateformat_todate.parse(startTime);
			 endDate=dateformat_todate.parse(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(startDate!=null&&endDate!=null){
			betweenTime=endDate.getTime()-startDate.getTime();
		}
		return betweenTime;
	}
	
	
	/**字符串的时间戳转换为格式化的时间*/
	public static String millsToDateTime(String str1){
		long time=Long.parseLong(str1);
		Date date=new Date(time);
		SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日H点mm分");
		String str=format.format(date);
		return str;
	}
	
	
	/**字符串的时间戳转化为时间*/
	public static String millsToDateTime(String str1,String partten){
		long time=Long.parseLong(str1);
		Date date=new Date(time);
		SimpleDateFormat format=new SimpleDateFormat(partten);
		String str=format.format(date);
		return str;
	}
	
	
	
	/**字符串的时间戳转换为格式化的时间*/
	public static String millsToDateTime2(String str1){
		long time=Long.parseLong(str1);
		Date date=new Date(time);
		SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日H点mm分");
		String str=format.format(date);
		return str;
	}

}