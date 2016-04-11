package com.brother.yckx.model;

import java.io.Serializable;

public class CardEntity implements Serializable{
	
	private String id;
	private String carNum;
	private String province;
	private String carBrand;
	private String carColor;
	private String carImage;
	private boolean isDefault;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCarBrand() {
		return carBrand;
	}
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	public String getCarColor() {
		return carColor;
	}
	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	public String getCarImage() {
		return carImage;
	}
	public void setCarImage(String carImage) {
		this.carImage = carImage;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public CardEntity(String id, String carNum, String province,
			String carBrand, String carColor, String carImage, boolean isDefault) {
		super();
		this.id = id;
		this.carNum = carNum;
		this.province = province;
		this.carBrand = carBrand;
		this.carColor = carColor;
		this.carImage = carImage;
		this.isDefault = isDefault;
	}
	@Override
	public String toString() {
		return "CardEntity [id=" + id + ", carNum=" + carNum + ", province="
				+ province + ", carBrand=" + carBrand + ", carColor="
				+ carColor + ", carImage=" + carImage + ", isDefault="
				+ isDefault + "]";
	}
	
	public CardEntity(){}
	
	

}
