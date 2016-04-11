package com.brother.yckx.view.banner;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brother.yckx.R;

import java.util.ArrayList;

/**
 *
 *
 */
public class ImageCycleView extends LinearLayout {
	/**
	 * 涓婁笅鏂�
	 */
	private Context mContext;
	/**
	 * 鍥剧墖杞挱瑙嗗浘
	 */
	private ViewPager mAdvPager = null;
	/**
	 * 婊氬姩鍥剧墖瑙嗗浘閫傞厤
	 */
	private ImageCycleAdapter mAdvAdapter;
	/**
	 * 鍥剧墖杞挱鎸囩ず鍣ㄦ帶浠�
	 */
	private ViewGroup mGroup;

	/**
	 * 鍥剧墖杞挱鎸囩ず涓浘
	 */
	private ImageView mImageView = null;

	/**
	 * 婊氬姩鍥剧墖鎸囩ず瑙嗗浘鍒楄〃
	 */
	private ImageView[] mImageViews = null;

	/**
	 * 鍥剧墖婊氬姩褰撳墠鍥剧墖涓嬫爣
	 */
	private int mImageIndex = 0;
	/**
	 * 鎵嬫満瀵嗗害
	 */
	private float mScale;
	private boolean isStop;
	private TextView imageName;
	private  ArrayList<String> mImageDescList;
	/**
	 * @param context
	 */
	public ImageCycleView(Context context) {
		super(context);
		init(context) ;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ImageCycleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context) ;
	}

	private void init(Context context){
		mContext = context;
		mScale = context.getResources().getDisplayMetrics().density;
		LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
		//mAdvPager.addOnPageChangeListener(new GuidePageChangeListener());
		mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
		mAdvPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						// 寮�濮嬪浘鐗囨粴鍔�
						startImageTimerTask();
						break;
					default:
						// 鍋滄鍥剧墖婊氬姩
						stopImageTimerTask();
						break;
				}
				return false;
			}
		});
		// 婊氬姩鍥剧墖鍙充笅鎸囩ず鍣ㄨ
		mGroup = (ViewGroup) findViewById(R.id.circles);
		imageName = (TextView) findViewById(R.id.viewGroup2);
	}


	/**
	 * 瑁呭～鍥剧墖鏁版嵁
	 *
	 * @param  :
	 * @param imageCycleViewListener
	 */
	public void setImageResources(ArrayList<String> imageDesList,ArrayList<String> imageUrlList,ImageCycleViewListener imageCycleViewListener) {
		mImageDescList=imageDesList;
		if(imageUrlList!=null&&imageUrlList.size()>0){
			this.setVisibility(View.VISIBLE);
		}else{
			this.setVisibility(View.GONE);
			return;
		}

		// 娓呴櫎
		mGroup.removeAllViews();
		// 鍥剧墖骞垮憡鏁伴噺
		final int imageCount = imageUrlList.size();
		mImageViews = new ImageView[imageCount];
		for (int i = 0; i < imageCount; i++) {
			mImageView = new ImageView(mContext);
			int imageParams = (int) (mScale * 10 + 0.5f);// XP涓嶥P杞崲锛岄�傚簲搴斾笉鍚屽垎杈ㄧ巼
			int imagePadding = (int) (mScale * 5 + 0.5f);
			LayoutParams params=new LayoutParams(imageParams,imageParams);
			params.leftMargin=10;
			mImageView.setScaleType(ScaleType.FIT_XY);
			mImageView.setLayoutParams(params);
			mImageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);

			mImageViews[i] = mImageView;
			if (i == 0) {
				mImageViews[i].setBackgroundResource(R.drawable.test_fragment_wowo_logo);
			} else {
				mImageViews[i].setBackgroundResource(R.drawable.test_fragment_wowo_logo);
			}
			mGroup.addView(mImageViews[i]);
		}

		imageName.setText(imageDesList.get(0));
		imageName.setTextColor(getResources().getColor(R.color.blue));
		mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList, imageDesList,imageCycleViewListener);
		mAdvPager.setAdapter(mAdvAdapter);
		startImageTimerTask();
	}

	/**
	 * 鍥剧墖杞挱(鎵嬪姩鎺у埗鑷姩杞挱涓庡惁锛屼究浜庤祫婧愭帶浠讹級
	 */
	public void startImageCycle() {
		startImageTimerTask();
	}

	/**
	 * 鏆傚仠杞挱鈥旂敤浜庤妭鐪佽祫婧�
	 */
	public void pushImageCycle() {
		stopImageTimerTask();
	}

	/**
	 * 鍥剧墖婊氬姩浠诲姟
	 */
	private void startImageTimerTask() {
		stopImageTimerTask();
		// 鍥剧墖婊氬姩
		mHandler.postDelayed(mImageTimerTask, 3000);
	}

	/**
	 * 鍋滄鍥剧墖婊氬姩浠诲姟
	 */
	private void stopImageTimerTask() {
		isStop=true;
		mHandler.removeCallbacks(mImageTimerTask);
	}

	private Handler mHandler = new Handler();

	/**
	 * 鍥剧墖鑷姩杞挱Task
	 */
	private Runnable mImageTimerTask = new Runnable() {
		@Override
		public void run() {
			if (mImageViews != null) {
				mAdvPager.setCurrentItem(mAdvPager.getCurrentItem()+1);
				if(!isStop){  //if  isStop=true   //褰撲綘閫�鍑哄悗 瑕佹妸杩欎釜缁欏仠涓嬫潵 涓嶇劧 杩欎釜涓�鐩村瓨鍦� 灏变竴鐩村湪鍚庡彴寰幆
					mHandler.postDelayed(mImageTimerTask, 3000);
				}

			}
		}
	};

	/**
	 * 杞挱鍥剧墖鐩戝惉
	 *
	 * @author minking
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE)
				startImageTimerTask();
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int index) {
			index=index%mImageViews.length;
			// 璁剧疆褰撳墠鏄剧ず鐨勫浘鐗�
			mImageIndex = index;
			// 璁剧疆鍥剧墖婊氬姩鎸囩ず鍣ㄨ儗
			mImageViews[index].setBackgroundResource(R.drawable.test_fragment_wowo_logo);
			imageName.setText(mImageDescList.get(index));
			for (int i = 0; i < mImageViews.length; i++) {
				if (index != i) {
					mImageViews[i].setBackgroundResource(R.drawable.test_fragment_wowo_logo);
				}
			}
		}
	}

	private class ImageCycleAdapter extends PagerAdapter {

		/**
		 * 鍥剧墖瑙嗗浘缂撳瓨鍒楄〃
		 */
		private ArrayList<ClickableImageView> mImageViewCacheList;

		/**
		 * 鍥剧墖璧勬簮鍒楄〃
		 */
		private ArrayList<String> mAdList = new ArrayList<String>();
		private ArrayList<String> nameList = new ArrayList<String>();

		/**
		 * 骞垮憡鍥剧墖鐐瑰嚮鐩戝惉
		 */
		private ImageCycleViewListener mImageCycleViewListener;

		private Context mContext;
		public ImageCycleAdapter(Context context, ArrayList<String> adList,ArrayList<String> nameList, ImageCycleViewListener imageCycleViewListener) {
			this.mContext = context;
			this.mAdList = adList;
			this.nameList = nameList;
			mImageCycleViewListener = imageCycleViewListener;
			mImageViewCacheList = new ArrayList<ClickableImageView>();
		}

		@Override
		public int getCount() {
//			return mAdList.size();
			return Integer.MAX_VALUE;
		}
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			String imageUrl = mAdList.get(position%mAdList.size());
			ClickableImageView imageView ;
			if (mImageViewCacheList.isEmpty()) {
				imageView = new ClickableImageView(mContext);
				imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				imageView.setScaleType(ScaleType.CENTER);


			} else {
				imageView = mImageViewCacheList.remove(0);
			}
			// 璁剧疆鍥剧墖鐐瑰嚮鐩戝惉
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mImageCycleViewListener.onImageClick(position%mAdList.size(), v);
				}
			});
			imageView.setTag(imageUrl);
			container.addView(imageView);
			mImageCycleViewListener.displayImage(imageUrl, imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ClickableImageView view = (ClickableImageView) object;
			mAdvPager.removeView(view);
			mImageViewCacheList.add(view);

		}

	}

	/**
	 * 杞挱鎺т欢鐨勭洃鍚簨浠�
	 *
	 * @author minking
	 */
	public  interface ImageCycleViewListener {
		/**
		 * 鍔犺浇鍥剧墖璧勬簮
		 *
		 * @param imageURL
		 * @param imageView
		 */
		void displayImage(String imageURL, ImageView imageView);

		/**
		 * 鍗曞嚮鍥剧墖浜嬩欢
		 *
		 * @param position
		 * @param imageView
		 */
		void onImageClick(int position, View imageView);
	}


}
