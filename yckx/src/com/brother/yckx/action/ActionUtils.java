package com.brother.yckx.action;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/3/30.
 *
 * 鐢ㄤ簬鎿嶄綔閫昏緫鎿嶄綔鐨勫伐鍏风被
 *
 */
public class ActionUtils {

    private static File yckx_tempFile;
    private static String fileName = "/sdcard/yckx/yckx_maobili_temp.png";
    /**灏咮itmap淇濆瓨鍒版寚瀹氫綅缃�*/
    public static void saveBitmap(Bitmap bitmap){
        FileOutputStream gaodeFos = null;
        yckx_tempFile = new File("/sdcard/yckx/");
        if(!yckx_tempFile.exists()){
            yckx_tempFile.mkdirs();// 鍒涘缓鏂囦欢澶�
        }
        try {
            gaodeFos = new FileOutputStream(fileName); //鍐欏埌瀛樺偍鍗�
            boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, gaodeFos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            gaodeFos.flush();
            gaodeFos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**鑾峰彇璧勬簮鍥剧墖*/
    public static  Bitmap getBitmapFromResourse(Context context,int resourseId){
        Bitmap  bitmap = BitmapFactory.decodeResource(context.getResources(), resourseId);
        return bitmap;
    }

    /**閲峴dcard涓幏鍙栧浘鐗�*/
    public static Bitmap getBitmapFromSDCard(String path){
        File mfile=new File(path);
        if(mfile.exists()){
            Bitmap bitmap= BitmapFactory.decodeFile(path);
            return bitmap;
        }else{
            return null;
        }
    }

    //api4.2浠ヤ笂鎵嶈兘浣跨敤鐨勬瘺鐜荤拑鏁堟灉鍥�
	/*private void blur(Context context,Bitmap bkg, View view, float radius) {
        Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bkg, -view.getLeft(), -view.getTop(), null);
        RenderScript rs = RenderScript.create(context);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        view.setBackground(new BitmapDrawable(context.getResources(), overlay));
        rs.destroy();
    }  */


    /**姣涚幓鐠冩晥鏋滃浘*/
    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    /**鎵嬫満鎴睆*/
    public static  Bitmap takeScreenShot(Activity activity){
        //View鏄綘闇�瑕佹埅鍥剧殑View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        //鑾峰彇鐘舵�佹爮楂樺害
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);//鑾峰彇灞忓箷闀垮拰楂�
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();//鍘绘帀鏍囬鏍�
        //Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }


    /**灏嗗埗瀹氱殑Bitmap淇濆瓨鍒版枃浠�*/
    public  static void saveBimap(Bitmap b,String strFileName){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos)
            {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 寰楀埌灞忓箷鐨勯珮搴�
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
    
    
    
    /**
     * 打开软键盘，不带监听
     * 
     * @param keyView点击此控件后打开软键盘
     * 
     * @param valueView需要监听的EditText
     * 
     * @param mainLayout一般为最外侧布局layout，用于计算屏幕的高度来判断当前软键盘是否为弹出状态
     * 
     * */
    public static void openkeyboard(View keyView,final EditText valueView,final View mainLayout){
    	keyView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				valueView.setInputType(InputType.TYPE_CLASS_NUMBER);
				valueView.setFocusableInTouchMode(true);
				valueView.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)valueView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(valueView, 0);
				mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = mainLayout.getRootView().getHeight() - mainLayout.getHeight();
								if (heightDiff > 100)
								{ // 说明键盘是弹出状态
									//ToastUtil.show(getApplicationContext(), "弹出", 2000);
								} else{
									valueView.setInputType(InputType.TYPE_NULL);
									valueView.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
								}
							}
						});
			}});
    }
    
    
    //---------------软键盘打开、关闭监听start-------------------//
    /**
     * 打开软键盘,带监听
     * 
     * @param keyView点击此控件后打开软键盘
     * 
     * @param valueView需要监听的EditText
     * 
     * @param mainLayout一般为最外侧布局layout，用于计算屏幕的高度来判断当前软键盘是否为弹出状态
     * 
     * */
    public  void openkeyboard2(View keyView,final EditText valueView,final View mainLayout){
    	keyView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				valueView.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
				valueView.setFocusableInTouchMode(true);
				valueView.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)valueView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(valueView, 0);
				mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = mainLayout.getRootView().getHeight() - mainLayout.getHeight();
								if (heightDiff > 100)
								{ // 说明键盘是弹出状态
									//ToastUtil.show(getApplicationContext(), "弹出", 2000);
									if(openkeyboardOpenStatusListener!=null){
										openkeyboardOpenStatusListener.onOpenkeyboardOpenStatusListener();
									}
								} else{
									valueView.setInputType(InputType.TYPE_NULL);
									valueView.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
									if(openkeyboardCloseStatusListener!=null){
										openkeyboardCloseStatusListener.onOpenkeyboardCloseStatusListener();
									}
								}
							}
						});
			}});
    }
    
    public interface OpenkeyboardOpenStatusListener{
    	public void onOpenkeyboardOpenStatusListener();
    }
    
    public interface OpenkeyboardCloseStatusListener{
    	public void onOpenkeyboardCloseStatusListener();
    }
    
    private OpenkeyboardOpenStatusListener openkeyboardOpenStatusListener;
    public void setOpenkeyboardOpenStatusListener(OpenkeyboardOpenStatusListener openkeyboardOpenStatusListener){
    	this.openkeyboardOpenStatusListener=openkeyboardOpenStatusListener;
    }
    
    private OpenkeyboardCloseStatusListener openkeyboardCloseStatusListener;
    public void setOpenkeyboardCloseStatusListener(OpenkeyboardCloseStatusListener openkeyboardCloseStatusListener){
    	this.openkeyboardCloseStatusListener=openkeyboardCloseStatusListener;
    }
    
    //---------------软键盘打开、关闭监听end-------------------//

}
