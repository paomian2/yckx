package com.brother.yckx.control.activity.owner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.androide.lib3.volley.RequestQueue;
import com.androide.lib3.volley.Response;
import com.androide.lib3.volley.VolleyError;
import com.androide.lib3.volley.toolbox.ImageRequest;
import com.androide.lib3.volley.toolbox.Volley;
import com.brother.BaseActivity;
import com.brother.BaseAdapter2;
import com.brother.utils.Contants;
import com.brother.utils.MyBitmapUtils;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.NumberUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.JavaHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.action.ActionUtils;
import com.brother.yckx.adapter.activity.HistoricalEvaluationAdapter;
import com.brother.yckx.control.activity.commonly.TouristCommentActivity;
import com.brother.yckx.control.activity.map.NativeLocationActivity;
import com.brother.yckx.control.activity.owner.OrdersActivity.CompanyProductAdapter.ViewHolerProduct;
import com.brother.yckx.model.BusinessEntity;
import com.brother.yckx.model.HistoryCommentEntity;
import com.brother.yckx.model.ProductEntity;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.LoadListView;
import com.brother.yckx.view.LoadListView.ILoadListener;
import com.brother.yckx.view.banner.ImageCycleView;
import com.brother.yckx.view.banner.ImageLoaderHelper;
import com.i4evercai.development.ImageLoader.ImageLoader;
import com.tencent.mm.sdk.modelmsg.WXTextObject;

public class OrdersActivity extends BaseActivity implements ILoadListener{
	private TextView free_stop,expense_stop,chewei,dazhe;
	//private LoadListView pingjia_listview;
	private HistoricalEvaluationAdapter adapter;
	//private RelativeLayout addOrders_layout;
	//private ImageView companeyImg;
	private TextView companeyName;
	private ImageView[] starImages=new ImageView[5];//公司评论星级
	private TextView companyAdress;
	//private ImageView companyImg2;
	private ImageView imgPhone;//拨打电话
	private BusinessEntity busineess;
	private List<ProductEntity> productEntityList;
	private LoadListView productList;//洗车产品列表
	private PruductAdapter mProductAdapter;
	private LinearLayout layout_show;
	private boolean isLoadedConpaneyImg1,isLoadedConpaneyImg2,isLoadedProductImg;
	private LinearLayout layout_showproduct;//显示优车快洗产品
	private boolean showYCKXproducts;
	private ListView lv_CompanyProducts;//显示商家产品的列表(如：标准房)
	private CompanyProductAdapter companyProductAdapter;
	private ImageCycleView imageCycleView;//轮播图
	//客服
	private LinearLayout layout_customer;
	//第二个页面---
	private List<HistoryCommentEntity> list;
	/**获取历史评论数据成功*/
	private final int LOADHISTORY_COMMENT_SUCCESS=10;
	/**评论内容加载完成*/
	private final int LOADHISTORY_OVER=104;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		setActionBar(R.string.addOrderTitle, R.drawable.btn_homeasup_default, NULL_ID, listener);
		//
		//showLoadingDialog();
		busineess=(BusinessEntity) getIntent().getSerializableExtra("BusinessEntity");
		productEntityList=busineess.getProducts();
		setupView();
		//getHistoryEvolution(busineess.getCompanyId());
	}



	private void setupView(){
		initHeaderView();//初始化商家产品listview的headerview控件
		//setupHistoryComment();
		initBanner();//初始化headerview中轮播界面控件
		initFooderView();//初始化商家产品listview的fooderView控件
		initView();//初始化本页view控件
	}

	private void initView(){
		//显示商家自身的产品列表
		lv_CompanyProducts=(ListView) findViewById(R.id.lv_companyProduct);
		lv_CompanyProducts.addHeaderView(headView);
		lv_CompanyProducts.addFooterView(fooderView1);
		lv_CompanyProducts.addFooterView(fooderView);
		companyProductAdapter=new CompanyProductAdapter(this,imageLoader);
		lv_CompanyProducts.setAdapter(companyProductAdapter);
		//添加测试数据
		List<String> companyProductsList=new ArrayList<String>();
		companyProductsList.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1458978841&di=e32b9114944a11d3e716ccdec9a644f0&src=http://www.elongstatic.com/imageapp/hotels/hotelimages/1526/41526002/dadcc874-fbb6-45aa-aa8d-9dc9afbe4835.png");
		companyProductsList.add("http://upload.17u.net/uploadpicbase/2010/08/06/aa/2010080613352820173.jpg");
		companyProductsList.add("http://www.elongstatic.com/imageapp/hotels/hotelimages/2014/42014016/3aca45ba-ac43-4019-a6ca-40b44bf6bfe3.png");
		companyProductsList.add("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E9%85%92%E5%BA%97%E5%9B%BE%E7%89%87&step_word=&pn=117&spn=0&di=320097694631&pi=&rn=1&tn=baiduimagedetail&is=&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=3825255667%2C1216227081&os=1902773858%2C2678572285&simid=3440667503%2C337781279&adpicid=0&ln=1997&fr=&fmq=1458979554202_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=15&oriquery=&objurl=http%3A%2F%2Fwww.skbsw.com%2Fuploadfile%2Fhotelpictures%2Fthumb_JhS1_tJYzyTY_rIzJ720_2014-11-23-17-14-07.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bfhkfo_z%26e3Bv54AzdH3Ftg1jx_z%26e3Brir%3F4%3Dv5gpjgp%26v%3Dtg1jx%26w%3Dfi5o%26vwpt1%3Dl%26t1%3Dnaa&gsm=3c");
		companyProductsList.add("http://pic.58pic.com/58pic/13/60/01/42D58PICFpK_1024.jpg");
		companyProductAdapter.addDataToAdapter(companyProductsList);
		companyProductAdapter.notifyDataSetChanged();

		//底部加号控件
		final RelativeLayout layou_add=(RelativeLayout)findViewById(R.id.layou_add);
		layou_add.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ToastUtil.show(getApplicationContext(), "aaa", 3000);
				initPopupMenu();
				// 弹出pop窗口 (评论，分享，收藏)
				addPopuWindow.showAtLocation(layou_add, Gravity.RIGHT,0, 0);
				//addPopuWindow.showAsDropDown(v);
				layou_add.setVisibility(View.GONE);
			}});
	}


	private ImageView banner1, banner2, banner4;
	private ImageLoader bannerimageLoader;
	//分享控件
	private ImageView img_share;
	/**图片轮播*/
	private void initBanner(){
		imageCycleView=(ImageCycleView)headView.findViewById(R.id.cycleView);
		/**装在数据的集合  文字描述*/
		ArrayList<String> imageDescList=new ArrayList<String>();
		/**装在数据的集合  图片地址*/
		ArrayList<String> urlList=new ArrayList<String>();
		for(int i=0;i< 5;i++){
			/**添加数据*/
			urlList.add(Contants.IMAGES[i]);
		}
		imageDescList.add("小仓柚子");
		imageDescList.add("抚媚妖娆性感美女");
		imageDescList.add("热血沸腾 比基尼");
		imageDescList.add(" 台球美女");
		imageDescList.add("身材妙曼");
		initCarsuelView(imageDescList, urlList);

		//弹出分享布局
		img_share=(ImageView)headView.findViewById(R.id.img_share);
		img_share.setOnClickListener(new OnClickListener(){
			@SuppressLint("SdCardPath")
			@Override
			public void onClick(View arg0) {
				//截屏
				Bitmap screenbitmap=MyBitmapUtils.takeScreenShot(OrdersActivity.this);
				if(screenbitmap!=null){
					//存储
					File yckx_tempFile=new File("/sdcard/yckx/");
					String fileName = "/sdcard/yckx/yckx_screen_temp.png";
					FileOutputStream gaodeFos = null;  
					if(!yckx_tempFile.exists()){
						yckx_tempFile.mkdirs();// 创建文件夹  
					}
					try {   
						gaodeFos = new FileOutputStream(fileName); //写到存储卡 
						boolean b = screenbitmap.compress(CompressFormat.PNG, 100, gaodeFos);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					try {  
						gaodeFos.flush();  
						gaodeFos.close();   
					} catch (IOException e) {  
						e.printStackTrace();  
					}  
				}
				//showShareDialog();
				String shareTittle=busineess.getCompanyName();
				String shareText=busineess.getAddress();//分享商家信息
				String commentText="";//QQ空间分享是默认的评价
				String shareUrl=WEBInterface.YCKX_APK_DOWNLOAD_URL;//优车快洗下载的连接
				showShare(shareTittle,shareText,commentText,shareUrl);
			}});
		//跳转到图片组列表
		ImageView img_showAll=(ImageView)headView.findViewById(R.id.img_showAll);
		img_showAll.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrdersActivity.this,PersonalLoadImgActivity.class);
				startActivity(intent);
			}});
	}

	private View headView;
	/**第一个页面*/
	private void initHeaderView(){
		headView=getLayoutInflater().inflate(R.layout.row_business_home_companyproductlist_header,null);

		free_stop=(TextView)headView.findViewById(R.id.free_stop);
		expense_stop=(TextView)headView.findViewById(R.id.expense_stop);
		chewei=(TextView)headView.findViewById(R.id.chewei);
		dazhe=(TextView)headView.findViewById(R.id.dazhe);
		free_stop.setText(busineess.getFreeTime());
		expense_stop.setText(busineess.getSpacePrice());
		chewei.setText(busineess.getSpaceCount());
		dazhe.setText(busineess.getSaleMsg());
		//公司评论星级
		starImages[0]=(ImageView)headView.findViewById(R.id.xingImg1);
		starImages[1]=(ImageView)headView.findViewById(R.id.xingImg2);
		starImages[2]=(ImageView)headView.findViewById(R.id.xingImg3);
		starImages[3]=(ImageView)headView.findViewById(R.id.xingImg4);
		starImages[4]=(ImageView)headView.findViewById(R.id.xingImg5);
		Integer companyScore=0;
		if(busineess.getAvgScore().equals("")){
			companyScore=0;
		}else{
			companyScore=Integer.parseInt(busineess.getAvgScore());
			TextView businessScore=(TextView) headView.findViewById(R.id.businessScore);
			businessScore.setText(companyScore+"");
		}
		for(int i=0;i<companyScore;i++){
			starImages[i].setBackgroundResource(R.drawable.icon_evaluate_selected);
		}
		//点击评论星级进入下一个评论页面
		headView.findViewById(R.id.layout_star).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrdersActivity.this,TouristCommentActivity.class);
				intent.putExtra("TouristCommentActivity", busineess);
				startActivity(intent);
			}});


		layout_show=(LinearLayout)headView.findViewById(R.id.layout_show);
		//companeyImg=(ImageView) findViewById(R.id.companeyImg);
		companeyName=(TextView)headView.findViewById(R.id.companyName);
		//companyImg2=(ImageView) findViewById(R.id.companyImg2);
		companyAdress=(TextView)headView.findViewById(R.id.companyAdress);
		LinearLayout layout_daohang=(LinearLayout)headView.findViewById(R.id.layout_daohang);
		layout_daohang.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrdersActivity.this,NativeLocationActivity.class);
				intent.putExtra("NativeLocationActivity", busineess);
				startActivity(intent);
			}});
		//距离
		TextView distance=(TextView)headView.findViewById(R.id.distance);
		String mDistance=NumberUtils.double2poins(busineess.getDistance())+"";
		distance.setText(mDistance);
		imgPhone=(ImageView)headView.findViewById(R.id.imgPhone);
		imgPhone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//拨打电话
				String phoneNo=busineess.getCompanyPhone();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				OrdersActivity.this.startActivity(intent);
			}});
		
		//现场付,判断商家是否支持现场付，支付则显示现场付Item
		RelativeLayout layout_payOnSpot=(RelativeLayout) headView.findViewById(R.id.layout_payOnSpot);
		TextView tv_payOnSpot=(TextView) headView.findViewById(R.id.tv_payOnSpot);
		tv_payOnSpot.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrdersActivity.this,PayOnSpotActivity.class);
				intent.putExtra("BusinessEntity", busineess);
				startActivity(intent);
			}});
		
		//洗车产品列表
		productList=(LoadListView)headView.findViewById(R.id.productListView);
		mProductAdapter=new PruductAdapter(this);
		mProductAdapter.addDataToAdapter(productEntityList);
		productList.setAdapter(mProductAdapter);
		productList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ProductEntity mEntity=productEntityList.get(arg2);
				Intent intent=new Intent(OrdersActivity.this,OrderDetaileActivity.class);
				intent.putExtra("BusinessEntity", busineess);
				intent.putExtra("ProductEntity", mEntity);
				OrdersActivity.this.startActivity(intent);
			}});

		//getCompanyImg2(WEBInterface.INDEXIMGURL+busineess.getCompanyMapImage(),SETIMG);//耗时操作
		companeyName.setText(""+busineess.getCompanyName());
		//耗时操作
		//getCompanyImg2(WEBInterface.INDEXIMGURL+busineess.getCompanyImage(),SETIMG2);
		companyAdress.setText(""+busineess.getAddress());

		//显示优车快洗洗车产品
		layout_showproduct=(LinearLayout) headView.findViewById(R.id.layout_showproduct);
		layout_showproduct.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(showYCKXproducts){
					showYCKXproducts=false;
				}else{
					showYCKXproducts=true;
				}
				if(showYCKXproducts){
					//productList.setVisibility(View.VISIBLE);
					LinearLayout layout_Productlist=(LinearLayout) headView.findViewById(R.id.layout_productlist);
					layout_Productlist.setVisibility(View.VISIBLE);
				}else{
					//productList.setVisibility(View.GONE);
					LinearLayout layout_Productlist=(LinearLayout) headView.findViewById(R.id.layout_productlist);
					layout_Productlist.setVisibility(View.GONE);
				}
			}});
	}


	private View fooderView1;
	//layout_listLoadDown是否显示要看商家产品列表listviewitem的个数，个数大于10则显示
	//tv_listItemtCount的值要根据商家产品列表listviewitem的个数来赋值tv_listItemtCount=listviewItem数-10
	private View fooderView;
	/**商家产品listview底部view*/
	private void initFooderView(){
		
		fooderView1=getLayoutInflater().inflate(R.layout.row_business_home_companyproductlist_fooder1,null);
		TextView tv_listItemtCount=(TextView) fooderView1.findViewById(R.id.tv_listItemtCount);
		
		RelativeLayout layout_listLoadDown=(RelativeLayout) fooderView1.findViewById(R.id.layout_listLoadDown);
		layout_listLoadDown.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ToastUtil.show(getApplicationContext(), "加载更多",2000);
			}});
		
		fooderView=getLayoutInflater().inflate(R.layout.row_business_home_companyproductlist_fooder,null);
		
		//公司联系电话
		fooderView.findViewById(R.id.layout_kefu).setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						//拨打电话
						String phoneNo=busineess.getCompanyPhone();
						Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
						OrdersActivity.this.startActivity(intent);
					}});
		
		//联系客服
		fooderView.findViewById(R.id.layout_customer).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//拨打电话
				String phoneNo=busineess.getManager().getUserPhone();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				OrdersActivity.this.startActivity(intent);
			}});
		//设置客服信息
		if(busineess.getManager()==null){
			LinearLayout layout_kefu=(LinearLayout) fooderView.findViewById(R.id.layout_kefu);
			layout_kefu.setVisibility(View.GONE);
		}else{
			TextView tv_customer=(TextView) fooderView.findViewById(R.id.tv_customer);
			tv_customer.setText(busineess.getManager().getUserName());
			final CircleImageView customerImage=(CircleImageView) fooderView.findViewById(R.id.customerImage);
			String customerImageUrl=WEBInterface.INDEXIMGURL+busineess.getManager().getUserImageUrl();
			RequestQueue mQueue = Volley.newRequestQueue(this);  
			@SuppressWarnings("deprecation")
			ImageRequest imageRequest = new ImageRequest(customerImageUrl,  
					new Response.Listener<Bitmap>() {  
				@Override  
				public void onResponse(Bitmap response) {  
					customerImage.setCircleImageBitmap(response);  
				}  
			}, 0, 0, Config.RGB_565, new Response.ErrorListener() {  
				@Override  
				public void onErrorResponse(VolleyError error) { 
					//touxiang.setCircleImageBitmap();
				}  
			});  
			mQueue.add(imageRequest);
		}
		//商家详情
		fooderView.findViewById(R.id.tv_shangjiahangqing).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrdersActivity.this,CompanyIntroductionActivity.class);
				startActivity(intent);
			}});
		//商家入驻
		fooderView.findViewById(R.id.layout_shangjiaruzhu).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrdersActivity.this,RegisterCompanyActivity.class);
				startActivity(intent);
			}});
	}


	/**初始化轮播图*/
	public void initCarsuelView(ArrayList<String> imageDescList,ArrayList<String>urlList) {
		LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ActionUtils.getScreenHeight(OrdersActivity.this) * 3 / 10);
		imageCycleView.setLayoutParams(cParams);
		ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
			@Override
			public void onImageClick(int position, View imageView) {
				/**实现点击事件*/
				Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();;
			}
			@Override
			public void displayImage(String imageURL, ImageView imageView) {
				/**在此方法中，显示图片，可以用自己的图片加载库，也可以用本demo中的（Imageloader）*/
				ImageLoaderHelper.getInstance().loadImage(imageURL, imageView);
			}
		};
		/**设置数据*/
		imageCycleView.setImageResources(imageDescList, urlList, mAdCycleViewListener);
		imageCycleView.startImageCycle();
	}


	private void showShare(String shareTittle,String shareText,String commentText,String tagetUrl) {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		//oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(shareTittle);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(tagetUrl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(shareText);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/yckx/yckx_screen_temp.png");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(tagetUrl);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment(commentText);
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(shareTittle);
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(tagetUrl);

		// 启动分享GUI
		oks.show(this);
	}




	/**分享布局*/
	private Dialog shareDialog;
	private void showShareDialog(){
		LayoutInflater inflater=getLayoutInflater();
		View shareView = inflater.inflate(R.layout.row_popu_sharelayout, null);
		shareDialog = new AlertDialog.Builder(OrdersActivity.this).create();
		shareDialog.show();
		shareDialog.setContentView(shareView);
		WindowManager m =getWindowManager();
		Window dialogWindow = shareDialog.getWindow();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams params = shareDialog.getWindow().getAttributes();
		params.width = (int)d.getWidth();
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//设置bottom的偏移量
		params.y=30;
		shareDialog.getWindow().setAttributes(params);

		shareView.findViewById(R.id.wx).setOnClickListener(listener);
		shareView.findViewById(R.id.pyq).setOnClickListener(listener);
		shareView.findViewById(R.id.wb).setOnClickListener(listener);
		shareView.findViewById(R.id.qq).setOnClickListener(listener);
		shareView.findViewById(R.id.qzone).setOnClickListener(listener);
		shareView.findViewById(R.id.share_cancel).setOnClickListener(listener);
	}

	/**点击加号时弹出的窗口*/
	private PopupWindow addPopuWindow;
	private View popupView;// popup窗口
	/** 初始化PopupMenu */
	private void initPopupMenu() {
		if (popupView != null) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = inflater.inflate(R.layout.row_popuwindow_business_home, null);
		popupView.findViewById(R.id.img_customer).setOnClickListener(popupViewlistener);
		popupView.findViewById(R.id.img_payOnSpot).setOnClickListener(popupViewlistener);
		popupView.findViewById(R.id.img_phone).setOnClickListener(popupViewlistener);
		popupView.findViewById(R.id.img_attention).setOnClickListener(popupViewlistener);
		popupView.findViewById(R.id.img_luchi).setOnClickListener(popupViewlistener);
		popupView.findViewById(R.id.tv_cancel).setOnClickListener(popupViewlistener);
		addPopuWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
		// 设置为touch窗口外关闭(一定要先设置好它的背景)
		addPopuWindow.setBackgroundDrawable(new ColorDrawable(0));
		addPopuWindow.update();
		addPopuWindow.setFocusable(false);
		addPopuWindow.setOutsideTouchable(false);
		addPopuWindow.setAnimationStyle(R.style.popwin_anim_style);
	}


	private View.OnClickListener popupViewlistener=new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.img_customer:

				break;
			case R.id.img_payOnSpot:

				break;
			case R.id.img_phone:

				break;
			case R.id.img_attention:

				break;
			case R.id.img_luchi:

				break;
			case R.id.tv_cancel:
				addPopuWindow.dismiss();
				//显示之前的加号
				RelativeLayout layou_add=(RelativeLayout) findViewById(R.id.layou_add);
				layou_add.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}
	};









	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	private View.OnClickListener listener=new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.iv_action_left:
				Intent intent_return=new Intent(OrdersActivity.this,HomeActivity.class);
				startActivity(intent_return);
				finish();
				break;
			}

		}
	};

	/**起调微信分享接口*/
	private void shareWX(){
		WXTextObject textObj=new WXTextObject();
	}



	/***/



	/*private Bitmap companyBitmp_big;
	private Bitmap companyBitmp_small;
	 *//**获取当前公司网络图片*//*
	private void getCompanyImg(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String img=busineess.getCompanyImage();
				String url=WEBInterface.INDEXIMGURL+img;
				InputStream stream=JavaHttpUtil.httpGet(url, null);
				companyBitmp_big=BitmapFactory.decodeStream(stream);
				Message msg=new Message();
				msg.what=SETIMG;
				mHandler.sendMessage(msg);
			}
		}).start();
	}*/

	/**获取当前公司网络图片*//*
	private void getCompanyImg2(final String url,final int action){
		new Thread(new Runnable() {
			@Override
			public void run() {
				InputStream stream=JavaHttpUtil.httpGet(url, null);
				if(action==SETIMG)
					companyBitmp_big=BitmapFactory.decodeStream(stream);
				if(action==SETIMG2)
					companyBitmp_small=BitmapFactory.decodeStream(stream);
				Message msg=new Message();
				msg.what=action;
				mHandler.sendMessage(msg);
			}
		}).start();
	}*/


	/**设置图片大图*//*
	private final int SETIMG=0;
	 *//**设置小图片*//*
	private final int SETIMG2=1;

	@SuppressLint("HandlerLeak") 
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==LOADHISTORY_OVER){
				//加载结束
				pingjia_listview.loadComplete();
				ToastUtil.show(getApplicationContext(), "已全部加载完", 2000);
			}
			if(msg.what==LOADHISTORY_COMMENT_SUCCESS){
				adapter.addDataToAdapter(list);
				adapter.notifyDataSetChanged();
				setListViewHeight(pingjia_listview);
				pingjia_listview.loadComplete();
			}
		};
	};*/
	ViewHolerProduct viewHolder;

	/**商家本身产品适配器,后期再去修改E实体*/
	class CompanyProductAdapter extends BaseAdapter2<String>{

		private com.nostra13.universalimageloader.core.ImageLoader imageLoader;

		public CompanyProductAdapter(Context context,com.nostra13.universalimageloader.core.ImageLoader imageLoader) {
			super(context);
			this.imageLoader=imageLoader;
		}

		@Override
		public View getView(int arg0, View contentView, ViewGroup arg2) {
			viewHolder=null;
			if(contentView==null){
				contentView=layoutInflater.inflate(R.layout.row_activity_business_products,null);
				viewHolder=new ViewHolerProduct(contentView);
				contentView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolerProduct)contentView.getTag();
			}
			//设置图片
			imageLoader.displayImage(getItem(arg0), viewHolder.img_product);
			//设置产品名
			viewHolder.tv_productName.setText("标准房");
			//设置产品内容
			//viewHolder.tv_productIntroduce.setText("");
			//进入产品详情
			viewHolder.layout_produceDetail.setTag(arg0);
			viewHolder.layout_produceDetail.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Integer position=Integer.parseInt(viewHolder.layout_produceDetail.getTag()+"");
					ToastUtil.show(getApplicationContext(), ""+position, 3000);
				}});
			//一键下单
			viewHolder.tv_OneKeyOrder.setTag(arg0);
			viewHolder.tv_OneKeyOrder.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Integer position=Integer.parseInt(viewHolder.tv_OneKeyOrder.getTag()+"");
					ToastUtil.show(getApplicationContext(), ""+position, 3000);

				}});
			return contentView;
		}

		class ViewHolerProduct{
			private ImageView img_product;
			private TextView tv_productName;
			private TextView tv_productIntroduce;
			private TextView tv_productPrice;
			private TextView tv_originalPrice;
			private LinearLayout layout_produceDetail;
			private TextView tv_OneKeyOrder;
			public ViewHolerProduct(View view){
				img_product=(ImageView)view.findViewById(R.id.img_products);
				tv_productName=(TextView)view.findViewById(R.id.tv_productName);
				tv_productIntroduce=(TextView)view.findViewById(R.id.tv_productIntroduction);
				tv_originalPrice=(TextView)view.findViewById(R.id.tv_originalPrice);
				layout_produceDetail=(LinearLayout)view.findViewById(R.id.layout_productDetail);
				tv_OneKeyOrder=(TextView)view.findViewById(R.id.tv_oneKeyOrder);
			}
		}
	}





	Bitmap circleImg=null;
	/**洗车产品适配器*/
	class PruductAdapter extends BaseAdapter2<ProductEntity>{

		public PruductAdapter(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(arg1==null){
				arg1=layoutInflater.inflate(R.layout.row_products,null);
			}
			CircleImageView cardImg=(CircleImageView) arg1.findViewById(R.id.cardImg);
			TextView productName=(TextView) arg1.findViewById(R.id.productName);
			TextView productDec=(TextView) arg1.findViewById(R.id.productDesc);
			TextView productPrice=(TextView) arg1.findViewById(R.id.price);
			final ProductEntity mProductEntity=getItem(arg0);
			cardImg.setTag(mProductEntity.getProductImage());
			//获取网络图片
			new Thread(new Runnable() {
				@Override
				public void run() {
					String icon=WEBInterface.INDEXIMGURL+mProductEntity.getProductImage();
					InputStream stream=JavaHttpUtil.httpGet(WEBInterface.INDEXIMGURL+mProductEntity.getProductImage(), null);
					circleImg=BitmapFactory.decodeStream(stream);
				}
			}).start();


			if(circleImg!=null){
				if(cardImg.getTag()!=null&&cardImg.getTag().equals(mProductEntity.getProductImage())){
					cardImg.setCircleImageBitmap(circleImg);
				}
			}
			productName.setText(""+mProductEntity.getProductName());
			productDec.setText(""+mProductEntity.getProductDesc());
			productPrice.setText(""+mProductEntity.getProductPrice());
			return arg1;
		}

	}


	//--------第二个页面，商家评价页面-----------


	//评价界面----

	/*private void setupHistoryComment(){
		pingjia_listview=(LoadListView)findViewById(R.id.pingjia_listview);
		pingjia_listview.setInterface(this);
		adapter=new HistoricalEvaluationAdapter(OrdersActivity.this);
		pingjia_listview.setAdapter(adapter);
		//setListViewHeight(pingjia_listview);
	}*/
	/**  
	 * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题  
	 * @param listView  
	 */  
	/*public void setListViewHeight(ListView listView) {    

		// 获取ListView对应的Adapter    

		ListAdapter listAdapter = listView.getAdapter();    

		if (listAdapter == null) {    
			return;    
		}    
		int totalHeight = 0;    
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目    
			View listItem = listAdapter.getView(i, null, listView);    
			listItem.measure(0, 0); // 计算子项View 的宽高    
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度    
		}    

		ViewGroup.LayoutParams params = listView.getLayoutParams();    
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));    
		listView.setLayoutParams(params);    
	}    




	private int page=0;
	private int size=20;
	 *//**获取评论列表数据*//*
	private void getHistoryEvolution(final String companyId){
		new Thread(new Runnable() {//测试id:id=12
			@Override
			public void run() {///comment/list/{page}/{size}/{targetOwnerId}
				String url=WEBInterface.COMPANYHISTORY_COMMENT_URL+page+"/"+size+"/"+companyId;
				String token=PrefrenceConfig.getUserMessage(OrdersActivity.this).getUserToken();
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
	}*/


	/**加载更多*//*
	@Override
	public void onLoad() {
		page++;
		getHistoryEvolution(busineess.getCompanyId());

	};*/




	/**加载中的dialog*//*
	private Dialog loadingDialog;
	//----代替popu窗口的dialog窗口----
	private void showLoadingDialog(){
		LayoutInflater inflater=getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_loading, null);
		loadingDialog = new AlertDialog.Builder(OrdersActivity.this).create();
		loadingDialog.show();
		loadingDialog.setCancelable(false);
		loadingDialog.setContentView(view);
		WindowManager m =getWindowManager();
		Window dialogWindow = loadingDialog.getWindow();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams params = loadingDialog.getWindow().getAttributes();
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		dialogWindow.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
		//设置bottom的偏移量
		loadingDialog.getWindow().setAttributes(params);
	}*/

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent intent=new Intent(this,HomeActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}

}
