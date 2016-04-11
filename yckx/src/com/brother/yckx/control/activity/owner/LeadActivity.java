package com.brother.yckx.control.activity.owner;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.brother.BaseActivity;
import com.brother.utils.PrefrenceConfig;
import com.brother.yckx.R;
import com.brother.yckx.adapter.activity.LeadAdapter;
import com.brother.yckx.control.activity.washer.WasherMainActivity;
import com.brother.yckx.model.UserEntity;

public class LeadActivity extends BaseActivity{
	private ViewPager viewPager;
	private LeadAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lead);
        
		viewPager=(ViewPager) findViewById(R.id.viewpager);
		viewPager.setOnPageChangeListener(pageChangeListener);
		adapter=new LeadAdapter();
		viewPager.setAdapter(adapter);
		initViewPager();
	}

	private void controller(){
		//判断是从哪个Activity进入，从主界面进入重新使用状态
		Intent mIntent=getIntent();
		String fromActivity=mIntent.getStringExtra("FromActivity");
		if(fromActivity.equals("MainAactivity")){
			//重写用户使用状态
			//PrefrenceConfig.setUseState(this, false);
			/*Intent intent=new Intent(this,HomeActivity.class);
			startActivity(intent);
			finish();*/
		}
		if(fromActivity.equals("SettingActivity")){
            
		}
	}
	
	private void initViewPager(){
		ImageView imageView = null;
		imageView = (ImageView) getLayoutInflater().inflate(R.layout.inflate_lead_icon, null);
		imageView.setImageResource(R.drawable.lead1);
		imageView.setScaleType(ScaleType.FIT_XY);
		adapter.addViewToAdapter(imageView);
		
		imageView = (ImageView) getLayoutInflater().inflate(R.layout.inflate_lead_icon, null);
		imageView.setImageResource(R.drawable.lead1);
		imageView.setScaleType(ScaleType.FIT_XY);
		adapter.addViewToAdapter(imageView);
		
		imageView = (ImageView) getLayoutInflater().inflate(R.layout.inflate_lead_icon, null);
		imageView.setImageResource(R.drawable.lead1);
		imageView.setScaleType(ScaleType.FIT_XY);
		adapter.addViewToAdapter(imageView);
		
		adapter.notifyDataSetChanged();
	}
	
	
	private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			// 更新下标图标
			if(arg0>=2){
				Timer timer=new Timer();
				timer.schedule(new TimerTask(){

					@Override
					public void run() {
						//首次肯定是普通用户(测试使用)
						UserEntity user=PrefrenceConfig.getUserMessage(LeadActivity.this);
						if(user.getUserRole().equals("washer")){
							Intent intent=new Intent(LeadActivity.this,WasherMainActivity.class);
							startActivity(intent);
							LeadActivity.this.finish();
							return;
						}
						Intent intent=new Intent(LeadActivity.this,HomeActivity.class);
						startActivity(intent);
						LeadActivity.this.finish();
					}}, 3000);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

}
