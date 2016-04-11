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
 * ��Ҫ��������
 * 
 * Activity֮���ֵ���Դ洢������,��ת��ӵڶ���ҳ���ȡ��Ҫ��ֵ
 * 
 * 
 * */
public class GlobalServiceUtils {
	
	private static GlobalServiceUtils instance;
	private CardEntity carEntity;//ʵ������
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
	
	
	//��ͨ���ݽ���洢
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
	 * ���ó�����Ϣ,�����������ʹ�����ֵ���д���
	 * {@link #GarageManagerActivity}
	 * 
	 * ������޸ĳ�������ʹ��
	 * @see #getGloubalCarMessage
	 * 
	 * */
	public void setGlobalServiceCarMessage(CardEntity carEntity){
		synchronized (carEntity) {
			this.carEntity=carEntity;
		}
	}
	
	/**������Ϣ�Ĵ�ֵ��������޸ĳ�����Ϣ����ʹ��
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
	
	
	private List<OrderListEntity> orderEntityList;//ʵ������
	/**
	 * 
	 * ���ô洢�����б���Ϣ���û�{@link #FormActivity}��{@link #MoreFragment}֮��������ݴ���
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
	 * ��ȡ�洢�����б���Ϣ���û�{@link #FormActivity}��{@link #MoreFragment}֮��������ݴ���
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
	
	
	/**��ϸ����*/
	private OrderDetailEntity orderDatailEntity;
	
	/**������ϸ����{@link #FormDetailActivity}��{@link #MoreFragment_FormDetail}��ʹ��*/
	public void setGloubalServiceOrderDetailEntity(OrderDetailEntity orderEntity){
		if(orderDatailEntity!=null){
			synchronized (orderDatailEntity) {
				this.orderDatailEntity=orderEntity;
			}
		}else{
			this.orderDatailEntity=orderEntity;
		}
		
	}
	
	/**������ϸ����{@link #FormDetailActivity}��{@link #MoreFragment_FormDetail}��ʹ��*/
	public OrderDetailEntity getGloubalServiceOrderDetailEntity(){
		if(orderDatailEntity==null){
			orderDatailEntity=new OrderDetailEntity();
		}
		synchronized (orderDatailEntity) {
		   return orderDatailEntity;	
		}
	}
	
	
	private AdressEntity  adressEntity;
	/**�޸ĵ�ַ
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
	 * ʹ��*/
	
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
