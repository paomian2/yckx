package com.brother.yckx.control.frgament;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.androide.lib3.volley.AuthFailureError;
import com.androide.lib3.volley.RequestQueue;
import com.androide.lib3.volley.Response;
import com.androide.lib3.volley.VolleyError;
import com.androide.lib3.volley.toolbox.JsonObjectRequest;
import com.androidy.lib3.view.pulltorefresh.library.PullToRefreshBase;
import com.androidy.lib3.view.pulltorefresh.library.PullToRefreshListView;
import com.androidy.lib3.view.pulltorefresh.library.PullToRefreshBase.Mode;
import com.brother.BaseAdapter2;
import com.brother.BaseFragment;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.TimeUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.model.OrderListEntity;
import com.brother.yckx.view.CircleImageView;

@SuppressLint("ValidFragment")
public class YCKXClassFragmentOrders extends BaseFragment{
	private PullToRefreshListView listView;
	private OrderlistInfo ordersAdapter;
	private int typeId;

	public YCKXClassFragmentOrders(int typeId){
		this.typeId=typeId;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		/*getVolleyRequestQueue().cancelAll(getClass().getSimpleName());
		// 刷新完成
		listView.onRefreshComplete();*/
	}

	@Override
	public View getAndInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 加载新闻Fragment视图效果
		View view = inflater.inflate(R.layout.fragment_orderclass_order_list, null);
		// 初始新闻Fragment视图上ListView内容(@see newsAdapter)
		/*listView = (PullToRefreshListView) view.findViewById(R.id.refreshListview);
		listView.setOnItemClickListener(listItemlistener);
		ordersAdapter = new OrderlistInfo(getActivity());
		listView.setMode(Mode.BOTH);
		listView.setAdapter(ordersAdapter);
		listView.setOnRefreshListener(refreshListener); // 监听
*/
		return view;
	}

	@Override
	public void loadData() {
		/*// Volley网络请求数据(默认20条最新数据的web接口)
		RequestQueue requestQueue = getVolleyRequestQueue();
		//http://112.74.18.34:80/wc/order/list/1/3?status=未支付
		String url=WEBInterface.MYORDERLISTURL+"/"+typeId+"/3";//全部
		JsonObjectRequest request = new JsonObjectRequest(url, null,  
				new Response.Listener<JSONObject>() {
			@Override  
			public void onResponse(JSONObject response) {  
				try {  
					List<OrderListEntity> datas=ParseJSONUtils.getOrderListEntity_volly(response);
					// 将数据加入到adapter
					ordersAdapter.setDataToAdapter(datas);
					// adapter刷新(通知adapter数据已更新,观察者模式)
					ordersAdapter.notifyDataSetChanged();
				} catch (Exception e) {  
					e.printStackTrace();  
				}  
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) { 
				ToastUtil.show(getActivity(), "数据获取失败", 800);
				listView.onRefreshComplete(); // 刷新完成
			}  
		}) {  
			@Override  
			public Map<String, String> getHeaders() throws AuthFailureError {  
				HashMap<String, String> headers = new HashMap<String, String>();
				String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
				headers.put("token", token);
				return headers;  
			}  
		};  
		request.setTag(getClass().getSimpleName());
		requestQueue.add(request);*/
	}

	@Override
	public void resetData() {
		// TODO Auto-generated method stub

	}

	private Response.ErrorListener errorListener = new Response.ErrorListener(){
		@Override
		public void onErrorResponse(VolleyError error) {
			ToastUtil.show(getActivity(), "数据获取失败", 800);
			listView.onRefreshComplete(); // 刷新完成
		}
	};


	private PullToRefreshBase.OnRefreshListener2<ListView> refreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>(){
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 没有数据的情况下，去获取最新数据
			if (ordersAdapter.getCount() <= 0) {
				loadData();
				return;
			}
			loadNextData();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 没有数据的情况下，去获取最新数据
			/*if (newsAdapter.getCount() <= 0) {
				loadData();
				return;
			}*/
			loadPreData();
		}
	};

   // private int 
	private void loadNextData() {
		// 下拉（取最新的), 取第一条ID之后的ID
		//long firstID = newsAdapter.getItem(0).getNid();
		//String url = WEBInterface.NEWSURL_NEXT + "?typeId=" + typeId + "&curId=" + firstID + "&size=20";
		// Volley网络请求数据(默认20条最新数据的web接口)
		/*String url="";
		RequestQueue requestQueue = getVolleyRequestQueue();
		JsonObjectRequest request = new JsonObjectRequest(url, 
				new Response.Listener<JSONObject>(){
			@Override
			public void onResponse(JSONObject response) {
				try {
					// 解析出此JSONObject
					List<NewslistInfo> datas = WEBJosnParseNews.parstJson(response);
						// 将数据加入到adapter
						newsAdapter.addDataToAdapterHead(datas);
						// adapter刷新(通知adapter数据已更新,观察者模式)
						newsAdapter.notifyDataSetChanged();
				}
				catch (Exception e) {
					ToastUtil.show(getActivity(), "数据解析异常", 800);
				}
				listView.onRefreshComplete(); // 刷新完成
			}
		}, errorListener);
		request.setTag(getClass().getSimpleName());
		requestQueue.add(request);*/
	}


	private void loadPreData() {
		/*// 上拉（取更老的), 取最后一条ID之前ID
		//	long lastID = newsAdapter.getItem(newsAdapter.getCount() - 1).getNid();
		//	String url = WEBInterface.NEWSURL_PRE + "?typeId=" + typeId + "&curId=" + lastID + "&size=20";
		String url="";
		// Volley网络请求数据(默认20条最新数据的web接口)
		RequestQueue requestQueue = getVolleyRequestQueue();
		JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>(){
			@Override
			public void onResponse(JSONObject response) {
				try {
					// 解析出此JSONObject
						List<NewslistInfo> datas = WEBJosnParseNews.parstJson(response);
					// 将数据加入到adapter
					newsAdapter.addDataToAdapterEnd(datas);
					// adapter刷新(通知adapter数据已更新,观察者模式)
					newsAdapter.notifyDataSetChanged();
				}
				catch (Exception e) {
					ToastUtil.show(getActivity(), "数据解析异常", 800);
				}
				listView.onRefreshComplete(); // 刷新完成
			}
		}, errorListener);
		request.setTag(getClass().getSimpleName());
		requestQueue.add(request);*/
	}

	/** 当前Fragment上控件ListView每个item监听 */
	private OnItemClickListener listItemlistener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 获取到当前adapter上的数据列表集合
			//List<NewslistInfo> datas = newsAdapter.getDataFromAdapter();
			// 取得当前click的实体数据对象
			//NewslistInfo info = datas.get(position);
			// 回调反馈数据给Activity
			// 单击了新闻列表的某一项,回传NewslistInfo实体对象
			// 使得Activity能进行下一步工作(进行此新闻的浏览)
			// @see HomActivity.java
			//callbackActivityWhenFragmentItemClick(R.id.tag_hnewsgroup_enternews, info);
		}
	};
	
	
	class OrderlistInfo extends BaseAdapter2<OrderListEntity>{

		public OrderlistInfo(Context context) {
			super(context);
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1==null){
				arg1=layoutInflater.inflate(R.layout.row_frgment_formdetails, null);
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
				evaluate_atonce.setVisibility(View.VISIBLE);
				sendVoice.setVisibility(View.VISIBLE);
				callPhone.setVisibility(View.VISIBLE);
				mOrderStatus="待评价";
			}
			if(mOrderStatus.equals("comment")){
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
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.pay_atonce://立即支付
				break;
			case R.id.evaluate_atonce:
				ToastUtil.show(getActivity().getApplicationContext(), "立即评论"+position, 3000);
				break;
			case R.id.sendVoice:
				ToastUtil.show(getActivity().getApplicationContext(), "发送语音"+position, 3000);
				break;
			case R.id.callPhone:
				ToastUtil.show(getActivity().getApplicationContext(), "拨打电话"+position, 3000);
				break;
			case R.id.cancelProduce:
				ToastUtil.show(getActivity().getApplicationContext(), "取消订单"+position, 3000);
				break;
			}
		}}

}
