package com.brother.yckx.control.activity.owner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.utils.PrefrenceConfig;
import com.brother.yckx.R;
import com.brother.yckx.model.OrderRegisterEntity;
public class RegisterCompanyActivity extends BaseActivity{
	private LinearLayout layout_main;
	private LinearLayout layout_return;
	private TextView tv_next;
	private EditText ev_companyName,ev_companyAdress,ev_companyPhone;
	private LinearLayout layout_location;
	private Spinner sp_vocation;
	private EditText ev_serviceMain;
	private EditText ev_principal,ev_principalPhone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_company);
		init();

	}
	private void init(){
		//Ev的点击弹出软键盘
		layout_main=(LinearLayout) findViewById(R.id.layout_main);
		//actionb
		layout_return=(LinearLayout) findViewById(R.id.layout_return);
		tv_next=(TextView) findViewById(R.id.tv_next);
		layout_return.setOnClickListener(listener);
		tv_next.setOnClickListener(listener);
		//item1
		ev_companyName=(EditText) findViewById(R.id.ev_companyName);
		ev_companyName.setInputType(InputType.TYPE_NULL);
		ev_companyName.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ev_companyName.setInputType(InputType.TYPE_CLASS_TEXT);
				ev_companyName.setFocusableInTouchMode(true);
				ev_companyName.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)ev_companyName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ev_companyName, 0);
				layout_main.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = layout_main.getRootView().getHeight() - layout_main.getHeight();
								if (heightDiff > 100)
								{ // 说明键盘是弹出状态
									//ToastUtil.show(getApplicationContext(), "弹出", 2000);
								} else{
									ev_companyName.setInputType(InputType.TYPE_NULL);
									ev_companyName.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
								}
							}
						});
			}});
		ev_companyAdress=(EditText) findViewById(R.id.ev_companyAdress);
		ev_companyAdress.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ev_companyAdress.setInputType(InputType.TYPE_CLASS_TEXT);
				ev_companyAdress.setFocusableInTouchMode(true);
				ev_companyAdress.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)ev_companyAdress.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ev_companyAdress, 0);
				layout_main.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = layout_main.getRootView().getHeight() - layout_main.getHeight();
								if (heightDiff > 100)
								{ // 说明键盘是弹出状态
									//ToastUtil.show(getApplicationContext(), "弹出", 2000);
								} else{
									ev_companyAdress.setInputType(InputType.TYPE_NULL);
									ev_companyAdress.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
								}
							}
						});
			}});
		ev_companyPhone=(EditText) findViewById(R.id.ev_companyPhone);
		ev_companyPhone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ev_companyPhone.setInputType(InputType.TYPE_CLASS_PHONE);
				ev_companyPhone.setFocusableInTouchMode(true);
				ev_companyPhone.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)ev_companyPhone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ev_companyPhone, 0);
				layout_main.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = layout_main.getRootView().getHeight() - layout_main.getHeight();
								if (heightDiff > 100)
								{ // 说明键盘是弹出状态
									//ToastUtil.show(getApplicationContext(), "弹出", 2000);
								} else{
									ev_companyPhone.setInputType(InputType.TYPE_NULL);
									ev_companyPhone.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
								}
							}
						});
			}});
		
		layout_location=(LinearLayout) findViewById(R.id.layout_location);
		layout_location.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(RegisterCompanyActivity.this,LBSLatLngLocationActivity.class);
				startActivityForResult(intent, 3);
			}});

		//item2
		sp_vocation=(Spinner) findViewById(R.id.sp_vocation);
		ev_serviceMain=(EditText) findViewById(R.id.ev_serviceMain);
		ev_serviceMain.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ev_serviceMain.setInputType(InputType.TYPE_CLASS_TEXT);
				ev_serviceMain.setFocusableInTouchMode(true);
				ev_serviceMain.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)ev_serviceMain.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ev_serviceMain, 0);
				layout_main.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = layout_main.getRootView().getHeight() - layout_main.getHeight();
								if (heightDiff > 100)
								{ // 说明键盘是弹出状态
									//ToastUtil.show(getApplicationContext(), "弹出", 2000);
								} else{
									ev_serviceMain.setInputType(InputType.TYPE_NULL);
									ev_serviceMain.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
								}
							}
						});
			}});
		//item3
		ev_principal=(EditText) findViewById(R.id.ev_principal);
		ev_principal.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ev_principal.setInputType(InputType.TYPE_CLASS_TEXT);
				ev_principal.setFocusableInTouchMode(true);
				ev_principal.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)ev_principal.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ev_principal, 0);
				layout_main.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = layout_main.getRootView().getHeight() - layout_main.getHeight();
								if (heightDiff > 100)
								{ // 说明键盘是弹出状态
									//ToastUtil.show(getApplicationContext(), "弹出", 2000);
								} else{
									ev_principal.setInputType(InputType.TYPE_NULL);
									ev_principal.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
								}
							}
						});
			}});
		ev_principalPhone=(EditText) findViewById(R.id.ev_principalPhone);
		ev_principalPhone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ev_principalPhone.setInputType(InputType.TYPE_CLASS_PHONE);
				ev_principalPhone.setFocusableInTouchMode(true);
				ev_principalPhone.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)ev_principalPhone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ev_principalPhone, 0);
				layout_main.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = layout_main.getRootView().getHeight() - layout_main.getHeight();
								if (heightDiff > 100)
								{ // 说明键盘是弹出状态
									//ToastUtil.show(getApplicationContext(), "弹出", 2000);
								} else{
									ev_principalPhone.setInputType(InputType.TYPE_NULL);
									ev_principalPhone.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
								}
							}
						});
			}});
	}

	/**提交订单*/
	private void SubmitOrder(){
		//item1
		String companyName=ev_companyName.getText().toString().trim();
		String companyAdress=ev_companyAdress.getText().toString().trim();
		String companyPone=ev_companyPhone.getText().toString().trim();
		//item2
		String companyHangye=sp_vocation.getSelectedItem().toString();
		String companyServiceMain=ev_serviceMain.getText().toString().trim();
		//item3
		String principal=ev_principal.getText().toString().trim();
		String principalPhone=ev_principalPhone.getText().toString().trim();
		//将数据提交服务器、或保存到本地
		OrderRegisterEntity orderEntity=new OrderRegisterEntity(companyName, companyAdress, companyPone, companyHangye, companyServiceMain, principal, principalPhone);
		PrefrenceConfig.setRegisterCompanyMessage(RegisterCompanyActivity.this, orderEntity);
	}

	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.layout_return:
				RegisterCompanyActivity.this.finish();
				break;
			case R.id.tv_next:
				//提交订单
				SubmitOrder();
				Intent intent=new Intent(RegisterCompanyActivity.this,RegisterCompanyActivity2.class);
				startActivity(intent);
				break;
			}

		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  super.onActivityResult(requestCode, resultCode, data);
	      if(resultCode ==3){
	                 /*  Bundle bundle =data.getExtras();
	                   String lbsLatLng = bundle.getString("lbsLatLng");*/
	      }
	};


}
