package com.brother.yckx.view.image2;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.brother.yckx.R;
import com.brother.yckx.view.image2.CacheImageTask.CompleteHandle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * ����ͼ��ؼ�
 * 
 * @author 
 */
public class CacheCircleImageView extends ImageView{
	private static Drawable loadDrawable; // �����е�ͼ��
	private static Drawable failDrawable; // ����ʧ��ͼ��
	/** ͼ������̳߳�(ע��Ҫ��̬,����ͼ����ھ�һ���̳߳ؿ���) */
	private static ExecutorService executorService = Executors.newFixedThreadPool(8);
	/** ��ǰͼ������߳�(ע��Ǿ�̬,ÿ��ͼ��һ���߳�����),��ֹ�ظ������߳� */
	private CacheImageTask cacheImageTask;

	// �ѿ������߳������б�,url��Ϊkey(δʹ��) */
	// private static HashMap<String, CacheImageTask> taskList = null;
	public CacheCircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadDrawable = context.getResources().getDrawable(R.drawable.loading_default);
		failDrawable = context.getResources().getDrawable(R.drawable.load_failure);
	}
	

	public CacheCircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	private CompleteHandle completeHandle = new CompleteHandle(){
		@Override
		public void onSucceed(Bitmap bitmap2) {
			//setImageBitmap(bitmap);
		     bitmap=bitmap2;
		     invalidate();
		}

		@Override
		public void onFail() {
			//setImageDrawable(failDrawable);
			init();
			invalidate();
		}
	};

	/** �첽ͨ��url����ͼ��,�������ȴ��ڴ��ȡ,���ػ�ȡ,�����ȡ */
	public void setImageUrl(String url) {
		// ��ǰImageView����ʾ������ͼ��
		//setImageDrawable(loadDrawable);
		init();
		invalidate();
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
		CacheCircleImageView.loadDrawable = succeedDrawable;
	}

	public void setFailDrawable(Drawable failDrawable) {
		CacheCircleImageView.failDrawable = failDrawable;
	}
	
	
	
	
	
	private Bitmap bitmap;
	private Paint paint;
	private Rect bitRect, viewRect;
	private int width, height, radius;
	private boolean isInit;
	private PorterDuffXfermode xfermode;
	
	private void init() {
		bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xff888888);
		bitRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
	}
	
	public void setCircleImageBitmap(Bitmap mbitmap){
		bitmap=mbitmap;
		invalidate();
	}
	
	
	
	
	protected void onDraw(Canvas canvas) {
		if (!isInit) {
			width = getWidth();
			height = getHeight();
			radius = Math.min(width, height) / 2;
			viewRect = new Rect(0, 0, width, height);
			isInit = true;
		}
		int layer = canvas.saveLayer(0, 0, width, height, paint,
				Canvas.ALL_SAVE_FLAG);
		canvas.drawColor(Color.TRANSPARENT);
		canvas.drawCircle(width / 2, height / 2, radius, paint);
		paint.setXfermode(xfermode);
		canvas.drawBitmap(bitmap, bitRect, viewRect, paint);
		paint.setXfermode(null);
		canvas.restoreToCount(layer);
	}
}
