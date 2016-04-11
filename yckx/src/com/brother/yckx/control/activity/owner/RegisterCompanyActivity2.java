package com.brother.yckx.control.activity.owner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.yckx.R;

@SuppressLint("SdCardPath")
public class RegisterCompanyActivity2 extends BaseActivity{

	private LinearLayout layout_return;
	private TextView tv_complete;
	private ImageView takephoto;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_company2);
		layout_return=(LinearLayout) findViewById(R.id.layout_return);
		layout_return.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}});
		tv_complete=(TextView) findViewById(R.id.tv_complete);
		tv_complete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {

			}});
		//获取图片
		takephoto=(ImageView) findViewById(R.id.takephoto);
		takephoto.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				takePhonesDialog();
			}});
	}


	private Dialog takephotoDialog;
	//----代替popu窗口的dialog窗口----
	private void takePhonesDialog(){
		LayoutInflater inflater=RegisterCompanyActivity2.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		takephotoDialog = new AlertDialog.Builder(RegisterCompanyActivity2.this).create();
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

	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
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
			private String fileName1 = "/sdcard/yckx/yckx_evaluate1.png";
			private String fileName2 = "/sdcard/yckx/yckx_evaluate2.png";
			private String fileName3 = "/sdcard/yckx/yckx_evaluate3.png";
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
						Cursor cursor = RegisterCompanyActivity2.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
						cursor.moveToFirst();  
						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
						String imgPath = cursor.getString(columnIndex);  
						cursor.close();  
						Bitmap bmp = BitmapFactory.decodeFile(imgPath); //将所选图片提取为bitmap格式  
						Log.d(LOG_TAG, imgPath);  
						Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
						Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
						//当前要保存的文件名
						saveBitmapToNative(bmp,fileName1);
					}

				} else if (requestCode == PHOTO_REQUEST_CAREMA) {
					// 从相机返回的数据
					if (hasSdcard()) {
						try{//相机没有拍照或确定时等不到数据
							Bundle bundle = data.getExtras();  
							Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
							//当前要保存的文件名
								saveBitmapToNative(bitmap,fileName1);
						}catch(Exception e){
						}
					} else {
						Toast.makeText(RegisterCompanyActivity2.this, "未找到存储卡，无法存储照片！", 0).show();
					}
				} 
				super.onActivityResult(requestCode, resultCode, data);
			}
			private static final String LOG_TAG = "yckx"; 

			/**将bitmap存储到本地*/
			@SuppressLint("SdCardPath")
			private void saveBitmapToNative(Bitmap bitmap,String savaFilName){
				FileOutputStream b = null;  				  
				yckx_tempFile = new File("/sdcard/yckx/");  
				if(!yckx_tempFile.exists()){
					yckx_tempFile.mkdirs();// 创建文件夹  
				}
				try {   
					ByteArrayOutputStream baos = new ByteArrayOutputStream();  
					int options =100;//个人喜欢从80开始,  
					bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);  
					while (baos.toByteArray().length / 1024 > 300) {   
						baos.reset();  
						options -= 10;  
						bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);  
					}
					File bitmapFile = new File(savaFilName);
					FileOutputStream fos = new FileOutputStream(bitmapFile);  
					fos.write(baos.toByteArray());  
					fos.flush();  
					fos.close(); 
					} catch (IOException e) {  
						e.printStackTrace();  
					}  
			}


			//------------照相机、相册end-----------------------//




}
