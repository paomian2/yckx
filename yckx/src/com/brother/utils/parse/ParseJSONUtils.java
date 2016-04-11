package com.brother.utils.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brother.yckx.control.activity.owner.GarageManagerActivity;
import com.brother.yckx.control.activity.washer.WasherMainActivity;
import com.brother.yckx.model.AdressEntity;
import com.brother.yckx.model.BusinessEntity;
import com.brother.yckx.model.CarParkEntity;
import com.brother.yckx.model.CardEntity;
import com.brother.yckx.model.CommentEntity;
import com.brother.yckx.model.CompanyEntity;
import com.brother.yckx.model.CompanyTypeEntity;
import com.brother.yckx.model.HistoryCommentEntity;
import com.brother.yckx.model.ManagerEntity;
import com.brother.yckx.model.MessageEntity;
import com.brother.yckx.model.OrderDetailEntity;
import com.brother.yckx.model.OrderListEntity;
import com.brother.yckx.model.OrderSubEntity;
import com.brother.yckx.model.OwnerEntity;
import com.brother.yckx.model.ProductEntity;
import com.brother.yckx.model.QiangDanListEntity;
import com.brother.yckx.model.SenderEntity;
import com.brother.yckx.model.WasherEntity;



public class ParseJSONUtils {

	/**解析地图搜索到的商家信息*/
	public static List<BusinessEntity> parseBusinessJSOn(String parseJson){
		List<BusinessEntity> list=new ArrayList<BusinessEntity>(); 
		try {
			JSONObject jSONObject=new JSONObject(parseJson);
			String code=jSONObject.getString("code");
			if(code.equals("0")){
				JSONArray jSONArray=jSONObject.getJSONArray("nearCompanyList");
				if(jSONArray!=null){
					for(int i=0;i<jSONArray.length();i++){
						JSONObject obj=jSONArray.getJSONObject(i);
						//String tempjson=obj.toString();
						//BusinessEntity businessEntity=objectMapper.readValue(tempjson, BusinessEntity.class);
						String companyId="";
						try{
							companyId=obj.getString("companyId");
						}catch(Exception e){
							e.printStackTrace();
						}
						String companyName="";
						try{
							companyName=obj.getString("companyName");
						}catch(Exception e){
							e.printStackTrace();
						}
						String address="";
						try{
							address=obj.getString("address");
						}catch(Exception e){
							e.printStackTrace();
						}
						String description="";
						try{
							description=obj.getString("description");
						}catch(Exception e){
							e.printStackTrace();
						}
						String companyImage="";
						try{
							companyImage=obj.getString("companyImage");
						}catch(Exception e){
							e.printStackTrace();
						}
						String spaceCount="";
						try{
							spaceCount=obj.getString("spaceCount");
						}catch(Exception e){
							e.printStackTrace();
						}
						String totalCount="";
						try{
							totalCount=obj.getString("totalCount");
						}catch(Exception e){
							e.printStackTrace();
						}
						String lbsPoiId="";
						try{
							lbsPoiId=obj.getString("lbsPoiId");
						}catch(Exception e){
							lbsPoiId="";
							e.printStackTrace();
						}
						JSONArray childArray=null;
						try{
							childArray=obj.getJSONArray("location");
						}catch(Exception e){
							e.printStackTrace();
						}
						String location_lat="";
						String location_lng="";
						if(childArray!=null){
							double x=childArray.getDouble(1);
							double y=childArray.getDouble(0);
							location_lat=x+"";
							location_lng=y+"";
						}
						String avgScore="";
						try{
							avgScore=obj.getString("avgScore");
						}catch(Exception e){
							e.printStackTrace();
						}
						String companyPhone="";
						try{
							companyPhone=obj.getString("companyPhone");
						}catch(Exception e){
							e.printStackTrace();
						}
						JSONArray jsonArray_products=null;
						List<ProductEntity> products=null;
						try{
							jsonArray_products=obj.getJSONArray("products");
							products=getProductEntity(jsonArray_products);
						}catch(Exception e){
							e.printStackTrace();
						}
						JSONObject companyTypeJSONObj=null;
						CompanyTypeEntity companyType=null;
						try{
							companyTypeJSONObj=obj.getJSONObject("companyType");
							companyType=getCompanyTypeEntity(companyTypeJSONObj);
						}catch(Exception e){
							e.printStackTrace();
						}
						String serviceBeginHour="";
						try{
							serviceBeginHour=obj.getString("serviceBeginHour");
						}catch(Exception e){
							e.printStackTrace();
						}
						String serviceBeginMinute="";
						try{
							serviceBeginMinute=obj.getString("serviceBeginMinute");
						}catch(Exception e){
							e.printStackTrace();
						}
						String serviceEndHour="";
						try{
							serviceEndHour=obj.getString("serviceEndHour");
						}catch(Exception e){
							e.printStackTrace();
						}

						String serviceEndMinute="";
						try{
							serviceEndMinute=obj.getString("serviceEndMinute");
						}catch(Exception e){
							e.printStackTrace();
						}
						String companyMapImage="";
						try{
							companyMapImage=obj.getString("companyMapImage");
						}catch(Exception e){
							e.printStackTrace();
						}
						String spacePrice="";
						try{
							spacePrice=obj.getString("spacePrice");
						}catch(Exception e){
							e.printStackTrace();
						}
						String freeTime="";
						try{
							freeTime=obj.getString("freeTime");
						}catch(Exception e){
							e.printStackTrace();
						}
						String saleMsg="";
						try{
							saleMsg=obj.getString("saleMsg");
						}catch(Exception e){
							e.printStackTrace();
						}
						JSONObject managerEntityObj=null;
						ManagerEntity manager=null;
						try{
							managerEntityObj=obj.getJSONObject("manager");
							manager=getManagerEntity(managerEntityObj);
						}catch(Exception e){
							e.printStackTrace();
						}
						BusinessEntity businessEntity=new BusinessEntity(companyId, companyName, address, description, companyImage, spaceCount, totalCount, lbsPoiId, location_lat,location_lng,avgScore, companyPhone, products, companyType, serviceBeginHour, serviceBeginMinute, serviceEndHour, serviceEndMinute, companyMapImage, spacePrice, freeTime, saleMsg, manager,0);
						list.add(businessEntity);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("--->>parseBusinessJSOn()解析数据失败");
			e.printStackTrace();
		}
		return list;
	}


	private static CompanyTypeEntity getCompanyTypeEntity(JSONObject companyTypeJSONObj){
		CompanyTypeEntity entity = null;
		if(companyTypeJSONObj!=null){
			try {
				String id="";
				try{
					id=companyTypeJSONObj.getString("id");
				}catch(Exception e){
					e.printStackTrace();
				}
				String name="";
				try{
					name=companyTypeJSONObj.getString("name");
				}catch(Exception e){
					e.printStackTrace();
				}
				String icon="";
				try{
					icon=companyTypeJSONObj.getString("icon");
				}catch(Exception e){
					e.printStackTrace();
				}
				String mapIcon="";
				try{
					mapIcon=companyTypeJSONObj.getString("mapIcon");
				}catch(Exception e){
					e.printStackTrace();
				}
				entity=new CompanyTypeEntity(id, name, icon, mapIcon);
			} catch (Exception e) {
				System.out.println("--->>getCompanyTypeEntity()解析数据失败");
				e.printStackTrace();
			}
		}
		return entity;
	}


	private  static ManagerEntity getManagerEntity(JSONObject managerObj){
		ManagerEntity entity=null;
		if(managerObj!=null){
			try {
				String id="";
				try{
					id=managerObj.getString("id");
				}catch(Exception e){
					e.printStackTrace();
				}
				String userName="";
				try{
					userName=managerObj.getString("userName");
				}catch(Exception e){
					e.printStackTrace();
				}
				String userPhone="";
				try{
					userPhone=managerObj.getString("userPhone");
				}catch(Exception e){
					e.printStackTrace();
				}
				String userImageUrl="";
				try{
					userImageUrl=managerObj.getString("userImageUrl");
				}catch(Exception e){
					e.printStackTrace();
				}
				String userRegistTime="";
				try{
					userRegistTime=managerObj.getString("userRegistTime");
				}catch(Exception e){
					e.printStackTrace();
				}
				String userRole="";
				try{
					userRole=managerObj.getString("userRole");
				}catch(Exception e){
					e.printStackTrace();
				}
				String washerRole=managerObj.getString("washerRole");
				try{
					washerRole=managerObj.getString("washerRole");
				}catch(Exception e){
					e.printStackTrace();
				}
				String userOpStatus="";
				try{
					userOpStatus=managerObj.getString("userOpStatus");
				}catch(Exception e){
					e.printStackTrace();
				}
				String userOrderCount="";
				try{
					userOrderCount=managerObj.getString("userOrderCount");
				}catch(Exception e){
					e.printStackTrace();
				}
				String userTotalScore="";
				try{
					userTotalScore=managerObj.getString("userTotalScore");
				}catch(Exception e){
					e.printStackTrace();
				}
				String userCommentCount="";
				try{
					userCommentCount=managerObj.getString("userCommentCount");
				}catch(Exception e){
					e.printStackTrace();
				}
				String userAuthority="";
				try{
					userAuthority=managerObj.getString("userAuthority");
				}catch(Exception e){
					e.printStackTrace();
				}
				String wowo="";
				try{
					wowo=managerObj.getString("wowo");
				}catch(Exception e){
					e.printStackTrace();
				}
				entity=new ManagerEntity(id, userName, userPhone, userImageUrl, userRegistTime, userRole, washerRole, userOpStatus, userOrderCount, userTotalScore, userCommentCount, userAuthority, wowo);
				return entity;
			} catch (JSONException e) {
				System.out.println("-->>getManagerEntity()解析数据失败");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return entity;
	}


	private  static List<ProductEntity> getProductEntity(JSONArray jsonArray_products){
		List<ProductEntity> list=new ArrayList<ProductEntity>();
		if(jsonArray_products!=null){
			for(int i=0;i<jsonArray_products.length();i++){
				try {

					JSONObject obj=jsonArray_products.getJSONObject(i);
					String id="";
					try{
						id=obj.getString("id");
					}catch(Exception e){
						e.printStackTrace();
					}
					String productName="";
					try{
						productName=obj.getString("productName");
					}catch(Exception e){
						e.printStackTrace();
					}
					String productPrice="";
					try{
						productPrice=""+obj.getDouble("productPrice");
					}catch(Exception e){
						e.printStackTrace();
					}
					String productDesc="";
					try{
						productDesc=obj.getString("productDesc");
					}catch(Exception e){
						e.printStackTrace();
					}
					String productImage="";
					try{
						productImage=obj.getString("productImage");
					}catch(Exception e){
						e.printStackTrace();
					}
					String h5url="";
					try{
						h5url=obj.getString("h5URL");
					}catch(Exception e){
						e.printStackTrace();
					}
					String productStatus="";
					try{
						productStatus=obj.getString("productStatus");
					}catch(Exception e){
						e.printStackTrace();
					}
					String productCreateTime="";
					try{
						productCreateTime=obj.getString("productCreateTime");
					}catch(Exception e){
						e.printStackTrace();
					}
					String remark="";
					try{
						remark=obj.getString("remark");
					}catch(Exception e){
						e.printStackTrace();
					}
					String doorService="";
					try{
						doorService=obj.getString("doorService");
					}catch(Exception e){
						e.printStackTrace();
					}
					ProductEntity entity=new ProductEntity(id, productName, productPrice, productDesc, productImage, h5url, productStatus, productCreateTime, remark, doorService);
					list.add(entity);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return list;
	}


	/**
	 * 解析我的车库的数据
	 * {@link GarageManagerActivity}
	 * 
	 * */
	public static List<CardEntity> parseCarList(String parseJson){
		List<CardEntity> list=new ArrayList<CardEntity>();
		try {
			JSONObject jSONObject=new JSONObject(parseJson);
			String code=jSONObject.getString("code");
			if(code.endsWith("0")){
				JSONArray jSONArray=jSONObject.getJSONArray("userCar");
				if(jSONArray!=null){
					for(int i=0;i<jSONArray.length();i++){
						JSONObject obj=jSONArray.getJSONObject(i);
						String id=obj.getString("id");
						String carNum=obj.getString("carNum");
						String province=obj.getString("province");
						String carBrand=obj.getString("carBrand");
						String carColor="";
						try{
							carColor=obj.getString("carColor");
						}catch(Exception e){
							e.printStackTrace();
						}
						String carImage="";
						try{
							carImage=obj.getString("carImage");//未上传图片时这个节点没有
						}catch(Exception e){
							e.printStackTrace();
						}
						String isDefault=obj.getString("isDefault");
						boolean carIsdefault=false;
						if(isDefault!=null&&!isDefault.equals("")){
							carIsdefault=Boolean.parseBoolean(isDefault);
						}
						CardEntity carEntity=new CardEntity(id, carNum, province, carBrand, carColor, carImage, carIsdefault);
						list.add(carEntity);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}



	/**解析订单列表数据*/
	public static List<OrderListEntity> getOrderListEntity(String pareseJSON){
		List<OrderListEntity> list=new ArrayList<OrderListEntity>();
		try {
			JSONObject jsonObject=new JSONObject(pareseJSON);
			String code=jsonObject.getString("code");
			if(code.equals("0")){
				JSONArray ordersArray=jsonObject.getJSONArray("orders");
				if(ordersArray!=null){
					for(int i=0;i<ordersArray.length();i++){
						JSONObject orderJSONObject=ordersArray.getJSONObject(i);
						String id=orderJSONObject.getString("id");
						String orderNum=orderJSONObject.getString("orderNum");
						String orderStatus=orderJSONObject.getString("orderStatus");
						String orderPrice=orderJSONObject.getString("orderPrice");
						String orderUpdateTime="";
						try {
							orderUpdateTime=orderJSONObject.getString("orderUpdateTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String orderCreateTime="";
						try {
							orderCreateTime=orderJSONObject.getString("orderCreateTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String orderFinishTime="";
						try {
							orderFinishTime=orderJSONObject.getString("orderFinishTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String orderPayedTime="";
						try {
							orderPayedTime=orderJSONObject.getString("orderPayedTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String orderRobbedTime="";
						try {
							orderRobbedTime=orderJSONObject.getString("orderRobbedTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String payType="";
						try {
							payType=orderJSONObject.getString("payType");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String payTradeNo="";
						try {
							payTradeNo=orderJSONObject.getString("payTradeNo");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String phone=orderJSONObject.getString("phone");
						String doorService=orderJSONObject.getString("doorService");
						String serviceTime=orderJSONObject.getString("serviceTime");
						String address=orderJSONObject.getString("address");
						String remark=orderJSONObject.getString("remark");
						//--------
						OwnerEntity ownerEntity=null;
						JSONObject ownerObject=orderJSONObject.getJSONObject("owner");
						String ownerid=ownerObject.getString("id");
						String owneruserPhone=ownerObject.getString("userPhone");
						String userImageUrl="";
						try {
							userImageUrl=ownerObject.getString("userImageUrl");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String owneruserRegistTime=ownerObject.getString("userRegistTime");
						String owneruserRole=ownerObject.getString("userRole");
						String owneruserOpStatus=ownerObject.getString("userOpStatus");
						String owneruserOrderCount=ownerObject.getString("userOrderCount");
						String owneruseruserTotalScore=ownerObject.getString("userTotalScore");
						String owneruserCommentCount=ownerObject.getString("userCommentCount");
						String owneruserwowo=ownerObject.getString("wowo");
						ownerEntity=new OwnerEntity(ownerid, owneruserPhone, userImageUrl, owneruserRegistTime, owneruserRole, owneruserOpStatus, owneruserOrderCount, owneruseruserTotalScore, owneruserCommentCount, owneruserwowo); 
						//洗护师
						WasherEntity washerEntity=null;
						JSONObject washerObject=null;
						try {
							washerObject=orderJSONObject.getJSONObject("washer");
						} catch (Exception e) {
							// TODO: handle exception
						}
						if(washerObject!=null){
							String washerwasherId=washerObject.getString("id");		
							String washeruserName=washerObject.getString("userName");
							String washeruserPhone=washerObject.getString("userPhone");
							String washeruserImageUrl="";
							try {
								userImageUrl=washerObject.getString("userImageUrl");
							} catch (Exception e) {
								// TODO: handle exception
							}
							String washeruserRegistTime=washerObject.getString("userRegistTime");
							String washeruserRole=washerObject.getString("userRole");
							String wahserwasherRole=washerObject.getString("washerRole");
							String washeruserOpStatus=washerObject.getString("userOpStatus");
							String washeruserOrderCount=washerObject.getString("userOrderCount");
							String washeruserTotalScore=washerObject.getString("userTotalScore");
							String washeruserCommentCount=washerObject.getString("userCommentCount");
							String washeruserAuthority=washerObject.getString("userAuthority");
							String washerwowo=washerObject.getString("wowo");
							washerEntity=new WasherEntity(washerwasherId, washeruserName, washeruserPhone, washeruserImageUrl, washeruserRegistTime, washeruserRole, wahserwasherRole, washeruserOpStatus, washeruserOrderCount, washeruserTotalScore, washeruserCommentCount, washeruserAuthority, washerwowo);
							
						}
						//公司
						CompanyEntity companyEntity=null;
						JSONObject companyObject=orderJSONObject.getJSONObject("company");
						String comapanyid=companyObject.getString("id");
						String comapanycompanyName=companyObject.getString("companyName");
						String comapanycompanyAddr=companyObject.getString("companyAddr");
						String comapanycompanyDesc=companyObject.getString("companyDesc");
						String comapanycompanyImg="";
						try {
							comapanycompanyImg=companyObject.getString("companyImg");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanycompanyMapImg="";
						try {
							comapanycompanyMapImg=companyObject.getString("companyMapImg");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanycompanyPhone=companyObject.getString("companyPhone");
						String comapanycompanySpaceCount="";
						try {
							comapanycompanySpaceCount=companyObject.getString("companySpaceCount");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanycompanyTotalCount="0";
						try {
							comapanycompanyTotalCount=companyObject.getString("companyTotalCount");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanyspacePrice="0";
						try {
							comapanyspacePrice=companyObject.getString("spacePrice");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanyfreeTime="0";
						try {
							comapanyfreeTime=companyObject.getString("freeTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanysaleMsg="";
						try {
							comapanysaleMsg=companyObject.getString("saleMsg");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanylbsId="";
						try {
							comapanylbsId=companyObject.getString("lbsId");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanylongitude="0";
						try {
							comapanylongitude=companyObject.getString("longitude");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanylatitude="0";
						try {
							comapanylatitude=companyObject.getString("latitude");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanylstatus=companyObject.getString("status");
						companyEntity=new CompanyEntity(comapanyid, comapanycompanyName, comapanycompanyAddr, comapanycompanyDesc, comapanycompanyImg, comapanycompanyMapImg, comapanycompanyPhone, comapanycompanySpaceCount, comapanycompanyTotalCount, comapanyspacePrice, comapanyfreeTime, comapanysaleMsg, comapanylbsId, comapanylongitude, comapanylatitude, comapanylstatus);
						//车子
						CardEntity carEntity=null;
						JSONObject carObject=orderJSONObject.getJSONObject("userCar");
						String carid=carObject.getString("id");
						String carcarNum=carObject.getString("carNum");
						String carprovince=carObject.getString("province");
						String carcarBrand=carObject.getString("carBrand");
						String carcarColor=carObject.getString("carColor");
						String carImage="";
						try {
							carImage=carObject.getString("carImage");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String carisDefault=carObject.getString("isDefault");
						if(carisDefault.equals("false")){
							carEntity=new CardEntity(carid, carcarNum, carprovince, carcarBrand, carcarColor, carImage, false);
						}else{
							carEntity=new CardEntity(carid, carcarNum, carprovince, carcarBrand, carcarColor, carImage, true);
						}
						//产品
						ProductEntity productEntity=null;

						JSONObject productJSONObject=orderJSONObject.getJSONObject("product");
						String productid=productJSONObject.getString("id");
						String productName=productJSONObject.getString("productName");
						String productPrice=productJSONObject.getString("productPrice");
						String productDesc=productJSONObject.getString("productDesc");
						String productImage="";
						try {
							productImage=productJSONObject.getString("productImage");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String h5URL="";
						try {
							h5URL=productJSONObject.getString("h5URL");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String productStatus=productJSONObject.getString("productStatus");
						String productCreateTime=productJSONObject.getString("productCreateTime");
						String productremark=productJSONObject.getString("remark");
						String productdoorService=productJSONObject.getString("doorService");
						productEntity=new ProductEntity(productid, productName, productPrice, productDesc, productImage, h5URL, productStatus, productCreateTime, productremark, productdoorService);

						
						OrderSubEntity orderSub=null;//有可能为空
						JSONObject orderSubEntityObj=null;
						try {
							orderSubEntityObj=jsonObject.getJSONObject("orderSub");
							if(orderSubEntityObj!=null){
								String subId=orderSubEntityObj.getString("id");
								String subStatus=orderSubEntityObj.getString("subStatus");
								String subarrivedTime="";
								try {
									subarrivedTime=orderSubEntityObj.getString("arrivedTime");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String subcheckedTime="";
								try {
									subcheckedTime=orderSubEntityObj.getString("checkedTime");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String subwashTime="";
								try {
									subwashTime=orderSubEntityObj.getString("washTime");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String subwashedTime="";
								try {
									subwashedTime=orderSubEntityObj.getString("washedTime");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String img1="";
								try {
									img1=orderSubEntityObj.getString("img1");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String img2="";
								try {
									img2=orderSubEntityObj.getString("img2");
								} catch (Exception e) {
									// TODO: handle exception
								}							
								String img3="";
								try {
									img3=orderSubEntityObj.getString("img3");
								} catch (Exception e) {
									// TODO: handle exception
								}
								orderSub=new OrderSubEntity(subId, subStatus, subarrivedTime, subcheckedTime, subwashTime, subwashedTime, img1, img2, img3);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						//用户评论
						JSONObject commentJSONObject=null;
						CommentEntity commentEntity=null;
						try {
							commentJSONObject=jsonObject.getJSONObject("comment");
							if(commentJSONObject!=null){
								String commentId=commentJSONObject.getString("commentId");
								String creatUserId=commentJSONObject.getString("creatUserId");
								String createUserPhone=commentJSONObject.getString("createUserPhone");
								String morderId=commentJSONObject.getString("orderId");
								String commentContent=commentJSONObject.getString("commentContent");
								String commentCreateTime=commentJSONObject.getString("commentCreateTime");
								String commentScore=commentJSONObject.getString("commentScore");
								String washerId=commentJSONObject.getString("washerId");
								String washerName=commentJSONObject.getString("washerName");
								String mimg1="";
								try {
									mimg1=commentJSONObject.getString("img1");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String mimg2="";
								try {
									mimg2=commentJSONObject.getString("img2");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String mimg3="";
								try {
									mimg3=commentJSONObject.getString("img3");
								} catch (Exception e) {
									// TODO: handle exception
								}
								commentEntity=new CommentEntity(commentId, creatUserId, createUserPhone, morderId, commentContent, commentCreateTime, commentScore, washerId, washerName, mimg1, mimg2, mimg3);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						OrderListEntity entity=new OrderListEntity(id, orderNum, orderStatus, orderPrice, orderUpdateTime, orderCreateTime, orderFinishTime, orderPayedTime, orderRobbedTime, payType, payTradeNo, phone, doorService, serviceTime, address, remark, orderSub,commentEntity,ownerEntity, washerEntity, companyEntity,productEntity, carEntity);
						list.add(entity);
					}
					return list;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}



	/**解析订单详情
	 * @throws Exception */
	public  static OrderDetailEntity getOrderDtailEntity(String json) throws Exception{
		JSONObject jsonObject=new JSONObject(json);
		String code=jsonObject.getString("code");
		if(code.equals("0")){
			OrderSubEntity orderSub=null;//有可能为空
			JSONObject orderSubEntityObj=null;
			try {
				orderSubEntityObj=jsonObject.getJSONObject("orderSub");
				if(orderSubEntityObj!=null){
					String subId=orderSubEntityObj.getString("id");
					String subStatus=orderSubEntityObj.getString("subStatus");
					String subarrivedTime="";
					try {
						subarrivedTime=orderSubEntityObj.getString("arrivedTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String subcheckedTime="";
					try {
						subcheckedTime=orderSubEntityObj.getString("checkedTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String subwashTime="";
					try {
						subwashTime=orderSubEntityObj.getString("washTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String subwashedTime="";
					try {
						subwashedTime=orderSubEntityObj.getString("washedTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String img1="";
					try {
						img1=orderSubEntityObj.getString("img1");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String img2="";
					try {
						img2=orderSubEntityObj.getString("img2");
					} catch (Exception e) {
						// TODO: handle exception
					}							
					String img3="";
					try {
						img3=orderSubEntityObj.getString("img3");
					} catch (Exception e) {
						// TODO: handle exception
					}
					orderSub=new OrderSubEntity(subId, subStatus, subarrivedTime, subcheckedTime, subwashTime, subwashedTime, img1, img2, img3);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			//用户评论
			JSONObject commentJSONObject=null;
			CommentEntity commentEntity=null;
			try {
				commentJSONObject=jsonObject.getJSONObject("comment");
				if(commentJSONObject!=null){
					String commentId=commentJSONObject.getString("commentId");
					String creatUserId=commentJSONObject.getString("creatUserId");
					String createUserPhone=commentJSONObject.getString("createUserPhone");
					String morderId=commentJSONObject.getString("orderId");
					String commentContent=commentJSONObject.getString("commentContent");
					String commentCreateTime=commentJSONObject.getString("commentCreateTime");
					String commentScore=commentJSONObject.getString("commentScore");
					String washerId=commentJSONObject.getString("washerId");
					String washerName=commentJSONObject.getString("washerName");
					String mimg1="";
					try {
						mimg1=commentJSONObject.getString("img1");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String mimg2="";
					try {
						mimg2=commentJSONObject.getString("img2");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String mimg3="";
					try {
						mimg3=commentJSONObject.getString("img3");
					} catch (Exception e) {
						// TODO: handle exception
					}
					commentEntity=new CommentEntity(commentId, creatUserId, createUserPhone, morderId, commentContent, commentCreateTime, commentScore, washerId, washerName, mimg1, mimg2, mimg3);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			JSONObject orderJSONObject=jsonObject.getJSONObject("order");
			String id=orderJSONObject.getString("id");
			String orderNum=orderJSONObject.getString("orderNum");
			String orderStatus=orderJSONObject.getString("orderStatus");
			String orderPrice=orderJSONObject.getString("orderPrice");

			String orderUpdateTime="";
			try {
				orderUpdateTime=orderJSONObject.getString("orderUpdateTime");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String orderCreateTime="";
			try {
				orderCreateTime=orderJSONObject.getString("orderCreateTime");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String orderPayedTime="";
			try {
				orderPayedTime=orderJSONObject.getString("orderPayedTime");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String orderRobbedTime="";
			try {
				orderRobbedTime=orderJSONObject.getString("orderRobbedTime");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String finishTime="";
			try {
				finishTime=orderJSONObject.getString("orderFinishTime");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String serviceTime="";
			try {
				serviceTime=orderJSONObject.getString("serviceTime");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String payType="";
			try {
				payType=orderJSONObject.getString("payType");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String payTradeNo="";
			try {
				payTradeNo=orderJSONObject.getString("payTradeNo");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String doorService="";
			try {
				doorService=orderJSONObject.getString("doorService");
			} catch (Exception e) {
				// TODO: handle exception
			}
			//产品信息
			ProductEntity productEntity=null;
			JSONObject productJSONObject=null;
			try {
				productJSONObject=orderJSONObject.getJSONObject("product");
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(productJSONObject!=null){
				String productid=productJSONObject.getString("id");
				String productName=productJSONObject.getString("productName");
				String productPrice=productJSONObject.getString("productPrice");
				String productDesc=productJSONObject.getString("productDesc");
				String productImage="";
				try {
					productImage=productJSONObject.getString("productImage");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String h5URL="";
				try {
					h5URL=productJSONObject.getString("h5URL");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String productStatus=productJSONObject.getString("productStatus");
				String productCreateTime=productJSONObject.getString("productCreateTime");
				String productremark=productJSONObject.getString("remark");
				String productdoorService=productJSONObject.getString("doorService");
				productEntity=new ProductEntity(productid, productName, productPrice, productDesc, productImage, h5URL, productStatus, productCreateTime, productremark, productdoorService);

			}
			//公司信息
			CompanyEntity companyEntity=null;
			JSONObject companyObject=null;
			try {
				companyObject=orderJSONObject.getJSONObject("company");
			} catch (Exception e) {
				// TODO: handle exception
			}
            if(companyObject!=null){
				String comapanyid=companyObject.getString("id");
				String comapanycompanyName=companyObject.getString("companyName");
				String comapanycompanyAddr=companyObject.getString("companyAddr");
				String comapanycompanyDesc=companyObject.getString("companyDesc");
				String comapanycompanyImg="";
				try {
					comapanycompanyImg=companyObject.getString("companyImg");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String comapanycompanyMapImg="";
				try {
					comapanycompanyMapImg=companyObject.getString("companyMapImg");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String comapanycompanyPhone="";
				try {
					comapanycompanyPhone=companyObject.getString("companyPhone");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String comapanycompanySpaceCount="";
				try {
					comapanycompanySpaceCount=companyObject.getString("companySpaceCount");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String comapanycompanyTotalCount="0";
				try {
					comapanycompanyTotalCount=companyObject.getString("companyTotalCount");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String comapanyspacePrice="0";
				try {
					comapanyspacePrice=companyObject.getString("spacePrice");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String comapanyfreeTime="0";
				try {
					comapanyfreeTime=companyObject.getString("freeTime");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String comapanysaleMsg="";
				try {
					comapanysaleMsg=companyObject.getString("saleMsg");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String comapanylbsId="";
				try {
					comapanylbsId=companyObject.getString("lbsId");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String comapanylongitude="0";
				try {
					comapanylongitude=companyObject.getString("longitude");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String comapanylatitude="0";
				try {
					comapanylatitude=companyObject.getString("latitude");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String comapanylstatus=companyObject.getString("status");
				companyEntity=new CompanyEntity(comapanyid, comapanycompanyName, comapanycompanyAddr, comapanycompanyDesc, comapanycompanyImg, comapanycompanyMapImg, comapanycompanyPhone, comapanycompanySpaceCount, comapanycompanyTotalCount, comapanyspacePrice, comapanyfreeTime, comapanysaleMsg, comapanylbsId, comapanylongitude, comapanylatitude, comapanylstatus);
            }
			//服务车辆
            CardEntity carEntity=null;
            JSONObject carObject=null;
            try {
            	carObject=orderJSONObject.getJSONObject("userCar");
			} catch (Exception e) {
				// TODO: handle exception
			}
            if(carObject!=null){
            	String carid=carObject.getString("id");
    			String carcarNum=carObject.getString("carNum");
    			String carprovince=carObject.getString("province");
    			String carcarBrand=carObject.getString("carBrand");
    			String carcarColor=carObject.getString("carColor");
    			String carImage="";
    			try {
    				carImage=carObject.getString("carImage");
    			} catch (Exception e) {
    				// TODO: handle exception
    			}
    			String carisDefault=carObject.getString("isDefault");
    			if(carisDefault.equals("false")){
    				carEntity=new CardEntity(carid, carcarNum, carprovince, carcarBrand, carcarColor, carImage, false);
    			}else{
    				carEntity=new CardEntity(carid, carcarNum, carprovince, carcarBrand, carcarColor, carImage, true);
    			}
            }
			//美护师信息
			WasherEntity washerEntity=null;
			JSONObject washerObj=null;
			try {
				washerObj=orderJSONObject.getJSONObject("washer");
			} catch (Exception e) {
			}
			if(washerObj!=null){
				String wahserId=washerObj.getString("id");
				String washerName=washerObj.getString("userName");
				String washderPhone=washerObj.getString("userPhone");
				String washerUrl="";
				try {
					washerUrl=washerObj.getString("userImageUrl");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String washerRegistTime="";
				try {
					washerRegistTime=washerObj.getString("userRegistTime");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String userRole="";
				try {
					userRole=washerObj.getString("userRole");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String washerRole="";
				try {
					washerRole=washerObj.getString("washerRole");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String userOpStatus="";
				try {
					userOpStatus=washerObj.getString("userOpStatus");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String userOrderCount="";
				try {
					userOrderCount=washerObj.getString("userOrderCount");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String userTotalScore=washerObj.getString("userTotalScore");
				String userCommentCount=washerObj.getString("userCommentCount");
				String userAuthority=washerObj.getString("userAuthority");
				String wowo=washerObj.getString("wowo");
				washerEntity=new WasherEntity(wahserId, washerName, washderPhone, washerUrl, washerRegistTime, userRole, washerRole, userOpStatus, userOrderCount, userTotalScore, userCommentCount, userAuthority, wowo);
			}
			String phone="phone";
			try {
				phone=orderJSONObject.getString("");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String address="";
			try {
				address=orderJSONObject.getString("address");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String remark="";
			try {
				remark=orderJSONObject.getString("remark");
			} catch (Exception e) {
			}
			//车主信息
			OwnerEntity ownerEntity=null;
			JSONObject ownerObject=null;
			try {
				ownerObject=orderJSONObject.getJSONObject("owner");
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(ownerObject!=null){
				String ownerid=ownerObject.getString("id");
				String owneruserPhone=ownerObject.getString("userPhone");
				String userImageUrl="";
				try {
					userImageUrl=ownerObject.getString("userImageUrl");
				} catch (Exception e) {
					// TODO: handle exception
				}
				String owneruserRegistTime=ownerObject.getString("userRegistTime");
				String owneruserRole=ownerObject.getString("userRole");
				String owneruserOpStatus=ownerObject.getString("userOpStatus");
				String owneruserOrderCount=ownerObject.getString("userOrderCount");
				String owneruseruserTotalScore=ownerObject.getString("userTotalScore");
				String owneruserCommentCount=ownerObject.getString("userCommentCount");
				String owneruserwowo=ownerObject.getString("wowo");
				ownerEntity=new OwnerEntity(ownerid, owneruserPhone, userImageUrl, owneruserRegistTime, owneruserRole, owneruserOpStatus, owneruserOrderCount, owneruseruserTotalScore, owneruserCommentCount, owneruserwowo); 
				
			}
			OrderDetailEntity entity=new OrderDetailEntity(orderSub, commentEntity, id, orderNum, orderStatus, orderPrice, orderUpdateTime, orderCreateTime, orderPayedTime, orderRobbedTime, finishTime, serviceTime, payType, payTradeNo, doorService, washerEntity, companyEntity, productEntity, carEntity,ownerEntity,phone,address,remark);
		    return entity;
		}
		return null;
	}

	/**解析我的订单数据*//*
	public static List<OrderListEntity> getOrderList(String parseJSON){
		List<OrderListEntity> list=new ArrayList<OrderListEntity>();
		try {
			JSONObject jSONObject=new JSONObject(parseJSON);
			String code=jSONObject.getString("code");
			if(code.equals("0")){
				JSONArray ordersJSONArray=jSONObject.getJSONArray("orders");
				for(int i=0;i<ordersJSONArray.length();i++){
					JSONObject orderJSONObject=ordersJSONArray.getJSONObject(i);
					String id=orderJSONObject.getString("id");
					String orderNum=orderJSONObject.getString("orderNum");
					String orderStatus=orderJSONObject.getString("orderStatus");
					String orderPrice=orderJSONObject.getString("orderPrice");
					String orderUpdateTime="";
					try {
						orderUpdateTime=orderJSONObject.getString("orderUpdateTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String orderCreateTime="";
					try {
						orderCreateTime=orderJSONObject.getString("orderCreateTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String orderPayedTime="";
					try {
						orderPayedTime=orderJSONObject.getString("orderPayedTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String payType="";
					try {
						orderJSONObject.getString("payType");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String payTradeNo="";
					try {
						payTradeNo=orderJSONObject.getString("payTradeNo");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String doorService=orderJSONObject.getString("doorService");
					String serviceTime=orderJSONObject.getString("serviceTime");
					String receiveTime="";
					try {
						receiveTime=orderJSONObject.getString("orderReceiveTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String completeTime="";
					try {
						completeTime=orderJSONObject.getString("orderCompleteTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String phone=orderJSONObject.getString("phone");
					String address=orderJSONObject.getString("address");
					String remark="";
					try {
						remark=orderJSONObject.getString("remark");
					} catch (Exception e) {
						// TODO: handle exception
					}
					//公司信息
					JSONObject companyJSONObject=orderJSONObject.getJSONObject("company");
					String companyId=companyJSONObject.getString("id");
					String companyName=companyJSONObject.getString("companyName");
					String companyPhone=companyJSONObject.getString("companyPhone");
					//产品信息
					JSONObject productJSONObject=orderJSONObject.getJSONObject("product");
					String productId=productJSONObject.getString("id");
					String productName=productJSONObject.getString("productName");
					//服务车辆
					JSONObject carJSONOject=orderJSONObject.getJSONObject("userCar");
					String carId=carJSONOject.getString("id");
					String carNum=carJSONOject.getString("carNum");
					String carBrand=carJSONOject.getString("carBrand");
					String carColor=carJSONOject.getString("carColor");
					String province=carJSONOject.getString("province");
					String carImageUrl="";
					try {
						carImageUrl=carJSONOject.getString("carImage");
					} catch (Exception e) {
						// TODO: handle exception
					}

					WasherEntity washerEntity=null;
					JSONObject washerObj=null;
					try {
						washerObj=orderJSONObject.getJSONObject("washer");
					} catch (Exception e) {
					}
					if(washerObj!=null){
						String wahserId=washerObj.getString("id");
						String washerName=washerObj.getString("userName");
						String washderPhone=washerObj.getString("userPhone");
						String washerUrl="";
						try {
							washerUrl=washerObj.getString("userImageUrl");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String washerRegistTime="";
						try {
							washerRegistTime=washerObj.getString("userRegistTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String userRole="";
						try {
							userRole=washerObj.getString("userRole");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String washerRole="";
						try {
							washerRole=washerObj.getString("washerRole");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String userOpStatus="";
						try {
							userOpStatus=washerObj.getString("userOpStatus");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String userOrderCount="";
						try {
							userOrderCount=washerObj.getString("userOrderCount");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String userTotalScore=washerObj.getString("userTotalScore");
						String userCommentCount=washerObj.getString("userCommentCount");
						String userAuthority=washerObj.getString("userAuthority");
						String wowo=washerObj.getString("wowo");
						washerEntity=new WasherEntity(wahserId, washerName, washderPhone, washerUrl, washerRegistTime, userRole, washerRole, userOpStatus, userOrderCount, userTotalScore, userCommentCount, userAuthority, wowo);
					}
					OrderListEntity entity=new OrderListEntity(id, orderNum, orderStatus, orderPrice, orderUpdateTime, orderCreateTime, serviceTime,receiveTime,completeTime, phone, payType,doorService, serviceTime, address, remark, companyId, companyName, companyPhone, productId, productName, carId, carNum, province, carBrand, carColor, carImageUrl,washerEntity);
					list.add(entity);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/


	/**解析地址列表*/
	public static List<AdressEntity> paresAdressListData(String parseJSON){
		List<AdressEntity> list=new ArrayList<AdressEntity>();
		try {
			JSONObject jSONObject=new JSONObject(parseJSON);
			String code=jSONObject.getString("code");
			if(code.equals("0")){
				JSONArray userAddresses=jSONObject.getJSONArray("userAddresses");
				for(int i=0;i<userAddresses.length();i++){
					JSONObject obj=userAddresses.getJSONObject(i);
					String id=obj.getString("id");
					String phone=obj.getString("phone");
					String provice=obj.getString("prefix");
					String carAdress=obj.getString("address");
					String isDefault_temp="false";//有可能没有此节点
					boolean isDefault=false;
					try {
						isDefault_temp=obj.getString("isDefault");
						if(isDefault_temp.equals("true")){
							isDefault=true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					AdressEntity entiy=new AdressEntity(id,phone, provice, carAdress, isDefault);
					list.add(entiy);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}


	/**洗护师抢单列表(待强列表)*/
	public static List<QiangDanListEntity> getQiangDanList(String parseJSON){
		List<QiangDanListEntity> list=new ArrayList<QiangDanListEntity>();
		try {
			JSONObject jSONObject=new JSONObject(parseJSON);
			String code=jSONObject.getString("code");
			if(code.equals("0")){
				JSONArray ordersJSONArray=jSONObject.getJSONArray("orders");
				for(int i=0;i<ordersJSONArray.length();i++){
					JSONObject orderJSONObject=ordersJSONArray.getJSONObject(i);
					String orderId=orderJSONObject.getString("id");
					String orderNum=orderJSONObject.getString("orderNum");
					String orderStatus="";
					try {
						orderStatus=orderJSONObject.getString("orderStatus");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String phone=orderJSONObject.getString("phone");
					String createTime="";
					try {
						createTime=orderJSONObject.getString("orderCreateTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String receiveTime="";
					try {
						receiveTime=orderJSONObject.getString("receiveTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String completeTime="";
					try {
						completeTime=orderJSONObject.getString("completeTime");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String orderPrice="";
					try {
						orderPrice=orderJSONObject.getString("orderPrice");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String payType="";
					try {
						payType=orderJSONObject.getString("payType");
					} catch (Exception e) {
						// TODO: handle exception
					}
					String serviceTime=orderJSONObject.getString("serviceTime");
					String address=orderJSONObject.getString("address");
					String doorService=orderJSONObject.getString("doorService");
					String say="";
					try {
						say=orderJSONObject.getString("remark");
					} catch (Exception e) {
						//e.printStackTrace();
					}
					JSONObject companyJSONObject=orderJSONObject.getJSONObject("company");
					String companyId=companyJSONObject.getString("id");
					String companyName=companyJSONObject.getString("companyName");

					JSONObject productJSONObject=orderJSONObject.getJSONObject("product");
					String productId=productJSONObject.getString("id");
					String productName=productJSONObject.getString("productName");

					JSONObject carJSONObject=orderJSONObject.getJSONObject("userCar");
					String carId=carJSONObject.getString("id");
					String carBrand=carJSONObject.getString("carBrand");
					String carColor=carJSONObject.getString("carColor");
					String province=carJSONObject.getString("province");

					QiangDanListEntity entity=new QiangDanListEntity(orderId, orderNum,orderStatus, phone,createTime,receiveTime,completeTime, serviceTime,orderPrice,payType, address, doorService, say, companyId, companyName, productId, productName, carId, carBrand, carColor, province);
					list.add(entity);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**历史订单解析*/
	public static List<HistoryCommentEntity> parseHistoryCommet(String json){
		try {
			JSONObject jSONObject=new JSONObject(json);
			String code=jSONObject.getString("code");
			if(code.equals("0")){
				List<HistoryCommentEntity> list=new ArrayList<HistoryCommentEntity>();
				JSONArray commentArray=jSONObject.getJSONArray("comment");
				if(commentArray!=null){
					for(int i=0;i<commentArray.length();i++){
						JSONObject obj=commentArray.getJSONObject(i);
						String commentId=obj.getString("commentId");
						String creatUserId=obj.getString("creatUserId");
						String createUserPhone=obj.getString("createUserPhone");
						String orderId=obj.getString("orderId");
						String commentContent=obj.getString("commentContent");
						String commentCreateTime=obj.getString("commentCreateTime");
						String commentScore=obj.getString("commentScore");
						String replyContent="";
						try {
							replyContent=obj.getString("replyContent");
						} catch (Exception e) {
							//e.printStackTrace();
						}
						String replyTime="";
						try {
							replyTime=obj.getString("replyTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String washerId=obj.getString("washerId");
						String washerName=obj.getString("washerName");
						String productName=obj.getString("productName");
						String orderTime=obj.getString("orderTime");
						String img1="";
						try {
							img1=obj.getString("img1");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String img2="";
						try {
							img2=obj.getString("img2");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String img3="";
						try {
							img3=obj.getString("img3");
						} catch (Exception e) {
							// TODO: handle exception
						}
						HistoryCommentEntity eneity=new HistoryCommentEntity(commentId, creatUserId, createUserPhone, orderId, commentContent, commentCreateTime, commentScore, replyContent, replyTime, washerId, washerName, img1, img2, img3,productName, orderTime);
						list.add(eneity);
					}

					return list;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**更新车位*/
	public static List<CarParkEntity> parseCarPark(String json){
		List<CarParkEntity> list=new ArrayList<CarParkEntity>();
		try {
			JSONObject jSONObject=new JSONObject(json);
			String code=jSONObject.getString("code");
			if(code.equals("0")){
				JSONArray jSONArray=jSONObject.getJSONArray("parkingInfo");
				for(int i=0;i<jSONObject.length();i++){
					JSONObject parkingObject=jSONArray.getJSONObject(i);
					String companyName=parkingObject.getString("companyName");
					String companySpaceCount=parkingObject.getString("companySpaceCount");
					String companyTotalCount=parkingObject.getString("companyTotalCount");
					String companyId=parkingObject.getString("companyId");
					String companyImageUrl=parkingObject.getString("companyImage");
					CarParkEntity entity=new CarParkEntity(companyName, companySpaceCount, companyTotalCount, companyId, companyImageUrl);
					list.add(entity);
				}
			}
			return list;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}
	
	
	/**volley框架解析*/
	/**解析订单列表数据*/
	public static List<OrderListEntity> getOrderListEntity_volly(JSONObject jsonObject){
		List<OrderListEntity> list=new ArrayList<OrderListEntity>();
		try {
			String code=jsonObject.getString("code");
			if(code.equals("0")){
				JSONArray ordersArray=jsonObject.getJSONArray("orders");
				if(ordersArray!=null){
					for(int i=0;i<ordersArray.length();i++){
						JSONObject orderJSONObject=ordersArray.getJSONObject(i);
						String id=orderJSONObject.getString("id");
						String orderNum=orderJSONObject.getString("orderNum");
						String orderStatus=orderJSONObject.getString("orderStatus");
						String orderPrice=orderJSONObject.getString("orderPrice");
						String orderUpdateTime="";
						try {
							orderUpdateTime=orderJSONObject.getString("orderUpdateTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String orderCreateTime="";
						try {
							orderCreateTime=orderJSONObject.getString("orderCreateTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String orderFinishTime="";
						try {
							orderFinishTime=orderJSONObject.getString("orderFinishTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String orderPayedTime="";
						try {
							orderPayedTime=orderJSONObject.getString("orderPayedTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String orderRobbedTime="";
						try {
							orderRobbedTime=orderJSONObject.getString("orderRobbedTime");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String payType=orderJSONObject.getString("payType");
						String payTradeNo=orderJSONObject.getString("payTradeNo");
						String phone=orderJSONObject.getString("phone");
						String doorService=orderJSONObject.getString("doorService");
						String serviceTime=orderJSONObject.getString("serviceTime");
						String address=orderJSONObject.getString("address");
						String remark=orderJSONObject.getString("remark");
						//--------
						OwnerEntity ownerEntity=null;
						JSONObject ownerObject=orderJSONObject.getJSONObject("owner");
						String ownerid=ownerObject.getString("id");
						String owneruserPhone=ownerObject.getString("userPhone");
						String userImageUrl="";
						try {
							userImageUrl=ownerObject.getString("userImageUrl");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String owneruserRegistTime=ownerObject.getString("userRegistTime");
						String owneruserRole=ownerObject.getString("userRole");
						String owneruserOpStatus=ownerObject.getString("userOpStatus");
						String owneruserOrderCount=ownerObject.getString("userOrderCount");
						String owneruseruserTotalScore=ownerObject.getString("userTotalScore");
						String owneruserCommentCount=ownerObject.getString("userCommentCount");
						String owneruserwowo=ownerObject.getString("wowo");
						ownerEntity=new OwnerEntity(ownerid, owneruserPhone, userImageUrl, owneruserRegistTime, owneruserRole, owneruserOpStatus, owneruserOrderCount, owneruseruserTotalScore, owneruserCommentCount, owneruserwowo); 
						//洗护师
						WasherEntity washerEntity=null;
						JSONObject washerObject=null;
						try {
							washerObject=orderJSONObject.getJSONObject("washer");
						} catch (Exception e) {
							// TODO: handle exception
						}
						if(washerObject!=null){
							String washerwasherId=washerObject.getString("id");		
							String washeruserName=washerObject.getString("userName");
							String washeruserPhone=washerObject.getString("userPhone");
							String washeruserImageUrl="";
							try {
								userImageUrl=washerObject.getString("userImageUrl");
							} catch (Exception e) {
								// TODO: handle exception
							}
							String washeruserRegistTime=washerObject.getString("userRegistTime");
							String washeruserRole=washerObject.getString("userRole");
							String wahserwasherRole=washerObject.getString("washerRole");
							String washeruserOpStatus=washerObject.getString("userOpStatus");
							String washeruserOrderCount=washerObject.getString("userOrderCount");
							String washeruserTotalScore=washerObject.getString("userTotalScore");
							String washeruserCommentCount=washerObject.getString("userCommentCount");
							String washeruserAuthority=washerObject.getString("userAuthority");
							String washerwowo=washerObject.getString("wowo");
							washerEntity=new WasherEntity(washerwasherId, washeruserName, washeruserPhone, washeruserImageUrl, washeruserRegistTime, washeruserRole, wahserwasherRole, washeruserOpStatus, washeruserOrderCount, washeruserTotalScore, washeruserCommentCount, washeruserAuthority, washerwowo);
							
						}
						//公司
						CompanyEntity companyEntity=null;
						JSONObject companyObject=orderJSONObject.getJSONObject("company");
						String comapanyid=companyObject.getString("id");
						String comapanycompanyName=companyObject.getString("companyName");
						String comapanycompanyAddr=companyObject.getString("companyAddr");
						String comapanycompanyDesc=companyObject.getString("companyDesc");
						String comapanycompanyImg="";
						try {
							comapanycompanyImg=companyObject.getString("companyImg");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanycompanyMapImg="";
						try {
							comapanycompanyMapImg=companyObject.getString("companyMapImg");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String comapanycompanyPhone=companyObject.getString("companyPhone");
						String comapanycompanySpaceCount=companyObject.getString("companySpaceCount");
						String comapanycompanyTotalCount=companyObject.getString("companyTotalCount");
						String comapanyspacePrice=companyObject.getString("spacePrice");
						String comapanyfreeTime=companyObject.getString("freeTime");
						String comapanysaleMsg=companyObject.getString("saleMsg");
						String comapanylbsId=companyObject.getString("lbsId");
						String comapanylongitude=companyObject.getString("longitude");
						String comapanylatitude=companyObject.getString("latitude");
						String comapanylstatus=companyObject.getString("status");
						companyEntity=new CompanyEntity(comapanyid, comapanycompanyName, comapanycompanyAddr, comapanycompanyDesc, comapanycompanyImg, comapanycompanyMapImg, comapanycompanyPhone, comapanycompanySpaceCount, comapanycompanyTotalCount, comapanyspacePrice, comapanyfreeTime, comapanysaleMsg, comapanylbsId, comapanylongitude, comapanylatitude, comapanylstatus);
						//车子
						CardEntity carEntity=null;
						JSONObject carObject=orderJSONObject.getJSONObject("userCar");
						String carid=carObject.getString("id");
						String carcarNum=carObject.getString("carNum");
						String carprovince=carObject.getString("province");
						String carcarBrand=carObject.getString("carBrand");
						String carcarColor=carObject.getString("carColor");
						String carImage="";
						try {
							carImage=carObject.getString("carImage");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String carisDefault=carObject.getString("isDefault");
						if(carisDefault.equals("false")){
							carEntity=new CardEntity(carid, carcarNum, carprovince, carcarBrand, carcarColor, carImage, false);
						}else{
							carEntity=new CardEntity(carid, carcarNum, carprovince, carcarBrand, carcarColor, carImage, true);
						}
						//产品
						ProductEntity productEntity=null;

						JSONObject productJSONObject=orderJSONObject.getJSONObject("product");
						String productid=productJSONObject.getString("id");
						String productName=productJSONObject.getString("productName");
						String productPrice=productJSONObject.getString("productPrice");
						String productDesc=productJSONObject.getString("productDesc");
						String productImage="";
						try {
							productImage=productJSONObject.getString("productImage");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String h5URL="";
						try {
							h5URL=productJSONObject.getString("h5URL");
						} catch (Exception e) {
							// TODO: handle exception
						}
						String productStatus=productJSONObject.getString("productStatus");
						String productCreateTime=productJSONObject.getString("productCreateTime");
						String productremark=productJSONObject.getString("remark");
						String productdoorService=productJSONObject.getString("doorService");
						productEntity=new ProductEntity(productid, productName, productPrice, productDesc, productImage, h5URL, productStatus, productCreateTime, productremark, productdoorService);

						OrderSubEntity orderSub=null;//有可能为空
						JSONObject orderSubEntityObj=null;
						try {
							orderSubEntityObj=jsonObject.getJSONObject("orderSub");
							if(orderSubEntityObj!=null){
								String subId=orderSubEntityObj.getString("id");
								String subStatus=orderSubEntityObj.getString("subStatus");
								String subarrivedTime="";
								try {
									subarrivedTime=orderSubEntityObj.getString("arrivedTime");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String subcheckedTime="";
								try {
									subcheckedTime=orderSubEntityObj.getString("checkedTime");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String subwashTime="";
								try {
									subwashTime=orderSubEntityObj.getString("washTime");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String subwashedTime="";
								try {
									subwashedTime=orderSubEntityObj.getString("washedTime");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String img1="";
								try {
									img1=orderSubEntityObj.getString("img1");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String img2="";
								try {
									img2=orderSubEntityObj.getString("img2");
								} catch (Exception e) {
									// TODO: handle exception
								}							
								String img3="";
								try {
									img3=orderSubEntityObj.getString("img3");
								} catch (Exception e) {
									// TODO: handle exception
								}
								orderSub=new OrderSubEntity(subId, subStatus, subarrivedTime, subcheckedTime, subwashTime, subwashedTime, img1, img2, img3);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						//用户评论
						JSONObject commentJSONObject=null;
						CommentEntity commentEntity=null;
						try {
							commentJSONObject=jsonObject.getJSONObject("comment");
							if(commentJSONObject!=null){
								String commentId=commentJSONObject.getString("commentId");
								String creatUserId=commentJSONObject.getString("creatUserId");
								String createUserPhone=commentJSONObject.getString("createUserPhone");
								String morderId=commentJSONObject.getString("orderId");
								String commentContent=commentJSONObject.getString("commentContent");
								String commentCreateTime=commentJSONObject.getString("commentCreateTime");
								String commentScore=commentJSONObject.getString("commentScore");
								String washerId=commentJSONObject.getString("washerId");
								String washerName=commentJSONObject.getString("washerName");
								String mimg1="";
								try {
									mimg1=commentJSONObject.getString("img1");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String mimg2="";
								try {
									mimg2=commentJSONObject.getString("img2");
								} catch (Exception e) {
									// TODO: handle exception
								}
								String mimg3="";
								try {
									mimg3=commentJSONObject.getString("img3");
								} catch (Exception e) {
									// TODO: handle exception
								}
								commentEntity=new CommentEntity(commentId, creatUserId, createUserPhone, morderId, commentContent, commentCreateTime, commentScore, washerId, washerName, mimg1, mimg2, mimg3);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						OrderListEntity entity=new OrderListEntity(id, orderNum, orderStatus, orderPrice, orderUpdateTime, orderCreateTime, orderFinishTime, orderPayedTime, orderRobbedTime, payType, payTradeNo, phone, doorService, serviceTime, address, remark, orderSub,commentEntity,ownerEntity, washerEntity, companyEntity,productEntity, carEntity);
						list.add(entity);
					}
					return list;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**车主用户消息列表*/
	public static List<MessageEntity> parseOwnMessageList(String targetJSON){
		List<MessageEntity> list = new ArrayList<MessageEntity>();
		if(targetJSON!=null){
			try {
				JSONObject jsonObject=new JSONObject(targetJSON);
				String code=jsonObject.getString("code");
				if(code.equals("0")){
					JSONArray messages=jsonObject.getJSONArray("messages");
					if(messages!=null){
						for(int i=0;i<messages.length();i++){
							JSONObject msgObj=messages.getJSONObject(i);
							String id=msgObj.getString("id");
							String tittle=msgObj.getString("title");
							String msg=msgObj.getString("msg");
							String type=msgObj.getString("type");
							String status=msgObj.getString("status");
							//订单超额的时候没有linId
							String linkId="";
							try {
								linkId=msgObj.getString("linkId");
							} catch (Exception e) {
								// TODO: handle exception
							}
							SenderEntity senderEntity=null;
							JSONObject senderObj=msgObj.getJSONObject("sender");
							String sendId=senderObj.getString("id");
							String userPhone=senderObj.getString("userPhone");
							String userImageUrl="";
							try {
								userImageUrl=senderObj.getString("userImageUrl");
							} catch (Exception e) {
								// TODO: handle exception
							}
							String userRegistTime=senderObj.getString("userRegistTime");
							String userRole=senderObj.getString("userRole");
							String userOpStatus=senderObj.getString("userOpStatus");
							String userOrderCount=senderObj.getString("userOrderCount");
							String userTotalScore=senderObj.getString("userTotalScore");
							String userCommentCount=senderObj.getString("userCommentCount");
							String wowo=senderObj.getString("wowo");
							senderEntity=new SenderEntity(sendId, userPhone, userImageUrl, userRegistTime, userRole, userOpStatus, userOrderCount, userTotalScore, userCommentCount, wowo);
							String createTime=msgObj.getString("createTime");
							String updateTime=msgObj.getString("updateTime");
							String needPush=msgObj.getString("needPush");
							String retryTimes=msgObj.getString("retryTimes");
							MessageEntity messageEntity=new MessageEntity(sendId, tittle, msg, type, userOpStatus, linkId, senderEntity, createTime, updateTime, needPush, retryTimes);
							list.add(messageEntity);
						}
						return list;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

}
