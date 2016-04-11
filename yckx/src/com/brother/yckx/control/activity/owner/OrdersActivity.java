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
	private ImageView[] starImages=new ImageView[5];//��˾�����Ǽ�
	private TextView companyAdress;
	//private ImageView companyImg2;
	private ImageView imgPhone;//����绰
	private BusinessEntity busineess;
	private List<ProductEntity> productEntityList;
	private LoadListView productList;//ϴ����Ʒ�б�
	private PruductAdapter mProductAdapter;
	private LinearLayout layout_show;
	private boolean isLoadedConpaneyImg1,isLoadedConpaneyImg2,isLoadedProductImg;
	private LinearLayout layout_showproduct;//��ʾ�ų���ϴ��Ʒ
	private boolean showYCKXproducts;
	private ListView lv_CompanyProducts;//��ʾ�̼Ҳ�Ʒ���б�(�磺��׼��)
	private CompanyProductAdapter companyProductAdapter;
	private ImageCycleView imageCycleView;//�ֲ�ͼ
	//�ͷ�
	private LinearLayout layout_customer;
	//�ڶ���ҳ��---
	private List<HistoryCommentEntity> list;
	/**��ȡ��ʷ�������ݳɹ�*/
	private final int LOADHISTORY_COMMENT_SUCCESS=10;
	/**�������ݼ������*/
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
		initHeaderView();//��ʼ���̼Ҳ�Ʒlistview��headerview�ؼ�
		//setupHistoryComment();
		initBanner();//��ʼ��headerview���ֲ�����ؼ�
		initFooderView();//��ʼ���̼Ҳ�Ʒlistview��fooderView�ؼ�
		initView();//��ʼ����ҳview�ؼ�
	}

	private void initView(){
		//��ʾ�̼�����Ĳ�Ʒ�б�
		lv_CompanyProducts=(ListView) findViewById(R.id.lv_companyProduct);
		lv_CompanyProducts.addHeaderView(headView);
		lv_CompanyProducts.addFooterView(fooderView1);
		lv_CompanyProducts.addFooterView(fooderView);
		companyProductAdapter=new CompanyProductAdapter(this,imageLoader);
		lv_CompanyProducts.setAdapter(companyProductAdapter);
		//��Ӳ�������
		List<String> companyProductsList=new ArrayList<String>();
		companyProductsList.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1458978841&di=e32b9114944a11d3e716ccdec9a644f0&src=http://www.elongstatic.com/imageapp/hotels/hotelimages/1526/41526002/dadcc874-fbb6-45aa-aa8d-9dc9afbe4835.png");
		companyProductsList.add("http://upload.17u.net/uploadpicbase/2010/08/06/aa/2010080613352820173.jpg");
		companyProductsList.add("http://www.elongstatic.com/imageapp/hotels/hotelimages/2014/42014016/3aca45ba-ac43-4019-a6ca-40b44bf6bfe3.png");
		companyProductsList.add("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E9%85%92%E5%BA%97%E5%9B%BE%E7%89%87&step_word=&pn=117&spn=0&di=320097694631&pi=&rn=1&tn=baiduimagedetail&is=&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=3825255667%2C1216227081&os=1902773858%2C2678572285&simid=3440667503%2C337781279&adpicid=0&ln=1997&fr=&fmq=1458979554202_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=15&oriquery=&objurl=http%3A%2F%2Fwww.skbsw.com%2Fuploadfile%2Fhotelpictures%2Fthumb_JhS1_tJYzyTY_rIzJ720_2014-11-23-17-14-07.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bfhkfo_z%26e3Bv54AzdH3Ftg1jx_z%26e3Brir%3F4%3Dv5gpjgp%26v%3Dtg1jx%26w%3Dfi5o%26vwpt1%3Dl%26t1%3Dnaa&gsm=3c");
		companyProductsList.add("http://pic.58pic.com/58pic/13/60/01/42D58PICFpK_1024.jpg");
		companyProductAdapter.addDataToAdapter(companyProductsList);
		companyProductAdapter.notifyDataSetChanged();

		//�ײ��Ӻſؼ�
		final RelativeLayout layou_add=(RelativeLayout)findViewById(R.id.layou_add);
		layou_add.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ToastUtil.show(getApplicationContext(), "aaa", 3000);
				initPopupMenu();
				// ����pop���� (���ۣ������ղ�)
				addPopuWindow.showAtLocation(layou_add, Gravity.RIGHT,0, 0);
				//addPopuWindow.showAsDropDown(v);
				layou_add.setVisibility(View.GONE);
			}});
	}


	private ImageView banner1, banner2, banner4;
	private ImageLoader bannerimageLoader;
	//����ؼ�
	private ImageView img_share;
	/**ͼƬ�ֲ�*/
	private void initBanner(){
		imageCycleView=(ImageCycleView)headView.findViewById(R.id.cycleView);
		/**װ�����ݵļ���  ��������*/
		ArrayList<String> imageDescList=new ArrayList<String>();
		/**װ�����ݵļ���  ͼƬ��ַ*/
		ArrayList<String> urlList=new ArrayList<String>();
		for(int i=0;i< 5;i++){
			/**�������*/
			urlList.add(Contants.IMAGES[i]);
		}
		imageDescList.add("С������");
		imageDescList.add("��������Ը���Ů");
		imageDescList.add("��Ѫ���� �Ȼ���");
		imageDescList.add(" ̨����Ů");
		imageDescList.add("�������");
		initCarsuelView(imageDescList, urlList);

		//����������
		img_share=(ImageView)headView.findViewById(R.id.img_share);
		img_share.setOnClickListener(new OnClickListener(){
			@SuppressLint("SdCardPath")
			@Override
			public void onClick(View arg0) {
				//����
				Bitmap screenbitmap=MyBitmapUtils.takeScreenShot(OrdersActivity.this);
				if(screenbitmap!=null){
					//�洢
					File yckx_tempFile=new File("/sdcard/yckx/");
					String fileName = "/sdcard/yckx/yckx_screen_temp.png";
					FileOutputStream gaodeFos = null;  
					if(!yckx_tempFile.exists()){
						yckx_tempFile.mkdirs();// �����ļ���  
					}
					try {   
						gaodeFos = new FileOutputStream(fileName); //д���洢�� 
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
				String shareText=busineess.getAddress();//�����̼���Ϣ
				String commentText="";//QQ�ռ������Ĭ�ϵ�����
				String shareUrl=WEBInterface.YCKX_APK_DOWNLOAD_URL;//�ų���ϴ���ص�����
				showShare(shareTittle,shareText,commentText,shareUrl);
			}});
		//��ת��ͼƬ���б�
		ImageView img_showAll=(ImageView)headView.findViewById(R.id.img_showAll);
		img_showAll.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrdersActivity.this,PersonalLoadImgActivity.class);
				startActivity(intent);
			}});
	}

	private View headView;
	/**��һ��ҳ��*/
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
		//��˾�����Ǽ�
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
		//��������Ǽ�������һ������ҳ��
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
		//����
		TextView distance=(TextView)headView.findViewById(R.id.distance);
		String mDistance=NumberUtils.double2poins(busineess.getDistance())+"";
		distance.setText(mDistance);
		imgPhone=(ImageView)headView.findViewById(R.id.imgPhone);
		imgPhone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//����绰
				String phoneNo=busineess.getCompanyPhone();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				OrdersActivity.this.startActivity(intent);
			}});
		
		//�ֳ���,�ж��̼��Ƿ�֧���ֳ�����֧������ʾ�ֳ���Item
		RelativeLayout layout_payOnSpot=(RelativeLayout) headView.findViewById(R.id.layout_payOnSpot);
		TextView tv_payOnSpot=(TextView) headView.findViewById(R.id.tv_payOnSpot);
		tv_payOnSpot.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrdersActivity.this,PayOnSpotActivity.class);
				intent.putExtra("BusinessEntity", busineess);
				startActivity(intent);
			}});
		
		//ϴ����Ʒ�б�
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

		//getCompanyImg2(WEBInterface.INDEXIMGURL+busineess.getCompanyMapImage(),SETIMG);//��ʱ����
		companeyName.setText(""+busineess.getCompanyName());
		//��ʱ����
		//getCompanyImg2(WEBInterface.INDEXIMGURL+busineess.getCompanyImage(),SETIMG2);
		companyAdress.setText(""+busineess.getAddress());

		//��ʾ�ų���ϴϴ����Ʒ
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
	//layout_listLoadDown�Ƿ���ʾҪ���̼Ҳ�Ʒ�б�listviewitem�ĸ�������������10����ʾ
	//tv_listItemtCount��ֵҪ�����̼Ҳ�Ʒ�б�listviewitem�ĸ�������ֵtv_listItemtCount=listviewItem��-10
	private View fooderView;
	/**�̼Ҳ�Ʒlistview�ײ�view*/
	private void initFooderView(){
		
		fooderView1=getLayoutInflater().inflate(R.layout.row_business_home_companyproductlist_fooder1,null);
		TextView tv_listItemtCount=(TextView) fooderView1.findViewById(R.id.tv_listItemtCount);
		
		RelativeLayout layout_listLoadDown=(RelativeLayout) fooderView1.findViewById(R.id.layout_listLoadDown);
		layout_listLoadDown.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ToastUtil.show(getApplicationContext(), "���ظ���",2000);
			}});
		
		fooderView=getLayoutInflater().inflate(R.layout.row_business_home_companyproductlist_fooder,null);
		
		//��˾��ϵ�绰
		fooderView.findViewById(R.id.layout_kefu).setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						//����绰
						String phoneNo=busineess.getCompanyPhone();
						Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
						OrdersActivity.this.startActivity(intent);
					}});
		
		//��ϵ�ͷ�
		fooderView.findViewById(R.id.layout_customer).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//����绰
				String phoneNo=busineess.getManager().getUserPhone();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				OrdersActivity.this.startActivity(intent);
			}});
		//���ÿͷ���Ϣ
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
		//�̼�����
		fooderView.findViewById(R.id.tv_shangjiahangqing).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrdersActivity.this,CompanyIntroductionActivity.class);
				startActivity(intent);
			}});
		//�̼���פ
		fooderView.findViewById(R.id.layout_shangjiaruzhu).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(OrdersActivity.this,RegisterCompanyActivity.class);
				startActivity(intent);
			}});
	}


	/**��ʼ���ֲ�ͼ*/
	public void initCarsuelView(ArrayList<String> imageDescList,ArrayList<String>urlList) {
		LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ActionUtils.getScreenHeight(OrdersActivity.this) * 3 / 10);
		imageCycleView.setLayoutParams(cParams);
		ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
			@Override
			public void onImageClick(int position, View imageView) {
				/**ʵ�ֵ���¼�*/
				Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();;
			}
			@Override
			public void displayImage(String imageURL, ImageView imageView) {
				/**�ڴ˷����У���ʾͼƬ���������Լ���ͼƬ���ؿ⣬Ҳ�����ñ�demo�еģ�Imageloader��*/
				ImageLoaderHelper.getInstance().loadImage(imageURL, imageView);
			}
		};
		/**��������*/
		imageCycleView.setImageResources(imageDescList, urlList, mAdCycleViewListener);
		imageCycleView.startImageCycle();
	}


	private void showShare(String shareTittle,String shareText,String commentText,String tagetUrl) {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//�ر�sso��Ȩ
		oks.disableSSOWhenAuthorize(); 

		// ����ʱNotification��ͼ�������  2.5.9�Ժ�İ汾�����ô˷���
		//oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(shareTittle);
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl(tagetUrl);
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText(shareText);
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		oks.setImagePath("/sdcard/yckx/yckx_screen_temp.png");//ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl(tagetUrl);
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment(commentText);
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(shareTittle);
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl(tagetUrl);

		// ��������GUI
		oks.show(this);
	}




	/**������*/
	private Dialog shareDialog;
	private void showShareDialog(){
		LayoutInflater inflater=getLayoutInflater();
		View shareView = inflater.inflate(R.layout.row_popu_sharelayout, null);
		shareDialog = new AlertDialog.Builder(OrdersActivity.this).create();
		shareDialog.show();
		shareDialog.setContentView(shareView);
		WindowManager m =getWindowManager();
		Window dialogWindow = shareDialog.getWindow();
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ����
		WindowManager.LayoutParams params = shareDialog.getWindow().getAttributes();
		params.width = (int)d.getWidth();
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//����bottom��ƫ����
		params.y=30;
		shareDialog.getWindow().setAttributes(params);

		shareView.findViewById(R.id.wx).setOnClickListener(listener);
		shareView.findViewById(R.id.pyq).setOnClickListener(listener);
		shareView.findViewById(R.id.wb).setOnClickListener(listener);
		shareView.findViewById(R.id.qq).setOnClickListener(listener);
		shareView.findViewById(R.id.qzone).setOnClickListener(listener);
		shareView.findViewById(R.id.share_cancel).setOnClickListener(listener);
	}

	/**����Ӻ�ʱ�����Ĵ���*/
	private PopupWindow addPopuWindow;
	private View popupView;// popup����
	/** ��ʼ��PopupMenu */
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
		// ����Ϊtouch������ر�(һ��Ҫ�����ú����ı���)
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
				//��ʾ֮ǰ�ļӺ�
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

	/**���΢�ŷ���ӿ�*/
	private void shareWX(){
		WXTextObject textObj=new WXTextObject();
	}



	/***/



	/*private Bitmap companyBitmp_big;
	private Bitmap companyBitmp_small;
	 *//**��ȡ��ǰ��˾����ͼƬ*//*
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

	/**��ȡ��ǰ��˾����ͼƬ*//*
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


	/**����ͼƬ��ͼ*//*
	private final int SETIMG=0;
	 *//**����СͼƬ*//*
	private final int SETIMG2=1;

	@SuppressLint("HandlerLeak") 
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==LOADHISTORY_OVER){
				//���ؽ���
				pingjia_listview.loadComplete();
				ToastUtil.show(getApplicationContext(), "��ȫ��������", 2000);
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

	/**�̼ұ����Ʒ������,������ȥ�޸�Eʵ��*/
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
			//����ͼƬ
			imageLoader.displayImage(getItem(arg0), viewHolder.img_product);
			//���ò�Ʒ��
			viewHolder.tv_productName.setText("��׼��");
			//���ò�Ʒ����
			//viewHolder.tv_productIntroduce.setText("");
			//�����Ʒ����
			viewHolder.layout_produceDetail.setTag(arg0);
			viewHolder.layout_produceDetail.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Integer position=Integer.parseInt(viewHolder.layout_produceDetail.getTag()+"");
					ToastUtil.show(getApplicationContext(), ""+position, 3000);
				}});
			//һ���µ�
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
	/**ϴ����Ʒ������*/
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
			//��ȡ����ͼƬ
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


	//--------�ڶ���ҳ�棬�̼�����ҳ��-----------


	//���۽���----

	/*private void setupHistoryComment(){
		pingjia_listview=(LoadListView)findViewById(R.id.pingjia_listview);
		pingjia_listview.setInterface(this);
		adapter=new HistoricalEvaluationAdapter(OrdersActivity.this);
		pingjia_listview.setAdapter(adapter);
		//setListViewHeight(pingjia_listview);
	}*/
	/**  
	 * ���¼���ListView�ĸ߶ȣ����ScrollView��ListView����View���й�����Ч������Ƕ��ʹ��ʱ���ͻ������  
	 * @param listView  
	 */  
	/*public void setListViewHeight(ListView listView) {    

		// ��ȡListView��Ӧ��Adapter    

		ListAdapter listAdapter = listView.getAdapter();    

		if (listAdapter == null) {    
			return;    
		}    
		int totalHeight = 0;    
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()�������������Ŀ    
			View listItem = listAdapter.getView(i, null, listView);    
			listItem.measure(0, 0); // ��������View �Ŀ��    
			totalHeight += listItem.getMeasuredHeight(); // ͳ������������ܸ߶�    
		}    

		ViewGroup.LayoutParams params = listView.getLayoutParams();    
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));    
		listView.setLayoutParams(params);    
	}    




	private int page=0;
	private int size=20;
	 *//**��ȡ�����б�����*//*
	private void getHistoryEvolution(final String companyId){
		new Thread(new Runnable() {//����id:id=12
			@Override
			public void run() {///comment/list/{page}/{size}/{targetOwnerId}
				String url=WEBInterface.COMPANYHISTORY_COMMENT_URL+page+"/"+size+"/"+companyId;
				String token=PrefrenceConfig.getUserMessage(OrdersActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpGet(url, token, null);
				Log.d("yckx", ""+respose);
				list=ParseJSONUtils.parseHistoryCommet(respose);
				if(list!=null){
					//��ȡ���ݳɹ�
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


	/**���ظ���*//*
	@Override
	public void onLoad() {
		page++;
		getHistoryEvolution(busineess.getCompanyId());

	};*/




	/**�����е�dialog*//*
	private Dialog loadingDialog;
	//----����popu���ڵ�dialog����----
	private void showLoadingDialog(){
		LayoutInflater inflater=getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_loading, null);
		loadingDialog = new AlertDialog.Builder(OrdersActivity.this).create();
		loadingDialog.show();
		loadingDialog.setCancelable(false);
		loadingDialog.setContentView(view);
		WindowManager m =getWindowManager();
		Window dialogWindow = loadingDialog.getWindow();
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ����
		WindowManager.LayoutParams params = loadingDialog.getWindow().getAttributes();
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		dialogWindow.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
		//����bottom��ƫ����
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
