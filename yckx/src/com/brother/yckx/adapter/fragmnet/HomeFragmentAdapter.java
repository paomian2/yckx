package com.brother.yckx.adapter.fragmnet;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeFragmentAdapter extends FragmentPagerAdapter{

	private List<Fragment> mFragmentList=new ArrayList<Fragment>();
	
	public void addFragmentList(List<Fragment> list){
		if(mFragmentList!=null){
			mFragmentList.addAll(list);
		}
	}
	public HomeFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}

}
