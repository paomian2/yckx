package com.brother.yckx.control.activity.owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.yckx.R;
import com.brother.yckx.view.CircleImageView;

public class PersonalBusinessManagerActivity extends BaseActivity{
	
	private LinearLayout layout_return;
	private TextView btncompelete;
	private RelativeLayout layout_addImage;
	private TextView tv_Imgloaded;
	private LinearLayout layout_wallet,layout_count,layout_QRcode,layout_RedMoney,layout_Push;
	private RelativeLayout layout_setBusinessTime;
	private LinearLayout layout_circleMembers,layout_settingAdress,layout_settingPhone;
	private LinearLayout layout_addProducts;
	private LinearLayout layout_addMembers;
	private LinearLayout layout_addBusinessIntroducet;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_business_manager);
		initview();
	}
	private void initview() {
		//actionbar
		layout_return=(LinearLayout) findViewById(R.id.layout_return);
		btncompelete=(TextView) findViewById(R.id.btncompelete);
		layout_return.setOnClickListener(listener);
		btncompelete.setOnClickListener(listener);
		//item1
		layout_addImage=(RelativeLayout) findViewById(R.id.layout_addImage);
		tv_Imgloaded=(TextView) findViewById(R.id.tv_Imgloaded);
		layout_addImage.setOnClickListener(listener);
		tv_Imgloaded.setOnClickListener(listener);
		//item2
		layout_wallet=(LinearLayout) findViewById(R.id.layout_wallet);
		layout_count=(LinearLayout) findViewById(R.id.layout_count);
		layout_QRcode=(LinearLayout) findViewById(R.id.layout_QRcode);
		layout_RedMoney=(LinearLayout) findViewById(R.id.layout_RedMoney);
		layout_Push=(LinearLayout) findViewById(R.id.layout_Push);
		//item3
		layout_setBusinessTime=(RelativeLayout) findViewById(R.id.layout_setBusinessTime);
		layout_circleMembers=(LinearLayout) findViewById(R.id.layout_circleMembers);
		layout_settingAdress=(LinearLayout) findViewById(R.id.layout_settingAdress);
		layout_settingPhone=(LinearLayout) findViewById(R.id.layout_settingPhone);
		layout_setBusinessTime.setOnClickListener(listener);
		layout_circleMembers.setOnClickListener(listener);
		layout_settingAdress.setOnClickListener(listener);
		layout_settingPhone.setOnClickListener(listener);
		//item4
		layout_addProducts=(LinearLayout) findViewById(R.id.layout_addProducts);
		layout_addProducts.setOnClickListener(listener);
		
		//item6
		layout_addMembers=(LinearLayout) findViewById(R.id.layout_addMembers);
		layout_addMembers.setOnClickListener(listener);
		//item7
		layout_addBusinessIntroducet=(LinearLayout) findViewById(R.id.layout_addBusinessIntroducet);
		layout_addBusinessIntroducet.setOnClickListener(listener);
		
	}
	
	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.layout_return:
				PersonalBusinessManagerActivity.this.finish();
				break;
			case R.id.btncompelete://完成
				break;
			case R.id.layout_addImage://添加图片
				break;
			case R.id.tv_Imgloaded:
				Intent intent=new Intent(PersonalBusinessManagerActivity.this,PersonalLoadImgActivity.class);
				startActivity(intent);
				break;
			//item3
			case R.id.layout_wallet://钱包
				break;
			case R.id.layout_count://统计
				break;
			case R.id.layout_QRcode://核销码
				break;
			case R.id.layout_RedMoney://红包
				break;
			//item4
			case R.id.layout_setBusinessTime:
				Intent intentSetTime=new Intent(PersonalBusinessManagerActivity.this,PersonalBusinessHoursActivity.class);
				startActivity(intentSetTime);
				break;
			case R.id.layout_circleMembers://会员圈
				Intent intentCircleMembers=new Intent(PersonalBusinessManagerActivity.this,PersonalBusinessHoursActivity.class);
				startActivity(intentCircleMembers);
				break;
			case R.id.layout_settingAdress://设置位置
				Intent intentSetAdress=new Intent(PersonalBusinessManagerActivity.this,PersonalEditAdressActivity.class);
				startActivity(intentSetAdress);
				break;
			case R.id.layout_settingPhone://设置电话
				Intent intentSetPhone=new Intent(PersonalBusinessManagerActivity.this,PersonalEditphoneActivity.class);
				startActivity(intentSetPhone);
				break;
			//item4
			case R.id.layout_addProducts:
				Intent intentAddProducts=new Intent(PersonalBusinessManagerActivity.this,PersonalAddProductsActivity.class);
				startActivity(intentAddProducts);
				break;
			//item6
			case R.id.layout_addMembers://添加员工
				Intent intentaddMembers=new Intent(PersonalBusinessManagerActivity.this,PersonalAddBusinessMemberActivity.class);
				startActivity(intentaddMembers);
				break;
			//item7
			case R.id.layout_addBusinessIntroducet://添加公司介绍
				Intent intentBusinessIntroducet=new Intent(PersonalBusinessManagerActivity.this,PersonalEditCompanyIntroduceActivity.class);
				startActivity(intentBusinessIntroducet);
				break;
			}
		}
	};

}
