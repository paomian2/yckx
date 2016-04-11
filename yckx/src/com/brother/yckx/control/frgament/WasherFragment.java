package com.brother.yckx.control.frgament;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.brother.App;
import com.brother.BaseAdapter2;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.TimeUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.net.JavaHttpUtil;
import com.brother.utils.net.StreamUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.HomeActivity;
import com.brother.yckx.control.activity.owner.LoginActivity;
import com.brother.yckx.control.activity.washer.WasherMainActivity;
import com.brother.yckx.control.activity.washer.WasherOderDetailActivity;
import com.brother.yckx.model.OrderListEntity;
import com.brother.yckx.model.QiangDanListEntity;
import com.brother.yckx.view.LoadListView;
import com.brother.yckx.view.LoadListView.ILoadListener;
import com.shizhefei.fragment.LazyFragment;

public class WasherFragment extends LazyFragment implements ILoadListener{
	private int tabIndex;
	public static final String INTENT_INT_INDEX = "intent_int_index";
	private LoadListView listview1;
	private List<OrderListEntity> list1=new ArrayList<OrderListEntity>();
	private QiangdanAdapter adapter1;
	private LoadListView listview2;
	private List<OrderListEntity > list2=new ArrayList<OrderListEntity >();
	private QiangdanAdapter2 adapter2;
	/**判断是否还可以加载数据*/
	private boolean isCanLoadQiangdan=true;
	/**判断是否还可以加载数据*/
	private boolean isCanLoadQiangdan2=true;
	/**本次加载完成*/
	private final int LOAD_QD_FINISH=0;
	/**本次加载完成2*/
	private final int LOAD_QDED_FINISH=10;
	/**token失效*/
	private final int TOKENEORROR=2;
	/**洗护师抢单成功*/
	private final int ROBB_SUCCESS=30;
	/**抢单失败*/
	private final int ROBB_FAIL=32;
	/**洗护师未处理订单已达上限*/
	private final int ORDER_ON_LIMIT=666;
	/**洗护师处于下班状态，不允许抢单*/
	private final int WASHER_OFF_WORK=777;
	/**位置错误*/
	private final int UNKNOWN_ERROR=404;
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		tabIndex = getArguments().getInt(INTENT_INT_INDEX);
		adapter1=new QiangdanAdapter(getActivity());
		adapter2=new QiangdanAdapter2(getActivity());
		getRunYunToken();
		switch (tabIndex) {
		case 0:
			selectTab1();
			break;
		case 1:
			selectTab2();
			break;
		}
	}


	/**选择tab1*/
	private void selectTab1(){
		setContentView(R.layout.fragment_wansher_unget_form);
		listview1=(LoadListView) findViewById(R.id.listview_1);
		listview1.setInterface(this);
		adapter1.addDataToAdapter(list1);
		listview1.setAdapter(adapter1);
		getUnFormData();
	}

	/**选择tab2*/
	private void selectTab2(){
		setContentView(R.layout.fragment_wansher_getform);
		listview2=(LoadListView) findViewById(R.id.listview_2);
		listview2.setInterface(this);
		adapter2.addDataToAdapter(list2);
		listview2.setAdapter(adapter2);
		getFormData(true);
		listview2.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent(getActivity(),WasherOderDetailActivity.class);
				OrderListEntity entity=(OrderListEntity) arg0.getItemAtPosition(arg2);
				GlobalServiceUtils.getInstance().setGloubalServiceOrderListEntity(entity);
				intent.putExtra("WasherOderDetailActivity", "WasherFragment");
				startActivity(intent);
			}});
	}

	private int page=0;
	private int pageSize=3;
	/**获取待抢抢单列表数据*/
	private void getUnFormData(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url=WEBInterface.QIANGDAN_LIST_URL+page+"/"+pageSize;
				String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
				String respose=ApacheHttpUtil.httpGet(url, token, null);
				Log.d("yckx", "抢单列表数据:"+respose);
				if(respose==null){
					return;
				}
				if(!isLoginState(respose)){
					//重新登录
					Message msg=new Message();
					msg.what=TOKENEORROR;
					mHandler.sendMessage(msg);
				}
				//List<OrderListEntity> tempList1=ParseJSONUtils.getQiangDanList(respose);
				List<OrderListEntity> tempList1=ParseJSONUtils.getOrderListEntity(respose);
				//保存当前数据，用户无网络的时候使用
				
				if(tempList1==null||tempList1.size()==0){
					//全部加载完成
					isCanLoadQiangdan=false;
				}else{
					isCanLoadQiangdan=true;
					list1.addAll(tempList1);
				}
				Message msg=new Message();
				msg.what=LOAD_QD_FINISH;
				mHandler.sendMessage(msg);
			}
		}).start();
	}


	private  int forDataPage=0;//第一页
	private int forPageSiz3=8;
	/**已经抢列表*/
	private void getFormData(final boolean isAddToUI){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url=WEBInterface.ROBBEDLIST_URL+forDataPage+"/"+forPageSiz3;
				String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
				String respose=ApacheHttpUtil.httpGet(url, token, null);
				Log.d("yckx","已抢列表:"+respose);
				if(respose==null){
					return;
				}
				if(!isLoginState(respose)){
					//重新登录
					Message msg=new Message();
					msg.what=TOKENEORROR;
					mHandler.sendMessage(msg);
				}
				List<OrderListEntity> tempList2=ParseJSONUtils.getOrderListEntity(respose);
				if(tempList2==null||tempList2.size()==0){
					//全部加载完成
					isCanLoadQiangdan2=false;
				}else{
					list2.addAll(tempList2);
					isCanLoadQiangdan2=true;
				}
				if(isAddToUI){
					Message msg=new Message();
					msg.what=LOAD_QDED_FINISH;
					mHandler.sendMessage(msg);
				}
				
			}
		}).start();
	}

	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_QD_FINISH:
				if(isCanLoadQiangdan){
					//更新ui
					//设置加载完成
					adapter1.clearAdapter();
					adapter1.addDataToAdapter(list1);
					adapter1.notifyDataSetChanged();
					listview1.loadComplete();
				}else{
					listview1.loadComplete();
					ToastUtil.show(getApplicationContext(), "已经全部加载完", 2000);
				}
				break;

			case LOAD_QDED_FINISH://已抢列表，本次加载完成
				if(isCanLoadQiangdan2){
					//更新ui
					//设置加载完成
					adapter1.clearAdapter();
					adapter2.addDataToAdapter(list2);
					adapter2.notifyDataSetChanged();
					listview2.loadComplete();
				}else{
					listview2.loadComplete();
					ToastUtil.show(getApplicationContext(), "已经全部加载完", 2000);
				}
				break;
			case TOKENEORROR://重新登录
				ToastUtil.show(getApplicationContext(), "您的登陆状态已失效，请重新登陆", 2000);
				AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
				dialog.setTitle("温馨提示");
				dialog.setMessage("您的登陆状态已失效，是否重新登录");
				dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent intent=new Intent(getActivity(),LoginActivity.class);
						startActivity(intent);
						getActivity().finish();
					}});
				dialog.setNegativeButton("否", null);
				dialog.show();
				break;
			case ROBB_SUCCESS:
				//更新已抢列表
				forDataPage=0;
				getFormData(false);
				ToastUtil.show(getApplicationContext(), "抢单成功", 2000);
				break;
			case ROBB_FAIL:
				ToastUtil.show(getApplicationContext(), "抢单失败", 2000);
				break;
			case ORDER_ON_LIMIT:
				ToastUtil.show(getApplicationContext(), "洗护师当前处理订单数已达上限", 2000);
				break;
			case WASHER_OFF_WORK://洗护师下班状态，不允许抢单
				ToastUtil.show(getApplicationContext(), "您已下班，禁止抢单", 2000);
				break;
			case UNKNOWN_ERROR:
				String response=(String) msg.obj;
				ToastUtil.show(getApplicationContext(), ""+response, 2000);
				break;
			 case 1://融云聊天
				Log.d("yckx", "取融云的token--success");
				String grTk=(String) msg.obj;
				//获取当前用户会话列表
				connect(grTk);
				break;
			}
		};
	};


	@Override
	public void onLoad() {
		if(tabIndex==0){
			page++;
			getUnFormData();
		}
		if(tabIndex==1){
			forDataPage++;
			getFormData(true);
		}
	}
	
	
	/**是否还是登录状态*/
	private boolean isLoginState(String json){
		if(json==null){
			return false;
		}
		try {
			JSONObject jSONObject=new JSONObject(json);
			String code=jSONObject.getString("code");
			if(code.equals("0")){
				return true;
			}
			if(code.equals("2")){
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
	/**洗护师抢单*/
	private void washerRobOrder(final String orderId){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url=WEBInterface.ROBBURL+orderId;
				String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
				String respose=ApacheHttpUtil.httpPost(url, token, null);
				if(respose==null){
					return;
				}
				Log.d("yckx", respose);
				System.out.println(respose);
				String code="";
				String success="";
				try {
					JSONObject jsonObject=new JSONObject(respose);
					 code=jsonObject.getString("code");
					 success=jsonObject.getString("success");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(code.equals("2")){
					//重新登录
					Message msg=new Message();
					msg.what=TOKENEORROR;
					mHandler.sendMessage(msg);
				}else if(code.equals("0")){
					Message msg=new Message();
					if(success.equals("true")){
						msg.what=ROBB_SUCCESS;
					}
					if(success.equals("false")){
						msg.what=ROBB_FAIL;
					}
					mHandler.sendMessage(msg);
				}else if(code.equals("14")){
					Message msg=new Message();
					msg.what=ORDER_ON_LIMIT;
					mHandler.sendMessage(msg);
				}else if(code.equals("1041000")){
					Message msg=new Message();
					msg.what=WASHER_OFF_WORK;
					mHandler.sendMessage(msg);
				}else{
					 String reponseMSG="未知错误";
					try{
					JSONObject jsonObject=new JSONObject(respose);
					 code=jsonObject.getString("code");
					 reponseMSG=jsonObject.getString("msg");
					}catch(Exception e){
					}
					Message msg=new Message();
					msg.what=UNKNOWN_ERROR;
					msg.obj=reponseMSG;
					mHandler.sendMessage(msg);
				}
				
			}
		}).start();
	}


	class QiangdanAdapter extends BaseAdapter2<OrderListEntity >{
		
		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}
		
		public QiangdanAdapter(Context context) {
			super(context);
		}
		@SuppressLint({ "SimpleDateFormat", "UseValueOf" }) @Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1==null){
				arg1=layoutInflater.inflate(R.layout.row_fragment_washer_unget_form,null);
			}
			TextView un_companyName=(TextView) arg1.findViewById(R.id.un_companyName);
			TextView un_customPhone=(TextView) arg1.findViewById(R.id.un_customPhone);
			TextView un_productName=(TextView) arg1.findViewById(R.id.un_productName);
			TextView un_orderTime=(TextView) arg1.findViewById(R.id.un_orderTime);
			TextView un_serviceAdress=(TextView) arg1.findViewById(R.id.un_serviceAdress);
			TextView un_customSay=(TextView) arg1.findViewById(R.id.un_customSay);
			final TextView un_qiangdan=(TextView) arg1.findViewById(R.id.un_qiangdan);

			OrderListEntity entity=getItem(arg0);
			un_companyName.setText(""+entity.getCompannyEntity().getCompanyName());
			un_customPhone.setText(""+entity.getPhone());
			un_productName.setText(""+entity.getProductEntity().getProductName());
			//格式化时间
			String mpatentTime=entity.getServiceTime();
			SimpleDateFormat formate=new SimpleDateFormat("yyyy年MM月dd日H点mm分");
			Long time=new Long(mpatentTime);  
			String dTime = formate.format(time); 
			un_orderTime.setText(""+dTime);
			un_serviceAdress.setText(""+entity.getAddress());
			if(!entity.getRemark().equals("")){
				un_customSay.setText(entity.getRemark());
			}else{
				un_customSay.setText("--");
			}
			un_qiangdan.setTag(arg0);
			un_qiangdan.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					//抢单
					OrderListEntity qiandan=list1.get((Integer)un_qiangdan.getTag());
					String orderId=qiandan.getId();
					washerRobOrder(orderId);
					//ToastUtil.show(getApplicationContext(), "我抢了"+orderId, 2000);
				}});
			return arg1;
		}}



	class QiangdanAdapter2 extends BaseAdapter2<OrderListEntity >{

		
		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}
		
		/*@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}*/
		
		
		public QiangdanAdapter2(Context context) {
			super(context);
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1==null){
				arg1=layoutInflater.inflate(R.layout.row_fragment_washer_geted_form,null);
			}

			TextView un_companyName=(TextView) arg1.findViewById(R.id.companyName);
			TextView un_customPhone=(TextView) arg1.findViewById(R.id.customPhone);
			TextView un_productName=(TextView) arg1.findViewById(R.id.productName);
			TextView un_orderTime=(TextView) arg1.findViewById(R.id.orderTime);
			TextView un_serviceAdress=(TextView) arg1.findViewById(R.id.serviceAdress);
			TextView un_customSay=(TextView) arg1.findViewById(R.id.customSay);
			TextView un_qiangdan=(TextView) arg1.findViewById(R.id.qiangdan);
			TextView customCancel=(TextView) arg1.findViewById(R.id.customCancel);
			OrderListEntity entity=getItem(arg0);
			un_companyName.setText(""+entity.getCompannyEntity().getCompanyName());
			un_customPhone.setText(""+entity.getPhone());
			un_productName.setText(""+entity.getProductEntity().getProductName());
			//格式化时间
			String mpatentTime=entity.getServiceTime();
			SimpleDateFormat formate=new SimpleDateFormat("yyyy年MM月dd日H点mm分");
			Long time=new Long(mpatentTime);  
			String dTime = formate.format(time); 
			un_orderTime.setText(""+dTime);
			un_serviceAdress.setText(""+entity.getAddress());
			if(!entity.getRemark().equals("")){
				un_customSay.setText(entity.getRemark());
			}else{
				un_customSay.setText("--");
			}
			Log.d("yckx", ""+entity.getOrderStatus());
			/**判断当前订单的状态*/
			if(entity.getOrderStatus().equals("cancle")){
				//订单状态:已取消
				customCancel.setTextColor(R.color.green_for_order);
			}
			if(entity.getOrderStatus().equals("finish")){
				un_qiangdan.setTextColor(R.color.green_for_order);
			}
			return arg1;
		}}
	
	
	/**获取荣云token值*/
	private void getRunYunToken(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
				String url=WEBInterface.RONGYUN_TOKEN_URL;
				String respose = null;
				try {
					respose = StreamUtil.getString(JavaHttpUtil.httpPost2(url, token, null));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Log.d("yckx", "ronyun token:"+respose);
				if(respose==null){
					return;
				}
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						String grTk=jSONObject.getString("grTk");
						Message msg=new Message();
						msg.what=1;
						msg.obj=grTk;
						mHandler_rongyun.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private Handler mHandler_rongyun=new Handler(){
		@SuppressLint("HandlerLeak") public void handleMessage(Message msg) {
			if(msg.what==1){
				String grTk=(String) msg.obj;
				//获取当前用户会话列表
				connect(grTk);
			}
		};
	};
	
	/**
	 * 建立与融云服务器的连接
	 *
	 * @param token
	 */
	private void connect(String token) {

	    if (getActivity().getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {

	        /**
	         * IMKit SDK调用第二步,建立与服务器的连接
	         */
	        RongIM.connect(token, new RongIMClient.ConnectCallback() {

	            /**
	             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
	             */
	            @Override
	            public void onTokenIncorrect() {

	                Log.d("LoginActivity", "--onTokenIncorrect");
	            }

	            /**
	             * 连接融云成功
	             * @param userid 当前 token
	             */
	            @Override
	            public void onSuccess(String userid) {

	                Log.d("LoginActivity", "--onSuccess" + userid);
	            }

	            /**
	             * 连接融云失败
	             * @param errorCode 错误码，可到官网 查看错误码对应的注释
	             */
	            @Override
	            public void onError(RongIMClient.ErrorCode errorCode) {

	                Log.d("LoginActivity", "--onError" + errorCode);
	            }
	        });
	    }
	}


}
