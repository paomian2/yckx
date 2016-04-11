package com.brother.yckx.control.activity.map;

import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.androide.lib3.view.slidingmenu.SlidingMenu.OnClosedListener;
import com.brother.BaseActivity;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;

public class POISearchActivity extends BaseActivity  implements OnMarkerClickListener, InfoWindowAdapter, OnClosedListener,
OnPoiSearchListener, OnMapClickListener, OnInfoWindowClickListener,
OnClickListener,OnItemSelectedListener{

	private AMap aMap;
	private ProgressDialog progDialog = null;// ����ʱ������
	private Spinner selectDeep;// ѡ������б�
	private String[] itemDeep = { "�Ƶ�", "����", "����", "ӰԺ" };
	private String deepType = "";// poi��������
	private PoiResult poiResult; // poi���صĽ��
	private int currentPage = 0;// ��ǰҳ�棬��0��ʼ����
	private PoiSearch.Query query;// Poi��ѯ������
	//private LatLonPoint lp = new LatLonPoint(39.908127, 116.375257);// Ĭ�������㳡
	//110.288968,25.267869����վ
	private LatLonPoint lp = new LatLonPoint(25.267869, 110.288968);// Ĭ�Ϲ���վ
	private Marker locationMarker; // ѡ��ĵ�
	private PoiSearch poiSearch;
	private PoiOverlay poiOverlay;// poiͼ��
	private List<PoiItem> poiItems;// poi����

	private Button nextButton;// ��һҳ

	//�ߵµ�ͼ
	private MapView mapView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poisearch);
		mapView=(MapView) findViewById(R.id.mapView);
		init();
	}

	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		if (aMap == null) {
			aMap=mapView.getMap();
			setUpMap();
			Button locationButton = (Button) findViewById(R.id.locationButton);
			locationButton.setOnClickListener(this);
			Button searchButton = (Button) findViewById(R.id.searchButton);
			searchButton.setOnClickListener(this);
			nextButton = (Button) findViewById(R.id.nextButton);
			nextButton.setOnClickListener(this);
			nextButton.setClickable(false);// Ĭ����һҳ��ť���ɵ�
			locationMarker = aMap.addMarker(new MarkerOptions()
			.anchor(0.5f, 1)
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.point))
					.position(new LatLng(lp.getLatitude(), lp.getLongitude()))
					.title("����վΪ���ĵ㣬�����ܱ�"));
			locationMarker.showInfoWindow();

		}
	}

	/**
	 * ���ó���ѡ��
	 */
	private void setUpMap() {
		selectDeep = (Spinner) findViewById(R.id.spinnerdeep);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemDeep);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectDeep.setAdapter(adapter);
		//selectDeep.setOnItemSelectedListener(this);// ���spinnerѡ�������¼�
		selectDeep.setOnItemSelectedListener(this);
		aMap.setOnMarkerClickListener(this);// ��ӵ��marker�����¼�
		aMap.setInfoWindowAdapter(this);// �����ʾinfowindow�����¼�

	}

	/**
	 * ע�����
	 */
	private void registerListener() {
		aMap.setOnMapClickListener(this);
		aMap.setOnMarkerClickListener(this);
		aMap.setOnInfoWindowClickListener(this);
		aMap.setInfoWindowAdapter(this);
	}

	/**
	 * ��ʾ���ȿ�
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("����������");
		progDialog.show();
	}

	/**
	 * ���ؽ��ȿ�
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * ��ʼ����poi����
	 */
	protected void doSearchQuery() {
		showProgressDialog();// ��ʾ���ȿ�
		aMap.setOnMapClickListener(null);// ����poi����ʱ�������ͼ����¼�
		currentPage = 0;
		query = new PoiSearch.Query("", deepType, "����");// ��һ��������ʾ�����ַ������ڶ���������ʾpoi�������ͣ�������������ʾpoi�������򣨿��ַ�������ȫ����
		query.setPageSize(10);// ����ÿҳ��෵�ض�����poiitem
		query.setPageNum(currentPage);// ���ò��һҳ

		if (lp != null) {
			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.setBound(new SearchBound(lp, 2000, true));//
			// ������������Ϊ��lp��ΪԲ�ģ�����Χ2000�׷�Χ
			/*
			 * List<LatLonPoint> list = new ArrayList<LatLonPoint>();
			 * list.add(lp);
			 * list.add(AMapUtil.convertToLatLonPoint(Constants.BEIJING));
			 * poiSearch.setBound(new SearchBound(list));// ���ö����poi������Χ
			 */
			poiSearch.searchPOIAsyn();// �첽����
		}
	}

	/**
	 * �����һҳpoi����
	 */
	public void nextSearch() {
		if (query != null && poiSearch != null && poiResult != null) {
			if (poiResult.getPageCount() - 1 > currentPage) {
				currentPage++;

				query.setPageNum(currentPage);// ���ò��һҳ
				poiSearch.searchPOIAsyn();
			} else {
				ToastUtil
				.show(this, "�޽��",2000);
			}
		}
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	/**
	 * poiû�����������ݣ�����һЩ�Ƽ����е���Ϣ
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "�Ƽ�����\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "��������:" + cities.get(i).getCityName() + "��������:"
					+ cities.get(i).getCityCode() + "���б���:"
					+ cities.get(i).getAdCode() + "\n";
		}
		ToastUtil
		.show(this, "infomation",2000);

	}


	/**
	 * POI�����ص�����
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dissmissProgressDialog();// ���ضԻ���
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// ����poi�Ľ��
				if (result.getQuery().equals(query)) {// �Ƿ���ͬһ��
					poiResult = result;
					poiItems = poiResult.getPois();// ȡ�õ�һҳ��poiitem���ݣ�ҳ��������0��ʼ
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// ����������poiitem����ʱ���᷵�غ��������ؼ��ֵĳ�����Ϣ
					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// ����֮ǰ��ͼ��
						poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();

						nextButton.setClickable(true);// ������һҳ�ɵ�
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						ToastUtil
						.show(this, "�޽��",2000);
					}
				}
			} else {
				ToastUtil
				.show(this, "�޽��",2000);
			}
		} else if (rCode == 27) {
			ToastUtil
			.show(this, "�������",2000);
		} else if (rCode == 32) {
			ToastUtil
			.show(this, "key error",2000);
		} else {
			ToastUtil
			.show(this, "����"+rCode,2000);
		}
	}

	@Override
	public void onPoiItemSearched(PoiItem item, int rCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng latng) {
		locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.point))
				.position(latng).title("���ѡ��Ϊ���ĵ�"));
		locationMarker.showInfoWindow();
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		locationMarker.hideInfoWindow();
		lp = new LatLonPoint(locationMarker.getPosition().latitude,
				locationMarker.getPosition().longitude);
		locationMarker.destroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * �����ǰ�ť
		 */
		case R.id.locationButton:
			aMap.clear();// ��������marker
			registerListener();
			break;
			/**
			 * ���������ť
			 */
		case R.id.searchButton:
			doSearchQuery();
			break;
			/**
			 * �����һҳ��ť
			 */
		case R.id.nextButton:
			nextSearch();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClosed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int position,
			long arg3) {
		if (parent == selectDeep) {
			deepType = itemDeep[position];
		}
		nextButton.setClickable(false);// �ı���������������������

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		if (parent == selectDeep) {
			deepType = "�Ƶ�";
		}
		nextButton.setClickable(false);// �ı���������������������

	}

}
