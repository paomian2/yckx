package com.brother.yckx.control.activity.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.yckx.R;
import com.brother.yckx.model.BusinessEntity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class WalkMapBaseActivity extends BaseActivity implements AMapNaviListener, AMapNaviViewListener{
	BusinessEntity businessEntity;
	AMapNaviView mAMapNaviView;
    AMapNavi mAMapNavi;
    TTSController mTtsManager;
    //NaviLatLng mEndLatlng = new NaviLatLng(39.925846, 116.432765);//108.397832,22.816726
    //NaviLatLng mStartLatlng = new NaviLatLng(39.925041, 116.437901);//108.398921,22.816526
    NaviLatLng mEndLatlng;//  new NaviLatLng(22.816726, 108.397832);//108.397832,22.816726
    NaviLatLng mStartLatlng;// = new NaviLatLng(22.816526, 108.398921);//108.398921,22.816526
    List<NaviLatLng> mStartList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> mEndList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> mWayPointList;
    private String nativeSyle=Utils.STYLE_WALKE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        businessEntity=(BusinessEntity) getIntent().getSerializableExtra("WalkMapBaseActivity");
       
        double startLat=(Double) GlobalServiceUtils.getGloubalServiceSession("starNativeLat");
        double startLng=(Double) GlobalServiceUtils.getGloubalServiceSession("starNativeLng");
        mStartLatlng=new NaviLatLng(startLat, startLng);
        
        String mlat=businessEntity.getLocation_lat();
        String mlng=businessEntity.getLocation_lng();
        double lat=Double.parseDouble(mlat);
        double lng=Double.parseDouble(mlng);
        mEndLatlng=new NaviLatLng(lat, lng);

        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();
        mTtsManager.startSpeaking();

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.addAMapNaviListener(mTtsManager);
        mAMapNavi.setEmulatorNaviSpeed(150);//模拟导航车速
        
        setContentView(R.layout.activity_drive_follow_map);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.map);
		mAMapNaviView.onCreate(savedInstanceState);
		mAMapNaviView.setAMapNaviViewListener(this);
    }

    
    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
        mStartList.add(mStartLatlng);
        mEndList.add(mEndLatlng);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();

//        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
        mTtsManager.stopSpeaking();
//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0
        //不再在naviview destroy的时候自动执行AMapNavi.stopNavi();
        //请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        mTtsManager.destroy();
    }
    
    
    
    @Override
    public void onInitNaviFailure() {
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
    }

    /**设置"步行"还是"开车"*/
    @Override
    public void onInitNaviSuccess() {
    	if(nativeSyle.equals(Utils.STYLE_WALKE)){
    		mAMapNavi.calculateWalkRoute(mStartList.get(0), mEndList.get(0));
    	}else {
    		mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList, PathPlanningStrategy.DRIVING_DEFAULT);
    	}
    }

    
    @Override
    public void onStartNavi(int type) {
        //设置导航时真实导航还是模拟导航
    	//mAMapNavi.startNavi(mAMapNavi.GPSNaviMode);//真实导航
    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {

    }

    @Override
    public void onGetNavigationText(int type, String text) {

    }

    @Override
    public void onEndEmulatorNavi() {
    }

    @Override
    public void onArriveDestination() {
    }

    /**模拟还是真实*/
    @Override
    public void onCalculateRouteSuccess() {
      //  mAMapNavi.startNavi(AMapNavi.GPSNaviMode);
    	Timer timer=new Timer();
    	timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);
			}}, 1000);
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int wayID) {

    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
    }

    @Override
    public void onNaviSetting() {
    }

    @Override
    public void onNaviMapMode(int isLock) {

    }

    @Override
    public void onNaviCancel() {
        finish();
    }


    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }


    @Override
    public void onScanViewButtonClick() {
    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
    }

    @Override
    public void hideCross() {
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }


    @Override
    public void onLockMap(boolean isLock) {
    }

    @Override
    public void onNaviViewLoaded() {
        Log.d("yckx", "导航页面加载成功");
        Log.d("yckx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }
	    
	
	

}
