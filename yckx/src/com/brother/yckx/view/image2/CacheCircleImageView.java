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
 * 缓存图像控件
 * 
 * @author 
 */
public class CacheCircleImageView extends ImageView{
	private static Drawable loadDrawable; // 加载中的图像
	private static Drawable failDrawable; // 加载失败图像
	/** 图像加载线程池(注意要静态,所有图像加在就一个线程池控制) */
	private static ExecutorService executorService = Executors.newFixedThreadPool(8);
	/** 当前图像加载线程(注意非静态,每张图像一个线程任务),防止重复开启线程 */
	private CacheImageTask cacheImageTask;

	// 已开启的线程任务列表,url做为key(未使用) */
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

	/** 异步通过url设置图像,它将优先从内存获取,本地获取,网络获取 */
	public void setImageUrl(String url) {
		// 当前ImageView先显示加载中图像
		//setImageDrawable(loadDrawable);
		init();
		invalidate();
		// 如果当前ImageView的线程任务已开启,先关闭
		if (cacheImageTask != null) {
			cacheImageTask.cancel();
			cacheImageTask = null;
		}
		// 执行此图像加载任务
		cacheImageTask = new CacheImageTask(getContext(), url, completeHandle);
		executorService.execute(cacheImageTask);
	}

	// 取消指定任务(未完成)
	// public static void cancelTask(String url) {}
	/** 取消所有任务 */
	public static void cancelAllTask() {
		// 取消清理关闭所有任务
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
