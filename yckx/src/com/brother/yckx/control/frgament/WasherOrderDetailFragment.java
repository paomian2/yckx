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
	//�û�����ҳ��ı�
	private WasherOderDetailActivity washerOrderDetailActivity;
	private int currenPage=0;
	//��עҳ���л���ʱ���Ƿ�Ҫ���¼�������
	private boolean isReFreshOrder;

	//--------ϴ��ʦ����
	private RelativeLayout tv_phone;
	private RelativeLayout tv_yuyin;
	private RelativeLayout tv_car_location;
	private RelativeLayout tv_notice_arrive;
	private RelativeLayout tv_washing;
	private RelativeLayout tv_washe_finish;
	private RelativeLayout tv_greeto_cancle;
	private TextView btn_load;
	private ImageView img1,img2,img3;
	/**ϴ��ʦ�����ɹ�*/
	private final int OPERATORSUCCESS=0;
	/**�ϴ�ͼƬ�ɹ�*/
	private final int UPLOADIMG_SUCCESS=10;
	/**�ϴ�ͼƬʧ��*/
	private final int UPLOADIMG_FAILD=-1;
	/**���¶�������ɹ�*/
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
				//��ǰҳ���Ƕ������飬���õ��״̬
                if(isReFreshOrder){
                	//��������ҳ�����¼�������
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
	/**����ʦ����(���������������˽���)*/
	private void selectTab1(){
		mPhone=PrefrenceConfig.getUserMessage(getActivity()).getUserPhone();
		//"��������ҳ�����"
		orderListEntity=GlobalServiceUtils.getInstance().getGloubalServiceOrderListEntity();
		Log.d("yckx","��ǰ����:"+orderListEntity.toString());
		mPhone=orderListEntity.getPhone();
		orderId=orderListEntity.getId();
		//���ö���״̬
		setTab1UI();
		//test---
		Log.d("yckx",""+orderListEntity.getId()+"--"+orderListEntity.getOrderNum()+orderListEntity.getOrderStatus());
		//"��ʷ����"ҳ�����
		orderId=orderListEntity.getId();
		Log.d("yckx",""+orderListEntity.getId()+"--"+orderListEntity.getOrderNum()+orderListEntity.getOrderStatus());

		tv_phone=(RelativeLayout)findViewById(R.id.tv_phone);
		//�绰����
		tv_phone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String phoneNo=mPhone;
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				getActivity().startActivity(intent);
				isWasherPhoneOwn=true;
				//�绰
				if(isWasherPhoneOwn){
					Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_contact);
					ImageView imgLeft=(ImageView) findViewById(R.id.imgPhone);
					imgLeft.setImageBitmap(bitmapLeft);
				}
			}});
		//����
		tv_yuyin=(RelativeLayout)findViewById(R.id.tv_yuyin);
		tv_yuyin.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				isWasherTalkOwn=true;
				//�洢��ǰ��ϵ��
				currentChatObj=orderListEntity;
				saveRongIMUser2DB(orderListEntity);
				RongIM.getInstance().startPrivateChat(getActivity(), orderListEntity.getWasherEntity().getWaherId(), "title");
				//����
				isWasherTalkOwn=true;
				if(isWasherTalkOwn){
					Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_talk);
					ImageView imgLeft=(ImageView) findViewById(R.id.imgyuyin);
					imgLeft.setImageBitmap(bitmapLeft);
				}
			}});
		//������λ
		tv_car_location=(RelativeLayout)findViewById(R.id.tv_car_location);
		tv_car_location.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				RongIM.getInstance().startPrivateChat(getActivity(), orderListEntity.getWasherEntity().getWaherId(), "title");
				isWasherLocationOwn=true;
				//��λ
				if(isWasherLocationOwn){
					Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_locate);
					ImageView imgLeft=(ImageView) findViewById(R.id.imgcar);
					imgLeft.setImageBitmap(bitmapLeft);
				}
			}});
		//֪ͨ����
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
				//ɾ��ͼƬ
				delFile(saveImgName1);
				return false;
			}});
		img2=(ImageView)findViewById(R.id.img2);
		img2.setOnClickListener(new loadImgListenr(R.id.img2));
		img2.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				img2.setImageResource(R.drawable.icon_image_toadd);
				//ɾ��ͼƬ
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
				//ɾ��ͼƬ
				return false;
			}});
		//�ϴ�ͼƬ(��鳵��)
		btn_load=(TextView)findViewById(R.id.btn_load);
		btn_load.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//�ϴ�ͼƬ
				uploadImg();
			}});

		//����ϴ��
		tv_washing=(RelativeLayout)findViewById(R.id.tv_washing);
		tv_washing.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String url=WEBInterface.NOTICEWASHERINGCAR_URL+orderId;
				washerOperation(url,R.id.tv_washing);
			}});
		//ϴ�����
		tv_washe_finish=(RelativeLayout)findViewById(R.id.tv_washe_finish);
		tv_washe_finish.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String url=WEBInterface.NOTICEWAHERED_URL+orderId;
				washerOperation(url,R.id.tv_washe_finish);
			}});
		//�û�ȡ��
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
	/**���ݶ���״̬����ui*/
	private void setTab1UI(){
		//�绰
		if(isWasherPhoneOwn){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_contact);
			ImageView imgLeft=(ImageView) findViewById(R.id.imgPhone);
			imgLeft.setImageBitmap(bitmapLeft);
		}
		//����
		if(isWasherTalkOwn){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_talk);
			ImageView imgLeft=(ImageView) findViewById(R.id.imgyuyin);
			imgLeft.setImageBitmap(bitmapLeft);
		}
		//��λ
		if(isWasherLocationOwn){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_locate);
			ImageView imgLeft=(ImageView) findViewById(R.id.imgcar);
			imgLeft.setImageBitmap(bitmapLeft);
		}
		//֪ͨ�û����ѵ���
		if(orderListEntity.getOrderSubEntity()!=null&&orderListEntity.getOrderSubEntity().getSubStatus().equals("arrived")){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_inlocation);
			ImageView imgLeft=(ImageView) findViewById(R.id.imgarrive);
			imgLeft.setImageBitmap(bitmapLeft);
		}
		//�����ϴ�ͼƬ
		//������ϴ
		if(orderListEntity.getOrderSubEntity()!=null&&orderListEntity.getOrderSubEntity().getSubStatus().equals("washing")){
			Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_washing);
			ImageView imgLeft=(ImageView) findViewById(R.id.imgwashing);
			imgLeft.setImageBitmap(bitmapLeft);
			Bitmap bitmapRight_washing=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_donging);
			ImageView imgRight_washing=(ImageView) findViewById(R.id.imgRight_washing);
			imgRight_washing.setImageBitmap(bitmapRight_washing);

		}
		//֪ͨ�û��Ѿ���ϴ���
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
		//�û�ͬ��ȡ������
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
				//�ж�����һ������
			{
				switch (msg.arg1) {
				case R.id.tv_notice_arrive:
					//֪ͨ�û����Ѿ�����(��ť����Ϊ���ɵ��)
					//ToastUtil.show(getApplicationContext(), "��֪ͨ�û����Ѿ�����", 2000);
					//֪ͨ�û����ѵ���
					Bitmap bitmapLeft_arrive=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_inlocation);
					ImageView imgLeft_arrive=(ImageView) findViewById(R.id.imgarrive);
					imgLeft_arrive.setImageBitmap(bitmapLeft_arrive);
					//������ɫ�޸�
					TextView car_tv=(TextView) findViewById(R.id.arrive_tv);
					car_tv.setTextColor(R.color.gray);
					isReFreshOrder=true;
					Log.d("yckx", "֪ͨ�û��ѵ���ɹ�");
					break;
				case R.id.tv_washing:
					//������ϴ��
					setOperatorColor(tv_washing);
					//������ϴ
					//
					Bitmap bitmapLeft_washing=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_washing);
					ImageView imgLeft_washing=(ImageView) findViewById(R.id.imgwashing);
					imgLeft_washing.setImageBitmap(bitmapLeft_washing);
					Bitmap bitmapRight_washing=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_donging);
					ImageView imgRight_washing=(ImageView) findViewById(R.id.imgRight_washing);
					imgRight_washing.setImageBitmap(bitmapRight_washing);
					//������ɫ�޸�
					TextView car_tv_arrive=(TextView) findViewById(R.id.arrive_tv);
					car_tv_arrive.setTextColor(R.color.red);
					TextView washing_tv=(TextView) findViewById(R.id.washing_tv);
					washing_tv.setTextColor(R.color.gray);
					isReFreshOrder=true;
					Log.d("yckx", "֪ͨ�û�����ϴ���ɹ�");
					break;
				case R.id.tv_washe_finish:
					//ϴ�����
					setOperatorColor(tv_washe_finish);
					//֪ͨ�û��Ѿ���ϴ���
					Bitmap bitmapLeft_finish=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_complete);
					ImageView imgLeft_finish=(ImageView) findViewById(R.id.imgwashing);
					imgLeft_finish.setImageBitmap(bitmapLeft_finish);
					Bitmap bitmapRight_finish=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_donging);
					ImageView imgRight_finish=(ImageView) findViewById(R.id.imgRight_washing);
					imgRight_finish.setImageBitmap(bitmapRight_finish);

					Bitmap bitmapRight_washing2=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.bg_empty);
					ImageView imgRight_washing2=(ImageView) findViewById(R.id.imgRight_washing);
					imgRight_washing2.setImageBitmap(bitmapRight_washing2);
					//������ɫ�޸�
					TextView car_tv_arrive2=(TextView) findViewById(R.id.arrive_tv);
					car_tv_arrive2.setTextColor(R.color.red);
					TextView washing_tv2=(TextView) findViewById(R.id.washing_tv);
					washing_tv2.setTextColor(R.color.red);
					TextView finish_tv=(TextView) findViewById(R.id.finish_tv);
					finish_tv.setTextColor(R.color.gray);
					isReFreshOrder=true;
					Log.d("yckx", "֪ͨ�û�ϴ����ɳɹ�");
					break;
				case R.id.tv_greeto_cancle:
					//ȡ������
					//�û�ͬ��ȡ������
					Bitmap bitmapLeft=MyBitmapUtils.getBitmapFromResourse(getActivity(), R.drawable.icon_operation_cancel);
					ImageView imgLeft=(ImageView) findViewById(R.id.imwash_agreento_cancel);
					imgLeft.setImageBitmap(bitmapLeft);
					isReFreshOrder=true;
					Log.d("yckx", "֪ͨ�û�������ȡ���ɹ�");
					break;
				}
			}
			break;
			case UPLOADIMG_FAILD://�ϴ�ͼƬʧ��
				//upLoadDialog.dismiss();
				ToastUtil.show(getApplicationContext(), "�ϴ�ͼƬʧ��",2000);
				break;
			case UPLOADIMG_SUCCESS:
				//����Ϊ������
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
			case RELOAD_ORDER_SUCCESS://���¼��ض�������ɹ�
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


	/**���ö�Ӧ����������ɫ*/
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



	//------���---���----
	private PopupWindow popupWindow;
	private View pview;
	/** ��ʼ��PopupMenu */
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
		// ����Ϊtouch������ر�(һ��Ҫ�����ú����ı���)
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.setOutsideTouchable(true);

	}

	private int width;
	private void initWidth(){
		DisplayMetrics dm=new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		width=dm.widthPixels;
		System.out.println("---->>��Ļ���"+width);
	}

	/**����Ƭ�ϴ�*/
	private Dialog takephotoDialog;
	//----����popu���ڵ�dialog����----
	private void takePhonesDialog(int imgId){
		LayoutInflater inflater=getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_style_getphone, null);
		takephotoDialog = new AlertDialog.Builder(getActivity()).create();
		takephotoDialog.show();
		takephotoDialog.setContentView(view);
		WindowManager m =getActivity().getWindowManager();
		Window dialogWindow = takephotoDialog.getWindow();
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ����
		WindowManager.LayoutParams params = takephotoDialog.getWindow().getAttributes();
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//����bottom��ƫ����
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
			case R.id.img1://�û���
				swichOfloadImage=1;
				/*initPopupMenu(imgId);
				popupWindow.showAtLocation(tv_phone, Gravity.BOTTOM, 0, 30);*/
				takePhonesDialog(imgId);
				break;
				//���ݴ���
			case R.id.img2://�û���
				swichOfloadImage=2;
				takePhonesDialog(imgId);
				break;
			case R.id.img3://�û���
				swichOfloadImage=3;
				takePhonesDialog(imgId);
				break;
			case R.id.openCamera:
				//��ת����ҳ������
				//�����
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


	//-------���-----���-----
	private static final int PHOTO_REQUEST_CAREMA = 1;// ����
	private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	private static final int PHOTO_REQUEST_CUT = 3;// ���

	/**��ȡ���ǵڼ���ͼƬ��1.2.3*/
	private int swichOfloadImage=0;

	private String saveImgName1="/sdcard/yckx/"+"img1.png";
	private String saveImageName2="/sdcard/yckx/"+"img2.png";
	private String saveImageName3="/sdcard/yckx/"+"img2.png";

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
	//private String fileName = "/sdcard/yckx/"+saveImgName;
	//private final String SDPATH="/sdcard/yckx/";

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


	@SuppressLint("SdCardPath") 
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			// ����᷵�ص�����
			if(data==null){
			}
			if (data != null) {
				// �õ�ͼƬ��ȫ·��
				Uri selectedImage = data.getData();  
				String[] filePathColumn = { MediaStore.Images.Media.DATA };  
				Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);  
				cursor.moveToFirst();  
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
				String imgPath = cursor.getString(columnIndex);  
				cursor.close();  
				Bitmap bmp = BitmapFactory.decodeFile(imgPath); //����ѡͼƬ��ȡΪbitmap��ʽ  
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
			// ��������ص�����
			if (hasSdcard()) {
				try{//���û�����ջ�ȷ��ʱ�Ȳ�������
					Bundle bundle = data.getExtras();  
					Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ  
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
				Toast.makeText(getApplicationContext(), "δ�ҵ��洢�����޷��洢��Ƭ��", 0).show();
			}
		} 
		super.onActivityResult(requestCode, resultCode, data);
	}


	private static final String LOG_TAG = "yckx"; 
	/**��bitmap�洢������*/
	@SuppressLint("SdCardPath") 
	private void saveBitmapToNative(Bitmap bitmap,String saveFileName){
		FileOutputStream b = null;  				  
		yckx_tempFile = new File("/sdcard/yckx/");  
		if(!yckx_tempFile.exists()){
			yckx_tempFile.mkdirs();// �����ļ���  
		}
		try {   
			b = new FileOutputStream(saveFileName); //д���洢�� 
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

	/**ɾ���ļ�*/
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


	/**��鳵���ϴ�ͼƬ*/
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
						Log.d("yckx","�ϴ�����ͼƬ"+arg1);
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
	/**�ϴ�ͼƬ��ʱ�򵯳���dialog����*/
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
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ����
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		//ƫ����
		upLoadDialog.getWindow().setAttributes(params);
	}

	/**�˳���ʱ��ɾ��ͼƬ*/
	@Override
	public void onDestroy() {
		super.onDestroy();
		delFile(saveImgName1);
		delFile(saveImageName2);
		delFile(saveImageName3);
	}



	/**��������*/
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
		//"��������ҳ�����"
		orderListEntity=GlobalServiceUtils.getInstance().getGloubalServiceOrderListEntity();
		String mformNo=orderListEntity.getOrderNum();
		String mformStatus=orderListEntity.getOrderStatus();
		if(mformStatus.equals("create")){
			mformStatus="��֧��";
		}
		if(mformStatus.equals("robbed")){
			mformStatus="�ѽӵ�";
		}
		if(mformStatus.equals("payed")){
			mformStatus="���ӵ�";
		}
		if(mformStatus.equals("cancle")){
			mformStatus="ȡ��";
		}
		if(mformStatus.equals("invalid")){
			mformStatus="����";
		}
		if(mformStatus.equals("finish")){
			mformStatus="������";
		}
		if(mformStatus.equals("comment")){
			mformStatus="������";
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
				mformPayType="����֧��";
			}
			if(mformPayType.equals("alipay")){
				mformPayType="֧����֧��";
			}
			if(mformPayType.equals("wx")){//��̫ȷ�����wxpay�ǲ�����ôƴ
				mformPayType="΢��֧��";
			}
		}
		String mformStore=orderListEntity.getCompannyEntity().getCompanyName();
		String mformCard=orderListEntity.getCarEntity().getCarBrand()+"|"+orderListEntity.getCarEntity().getProvince()+orderListEntity.getCarEntity().getCarNum()+"|"+orderListEntity.getCarEntity().getCarColor();
		String mformStyle=orderListEntity.getDoorService();
		if(mformStyle.equals("false")){
			mformStyle="�������";
		}else{
			mformStyle="���ŷ���";
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
	
	
	/**���¼��ض�����������*/
	private void reloadOrderEntity(final String orderId){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
				String host=WEBInterface.ORDERDETAIL_URL+orderId;
				String response=ApacheHttpUtil.httpGet(host, token, null);
				Log.d("yckx","���¼��ض�����������:"+response);
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
	
	
	/**���漴ʹ����Ķ���*/
	private void saveRongIMUser2DB(OrderListEntity orderlistEntity){
		//����Ƿ�������ݿ�
		if(!DBRead.isExistsCardbFile()){//�����������
			try {
				// ��������Ŀ�е�Assets/db/commonnum.db�ļ�����д���� DBRead.telFile�ļ���
				AssetsDBManager.copyAssetsFileToFile(getApplicationContext(), "db/carsplist.db", DBRead.carFile);
			} catch (IOException e) {
				ToastUtil.show(getActivity(), "��ʼ��������Ϣ���ݿ��쳣", 2000);
			}
		}
		//��ӵ����ݿ�
		DBRead.addRongIMUser(orderlistEntity, false);
	}
	
	
	/**��¼��ǰ����Ķ���*/
	private OrderListEntity currentChatObj;
	/**������������û���Ϣ*/
	@Override
	public UserInfo getUserInfo(String arg0) {
		if(currentChatObj!=null){
			Log.d("yckx", "���Ʒ���getUserInfo()");
			return new UserInfo(currentChatObj.getOwnerEntity().getId(), currentChatObj.getOwnerEntity().getUserPhone(), Uri.parse(WEBInterface.INDEXIMGURL+currentChatObj.getOwnerEntity().getUserImageUrl()));
		}
		Log.d("yckx", "���Ʒ���getUserInfo()ʧ��:"+arg0);
		return null;
	}

}
