package com.brother.utils.logic;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast管理工具类,有且只有一个Toast对象进行展示操作
 * 
 * @author yuanc
 */
public class ToastUtil{
	private static Toast toast;

	public static void show(Context context, String text, int duration) {
		if (toast == null) {
			toast = Toast.makeText(context, "", 0);
		}
		toast.setText(text);
		toast.setDuration(duration);
		toast.show();
	}
}
