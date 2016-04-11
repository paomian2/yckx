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

/** ����ͼ���� */
public class CacheImage{
	// �ڴ滺��(��ʾ����ͼ�񽫴浽�����ڴ��У��������ʹ��ԭ���滻)
	private static LruCache<String, Bitmap> memoryCache;
	// ���ػ���·��
	private static String diskCachePath;
	// ���ػ���·���Ƿ���п��еı��(Ĭ�ϲ�����)
	private static boolean diskCacheEnabled;
	// ���̳߳�(������ͼ��ı��湤��)
	private static ExecutorService writerThread = Executors.newSingleThreadExecutor();
	// ͼ�����url��ַ (ע��Ϊ�Ǿ�̬��ÿ������ͼ�������һ��url��ַ)
	private String url;
	// �Ƿ�ȡ��
	private boolean cancel;

	@SuppressLint("SdCardPath")
	protected CacheImage(Context context, String url) {
		this.url = url;
		// ��ʼ�ڴ滺��
		if (memoryCache == null) {
			memoryCache = new LruCache<String, Bitmap>(8 * 1024 * 1024){
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
			};
		}
		// ��ʼ���ػ���
		if (diskCachePath == null || diskCachePath.equals("")) {
			// ��ʼ�趨���ػ����ļ�Ŀ¼ @see diskCachePath
			String cacheDir = "/sdcard/yckx";
			diskCachePath = cacheDir + "/" + "CacheImage";
			// ����׼������ͼ����ļ���
			File cacheFileDir = new File(diskCachePath);
			if (!cacheFileDir.exists()) {
				cacheFileDir.mkdirs(); // �������ļ���
			}
			// ������ɺ��ٴ�����ⴴ���Ƿ����(���ļ����Ƿ���У�����)
			diskCacheEnabled = cacheFileDir.exists();
		}
	}

	public void cancel() {
		this.cancel = true;
	}

	/**
	 * ��ȡͼ��ķ���
	 * 
	 * @see 1 �ڴ��ȡ{@link #getBitmapFromMemory(String)}
	 * @see 2 ���ػ�ȡ{@link #getBitmapFromDisk(String)}
	 * @see 3 �����ȡ{@link #getBitmapFromUrl(String)}
	 */
	public Bitmap getBitmap() {
		Bitmap bitmap = null;
		// #1 �ڴ��ȡ
		bitmap = getBitmapFromMemory(url);
		Log.d("yckx", "��ʼ��ȡbitma");
		if (bitmap != null) {
			Log.d("CacheImage", "getBitmapFromMemory");
			return bitmap;
		}else{
			Log.d("CacheImage", "getBitmapFromMemory--null");
		}
		// #2 ���ػ�ȡ
		bitmap = getBitmapFromDisk(url);
		if (bitmap != null) {
			Log.d("CacheImage", "getBitmapFromDisk");
			Log.d("CacheImage", "getBitmapFromMemory--null");
			return bitmap;
		}
		// #3 �����ȡ
		bitmap = getBitmapFromUrl(url);
		if (bitmap != null) {
			Log.d("CacheImage", "getBitmapFromUrl");
			return bitmap;
		}
		return bitmap;
	}

	// ���ڴ��ȡͼ��
	private Bitmap getBitmapFromMemory(String url) {
		String key = getCacheKey(url); // ��ͼ��url��Ϊkey
		Bitmap bitmap = memoryCache.get(key); // ���ڴ��л�ȡͼ��
		return bitmap;
	}

	/**
	 * �ӱ��ػ�ȡͼ��
	 * 
	 * @see �뱣֤�ͱ���ͼ�񱣴��·��һ����,{@link #saveToDisk(String, Bitmap)}
	 */
	private Bitmap getBitmapFromDisk(String url) {
		Bitmap bitmap = null;
		// ׼�����ļ���·�� (diskCachePath + url)
		String filePath = getFilePath(url);
		// ���һ�´��ļ��Ƿ����
		File bitmapFile = new File(filePath);
		if (bitmapFile.exists()) {
			bitmap = BitmapFactory.decodeFile(filePath);
			if (bitmap != null) {
				saveToMemory(url, bitmap); // ��ͼ�񱣴浽�ڴ�
			}
		}
		return bitmap;
	}

	// �������ȡͼ��
	private Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = null;
		try {
			Log.d("CacheImage", "start getBitmapFromUrl->" + url);
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.connect();
			InputStream stream = conn.getInputStream();
			// �������ͼ��
			BitmapFactory.Options options=new BitmapFactory.Options();
		    options.inSampleSize = 10;
			bitmap = BitmapFactory.decodeStream(stream,null,options);
			// ������سɹ� -> ����ͼ��
			if (bitmap != null) {
				saveToMemory(url, bitmap); // ��ͼ�񱣴浽�ڴ�
				saveToDisk(url, bitmap);// ��ͼ�񱣴浽����
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
	 * ����ͼ�񵽱��ػ���
	 * 
	 * @see �뱣֤�ͱ���ͼ���ȡ��·��һ�� {@link #getBitmapFromDisk(String)}
	 */
	private void saveToDisk(final String url, final Bitmap bitmap) {
		// ����Ŀ¼�Ƿ���п���
		if (diskCacheEnabled) {
			// ִ��ͼ���ļ�ѹ�����湤��
			writerThread.execute(new Runnable(){
				@Override
				public void run() {
					if (!cancel) {
						try {
							// ͼ���ļ�����·��(�뱣֤�ͻ�ȡͼ��·��һ��)
							String filePath = getFilePath(url);
							Log.d("yckx", filePath+"");
							ByteArrayOutputStream baos = new ByteArrayOutputStream();  
							int options =100;//����ϲ����80��ʼ,  
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

	// ����ͼ���ڴ滺��
	private void saveToMemory(String url, Bitmap bitmap) {
		String key = getCacheKey(url);
		memoryCache.put(key, bitmap);
		Log.d("CacheImage", "saveToMemory");
	}

	private String getFilePath(String url) {
		return diskCachePath + "/" + getCacheKey(url);
	}

	// ͨ��urlȡ��key,ȥ��.:�ȣ��滻���ַ���+��ʾ
	private static String getCacheKey(String url) {
		if (url == null) {
			throw new RuntimeException("Null url Exception");
		}
		else {
			return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+")+".png";
		}
	}
}
