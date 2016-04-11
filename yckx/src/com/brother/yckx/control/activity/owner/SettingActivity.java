package com.brother.yckx.control.activity.owner;


import io.rong.imkit.RongIM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.model.UserEntity;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.image2.CacheCircleImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
public class SettingActivity extends BaseActivity{
	private CacheCircleImageView touxiang;
	private TextView userName,newfunction;
	private RelativeLayout userName_layout,modify_pwd_layout,user_agreement_layout,newuser_lead_layout,CustomerService_layout,exit_layout;
	/**加载头像成功*/
	private final int LOADIMG_SUCCESS=10;
	/**上传头像成功*/
	private final int UPLOADNIMG_SUCCESS=0;
	/**上传头像失败*/
	private final int UPLOADIMG_FAILED=40;
	/**token失效*/
	private final int TOKEN_RORRO=2;
	/**修改密码成功*/
	private final int MODIFY_PWD_SUCCESS=20;
	/**原始密码错误*/
	private final int OLDPWD_ERROR=21;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setActionBar(R.string.setting, R.drawable.btn_homeasup_default, NULL_ID, listener);
		init();
	}

	private void init(){
		touxiang=(CacheCircleImageView) findViewById(R.id.touxiang);
		userName=(TextView) findViewById(R.id.userName);
		newfunction=(TextView) findViewById(R.id.newfunction);
		userName_layout=(RelativeLayout) findViewById(R.id.userName_layout);
		modify_pwd_layout=(RelativeLayout) findViewById(R.id.modify_pwd_layout);
		user_agreement_layout=(RelativeLayout) findViewById(R.id.user_agreement_layout);
		newuser_lead_layout=(RelativeLayout) findViewById(R.id.newuser_lead_layout);
		CustomerService_layout=(RelativeLayout) findViewById(R.id.CustomerService_layout);
		exit_layout=(RelativeLayout) findViewById(R.id.layout_exit);
		//设置监听
		userName_layout.setOnClickListener(listener);
		modify_pwd_layout.setOnClickListener(listener);
		user_agreement_layout.setOnClickListener(listener);
		newuser_lead_layout.setOnClickListener(listener);
		CustomerService_layout.setOnClickListener(listener);
		exit_layout.setOnClickListener(listener);
		//设置头像和用户名
		setImgAndUserNameInbackGround();
	}

	/**设置头像和用户名,用户名简介(暂时不开放)*/
	private void setImgAndUserNameInbackGround(){
		UserEntity mUserEntity=PrefrenceConfig.getUserMessage(SettingActivity.this);
		String name=mUserEntity.getUserName();
		String phone=mUserEntity.getUserPhone();
		userName.setText(name);
		if(!name.equals(phone)){
			newfunction.setText(phone);
		}else{
			//显示用户简介(未开通)
		}
		
	    String imgUrl=WEBInterface.INDEXIMGURL+PrefrenceConfig.getUserMessage(SettingActivity.this).getUserIamgeUrl();
		touxiang.setImageUrl(imgUrl);
		
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				String imgUrl=WEBInterface.INDEXIMGURL+PrefrenceConfig.getUserMessage(SettingActivity.this).getUserIamgeUrl();
				Log.d("yckx", "加载头像的地址"+imgUrl);
				//ImageLoader.getInstance().displayImage(imgUrl, touxiang); 
				Bitmap settingBitmp=ImageLoader.getInstance().loadImageSync(imgUrl); 
				if(settingBitmp!=null){
					Message msg=new Message();
					msg.what=LOADIMG_SUCCESS;
					msg.obj=settingBitmp;
					mHandler.sendMessage(msg);
				}
			}
		}).start();*/
	}


	/**处理UI层*/
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOADIMG_SUCCESS://下载头像成功
				Bitmap bitmap=(Bitmap) msg.obj;
				if (bitmap!=null) {
					touxiang.setCircleImageBitmap(bitmap);
				}break;
			case UPLOADIMG_FAILED://上传失败
				if(mPreShowPhotoDialog!=null){
					mPreShowPhotoDialog.dismiss();
				}
				break;
			case UPLOADNIMG_SUCCESS://上传成功，重新设置头像
				setImgAndUserNameInbackGround();
				if(mPreShowPhotoDialog!=null){
					mPreShowPhotoDialog.dismiss();
				}
				break;
			case TOKEN_RORRO://登录状态失效
				Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
				startActivity(intent);
				break;
			case MODIFY_PWD_SUCCESS://修改密码成功
				if(modifyDialog!=null){
					modifyDialog.dismiss();
					ToastUtil.show(getApplicationContext(), "修改成功",2000);
				}
			case OLDPWD_ERROR://原始密码错误
				ToastUtil.show(getApplicationContext(), "您的原始密码错误", 2000);
				if(btn_ok!=null){
					btn_ok.setClickable(true);
				}
			default:
				break;
			}
		};
	};

	private Dialog takephotoDialog;
	//----代替popu窗口的dialog窗口----
	private void takePhonesDialog(){
		LayoutInflater inflater=SettingActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		takephotoDialog = new AlertDialog.Builder(SettingActivity.this).create();
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


	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.iv_action_left:
				SettingActivity.this.finish();
			case R.id.userName_layout://用户名
				takePhonesDialog();
				break;
			case R.id.modify_pwd_layout://在线修改密码
				resetPwdOnline();
				break;
			case R.id.user_agreement_layout://用户使用协议
				GlobalServiceUtils.setGloubalServiceSession("WebViewActivity","useAgreement");
				Intent intent=new Intent(SettingActivity.this,WebViewActivity.class);
				startActivity(intent);
				break;
			case R.id.newuser_lead_layout://新手引导界面
				break;
			case R.id.CustomerService_layout://客服咨询
				String phoneNo="4008793689";
				Intent intentPhone = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				SettingActivity.this.startActivity(intentPhone);
				break;
			case R.id.layout_exit://退出登录
				//清空用户信息，信鸽、荣云注销
				existLogin();
				break;
			case R.id.openCamera:
				//打开相机
				if(takephotoDialog!=null){
					takephotoDialog.dismiss();
				}
				camera();
				break;
			case R.id.openGallery:
				//打开相册
				if(takephotoDialog!=null){
					takephotoDialog.dismiss();
				}
				gallery();
				break;
			case R.id.openCancel:
				if(takephotoDialog!=null)
					takephotoDialog.dismiss();
				break;
			}
		}
	};


	//private File yckx_tempFile;
	private File headImgFile;
	/**把图片保存到本地*/
	/**将bitmap存储到本地*/
	@SuppressLint("SdCardPath") 
	private void saveHeadBitmapToNative(String fileName,Bitmap bitmap){
		FileOutputStream b = null;  				  
		yckx_tempFile = new File("/sdcard/yckx/");  
		if(!yckx_tempFile.exists()){
			yckx_tempFile.mkdirs();// 创建文件夹  
		}
		headImgFile=new File("/sdcard/yckx/headimg/");
		if(!headImgFile.exists()){
			headImgFile.mkdirs();
		}
		try {   
			b = new FileOutputStream(fileName); //写到存储卡 
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} finally {  
			try {
				if(b!=null){
					b.flush();  
					b.close();
				}
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}
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
	private String fileName = "/sdcard/yckx/yckx_temp.png";
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
				Cursor cursor = SettingActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
				cursor.moveToFirst();  
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
				String imgPath = cursor.getString(columnIndex);  
				cursor.close();  
				Bitmap bmp = BitmapFactory.decodeFile(imgPath); //将所选图片提取为bitmap格式  
				Log.d(LOG_TAG, imgPath);  
				Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
				Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
				saveBitmapToNative(bmp);
				preShowDialog(bmp);
			}

		} else if (requestCode == PHOTO_REQUEST_CAREMA) {
			// 从相机返回的数据
			if (hasSdcard()) {
				try{//相机没有拍照或确定时等不到数据
					Bundle bundle = data.getExtras();  
					Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
					saveBitmapToNative(bitmap);
					preShowDialog(bitmap);
				}catch(Exception e){
				}
			} else {
				Toast.makeText(SettingActivity.this, "未找到存储卡，无法存储照片！", 0).show();
			}
		} 
		super.onActivityResult(requestCode, resultCode, data);
	}
	private static final String LOG_TAG = "yckx"; 

	/**将bitmap存储到本地*/
	private void saveBitmapToNative(Bitmap bitmap){
		FileOutputStream b = null;  				  
		yckx_tempFile = new File("/sdcard/yckx/");  
		if(!yckx_tempFile.exists()){
			yckx_tempFile.mkdirs();// 创建文件夹  
		}
		try {   
			b = new FileOutputStream(fileName); //写到存储卡 
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				b.flush();  
				b.close();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}
	}


	//------------照相机、相册end-----------------------//


	//-----------获取到图片之后进行预览start--------------//

	private Dialog mPreShowPhotoDialog;
	//----代替popu窗口的dialog窗口----
	private void preShowDialog(Bitmap bitmap){
		LayoutInflater inflater=SettingActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_preshow, null);
		mPreShowPhotoDialog = new AlertDialog.Builder(SettingActivity.this).create();
		mPreShowPhotoDialog.show();
		mPreShowPhotoDialog.setContentView(view);
		/*mPreShowPhotoDialog.getWindow().setLayout(getWith()-50, 500);
		Window dialogWindow = mPreShowPhotoDialog.getWindow();  */
		Window dialogWindow = mPreShowPhotoDialog.getWindow(); 
		WindowManager.LayoutParams params = dialogWindow.getAttributes();  
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		WindowManager m =getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		//偏移量
		params.y=30;
		mPreShowPhotoDialog.getWindow().setAttributes(params);
		CircleImageView preShowImg=(CircleImageView) view.findViewById(R.id.preShowImg);
		preShowImg.setCircleImageBitmap(bitmap);
		final TextView uploadOk=(TextView) view.findViewById(R.id.uploadOk);
		uploadOk.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//上传头像//设置按钮不可用
				uploadOk.setText("正在上传...");
				uploadOk.setClickable(false);
				uploadHeadImg();

			}});
		view.findViewById(R.id.uploadCancel).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				mPreShowPhotoDialog.dismiss();
			}});
	}

	//-----------获取到图片之后进行预览end--------------//


	/**上传头像*/
	@SuppressWarnings("unchecked")
	private void uploadHeadImg(){
		System.out.println("--->>uploadHeadImg()开始上传数据");
		new Thread(new Runnable() {
			@Override
			public void run() {
				File file=new File(fileName);
				if(file!=null){
					String token=PrefrenceConfig.getUserMessage(SettingActivity.this).getUserToken();
					RequestParams params=new RequestParams();	
					params.addHeader("token",token);
					params.addBodyParameter("header",file);
					HttpUtils http=new HttpUtils();
					http.send(HttpMethod.POST, WEBInterface.UPLOADHEADURL, params, new RequestCallBack(){
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							System.out.println("---->>uploadHeadImg()上传头像失败");
							Message msg=new Message();
							msg.what=UPLOADIMG_FAILED;
							mHandler.sendMessage(msg);
						}
						@Override
						public void onSuccess(ResponseInfo arg0) {
							Message msg=new Message();
							//解析数据
							String respose=(String) arg0.result;
							try {
								JSONObject jSONObject=new JSONObject(respose);
								String code=jSONObject.getString("code");
								if(code.equals("0")){
									JSONObject childJSONObject=jSONObject.getJSONObject("user");
									String userImgUrl=childJSONObject.getString("userImageUrl");
									UserEntity user=PrefrenceConfig.getUserMessage(SettingActivity.this);
									user.setUserIamgeUrl(userImgUrl);
									//更新本地文件
									PrefrenceConfig.setLoginUserMessage(SettingActivity.this, user);
									msg.what=UPLOADNIMG_SUCCESS;
									mHandler.sendMessage(msg);
								}
								if(code.equals("2")){
									//token失效
									msg.what=TOKEN_RORRO;
									mHandler.sendMessage(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							System.out.println("--->>>uploadHeadImg()返回的数据"+respose);
							System.out.println("---->>uploadHeadImg()访问网络成功头成功");
						}});
				}
			}
		}).start();
	}
	
	private Dialog modifyDialog;
	private Button btn_ok;//确定修改密码
	/**用户在线修改密码*/
	private void resetPwdOnline(){
		LayoutInflater inflater=SettingActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_modify_pwd, null);
		modifyDialog = new AlertDialog.Builder(SettingActivity.this).create();
		modifyDialog.show();
		modifyDialog.setContentView(view);
		Window dialogWindow = modifyDialog.getWindow(); 
		WindowManager.LayoutParams params = dialogWindow.getAttributes();  
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		WindowManager m =getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		//偏移量
		modifyDialog.getWindow().setAttributes(params);
		
		final EditText ev_oldPwd=(EditText) view.findViewById(R.id.oldPwd);
		final EditText ev_newPwd=(EditText) view.findViewById(R.id.newPwd);
		Button btn_cancel=(Button) view.findViewById(R.id.btn_cancel);
	    btn_ok=(Button) view.findViewById(R.id.btn_ok);
		btn_cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				modifyDialog.dismiss();
			}});
		btn_ok.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				btn_ok.setClickable(false);
				String oldPwd=ev_oldPwd.getText().toString().trim();
				String newPwd=ev_newPwd.getText().toString().trim();
				if(oldPwd.equals("")||newPwd.equals("")){
					btn_ok.setClickable(true);
					ToastUtil.show(getApplicationContext(), "密码不能为空", 2000);
					return;
				}
				if(oldPwd.equals(newPwd)){
					btn_ok.setClickable(true);
					ToastUtil.show(getApplicationContext(), "新旧密码不能一致", 2000);
					return;
				}
				resetPwdInbackground(oldPwd,newPwd);
			}});
		
	}
	
	
	private void resetPwdInbackground(final String oldPwd,final String newPwd){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url=WEBInterface.MODIFYPWDONLINE_URL+oldPwd+"/"+newPwd;
				String token=PrefrenceConfig.getUserMessage(SettingActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpPost(url, token, null);
				Log.d("yckx",respose);
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						//修改密码成功
						Message msg=new Message();
						msg.what=MODIFY_PWD_SUCCESS;
						mHandler.sendMessage(msg);
					}
					if(code.equals("2")){
						Message msg=new Message();
						msg.what=TOKEN_RORRO;
						mHandler.sendMessage(msg);
					}
					if(code.equals("21")){
						Message msg=new Message();
						msg.what=OLDPWD_ERROR;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**用户退出登录状态*/
	private void existLogin(){
		LayoutInflater inflater=SettingActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_setting_activity, null);
		final Dialog dialog = new AlertDialog.Builder(SettingActivity.this).create();
		dialog.show();
		dialog.setContentView(view);
		int thisWith=(int) (getWith()*0.6);
		int thisHeight=(int) (getHeight()*0.3);
		dialog.getWindow().setLayout(thisWith, thisHeight);

		TextView exitOK=(TextView) view.findViewById(R.id.exist);
		TextView cancel=(TextView) view.findViewById(R.id.cancel);
		exitOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				UserEntity user=new UserEntity();
				PrefrenceConfig.setLoginUserMessage(SettingActivity.this, user);
				RongIM.getInstance().logout();
				Intent intent=new Intent(SettingActivity.this,HomeActivity.class);
				startActivity(intent);
				finish();
			}});
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				return;

			}});
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(takephotoDialog!=null){
			takephotoDialog.dismiss();
		}
		if(mPreShowPhotoDialog!=null){
			mPreShowPhotoDialog.dismiss();
		}
		if(modifyDialog!=null){
			modifyDialog.dismiss();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(takephotoDialog!=null){
			takephotoDialog=null;
		}
		if(mPreShowPhotoDialog!=null){
			mPreShowPhotoDialog=null;
		}
		if(modifyDialog!=null){
			modifyDialog=null;
		}
	}

}
