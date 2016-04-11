package com.brother.yckx.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class BusinessEntity implements Serializable,Comparable{
	
	private String companyId;
	private String companyName;
    private String address;
    private String description;
    private String companyImage;
    private String spaceCount;
    private String totalCount;
    private String lbsPoiId;
    private String location_lat;
    private String location_lng;
    private String avgScore;
    private String companyPhone;
    private List<ProductEntity> products;
    private CompanyTypeEntity companyType;
    private String serviceBeginHour;
    private String serviceBeginMinute;
    private String serviceEndHour;
    private String serviceEndMinute;
    private String companyMapImage;
    private String spacePrice;
    private String freeTime;
    private String saleMsg;
    private ManagerEntity manager;
    
    /**用于在地图中显示被选中的item*/
    private boolean isSelect;
    
    public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	
	/**专门用于定位置到商店的距离，用于排序*/
    private double distance;
	public String getLocation_lat() {
		return location_lat;
	}
	public void setLocation_lat(String location_lat) {
		this.location_lat = location_lat;
	}
	public String getLocation_lng() {
		return location_lng;
	}
	public void setLocation_lng(String location_lng) {
		this.location_lng = location_lng;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCompanyImage() {
		return companyImage;
	}
	public void setCompanyImage(String companyImage) {
		this.companyImage = companyImage;
	}
	public String getSpaceCount() {
		return spaceCount;
	}
	public void setSpaceCount(String spaceCount) {
		this.spaceCount = spaceCount;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getLbsPoiId() {
		return lbsPoiId;
	}
	public void setLbsPoiId(String lbsPoiId) {
		this.lbsPoiId = lbsPoiId;
	}
	public String getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(String avgScore) {
		this.avgScore = avgScore;
	}
	public String getCompanyPhone() {
		return companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	public List<ProductEntity> getProducts() {
		return products;
	}
	public void setProducts(List<ProductEntity> products) {
		this.products = products;
	}
	public CompanyTypeEntity getCompanyType() {
		return companyType;
	}
	public void setCompanyType(CompanyTypeEntity companyType) {
		this.companyType = companyType;
	}
	public String getServiceBeginHour() {
		return serviceBeginHour;
	}
	public void setServiceBeginHour(String serviceBeginHour) {
		this.serviceBeginHour = serviceBeginHour;
	}
	public String getServiceBeginMinute() {
		return serviceBeginMinute;
	}
	public void setServiceBeginMinute(String serviceBeginMinute) {
		this.serviceBeginMinute = serviceBeginMinute;
	}
	public String getServiceEndHour() {
		return serviceEndHour;
	}
	public void setServiceEndHour(String serviceEndHour) {
		this.serviceEndHour = serviceEndHour;
	}
	public String getServiceEndMinute() {
		return serviceEndMinute;
	}
	public void setServiceEndMinute(String serviceEndMinute) {
		this.serviceEndMinute = serviceEndMinute;
	}
	public String getCompanyMapImage() {
		return companyMapImage;
	}
	public void setCompanyMapImage(String companyMapImage) {
		this.companyMapImage = companyMapImage;
	}
	public String getSpacePrice() {
		return spacePrice;
	}
	public void setSpacePrice(String spacePrice) {
		this.spacePrice = spacePrice;
	}
	public String getFreeTime() {
		return freeTime;
	}
	public void setFreeTime(String freeTime) {
		this.freeTime = freeTime;
	}
	public String getSaleMsg() {
		return saleMsg;
	}
	public void setSaleMsg(String saleMsg) {
		this.saleMsg = saleMsg;
	}
	public ManagerEntity getManager() {
		return manager;
	}
	public void setManager(ManagerEntity manager) {
		this.manager = manager;
	}
	
	
	
	public BusinessEntity(String companyId, String companyName, String address,
			String description, String companyImage, String spaceCount,
			String totalCount, String lbsPoiId, String location_lat,
			String location_lng, String avgScore, String companyPhone,
			List<ProductEntity> products, CompanyTypeEntity companyType,
			String serviceBeginHour, String serviceBeginMinute,
			String serviceEndHour, String serviceEndMinute,
			String companyMapImage, String spacePrice, String freeTime,
			String saleMsg, ManagerEntity manager, double distance) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
		this.address = address;
		this.description = description;
		this.companyImage = companyImage;
		this.spaceCount = spaceCount;
		this.totalCount = totalCount;
		this.lbsPoiId = lbsPoiId;
		this.location_lat = location_lat;
		this.location_lng = location_lng;
		this.avgScore = avgScore;
		this.companyPhone = companyPhone;
		this.products = products;
		this.companyType = companyType;
		this.serviceBeginHour = serviceBeginHour;
		this.serviceBeginMinute = serviceBeginMinute;
		this.serviceEndHour = serviceEndHour;
		this.serviceEndMinute = serviceEndMinute;
		this.companyMapImage = companyMapImage;
		this.spacePrice = spacePrice;
		this.freeTime = freeTime;
		this.saleMsg = saleMsg;
		this.manager = manager;
		this.distance = distance;
	}
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		BusinessEntity tmp=(BusinessEntity) arg0;
		int result=tmp.distance<distance?1:(tmp.distance==distance?0:-1);
        if (result==0) {
            result=tmp.getCompanyName().indexOf(0)<getCompanyName().indexOf(0)?1:-1;
        }
        return result;
	}
	@Override
	public String toString() {
		return "BusinessEntity [companyId=" + companyId + ", companyName="
				+ companyName + ", address=" + address + ", description="
				+ description + ", companyImage=" + companyImage
				+ ", spaceCount=" + spaceCount + ", totalCount=" + totalCount
				+ ", lbsPoiId=" + lbsPoiId + ", location_lat=" + location_lat
				+ ", location_lng=" + location_lng + ", avgScore=" + avgScore
				+ ", companyPhone=" + companyPhone + ", products=" + products
				+ ", companyType=" + companyType + ", serviceBeginHour="
				+ serviceBeginHour + ", serviceBeginMinute="
				+ serviceBeginMinute + ", serviceEndHour=" + serviceEndHour
				+ ", serviceEndMinute=" + serviceEndMinute
				+ ", companyMapImage=" + companyMapImage + ", spacePrice="
				+ spacePrice + ", freeTime=" + freeTime + ", saleMsg="
				+ saleMsg + ", manager=" + manager + ", distance=" + distance
				+ "]";
	}
    
	
	
    
}
