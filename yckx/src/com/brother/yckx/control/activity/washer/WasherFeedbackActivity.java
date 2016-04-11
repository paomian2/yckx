package com.brother.yckx.control.activity.washer;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.ViewUtils;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;

public class WasherFeedbackActivity extends BaseActivity{
	private String orderId;
	private TextView evaluationCancel,evaluationCommit;
	private EditText ev_evaluation;
	private LinearLayout layout_main,layout_evaluation;
	private final int FEEBACK_RETURN_SUCCESS=0;
	private final int TOKEN_ERROR=2;
	private final int OTHER_ERROR=3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_washerfeedback);
		orderId=getIntent().getStringExtra("WasherFeedbackActivity"); 
		init();
	}
	private void init() {
		layout_main=(LinearLayout) findViewById(R.id.layout_main);
		layout_evaluation=(LinearLayout) findViewById(R.id.layout_evaluation);
		evaluationCancel=(TextView) findViewById(R.id.evaluationCancel);
		evaluationCommit=(TextView) findViewById(R.id.evaluationCommit);
		ev_evaluation=(EditText) findViewById(R.id.ev_evaluation);
		
		layout_evaluation.setOnClickListener(listener);
		evaluationCancel.setOnClickListener(listener);
		evaluationCommit.setOnClickListener(listener);
		
	}
	
	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.evaluationCancel:
				finish();
				break;
			case R.id.layout_evaluation:
				ViewUtils.setEditTextKeyBoard(ev_evaluation, layout_main);
				break;
			case R.id.evaluationCommit:
				donInbackground();
				break;
			}
		}
	};
	
	
	private void donInbackground(){
		evaluationCommit.setClickable(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String replyContent=ev_evaluation.getText().toString().trim();
				String token=PrefrenceConfig.getUserMessage(WasherFeedbackActivity.this).getUserToken();
				HashMap<String,String> hasp=new HashMap<String, String>();
				hasp.put("content", replyContent);
				String host=WEBInterface.WASHERCOMMENTREPLY+orderId;
                String respose=ApacheHttpUtil.httpPost(host, token, hasp);
                Log.d("yckx","美护师回复评论返回的网络数据:"+respose);
                try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					String reminderMsg=jSONObject.getString("msg")+"";
					if(code.equals("0")){
						Message msg=new Message();
						msg.what=FEEBACK_RETURN_SUCCESS;
						mHandler.sendMessage(msg);
					}else if(code.equals("2")){
						Message msg=new Message();
						msg.what=TOKEN_ERROR;
						mHandler.sendMessage(msg);
					}else{
						Message msg=new Message();
						msg.what=OTHER_ERROR;
						msg.obj=reminderMsg;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
			}
		}).start();
	}

	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(FEEBACK_RETURN_SUCCESS==msg.what){
				ToastUtil.show(getApplicationContext(), "回复成功", 2000);
				evaluationCommit.setClickable(true);
				WasherFeedbackActivity.this.finish();
			}
			if(TOKEN_ERROR==msg.what){
				evaluationCommit.setClickable(true);
				ToastUtil.show(getApplicationContext(), "登录状态失效，请重新登录再评论", 2000);
			}
			if(OTHER_ERROR==msg.what){
				evaluationCommit.setClickable(true);
				ToastUtil.show(getApplicationContext(), ""+msg.obj, 2000);
			}
		};
	};
	

}
