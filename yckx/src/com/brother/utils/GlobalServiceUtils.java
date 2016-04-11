package com.brother.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.brother.yckx.control.activity.owner.AdressActivity;
import com.brother.yckx.model.AdressEntity;
import com.brother.yckx.model.CardEntity;
import com.brother.yckx.model.OrderDetailEntity;
import com.brother.yckx.model.OrderListEntity;
import com.brother.yckx.model.QiangDanListEntity;

/**
 * 主要用来传真
 * 
 * Activity之间的值可以存储在这里,跳转后从第二个页面获取想要的值
 * 
 * 
 * */
public class GlobalServiceUtils {
	
	private static GlobalServiceUtils instance;
	private CardEntity carEntity;//实体数据
	private GlobalServiceUtils(){
		orderEntityList=new ArrayList<OrderListEntity>();
		orderDatailEntity=new OrderDetailEntity();
		adressEntity=new AdressEntity();
		qiangdanListEntity=new QiangDanListEntity();
		orderListEntity=new OrderListEntity();
		carEntity=new CardEntity();
	}
	
	
	public static GlobalServiceUtils getInstance(){
		if(instance==null){
			instance=new GlobalServiceUtils();
		}
		return instance;
	}
	
	
	//普通数据进入存储
	private static final HashMap serviceHasmap=new HashMap();
	
	public static void setGloubalServiceSession(String name,Object obj){
		synchronized (serviceHasmap) {
			serviceHasmap.put(name, obj);
		}
	}
	
	public static Object getGloubalServiceSession(String name){
		synchronized (serviceHasmap) {
			return serviceHasmap.get(name);
		}
	}
	
	
	/**
	 * 设置车辆信息,车辆管理界面使用这个值进行传递
	 * {@link #GarageManagerActivity}
	 * 
	 * 在添加修改车辆类中使用
	 * @see #getGloubalCarMessage
	 * 
	 * */
	public void setGlobalServiceCarMessage(CardEntity carEntity){
		synchronized (carEntity) {
			this.carEntity=carEntity;
		}
	}
	
	/**车辆信息的传值，早添加修改车辆信息类中使用
	 * 
	 * {@link #AddCarActivity}
	 * 
	 * */
	public CardEntity GlobalServiceCarMessage(){
		if(carEntity==null){
			carEntity=new CardEntity();
		}
		synchronized (carEntity) {
			return carEntity;
		}
	}
	
	
	private List<OrderListEntity> orderEntityList;//实体数据
	/**
	 * 
	 * 设置存储订单列表信息，用户{@link #FormActivity}和{@link #MoreFragment}之间进行数据传递
	 * 
	 * */
	public void setGloubalServiceOrderEntityList(List<OrderListEntity> orderlist){
		if(orderEntityList!=null){
			synchronized (orderEntityList) {
				orderEntityList=orderlist;
			}
		}else{
			orderEntityList=orderlist;
		}
		
	}
	/**
	 * 
	 * 获取存储订单列表信息，用户{@link #FormActivity}和{@link #MoreFragment}之间进行数据传递
	 * 
	 * */
	public List<OrderListEntity> getGloubalServiceOrderEntityList(){
		if(orderEntityList!=null){
			synchronized (orderEntityList) {
				return orderEntityList;
			}
		}else{
			return orderEntityList;
		}
		
	}
	
	
	/**详细订单*/
	private OrderDetailEntity orderDatailEntity;
	
	/**设置详细订单{@link #FormDetailActivity}到{@link #MoreFragment_FormDetail}中使用*/
	public void setGloubalServiceOrderDetailEntity(OrderDetailEntity orderEntity){
		if(orderDatailEntity!=null){
			synchronized (orderDatailEntity) {
				this.orderDatailEntity=orderEntity;
			}
		}else{
			this.orderDatailEntity=orderEntity;
		}
		
	}
	
	/**设置详细订单{@link #FormDetailActivity}到{@link #MoreFragment_FormDetail}中使用*/
	public OrderDetailEntity getGloubalServiceOrderDetailEntity(){
		if(orderDatailEntity==null){
			orderDatailEntity=new OrderDetailEntity();
		}
		synchronized (orderDatailEntity) {
		   return orderDatailEntity;	
		}
	}
	
	
	private AdressEntity  adressEntity;
	/**修改地址
	 * 
	 * {@link AdressActivity}
	 * */
	
	public void setGloubalServiveAdress(AdressEntity entity){
		if(adressEntity!=null){
			synchronized (adressEntity) {
				this.adressEntity=entity;
			}
		}else{
			this.adressEntity=entity;
		}
		
	}
	
	public AdressEntity getGloubalServiceAdress(){
		if(adressEntity==null){
			adressEntity=new AdressEntity();
		}
		synchronized (adressEntity) {
			return adressEntity;
		}
	}
	
	
	/** 
	 * 
	 * {@link #WasherOderDetailActivity}
	 * 使用*/
	
	private QiangDanListEntity qiangdanListEntity;
	
	public void setGloubalServiceQiangDan(QiangDanListEntity qiangdanListEntity){
		synchronized (this.qiangdanListEntity) {
			this.qiangdanListEntity=qiangdanListEntity;
		}
	}
	
	public QiangDanListEntity getGloubalServiceQiangDan(){
		if(qiangdanListEntity==null){
			qiangdanListEntity=new QiangDanListEntity();
		}
		synchronized (qiangdanListEntity) {
			return qiangdanListEntity;
		}
	}
	
	private OrderListEntity orderListEntity;
	/**
	 * {@link #WasherOderDetailActivity}
	 * 
	 * */
	public void setGloubalServiceOrderListEntity(OrderListEntity orderListEntity){
		synchronized (this.orderListEntity) {
			this.orderListEntity=orderListEntity;
		}
	}
	
	public OrderListEntity getGloubalServiceOrderListEntity(){
		if(orderListEntity==null){
			orderListEntity=new OrderListEntity();
		}
		synchronized (orderListEntity) {
			return orderListEntity;
		}
	}

}
