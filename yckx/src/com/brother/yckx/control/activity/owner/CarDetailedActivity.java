package com.brother.yckx.control.activity.owner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.net.JavaHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.model.CardEntity;
import com.brother.yckx.view.WheelView;
import com.brother.yckx.view.WheelView.OnWheelPickerListener;
import com.brother.yckx.view.image.CacheImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

@SuppressLint("SdCardPath")
public class CarDetailedActivity extends BaseActivity{
	private LinearLayout main_layout;
	private TextView btnSave;
	private ImageView addImge;
	private TextView carMessage;//车辆信息
	private EditText carNo;//车牌号
	private TextView tv_province;//车辆默认地(桂)
	private ImageView icon_triangle;//根据这个来这种省份
	private TextView carColor;//车辆颜色
	private Button btnDelete;
	private long rgb=0;//与rgbImg绑定的值，控制rgbImg获取到相应的rgb值(默认是黑色)
	//private TextView 
	private String comFrom="";
	/**上传的车辆图片的地址*/
	//String fileName="";
	/**保存车辆成功*/
	private final int ADDCARSUCESSED=0;
	/**token值失效，要重新登录*/
	private final int TOKENERROR=2;
	/**判断是否要上传车辆图片*/
	private boolean isLoadCarImg;
	/**上传车辆图片成功*/
	private int UPLOADCARIMGSUCCESS=11;
	/**上传车辆成功后获取到图片的网络存储路径，绑定上传到车库数据*/
	private String mCarImage="";
	/**用来接收"我的车库"页面传过来的数据*/
	private CardEntity carEntity;
	/**车库页面进来的情况下的一个存储图片的缓存变量*/
	Bitmap modifyBitmap;
	/**车库页面进来的情况下的一个存储图片的缓存变量,获取图片成功*/
	private final int MODIFYGETIMGSUCCESS=6;
	/**是否是修改车辆信息*/
	private final int RESULT_CODE_CARNAME=1;
	private boolean modifyCar;
	@SuppressLint("SdCardPath") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addcard);
		setActionBar(R.string.AddCardTitle, R.drawable.btn_homeasup_default, R.drawable.icon_default_save, listener);
		//判断是从哪一端尽量
		init();
		comFrom=(String) GlobalServiceUtils.getGloubalServiceSession("add_modifay");
		if(comFrom.equals("modifyCar")){
			modifyCar=true;
			btnDelete.setVisibility(View.VISIBLE);
			carEntity =GlobalServiceUtils.getInstance().GlobalServiceCarMessage();
			if(carEntity!=null){
				//设置页面中的值
				setViewData();
			}
		}else{
			modifyCar=false;
			//设置车辆系列
			String mCarName=(String) GlobalServiceUtils.getGloubalServiceSession("carName");
			carMessage.setText(mCarName);
			btnDelete.setVisibility(View.GONE);
		}

		findViewById(R.id.layout_carMessge).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(CarDetailedActivity.this,SearCarMessageActivity.class);
				startActivityForResult(intent,RESULT_CODE_CARNAME);
			}});
	}

	private void init(){
		main_layout=(LinearLayout) findViewById(R.id.main_layout);
		btnSave=(TextView) findViewById(R.id.btnSave);
		
		addImge=(ImageView) findViewById(R.id.addImge);
		carMessage=(TextView) findViewById(R.id.carMessage);
		carNo=(EditText) findViewById(R.id.carNo);
		tv_province=(TextView) findViewById(R.id.province);
		icon_triangle=(ImageView) findViewById(R.id.icon_triangle);
		carColor=(TextView) findViewById(R.id.carColor);
		btnDelete=(Button) findViewById(R.id.delete);
		
		btnSave.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				saveCarMessage();
			}});
		
		//选择车辆省份
		icon_triangle.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				setProviceDialog();
			}});
		
		
		carMessage.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				//setEditTextKeyBoard(carMessage, main_layout);
				Intent intent=new Intent(CarDetailedActivity.this,SearCarMessageActivity.class);
				startActivity(intent);
				return false;
			}});
		carNo.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				setEditTextKeyBoard(carNo, main_layout);
				return false;
			}});
		//选择颜色
		findViewById(R.id.layout_carColor).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				showColorDialog();
			}});
        //打开相机或相册
		addImge.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//弹出相册或相机
				takePhonesDialog();
			}});
		
		//删除该车辆
		findViewById(R.id.delete).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				deleteDialog();
			}});
	}
	
	
	private Dialog deleteDialog;
	//----代替popu窗口的dialog窗口----
	private void deleteDialog(){
		LayoutInflater inflater=CarDetailedActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_delete_car, null);
		deleteDialog = new AlertDialog.Builder(CarDetailedActivity.this).create();
		deleteDialog.show();
		deleteDialog.setContentView(view);
		WindowManager m =getWindowManager();
		Window dialogWindow = deleteDialog.getWindow();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams params = deleteDialog.getWindow().getAttributes();
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		dialogWindow.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
		//设置bottom的偏移量
		params.y=30;
		deleteDialog.getWindow().setAttributes(params);

		TextView deleteCarMsg=(TextView) view.findViewById(R.id.deleteCarMsg);
		if(carEntity!=null){
			deleteCarMsg.setText(carEntity.getCarBrand()+" "+carEntity.getProvince()+carEntity.getCarNum()+" "+carEntity.getCarColor());
		}
		view.findViewById(R.id.deleteCancel).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				deleteDialog.dismiss();
			}});
		view.findViewById(R.id.deleteConfirm).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.d("yckx", "删除的车辆信息:"+carEntity.toString());
				deleteCar(carEntity.getId());
				deleteDialog.dismiss();
			}});
	}
	
	
	private Dialog takephotoDialog;
	//----代替popu窗口的dialog窗口----
	private void takePhonesDialog(){
		LayoutInflater inflater=CarDetailedActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		takephotoDialog = new AlertDialog.Builder(CarDetailedActivity.this).create();
		takephotoDialog.show();
		takephotoDialog.setContentView(view);
		WindowManager m =getWindowManager();
		Window dialogWindow = takephotoDialog.getWindow();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams params = takephotoDialog.getWindow().getAttributes();
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//设置bottom的偏移量
		params.y=30;
		takephotoDialog.getWindow().setAttributes(params);

		view.findViewById(R.id.openCamera).setOnClickListener(listener);
		view.findViewById(R.id.openGallery).setOnClickListener(listener);
		view.findViewById(R.id.openCancel).setOnClickListener(listener);
	}
	
	private Dialog setProviceDialog;
	//----代替popu窗口的dialog窗口----
	private void setProviceDialog(){
		LayoutInflater inflater=CarDetailedActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_provice, null);
		setProviceDialog = new AlertDialog.Builder(CarDetailedActivity.this).create();
		setProviceDialog.show();
		setProviceDialog.setContentView(view);
		WindowManager m =getWindowManager();
		Window dialogWindow = setProviceDialog.getWindow();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams params = setProviceDialog.getWindow().getAttributes();
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//设置bottom的偏移量
		params.y=30;
		setProviceDialog.getWindow().setAttributes(params);
        //选择空间
		Button btnJing=(Button) view.findViewById(R.id.province_jing);
		btnJing.setOnClickListener(new ProvinceSelectListener(btnJing));
		Button btnHu=(Button) view.findViewById(R.id.province_hu);
		btnHu.setOnClickListener(new ProvinceSelectListener(btnHu));
		Button btnZhe=(Button) view.findViewById(R.id.province_zhe);
		btnZhe.setOnClickListener(new ProvinceSelectListener(btnZhe));
		Button btnSu=(Button) view.findViewById(R.id.province_su);
		btnSu.setOnClickListener(new ProvinceSelectListener(btnSu));
		Button btnYue=(Button) view.findViewById(R.id.province_yue);
		btnYue.setOnClickListener(new ProvinceSelectListener(btnYue));
		Button btnLu=(Button) view.findViewById(R.id.province_lu);
		btnLu.setOnClickListener(new ProvinceSelectListener(btnLu));
		Button btnJin=(Button) view.findViewById(R.id.province_jin);
		btnJin.setOnClickListener(new ProvinceSelectListener(btnJin));
		Button btnJ=(Button) view.findViewById(R.id.province_ji);
		btnJ.setOnClickListener(new ProvinceSelectListener(btnJ));
		
		Button btnYu=(Button) view.findViewById(R.id.province_yu);
		btnYu.setOnClickListener(new ProvinceSelectListener(btnYu));
		Button btnChuan=(Button) view.findViewById(R.id.province_chuan);
		btnChuan.setOnClickListener(new ProvinceSelectListener(btnChuan));
		Button btnYuzhou=(Button) view.findViewById(R.id.province_yuzhou);
		btnYuzhou.setOnClickListener(new ProvinceSelectListener(btnYuzhou));
		Button btnLiao=(Button) view.findViewById(R.id.province_liao);
		btnLiao.setOnClickListener(new ProvinceSelectListener(btnLiao));
		Button btnJilin=(Button) view.findViewById(R.id.province_jilin);
		btnJilin.setOnClickListener(new ProvinceSelectListener(btnJilin));
		Button btnHeilongjiang=(Button) view.findViewById(R.id.province_heilongjiang);
		btnHeilongjiang.setOnClickListener(new ProvinceSelectListener(btnHeilongjiang));
		Button btnWanning=(Button) view.findViewById(R.id.province_wanning);
		btnWanning.setOnClickListener(new ProvinceSelectListener(btnWanning));
		Button btnE=(Button) view.findViewById(R.id.province_e);
		btnE.setOnClickListener(new ProvinceSelectListener(btnE));
		
		Button btnXiang=(Button) view.findViewById(R.id.province_xiang);
		btnXiang.setOnClickListener(new ProvinceSelectListener(btnXiang));
		Button btnGangzhou=(Button) view.findViewById(R.id.province_gangzhou);
		btnGangzhou.setOnClickListener(new ProvinceSelectListener(btnGangzhou));
		Button btnMin=(Button) view.findViewById(R.id.province_min);
		btnMin.setOnClickListener(new ProvinceSelectListener(btnMin));
		Button btnShan=(Button) view.findViewById(R.id.province_shan);
		btnShan.setOnClickListener(new ProvinceSelectListener(btnShan));
		Button btnGang=(Button) view.findViewById(R.id.province_gang);
		btnGang.setOnClickListener(new ProvinceSelectListener(btnGang));
		Button btnNingxia=(Button) view.findViewById(R.id.province_ningxia);
		btnNingxia.setOnClickListener(new ProvinceSelectListener(btnNingxia));
		Button btnMeng=(Button) view.findViewById(R.id.province_meng);
		btnMeng.setOnClickListener(new ProvinceSelectListener(btnMeng));
		Button btnTianjing=(Button) view.findViewById(R.id.province_tianjing);
		btnTianjing.setOnClickListener(new ProvinceSelectListener(btnTianjing));
		
		Button btnGui=(Button) view.findViewById(R.id.province_gui);
		btnGui.setOnClickListener(new ProvinceSelectListener(btnGui));
		Button btnYun=(Button) view.findViewById(R.id.province_yun);
		btnYun.setOnClickListener(new ProvinceSelectListener(btnYun));
		Button btnGuangxi=(Button) view.findViewById(R.id.province_guangxi);
		btnGuangxi.setOnClickListener(new ProvinceSelectListener(btnGuangxi));
		Button btnQiong=(Button) view.findViewById(R.id.province_qiong);
		btnQiong.setOnClickListener(new ProvinceSelectListener(btnQiong));
		Button btnQinghai=(Button) view.findViewById(R.id.province_qinghai);
		btnQinghai.setOnClickListener(new ProvinceSelectListener(btnQinghai));
		Button btnXinjiang=(Button) view.findViewById(R.id.province_xinjiang);
		btnXinjiang.setOnClickListener(new ProvinceSelectListener(btnXinjiang));
		Button btnXinzang=(Button) view.findViewById(R.id.province_xizang);
		btnXinzang.setOnClickListener(new ProvinceSelectListener(btnXinzang));
	}
	
	
	
	class ProvinceSelectListener implements View.OnClickListener{
		private Button btnProvince;
        public ProvinceSelectListener(Button btnProvince){
        	this.btnProvince=btnProvince;
        }
		@Override
		public void onClick(View arg0) {
			String btnString=btnProvince.getText().toString().trim();
			tv_province.setText(btnString);
			if(setProviceDialog!=null){
				setProviceDialog.dismiss();
			}
		}
		
	}

	private int getWith(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width=dm.widthPixels;
		System.out.println("---->>屏幕宽度"+width);
		return width;
	}


	private int getHeight(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height=dm.heightPixels;
		return height;
	}

	/**设置控件中的值*/
	private void setViewData(){
		if(carEntity!=null){
			carMessage.setText(carEntity.getCarBrand());
			tv_province.setText(carEntity.getProvince());
			carNo.setText(carEntity.getCarNum());
			carColor.setText(carEntity.getCarColor());
			String img=carEntity.getCarImage();
			//addImge.setImageUrl(WEBInterface.INDEXIMGURL+img);
			DisplayImageOptions options=
					/**
					 * 配置
					 */
					 new DisplayImageOptions.Builder()
								.showImageForEmptyUri(R.drawable.logo)
								.showImageOnFail(R.drawable.logo)
								.resetViewBeforeLoading(true)
								.cacheOnDisc(true)
								.imageScaleType(ImageScaleType.EXACTLY)
								.bitmapConfig(Bitmap.Config.RGB_565)
								.displayer(new FadeInBitmapDisplayer(300))
								.build();
			//将当前图片保存到sdcard中
			imageLoader.displayImage(WEBInterface.INDEXIMGURL+img, addImge);
			doImageInbackground(WEBInterface.INDEXIMGURL+img);
			
		}
	}

	/**获取当前图片*/
	private void doImageInbackground(final String host){
		new Thread(new Runnable() {
			@Override
			public void run() {
				InputStream stream=JavaHttpUtil.httpGet(host, null);
				if(stream==null){
					Log.d("yckx", "当前图片为空");
					return;
				}
				Log.d("yckx", "保存当前图片");
				isLoadCarImg=true;
				Bitmap bitmap=BitmapFactory.decodeStream(stream);
				saveBitmapToNative(bitmap);
			}
		}).start();
	}

	/**点击事件*/
	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.openCancel:
				takephotoDialog.dismiss();
				break;
			case R.id.openCamera:
				camera();
				if(takephotoDialog!=null){
					takephotoDialog.dismiss();
				}
				break;
			case R.id.openGallery:
				gallery();
				if(takephotoDialog!=null){
					takephotoDialog.dismiss();
				}
				break;
			}
		}
	};

	/**保存并上传车辆数据*/
	private void saveCarMessage(){
		//判断是否要上传车辆图片
		if(isLoadCarImg){
			//先上传车辆图片
			uploadHeadImg();
		}else{
			if(modifyCar){
				addOrModifyCar(carEntity.getId());
			}else{
				addOrModifyCar(null);
			}

		}

	}

	/**上传车辆图片*/
	@SuppressWarnings("unchecked")
	private void uploadHeadImg(){
		new Thread(new Runnable() {
			@Override
			public void run() {

				File file=new File(fileName);
				if(file!=null){
					String token=PrefrenceConfig.getUserMessage(CarDetailedActivity.this).getUserToken();
					RequestParams params=new RequestParams();	
					params.addHeader("token",token);
					params.addBodyParameter("file",file);
					HttpUtils http=new HttpUtils();
					http.send(HttpMethod.POST, WEBInterface.UPLOADURL, params, new RequestCallBack(){
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Log.d(LOG_TAG, "---->>uploadHeadImg()上传图片失败"+arg1);
							Message msg=new Message();
							msg.what=-1;
							mHandler.sendMessage(msg);
						}
						@Override
						public void onSuccess(ResponseInfo arg0) {
							Message msg=new Message();
							//解析数据
							String respose=(String) arg0.result;
							Log.d(LOG_TAG, "---->>uploadHeadImg()上传图片返回来的数据"+respose);
							try {
								JSONObject jSONObject=new JSONObject(respose);
								String code=jSONObject.getString("code");
								if(code.equals("0")){
									String imgurl=jSONObject.getString("filePath");
									mCarImage=imgurl;
									msg.what=UPLOADCARIMGSUCCESS;
									mHandler.sendMessage(msg);
								}
								if(code.equals("2")){
									//token失效
									msg.what=TOKENERROR;
									mHandler.sendMessage(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							System.out.println("---->>uploadHeadImg()访问网络成功头成功");
							Log.d(LOG_TAG, "---->>uploadHeadImg()访问网络成功头成功");
						}});

				}

			}
		}).start();
	}


	/**添加或修改车辆*/
	private void addOrModifyCar(String actionId){
		//final String id="1234";
		final String carNum=carNo.getText().toString().trim();//车牌号
		final String carBrand=carMessage.getText().toString().trim();//品牌
		final String mcarColor=carColor.getText().toString().trim();
		final long carColorRGB=rgb;
		final String carImage=mCarImage;//先上传图片，获取到返回来的carImage值，然后再访问添加接口
		mCarImage="";//清空原来的数据
		final String isDefault="false";
		final String mprovince=tv_province.getText().toString().trim();//省份简称
		//添加车辆
		final HashMap<String, String> hasmp=new HashMap<String, String>();
		if(actionId!=null){
			hasmp.put("id", actionId);
		}
		hasmp.put("carNum", carNum);
		hasmp.put("carBrand", carBrand);
		hasmp.put("carColor", mcarColor);
		if(!carImage.equals("")){
			hasmp.put("carImage", carImage);
		}
		hasmp.put("isDefault", isDefault);
		hasmp.put("province", mprovince);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(CarDetailedActivity.this).getUserToken();
				//new StringEntity(new JSONObject(hasmp).toString(),"UTF-8")
				String respose = null;
				try {
					respose = ApacheHttpUtil.httpPost2(WEBInterface.ADD_OR_MODIFY_CAR_URL, token, new StringEntity(new JSONObject(hasmp).toString(),"UTF-8"));
					//解析数据
					Log.d("yckx", "添加或修改车辆信息:"+respose);
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					Message msg=new Message();
					if(code.equals("0")){
						msg.what=ADDCARSUCESSED;
						mHandler.sendMessage(msg);
					}
					if(code.equals("2")){
						//token失效
						msg.what=TOKENERROR;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("-->>addOrModifyCar()添加车辆返回来的数据"+respose);
				Log.d(LOG_TAG, "-->>addOrModifyCar()添加车辆返回来的数据"+respose);
			}
		}).start();
	}
	
	/**删除车辆成功*/
	private final int DELETECARSUCCESS=3;
	/**删除指定的车辆信息*/
	private void deleteCar(final String id){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url=WEBInterface.DELETECAR+id;
				String token=PrefrenceConfig.getUserMessage(CarDetailedActivity.this).getUserToken();
				try {
					String respose=ApacheHttpUtil.httpPost(url, token, null);
					Log.d("yckx", "--->>deleteCar()删除车辆返回的数据"+respose);
					JSONObject jsonObject=new JSONObject(respose);
					String code=jsonObject.getString("code");
					if(code.equals("0")){
						//成功
						Message msg=new Message();
						msg.what=DELETECARSUCCESS;
						mHandler.sendMessage(msg);
					}
					if(code.equals("2")){
						Message msg=new Message();
						msg.what=TOKENERROR;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}



	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==UPLOADCARIMGSUCCESS){
				//上传车辆图片成功，开始长传车辆数据
				if(modifyCar){
					addOrModifyCar(carEntity.getId());
				}else{
					addOrModifyCar(null);
				}
			}
			if(msg.what==ADDCARSUCESSED){
				//添加车辆成功
				//ToastUtil.show(getApplicationContext(), "添加成功", 5000);
					Intent intent=new Intent(CarDetailedActivity.this,GarageManagerActivity.class);
					intent.putExtra("AddCarActivity", "addsuccess");
					CarDetailedActivity.this.setResult(30, intent);
					CarDetailedActivity.this.finish();
			}
			if(msg.what==TOKENERROR){
				//token失效，跳转到登录界面
				ToastUtil.show(getApplicationContext(), "登录状态失效，请重新登录", 5000);
				Intent intent_login=new Intent(CarDetailedActivity.this,LoginActivity.class);
				startActivity(intent_login);
				finish();
			}
			if(msg.what==DELETECARSUCCESS){
				ToastUtil.show(getApplicationContext(), "删除成功", 2000);
				Intent intent=new Intent(CarDetailedActivity.this,GarageManagerActivity.class);
				intent.putExtra("AddCarActivity", "addsuccess");
				CarDetailedActivity.this.setResult(30, intent);
				CarDetailedActivity.this.finish();
			}
		};
	};

	/**
	 * EditText支持手动输入的监听方法
	 * @param ev_target
	 *        要监听的EditText
	 * @param layout
	 *        ev_target所在的布局中的最外侧layout(LinearLayout,RelayoutLayout,FrameLayout)
	 * 
	 * */
	private void setEditTextKeyBoard(final EditText ev_target,final View layout){
		ev_target.setInputType(InputType.TYPE_CLASS_PHONE);
		ev_target.setFocusableInTouchMode(true);
		ev_target.requestFocus();
		InputMethodManager inputManager = (InputMethodManager)ev_target.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(ev_target, 0);
		layout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener(){
					@Override
					public void onGlobalLayout()
					{
						int heightDiff = layout.getRootView().getHeight() - layout.getHeight();
						if (heightDiff > 100)
						{ // 说明键盘是弹出状态
							//ToastUtil.show(getApplicationContext(), "弹出", 2000);
						} else{
							ev_target.setInputType(InputType.TYPE_NULL);
							ev_target.setFocusableInTouchMode(false);
							//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
						}
					}
				});
	}
	
	
	
	
	//------------照相机、相册start-----------------------//
		private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
		private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
		private static final int PHOTO_REQUEST_CUT = 3;// 结果
		/*
		 * 从相册获取
		 */
		public void gallery() {
			// 激活系统图库，选择一张图片
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
			startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
		}

		private File yckx_tempFile;
		//private String fileName = "/sdcard/yckx/yckx_temp.jpg";
		private String fileName = "/sdcard/yckx/yckx_temp.jpg";
		/* 头像名称 */
		//private static final String PHOTO_FILE_NAME = "temp_photo.jpg";

		public static final int MEDIA_TYPE_IMAGE = 1;  //choose camera image type 
		/*
		 * 从相机获取
		 */
		public void camera() {
			// 激活相机
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
		}
		/*
		 * 判断sdcard是否被挂载
		 */
		private boolean hasSdcard() {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				return false;
			}
		}
		@SuppressLint("SdCardPath") @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(resultCode==RESULT_CODE_CARNAME){
				//返回车辆信息
				EditText carMessage=(EditText)findViewById(R.id.carMessage);
				//String result =data.getStringExtra("carName");//得到新Activity关闭后返回的数据  
				String result=(String) GlobalServiceUtils.getGloubalServiceSession("carName");
				carMessage.setText(result);
			}
			
			if (requestCode == PHOTO_REQUEST_GALLERY) {
				// 从相册返回的数据
				if(data==null){
					/*Intent intent=new Intent(GetPhotoActivity.this,SettingActivity.class);
					intent.putExtra("changeImg", "no");
					startActivity(intent);
					finish();*/
				}
				if (data != null) {
					// 得到图片的全路径
					Uri selectedImage = data.getData();  
					String[] filePathColumn = { MediaStore.Images.Media.DATA };  
					Cursor cursor = CarDetailedActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
					cursor.moveToFirst();  
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
					String imgPath = cursor.getString(columnIndex);  
					cursor.close();  
					Bitmap bmp = BitmapFactory.decodeFile(imgPath); //将所选图片提取为bitmap格式  
					//压缩图片
					//Bitmap bmp=createThumbnail(imgPath,10);
					Log.d(LOG_TAG, imgPath);  
					Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
					Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
					saveBitmapToNative(bmp);
					isLoadCarImg=true;
					//preShowDialog(bmp);
					//展示照片
					if(addImge!=null){
						addImge.setImageBitmap(bmp);
					}
				}

			} else if (requestCode == PHOTO_REQUEST_CAREMA) {
				// 从相机返回的数据
				if (hasSdcard()) {
					try{//相机没有拍照或确定时等不到数据
						Bundle bundle = data.getExtras();  
						Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
						saveBitmapToNative(bitmap);
						//preShowDialog(bitmap);
						isLoadCarImg=true;
						if(addImge!=null){
							addImge.setImageBitmap(bitmap);
						}
					}catch(Exception e){
					}
				} else {
					Toast.makeText(CarDetailedActivity.this, "未找到存储卡，无法存储照片！", 0).show();
				}
			} 
			super.onActivityResult(requestCode, resultCode, data);
		}
		private static final String LOG_TAG = "yckx"; 

		/**将bitmap存储到本地*/
		private void saveBitmapToNative(Bitmap bitmap){
			try {   
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				int options =100;//个人喜欢从80开始,  
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				/*while ((baos.toByteArray().length / 1024 > 300)&&options>=10) {   
					baos.reset();  
					options -= 10;  
					bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
				}*/
				File bitmapFile = new File(fileName);
				FileOutputStream fos = new FileOutputStream(bitmapFile);  
				fos.write(baos.toByteArray());  
				fos.flush();  
				fos.close(); 
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
		}
		//------------照相机、相册end-----------------------//
		
		/**
		 * 压缩图片
		 * 
		 * 
		 */
		private Bitmap createThumbnail(String filepath, int i) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = i;
			return BitmapFactory.decodeFile(filepath, options);
		}
		
		//----------------性别  start-----------------------//
	    private WheelView wheelview;
		private Dialog colorDialog;
		private String carColorStr="黑色";
		private static final String[] PLANETS = new String[]{"黑色", "白色","棕色"};
		//----代替popu窗口的dialog窗口----
		private void showColorDialog(){
			LayoutInflater inflater=CarDetailedActivity.this.getLayoutInflater();
			View view = inflater.inflate(R.layout.row_dialog_sex_select, null);
			colorDialog = new AlertDialog.Builder(CarDetailedActivity.this).create();
			colorDialog.show();
			colorDialog.setContentView(view);
			WindowManager m =getWindowManager();
			Window dialogWindow = colorDialog.getWindow();
			Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
			WindowManager.LayoutParams params = colorDialog.getWindow().getAttributes();
			params.width = (int) (d.getWidth());
			params.height = params.height;
			dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
			//设置bottom的偏移量
			colorDialog.getWindow().setAttributes(params);

			wheelview=(WheelView) view.findViewById(R.id.main_wv);
			wheelview.setOffset(2);
			wheelview.setItems(Arrays.asList(PLANETS));
			wheelview.setOnWheelPickerListener(new OnWheelPickerListener(){
				@Override
				public void wheelSelect(int position, String content) {
					carColorStr=content;
				}});
			view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					colorDialog.dismiss();
				}});
			view.findViewById(R.id.confirm).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					carColor.setText(carColorStr);
					colorDialog.dismiss();
				}});
		}
		//----------------性别  end-----------------------//
		
}
