package com.brother.yckx.control.activity.owner;

import android.os.Bundle;
import android.view.Window;

import com.brother.BaseActivity;
import com.brother.yckx.R;

public class AgreementActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_agreement);
	}

}
