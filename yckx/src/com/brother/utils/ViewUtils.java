package com.brother.utils;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 关于一些view控件的处理的工具类
 * 
 * */
public class ViewUtils {
	
	
	/**
	 * EditText支持手动输入的监听方法
	 * @param ev_target
	 *        要监听的EditText
	 * @param layout
	 *        ev_target所在的布局中的最外侧layout(LinearLayout,RelayoutLayout,FrameLayout)
	 * 
	 * */
	public static  void setEditTextKeyBoard(final EditText ev_target,final View layout){
		ev_target.setInputType(InputType.TYPE_CLASS_PHONE);
		ev_target.setFocusableInTouchMode(true);
		ev_target.requestFocus();
		InputMethodManager inputManager = (InputMethodManager)ev_target.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(ev_target, 0);
		layout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener(){
					@Override
					public void onGlobalLayout()
					{
						int heightDiff = layout.getRootView().getHeight() - layout.getHeight();
						if (heightDiff > 100)
						{ // 说明键盘是弹出状态
							//ToastUtil.show(getApplicationContext(), "弹出", 2000);
						} else{
							ev_target.setInputType(InputType.TYPE_NULL);
							ev_target.setFocusableInTouchMode(false);
							//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
						}
					}
				});
	}

}
