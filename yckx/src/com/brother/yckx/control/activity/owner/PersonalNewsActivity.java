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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.BaseAdapter2;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.TimeUtils;
import com.brother.yckx.R;
import com.brother.yckx.model.CommentEntity;
import com.brother.yckx.model.PersonalNewsEntity;
import com.brother.yckx.model.UserEntity;
import com.brother.yckx.view.LoadListView;
import com.brother.yckx.view.image.CacheImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 个人动态页面
 * */
public class PersonalNewsActivity extends BaseActivity{
	
	private LoadListView listiview;
	private PersonalNewsAdapter adapter;
	private List<PersonalNewsEntity> list=new ArrayList<PersonalNewsEntity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_user_news);
		findViewById(R.id.layout_return).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				PersonalNewsActivity.this.finish();
			}});
		findViewById(R.id.btn_fabu).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//发布我的动态
				Intent intent=new Intent(PersonalNewsActivity.this,PushPersonalNewsActivity.class);
				startActivity(intent);
			}});
		listiview=(LoadListView) findViewById(R.id.newsList);
		listiview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent(PersonalNewsActivity.this,PersonalNewsDetailsActivity.class);
				startActivity(intent);
			}});
		adapter=new PersonalNewsAdapter(this,imageLoader);
		listiview.setAdapter(adapter);
		setTestListData();
		
	}
	
	
	/**测试数据*/
	private void setTestListData(){
		list.clear();
		 for(int i=0;i<5;i++){
			 String newsId="123";
			 UserEntity newsUserEntity=new UserEntity("4132", "Test1", "2016/01/14/8c8312316c3398147d62a49c6c294bca_yckx_temp.png", "12345678", "", "", "", "", "", "", false, "", "", "", "", "");
		     String newsTime="1234567891234";
		     String newsImg[]=new String[1];
		     //newsImg[0]="2016/01/14/8c8312316c3398147d62a49c6c294bca_yckx_temp.png";
		     newsImg[0]="2016/01/10/72bcb757e32f14fc2927099e951d9d91_yckx_temp.png";
			 String newsTxt="这是一个测试这是一个猪的世界这是一个猪的世界这是一个猪的世界这是一个猪的世界这是一个猪的世界这是一个猪的世界这是一个猪的世界这是一个猪的世界这是一个猪的世界这是一个猪的世界";
			 String newsTag="supply";
			 String keyword[]=new String[1];
			 keyword[0]="奥巴马";
			 String praise="10";
			 CommentEntity commentEntity=null;
			 PersonalNewsEntity newsEntity=new PersonalNewsEntity(newsId, newsUserEntity, newsTime, newsImg, newsTxt, newsTag, keyword, praise, commentEntity);
			 list.add(newsEntity);
		 }
		 for(int i=0;i<5;i++){
			 String newsId="1234";
			 UserEntity newsUserEntity=new UserEntity("4132", "Test1", "2016/01/14/8c8312316c3398147d62a49c6c294bca_yckx_temp.png", "12345678", "", "", "", "", "", "", false, "", "", "", "", "");
		     String newsTime="2234567812345";
		     String newsImg[]=new String[1];
		     //newsImg[0]="2016/01/14/8c8312316c3398147d62a49c6c294bca_yckx_temp.png";
		     newsImg[0]="";
			 String newsTxt="这也是一个测试，这里不是猪的世界，是逗比的世界是逗比的世界是逗比的世界是逗比的世界是逗比的世界";
			 String newsTag="No";
			 String keyword[]=null;
			 //keyword[0]="奥巴马";
			 String praise="10";
			 CommentEntity commentEntity=null;
			 PersonalNewsEntity newsEntity=new PersonalNewsEntity(newsId, newsUserEntity, newsTime, newsImg, newsTxt, newsTag, keyword, praise, commentEntity);
			 list.add(newsEntity);
		 }
		 
		 adapter.clearAdapter();
		 adapter.addDataToAdapter(list);
		 adapter.notifyDataSetChanged();
	}
	
	
	
	//我的动态数据适配器
	class PersonalNewsAdapter extends BaseAdapter2<PersonalNewsEntity>{
		
		private ImageLoader imageLoader;
		private DisplayImageOptions options;
		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		/*@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}*/

		public PersonalNewsAdapter(Context context,ImageLoader imageLoader) {
			super(context);
			this.imageLoader=imageLoader;
			/**
			 * 配置
			 */
			options = new DisplayImageOptions.Builder()
						.showImageForEmptyUri(R.drawable.logo)
						.showImageOnFail(R.drawable.load_failure)
						.resetViewBeforeLoading(true)
						.cacheOnDisc(true)
						.imageScaleType(ImageScaleType.EXACTLY)
						.bitmapConfig(Bitmap.Config.RGB_565)
						.displayer(new FadeInBitmapDisplayer(300))
						.build();
		}

		@Override
		public View getView(int position, View contentView, ViewGroup arg2) {
			ViewHolder viewHolder;
			if(contentView==null){
				contentView=layoutInflater.inflate(R.layout.row_activity_personal_user_news,null);
				viewHolder=new ViewHolder(contentView);
				contentView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder) contentView.getTag();
			}
			PersonalNewsEntity newsEntity=getItem(position);
			viewHolder.monthBig.setText(TimeUtils.millsToDateTime(newsEntity.getNewsTime(), "M"));
			viewHolder.monthSmall.setText(TimeUtils.millsToDateTime(newsEntity.getNewsTime(), "M"));
			if(newsEntity.getNewsImg()!=null&&newsEntity.getNewsImg().length>0){
				//viewHolder.newsImg.setImageUrl(WEBInterface.INDEXIMGURL+newsEntity.getNewsImg()[0]);
				imageLoader.displayImage(WEBInterface.INDEXIMGURL+newsEntity.getNewsImg()[0], viewHolder.newsImg);
			}
			viewHolder.newsTxt.setText(newsEntity.getNewsTxt());
			if(newsEntity.getNewsTag().equals("No")){
				viewHolder.layout_newsTag.setVisibility(View.VISIBLE);
				if(newsEntity.getKeyword()!=null&&newsEntity.getKeyword().length==3){
					viewHolder.keyword1.setText(newsEntity.getKeyword()[0]);
					viewHolder.keyword2.setText(newsEntity.getKeyword()[1]);
					viewHolder.keyword3.setText(newsEntity.getKeyword()[2]);
				}
				if(newsEntity.getKeyword()!=null&&newsEntity.getKeyword().length==2){
					viewHolder.keyword1.setText(newsEntity.getKeyword()[0]);
					viewHolder.keyword2.setText(newsEntity.getKeyword()[1]);
				}
				if(newsEntity.getKeyword()!=null&&newsEntity.getKeyword().length==1){
					viewHolder.keyword1.setText(newsEntity.getKeyword()[0]);
				}
			}
			return contentView;
		}
		
		
		class ViewHolder{
			TextView monthBig;
			TextView monthSmall;
			ImageView newsImg;
			TextView newsTxt;
			LinearLayout layout_newsTag;
			TextView newsTag,keyword1,keyword2,keyword3;
			public ViewHolder(View view){
				monthBig=(TextView) view.findViewById(R.id.monthBig);
				monthSmall=(TextView) view.findViewById(R.id.monthSmall);
				newsImg=(ImageView) view.findViewById(R.id.newsImg);
				newsTxt=(TextView) view.findViewById(R.id.newsTxt);
				layout_newsTag=(LinearLayout) view.findViewById(R.id.layout_newsTag);
				newsTag=(TextView) view.findViewById(R.id.newsTag);
				keyword1=(TextView) view.findViewById(R.id.keyword1);
				keyword2=(TextView) view.findViewById(R.id.keyword2);
				keyword3=(TextView) view.findViewById(R.id.keyword3);
			}
		}
		
	}
	

	
}
