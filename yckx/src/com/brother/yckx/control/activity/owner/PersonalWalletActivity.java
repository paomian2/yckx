package com.brother.yckx.control.activity.owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.yckx.R;

public class PersonalWalletActivity extends BaseActivity{

	private LinearLayout layout_return;
	private LinearLayout layout_banlance;
	private TextView tv_balance;
	private RelativeLayout layout_Recharge,layout_Tixian;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_wallet);
		init();

	}

	private void init(){
		layout_return=(LinearLayout) findViewById(R.id.layout_return);
		layout_banlance=(LinearLayout) findViewById(R.id.layout_banlance);
		tv_balance=(TextView) findViewById(R.id.tv_balance);
		layout_Recharge=(RelativeLayout) findViewById(R.id.layout_Recharge);
		layout_Tixian=(RelativeLayout) findViewById(R.id.layout_Tixian);

		layout_return.setOnClickListener(listener);
		layout_banlance.setOnClickListener(listener);
		layout_Recharge.setOnClickListener(listener);
		layout_Tixian.setOnClickListener(listener);
	}

	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.layout_return:
				PersonalWalletActivity.this.finish();
				break;
			case R.id.layout_banlance:
				Intent intentBanlance=new Intent(PersonalWalletActivity.this,PersonalBalanceDetailActivity.class);
				startActivity(intentBanlance);
				break;
			case R.id.layout_Recharge:
				Intent intentRecharge=new Intent(PersonalWalletActivity.this,PersonalRechargeActivity.class);
				startActivity(intentRecharge);

				break;
			case R.id.layout_Tixian:
				Intent intentTixian=new Intent(PersonalWalletActivity.this,PersonalTakeCashActivity.class);
				startActivity(intentTixian);
				break;
			}
		}
	};
}
