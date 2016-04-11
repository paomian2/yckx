package com.brother.utils;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * ����һЩview�ؼ��Ĵ���Ĺ�����
 * 
 * */
public class ViewUtils {
	
	
	/**
	 * EditText֧���ֶ�����ļ�������
	 * @param ev_target
	 *        Ҫ������EditText
	 * @param layout
	 *        ev_target���ڵĲ����е������layout(LinearLayout,RelayoutLayout,FrameLayout)
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
						{ // ˵�������ǵ���״̬
							//ToastUtil.show(getApplicationContext(), "����", 2000);
						} else{
							ev_target.setInputType(InputType.TYPE_NULL);
							ev_target.setFocusableInTouchMode(false);
							//ToastUtil.show(getApplicationContext(), "����", 2000);
						}
					}
				});
	}

}
