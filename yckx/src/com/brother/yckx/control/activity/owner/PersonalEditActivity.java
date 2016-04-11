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
		
		//����ͷ��
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
			case R.id.layout_headImg://����һ������
				takePhonesDialog();
				break;
			case R.id.layout_nickname:
				Intent intentNick=new Intent(PersonalEditActivity.this,EditNicknameActivity.class);
				startActivityForResult(intentNick, NICKNAMEFROMNEXT);
				break;
			case R.id.layout_sex://����һ������
				showSexDialog();
				break;
			case R.id.layout_age://����һ������
				showAgeDialog();
				break;
			case R.id.layout_sign:
				Intent intentSign=new Intent(PersonalEditActivity.this,SignActivity.class);
				startActivityForResult(intentSign, SIGNROMNEXT);
				break;
			case R.id.layout_hangye:
				break;
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

	private Dialog takephotoDialog;
	//----����popu���ڵ�dialog����----
	private void takePhonesDialog(){
		LayoutInflater inflater=PersonalEditActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		takephotoDialog = new AlertDialog.Builder(PersonalEditActivity.this).create();
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


	//private File yckx_tempFile;
	private File headImgFile;
	/**��ͼƬ���浽����*/
	/**��bitmap�洢������*/
	@SuppressLint("SdCardPath") 
	private void saveHeadBitmapToNative(String fileName,Bitmap bitmap){
		FileOutputStream b = null;  				  
		yckx_tempFile = new File("/sdcard/yckx/");  
		if(!yckx_tempFile.exists()){
			yckx_tempFile.mkdirs();// �����ļ���  
		}
		headImgFile=new File("/sdcard/yckx/headimg/");
		if(!headImgFile.exists()){
			headImgFile.mkdirs();
		}
		try {   
			b = new FileOutputStream(fileName); //д���洢�� 
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�  
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
	
	/**��ӳɹ�����ˢ��ҳ��*/
	private final int NICKNAMEFROMNEXT=3;
	private final int SIGNROMNEXT=4;
	@SuppressLint("SdCardPath") @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//�����Ƿ�������һ��ҳ�淵����������
		if(resultCode==NICKNAMEFROMNEXT){
			String result =data.getStringExtra("nickName").trim();//�õ���Activity�رպ󷵻ص�����  
			Log.d("yckx", "�������:"+result.length());
			tv_nickname.setText(result);
		}
		if(resultCode==SIGNROMNEXT){
			String result =data.getStringExtra("sign").trim();//�õ���Activity�رպ󷵻ص�����  
			Log.d("yckx", "�������:"+result.length());
			tv_sign.setText(result);
		}
		
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
				Cursor cursor = PersonalEditActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
				cursor.moveToFirst();  
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
				String imgPath = cursor.getString(columnIndex);  
				cursor.close();  
				Bitmap bmp = BitmapFactory.decodeFile(imgPath); //����ѡͼƬ��ȡΪbitmap��ʽ  
				Log.d(LOG_TAG, imgPath);  
				Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
				Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
				saveBitmapToNative(bmp);
				heandImg.setCircleImageBitmap(bmp);
			}

		} else if (requestCode == PHOTO_REQUEST_CAREMA) {
			// ��������ص�����
			if (hasSdcard()) {
				try{//���û�����ջ�ȷ��ʱ�Ȳ�������
					Bundle bundle = data.getExtras();  
					Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ  
					saveBitmapToNative(bitmap);
					//(bitmap);
					heandImg.setCircleImageBitmap(bitmap);
				}catch(Exception e){
				}
			} else {
				Toast.makeText(PersonalEditActivity.this, "δ�ҵ��洢�����޷��洢��Ƭ��", 0).show();
			}
		} 
		super.onActivityResult(requestCode, resultCode, data);
	}
	private static final String LOG_TAG = "yckx"; 

	/**��bitmap�洢������*/
	@SuppressLint("SdCardPath")
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
	//------------����������end-----------------------//


	//----------------�Ա�  start-----------------------//
    private WheelView wheelview;
	private Dialog sexDialog;
	private String userSex="��";
	private static final String[] PLANETS = new String[]{"��", "Ů"};
	//----����popu���ڵ�dialog����----
	private void showSexDialog(){
		LayoutInflater inflater=PersonalEditActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_sex_select, null);
		sexDialog = new AlertDialog.Builder(PersonalEditActivity.this).create();
		sexDialog.show();
		sexDialog.setContentView(view);
		WindowManager m =getWindowManager();
		Window dialogWindow = sexDialog.getWindow();
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ����
		WindowManager.LayoutParams params = sexDialog.getWindow().getAttributes();
		params.width = (int) (d.getWidth());
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//����bottom��ƫ����
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
	//----------------�Ա�  end-----------------------//
	
	//----------------�Ա�  start-----------------------//
    private WheelView wheelview_age;
	private Dialog ageDialog;
	private String userAge="90��";
	private static final String[] PLANETS_AGE = new String[]{"00��","90��","80��","70��","60��","50��"};
	//----����popu���ڵ�dialog����----
	private void showAgeDialog(){
		LayoutInflater inflater=PersonalEditActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_sex_select, null);
		ageDialog = new AlertDialog.Builder(PersonalEditActivity.this).create();
		ageDialog.show();
		ageDialog.setContentView(view);
		WindowManager m =getWindowManager();
		Window dialogWindow = ageDialog.getWindow();
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ����
		WindowManager.LayoutParams params = ageDialog.getWindow().getAttributes();
		params.width = (int) (d.getWidth());
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//����bottom��ƫ����
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
	//----------------�Ա�  end-----------------------//
	
	
}
