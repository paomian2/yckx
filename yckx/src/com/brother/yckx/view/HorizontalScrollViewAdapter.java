package com.brother.yckx.view;

import java.util.List;

import com.brother.yckx.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalScrollViewAdapter
{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<Integer> mDatas;//加载资源的图片
	private List<String> mStringDatas;//加载指定网络地址的图片(后前要写成实体类)
	private boolean useIntenger;
	ImageLoader imageLoader;
	ViewHolder viewHolder = null;
	public HorizontalScrollViewAdapter(Context context, List<Integer> mDatas)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
		useIntenger=true;
	}

	public HorizontalScrollViewAdapter(Context context,List<String> mStringDatas,ImageLoader imageLoader){
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mStringDatas = mStringDatas;
		useIntenger=false;
		this.imageLoader=imageLoader;
	}


	public int getCount()
	{
		if(useIntenger){
			return mDatas.size();
		}else{
			return mStringDatas.size();
		}
	}

	public Object getItem(int position)
	{
		if(useIntenger){
			return mDatas.get(position);
		}else{
			return mStringDatas.get(position);
		}
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.activity_index_gallery_item, parent, false);
			viewHolder.mImg = (ImageView) convertView
					.findViewById(R.id.id_index_gallery_item_image);
			viewHolder.mText = (TextView) convertView
					.findViewById(R.id.id_index_gallery_item_text);

			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(useIntenger){
			viewHolder.mImg.setImageResource(mDatas.get(position));
			viewHolder.mText.setText("some info ");
		}else{
			DisplayImageOptions options=/**
					 * 配置
					 */
					 new DisplayImageOptions.Builder()
								.showImageForEmptyUri(R.drawable.logo)
								.showImageOnFail(R.drawable.logo)
								.resetViewBeforeLoading(true)
								.cacheOnDisc(true)
								.imageScaleType(ImageScaleType.EXACTLY)
								.bitmapConfig(Bitmap.Config.RGB_565)
								.displayer(new FadeInBitmapDisplayer(300))
								.build();
			imageLoader.displayImage(mStringDatas.get(position), viewHolder.mImg);
			
			viewHolder.mText.setText("some info ");
		}
		

		return convertView;
	}

	private class ViewHolder
	{//com.brother.yckx.view.CircleImageView
		//CircleImageView mImg;
		ImageView mImg;
		TextView mText;
	}

}
