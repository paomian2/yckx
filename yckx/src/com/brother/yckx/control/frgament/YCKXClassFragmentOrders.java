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
		// ˢ�����
		listView.onRefreshComplete();*/
	}

	@Override
	public View getAndInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ��������Fragment��ͼЧ��
		View view = inflater.inflate(R.layout.fragment_orderclass_order_list, null);
		// ��ʼ����Fragment��ͼ��ListView����(@see newsAdapter)
		/*listView = (PullToRefreshListView) view.findViewById(R.id.refreshListview);
		listView.setOnItemClickListener(listItemlistener);
		ordersAdapter = new OrderlistInfo(getActivity());
		listView.setMode(Mode.BOTH);
		listView.setAdapter(ordersAdapter);
		listView.setOnRefreshListener(refreshListener); // ����
*/
		return view;
	}

	@Override
	public void loadData() {
		/*// Volley������������(Ĭ��20���������ݵ�web�ӿ�)
		RequestQueue requestQueue = getVolleyRequestQueue();
		//http://112.74.18.34:80/wc/order/list/1/3?status=δ֧��
		String url=WEBInterface.MYORDERLISTURL+"/"+typeId+"/3";//ȫ��
		JsonObjectRequest request = new JsonObjectRequest(url, null,  
				new Response.Listener<JSONObject>() {
			@Override  
			public void onResponse(JSONObject response) {  
				try {  
					List<OrderListEntity> datas=ParseJSONUtils.getOrderListEntity_volly(response);
					// �����ݼ��뵽adapter
					ordersAdapter.setDataToAdapter(datas);
					// adapterˢ��(֪ͨadapter�����Ѹ���,�۲���ģʽ)
					ordersAdapter.notifyDataSetChanged();
				} catch (Exception e) {  
					e.printStackTrace();  
				}  
			}  
		}, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) { 
				ToastUtil.show(getActivity(), "���ݻ�ȡʧ��", 800);
				listView.onRefreshComplete(); // ˢ�����
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
			ToastUtil.show(getActivity(), "���ݻ�ȡʧ��", 800);
			listView.onRefreshComplete(); // ˢ�����
		}
	};


	private PullToRefreshBase.OnRefreshListener2<ListView> refreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>(){
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// û�����ݵ�����£�ȥ��ȡ��������
			if (ordersAdapter.getCount() <= 0) {
				loadData();
				return;
			}
			loadNextData();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// û�����ݵ�����£�ȥ��ȡ��������
			/*if (newsAdapter.getCount() <= 0) {
				loadData();
				return;
			}*/
			loadPreData();
		}
	};

   // private int 
	private void loadNextData() {
		// ������ȡ���µ�), ȡ��һ��ID֮���ID
		//long firstID = newsAdapter.getItem(0).getNid();
		//String url = WEBInterface.NEWSURL_NEXT + "?typeId=" + typeId + "&curId=" + firstID + "&size=20";
		// Volley������������(Ĭ��20���������ݵ�web�ӿ�)
		/*String url="";
		RequestQueue requestQueue = getVolleyRequestQueue();
		JsonObjectRequest request = new JsonObjectRequest(url, 
				new Response.Listener<JSONObject>(){
			@Override
			public void onResponse(JSONObject response) {
				try {
					// ��������JSONObject
					List<NewslistInfo> datas = WEBJosnParseNews.parstJson(response);
						// �����ݼ��뵽adapter
						newsAdapter.addDataToAdapterHead(datas);
						// adapterˢ��(֪ͨadapter�����Ѹ���,�۲���ģʽ)
						newsAdapter.notifyDataSetChanged();
				}
				catch (Exception e) {
					ToastUtil.show(getActivity(), "���ݽ����쳣", 800);
				}
				listView.onRefreshComplete(); // ˢ�����
			}
		}, errorListener);
		request.setTag(getClass().getSimpleName());
		requestQueue.add(request);*/
	}


	private void loadPreData() {
		/*// ������ȡ���ϵ�), ȡ���һ��ID֮ǰID
		//	long lastID = newsAdapter.getItem(newsAdapter.getCount() - 1).getNid();
		//	String url = WEBInterface.NEWSURL_PRE + "?typeId=" + typeId + "&curId=" + lastID + "&size=20";
		String url="";
		// Volley������������(Ĭ��20���������ݵ�web�ӿ�)
		RequestQueue requestQueue = getVolleyRequestQueue();
		JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>(){
			@Override
			public void onResponse(JSONObject response) {
				try {
					// ��������JSONObject
						List<NewslistInfo> datas = WEBJosnParseNews.parstJson(response);
					// �����ݼ��뵽adapter
					newsAdapter.addDataToAdapterEnd(datas);
					// adapterˢ��(֪ͨadapter�����Ѹ���,�۲���ģʽ)
					newsAdapter.notifyDataSetChanged();
				}
				catch (Exception e) {
					ToastUtil.show(getActivity(), "���ݽ����쳣", 800);
				}
				listView.onRefreshComplete(); // ˢ�����
			}
		}, errorListener);
		request.setTag(getClass().getSimpleName());
		requestQueue.add(request);*/
	}

	/** ��ǰFragment�Ͽؼ�ListViewÿ��item���� */
	private OnItemClickListener listItemlistener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// ��ȡ����ǰadapter�ϵ������б���
			//List<NewslistInfo> datas = newsAdapter.getDataFromAdapter();
			// ȡ�õ�ǰclick��ʵ�����ݶ���
			//NewslistInfo info = datas.get(position);
			// �ص��������ݸ�Activity
			// �����������б��ĳһ��,�ش�NewslistInfoʵ�����
			// ʹ��Activity�ܽ�����һ������(���д����ŵ����)
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
			String temp=entity.getDoorService();//�Ƿ񵽵����
			if(temp.equals("false")){
				doorService.setText("��������");
			}else{
				doorService.setText("���ŷ���");
			}
			if(entity.getServiceTime()!=null&&!entity.getServiceTime().equals("")){
				productTime.setText(TimeUtils.millsToDateTime(entity.getServiceTime()));
			}
			productPrice.setText(entity.getOrderPrice());
			productCard.setText(entity.getCarEntity().getCarBrand()+entity.getCarEntity().getCarColor()+"|"+entity.getCarEntity().getProvince()+entity.getCarEntity().getCarNum());
			productPhone.setText(entity.getCompannyEntity().getCompanyPhone());
			//����״̬(���ݶ���״̬��ʾ�ײ���ť)
			String mOrderStatus=entity.getOrderStatus();
			if(mOrderStatus.equals("create")){
				payAtonce.setVisibility(View.VISIBLE);
				cancelProduce.setVisibility(View.VISIBLE);
				evaluate_atonce.setVisibility(View.GONE);
				sendVoice.setVisibility(View.GONE);
				callPhone.setVisibility(View.GONE);
				mOrderStatus="��֧��";
			}
			if(mOrderStatus.equals("robbed")){
				payAtonce.setVisibility(View.GONE);
				cancelProduce.setVisibility(View.GONE);
				evaluate_atonce.setVisibility(View.GONE);
				sendVoice.setVisibility(View.VISIBLE);
				callPhone.setVisibility(View.VISIBLE);
				mOrderStatus="��ϴ��";
			}
			if(mOrderStatus.equals("payed")){
				payAtonce.setVisibility(View.GONE);
				cancelProduce.setVisibility(View.VISIBLE);
				evaluate_atonce.setVisibility(View.GONE);
				sendVoice.setVisibility(View.GONE);
				callPhone.setVisibility(View.GONE);
				mOrderStatus="���ӵ�";
			}
			if(mOrderStatus.equals("cancle")){
				payAtonce.setVisibility(View.GONE);
				cancelProduce.setVisibility(View.GONE);
				evaluate_atonce.setVisibility(View.GONE);
				sendVoice.setVisibility(View.VISIBLE);
				callPhone.setVisibility(View.VISIBLE);
				mOrderStatus="ȡ��";
			}
			if(mOrderStatus.equals("invalid")){
				payAtonce.setVisibility(View.GONE);
				cancelProduce.setVisibility(View.GONE);
				evaluate_atonce.setVisibility(View.GONE);
				sendVoice.setVisibility(View.GONE);
				callPhone.setVisibility(View.GONE);
				mOrderStatus="����";
			}
			if(mOrderStatus.equals("finish")){
				payAtonce.setVisibility(View.GONE);
				cancelProduce.setVisibility(View.GONE);
				evaluate_atonce.setVisibility(View.VISIBLE);
				sendVoice.setVisibility(View.VISIBLE);
				callPhone.setVisibility(View.VISIBLE);
				mOrderStatus="������";
			}
			if(mOrderStatus.equals("comment")){
				mOrderStatus="������";
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
			case R.id.pay_atonce://����֧��
				break;
			case R.id.evaluate_atonce:
				ToastUtil.show(getActivity().getApplicationContext(), "��������"+position, 3000);
				break;
			case R.id.sendVoice:
				ToastUtil.show(getActivity().getApplicationContext(), "��������"+position, 3000);
				break;
			case R.id.callPhone:
				ToastUtil.show(getActivity().getApplicationContext(), "����绰"+position, 3000);
				break;
			case R.id.cancelProduce:
				ToastUtil.show(getActivity().getApplicationContext(), "ȡ������"+position, 3000);
				break;
			}
		}}

}
