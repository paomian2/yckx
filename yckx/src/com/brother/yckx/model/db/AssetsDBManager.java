package com.brother.yckx.model.db;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

/** ������������Assets�ļ�����db���ݵĹ����� */
public class AssetsDBManager {
	
	/**
	 * ���Ʊ���Assets�е�db�ļ���ָ��Ŀ¼��
	 * 
	 * @param context
	 * @param path
	 *            Assets��db�ļ�·��
	 * @param toFile
	 *            Ŀ��λ��
	 * @throws IOException
	 */
	public static void copyAssetsFileToFile(Context context, String path, File toFile) throws IOException {
		InputStream inStream = context.getAssets().open(path);
		// ������(������ȡ��ǰ��Ŀ��Assets�ڵ�db�ı�)
		BufferedInputStream buffInputStream = new BufferedInputStream(inStream);
		// �����(��������ȡ����db��Ϣд��ָ��Ŀ¼�ļ�toFile��ȥ)
		BufferedOutputStream buffOutputStream = new BufferedOutputStream(new FileOutputStream(toFile, false));
		// IO����
		int len = 0;
		byte[] buff = new byte[2 * 1024];
		while ((len = buffInputStream.read(buff)) != -1) {
			buffOutputStream.write(buff, 0, len);
			buffOutputStream.flush();
		}
		// IO�ر�
		buffOutputStream.close();
		buffInputStream.close();
		inStream.close();
	}

}
