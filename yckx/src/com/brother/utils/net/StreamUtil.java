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

	/** ��ȡ���������ڵ����� -> �ַ��� : �ʺ�json���ݶ�ȡ */
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

	/** ��ȡ���������ڵ����ݱ��浽ָ���ļ��� : �ʺ�С�ļ����� */
	public static void saveToFile(InputStream inStream, File toFile) throws Exception {
		LogUtil.d("AA", "saveToFile: " + toFile.getAbsolutePath());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(toFile));
		// inStream ��ȡ��������
		BufferedInputStream bis = new BufferedInputStream(inStream);
		int len = 0;
		byte[] buff = new byte[256];
		while ((len = bis.read(buff)) != -1) {
			// toFile ����ȡ���������ݱ��浽����ȥ
			bos.write(buff, 0, len);
			bos.flush();
		}
		bis.close();
		bos.close();
	}
}
