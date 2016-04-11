package com.brother.yckx.view.banner;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.brother.yckx.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 鍥剧墖鍔犺浇宸ュ叿绫�
 * @author锛欽orge on 2015/10/13 12:14
 */
public class ImageLoaderHelper {

    public static final int IMG_LOAD_DELAY=200;
    private static ImageLoaderHelper imageLoaderHelper;
    private ImageLoader imageLoader;
    // 鏄剧ず鍥剧墖鐨勮缃�
    private DisplayImageOptions options;


    public static ImageLoaderHelper getInstance(){
        if(imageLoaderHelper==null)
            imageLoaderHelper = new ImageLoaderHelper();
        return  imageLoaderHelper;
    }
    public ImageLoaderHelper(){
        init();
    }

    /**
     * 閰嶇疆 imagloader 鍩烘湰淇℃伅
     */
    private void init(){
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.test_fragment_wowo_logo)
                .showImageForEmptyUri(R.drawable.test_fragment_wowo_logo)
                .showImageOnFail(R.drawable.test_failed)
                .delayBeforeLoading(IMG_LOAD_DELAY)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)	 //璁剧疆鍥剧墖鐨勮В鐮佺被鍨�
                .build();
        imageLoader = ImageLoader.getInstance();
    }

    /**
     * 鍔犺浇鍥剧墖
     * @param url
     * @param imageView
     */
    public  void loadImage(String url,ImageView imageView){
        url=url.trim();
        imageLoader.displayImage(url,imageView,options);
    }
}
