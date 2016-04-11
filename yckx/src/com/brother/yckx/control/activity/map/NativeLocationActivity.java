package com.brother.yckx.control.activity.map;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.WEBInterface;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.model.BusinessEntity;

public class NativeLocationActivity extends BaseActivity implements LocationSource,
AMapLocationListener,OnMarkerClickListener{
	private AMap aMap;
	private UiSettings mUiSettings;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	//marke
	private MarkerOptions markerOption;
	private Marker marker;
	private BusinessEntity businessEntity=null;
	private String mlat,mlng;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nativelocation);
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);// �˷���������д
		init();
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		businessEntity=(BusinessEntity) getIntent().getSerializableExtra("NativeLocationActivity");
        TextView tv_companyName=(TextView) findViewById(R.id.companyName);
        TextView tv_companyAdress=(TextView) findViewById(R.id.companyAdress);
        tv_companyName.setText(businessEntity.getCompanyName());
        tv_companyAdress.setText(businessEntity.getAddress());
        //�ݳ�����
        findViewById(R.id.drive).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				GlobalServiceUtils.setGloubalServiceSession("lat", mlat);
				GlobalServiceUtils.setGloubalServiceSession("lng", mlng);
				Intent intent=new Intent(NativeLocationActivity.this,DriveMapBaseActivity.class);
				intent.putExtra("DriveMapBaseActivity", businessEntity);
				startActivity(intent);
			}});
        //���е���
        findViewById(R.id.walk).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				GlobalServiceUtils.setGloubalServiceSession("lat", mlat);
				GlobalServiceUtils.setGloubalServiceSession("lng", mlng);
				Intent intent=new Intent(NativeLocationActivity.this,WalkMapBaseActivity.class);
				intent.putExtra("WalkMapBaseActivity", businessEntity);
				startActivity(intent);
			}});
	}

	/**
	 * ����һЩamap������
	 */
	private void setUpMap() {
		// �Զ���ϵͳ��λС����
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// ����С�����ͼ��
		myLocationStyle.strokeColor(Color.BLACK);// ����Բ�εı߿���ɫ
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// ����Բ�ε������ɫ
		// myLocationStyle.anchor(int,int)//����С�����ê��
		myLocationStyle.strokeWidth(1.0f);// ����Բ�εı߿��ϸ
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		// aMap.setMyLocationType()
		aMap.moveCamera(CameraUpdateFactory.zoomTo(12));//�������ż���
		aMap.setOnMarkerClickListener(this);// ���õ��marker�¼�������
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setScaleControlsEnabled(false);//��������ʾ
		mUiSettings.setZoomControlsEnabled(false);//�����ֶ��ı������
		mUiSettings.setCompassEnabled(false);//����ָ����
		//aMap.setOnInfoWindowClickListener(this);// ���õ��infoWindow�¼�������
		//aMap.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ
		//addMarkersToMap();// ����ͼ�����marker
	}


	//-----marker--- start---//
	/**
	 * �ڵ�ͼ�����marker
	 * 
	 */
	private void addMarkersToMap(LatLng latlng,BusinessEntity businessEntity) {
		//������ʾ��ע������������ʾ���ݣ�λ�ã������С��ɫ������ɫ��ת�Ƕ�,Zֵ��
		markerOption = new MarkerOptions();
		markerOption.position(latlng);
		markerOption.title(businessEntity.getCompanyName()).snippet(businessEntity.getDescription());
		markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_small));
		marker = aMap.addMarker(markerOption);
		/*marker.setObject(businessEntity);
		marker.showInfoWindow();*/
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * ��λ�ɹ���ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
				double lat=amapLocation.getLatitude();
				double lng=amapLocation.getLongitude();
				GlobalServiceUtils.setGloubalServiceSession("starNativeLat", lat);
				GlobalServiceUtils.setGloubalServiceSession("starNativeLng", lng);
			} else {
				String errText = "��λʧ��," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("yckx",errText);
			}
		}
	}

	/**
	 * ���λ
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
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

	/**
	 * ֹͣ��λ
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}



	private final int LOAD_LAT_LNG_BS_MESSAGE_SUCCESS=1;
	@SuppressLint("HandlerLeak")
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_LAT_LNG_BS_MESSAGE_SUCCESS://ѡ���һ���̼ң����뵽����ҳ��
				//��ʾ��Ӧ���Ƽ��̼���Ϣ
				//
				@SuppressWarnings("unchecked")
				List<BusinessEntity> list=(List<BusinessEntity>) msg.obj;
				for(int i=0;i<list.size();i++){
					double lat_marke=Double.parseDouble(list.get(i).getLocation_lat());
					double lng_marke=Double.parseDouble(list.get(i).getLocation_lng());
					LatLng latlng_marke=new LatLng(lat_marke, lng_marke);
					addMarkersToMap(latlng_marke,list.get(i));
				}
				break;
			}
		};
	};

}
