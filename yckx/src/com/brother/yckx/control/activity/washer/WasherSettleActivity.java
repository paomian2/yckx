package com.brother.yckx.control.activity.washer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.brother.BaseActivity;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.yckx.R;
/**
 * 美护师结算
 * */
public class WasherSettleActivity extends BaseActivity{
	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settle);
		setActionBar(R.string.settle_title, R.drawable.btn_homeasup_default, NULL_ID, new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		init();
	}
	
	private void init(){
		webView=(WebView) findViewById(R.id.webview);
		String token=PrefrenceConfig.getUserMessage(WasherSettleActivity.this).getUserToken();
		String host=WEBInterface.SETTLEURL2+token;
		Log.d("yckx", "洗护师结算url:"+host);
		webView.loadUrl(host);
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
			}
		});
		webView.setWebViewClient(webViewClient);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	}
	
	
	private WebViewClient webViewClient=new WebViewClient(){
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;

		};

		//开始加载
		public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			System.out.println("----->>>开始加载");
		};

		//加载介绍
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			System.out.println("-------->>加载结束");
		};

		//加载出错
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			System.out.println("------->>加载出错");
		};

	};

}
