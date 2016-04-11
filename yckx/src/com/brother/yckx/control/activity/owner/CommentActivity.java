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
 * 洗完车后用户进行评论
 * 
 * */
public class CommentActivity extends BaseActivity{

	private LinearLayout main_layout,layout_edittext;
	private ImageView[] starImages=new ImageView[5];//评论星级
	private ImageView[] showImages=new ImageView[3];;//显示用户上传的图片
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
		//获取毛玻璃图片
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

	/**第几颗星*/
	private boolean starSelects[]={false,false,false,false,false};
	/**洗护师分数*/
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

	/**当前点击获取图片按钮时获取的是第几张图片*/
	private int countOfGetPhotos=0;

	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			//---星级评论start----//
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
				//---星级评论end----//

				//弹出编辑键盘
			case R.id.layout_edittext:
				setEditTextKeyBoard(evaluate,main_layout);
				break;
		   //上传图片
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
				//发表评论
				sendEvaluation();
				break;
			case R.id.evaluate_return:
				finish();
				break;
				
			//---获取图片start---//
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
		    //---获取图片end-----//
			}

		}
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


	private Dialog takephotoDialog;
	//----代替popu窗口的dialog窗口----
	private void takePhonesDialog(){
		LayoutInflater inflater=getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		takephotoDialog = new AlertDialog.Builder(CommentActivity.this).create();
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
					countOfGetPhotos=0;
				}
				if (data != null) {
					// 得到图片的全路径
					Uri selectedImage = data.getData();  
					String[] filePathColumn = { MediaStore.Images.Media.DATA };  
					Cursor cursor = CommentActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
					cursor.moveToFirst();  
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
					String imgPath = cursor.getString(columnIndex);  
					cursor.close();  
					Bitmap bmp = BitmapFactory.decodeFile(imgPath); //将所选图片提取为bitmap格式  
					Log.d(LOG_TAG, imgPath);  
					Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
					Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
					//当前要保存的文件名
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
				// 从相机返回的数据
				if (hasSdcard()) {
					try{//相机没有拍照或确定时等不到数据
						Bundle bundle = data.getExtras();  
						Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
						//当前要保存的文件名
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
					Toast.makeText(CommentActivity.this, "未找到存储卡，无法存储照片！", 0).show();
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

		
	/**将获取到的图片展现出来*/
	private void preShowImage(ImageView showView,Bitmap bitmap){
		if(showView!=null&&bitmap!=null){
			showView.setImageBitmap(bitmap);
		}
	}
	
	
	/**发表评论*/
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
				Log.d("ycxk","打分"+washerScore);
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
						Log.d("yckx","用户评论订单"+arg1);
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
				//评论成功
				ToastUtil.show(getApplicationContext(), "评论成功", 2000);
				CommentActivity.this.finish();
				break;
			case EVALUATION_FAILED:
				ToastUtil.show(getApplicationContext(), "评论失败:请给洗护师打分,并填写评论内容", 2000);
				break;
			case EVALUATION_TIMEOUT:
				ToastUtil.show(getApplicationContext(), "订单评论已过时", 2000);
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
