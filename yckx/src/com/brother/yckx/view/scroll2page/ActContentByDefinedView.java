/*package com.brother.yckx.view.scroll2page; 
  
import java.util.ArrayList;
import java.util.List;

import com.brother.yckx.R;

import android.app.Activity;
import android.graphics.Bitmap;
/////import android.graphics.drawable.Drawable.Callback;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
  
*//** 
* 利用自定义的ScrollView加载视图来实现翻页效果，下面用页码和总页数标识当前的视图是第几屏 
*  
* @author WANGXIAOHONG 
*  
*//*
public class ActContentByDefinedView extends Activity { 
	private LinearLayout mLinearLayout; 
	private LinearLayout.LayoutParams param; 
	private DefinedScrollView scrollView; 
	private LayoutInflater inflater; 
	private TextView mTextView; 
	private ImageView mImageViewMagaPic; 
	private int pageCount = 0; 
  
  
	private void setupView() { 
		scrollView = (DefinedScrollView) findViewById(R.id.definedview); 
		mTextView = (TextView) findViewById(R.id.text_page); 
	  
		pageCount = 2;
	  
		mTextView.setText(1 + "/" + pageCount); 
	  
		for (int i = 0; i < pageCount; i++) { 
			param = new LinearLayout.LayoutParams( 
			android.view.ViewGroup.LayoutParams.FILL_PARENT, 
			android.view.ViewGroup.LayoutParams.FILL_PARENT); 
			inflater = this.getLayoutInflater(); 
		  
			if (i == 0) { 
				final View addview = inflater.inflate( R.layout.activity_main_first, null); 
				
				
				addview.findViewById(R.id.btn_first).setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						Toast.makeText(getApplicationContext(), "hahah", 2000).show();
						
					}});
			  
				mLinearLayout = new LinearLayout(this); 
				mLinearLayout.addView(addview, param); 
				scrollView.addView(mLinearLayout); 
			} else { 
				View addview = inflater.inflate(R.layout.activity_main_two, null); 
				
				addview.findViewById(R.id.tv_two).setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						Toast.makeText(getApplicationContext(), "adddwww", 2000).show();
						
					}});
				
			  
				mLinearLayout = new LinearLayout(this); 
				mLinearLayout.addView(addview, param); 
				scrollView.addView(mLinearLayout); 
			} 
		  
	} 

	scrollView.setPageListener(new DefinedScrollView.PageListener() {
		@Override
		public void page(int page) { 
		setCurPage(page); 
		} 
	}); 
} 
  
	private void setCurPage(int page) { 
		mTextView.setText((page + 1) + "/" + pageCount); 
	} 
  
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main); 
		setupView(); 
	}
}*/