package com.brother.utils.parse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClassFieldName {
	
	public static List<String> getNames(String cls){
		List<String> list=new ArrayList<String>();
		try {
			Class clazz = Class.forName("cls");//��������������Ӧ��Class���� д������Ҫ������������ ע����ȫ�� ����а��Ļ�Ҫ���� ����java.Lang.String
			Field[] fields = clazz.getDeclaredFields();//����Class���������� ˽�е�Ҳ���Ի��
			for(int i=0;i<fields.length;i++){
				//System.out.println(fields[i].getName());//��ӡÿ�����Ե���������
				list.add(fields[i].getName());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
