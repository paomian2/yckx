package com.brother.yckx.control.activity.owner;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.brother.yckx.action.ActionUtils;
import com.brother.yckx.action.ActionUtils.OpenkeyboardCloseStatusListener;
import com.brother.yckx.model.BusinessEntity;
/**
 * 现场付
 * 
 * */
public class PayOnSpotActivity extends BaseActivity{
	private BusinessEntity businessEntity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payonspot);
		businessEntity=(BusinessEntity) getIntent().getSerializableExtra("BusinessEntity");
		initActionBar();
		initView();
	}
	
	
	private void initActionBar(){
		findViewById(R.id.iv_action_left).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				PayOnSpotActivity.this.finish();
			}});
		TextView tv_actionbar_title=(TextView) findViewById(R.id.tv_actionbar_title);
		tv_actionbar_title.setText(businessEntity.getCompanyName());
	}
	
	private LinearLayout layout_main;
	private EditText ev_price;
	private TextView tv_sale;
	private TextView tv_ActualPaymentAmount;
	private Button btn_payConfirm;
	private TextView tv_BigClienConnect;
	private void initView(){
		layout_main=(LinearLayout) findViewById(R.id.layout_main);
		ev_price=(EditText) findViewById(R.id.ev_price);
		ev_price.setInputType(InputType.TYPE_NULL);//不可直接输入
		ActionUtils actionUtils=new ActionUtils();
		//监听软键盘，软键盘关闭后计算打折后的值
		actionUtils.setOpenkeyboardCloseStatusListener(new OpenkeyboardCloseStatusListener(){
			@Override
			public void onOpenkeyboardCloseStatusListener() {
				try {
					if(tv_ActualPaymentAmount!=null){
		          		Integer IPrice=Integer.parseInt(ev_price.getText().toString().trim());
		          		Double InSale=Double.parseDouble(tv_sale.getText().toString().trim());
		          		double Fsale=InSale/10;
		          		double FactualPaymentAmount=IPrice*Fsale;
		          		tv_ActualPaymentAmount.setText("￥"+FactualPaymentAmount);
		          	 }   
				} catch (Exception e) {
					tv_ActualPaymentAmount.setText("￥0 ");
					Log.d("yckx","计算出错");
				} 
			}});
		actionUtils.openkeyboard2(ev_price, ev_price, layout_main);
		//
		tv_sale=(TextView) findViewById(R.id.tv_sale);
		tv_ActualPaymentAmount=(TextView) findViewById(R.id.tv_ActualPaymentAmount);
		//确认支付
		btn_payConfirm=(Button) findViewById(R.id.btn_payConfirm);
		btn_payConfirm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String actualPaymentAmount=tv_ActualPaymentAmount.getText().toString();
				ToastUtil.show(getApplicationContext(), ""+actualPaymentAmount, 3000);
			}});
		tv_BigClienConnect=(TextView) findViewById(R.id.tv_BigClienConnect);
		tv_BigClienConnect.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				RelativeLayout layout_BigImg1=(RelativeLayout) findViewById(R.id.layout_BigImg1);
				layout_BigImg1.setVisibility(View.GONE);
				LinearLayout layout_BigImg2=(LinearLayout) findViewById(R.id.layout_BigImg2);
				layout_BigImg2.setVisibility(View.VISIBLE);
				
			}});
	}
}
