package com.brother.yckx.view.banner;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.RandomAccessFile;

/**
 *   鎿嶄綔鏂囦欢 鐨勫伐鍏风被
 */
public class FileUtils {

	public static final String ROOT_DIR = "AppCartoon";
	public static final String DOWNLOAD_DIR = "download";
	public static final String CACHE_DIR = "cache";
	public static final String ICON_DIR = "pics";

	/** 鍒ゆ柇SD鍗℃槸鍚︽寕杞� */
	public static boolean isSDCardAvailable() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}

	/** 鑾峰彇涓嬭浇鐩綍 */
	public static String getDownloadDir(Context context) {
		return getDir(DOWNLOAD_DIR,context);
	}

	/** 鑾峰彇缂撳瓨鐩綍 */
	public static String getCacheDir(Context context) {
		return getDir(CACHE_DIR, context);
	}

	/** 鑾峰彇icon鐩綍 */
	public static String getIconDir(Context context) {
		return getDir(ICON_DIR,context);
	}

	/** 鑾峰彇搴旂敤鐩綍锛屽綋SD鍗″瓨鍦ㄦ椂锛岃幏鍙朣D鍗′笂鐨勭洰褰曪紝褰揝D鍗′笉瀛樺湪鏃讹紝鑾峰彇搴旂敤鐨刢ache鐩綍 */
	public static String getDir(String name,Context context) {
		StringBuilder sb = new StringBuilder();
		if (isSDCardAvailable()) {
			sb.append(getExternalStoragePath());
		} else {
			sb.append(getCachePath(context));
		}
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		if (createDirs(path)) {
			return path;
		} else {
			return null;
		}
	}

	/** 鑾峰彇SD涓嬬殑搴旂敤鐩綍 */
	public static String getExternalStoragePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 鑾峰彇搴旂敤鐨刢ache鐩綍 */
	public static String getCachePath( Context context) {
		File f =context.getCacheDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + "/";
		}
	}

	/** 鍒涘缓鏂囦欢澶� */
	public static boolean createDirs(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			return file.mkdirs();
		}
		return true;
	}

	/**
	 * 鎶婂瓧绗︿覆鏁版嵁鍐欏叆鏂囦欢
	 * @param content 闇�瑕佸啓鍏ョ殑瀛楃涓�
	 * @param path    鏂囦欢璺緞鍚嶇О
	 * @param append  鏄惁浠ユ坊鍔犵殑妯″紡鍐欏叆
	 * @return 鏄惁鍐欏叆鎴愬姛
	 */
	public static boolean writeFile(byte[] content, String path, boolean append) {
		boolean res = false;
		File f = new File(path);
		RandomAccessFile raf = null;
		try {
			if (f.exists()) {
				if (!append) {
					f.delete();
					f.createNewFile();
				}
			} else {
				f.createNewFile();
			}
			if (f.canWrite()) {
				raf = new RandomAccessFile(f, "rw");
				raf.seek(raf.length());
				raf.write(content);
				res = true;
			}
		} catch (Exception e) {
//			LogUtils.e(e);
		} finally {
//			IOUtils.close(raf);
		}
		return res;
	}

}
