package com.brother.yckx.control.activity.owner;

import java.util.Arrays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.yckx.R;
import com.brother.yckx.view.WheelView;
import com.brother.yckx.view.WheelView.OnWheelPickerListener;

public class PersonalBusinessHoursActivity extends BaseActivity{
	
	private RelativeLayout layout_startTime,layout_endTime;
	private TextView tv_startTime,tv_endTime;
	private LinearLayout layout_return;
	private TextView btncompelete;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_business_hours);
		init();
	}
	private void init() {
		layout_return=(LinearLayout) findViewById(R.id.layout_return);
		btncompelete=(TextView) findViewById(R.id.btncompelete);
		layout_startTime=(RelativeLayout) findViewById(R.id.layout_startTime);
		layout_endTime=(RelativeLayout) findViewById(R.id.layout_endTime);
		tv_startTime=(TextView) findViewById(R.id.tv_startTime);
		tv_endTime=(TextView) findViewById(R.id.tv_endTime);
		
		layout_return.setOnClickListener(listener);
		btncompelete.setOnClickListener(listener);
		layout_startTime.setOnClickListener(listener);
		layout_endTime.setOnClickListener(listener);
	}
	
	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.layout_return:
				PersonalBusinessHoursActivity.this.finish();
				break;
			case R.id.btncompelete://完成
				break;
			case R.id.layout_startTime://开始时间
				showAgeDialog(tv_startTime);
				break;
			case R.id.layout_endTime://结束时间
				showAgeDialog(tv_endTime);
				break;
			}
			
		}
	};
	
	
	//----------------性别  start-----------------------//
    private WheelView wheelview_age;
	private Dialog ageDialog;
	private String statTime="00:00";
	private static final String[] PLANETS_AGE = new String[]{"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00"
		,"09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
	//----代替popu窗口的dialog窗口----
	private void showAgeDialog(final TextView targetTextView){
		LayoutInflater inflater=PersonalBusinessHoursActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_sex_select, null);
		ageDialog = new AlertDialog.Builder(PersonalBusinessHoursActivity.this).create();
		ageDialog.show();
		ageDialog.setContentView(view);
		WindowManager m =getWindowManager();
		Window dialogWindow = ageDialog.getWindow();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams params = ageDialog.getWindow().getAttributes();
		params.width = (int) (d.getWidth());
		params.height = params.height;
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
		//设置bottom的偏移量
		ageDialog.getWindow().setAttributes(params);

		wheelview_age=(WheelView) view.findViewById(R.id.main_wv);
		wheelview_age.setOffset(1);
		wheelview_age.setItems(Arrays.asList(PLANETS_AGE));
		wheelview_age.setOnWheelPickerListener(new OnWheelPickerListener(){
			@Override
			public void wheelSelect(int position, String content) {
				statTime=content;
			}});
		view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ageDialog.dismiss();
			}});
		view.findViewById(R.id.confirm).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				targetTextView.setText(statTime);
				ageDialog.dismiss();
			}});
	}
	//----------------性别  end-----------------------//

}
