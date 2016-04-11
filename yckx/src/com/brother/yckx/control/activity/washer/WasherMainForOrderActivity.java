package com.brother.yckx.control.activity.washer;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.mapcore2d.en;
import com.brother.BaseActivity;
import com.brother.BaseAdapter2;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.model.CardEntity;
import com.brother.yckx.model.HistoryCommentEntity;
import com.brother.yckx.model.OrderDetailEntity;
import com.brother.yckx.model.OrderEntity;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.LoadListView;
import com.brother.yckx.view.LoadListView.ILoadListener;
import com.brother.yckx.view.image.CacheImageView;
import com.brother.yckx.view.scroll2page.DefinedScrollView;

public class WasherMainForOrderActivity extends BaseActivity implements ILoadListener{
	private OrderEntity orderEntity;
    private OrderDetailEntity orderDetailEntity;
	private LinearLayout mLinearLayout; 
	private LinearLayout.LayoutParams param; 
	private DefinedScrollView scrollView; 
	private LayoutInflater inflater; 
	private int pageCount = 0; 
	
	
	private View addview_first;//美护师页面
	private View addview_secon;//历史评价界面
	private LoadListView pingjia_listview;
	private HistoryCommentAdapter adapter;
	
	/**查询订单数据成功*/
	private final int LOADORDERD_SUCCESS=0;
	/**获取历史评论数据成功*/
	private final int LOADHISTORY_COMMENT_SUCCESS=10;
	/**评论内容加载完成*/
    private final int LOADHISTORY_OVER=104;
	
	private List<HistoryCommentEntity> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wahsermain_for_order_main);
		setActionBar(R.string.washerMain, R.drawable.btn_homeasup_default, NULL_ID, null);
		setupView();
	}



	private void setupView() { 
		scrollView = (DefinedScrollView) findViewById(R.id.definedview); 
		pageCount = 2;
		for (int i = 0; i < pageCount; i++) { 
			param = new LinearLayout.LayoutParams( 
					android.view.ViewGroup.LayoutParams.FILL_PARENT, 
					android.view.ViewGroup.LayoutParams.FILL_PARENT); 
			inflater = this.getLayoutInflater(); 
			if (i == 0) { 
				addview_first = inflater.inflate( R.layout.activity_washer_order_mian_first, null); 
				addview_first.findViewById(R.id.phoneImage).setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						takePhoneForWasher();
					}});
				addview_first.findViewById(R.id.messgImage).setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						ToastUtil.show(getApplicationContext(), "调用荣云聊天接口", 2000);
					}});

				mLinearLayout = new LinearLayout(this); 
				mLinearLayout.addView(addview_first, param); 
				scrollView.addView(mLinearLayout); 
			} else { 
			    addview_secon = inflater.inflate(R.layout.activity_washer_order_mian_second, null); 
				//美护师历史评价界面
			    setupHistoryComment();
			    getHistoryEvolution("12");
				mLinearLayout = new LinearLayout(this); 
				mLinearLayout.addView(addview_secon, param); 
				scrollView.addView(mLinearLayout); 
			} 

		} 
		scrollView.setPageListener(new DefinedScrollView.PageListener() {
			@Override
			public void page(int page) { 
				//setCurPage(page); 
				if(page==0){
					setActionBar(R.string.washerMain, R.drawable.btn_homeasup_default, NULL_ID, null);
				}
				if(page==1){
					setActionBar(R.string.pingjiaMain, R.drawable.btn_homeasup_default, NULL_ID, null);
				}
			} 
		}); 
	}



	private Handler mHandler=new Handler(){
        public void handleMessage(Message msg) {
        	switch (msg.what) {
			case LOADORDERD_SUCCESS://查询订单数就成功
				setupOrderDetail();
				break;

			case LOADHISTORY_COMMENT_SUCCESS:
				//
				adapter.addDataToAdapter(list);
				adapter.notifyDataSetChanged();
				pingjia_listview.loadComplete();
				break;
			case LOADHISTORY_OVER://加载完成
				pingjia_listview.loadComplete();
				ToastUtil.show(getApplicationContext(), "加载完成",2000);
				break;
			}
        };
	};

	//----美护师主界面操作
	/**展示用户订单详情*/
	private void setupOrderDetail(){
		String serviceName=orderDetailEntity.getProductEntity().getProductName();
		String doorSerice=orderDetailEntity.getDoorService();
		String serviceSyle="到店服务";
		if(doorSerice.equals("true")){
			serviceSyle="上门服务";
		}
		CardEntity carEntity=orderDetailEntity.getCarEntity();
		String carMessage=carEntity.getCarBrand()+"|"+carEntity.getProvince()+carEntity.getCarNum()+"|"+carEntity.getCarColor();
	    String serviceTime=orderDetailEntity.getServiceTime();
	   // serviceTime=TimeUtils.millsToDateTime(serviceTime);
	    String mPhone=orderDetailEntity.getPhone();
	    String mCarAdress=orderDetailEntity.getAddress();
	    String remark=orderDetailEntity.getRemark();
	    //订单详情
	    TextView tv_serviceName=(TextView)addview_first.findViewById(R.id.serviceName);
	    tv_serviceName.setText(serviceName);
	    TextView tv_serviceSyle=(TextView)addview_first.findViewById(R.id.serviceSyle);
	    tv_serviceSyle.setText(serviceSyle);
	    TextView tv_carMessage=(TextView)addview_first.findViewById(R.id.serviceCar);
	    tv_carMessage.setText(carMessage);
	    TextView tv_serviceTime=(TextView)addview_first.findViewById(R.id.serviceTime);
	    tv_serviceTime.setText(serviceTime);
	    TextView tv_carOwnerSay=(TextView) addview_first.findViewById(R.id.carOwnerSay);
	    //车主信息
	    tv_serviceSyle.setText(serviceSyle);
	    TextView tv_mPhone=(TextView)addview_first.findViewById(R.id.phone);
	    tv_mPhone.setText(mPhone);
	    TextView tv_mCarAdress=(TextView)addview_first.findViewById(R.id.carParkAdress);
	    tv_mCarAdress.setText(mCarAdress);
	    if(!remark.equals("")){
	    	tv_carOwnerSay.setText(remark);
	    }
	}
	
	/**订单详情*/
	/**(支付成功后进入)获取订单数据*/
	private void getOrderDetailData(final String orderId){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url=WEBInterface.ORDERDETAIL_URL+orderId;//orderId3262
				String token=PrefrenceConfig.getUserMessage(WasherMainForOrderActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpGet(url, token, null);
				if(respose!=null){
					Log.d("yckx", "订单编号:"+orderId);
					Log.d("yckx", ":"+respose);
					try {
						OrderDetailEntity entity=ParseJSONUtils.getOrderDtailEntity(respose);
						orderDetailEntity=entity;
						GlobalServiceUtils.getInstance().setGloubalServiceOrderDetailEntity(entity);
						Message msg=new Message();
						msg.what=LOADORDERD_SUCCESS;
						mHandler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Log.d("yckx", respose);
					//解析数据
				}
			}
		}).start();
	}

	/**播放美护师电话*/
	private void takePhoneForWasher(){
		String phoneNo=orderEntity.getWasherPhone();//
		//test
		phoneNo="15011429785";
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
		WasherMainForOrderActivity.this.startActivity(intent);
	}
	
	
	//评价界面----
	
	private void setupHistoryComment(){
		pingjia_listview=(LoadListView) addview_secon.findViewById(R.id.pingjia_listview);
		pingjia_listview.setInterface(this);
		adapter=new HistoryCommentAdapter(WasherMainForOrderActivity.this);
		pingjia_listview.setAdapter(adapter);
	}
    
	private int page=0;
	private int size=2;
	/**获取评论列表数据*/
	private void getHistoryEvolution(final String washerId){
		new Thread(new Runnable() {//测试id:id=12
			@Override
			public void run() {///comment/list/{page}/{size}/{targetOwnerId}
				String url=WEBInterface.HISTORYEVOLOTION_URL+page+"/"+size+"/"+washerId;
				String token=PrefrenceConfig.getUserMessage(WasherMainForOrderActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpGet(url, token, null);
				Log.d("yckx", ""+respose);
				list=ParseJSONUtils.parseHistoryCommet(respose);
				if(list!=null){
					//获取数据成功
					if(list.size()==0){
						Message msg=new Message();
						msg.what=LOADHISTORY_OVER;
						mHandler.sendMessage(msg);
					}else{
						Message msg=new Message();
						msg.what=LOADHISTORY_COMMENT_SUCCESS;
						mHandler.sendMessage(msg);
					}
					
				}
			}
		}).start();
	}
	
	
	



	/**分页加载数据*/
	@Override
	public void onLoad() {
		page++;
		getHistoryEvolution("12");
		
	}
	
	
	class HistoryCommentAdapter extends BaseAdapter2<HistoryCommentEntity>{

		public HistoryCommentAdapter(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1==null){
				arg1=layoutInflater.inflate(R.layout.row_history_comment, null);
			}
			
			CircleImageView userImg=(CircleImageView) arg1.findViewById(R.id.userImg);
			TextView tv_userPhone=(TextView) arg1.findViewById(R.id.tv_userPhone);
			TextView tv_commentTime=(TextView) arg1.findViewById(R.id.tv_commentTime);
			TextView tv_commentContent=(TextView) arg1.findViewById(R.id.tv_commentContent);
			TextView tv_serviceProducetName=(TextView) arg1.findViewById(R.id.tv_serviceProducetName);
			TextView tv_serviceTime=(TextView) arg1.findViewById(R.id.tv_serviceTime);
			TextView tv_replyContent=(TextView) arg1.findViewById(R.id.replyContent);
			
			CacheImageView img1=(CacheImageView) arg1.findViewById(R.id.img1);
			CacheImageView img2=(CacheImageView) arg1.findViewById(R.id.img2);
			CacheImageView img3=(CacheImageView) arg1.findViewById(R.id.img3);
			
			HistoryCommentEntity entity=getItem(arg0);
			String createPhone=entity.getCreateUserPhone();
			String commentCreateTime=entity.getCommentCreateTime();
			String commentContent=entity.getCommentContent();
			String productName=entity.getProductName();
			String orderTime=entity.getOrderTime();
			String replyContent=entity.getReplyContent();
			String mImg1=entity.getImg1();
			String mImg2=entity.getImg2();
			
			tv_userPhone.setText(createPhone);
			tv_commentTime.setText(commentCreateTime);
			tv_commentContent.setText(commentContent);
			tv_serviceProducetName.setText(productName);
			tv_serviceTime.setText(orderTime);
			tv_replyContent.setText(replyContent);
			if(!mImg1.equals("")){
				img1.setImageUrl(WEBInterface.INDEXIMGURL+mImg1);
				img1.setVisibility(View.VISIBLE);
			}else{
				img1.setVisibility(View.GONE);
			}
			if(!mImg2.equals("")){
				img2.setImageUrl(WEBInterface.INDEXIMGURL+mImg2);
				img2.setVisibility(View.VISIBLE);
			}else{
				img2.setVisibility(View.GONE);
			}
			return arg1;
		}}

}
