package com.brother.utils.logic;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DeviceUtil{
	private static InputMethodManager imm = null;

	public static void softInput(Context context, View view, boolean hide) {
		// Òþ²ØÈí¼üÅÌ
		if (imm == null) {
			imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		if (hide && imm.isActive()) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);// Ç¿ÖÆÒþ²Ø¼üÅÌ
		}
		else {
			imm.showSoftInput(view, 0);
		}
	}
}
