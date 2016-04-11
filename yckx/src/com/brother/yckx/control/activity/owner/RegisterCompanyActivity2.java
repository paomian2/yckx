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
		//��ȡͼƬ
		takephoto=(ImageView) findViewById(R.id.takephoto);
		takephoto.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				takePhonesDialog();
			}});
	}


	private Dialog takephotoDialog;
	//----����popu���ڵ�dialog����----
	private void takePhonesDialog(){
		LayoutInflater inflater=RegisterCompanyActivity2.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		takephotoDialog = new AlertDialog.Builder(RegisterCompanyActivity2.this).create();
		takephotoDialog.show();
		takephotoDialog.setContentView(view);
		WindowManager m =getWindowManager();
		Window dialogWindow = takephotoDialog.getWindow();
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ����
		WindowManager.LayoutParams params = takephotoDialog.getWindow().getAttributes();
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//����bottom��ƫ����
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
				//�����
				if(takephotoDialog!=null){
					takephotoDialog.dismiss();
				}
				camera();
				break;
			case R.id.openGallery:
				//�����
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


	//------------����������start-----------------------//
			private static final int PHOTO_REQUEST_CAREMA = 1;// ����
			private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
			private static final int PHOTO_REQUEST_CUT = 3;// ���
			/*
			 * ������ȡ
			 */
			public void gallery() {
				// ����ϵͳͼ�⣬ѡ��һ��ͼƬ
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				// ����һ�����з���ֵ��Activity��������ΪPHOTO_REQUEST_GALLERY
				startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
			}

			private File yckx_tempFile;
			//private String fileName = "/sdcard/yckx/yckx_temp.jpg";
			private String fileName1 = "/sdcard/yckx/yckx_evaluate1.png";
			private String fileName2 = "/sdcard/yckx/yckx_evaluate2.png";
			private String fileName3 = "/sdcard/yckx/yckx_evaluate3.png";
			/* ͷ������ */
			//private static final String PHOTO_FILE_NAME = "temp_photo.jpg";

			public static final int MEDIA_TYPE_IMAGE = 1;  //choose camera image type 
			/*
			 * �������ȡ
			 */
			public void camera() {
				// �������
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
			}
			/*
			 * �ж�sdcard�Ƿ񱻹���
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
					// ����᷵�ص�����
					if(data==null){
						/*Intent intent=new Intent(GetPhotoActivity.this,SettingActivity.class);
						intent.putExtra("changeImg", "no");
						startActivity(intent);
						finish();*/
					}
					if (data != null) {
						// �õ�ͼƬ��ȫ·��
						Uri selectedImage = data.getData();  
						String[] filePathColumn = { MediaStore.Images.Media.DATA };  
						Cursor cursor = RegisterCompanyActivity2.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
						cursor.moveToFirst();  
						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
						String imgPath = cursor.getString(columnIndex);  
						cursor.close();  
						Bitmap bmp = BitmapFactory.decodeFile(imgPath); //����ѡͼƬ��ȡΪbitmap��ʽ  
						Log.d(LOG_TAG, imgPath);  
						Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
						Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
						//��ǰҪ������ļ���
						saveBitmapToNative(bmp,fileName1);
					}

				} else if (requestCode == PHOTO_REQUEST_CAREMA) {
					// ��������ص�����
					if (hasSdcard()) {
						try{//���û�����ջ�ȷ��ʱ�Ȳ�������
							Bundle bundle = data.getExtras();  
							Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ  
							//��ǰҪ������ļ���
								saveBitmapToNative(bitmap,fileName1);
						}catch(Exception e){
						}
					} else {
						Toast.makeText(RegisterCompanyActivity2.this, "δ�ҵ��洢�����޷��洢��Ƭ��", 0).show();
					}
				} 
				super.onActivityResult(requestCode, resultCode, data);
			}
			private static final String LOG_TAG = "yckx"; 

			/**��bitmap�洢������*/
			@SuppressLint("SdCardPath")
			private void saveBitmapToNative(Bitmap bitmap,String savaFilName){
				FileOutputStream b = null;  				  
				yckx_tempFile = new File("/sdcard/yckx/");  
				if(!yckx_tempFile.exists()){
					yckx_tempFile.mkdirs();// �����ļ���  
				}
				try {   
					ByteArrayOutputStream baos = new ByteArrayOutputStream();  
					int options =100;//����ϲ����80��ʼ,  
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


			//------------����������end-----------------------//




}
