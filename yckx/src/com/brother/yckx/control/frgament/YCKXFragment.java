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
	private ImageView imgLeft,imgRight;//(��������ܵ��)
	private TextView tv_title;
	private PopupWindow popupWindow;
	private View popupView;
	//�ߵµ�ͼ
	private MapView mapView;
	private AMap aMap;
	private UiSettings mUiSettings;
	//��λ
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	//marke
	private MarkerOptions markerOption;
	private Marker marker;

	//��ʾ�Ƽ��̼ҵ��б�
	private ListView recommentListView_location;
	private RBAdapter rbAdapter;

	private double latFirst;
	private double lngFirst;

	/**��ȡ�̼��������ݳɹ�*/
	private final int LOADBSTYPE_SUCCESS=10;
	/**�Ƿ�������ʾ�Ƽ�����Ϣ*/
	boolean resetRecommentBusiness;
	/**��ȡָ����γ�ȸ����̼���Ϣ�ɹ�*/
	private final int LOAD_LAT_LNG_BS_MESSAGE_SUCCESS=20;
	/**�ߵµ�ͼ��ʾ�����б�*/
	private ListView selectClassListView;
	private List<CompanyTypeEntity> gaodeList;
	private SelectGaodeCalssAdapter selectGaodeClassAdapter;
	//�жϵ�ǰҳ�������̼��Ƽ��б�ľ�γ���Ƕ�λ��ȡ������ͨ��������ȡ���������ľ�γ������ݾ�γ�ȶ�λ
	String mapStype=GlobalServiceUtils.getGloubalServiceSession("mapStyle")+"";

    private View backgroundView;
    
	/**����˰�ť��������λ*/
	private ImageView btnLocation;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		resetRecommentBusiness=true;
		//�ж���ʾ�Ƕ�λ������ʾ����������
		
		if(mapStype.equals("location")){
			view=inflater.inflate(R.layout.yckx_fragment,container,false);
			mapView=(MapView) view.findViewById(R.id.mapView);
			mapView.onCreate(savedInstanceState);
			initLocationView();
			initLocationMap();
			//�Ƽ��̼��б�---
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

		//���ظߵµ�ͼ������ʾ����
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

		//��������λ
		btnLocation=(ImageView) view.findViewById(R.id.btnLocation);
		btnLocation.setOnClickListener(listener);
	}


	/**
	 * ����һЩamap������
	 */
	private void setUpLocationMap() {
		// �Զ���ϵͳ��λС����
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gaode_point));// ����С�����ͼ��location_marker
		//myLocationStyle.strokeColor(Color.BLACK);// ����Բ�εı߿���ɫ
		//myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// ����Բ�ε������ɫ
		// myLocationStyle.anchor(int,int)//����С�����ê��
		//myLocationStyle.strokeWidth(1.0f);// ����Բ�εı߿��ϸ
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		//AMap.moveCamera(CameraUpdateFactory.zoomTo(zoom))
		aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		// aMap.setMyLocationType()
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setScaleControlsEnabled(true);//��������ʾ
		mUiSettings.setZoomControlsEnabled(false);//�����ֶ��ı������
		mUiSettings.setCompassEnabled(true);//����ָ����

		aMap.setOnMarkerClickListener(this);// ���õ��marker�¼�������
		aMap.setOnInfoWindowClickListener(this);// ���õ��infoWindow�¼�������
		aMap.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ
		//addMarkersToMap();// ����ͼ�����marker
	}
	
	/**������λ*/
	private void startThisLocation(){
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(getActivity());
			mLocationOption = new AMapLocationClientOption();
			//���ö�λ����
			mlocationClient.setLocationListener(this);
			//����Ϊ�߾��ȶ�λģʽ
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//���ö�λ����
			mlocationClient.setLocationOption(mLocationOption);
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����stopLocation()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���onDestroy()����
			// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������stopLocation()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
			mlocationClient.startLocation();
		}
	}
	
	/**ֹͣע����λ*/
	private void stopDestoryLocation(){
		mlocationClient.stopLocation();//ֹͣ��λ
		mlocationClient.onDestroy();//���ٶ�λ�ͻ��ˡ�
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
				//ToastUtil.show(getActivity(), "����˶�λ", 2000);
				break;
			case R.id.btnLocation_search:
				//ToastUtil.show(getActivity(), "�����������˶�λ", 2000);
				Intent intent=new Intent(getActivity(),HomeActivity.class);
				startActivity(intent);
				getActivity().finish();
				break;
			}

		}
	};

	/** ��ʼ��PopupMenu */
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
		// ����Ϊtouch������ر�(һ��Ҫ�����ú����ı���)
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.setOutsideTouchable(true);
	}


	/**��ȡ�̼����ͽӿ�*/
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
					System.out.println("-->>loadBusinessType()��������ʧ��");
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
			case LOADBSTYPE_SUCCESS://�̼����ͼ�����
				gaodeList=(List<CompanyTypeEntity>) msg.obj;
				if(imgRight!=null){
					imgRight.setClickable(true);
				}
				break;
			case LOAD_LAT_LNG_BS_MESSAGE_SUCCESS://ѡ���һ���̼ң����뵽����ҳ��
				//��ʾ��Ӧ���Ƽ��̼���Ϣ
				//
				List<BusinessEntity> list=(List<BusinessEntity>) msg.obj;
				showRecommendBusiness(list);
				break;
			}
		};
	};

	/**��ʾ�Ƽ�����Ϣ*/
	@SuppressWarnings("unchecked")
	private void showRecommendBusiness(List<BusinessEntity> list){
		if(mapStype.equals("location")){//��λҳ��
			if(list!=null){
				for(int i=0;i<list.size();i++){
					String lat_show=list.get(i).getLocation_lat();
					String lng_show=list.get(i).getLocation_lng();
					double lat_show_float=Double.parseDouble(lat_show);
					double lng_show_float=Double.parseDouble(lng_show);
					LatLng startLatlng = new LatLng(latFirst, lngFirst);
					LatLng endLatlng = new LatLng(lat_show_float, lng_show_float);
					// ��������������
					double distance=AMapUtils.calculateLineDistance(startLatlng, endLatlng);
					list.get(i).setDistance(distance);
				}
				//���������б�
				Collections.sort(list);
				tempbussinesslist=list;
			}
			//չʾ���µ��Ƽ��б�
			if(list.size()>3){
				//ֻѡ����������������
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
		}else{//����ҳ��
			if(list!=null){
				for(int i=0;i<list.size();i++){
					String lat_show=list.get(i).getLocation_lat();
					String lng_show=list.get(i).getLocation_lng();
					double lat_show_float=Double.parseDouble(lat_show);
					double lng_show_float=Double.parseDouble(lng_show);
					//LatLng startLatlng = new LatLng(latFirst, lngFirst);
					LatLng startLatlng = new LatLng(latLonPoint_search.getLatitude(), latLonPoint_search.getLongitude());
					LatLng endLatlng = new LatLng(lat_show_float, lng_show_float);
					// ��������������
					double distance=AMapUtils.calculateLineDistance(startLatlng, endLatlng);
					list.get(i).setDistance(distance);
				}
				//���������б�
				Collections.sort(list);
				tempbussinesslist=list;
			}
			//չʾ���µ��Ƽ��б�
			if(list.size()>3){
				//ֻѡ����������������
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


	/**���湫˾�Ƽ��б����ڵ������marker������չʾ*/
	private List<BusinessEntity> tempbussinesslist;

	//http://112.74.18.34:80/wc/company/lbs?lng=108.397434&lat=22.81906
	/**��ȡ����ָ����γ�ȸ������̵���Ϣ*/
	private void getBusinessList(final HashMap<String,String> hasp){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String respose=ApacheHttpUtil.httpGet(WEBInterface.MAP_STORELIST_URL, hasp);
				//��������
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
	//�ߵص�ͼ��������
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
		//����ҳ�������Ϊ��λ
		GlobalServiceUtils.setGloubalServiceSession("mapStyle", "location");
	}
	//-----marker--- start---//
	/**
	 * �ڵ�ͼ�����marker
	 * 
	 */
	private void addMarkersToMap(LatLng latlng,final BusinessEntity businessEntity) {
		//������ʾ��ע������������ʾ���ݣ�λ�ã������С��ɫ������ɫ��ת�Ƕ�,Zֵ��
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
			tv_freeTime.setText("��"+businessEntity.getFreeTime());
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
	 * marker����
	 * */
	@Override
	public boolean onMarkerClick(Marker arg0) {
		BusinessEntity markerBussinessEntity=(BusinessEntity) arg0.getObject();
		ToastUtil.show(getActivity(), ""+markerBussinessEntity.getCompanyName(),3000);
		Intent intent=new Intent(getActivity(),OrdersActivity.class);
		intent.putExtra("BusinessEntity", markerBussinessEntity);
		startActivity(intent);
		getActivity().finish();
		//��λ����
		/*if(mapStype.equals("location")){
			if(tempbussinesslist!=null){
				BusinessEntity markerBussinessEntity=(BusinessEntity) arg0.getObject();
				for(int i=0;i<tempbussinesslist.size();i++){
					tempbussinesslist.get(i).setSelect(false);
					if(tempbussinesslist.get(i).getCompanyId().equals(markerBussinessEntity.getCompanyId())){
						tempbussinesslist.get(i).setSelect(true);
					}
				}
				//����չʾ�Ƽ��̼��б�
				if(rbAdapter!=null){
					//չʾ���µ��Ƽ��б�
					if(tempbussinesslist.size()>3){
						//ֻѡ����������������
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

	/**marker�����window�������*/
	@Override
	public void onInfoWindowClick(Marker arg0) {
		/*Log.d("yckx", mapStype);
		if(mapStype.equals("location")){
			BusinessEntity entity=(BusinessEntity)arg0.getObject();
			Intent intent=new Intent(getActivity(),OrdersActivity.class);
			intent.putExtra("BusinessEntity", entity);
			//���õ�����ʼλ��
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

	/**�Զ���infoWindow����*/
	private void defMyInfoWind(Marker marker, View view){
		if(mapStype.equals("location")){
			BusinessEntity bussinessEntity=(BusinessEntity) marker.getObject();
			TextView tv_companyName=(TextView) view.findViewById(R.id.marker_companeyName);
			TextView tv_freeTime=(TextView) view.findViewById(R.id.freeTime);
			TextView tv_partCount=(TextView) view.findViewById(R.id.partCount);
			TextView tv_dazhe=(TextView) view.findViewById(R.id.dazhe);
			tv_companyName.setText(bussinessEntity.getCompanyName());
			if(!bussinessEntity.getFreeTime().equals("")){
				tv_freeTime.setText("��"+bussinessEntity.getFreeTime());
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
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
				String str=amapLocation.toString();
				//���������̼�
				double lat=amapLocation.getLatitude();
				double lng=amapLocation.getLongitude();
				//ʵʱ�����λ�ã����ڶ���ҳ�浼��
				GlobalServiceUtils.setGloubalServiceSession("lat", lat);
				GlobalServiceUtils.setGloubalServiceSession("lng", lng);

				/*//����һ��Բ��
				LatLng thisCenter = new LatLng(lat, lng);// �����о�γ��
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
						//ˢ�µ�ͼ
						latFirst=lat;
						lngFirst=lng;
						resetRecommentBusiness=false;
						getBusinessList(map);
					}
				}
			}
		} else {
			String errText = "��λʧ��," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
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

	//----------���øߵµ�ͼ������ʾ��ģ��(ȫ�������У��Ƶꡢͣ����)start-----------//

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
					ToastUtil.show(getActivity(), "ѡ����"+clickEntity.getStoreName(), 2000);
					popupWindow.dismiss();
				}});
			return arg1;
		}}
	//----------���øߵµ�ͼ������ʾ��ģ��(ȫ�������У�ͣ����)end-----------//

	//------------�ߵµ�ͼ����  start-------------------------------//

	/**
	 * �Ե�ͼ���н���
	 */
	private void getMapScreenShot() {
		if(mapStype.equals("location")){
			Log.d("yckx", "amap��ͼ");
			if(aMap!=null){
				aMap.getMapScreenShot(this);
				aMap.invalidate();// ˢ�µ�ͼ
			}
		}
		if(mapStype.equals("searchPOI")){
			Log.d("yckx", "searchPOI��ͼ");
			if(aMap_search!=null){
				aMap_search.getMapScreenShot(this);
				aMap_search.invalidate();// ˢ�µ�ͼ
			}
		}

	}

	private File yckx_tempFile;
	private String fileName = "/sdcard/yckx/yckx_maobili_temp.png";

	/**�ߵµ�ͼ��ͼ����bitmap����poi��������ģ����ë����Ч��*/
	@SuppressLint("SdCardPath")
	@Override
	public void onMapScreenShot(Bitmap bitmap) {
		FileOutputStream gaodeFos = null;  				  
		yckx_tempFile = new File("/sdcard/yckx/");  
		if(!yckx_tempFile.exists()){
			yckx_tempFile.mkdirs();// �����ļ���  
		}
		try {   
			gaodeFos = new FileOutputStream(fileName); //д���洢�� 
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

	//------------�ߵµ�ͼ����  end-------------------------------//


	//-------------POI������ȡ��γ�Ⱥ�λ start---------------------//
	private MarkerOptions markerOption_search;
	private ProgressDialog progDialog_search = null;
	private GeocodeSearch geocoderSearch;
	private String addressName_search;
	private AMap aMap_search;
	private MapView mapView_search;
	private Marker geoMarker_search;
	private Marker regeoMarker_search;
	private LatLonPoint latLonPoint_search = null;
	private ImageView btnLocation_search;//��������λ
	//��ʾ�Ƽ��̼ҵ��б�
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
		//�Ƽ��̼�
		recommentListView_search=(ListView) view.findViewById(R.id.recommentListView_search);
		rbAdapter_search=new RBAdapter(getActivity());
		recommentListView_search.setAdapter(rbAdapter_search);
		
		mapView_search = (MapView) view.findViewById(R.id.mapView_search);
		mapView_search.onCreate(savedInstanceState);// �˷���������д
		btnLocation_search=(ImageView) view.findViewById(R.id.btnLocation_search);
		btnLocation_search.setOnClickListener(listener);
		initSearch();
	}

	/**
	 * ��ʼ��AMap����
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
		aMap_search.setOnMarkerDragListener(this);// ����marker����ק�¼�������
		aMap_search.setOnMapLoadedListener(this);// ����amap���سɹ��¼�������
		aMap_search.setOnMarkerClickListener(this);// ���õ��marker�¼�������
		aMap_search.setOnInfoWindowClickListener(this);// ���õ��infoWindow�¼�������
		aMap_search.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ
		addMarkersToMap_search();// ����ͼ�����marker
	}

	private void addMarkersToMap_search() {
		LatLng searchLatLng = new LatLng(latLonPoint_search.getLatitude(), latLonPoint_search.getLongitude());// �����о�γ��
		Marker searchmarker = aMap_search.addMarker(new MarkerOptions()
		.position(searchLatLng)
		.title(""+GlobalServiceUtils.getGloubalServiceSession("searchAdress")).snippet("���Ĳ�ѯ�ص�")
		.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				.draggable(true));
		searchmarker.showInfoWindow();// ����Ĭ����ʾһ��infowinfow
	}

	/**
	 * ��Ӧ��������
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// ��һ��������ʾһ��Latlng���ڶ�������ʾ��Χ�����ף�������������ʾ�ǻ�ϵ����ϵ����GPSԭ������ϵ
		geocoderSearch.getFromLocationAsyn(query);// ����ͬ��������������
	}

	/**
	 * ��ʾ�������Ի���
	 */
	public void showDialog() {
		progDialog_search.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog_search.setIndeterminate(false);
		progDialog_search.setCancelable(true);
		progDialog_search.setMessage("���ڻ�ȡ��ַ");
		progDialog_search.show();
	}


	/**
	 * ���ؽ������Ի���
	 */
	public void dismissDialog() {
		if (progDialog_search != null) {
			progDialog_search.dismiss();
		}
	}



	/**
	 * ��������ѯ�ص�
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
				addressName_search = "��γ��ֵ:" + address.getLatLonPoint() + "\nλ������:"
						+ address.getFormatAddress();
				//ToastUtil.show(GeocoderActivity.this, addressName);
				//�����Ƽ��̼���Ϣ
				HashMap<String,String> hashMap=new HashMap<String, String>();
				hashMap.put("lat", latLonPoint_search.getLatitude()+"");
				hashMap.put("lng", latLonPoint_search.getLongitude()+"");
				getBusinessList(hashMap);
				
			} else {
				ToastUtil.show(getActivity(), "�޽��",2000);
			}

		} else if (rCode == 27) {
			ToastUtil.show(getActivity(), "�������",2000);
		} else if (rCode == 32) {
			ToastUtil.show(getActivity(), "key error",2000);
		} else {
			ToastUtil.show(getActivity(),
					"��������"+ rCode,2000);
		}
	}


	/**
	 * ��������ص�
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName_search = result.getRegeocodeAddress().getFormatAddress()
						+ "����";
				aMap_search.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint_search), 15));
				regeoMarker_search.setPosition(AMapUtil.convertToLatLng(latLonPoint_search));
				//ToastUtil.show(GeocoderActivity.this, addressName);
				//�����Ƽ��̼���Ϣ
				HashMap<String,String> hashMap=new HashMap<String, String>();
				hashMap.put("lat", latLonPoint_search.getLatitude()+"");
				hashMap.put("lng", latLonPoint_search.getLongitude()+"");
				getBusinessList(hashMap);
			} else {
				ToastUtil.show(getActivity(), "�޽��",2000);
			}
		} else if (rCode == 27) {
			ToastUtil.show(getActivity(), "�������",2000);
		} else if (rCode == 32) {
			ToastUtil.show(getActivity(), "key error",2000);
		} else {
			ToastUtil.show(getActivity(),
					"����" + rCode,2000);
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


	//-------------POI������ȡ��γ�Ⱥ�λ end---------------------//

}
