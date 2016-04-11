package com.brother.yckx.view.image;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.brother.yckx.R;
import com.brother.yckx.view.image.CacheImageTask.CompleteHandle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * ����ͼ��ؼ�
 * 
 * @author 
 */
public class CacheImageView extends ImageView{
	private static Drawable loadDrawable; // �����е�ͼ��
	private static Drawable failDrawable; // ����ʧ��ͼ��
	/** ͼ������̳߳�(ע��Ҫ��̬,����ͼ����ھ�һ���̳߳ؿ���) */
	private static ExecutorService executorService = Executors.newFixedThreadPool(8);
	/** ��ǰͼ������߳�(ע��Ǿ�̬,ÿ��ͼ��һ���߳�����),��ֹ�ظ������߳� */
	private CacheImageTask cacheImageTask;

	// �ѿ������߳������б�,url��Ϊkey(δʹ��) */
	// private static HashMap<String, CacheImageTask> taskList = null;
	public CacheImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadDrawable = context.getResources().getDrawable(R.drawable.loading_default);
		failDrawable = context.getResources().getDrawable(R.drawable.load_failure);
	}
	

	public CacheImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	private CompleteHandle completeHandle = new CompleteHandle(){
		@Override
		public void onSucceed(Bitmap bitmap) {
			setImageBitmap(bitmap);
		}

		@Override
		public void onFail() {
			setImageDrawable(failDrawable);
		}
	};

	/** �첽ͨ��url����ͼ��,�������ȴ��ڴ��ȡ,���ػ�ȡ,�����ȡ */
	public void setImageUrl(String url) {
		// ��ǰImageView����ʾ������ͼ��
		setImageDrawable(loadDrawable);
		// �����ǰImageView���߳������ѿ���,�ȹر�
		if (cacheImageTask != null) {
			cacheImageTask.cancel();
			cacheImageTask = null;
		}
		// ִ�д�ͼ���������
		cacheImageTask = new CacheImageTask(getContext(), url, completeHandle);
		executorService.execute(cacheImageTask);
	}

	// ȡ��ָ������(δ���)
	// public static void cancelTask(String url) {}
	/** ȡ���������� */
	public static void cancelAllTask() {
		// ȡ������ر���������
		List<Runnable> runnables = executorService.shutdownNow();
		for (Runnable runnable : runnables) {
			CacheImageTask task = (CacheImageTask) runnable;
			task.cancel();
		}
		executorService = null;
		executorService = Executors.newFixedThreadPool(8);
		System.gc();
	}

	public void setSucceedDrawable(Drawable succeedDrawable) {
		CacheImageView.loadDrawable = succeedDrawable;
	}

	public void setFailDrawable(Drawable failDrawable) {
		CacheImageView.failDrawable = failDrawable;
	}
}
