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
/**����ʦ������(������:����ʦ����/����ʦ�����б�)*/
public class WasherMainActivity extends BaseActivity implements FragmentItemOnClickListener{
	private IndicatorViewPager indicatorViewPager;
	private LayoutInflater inflate;
	private ViewPager viewPager;
	private ScrollIndicatorView  tabs;
	private String[] names = { "�����б�", "�����б�"};
	private CircleImageView washerImg;
	private TextView washerName;
	// �໬�˵�
	private SlidingMenu slidingMenu;
	/**����ͷ��ɹ�*/
	private final int LOADIMG_SUCCESS=0;
	/** ��ʼ���ò໬�˵� */
	private void initSlidingMenu() {
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);// ���
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_contentw);// ���ݿ��
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// slidingMenu.setMenu(R.layout.inflate_slidleftmenu);// ���ò໬�˵�����Ч��
		View leftMenu = getLayoutInflater().inflate(R.layout.inflater_slidleftmenu, null);
		leftMenu.findViewById(R.id.tv_leftmenu_qiandan).setOnClickListener(lefntmenuListener);//����
		leftMenu.findViewById(R.id.tv_leftmenu_chat_message).setOnClickListener(lefntmenuListener);//������Ϣ
		leftMenu.findViewById(R.id.tv_leftmenu_push).setOnClickListener(lefntmenuListener);//����֪ͨ
		leftMenu.findViewById(R.id.tv_leftmenu_orders).setOnClickListener(lefntmenuListener);//��ʷ����
		leftMenu.findViewById(R.id.tv_leftmenu_evaluate).setOnClickListener(lefntmenuListener);//��ʷ����
		leftMenu.findViewById(R.id.tv_leftmenu_punch).setOnClickListener(lefntmenuListener);//��Ҫ��
		leftMenu.findViewById(R.id.tv_leftmenu_updatepark).setOnClickListener(lefntmenuListener);//��λ����
		leftMenu.findViewById(R.id.tv_leftmenu_settleaccount).setOnClickListener(lefntmenuListener);//����
		leftMenu.findViewById(R.id.tv_leftmenu_system).setOnClickListener(lefntmenuListener);//����
		//����ϴ��ʦ��ͷ�������
		washerImg=(CircleImageView) leftMenu.findViewById(R.id.washerImg);
		washerName=(TextView) leftMenu.findViewById(R.id.washerName);
		String name=PrefrenceConfig.getUserMessage(WasherMainActivity.this).getUserName();
		washerName.setText(name);
		//��������ʦ�Ǽ�
		String mUserTotalScore=PrefrenceConfig.getUserMessage(WasherMainActivity.this).getUserTotalScore_mhs();
		String mUserCommentCount=PrefrenceConfig.getUserMessage(WasherMainActivity.this).getUserCommentCount_mhs();
		String mScore=NumberUtils.InterDivisionInter(mUserTotalScore, mUserCommentCount);
		Integer intScore=Integer.parseInt(mScore);
		//��������ʦ�Ǽ�
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
	/**����ͷ����û���,�û������(��ʱ������)*//*
	private void setImgAndUserNameInbackGround(){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String imgUrl=WEBInterface.INDEXIMGURL+PrefrenceConfig.getUserMessage(WasherMainActivity.this).getUserIamgeUrl();
				Log.d("yckx", "����ͷ��ĵ�ַ"+imgUrl);
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
	
	
	/**����ui*/
	private Handler mHandler=new Handler(){
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOADIMG_SUCCESS://����ͷ��ɹ�
				Bitmap bitmap=(Bitmap) msg.obj;
				if(bitmap!=null&&washerImg!=null){
					washerImg.setCircleImageBitmap(bitmap);
				}
				break;
			}
		};
	};
	
	
	/** ��ǰActivity�²໬�˵��в˵��ļ����ص� */
	private View.OnClickListener lefntmenuListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_leftmenu_qiandan:
				break;
			case R.id.tv_leftmenu_chat_message://������Ϣ
				Intent intent_chat=new Intent(WasherMainActivity.this,WasherConversationListActivity.class);
				startActivity(intent_chat);
				break;
			case R.id.tv_leftmenu_push://������Ϣ
				Intent intent_msg=new Intent(WasherMainActivity.this,WasherNoticeListActivity.class);
				startActivity(intent_msg);
				break;
			case R.id.tv_leftmenu_orders://��ʷ����
				Intent intent_orders=new Intent(WasherMainActivity.this,WasherHistoryFormActivity.class);
				startActivity(intent_orders);
				break;
			case R.id.tv_leftmenu_evaluate://��ʷ����
				Intent intent_evaluate=new Intent(WasherMainActivity.this,HistoricalEvaluationActivity.class);
				startActivity(intent_evaluate);
				break;
			case R.id.tv_leftmenu_punch://��Ҫ��
				Intent intent_daka=new Intent(WasherMainActivity.this,WasherPunchActivity.class);
				startActivity(intent_daka);
				break;
			case R.id.tv_leftmenu_updatepark://���³�λ
				Intent intent_updatepark=new Intent(WasherMainActivity.this,WasherUpdatePark.class);
				startActivity(intent_updatepark);
				break;
			case R.id.tv_leftmenu_settleaccount://����
				Intent intent_settle=new Intent(WasherMainActivity.this,WasherSettleActivity.class);
				startActivity(intent_settle);
				break;
			case R.id.tv_leftmenu_system://����
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
		// ��ʼ���ò໬�˵�
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
	
	
	/**�˳���ʾ��*/
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
		Display d = m.getDefaultDisplay(); // Ϊ��ȡ��Ļ����
		params.width = (int) ((d.getWidth()) * 0.9);
		params.height = params.height;
		//ƫ����
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
