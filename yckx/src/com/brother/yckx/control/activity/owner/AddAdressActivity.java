package com.brother.yckx.control.activity.owner;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.model.AdressEntity;

public class AddAdressActivity extends BaseActivity{
	
	private EditText phone_ev,provice_ev,carAdress_ev;
	private String id="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_adress);
		setActionBar(R.string.addadress_title, R.drawable.btn_homeasup_default, R.drawable.icon_default_save, listener);
		id=getIntent().getStringExtra("AddAdressActivity");
		init();
	}

	private void init(){
		phone_ev=(EditText) findViewById(R.id.phone);
		provice_ev=(EditText) findViewById(R.id.provice);
		carAdress_ev=(EditText) findViewById(R.id.carAdress);
		setUp();
	}
	
	private void setUp(){
		if(!TextUtils.isEmpty(id)){
			AdressEntity entity=GlobalServiceUtils.getInstance().getGloubalServiceAdress();
			if(entity!=null){
				phone_ev.setText(entity.getPhone());
				provice_ev.setText(entity.getProvince());
				carAdress_ev.setText(entity.getLocation());
			}
		}
	}
	
	
	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.iv_action_right:
				uploadData(id);
				break;

			default:
				break;
			}
		}
	};
	
	
	private void uploadData(final String id){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String phone=phone_ev.getText().toString().trim();
				String provice=provice_ev.getText().toString().trim();
				String carAdress=carAdress_ev.getText().toString().trim();
				String isDefault="false";
				HashMap<String, String> hasmp=new HashMap<String, String>();
				if(id!=null&&!id.equals("")){
					hasmp.put("id", id);
				}
				hasmp.put("phone", phone);
				hasmp.put("prefix", provice);
				hasmp.put("address", carAdress);
				hasmp.put("isDefault", isDefault);
				String token=PrefrenceConfig.getUserMessage(AddAdressActivity.this).getUserToken();
				String respose="";
				try {
					 respose=ApacheHttpUtil.httpPost2(WEBInterface.ADDADRESS_URL, token, new StringEntity(new JSONObject(hasmp).toString(),"UTF-8"));
				     Log.d("yckx", respose);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						//添加成功
						Message msg=new Message();
						msg.what=ADDADRESS_SUCCESS;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**添加成功*/
	private final int ADDADRESS_SUCCESS=0;
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ADDADRESS_SUCCESS:
				ToastUtil.show(getApplicationContext(), "添加成功",2000);
				Intent intent=new Intent(AddAdressActivity.this,AdressActivity.class);
				intent.putExtra("AdressActivity", "addsuccess");
				AddAdressActivity.this.setResult(3, intent);
				AddAdressActivity.this.finish();
				break;

			default:
				break;
			}
		};
	};
}
