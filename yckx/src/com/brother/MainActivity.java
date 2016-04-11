package com.brother;


import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.FormDetailActivity;
import com.brother.yckx.control.activity.owner.HomeActivity;
import com.brother.yckx.control.activity.owner.LeadActivity;
import com.brother.yckx.control.activity.rongyun.model.RongIMFriend;
import com.brother.yckx.control.activity.washer.WasherMainActivity;
import com.brother.yckx.model.UserEntity;
import com.brother.yckx.model.db.AssetsDBManager;
import com.brother.yckx.model.db.DBRead;
import com.brother.yckx.view.NotificationUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.tencent.android.tpush.common.Constants;
public class MainActivity extends BaseActivity implements RongIM.UserInfoProvider{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initApp();
		//融云聊天
		inituserInfo();
		getRunYunToken();
		//
		XGPushConfig.enableDebug(this, true);
		//注册信鸽
		registerXG();
		//信鸽接受通知
		receiveXGNotice();
		//判断是否要进入引导界面
		if(PrefrenceConfig.isFistUse(this)){
			//第一次使用
			Intent intent=new Intent(this,LeadActivity.class);
			intent.putExtra("FromActivity", "MainActivity");
			startActivity(intent);
			finish();
		}else{
			//判断是美护师还是普通用户
			UserEntity user=PrefrenceConfig.getUserMessage(MainActivity.this);
			if(user.getUserRole().equals("washer")){
				//美护师
				Intent intent=new Intent(this,WasherMainActivity.class);
				startActivity(intent);
				finish();
				return;
			}
			Intent intent=new Intent(this,HomeActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void initApp(){
		double searchLat=0;
		double searchLng=0;
		GlobalServiceUtils.setGloubalServiceSession("searchLat", searchLat);
		GlobalServiceUtils.setGloubalServiceSession("searchLng", searchLng);
		GlobalServiceUtils.setGloubalServiceSession("mapStyle", "location");
	}
	//--------------信鸽start---------------//
	Message m = null;
	private void registerXG(){
		// 1.获取设备Token
		Handler handler = new HandlerExtension(MainActivity.this);			
		m = handler.obtainMessage();
		XGPushManager.registerPush(getApplicationContext(),
				new XGIOperateCallback() {
			@Override
			public void onSuccess(Object data, int flag) {
				Log.w(Constants.LogTag,
						"+++ register push sucess. token:" + data);
				m.obj = "+++ register push sucess. token:" + data;
				//把设备的token发送到服务器
				sendToken2Service(""+data);
				m.sendToTarget();
			}
			@Override
			public void onFail(Object data, int errCode, String msg) {
				Log.w(Constants.LogTag,
						"+++ register push fail. token:" + data
						+ ", errCode:" + errCode + ",msg:"
						+ msg);
				m.obj = "+++ register push fail. token:" + data
						+ ", errCode:" + errCode + ",msg:" + msg;
				m.sendToTarget();
			}
		});
	}
	private static class HandlerExtension extends Handler {
		WeakReference<MainActivity> mActivity;
		HandlerExtension(MainActivity activity) {
			mActivity = new WeakReference<MainActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MainActivity theActivity = mActivity.get();
			if (theActivity == null) {
				theActivity = new MainActivity();
			}
			if (msg != null) {
				Log.w(Constants.LogTag, msg.obj.toString());
			}
		}}


	/**发送token到服务器*/
	private void sendToken2Service(final String xgtoken){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String deviceType="android";
				HashMap<String,String> hasmp=new HashMap<String,String>();
				hasmp.put("token", xgtoken);
				hasmp.put("deviceType", deviceType);
				String token=PrefrenceConfig.getUserMessage(MainActivity.this).getUserToken();
				String respose="";
				try {
					respose = ApacheHttpUtil.httpPost2(WEBInterface.SENDTOKEN2SERVICE_URL, token, new StringEntity(new JSONObject(hasmp).toString(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("yckx", respose+"");
			}
		}).start();
	}

	public boolean isMainProcess() {
		ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		String mainProcessName = getPackageName();
		int myPid = android.os.Process.myPid();
		for (RunningAppProcessInfo info : processInfos) {
			if (info.pid == myPid && mainProcessName.equals(info.processName)) {
				return true;
			}
		}
		return false;
	}

	private void receiveXGNotice(){
		// 在主进程设置信鸽相关的内容
		if (isMainProcess()) {
			// 为保证弹出通知前一定调用本方法，需要在application的onCreate注册
			// 收到通知时，会调用本回调函数。
			// 相当于这个回调会拦截在信鸽的弹出通知之前被截取
			// 一般上针对需要获取通知内容、标题，设置通知点击的跳转逻辑等等
			XGPushManager
			.setNotifactionCallback(new XGPushNotifactionCallback() {

				@Override
				public void handleNotify(XGNotifaction xGNotifaction) {
					Log.d("yckx", "处理信鸽通知：" + xGNotifaction);
					// 获取标签、内容、自定义内容
					int notifyId=xGNotifaction.getNotifyId();
					String title = xGNotifaction.getTitle();
					String content = xGNotifaction.getContent();
					String customContent = xGNotifaction
							.getCustomContent();//customContent中包括了需要推送的数据信息
					NotificationUtil.setOpenNotification(getApplicationContext(), true);
					Log.d("yckx", customContent);
					//String mstr="{type:robbed,messageId:491,linkId:3369}";
					//System.out.println(datas[3].split("}")[0]);
					/*String datas[]=customContent.split(":");
					dingdanhao=datas[3].split("}")[0];*/
					//新订单{"type":"payed","messageId":540,"linkId":3382}
					try {
						JSONObject jsonObject=new JSONObject(customContent);
					    String linkId=jsonObject.getString("linkId");
					    GlobalServiceUtils.setGloubalServiceSession("orderId", linkId);
					    dingdanhao=linkId;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					notification(title,content);
					sendNotice2Service(notifyId+"");
					//播放声音
					// 其它的处理
					// 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
					//xGNotifaction.doNotify();(这个是系统的消息)
				}
			});}
	}
	/**通知服务器我已经接受到推送了*/
	private void sendNotice2Service(final String messageId){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(getApplicationContext()).getUserToken();
				String host=WEBInterface.SENDG_I_GET_NOTICE_FROMSERVICE_URL+messageId;
				String respose=ApacheHttpUtil.httpPost(host, token, null);
				Log.d("yckx", "通知服务器我已经收到推送了"+respose);
			}
		}).start();
	}

	//--------------信鸽end---------------//
	private String dingdanhao;

	//-----------------notificaton--stary---------//
	public final static String ACTION_BTN = "com.notification.btn.topager";
	public final static String INTENT_NAME = "btnid";
	public final static int INTENT_BTN_LOGIN = 1;
	NotificationBroadcastReceiver mReceiver;
	private void notification(String tittle,String content) {
		unregeisterReceiver();
		intiReceiver();
		Log.d("yckx", "notification()---start");
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification_appincon);
		remoteViews.setTextViewText(R.id.tv_text, content);
		remoteViews.setTextViewText(R.id.tv_title, tittle);
		Intent intent = new Intent(ACTION_BTN);
		intent.putExtra(INTENT_NAME, INTENT_BTN_LOGIN);
		PendingIntent intentpi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.tv_xiangqing, intentpi);
		Intent intent2 = new Intent();
		intent2.setClass(this, MainActivity.class);
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent intentContent = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setOngoing(false);
		builder.setAutoCancel(false);
		builder.setContent(remoteViews);
		builder.setTicker(tittle);
		builder.setSmallIcon(R.drawable.edit_background_red);
		Notification notification = builder.build();
		notification.sound=Uri.parse("android.resource://" 
				+ getPackageName() + "/" +R.raw.canrobbed);
		notification.flags = Notification.FLAG_NO_CLEAR;
		notification.contentIntent = intentContent;
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
		Log.d("yckx", "notification()---end");
	}

	class NotificationBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ACTION_BTN)) {
				int btn_id = intent.getIntExtra(INTENT_NAME, 0);
				switch (btn_id) {
				case INTENT_BTN_LOGIN:
					//Toast.makeText(MainActivity.this, "从通知栏点登录", Toast.LENGTH_SHORT).show();
					Intent intent_topage=new Intent(MainActivity.this,FormDetailActivity.class);
					startActivity(intent_topage);
					unregeisterReceiver();
					NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
					notificationManager.cancel(0);
					break;
				}
			}
		}
	}
	private void intiReceiver() {
		mReceiver = new NotificationBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_BTN);
		getApplicationContext().registerReceiver(mReceiver, intentFilter);
	}
	private void unregeisterReceiver() {
		if (mReceiver != null) {
			getApplicationContext().unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}
	//-----------------notificaton--end---------//

	
	//-----------------RongIM-----start-------//
	private List<RongIMFriend> userIdlist;
	private void inituserInfo(){
		//检查是否存在数据库
		if(!DBRead.isExistsCardbFile()){//不存在则加载
			try {
				// 将本地项目中的Assets/db/commonnum.db文件复制写出到 DBRead.telFile文件中
				AssetsDBManager.copyAssetsFileToFile(getApplicationContext(), "db/carsplist.db", DBRead.carFile);
			} catch (IOException e) {
				ToastUtil.show(MainActivity.this, "初始化车辆信息数据库异常", 2000);
				Log.d("yckx","加载已经聊过天的用户失败");
			}
		}
		DBRead.initRongIMUserTable();
		UserEntity user=PrefrenceConfig.getUserMessage(MainActivity.this);
		if(user.getUserRole().equals("washer")){
			userIdlist=DBRead.queryRongIMUser(false);
		}else{
			userIdlist=DBRead.queryRongIMUser(true);
		}
		RongIM.setUserInfoProvider(this, true);
		Log.d("yckx","加载已经聊过天的用户"+userIdlist.size());
		//getRunYunToken();
	}
	
	

	@Override
	public UserInfo getUserInfo(String s) {
		// TODO Auto-generated method stub
		for(RongIMFriend i:userIdlist){
			if(i.getUserId().equals(s)){
				Log.d("yckx", "融云返回getUserInfo()");
				//return new UserInfo(i.getUserId(), i.getNickname(), Uri.parse(WEBInterface.INDEXIMGURL+i.getPortrait()));
				return new UserInfo("1695", "aaaa", Uri.parse(WEBInterface.INDEXIMGURL+i.getPortrait()));
			}
		}
		Log.d("yckx", "融云返回getUserInfo()失败:"+s);
		return null ;
	}
	
	
	
	/**获取荣云token值*/
	private void getRunYunToken(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(MainActivity.this).getUserToken();
				String url=WEBInterface.RONGYUN_TOKEN_URL;
				String respose=ApacheHttpUtil.httpPost(url, token, null);
				Log.d("yckx", "ronyun token:"+respose);
				if(respose==null){
					return;
				}
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						String grTk=jSONObject.getString("grTk");
						Message msg=new Message();
						msg.what=1;
						msg.obj=grTk;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private Handler mHandler=new Handler(){
		@SuppressLint("HandlerLeak") public void handleMessage(Message msg) {
			if(msg.what==1){
				String grTk=(String) msg.obj;
				//获取当前用户会话列表
				connect(grTk);
			}
		};
	};
	
	/**
	 * 建立与融云服务器的连接
	 *
	 * @param token
	 */
	private void connect(String token) {

	    if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {

	        /**
	         * IMKit SDK调用第二步,建立与服务器的连接
	         */
	        RongIM.connect(token, new RongIMClient.ConnectCallback() {

	            /**
	             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
	             */
	            @Override
	            public void onTokenIncorrect() {

	                Log.d("LoginActivity", "--onTokenIncorrect");
	            }

	            /**
	             * 连接融云成功
	             * @param userid 当前 token
	             */
	            @Override
	            public void onSuccess(String userid) {

	                Log.d("LoginActivity", "--onSuccess" + userid);
	            }

	            /**
	             * 连接融云失败
	             * @param errorCode 错误码，可到官网 查看错误码对应的注释
	             */
	            @Override
	            public void onError(RongIMClient.ErrorCode errorCode) {

	                Log.d("LoginActivity", "--onError" + errorCode);
	            }
	        });
	    }
	}
	//-----------------RongIM-----end-------//
}
