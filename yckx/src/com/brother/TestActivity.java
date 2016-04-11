package com.brother;

import com.brother.yckx.R;
import com.brother.yckx.view.DateTimePickDialogUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class TestActivity extends BaseActivity{
	
	private EditText ev_test;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		ev_test=(EditText) findViewById(R.id.ev_test);
		ev_test.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String initDateTime="2013Äê9ÔÂ3ÈÕ 14:44";
				DateTimePickDialogUtil dateTimePick=new DateTimePickDialogUtil(TestActivity.this, initDateTime);
				dateTimePick.dateTimePicKDialog(ev_test);
			}});
	}

}
