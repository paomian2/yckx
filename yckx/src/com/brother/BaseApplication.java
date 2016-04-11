package com.brother;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.androide.lib3.volley.RequestQueue;
import com.androide.lib3.volley.toolbox.Volley;
import com.brother.utils.logic.LogUtil;

// Application һ��Ӧ�ó���ֻ��һ��Ψһ�Ķ���, ���������
// �ʺ���Ŀ��һЩΨһ����ı���
// �ʺ������ݴ���
// �ʺ�������
public class BaseApplication extends Application{
	/** ����Ŀ��Ψһ�������(Volley) */
	private RequestQueue requestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.d("BaseApplication", "BaseApplication onCreate");
		requestQueue = Volley.newRequestQueue(getApplicationContext());
	}

	/** ��ȡ�������(Volley) */
	public RequestQueue getRequestQueue() {
		return requestQueue;
	}
}
