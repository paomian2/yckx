package com.brother.yckx.control.activity.owner;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.BaseFragment.FragmentItemOnClickListener;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.control.frgament.MoreFragment_FormDetail;
import com.brother.yckx.model.OrderDetailEntity;
import com.shizhefei.view.indicator.FragmentListPageAdapter;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

public class FormDetailActivity extends BaseActivity implements FragmentItemOnClickListener{
	private IndicatorViewPager indicatorViewPager;
	private LayoutInflater inflate;
	private ViewPager viewPager;
	private ScrollIndicatorView  tabs;
	private String[] names = { "订单内容", "服务详情", "美护师"};
	private ProgressBar progressBar;
	private LinearLayout layout_show;
	private String orderId;
	/**加载订单数据完成*/
	private final int LOADORDERD_SUCCESS=0;
	//设置监听用户与fragment之间进行通信
	private RefreshOnclickListner refreshOnclickListener;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formdetail);
		setActionBar(R.string.formdetail_title, R.drawable.btn_homeasup_default, R.drawable.icon_default_refresh, actionListener);
		viewPager=(ViewPager) findViewById(R.id.viewPager);
		tabs=(ScrollIndicatorView) findViewById(R.id.tabs_viewPager);
		progressBar=(ProgressBar) findViewById(R.id.progressBar);
		layout_show=(LinearLayout) findViewById(R.id.layout_show);
		tabs.setScrollBar(new ColorBar(this, Color.RED, 5));
		orderId=GlobalServiceUtils.getGloubalServiceSession("orderId")+"";
		Log.d("yckx", orderId+"");
		getOrderDetailData(orderId);
	}


	private int size =3;
	private class MyAdapter extends IndicatorFragmentPagerAdapter {

		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return size;
		}

		@Override
		public View getViewForTab(int position, View convertView, ViewGroup container) {
			if (convertView == null) {
				convertView = inflate.inflate(R.layout.tab_top, container, false);
			}
			TextView textView = (TextView) convertView;
			textView.setText(names[position % names.length]);
			textView.setPadding(20, 0, 20, 0);
			return convertView;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			MoreFragment_FormDetail fragment = new MoreFragment_FormDetail();
			Bundle bundle = new Bundle();
			bundle.putInt(MoreFragment_FormDetail.INTENT_INT_INDEX, position);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			return FragmentListPageAdapter.POSITION_NONE;
		}

	};

	@Override
	public void onFragmentItemClick(String tag, int eventTagID, Object data) {
		// TODO Auto-generated method stub

	}

	private View.OnClickListener actionListener=new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if(arg0.getId()==R.id.iv_action_left){
				FormDetailActivity.this.finish();
			}
			if(arg0.getId()==R.id.iv_action_right){
				try {
					refreshOnclickListener.onRefreshclick("刷新完订单进度的数据了");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	};


	/**获取订单数据*/
	private void getOrderDetailData(final String orderId){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url=WEBInterface.ORDERDETAIL_URL+orderId;
				String token=PrefrenceConfig.getUserMessage(FormDetailActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpGet(url, token, null);
				Log.d("yckx", respose+"");
				if(respose!=null){
					try {
						OrderDetailEntity entity=ParseJSONUtils.getOrderDtailEntity(respose);
						GlobalServiceUtils.getInstance().setGloubalServiceOrderDetailEntity(entity);
						Message msg=new Message();
						msg.what=LOADORDERD_SUCCESS;
						mHandler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Log.d("yckx", "订单详情界面数据"+respose);
					//解析数据
				}
			}
		}).start();
	}


	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==LOADORDERD_SUCCESS)
				progressBar.setVisibility(View.GONE);
			layout_show.setVisibility(View.VISIBLE);
			showFragmentLayout();
		};
	};

	private void showFragmentLayout(){
		int selectColorId = R.color.tab_top_text_2;
		int unSelectColorId = R.color.tab_top_text_1;
		tabs.setOnTransitionListener(new OnTransitionTextListener().setColorId(this, selectColorId, unSelectColorId));
		viewPager.setOffscreenPageLimit(2);
		indicatorViewPager = new IndicatorViewPager(tabs, viewPager);
		inflate = LayoutInflater.from(getApplicationContext());
		indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
	}

	/**一个用于跟fragment进行通信的接口*/
	public  interface RefreshOnclickListner{
		public void onRefreshclick(String s);
	}
	
	/**设置监听器的方法*/
	public void setRefreseOnclickListner(RefreshOnclickListner refreshOnclickListener){
		this.refreshOnclickListener=refreshOnclickListener;
	}
	
	
	private  Dialog exitDialog;
	/**提示用户是否回到主界面*/
	private void showRemindDialog(){
		LayoutInflater inflater=FormDetailActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_formdetail_activity, null);
		exitDialog = new AlertDialog.Builder(FormDetailActivity.this).create();
		exitDialog.show();
		exitDialog.setContentView(view);
		Window dialogWindow = exitDialog.getWindow(); 
		WindowManager.LayoutParams params = dialogWindow.getAttributes();  
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		WindowManager m =getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		params.width = (int) ((d.getWidth()) * 0.8);
		params.height = params.height;
		//偏移量
		exitDialog.getWindow().setAttributes(params);
		TextView exitOK=(TextView) view.findViewById(R.id.exist);
		TextView cancel=(TextView) view.findViewById(R.id.cancel);
		exitOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(FormDetailActivity.this,HomeActivity.class);
				startActivity(intent);
				FormDetailActivity.this.finish();
			}});
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				exitDialog.dismiss();
				return;

			}});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		String action=GlobalServiceUtils.getGloubalServiceSession("FormDetailActivity")+"";
		if(keyCode==KeyEvent.KEYCODE_BACK){//	
			if(action.equals("FormActivity")){
				FormDetailActivity.this.finish();
			}else if(action.equals("NoticeListActivity")){
				FormDetailActivity.this.finish();
			}else{
				showRemindDialog();
			}
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		GlobalServiceUtils.setGloubalServiceSession("FormDetailActivity","");
		GlobalServiceUtils.setGloubalServiceSession("orderId","");
	}

}
