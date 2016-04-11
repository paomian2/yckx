package com.brother;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.androide.lib3.volley.RequestQueue;
import com.androide.lib3.volley.toolbox.Volley;
import com.brother.utils.logic.LogUtil;

// Application 一个应用程序只有一个唯一的对象, 生命周期最长
// 适合项目中一些唯一对象的保存
// 适合做数据传递
// 适合做缓存
public class BaseApplication extends Application{
	/** 本项目的唯一请求队列(Volley) */
	private RequestQueue requestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.d("BaseApplication", "BaseApplication onCreate");
		requestQueue = Volley.newRequestQueue(getApplicationContext());
	}

	/** 获取请求队列(Volley) */
	public RequestQueue getRequestQueue() {
		return requestQueue;
	}
}
