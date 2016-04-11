package com.brother.yckx.control.activity.owner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.brother.yckx.view.CircleImageView;

public class PersonalAddBusinessMemberActivity extends BaseActivity{
	private LinearLayout layout_return;
	private TextView btncompelete;
	//
	private LinearLayout layout_inputPhone;
	private EditText ev_phone;
	private TextView btnSearch;
	//所搜到的人
	private RelativeLayout layout_showSearchResult;
	private CircleImageView cv_addmemberImg;
	private TextView tv_addmemberName,btnAdd;
	//设置权限
	private LinearLayout layout_setMemberJob;
	private EditText ev_setJob;
	private LinearLayout layout_setLimite;
	//权限
	private RelativeLayout layout_push_product,layout_managerCircleMember,layout_addAndrEditMember,layout_addCompanyImage,
	layout_addCompanyIntroduce,layout_addEditAdress,layout_addEditPhone,layout_addEditBusinessHours,layout_showImageInBusinessPage
	,layout_lookBusinessCount,layout_lookBusinessIncome;
	
	private ImageView img_push_product,img_managerCircleMember,img_addAndrEditMember,img_addCompanyImage,img_addCompanyIntroduce
	,img_addEditAdress,img_addEditPhone,img_addEditBusinessHours,img_showImageInBusinessPage,img_lookBusinessCount,img_lookBusinessIncome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_business_addmember);
		init();
	}
	
	private void init(){
		//actionbar
		layout_return=(LinearLayout) findViewById(R.id.layout_return);
		btncompelete=(TextView) findViewById(R.id.btncompelete);
		layout_return.setOnClickListener(listener);
		btncompelete.setOnClickListener(listener);
		//search
		layout_inputPhone=(LinearLayout) findViewById(R.id.layout_inputPhone);
		ev_phone=(EditText) findViewById(R.id.ev_phone);
		btnSearch=(TextView) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(listener);
		//search2
		layout_showSearchResult=(RelativeLayout) findViewById(R.id.layout_showSearchResult);
		cv_addmemberImg=(CircleImageView) findViewById(R.id.cv_addmemberImg);
		tv_addmemberName=(TextView) findViewById(R.id.tv_addmemberName);
		btnAdd=(TextView) findViewById(R.id.btnAdd);
		//测试阶段使用flag标记
		btnAdd.setTag(0);
		btnAdd.setOnClickListener(listener);
		//set limite
		layout_setMemberJob=(LinearLayout) findViewById(R.id.layout_setMemberJob);
		ev_setJob=(EditText) findViewById(R.id.ev_setJob);

		layout_setLimite=(LinearLayout) findViewById(R.id.layout_setLimite);
		img_push_product=(ImageView) findViewById(R.id.img_push_product);
		img_managerCircleMember=(ImageView) findViewById(R.id.img_managerCircleMember);
		img_addAndrEditMember=(ImageView) findViewById(R.id.img_addAndrEditMember);
		img_addCompanyImage=(ImageView) findViewById(R.id.img_addCompanyImage);
		img_addCompanyIntroduce=(ImageView) findViewById(R.id.img_addCompanyIntroduce);
		img_addEditAdress=(ImageView) findViewById(R.id.img_addEditAdress);
		img_addEditPhone=(ImageView) findViewById(R.id.img_addEditPhone);
		img_addEditBusinessHours=(ImageView) findViewById(R.id.img_addEditBusinessHours);
		img_showImageInBusinessPage=(ImageView) findViewById(R.id.img_showImageInBusinessPage);
		img_lookBusinessCount=(ImageView) findViewById(R.id.img_lookBusinessCount);
		img_lookBusinessIncome=(ImageView) findViewById(R.id.img_lookBusinessIncome);
		
		img_push_product.setTag(0);
		img_managerCircleMember.setTag(0);
		img_addAndrEditMember.setTag(0);
		img_addCompanyImage.setTag(0);
		img_addCompanyIntroduce.setTag(0);
		img_addEditAdress.setTag(0);
		img_addEditBusinessHours.setTag(0);
		img_showImageInBusinessPage.setTag(0);
		img_lookBusinessCount.setTag(0);
		img_lookBusinessIncome.setTag(0);
		img_push_product.setOnClickListener(new SwitchViewListener(img_push_product));
		img_managerCircleMember.setOnClickListener(new SwitchViewListener(img_managerCircleMember));
		img_addAndrEditMember.setOnClickListener(new SwitchViewListener(img_addAndrEditMember));
		img_addCompanyImage.setOnClickListener(new SwitchViewListener(img_addCompanyImage));
		img_addCompanyIntroduce.setOnClickListener(new SwitchViewListener(img_addCompanyIntroduce));
		img_addEditAdress.setOnClickListener(new SwitchViewListener(img_addEditAdress));
		img_addEditBusinessHours.setOnClickListener(new SwitchViewListener(img_addEditBusinessHours));
		img_showImageInBusinessPage.setOnClickListener(new SwitchViewListener(img_showImageInBusinessPage));
		img_lookBusinessCount.setOnClickListener(new SwitchViewListener(img_lookBusinessCount));
		img_lookBusinessIncome.setOnClickListener(new SwitchViewListener(img_lookBusinessIncome));
	}
	
	private View.OnClickListener listener=new View.OnClickListener() {
		@SuppressLint("ResourceAsColor")
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.layout_return:
				PersonalAddBusinessMemberActivity.this.finish();
				break;
			case R.id.btnSearch://搜索
				layout_showSearchResult.setVisibility(View.VISIBLE);
				break;
			case R.id.btnAdd:
				int flag=(Integer) btnAdd.getTag();
				Log.d("yckx", "btnAdd"+flag);
				if(flag==0){
					layout_setMemberJob.setVisibility(View.VISIBLE);
					layout_setLimite.setVisibility(View.VISIBLE);
					layout_inputPhone.setVisibility(View.GONE);
					btnAdd.setTag(1);
					btnAdd.setText("取消");
					btnAdd.setBackgroundResource(R.drawable.icon_defalut_green);
				}else if(flag==1){
					layout_setMemberJob.setVisibility(View.GONE);
					layout_setLimite.setVisibility(View.GONE);
					layout_showSearchResult.setVisibility(View.GONE);
					layout_inputPhone.setVisibility(View.VISIBLE);
					btnAdd.setTag(0);
					btnAdd.setText("添加");
					btnAdd.setBackgroundResource(R.drawable.icon_defalut_red);
				}
				
				break;
			}
		}
	};
	
	
	
	class SwitchViewListener implements View.OnClickListener{

		private ImageView switchImageView;
		public SwitchViewListener(ImageView switchImageView){
			this.switchImageView=switchImageView;
		}
		@Override
		public void onClick(View view) {
			int flag=(Integer) switchImageView.getTag();
			Log.d("yckx", "SwitchViewListener:"+flag);
			if(flag==0){
				ToastUtil.show(getApplicationContext(), "转换到:"+flag, 2000);
				switchImageView.setTag(1);
			}else if(flag==1){
				switchImageView.setTag(0);
				ToastUtil.show(getApplicationContext(), "转换到:"+flag, 2000);
			}
		}
		
	}

}
