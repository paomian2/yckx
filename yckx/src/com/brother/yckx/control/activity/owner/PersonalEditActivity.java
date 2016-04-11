package com.brother.yckx.control.activity.owner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brother.BaseActivity;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.yckx.R;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.WheelView;
import com.brother.yckx.view.WheelView.OnWheelPickerListener;
import com.brother.yckx.view.image2.CacheCircleImageView;

public class PersonalEditActivity extends BaseActivity{
	private LinearLayout layout_return;
	private TextView btnsave;
	private CacheCircleImageView heandImg;
	private RelativeLayout layout_headImg,layout_nickname,layout_sex,layout_age,layout_phone,layout_sign,layout_hangye;
	
	private TextView tv_nickname,tv_sex,tv_age,tv_sign,tv_hangye;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_edit);
		initview();
	}


	private void initview(){
		layout_return=(LinearLayout) findViewById(R.id.layout_return);
		btnsave=(TextView) findViewById(R.id.btnsave);
		heandImg=(CacheCircleImageView) findViewById(R.id.userImg);
		

		layout_headImg=(RelativeLayout) findViewById(R.id.layout_headImg);
		layout_nickname=(RelativeLayout) findViewById(R.id.layout_nickname);
		layout_sex=(RelativeLayout) findViewById(R.id.layout_sex);
		layout_age=(RelativeLayout) findViewById(R.id.layout_age);
		layout_sign=(RelativeLayout) findViewById(R.id.layout_sign);
		layout_hangye=(RelativeLayout) findViewById(R.id.layout_hangye);;
		
		tv_nickname=(TextView) findViewById(R.id.tv_nickname);
		tv_sex=(TextView) findViewById(R.id.tv_sex);
		tv_age=(TextView) findViewById(R.id.tv_age);
		tv_sign=(TextView) findViewById(R.id.tv_sign);
		tv_hangye=(TextView) findViewById(R.id.tv_hangye);
		
		layout_headImg.setOnClickListener(listener);
		layout_nickname.setOnClickListener(listener);
		layout_sex.setOnClickListener(listener);
		layout_age.setOnClickListener(listener);
		layout_sign.setOnClickListener(listener);
		layout_sign.setOnClickListener(listener);
		
		//设置头像
		String headURL=WEBInterface.INDEXIMGURL+PrefrenceConfig.getUserMessage(PersonalEditActivity.this).getUserIamgeUrl();
		heandImg.setImageUrl(headURL);
	}

	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.layout_return:
				PersonalEditActivity.this.finish();
				break;
			case R.id.btnsave:
				break;
			case R.id.layout_headImg://弹出一个窗口
				takePhonesDialog();
				break;
			case R.id.layout_nickname:
				Intent intentNick=new Intent(PersonalEditActivity.this,EditNicknameActivity.class);
				startActivityForResult(intentNick, NICKNAMEFROMNEXT);
				break;
			case R.id.layout_sex://弹出一个窗口
				showSexDialog();
				break;
			case R.id.layout_age://弹出一个窗口
				showAgeDialog();
				break;
			case R.id.layout_sign:
				Intent intentSign=new Intent(PersonalEditActivity.this,SignActivity.class);
				startActivityForResult(intentSign, SIGNROMNEXT);
				break;
			case R.id.layout_hangye:
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

	private Dialog takephotoDialog;
	//----代替popu窗口的dialog窗口----
	private void takePhonesDialog(){
		LayoutInflater inflater=PersonalEditActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		takephotoDialog = new AlertDialog.Builder(PersonalEditActivity.this).create();
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
	
	/**添加成功重新刷新页面*/
	private final int NICKNAMEFROMNEXT=3;
	private final int SIGNROMNEXT=4;
	@SuppressLint("SdCardPath") @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//监听是否是上下一个页面返回来的数据
		if(resultCode==NICKNAMEFROMNEXT){
			String result =data.getStringExtra("nickName").trim();//得到新Activity关闭后返回的数据  
			Log.d("yckx", "结果长度:"+result.length());
			tv_nickname.setText(result);
		}
		if(resultCode==SIGNROMNEXT){
			String result =data.getStringExtra("sign").trim();//得到新Activity关闭后返回的数据  
			Log.d("yckx", "结果长度:"+result.length());
			tv_sign.setText(result);
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
				Cursor cursor = PersonalEditActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
				cursor.moveToFirst();  
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
				String imgPath = cursor.getString(columnIndex);  
				cursor.close();  
				Bitmap bmp = BitmapFactory.decodeFile(imgPath); //将所选图片提取为bitmap格式  
				Log.d(LOG_TAG, imgPath);  
				Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
				Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
				saveBitmapToNative(bmp);
				heandImg.setCircleImageBitmap(bmp);
			}

		} else if (requestCode == PHOTO_REQUEST_CAREMA) {
			// 从相机返回的数据
			if (hasSdcard()) {
				try{//相机没有拍照或确定时等不到数据
					Bundle bundle = data.getExtras();  
					Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
					saveBitmapToNative(bitmap);
					//(bitmap);
					heandImg.setCircleImageBitmap(bitmap);
				}catch(Exception e){
				}
			} else {
				Toast.makeText(PersonalEditActivity.this, "未找到存储卡，无法存储照片！", 0).show();
			}
		} 
		super.onActivityResult(requestCode, resultCode, data);
	}
	private static final String LOG_TAG = "yckx"; 

	/**将bitmap存储到本地*/
	@SuppressLint("SdCardPath")
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


	//----------------性别  start-----------------------//
    private WheelView wheelview;
	private Dialog sexDialog;
	private String userSex="男";
	private static final String[] PLANETS = new String[]{"男", "女"};
	//----代替popu窗口的dialog窗口----
	private void showSexDialog(){
		LayoutInflater inflater=PersonalEditActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_sex_select, null);
		sexDialog = new AlertDialog.Builder(PersonalEditActivity.this).create();
		sexDialog.show();
		sexDialog.setContentView(view);
		WindowManager m =getWindowManager();
		Window dialogWindow = sexDialog.getWindow();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams params = sexDialog.getWindow().getAttributes();
		params.width = (int) (d.getWidth());
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//设置bottom的偏移量
		sexDialog.getWindow().setAttributes(params);

		wheelview=(WheelView) view.findViewById(R.id.main_wv);
		wheelview.setOffset(1);
		wheelview.setItems(Arrays.asList(PLANETS));
		wheelview.setOnWheelPickerListener(new OnWheelPickerListener(){
			@Override
			public void wheelSelect(int position, String content) {
				userSex=content;
			}});
		view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				sexDialog.dismiss();
			}});
		view.findViewById(R.id.confirm).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				tv_sex.setText(userSex);
				sexDialog.dismiss();
			}});
	}
	//----------------性别  end-----------------------//
	
	//----------------性别  start-----------------------//
    private WheelView wheelview_age;
	private Dialog ageDialog;
	private String userAge="90后";
	private static final String[] PLANETS_AGE = new String[]{"00后","90后","80后","70后","60后","50后"};
	//----代替popu窗口的dialog窗口----
	private void showAgeDialog(){
		LayoutInflater inflater=PersonalEditActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_sex_select, null);
		ageDialog = new AlertDialog.Builder(PersonalEditActivity.this).create();
		ageDialog.show();
		ageDialog.setContentView(view);
		WindowManager m =getWindowManager();
		Window dialogWindow = ageDialog.getWindow();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams params = ageDialog.getWindow().getAttributes();
		params.width = (int) (d.getWidth());
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//设置bottom的偏移量
		ageDialog.getWindow().setAttributes(params);

		wheelview_age=(WheelView) view.findViewById(R.id.main_wv);
		wheelview_age.setOffset(1);
		wheelview_age.setItems(Arrays.asList(PLANETS_AGE));
		wheelview_age.setOnWheelPickerListener(new OnWheelPickerListener(){
			@Override
			public void wheelSelect(int position, String content) {
				userAge=content;
			}});
		view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ageDialog.dismiss();
			}});
		view.findViewById(R.id.confirm).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				tv_age.setText(userAge);
				ageDialog.dismiss();
			}});
	}
	//----------------性别  end-----------------------//
	
	
}
