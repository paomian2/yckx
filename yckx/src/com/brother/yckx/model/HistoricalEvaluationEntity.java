package com.brother.yckx.model;

import android.graphics.drawable.Drawable;

public class HistoricalEvaluationEntity {
	
	private Drawable xhs_img;
	private String xhs_phone;
	private String user_evaluation;
	private Drawable[] upImages;
	private String product_service;
	private String serviceTime;
	private String xhs_respose;
	public Drawable getXhs_img() {
		return xhs_img;
	}
	public void setXhs_img(Drawable xhs_img) {
		this.xhs_img = xhs_img;
	}
	public String getXhs_phone() {
		return xhs_phone;
	}
	public void setXhs_phone(String xhs_phone) {
		this.xhs_phone = xhs_phone;
	}
	public String getUser_evaluation() {
		return user_evaluation;
	}
	public void setUser_evaluation(String user_evaluation) {
		this.user_evaluation = user_evaluation;
	}
	public Drawable[] getUpImages() {
		return upImages;
	}
	public void setUpImages(Drawable[] upImages) {
		this.upImages = upImages;
	}
	public String getProduct_service() {
		return product_service;
	}
	public void setProduct_service(String product_service) {
		this.product_service = product_service;
	}
	public String getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}
	public String getXhs_respose() {
		return xhs_respose;
	}
	public void setXhs_respose(String xhs_respose) {
		this.xhs_respose = xhs_respose;
	}
	public HistoricalEvaluationEntity(Drawable xhs_img, String xhs_phone,
			String user_evaluation, Drawable[] upImages,
			String product_service, String serviceTime, String xhs_respose) {
		super();
		this.xhs_img = xhs_img;
		this.xhs_phone = xhs_phone;
		this.user_evaluation = user_evaluation;
		this.upImages = upImages;
		this.product_service = product_service;
		this.serviceTime = serviceTime;
		this.xhs_respose = xhs_respose;
	}
	
	
	

}
