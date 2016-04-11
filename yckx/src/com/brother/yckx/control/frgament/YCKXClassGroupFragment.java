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
 * 订单列表Fragment(测试使用)
 * */
public class YCKXClassGroupFragment extends BaseFragment{

	private ViewPager viewPager;
	private FragmentAdapter pagerAdapter;
	private PagerSlidingTabStrip tabStrip;

	@Override
	public View getAndInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 加载主页面Fragment视图效果
		View view = inflater.inflate(R.layout.fragment_orderclass_fragment_groups, null);
		// 初始主页面Fragment视图上ViewPager内容(@see pagerAdapter)
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		return view;
	}
	@Override
	public void loadData() {
		pagerAdapter = new FragmentAdapter(getFragmentManager());
		pagerAdapter.addFragmentToAdapter("全部", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("代付款", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("派单中", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("清洗中", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("待评价", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("已完成", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("已取消", new YCKXClassFragmentOrders(1));
		pagerAdapter.addFragmentToAdapter("已作废", new YCKXClassFragmentOrders(1));
		viewPager.setAdapter(pagerAdapter);
		tabStrip.setViewPager(viewPager);
	}
	@Override
	public void resetData() {}

}
