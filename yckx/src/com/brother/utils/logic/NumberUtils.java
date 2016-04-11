package com.brother.utils.logic;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class NumberUtils {
	
	
	
	/**两个整数相除四舍五入*/
    public static String InterDivisionInter(int chushu,int beichushu){
    	double x=chushu/beichushu;
    	NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(0);
        String s = formatter.format(x);
        return s;
    }
    
    
    /**两个整数相除四舍五入*/
    public static String InterDivisionInter(String chushu,String beichushu){
    	int mchushu=Integer.parseInt(chushu);
    	int mbeichushu=Integer.parseInt(beichushu);
    	double x=mchushu/mbeichushu;
    	NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(0);
        String s = formatter.format(x);
        return s;
    }
    
    
    /**double数据改变为两位小数，四舍五入*/
	public static double double2poins(double target){
		BigDecimal b= new BigDecimal(target);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}
	
}
