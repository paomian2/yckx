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
	/**����ͷ��ɹ�*/
	private final int LOADIMG_SUCCESS=10;
	/**�ϴ�ͷ��ɹ�*/
	private final int UPLOADNIMG_SUCCESS=0;
	/**�ϴ�ͷ��ʧ��*/
	private final int UPLOADIMG_FAILED=40;
	/**tokenʧЧ*/
	private final int TOKEN_RORRO=2;
	/**�޸�����ɹ�*/
	private final int MODIFY_PWD_SUCCESS=20;
	/**ԭʼ�������*/
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
		//���ü���
		userName_layout.setOnClickListener(listener);
		modify_pwd_layout.setOnClickListener(listener);
		user_agreement_layout.setOnClickListener(listener);
		newuser_lead_layout.setOnClickListener(listener);
		CustomerService_layout.setOnClickListener(listener);
		exit_layout.setOnClickListener(listener);
		//����ͷ����û���
		setImgAndUserNameInbackGround();
	}

	/**����ͷ����û���,�û������(��ʱ������)*/
	private void setImgAndUserNameInbackGround(){
		UserEntity mUserEntity=PrefrenceConfig.getUserMessage(SettingActivity.this);
		String name=mUserEntity.getUserName();
		String phone=mUserEntity.getUserPhone();
		userName.setText(name);
		if(!name.equals(phone)){
			newfunction.setText(phone);
		}else{
			//��ʾ�û����(δ��ͨ)
		}
		
	    String imgUrl=WEBInterface.INDEXIMGURL+PrefrenceConfig.getUserMessage(SettingActivity.this).getUserIamgeUrl();
		touxiang.setImageUrl(imgUrl);
		
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				String imgUrl=WEBInterface.INDEXIMGURL+PrefrenceConfig.getUserMessage(SettingActivity.this).getUserIamgeUrl();
				Log.d("yckx", "����ͷ��ĵ�ַ"+imgUrl);
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


	/**����UI��*/
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOADIMG_SUCCESS://����ͷ��ɹ�
				Bitmap bitmap=(Bitmap) msg.obj;
				if (bitmap!=null) {
					touxiang.setCircleImageBitmap(bitmap);
				}break;
			case UPLOADIMG_FAILED://�ϴ�ʧ��
				if(mPreShowPhotoDialog!=null){
					mPreShowPhotoDialog.dismiss();
				}
				break;
			case UPLOADNIMG_SUCCESS://�ϴ��ɹ�����������ͷ��
				setImgAndUserNameInbackGround();
				if(mPreShowPhotoDialog!=null){
					mPreShowPhotoDialog.dismiss();
				}
				break;
			case TOKEN_RORRO://��¼״̬ʧЧ
				Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
				startActivity(intent);
				break;
			case MODIFY_PWD_SUCCESS://�޸�����ɹ�
				if(modifyDialog!=null){
					modifyDialog.dismiss();
					ToastUtil.show(getApplicationContext(), "�޸ĳɹ�",2000);
				}
			case OLDPWD_ERROR://ԭʼ�������
				ToastUtil.show(getApplicationContext(), "����ԭʼ�������", 2000);
				if(btn_ok!=null){
					btn_ok.setClickable(true);
				}
			default:
				break;
			}
		};
	};

	private Dialog takephotoDialog;
	//----����popu���ڵ�dialog����----
	private void takePhonesDialog(){
		LayoutInflater inflater=SettingActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		takephotoDialog = new AlertDialog.Builder(SettingActivity.this).create();
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

	private int getWith(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width=dm.widthPixels;
		System.out.println("---->>��Ļ���"+width);
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
			case R.id.userName_layout://�û���
				takePhonesDialog();
				break;
			case R.id.modify_pwd_layout://�����޸�����
				resetPwdOnline();
				break;
			case R.id.user_agreement_layout://�û�ʹ��Э��
				GlobalServiceUtils.setGloubalServiceSession("WebViewActivity","useAgreement");
				Intent intent=new Intent(SettingActivity.this,WebViewActivity.class);
				startActivity(intent);
				break;
			case R.id.newuser_lead_layout://������������
				break;
			case R.id.CustomerService_layout://�ͷ���ѯ
				String phoneNo="4008793689";
				Intent intentPhone = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				SettingActivity.this.startActivity(intentPhone);
				break;
			case R.id.layout_exit://�˳���¼
				//����û���Ϣ���Ÿ롢����ע��
				existLogin();
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
				Cursor cursor = SettingActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
				cursor.moveToFirst();  
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
				String imgPath = cursor.getString(columnIndex);  
				cursor.close();  
				Bitmap bmp = BitmapFactory.decodeFile(imgPath); //����ѡͼƬ��ȡΪbitmap��ʽ  
				Log.d(LOG_TAG, imgPath);  
				Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
				Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
				saveBitmapToNative(bmp);
				preShowDialog(bmp);
			}

		} else if (requestCode == PHOTO_REQUEST_CAREMA) {
			// ��������ص�����
			if (hasSdcard()) {
				try{//���û�����ջ�ȷ��ʱ�Ȳ�������
					Bundle bundle = data.getExtras();  
					Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ  
					saveBitmapToNative(bitmap);
					preShowDialog(bitmap);
				}catch(Exception e){
				}
			} else {
				Toast.makeText(SettingActivity.this, "δ�ҵ��洢�����޷��洢��Ƭ��", 0).show();
			}
		} 
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


	//------------����������end-----------------------//


	//-----------��ȡ��ͼƬ֮�����Ԥ��start--------------//

	private Dialog mPreShowPhotoDialog;
	//----����popu���ڵ�dialog����----
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
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ����
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		//ƫ����
		params.y=30;
		mPreShowPhotoDialog.getWindow().setAttributes(params);
		CircleImageView preShowImg=(CircleImageView) view.findViewById(R.id.preShowImg);
		preShowImg.setCircleImageBitmap(bitmap);
		final TextView uploadOk=(TextView) view.findViewById(R.id.uploadOk);
		uploadOk.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//�ϴ�ͷ��//���ð�ť������
				uploadOk.setText("�����ϴ�...");
				uploadOk.setClickable(false);
				uploadHeadImg();

			}});
		view.findViewById(R.id.uploadCancel).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				mPreShowPhotoDialog.dismiss();
			}});
	}

	//-----------��ȡ��ͼƬ֮�����Ԥ��end--------------//


	/**�ϴ�ͷ��*/
	@SuppressWarnings("unchecked")
	private void uploadHeadImg(){
		System.out.println("--->>uploadHeadImg()��ʼ�ϴ�����");
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
							System.out.println("---->>uploadHeadImg()�ϴ�ͷ��ʧ��");
							Message msg=new Message();
							msg.what=UPLOADIMG_FAILED;
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
									UserEntity user=PrefrenceConfig.getUserMessage(SettingActivity.this);
									user.setUserIamgeUrl(userImgUrl);
									//���±����ļ�
									PrefrenceConfig.setLoginUserMessage(SettingActivity.this, user);
									msg.what=UPLOADNIMG_SUCCESS;
									mHandler.sendMessage(msg);
								}
								if(code.equals("2")){
									//tokenʧЧ
									msg.what=TOKEN_RORRO;
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
	
	private Dialog modifyDialog;
	private Button btn_ok;//ȷ���޸�����
	/**�û������޸�����*/
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
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ����
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		//ƫ����
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
					ToastUtil.show(getApplicationContext(), "���벻��Ϊ��", 2000);
					return;
				}
				if(oldPwd.equals(newPwd)){
					btn_ok.setClickable(true);
					ToastUtil.show(getApplicationContext(), "�¾����벻��һ��", 2000);
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
						//�޸�����ɹ�
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

	/**�û��˳���¼״̬*/
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
