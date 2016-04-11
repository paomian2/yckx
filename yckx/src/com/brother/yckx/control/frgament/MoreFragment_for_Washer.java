package com.brother.yckx.control.frgament;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.brother.BaseAdapter2;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.TimeUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.FormDetailActivity;
import com.brother.yckx.control.activity.washer.WasherOderDetailActivity;
import com.brother.yckx.model.OrderListEntity;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.LoadListView;
import com.brother.yckx.view.LoadListView.ILoadListener;
import com.shizhefei.fragment.LazyFragment;

public class MoreFragment_for_Washer extends LazyFragment implements ILoadListener{
	private int tabIndex;
	public static final String INTENT_INT_INDEX = "intent_int_index";
	private LoadListView listview;
	private MoreFragmentAdapter adapter;
	private List<OrderListEntity> allList=new ArrayList<OrderListEntity>();


	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		tabIndex = getArguments().getInt(INTENT_INT_INDEX);
		setContentView(R.layout.fragment_more);
		adapter=new MoreFragmentAdapter(getActivity());
		listview=(LoadListView)findViewById(R.id.listView);
		listview.setInterface(this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(listener);
		//listview.addFooterView(v);添加在底部的数据
		switch (tabIndex) {
		case 0://全部
			getOrderListInbackground();
			break;
		case 1://处理中
			break;
		case 2://待评价
			break;
		case 3://已完成
			break;
		case 4://已取消
			break;
		}

	}

	private OnItemClickListener listener=new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			switch (tabIndex) {
			case 0:
				Intent intent=new Intent(getActivity(),WasherOderDetailActivity.class);
				OrderListEntity orderListEntity=allList.get(arg2);
				GlobalServiceUtils.getInstance().setGloubalServiceOrderListEntity(orderListEntity);
				intent.putExtra("WasherOderDetailActivity", "MoreFragment_for_Washer");
				startActivity(intent);
				break;

			default:
				break;
			}

		}};


		/**加载全部数据*/
		private int page=0;
		private int pageSize=1;
		/**加载完全部数据*/
		private void getOrderListInbackground(){
			new Thread(new Runnable() {
				@Override
				public void run() {
					//我的订单接口,订单列表/order/list/{page}/{size}?status=xxxx(create、robbed、payed、cancel、invalid、finish、commend)
					String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
					String unpayUrl=WEBInterface.MYORDERLISTURL+"/"+page+"/"+pageSize;
					String respose=ApacheHttpUtil.httpGet(unpayUrl, token, null);
					Log.d("yckx", "---->>"+unpayUrl);
					Log.d("yckx", "历史订单---->>"+respose);
					//解析数据
					List<OrderListEntity> list=ParseJSONUtils.getOrderListEntity(respose);
					if(list==null||list.size()==0){
						//加载完成
						Message msg=new Message();
						msg.what=LOADCOMPLETE;
						mHandler.sendMessage(msg);
					}else{
						//刷新ui
						allList.addAll(list);
						Message msg=new Message();
						mHandler.sendMessage(msg);
					}

				}
			}).start();
		}


		@Override
		public void onLoad() {
			//加载数据
			page++;
			getOrderListInbackground();
		}

		/**没有数据*/
		private final int LOADCOMPLETE=10;

		/**加载全部完成*/
		private final int LOADALL_SUCCESS=0;
		private Handler mHandler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case LOADALL_SUCCESS:
					adapter.addDataToAdapter(allList);
					adapter.notifyDataSetChanged();
					//加载完成
					listview.loadComplete();
					break;
				case LOADCOMPLETE:
					ToastUtil.show(getApplicationContext(), "已经全部加载完成", 2000);
					listview.loadComplete();
					break;
				}
			};
		};

		//---listView 设置下拉刷新

		class MoreFragmentAdapter extends BaseAdapter2<OrderListEntity>{

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
			
			public MoreFragmentAdapter(Context context) {
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
				//FormDetalisEntity entity=new FormDetalisEntity(leagueNameImg, leagueName, productImg, productName, productTime, productPrice, productCard, productPhone, productStatus, actualPay);
				OrderListEntity entity=getItem(arg0);
				//leagueNameImg.setImageDrawable(entity.getLeagueNameImg());
				leagueName.setText(entity.getCompannyEntity().getCompanyName());
				//productImg.setImageDrawable(entity.getProductImg());
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
				productCard.setText(entity.getCarEntity().getCarBrand()+"|"+entity.getCarEntity().getCarNum());
				productPhone.setText(entity.getCompannyEntity().getCompanyPhone());
				productStatus.setText(entity.getOrderStatus());
				actualPay.setText(entity.getOrderPrice());

				//
				TextView evaluate_atonce=(TextView) arg1.findViewById(R.id.evaluate_atonce);
				TextView sendVoice=(TextView) arg1.findViewById(R.id.sendVoice); 
				TextView callPhone=(TextView) arg1.findViewById(R.id.callPhone); 
				TextView cancelProduce=(TextView) arg1.findViewById(R.id.cancelProduce);

				evaluate_atonce.setOnClickListener(new Listener(arg0));
				sendVoice.setOnClickListener(new Listener(arg0));
				callPhone.setOnClickListener(new Listener(arg0));
				cancelProduce.setOnClickListener(new Listener(arg0));
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
				case R.id.evaluate_atonce:
					ToastUtil.show(getApplicationContext(), "立即评论"+position, 3000);
					break;
				case R.id.sendVoice:
					ToastUtil.show(getApplicationContext(), "发送语音"+position, 3000);
					break;
				case R.id.callPhone:
					ToastUtil.show(getApplicationContext(), "拨打电话"+position, 3000);
					break;
				case R.id.cancelProduce:
					ToastUtil.show(getApplicationContext(), "取消订单"+position, 3000);
					break;
				}
			}}


}
