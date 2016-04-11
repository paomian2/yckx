package com.brother.yckx.control.activity.owner;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import net.sourceforge.simcpux.PayActivity;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.alipay.PayResult;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.model.OrderEntity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

public class PayMoneyActivity extends BaseActivity{

	private RelativeLayout layout_wowotou,layout_wechat,layout_zhifubao,layout_union;
	private ImageView payWayImg_wowotou,payWayImg_wechat,payWayImg_zhifubao,payWayImg_union;
	//private OrderEntity orderEntity;
	private String payOrderId;
	private String aliPrePayOrderInfos="";//����֧����֧��ʹ�õ�orderInfo
	private String sign="";//����֧����֧����sign
	private final int ALIPREPAY_SUCCESS=0;
	/**����֧����sdk*/
	private static final int SDK_PAY_FLAG = 1;
	/**����Ԥ֧�ֳɹ�����ʼ֧��*/
	private static final int SDK_UPPAY_FLAG=10;
	/**��ȡ΢��Ԥ֧���ɹ�����ʼ����΢��֧��*/
	private static final int SDK_WXPAY_FLAG=21;
	private IWXAPI api;
	private HashMap<String,String> testHashMap=new HashMap<String, String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pay_money);
		setActionBar(R.string.selectFormPayStyle, R.drawable.btn_homeasup_default, NULL_ID, listener);
		
		api = WXAPIFactory.createWXAPI(this, "wx2d43b874490dc17d");
		//orderEntity=(OrderEntity) getIntent().getSerializableExtra("PayMoneyActivity");
		payOrderId=getIntent().getStringExtra("PayMoneyActivity");
		//΢�Ų���
		findViewById(R.id.wx_test).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				testHashMap.put("orderId", payOrderId);
				GlobalServiceUtils.setGloubalServiceSession("testWX", testHashMap);
				Intent intent=new Intent(PayMoneyActivity.this,PayActivity.class);
				startActivity(intent);
			}});
		init();
	}

	private void init(){
		layout_wowotou=(RelativeLayout) findViewById(R.id.layout_wowotou);
		layout_wechat=(RelativeLayout) findViewById(R.id.layout_wechat);
		layout_zhifubao=(RelativeLayout) findViewById(R.id.layout_zhifubao);
		layout_union=(RelativeLayout) findViewById(R.id.layout_union);

		payWayImg_wowotou=(ImageView) findViewById(R.id.payWayImg_wowotou);
		payWayImg_wechat=(ImageView) findViewById(R.id.payWayImg_wechat);
		payWayImg_zhifubao=(ImageView) findViewById(R.id.payWayImg_zhifubao);
		payWayImg_union=(ImageView) findViewById(R.id.payWayImg_union);

		layout_wowotou.setOnClickListener(listener);
		layout_wechat.setOnClickListener(listener);
		layout_zhifubao.setOnClickListener(listener);
		layout_union.setOnClickListener(listener);


		/**���Կؼ�*/

	}


	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.layout_wowotou:
				resetIcon();
				setPayIcon(R.id.payWayImg_wowotou);
				break;
			case R.id.layout_wechat:
				resetIcon();
				setPayIcon(R.id.payWayImg_wechat);
				wxPayInbackground();
				break;
			case R.id.layout_zhifubao:
				resetIcon();
				setPayIcon(R.id.payWayImg_zhifubao);
				aliPrePayInbackground();
				break;
			case R.id.layout_union:
				resetIcon();
				setPayIcon(R.id.payWayImg_union);
				uPPayInbackground();
				break;
			case R.id.iv_action_left:
				finish();
				break;
			}
		}
	};

	private void resetIcon(){
		payWayImg_wowotou.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_payway_normal));
		payWayImg_wechat.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_payway_normal));
		payWayImg_zhifubao.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_payway_normal));
		payWayImg_union.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_payway_normal));
	}

	private void setPayIcon(int viewId){
		findViewById(viewId).setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_payway_select));
	}


	/**֧����Ԥ֧�����ö���id��������,��ȡ����֧������Ҫ������(��ȡ����signֵһ��Ҫת��Ϊurl�����ʽ)*/
	private void aliPrePayInbackground(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String orderId=payOrderId;
				String url=WEBInterface.ALIPREPAY_URL+orderId;
				String token=PrefrenceConfig.getUserMessage(PayMoneyActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpPost(url, token, null);
				try {
					JSONObject jSOSNObject=new JSONObject(respose);
					String code=jSOSNObject.getString("code");
					if(code.equals("0")){
						aliPrePayOrderInfos=jSOSNObject.getString("aliPrePayOrderInfos");
						sign=jSOSNObject.getString("sign");
						try {
							sign=java.net.URLEncoder.encode(sign,"UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						Message msg=new Message();
						msg.what=ALIPREPAY_SUCCESS;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**����֧��*/
	private void uPPayInbackground(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String orderId=payOrderId;
				String url=WEBInterface.UPPAY_URL+orderId;
				String token=PrefrenceConfig.getUserMessage(PayMoneyActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpPost(url, token, null);
				Log.d("yckx", ""+respose);
				if(respose==null){
					return;
				}
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						JSONObject unionPayOrderInfos=jSONObject.getJSONObject("unionPayOrderInfos");
						String tn=unionPayOrderInfos.getString("tn");
						Message msg=new Message();
						msg.what=SDK_UPPAY_FLAG;
						msg.obj=tn;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//
			}
		}).start();
	}


	private  Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ALIPREPAY_SUCCESS://֧����Ԥ֧���ɹ�
				if(aliPrePayOrderInfos.equals("")||sign.equals("")){
					//��ȡ������ȷ�����ݣ����ܷ���֧����ƽ̨
					ToastUtil.show(getApplicationContext(), "Ԥ֧��ʧ�ܣ�����", 2000);
					return;
				}
				//����֧����ƽ̨
				String payInfo=aliPrePayOrderInfos+"&sign=\"" + sign + "\"&" + getSignType();
				Log.d("yckx", payInfo);
				payAlipaySDK(payInfo);
				break;

			case SDK_PAY_FLAG://֧����֧��������������
			{
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * ͬ�����صĽ��������õ�����˽�����֤����֤�Ĺ����뿴https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) �����̻������첽֪ͨ
				 */
				String resultInfo = payResult.getResult();// ͬ��������Ҫ��֤����Ϣ

				String resultStatus = payResult.getResultStatus();
				// �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
				if (TextUtils.equals(resultStatus, "9000")) {
					//Toast.makeText(PayMoneyActivity.this, "֧���ɹ�", Toast.LENGTH_SHORT).show();
					//�������ָ������ʦ����ת������ʦ���棬������ת�������������
					//����ʦ������ҲҪ�ж�(�Ȳ�����,����һ��ҳ�洦��)
					Intent intent=new Intent(PayMoneyActivity.this,FormDetailActivity.class);//FormDetailActivity���ԣ���ת������ʦ������
					intent.putExtra("FormDetailActivity", payOrderId);
					GlobalServiceUtils.setGloubalServiceSession("orderId",payOrderId);
					//intent.putExtra("FormDetailActivity",  orderEntity.getOrderId());
					startActivity(intent);
					finish();
				} else {
					// �ж�resultStatus Ϊ��"9000"��������֧��ʧ��
					// "8000"����֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayMoneyActivity.this, "֧�����ȷ����", Toast.LENGTH_SHORT).show();

					} else {
						// ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
						Toast.makeText(PayMoneyActivity.this, "֧��ʧ��", Toast.LENGTH_SHORT).show();
					}}
			}
			break;
			case SDK_UPPAY_FLAG:
			{
				String tn=(String) msg.obj;
				UPPayAssistEx.startPay(PayMoneyActivity.this, null, null, tn, "00");
			}
			break;
			case UPPAY_SUCCESS:
				Intent intent=new Intent(PayMoneyActivity.this,FormDetailActivity.class);//FormDetailActivity���ԣ���ת������ʦ������
				intent.putExtra("FormDetailActivity", payOrderId);
				GlobalServiceUtils.setGloubalServiceSession("orderId",payOrderId);
				//intent.putExtra("FormDetailActivity",  orderEntity.getOrderId());
				startActivity(intent);
				finish();
				break;
			case SDK_WXPAY_FLAG:
				HashMap<String,String> hashMap=(HashMap<String, String>) msg.obj;  
				testHashMap=hashMap;
				String appid=hashMap.get("appid");
				String partnerid=hashMap.get("partnerid");
				String prepayid=hashMap.get("prepayid");
				String packageValue=hashMap.get("packageValue");
				String noncestr=hashMap.get("noncestr");
				String timestamp=hashMap.get("timestamp");
				String sign=hashMap.get("sign");
				PayReq request = new PayReq();
				request.appId = appid;
				request.partnerId = partnerid;
				request.prepayId= prepayid;
				request.nonceStr= noncestr;
				request.timeStamp= timestamp;
				request.packageValue = packageValue;
				request.sign= sign;
				request.extData= "app data"; // optional
				api.sendReq(request);
				Toast.makeText(PayMoneyActivity.this, "��������֧��", Toast.LENGTH_SHORT).show();
				Log.d("yckx", "����΢��֧��");
				break;
			}
		};
	};

	/**֧����*/
	private void payAlipaySDK(final String payInfo){
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// ����PayTask ����
				PayTask alipay = new PayTask(PayMoneyActivity.this);
				// ����֧���ӿڣ���ȡ֧�����
				String result = alipay.pay(payInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// �����첽����
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * get the sign type we use. ��ȡǩ����ʽ(֧����)
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}


	/**��ȡ����֧�����*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if( data == null ){
			return;
		}
		String str =  data.getExtras().getString("pay_result");
		if( str.equalsIgnoreCase("success") ){
			// ֧���ɹ���extra���������result_data��ȡ��У��
			// result_data�ṹ��c��result_data����˵��
			if(data.hasExtra("result_data")) {
				String sign =  data.getExtras().getString("result_data");  
				// ��ǩ֤��ͬ��̨��ǩ֤��
				// �˴���verify���̻�����ȥ�̻���̨����ǩ 
				verify(sign);//�Ȳ�����ǩ
			} else {
				// δ�յ�ǩ����Ϣ
				// ����ͨ���̻���̨��ѯ֧�����
			}
		}else if(str.equalsIgnoreCase("fail") ){
			//showResultDialog(" ֧��ʧ�ܣ� ");
			ToastUtil.show(getApplicationContext(), "֧��ʧ��", 2000);
		}else if(str.equalsIgnoreCase("cancel") ){
			//showResultDialog(" ����ȡ���˱��ζ�����֧���� ");
			ToastUtil.show(getApplicationContext(), "���Ѿ�ȡ���˱���֧��", 2000);
		}
	}
	
	/**֧���ɹ�*/
	private static final int UPPAY_SUCCESS=11;
	
	/**����֧����ɻص��������ӿ�*/
	private void verify(String sign){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String orderId=payOrderId;
				String url=WEBInterface.UPPAY_END_URL+orderId;
				String token=PrefrenceConfig.getUserMessage(PayMoneyActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpPost(url, token, null);
				Log.d("yckx", "�ص����ؽ��:"+respose+"");
				if(respose==null){
					return;
				}
			    try {
					JSONObject jsonObject=new JSONObject(respose);
					String code=jsonObject.getString("code");
					if(code.equals("0")){
						Message msg=new Message();
						msg.what=UPPAY_SUCCESS;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**΢��ע��*/
	private void wxPayInbackground(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String orderId=payOrderId;
				String token=PrefrenceConfig.getUserMessage(PayMoneyActivity.this).getUserToken();
				String url=WEBInterface.WXPAY_URL+orderId;
				String respose=ApacheHttpUtil.httpPost(url, token, null);
				Log.d("yckx","wx֧�����"+respose);
				if(respose==null){
					return;
				}
				//΢��֧��start
				//΢��֧��ע��
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						JSONObject wxPrePayOrderInfos=jSONObject.getJSONObject("wxPrePayOrderInfos");
						String packageValue=wxPrePayOrderInfos.getString("package");
						String appid=wxPrePayOrderInfos.getString("appid");
						String sign=wxPrePayOrderInfos.getString("sign");
						String partnerid=wxPrePayOrderInfos.getString("partnerid");
						String prepayid=wxPrePayOrderInfos.getString("prepayid");
						String noncestr=wxPrePayOrderInfos.getString("noncestr");
						String timestamp=wxPrePayOrderInfos.getString("timestamp");
						HashMap<String,String> hashMap=new HashMap<String, String>();
						hashMap.put("packageValue", packageValue);
						hashMap.put("appid", appid);
						hashMap.put("sign", sign);
						hashMap.put("partnerid", partnerid);
						hashMap.put("prepayid", prepayid);
						hashMap.put("noncestr", noncestr);
						hashMap.put("timestamp", timestamp);
						Message msg=new Message();
						msg.what=SDK_WXPAY_FLAG;
						msg.obj=hashMap;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
				
				//msgApi.sendReq(request);
				
				//΢��֧��end
				
			}
		}).start();
	}
	
	
	//---------΢��֧��start-----//
	/*private static final String TAG = "MicroMsg.SDKSample.PayActivity";

	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	TextView show;
	Map<String,String> resultunifiedorder;
	StringBuffer sb;
	private void wxpay(){
		//����prepay_id
		GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
		getPrepayId.execute();
		//����ǩ������
		genPayReq();
       //����΢��֧��
	}
	
	
	*//**
	 ����ǩ��
	 *//*

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);


		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion","----"+packageSign);
		return packageSign;
	}
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		this.sb.append("sign str\n"+sb.toString()+"\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes());
		Log.e("orion","----"+appSign);
		return appSign;
	}
	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");


			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");

		Log.e("orion","----"+sb.toString());
		return sb.toString();
	}

	private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {
		private ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PayMoneyActivity.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
		}
		@Override
		protected void onPostExecute(Map<String,String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			sb.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
			show.setText(sb.toString());
			resultunifiedorder=result;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String,String>  doInBackground(Void... params) {
			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();
			Log.e("orion","----"+entity);
			byte[] buf = Util.httpPost(url, entity);
			String content = new String(buf);
			Log.e("orion", "----"+content);
			Map<String,String> xml=decodeXml(content);
			return xml;
		}
	}

	public Map<String,String> decodeXml(String content) {
		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName=parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if("xml".equals(nodeName)==false){
						//ʵ����student����
						xml.put(nodeName,parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion","----"+e.toString());
		}
		return null;

	}


	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}



	private String genOutTradNo() {
		Random random = new Random();
//		return "COATBAE810"; //������д���Ļ�ֻ��֧��һ�Σ��ڶ��β������ɶ���
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}


	//
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String	nonceStr = genNonceStr();

		xml.append("</xml>");
		List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
		packageParams.add(new BasicNameValuePair("body", "APP pay test"));
		packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
		packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
		packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
		packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
		packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
		packageParams.add(new BasicNameValuePair("total_fee", "2000000"));
		packageParams.add(new BasicNameValuePair("trade_type", "APP"));


		String sign = genPackageSign(packageParams);
		packageParams.add(new BasicNameValuePair("sign", sign));


		String xmlstring =toXml(packageParams);

		return xmlstring;

		} catch (Exception e) {
			Log.e(TAG, "----genProductArgs fail, ex = " + e.getMessage());
			return null;
		}


	}
	private void genPayReq() {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "prepay_id="+resultunifiedorder.get("prepay_id");
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());


		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n"+req.sign+"\n\n");

		show.setText(sb.toString());

		Log.e("orion", "----"+signParams.toString());

	}
	private void sendPayReq() {


		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}
	//---------΢��֧��end-----//
*/}
