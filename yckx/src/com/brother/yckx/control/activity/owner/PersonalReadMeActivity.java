package com.brother.yckx.control.activity.owner;

import android.os.Bundle;

import com.brother.BaseActivity;
import com.brother.yckx.R;
import com.brother.yckx.view.LoadListView;

public class PersonalReadMeActivity extends BaseActivity{
	
	private LoadListView loadlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_readme);
		loadlist=(LoadListView) findViewById(R.id.loadlist);
	}
	
  
}
