package com.brother.yckx.control.activity.owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.brother.BaseActivity;
import com.brother.yckx.R;

public class EditNicknameActivity extends BaseActivity{
	
	private EditText nickeName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_edit_set_snackname);
		nickeName=(EditText) findViewById(R.id.ev_nickName);
		findViewById(R.id.layout_return).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}});
		findViewById(R.id.btncompelete).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String nickNameStr=nickeName.getText().toString().trim();
				Intent intent=new Intent(EditNicknameActivity.this,PersonalEditActivity.class);
				intent.putExtra("nickName", nickNameStr);
				EditNicknameActivity.this.setResult(3, intent);
				EditNicknameActivity.this.finish();
			}});
	}

}
