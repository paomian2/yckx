package com.brother.yckx.control.activity.owner;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brother.App;
import com.brother.BaseActivity;
import com.brother.BaseFragment.FragmentItemOnClickListener;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.adapter.fragmnet.HomeFragmentAdapter;
import com.brother.yckx.control.frgament.ChatOnlineFragment;
import com.brother.yckx.control.frgament.PersonaCenterFragment;
import com.brother.yckx.control.frgament.WoWoFragment;
import com.brother.yckx.control.frgament.YCKXFragment;
import com.brother.yckx.view.CanSrcollViewPager;
import com.tencent.android.tpush.XGPushConfig;

public class HomeActivity extends BaseActivity implements FragmentItemOnClickListener{
	private CanSrcollViewPager viewpager;
	private LinearLayout main,conversation,user_center;
	private ImageView main_img,conversation_img,user_center_img;
	private TextView main_tv,conversation_tv,user_center_tv;

	private List<Fragment> fragmentList;
	private HomeFragmentAdapter adapter;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		//setActionBar(R.string.home_title_bar, R.drawable.icon_default_search, R.drawable.icon_shop_all_blue, listener);
		XGPushConfig.enableDebug(this, true);
		init();
		selectTab(1);
		//RongIM.getInstance().logout();
		//getRunYunToken();
	}

	private void init(){
		viewpager=(CanSrcollViewPager) findViewById(R.id.viewpager);
		main=(LinearLayout) findViewById(R.id.yckx_layout);
		conversation=(LinearLayout) findViewById(R.id.chatonline_layout);
		user_center=(LinearLayout) findViewById(R.id.member_layout);

		main_img=(ImageView) findViewById(R.id.yckx_img);
		conversation_img=(ImageView) findViewById(R.id.chat_img);
		user_center_img=(ImageView) findViewById(R.id.member_img);

		main_tv=(TextView) findViewById(R.id.yckx_tv);
		conversation_tv=(TextView) findViewById(R.id.chat_tv);
		user_center_tv=(TextView) findViewById(R.id.member_tv);

		main.setOnClickListener(listener);
		conversation.setOnClickListener(listener);
		user_center.setOnClickListener(listener);

		fragmentList=new ArrayList<Fragment>();
		//窝窝头
		LinearLayout wowo_layout=(LinearLayout) findViewById(R.id.wowo_layout);
		wowo_layout.setOnClickListener(listener);
		Fragment wowoFragment=new WoWoFragment();
		fragmentList.add(wowoFragment);
		
		Fragment mainTab=new YCKXFragment();
		//Fragment conversationTab=new ChatOnlineFragment();
		Fragment user_center_Tab=new PersonaCenterFragment();
		fragmentList.add(mainTab);
		
		mConversationFragment=initConversationList();
		fragmentList.add(mConversationFragment);
		//fragmentList.add(conversationTab);
		fragmentList.add(user_center_Tab);

		adapter=new HomeFragmentAdapter(getSupportFragmentManager());
		adapter.addFragmentList(fragmentList);
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(pagerChangeListener);
		viewpager.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}});
	}

	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.wowo_layout:
				selectTab(0);
				break;
			case R.id.yckx_layout:
				selectTab(1);
				break;
			case R.id.chatonline_layout:
				selectTab(2);
				break;
			case R.id.member_layout:
				selectTab(3);
				break;
			}
		}
	};


	private OnPageChangeListener pagerChangeListener=new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			int currentTab=viewpager.getCurrentItem();
			setTab(currentTab);
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	/**选择选项*/
	private void selectTab(int currentTab){
		resetColor();
		setTab(currentTab);
	}

	/**设置当前选择项的图片和文本*/
	private void setTab(int currentTab){
		resetColor();
		viewpager.setCurrentItem(currentTab);
		switch (currentTab) {
		case 0:
			break;
		case 1:
			main_img.setImageDrawable(getResources().getDrawable(R.drawable.tabbar_main_selected));
			main_tv.setTextColor(getResources().getColor(R.color.select_tab));
			break;
		case 2:
			conversation_img.setImageDrawable(getResources().getDrawable(R.drawable.tabbar_conversation_selected));
			conversation_tv.setTextColor(getResources().getColor(R.color.select_tab));
			break;
		case 3:
			user_center_img.setImageDrawable(getResources().getDrawable(R.drawable.tabbar_user_center_selected));
			user_center_tv.setTextColor(getResources().getColor(R.color.select_tab));
			break;
		}
	}

	/**重置选项卡颜色*/
	private void resetColor(){
		main_img.setImageDrawable(getResources().getDrawable(R.drawable.tabbar_main_normal));
		conversation_img.setImageDrawable(getResources().getDrawable(R.drawable.tabbar_conversation_normal));
		user_center_img.setImageDrawable(getResources().getDrawable(R.drawable.tabbar_user_center_normal));

		main_tv.setTextColor(getResources().getColor(R.color.nomal_tab));
		conversation_tv.setTextColor(getResources().getColor(R.color.nomal_tab));
		user_center_tv.setTextColor(getResources().getColor(R.color.nomal_tab));
	}
	@Override
	public void onFragmentItemClick(String tag, int eventTagID, Object data) {

	}
	
	
	/**获取荣云token值*/
	private void getRunYunToken(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(HomeActivity.this).getUserToken();
				String url=WEBInterface.RONGYUN_TOKEN_URL;
				String respose=ApacheHttpUtil.httpPost(url, token, null);
				Log.d("yckx", "ronyun token:"+respose);
				if(respose==null){
					return;
				}
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						String grTk=jSONObject.getString("grTk");
						Message msg=new Message();
						msg.what=1;
						msg.obj=grTk;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private Handler mHandler=new Handler(){
		@SuppressLint("HandlerLeak") public void handleMessage(Message msg) {
			if(msg.what==1){
				String grTk=(String) msg.obj;
				//获取当前用户会话列表
				connect(grTk);
			}
		};
	};
	
	
	private Fragment mConversationFragment;
	
	/**融云回话类表*/
	private Fragment initConversationList(){
		if(mConversationFragment==null){
			ConversationListFragment listFragment = ConversationListFragment.getInstance();
			listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
			Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
					.appendPath("conversationlist")
					.appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
					.appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
					.appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组
					.appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
					.appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
					.appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
					.build();
			listFragment.setUri(uri);
			return listFragment;
		}else{
			return mConversationFragment;
		}
	}
	
	
	class ConversationListAdapterEx extends ConversationListAdapter {
		public ConversationListAdapterEx(Context context) {
			super(context);
		}

		@Override
		protected View newView(Context context, int position, ViewGroup group) {
			return super.newView(context, position, group);
		}

		@Override
		protected void bindView(View v, int position, UIConversation data) {
			if(data.getConversationType().equals(Conversation.ConversationType.DISCUSSION))
				data.setUnreadType(UIConversation.UnreadRemindType.REMIND_ONLY);

			super.bindView(v, position, data);
		}
	}
	
	
	/**
	 * 建立与融云服务器的连接
	 *
	 * @param token
	 */
	private void connect(String token) {

	    if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {

	        /**
	         * IMKit SDK调用第二步,建立与服务器的连接
	         */
	        RongIM.connect(token, new RongIMClient.ConnectCallback() {

	            /**
	             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
	             */
	            @Override
	            public void onTokenIncorrect() {

	                Log.d("LoginActivity", "--onTokenIncorrect");
	            }

	            /**
	             * 连接融云成功
	             * @param userid 当前 token
	             */
	            @Override
	            public void onSuccess(String userid) {

	                Log.d("LoginActivity", "--onSuccess" + userid);
	            }

	            /**
	             * 连接融云失败
	             * @param errorCode 错误码，可到官网 查看错误码对应的注释
	             */
	            @Override
	            public void onError(RongIMClient.ErrorCode errorCode) {

	                Log.d("LoginActivity", "--onError" + errorCode);
	            }
	        });
	    }
	}
	
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
		LayoutInflater inflater=HomeActivity.this.getLayoutInflater();
		View view = inflater.inflate(R.layout.row_dialog_exist, null);
		exitDialog = new AlertDialog.Builder(HomeActivity.this).create();
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
		view.findViewById(R.id.deleteCancel).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				exitDialog.cancel();
			}});
		view.findViewById(R.id.deleteConfirm).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				/*finishAll();
				System.exit(0);*/
				App.getApplication().exitApp();
			}});
	}
}
