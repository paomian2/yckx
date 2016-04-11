package com.brother.yckx.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public class CacheImageTask implements Runnable{
	private CacheImage cacheImage;
	private boolean cancel = false;
	private CompleteHandle completeHandle;// ����������handle

	public void cancel() {
		this.cancel = true;
		if (cacheImage != null) {
			cacheImage.cancel();
		}
	}

	/**
	 * ����ͼ�����췽��
	 * 
	 * @param context
	 * @param url
	 * @param completeHandle
	 *            ��ɴ����Handle����(�������MainThread��Handler�ͽ�Ҫ���̻߳ص��ɹ���ʧ�ܵķ��� )
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
			// ��ȡ�ɹ�(#1 #2 #3)
			if (bitmap != null) {
				if (completeHandle != null) {
					Message msg = completeHandle.obtainMessage();
					msg.what = 0;
					msg.obj = bitmap;
					completeHandle.sendMessage(msg);
				}
			}
			// ��ȡʧ��
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

		/** ��������ɹ���ȡͼ�����ص��ķ��� */
		public abstract void onSucceed(Bitmap bitmap);

		/** ���������ȡͼ��ʧ�ܽ����ص��ķ��� */
		public abstract void onFail();
	}
}
