package com.brother.yckx.model;

import android.graphics.drawable.Drawable;

public class FormDetalisEntity {
	
	private Drawable leagueNameImg;
	private String leagueName;
	private Drawable productImg;
	private String productName;
	private String productTime;
	private String productPrice;
	private String productCard;
	private String productPhone;
	private String productStatus;
	private String ActualPay;
	public Drawable getLeagueNameImg() {
		return leagueNameImg;
	}
	public void setLeagueNameImg(Drawable leagueNameImg) {
		this.leagueNameImg = leagueNameImg;
	}
	public String getLeagueName() {
		return leagueName;
	}
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
	public Drawable getProductImg() {
		return productImg;
	}
	public void setProductImg(Drawable productImg) {
		this.productImg = productImg;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductTime() {
		return productTime;
	}
	public void setProductTime(String productTime) {
		this.productTime = productTime;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductCard() {
		return productCard;
	}
	public void setProductCard(String productCard) {
		this.productCard = productCard;
	}
	public String getProductPhone() {
		return productPhone;
	}
	public void setProductPhone(String productPhone) {
		this.productPhone = productPhone;
	}
	public String getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	public String getActualPay() {
		return ActualPay;
	}
	public void setActualPay(String actualPay) {
		ActualPay = actualPay;
	}
	public FormDetalisEntity(Drawable leagueNameImg, String leagueName,
			Drawable productImg, String productName, String productTime,
			String productPrice, String productCard, String productPhone,
			String productStatus, String actualPay) {
		super();
		this.leagueNameImg = leagueNameImg;
		this.leagueName = leagueName;
		this.productImg = productImg;
		this.productName = productName;
		this.productTime = productTime;
		this.productPrice = productPrice;
		this.productCard = productCard;
		this.productPhone = productPhone;
		this.productStatus = productStatus;
		ActualPay = actualPay;
	}

	
	
}
