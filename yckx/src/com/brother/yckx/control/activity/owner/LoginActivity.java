package com.brother.yckx.control.activity.owner;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.brother.App;
import com.brother.BaseActivity;
import com.brother.MainActivity;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.EncryptUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.rongyun.model.RongIMFriend;
import com.brother.yckx.control.activity.washer.WasherMainActivity;
import com.brother.yckx.model.UserEntity;
import com.brother.yckx.model.db.DBRead;

public class LoginActivity extends BaseActivity implements RongIM.UserInfoProvider{
	private ImageView imgLeft;
	private EditText userName,passWord;
	private Button btnLogin;
	private TextView tvRegist,tvForget;
	/**用户名或密码错误*/
	private final int USERNAME_OR_PWD_ERROR=3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setActionBar(NULL_ID, R.drawable.btn_homeasup_default, NULL_ID, listener);
		init();
	}
	
	private void init(){
		imgLeft=(ImageView) findViewById(R.id.imgLeft);
		userName=(EditText) findViewById(R.id.userName);
		passWord=(EditText) findViewById(R.id.passWord);
		btnLogin=(Button) findViewById(R.id.btnLogin);
		tvRegist=(TextView) findViewById(R.id.tvRegist);
		tvForget=(TextView) findViewById(R.id.tvForget);
		
		imgLeft.setOnClickListener(listener);
		btnLogin.setOnClickListener(listener);
		tvRegist.setOnClickListener(listener);
		tvForget.setOnClickListener(listener);
	}
	
	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.imgLeft:
				Intent intent_back=new Intent(LoginActivity.this,HomeActivity.class);
				startActivity(intent_back);
				finish();
				break;
			case R.id.btnLogin:
				loginInBackground();
				break;
			case R.id.tvRegist:
				Intent intent_R=new Intent(LoginActivity.this,RegisteActivity.class);
				intent_R.putExtra("RegistOrModify", "Regist");
				startActivity(intent_R);
				break;
			case R.id.tvForget:
				Intent intent_M=new Intent(LoginActivity.this,RegisteActivity.class);
				intent_M.putExtra("RegistOrModify", "Modify");
				startActivity(intent_M);
				break;
			}
		}
	};
	
	/**登录成功*/
	private final int LOGIN_SUCCESS=0;
	/**登录失败*/
	private final int LOGIN_FAILD=-1;
	/**停止登录*/
	private final int LOGIN_STOP=-2;
	@SuppressLint("HandlerLeak") private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOGIN_SUCCESS:
				//登录融云
				inituserInfo();
				getRunYunToken();
				String userRole=PrefrenceConfig.getUserMessage(LoginActivity.this).getUserRole();
				Intent intent=new Intent();
				if(userRole.equals("washer")){//洗护师登录
					intent.setClass(LoginActivity.this,WasherMainActivity.class);
				}else if(userRole.equals("owner")){
					intent.setClass(LoginActivity.this,HomeActivity.class);
				}
				startActivity(intent);
				finish();
				break;
			case LOGIN_FAILD:
				ToastUtil.show(getApplicationContext(), "登录失败", 3000);
				btnLogin.setText("登录");
				break;
			case LOGIN_STOP:
				btnLogin.setText("登录");
				break;
			case USERNAME_OR_PWD_ERROR:
				ToastUtil.show(getApplicationContext(), "用户名或密码错误", 2000);
				break;
			}
		};
	};
	
	private boolean isLogining;
	private void loginInBackground(){
		if(isLogining){
			return;
		}
		final String userPhone=userName.getText().toString().trim();
		final String userPwd=passWord.getText().toString().trim();
		isLogining=true;
		btnLogin.setText("登录中...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> hasmp=new HashMap<String, String>();
				hasmp.put("phone", userPhone);
				//hasmp.put("password",EncryptUtils.md5(userPwd));
				hasmp.put("password",userPwd);
				try {
					String respose=ApacheHttpUtil.httpPost(WEBInterface.LOGINURL,new StringEntity(new JSONObject(hasmp).toString(),"UTF-8"));
					System.out.println("--->>Login respose:"+respose);
					Log.d("yckx", respose);
					isLogining=false;
					Message msg=new Message();
					msg.what=LOGIN_STOP;
					mHandler.sendMessage(msg);
					//解析数据
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						UserEntity user=PrefrenceConfig.getUserMessage(LoginActivity.this);
						JSONObject childObj=jSONObject.getJSONObject("user");
						String userId=childObj.getString("id");
						String userName="";
						try {
							userName=childObj.getString("userName");
						} catch (Exception e) {
							//userName=childObj.getString("userPhone");
							userName="";
						}
						String userPhone=childObj.getString("userPhone");
						
						String userImageUrl="";
						try {
							userImageUrl=childObj.getString("userImageUrl");
						} catch (Exception e) {
							userImageUrl=user.getUserIamgeUrl();
							// TODO: handle exception
						}
						//图片
						String userRole=childObj.getString("userRole");
						
						String userOpStatus_mhs=childObj.getString("userOpStatus");
						String userOrderCount_mhs=childObj.getString("userOrderCount");
						String userTotalScore_mhs=childObj.getString("userTotalScore");
						String userCommentCount_mhs=childObj.getString("userCommentCount");
						
						String userToken=jSONObject.getString("token");
						String userAdress_user=jSONObject.getString("defaultAddress");
						String userCard_user=jSONObject.getString("defaultCar");
						
						
						UserEntity user1=new UserEntity(userId, userName, userImageUrl, userPhone, userRole, userToken, userTotalScore_mhs, userCommentCount_mhs, userOpStatus_mhs, userOrderCount_mhs, false, userAdress_user, userCard_user, "0", "0", "0");
						PrefrenceConfig.setLoginUserMessage(LoginActivity.this, user1);
					    
						//跳转到主页面
						Message msg_success=new Message();
						msg.what=LOGIN_SUCCESS;
						mHandler.sendMessage(msg_success);
					}
					if(code.equals("3")){
						Message msg2=new Message();
						msg2.what=USERNAME_OR_PWD_ERROR;
						mHandler.sendMessage(msg2);
					}
					
				} catch (Exception e) {
					isLogining=false;
					Message msg=new Message();
					msg.what=LOGIN_FAILD;
					mHandler.sendMessage(msg);
					System.out.println("--->>loginInBackground()访问网络失败");
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
  /**根据当前用户是洗护师还是普通用户来确定退到哪个页面*/
  private boolean isWasher(){
	  String role=PrefrenceConfig.getUserMessage(LoginActivity.this).getUserRole();
	  if(role.equals("washer")){
		  return true;
	  }else{
		  return false;
	  }
	  
  }	
  
//-----------------RongIM-----start-------//
	private List<RongIMFriend> userIdlist;
	private void inituserInfo(){
		UserEntity user=PrefrenceConfig.getUserMessage(LoginActivity.this);
		if(user.getUserRole().equals("washer")){
			userIdlist=DBRead.queryRongIMUser(false);
		}else{
			userIdlist=DBRead.queryRongIMUser(true);
		}
		RongIM.setUserInfoProvider(this, true);
		//getRunYunToken();
	}
	
	

	@Override
	public UserInfo getUserInfo(String s) {
		// TODO Auto-generated method stub
		for(RongIMFriend i:userIdlist){
			if(i.getUserId().equals(s)){
				Log.d("yckx", "融云返回getUserInfo()");
				return new UserInfo(i.getUserId(), i.getNickname(), Uri.parse(i.getPortrait()));
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
				String token=PrefrenceConfig.getUserMessage(LoginActivity.this).getUserToken();
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
						mHandlerToken.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private Handler mHandlerToken=new Handler(){
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
  
   public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
	   if(keyCode==KeyEvent.KEYCODE_BACK){
		   Intent intent=new Intent();
		   if(isWasher()){
			   intent.setClass(LoginActivity.this,WasherMainActivity.class);
		   }else{
			   intent.setClass(LoginActivity.this,HomeActivity.class);
		   }
		  
		   startActivity(intent);
		   finish();
	   }
	return false;};
	
	

}
