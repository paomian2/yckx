package com.brother.utils.parse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClassFieldName {
	
	public static List<String> getNames(String cls){
		List<String> list=new ArrayList<String>();
		try {
			Class clazz = Class.forName("cls");//根据类名获得其对应的Class对象 写上你想要的类名就是了 注意是全名 如果有包的话要加上 比如java.Lang.String
			Field[] fields = clazz.getDeclaredFields();//根据Class对象获得属性 私有的也可以获得
			for(int i=0;i<fields.length;i++){
				//System.out.println(fields[i].getName());//打印每个属性的类型名字
				list.add(fields[i].getName());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
