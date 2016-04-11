package com.brother.yckx.adapter.fragmnet;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter{
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private ArrayList<String> titles = new ArrayList<String>();

	public FragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public void addFragmentToAdapter(String title, Fragment fragment) {
		if (fragment != null) {
			fragments.add(fragment);
		}
		if (title != null) {
			titles.add(title);
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return titles.get(position);
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}
}
