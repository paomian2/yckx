package com.brother.yckx.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public class CacheImageTask implements Runnable{
	private CacheImage cacheImage;
	private boolean cancel = false;
	private CompleteHandle completeHandle;// 完成任务处理的handle

	public void cancel() {
		this.cancel = true;
		if (cacheImage != null) {
			cacheImage.cancel();
		}
	}

	/**
	 * 缓存图像处理构造方法
	 * 
	 * @param context
	 * @param url
	 * @param completeHandle
	 *            完成处理的Handle对象(传入的是MainThread的Handler就将要主线程回调成功或失败的方法 )
	 */
	protected CacheImageTask(Context context, String url, CompleteHandle completeHandle) {
		cacheImage = new CacheImage(context, url);
		this.completeHandle = completeHandle;
	}

	@Override
	public void run() {
		if (!cancel && cacheImage != null) {
			Bitmap bitmap = cacheImage.getBitmap();
			if (cancel) {
				return;
			}
			// 获取成功(#1 #2 #3)
			if (bitmap != null) {
				if (completeHandle != null) {
					Message msg = completeHandle.obtainMessage();
					msg.what = 0;
					msg.obj = bitmap;
					completeHandle.sendMessage(msg);
				}
			}
			// 获取失败
			else {
				if (completeHandle != null) {
					completeHandle.sendEmptyMessage(1);
				}
			}
		}
	}

	public abstract static class CompleteHandle extends Handler{
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			int what = msg.what;
			switch (what) {
			case 0:
				Bitmap bitmap = (Bitmap) msg.obj;
				onSucceed(bitmap);
				break;
			default:
				onFail();
				break;
			}
		}

		/** 当此任务成功获取图像将来回调的方法 */
		public abstract void onSucceed(Bitmap bitmap);

		/** 当此任务获取图像失败将来回调的方法 */
		public abstract void onFail();
	}
}
