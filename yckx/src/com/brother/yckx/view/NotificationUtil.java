package com.brother.yckx.view;

import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.FormDetailActivity;
import com.brother.yckx.control.activity.owner.OrderDetaileActivity;
import com.brother.yckx.control.activity.owner.SettingActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


public class NotificationUtil{
	private static Activity mfromActivity;
	private static NotificationManager manager;
	private static Notification notification;
	public static final int NOTIFI_APPICON_ID = 1;
	
	
	 public final static String ACTION_BTN = "com.example.notification.btn.topager";
	 public final static String INTENT_NAME = "btnid";
	 public final static int INTENT_BTN_LOGIN = 1;
	 NotificationBroadcastReceiver mReceiver;
	 

	public static boolean isOpenNotification(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("notifi", Context.MODE_PRIVATE);
		return preferences.getBoolean("open", true);
	}

	public static void setOpenNotification(Context context, boolean open) {
		SharedPreferences preferences = context.getSharedPreferences("notifi", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("open", open);
		editor.commit();
	}

	public static void cancelAppIconNotification(Context context) {
		if (manager == null) {
			manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		manager.cancel(NOTIFI_APPICON_ID);
	}

	@SuppressLint("NewApi") public static void showAppIconNotification(final Context context,Bitmap bimap,String title,String content,Uri sound,Activity fromActivity) {
		if (notification == null) {
			notification = new Notification();
		}
		mfromActivity=fromActivity;
		//notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.flags = Notification.FLAG_NO_CLEAR;
		notification.when = System.currentTimeMillis();
		notification.tickerText=title;
		notification.icon=R.drawable.icon_default_push_mess;
		notification.defaults = Notification.DEFAULT_VIBRATE;// 设置默认震动
		notification.flags |= Notification.FLAG_INSISTENT; 
		notification.sound=sound;
		RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.layout_notification_appincon);
		contentView.setTextViewText(R.id.tv_text, content);
		contentView.setTextViewText(R.id.tv_title, title);
		Intent intent_topage=new Intent(fromActivity,FormDetailActivity.class);
		contentView.setOnClickFillInIntent(R.id.tv_xiangqing, intent_topage);
		notification.contentView = contentView;
		Intent intent = new Intent("com.brother.yckx.control.activity.FormDetailActivity");
		PendingIntent contentIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentIntent = contentIntent;
		if (manager == null) {
			manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		manager.notify(NOTIFI_APPICON_ID, notification);
	}
	
	
	 class NotificationBroadcastReceiver extends BroadcastReceiver {
           
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();
	            if (action.equals(ACTION_BTN)) {
	                int btn_id = intent.getIntExtra(INTENT_NAME, 0);
	                switch (btn_id) {
	                    case INTENT_BTN_LOGIN:
	                        Toast.makeText(mfromActivity, "从通知栏点登录", Toast.LENGTH_SHORT).show();
	                        unregeisterReceiver();
	                        NotificationManager notificationManager = (NotificationManager) mfromActivity.getSystemService(Context.NOTIFICATION_SERVICE);
	                        notificationManager.cancel(0);
	                        break;
	                }
	            }
	        }
	    }
	 
	 
	 
	 
	  private void intiReceiver() {
	        mReceiver = new NotificationBroadcastReceiver();
	        IntentFilter intentFilter = new IntentFilter();
	        intentFilter.addAction(ACTION_BTN);
	        mfromActivity.getApplicationContext().registerReceiver(mReceiver, intentFilter);
	    }
	  private void unregeisterReceiver() {
	        if (mReceiver != null) {
	        	mfromActivity.getApplicationContext().unregisterReceiver(mReceiver);
	            mReceiver = null;
	        }
	    }
}
