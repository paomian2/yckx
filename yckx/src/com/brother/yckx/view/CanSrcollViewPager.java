package com.brother.yckx.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CanSrcollViewPager extends ViewPager{
	
	private boolean isCanScroll=false;

	public CanSrcollViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CanSrcollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setCanScroll(boolean canScroll){
		this.isCanScroll=canScroll;
	}
	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if(isCanScroll){
			return super.onInterceptTouchEvent(arg0);
		}
		return false;
	}
}
