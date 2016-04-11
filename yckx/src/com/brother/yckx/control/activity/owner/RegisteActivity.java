package com.brother.yckx.control.activity.owner;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.EncryptUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.net.JavaHttpUtil;
import com.brother.utils.net.StreamUtil;
import com.brother.yckx.R;
import com.brother.yckx.model.UserEntity;

public class RegisteActivity extends BaseActivity{

	private String mRegistOrModify=null;
	private EditText userName,code,pwd,pwd_ok;
	private TextView sendCode,yckx_agreement;
	private LinearLayout registLayout,modifyLayout;
	private Button btn_regist,btn_modify;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist);
		init();
		//如果是修改密码，显示修改密码的布局，如果是注册，显示注册的布局
		Intent intent=getIntent();
		mRegistOrModify=intent.getStringExtra("RegistOrModify");
		if(mRegistOrModify.equals("Regist")){
			btn_regist.setVisibility(View.VISIBLE);
			btn_modify.setVisibility(View.GONE);
		}
		if(mRegistOrModify.equals("Modify")){
			btn_modify.setVisibility(View.VISIBLE);
			btn_regist.setVisibility(View.GONE);
		}
	}

	/**验证码发送成功*/
	private final int SENDCODE_SUCCESS=1;
	/**验证码发送失败*/
	private final int SENDCODE_FAIL=-1;
	/**输入的验证码不正确*/
	private final int CODE_ERROR=-11;
	/**账号已经存在*/
	private final int USER_EXIST=6;
	/**注册成功*/
	private final int REGISTER_SUCCESS=2;
	/**注册失败*/
	private final int REGISTER_FAIL=-2;
	/**修改密码成功*/
	private final int MODIFY_SUCEESS=3;
	/**修改密码失败*/
	private final int MODIFY_FAIL=-3;
	/**该手机号码不存在*/
	private final int MODIFY_PHONE_UNEXIST=-4;
	/**未知错误*/
	private final int UNKNOWN_ERROR=18;

	/**正在注册*/
	private final int REGISTERING=20;
	/**停止注册*/
	private final int REGISTERING_STOP=21;
	
	/**正在修改密码*/
	private final int MODIFYING=30;
	/**停止修改密码*/
	private final int MODIFYING_STOP=31;
	

	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SENDCODE_SUCCESS://注册成功返回主界面
				ToastUtil.show(getApplicationContext(), "发送成功", 3000);
				break;
			case SENDCODE_FAIL:
				ToastUtil.show(getApplicationContext(), "发送失败，请核对手机号是否正确", 3000);
				break;
			case CODE_ERROR:
				ToastUtil.show(getApplicationContext(), "验证码不正确", 3000);
				break;
			case REGISTER_SUCCESS:
				//跳转到主界面
				Intent intent=new Intent(RegisteActivity.this,HomeActivity.class);
				startActivity(intent);
				RegisteActivity.this.finish();
				break;
			case REGISTER_FAIL:
				ToastUtil.show(getApplicationContext(), "注册失败", 3000);
				btn_regist.setText("立即注册");
				break;
			case MODIFY_SUCEESS:
				//修改成功，调回主页面
				Intent inetent_modify=new Intent(RegisteActivity.this,HomeActivity.class);
				startActivity(inetent_modify);
				finish();
				break;
			case MODIFY_FAIL:
				ToastUtil.show(getApplicationContext(), "修改失败", 3000);
				btn_modify.setText("确认修改");
				break;
			case MODIFY_PHONE_UNEXIST:
				ToastUtil.show(getApplicationContext(), "该电话号码不存在", 3000);
				break;
			case UNKNOWN_ERROR:
				ToastUtil.show(getApplicationContext(), "未知错误", 3000);
				break;
			case REGISTERING:
				btn_regist.setText("正在注册...");
				break;
			case REGISTERING_STOP:
				btn_regist.setText("立即注册");
				break;
			case MODIFYING_STOP:
				btn_modify.setText("确认修改");
				break;
			case USER_EXIST:
				ToastUtil.show(getApplicationContext(), "账号已经存在", 3000);
				break;
			}
		};
	};

	@SuppressLint("HandlerLeak") private Handler showTimeHandler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==-1){
				sendCode.setText("发送验证码");
				isRunning=false;
				timer.resCancel();
			}
			if(msg.what!=-1)
				sendCode.setText(""+msg.what);
		};
	};

	private void init(){
		userName=(EditText) findViewById(R.id.userName);
		code=(EditText) findViewById(R.id.code);
		pwd=(EditText) findViewById(R.id.pwd);
		pwd_ok=(EditText) findViewById(R.id.pwd_ok);

		sendCode=(TextView) findViewById(R.id.sendCode);
		yckx_agreement=(TextView) findViewById(R.id.yckx_agreement);

		registLayout=(LinearLayout) findViewById(R.id.end_regist_layout);
		modifyLayout=(LinearLayout) findViewById(R.id.end_modify_layout);

		btn_regist=(Button) findViewById(R.id.btn_regist);
		btn_modify=(Button) findViewById(R.id.btn_modify);

		sendCode.setOnClickListener(listener);
		yckx_agreement.setOnClickListener(listener);
		btn_regist.setOnClickListener(listener);
		btn_modify.setOnClickListener(listener);
		
		findViewById(R.id.imgLeft).setOnClickListener(listener);
	}

	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.imgLeft:
				finish();
				break;
			case R.id.sendCode://发送验证码
				sendCodeInbackground();//60秒限制
				break;
			case R.id.btn_regist://注册
				registerInbackground();
				break;
			case R.id.btn_modify://修改密码
				modifyInbackground();
				break;
			case R.id.yckx_agreement:
				GlobalServiceUtils.setGloubalServiceSession("WebViewActivity","useAgreement");
				Intent intent=new Intent(RegisteActivity.this,WebViewActivity.class);
				startActivity(intent);
				break;
			}
		}
	};


	private void sendCodeInbackground(){
		/**判断网络是否已经连接*/
		//还没写

		if(isRunning){
			return;
		}
		final String userPhone=userName.getText().toString().trim();
		//^1[358]\\d{9}$
		String parten="^1[358]\\d{9}$";
		if(userPhone==null){
			ToastUtil.show(getApplicationContext(), "手机号不能为空", 3000);
			return;
		}
		if(!userPhone.matches(parten)){
			ToastUtil.show(getApplicationContext(), "请输入正确的手机号", 3000);
			return;
		}
		timer=setRunningText();
		isRunning=true;
		//显示60秒倒计时
		new Thread(new Runnable() {
			@Override
			public void run() {
				InputStream inStream=JavaHttpUtil.httpGet(WEBInterface.SENDCODEURL+userPhone, null);
				try {
					String json=StreamUtil.getString(inStream);
					if(json!=null){
						JSONObject jSONObject=new JSONObject(json);
						String jSONmsg=jSONObject.getString("msg");
						if(jSONmsg.equals("成功")){
							//
							Message msg=new Message();
							msg.what=SENDCODE_SUCCESS;
							mHandler.sendMessage(msg);

						}else if(jSONmsg.equals("短信发送失败")){
							Message msg=new Message();
							msg.what=SENDCODE_FAIL;
							mHandler.sendMessage(msg);
						}else{
							Message msg=new Message();
							msg.what=UNKNOWN_ERROR;
							mHandler.sendMessage(msg);
						}
					}
				} catch (Exception e) {
					Log.d("yckx", "sendCodeInbackground()访问网络失败");
					e.printStackTrace();
				}
			}
		}).start();
	}

	private boolean isRegistering;
	private void registerInbackground(){
		if(isRegistering){
			return;
		}
		final String userPhone=userName.getText().toString().trim();
		final String userCode=code.getText().toString().trim();
		final String userPwd=pwd.getText().toString().trim();
		final String userPwdOk=pwd_ok.getText().toString().trim();
		if(!userPwd.equals(userPwdOk)){
			ToastUtil.show(getApplicationContext(), "两次密码不一样，请重新输入", 3000);
			return;
		}
		if(userPhone==null){
			ToastUtil.show(getApplicationContext(), "请输入手机号", 3000);
			return;
		}
		if(userCode==null){
			ToastUtil.show(getApplicationContext(), "请输入验证码", 3000);
			return;
		}

		if(userPwdOk==null){
			ToastUtil.show(getApplicationContext(), "请输入确认手机号", 3000);
			return;
		}
		isRegistering=true;
		btn_regist.setText("正在注册...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> hasmap=new HashMap<String, String>();
				hasmap.put("checkCode", userCode);
				hasmap.put("password", userPwd);//加密再上传数据包
				hasmap.put("phone", userPhone);
				try {
					String respose=ApacheHttpUtil.httpPost(WEBInterface.REGISTERURL,  new StringEntity(new JSONObject(hasmap).toString(), "UTF-8"));
					isRegistering=false;
					Message msg_regist=new Message();
					msg_regist.what=REGISTERING_STOP;
					mHandler.sendMessage(msg_regist);
					Log.d("yckx", "注册:"+respose);
					System.out.println("--->>respose"+respose);
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					Message msg=new Message();
					if(code.equals("0")){
						JSONObject childObj=jSONObject.getJSONObject("user");
						String userId=childObj.getString("id");
						String userPhone=childObj.getString("userPhone");
						String userRole=childObj.getString("userRole");
						
						String userOpStatus_mhs=childObj.getString("userOpStatus");
						String userOrderCount_mhs=childObj.getString("userOrderCount");
						String userTotalScore_mhs=childObj.getString("userTotalScore");
						String userCommentCount_mhs=childObj.getString("userCommentCount");
						
						String userToken=jSONObject.getString("token");
						String userAdress_user=jSONObject.getString("defaultAddress");
						String userCard_user=jSONObject.getString("defaultCar");
						
						UserEntity user=new UserEntity(userId, userPhone, "", userPhone, userRole, userToken, userTotalScore_mhs, userCommentCount_mhs, userOpStatus_mhs, userOrderCount_mhs, false, userAdress_user, userCard_user, "0", "0", "0");
						PrefrenceConfig.setLoginUserMessage(RegisteActivity.this, user);
						msg.what=REGISTER_SUCCESS;
						mHandler.sendMessage(msg);
					}
					if(code.equals("7")){
						msg.what=CODE_ERROR;
						mHandler.sendMessage(msg);
					}
					if(code.equals("6")){
						msg.what=USER_EXIST;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e1) {
					isRegistering=false;
					Message msg=new Message();
					msg.what=REGISTER_FAIL;
					mHandler.sendMessage(msg);
					System.out.println("------>>registerInbackground():访问网络失败");
					Log.d("yckx", "registerInbackground()访问网络失败");
					e1.printStackTrace();
				}
			}
		}).start();
	}
	
	
	private boolean isModifing;
	private void modifyInbackground(){
		if(isModifing){
			return;
		}
		final String userPhone=userName.getText().toString().trim();
		final String userCode=code.getText().toString().trim();
		final String userPwd=pwd.getText().toString().trim();
		final String userPwdOk=pwd_ok.getText().toString().trim();
		if(!userPwd.equals(userPwdOk)){
			ToastUtil.show(getApplicationContext(), "两次密码不一样，请重新输入", 3000);
			return;
		}
		if(userPhone==null){
			ToastUtil.show(getApplicationContext(), "请输入手机号", 3000);
			return;
		}
		if(userCode==null){
			ToastUtil.show(getApplicationContext(), "请输入验证码", 3000);
			return;
		}

		if(userPwdOk==null){
			ToastUtil.show(getApplicationContext(), "请输入确认手机号", 3000);
			return;
		}
		isModifing=true;
		btn_modify.setText("正在修改...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream inStream=JavaHttpUtil.httpPost(WEBInterface.MODIFYPWDURL+"/"+userPhone+"/"+userCode+"/"+userPwd, null);//先不加密
					System.out.println("--->>修改密码的网址"+WEBInterface.MODIFYPWDURL+"/"+userPhone+"/"+userCode+"/"+userPwd);
					String respose=StreamUtil.getString(inStream);
					isModifing=false;
					Message msg_regist=new Message();
					msg_regist.what=MODIFYING_STOP;
					mHandler.sendMessage(msg_regist);
					Log.d("yckx", "注册:"+respose);
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					Message msg=new Message();
					if(code.equals("0")){
						//
						msg.what=MODIFY_SUCEESS;
						mHandler.sendMessage(msg);
					}
					if(code.equals("7")){
						msg.what=CODE_ERROR;
						mHandler.sendMessage(msg);
					}
					if(code.equals("19")){
						msg.what=MODIFY_PHONE_UNEXIST;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e1) {
					isModifing=false;
					Message msg=new Message();
					msg.what=MODIFY_FAIL;
					mHandler.sendMessage(msg);
					System.out.println("------>>modifyInbackground():访问网络失败");
					Log.d("yckx", "modifyInbackground()访问网络失败");
					e1.printStackTrace();
				}
			}
		}).start();
	}

	MyTimer timer;
	private int time=60;
	private int showTime;
	private boolean isRunning;
	public MyTimer setRunningText(){
		showTime=time;
		final MyTimer timer=new MyTimer();
		final TimerTask timerTask=new TimerTask() {
			@Override
			public void run() {
				showTime--;
				Message msg=new Message();
				msg.what=showTime;
				showTimeHandler.sendMessage(msg);
			}
		};
		timer.schedule(timerTask, 1000, 1000);
		return timer;
	}
	class MyTimer extends Timer{
		public void resCancel(){
			this.cancel();
		};
	}

}
