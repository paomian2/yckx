package com.brother.yckx.control.activity.owner;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.brother.BaseActivity;
import com.brother.BaseAdapter2;
import com.brother.utils.Contants;
import com.brother.yckx.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class PersonalLoadImgActivity extends BaseActivity{
	
	private GridView img_gridview;
	private List<String> urlList=new ArrayList<String>();
	private LoadImgeViewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_showload_img);
		img_gridview=(GridView) findViewById(R.id.img_gridview);
		adapter=new LoadImgeViewAdapter(this,imageLoader);
		img_gridview.setAdapter(adapter);
		donInbackground();
		
	}
	private void donInbackground() {
		for(int i=0;i<Contants.IMAGES.length;i++){
			urlList.add(Contants.IMAGES[i]);
		}
		adapter.addDataToAdapter(urlList);
		adapter.notifyDataSetChanged();
	}


	class LoadImgeViewAdapter extends BaseAdapter2<String>{
		
		private ImageLoader imageLoader;
		private DisplayImageOptions options;

		public LoadImgeViewAdapter(Context context,ImageLoader imageLoader) {
			super(context);
			this.imageLoader=imageLoader;
			/**
			 * ≈‰÷√
			 */
			options = new DisplayImageOptions.Builder()
						.showImageForEmptyUri(R.drawable.logo)
						.showImageOnFail(R.drawable.logo)
						.resetViewBeforeLoading(true)
						.cacheOnDisc(true)
						.imageScaleType(ImageScaleType.EXACTLY)
						.bitmapConfig(Bitmap.Config.RGB_565)
						.displayer(new FadeInBitmapDisplayer(300))
						.build();
		}

		@Override
		public View getView(final int position, View contentView, ViewGroup arg2) {
			final ViewHodler viewHolder;
			if(contentView==null){
				contentView=layoutInflater.inflate(R.layout.row_personal_showload_img, null);
				viewHolder=new ViewHodler(contentView);
				contentView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHodler) contentView.getTag();
			}
			String imgUrl=getItem(position);
			Log.d("yckx", imgUrl);
			//viewHolder.cacheImageView.setImageUrl(imgUrl);
			final ProgressBar spinner = (ProgressBar) contentView.findViewById(R.id.loading);
			imageLoader.displayImage(imgUrl, viewHolder.cacheImageView, options,new MySimpleImageLoadingListener(spinner));
			viewHolder.cacheImageView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(PersonalLoadImgActivity.this, PersonalSpaceImageDetailActivity.class);
					intent.putExtra("images", (ArrayList<String>) urlList);
					intent.putExtra("position", position);
					int[] location = new int[2];
					viewHolder.cacheImageView.getLocationOnScreen(location);
					intent.putExtra("locationX", location[0]);
					intent.putExtra("locationY", location[1]);

					intent.putExtra("width", viewHolder.cacheImageView.getWidth());
					intent.putExtra("height", viewHolder.cacheImageView.getHeight());
					startActivity(intent);
					overridePendingTransition(0, 0);

				}});
			return contentView;
		}
		
		/**
		 * …Ë÷√º”‘ÿº‡Ã˝
		 * @author qubian
		 *
		 */
		public class MySimpleImageLoadingListener extends SimpleImageLoadingListener
		{
			ProgressBar spinner;
			public MySimpleImageLoadingListener(ProgressBar spinner)
			{
				this.spinner= spinner;
			}
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				spinner.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {		
					case IO_ERROR:				
						message = "Input/Output error";
						break;
					case DECODING_ERROR:		
						message = "Image can't be decoded";
						break;
					case NETWORK_DENIED:		
						message = "Downloads are denied";
						break;
					case OUT_OF_MEMORY:		   
						message = "Out Of Memory error";
						break;
					case UNKNOWN:				
						message = "Unknown error";
						break;
				}
				Log.i("ImagePagerAdapter", message);
				spinner.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				spinner.setVisibility(View.GONE);		
			}
		
		}
		
		
		class ViewHodler{
			ImageView cacheImageView=null;
			public ViewHodler(View view){
				cacheImageView=(ImageView) view.findViewById(R.id.img_show);
			}
		}
		
	}

}
