package com.brother.yckx.control.activity.owner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.brother.BaseActivity;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.brother.yckx.model.UserEntity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GetPhotoToudloadCarImgActivity extends BaseActivity{
	private ImageView showImg;
	private Button btnUp,upCancle;
	
	//判断是否已经选择图片
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getphoto);
		findViewById(R.id.commitImg).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				uploadHeadImg();

			}});
		
		findViewById(R.id.upCancle).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
			}});
		//
		Intent intent=getIntent();
		String action=intent.getStringExtra("action");
		if(action.equals("camera")){
			//打开相机
			camera();
		}
		if(action.equals("gallery")){
			//打开相册
			gallery();
		}
	}



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
				Intent intent=new Intent(GetPhotoToudloadCarImgActivity.this,CarDetailedActivity.class);
				intent.putExtra("changeImg", "no");
				startActivity(intent);
				finish();
			}
			if (data != null) {
				// 得到图片的全路径
				Uri selectedImage = data.getData();  
				String[] filePathColumn = { MediaStore.Images.Media.DATA };  
				Cursor cursor = GetPhotoToudloadCarImgActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
				cursor.moveToFirst();  
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
				String imgPath = cursor.getString(columnIndex);  
				cursor.close();  
				Bitmap bmp = BitmapFactory.decodeFile(imgPath); //将所选图片提取为bitmap格式  
				Log.d(LOG_TAG, imgPath);  
				Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
				Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
				saveBitmapToNative(bmp);
				((ImageView) findViewById(R.id.showImg)).setImageBitmap(bmp); //show image  
			}

		} else if (requestCode == PHOTO_REQUEST_CAREMA) {
			// 从相机返回的数据
			if (hasSdcard()) {
				try{//相机没有拍照或确定时等不到数据
					Bundle bundle = data.getExtras();  
					Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
					saveBitmapToNative(bitmap);
					((ImageView) findViewById(R.id.showImg)).setImageBitmap(bitmap);// 将图片显示在ImageView里
					Toast.makeText(this, fileName, Toast.LENGTH_LONG).show();
				}catch(Exception e){
					Intent intent=new Intent(GetPhotoToudloadCarImgActivity.this,CarDetailedActivity.class);
					intent.putExtra("changeImg", "no");
					startActivity(intent);
					finish();
					e.printStackTrace();
				}
				//saveBitmap(bitmap);
			} else {
				Toast.makeText(GetPhotoToudloadCarImgActivity.this, "未找到存储卡，无法存储照片！", 0).show();
			}

		} /*else if (requestCode == PHOTO_REQUEST_CUT) {
			// 从剪切图片返回的数据
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
			    showImg.setImageBitmap(bitmap);
			}
			try {
				// 将临时文件删除
				//上传数据
				//yckx_tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}*/
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


	
	private void uploadHeadImg(){
		Intent intent=new Intent(this,CarDetailedActivity.class);
		intent.putExtra("AddCarActivity", "upload_success");
		startActivity(intent);
		finish();
	}
	
}
