package com.brother.yckx.control.activity.owner;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.yckx.R;

public class CompanyIntroductionActivity extends BaseActivity{
	
	private LinearLayout layout_return;
	private TextView companyName;
	private TextView tv_companyIntroduction;
	private TextView companyPhone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_introduction);
		//返回上一页
		layout_return=(LinearLayout) findViewById(R.id.layout_return);
		layout_return.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}});
		//设置actionbar中公司的名字
		companyName=(TextView) findViewById(R.id.companyName);
		//设置公司简介
		tv_companyIntroduction=(TextView) findViewById(R.id.tv_companyIntroduction);
		String instroduction="雅斯特酒店是荣膺“亚洲魅力酒店”、“中国饭店金马奖百佳酒店”、“中国酒店设计风格大奖酒店”、“消费者满意品牌”、" +
				"“具社会责任感民营企业”等多种社会及行业荣誉的精选商旅连锁酒店集团。酒店采用“雅，亲和，" +
				"轻时尚”的设计风格，以“一段音乐、一杯咖啡、一本书、一条画廊、一份早餐—— 一切为了你心的旅行！”为品牌主张（5A价值环）。" +
				"为你在繁忙的商旅途中，注入更深层次的心情美学，提供释放身心的旅居空间。雅斯特酒店集团致力于精选商旅连锁酒店的开发与经营。" +
				"目前，集团旗下运营、在建及签约酒店已覆盖重庆、两湖两广（如武汉、宜昌、深圳、南宁、桂林、贵港、柳州、玉林、百色）等地区的30多个重点城市，有效会员总数超过100万人。" +
				"未来5年内，以深圳为品牌管理中心、南宁为连锁酒店运营支持中心，铺就“以南宁、桂林、武汉、广州为中心带的3小时高铁经济圈”战略布局：" +
				"自广西南宁出发，东由贵港、玉林、梧州、肇庆、佛山通广州；南经钦州、北海、湛江、珠海、中山到深圳；西取百色、河池、贵阳、重庆、昆明连云贵；" +
				"北上柳州、桂林、永州、邵阳、衡阳、株洲接两湖；在战略区域中，力争2018年酒店数量达到500家以上，以高密度连锁覆盖，完成从大区域品牌向全国连锁品牌的转变，" +
				"缔造中国（中西部）精选商旅连锁酒店第一品牌！";
		tv_companyIntroduction.setText(instroduction);
		//设置公司的名字
		companyPhone=(TextView) findViewById(R.id.companyPhone);
		companyPhone.setText("400-100");
	}

}
