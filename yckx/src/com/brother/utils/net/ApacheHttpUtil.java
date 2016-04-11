package com.brother.utils.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.brother.utils.WEBInterface;


/**
 * 基于Apache提供HTTP网络连接(HttpClient)工具类,主要提供了两个方法
 * 
 * @author 
 */
public class ApacheHttpUtil{
	private static HttpClient httpClient = null;

	private static HttpClient getDefaultHttpClient() {
		if (httpClient == null) {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 3000);
			httpClient = new DefaultHttpClient(params);
		}
		return httpClient;
	}

	public static String httpGet(String host, HashMap<String, String> params) {
		HttpClient httpClient = getDefaultHttpClient();
		String url = generateUrl(host, params);
		try {
			HttpGet httpGet = new HttpGet(url);
			// Http连接客户端 执行 -- 请求 -- 返回拿到响应
			HttpResponse httpResponse = httpClient.execute(httpGet);
			// 获取响应行 中的响应码
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 获取响应实体
				HttpEntity httpEntity = httpResponse.getEntity();
				// InputStream inputStream = httpEntity.getContent();
				// 利用实体工具，将响应实体转化为字符串
				String jsonstr = EntityUtils.toString(httpEntity);
				return jsonstr;
			}
		}
		catch (Exception e) {
			//LogUtil.d("ApacheHttpUtil", "httpGet exception \n" + url);
		}
		return null;
	}
	
	
	public static String httpGet(String host,String token, HashMap<String, String> params) {
		HttpClient httpClient = getDefaultHttpClient();
		String url = generateUrl(host, params);
		try {
			HttpGet httpGet = new HttpGet(url);
			// Http连接客户端 执行 -- 请求 -- 返回拿到响应
			httpGet.setHeader("token", token);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			// 获取响应行 中的响应码
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 获取响应实体
				HttpEntity httpEntity = httpResponse.getEntity();
				// InputStream inputStream = httpEntity.getContent();
				// 利用实体工具，将响应实体转化为字符串
				String jsonstr = EntityUtils.toString(httpEntity);
				return jsonstr;
			}
		}
		catch (Exception e) {
			//LogUtil.d("ApacheHttpUtil", "httpGet exception \n" + url);
		}
		return null;
	}

	public static String httpPost(String host, HashMap<String, String> params) {
		HttpClient httpClient = getDefaultHttpClient();
		HttpPost httpPost = new HttpPost(host);
		try {
			List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
			if (params != null && params.size() > 0) {
				Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, String> param = iterator.next();
					String key = param.getKey();
					String value = param.getValue();
					BasicNameValuePair pair = new BasicNameValuePair(key, value);
					pairs.add(pair);
				}
				
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
			}
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}
		}
		catch (Exception e) {
			//LogUtil.d("ApacheHttpUtil", "httpPost exception \n" + host);
		}
		return null;
	}
	
	
	public static String httpPost(String host, String token,HashMap<String, String> params) {
		HttpClient httpClient = getDefaultHttpClient();
		HttpPost httpPost = new HttpPost(host);
		httpPost.setHeader("token", token);
		try {
			List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
			if (params != null && params.size() > 0) {
				Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, String> param = iterator.next();
					String key = param.getKey();
					String value = param.getValue();
					BasicNameValuePair pair = new BasicNameValuePair(key, value);
					pairs.add(pair);
				}
				
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
			}
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}
		}
		catch (Exception e) {
			//LogUtil.d("ApacheHttpUtil", "httpPost exception \n" + host);
		}
		return null;
	}
	
	public static String httpPost(String host, AbstractHttpEntity entity) {
		HttpClient httpClient = getDefaultHttpClient();
		HttpPost httpPost = new HttpPost(host);
		try {
			entity.setContentType(new BasicHeader("Content-Type","application/json"));
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}		
				
		}
		catch (Exception e) {
			//LogUtil.d("ApacheHttpUtil", "httpPost exception \n" + host);
		}
		return null;
	}
	
	/**
	 * Example
	 * String respose=ApacheHttpUtil.httpPost(WEBInterface.LOGINURL,new StringEntity(new JSONObject(hasmp).toString(),"UTF-8"));
	 * 
	 * */
	public static String httpPost2(String host, String token,AbstractHttpEntity entity) {
		HttpClient httpClient = getDefaultHttpClient();
		HttpPost httpPost = new HttpPost(host);
		httpPost.setHeader("token", token);
		try {
			entity.setContentType(new BasicHeader("Content-Type","application/json"));
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}		
				
		}
		catch (Exception e) {
			//LogUtil.d("ApacheHttpUtil", "httpPost exception \n" + host);
		}
		return null;
	}
	

	/** 拼接链接完整URL */
	private static String generateUrl(String url, Map<String, String> params) {
		StringBuilder urlBuilder = new StringBuilder(url);
		// web接口: url?key=val&key=val&key=val;
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
}
