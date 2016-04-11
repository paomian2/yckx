package com.brother.utils.view_def;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager{

	private static final boolean result=false;
	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/*@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if(result){
			return super.onInterceptTouchEvent(arg0);
		}
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if(result){
			return super.onTouchEvent(arg0);
		}
		return false;
	}*/
}
