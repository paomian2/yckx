package com.brother.yckx.control.activity.owner;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.brother.BaseActivity;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.map.util.AMapUtil;

public class LBSLatLngLocationActivity extends BaseActivity implements LocationSource,AMapLocationListener,OnMapClickListener,OnGeocodeSearchListener{

	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;


	private Button button;
	private String latLng;
	private RelativeLayout layout_Adress;
	private TextView tv_adressName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lbs_location_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// �˷���������д
		init();
	}


	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		
		layout_Adress=(RelativeLayout) findViewById(R.id.layout_Adress);
		layout_Adress.setVisibility(View.GONE);
		
		tv_adressName=(TextView) findViewById(R.id.tv_adressName);
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
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		// aMap.setMyLocationType()
		aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		aMap.setOnMapClickListener(this);// ��amap��ӵ�����ͼ�¼�������
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
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
				/*mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
				double lat=amapLocation.getLatitude();
				double lng=amapLocation.getLongitude();
				latLonPoint=new LatLonPoint(lat, lng);
				getAddress(latLonPoint);*/
			} else {
				String errText = "��λʧ��," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
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
	public void onMapClick(LatLng arg0) {
		double lat=arg0.latitude;
		double lng=arg0.longitude;
		latLonPoint=new LatLonPoint(lat, lng);
		//ToastUtil.show(getApplicationContext(), ""+lat+" "+lng, 2000);
		getAddress(latLonPoint);
	}



	private GeocodeSearch geocoderSearch;
	private String addressName;
	/**
	 * ��������ѯ�ص�
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}


	/**
	 * ��Ӧ��������
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// ��һ��������ʾһ��Latlng���ڶ�������ʾ��Χ�����ף�������������ʾ�ǻ�ϵ����ϵ����GPSԭ������ϵ
		geocoderSearch.getFromLocationAsyn(query);// ����ͬ��������������
	}


	private LatLonPoint latLonPoint = new LatLonPoint(22.81906, 108.397434);
	/**
	 * ��������ص�
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		Log.d("yckx", ""+result.toString()+rCode);
		
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "";
				/*aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 15));*/
				tv_adressName.setText(addressName);
				layout_Adress.setVisibility(View.VISIBLE);
			} else {
			}
		} else if (rCode == 27) {
			layout_Adress.setVisibility(View.GONE);
			ToastUtil.show(getApplicationContext(), "�������", 2000);
		} else if (rCode == 32) {
			layout_Adress.setVisibility(View.GONE);
			ToastUtil.show(getApplicationContext(), "key����", 2000);
		} else {
			layout_Adress.setVisibility(View.GONE);
			ToastUtil.show(getApplicationContext(), "�������ʹ���"+rCode, 2000);
		}
	}


	private View.OnClickListener listener=new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent=new Intent(LBSLatLngLocationActivity.this,RegisterCompanyActivity.class);
			intent.putExtra("lbsLatLng", latLng);
			LBSLatLngLocationActivity.this.setResult(3,intent);
			LBSLatLngLocationActivity.this.finish();
			//��λʹ��hasmap�����ȡ�ֵ�����
		}
	};





}
