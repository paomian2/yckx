package com.brother.utils.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.brother.utils.logic.LogUtil;


public class StreamUtil {

	/** 读取此输入流内的数据 -> 字符串 : 适合json数据读取 */
	public static String getString(InputStream inStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(baos);

		BufferedInputStream bis = new BufferedInputStream(inStream);
		int len = 0;
		byte[] buff = new byte[128];
		while ((len = bis.read(buff)) != -1) {
			bos.write(buff, 0, len);
		}
		baos.flush();
		bos.flush();
		buff = baos.toByteArray();

		bis.close();
		inStream.close();
		bos.close();
		baos.close();
		return new String(buff, "UTF-8");
	}

	/** 读取此输入流内的数据保存到指定文件中 : 适合小文件下载 */
	public static void saveToFile(InputStream inStream, File toFile) throws Exception {
		LogUtil.d("AA", "saveToFile: " + toFile.getAbsolutePath());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(toFile));
		// inStream 读取下来数据
		BufferedInputStream bis = new BufferedInputStream(inStream);
		int len = 0;
		byte[] buff = new byte[256];
		while ((len = bis.read(buff)) != -1) {
			// toFile 将读取下来的数据保存到这里去
			bos.write(buff, 0, len);
			bos.flush();
		}
		bis.close();
		bos.close();
	}
}
