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
 * FragmentActivity基础类 ，主要实现了以下功能：
 * 
 * @see 1 实现了对Activity生命周期的日志管理；
 * @see 2 实现对所有“在线”Activity的管理 {@link #finishAll()}；
 * @see 3 实现了对startActivity的二次封装 {@link #openActivity(Class)}；
 * @see 4 fragment替换操作 {@link #smartFragmentReplace(int, Fragment)}
 * @see 5 setAction()
 * @see 6 获取Volley请求队列
 * 
 * @author yuanc
 */
public abstract class BaseActivity extends FragmentActivity{
	
	public ImageLoader imageLoader;
	
	private static final String TAG = "ActivityLife";
	/** 用来保存所有已打开的Activity */
	private static Stack<Activity> onLineActivityList = new Stack<Activity>();

	/* *********************************************************************** */
	/** 获取BaseApplication类中的唯一一个请求队列(Volley) */
	public RequestQueue getVolleyRequestQueue() {
		BaseApplication application = (BaseApplication) getApplication();
		return application.getRequestQueue();
	}

	/* *********************************************************************** */
	private Fragment currentFragment;

	/** Fragment替换(当前destrory,新的create) */
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

	/** 聪明的Fragment替换(核心为隐藏当前的,显示现在的,用过的将不会destrory与create) */
	public void smartFragmentReplace(int target, Fragment toFragment) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		// 如有当前在使用的->隐藏当前的
		if (currentFragment != null) {
			transaction.hide(currentFragment);
		}
		String toClassName = toFragment.getClass().getSimpleName();
		// toFragment之前添加使用过->显示出来
		if (manager.findFragmentByTag(toClassName) != null) {
			transaction.show(toFragment);
		}
		else {// toFragment还没添加使用过->添加上去
			transaction.add(target, toFragment, toClassName);
		}
		transaction.commit();
		// toFragment更新为当前的
		currentFragment = toFragment;
	}

	/* ********************************************************************* */
	/** 关闭所有(前台、后台)Activity,注意：请已BaseActivity为父类 */
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
	 * 设置ActionBar控件上的数据
	 * 
	 * @param resIdTitle
	 *            中间主标题文本id,没有标题时可使用{@link #NULL_ID}
	 * @param resIdLeft
	 *            左侧图标资源id,没有图标时可使用{@link #NULL_ID}
	 * @param resIdRight
	 *            右侧图标资源id,没有图标时可使用{@link #NULL_ID}
	 * @param actionbarListener
	 *            动作导航条onclick监听
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
			LogUtil.w("BaseActionbarActivity", "ActionBar有异常,是否当前页面并没有includle==> include_actionbar.xml布局?");
		}
	}

	/* 生命周期管理 *********************************************************** */
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

	/* 启动Activity控制******************************************************* */
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
	 * 清除内存缓存
	 */
	public void clearMemoryCache()
	{
		imageLoader.clearMemoryCache();
	}
	
	/**
	 * 清除SD卡中的缓存
	 */
	public void clearDiscCache()
	{
		imageLoader.clearDiscCache();
		
	}
}
