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

public class GetPhotoActivity extends BaseActivity{
	private ImageView showImg;
	private Button btnUp;
	
	//�ж��Ƿ��Ѿ�ѡ��ͼƬ
	
	//�ж����ϴ�ͷ�����ϴ�����ͼƬ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getphoto);
		
		
		findViewById(R.id.commitImg).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				uploadHeadImg();

			}});
		//
		Intent intent=getIntent();
		String action=intent.getStringExtra("action");
		if(action.equals("camera")){
			//�����
			camera();
		}
		if(action.equals("gallery")){
			//�����
			gallery();
		}
	}



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
	private String fileName = "/sdcard/yckx/yckx_temp.png";
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
				Intent intent=new Intent(GetPhotoActivity.this,SettingActivity.class);
				intent.putExtra("changeImg", "no");
				startActivity(intent);
				finish();
			}
			if (data != null) {
				// �õ�ͼƬ��ȫ·��
				Uri selectedImage = data.getData();  
				String[] filePathColumn = { MediaStore.Images.Media.DATA };  
				Cursor cursor = GetPhotoActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
				cursor.moveToFirst();  
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
				String imgPath = cursor.getString(columnIndex);  
				cursor.close();  
				Bitmap bmp = BitmapFactory.decodeFile(imgPath); //����ѡͼƬ��ȡΪbitmap��ʽ  
				Log.d(LOG_TAG, imgPath);  
				Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
				Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
				saveBitmapToNative(bmp);
				((ImageView) findViewById(R.id.showImg)).setImageBitmap(bmp); //show image  
			}

		} else if (requestCode == PHOTO_REQUEST_CAREMA) {
			// ��������ص�����
			if (hasSdcard()) {
				try{//���û�����ջ�ȷ��ʱ�Ȳ�������
					Bundle bundle = data.getExtras();  
					Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ  
					saveBitmapToNative(bitmap);
					((ImageView) findViewById(R.id.showImg)).setImageBitmap(bitmap);// ��ͼƬ��ʾ��ImageView��
					Toast.makeText(this, fileName, Toast.LENGTH_LONG).show();
				}catch(Exception e){
					Intent intent=new Intent(GetPhotoActivity.this,SettingActivity.class);
					intent.putExtra("changeImg", "no");
					startActivity(intent);
					finish();
					e.printStackTrace();
				}
				//saveBitmap(bitmap);
			} else {
				Toast.makeText(GetPhotoActivity.this, "δ�ҵ��洢�����޷��洢��Ƭ��", 0).show();
			}

		} /*else if (requestCode == PHOTO_REQUEST_CUT) {
			// �Ӽ���ͼƬ���ص�����
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
			    showImg.setImageBitmap(bitmap);
			}
			try {
				// ����ʱ�ļ�ɾ��
				//�ϴ�����
				//yckx_tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}*/
		super.onActivityResult(requestCode, resultCode, data);
	}
	private static final String LOG_TAG = "yckx"; 

	/**��bitmap�洢������*/
	private void saveBitmapToNative(Bitmap bitmap){
		FileOutputStream b = null;  				  
		yckx_tempFile = new File("/sdcard/yckx/");  
		if(!yckx_tempFile.exists()){
			yckx_tempFile.mkdirs();// �����ļ���  
		}
		try {   
			b = new FileOutputStream(fileName); //д���洢�� 
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�  
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


	/**�ϴ�ͷ��*/
	@SuppressWarnings("unchecked")
	private void uploadHeadImg(){
		System.out.println("--->>uploadHeadImg()��ʼ�ϴ�����");
		new Thread(new Runnable() {
			@Override
			public void run() {

				File file=new File(fileName);
				if(file!=null){
					String token=PrefrenceConfig.getUserMessage(GetPhotoActivity.this).getUserToken();
					RequestParams params=new RequestParams();	
					params.addHeader("token",token);
					params.addBodyParameter("header",file);
					HttpUtils http=new HttpUtils();
					http.send(HttpMethod.POST, WEBInterface.UPLOADHEADURL, params, new RequestCallBack(){

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							System.out.println("---->>uploadHeadImg()�ϴ�ͷ��ʧ��");
							Message msg=new Message();
							msg.what=-1;
							mHandler.sendMessage(msg);
						}
						@Override
						public void onSuccess(ResponseInfo arg0) {
							Message msg=new Message();
							//��������
							String respose=(String) arg0.result;
							try {
								JSONObject jSONObject=new JSONObject(respose);
								String code=jSONObject.getString("code");
								if(code.equals("0")){
									JSONObject childJSONObject=jSONObject.getJSONObject("user");
									String userImgUrl=childJSONObject.getString("userImageUrl");
									UserEntity user=PrefrenceConfig.getUserMessage(GetPhotoActivity.this);
									user.setUserIamgeUrl(userImgUrl);
								    //���±����ļ�
									PrefrenceConfig.setLoginUserMessage(GetPhotoActivity.this, user);
									msg.what=0;
									mHandler.sendMessage(msg);
								}
								if(code.equals("2")){
									//tokenʧЧ
									msg.what=2;
									mHandler.sendMessage(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							System.out.println("--->>>uploadHeadImg()���ص�����"+respose);
							System.out.println("---->>uploadHeadImg()��������ɹ�ͷ�ɹ�");
						}});

				}

			}
		}).start();
	}

	
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==0){
				Intent intent=new Intent(GetPhotoActivity.this,SettingActivity.class);
				intent.putExtra("changeImg", "success");
				startActivity(intent);
				finish();
			}
			if(msg.what==2){
				//tokenʧЧ����ת����¼����
				ToastUtil.show(getApplicationContext(), "��¼״̬ʧЧ�������µ�¼", 5000);
				Intent intent_login=new Intent(GetPhotoActivity.this,LoginActivity.class);
				startActivity(intent_login);
				finish();
			}
			if(msg.what==-1){
				Intent intent=new Intent(GetPhotoActivity.this,SettingActivity.class);
				intent.putExtra("changeImg", "fail");
				startActivity(intent);
				finish();
			}
		};
	};
}
