package com.brother.yckx.control.activity.washer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.androide.lib3.view.slidingmenu.SlidingMenu;
import com.brother.BaseActivity;
import com.brother.BaseFragment.FragmentItemOnClickListener;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.NumberUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.HistoricalEvaluationActivity;
import com.brother.yckx.control.activity.owner.SettingActivity;
import com.brother.yckx.control.activity.rongyun.ui.activity.WasherConversationListActivity;
import com.brother.yckx.control.frgament.MoreFragment_FormDetail;
import com.brother.yckx.control.frgament.WasherFragment;
import com.brother.yckx.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shizhefei.view.indicator.FragmentListPageAdapter;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
/**美护师主界面(主界面:美护师抢单/美护师已抢列表)*/
public class WasherMainActivity extends BaseActivity implements FragmentItemOnClickListener{
	private IndicatorViewPager indicatorViewPager;
	private LayoutInflater inflate;
	private ViewPager viewPager;
	private ScrollIndicatorView  tabs;
	private String[] names = { "待抢列表", "已抢列表"};
	private CircleImageView washerImg;
	private TextView washerName;
	// 侧滑菜单
	private SlidingMenu slidingMenu;
	/**加载头像成功*/
	private final int LOADIMG_SUCCESS=0;
	/** 初始设置侧滑菜单 */
	private void initSlidingMenu() {
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);// 左侧
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_contentw);// 内容宽度
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// slidingMenu.setMenu(R.layout.inflate_slidleftmenu);// 设置侧滑菜单布局效果
		View leftMenu = getLayoutInflater().inflate(R.layout.inflater_slidleftmenu, null);
		leftMenu.findViewById(R.id.tv_leftmenu_qiandan).setOnClickListener(lefntmenuListener);//抢单
		leftMenu.findViewById(R.id.tv_leftmenu_chat_message).setOnClickListener(lefntmenuListener);//聊天消息
		leftMenu.findViewById(R.id.tv_leftmenu_push).setOnClickListener(lefntmenuListener);//推送通知
		leftMenu.findViewById(R.id.tv_leftmenu_orders).setOnClickListener(lefntmenuListener);//历史订单
		leftMenu.findViewById(R.id.tv_leftmenu_evaluate).setOnClickListener(lefntmenuListener);//历史评价
		leftMenu.findViewById(R.id.tv_leftmenu_punch).setOnClickListener(lefntmenuListener);//我要打卡
		leftMenu.findViewById(R.id.tv_leftmenu_updatepark).setOnClickListener(lefntmenuListener);//车位更新
		leftMenu.findViewById(R.id.tv_leftmenu_settleaccount).setOnClickListener(lefntmenuListener);//结算
		leftMenu.findViewById(R.id.tv_leftmenu_system).setOnClickListener(lefntmenuListener);//设置
		//设置洗护师的头像和名字
		washerImg=(CircleImageView) leftMenu.findViewById(R.id.washerImg);
		washerName=(TextView) leftMenu.findViewById(R.id.washerName);
		String name=PrefrenceConfig.getUserMessage(WasherMainActivity.this).getUserName();
		washerName.setText(name);
		//计算美护师星级
		String mUserTotalScore=PrefrenceConfig.getUserMessage(WasherMainActivity.this).getUserTotalScore_mhs();
		String mUserCommentCount=PrefrenceConfig.getUserMessage(WasherMainActivity.this).getUserCommentCount_mhs();
		String mScore=NumberUtils.InterDivisionInter(mUserTotalScore, mUserCommentCount);
		Integer intScore=Integer.parseInt(mScore);
		//设置美护师星级
		ImageView starImages[]=new ImageView[5];
		starImages[0]=(ImageView) leftMenu.findViewById(R.id.starImg1);
		starImages[1]=(ImageView) leftMenu.findViewById(R.id.starImg2);
		starImages[2]=(ImageView) leftMenu.findViewById(R.id.starImg3);
		starImages[3]=(ImageView) leftMenu.findViewById(R.id.starImg4);
		starImages[4]=(ImageView) leftMenu.findViewById(R.id.starImg5);
		for(int i=0;i<intScore;i++){
			starImages[i].setBackgroundResource(R.drawable.icon_evaluate_selected);
		}
	//	setImgAndUserNameInbackGround();
		slidingMenu.setMenu(leftMenu);
	}
	/**设置头像和用户名,用户名简介(暂时不开放)*//*
	private void setImgAndUserNameInbackGround(){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String imgUrl=WEBInterface.INDEXIMGURL+PrefrenceConfig.getUserMessage(WasherMainActivity.this).getUserIamgeUrl();
				Log.d("yckx", "加载头像的地址"+imgUrl);
				//ImageLoader.getInstance().displayImage(imgUrl, touxiang); 
			    Bitmap settingBitmp=ImageLoader.getInstance().loadImageSync(imgUrl); 
				if(settingBitmp!=null){
					Message msg=new Message();
					msg.what=LOADIMG_SUCCESS;
					msg.obj=settingBitmp;
					mHandler.sendMessage(msg);
				}
			}
		}).start();
	}*/
	
	
	/**处理ui*/
	private Handler mHandler=new Handler(){
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOADIMG_SUCCESS://加载头像成功
				Bitmap bitmap=(Bitmap) msg.obj;
				if(bitmap!=null&&washerImg!=null){
					washerImg.setCircleImageBitmap(bitmap);
				}
				break;
			}
		};
	};
	
	
	/** 当前Activity下侧滑菜单中菜单的监听回调 */
	private View.OnClickListener lefntmenuListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_leftmenu_qiandan:
				break;
			case R.id.tv_leftmenu_chat_message://聊天消息
				Intent intent_chat=new Intent(WasherMainActivity.this,WasherConversationListActivity.class);
				startActivity(intent_chat);
				break;
			case R.id.tv_leftmenu_push://推送消息
				Intent intent_msg=new Intent(WasherMainActivity.this,WasherNoticeListActivity.class);
				startActivity(intent_msg);
				break;
			case R.id.tv_leftmenu_orders://历史订单
				Intent intent_orders=new Intent(WasherMainActivity.this,WasherHistoryFormActivity.class);
				startActivity(intent_orders);
				break;
			case R.id.tv_leftmenu_evaluate://历史评价
				Intent intent_evaluate=new Intent(WasherMainActivity.this,HistoricalEvaluationActivity.class);
				startActivity(intent_evaluate);
				break;
			case R.id.tv_leftmenu_punch://我要打卡
				Intent intent_daka=new Intent(WasherMainActivity.this,WasherPunchActivity.class);
				startActivity(intent_daka);
				break;
			case R.id.tv_leftmenu_updatepark://更新车位
				Intent intent_updatepark=new Intent(WasherMainActivity.this,WasherUpdatePark.class);
				startActivity(intent_updatepark);
				break;
			case R.id.tv_leftmenu_settleaccount://结算
				Intent intent_settle=new Intent(WasherMainActivity.this,WasherSettleActivity.class);
				startActivity(intent_settle);
				break;
			case R.id.tv_leftmenu_system://设置
				ToastUtil.show(WasherMainActivity.this, "update", 800);
				Intent intent_system=new Intent(WasherMainActivity.this,SettingActivity.class);
				intent_system.putExtra("changeImg", "WasherMainActivity");
				startActivity(intent_system);
				break;
			}
			showSlidingMenu(true);
		}
	};
	
	private void showSlidingMenu(boolean isAnim) {
		if (slidingMenu == null) {
			return;
		}
		if (slidingMenu.isMenuShowing()) {
			slidingMenu.showContent(isAnim);
		}
		else {
			slidingMenu.showMenu(isAnim);
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_washer);
		setActionBar(R.string.washerMain_title, R.drawable.icon_washer_func, NULL_ID, actionBarListener);
		viewPager=(ViewPager) findViewById(R.id.viewPager);
		tabs=(ScrollIndicatorView) findViewById(R.id.tabs_viewPager);
		tabs.setScrollBar(new ColorBar(this, Color.RED, 5));
		
		showFragmentLayout();
		// 初始设置侧滑菜单
	    initSlidingMenu();
	}
	
	
	private View.OnClickListener actionBarListener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			showSlidingMenu(true);
		}
	};

	@Override
	public void onFragmentItemClick(String tag, int eventTagID, Object data) {
		
	}
	
	
	private void showFragmentLayout(){
		int selectColorId = R.color.tab_top_text_2;
		int unSelectColorId = R.color.tab_top_text_1;
		tabs.setOnTransitionListener(new OnTransitionTextListener().setColorId(this, selectColorId, unSelectColorId));
		viewPager.setOffscreenPageLimit(2);
		indicatorViewPager = new IndicatorViewPager(tabs, viewPager);
		inflate = LayoutInflater.from(getApplicationContext());
		indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
	}
	
	
	
	private int size =2;
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
			WasherFragment fragment = new WasherFragment();
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			showExitDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	/**退出提示框*/
	private Dialog exitDialog;
	private void showExitDialog(){
		LayoutInflater inflater=WasherMainActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_exist, null);
		exitDialog = new AlertDialog.Builder(WasherMainActivity.this).create();
		exitDialog.show();
		exitDialog.setContentView(view);
		Window dialogWindow = exitDialog.getWindow(); 
		WindowManager.LayoutParams params = dialogWindow.getAttributes();  
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		WindowManager m =getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		//偏移量
		exitDialog.getWindow().setAttributes(params);
		view.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				exitDialog.cancel();
			}});
		view.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finishAll();
				System.exit(0);
			}});
	}
}
