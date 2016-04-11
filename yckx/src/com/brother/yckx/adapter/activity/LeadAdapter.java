package com.brother.yckx.adapter.activity;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/** 引导面适配器*/
public class LeadAdapter extends PagerAdapter {

	/** 适配器内的所有View */
	private ArrayList<View> viewList = new ArrayList<View>();

	/** 向适配器内添加View */
	public void addViewToAdapter(View view) {
		if (view != null) {
			viewList.add(view);
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = viewList.get(position);
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = viewList.get(position);
		container.removeView(view);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return viewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

}
