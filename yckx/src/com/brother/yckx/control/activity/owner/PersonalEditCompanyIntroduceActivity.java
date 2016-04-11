package com.brother.yckx.control.activity.owner;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.yckx.R;

public class PersonalEditCompanyIntroduceActivity extends BaseActivity{
	
	private LinearLayout layout_return;
	private TextView btncompelete;
	private EditText ev_phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_setcompany_introduce);
		init();
	}
	
	private void init(){
		layout_return=(LinearLayout) findViewById(R.id.layout_return);
		layout_return.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				PersonalEditCompanyIntroduceActivity.this.finish();
			}});
		btncompelete=(TextView) findViewById(R.id.btncompelete);
		btncompelete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
			}});
		
		ev_phone=(EditText) findViewById(R.id.ev_phone);
	}

}
