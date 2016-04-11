package com.brother.utils.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpStatus;


import android.util.Log;

/**
 * ����Java�ṩHTTP�������ӹ�����,��Ҫ�ṩ����������
 * 
 * @see get���� {@link #httpGet(String, HashMap)};
 * 
 * @see post���� {@link #httpPost(String, HashMap)};
 * 
 * @author yuanc
 */
public class JavaHttpUtil{
	public static InputStream httpGet(String host, HashMap<String, String> params) {
		String url = generateUrl(host, params);
		try {
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setUseCaches(false); // ��ʹ�û���
			conn.setConnectTimeout(5000);// �������������ӳ�ʱʱ��
			conn.setReadTimeout(3000);// ���������Ӷ�ȡ��ʱʱ��
			conn.setRequestMethod("GET"); // ��������������ʽ
			conn.setDoInput(true); // ����������֧�ֻ�ȡ����
			conn.setDoOutput(false);// ����������֧���ϴ�����
			conn.connect();
			int code = conn.getResponseCode();
			if (code == HttpStatus.SC_OK) { // 200
				return conn.getInputStream();
			}
		}
		catch (Exception e) {
			//LogUtil.d("JavaHttpUtil", "httpGet exception \n" + url);
		}
		return null;
	}

	/** ƴ����������URL */
	private static String generateUrl(String url, Map<String, String> params) {
		StringBuilder urlBuilder = new StringBuilder(url);
		// web�ӿ�: url?key=val&key=val&key=val;
		if (null != params) {
			urlBuilder.append("?");
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> param = iterator.next();
				String key = param.getKey();
				String value = param.getValue();
				urlBuilder.append(key).append('=').append(value);
				if (iterator.hasNext()) {
					urlBuilder.append('&');
				}
			}
		}
		return urlBuilder.toString();
	}

	public static InputStream httpPost(String host, HashMap<String, String> param) {
		try {
			URL urlObj = new URL(host);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setUseCaches(false); // ��ʹ�û���
			conn.setConnectTimeout(5000);// �������������ӳ�ʱʱ��
			conn.setReadTimeout(3000);// ���������Ӷ�ȡ��ʱʱ��
			conn.setRequestMethod("POST"); // ��������������ʽ
			conn.setDoInput(true); // ����������֧�ֻ�ȡ����
			conn.setDoOutput(true);// ���������Ӳ�֧���ϴ�����
			// ���������ӽ��ϴ�����Ϣ
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStream outStream = conn.getOutputStream();
			if (param != null) {
				outStream.write(mapToString(param).getBytes());
			}
			conn.connect();
			if (conn.getResponseCode() == 200) {
				return conn.getInputStream();
			}
		}
		catch (Exception e) {
			//LogUtil.d("JavaHttpUtil", "httpPost exception \n" + host);
		}
		return null;
	}
	
	
	public static InputStream httpPost2(String host,String token, HashMap<String, String> param) {
		try {
			URL urlObj = new URL(host);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setUseCaches(false); // ��ʹ�û���
			conn.setConnectTimeout(5000);// �������������ӳ�ʱʱ��
			conn.setReadTimeout(3000);// ���������Ӷ�ȡ��ʱʱ��
			conn.setRequestMethod("POST"); // ��������������ʽ
			conn.setDoInput(true); // ����������֧�ֻ�ȡ����
			conn.setDoOutput(true);// ���������Ӳ�֧���ϴ�����
			// ���������ӽ��ϴ�����Ϣ
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("token", token);
			OutputStream outStream = conn.getOutputStream();
			if (param != null) {
				outStream.write(mapToString(param).getBytes());
			}
			conn.connect();
			if (conn.getResponseCode() == 200) {
				return conn.getInputStream();
			}
		}
		catch (Exception e) {
			//LogUtil.d("JavaHttpUtil", "httpPost exception \n" + host);
		}
		return null;
	}

	/** ��HasmMap�����ݰ�key:val��ʽƴ�����ӳ��ַ��� */
	private static String mapToString(HashMap<String, String> param) {
		// key=val&key=val&key=val
		StringBuffer strb = new StringBuffer();
		Set<String> keys = param.keySet();
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String val = param.get(key);
			strb.append(key);
			strb.append("=");
			strb.append(val);
			strb.append("&");
		}
		strb.delete(strb.length() - 1, strb.length());
		return strb.toString();
	}
}
