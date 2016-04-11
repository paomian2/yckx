package com.brother.yckx.control.frgament;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.mapcore2d.en;
import com.androide.lib3.volley.RequestQueue;
import com.androide.lib3.volley.Response;
import com.androide.lib3.volley.VolleyError;
import com.androide.lib3.volley.toolbox.ImageRequest;
import com.androide.lib3.volley.toolbox.Volley;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.MyBitmapUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.NumberUtils;
import com.brother.utils.logic.TimeUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.CommentActivity;
import com.brother.yckx.control.activity.owner.FormDetailActivity;
import com.brother.yckx.control.activity.owner.PayMoneyActivity;
import com.brother.yckx.control.activity.owner.FormDetailActivity.RefreshOnclickListner;
import com.brother.yckx.model.CardEntity;
import com.brother.yckx.model.OrderDetailEntity;
import com.brother.yckx.model.OrderListEntity;
import com.brother.yckx.model.db.AssetsDBManager;
import com.brother.yckx.model.db.DBRead;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.image.CacheImageView;
import com.shizhefei.fragment.LazyFragment;

public class MoreFragment_FormDetail extends LazyFragment implements RongIM.UserInfoProvider{
	//private LinearLayout mainLayout,mainLayout2;
	private int tabIndex;
	public static final String INTENT_INT_INDEX = "intent_int_index";
	private OrderDetailEntity entity;
	private String orderId;
	/**判断洗护师操作的状态(接单、到达、检查、开始洗车、洗车结束)*/
	private boolean washerReviceOrder,washerArrive,washerCheck,waherWashing,wahserEnd;
	/**加载订单数据完成*/
	private final int REFRESH_SUCCESS=0;
	/**监听Activity的刷新动作*/


	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		//获取订单数据
		entity=GlobalServiceUtils.getInstance().getGloubalServiceOrderDetailEntity();
		orderId=entity.getId();
		FormDetailActivity activity=(FormDetailActivity) getActivity();
		activity.setRefreseOnclickListner(new RefreshOnclickListner(){
			@Override
			public void onRefreshclick(String s) {
				//重新刷新订单数据
				ToastUtil.show(getApplicationContext(),"点击了刷新", 2000);
				getOrderDetailData(orderId);
			}});

		if(entity.getOrderStatus()!=null&&entity.getOrderStatus().equals("washed")){
			washerReviceOrder=true;
			washerArrive=true;
			washerCheck=true;
			waherWashing=true;
			wahserEnd=true;
		}
		tabIndex = getArguments().getInt(INTENT_INT_INDEX);
		switch (tabIndex) {
		case 0://订单内容,生成一个动态布局
			setContentView(R.layout.fragment_more_formdetail);
			setData();
			break;
		case 1://一个h5网页
			setWebView();
			break;
		case 2://美护师
			setContentView(R.layout.fragment_morefragment_formdetail_meihushi);
			selectTab3();
			break;
		}
	}

	private void setData(){
		TextView formNo=(TextView) findViewById(R.id.formNo);//订单编号
		TextView formStatus=(TextView) findViewById(R.id.formStatus);//支付状态
		TextView formCreateTime=(TextView) findViewById(R.id.formCreateTime);//订单创建时间
		TextView formReceiveTime=(TextView) findViewById(R.id.formReceiveTime);//订单编号,接单时间
		TextView formCompleteTime=(TextView) findViewById(R.id.formCompleteTime);//完成时间
		TextView formPay=(TextView) findViewById(R.id.formPay);//订单支付金额
		TextView formPayType=(TextView) findViewById(R.id.formPayType);//订单支付方式
		TextView formStore=(TextView) findViewById(R.id.formStore);//订单商家名称
		TextView formProject=(TextView) findViewById(R.id.formProject);//订单项目名称
		TextView formCard=(TextView) findViewById(R.id.formCard);//订单车辆系列
		TextView formStyle=(TextView) findViewById(R.id.formStyle);//订单服务方式
		formNo.setText(""+entity.getOrderNum());
		//订单状态
		String mOrderStatus=entity.getOrderStatus();
		if(mOrderStatus.equals("create")){
			mOrderStatus="待支付";
		}
		if(mOrderStatus.equals("robbed")){
			mOrderStatus="已接单";
		}
		if(mOrderStatus.equals("payed")){
			mOrderStatus="待接单";
		}	
		if(mOrderStatus.equals("cancle")){
			mOrderStatus="取消";
		}
		if(mOrderStatus.equals("invalid")){
			mOrderStatus="作废";
		}
		if(mOrderStatus.equals("finish")){
			mOrderStatus="待评价";
		}
		if(mOrderStatus.equals("comment")){
			mOrderStatus="已评价";
		}
		formStatus.setText(""+mOrderStatus);
		if(entity.getOrderCreateTime()!=null&&!entity.getOrderCreateTime().equals("")){
			formCreateTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
		}
		if(entity.getOrderUpdateTime()!=null&&!entity.getOrderUpdateTime().equals("")){
			formReceiveTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderUpdateTime()));
		}
		if(entity.getOrderFiniahTime()==null||entity.getOrderFiniahTime().equals("")){
			formCompleteTime.setText("--");
		}else{
			formCompleteTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderFiniahTime()));
		}
		formPay.setText(""+entity.getOrderPrice());
		//支付方式
		String mPayType=entity.getPayType();
		if(mPayType.equals("ali")){
			mPayType="支付宝";
		}
		if(mPayType.equals("union")){
			mPayType="银联";
		}
		if(mPayType.equals("wx")){
			mPayType="微信";
		}
		formPayType.setText(""+mPayType);
		formStore.setText(""+entity.getCompanyEntity().getCompanyName());
		formProject.setText(""+entity.getProductEntity().getProductName());
		CardEntity cardEntity=entity.getCarEntity();
		formCard.setText(""+cardEntity.getCarBrand()+"|"+cardEntity.getProvince()+cardEntity.getCarNum()+"|"+cardEntity.getCarColor());
		if(entity.getDoorService().equals("false")){
			formStyle.setText("到店享受");
		}else{
			formStyle.setText("上门服务");
		}
	}





	/**第二个页面是一个网页，进行网页设置*/
	private void setWebView(){
		setContentView(R.layout.fragment_morefragment_formdetail_service);
		WebView webView=(WebView) findViewById(R.id.webView);
		//webView.loadUrl("https://www.hao123.com/");
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
			}
		});
		webView.setWebViewClient(webViewClient);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.loadUrl("http://s.fuli007.com");
	}

	private WebViewClient webViewClient=new WebViewClient(){
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;

		};

		//开始加载
		public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			System.out.println("----->>>开始加载");
		};

		//加载介绍
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			System.out.println("-------->>加载结束");
		};

		//加载出错
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			System.out.println("------->>加载出错");
		};

	};


	//订单状态    create待支付   robbed已抢单  payed待接单   cancle取消  invalid作废    finish待评价    comment已评价 

	//洗护师状态  arrived到达指定位置  checked检查车辆  washing正在清洗   washed清洗完毕

	/**选择美护师页面*/
	@SuppressLint("ResourceAsColor")
	private void selectTab3(){
		
		Log.d("yckx", entity.getId()+"");

		TextView createTime=(TextView) findViewById(R.id.createTime);//订单生成时间

		LinearLayout layout_unpay=(LinearLayout) findViewById(R.id.layout_unpay);//未支付
		TextView btn_payOrder=(TextView) findViewById(R.id.btn_payOrder);//立即支付(点击)
		btn_payOrder.setOnClickListener(timeProgressClickListener);

		LinearLayout layout_pay=(LinearLayout) findViewById(R.id.layout_pay);//已经支付
		TextView payTime=(TextView) findViewById(R.id.payTime);//支付时间

		LinearLayout layout_receive=(LinearLayout) findViewById(R.id.layout_washer_Receive);//洗护师接单
		TextView washerDoTime=(TextView) findViewById(R.id.washerDoTime);//洗护师接单时间
		CircleImageView washerImg=(CircleImageView) findViewById(R.id.washerImg);//洗护师头像
		TextView wahserName=(TextView) findViewById(R.id.wahserName);//洗护师名
		ImageView wahser_img1=(ImageView) findViewById(R.id.wahser_img1);//洗护师获得的评价(星星)根据userTotalScore/userCommentCount获得
		ImageView wahser_img2=(ImageView) findViewById(R.id.wahser_img2);
		ImageView wahser_img3=(ImageView) findViewById(R.id.wahser_img3);
		ImageView wahser_img4=(ImageView) findViewById(R.id.wahser_img4);
		ImageView wahser_img5=(ImageView) findViewById(R.id.wahser_img5);
		TextView phoneCall=(TextView) findViewById(R.id.phoneCall);//点击拨打洗护师电话
		TextView yuyin=(TextView) findViewById(R.id.yuyin);//点击进入融云聊天
		TextView orderCancel=(TextView) findViewById(R.id.orderCancel);//点击申请取消订单
		phoneCall.setOnClickListener(timeProgressClickListener);
		yuyin.setOnClickListener(timeProgressClickListener);
		orderCancel.setOnClickListener(timeProgressClickListener);

		LinearLayout layout_washerArrive=(LinearLayout) findViewById(R.id.layout_washerArrive);//洗护师到达指定位置
		TextView washerArriveImg_arrive=(TextView) findViewById(R.id.wahserArriveTime);//洗护师到达指定位置时间

		LinearLayout layout_washerCheck=(LinearLayout) findViewById(R.id.layout_washerCheck);//检查车辆
		TextView washerCheckCarTime=(TextView) findViewById(R.id.washerCheckCarTime);//检查车辆时间
		CacheImageView img1=(CacheImageView) findViewById(R.id.img1);//美护师上传的图片
		CacheImageView img2=(CacheImageView) findViewById(R.id.img2);//美护师上传的图片
		CacheImageView img3=(CacheImageView) findViewById(R.id.img3);//美护师上传的图片

		LinearLayout layout_washerWashering=(LinearLayout) findViewById(R.id.layout_washerWashering);//开始洗车
		TextView washeringTime=(TextView) findViewById(R.id.washeringTime);//开始洗车时间

		LinearLayout layout_washerEnd=(LinearLayout) findViewById(R.id.layout_washerEnd);//洗车结束
		TextView washerEndTime=(TextView) findViewById(R.id.washerEndTime);//洗车结束时间
		TextView btn_pingjia=(TextView) findViewById(R.id.btn_pingjia);//立即评价(超过三天时间无法评价,已经评价过的不能再进行评价)
		if(entity.getCommentEntity()!=null){
			findViewById(R.id.layout_btn_evaluation).setBackgroundColor(R.color.click_enabal);
			btn_pingjia.setClickable(false);
		}
		btn_pingjia.setOnClickListener(timeProgressClickListener);
		
		LinearLayout layout_pingjia=(LinearLayout) findViewById(R.id.layout_pingjia);//用户已经评价了
		TextView pingjiaTime=(TextView) findViewById(R.id.pingjiaTime);//评价时间
		TextView pingjiaContext=(TextView) findViewById(R.id.pingjiaContent);//评价内容
		ImageView pingajiaImg1=(ImageView) findViewById(R.id.pingjiaImg1);//评价的星星
		ImageView pingajiaImg2=(ImageView) findViewById(R.id.pingjiaImg2);//评价的星星
		ImageView pingajiaImg3=(ImageView) findViewById(R.id.pingjiaImg3);//评价的星星
		ImageView pingajiaImg4=(ImageView) findViewById(R.id.pingjiaImg4);//评价的星星
		ImageView pingajiaImg5=(ImageView) findViewById(R.id.pingjiaImg5);//评价的星星
		CacheImageView pinglunImg1=(CacheImageView) findViewById(R.id.pinglunImg1);//评论提交的图片
		CacheImageView pinglunImg2=(CacheImageView) findViewById(R.id.pinglunImg2);//评论提交的图片
		CacheImageView pinglunImg3=(CacheImageView) findViewById(R.id.pinglunImg3);//评论提交的图片
		
		LinearLayout layout_next=(LinearLayout) findViewById(R.id.layout_next);//进行下一步
		TextView tv_netDo=(TextView) findViewById(R.id.tv_netDo);//下一步要干嘛呢

		LinearLayout layout_orderEnd=(LinearLayout) findViewById(R.id.layout_orderEnd);//订单完成

		//逻辑处理
		if(entity.getOrderSubEntity()!=null&&entity.getOrderSubEntity().getSubStatus().equals("create")){//待支付
			//显示生成订单的时间
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));

			layout_unpay.setVisibility(View.VISIBLE);
			layout_pay.setVisibility(View.GONE);
			layout_receive.setVisibility(View.GONE);
			layout_washerArrive.setVisibility(View.GONE);
			layout_washerCheck.setVisibility(View.GONE);
			layout_washerWashering.setVisibility(View.GONE);
			layout_washerEnd.setVisibility(View.GONE);
			layout_pingjia.setVisibility(View.GONE);
			layout_next.setVisibility(View.GONE);
			layout_orderEnd.setVisibility(View.GONE);
		}
		if(entity.getOrderStatus().equals("payed")){//待接单
			//显示生成订单的时间
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//支付时间
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));

			layout_unpay.setVisibility(View.GONE);
			layout_pay.setVisibility(View.VISIBLE);
			layout_receive.setVisibility(View.GONE);
			layout_washerArrive.setVisibility(View.GONE);
			layout_washerCheck.setVisibility(View.GONE);
			layout_washerWashering.setVisibility(View.GONE);
			layout_washerEnd.setVisibility(View.GONE);
			layout_orderEnd.setVisibility(View.GONE);
			layout_next.setVisibility(View.VISIBLE);

			tv_netDo.setText("等待美护师接单");
		}
		if(entity.getOrderStatus().equals("robbed")){//美护师已接单,未到达
			//显示生成订单的时间
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//支付时间
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//美护师数据
			layout_receive.setVisibility(View.VISIBLE);
			//美护师接单时间
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//设置美护师的头像
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//美护师姓名
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//美护师分数。根据分数设置"星星"
			String score=NumberUtils.InterDivisionInter(entity.getWasherEntity().getUserTotalScore(), entity.getWasherEntity().getUserCommentCount());
			int mScore=Integer.parseInt(score);
			ImageView[] imageview={wahser_img1,wahser_img2,wahser_img3,wahser_img4,wahser_img5};
			setStarImageView(mScore,imageview);

			layout_unpay.setVisibility(View.GONE);
			layout_pay.setVisibility(View.VISIBLE);
			layout_receive.setVisibility(View.VISIBLE);
			layout_washerArrive.setVisibility(View.GONE);
			layout_washerCheck.setVisibility(View.GONE);
			layout_washerWashering.setVisibility(View.GONE);
			layout_washerEnd.setVisibility(View.GONE);
			layout_pingjia.setVisibility(View.GONE);
			layout_orderEnd.setVisibility(View.GONE);
			layout_next.setVisibility(View.VISIBLE);
			tv_netDo.setText("美护师正在赶往指定位置");
		}
		if(entity.getOrderSubEntity()!=null&&entity.getOrderSubEntity().getSubStatus().equals("arrived")){//美护师到达指定位置，还没开始检查车辆
			//显示生成订单的时间
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//支付时间
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//美护师数据
			layout_receive.setVisibility(View.VISIBLE);
			//美护师接单时间
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//设置美护师的头像
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//美护师姓名
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//美护师分数。根据分数设置"星星"
			String score=NumberUtils.InterDivisionInter(entity.getWasherEntity().getUserTotalScore(), entity.getWasherEntity().getUserCommentCount());
			int mScore=Integer.parseInt(score);
			ImageView[] imageview={wahser_img1,wahser_img2,wahser_img3,wahser_img4,wahser_img5};
			setStarImageView(mScore,imageview);
			//美护师到达指定位置的时间
			washerArriveImg_arrive.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getArrivedTime()));

			layout_unpay.setVisibility(View.GONE);
			layout_pay.setVisibility(View.VISIBLE);
			layout_receive.setVisibility(View.VISIBLE);
			layout_washerArrive.setVisibility(View.VISIBLE);
			layout_washerCheck.setVisibility(View.GONE);
			layout_washerWashering.setVisibility(View.GONE);
			layout_washerEnd.setVisibility(View.GONE);
			layout_pingjia.setVisibility(View.GONE);
			layout_orderEnd.setVisibility(View.GONE);
			layout_next.setVisibility(View.VISIBLE);
			tv_netDo.setText("美护师准备检查车辆");
		}
		if(entity.getOrderSubEntity()!=null&&entity.getOrderSubEntity().getSubStatus().equals("checked")){//美护师检查车辆，还没有开始清洗
			//显示生成订单的时间
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//支付时间
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//美护师数据
			layout_receive.setVisibility(View.VISIBLE);
			//美护师接单时间
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//设置美护师的头像
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//美护师姓名
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//美护师分数。根据分数设置"星星"
			String score=NumberUtils.InterDivisionInter(entity.getWasherEntity().getUserTotalScore(), entity.getWasherEntity().getUserCommentCount());
			int mScore=Integer.parseInt(score);
			ImageView[] imageview={wahser_img1,wahser_img2,wahser_img3,wahser_img4,wahser_img5};
			setStarImageView(mScore,imageview);
			//美护师到达指定位置的时间
			washerArriveImg_arrive.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getArrivedTime()));
			//检查车辆时间
			washerCheckCarTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getCheckedTime()));
			//美护师上传的车辆图片
			String img1Url=entity.getOrderSubEntity().getImg1Url();
			String img2Url=entity.getOrderSubEntity().getImg1Ur2();
			String img3Url=entity.getOrderSubEntity().getImg1Ur3();
			if(img1Url!=null){
				img1.setImageUrl(WEBInterface.INDEXIMGURL+img1Url);
			}
			if(img2Url!=null){
				img2.setImageUrl(WEBInterface.INDEXIMGURL+img2Url);
			}
			if(img3Url!=null){
				img3.setImageUrl(WEBInterface.INDEXIMGURL+img3Url);
			}
			
			layout_unpay.setVisibility(View.GONE);
			layout_pay.setVisibility(View.GONE);
			layout_receive.setVisibility(View.VISIBLE);
			layout_washerArrive.setVisibility(View.VISIBLE);
			layout_washerCheck.setVisibility(View.VISIBLE);
			layout_washerWashering.setVisibility(View.GONE);
			layout_washerEnd.setVisibility(View.GONE);
			layout_pingjia.setVisibility(View.GONE);
			layout_orderEnd.setVisibility(View.GONE);
			layout_next.setVisibility(View.VISIBLE);
			tv_netDo.setText("美护师即将清洗");
		}
		if(entity.getOrderSubEntity()!=null&&entity.getOrderSubEntity().getSubStatus().equals("washing")){//开始清洗，还没有清洗完
			//显示生成订单的时间
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//支付时间
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//美护师数据
			layout_receive.setVisibility(View.VISIBLE);
			//美护师接单时间
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//设置美护师的头像
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//美护师姓名
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//美护师分数。根据分数设置"星星"
			String score=NumberUtils.InterDivisionInter(entity.getWasherEntity().getUserTotalScore(), entity.getWasherEntity().getUserCommentCount());
			int mScore=Integer.parseInt(score);
			ImageView[] imageview={wahser_img1,wahser_img2,wahser_img3,wahser_img4,wahser_img5};
			setStarImageView(mScore,imageview);
			//美护师到达指定位置的时间
			washerArriveImg_arrive.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getArrivedTime()));
			//检查车辆时间
			washerCheckCarTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getCheckedTime()));
			//美护师上传的车辆图片
			String img1Url=entity.getOrderSubEntity().getImg1Ur2();
			String img2Url=entity.getOrderSubEntity().getImg1Ur2();
			String img3Url=entity.getOrderSubEntity().getImg1Ur2();
			if(img1Url!=null){
				img1.setImageUrl(WEBInterface.INDEXIMGURL+img1Url);
			}
			if(img2Url!=null){
				img2.setImageUrl(WEBInterface.INDEXIMGURL+img2Url);
			}
			if(img3Url!=null){
				img3.setImageUrl(WEBInterface.INDEXIMGURL+img3Url);
			}
			//美护师开始清洗时间
			washeringTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getWashTime()));
			layout_unpay.setVisibility(View.GONE);
			layout_pay.setVisibility(View.GONE);
			layout_receive.setVisibility(View.VISIBLE);
			layout_washerArrive.setVisibility(View.VISIBLE);
			layout_washerCheck.setVisibility(View.VISIBLE);
			layout_washerWashering.setVisibility(View.GONE);
			layout_washerEnd.setVisibility(View.GONE);
			layout_pingjia.setVisibility(View.GONE);
			layout_orderEnd.setVisibility(View.GONE);
			layout_next.setVisibility(View.VISIBLE);
			tv_netDo.setText("美护师就快清洗完成了");
		}
		if(entity.getOrderSubEntity()!=null&&entity.getOrderSubEntity().getSubStatus().equals("washed")&&entity.getOrderStatus().equals("finish")){//清洗完成，还没有评价
			//显示生成订单的时间
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//支付时间
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//美护师数据
			layout_receive.setVisibility(View.VISIBLE);
			//美护师接单时间
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//设置美护师的头像
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//美护师姓名
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//美护师分数。根据分数设置"星星"
			String score=NumberUtils.InterDivisionInter(entity.getWasherEntity().getUserTotalScore(), entity.getWasherEntity().getUserCommentCount());
			int mScore=Integer.parseInt(score);
			ImageView[] imageview={wahser_img1,wahser_img2,wahser_img3,wahser_img4,wahser_img5};
			setStarImageView(mScore,imageview);
			//拨打美护师电话
			phoneCall.setOnClickListener(timeProgressClickListener);
			//与美护师语音聊天
			yuyin.setOnClickListener(timeProgressClickListener);
			//取消订单
			orderCancel.setOnClickListener(timeProgressClickListener);
			//美护师到达指定位置的时间
			washerArriveImg_arrive.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getArrivedTime()));
			//检查车辆时间
			washerCheckCarTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getCheckedTime()));
			//美护师上传的车辆图片
			String img1Url=entity.getOrderSubEntity().getImg1Url();
			String img2Url=entity.getOrderSubEntity().getImg1Ur2();
			String img3Url=entity.getOrderSubEntity().getImg1Ur3();
			if(img1Url!=null){
				img1.setImageUrl(WEBInterface.INDEXIMGURL+img1Url);
			}
			if(img2Url!=null){
				img2.setImageUrl(WEBInterface.INDEXIMGURL+img2Url);
			}
			if(img3Url!=null){
				img3.setImageUrl(WEBInterface.INDEXIMGURL+img3Url);
			}
			//美护师开始清洗时间
			washeringTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getWashTime()));
			//美护师清洗完成时间
			washerEndTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getWashedTime()));
			//评价美护师
			btn_payOrder.setOnClickListener(timeProgressClickListener);
			
			layout_unpay.setVisibility(View.GONE);
			layout_pay.setVisibility(View.VISIBLE);
			layout_receive.setVisibility(View.VISIBLE);
			layout_washerArrive.setVisibility(View.VISIBLE);
			layout_washerCheck.setVisibility(View.VISIBLE);
			layout_washerWashering.setVisibility(View.VISIBLE);
			layout_washerEnd.setVisibility(View.VISIBLE);
			layout_pingjia.setVisibility(View.GONE);
			layout_orderEnd.setVisibility(View.GONE);
			layout_next.setVisibility(View.VISIBLE);
			tv_netDo.setText("美护师就快清洗完成了,来个评价吧");
		}
		if(entity.getOrderStatus().equals("comment")){//用户评价完
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//支付时间
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//美护师数据
			layout_receive.setVisibility(View.VISIBLE);
			//美护师接单时间
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//设置美护师的头像
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//美护师姓名
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//美护师分数。根据分数设置"星星"
			String score=NumberUtils.InterDivisionInter(entity.getWasherEntity().getUserTotalScore(), entity.getWasherEntity().getUserCommentCount());
			int mScore=Integer.parseInt(score);
			ImageView[] imageview={wahser_img1,wahser_img2,wahser_img3,wahser_img4,wahser_img5};
			setStarImageView(mScore,imageview);
			//拨打美护师电话
			phoneCall.setOnClickListener(timeProgressClickListener);
			//与美护师语音聊天
			yuyin.setOnClickListener(timeProgressClickListener);
			//取消订单
			orderCancel.setOnClickListener(timeProgressClickListener);
			//美护师到达指定位置的时间
			washerArriveImg_arrive.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getArrivedTime()));
			//检查车辆时间
			washerCheckCarTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getCheckedTime()));
			//美护师上传的车辆图片
			String img1Url=entity.getOrderSubEntity().getImg1Url();
			String img2Url=entity.getOrderSubEntity().getImg1Ur2();
			String img3Url=entity.getOrderSubEntity().getImg1Ur3();
			if(!img1Url.equals("")){
				img1.setImageUrl(WEBInterface.INDEXIMGURL+img1Url);
			}
			if(!img2Url.equals("")){
				img2.setImageUrl(WEBInterface.INDEXIMGURL+img2Url);
			}
			if(!img3Url.equals("")){
				img3.setImageUrl(WEBInterface.INDEXIMGURL+img3Url);
			}
			//美护师开始清洗时间
			washeringTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getWashTime()));
			//美护师清洗完成时间
			washerEndTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getArrivedTime()));
			//评价美护师
			
			//评价时间pingjiaTime
			pingjiaTime.setText(""+TimeUtils.millsToDateTime(entity.getCommentEntity().getCommentCreateTime()));
			//评价内容
			pingjiaContext.setText(""+entity.getCommentEntity().getCommentContent());
			//评价分数
			String pinglunScore=entity.getCommentEntity().getCommentScore();
			Integer mpinglunScore=Integer.parseInt(pinglunScore);
			ImageView[] pinglunImageView={pingajiaImg1,pingajiaImg2,pingajiaImg3,pingajiaImg4,pingajiaImg5};
			setStarImageView(mpinglunScore,pinglunImageView);
			//评论提交的图片
			String pinglun1Url=entity.getCommentEntity().getImg1();
			String pinglun2Ur=entity.getCommentEntity().getImg2();
			String pinglun3Url=entity.getCommentEntity().getImg3();
			if(!pinglun1Url.equals("")){
				pinglunImg1.setImageUrl(WEBInterface.INDEXIMGURL+pinglun1Url);
			}
			if(!pinglun1Url.equals("")){
				pinglunImg2.setImageUrl(WEBInterface.INDEXIMGURL+pinglun2Ur);
			}
			if(!pinglun2Ur.equals("")){
				pinglunImg3.setImageUrl(WEBInterface.INDEXIMGURL+pinglun3Url);
			}
			
			layout_unpay.setVisibility(View.GONE);
			layout_pay.setVisibility(View.VISIBLE);
			layout_receive.setVisibility(View.VISIBLE);
			layout_washerArrive.setVisibility(View.VISIBLE);
			layout_washerCheck.setVisibility(View.VISIBLE);
			layout_washerWashering.setVisibility(View.VISIBLE);
			layout_washerEnd.setVisibility(View.VISIBLE);
			layout_pingjia.setVisibility(View.VISIBLE);
			layout_orderEnd.setVisibility(View.VISIBLE);
			layout_next.setVisibility(View.GONE);
			
		}

	}


	/**设置美护师头像*/
	private void setWasherImg(final CircleImageView view,String imageUrl){
		RequestQueue mQueue = Volley.newRequestQueue(getActivity());  
		final String imgUrl=WEBInterface.INDEXIMGURL+imageUrl;
		@SuppressWarnings("deprecation")
		ImageRequest imageRequest = new ImageRequest(imgUrl,  
				new Response.Listener<Bitmap>() {  
			@Override  
			public void onResponse(Bitmap response) {  
				view.setCircleImageBitmap(response);  
			}  
		}, 0, 0, Config.RGB_565, new Response.ErrorListener() {  
			@Override  
			public void onErrorResponse(VolleyError error) { 
				//touxiang.setCircleImageBitmap(); 
			}  
		});  
		mQueue.add(imageRequest);
	}

	/**设置五个星星*/
	private void setStarImageView(int score,ImageView[] iamgeview){
		switch (score) {
		case 0://icon_evaluate_selected
			iamgeview[0].setImageResource(R.drawable.icon_evaluate_normal);
			iamgeview[1].setImageResource(R.drawable.icon_evaluate_normal);
			iamgeview[2].setImageResource(R.drawable.icon_evaluate_normal);
			iamgeview[3].setImageResource(R.drawable.icon_evaluate_normal);
			iamgeview[4].setImageResource(R.drawable.icon_evaluate_normal);
			break;
		case 1:
			iamgeview[0].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[1].setImageResource(R.drawable.icon_evaluate_normal);
			iamgeview[2].setImageResource(R.drawable.icon_evaluate_normal);
			iamgeview[3].setImageResource(R.drawable.icon_evaluate_normal);
			iamgeview[4].setImageResource(R.drawable.icon_evaluate_normal);
			break;
		case 2:
			iamgeview[0].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[1].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[2].setImageResource(R.drawable.icon_evaluate_normal);
			iamgeview[3].setImageResource(R.drawable.icon_evaluate_normal);
			iamgeview[4].setImageResource(R.drawable.icon_evaluate_normal);
			break;
		case 3:
			iamgeview[0].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[1].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[2].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[3].setImageResource(R.drawable.icon_evaluate_normal);
			iamgeview[4].setImageResource(R.drawable.icon_evaluate_normal);
			break;
		case 4:
			iamgeview[0].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[1].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[2].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[3].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[4].setImageResource(R.drawable.icon_evaluate_normal);
			break;
		case 5:
			iamgeview[0].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[1].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[2].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[3].setImageResource(R.drawable.icon_evaluate_selected);
			iamgeview[4].setImageResource(R.drawable.icon_evaluate_selected);
			break;
		}
	}


	/**时间轴点击事件*/
	private View.OnClickListener timeProgressClickListener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.phoneCall://拨打美护师电话
				String phoneNo=entity.getWasherEntity().getUserPhone();
				Intent intentPhone = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				getActivity().startActivity(intentPhone);
				break;
			case R.id.btn_payOrder://立即支付
				String orderId=entity.getId();
				Intent intent_pay=new Intent(getActivity(),PayMoneyActivity.class);
				startActivity(intent_pay);
				getActivity().finish();
				break;
			case R.id.yuyin://和美护师语音聊天
				//存储当前联系人
				currentChatObj=entity;
				saveRongIMUser2DB(entity);
				RongIM.getInstance().startPrivateChat(getActivity(), entity.getWasherEntity().getWaherId(), "title");
				break;
			case R.id.btn_pingjia://评价美护师
				//判断评论时间是否已经过期
				//当前的时间戳
				Date todateDate=new Date();
				long todateTime = todateDate.getTime();
				//订单完成时间
				String completeTime=entity.getOrderFiniahTime();
				long mPromissTime=Long.parseLong(completeTime);
				long betweenDays = (long)((todateTime-mPromissTime) / (1000 * 60 * 60 *24) + 0.5);
				if(betweenDays>3){
					ToastUtil.show(getApplicationContext(), "已过期，不能进行评论", 2000);
					return;
				}
				Bitmap bitmap=MyBitmapUtils.takeScreenShot(getActivity());
				MyBitmapUtils.savePic(bitmap, "/sdcard/yckx/yckx_temp.png");
				Log.d("yckx","--待评论的订单"+entity.toString());
				Intent intent=new Intent(getActivity(),CommentActivity.class);
				//intent.putExtra("CommentActivity", entity);
				intent.putExtra("CommentActivity", entity.getId());
				startActivity(intent);
				break;
			case R.id.orderCancel://取消订单
				ownCancelOrder(entity.getId());
				break;
			}

		}
	};

	/**刷新数据的时候根据订单号进行再次查询*/
	private void getOrderDetailData(final String orderId){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url=WEBInterface.ORDERDETAIL_URL+orderId;//orderId3262
				String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
				String respose=ApacheHttpUtil.httpGet(url, token, null);
				if(respose!=null){
					try {
						OrderDetailEntity refreshEntity=ParseJSONUtils.getOrderDtailEntity(respose);
						Message msg=new Message();
						msg.what=REFRESH_SUCCESS;
						msg.obj=refreshEntity;
						mHandler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Log.d("yckx", "订单详情界面数据"+respose);
					//解析数据
				}
			}
		}).start();
	}


	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_SUCCESS://
				entity=(OrderDetailEntity) msg.obj;
				//重新设置数据
				GlobalServiceUtils.getInstance().setGloubalServiceOrderDetailEntity(entity);
				try {
					setData();
				} catch (Exception e) {
					Log.d("yckx", "刷新后调用setData()异常");
				}
				try {
					selectTab3();
				} catch (Exception e) {
					Log.d("yckx", "刷新后调用selectTab3()异常");
				}
				break;

			case CANCLEORDER_SUCCESS://取消订单成功,取消成功，重新刷新数据
				ToastUtil.show(getApplicationContext(), "取消订单成功", 2000);
				break;
			}
		};
	};
	
	private final int CANCLEORDER_SUCCESS=10;
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
	
	
	private OrderDetailEntity currentChatObj;
	
	/**保存即使聊天的对象*/
	private void saveRongIMUser2DB(OrderDetailEntity orderlistEntity){
		//检查是否存在数据库
		if(!DBRead.isExistsCardbFile()){//不存在则加载
			try {
				// 将本地项目中的Assets/db/commonnum.db文件复制写出到 DBRead.telFile文件中
				AssetsDBManager.copyAssetsFileToFile(getApplicationContext(), "db/carsplist.db", DBRead.carFile);
			} catch (IOException e) {
				ToastUtil.show(getActivity(), "初始化车辆信息数据库异常", 2000);
			}
		}
		//添加到数据库
		DBRead.addRongIMUser(orderlistEntity, true);
	}

	/**融云添加聊天用户信息*/
	@Override
	public UserInfo getUserInfo(String arg0) {
		if(currentChatObj!=null){
			Log.d("yckx", "融云返回getUserInfo()");
			return new UserInfo(currentChatObj.getOwnerEntity().getId(), currentChatObj.getOwnerEntity().getUserPhone(), Uri.parse(WEBInterface.INDEXIMGURL+currentChatObj.getOwnerEntity().getUserImageUrl()));
		}
		Log.d("yckx", "融云返回getUserInfo()失败:"+arg0);
		return null;
	}
}
