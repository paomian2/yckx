package com.brother.utils.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {


	/**���ַ���ת��Ϊʱ���*/
	public static String getTime(String user_time) {  
		String re_time = null;  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��H��mm��");  
		Date d;  
		try {  
			d = sdf.parse(user_time);  
			long l = d.getTime();  
			String str = String.valueOf(l);  
			re_time = str.substring(0, 10);
			//תΪ13Ϊʱ���
			re_time=re_time+"000";
		} catch (ParseException e) {  
			e.printStackTrace();  
		}  
		return re_time;  
	}  



	/**��ʱ���תΪ�ַ���  */
	public static String getStrTime(String cc_time) {  
		String re_StrTime = null;  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��H��mm��");  
		// ���磺cc_time=1291778220  
		long lcc_time = Long.valueOf(cc_time);  
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));  
		return re_StrTime; 
	}
	
	/**����ʱ���ַ�����ȡlong��ʱ���*/
	public static long betweenTime(String startTime,String endTime){
		SimpleDateFormat dateformat_todate = new SimpleDateFormat("yyyy��MM��dd��H��mm��");
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
	
	
	/**�ַ�����ʱ���ת��Ϊ��ʽ����ʱ��*/
	public static String millsToDateTime(String str1){
		long time=Long.parseLong(str1);
		Date date=new Date(time);
		SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd��H��mm��");
		String str=format.format(date);
		return str;
	}
	
	
	/**�ַ�����ʱ���ת��Ϊʱ��*/
	public static String millsToDateTime(String str1,String partten){
		long time=Long.parseLong(str1);
		Date date=new Date(time);
		SimpleDateFormat format=new SimpleDateFormat(partten);
		String str=format.format(date);
		return str;
	}
	
	
	
	/**�ַ�����ʱ���ת��Ϊ��ʽ����ʱ��*/
	public static String millsToDateTime2(String str1){
		long time=Long.parseLong(str1);
		Date date=new Date(time);
		SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd��H��mm��");
		String str=format.format(date);
		return str;
	}

}