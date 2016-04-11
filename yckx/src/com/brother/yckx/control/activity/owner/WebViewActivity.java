package com.brother.yckx.control.activity.owner;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.WEBInterface;
import com.brother.yckx.R;

public class WebViewActivity extends BaseActivity{
	private WebView webView;
	private String action;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_aboutus);
		webView=(WebView) findViewById(R.id.webViewAboutus);
		//webView.loadUrl("https://www.hao123.com/");
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
			}
		});
		webView.setWebViewClient(webViewClient);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		action=GlobalServiceUtils.getGloubalServiceSession("WebViewActivity")+"";
		if(action.equals("aboutUs")){
			setActionBar(R.string.aboutus, R.drawable.btn_homeasup_default, NULL_ID, actionListener);
			webView.loadUrl(WEBInterface.ABOUTUS_RUL);
		}else if(action.equals("useAgreement")){
			setActionBar(R.string.useAgreement_tittle, R.drawable.btn_homeasup_default, NULL_ID, actionListener);
			webView.loadUrl(WEBInterface.YCKX_USE_AGREEMENT);
		}
		
	}
	
	private View.OnClickListener actionListener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
		   WebViewActivity.this.finish();
		}
	};
	
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		GlobalServiceUtils.setGloubalServiceSession("WebViewActivity","");
	}
	
	

}
