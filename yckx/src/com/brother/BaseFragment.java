package com.brother;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androide.lib3.volley.RequestQueue;
import com.brother.utils.logic.LogUtil;

/**
 * 基础Fragment类
 * 
 * @see 1 加入监听接口,在onAttach中实现监听对像传递
 * 
 * @see 2 完成currentView强引用,解决资源重复加载问题
 * 
 * @author yuanc
 */
public abstract class BaseFragment extends Fragment{
	/** 此Fragmet上组件点击事件监听对象 */
	private FragmentItemOnClickListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		String fName = getClass().getSimpleName();// 此fragment的类名
		String aName = activity.getClass().getSimpleName();// 运用此fragment的activity的类名
		LogUtil.d("BaseFragment", fName + " onAttach() 到 " + aName);
		if (activity instanceof FragmentItemOnClickListener) {
			listener = (FragmentItemOnClickListener) activity;
		}
	}

	/** 获取BaseApplication类中的唯一一个请求队列(Volley) */
	public RequestQueue getVolleyRequestQueue() {
		BaseApplication application = (BaseApplication) getActivity().getApplication();
		return application.getRequestQueue();
	}

	// --------------------------------------------------------------------
	private View currentView;

	/** 当前getAndInitView()所返回的视图,注意在getAndInitView方法后来调用 */
	public View getCurrentView() {
		return currentView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (currentView == null) {
			currentView = getAndInitView(inflater, container, savedInstanceState);// #1
			loadData();// #2
			LogUtil.d("BaseFragment", getClass().getSimpleName() + " onCreateView()->getAndInitView() AND loadData()");
		}
		resetData();// #3
		LogUtil.d("BaseFragment", getClass().getSimpleName() + " onCreateView()->resetData()");
		return currentView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup viewGroup = (ViewGroup) currentView.getParent();
		if (viewGroup != null) {
			viewGroup.removeView(currentView);
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			resetData();
			LogUtil.d("BaseFragment", getClass().getSimpleName() + " onHiddenChanged()[show] -> resetData()");
			onResume();
		}
		else {
			onPause();
		}
	}

	/** 当前Fragment的布局, 只有在第一次create时才会触发 */
	public abstract View getAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	/** 当前Fragment的数据加载, 只有在第一次create时才会触发 */
	public abstract void loadData();

	/** 当前Fragment的布局数据重置, 每次onCreateView和onHiddenChanged(false)都会触发 */
	public abstract void resetData();

	// --------------------------------------------------------------------
	/** 当前Fragment上有Click事件,触发其依附的Activity的回调 */
	protected void callbackActivityWhenFragmentItemClick(int eventTagID, Object data) {
		if (listener != null) {
			listener.onFragmentItemClick(this.getClass().getSimpleName(), eventTagID, data);
		}
	}

	protected void callbackActivityWhenFragmentItemClick(String tag, int eventTagID, Object data) {
		if (listener != null) {
			listener.onFragmentItemClick(tag, eventTagID, data);
		}
	}

	/** Fragmet上组件点击事件监听 */
	public static interface FragmentItemOnClickListener{
		/**
		 * 当点击Fragment上组件时调用
		 * 
		 * @param tag
		 *            当前事件所发生在的Fragment标签(用做区分各Fragment的标识),默认为当前Fragment类名
		 * @param eventTagID
		 *            当前事件的标签(用做区分当前Fragment上不同事件的标识)
		 * @param data
		 *            需携带的数据,没有数据传入null
		 * 
		 * @see 在各子类Fragment中可用{@link BaseFragment#callbackActivityWhenFragmentItemClick(View, Object)}
		 *      来触发事件
		 */
		public void onFragmentItemClick(String tag, int eventTagID, Object data);
	}
}
