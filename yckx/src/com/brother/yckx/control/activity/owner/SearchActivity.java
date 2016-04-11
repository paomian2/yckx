package com.brother.yckx.control.activity.owner;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.brother.BaseActivity;
import com.brother.yckx.R;

public class SearchActivity extends BaseActivity{
	private ImageView img_left,img_right;
	private EditText ev_search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		init();
	}
	
	private void init(){
		img_left=(ImageView) findViewById(R.id.img_left);
		img_right=(ImageView) findViewById(R.id.img_right);
		ev_search=(EditText) findViewById(R.id.ev_search);
		
		img_left.setOnClickListener(listener);
		img_right.setOnClickListener(listener);
	}
	
	
	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.img_left:
			    finish();
				break;
			case R.id.img_right:
				//È·¶¨ËÑË÷
				break;
			}
		}
	};

}
