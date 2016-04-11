package com.brother.yckx.view;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

	private Bitmap bitmap;
	private Paint paint;
	private Rect bitRect, viewRect;
	private int width, height, radius;
	private boolean isInit;
	private PorterDuffXfermode xfermode;

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleImageView(Context context) {
		this(context, null);
	}

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
