package com.brother.yckx.control.activity.owner;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.yckx.R;

public class PersonalEditphoneActivity extends BaseActivity{
	
	private LinearLayout layout_return;
	private TextView btncompelete;
	private EditText ev_companyIntroduce;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_setphone);
		init();
	}
	
	private void init(){
		layout_return=(LinearLayout) findViewById(R.id.layout_return);
		layout_return.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				PersonalEditphoneActivity.this.finish();
			}});
		btncompelete=(TextView) findViewById(R.id.btncompelete);
		btncompelete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
			}});
		
		ev_companyIntroduce=(EditText) findViewById(R.id.ev_companyIntroduce);
	}

}
