package com.brother.yckx.control.frgament;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androide.lib3.view.astuetz.PagerSlidingTabStrip;
import com.brother.BaseFragment;
import com.brother.yckx.R;
import com.brother.yckx.adapter.fragmnet.FragmentAdapter;

/**
 * �����б�Fragment(����ʹ��)
 * */
public class YCKXClassGroupFragment extends BaseFragment{

	private ViewPager viewPager;
	private FragmentAdapter pagerAdapter;
	private PagerSlidingTabStrip tabStrip;

	@Override
	public View getAndInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ������ҳ��Fragment��ͼЧ��
		View view = inflater.inflate(R.layout.fragment_orderclass_fragment_groups, null);
		// ��ʼ��ҳ��Fragment��ͼ��ViewPager����(@see pagerAdapter)
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		return view;
	}
	@Override
	public void loadData() {
		pagerAdapter = new FragmentAdapter(getFragmentManager());
		pagerAdapter.addFragmentToAdapter("ȫ��", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("������", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("�ɵ���", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("��ϴ��", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("������", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("�����", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("��ȡ��", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("������", new YCKXClassFragmentOrders(1));
		viewPager.setAdapter(pagerAdapter);
		tabStrip.setViewPager(viewPager);
	}
	@Override
	public void resetData() {}

}
