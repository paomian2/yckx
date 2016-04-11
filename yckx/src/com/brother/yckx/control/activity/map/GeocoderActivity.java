package com.brother.yckx.control.activity.map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMap.OnMarkerDragListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.map.util.AMapUtil;
import com.brother.yckx.control.activity.map.util.ToastUtil;

public class GeocoderActivity extends BaseActivity  implements OnGeocodeSearchListener, OnClickListener,OnMarkerClickListener,
OnInfoWindowClickListener, OnMarkerDragListener, OnMapLoadedListener,InfoWindowAdapter{
	private MarkerOptions markerOption;
	private ProgressDialog progDialog = null;
	private GeocodeSearch geocoderSearch;
	private String addressName;
	private AMap aMap;
	private MapView mapView;
	private Marker geoMarker;
	private Marker regeoMarker;
	private LatLonPoint latLonPoint = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geocoder);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// �˷���������д
		init();
	}
	
	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		double lat=(Double) GlobalServiceUtils.getGloubalServiceSession("searchLat");
		double lng=(Double) GlobalServiceUtils.getGloubalServiceSession("searchLng");
		latLonPoint=new LatLonPoint(lat,lng);
		if (aMap == null) {
			aMap = mapView.getMap();
			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gaode_point)));
		}
		setupMarker();
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);
		getAddress(latLonPoint);
	}
	
	private void setupMarker(){
		aMap.setOnMarkerDragListener(this);// ����marker����ק�¼�������
		aMap.setOnMapLoadedListener(this);// ����amap���سɹ��¼�������
		aMap.setOnMarkerClickListener(this);// ���õ��marker�¼�������
		aMap.setOnInfoWindowClickListener(this);// ���õ��infoWindow�¼�������
		aMap.setInfoWindowAdapter(this);// �����Զ���InfoWindow��ʽ
		addMarkersToMap();// ����ͼ�����marker
	}

	private void addMarkersToMap() {
		LatLng searchLatLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());// �����о�γ��
		Marker searchmarker = aMap.addMarker(new MarkerOptions()
		.position(searchLatLng)
		.title(""+GlobalServiceUtils.getGloubalServiceSession("searchAdress")).snippet("���Ĳ�ѯ�ص�")
		.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		.draggable(true));
		searchmarker.showInfoWindow();// ����Ĭ����ʾһ��infowinfow
		
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
	 * ��ʾ�������Ի���
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("���ڻ�ȡ��ַ");
		progDialog.show();
	}

	/**
	 * ���ؽ������Ի���
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
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
	 * ��������ѯ�ص�
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
				geoMarker.setPosition(AMapUtil.convertToLatLng(address
						.getLatLonPoint()));
				addressName = "��γ��ֵ:" + address.getLatLonPoint() + "\nλ������:"
						+ address.getFormatAddress();
				//ToastUtil.show(GeocoderActivity.this, addressName);
			} else {
				ToastUtil.show(GeocoderActivity.this, "�޽��");
			}

		} else if (rCode == 27) {
			ToastUtil.show(GeocoderActivity.this, "�������");
		} else if (rCode == 32) {
			ToastUtil.show(GeocoderActivity.this, "key error");
		} else {
			ToastUtil.show(GeocoderActivity.this,
					"��������"+ rCode);
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
				addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "����";
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 15));
				regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
				//ToastUtil.show(GeocoderActivity.this, addressName);
			} else {
				ToastUtil.show(GeocoderActivity.this, "�޽��");
			}
		} else if (rCode == 27) {
			ToastUtil.show(GeocoderActivity.this, "�������");
		} else if (rCode == 32) {
			ToastUtil.show(GeocoderActivity.this, "key error");
		} else {
			ToastUtil.show(GeocoderActivity.this,
					"����" + rCode);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}

	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		
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
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
