package com.brother.yckx.control.frgament;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMapScreenShotListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMap.OnMarkerDragListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.brother.BaseAdapter2;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.adapter.fragmnet.RBAdapter;
import com.brother.yckx.control.activity.map.POIGJZSearchActivity;
import com.brother.yckx.control.activity.map.util.AMapUtil;
import com.brother.yckx.control.activity.owner.HomeActivity;
import com.brother.yckx.control.activity.owner.OrdersActivity;
import com.brother.yckx.model.BusinessEntity;
import com.brother.yckx.model.CompanyTypeEntity;
import com.brother.yckx.view.image.CacheImageView;
@SuppressLint("SdCardPath")
public class YCKXFragment extends Fragment implements LocationSource,AMapLocationListener,OnMarkerClickListener,OnInfoWindowClickListener,InfoWindowAdapter,OnMapScreenShotListener,OnGeocodeSearchListener,OnMarkerDragListener,OnMapLoadedListener{
	private View view;
	private ImageView imgLeft,imgRight;//(加载完才能点击)
	private TextView tv_title;
	private PopupWindow popupWindow;
	private View popupView;
	//高德地图
	private MapView mapView;
	private AMap aMap;
	private UiSettings mUiSettings;
	//定位
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	//marke
	private MarkerOptions markerOption;
	private Marker marker;

	//显示推荐商家的列表
	private ListView recommentListView_location;
	private RBAdapter rbAdapter;

	private double latFirst;
	private double lngFirst;

	/**获取商家类型数据成功*/
	private final int LOADBSTYPE_SUCCESS=10;
	/**是否重新显示推荐商信息*/
	boolean resetRecommentBusiness;
	/**获取指定经纬度附件商家信息成功*/
	private final int LOAD_LAT_LNG_BS_MESSAGE_SUCCESS=20;
	/**高德地图显示分类列表*/
	private ListView selectClassListView;
	private List<CompanyTypeEntity> gaodeList;
	private SelectGaodeCalssAdapter selectGaodeClassAdapter;
	//判断当前页面搜索商家推荐列表的经纬度是定位获取到还是通过搜索获取。搜索到的经纬度则根据经纬度定位
	String mapStype=GlobalServiceUtils.getGloubalServiceSession("mapStyle")+"";

    private View backgroundView;
    
	/**点击此按钮无条件定位*/
	private ImageView btnLocation;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		resetRecommentBusiness=true;
		//判断显示是定位还是显示搜索的内容
		
		if(mapStype.equals("location")){
			view=inflater.inflate(R.layout.yckx_fragment,container,false);
			mapView=(MapView) view.findViewById(R.id.mapView);
			mapView.onCreate(savedInstanceState);
			initLocationView();
			initLocationMap();
			//推荐商家列表---
			recommentListView_location=(ListView) view.findViewById(R.id.recommentListView);
			rbAdapter=new RBAdapter(getActivity());
			recommentListView_location.setAdapter(rbAdapter);
			recommentListView_location.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					BusinessEntity business=(BusinessEntity) arg0.getItemAtPosition(arg2);
					Intent intent=new Intent(getActivity(),OrdersActivity.class);
					intent.putExtra("BusinessEntity", business);
					startActivity(intent);
					getActivity().finish();
				}});
		}else if(mapStype.equals("searchPOI")){
			view=inflater.inflate(R.layout.yckx_fragment_search,container,false);
			intSearchMap(view,savedInstanceState);
		}

		//加载高德地图分类显示数据
		loadBusinessType();
		return view;
	}

	private void initLocationMap(){
		//if(aMap==null){
		aMap=mapView.getMap();
		//}
		setUpLocationMap();
	}

	private void initLocationView(){
		imgLeft=(ImageView) view.findViewById(R.id.iv_action_left);
		imgRight=(ImageView) view.findViewById(R.id.iv_action_right);

		imgLeft.setImageResource(R.drawable.icon_default_search);
		imgRight.setImageResource(R.drawable.icon_shop_all_blue);
		imgRight.setClickable(false);

		imgLeft.setOnClickListener(listener);
		imgRight.setOnClickListener(listener);

		tv_title=(TextView) view.findViewById(R.id.tv_actionbar_title);
		tv_title.setText(getResources().getString(R.string.home_title_bar)+"");

		//无条件定位
		btnLocation=(ImageView) view.findViewById(R.id.btnLocation);
		btnLocation.setOnClickListener(listener);
	}


	/**
	 * 设置一些amap的属性
	 */
	private void setUpLocationMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gaode_point));// 设置小蓝点的图标location_marker
		//myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		//myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		//myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		//AMap.moveCamera(CameraUpdateFactory.zoomTo(zoom))
		aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		// aMap.setMyLocationType()
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setScaleControlsEnabled(true);//比例尺显示
		mUiSettings.setZoomControlsEnabled(false);//设置手动改变比例尺
		mUiSettings.setCompassEnabled(true);//设置指南针

		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		//addMarkersToMap();// 往地图上添加marker
	}
	
	/**启动定位*/
	private void startThisLocation(){
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(getActivity());
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}
	
	/**停止注销定位*/
	private void stopDestoryLocation(){
		mlocationClient.stopLocation();//停止定位
		mlocationClient.onDestroy();//销毁定位客户端。
	}

	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.iv_action_left:
				getMapScreenShot();
				break;
			case R.id.iv_action_right:
				initPopupMenu();
				popupWindow.showAsDropDown(arg0);
				break;
			case R.id.btnLocation:
				aMap.invalidate();
				//ToastUtil.show(getActivity(), "点击了定位", 2000);
				break;
			case R.id.btnLocation_search:
				//ToastUtil.show(getActivity(), "搜索界面点击了定位", 2000);
				Intent intent=new Intent(getActivity(),HomeActivity.class);
				startActivity(intent);
				getActivity().finish();
				break;
			}

		}
	};

	/** 初始化PopupMenu */
	private void initPopupMenu() {
		if (popupView != null) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = inflater.inflate(R.layout.inflater_popuwindow_gaode_select_class, null);
		selectClassListView=(ListView) popupView.findViewById(R.id.selectClass_Listview);
		selectGaodeClassAdapter=new SelectGaodeCalssAdapter(getActivity());
		if(gaodeList!=null){
			selectGaodeClassAdapter.addDataToAdapter(gaodeList);
		}
		selectClassListView.setAdapter(selectGaodeClassAdapter);
		popupWindow = new PopupWindow(popupView, 200, ViewGroup.LayoutParams.WRAP_CONTENT, true);
		// 设置为touch窗口外关闭(一定要先设置好它的背景)
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.setOutsideTouchable(true);
	}


	/**获取商家类型接口*/
	private void loadBusinessType(){
		new Thread(new Runnable() {
			@SuppressWarnings("unchecked")//WEBInterface.STORE_TYPE
			@Override
			public void run() {
				String respose=ApacheHttpUtil.httpGet(WEBInterface.STORE_TYPE, null);
				Log.d("yckx", respose+"");
				if(respose==null){
					return;
				}
				try {
					List<CompanyTypeEntity> businessList=null;;
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						businessList =new ArrayList<CompanyTypeEntity>();
						JSONArray jsonArray=jSONObject.getJSONArray("types");
						if(jsonArray!=null){
							for(int i=0;i<jsonArray.length();i++){
								JSONObject obj=jsonArray.getJSONObject(i);
								String id=obj.getString("id");
								String name=obj.getString("name");
								String storeImgUrl=obj.getString("icon");
								String amapUrl=obj.getString("mapIcon");
								CompanyTypeEntity entity=new CompanyTypeEntity(id, name, storeImgUrl, amapUrl);
								businessList.add(entity);
							}
						}
					}
					Message msg=new Message();
					msg.what=LOADBSTYPE_SUCCESS;
					msg.obj=businessList;
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					System.out.println("-->>loadBusinessType()解析数据失败");
					e.printStackTrace();
				}
			}
		}).start();
	}

	@SuppressLint("HandlerLeak") 
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOADBSTYPE_SUCCESS://商家类型加载完
				gaodeList=(List<CompanyTypeEntity>) msg.obj;
				if(imgRight!=null){
					imgRight.setClickable(true);
				}
				break;
			case LOAD_LAT_LNG_BS_MESSAGE_SUCCESS://选择第一个商家，进入到二级页面
				//显示对应的推荐商家信息
				//
				List<BusinessEntity> list=(List<BusinessEntity>) msg.obj;
				showRecommendBusiness(list);
				break;
			}
		};
	};

	/**显示推荐商信息*/
	@SuppressWarnings("unchecked")
	private void showRecommendBusiness(List<BusinessEntity> list){
		if(mapStype.equals("location")){//定位页面
			if(list!=null){
				for(int i=0;i<list.size();i++){
					String lat_show=list.get(i).getLocation_lat();
					String lng_show=list.get(i).getLocation_lng();
					double lat_show_float=Double.parseDouble(lat_show);
					double lng_show_float=Double.parseDouble(lng_show);
					LatLng startLatlng = new LatLng(latFirst, lngFirst);
					LatLng endLatlng = new LatLng(lat_show_float, lng_show_float);
					// 计算量坐标点距离
					double distance=AMapUtils.calculateLineDistance(startLatlng, endLatlng);
					list.get(i).setDistance(distance);
				}
				//重新排列列表
				Collections.sort(list);
				tempbussinesslist=list;
			}
			//展示最新的推荐列表
			if(list.size()>3){
				//只选择其中三项来排列
				rbAdapter.clearAdapter();
				for(int k=0;k<3;k++){
					list.get(0).setSelect(true);
					rbAdapter.addDataToAdapter(list.get(k));
				}
				rbAdapter.notifyDataSetChanged();
				for(int k=2;k>=0;k--){
					double lat_marke=Double.parseDouble(list.get(k).getLocation_lat());
					double lng_marke=Double.parseDouble(list.get(k).getLocation_lng());
					LatLng latlng_marke=new LatLng(lat_marke, lng_marke);
					addMarkersToMap(latlng_marke,list.get(k));
				}

			}
			if(list.size()<=3){
				rbAdapter.clearAdapter();
				if(list.size()>0){
					list.get(0).setSelect(true);
				}
				rbAdapter.addDataToAdapter(list);
				rbAdapter.notifyDataSetChanged();
				for(int i=list.size()-1;i>=0;i--){
					double lat_marke=Double.parseDouble(list.get(i).getLocation_lat());
					double lng_marke=Double.parseDouble(list.get(i).getLocation_lng());
					LatLng latlng_marke=new LatLng(lat_marke, lng_marke);
					addMarkersToMap(latlng_marke,list.get(i));
				}
			}
		}else{//搜索页面
			if(list!=null){
				for(int i=0;i<list.size();i++){
					String lat_show=list.get(i).getLocation_lat();
					String lng_show=list.get(i).getLocation_lng();
					double lat_show_float=Double.parseDouble(lat_show);
					double lng_show_float=Double.parseDouble(lng_show);
					//LatLng startLatlng = new LatLng(latFirst, lngFirst);
					LatLng startLatlng = new LatLng(latLonPoint_search.getLatitude(), latLonPoint_search.getLongitude());
					LatLng endLatlng = new LatLng(lat_show_float, lng_show_float);
					// 计算量坐标点距离
					double distance=AMapUtils.calculateLineDistance(startLatlng, endLatlng);
					list.get(i).setDistance(distance);
				}
				//重新排列列表
				Collections.sort(list);
				tempbussinesslist=list;
			}
			//展示最新的推荐列表
			if(list.size()>3){
				//只选择其中三项来排列
				rbAdapter_search.clearAdapter();
				for(int k=0;k<3;k++){
					list.get(0).setSelect(true);
					rbAdapter_search.addDataToAdapter(list.get(k));
				}
				rbAdapter_search.notifyDataSetChanged();
				for(int k=2;k>=0;k--){
					double lat_marke=Double.parseDouble(list.get(k).getLocation_lat());
					double lng_marke=Double.parseDouble(list.get(k).getLocation_lng());
					LatLng latlng_marke=new LatLng(lat_marke, lng_marke);
					//addMarkersToMap(latlng_marke,list.get(k));
				}

			}
			if(list.size()<=3){
				rbAdapter_search.clearAdapter();
				if(list.size()>0){
					list.get(0).setSelect(true);
				}
				rbAdapter_search.addDataToAdapter(list);
				rbAdapter_search.notifyDataSetChanged();
				for(int i=list.size()-1;i>=0;i--){
					double lat_marke=Double.parseDouble(list.get(i).getLocation_lat());
					double lng_marke=Double.parseDouble(list.get(i).getLocation_lng());
					LatLng latlng_marke=new LatLng(lat_marke, lng_marke);
					//addMarkersToMap(latlng_marke,list.get(i));
				}
			}
		}
	}


	/**保存公司推荐列表，用于点击其他marker后重新展示*/
	private List<BusinessEntity> tempbussinesslist;

	//http://112.74.18.34:80/wc/company/lbs?lng=108.397434&lat=22.81906
	/**获取附件指定经纬度附件的商店信息*/
	private void getBusinessList(final HashMap<String,String> hasp){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String respose=ApacheHttpUtil.httpGet(WEBInterface.MAP_STORELIST_URL, hasp);
				//解析数据
				List<BusinessEntity> list=ParseJSONUtils.parseBusinessJSOn(respose);
				if(list==null){
					return;
				}
				Message msg=new Message();
				msg.what=LOAD_LAT_LNG_BS_MESSAGE_SUCCESS;
				msg.obj=list;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	//高地地图生命周期
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mapView!=null){
			mapView.onResume();
		}
		if(mapView_search!=null){
			mapView_search.onResume();
		}

	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if(mapView!=null){
			mapView.onCreate(outState);
		}
		if(mapView_search!=null){
			mapView_search.onCreate(outState);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mapView!=null){
			mapView.onPause();
		}
		if(mapView_search!=null){
			mapView_search.onPause();
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mapView!=null){
			mapView.onDestroy();
		}
		if(mapView_search!=null){
			mapView_search.onDestroy();
		}
		//销毁页面后设置为定位
		GlobalServiceUtils.setGloubalServiceSession("mapStyle", "location");
	}
	//-----marker--- start---//
	/**
	 * 在地图上添加marker
	 * 
	 */
	private void addMarkersToMap(LatLng latlng,final BusinessEntity businessEntity) {
		//文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色旋转角度,Z值等
		markerOption = new MarkerOptions();
		markerOption.position(latlng);
		//markerOption.title(businessEntity.getCompanyName()).snippet(businessEntity.getDescription());
		//markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_small));
	
		//marker.setObject(businessEntity);
		//marker.showInfoWindow();
		
		View markView=getActivity().getLayoutInflater().inflate(R.layout.dialog_marker,null);
		TextView tv_companyName=(TextView) markView.findViewById(R.id.marker_companeyName);
		TextView tv_freeTime=(TextView) markView.findViewById(R.id.freeTime);
		TextView tv_partCount=(TextView) markView.findViewById(R.id.partCount);
		TextView tv_dazhe=(TextView) markView.findViewById(R.id.dazhe);
		tv_companyName.setText(businessEntity.getCompanyName());
		if(!businessEntity.getFreeTime().equals("")){
			tv_freeTime.setText("免"+businessEntity.getFreeTime());
		}else{
			tv_freeTime.setText(businessEntity.getSpacePrice());
		}
		
		tv_partCount.setText(businessEntity.getSpaceCount()+"/"+businessEntity.getTotalCount());
		tv_dazhe.setText(businessEntity.getSaleMsg());
		BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(markView);
		markerOption.icon(descriptor);
		marker = aMap.addMarker(markerOption);
		marker.setObject(businessEntity);
	}

	/**
	 * marker监听
	 * */
	@Override
	public boolean onMarkerClick(Marker arg0) {
		BusinessEntity markerBussinessEntity=(BusinessEntity) arg0.getObject();
		ToastUtil.show(getActivity(), ""+markerBussinessEntity.getCompanyName(),3000);
		Intent intent=new Intent(getActivity(),OrdersActivity.class);
		intent.putExtra("BusinessEntity", markerBussinessEntity);
		startActivity(intent);
		getActivity().finish();
		//定位界面
		/*if(mapStype.equals("location")){
			if(tempbussinesslist!=null){
				BusinessEntity markerBussinessEntity=(BusinessEntity) arg0.getObject();
				for(int i=0;i<tempbussinesslist.size();i++){
					tempbussinesslist.get(i).setSelect(false);
					if(tempbussinesslist.get(i).getCompanyId().equals(markerBussinessEntity.getCompanyId())){
						tempbussinesslist.get(i).setSelect(true);
					}
				}
				//重新展示推荐商家列表
				if(rbAdapter!=null){
					//展示最新的推荐列表
					if(tempbussinesslist.size()>3){
						//只选择其中三项来排列
						rbAdapter.clearAdapter();
						for(int k=0;k<3;k++){
							rbAdapter.addDataToAdapter(tempbussinesslist.get(k));
						}
						rbAdapter.notifyDataSetChanged();

					}
					if(tempbussinesslist.size()<=3){
						rbAdapter.clearAdapter();
						rbAdapter.addDataToAdapter(tempbussinesslist);
						rbAdapter.notifyDataSetChanged();
					}
				}
			}
		}*/

		return false;
	}

	/**marker上面的window点击监听*/
	@Override
	public void onInfoWindowClick(Marker arg0) {
		/*Log.d("yckx", mapStype);
		if(mapStype.equals("location")){
			BusinessEntity entity=(BusinessEntity)arg0.getObject();
			Intent intent=new Intent(getActivity(),OrdersActivity.class);
			intent.putExtra("BusinessEntity", entity);
			//设置导航起始位置
			//GlobalServiceUtils.setGloubalServiceSession("starNativeLat", GlobalServiceUtils.getGloubalServiceSession("lat"));
			//GlobalServiceUtils.setGloubalServiceSession("starNativeLng", GlobalServiceUtils.getGloubalServiceSession("lng"));
			startActivity(intent);
			getActivity().finish();
		}*/

	}

	@Override
	public View getInfoContents(Marker arg0) {
		/*if(mapStype.equals("location")){
			View infoWindow=getActivity().getLayoutInflater().inflate(R.layout.dialog_marker,null);
			defMyInfoWind(arg0,infoWindow);
		}*/
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		/*if(mapStype.equals("location")){
			View infoWindow=getActivity().getLayoutInflater().inflate(R.layout.dialog_marker,null);
			defMyInfoWind(arg0,infoWindow);
			return infoWindow;
		}*/
		return null;
	}

	/**自定义infoWindow窗口*/
	private void defMyInfoWind(Marker marker, View view){
		if(mapStype.equals("location")){
			BusinessEntity bussinessEntity=(BusinessEntity) marker.getObject();
			TextView tv_companyName=(TextView) view.findViewById(R.id.marker_companeyName);
			TextView tv_freeTime=(TextView) view.findViewById(R.id.freeTime);
			TextView tv_partCount=(TextView) view.findViewById(R.id.partCount);
			TextView tv_dazhe=(TextView) view.findViewById(R.id.dazhe);
			tv_companyName.setText(bussinessEntity.getCompanyName());
			if(!bussinessEntity.getFreeTime().equals("")){
				tv_freeTime.setText("免"+bussinessEntity.getFreeTime());
			}else{
				tv_freeTime.setText(bussinessEntity.getSpacePrice());
			}
			
			tv_partCount.setText(bussinessEntity.getSpaceCount()+"/"+bussinessEntity.getTotalCount());
			tv_dazhe.setText(bussinessEntity.getSaleMsg());
		}

	}

	//-----marker--- end---//

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				String str=amapLocation.toString();
				//搜索附近商家
				double lat=amapLocation.getLatitude();
				double lng=amapLocation.getLongitude();
				//实时保存此位置，用于二级页面导航
				GlobalServiceUtils.setGloubalServiceSession("lat", lat);
				GlobalServiceUtils.setGloubalServiceSession("lng", lng);

				/*//绘制一个圆形
				LatLng thisCenter = new LatLng(lat, lng);// 北京市经纬度
				circle = aMap.addCircle(new CircleOptions().center(thisCenter)
						.radius(4000).strokeColor(Color.argb(50, 1, 1, 1))
						.fillColor(Color.argb(50, 1, 1, 1)).strokeWidth(25));*/
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("lng", lng+"");
				map.put("lat", lat+"");
				if(resetRecommentBusiness){
					lngFirst=lat;
					latFirst=lng;
					resetRecommentBusiness=false;
					getBusinessList(map);
				}
				if(!resetRecommentBusiness){
					LatLng startLatlng = new LatLng(latFirst, lngFirst);
					LatLng endLatlng = new LatLng(lat, lng);
					double distance=AMapUtils.calculateLineDistance(startLatlng, endLatlng);
					if(distance>1000){
						Log.d("gaode", distance+"");
						//刷新地图
						latFirst=lat;
						lngFirst=lng;
						resetRecommentBusiness=false;
						getBusinessList(map);
					}
				}
			}
		} else {
			String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
			Log.e("AmapErr",errText);
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		startThisLocation();
	}
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	//----------设置高德地图分类显示的模块(全部，银行，酒店、停车场)start-----------//

	class SelectGaodeCalssAdapter extends BaseAdapter2<CompanyTypeEntity>{
		public SelectGaodeCalssAdapter(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(arg1==null){
				arg1=layoutInflater.inflate(R.layout.row_inflater_popuwindow_gaode_select_class,null);
			}
			final LinearLayout layout_select=(LinearLayout) arg1.findViewById(R.id.layout_select);
			CacheImageView selectIcon=(CacheImageView) arg1.findViewById(R.id.selectIcon);
			TextView selectName=(TextView) arg1.findViewById(R.id.selectName);
			CompanyTypeEntity entity=getItem(arg0);
			selectIcon.setImageUrl(WEBInterface.INDEXIMGURL+entity.getAmapImgUrl());
			selectName.setText(entity.getStoreName());
			layout_select.setTag(arg0);
			layout_select.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					CompanyTypeEntity clickEntity=gaodeList.get((Integer)layout_select.getTag());
					ToastUtil.show(getActivity(), "选择了"+clickEntity.getStoreName(), 2000);
					popupWindow.dismiss();
				}});
			return arg1;
		}}
	//----------设置高德地图分类显示的模块(全部，银行，停车场)end-----------//

	//------------高德地图截屏  start-------------------------------//

	/**
	 * 对地图进行截屏
	 */
	private void getMapScreenShot() {
		if(mapStype.equals("location")){
			Log.d("yckx", "amap截图");
			if(aMap!=null){
				aMap.getMapScreenShot(this);
				aMap.invalidate();// 刷新地图
			}
		}
		if(mapStype.equals("searchPOI")){
			Log.d("yckx", "searchPOI截图");
			if(aMap_search!=null){
				aMap_search.getMapScreenShot(this);
				aMap_search.invalidate();// 刷新地图
			}
		}

	}

	private File yckx_tempFile;
	private String fileName = "/sdcard/yckx/yckx_maobili_temp.png";

	/**高德地图截图，把bitmap传给poi搜索界面模糊成毛玻璃效果*/
	@SuppressLint("SdCardPath")
	@Override
	public void onMapScreenShot(Bitmap bitmap) {
		FileOutputStream gaodeFos = null;  				  
		yckx_tempFile = new File("/sdcard/yckx/");  
		if(!yckx_tempFile.exists()){
			yckx_tempFile.mkdirs();// 创建文件夹  
		}
		try {   
			gaodeFos = new FileOutputStream(fileName); //写到存储卡 
			boolean b = bitmap.compress(CompressFormat.PNG, 100, gaodeFos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {  
			gaodeFos.flush();  
			gaodeFos.close();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		if(mapStype.equals("location")){
			stopDestoryLocation();
		}
		Intent intent=new Intent(getActivity(),POIGJZSearchActivity.class);
		getActivity().startActivity(intent);
		getActivity().finish();
	}

	//------------高德地图截屏  end-------------------------------//


	//-------------POI搜索获取经纬度后定位 start---------------------//
	private MarkerOptions markerOption_search;
	private ProgressDialog progDialog_search = null;
	private GeocodeSearch geocoderSearch;
	private String addressName_search;
	private AMap aMap_search;
	private MapView mapView_search;
	private Marker geoMarker_search;
	private Marker regeoMarker_search;
	private LatLonPoint latLonPoint_search = null;
	private ImageView btnLocation_search;//无条件定位
	//显示推荐商家的列表
		private ListView recommentListView_search;
		private RBAdapter rbAdapter_search;
	private void intSearchMap(View view,Bundle savedInstanceState){
		//actionbar
		imgLeft=(ImageView) view.findViewById(R.id.iv_action_left);
		imgRight=(ImageView) view.findViewById(R.id.iv_action_right);
		imgLeft.setImageResource(R.drawable.icon_default_search);
		imgRight.setImageResource(R.drawable.icon_shop_all_blue);
		imgRight.setClickable(false);
		imgLeft.setOnClickListener(listener);
		imgRight.setOnClickListener(listener);
		tv_title=(TextView) view.findViewById(R.id.tv_actionbar_title);
		tv_title.setText(getResources().getString(R.string.home_title_bar)+"");
		//actionbar end
		//推荐商家
		recommentListView_search=(ListView) view.findViewById(R.id.recommentListView_search);
		rbAdapter_search=new RBAdapter(getActivity());
		recommentListView_search.setAdapter(rbAdapter_search);
		
		mapView_search = (MapView) view.findViewById(R.id.mapView_search);
		mapView_search.onCreate(savedInstanceState);// 此方法必须重写
		btnLocation_search=(ImageView) view.findViewById(R.id.btnLocation_search);
		btnLocation_search.setOnClickListener(listener);
		initSearch();
	}

	/**
	 * 初始化AMap对象
	 */
	private void initSearch() {
		double lat=(Double) GlobalServiceUtils.getGloubalServiceSession("searchLat");
		double lng=(Double) GlobalServiceUtils.getGloubalServiceSession("searchLng");
		latLonPoint_search=new LatLonPoint(lat,lng);
		aMap_search = mapView_search.getMap();
		regeoMarker_search = aMap_search.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gaode_point)));
		setupMarkerSearch();
		geocoderSearch = new GeocodeSearch(getActivity());
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog_search = new ProgressDialog(getActivity());
		getAddress(latLonPoint_search);
	}

	private void setupMarkerSearch(){
		aMap_search.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
		aMap_search.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap_search.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap_search.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap_search.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		addMarkersToMap_search();// 往地图上添加marker
	}

	private void addMarkersToMap_search() {
		LatLng searchLatLng = new LatLng(latLonPoint_search.getLatitude(), latLonPoint_search.getLongitude());// 西安市经纬度
		Marker searchmarker = aMap_search.addMarker(new MarkerOptions()
		.position(searchLatLng)
		.title(""+GlobalServiceUtils.getGloubalServiceSession("searchAdress")).snippet("您的查询地点")
		.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				.draggable(true));
		searchmarker.showInfoWindow();// 设置默认显示一个infowinfow
	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog_search.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog_search.setIndeterminate(false);
		progDialog_search.setCancelable(true);
		progDialog_search.setMessage("正在获取地址");
		progDialog_search.show();
	}


	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog_search != null) {
			progDialog_search.dismiss();
		}
	}



	/**
	 * 地理编码查询回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				aMap_search.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
				geoMarker_search.setPosition(AMapUtil.convertToLatLng(address
						.getLatLonPoint()));
				addressName_search = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
						+ address.getFormatAddress();
				//ToastUtil.show(GeocoderActivity.this, addressName);
				//设置推荐商家信息
				HashMap<String,String> hashMap=new HashMap<String, String>();
				hashMap.put("lat", latLonPoint_search.getLatitude()+"");
				hashMap.put("lng", latLonPoint_search.getLongitude()+"");
				getBusinessList(hashMap);
				
			} else {
				ToastUtil.show(getActivity(), "无结果",2000);
			}

		} else if (rCode == 27) {
			ToastUtil.show(getActivity(), "网络错误",2000);
		} else if (rCode == 32) {
			ToastUtil.show(getActivity(), "key error",2000);
		} else {
			ToastUtil.show(getActivity(),
					"其他错误"+ rCode,2000);
		}
	}


	/**
	 * 逆地理编码回调
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName_search = result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				aMap_search.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint_search), 15));
				regeoMarker_search.setPosition(AMapUtil.convertToLatLng(latLonPoint_search));
				//ToastUtil.show(GeocoderActivity.this, addressName);
				//设置推荐商家信息
				HashMap<String,String> hashMap=new HashMap<String, String>();
				hashMap.put("lat", latLonPoint_search.getLatitude()+"");
				hashMap.put("lng", latLonPoint_search.getLongitude()+"");
				getBusinessList(hashMap);
			} else {
				ToastUtil.show(getActivity(), "无结果",2000);
			}
		} else if (rCode == 27) {
			ToastUtil.show(getActivity(), "网络错误",2000);
		} else if (rCode == 32) {
			ToastUtil.show(getActivity(), "key error",2000);
		} else {
			ToastUtil.show(getActivity(),
					"其他" + rCode,2000);
		}
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub

	}


	//-------------POI搜索获取经纬度后定位 end---------------------//

}
