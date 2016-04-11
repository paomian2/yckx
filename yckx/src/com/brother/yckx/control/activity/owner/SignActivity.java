package com.brother.yckx.control.activity.owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.brother.BaseActivity;
import com.brother.yckx.R;

public class SignActivity extends BaseActivity{
	
	private EditText ev_sign;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_edit_sign);
		ev_sign=(EditText) findViewById(R.id.ev_sign);
		findViewById(R.id.layout_return).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}});
		findViewById(R.id.btncompelete).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String signStr=ev_sign.getText().toString().trim();
				Intent intent=new Intent(SignActivity.this,PersonalEditActivity.class);
				intent.putExtra("sign", signStr);
				SignActivity.this.setResult(4, intent);
				SignActivity.this.finish();
			}});
	}

}
