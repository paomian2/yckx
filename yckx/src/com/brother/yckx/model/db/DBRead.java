package com.brother.yckx.model.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.brother.utils.WEBInterface;
import com.brother.yckx.control.activity.rongyun.model.RongIMFriend;
import com.brother.yckx.model.OrderDetailEntity;
import com.brother.yckx.model.OrderListEntity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
/**
 * �ų���ϴ���ݿ�:
 * ��Ҫ����1.���泵����Ϣ   2.����������������û�
 * 
 * */
@SuppressLint("SdCardPath")
public class DBRead {
	
	/**������Ϣ���ݿ�*/
    public static File carFile;
    static{
    	// Ĭ��λ��(baoming)
    	String dbFileDir = "/data/data/com.brother.yckx";
    	// �洢����
    	String sdcardState = Environment.getExternalStorageState();
    	if (sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			dbFileDir = Environment.getExternalStorageDirectory() + "/azyphone/cache";
		}
    	File fileDir = new File(dbFileDir);
		fileDir.mkdirs(); // �ļ�Ŀ¼�Ĵ���
		carFile = new File(dbFileDir, "carsplist.db");
    }
    
    /** ����Ƿ���ڳ�����Ϣ�ļ� */
	public static boolean isExistsCardbFile() {
		// û��ͨѶ��ȫFile
		File toFile = DBRead.carFile;
		if (!toFile.exists() || toFile.length() <= 0) {
			return false;
		}
		return true;
	}
	
	/** ��ȡcarFile������ݿ��ļ��е�carmsg������ڵ����� */
	public static ArrayList<CarClassInfo> readCardbClasslist() {
		ArrayList<CarClassInfo> classListInfos = new ArrayList<CarClassInfo>();
		// ��DB�ļ�
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		// ִ�в�ѯ��SQL��� select * from carmsg
		Cursor cursor = db.rawQuery("select * from carmsg", null);
		if (cursor.moveToFirst()) {
			do {
				String zimu=cursor.getString(cursor.getColumnIndex("zimu"));
				String cid=cursor.getString(cursor.getColumnIndex("cid"));
				String name=cursor.getString(cursor.getColumnIndex("name"));
				CarClassInfo carlistInfo = new CarClassInfo(zimu,cid,name);
				classListInfos.add(carlistInfo);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return classListInfos;
	}
	
	/**
	   SQLģ����ѯ���
	����SQLģ����ѯ��ʹ��like�Ƚ��֣�����SQL���ͨ�������ο����£�
	����1��LIKE'Mc%' ����������ĸ Mc ��ͷ�������ַ������� McBadden����
	����2��LIKE'%inger' ����������ĸ inger ��β�������ַ������� Ringer��Stringer����
	����3��LIKE'%en%' ���������κ�λ�ð�����ĸ en �������ַ������� Bennet��Green��McBadden����
	����4��LIKE'_heryl' ����������ĸ heryl ��β������������ĸ�����ƣ��� Cheryl��Sheryl����
	����5��LIKE'[CK]ars[eo]n' �����������ַ�����Carsen��Karsen��Carson �� Karson���� Carson����
	����6��LIKE'[M-Z]inger' ���������ַ��� inger ��β���Դ� M �� Z ���κε�����ĸ��ͷ���������ƣ��� Ringer����
	����7��LIKE'M[^c]%' ����������ĸ M ��ͷ�����ҵڶ�����ĸ���� c ���������ƣ���MacFeather����
	*/
	
	/**ģ����ѯ�������û�����ĺ��ֲ�ѯ������Ϣ*/
	public static ArrayList<CarClassInfo> readCardbClasslistForKey(String key){
		ArrayList<CarClassInfo> classListInfos = new ArrayList<CarClassInfo>();
		// ��DB�ļ�
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		// ִ�в�ѯ��SQL��� select * from carmsg
		String current_sql_sel = "SELECT  * FROM carmsg where name like '%"+key+"%'";
		Cursor cursor = db.rawQuery(current_sql_sel, null);
		if (cursor.moveToFirst()) {
			do {
				String zimu=cursor.getString(cursor.getColumnIndex("zimu"));
				String cid=cursor.getString(cursor.getColumnIndex("cid"));
				String name=cursor.getString(cursor.getColumnIndex("name"));
				CarClassInfo carlistInfo = new CarClassInfo(zimu,cid,name);
				classListInfos.add(carlistInfo);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return classListInfos;
	}
	
	
	/**
	 * ����������������û�
	 * @param orderlistentity:
	 *        ������û�ʵ��
	 * @param isOwner:
	 *        �Ƿ��ǳ����û���true�ǲ��복��ʵ��,false�ǲ�������ʦʵ��
	 * 
	 * */
	public static void addRongIMUser(OrderListEntity orderlistentity,boolean isInsertOwner){
		// ��DB�ļ�
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		if(isInsertOwner){
			if(orderlistentity.getOwnerEntity()==null)
				return;
			String userId=orderlistentity.getOwnerEntity().getId();
			String userName=orderlistentity.getOwnerEntity().getUserPhone();
			String userImgUrl=orderlistentity.getOwnerEntity().getUserImageUrl();
			String sql="CREATE TABLE IF NOT EXISTS RongIMOwnerUser(userId varchar,userName varchar,userImgUrl varchar)";
			//������
			db.execSQL(sql);
			String insertSQL="insert into RongIMOwnerUser(userId,userName,userImgUrl) values('"+userId+"','"+userName+"','"+userImgUrl+"')";
			db.execSQL(insertSQL);
			db.close();
		}else{
			if(orderlistentity.getWasherEntity()==null)
				return;
			String userId=orderlistentity.getWasherEntity().getWaherId();
			String userName=orderlistentity.getWasherEntity().getWasherName();
			String userImgUrl=orderlistentity.getWasherEntity().getUserImageUrl();
			String sql="CREATE TABLE IF NOT EXISTS RongIMWasherUser(userId varchar,userName varchar,userImgUrl varchar)";
			//������
			db.execSQL(sql);
			String insertSQL="insert into RongIMWasherUser(userId,userName,userImgUrl) values('"+userId+"','"+userName+"','"+userImgUrl+"')";
			db.execSQL(insertSQL);
			db.close();
		}
	}
	
	
	/**
	 * ����������������û�
	 * @param orderlistentity:
	 *        ������û�ʵ��
	 * @param isOwner:
	 *        �Ƿ��ǳ����û���true�ǳ����û�,false������ʦ
	 * 
	 * */
	public static void addRongIMUser(OrderDetailEntity orderlistentity,boolean isInsertOwner){
		// ��DB�ļ�
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		if(isInsertOwner){
			if(orderlistentity.getOwnerEntity()==null)
				return;
			String userId=orderlistentity.getOwnerEntity().getId();
			String userName=orderlistentity.getOwnerEntity().getUserPhone();
			String userImgUrl=orderlistentity.getOwnerEntity().getUserImageUrl();
			String sql="CREATE TABLE IF NOT EXISTS RongIMOwnerUser(userId varchar,userName varchar,userImgUrl varchar)";
			//������
			db.execSQL(sql);
			String insertSQL="insert into RongIMOwnerUser(userId,userName,userImgUrl) values('"+userId+"','"+userName+"','"+userImgUrl+"')";
			db.execSQL(insertSQL);
			db.close();
		}else{
			if(orderlistentity.getWasherEntity()==null)
				return;
			String userId=orderlistentity.getWasherEntity().getWaherId();
			String userName=orderlistentity.getWasherEntity().getWasherName();
			String userImgUrl=orderlistentity.getWasherEntity().getUserImageUrl();
			String sql="CREATE TABLE IF NOT EXISTS RongIMWasherUser(userId varchar,userName varchar,userImgUrl varchar)";
			//������
			db.execSQL(sql);
			String insertSQL="insert into RongIMWasherUser(userId,userName,userImgUrl) values('"+userId+"','"+userName+"','"+userImgUrl+"')";
			db.execSQL(insertSQL);
			db.close();
		}
	}
	
	
	/**
	 * ��ѯ���ݿ��ȡ������������û�
	 * @param isQueryOwner:��ѯ���ǳ�����������ʦ��trueΪ������falseΪ����ʦ
	 * 
	 * */
	public static List<RongIMFriend> queryRongIMUser(boolean isQueryOwner){
		List<RongIMFriend> all = new ArrayList<RongIMFriend>(); //��ʱֻ��String
		// ��DB�ļ�
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		 String selectSQL="";
		if(isQueryOwner){
		    selectSQL="SELECT * FROM RongIMOwnerUser";
		}else{
			selectSQL="SELECT * FROM RongIMWasherUser";
		}
		Cursor result=db.rawQuery(selectSQL, null);//ִ�в�ѯ���
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()  )//����ѭ���ķ�ʽ��ѯ����
        {
			String userId=result.getString(result.getColumnIndex("userId"));
			String userName=result.getString(result.getColumnIndex("userName"));
			String userImgUrl=result.getString(result.getColumnIndex("userImgUrl"));
			RongIMFriend friend=new RongIMFriend(userId, userName,userImgUrl);
			all.add(friend);
        } 
        db.close();
        return all;
	}
	
	
	/**���������û���*/
	public static void initRongIMUserTable(){
		// ��DB�ļ�
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		String sql1="CREATE TABLE IF NOT EXISTS RongIMOwnerUser(userId varchar,userName varchar,userImgUrl varchar)";
		//������
		db.execSQL(sql1);
		String sql2="CREATE TABLE IF NOT EXISTS RongIMWasherUser(userId varchar,userName varchar,userImgUrl varchar)";
		//������
		db.execSQL(sql2);
		db.close();
	}
	
}
