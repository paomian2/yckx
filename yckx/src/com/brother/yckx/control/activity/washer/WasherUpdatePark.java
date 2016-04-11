package com.brother.yckx.control.activity.washer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.BaseAdapter2;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.LoginActivity;
import com.brother.yckx.model.CarParkEntity;
import com.brother.yckx.view.image.CacheImageView;

public class WasherUpdatePark extends BaseActivity{
	private ListView listview;
	private List<CarParkEntity> list;
	private CarParkAdapter adapter;
	
	/**获取停车位信息成功*/
	private final int LOAD_PARK_SUCCESS=0;
	/**设置商家空余车位成功*/
	private final int SETTING_PARK_COUNT_SUCCESS=10;
	/**token错误*/
	private final int TOKEN_ERROR=2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_park);
		setActionBar(R.string.updatePark, R.drawable.btn_homeasup_default, NULL_ID, actionListener);
		listview=(ListView) findViewById(R.id.listview);
	    list=new ArrayList<CarParkEntity>();
		
		adapter=new CarParkAdapter(WasherUpdatePark.this);
		listview.setAdapter(adapter);
		getUpdateParkMessage();
	}
	
	
	private void getUpdateParkMessage(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(WasherUpdatePark.this).getUserToken();
				String response=ApacheHttpUtil.httpGet(WEBInterface.UPDATEPARK, token, null);
				Log.d("yckx", response+"");
				 List<CarParkEntity> mlist=null;
				if(response!=null){
					try {
						JSONObject jsonObject=new JSONObject(response);
						String code=jsonObject.getString("code");
						if(code.equals("2")){
							 Message msg=new Message();
							  msg.what=TOKEN_ERROR;
							  mHandler.sendMessage(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					mlist=ParseJSONUtils.parseCarPark(response);
				}
				 WasherUpdatePark.this.list=mlist;
				if(mlist!=null){
					//成功
				  Message msg=new Message();
				  msg.what=LOAD_PARK_SUCCESS;
				  mHandler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	/**是否还是登录状态*/
	private boolean isLoginState(String json){
		if(json==null){
			return false;
		}
		try {
			JSONObject jSONObject=new JSONObject(json);
			String code=jSONObject.getString("code");
			if(code.equals("0")){
				return true;
			}
			if(code.equals("2")){
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_PARK_SUCCESS:
				//加载成功
				adapter.addDataToAdapter(list);
				adapter.notifyDataSetChanged();
				break;
			case SETTING_PARK_COUNT_SUCCESS:
				ToastUtil.show(getApplicationContext(), "设置成功",2000);
				break;
			case TOKEN_ERROR:
				Intent intent=new Intent(WasherUpdatePark.this,LoginActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		};
	};
	private View.OnClickListener actionListener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.iv_action_left:
				finish();
				break;

			default:
				break;
			}
			
		}
	};
	 
	
	
	class CarParkAdapter extends BaseAdapter2<CarParkEntity>{

		public CarParkAdapter(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1==null){
				arg1=layoutInflater.inflate(R.layout.row_activity_update_park,null);
			}
			CacheImageView shopImg=(CacheImageView) arg1.findViewById(R.id.shopImg);
			TextView storeName=(TextView) arg1.findViewById(R.id.storeName);
			TextView free=(TextView) arg1.findViewById(R.id.free);
			TextView total=(TextView) arg1.findViewById(R.id.total);
			final CarParkEntity carParkEntity=getItem(arg0);
			shopImg.setImageUrl(WEBInterface.INDEXIMGURL+carParkEntity.getCompanyImageUrl());
			storeName.setText(carParkEntity.getCompanyName());
			free.setText(carParkEntity.getCompanySpaceCount());
			total.setText(carParkEntity.getCompanyTotalCount());
			final ImageView updatePark=(ImageView) arg1.findViewById(R.id.updateParkImg);
			updatePark.setTag(arg0);
			updatePark.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					CarParkEntity entity=list.get((Integer)updatePark.getTag());
					
					final Dialog upPrakDialog=new AlertDialog.Builder(WasherUpdatePark.this).create();
					LayoutInflater inflater=WasherUpdatePark.this.getLayoutInflater();
					View view = inflater.inflate(R.layout.dialog_update_park, null);
					upPrakDialog.show();
					upPrakDialog.setContentView(view);
					Window dialogWindow = upPrakDialog.getWindow(); 
					WindowManager.LayoutParams params = dialogWindow.getAttributes();  
					dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
					WindowManager m =getWindowManager();
					Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
					params.width = (int) ((d.getWidth()) * 0.7);
					params.height = params.height;
					//偏移量
					upPrakDialog.getWindow().setAttributes(params);
					
					TextView tv_carParkTotal=(TextView) view.findViewById(R.id.carParkTotal);
					tv_carParkTotal.setText(carParkEntity.getCompanyTotalCount());
					final EditText tv_spacePark=(EditText) view.findViewById(R.id.spacePark);
					ImageView tuige=(ImageView) view.findViewById(R.id.tuige);
					tuige.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View arg0) {
							tv_spacePark.setText("");
						}});
					Button ok=(Button) view.findViewById(R.id.ok);
					ok.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View arg0) {
							String companyId=carParkEntity.getCompanyId();
							String spacePark=tv_spacePark.getText().toString().trim();
							settingParkCount(companyId,spacePark);
						}});
					Button cacel=(Button) view.findViewById(R.id.cacel);
					cacel.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View arg0) {
							upPrakDialog.dismiss();
							
						}});
				}});
			return arg1;
		}
		
	}
	
	
	private void settingParkCount(final String companyId,final String spaceCount){
		new Thread(new Runnable() {
			@Override
			public void run() {///company/space/{companyId}/{num}
				String url=WEBInterface.SETTINGPARKCOUNT+companyId+"/"+spaceCount;
				String token=PrefrenceConfig.getUserMessage(WasherUpdatePark.this).getUserToken();
				String respose=ApacheHttpUtil.httpGet(url, token, null);
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						Message msg=new Message();
						msg.what=SETTING_PARK_COUNT_SUCCESS;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	

}
