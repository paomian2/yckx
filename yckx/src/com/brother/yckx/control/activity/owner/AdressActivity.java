package com.brother.yckx.control.activity.owner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.BaseAdapter2;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.model.AdressEntity;

/**
 * 常用位置
 * */
public class AdressActivity extends BaseActivity{
	
	private ListView listview;
	private AdressAdapter adapter;
	private List<AdressEntity> adressList=new ArrayList<AdressEntity>();
	/**获取数据成功*/
	private final int QUERYADRESS_SUCCESS=0;
	/**设置为默认成功*/
	private final int SETTINGDEFALU_SUCCESS=10;
	/**删除成功*/
	private final int DELETEADRESS_SUCCESS=40;
	/**添加成功重新刷新页面*/
	private final int RESULTCODE_SUCCESS_UPDATE=3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_adress);
		setActionBar(R.string.adress_title, R.drawable.btn_homeasup_default, R.drawable.icon_default_add, listener);
		listview=(ListView) findViewById(R.id.listview);
		adapter=new AdressAdapter(this);
		listview.setAdapter(adapter);
		queryAdressList();
	}
	
	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.iv_action_right://跳转到地址的页面
				Intent intent=new Intent(AdressActivity.this,AddAdressActivity.class);
				startActivityForResult(intent, RESULTCODE_SUCCESS_UPDATE);
				break;
			case R.id.iv_action_left:
				finish();
				break;
			}
		}
	};
	
	
	/**获取地址列表数据*/
	private void queryAdressList(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(AdressActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpGet(WEBInterface.ADRESSLIST_URL, token,null);
				adressList.clear();
				adressList=ParseJSONUtils.paresAdressListData(respose);
				if(adressList!=null){
					//获取数据成功
					Message msg=new Message();
					msg.what=QUERYADRESS_SUCCESS;
					mHandler.sendMessage(msg);
				}
				Log.d("yckx", respose);
			}
		}).start();
	}
	
	 private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case QUERYADRESS_SUCCESS:
				//更新ui
				adapter.clearAdapter();
				adapter.addDataToAdapter(adressList);
				adapter.notifyDataSetChanged();
				break;
			case SETTINGDEFALU_SUCCESS://设置默认成功
				queryAdressList();
				break;
			case DELETEADRESS_SUCCESS:
				//删除成功
				queryAdressList();
				break;
			}
		};
	};
	
	
	class AdressAdapter extends BaseAdapter2<AdressEntity>{
		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}
		public AdressAdapter(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			if(view==null){
				//view=layoutInflater.inflate(R.layout.row_activity_adress,null);
				view=layoutInflater.inflate(R.layout.row_activity_adress2,null);
			}
			TextView phone_tv=(TextView) view.findViewById(R.id.phone);
			TextView provice_tv=(TextView) view.findViewById(R.id.provice);
			TextView location_tv=(TextView) view.findViewById(R.id.location);
			
			//新原型
			RelativeLayout layout_adressDefal=(RelativeLayout) view.findViewById(R.id.layout_adressDefal);
			ImageView img_defalut=(ImageView) view.findViewById(R.id.img_defalut);
			RelativeLayout layout_adressEdit=(RelativeLayout) view.findViewById(R.id.layout_adressEdit);
			RelativeLayout layout_adressDelete=(RelativeLayout) view.findViewById(R.id.layout_adressDelete);
			
			final ImageView select_defalut_img=(ImageView) view.findViewById(R.id.select_defalut);
			final ImageView editImg=(ImageView) view.findViewById(R.id.editImg);
			final ImageView delete_img=(ImageView) view.findViewById(R.id.delete);
			
			AdressEntity entity=getItem(position);
			phone_tv.setText(entity.getPhone());
			provice_tv.setText(entity.getProvince());
			location_tv.setText(entity.getLocation());
			if(entity.isDefalut()){
				select_defalut_img.setImageResource(R.drawable.icon_tick_selected);
				//新原型
				img_defalut.setImageResource(R.drawable.icon_defalut_adress_select);
				//end
				PrefrenceConfig.setAdressDefalut(AdressActivity.this, entity);
			}else{
				select_defalut_img.setImageResource(R.drawable.icon_tick_normal);
				//新原型
				img_defalut.setImageResource(R.drawable.icon_defalut_adress_defal);
			}
			//设置默认值
			
			//start 新原型
			layout_adressDefal.setTag(position);
			layout_adressDefal.setOnClickListener(new OnClickListener(){
				@SuppressLint("NewApi")
				@Override
				public void onClick(View arg0) {
					/*AlertDialog.Builder dialog=new AlertDialog.Builder(context,R.style.dialog);
					dialog.setTitle("设置"+(adressList.get((Integer)select_defalut_img.getTag()).getLocation())+"为默认");*/
					final Dialog dialog;
					LayoutInflater inflater=AdressActivity.this.getLayoutInflater();
					View view = inflater.inflate(R.layout.row_dialog_delete_car, null);
					dialog = new AlertDialog.Builder(AdressActivity.this).create();
					dialog.show();
					dialog.setContentView(view);
					/*mPreShowPhotoDialog.getWindow().setLayout(getWith()-50, 500);
					Window dialogWindow = mPreShowPhotoDialog.getWindow();  */
					Window dialogWindow = dialog.getWindow(); 
					WindowManager.LayoutParams params = dialogWindow.getAttributes();  
					dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
					WindowManager m =getWindowManager();
					Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
					params.width = (int) ((d.getWidth()) * 0.9);
					params.height = params.height;
					//偏移量
					//params.y=30;
					dialog.getWindow().setAttributes(params);
					TextView tv_flag=(TextView) view.findViewById(R.id.tv_flag);
					//tv_flag.setVisibility(View.GONE);
					TextView deleteCarMsg=(TextView) view.findViewById(R.id.deleteCarMsg);
					AdressEntity tempEntity=adressList.get((Integer)select_defalut_img.getTag());
					final String id=tempEntity.getId();
					final String phone=tempEntity.getPhone();
					final String provice=tempEntity.getProvince();
					final String carAdress=tempEntity.getLocation();
					tv_flag.setText("设置默认:");
					deleteCarMsg.setText(""+carAdress);
					view.findViewById(R.id.deleteConfirm).setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View arg0) {
							final HashMap<String, String> hasmp=new HashMap<String, String>();
							hasmp.put("id", id);
							hasmp.put("phone", phone);
							hasmp.put("prefix", provice);
							hasmp.put("address", carAdress);
							hasmp.put("isDefault","true");
							uploadData(hasmp);
							dialog.dismiss();
						}});
					view.findViewById(R.id.deleteCancel).setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View arg0) {
							dialog.dismiss();
						}});
				}});
			layout_adressEdit.setTag(position);
			layout_adressEdit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					AdressEntity tempEntity=adressList.get((Integer)editImg.getTag());
					String id=tempEntity.getId();
					GlobalServiceUtils.getInstance().setGloubalServiveAdress(tempEntity);
					Intent intent=new Intent(AdressActivity.this,AddAdressActivity.class);
					intent.putExtra("AddAdressActivity", id);
					startActivityForResult(intent, RESULTCODE_SUCCESS_UPDATE);
				}});
			layout_adressDelete.setTag(position);
			layout_adressDelete.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					final Dialog dialog;
					LayoutInflater inflater=AdressActivity.this.getLayoutInflater();
					View view = inflater.inflate(R.layout.row_dialog_delete_car, null);
					dialog = new AlertDialog.Builder(AdressActivity.this).create();
					dialog.show();
					dialog.setContentView(view);
					/*mPreShowPhotoDialog.getWindow().setLayout(getWith()-50, 500);
					Window dialogWindow = mPreShowPhotoDialog.getWindow();  */
					Window dialogWindow = dialog.getWindow(); 
					WindowManager.LayoutParams params = dialogWindow.getAttributes();  
					dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
					WindowManager m =getWindowManager();
					Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
					params.width = (int) ((d.getWidth()) * 0.9);
					params.height = params.height;
					//偏移量
					//params.y=30;
					dialog.getWindow().setAttributes(params);
					TextView tv_flag=(TextView) view.findViewById(R.id.tv_flag);
					//tv_flag.setVisibility(View.GONE);
					TextView deleteCarMsg=(TextView) view.findViewById(R.id.deleteCarMsg);
					AdressEntity tempEntity=adressList.get((Integer)select_defalut_img.getTag());
					final String id=tempEntity.getId();
					final String phone=tempEntity.getPhone();
					final String provice=tempEntity.getProvince();
					final String carAdress=tempEntity.getLocation();
					tv_flag.setText("删除:");
					deleteCarMsg.setText(""+carAdress);
					view.findViewById(R.id.deleteConfirm).setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View arg0) {
							final HashMap<String, String> hasmp=new HashMap<String, String>();
							AdressEntity tempEntity=adressList.get((Integer)delete_img.getTag());
							String id=tempEntity.getId();
							deleteAdress(id);
							dialog.dismiss();
						}});
					view.findViewById(R.id.deleteCancel).setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View arg0) {
							dialog.dismiss();
						}});
				}});
			
			select_defalut_img.setTag(position);
			select_defalut_img.setOnClickListener(new OnClickListener(){
				@SuppressLint("NewApi") @Override
				public void onClick(View arg0) {//设置为默认
					AlertDialog.Builder dialog=new AlertDialog.Builder(context,R.style.dialog);
					dialog.setTitle("设置"+(adressList.get((Integer)select_defalut_img.getTag()).getLocation())+"为默认");
					dialog.setPositiveButton("是", new android.content.DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							AdressEntity tempEntity=adressList.get((Integer)select_defalut_img.getTag());
							String id=tempEntity.getId();
							String phone=tempEntity.getPhone();
							String provice=tempEntity.getProvince();
							String carAdress=tempEntity.getLocation();
							final HashMap<String, String> hasmp=new HashMap<String, String>();
							hasmp.put("id", id);
							hasmp.put("phone", phone);
							hasmp.put("prefix", provice);
							hasmp.put("address", carAdress);
							hasmp.put("isDefault","true");
							uploadData(hasmp);
						}});
					dialog.setNegativeButton("否", null);
					dialog.show();
				}});
			// end 新原型
			
			//编辑地址
			editImg.setTag(position);
			editImg.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					AdressEntity tempEntity=adressList.get((Integer)editImg.getTag());
					String id=tempEntity.getId();
					GlobalServiceUtils.getInstance().setGloubalServiveAdress(tempEntity);
					Intent intent=new Intent(AdressActivity.this,AddAdressActivity.class);
					intent.putExtra("AddAdressActivity", id);
					startActivityForResult(intent, RESULTCODE_SUCCESS_UPDATE);
				}});
			delete_img.setTag(position);
			delete_img.setOnClickListener(new OnClickListener(){
				@SuppressLint("NewApi") @Override
				public void onClick(View arg0) {
					AlertDialog.Builder dialog=new AlertDialog.Builder(context,R.style.dialog);
					dialog.setTitle("删除"+(adressList.get((Integer)select_defalut_img.getTag()).getLocation())+"");
					dialog.setPositiveButton("是", new android.content.DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							AdressEntity tempEntity=adressList.get((Integer)delete_img.getTag());
							String id=tempEntity.getId();
							deleteAdress(id);
						}});
					dialog.setNegativeButton("否", null);
					dialog.show();
				}});
			return view;
		}
	}
	
	
	private void uploadData(final HashMap<String, String> hasmp){
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				String token=PrefrenceConfig.getUserMessage(AdressActivity.this).getUserToken();
				String respose="";
				try {
					 respose=ApacheHttpUtil.httpPost2(WEBInterface.ADDADRESS_URL, token, new StringEntity(new JSONObject(hasmp).toString(),"UTF-8"));
				     Log.d("yckx", respose);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						//修改成功
						String id=hasmp.get("id");
						String phone=hasmp.get("phone");
						String provice=hasmp.get("prefix");
						String carAdress=hasmp.get("address");
						AdressEntity defalutEntity=new AdressEntity(id,phone, provice, carAdress, true);
						//写入文件
						PrefrenceConfig.setAdressDefalut(AdressActivity.this, defalutEntity);
						Message msg=new Message();
						msg.what=SETTINGDEFALU_SUCCESS;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	/**删除地址*/
	private void deleteAdress(final String id){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url=WEBInterface.DELETEADRESS_URL+id;
				String token=PrefrenceConfig.getUserMessage(AdressActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpPost(url, token, null);
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						//删除成功
						Message msg=new Message();
						msg.what=DELETEADRESS_SUCCESS;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULTCODE_SUCCESS_UPDATE){
			String result =data.getStringExtra("AdressActivity");//得到新Activity关闭后返回的数据  
			if(result.equals("addsuccess")){
				//刷新界面
				queryAdressList();
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent intent=new Intent(this,OrderDetaileActivity.class);
		    intent.putExtra("adress", "常用地址是...");//常用地址
		    this.setResult(3,intent);
		    this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
