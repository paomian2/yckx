package com.brother.yckx.control.frgament.washer;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.brother.BaseAdapter2;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.MyBitmapUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.TimeUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.CommentActivity;
import com.brother.yckx.control.activity.owner.FormDetailActivity;
import com.brother.yckx.control.activity.owner.LoginActivity;
import com.brother.yckx.control.activity.owner.PayMoneyActivity;
import com.brother.yckx.control.activity.washer.WasherFeedbackActivity;
import com.brother.yckx.control.activity.washer.WasherHistoryFormActivity;
import com.brother.yckx.model.OrderListEntity;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.LoadListView;
import com.brother.yckx.view.LoadListView.ILoadListener;
import com.shizhefei.fragment.LazyFragment;

public class WasherMoreFragment extends LazyFragment implements ILoadListener{
	private int tabIndex;
	public static final String INTENT_INT_INDEX = "intent_int_index";
	private LoadListView listview;
	private ProgressBar progressBar;
	private MoreFragmentAdapter adapter;
	private List<OrderListEntity> allList=new ArrayList<OrderListEntity>();//全部
	//private List<OrderListEntity> createList=new ArrayList<OrderListEntity>();
	private List<OrderListEntity> robbedList=new ArrayList<OrderListEntity>();//处理中
	//private List<OrderListEntity> payedList=new ArrayList<OrderListEntity>();
	private List<OrderListEntity> cancleList=new ArrayList<OrderListEntity>();//已取消
	private List<OrderListEntity> invalidList=new ArrayList<OrderListEntity>();//已作废
	private List<OrderListEntity> finishList=new ArrayList<OrderListEntity>();//待评价
	private List<OrderListEntity> commentList=new ArrayList<OrderListEntity>();//已评价
	//用户监听当前的页面是哪个一个
	private WasherHistoryFormActivity formActivity;
	//用户监听当前的页面是哪个一个
	private int currentPage=0;
	/**token值错误*/
	private final int TOKEN_ERROR=2;
	/**加载完一轮数据*/
	private final int LOADED_THIS_DATA=10;
	/**取消订单成功*/
	private final int CANCLEORDER_SUCCESS=20;
	/**全部、清洗中、取消、作废、待评价、已评价*/
	private final int[] LOADN_STATUS={20,21,22,23,24,25};
	/**全部、清洗中、取消、作废、待评价、已评价*/
	private final String[] load_status={"all","robbed","finish","comment","cancle","invalid"};
	//{ "全部", "待付款", "派单中", "清洗中", "待评价", "已完成", "已取消", "已作废" };
	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		tabIndex = getArguments().getInt(INTENT_INT_INDEX);
		setContentView(R.layout.fragment_more);
		//监听当前页面是哪一个页面
		formActivity=(WasherHistoryFormActivity) getActivity();
		formActivity.setCurrentPageWatchListner(new com.brother.yckx.control.activity.washer.WasherHistoryFormActivity.CurrentPageWatchListener(){
			@Override
			public void onCurrentPageWatchListener(int currenPage) {
				currentPage=currenPage;
			}});
		adapter=new MoreFragmentAdapter(getActivity());
		listview=(LoadListView)findViewById(R.id.listView);
		listview.setInterface(this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(listener);
		progressBar=(ProgressBar) findViewById(R.id.progressBar);
		//listview.addFooterView(v);添加在底部的数据
		switch (tabIndex) {
		case 0://全部
			getOrderListInbackground("all");
			Log.d("ycxk", "select tab1");
			break;
		case 1://处理中
			getOrderListInbackground("robbed");
			Log.d("ycxk", "select tab2");
			break;
		case 2://待评价
			getOrderListInbackground("finish");
			Log.d("ycxk", "select tab3");
			break;
		case 3://已评价
			getOrderListInbackground("comment");
			break;
		case 4://已取消
			getOrderListInbackground("cancle");
			break;
		case 5://已作废
			getOrderListInbackground("invalid");
			break;
		}

	}

	private OnItemClickListener listener=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent=new Intent(getActivity(),FormDetailActivity.class);
			//标注进入订单详情的入口时订单页面
			GlobalServiceUtils.setGloubalServiceSession("FormDetailActivity", "FormActivity");
			switch (tabIndex) {
			case 0:
				GlobalServiceUtils.setGloubalServiceSession("orderId",allList.get(arg2).getId());
				break;
			
			case 1:
				GlobalServiceUtils.setGloubalServiceSession("orderId",robbedList.get(arg2).getId());
				break;
			case 2:
				GlobalServiceUtils.setGloubalServiceSession("orderId",cancleList.get(arg2).getId());
				break;
			case 3:
				GlobalServiceUtils.setGloubalServiceSession("orderId",invalidList.get(arg2).getId());
				break;
			case 4:
				GlobalServiceUtils.setGloubalServiceSession("orderId",finishList.get(arg2).getId());
				break;
			case 5:
				GlobalServiceUtils.setGloubalServiceSession("orderId",commentList.get(arg2).getId());
				break;
			}
			startActivity(intent);
		}};

		/**调用了哪个接口*/
		private int jiekou=-10;
		/**加载全部数据*/
		private int page=0;
		private int pageSize=8;
		/**加载完全部数据*/
		private void getOrderListInbackground(final String status){
			//标志调用了哪个接口
			for(int i=0;i<load_status.length;i++){
				if(load_status[i].equals(status)){
					jiekou=LOADN_STATUS[i];
				}
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					//我的订单接口,订单列表/order/list/{page}/{size}?status=xxxx(create、robbed、payed、cancel、invalid、finish、commend)
					String unpayUrl="";
					if(status.equals("all")){
						unpayUrl=WEBInterface.MYORDERLISTURL+"/"+page+"/"+pageSize;
					}else{
						unpayUrl=WEBInterface.MYORDERLISTURL+"/"+page+"/"+pageSize+"?status="+status;
					}
					String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
					String respose=ApacheHttpUtil.httpGet(unpayUrl, token, null);
					//http://112.74.18.34:80/wc/order/list/0/1/status=create
					try {
						JSONObject jSONObject=new JSONObject(respose);
						String code=jSONObject.getString("code");
						if(code.equals("2")){
							Message msg=new Message();
							msg.what=TOKEN_ERROR;
							mHandler.sendMessage(msg);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d("yckx","需要调用的接口"+jiekou);
					Log.d("yckx", "---->>"+unpayUrl);
					Log.d("yckx", "订单列表数据---->>"+respose);
					//解析数据
					List<OrderListEntity> list=ParseJSONUtils.getOrderListEntity(respose);
					if(list==null||list.size()==0){
						//加载完成
						isCanLoad=false;
						Message msg=new Message();
						msg.what=LOADCOMPLETE;
						mHandler.sendMessage(msg);
					}else{
						isCanLoad=true;
						//刷新ui
						//allList.addAll(list);
						Message msg=new Message();
						msg.what=LOADED_THIS_DATA;
						msg.arg1=jiekou;
						msg.obj=list;
						mHandler.sendMessage(msg);
					}

				}
			}).start();
		}


		@Override
		public void onLoad() {
			//加载数据
			if(isCanLoad){
				page++;
				//"all","create","payed","robbed","finish","comment","cancle","invalid"
				switch (jiekou) {
				case 20:
					getOrderListInbackground("all");
					break;
				case 21:
					getOrderListInbackground("robbed");
					break;
				case 22:
					getOrderListInbackground("finish");
					break;
				case 23:
					getOrderListInbackground("comment");
					break;
				case 24:
					getOrderListInbackground("cancle");
					break;
				case 25:
					getOrderListInbackground("invalid");
					break;
				}

			}else{//已经加载完成，不能再加载
				ToastUtil.show(getApplicationContext(), "已经全部加载完成", 2000);
				progressBar.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				listview.loadComplete();
				//isCanLoad=true;
			}
		}

		/**是否继续还能继续加载*/
		private boolean isCanLoad=true;
		/**没有数据*/
		private final int LOADCOMPLETE=12;

		/**加载全部完成*/
		private final int LOADALL_SUCCESS=0;
		private Handler mHandler=new Handler(){
			@SuppressLint("HandlerLeak") public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case TOKEN_ERROR://进入登录界面
					goToLogin();
					break;
				case LOADALL_SUCCESS:
					progressBar.setVisibility(View.VISIBLE);
					listview.setVisibility(View.VISIBLE);
					adapter.clearAdapter();
					adapter.addDataToAdapter(allList);
					adapter.notifyDataSetChanged();
					//加载完成
					listview.loadComplete();
					break;
				case LOADCOMPLETE:
					progressBar.setVisibility(View.GONE);
					listview.setVisibility(View.VISIBLE);
					listview.loadComplete();
					break;
				case LOADED_THIS_DATA:
					@SuppressWarnings("unchecked")
					List<OrderListEntity> getlist=(List<OrderListEntity>) msg.obj;
					listview.loadComplete();
					{switch (msg.arg1) {
					case 20://"all","create","payed","robbed","finish","comment","cancle","invalid"
						Log.d("yckx","已经调用了:all"+msg.arg1);
						allList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(allList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					case 21:
						Log.d("yckx","已经调用了:robbed"+msg.arg1);
						robbedList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(robbedList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					case 22:
						Log.d("yckx","已经调用了:finish"+msg.arg1);
						cancleList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(cancleList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					case 23:
						Log.d("yckx","已经调用了:comment"+msg.arg1);
						invalidList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(invalidList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					case 24:
						Log.d("yckx","已经调用了:cancle"+msg.arg1);
						finishList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(finishList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					case 25:
						Log.d("yckx","已经调用了:invalid"+msg.arg1);
						commentList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(commentList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					}}
					break;
				case CANCLEORDER_SUCCESS://取消订单成功,取消成功，重新刷新数据
					ToastUtil.show(getApplicationContext(), "该订单已取消", 2000);
					//判断要加载哪一个页面
					{
						switch (currentPage) {
						case 0:
							allList.clear();
							getOrderListInbackground("all");
							break;
						case 1:
							robbedList.clear();
							getOrderListInbackground("robbed");
							break;
						case 2:
							finishList.clear();
							getOrderListInbackground("finish");
							break;
						case 3:
							commentList.clear();
							getOrderListInbackground("comment");
							break;
						case 4:
							cancleList.clear();
							getOrderListInbackground("cancle");
							break;
						case 5:
							invalidList.clear();
							getOrderListInbackground("invalid");
							break;
						}
					}
					break;
				}
			};
		};


		/**进入登录界面*/
		private void goToLogin(){
			Intent intent=new Intent(getActivity(),LoginActivity.class);
			startActivity(intent);
		}

		//---listView 设置下拉刷新

		class MoreFragmentAdapter extends BaseAdapter2<OrderListEntity>{


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

			public MoreFragmentAdapter(Context context) {
				super(context);
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				if(arg1==null){
					arg1=layoutInflater.inflate(R.layout.row_washer_frgment_formdetails, null);
				}
				ImageView leagueNameImg=(ImageView) arg1.findViewById(R.id.leagueNameImg);
				TextView leagueName=(TextView) arg1.findViewById(R.id.leagueName);
				CircleImageView productImg=(CircleImageView) arg1.findViewById(R.id.productImg);
				TextView productName_static=(TextView) arg1.findViewById(R.id.productName_static);
				TextView doorService=(TextView) arg1.findViewById(R.id.doorService);
				TextView productTime=(TextView) arg1.findViewById(R.id.productTime);
				TextView productPrice=(TextView) arg1.findViewById(R.id.productPrice);
				TextView productCard=(TextView) arg1.findViewById(R.id.productCard);
				TextView productPhone=(TextView) arg1.findViewById(R.id.productPhone);
				TextView productStatus=(TextView) arg1.findViewById(R.id.productStatus);
				TextView actualPay=(TextView) arg1.findViewById(R.id.actualPay);

				//
				TextView payAtonce=(TextView) arg1.findViewById(R.id.pay_atonce);
				TextView evaluate_atonce=(TextView) arg1.findViewById(R.id.evaluate_atonce);
				TextView sendVoice=(TextView) arg1.findViewById(R.id.sendVoice); 
				TextView callPhone=(TextView) arg1.findViewById(R.id.callPhone); 
				TextView cancelProduce=(TextView) arg1.findViewById(R.id.cancelProduce);

				payAtonce.setOnClickListener(new Listener(arg0));
				evaluate_atonce.setOnClickListener(new Listener(arg0));
				sendVoice.setOnClickListener(new Listener(arg0));
				callPhone.setOnClickListener(new Listener(arg0));
				cancelProduce.setOnClickListener(new Listener(arg0));

				OrderListEntity entity=getItem(arg0);
				leagueName.setText(entity.getCompannyEntity().getCompanyName());
				productName_static.setText(""+entity.getProductEntity().getProductName());
				String temp=entity.getDoorService();//是否到点服务
				if(temp.equals("false")){
					doorService.setText("到店享受");
				}else{
					doorService.setText("上门服务");
				}
				if(entity.getServiceTime()!=null&&!entity.getServiceTime().equals("")){
					productTime.setText(TimeUtils.millsToDateTime(entity.getServiceTime()));
				}
				productPrice.setText(entity.getOrderPrice());
				productCard.setText(entity.getCarEntity().getCarBrand()+entity.getCarEntity().getCarColor()+"|"+entity.getCarEntity().getProvince()+entity.getCarEntity().getCarNum());
				productPhone.setText(entity.getCompannyEntity().getCompanyPhone());
				//订单状态(根据订单状态显示底部按钮)
				String mOrderStatus=entity.getOrderStatus();
				if(mOrderStatus.equals("create")){
					payAtonce.setVisibility(View.VISIBLE);
					cancelProduce.setVisibility(View.VISIBLE);
					evaluate_atonce.setVisibility(View.GONE);
					sendVoice.setVisibility(View.GONE);
					callPhone.setVisibility(View.GONE);
					mOrderStatus="待支付";
				}
				if(mOrderStatus.equals("robbed")){
					payAtonce.setVisibility(View.GONE);
					cancelProduce.setVisibility(View.GONE);
					evaluate_atonce.setVisibility(View.GONE);
					sendVoice.setVisibility(View.VISIBLE);
					callPhone.setVisibility(View.VISIBLE);
					mOrderStatus="清洗中";
				}
				if(mOrderStatus.equals("payed")){
					payAtonce.setVisibility(View.GONE);
					cancelProduce.setVisibility(View.VISIBLE);
					evaluate_atonce.setVisibility(View.GONE);
					sendVoice.setVisibility(View.GONE);
					callPhone.setVisibility(View.GONE);
					mOrderStatus="待接单";
				}
				if(mOrderStatus.equals("cancle")){
					payAtonce.setVisibility(View.GONE);
					cancelProduce.setVisibility(View.GONE);
					evaluate_atonce.setVisibility(View.GONE);
					sendVoice.setVisibility(View.VISIBLE);
					callPhone.setVisibility(View.VISIBLE);
					mOrderStatus="取消";
				}
				if(mOrderStatus.equals("invalid")){
					payAtonce.setVisibility(View.GONE);
					cancelProduce.setVisibility(View.GONE);
					evaluate_atonce.setVisibility(View.GONE);
					sendVoice.setVisibility(View.GONE);
					callPhone.setVisibility(View.GONE);
					mOrderStatus="作废";
				}
				if(mOrderStatus.equals("finish")){
					payAtonce.setVisibility(View.GONE);
					cancelProduce.setVisibility(View.GONE);
					evaluate_atonce.setVisibility(View.GONE);
					sendVoice.setVisibility(View.VISIBLE);
					callPhone.setVisibility(View.VISIBLE);
					mOrderStatus="待评价";
				}
				if(mOrderStatus.equals("comment")){
					evaluate_atonce.setVisibility(View.VISIBLE);
					mOrderStatus="已评价";
				}
				productStatus.setText(mOrderStatus);
				actualPay.setText(entity.getOrderPrice());

				return arg1;
			}}

		private class Listener implements View.OnClickListener{
			private int position=-1;
			public Listener(int position){
				this.position=position;
			}
			@SuppressLint("SdCardPath")
			@Override
			public void onClick(View arg0) {
				switch (arg0.getId()) {
				case R.id.pay_atonce://立即支付
					OrderListEntity orders=adapter.getItem(position);
					Intent intent_pay=new Intent(getActivity(),PayMoneyActivity.class);
					intent_pay.putExtra("PayMoneyActivity", orders.getId());
					startActivity(intent_pay);
					getActivity().finish();
					break;
				case R.id.evaluate_atonce://评论
					OrderListEntity orders_evaluate=adapter.getItem(position);
					//判断评论时间是否已经过期
					//当前的时间戳
					Date todateDate=new Date();
					long todateTime = todateDate.getTime();
					//订单完成时间
					String completeTime=orders_evaluate.getOrderFinishTime();
					long mPromissTime=Long.parseLong(completeTime);
					long betweenDays = (long)((todateTime-mPromissTime) / (1000 * 60 * 60 *24) + 0.5);
					if(betweenDays>3){
						ToastUtil.show(getApplicationContext(), "已过期，不能进行评论", 2000);
						return;
					}
					//截取当前页面
					Bitmap bitmap=MyBitmapUtils.takeScreenShot(getActivity());
					MyBitmapUtils.savePic(bitmap, "/sdcard/yckx/yckx_temp.png");
					Log.d("yckx","--待评论的订单"+orders_evaluate.toString());
					Intent intent=new Intent(getActivity(),WasherFeedbackActivity.class);
					intent.putExtra("WasherFeedbackActivity", orders_evaluate.getId());
					startActivity(intent);
					break;
				case R.id.sendVoice:
					OrderListEntity orders_sendvoice=adapter.getItem(position);
					if(orders_sendvoice.getWasherEntity()==null||orders_sendvoice.getWasherEntity().getWaherId().equals("")){
						ToastUtil.show(getApplicationContext(), "美护师接单前您已取消订单,没有美护师信息", 2000);
					}else{
						RongIM.getInstance().startPrivateChat(getActivity(), orders_sendvoice.getOwnerEntity().getId(), "title");
					}
					break;
				case R.id.callPhone:
					OrderListEntity ordersPhone=adapter.getItem(position);
						String phoneNo=ordersPhone.getOwnerEntity().getUserPhone();
						Intent intentPhone = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
						getActivity().startActivity(intentPhone);
					break;
				case R.id.cancelProduce://取消订单/order/cancel/{orderId}
					OrderListEntity orders_cancle=adapter.getItem(position);
					ownCancelOrder(orders_cancle.getId());
					break;
				}
			}}


		/**取消订单*/
		private void ownCancelOrder(final String orderId){
			new Thread(new Runnable() {
				@Override
				public void run() {
					String host=WEBInterface.ORDER_CANCEL_URL+orderId;
					String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
					String response=ApacheHttpUtil.httpPost(host, token, null);
					Log.d("yckx","取消订单返回数据"+response);
					if(response!=null){
						try {
							JSONObject jsonOBJ=new JSONObject(response);
							String code=jsonOBJ.getString("code");
							if(code.equals("0")){
								//取消成功
								Message msg=new Message();
								msg.what=CANCLEORDER_SUCCESS;
								mHandler.sendMessage(msg);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
}

