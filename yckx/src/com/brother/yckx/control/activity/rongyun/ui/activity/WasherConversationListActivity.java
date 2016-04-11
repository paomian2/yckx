package com.brother.yckx.control.activity.rongyun.ui.activity;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.model.Conversation;

import com.brother.yckx.R;
import com.brother.yckx.adapter.fragmnet.HomeFragmentAdapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class WasherConversationListActivity extends FragmentActivity{
	private List<Fragment> fragmentList;
	private HomeFragmentAdapter adapter;
	private ViewPager viewpager;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.conversationlist);
		viewpager=(ViewPager) findViewById(R.id.viewpager);
		fragmentList=new ArrayList<Fragment>();
		mConversationFragment=initConversationList();
		fragmentList.add(mConversationFragment);
		adapter=new HomeFragmentAdapter(getSupportFragmentManager());
		adapter.addFragmentList(fragmentList);
		viewpager.setAdapter(adapter);
		viewpager.setCurrentItem(0);
	}
	
	
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

}
