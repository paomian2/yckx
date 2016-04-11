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
	/**�ж�ϴ��ʦ������״̬(�ӵ��������顢��ʼϴ����ϴ������)*/
	private boolean washerReviceOrder,washerArrive,washerCheck,waherWashing,wahserEnd;
	/**���ض����������*/
	private final int REFRESH_SUCCESS=0;
	/**����Activity��ˢ�¶���*/


	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		//��ȡ��������
		entity=GlobalServiceUtils.getInstance().getGloubalServiceOrderDetailEntity();
		orderId=entity.getId();
		FormDetailActivity activity=(FormDetailActivity) getActivity();
		activity.setRefreseOnclickListner(new RefreshOnclickListner(){
			@Override
			public void onRefreshclick(String s) {
				//����ˢ�¶�������
				ToastUtil.show(getApplicationContext(),"�����ˢ��", 2000);
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
		case 0://��������,����һ����̬����
			setContentView(R.layout.fragment_more_formdetail);
			setData();
			break;
		case 1://һ��h5��ҳ
			setWebView();
			break;
		case 2://����ʦ
			setContentView(R.layout.fragment_morefragment_formdetail_meihushi);
			selectTab3();
			break;
		}
	}

	private void setData(){
		TextView formNo=(TextView) findViewById(R.id.formNo);//�������
		TextView formStatus=(TextView) findViewById(R.id.formStatus);//֧��״̬
		TextView formCreateTime=(TextView) findViewById(R.id.formCreateTime);//��������ʱ��
		TextView formReceiveTime=(TextView) findViewById(R.id.formReceiveTime);//�������,�ӵ�ʱ��
		TextView formCompleteTime=(TextView) findViewById(R.id.formCompleteTime);//���ʱ��
		TextView formPay=(TextView) findViewById(R.id.formPay);//����֧�����
		TextView formPayType=(TextView) findViewById(R.id.formPayType);//����֧����ʽ
		TextView formStore=(TextView) findViewById(R.id.formStore);//�����̼�����
		TextView formProject=(TextView) findViewById(R.id.formProject);//������Ŀ����
		TextView formCard=(TextView) findViewById(R.id.formCard);//��������ϵ��
		TextView formStyle=(TextView) findViewById(R.id.formStyle);//��������ʽ
		formNo.setText(""+entity.getOrderNum());
		//����״̬
		String mOrderStatus=entity.getOrderStatus();
		if(mOrderStatus.equals("create")){
			mOrderStatus="��֧��";
		}
		if(mOrderStatus.equals("robbed")){
			mOrderStatus="�ѽӵ�";
		}
		if(mOrderStatus.equals("payed")){
			mOrderStatus="���ӵ�";
		}	
		if(mOrderStatus.equals("cancle")){
			mOrderStatus="ȡ��";
		}
		if(mOrderStatus.equals("invalid")){
			mOrderStatus="����";
		}
		if(mOrderStatus.equals("finish")){
			mOrderStatus="������";
		}
		if(mOrderStatus.equals("comment")){
			mOrderStatus="������";
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
		//֧����ʽ
		String mPayType=entity.getPayType();
		if(mPayType.equals("ali")){
			mPayType="֧����";
		}
		if(mPayType.equals("union")){
			mPayType="����";
		}
		if(mPayType.equals("wx")){
			mPayType="΢��";
		}
		formPayType.setText(""+mPayType);
		formStore.setText(""+entity.getCompanyEntity().getCompanyName());
		formProject.setText(""+entity.getProductEntity().getProductName());
		CardEntity cardEntity=entity.getCarEntity();
		formCard.setText(""+cardEntity.getCarBrand()+"|"+cardEntity.getProvince()+cardEntity.getCarNum()+"|"+cardEntity.getCarColor());
		if(entity.getDoorService().equals("false")){
			formStyle.setText("��������");
		}else{
			formStyle.setText("���ŷ���");
		}
	}





	/**�ڶ���ҳ����һ����ҳ��������ҳ����*/
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

		//��ʼ����
		public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			System.out.println("----->>>��ʼ����");
		};

		//���ؽ���
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			System.out.println("-------->>���ؽ���");
		};

		//���س���
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			System.out.println("------->>���س���");
		};

	};


	//����״̬    create��֧��   robbed������  payed���ӵ�   cancleȡ��  invalid����    finish������    comment������ 

	//ϴ��ʦ״̬  arrived����ָ��λ��  checked��鳵��  washing������ϴ   washed��ϴ���

	/**ѡ������ʦҳ��*/
	@SuppressLint("ResourceAsColor")
	private void selectTab3(){
		
		Log.d("yckx", entity.getId()+"");

		TextView createTime=(TextView) findViewById(R.id.createTime);//��������ʱ��

		LinearLayout layout_unpay=(LinearLayout) findViewById(R.id.layout_unpay);//δ֧��
		TextView btn_payOrder=(TextView) findViewById(R.id.btn_payOrder);//����֧��(���)
		btn_payOrder.setOnClickListener(timeProgressClickListener);

		LinearLayout layout_pay=(LinearLayout) findViewById(R.id.layout_pay);//�Ѿ�֧��
		TextView payTime=(TextView) findViewById(R.id.payTime);//֧��ʱ��

		LinearLayout layout_receive=(LinearLayout) findViewById(R.id.layout_washer_Receive);//ϴ��ʦ�ӵ�
		TextView washerDoTime=(TextView) findViewById(R.id.washerDoTime);//ϴ��ʦ�ӵ�ʱ��
		CircleImageView washerImg=(CircleImageView) findViewById(R.id.washerImg);//ϴ��ʦͷ��
		TextView wahserName=(TextView) findViewById(R.id.wahserName);//ϴ��ʦ��
		ImageView wahser_img1=(ImageView) findViewById(R.id.wahser_img1);//ϴ��ʦ��õ�����(����)����userTotalScore/userCommentCount���
		ImageView wahser_img2=(ImageView) findViewById(R.id.wahser_img2);
		ImageView wahser_img3=(ImageView) findViewById(R.id.wahser_img3);
		ImageView wahser_img4=(ImageView) findViewById(R.id.wahser_img4);
		ImageView wahser_img5=(ImageView) findViewById(R.id.wahser_img5);
		TextView phoneCall=(TextView) findViewById(R.id.phoneCall);//�������ϴ��ʦ�绰
		TextView yuyin=(TextView) findViewById(R.id.yuyin);//���������������
		TextView orderCancel=(TextView) findViewById(R.id.orderCancel);//�������ȡ������
		phoneCall.setOnClickListener(timeProgressClickListener);
		yuyin.setOnClickListener(timeProgressClickListener);
		orderCancel.setOnClickListener(timeProgressClickListener);

		LinearLayout layout_washerArrive=(LinearLayout) findViewById(R.id.layout_washerArrive);//ϴ��ʦ����ָ��λ��
		TextView washerArriveImg_arrive=(TextView) findViewById(R.id.wahserArriveTime);//ϴ��ʦ����ָ��λ��ʱ��

		LinearLayout layout_washerCheck=(LinearLayout) findViewById(R.id.layout_washerCheck);//��鳵��
		TextView washerCheckCarTime=(TextView) findViewById(R.id.washerCheckCarTime);//��鳵��ʱ��
		CacheImageView img1=(CacheImageView) findViewById(R.id.img1);//����ʦ�ϴ���ͼƬ
		CacheImageView img2=(CacheImageView) findViewById(R.id.img2);//����ʦ�ϴ���ͼƬ
		CacheImageView img3=(CacheImageView) findViewById(R.id.img3);//����ʦ�ϴ���ͼƬ

		LinearLayout layout_washerWashering=(LinearLayout) findViewById(R.id.layout_washerWashering);//��ʼϴ��
		TextView washeringTime=(TextView) findViewById(R.id.washeringTime);//��ʼϴ��ʱ��

		LinearLayout layout_washerEnd=(LinearLayout) findViewById(R.id.layout_washerEnd);//ϴ������
		TextView washerEndTime=(TextView) findViewById(R.id.washerEndTime);//ϴ������ʱ��
		TextView btn_pingjia=(TextView) findViewById(R.id.btn_pingjia);//��������(��������ʱ���޷�����,�Ѿ����۹��Ĳ����ٽ�������)
		if(entity.getCommentEntity()!=null){
			findViewById(R.id.layout_btn_evaluation).setBackgroundColor(R.color.click_enabal);
			btn_pingjia.setClickable(false);
		}
		btn_pingjia.setOnClickListener(timeProgressClickListener);
		
		LinearLayout layout_pingjia=(LinearLayout) findViewById(R.id.layout_pingjia);//�û��Ѿ�������
		TextView pingjiaTime=(TextView) findViewById(R.id.pingjiaTime);//����ʱ��
		TextView pingjiaContext=(TextView) findViewById(R.id.pingjiaContent);//��������
		ImageView pingajiaImg1=(ImageView) findViewById(R.id.pingjiaImg1);//���۵�����
		ImageView pingajiaImg2=(ImageView) findViewById(R.id.pingjiaImg2);//���۵�����
		ImageView pingajiaImg3=(ImageView) findViewById(R.id.pingjiaImg3);//���۵�����
		ImageView pingajiaImg4=(ImageView) findViewById(R.id.pingjiaImg4);//���۵�����
		ImageView pingajiaImg5=(ImageView) findViewById(R.id.pingjiaImg5);//���۵�����
		CacheImageView pinglunImg1=(CacheImageView) findViewById(R.id.pinglunImg1);//�����ύ��ͼƬ
		CacheImageView pinglunImg2=(CacheImageView) findViewById(R.id.pinglunImg2);//�����ύ��ͼƬ
		CacheImageView pinglunImg3=(CacheImageView) findViewById(R.id.pinglunImg3);//�����ύ��ͼƬ
		
		LinearLayout layout_next=(LinearLayout) findViewById(R.id.layout_next);//������һ��
		TextView tv_netDo=(TextView) findViewById(R.id.tv_netDo);//��һ��Ҫ������

		LinearLayout layout_orderEnd=(LinearLayout) findViewById(R.id.layout_orderEnd);//�������

		//�߼�����
		if(entity.getOrderSubEntity()!=null&&entity.getOrderSubEntity().getSubStatus().equals("create")){//��֧��
			//��ʾ���ɶ�����ʱ��
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
		if(entity.getOrderStatus().equals("payed")){//���ӵ�
			//��ʾ���ɶ�����ʱ��
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//֧��ʱ��
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

			tv_netDo.setText("�ȴ�����ʦ�ӵ�");
		}
		if(entity.getOrderStatus().equals("robbed")){//����ʦ�ѽӵ�,δ����
			//��ʾ���ɶ�����ʱ��
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//֧��ʱ��
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//����ʦ����
			layout_receive.setVisibility(View.VISIBLE);
			//����ʦ�ӵ�ʱ��
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//��������ʦ��ͷ��
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//����ʦ����
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//����ʦ���������ݷ�������"����"
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
			tv_netDo.setText("����ʦ���ڸ���ָ��λ��");
		}
		if(entity.getOrderSubEntity()!=null&&entity.getOrderSubEntity().getSubStatus().equals("arrived")){//����ʦ����ָ��λ�ã���û��ʼ��鳵��
			//��ʾ���ɶ�����ʱ��
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//֧��ʱ��
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//����ʦ����
			layout_receive.setVisibility(View.VISIBLE);
			//����ʦ�ӵ�ʱ��
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//��������ʦ��ͷ��
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//����ʦ����
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//����ʦ���������ݷ�������"����"
			String score=NumberUtils.InterDivisionInter(entity.getWasherEntity().getUserTotalScore(), entity.getWasherEntity().getUserCommentCount());
			int mScore=Integer.parseInt(score);
			ImageView[] imageview={wahser_img1,wahser_img2,wahser_img3,wahser_img4,wahser_img5};
			setStarImageView(mScore,imageview);
			//����ʦ����ָ��λ�õ�ʱ��
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
			tv_netDo.setText("����ʦ׼����鳵��");
		}
		if(entity.getOrderSubEntity()!=null&&entity.getOrderSubEntity().getSubStatus().equals("checked")){//����ʦ��鳵������û�п�ʼ��ϴ
			//��ʾ���ɶ�����ʱ��
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//֧��ʱ��
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//����ʦ����
			layout_receive.setVisibility(View.VISIBLE);
			//����ʦ�ӵ�ʱ��
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//��������ʦ��ͷ��
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//����ʦ����
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//����ʦ���������ݷ�������"����"
			String score=NumberUtils.InterDivisionInter(entity.getWasherEntity().getUserTotalScore(), entity.getWasherEntity().getUserCommentCount());
			int mScore=Integer.parseInt(score);
			ImageView[] imageview={wahser_img1,wahser_img2,wahser_img3,wahser_img4,wahser_img5};
			setStarImageView(mScore,imageview);
			//����ʦ����ָ��λ�õ�ʱ��
			washerArriveImg_arrive.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getArrivedTime()));
			//��鳵��ʱ��
			washerCheckCarTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getCheckedTime()));
			//����ʦ�ϴ��ĳ���ͼƬ
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
			tv_netDo.setText("����ʦ������ϴ");
		}
		if(entity.getOrderSubEntity()!=null&&entity.getOrderSubEntity().getSubStatus().equals("washing")){//��ʼ��ϴ����û����ϴ��
			//��ʾ���ɶ�����ʱ��
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//֧��ʱ��
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//����ʦ����
			layout_receive.setVisibility(View.VISIBLE);
			//����ʦ�ӵ�ʱ��
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//��������ʦ��ͷ��
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//����ʦ����
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//����ʦ���������ݷ�������"����"
			String score=NumberUtils.InterDivisionInter(entity.getWasherEntity().getUserTotalScore(), entity.getWasherEntity().getUserCommentCount());
			int mScore=Integer.parseInt(score);
			ImageView[] imageview={wahser_img1,wahser_img2,wahser_img3,wahser_img4,wahser_img5};
			setStarImageView(mScore,imageview);
			//����ʦ����ָ��λ�õ�ʱ��
			washerArriveImg_arrive.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getArrivedTime()));
			//��鳵��ʱ��
			washerCheckCarTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getCheckedTime()));
			//����ʦ�ϴ��ĳ���ͼƬ
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
			//����ʦ��ʼ��ϴʱ��
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
			tv_netDo.setText("����ʦ�Ϳ���ϴ�����");
		}
		if(entity.getOrderSubEntity()!=null&&entity.getOrderSubEntity().getSubStatus().equals("washed")&&entity.getOrderStatus().equals("finish")){//��ϴ��ɣ���û������
			//��ʾ���ɶ�����ʱ��
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//֧��ʱ��
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//����ʦ����
			layout_receive.setVisibility(View.VISIBLE);
			//����ʦ�ӵ�ʱ��
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//��������ʦ��ͷ��
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//����ʦ����
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//����ʦ���������ݷ�������"����"
			String score=NumberUtils.InterDivisionInter(entity.getWasherEntity().getUserTotalScore(), entity.getWasherEntity().getUserCommentCount());
			int mScore=Integer.parseInt(score);
			ImageView[] imageview={wahser_img1,wahser_img2,wahser_img3,wahser_img4,wahser_img5};
			setStarImageView(mScore,imageview);
			//��������ʦ�绰
			phoneCall.setOnClickListener(timeProgressClickListener);
			//������ʦ��������
			yuyin.setOnClickListener(timeProgressClickListener);
			//ȡ������
			orderCancel.setOnClickListener(timeProgressClickListener);
			//����ʦ����ָ��λ�õ�ʱ��
			washerArriveImg_arrive.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getArrivedTime()));
			//��鳵��ʱ��
			washerCheckCarTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getCheckedTime()));
			//����ʦ�ϴ��ĳ���ͼƬ
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
			//����ʦ��ʼ��ϴʱ��
			washeringTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getWashTime()));
			//����ʦ��ϴ���ʱ��
			washerEndTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getWashedTime()));
			//��������ʦ
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
			tv_netDo.setText("����ʦ�Ϳ���ϴ�����,�������۰�");
		}
		if(entity.getOrderStatus().equals("comment")){//�û�������
			createTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderCreateTime()));
			//֧��ʱ��
			payTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderPayedTime()));
			//����ʦ����
			layout_receive.setVisibility(View.VISIBLE);
			//����ʦ�ӵ�ʱ��
			washerDoTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderRobbedTime()));
			//��������ʦ��ͷ��
			setWasherImg(washerImg,entity.getWasherEntity().getUserImageUrl());
			//����ʦ����
			wahserName.setText(""+entity.getWasherEntity().getWasherName());
			//����ʦ���������ݷ�������"����"
			String score=NumberUtils.InterDivisionInter(entity.getWasherEntity().getUserTotalScore(), entity.getWasherEntity().getUserCommentCount());
			int mScore=Integer.parseInt(score);
			ImageView[] imageview={wahser_img1,wahser_img2,wahser_img3,wahser_img4,wahser_img5};
			setStarImageView(mScore,imageview);
			//��������ʦ�绰
			phoneCall.setOnClickListener(timeProgressClickListener);
			//������ʦ��������
			yuyin.setOnClickListener(timeProgressClickListener);
			//ȡ������
			orderCancel.setOnClickListener(timeProgressClickListener);
			//����ʦ����ָ��λ�õ�ʱ��
			washerArriveImg_arrive.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getArrivedTime()));
			//��鳵��ʱ��
			washerCheckCarTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getCheckedTime()));
			//����ʦ�ϴ��ĳ���ͼƬ
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
			//����ʦ��ʼ��ϴʱ��
			washeringTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getWashTime()));
			//����ʦ��ϴ���ʱ��
			washerEndTime.setText(""+TimeUtils.millsToDateTime(entity.getOrderSubEntity().getArrivedTime()));
			//��������ʦ
			
			//����ʱ��pingjiaTime
			pingjiaTime.setText(""+TimeUtils.millsToDateTime(entity.getCommentEntity().getCommentCreateTime()));
			//��������
			pingjiaContext.setText(""+entity.getCommentEntity().getCommentContent());
			//���۷���
			String pinglunScore=entity.getCommentEntity().getCommentScore();
			Integer mpinglunScore=Integer.parseInt(pinglunScore);
			ImageView[] pinglunImageView={pingajiaImg1,pingajiaImg2,pingajiaImg3,pingajiaImg4,pingajiaImg5};
			setStarImageView(mpinglunScore,pinglunImageView);
			//�����ύ��ͼƬ
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


	/**��������ʦͷ��*/
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

	/**�����������*/
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


	/**ʱ�������¼�*/
	private View.OnClickListener timeProgressClickListener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.phoneCall://��������ʦ�绰
				String phoneNo=entity.getWasherEntity().getUserPhone();
				Intent intentPhone = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo));
				getActivity().startActivity(intentPhone);
				break;
			case R.id.btn_payOrder://����֧��
				String orderId=entity.getId();
				Intent intent_pay=new Intent(getActivity(),PayMoneyActivity.class);
				startActivity(intent_pay);
				getActivity().finish();
				break;
			case R.id.yuyin://������ʦ��������
				//�洢��ǰ��ϵ��
				currentChatObj=entity;
				saveRongIMUser2DB(entity);
				RongIM.getInstance().startPrivateChat(getActivity(), entity.getWasherEntity().getWaherId(), "title");
				break;
			case R.id.btn_pingjia://��������ʦ
				//�ж�����ʱ���Ƿ��Ѿ�����
				//��ǰ��ʱ���
				Date todateDate=new Date();
				long todateTime = todateDate.getTime();
				//�������ʱ��
				String completeTime=entity.getOrderFiniahTime();
				long mPromissTime=Long.parseLong(completeTime);
				long betweenDays = (long)((todateTime-mPromissTime) / (1000 * 60 * 60 *24) + 0.5);
				if(betweenDays>3){
					ToastUtil.show(getApplicationContext(), "�ѹ��ڣ����ܽ�������", 2000);
					return;
				}
				Bitmap bitmap=MyBitmapUtils.takeScreenShot(getActivity());
				MyBitmapUtils.savePic(bitmap, "/sdcard/yckx/yckx_temp.png");
				Log.d("yckx","--�����۵Ķ���"+entity.toString());
				Intent intent=new Intent(getActivity(),CommentActivity.class);
				//intent.putExtra("CommentActivity", entity);
				intent.putExtra("CommentActivity", entity.getId());
				startActivity(intent);
				break;
			case R.id.orderCancel://ȡ������
				ownCancelOrder(entity.getId());
				break;
			}

		}
	};

	/**ˢ�����ݵ�ʱ����ݶ����Ž����ٴβ�ѯ*/
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
					Log.d("yckx", "���������������"+respose);
					//��������
				}
			}
		}).start();
	}


	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_SUCCESS://
				entity=(OrderDetailEntity) msg.obj;
				//������������
				GlobalServiceUtils.getInstance().setGloubalServiceOrderDetailEntity(entity);
				try {
					setData();
				} catch (Exception e) {
					Log.d("yckx", "ˢ�º����setData()�쳣");
				}
				try {
					selectTab3();
				} catch (Exception e) {
					Log.d("yckx", "ˢ�º����selectTab3()�쳣");
				}
				break;

			case CANCLEORDER_SUCCESS://ȡ�������ɹ�,ȡ���ɹ�������ˢ������
				ToastUtil.show(getApplicationContext(), "ȡ�������ɹ�", 2000);
				break;
			}
		};
	};
	
	private final int CANCLEORDER_SUCCESS=10;
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
	
	
	private OrderDetailEntity currentChatObj;
	
	/**���漴ʹ����Ķ���*/
	private void saveRongIMUser2DB(OrderDetailEntity orderlistEntity){
		//����Ƿ�������ݿ�
		if(!DBRead.isExistsCardbFile()){//�����������
			try {
				// ��������Ŀ�е�Assets/db/commonnum.db�ļ�����д���� DBRead.telFile�ļ���
				AssetsDBManager.copyAssetsFileToFile(getApplicationContext(), "db/carsplist.db", DBRead.carFile);
			} catch (IOException e) {
				ToastUtil.show(getActivity(), "��ʼ��������Ϣ���ݿ��쳣", 2000);
			}
		}
		//��ӵ����ݿ�
		DBRead.addRongIMUser(orderlistEntity, true);
	}

	/**������������û���Ϣ*/
	@Override
	public UserInfo getUserInfo(String arg0) {
		if(currentChatObj!=null){
			Log.d("yckx", "���Ʒ���getUserInfo()");
			return new UserInfo(currentChatObj.getOwnerEntity().getId(), currentChatObj.getOwnerEntity().getUserPhone(), Uri.parse(WEBInterface.INDEXIMGURL+currentChatObj.getOwnerEntity().getUserImageUrl()));
		}
		Log.d("yckx", "���Ʒ���getUserInfo()ʧ��:"+arg0);
		return null;
	}
}
