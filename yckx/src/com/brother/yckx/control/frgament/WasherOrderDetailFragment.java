package com.brother.yckx.control.frgament;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.MyBitmapUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.TimeUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.washer.WasherOderDetailActivity;
import com.brother.yckx.control.activity.washer.WasherOderDetailActivity.OnPageChangeListener;
import com.brother.yckx.model.OrderDetailEntity;
import com.brother.yckx.model.OrderListEntity;
import com.brother.yckx.model.db.AssetsDBManager;
import com.brother.yckx.model.db.DBRead;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shizhefei.fragment.LazyFragment;

@SuppressLint({ "ResourceAsColor", "CutPasteId" }) public class WasherOrderDetailFragment extends LazyFragment implements RongIM.UserInfoProvider{
	private int tabIndex;
	public static final String INTENT_INT_INDEX = "intent_int_index";
	private OrderListEntity orderListEntity;
	private String orderId="";
	//用户监听页面改变
	private WasherOderDetailActivity washerOrderDetailActivity;
	private int currenPage=0;
	//标注页面切换的时候是否要重新加载数据
	private boolean isReFreshOrder;

	//--------洗护师处理
	private RelativeLayout tv_phone;
	private RelativeLayout tv_yuyin;
	private RelativeLayout tv_car_location;
	private RelativeLayout tv_notice_arrive;
	private RelativeLayout tv_washing;
	private RelativeLayout tv_washe_finish;
	private RelativeLayout tv_greeto_cancle;
	private TextView btn_load;
	private ImageView img1,img2,img3;
	/**洗护师操作成功*/
	private final int OPERATORSUCCESS=0;
	/**上传图片成功*/
	private final int UPLOADIMG_SUCCESS=10;
	/**上传图片失败*/
	private final int UPLOADIMG_FAILD=-1;
	/**重新订单详情成功*/
    private final int RELOAD_ORDER_SUCCESS=100;
	
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		tabIndex = getArguments().getInt(INTENT_INT_INDEX);
		washerOrderDetailActivity=(WasherOderDetailActivity) getActivity();
		washerOrderDetailActivity.setOnPageChangeListener(new OnPageChangeListener(){
			@Override
			public void onPagechane(int currentPage) {
				if(currenPage==1){
					isReFreshOrder=false;
				}
				currenPage=currentPage;
				//当前页面是订单详情，重置点击状态
                if(isReFreshOrder){
                	//订单详情页面重新加载数据
                	reloadOrderEntity(orderId);
                }
			}});
		switch (tabIndex) {
		case 0:
			setContentView(R.layout.fragment_washerorder_detail_first);
			selectTab1();
			break;
		case 1:
			selectTab2();
			break;
		}
	}
	private String mPhone;
	/**美护师处理(已抢订单里面进入此界面)*/
	private void selectTab1(){
		mPhone=PrefrenceConfig.getUserMessage(getActivity()).getUserPhone();
		//"已抢订单页面进入"
		orderListEntity=GlobalServiceUtils.getInstance().getGloubalServiceOrderListEntity();
		Log.d("yckx","当前订单:"+orderListEntity.toString());
		mPhone=orderListEntity.getPhone();
		orderId=orderListEntity.getId();
		//设置订单状态
		setTab1UI();
		//test---
		Log.d("yckx",""+orderListEntity.getId()+"--"+orderListEntity.getOrderNum()+orderListEntity.getOrderStatus());
		//"历史订单"页面进入
		orderId=orderListEntity.getId();
		Log.d("yckx",""+orderListEntity.getId()+"--"+orderListEntity.getOrderNum()+orderListEntity.getOrderStatus());

		tv_phone=(RelativeLayout)findViewById(R.id.tv_phone);
		//电话拨打
		tv_phone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String phoneNo=mPhone;
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				getActivity().startActivity(intent);
				isWasherPhoneOwn=true;
				//电话
				if(isWasherPhoneOwn){
					Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_contact);
					ImageView imgLeft=(ImageView) findViewById(R.id.imgPhone);
					imgLeft.setImageBitmap(bitmapLeft);
				}
			}});
		//语音
		tv_yuyin=(RelativeLayout)findViewById(R.id.tv_yuyin);
		tv_yuyin.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				isWasherTalkOwn=true;
				//存储当前联系人
				currentChatObj=orderListEntity;
				saveRongIMUser2DB(orderListEntity);
				RongIM.getInstance().startPrivateChat(getActivity(), orderListEntity.getWasherEntity().getWaherId(), "title");
				//语音
				isWasherTalkOwn=true;
				if(isWasherTalkOwn){
					Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_talk);
					ImageView imgLeft=(ImageView) findViewById(R.id.imgyuyin);
					imgLeft.setImageBitmap(bitmapLeft);
				}
			}});
		//车辆定位
		tv_car_location=(RelativeLayout)findViewById(R.id.tv_car_location);
		tv_car_location.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				RongIM.getInstance().startPrivateChat(getActivity(), orderListEntity.getWasherEntity().getWaherId(), "title");
				isWasherLocationOwn=true;
				//定位
				if(isWasherLocationOwn){
					Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_locate);
					ImageView imgLeft=(ImageView) findViewById(R.id.imgcar);
					imgLeft.setImageBitmap(bitmapLeft);
				}
			}});
		//通知到达
		tv_notice_arrive=(RelativeLayout)findViewById(R.id.tv_notice_arrive);
		tv_notice_arrive.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String url=WEBInterface.NOTICEUSER_ARRIVE+orderId;
				washerOperation(url,R.id.tv_notice_arrive);
			}});
		img1=(ImageView)findViewById(R.id.img1);
		img1.setOnClickListener(new loadImgListenr(R.id.img1));
		img1.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				img1.setImageResource(R.drawable.icon_image_toadd);
				//删除图片
				delFile(saveImgName1);
				return false;
			}});
		img2=(ImageView)findViewById(R.id.img2);
		img2.setOnClickListener(new loadImgListenr(R.id.img2));
		img2.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				img2.setImageResource(R.drawable.icon_image_toadd);
				//删除图片
				delFile(saveImageName2);
				return false;
			}});
		img3=(ImageView)findViewById(R.id.img3);
		img3.setOnClickListener(new loadImgListenr(R.id.img3));
		img3.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				img3.setImageResource(R.drawable.icon_image_toadd);
				delFile(saveImageName3);
				//删除图片
				return false;
			}});
		//上传图片(检查车辆)
		btn_load=(TextView)findViewById(R.id.btn_load);
		btn_load.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//上传图片
				uploadImg();
			}});

		//正在洗车
		tv_washing=(RelativeLayout)findViewById(R.id.tv_washing);
		tv_washing.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String url=WEBInterface.NOTICEWASHERINGCAR_URL+orderId;
				washerOperation(url,R.id.tv_washing);
			}});
		//洗车完毕
		tv_washe_finish=(RelativeLayout)findViewById(R.id.tv_washe_finish);
		tv_washe_finish.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String url=WEBInterface.NOTICEWAHERED_URL+orderId;
				washerOperation(url,R.id.tv_washe_finish);
			}});
		//用户取消
		tv_greeto_cancle=(RelativeLayout)findViewById(R.id.tv_greeto_cancle);
		tv_greeto_cancle.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String orderId=orderListEntity.getId();
				String url=WEBInterface.ORDER_CANCEL_URL+"/"+orderId;
				washerOperation(url,R.id.tv_greeto_cancle);
			}});
	};


	private boolean isWasherPhoneOwn=false;
	private boolean isWasherTalkOwn=false;
	private boolean isWasherLocationOwn=false;
	/**根据订单状态设置ui*/
	private void setTab1UI(){
		//电话
		if(isWasherPhoneOwn){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_contact);
			ImageView imgLeft=(ImageView) findViewById(R.id.imgPhone);
			imgLeft.setImageBitmap(bitmapLeft);
		}
		//语音
		if(isWasherTalkOwn){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_talk);
			ImageView imgLeft=(ImageView) findViewById(R.id.imgyuyin);
			imgLeft.setImageBitmap(bitmapLeft);
		}
		//定位
		if(isWasherLocationOwn){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_locate);
			ImageView imgLeft=(ImageView) findViewById(R.id.imgcar);
			imgLeft.setImageBitmap(bitmapLeft);
		}
		//通知用户我已到达
		if(orderListEntity.getOrderSubEntity()!=null&&orderListEntity.getOrderSubEntity().getSubStatus().equals("arrived")){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_inlocation);
			ImageView imgLeft=(ImageView) findViewById(R.id.imgarrive);
			imgLeft.setImageBitmap(bitmapLeft);
		}
		//正在上传图片
		//正在清洗
		if(orderListEntity.getOrderSubEntity()!=null&&orderListEntity.getOrderSubEntity().getSubStatus().equals("washing")){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_washing);
			ImageView imgLeft=(ImageView) findViewById(R.id.imgwashing);
			imgLeft.setImageBitmap(bitmapLeft);
			Bitmap bitmapRight_washing=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_donging);
			ImageView imgRight_washing=(ImageView) findViewById(R.id.imgRight_washing);
			imgRight_washing.setImageBitmap(bitmapRight_washing);

		}
		//通知用户已经清洗完成
		if(orderListEntity.getOrderSubEntity()!=null&&orderListEntity.getOrderSubEntity().getSubStatus().equals("washed")&&orderListEntity.getOrderStatus().equals("finish")){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_complete);
			ImageView imgLeft=(ImageView) findViewById(R.id.imgwashing);
			imgLeft.setImageBitmap(bitmapLeft);
			Bitmap bitmapRight_finish=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_donging);
			ImageView imgRight_finish=(ImageView) findViewById(R.id.imgRight_washing);
			imgRight_finish.setImageBitmap(bitmapRight_finish);

			Bitmap bitmapRight_washing=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.bg_empty);
			ImageView imgRight_washing=(ImageView) findViewById(R.id.imgRight_washing);
			imgRight_washing.setImageBitmap(bitmapRight_washing);
		}
		//用户同意取消订单
		if(orderListEntity.getOrderStatus().equals("cancle")){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_cancel);
			ImageView imgLeft=(ImageView) findViewById(R.id.imwash_agreento_cancel);
			imgLeft.setImageBitmap(bitmapLeft);
			Bitmap bitmapRight_cancel=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_donging);
			ImageView imgRight_cancel=(ImageView) findViewById(R.id.imgRight_washing);
			imgRight_cancel.setImageBitmap(bitmapRight_cancel);
		}
	}

	private Handler mHandler=new Handler(){
		@SuppressLint("ResourceAsColor") 
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case OPERATORSUCCESS:
				//判断是哪一个操作
			{
				switch (msg.arg1) {
				case R.id.tv_notice_arrive:
					//通知用户我已经到达(按钮设置为不可点击)
					//ToastUtil.show(getApplicationContext(), "已通知用户我已经到达", 2000);
					//通知用户我已到达
					Bitmap bitmapLeft_arrive=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_inlocation);
					ImageView imgLeft_arrive=(ImageView) findViewById(R.id.imgarrive);
					imgLeft_arrive.setImageBitmap(bitmapLeft_arrive);
					//字体颜色修改
					TextView car_tv=(TextView) findViewById(R.id.arrive_tv);
					car_tv.setTextColor(R.color.gray);
					isReFreshOrder=true;
					Log.d("yckx", "通知用户已到达成功");
					break;
				case R.id.tv_washing:
					//我正在洗车
					setOperatorColor(tv_washing);
					//正在清洗
					//
					Bitmap bitmapLeft_washing=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_washing);
					ImageView imgLeft_washing=(ImageView) findViewById(R.id.imgwashing);
					imgLeft_washing.setImageBitmap(bitmapLeft_washing);
					Bitmap bitmapRight_washing=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_donging);
					ImageView imgRight_washing=(ImageView) findViewById(R.id.imgRight_washing);
					imgRight_washing.setImageBitmap(bitmapRight_washing);
					//字体颜色修改
					TextView car_tv_arrive=(TextView) findViewById(R.id.arrive_tv);
					car_tv_arrive.setTextColor(R.color.red);
					TextView washing_tv=(TextView) findViewById(R.id.washing_tv);
					washing_tv.setTextColor(R.color.gray);
					isReFreshOrder=true;
					Log.d("yckx", "通知用户正在洗车成功");
					break;
				case R.id.tv_washe_finish:
					//洗车完毕
					setOperatorColor(tv_washe_finish);
					//通知用户已经清洗完成
					Bitmap bitmapLeft_finish=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_complete);
					ImageView imgLeft_finish=(ImageView) findViewById(R.id.imgwashing);
					imgLeft_finish.setImageBitmap(bitmapLeft_finish);
					Bitmap bitmapRight_finish=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_donging);
					ImageView imgRight_finish=(ImageView) findViewById(R.id.imgRight_washing);
					imgRight_finish.setImageBitmap(bitmapRight_finish);

					Bitmap bitmapRight_washing2=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.bg_empty);
					ImageView imgRight_washing2=(ImageView) findViewById(R.id.imgRight_washing);
					imgRight_washing2.setImageBitmap(bitmapRight_washing2);
					//字体颜色修改
					TextView car_tv_arrive2=(TextView) findViewById(R.id.arrive_tv);
					car_tv_arrive2.setTextColor(R.color.red);
					TextView washing_tv2=(TextView) findViewById(R.id.washing_tv);
					washing_tv2.setTextColor(R.color.red);
					TextView finish_tv=(TextView) findViewById(R.id.finish_tv);
					finish_tv.setTextColor(R.color.gray);
					isReFreshOrder=true;
					Log.d("yckx", "通知用户洗车完成成功");
					break;
				case R.id.tv_greeto_cancle:
					//取消订单
					//用户同意取消订单
					Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_cancel);
					ImageView imgLeft=(ImageView) findViewById(R.id.imwash_agreento_cancel);
					imgLeft.setImageBitmap(bitmapLeft);
					isReFreshOrder=true;
					Log.d("yckx", "通知用户订单已取消成功");
					break;
				}
			}
			break;
			case UPLOADIMG_FAILD://上传图片失败
				//upLoadDialog.dismiss();
				ToastUtil.show(getApplicationContext(), "上传图片失败",2000);
				break;
			case UPLOADIMG_SUCCESS:
				//设置为不可用
				/*Timer timer=new Timer();
				timer.schedule(new TimerTask(){
					@Override
					public void run() {
						upLoadDialog.dismiss();
					}}, 2000);*/
				img1.setClickable(false);
				img2.setClickable(false);
				img3.setClickable(false);
				btn_load.setBackgroundResource(R.drawable.btn_click_unable);
				btn_load.setClickable(false);
				break;
			case RELOAD_ORDER_SUCCESS://重新加载订单详情成功
				OrderDetailEntity entity_orderDetial=(OrderDetailEntity) msg.obj;
				OrderListEntity orderListEntity=new OrderListEntity();
				orderListEntity.setAddress(entity_orderDetial.getAddress());
				orderListEntity.setCarEntity(entity_orderDetial.getCarEntity());
				orderListEntity.setCommentEntity(entity_orderDetial.getCommentEntity());
				orderListEntity.setDoorService(entity_orderDetial.getDoorService());
				orderListEntity.setId(entity_orderDetial.getId());
				orderListEntity.setOrderCreateTime(entity_orderDetial.getOrderCreateTime());
				orderListEntity.setOrderFinishTime(entity_orderDetial.getOrderFiniahTime());
				orderListEntity.setOrderNum(entity_orderDetial.getOrderNum());
				orderListEntity.setOrderPayedTime(entity_orderDetial.getOrderPayedTime());
				orderListEntity.setOrderRobbedTime(entity_orderDetial.getOrderRobbedTime());
				orderListEntity.setOrderStatus(entity_orderDetial.getOrderStatus());
				orderListEntity.setOrderSubEntity(entity_orderDetial.getOrderSubEntity());
				orderListEntity.setOrderUpdateTime(entity_orderDetial.getOrderUpdateTime());
				WasherOrderDetailFragment.this.orderListEntity=orderListEntity;
				GlobalServiceUtils.getInstance().setGloubalServiceOrderListEntity(orderListEntity);
				//
				selectTab1();
				break;
			}
		};
	};


	/**设置对应步骤字体颜色*/
	private void setOperatorColor(RelativeLayout layout){

	}


	//---------
	private void washerOperation(final String url,final int viewId){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
				String respose=ApacheHttpUtil.httpPost(url, token, null);
				Log.d("yckx", ""+url+"--"+respose);
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						Message msg=new Message();
						msg.what=OPERATORSUCCESS;
						msg.arg1=viewId;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}



	//------相册---相机----
	private PopupWindow popupWindow;
	private View pview;
	/** 初始化PopupMenu */
	private void initPopupMenu(int imgId) {
		if (pview != null) {
			return;
		}
		initWidth();
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pview = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		pview.findViewById(R.id.openCamera).setOnClickListener(new loadImgListenr(imgId));
		pview.findViewById(R.id.openGallery).setOnClickListener(new loadImgListenr(imgId));
		pview.findViewById(R.id.openCancel).setOnClickListener(new loadImgListenr(imgId));
		popupWindow = new PopupWindow(pview,width-50, ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置为touch窗口外关闭(一定要先设置好它的背景)
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.setOutsideTouchable(true);

	}

	private int width;
	private void initWidth(){
		DisplayMetrics dm=new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		width=dm.widthPixels;
		System.out.println("---->>屏幕宽度"+width);
	}

	/**拍照片上传*/
	private Dialog takephotoDialog;
	//----代替popu窗口的dialog窗口----
	private void takePhonesDialog(int imgId){
		LayoutInflater inflater=getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		takephotoDialog = new AlertDialog.Builder(getActivity()).create();
		takephotoDialog.show();
		takephotoDialog.setContentView(view);
		WindowManager m =getActivity().getWindowManager();
		Window dialogWindow = takephotoDialog.getWindow();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams params = takephotoDialog.getWindow().getAttributes();
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//设置bottom的偏移量
		params.y=30;
		takephotoDialog.getWindow().setAttributes(params);

		view.findViewById(R.id.openCamera).setOnClickListener(new loadImgListenr(imgId));
		view.findViewById(R.id.openGallery).setOnClickListener(new loadImgListenr(imgId));
		view.findViewById(R.id.openCancel).setOnClickListener(new loadImgListenr(imgId));
	}

	//---------end-------------





	class loadImgListenr implements View.OnClickListener{
		private int imgId;
		public loadImgListenr(int imgId){
			this.imgId=imgId;
		}
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.img1://用户名
				swichOfloadImage=1;
				/*initPopupMenu(imgId);
				popupWindow.showAtLocation(tv_phone, Gravity.BOTTOM, 0, 30);*/
				takePhonesDialog(imgId);
				break;
				//泡泡窗口
			case R.id.img2://用户名
				swichOfloadImage=2;
				takePhonesDialog(imgId);
				break;
			case R.id.img3://用户名
				swichOfloadImage=3;
				takePhonesDialog(imgId);
				break;
			case R.id.openCamera:
				//跳转到新页面打开相机
				//打开相机
				if(takephotoDialog!=null){
					takephotoDialog.dismiss();
				}
				camera();
				break;
			case R.id.openGallery:
				if(takephotoDialog!=null){
					takephotoDialog.dismiss();
				}
				gallery();
				break;
			case R.id.openCancel:
				//popupWindow.dismiss();
				if(takephotoDialog!=null){
					takephotoDialog.dismiss();
				}
				break;
			}

		}}


	//-------相册-----相机-----
	private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	/**获取的是第几张图片，1.2.3*/
	private int swichOfloadImage=0;

	private String saveImgName1="/sdcard/yckx/"+"img1.png";
	private String saveImageName2="/sdcard/yckx/"+"img2.png";
	private String saveImageName3="/sdcard/yckx/"+"img2.png";

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
	//private String fileName = "/sdcard/yckx/"+saveImgName;
	//private final String SDPATH="/sdcard/yckx/";

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


	@SuppressLint("SdCardPath") 
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			// 从相册返回的数据
			if(data==null){
			}
			if (data != null) {
				// 得到图片的全路径
				Uri selectedImage = data.getData();  
				String[] filePathColumn = { MediaStore.Images.Media.DATA };  
				Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
				cursor.moveToFirst();  
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
				String imgPath = cursor.getString(columnIndex);  
				cursor.close();  
				Bitmap bmp = BitmapFactory.decodeFile(imgPath); //将所选图片提取为bitmap格式  
				Log.d(LOG_TAG, imgPath);  
				Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));  
				Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
				if(swichOfloadImage==1){
					saveBitmapToNative(bmp,saveImgName1);
					((ImageView) findViewById(R.id.img1)).setImageBitmap(bmp); //show image
				}
				if(swichOfloadImage==2){
					saveBitmapToNative(bmp,saveImageName2);
					((ImageView) findViewById(R.id.img2)).setImageBitmap(bmp); //show image
				}
				if(swichOfloadImage==3){
					saveBitmapToNative(bmp,saveImageName3);
					((ImageView) findViewById(R.id.img3)).setImageBitmap(bmp); //show image
				}
			}

		} else if (requestCode == PHOTO_REQUEST_CAREMA) {
			// 从相机返回的数据
			if (hasSdcard()) {
				try{//相机没有拍照或确定时等不到数据
					Bundle bundle = data.getExtras();  
					Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
					if(swichOfloadImage==1){
						saveBitmapToNative(bitmap,saveImgName1);
						((ImageView) findViewById(R.id.img1)).setImageBitmap(bitmap); //show image
					}
					if(swichOfloadImage==2){
						saveBitmapToNative(bitmap,saveImageName2);
						((ImageView) findViewById(R.id.img2)).setImageBitmap(bitmap); //show image
					}
					if(swichOfloadImage==3){
						saveBitmapToNative(bitmap,saveImageName3);
						((ImageView) findViewById(R.id.img3)).setImageBitmap(bitmap); //show image
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				//saveBitmap(bitmap);
			} else {
				Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！", 0).show();
			}
		} 
		super.onActivityResult(requestCode, resultCode, data);
	}


	private static final String LOG_TAG = "yckx"; 
	/**将bitmap存储到本地*/
	@SuppressLint("SdCardPath") 
	private void saveBitmapToNative(Bitmap bitmap,String saveFileName){
		FileOutputStream b = null;  				  
		yckx_tempFile = new File("/sdcard/yckx/");  
		if(!yckx_tempFile.exists()){
			yckx_tempFile.mkdirs();// 创建文件夹  
		}
		try {   
			b = new FileOutputStream(saveFileName); //写到存储卡 
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

	/**删除文件*/
	public void delFile(String fileName){
		File file = new File(fileName);
		if(file.isFile()){
			file.delete();
		}
		file.exists();
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


	/**检查车辆上传图片*/
	private void uploadImg(){
		//showUploadDialog();
		new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				String url=WEBInterface.NOTICECHECKINGCAR_URL+orderId;
				String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
				RequestParams params=new RequestParams();	
				params.addHeader("token",token);
				if(fileExist(saveImgName1)){
					File fileImg1=new File(saveImgName1);
					if(fileImg1!=null){
						params.addBodyParameter("img1",fileImg1);
					}
				}
				if(fileExist(saveImageName2)){
					File fileImg2=new File(saveImageName2);
					if(fileImg2!=null){
						params.addBodyParameter("img2",fileImg2);
					}
				}
				if(fileExist(saveImageName3)){
					File fileImg1=new File(saveImageName3);
					if(fileImg1!=null){
						params.addBodyParameter("img3",fileImg1);
					}
				}
				HttpUtils http=new HttpUtils();
				http.send(HttpMethod.POST, url, params, new RequestCallBack(){

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.d("yckx","上传车辆图片"+arg1);
						Message msg=new Message();
						msg.what=UPLOADIMG_FAILD;
						mHandler.sendMessage(msg);
					}

					@Override
					public void onSuccess(ResponseInfo arg0) {
						String respose=(String) arg0.result;
						Log.d("yckx", respose+"");
						try {
							JSONObject jSONObject=new JSONObject(respose);
							String code=jSONObject.getString("code");
							if(code.equals("0")){
								Message msg=new Message();
								msg.what=UPLOADIMG_SUCCESS;
								mHandler.sendMessage(msg);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}});
			}
		}).start();
	}

	private Dialog upLoadDialog;
	/**上传图片的时候弹出的dialog窗口*/
	private void showUploadDialog(){
		LayoutInflater inflater=getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_load, null);
		upLoadDialog = new AlertDialog.Builder(getActivity()).create();
		upLoadDialog.show();
		upLoadDialog.setCancelable(false);
		upLoadDialog.setContentView(view);
		Window dialogWindow = upLoadDialog.getWindow(); 
		WindowManager.LayoutParams params = dialogWindow.getAttributes();  
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		WindowManager m =getActivity().getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		//偏移量
		upLoadDialog.getWindow().setAttributes(params);
	}

	/**退出的时候删除图片*/
	@Override
	public void onDestroy() {
		super.onDestroy();
		delFile(saveImgName1);
		delFile(saveImageName2);
		delFile(saveImageName3);
	}



	/**订单内容*/
	private void selectTab2() {
		setContentView(R.layout.fragment_more_formdetail);
		//	LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View view2=inflater.inflate(R.layout.fragment_more_formdetail, null);
		TextView formNo=(TextView)findViewById(R.id.formNo);
		TextView formStatus=(TextView)findViewById(R.id.formStatus);
		TextView formCreateTime=(TextView)findViewById(R.id.formCreateTime);
		TextView formReceiveTime=(TextView)findViewById(R.id.formReceiveTime);
		TextView formCompleteTime=(TextView)findViewById(R.id.formCompleteTime);
		TextView formPay=(TextView)findViewById(R.id.formPay);
		TextView formPayType=(TextView)findViewById(R.id.formPayType);
		TextView formStore=(TextView)findViewById(R.id.formStore);
		TextView formCard=(TextView)findViewById(R.id.formCard);
		TextView formStyle=(TextView)findViewById(R.id.formStyle);

		//String comFrom=(String) GlobalServiceUtils.getGloubalServiceSession("WasherOderDetailActivity_comFrom");
		//if(comFrom.equals("WasherFragment")){
		//"已抢订单页面进入"
		orderListEntity=GlobalServiceUtils.getInstance().getGloubalServiceOrderListEntity();
		String mformNo=orderListEntity.getOrderNum();
		String mformStatus=orderListEntity.getOrderStatus();
		if(mformStatus.equals("create")){
			mformStatus="待支付";
		}
		if(mformStatus.equals("robbed")){
			mformStatus="已接单";
		}
		if(mformStatus.equals("payed")){
			mformStatus="待接单";
		}
		if(mformStatus.equals("cancle")){
			mformStatus="取消";
		}
		if(mformStatus.equals("invalid")){
			mformStatus="作废";
		}
		if(mformStatus.equals("finish")){
			mformStatus="待评价";
		}
		if(mformStatus.equals("comment")){
			mformStatus="已评价";
		}
		String mformCreateTime=orderListEntity.getOrderCreateTime();
		if(mformCreateTime!=null&&!mformCreateTime.equals("")){
			mformCreateTime=TimeUtils.millsToDateTime(mformCreateTime);
		}
		String mformReceiveTime=orderListEntity.getOrderRobbedTime();
		if(mformReceiveTime!=null&&!mformReceiveTime.equals("")){
			mformReceiveTime=TimeUtils.millsToDateTime(mformReceiveTime);
		}
		String mformCompleteTime=orderListEntity.getOrderFinishTime();
		if(mformCompleteTime!=null&&!mformCompleteTime.equals("")){
			mformCompleteTime=TimeUtils.millsToDateTime(mformCompleteTime);
		}
		String mformPay=orderListEntity.getOrderPrice();
		String mformPayType=orderListEntity.getPayType();
		if(mformPayType!=null&&!mformPayType.equals("")){
			if(mformPayType.equals("union")){
				mformPayType="银联支付";
			}
			if(mformPayType.equals("alipay")){
				mformPayType="支付宝支付";
			}
			if(mformPayType.equals("wx")){//不太确定这个wxpay是不是这么拼
				mformPayType="微信支付";
			}
		}
		String mformStore=orderListEntity.getCompannyEntity().getCompanyName();
		String mformCard=orderListEntity.getCarEntity().getCarBrand()+"|"+orderListEntity.getCarEntity().getProvince()+orderListEntity.getCarEntity().getCarNum()+"|"+orderListEntity.getCarEntity().getCarColor();
		String mformStyle=orderListEntity.getDoorService();
		if(mformStyle.equals("false")){
			mformStyle="到店服务";
		}else{
			mformStyle="上门服务";
		}
		formNo.setText(mformNo);
		formStatus.setText(mformStatus);
		formCreateTime.setText(mformCreateTime);
		formReceiveTime.setText(mformReceiveTime);
		formCompleteTime.setText(mformCompleteTime);
		formPay.setText(mformPay);
		formPayType.setText(mformPayType);
		formStore.setText(mformStore);
		formCard.setText(mformCard);
		formStyle.setText(mformStyle);
	}
	
	
	/**重新加载订单详情数据*/
	private void reloadOrderEntity(final String orderId){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
				String host=WEBInterface.ORDERDETAIL_URL+orderId;
				String response=ApacheHttpUtil.httpGet(host, token, null);
				Log.d("yckx","重新加载订单详情数据:"+response);
				if(response!=null){
					try {
						OrderDetailEntity entity=ParseJSONUtils.getOrderDtailEntity(response);
						if(entity!=null){
							Message msg=new Message();
							msg.what=RELOAD_ORDER_SUCCESS;
							msg.obj=entity;
							mHandler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	
	/**保存即使聊天的对象*/
	private void saveRongIMUser2DB(OrderListEntity orderlistEntity){
		//检查是否存在数据库
		if(!DBRead.isExistsCardbFile()){//不存在则加载
			try {
				// 将本地项目中的Assets/db/commonnum.db文件复制写出到 DBRead.telFile文件中
				AssetsDBManager.copyAssetsFileToFile(getApplicationContext(), "db/carsplist.db", DBRead.carFile);
			} catch (IOException e) {
				ToastUtil.show(getActivity(), "初始化车辆信息数据库异常", 2000);
			}
		}
		//添加到数据库
		DBRead.addRongIMUser(orderlistEntity, false);
	}
	
	
	/**记录当前聊天的对象*/
	private OrderListEntity currentChatObj;
	/**融云添加聊天用户信息*/
	@Override
	public UserInfo getUserInfo(String arg0) {
		if(currentChatObj!=null){
			Log.d("yckx", "融云返回getUserInfo()");
			return new UserInfo(currentChatObj.getOwnerEntity().getId(), currentChatObj.getOwnerEntity().getUserPhone(), Uri.parse(WEBInterface.INDEXIMGURL+currentChatObj.getOwnerEntity().getUserImageUrl()));
		}
		Log.d("yckx", "融云返回getUserInfo()失败:"+arg0);
		return null;
	}

}
