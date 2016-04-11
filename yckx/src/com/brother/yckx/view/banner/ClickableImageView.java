package com.brother.yckx.view.banner;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**  鏈夋寜鍘嬫晥鏋滅殑ImageView
 * @author锛欽orge on 2015/9/10 12:40
 */
public class ClickableImageView extends ImageView {
    public ClickableImageView(Context context) {
        super(context);
        this.setOnTouchListener(VIEW_TOUCH_DARK);
    }

    public ClickableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(VIEW_TOUCH_DARK);
    }

    public ClickableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnTouchListener(VIEW_TOUCH_DARK);
    }
    public OnTouchListener VIEW_TOUCH_DARK = new OnTouchListener() {
        // 鍙樻殫(涓変釜-50锛屽�艰秺澶у垯鏁堟灉瓒婃繁)
        public final float[] BT_SELECTED_DARK = new float[] { 1, 0, 0, 0, -50,
                0, 1, 0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0 };
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ImageView iv = (ImageView) v;
                iv.setColorFilter(new ColorMatrixColorFilter(BT_SELECTED_DARK));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                ImageView iv = (ImageView) v;
                iv.clearColorFilter();
                mPerformClick();
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                ImageView iv = (ImageView) v;
                iv.clearColorFilter();
            }
            return true; // 濡備负false锛屾墽琛孉CTION_DOWN鍚庝笉鍐嶅線涓嬫墽琛�
        }
    };
    private void mPerformClick() {
        ClickableImageView.this.performClick();
    }

}
