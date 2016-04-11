package com.brother.yckx.control.frgament;


import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import java.util.List;


import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.brother.App;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.model.UserEntity;

public class ChatOnlineFragment extends Fragment{
	private UserEntity userMessage;
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.conversationlist, container,false);
		//获取用户信息,进行展示...第一次使用进入登录界面
		//getRunYunToken();
		return view;
	}


	private void setconversationList(){
		ConversationListFragment listFragment = ConversationListFragment.getInstance();
		//listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
		Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
				.appendPath("conversationlist")
				.appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
				.appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
				.appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组
				.appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
				.appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
				.appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
				.build();
		listFragment.setUri(uri);
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


	/**获取荣云token值*/
	private void getRunYunToken(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(getActivity()).getUserToken();
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

	/**
	 * 建立与融云服务器的连接
	 *
	 * @param token
	 */
	private void connect(String token) {

		if (getActivity().getApplicationInfo().packageName.equals(App.getCurProcessName(getActivity().getApplicationContext()))) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				/**
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
				 */
				@Override
				public void onTokenIncorrect() {

					Log.d("yckx", "--onTokenIncorrect");
				}

				/**
				 * 连接融云成功
				 * @param userid 当前 token
				 */
				@Override
				public void onSuccess(String userid) {
					Log.d("yckx", "--onSuccess" + userid);
					setconversationList();
				}

				/**
				 * 连接融云失败
				 * @param errorCode 错误码，可到官网 查看错误码对应的注释
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {

					Log.d("yckx", "--onError" + errorCode);
				}
			});
		}
	}


}
