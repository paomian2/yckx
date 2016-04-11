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
 * 优车快洗数据库:
 * 主要用于1.保存车辆信息   2.保存融云中聊天的用户
 * 
 * */
@SuppressLint("SdCardPath")
public class DBRead {
	
	/**车辆信息数据库*/
    public static File carFile;
    static{
    	// 默认位置(baoming)
    	String dbFileDir = "/data/data/com.brother.yckx";
    	// 存储卡上
    	String sdcardState = Environment.getExternalStorageState();
    	if (sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			dbFileDir = Environment.getExternalStorageDirectory() + "/azyphone/cache";
		}
    	File fileDir = new File(dbFileDir);
		fileDir.mkdirs(); // 文件目录的创建
		carFile = new File(dbFileDir, "carsplist.db");
    }
    
    /** 检测是否存在车辆信息文件 */
	public static boolean isExistsCardbFile() {
		// 没有通讯大全File
		File toFile = DBRead.carFile;
		if (!toFile.exists() || toFile.length() <= 0) {
			return false;
		}
		return true;
	}
	
	/** 读取carFile这个数据库文件中的carmsg这个表内的数据 */
	public static ArrayList<CarClassInfo> readCardbClasslist() {
		ArrayList<CarClassInfo> classListInfos = new ArrayList<CarClassInfo>();
		// 打开DB文件
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		// 执行查询的SQL语句 select * from carmsg
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
	   SQL模糊查询语句
	　　SQL模糊查询，使用like比较字，加上SQL里的通配符，请参考以下：
	　　1、LIKE'Mc%' 将搜索以字母 Mc 开头的所有字符串（如 McBadden）。
	　　2、LIKE'%inger' 将搜索以字母 inger 结尾的所有字符串（如 Ringer、Stringer）。
	　　3、LIKE'%en%' 将搜索在任何位置包含字母 en 的所有字符串（如 Bennet、Green、McBadden）。
	　　4、LIKE'_heryl' 将搜索以字母 heryl 结尾的所有六个字母的名称（如 Cheryl、Sheryl）。
	　　5、LIKE'[CK]ars[eo]n' 将搜索下列字符串：Carsen、Karsen、Carson 和 Karson（如 Carson）。
	　　6、LIKE'[M-Z]inger' 将搜索以字符串 inger 结尾、以从 M 到 Z 的任何单个字母开头的所有名称（如 Ringer）。
	　　7、LIKE'M[^c]%' 将搜索以字母 M 开头，并且第二个字母不是 c 的所有名称（如MacFeather）。
	*/
	
	/**模糊查询，根据用户输入的汉字查询车辆信息*/
	public static ArrayList<CarClassInfo> readCardbClasslistForKey(String key){
		ArrayList<CarClassInfo> classListInfos = new ArrayList<CarClassInfo>();
		// 打开DB文件
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		// 执行查询的SQL语句 select * from carmsg
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
	 * 保存融云中聊天的用户
	 * @param orderlistentity:
	 *        保存的用户实体
	 * @param isOwner:
	 *        是否是车主用户，true是插入车主实体,false是插入美护师实体
	 * 
	 * */
	public static void addRongIMUser(OrderListEntity orderlistentity,boolean isInsertOwner){
		// 打开DB文件
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		if(isInsertOwner){
			if(orderlistentity.getOwnerEntity()==null)
				return;
			String userId=orderlistentity.getOwnerEntity().getId();
			String userName=orderlistentity.getOwnerEntity().getUserPhone();
			String userImgUrl=orderlistentity.getOwnerEntity().getUserImageUrl();
			String sql="CREATE TABLE IF NOT EXISTS RongIMOwnerUser(userId varchar,userName varchar,userImgUrl varchar)";
			//创建表
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
			//创建表
			db.execSQL(sql);
			String insertSQL="insert into RongIMWasherUser(userId,userName,userImgUrl) values('"+userId+"','"+userName+"','"+userImgUrl+"')";
			db.execSQL(insertSQL);
			db.close();
		}
	}
	
	
	/**
	 * 保存融云中聊天的用户
	 * @param orderlistentity:
	 *        保存的用户实体
	 * @param isOwner:
	 *        是否是车主用户，true是车主用户,false是美护师
	 * 
	 * */
	public static void addRongIMUser(OrderDetailEntity orderlistentity,boolean isInsertOwner){
		// 打开DB文件
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		if(isInsertOwner){
			if(orderlistentity.getOwnerEntity()==null)
				return;
			String userId=orderlistentity.getOwnerEntity().getId();
			String userName=orderlistentity.getOwnerEntity().getUserPhone();
			String userImgUrl=orderlistentity.getOwnerEntity().getUserImageUrl();
			String sql="CREATE TABLE IF NOT EXISTS RongIMOwnerUser(userId varchar,userName varchar,userImgUrl varchar)";
			//创建表
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
			//创建表
			db.execSQL(sql);
			String insertSQL="insert into RongIMWasherUser(userId,userName,userImgUrl) values('"+userId+"','"+userName+"','"+userImgUrl+"')";
			db.execSQL(insertSQL);
			db.close();
		}
	}
	
	
	/**
	 * 查询数据库获取融云中聊天的用户
	 * @param isQueryOwner:查询的是车主还是美护师，true为车主，false为美护师
	 * 
	 * */
	public static List<RongIMFriend> queryRongIMUser(boolean isQueryOwner){
		List<RongIMFriend> all = new ArrayList<RongIMFriend>(); //此时只是String
		// 打开DB文件
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		 String selectSQL="";
		if(isQueryOwner){
		    selectSQL="SELECT * FROM RongIMOwnerUser";
		}else{
			selectSQL="SELECT * FROM RongIMWasherUser";
		}
		Cursor result=db.rawQuery(selectSQL, null);//执行查询语句
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()  )//采用循环的方式查询数据
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
	
	
	/**创建融云用户表*/
	public static void initRongIMUserTable(){
		// 打开DB文件
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(carFile, null);
		String sql1="CREATE TABLE IF NOT EXISTS RongIMOwnerUser(userId varchar,userName varchar,userImgUrl varchar)";
		//创建表
		db.execSQL(sql1);
		String sql2="CREATE TABLE IF NOT EXISTS RongIMWasherUser(userId varchar,userName varchar,userImgUrl varchar)";
		//创建表
		db.execSQL(sql2);
		db.close();
	}
	
}
