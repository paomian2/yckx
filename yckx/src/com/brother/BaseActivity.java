package com.brother;

import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.androide.lib3.volley.RequestQueue;
import com.brother.utils.logic.LogUtil;
import com.brother.yckx.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * FragmentActivity������ ����Ҫʵ�������¹��ܣ�
 * 
 * @see 1 ʵ���˶�Activity�������ڵ���־����
 * @see 2 ʵ�ֶ����С����ߡ�Activity�Ĺ��� {@link #finishAll()}��
 * @see 3 ʵ���˶�startActivity�Ķ��η�װ {@link #openActivity(Class)}��
 * @see 4 fragment�滻���� {@link #smartFragmentReplace(int, Fragment)}
 * @see 5 setAction()
 * @see 6 ��ȡVolley�������
 * 
 * @author yuanc
 */
public abstract class BaseActivity extends FragmentActivity{
	
	public ImageLoader imageLoader;
	
	private static final String TAG = "ActivityLife";
	/** �������������Ѵ򿪵�Activity */
	private static Stack<Activity> onLineActivityList = new Stack<Activity>();

	/* *********************************************************************** */
	/** ��ȡBaseApplication���е�Ψһһ���������(Volley) */
	public RequestQueue getVolleyRequestQueue() {
		BaseApplication application = (BaseApplication) getApplication();
		return application.getRequestQueue();
	}

	/* *********************************************************************** */
	private Fragment currentFragment;

	/** Fragment�滻(��ǰdestrory,�µ�create) */
	public void fragmentReplace(int target, Fragment toFragment, boolean backStack) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		String toClassName = toFragment.getClass().getSimpleName();
		if (manager.findFragmentByTag(toClassName) == null) {
			transaction.replace(target, toFragment, toClassName);
			if (backStack) {
				transaction.addToBackStack(toClassName);
			}
			transaction.commit();
		}
	}

	/** ������Fragment�滻(����Ϊ���ص�ǰ��,��ʾ���ڵ�,�ù��Ľ�����destrory��create) */
	public void smartFragmentReplace(int target, Fragment toFragment) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		// ���е�ǰ��ʹ�õ�->���ص�ǰ��
		if (currentFragment != null) {
			transaction.hide(currentFragment);
		}
		String toClassName = toFragment.getClass().getSimpleName();
		// toFragment֮ǰ���ʹ�ù�->��ʾ����
		if (manager.findFragmentByTag(toClassName) != null) {
			transaction.show(toFragment);
		}
		else {// toFragment��û���ʹ�ù�->�����ȥ
			transaction.add(target, toFragment, toClassName);
		}
		transaction.commit();
		// toFragment����Ϊ��ǰ��
		currentFragment = toFragment;
	}

	/* ********************************************************************* */
	/** �ر�����(ǰ̨����̨)Activity,ע�⣺����BaseActivityΪ���� */
	protected static void finishAll() {
		int len = onLineActivityList.size();
		for (int i = 0; i < len; i++) {
			Activity activity = onLineActivityList.pop();
			activity.finish();
		}
	}

	/* ********************************************************************* */
	public static final int NULL_ID = -1;

	/**
	 * ����ActionBar�ؼ��ϵ�����
	 * 
	 * @param resIdTitle
	 *            �м��������ı�id,û�б���ʱ��ʹ��{@link #NULL_ID}
	 * @param resIdLeft
	 *            ���ͼ����Դid,û��ͼ��ʱ��ʹ��{@link #NULL_ID}
	 * @param resIdRight
	 *            �Ҳ�ͼ����Դid,û��ͼ��ʱ��ʹ��{@link #NULL_ID}
	 * @param actionbarListener
	 *            ����������onclick����
	 */
	public void setActionBar(int resIdTitle, int resIdLeft, int resIdRight, OnClickListener actionListener) {
		try {
			TextView tv_action_title = (TextView) findViewById(R.id.tv_actionbar_title);
			ImageView iv_action_left = (ImageView) findViewById(R.id.iv_action_left);
			ImageView iv_action_right = (ImageView) findViewById(R.id.iv_action_right);
			if (actionListener != null) {
				tv_action_title.setOnClickListener(actionListener);
				iv_action_left.setOnClickListener(actionListener);
				iv_action_right.setOnClickListener(actionListener);
			}
			tv_action_title.setVisibility(View.INVISIBLE);
			iv_action_left.setVisibility(View.INVISIBLE);
			iv_action_right.setVisibility(View.INVISIBLE);
			if (resIdLeft != NULL_ID) {
				iv_action_left.setVisibility(View.VISIBLE);
				iv_action_left.setImageResource(resIdLeft);
			}
			if (resIdRight != NULL_ID) {
				iv_action_right.setVisibility(View.VISIBLE);
				iv_action_right.setImageResource(resIdRight);
			}
			if (resIdTitle != NULL_ID) {
				tv_action_title.setVisibility(View.VISIBLE);
				tv_action_title.setText(resIdTitle);
			}
		}
		catch (Exception e) {
			LogUtil.w("BaseActionbarActivity", "ActionBar���쳣,�Ƿ�ǰҳ�沢û��includle==> include_actionbar.xml����?");
		}
	}

	/* �������ڹ��� *********************************************************** */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageLoader = ImageLoader.getInstance();
		LogUtil.d(TAG, getClass().getSimpleName() + " onCreate()");
		onLineActivityList.push(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		LogUtil.d(TAG, getClass().getSimpleName() + " onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogUtil.d(TAG, getClass().getSimpleName() + " onResume()");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtil.d(TAG, getClass().getSimpleName() + " onPause()");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LogUtil.d(TAG, getClass().getSimpleName() + " onStop()");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.d(TAG, getClass().getSimpleName() + " onDestroy()");
		if (onLineActivityList.contains(this)) {
			onLineActivityList.remove(this);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		LogUtil.d(TAG, getClass().getSimpleName() + " onSaveInstanceState()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		LogUtil.d(TAG, getClass().getSimpleName() + " onConfigurationChanged()");
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}

	/* ����Activity����******************************************************* */
	public void openActivity(Class<?> targetActivityClass) {
		openActivity(targetActivityClass, null);
	}

	public void openActivity(Class<?> targetActivityClass, Bundle bundle) {
		Intent intent = new Intent(this, targetActivityClass);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}

	public void openActivityAndCloseThis(Class<?> targetActivityClass) {
		openActivity(targetActivityClass);
		this.finish();
	}

	public void openActivityAndCloseThis(Class<?> targetActivityClass, Bundle bundle) {
		openActivity(targetActivityClass, bundle);
		this.finish();
	}
	
	
	/**
	 * ����ڴ滺��
	 */
	public void clearMemoryCache()
	{
		imageLoader.clearMemoryCache();
	}
	
	/**
	 * ���SD���еĻ���
	 */
	public void clearDiscCache()
	{
		imageLoader.clearDiscCache();
		
	}
}
