package com.brother.yckx.control.activity.owner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brother.BaseActivity;
import com.brother.utils.MyBitmapUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
/**
 * ϴ�공���û���������
 * 
 * */
public class CommentActivity extends BaseActivity{

	private LinearLayout main_layout,layout_edittext;
	private ImageView[] starImages=new ImageView[5];//�����Ǽ�
	private ImageView[] showImages=new ImageView[3];;//��ʾ�û��ϴ���ͼƬ
	private EditText evaluate;
	private TextView evaluate_ok,evaluate_return;

	
	/*private OrderListEntity orderListEntity;
	private OrderDetailEntity OrderDetailEntity;*/
	private String mOrderId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_comment);
		//setActionBar(R.string.evaluationTitle, R.drawable.btn_homeasup_default, NULL_ID, listener);
		mOrderId=getIntent().getStringExtra("CommentActivity");
		init();
	}

	private void init() {
		main_layout=(LinearLayout) findViewById(R.id.main_layout);
		layout_edittext=(LinearLayout) findViewById(R.id.layout_edittext);
		layout_edittext.setOnClickListener(listener);
		//��ȡë����ͼƬ
		Bitmap bitmap=MyBitmapUtils.getBitmapFromSDCard("/sdcard/yckx/yckx_temp.png");
		if(bitmap!=null){
			bitmap=MyBitmapUtils.fastblur(bitmap, 10);
		}
        ImageView gaosiImageView=(ImageView) findViewById(R.id.gaosi);
        if(bitmap!=null){
        	gaosiImageView.setImageBitmap(bitmap);
        }
		starImages[0]=(ImageView) findViewById(R.id.starimg1);
		starImages[1]=(ImageView) findViewById(R.id.starimg2);
		starImages[2]=(ImageView) findViewById(R.id.starimg3);
		starImages[3]=(ImageView) findViewById(R.id.starimg4);
		starImages[4]=(ImageView) findViewById(R.id.starimg5);

		showImages[0]=(ImageView) findViewById(R.id.getphotos1);
		showImages[1]=(ImageView) findViewById(R.id.getphotos2);
		showImages[2]=(ImageView) findViewById(R.id.getphotos3);
		for(int i=0;i<3;i++){
			showImages[i].setOnClickListener(listener);
		}

		evaluate=(EditText) findViewById(R.id.evaluate);
		//imgGetphotos=(ImageView) findViewById(R.id.getphotos);

		evaluate_ok=(TextView) findViewById(R.id.evaluate_ok);
		evaluate_return=(TextView) findViewById(R.id.evaluate_return);
		for(int i=0;i<5;i++){
			starImages[i].setOnClickListener(listener);
		}
		evaluate_ok.setOnClickListener(listener);
		evaluate_return.setOnClickListener(listener);
	}

	/**�ڼ�����*/
	private boolean starSelects[]={false,false,false,false,false};
	/**ϴ��ʦ����*/
	private int washerScore=0;
	private void setStartSelect(int index){
		if(starSelects[index]){
			starSelects[index]=false;
		}else{
			starSelects[index]=true;
		}
		if(starSelects[index]){
			starImages[index].setBackgroundResource(R.drawable.icon_evaluate_selected);
		}else{
			starImages[index].setBackgroundResource(R.drawable.icon_evaluate_normal);
		}
	}

	/**��ǰ�����ȡͼƬ��ťʱ��ȡ���ǵڼ���ͼƬ*/
	private int countOfGetPhotos=0;

	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			//---�Ǽ�����start----//
			case R.id.starimg1:
				setStartSelect(0);
				break;
			case R.id.starimg2:
				setStartSelect(1);
				break;
			case R.id.starimg3:
				setStartSelect(2);
				break;
			case R.id.starimg4:
				setStartSelect(3);
				break;
			case R.id.starimg5:
				setStartSelect(4);
				break;
				//---�Ǽ�����end----//

				//�����༭����
			case R.id.layout_edittext:
				setEditTextKeyBoard(evaluate,main_layout);
				break;
		   //�ϴ�ͼƬ
			case R.id.getphotos1:
				countOfGetPhotos=1;
				takePhonesDialog();
				break;
			case R.id.getphotos2:
				countOfGetPhotos=2;
				takePhonesDialog();
				break; 
			case R.id.getphotos3:
				countOfGetPhotos=3;
				takePhonesDialog();
				break;
			case R.id.evaluate_ok:
				//��������
				sendEvaluation();
				break;
			case R.id.evaluate_return:
				finish();
				break;
				
			//---��ȡͼƬstart---//
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
		    //---��ȡͼƬend-----//
			}

		}
	};

	/**
	 * EditText֧���ֶ�����ļ�������
	 * @param ev_target
	 *        Ҫ������EditText
	 * @param layout
	 *        ev_target���ڵĲ����е������layout(LinearLayout,RelayoutLayout,FrameLayout)
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
						{ // ˵�������ǵ���״̬
							//ToastUtil.show(getApplicationContext(), "����", 2000);
						} else{
							ev_target.setInputType(InputType.TYPE_NULL);
							ev_target.setFocusableInTouchMode(false);
							//ToastUtil.show(getApplicationContext(), "����", 2000);
						}
					}
				});
	}


	private Dialog takephotoDialog;
	//----����popu���ڵ�dialog����----
	private void takePhonesDialog(){
		LayoutInflater inflater=getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		takephotoDialog = new AlertDialog.Builder(CommentActivity.this).create();
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
					countOfGetPhotos=0;
				}
				if (data != null) {
					// �õ�ͼƬ��ȫ·��
					Uri selectedImage = data.getData();  
					String[] filePathColumn = { MediaStore.Images.Media.DATA };  
					Cursor cursor = CommentActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
					cursor.moveToFirst();  
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
					String imgPath = cursor.getString(columnIndex);  
					cursor.close();  
					Bitmap bmp = BitmapFactory.decodeFile(imgPath); //����ѡͼƬ��ȡΪbitmap��ʽ  
					Log.d(LOG_TAG, imgPath);  
					Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
					Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
					//��ǰҪ������ļ���
					if(countOfGetPhotos==1){
						saveBitmapToNative(bmp,fileName1);
						preShowImage(showImages[0], bmp);
					}
					if(countOfGetPhotos==2){
						saveBitmapToNative(bmp,fileName2);
						preShowImage(showImages[1], bmp);
					}
					if(countOfGetPhotos==3){
						saveBitmapToNative(bmp,fileName3);
						preShowImage(showImages[2], bmp);
					}
				}

			} else if (requestCode == PHOTO_REQUEST_CAREMA) {
				// ��������ص�����
				if (hasSdcard()) {
					try{//���û�����ջ�ȷ��ʱ�Ȳ�������
						Bundle bundle = data.getExtras();  
						Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ  
						//��ǰҪ������ļ���
						if(countOfGetPhotos==1){
							saveBitmapToNative(bitmap,fileName1);
							preShowImage(showImages[0], bitmap);
						}
						if(countOfGetPhotos==2){
							saveBitmapToNative(bitmap,fileName2);
							preShowImage(showImages[1], bitmap);
						}
						if(countOfGetPhotos==3){
							saveBitmapToNative(bitmap,fileName3);
							preShowImage(showImages[2], bitmap);
						}
					}catch(Exception e){
					}
				} else {
					countOfGetPhotos--;
					Toast.makeText(CommentActivity.this, "δ�ҵ��洢�����޷��洢��Ƭ��", 0).show();
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

		
	/**����ȡ����ͼƬչ�ֳ���*/
	private void preShowImage(ImageView showView,Bitmap bitmap){
		if(showView!=null&&bitmap!=null){
			showView.setImageBitmap(bitmap);
		}
	}
	
	
	/**��������*/
    private void sendEvaluation(){
    	new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(CommentActivity.this).getUserToken();
				String content=evaluate.getText().toString().trim();
				washerScore=0;
				for(int i=0;i<starSelects.length;i++){
					if(starSelects[i]){
						washerScore++;
					}
				}
				Log.d("ycxk","���"+washerScore);
				String orderId=mOrderId;
				RequestParams params=new RequestParams();	
				params.addHeader("token",token);
				params.addBodyParameter("content", content);
				params.addBodyParameter("score", washerScore+"");
				params.addBodyParameter("orderId", orderId);
				if(fileExist(fileName1)){
					File fileImg1=new File(fileName1);
					if(fileImg1!=null){
						params.addBodyParameter("img1",fileImg1);
					}
				}
				if(fileExist(fileName2)){
					File fileImg2=new File(fileName2);
					if(fileImg2!=null){
						params.addBodyParameter("img2",fileImg2);
					}
				}
				if(fileExist(fileName3)){
					File fileImg1=new File(fileName3);
					if(fileImg1!=null){
						params.addBodyParameter("img3",fileImg1);
					}
				}
				HttpUtils http=new HttpUtils();
				http.send(HttpMethod.POST, WEBInterface.USERCOMMENT, params, new RequestCallBack<Object>(){

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.d("yckx","�û����۶���"+arg1);
						Message msg=new Message();
						msg.what=EVALUATION_FAILED;
						msg.obj=arg1;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onSuccess(@SuppressWarnings("rawtypes") ResponseInfo arg0) {
						String respose=(String) arg0.result;
						Log.d("yckx", respose+"");
						try {
							JSONObject jSONObject=new JSONObject(respose);
							String code=jSONObject.getString("code");
							if(code.equals("0")){
								Message msg=new Message();
								msg.what=EVALUATION_SUCCESS;
								mHandler.sendMessage(msg);
							}else if(code.equals(EVALUATION_TIMEOUT+"")){
								Message msg=new Message();
								msg.what=EVALUATION_TIMEOUT;
								mHandler.sendMessage(msg);
							}else if(code.equals("2")){
								Message msg=new Message();
								msg.what=EVALUATION_TIMEOUT;
								mHandler.sendMessage(msg);
							}else{
								Message msg=new Message();
								msg.what=UNKOWN_ERROR;
								msg.obj=respose;
								mHandler.sendMessage(msg);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}});
			
			}
		}).start();
    }
    
    
    private boolean fileExist(String fileName){
		File file=new File(fileName);
		try {
			if(file.exists()){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
    
    private final int EVALUATION_SUCCESS=1;
    private final int EVALUATION_FAILED=401;
    private final int EVALUATION_TIMEOUT=1012000;
    private final int TOKEN_ERROR=2;
    private final int UNKOWN_ERROR=10000;
    
    private Handler mHandler=new Handler(){
    	@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
    		switch (msg.what) {
			case EVALUATION_SUCCESS:
				//���۳ɹ�
				ToastUtil.show(getApplicationContext(), "���۳ɹ�", 2000);
				CommentActivity.this.finish();
				break;
			case EVALUATION_FAILED:
				ToastUtil.show(getApplicationContext(), "����ʧ��:���ϴ��ʦ���,����д��������", 2000);
				break;
			case EVALUATION_TIMEOUT:
				ToastUtil.show(getApplicationContext(), "���������ѹ�ʱ", 2000);
				break;
			case TOKEN_ERROR:
				Intent intent_Login=new Intent(CommentActivity.this,LoginActivity.class);
				startActivity(intent_Login);
				finish();
				break;
			case UNKOWN_ERROR:
				ToastUtil.show(getApplicationContext(), msg.obj.toString(), 2000);
				break;
			}
    	};
    };
}
