package com.brother.yckx.view.image;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

/** 缓存图像处理 */
public class CacheImage{
	// 内存缓存(显示过的图像将存到运行内存中，最近最少使用原则替换)
	private static LruCache<String, Bitmap> memoryCache;
	// 本地缓存路径
	private static String diskCachePath;
	// 本地缓存路径是否可行可行的标记(默认不可用)
	private static boolean diskCacheEnabled;
	// 单线程池(用来做图像的保存工作)
	private static ExecutorService writerThread = Executors.newSingleThreadExecutor();
	// 图像加载url地址 (注意为非静态，每个缓存图像对象有一个url地址)
	private String url;
	// 是否取消
	private boolean cancel;

	@SuppressLint("SdCardPath")
	protected CacheImage(Context context, String url) {
		this.url = url;
		// 初始内存缓存
		if (memoryCache == null) {
			memoryCache = new LruCache<String, Bitmap>(8 * 1024 * 1024){
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
			};
		}
		// 初始本地缓存
		if (diskCachePath == null || diskCachePath.equals("")) {
			// 初始设定本地缓存文件目录 @see diskCachePath
			String cacheDir = "/sdcard/yckx";
			diskCachePath = cacheDir + "/" + "CacheImage";
			// 用来准备缓存图像的文件夹
			File cacheFileDir = new File(diskCachePath);
			if (!cacheFileDir.exists()) {
				cacheFileDir.mkdirs(); // 创建此文件夹
			}
			// 创建完成后，再次来检测创建是否完成(此文件夹是否可行，可用)
			diskCacheEnabled = cacheFileDir.exists();
		}
	}

	public void cancel() {
		this.cancel = true;
	}

	/**
	 * 获取图像的方法
	 * 
	 * @see 1 内存获取{@link #getBitmapFromMemory(String)}
	 * @see 2 本地获取{@link #getBitmapFromDisk(String)}
	 * @see 3 网络获取{@link #getBitmapFromUrl(String)}
	 */
	public Bitmap getBitmap() {
		Bitmap bitmap = null;
		// #1 内存获取
		bitmap = getBitmapFromMemory(url);
		Log.d("yckx", "开始获取bitma");
		if (bitmap != null) {
			Log.d("CacheImage", "getBitmapFromMemory");
			return bitmap;
		}else{
			Log.d("CacheImage", "getBitmapFromMemory--null");
		}
		// #2 本地获取
		bitmap = getBitmapFromDisk(url);
		if (bitmap != null) {
			Log.d("CacheImage", "getBitmapFromDisk");
			Log.d("CacheImage", "getBitmapFromMemory--null");
			return bitmap;
		}
		// #3 网络获取
		bitmap = getBitmapFromUrl(url);
		if (bitmap != null) {
			Log.d("CacheImage", "getBitmapFromUrl");
			return bitmap;
		}
		return bitmap;
	}

	// 从内存获取图像
	private Bitmap getBitmapFromMemory(String url) {
		String key = getCacheKey(url); // 用图像url做为key
		Bitmap bitmap = memoryCache.get(key); // 到内存中获取图像
		return bitmap;
	}

	/**
	 * 从本地获取图像
	 * 
	 * @see 请保证和本地图像保存的路径一至性,{@link #saveToDisk(String, Bitmap)}
	 */
	private Bitmap getBitmapFromDisk(String url) {
		Bitmap bitmap = null;
		// 准备好文件的路径 (diskCachePath + url)
		String filePath = getFilePath(url);
		// 检测一下此文件是否存在
		File bitmapFile = new File(filePath);
		if (bitmapFile.exists()) {
			bitmap = BitmapFactory.decodeFile(filePath);
			if (bitmap != null) {
				saveToMemory(url, bitmap); // 将图像保存到内存
			}
		}
		return bitmap;
	}

	// 从网络获取图像
	private Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = null;
		try {
			Log.d("CacheImage", "start getBitmapFromUrl->" + url);
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.connect();
			InputStream stream = conn.getInputStream();
			// 网络加载图像
			BitmapFactory.Options options=new BitmapFactory.Options();
		    options.inSampleSize = 10;
			bitmap = BitmapFactory.decodeStream(stream,null,options);
			// 如果加载成功 -> 缓存图像
			if (bitmap != null) {
				saveToMemory(url, bitmap); // 将图像保存到内存
				saveToDisk(url, bitmap);// 将图像保存到本地
			}
			stream.close();
		}
		catch (Exception e) {
			Log.d("CacheImage", "getBitmapFromUrl()! Exception!");
			return null;
		}
		return bitmap;
	}

	/**
	 * 保存图像到本地缓存
	 * 
	 * @see 请保证和本地图像获取的路径一至 {@link #getBitmapFromDisk(String)}
	 */
	private void saveToDisk(final String url, final Bitmap bitmap) {
		// 缓存目录是否可行可用
		if (diskCacheEnabled) {
			// 执行图像文件压缩保存工作
			writerThread.execute(new Runnable(){
				@Override
				public void run() {
					if (!cancel) {
						try {
							// 图像文件保存路径(请保证和获取图像路径一至)
							String filePath = getFilePath(url);
							Log.d("yckx", filePath+"");
							ByteArrayOutputStream baos = new ByteArrayOutputStream();  
							int options =100;//个人喜欢从80开始,  
							bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);  
							while (baos.toByteArray().length / 1024 > 300) {   
								baos.reset();  
								options -= 10;  
								bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);  
							}
							File bitmapFile = new File(filePath);
							FileOutputStream fos = new FileOutputStream(bitmapFile);  
							fos.write(baos.toByteArray());  
							fos.flush();  
							fos.close(); 
							Log.d("CacheImage", "saveToDisk");
						}
						catch (Exception e) {
							Log.d("CacheImage", "saveToDisk! Exception! ");
						}
					}
				}
			});
		}
	}

	// 保存图像到内存缓存
	private void saveToMemory(String url, Bitmap bitmap) {
		String key = getCacheKey(url);
		memoryCache.put(key, bitmap);
		Log.d("CacheImage", "saveToMemory");
	}

	private String getFilePath(String url) {
		return diskCachePath + "/" + getCacheKey(url);
	}

	// 通过url取出key,去除.:等，替换成字符串+表示
	private static String getCacheKey(String url) {
		if (url == null) {
			throw new RuntimeException("Null url Exception");
		}
		else {
			return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+")+".png";
		}
	}
}
