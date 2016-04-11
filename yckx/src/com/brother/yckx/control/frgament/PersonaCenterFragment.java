package com.brother.yckx.control.frgament;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.LruCache;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androide.lib3.volley.RequestQueue;
import com.androide.lib3.volley.Response;
import com.androide.lib3.volley.VolleyError;
import com.androide.lib3.volley.toolbox.ImageLoader;
import com.androide.lib3.volley.toolbox.ImageRequest;
import com.androide.lib3.volley.toolbox.Volley;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.MobileManager;
import com.brother.utils.logic.MobileManager.PhoneInfo2;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.AdressActivity;
import com.brother.yckx.control.activity.owner.FeedBackActivity;
import com.brother.yckx.control.activity.owner.FormActivity;
import com.brother.yckx.control.activity.owner.GarageManagerActivity;
import com.brother.yckx.control.activity.owner.HistoricalEvaluationActivity;
import com.brother.yckx.control.activity.owner.LoginActivity;
import com.brother.yckx.control.activity.owner.NoticeListActivity;
import com.brother.yckx.control.activity.owner.OrderClassFragmentActivity;
import com.brother.yckx.control.activity.owner.PersonalBusinessManagerActivity;
import com.brother.yckx.control.activity.owner.PersonalEditActivity;
import com.brother.yckx.control.activity.owner.PersonalNewsActivity;
import com.brother.yckx.control.activity.owner.PersonalWalletActivity;
import com.brother.yckx.control.activity.owner.PushPersonalNewsActivity;
import com.brother.yckx.control.activity.owner.RegisterCompanyActivity;
import com.brother.yckx.control.activity.owner.SettingActivity;
import com.brother.yckx.control.activity.owner.WebViewActivity;
import com.brother.yckx.control.frgament.WoWoFragment.PublishSelectPicPopupWindow;
import com.brother.yckx.model.UserEntity;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.image2.CacheCircleImageView;
/**
 * 会员中心
 * HomeActivity切换到次界面时先要判断本地是否有用户的信息，没有则跳转到登录界面...否则展示用户信息
 * 
 * */
public class PersonaCenterFragment extends Fragment{
	private View view;
	private ImageView settingImg;
	private CacheCircleImageView touxiang;
	private TextView phonenum;
	private TextView balanceTv,cardTimes_tv,notice_tv;
	private LinearLayout balance_layout,cardTimes_layout,notice_layout;
	private RelativeLayout myform_layout,evaluate_layout,mycar_layout,adress_layout,opinion_layout,aboutus_layout;
	private UserEntity userMessage;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//获取用户信息,进行展示...第一次使用进入登录界面
		userMessage=PrefrenceConfig.getUserMessage(getActivity());
		if(userMessage.getUserPhone()==null||userMessage.getUserPhone().equals("")){
			//第一次使用，进入到登录界面
			view=inflater.inflate(R.layout.activity_login, container,false);
			Intent intent=new Intent(getActivity(),LoginActivity.class);
			getActivity().startActivity(intent);
			getActivity().finish();
			return view;
		}else{
			//view=inflater.inflate(R.layout.fragment_user_main, container,false);
			view=inflater.inflate(R.layout.fragment_personal, container,false);
			//initLoginView();
			initview();
			return view;
		}
	}



	//修改页面
	//actionbar
	private ImageView icon_defalut_search,add;
	//bg
	private CacheCircleImageView userimg;
	private TextView userName,userType,nickname,wowoWallet;
	private ImageView userTypeImg,userEdit;
	//动态
	private LinearLayout userLayoutNews;
	private TextView userNews;
	private ImageView userNewsImg1,userNewsImg2,userNewsImg3;
	//item
	private RelativeLayout layout_redMe,layout_fabu,layout_order,layout_wallet,layout_carManager,layout_location,layout_help,layout_setting,layout_businiessManager,layout_business_apply;
	//泡泡窗口
	private PublishSelectPicPopupWindow menuWindow;

	/**初始化View*/
	private void initview(){
		icon_defalut_search=(ImageView) view.findViewById(R.id.icon_defalut_search);
		add=(ImageView) view.findViewById(R.id.add);

		userimg=(CacheCircleImageView) view.findViewById(R.id.userimg);
		userName=(TextView) view.findViewById(R.id.userName);
		userType=(TextView) view.findViewById(R.id.userType);
		userTypeImg=(ImageView) view.findViewById(R.id.userTypeImg);
		userEdit=(ImageView) view.findViewById(R.id.userEdit);
		nickname=(TextView) view.findViewById(R.id.nickname);
		wowoWallet=(TextView) view.findViewById(R.id.wowoWallet);

		userLayoutNews=(LinearLayout) view.findViewById(R.id.userLayoutNews);
		userNews=(TextView) view.findViewById(R.id.userNews);
		userNewsImg1=(ImageView) view.findViewById(R.id.userNewsImg1);
		userNewsImg2=(ImageView) view.findViewById(R.id.userNewsImg2);
		userNewsImg3=(ImageView) view.findViewById(R.id.userNewsImg3);

		layout_redMe=(RelativeLayout) view.findViewById(R.id.layout_redMe);
		layout_fabu=(RelativeLayout) view.findViewById(R.id.layout_fabu);
		layout_order=(RelativeLayout) view.findViewById(R.id.layout_order);
		layout_wallet=(RelativeLayout) view.findViewById(R.id.layout_wallet);
		layout_carManager=(RelativeLayout) view.findViewById(R.id.layout_carManager);
		layout_location=(RelativeLayout) view.findViewById(R.id.layout_location);
		layout_help=(RelativeLayout) view.findViewById(R.id.layout_help);
		layout_setting=(RelativeLayout) view.findViewById(R.id.layout_setting);
		layout_businiessManager=(RelativeLayout) view.findViewById(R.id.layout_businiessManager);
		layout_business_apply=(RelativeLayout) view.findViewById(R.id.layout_business_apply);

		//
		icon_defalut_search.setOnClickListener(listener);
		add.setOnClickListener(listener);
		//头像、个人编辑
		userimg.setOnClickListener(listener);
		userEdit.setOnClickListener(listener);
		//动态
		userLayoutNews.setOnClickListener(listener);
		//item
		layout_redMe.setOnClickListener(listener);
		layout_fabu.setOnClickListener(listener);
		layout_order.setOnClickListener(listener);
		layout_wallet.setOnClickListener(listener);
		layout_carManager.setOnClickListener(listener);
		layout_location.setOnClickListener(listener);
		layout_help.setOnClickListener(listener);
		layout_setting.setOnClickListener(listener);
		layout_businiessManager.setOnClickListener(listener);
		layout_business_apply.setOnClickListener(listener);

		//设置背景模块各个控件的值
		//设置用户信息
		String userNameStr=userMessage.getUserName();
		String userPhone=userMessage.getUserPhone();
		String userImageUrl=userMessage.getUserIamgeUrl();
		String balance=userMessage.getUserBalance_user();
		String cardTimes=userMessage.getUserCardTimes_user();
		String notice=userMessage.getUserNotice_user();
		if(userNameStr!=null){
			if(userNameStr.equals("")){
				userName.setText("请完善资料");
			}else{
				userName.setText(userNameStr);
			}

		}
		final String imgUrl=WEBInterface.INDEXIMGURL+PrefrenceConfig.getUserMessage(getActivity()).getUserIamgeUrl();
		userimg.setImageUrl(imgUrl);
		wowoWallet.setText(balance);
	}


	private void initLoginView(){
		settingImg=(ImageView) view.findViewById(R.id.settingImg);
		touxiang=(CacheCircleImageView) view.findViewById(R.id.touxiang);
		phonenum=(TextView) view.findViewById(R.id.userPhone);
		balanceTv=(TextView) view.findViewById(R.id.tv_balance);
		cardTimes_tv=(TextView) view.findViewById(R.id.tv_cardTimes);
		notice_tv=(TextView) view.findViewById(R.id.tv_notice);

		balance_layout=(LinearLayout) view.findViewById(R.id.balance_layout);
		cardTimes_layout=(LinearLayout) view.findViewById(R.id.cardTimes_layout);
		notice_layout=(LinearLayout) view.findViewById(R.id.notice_layout);

		myform_layout=(RelativeLayout) view.findViewById(R.id.myform_layout);
		evaluate_layout=(RelativeLayout) view.findViewById(R.id.evaluate_layout);
		mycar_layout=(RelativeLayout) view.findViewById(R.id.mycar_layout);
		adress_layout=(RelativeLayout) view.findViewById(R.id.adress_layout);
		opinion_layout=(RelativeLayout) view.findViewById(R.id.opinion_layout);
		aboutus_layout=(RelativeLayout) view.findViewById(R.id.aboutus_layout);

		settingImg.setOnClickListener(listener);
		touxiang.setOnClickListener(listener);

		notice_layout.setOnClickListener(listener);//点击进入通知页面

		myform_layout.setOnClickListener(listener);
		evaluate_layout.setOnClickListener(listener);
		mycar_layout.setOnClickListener(listener);
		adress_layout.setOnClickListener(listener);
		opinion_layout.setOnClickListener(listener);
		aboutus_layout.setOnClickListener(listener);

		//设置用户信息
		String userName=userMessage.getUserName();
		String userPhone=userMessage.getUserPhone();
		String userImageUrl=userMessage.getUserIamgeUrl();
		String balance=userMessage.getUserBalance_user();
		String cardTimes=userMessage.getUserCardTimes_user();
		String notice=userMessage.getUserNotice_user();
		if(userPhone!=null){
			phonenum.setText(userPhone);
		}
		final String imgUrl=WEBInterface.INDEXIMGURL+PrefrenceConfig.getUserMessage(getActivity()).getUserIamgeUrl();
		touxiang.setImageUrl(imgUrl);
	}

	private View.OnClickListener listener=new View.OnClickListener() {
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.icon_defalut_search://搜索
				break;
			case R.id.add://弹出模块
				// 实例化SelectPicPopupWindow
				menuWindow = new PublishSelectPicPopupWindow(getActivity(),itemsOnClick);
				// 显示窗口
				menuWindow.showAtLocation(add,
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case R.id.userimg://点击头像
				break;
			case R.id.userEdit://点击个人编辑
				Intent intentEdit=new Intent(getActivity(),PersonalEditActivity.class);
				startActivity(intentEdit);
				break;
			case R.id.userLayoutNews://个人动态
				Intent intentNews=new Intent(getActivity(),PersonalNewsActivity.class);
				startActivity(intentNews);
				break;
			case R.id.layout_redMe://谁看过我
				break;
			case R.id.layout_fabu://发布
				Intent intentPush=new Intent(getActivity(),PushPersonalNewsActivity.class);
				startActivity(intentPush);
				break;
			case R.id.layout_order://订单
				//根据用户的信息访问网络，获取订单数据再跳转...访问获取到失败的数据则跳转到登录界面(先查询所有的数据，然后跳转)
				if(!isLinknetWork()){
					ToastUtil.show(getActivity(), "网络异常", 2000);
					return;
				}
				UserEntity user=PrefrenceConfig.getUserMessage(getActivity());
				checkTokenToGoToPager();
				break;
			case R.id.layout_wallet://钱包
				Intent intentWallet=new Intent(getActivity(),PersonalWalletActivity.class);
				startActivity(intentWallet);
				break;
			case R.id.layout_carManager://车库
				if(!isLinknetWork()){
					ToastUtil.show(getActivity(), "网络异常", 2000);
					return;
				}
				Intent intent_car=new Intent(getActivity(),GarageManagerActivity.class);
				startActivity(intent_car);
				break;
			case R.id.layout_location://位置
				if(!isLinknetWork()){
					ToastUtil.show(getActivity(), "网络异常", 2000);
					return;
				}
				Intent intent_location=new Intent(getActivity(),AdressActivity.class);
				//intent_location.putExtra("AddAdressActivity", "");
				startActivity(intent_location);
				break;
			case R.id.layout_help://帮助与反馈
				if(!isLinknetWork()){
					ToastUtil.show(getActivity(), "网络异常", 2000);
					return;
				}
				//test
				Intent intent_order=new Intent(getActivity(),FeedBackActivity.class);
				startActivity(intent_order);
				break;
			case R.id.layout_setting://设置
				Intent intent=new Intent(getActivity(),SettingActivity.class);
				intent.putExtra("changeImg", "no");
				startActivity(intent);
				break;
			case R.id.layout_businiessManager://商家管理
				Intent intentBusinessManager=new Intent(getActivity(),PersonalBusinessManagerActivity.class);
				startActivity(intentBusinessManager);
				break;
			case R.id.layout_business_apply://商家申请
				Intent intentApply=new Intent(getActivity(),RegisterCompanyActivity.class);
				startActivity(intentApply);
				break;
				/*case R.id.settingImg:
				Intent intent=new Intent(getActivity(),SettingActivity.class);
				intent.putExtra("changeImg", "no");
				startActivity(intent);
				break;
			case R.id.touxiang://弹出Dialog选择相机或者相册
				break;
			case R.id.notice_layout:
				Intent intent_notice=new Intent(getActivity(),NoticeListActivity.class);
				startActivity(intent_notice);
				break;
			case R.id.myform_layout://我的订单
				//根据用户的信息访问网络，获取订单数据再跳转...访问获取到失败的数据则跳转到登录界面(先查询所有的数据，然后跳转)
				if(!isLinknetWork()){
					ToastUtil.show(getActivity(), "网络异常", 2000);
					return;
				}
				UserEntity user=PrefrenceConfig.getUserMessage(getActivity());
				System.out.println("---->>当前用户的Token"+user.getUserToken());
				checkTokenToGoToPager();
				break;
			case R.id.evaluate_layout://历史评价
				//访问网络再跳转
				if(!isLinknetWork()){
					ToastUtil.show(getActivity(), "网络异常", 2000);
					return;
				}
				Intent intent_evaluation=new Intent(getActivity(),HistoricalEvaluationActivity.class);
				startActivity(intent_evaluation);
				break;
			case R.id.mycar_layout://我的车库
				if(!isLinknetWork()){
					ToastUtil.show(getActivity(), "网络异常", 2000);
					return;
				}
				Intent intent_car=new Intent(getActivity(),GarageManagerActivity.class);
				startActivity(intent_car);
				break;
			case R.id.adress_layout://常用位置
				if(!isLinknetWork()){
					ToastUtil.show(getActivity(), "网络异常", 2000);
					return;
				}
				Intent intent_location=new Intent(getActivity(),AdressActivity.class);
				//intent_location.putExtra("AddAdressActivity", "");
				startActivity(intent_location);
				break;
			case R.id.opinion_layout://意见反馈
				if(!isLinknetWork()){
					ToastUtil.show(getActivity(), "网络异常", 2000);
					return;
				}
				//test
				Intent intent_order=new Intent(getActivity(),FeedBackActivity.class);
				startActivity(intent_order);
				break;
			case R.id.aboutus_layout://关于我们
				if(!isLinknetWork()){
					ToastUtil.show(getActivity(), "网络异常", 2000);
					return;
				}else{
					GlobalServiceUtils.setGloubalServiceSession("WebViewActivity","aboutUs");
					Intent intent_abouts=new Intent(getActivity(),WebViewActivity.class);
					startActivity(intent_abouts);
				}
				break;*/
			}
		}
	};


	//---------------泡泡窗口  start---------//
	/**泡泡窗口*/
	class PublishSelectPicPopupWindow extends PopupWindow{
		private ImageView btnCancel;
		private View mMenuView;
		public PublishSelectPicPopupWindow(Activity context,OnClickListener itemsOnClick){
			super(context);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.row_dialog_wowo_popu, null);
			btnCancel=(ImageView) mMenuView.findViewById(R.id.btnCancel);
			btnCancel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					dismiss();
				}});
			//设置SelectPicPopupWindow的View
			this.setContentView(mMenuView);
			//设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.FILL_PARENT);
			//设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.FILL_PARENT);
			//设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			//设置SelectPicPopupWindow弹出窗体动画效果
			this.setAnimationStyle(R.style.popwin_anim_style);
			//实例化一个ColorDrawable颜色为半透明0xb0000000
			ColorDrawable dw = new ColorDrawable(0);
			//设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(dw);
			//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
			mMenuView.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					int height = mMenuView.findViewById(R.id.layout_main).getTop();
					int y=(int) event.getY();
					if(event.getAction()==MotionEvent.ACTION_UP){
						if(y<height){
							dismiss();
						}
					}				
					return true;
				}
			});

		}
	}


	/**popu窗口点击事件的监听*/
	private View.OnClickListener itemsOnClick=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {

		}
	};


	/**判断是否已经连接网络*/
	private boolean isLinknetWork(){
		MobileManager manager=MobileManager.getPhoneManager(getActivity());
		PhoneInfo2 thisPhone=manager.getPhoneInfo();
		String netStatus=thisPhone.getPhoneNetworkType();
		if(netStatus.equals("WIFI")||netStatus.equals("MOBILE")||netStatus.equals("wifi")||netStatus.equals("mobile")){
			return true;
		}
		return false;
	}

	/**判断保存在本地的用户的token是否有效，无效则跳转到登录界面，有效则访问网络获取“我的订单”数据，然后进入到“我的订单”页面*/
	private boolean tokenIsValid=false;


	/**是否可以点击"我的订单"的Item,是否无效*/
	private boolean isMyform_layoutInvaild;
	/**检查当前用户的token是否有效，有效则跳转到"我的订单列表"，否则跳转到登录界面*/
	private void checkTokenToGoToPager(){
		Intent intent=new Intent(getActivity(),FormActivity.class);
		startActivity(intent);
		/*if(isMyform_layoutInvaild){
			return;
		}
		isMyform_layoutInvaild=true;
		initLoadingDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String orderListURL=WEBInterface.MYORDERLISTURL+"/"+pagerCoun+"/"+eachePageSize+"?="+status;
				System.out.println("--->>getOrderData()我的订单访问网络的完整网址"+orderListURL);
				String respose=ApacheHttpUtil.httpGet(orderListURL, userToken, null);
				System.out.println("--->>getOrderData()的respose:"+respose);
				isMyform_layoutInvaild=false;
				//判断token是否有效，无效则设置tokenIsValid无效
				if(respose!=null){
					try {
						JSONObject jSONObject=new JSONObject(respose);
						String code=jSONObject.getString("code");
						if(code.equals("0")){
							//token有效
							Message msg=new Message();
							msg.what=TOKEN_SUCCESS;
							//把用户的数据封装起来，传到“我的订单”二级页面
							mHandler.sendMessage(msg);
						}
						if(code.equals("2")){
							//token错误
							Message msg=new Message();
							msg.what=TOKEN_ERROR;
							mHandler.sendMessage(msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}).start();*/
	}


	/**加载中的dialog*/
	private Dialog loadingDialog;
	private void initLoadingDialog(){
		LayoutInflater inflater=getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_upload, null);
		TextView tv_miaoshu=(TextView)view.findViewById(R.id.miaoshu);
		tv_miaoshu.setText("拼命加载中...");
		loadingDialog = new AlertDialog.Builder(getActivity()).create();
		loadingDialog.show();
		loadingDialog.setContentView(view);
		Window dialogWindow = loadingDialog.getWindow(); 
		WindowManager.LayoutParams params = dialogWindow.getAttributes();  
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		WindowManager m =getActivity().getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		//偏移量
		loadingDialog.getWindow().setAttributes(params);
	}


	private void  loadMoreInterfaceData(){
		/*String orderListURL_daizhifu=WEBInterface.MYORDERLISTURL+"/"+pagerCoun+"/"+eachePageSize+"?="+"待支付";
		String orderListURL_qingxizhong=WEBInterface.MYORDERLISTURL+"/"+pagerCoun+"/"+eachePageSize+"?="+"清洗中";
		String orderListURL_daijiedan=WEBInterface.MYORDERLISTURL+"/"+pagerCoun+"/"+eachePageSize+"?="+"待接单";
		String orderListURL_quxiao=WEBInterface.MYORDERLISTURL+"/"+pagerCoun+"/"+eachePageSize+"?="+"取消";
		String orderListURL_zuofei=WEBInterface.MYORDERLISTURL+"/"+pagerCoun+"/"+eachePageSize+"?="+"作废";
		String orderListURL_daipingjia=WEBInterface.MYORDERLISTURL+"/"+pagerCoun+"/"+eachePageSize+"?="+"作废";
		String orderListURL_yipingjia=WEBInterface.MYORDERLISTURL+"/"+pagerCoun+"/"+eachePageSize+"?="+"已评价";*/
	}

	/**历史评价*/
	private final int TOKEN_ERROR=-2;
	private final int TOKEN_SUCCESS=2;
	@SuppressLint("HandlerLeak")
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TOKEN_ERROR://订单失效
				loadingDialog.dismiss();
				Intent intent_login=new Intent(getActivity(),LoginActivity.class);
				startActivity(intent_login);
				getActivity().finish();
				break;
			case TOKEN_SUCCESS:
				loadingDialog.dismiss();
				Intent intent_myOrder=new Intent(getActivity(),FormActivity.class);
				startActivity(intent_myOrder);
				break;
			}
		};
	};

	/**loadImage*/
	@SuppressLint("NewApi")
	class BitmapCache implements ImageLoader.ImageCache {    
		//LruCache对象    
		private LruCache<String, Bitmap> lruCache ;    
		//设置最大缓存为10Mb，大于这个值会启动自动回收    
		private int max = 10*1024*1024;    

		public BitmapCache(){       
			//初始化 LruCache
			lruCache = new LruCache<String, Bitmap>(max){            
				@Override            
				protected int sizeOf(String key, Bitmap value) {                
					return value.getRowBytes()*value.getHeight();            
				}        
			};    
		}   
		@Override    
		public Bitmap getBitmap(String url) {        
			return lruCache.get(url);    
		}    
		@Override    
		public void putBitmap(String url, Bitmap bitmap) {          
			lruCache.put(url, bitmap);    
		}
	}


}
