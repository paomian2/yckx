package com.brother.yckx.control.activity.owner;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.brother.BaseActivity;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.ViewUtils;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;

public class FeedBackActivity extends BaseActivity{
	
	private EditText ev_feedback;
	private LinearLayout layout_main;
	private final int FEEDBACK_SUCCESS=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_feedback);
		setActionBar(R.string.feedb_tittle, R.drawable.btn_homeasup_default, R.drawable.icon_default_send, actionListener);
		layout_main=(LinearLayout) findViewById(R.id.layout_main);
		ev_feedback=(EditText) findViewById(R.id.ev_feedback);
		findViewById(R.id.layout_edittext).setOnClickListener(actionListener);
	}
	
	private View.OnClickListener actionListener=new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.iv_action_left:
				finish();
				break;
			case R.id.iv_action_right:
				commitFeedBackInbackgound();
				break;
			case R.id.layout_edittext:
				//setEditTextKeyBoard(ev_feedback,layout_main);
				InputMethodManager inputManager = (InputMethodManager)ev_feedback.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ev_feedback, 0);
				break;
			}
		}
	};
	
	
	private void commitFeedBackInbackgound(){
		if(ev_feedback.getText().toString().trim().equals("")){
			ToastUtil.show(getApplicationContext(), "请填写反馈内容", 2000);
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {//http://112.74.18.34:80/wc/advice&advice=aaa
				String advice=ev_feedback.getText().toString().trim();
				String token=PrefrenceConfig.getUserMessage(FeedBackActivity.this).getUserToken();
				String host=WEBInterface.FEEDBACK_URL;
				HashMap<String,String> params=new HashMap<String, String>();
				params.put("advice", advice);
				String response=ApacheHttpUtil.httpPost(host+"&advice="+advice, token, null);
				Log.d("ycxk", response+"");
				if(response.contains("0")){
					Message msg=new Message();
					msg.what=FEEDBACK_SUCCESS;
					mHandler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==FEEDBACK_SUCCESS){
				ToastUtil.show(getApplicationContext(), "您的好贵意见已经提交成功", 2000);
				Timer timer=new Timer();
				timer.schedule(new TimerTask(){
					@Override
					public void run() {
						FeedBackActivity.this.finish();
					}}, 2000);
			}
		};
	};
	
	
	/**
	 * EditText支持手动输入的监听方法
	 * @param ev_target
	 *        要监听的EditText
	 * @param layout
	 *        ev_target所在的布局中的最外侧layout(LinearLayout,RelayoutLayout,FrameLayout)
	 * 
	 * */
	public void setEditTextKeyBoard(final EditText ev_target,final View layout){
		//ev_target.setInputType(InputType.TYPE_CLASS_PHONE);
		ev_target.setFocusableInTouchMode(true);
		ev_target.requestFocus();
		InputMethodManager inputManager = (InputMethodManager)ev_target.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(ev_target, 0);
		layout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener(){
					@Override
					public void onGlobalLayout()
					{
						int heightDiff = layout.getRootView().getHeight() - layout.getHeight();
						if (heightDiff > 100)
						{ // 说明键盘是弹出状态
							//ToastUtil.show(getApplicationContext(), "弹出", 2000);
						} else{
							ev_target.setInputType(InputType.TYPE_NULL);
							ev_target.setFocusableInTouchMode(true);
							//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
						}
					}
				});
	}

}
