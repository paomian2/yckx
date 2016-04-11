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
	private List<OrderListEntity> allList=new ArrayList<OrderListEntity>();//ȫ��
	//private List<OrderListEntity> createList=new ArrayList<OrderListEntity>();
	private List<OrderListEntity> robbedList=new ArrayList<OrderListEntity>();//������
	//private List<OrderListEntity> payedList=new ArrayList<OrderListEntity>();
	private List<OrderListEntity> cancleList=new ArrayList<OrderListEntity>();//��ȡ��
	private List<OrderListEntity> invalidList=new ArrayList<OrderListEntity>();//������
	private List<OrderListEntity> finishList=new ArrayList<OrderListEntity>();//������
	private List<OrderListEntity> commentList=new ArrayList<OrderListEntity>();//������
	//�û�������ǰ��ҳ�����ĸ�һ��
	private WasherHistoryFormActivity formActivity;
	//�û�������ǰ��ҳ�����ĸ�һ��
	private int currentPage=0;
	/**tokenֵ����*/
	private final int TOKEN_ERROR=2;
	/**������һ������*/
	private final int LOADED_THIS_DATA=10;
	/**ȡ�������ɹ�*/
	private final int CANCLEORDER_SUCCESS=20;
	/**ȫ������ϴ�С�ȡ�������ϡ������ۡ�������*/
	private final int[] LOADN_STATUS={20,21,22,23,24,25};
	/**ȫ������ϴ�С�ȡ�������ϡ������ۡ�������*/
	private final String[] load_status={"all","robbed","finish","comment","cancle","invalid"};
	//{ "ȫ��", "������", "�ɵ���", "��ϴ��", "������", "�����", "��ȡ��", "������" };
	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		tabIndex = getArguments().getInt(INTENT_INT_INDEX);
		setContentView(R.layout.fragment_more);
		//������ǰҳ������һ��ҳ��
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
		//listview.addFooterView(v);����ڵײ�������
		switch (tabIndex) {
		case 0://ȫ��
			getOrderListInbackground("all");
			Log.d("ycxk", "select tab1");
			break;
		case 1://������
			getOrderListInbackground("robbed");
			Log.d("ycxk", "select tab2");
			break;
		case 2://������
			getOrderListInbackground("finish");
			Log.d("ycxk", "select tab3");
			break;
		case 3://������
			getOrderListInbackground("comment");
			break;
		case 4://��ȡ��
			getOrderListInbackground("cancle");
			break;
		case 5://������
			getOrderListInbackground("invalid");
			break;
		}

	}

	private OnItemClickListener listener=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent=new Intent(getActivity(),FormDetailActivity.class);
			//��ע���붩����������ʱ����ҳ��
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

		/**�������ĸ��ӿ�*/
		private int jiekou=-10;
		/**����ȫ������*/
		private int page=0;
		private int pageSize=8;
		/**������ȫ������*/
		private void getOrderListInbackground(final String status){
			//��־�������ĸ��ӿ�
			for(int i=0;i<load_status.length;i++){
				if(load_status[i].equals(status)){
					jiekou=LOADN_STATUS[i];
				}
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					//�ҵĶ����ӿ�,�����б�/order/list/{page}/{size}?status=xxxx(create��robbed��payed��cancel��invalid��finish��commend)
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
					Log.d("yckx","��Ҫ���õĽӿ�"+jiekou);
					Log.d("yckx", "---->>"+unpayUrl);
					Log.d("yckx", "�����б�����---->>"+respose);
					//��������
					List<OrderListEntity> list=ParseJSONUtils.getOrderListEntity(respose);
					if(list==null||list.size()==0){
						//�������
						isCanLoad=false;
						Message msg=new Message();
						msg.what=LOADCOMPLETE;
						mHandler.sendMessage(msg);
					}else{
						isCanLoad=true;
						//ˢ��ui
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
			//��������
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

			}else{//�Ѿ�������ɣ������ټ���
				ToastUtil.show(getApplicationContext(), "�Ѿ�ȫ���������", 2000);
				progressBar.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				listview.loadComplete();
				//isCanLoad=true;
			}
		}

		/**�Ƿ�������ܼ�������*/
		private boolean isCanLoad=true;
		/**û������*/
		private final int LOADCOMPLETE=12;

		/**����ȫ�����*/
		private final int LOADALL_SUCCESS=0;
		private Handler mHandler=new Handler(){
			@SuppressLint("HandlerLeak") public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case TOKEN_ERROR://�����¼����
					goToLogin();
					break;
				case LOADALL_SUCCESS:
					progressBar.setVisibility(View.VISIBLE);
					listview.setVisibility(View.VISIBLE);
					adapter.clearAdapter();
					adapter.addDataToAdapter(allList);
					adapter.notifyDataSetChanged();
					//�������
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
						Log.d("yckx","�Ѿ�������:all"+msg.arg1);
						allList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(allList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					case 21:
						Log.d("yckx","�Ѿ�������:robbed"+msg.arg1);
						robbedList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(robbedList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					case 22:
						Log.d("yckx","�Ѿ�������:finish"+msg.arg1);
						cancleList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(cancleList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					case 23:
						Log.d("yckx","�Ѿ�������:comment"+msg.arg1);
						invalidList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(invalidList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					case 24:
						Log.d("yckx","�Ѿ�������:cancle"+msg.arg1);
						finishList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(finishList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					case 25:
						Log.d("yckx","�Ѿ�������:invalid"+msg.arg1);
						commentList.addAll(getlist);
						adapter.clearAdapter();
						adapter.addDataToAdapterEnd(commentList);
						adapter.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
						listview.setVisibility(View.VISIBLE);
						break;
					}}
					break;
				case CANCLEORDER_SUCCESS://ȡ�������ɹ�,ȡ���ɹ�������ˢ������
					ToastUtil.show(getApplicationContext(), "�ö�����ȡ��", 2000);
					//�ж�Ҫ������һ��ҳ��
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


		/**�����¼����*/
		private void goToLogin(){
			Intent intent=new Intent(getActivity(),LoginActivity.class);
			startActivity(intent);
		}

		//---listView ��������ˢ��

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
					evaluate_atonce.setVisibility(View.GONE);
					sendVoice.setVisibility(View.VISIBLE);
					callPhone.setVisibility(View.VISIBLE);
					mOrderStatus="������";
				}
				if(mOrderStatus.equals("comment")){
					evaluate_atonce.setVisibility(View.VISIBLE);
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
			@SuppressLint("SdCardPath")
			@Override
			public void onClick(View arg0) {
				switch (arg0.getId()) {
				case R.id.pay_atonce://����֧��
					OrderListEntity orders=adapter.getItem(position);
					Intent intent_pay=new Intent(getActivity(),PayMoneyActivity.class);
					intent_pay.putExtra("PayMoneyActivity", orders.getId());
					startActivity(intent_pay);
					getActivity().finish();
					break;
				case R.id.evaluate_atonce://����
					OrderListEntity orders_evaluate=adapter.getItem(position);
					//�ж�����ʱ���Ƿ��Ѿ�����
					//��ǰ��ʱ���
					Date todateDate=new Date();
					long todateTime = todateDate.getTime();
					//�������ʱ��
					String completeTime=orders_evaluate.getOrderFinishTime();
					long mPromissTime=Long.parseLong(completeTime);
					long betweenDays = (long)((todateTime-mPromissTime) / (1000 * 60 * 60 *24) + 0.5);
					if(betweenDays>3){
						ToastUtil.show(getApplicationContext(), "�ѹ��ڣ����ܽ�������", 2000);
						return;
					}
					//��ȡ��ǰҳ��
					Bitmap bitmap=MyBitmapUtils.takeScreenShot(getActivity());
					MyBitmapUtils.savePic(bitmap, "/sdcard/yckx/yckx_temp.png");
					Log.d("yckx","--�����۵Ķ���"+orders_evaluate.toString());
					Intent intent=new Intent(getActivity(),WasherFeedbackActivity.class);
					intent.putExtra("WasherFeedbackActivity", orders_evaluate.getId());
					startActivity(intent);
					break;
				case R.id.sendVoice:
					OrderListEntity orders_sendvoice=adapter.getItem(position);
					if(orders_sendvoice.getWasherEntity()==null||orders_sendvoice.getWasherEntity().getWaherId().equals("")){
						ToastUtil.show(getApplicationContext(), "����ʦ�ӵ�ǰ����ȡ������,û������ʦ��Ϣ", 2000);
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
				case R.id.cancelProduce://ȡ������/order/cancel/{orderId}
					OrderListEntity orders_cancle=adapter.getItem(position);
					ownCancelOrder(orders_cancle.getId());
					break;
				}
			}}


		/**ȡ������*/
		private void ownCancelOrder(final String orderId){
			new Thread(new Runnable() {
				@Override
				public void run() {
					String host=WEBInterface.ORDER_CANCEL_URL+orderId;
					String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
					String response=ApacheHttpUtil.httpPost(host, token, null);
					Log.d("yckx","ȡ��������������"+response);
					if(response!=null){
						try {
							JSONObject jsonOBJ=new JSONObject(response);
							String code=jsonOBJ.getString("code");
							if(code.equals("0")){
								//ȡ���ɹ�
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

