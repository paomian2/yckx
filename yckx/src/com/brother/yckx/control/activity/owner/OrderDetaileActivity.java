package com.brother.yckx.control.activity.owner;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.brother.BaseActivity;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.TimeUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.view_def.date_time_view.NumericWheelAdapter;
import com.brother.utils.view_def.date_time_view.OnWheelScrollListener;
import com.brother.utils.view_def.date_time_view.WheelView;
import com.brother.yckx.R;
import com.brother.yckx.model.AdressEntity;
import com.brother.yckx.model.BusinessEntity;
import com.brother.yckx.model.CardEntity;
import com.brother.yckx.model.OrderEntity;
import com.brother.yckx.model.ProductEntity;
import com.brother.yckx.view.DateTimePickDialogUtil;

public class OrderDetaileActivity extends BaseActivity{
	private LinearLayout main_layout;//���ڼ�������̵�ʹ��״̬
	private TextView leagueStoreName,productName,product_jianjie;;
	private EditText talkPhone;
	private ImageView  openkeyboard;
	private EditText cardMessage;
	private ImageView openCardMessage;
	private TextView serviceSyle_daodian,serviceSyle_shangmen;
	private EditText promiseTime;
	private ImageView openTimeSelect;//ʱ��ѡ����
	private EditText cardAdress;
	private ImageView openCardAdress;
	private EditText liuyan;
	private EditText washerPhone;//����ʦ�绰
	private TextView actualMoney,payOK;
	private BusinessEntity mBusinessEntity;
	private ProductEntity mProductEntity;
	private String serviceState="service_daodian";//service_daodian,service_shangmen
	private String mHours="00";
	private String mMin="00";
	private String orderTime="";
	/**���ɵĶ���*/
	private OrderEntity orderEntity;
	/**���ɶ������*/
	private final int CREATE_ORDER_SUCCESS=0;
	/**������ϢʧЧ*/
	private final int INVALIBAL_CARMSG=4;
	/**ָ����ϴ��ʦ������*/
	private final int WASHAR_NO_EXIST=1052;
	private final int WASHAR_EROR=10521;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_order_detail);
		setActionBar(R.string.orderOk_title, R.drawable.btn_homeasup_default, NULL_ID, new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mBusinessEntity=(BusinessEntity) getIntent().getSerializableExtra("BusinessEntity");
		mProductEntity=(ProductEntity) getIntent().getSerializableExtra("ProductEntity");
		init();
		viewClickEnven();
	}

	private void init(){

		main_layout=(LinearLayout) findViewById(R.id.main_layout);

		leagueStoreName=(TextView) findViewById(R.id.league_store);
		leagueStoreName.setText(""+mBusinessEntity.getCompanyName());

		productName=(TextView) findViewById(R.id.service_product_name);
		productName.setText(""+mProductEntity.getProductName());

		product_jianjie=(TextView) findViewById(R.id.product_jianjie);
		product_jianjie.setText(""+mProductEntity.getProductDesc());

		talkPhone=(EditText) findViewById(R.id.talk_phone);
		talkPhone.setInputType(InputType.TYPE_NULL);//����ֱ������
		openkeyboard=(ImageView) findViewById(R.id.open_key);
		
		cardMessage=(EditText) findViewById(R.id.cardMessage);
		cardMessage.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				cardMessage.setInputType(InputType.TYPE_CLASS_TEXT);
				cardMessage.setFocusableInTouchMode(true);
				cardMessage.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)cardMessage.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(cardMessage, 0);
				main_layout.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = main_layout.getRootView().getHeight() - main_layout.getHeight();
								if (heightDiff > 100)
								{ // ˵�������ǵ���״̬
									//ToastUtil.show(getApplicationContext(), "����", 2000);
								} else{
									cardMessage.setInputType(InputType.TYPE_NULL);
									cardMessage.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "����", 2000);
								}
							}
						});
			}});
		CardEntity writeCarEntity=PrefrenceConfig.getCarMessage(OrderDetaileActivity.this);
		String mcardMessage=writeCarEntity.getCarBrand()+"|"+writeCarEntity.getProvince()+writeCarEntity.getCarNum()+"|"+writeCarEntity.getCarColor();
		if(mcardMessage==null||mcardMessage.equals("")){
			mcardMessage="������Ĭ�ϳ�����Ϣ";
		}
		cardMessage.setText(mcardMessage);
		openCardMessage=(ImageView) findViewById(R.id.open_cardMessage);
		openCardMessage.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrderDetaileActivity.this,GarageManagerActivity.class);
				//����ַ�������
				startActivityForResult(intent, 0);
			}});
		serviceSyle_daodian=(TextView) findViewById(R.id.service_style_daodian);
		serviceSyle_daodian.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				serviceState="service_daodian";
				serviceSyle_daodian.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_blue_left));
				serviceSyle_shangmen.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_line_blue));
			}});
		serviceSyle_shangmen=(TextView) findViewById(R.id.service_style_shangmen);
		serviceSyle_shangmen.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				serviceSyle_shangmen.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_blue_right));
				serviceSyle_daodian.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_line_blue));
			}});
		promiseTime=(EditText) findViewById(R.id.promise_time);
		openTimeSelect=(ImageView) findViewById(R.id.time_select);
		SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd�� H��mm�� ");
		//final String initPromiseTime=format.format(new Date());
		//final String initPromiseTime="2013��9��3�� 14:44";
		//promiseTime.setText(initPromiseTime);
		/*promiseTime.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(  
                        OrderDetaileActivity.this, initPromiseTime);  
                dateTimePicKDialog.dateTimePicKDialog(promiseTime); 
			}});*/
		
		openTimeSelect.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//showPopwindow(getTimePick());
				showTimeDialog(getTimePick());
				/*DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(  
                        OrderDetaileActivity.this, initPromiseTime);  
                dateTimePicKDialog.dateTimePicKDialog(promiseTime);  */
			}});

		cardAdress=(EditText) findViewById(R.id.cardAdress);
		cardAdress.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				cardAdress.setInputType(InputType.TYPE_CLASS_TEXT);
				cardAdress.setFocusableInTouchMode(true);
				cardAdress.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)cardAdress.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(cardAdress, 0);
				main_layout.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = main_layout.getRootView().getHeight() - main_layout.getHeight();
								if (heightDiff > 100)
								{ // ˵�������ǵ���״̬
									//ToastUtil.show(getApplicationContext(), "����", 2000);
								} else{
									cardAdress.setInputType(InputType.TYPE_NULL);
									cardAdress.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "����", 2000);
								}
							}
						});
			}});
		//����Ĭ�ϵ�ַ
		String mAdress=PrefrenceConfig.getAdressDefalut(OrderDetaileActivity.this).getLocation();
		cardAdress.setText(mAdress);
		openCardAdress=(ImageView) findViewById(R.id.location_dingwei);
		openCardAdress.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrderDetaileActivity.this,AdressActivity.class);
				//���ط��ؽ��
				startActivityForResult(intent, 3);
			}});

		liuyan=(EditText) findViewById(R.id.liuyan);
		liuyan.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				liuyan.setInputType(InputType.TYPE_CLASS_TEXT);
				liuyan.setFocusableInTouchMode(true);
				liuyan.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)liuyan.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(liuyan, 0);
				main_layout.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = main_layout.getRootView().getHeight() - main_layout.getHeight();
								if (heightDiff > 100)
								{ // ˵�������ǵ���״̬
									//ToastUtil.show(getApplicationContext(), "����", 2000);
								} else{
									liuyan.setInputType(InputType.TYPE_NULL);
									liuyan.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "����", 2000);
								}
							}
						});
			}});

		washerPhone=(EditText) findViewById(R.id.washerPhone);
		washerPhone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				washerPhone.setInputType(InputType.TYPE_CLASS_PHONE);
				washerPhone.setFocusableInTouchMode(true);
				washerPhone.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)washerPhone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(washerPhone, 0);
				main_layout.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = main_layout.getRootView().getHeight() - main_layout.getHeight();
								if (heightDiff > 100)
								{ // ˵�������ǵ���״̬
									//ToastUtil.show(getApplicationContext(), "����", 2000);
								} else{
									washerPhone.setInputType(InputType.TYPE_NULL);
									washerPhone.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "����", 2000);
								}
							}
						});
			}});

		actualMoney=(TextView) findViewById(R.id.actualPay);
		actualMoney.setText(""+mProductEntity.getProductPrice());
		payOK=(TextView) findViewById(R.id.payOk);
		payOK.setOnClickListener(new OnClickListener(){//ȷ�϶���
			@Override
			public void onClick(View arg0) {
				String companyId=mBusinessEntity.getCompanyId();
				String userCarId=PrefrenceConfig.getCarMessage(OrderDetaileActivity.this).getId();
				String productId=mProductEntity.getId();
				String phone=talkPhone.getText().toString().trim();

				String doorService="false";
				if(serviceState.equals("service_daodian")){
					doorService="false";
				}
				if(serviceState.equals("service_shangmen")){
					doorService="true";
				}
				if(phone==null||phone.equals("")){
					ToastUtil.show(getApplicationContext(), "�����Ҳఴť�����ֻ���", 2000);
				}
				String address=cardAdress.getText().toString().trim();
				if(address.equals("")){
					ToastUtil.show(getApplicationContext(), "��ѡ��ǰ������ַ", 2000);
					return;
				}
				String serviceTime=promiseTime.getText().toString().trim();//���ʱ�����������ܾ��µ�
				if(serviceTime.equals("������ʱ��")||serviceTime.equals("")){
					ToastUtil.show(getApplicationContext(), "��ѡ��ԤԼʱ��", 2000);
					return;
				}
				String mTime="0";
				mTime=TimeUtils.getTime(serviceTime);
				long mPromissTime=Long.parseLong(mTime);
				//��ǰ��ʱ���
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy��MM��dd��H��mm��");
				Date todateDate=new Date();
				String todateTimeStr=dateformat.format(todateDate);
				if(TimeUtils.betweenTime(todateTimeStr, serviceTime)<0){
					ToastUtil.show(getApplicationContext(), "ʱ��ѡ����ȷ", 2000);
					return;
				}
				long todateTime = todateDate.getTime();
				long betweenDays = (long)((mPromissTime - todateTime) / (1000 * 60 * 60 *24) + 0.5);
				if(betweenDays>5){
					ToastUtil.show(getApplicationContext(), "ֻ��ԤԼδ��5��", 2000);
					return;
				}
				String remark=liuyan.getText().toString().trim();
				//ָ������ʦ�绰
				String mWasherPhone=washerPhone.getText().toString().trim();
				HashMap<String, String> hasmp=new HashMap<String, String>();
				hasmp.put("companyId", companyId);
				hasmp.put("userCarId", userCarId);
				hasmp.put("productId", productId);
				hasmp.put("phone", phone);
				hasmp.put("doorService", doorService);
				hasmp.put("address", address);
				hasmp.put("serviceTime", mTime);
				hasmp.put("remark", remark);
				hasmp.put("washerPhone", mWasherPhone);
				//���ɶ���
				orderEntity=new OrderEntity(null,companyId, userCarId, productId, phone, doorService, address, mTime, remark, mWasherPhone, false);
				OrderDetermine(hasmp);
			}});
		productName.setText(""+mProductEntity.getProductName());
	}

    /**�����*/
	private void viewClickEnven(){
		//�������
		openkeyboard.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				talkPhone.setInputType(InputType.TYPE_CLASS_PHONE);
				talkPhone.setFocusableInTouchMode(true);
				talkPhone.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)talkPhone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(talkPhone, 0);
				main_layout.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = main_layout.getRootView().getHeight() - main_layout.getHeight();
								if (heightDiff > 100)
								{ // ˵�������ǵ���״̬
									//ToastUtil.show(getApplicationContext(), "����", 2000);
								} else{
									talkPhone.setInputType(InputType.TYPE_NULL);
									talkPhone.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "����", 2000);
								}
							}
						});
			}});
	}


	/**ȷ������*/
	private void OrderDetermine(final HashMap<String, String> hasmp){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(OrderDetaileActivity.this).getUserToken();
				String respose = null;
				try {
					respose = ApacheHttpUtil.httpPost2(WEBInterface.ORDERURL, token, new StringEntity(new JSONObject(hasmp).toString(),"UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Log.d("yckx", respose+"");
				if(respose!=null){
					try {
						JSONObject jSONObject=new JSONObject(respose);
						String code=jSONObject.getString("code");
						if(code.equals("0")){
							//�µ��ɹ�����ȡ����
							String orderId=jSONObject.getString("orderId");
							System.out.println("-->>OrderDetermine()�µ����ɵĵ���"+orderId);
							//���ö�����
							orderEntity.setOrderId(orderId);
							Message msg=new Message();
							msg.what=CREATE_ORDER_SUCCESS;
							mHandler.sendMessage(msg);
						}else
							if(code.equals("1051000")){
								Message msg=new Message();
								msg.what=INVALIBAL_CARMSG;
								mHandler.sendMessage(msg);
							}else
								if(code.equals("1052000")){
									Message msg=new Message();
									msg.what=WASHAR_NO_EXIST;
									mHandler.sendMessage(msg);
								}else{
									Message msg=new Message();
									msg.what=WASHAR_EROR;
									String errorCode=code;
									msg.obj=errorCode;
									mHandler.sendMessage(msg);
								}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==CREATE_ORDER_SUCCESS){
				Intent intent=new Intent(OrderDetaileActivity.this,PayMoneyActivity.class);
				intent.putExtra("PayMoneyActivity", orderEntity.getOrderId());
				startActivity(intent);
			}
			if(msg.what==INVALIBAL_CARMSG){
				ToastUtil.show(getApplicationContext(), "������ϢʧЧ���뵽�������ó�����Ϣ", 2000);
			}
			if(msg.what==WASHAR_NO_EXIST){
				ToastUtil.show(getApplicationContext(), "ָ����ϴ��ʦ������", 2000);
			}
			if(msg.what==WASHAR_EROR){
				String errorCode=(String) msg.obj;
				ToastUtil.show(getApplicationContext(), "�µ�����"+errorCode, 2000);
			}
		};
	};


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	//-------------�Ż���ʱ��ѡ����start---------------
	private LayoutInflater inflater = null;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private WheelView hour;
	private WheelView mins;
	PopupWindow menuWindow;
	private Dialog timeDialog;
	/**ʹ��Dialog����popuwindow**/
	private void showTimeDialog(View view){
		timeDialog = new AlertDialog.Builder(this).create();
		timeDialog.show();
		timeDialog.setContentView(view);
		Window dialogWindow = timeDialog.getWindow(); 
		WindowManager.LayoutParams params = dialogWindow.getAttributes();  
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		WindowManager m =getWindowManager();
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ����
		params.width = (int) ((d.getWidth()));
		params.height = params.height;
		//ƫ����
		timeDialog.getWindow().setAttributes(params);
	}
	
	

	/**
	 * 
	 * @return
	 */
	private View getTimePick() {
		inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.timepick, null);
		getDataPick(view);
		hour = (WheelView) view.findViewById(R.id.hour);
		hour.setAdapter(new NumericWheelAdapter(0, 23));
		hour.setLabel("ʱ");
		hour.setCyclic(true);
		mins = (WheelView) view.findViewById(R.id.mins);
		mins.setAdapter(new NumericWheelAdapter(0, 59));
		mins.setLabel("��");
		mins.setCyclic(true);

		hour.setCurrentItem(8);
		mins.setCurrentItem(30);

		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String,String> ymd=new HashMap<String, String>();
				ymd.put("year", (year.getCurrentItem()+1950)+"");
				ymd.put("month", (month.getCurrentItem()+1)+"");
				ymd.put("day", (day.getCurrentItem()+1)+"");
				ymd.put("hour", hour.getCurrentItem()+"");
				ymd.put("mins", mins.getCurrentItem()+"");
				String mPromiseTime=ymd.get("year")+"��"+ymd.get("month")+"��"+ymd.get("day")+"��"+ymd.get("hour")+"��"+ymd.get("mins")+"��";
				promiseTime.setText(mPromiseTime);
				if(timeDialog!=null){
					timeDialog.dismiss();
				}
			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//menuWindow.dismiss();
				if(timeDialog!=null){
					timeDialog.dismiss();
				}
			}
		});
		return view;
	}

	/**
	 * 
	 * @return
	 */
	private HashMap<String, String> getDataPick(View view) {
		HashMap<String, String> hashMap=new HashMap<String, String>();
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;//ͨ��Calendar���������Ҫ+1
		int curDate = c.get(Calendar.DATE);
		//final View view = inflater.inflate(R.layout.datapick, null);

		year = (WheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(1950, curYear+1));//����������Ҫ�Ƚ����һ�꣬�Է���ĩ����ѡ��
		year.setLabel("��");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel("��");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear,curMonth);
		day.setLabel("��");
		day.setCyclic(true);

		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		String str = (year.getCurrentItem()+1950) + "-"+ (month.getCurrentItem()+1)+"-"+(day.getCurrentItem()+1);

		hashMap.put("year", (year.getCurrentItem()+1950)+"");
		hashMap.put("month", (month.getCurrentItem()+1)+"");
		hashMap.put("day", (day.getCurrentItem()+1)+"");
		Log.d("yckx", str);
		return hashMap;
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			// TODO Auto-generated method stub
			int n_year = year.getCurrentItem() + 1950;// 骞�
			int n_month = month.getCurrentItem() + 1;// 鏈�
			initDay(n_year,n_month);
		}
	};

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	/**
	 */
	private void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
	}


	//-------------�Ż���ʱ��ѡ����end-----------------

	
	/**��дonActivityResult��ȡ����ҳ�淵����������*/
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if(arg1==0){
			CardEntity carEntity=PrefrenceConfig.getCarMessage(OrderDetaileActivity.this);
			String mCarMessage=carEntity.getCarBrand()+"|"+carEntity.getProvince()+carEntity.getCarNum()+"|"+carEntity.getCarColor();
			cardMessage.setText(mCarMessage);
		}
		if(arg1==3){
			AdressEntity adressEntity=PrefrenceConfig.getAdressDefalut(OrderDetaileActivity.this);
			String mAdress=adressEntity.getLocation();
			cardAdress.setText(mAdress);
		}
		super.onActivityResult(arg0, arg1, arg2);
	}
}
