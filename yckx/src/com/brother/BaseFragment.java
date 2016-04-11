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
 * ����Fragment��
 * 
 * @see 1 ��������ӿ�,��onAttach��ʵ�ּ������񴫵�
 * 
 * @see 2 ���currentViewǿ����,�����Դ�ظ���������
 * 
 * @author yuanc
 */
public abstract class BaseFragment extends Fragment{
	/** ��Fragmet���������¼��������� */
	private FragmentItemOnClickListener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		String fName = getClass().getSimpleName();// ��fragment������
		String aName = activity.getClass().getSimpleName();// ���ô�fragment��activity������
		LogUtil.d("BaseFragment", fName + " onAttach() �� " + aName);
		if (activity instanceof FragmentItemOnClickListener) {
			listener = (FragmentItemOnClickListener) activity;
		}
	}

	/** ��ȡBaseApplication���е�Ψһһ���������(Volley) */
	public RequestQueue getVolleyRequestQueue() {
		BaseApplication application = (BaseApplication) getActivity().getApplication();
		return application.getRequestQueue();
	}

	// --------------------------------------------------------------------
	private View currentView;

	/** ��ǰgetAndInitView()�����ص���ͼ,ע����getAndInitView������������ */
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

	/** ��ǰFragment�Ĳ���, ֻ���ڵ�һ��createʱ�Żᴥ�� */
	public abstract View getAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	/** ��ǰFragment�����ݼ���, ֻ���ڵ�һ��createʱ�Żᴥ�� */
	public abstract void loadData();

	/** ��ǰFragment�Ĳ�����������, ÿ��onCreateView��onHiddenChanged(false)���ᴥ�� */
	public abstract void resetData();

	// --------------------------------------------------------------------
	/** ��ǰFragment����Click�¼�,������������Activity�Ļص� */
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

	/** Fragmet���������¼����� */
	public static interface FragmentItemOnClickListener{
		/**
		 * �����Fragment�����ʱ����
		 * 
		 * @param tag
		 *            ��ǰ�¼��������ڵ�Fragment��ǩ(�������ָ�Fragment�ı�ʶ),Ĭ��Ϊ��ǰFragment����
		 * @param eventTagID
		 *            ��ǰ�¼��ı�ǩ(�������ֵ�ǰFragment�ϲ�ͬ�¼��ı�ʶ)
		 * @param data
		 *            ��Я��������,û�����ݴ���null
		 * 
		 * @see �ڸ�����Fragment�п���{@link BaseFragment#callbackActivityWhenFragmentItemClick(View, Object)}
		 *      �������¼�
		 */
		public void onFragmentItemClick(String tag, int eventTagID, Object data);
	}
}
