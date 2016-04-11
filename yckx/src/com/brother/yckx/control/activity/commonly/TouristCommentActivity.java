package com.brother.yckx.control.activity.commonly;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.brother.BaseActivity;
import com.brother.BaseAdapter2;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.TimeUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.model.BusinessEntity;
import com.brother.yckx.model.HistoryCommentEntity;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.LoadListView;
import com.brother.yckx.view.LoadListView.ILoadListener;
import com.nostra13.universalimageloader.core.ImageLoader;
public class TouristCommentActivity extends BaseActivity implements ILoadListener{
	private BusinessEntity businessEntity;
	private LoadListView listview;
	private List<HistoryCommentEntity> list;
	private HistoryEvolutionAdapter adapter;
	private final int LOADHISTORY_OVER=10;
	private final int LOADHISTORY_COMMENT_SUCCESS=0;
	private final int TOKEN_EORRO=2;
	
	//listview的表头
	private View headerView;
    //评论星级
	private ImageView[] img_stars=new ImageView[5];
	//评论内容选择控件
	private TextView tv_selectPrice,tv_selectService,tv_selectEnvironment,tv_selectComeAgain;
	private String mSelectContent="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tourist_comment);
		businessEntity=(BusinessEntity) getIntent().getSerializableExtra("TouristCommentActivity");
		initview();
		listview=(LoadListView) findViewById(R.id.lv_businessComment);
		listview.addHeaderView(headerView);
		adapter=new HistoryEvolutionAdapter(this, imageLoader);
		listview.setAdapter(adapter);
		getHistoryEvolution(businessEntity.getCompanyId());
	}


	private void initview(){
		
		headerView=getLayoutInflater().inflate(R.layout.row_business_history_commeit_listview_header,null);
		
		img_stars[0]=(ImageView) headerView.findViewById(R.id.img_star1);
		img_stars[1]=(ImageView) headerView.findViewById(R.id.img_star2);
		img_stars[2]=(ImageView) headerView.findViewById(R.id.img_star3);
		img_stars[3]=(ImageView) headerView.findViewById(R.id.img_star4);
		img_stars[4]=(ImageView) headerView.findViewById(R.id.img_star5);
		for(int i=0;i<5;i++){
			img_stars[i].setOnClickListener(new ImgStarListener(i));
		}
		//评论内容选择控件
		tv_selectPrice=(TextView) headerView.findViewById(R.id.tv_selectPrice);
		tv_selectService=(TextView) headerView.findViewById(R.id.tv_selectService);
		tv_selectEnvironment=(TextView) headerView.findViewById(R.id.tv_selectEnvironment);
		tv_selectComeAgain=(TextView) headerView.findViewById(R.id.tv_selectComeAgain);
		tv_selectPrice.setOnClickListener(commentContentListener);
		tv_selectService.setOnClickListener(commentContentListener);
		tv_selectEnvironment.setOnClickListener(commentContentListener);
		tv_selectComeAgain.setOnClickListener(commentContentListener);
		//提交选择的内容
		Button btn_commit=(Button) headerView.findViewById(R.id.btn_commit);
		btn_commit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(mSelectContent.equals("")){
					ToastUtil.show(getApplicationContext(), "请选择相应的评价内容", 3000);
				}else{//以后只分享到朋友圈
					//分享到ShareSDK,,String shareTittle,String shareText,String commentText,String tagetUrl
					String shareTittle=businessEntity.getCompanyName();
					String shareText="我的分享XXX";
					String commentText="我点评了"+businessEntity.getCompanyName()+mSelectContent+"朋友们要来就来这一家吧。";
					String tagetUrl="WEBInterface.YCKX_APK_DOWNLOAD_URL";
					showShare(shareTittle,shareText,commentText,tagetUrl);
				}
			}
			
		});
	}

	/**选择评论星级的监听事件*/
	class ImgStarListener implements View.OnClickListener{
		private int index;
		public ImgStarListener(int index){
			this.index=index;
		}
		@Override
		public void onClick(View arg0) {
			setImgStarColor(index);
			TextView tv_star_Instruction=(TextView) headerView.findViewById(R.id.tv_star_Instruction);
			tv_star_Instruction.setVisibility(View.GONE);
			RelativeLayout layout_commentContent=(RelativeLayout) headerView.findViewById(R.id.layout_commentContent);
			layout_commentContent.setVisibility(View.VISIBLE);
			}
	}
	
	/**评论内容选择监听器**/
	private View.OnClickListener commentContentListener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			resetSelectContentStatus();
			switch (arg0.getId()) {
			case R.id.tv_selectPrice:
				mSelectContent="值得这个价格";//服务超好,环境好,下次我还来
				//ev_background_green2
				tv_selectPrice.setBackgroundResource(R.drawable.ev_background_green2);
				break;
			case R.id.tv_selectService:
				mSelectContent="服务超好";
				tv_selectService.setBackgroundResource(R.drawable.ev_background_green2);
				break;
			case R.id.tv_selectEnvironment:
				mSelectContent="环境好";
				tv_selectEnvironment.setBackgroundResource(R.drawable.ev_background_green2);
				break;
			case R.id.tv_selectComeAgain:
				mSelectContent="下次我还来";
				tv_selectComeAgain.setBackgroundResource(R.drawable.ev_background_green2);
				break;
			}
			
		}
	}; 
	
	/**重置选择内容控件的选择状态*/
	private void resetSelectContentStatus(){
		tv_selectPrice.setBackgroundResource(R.drawable.test_rectangle);
		tv_selectService.setBackgroundResource(R.drawable.test_rectangle);
		tv_selectEnvironment.setBackgroundResource(R.drawable.test_rectangle);
		tv_selectComeAgain.setBackgroundResource(R.drawable.test_rectangle);
	}
	
	/**设置评论星级*/
	private void setImgStarColor(int index){
		for(int i=0;i<5;i++){
			img_stars[i].setBackgroundResource(R.drawable.icon_evaluate_normal);
		}
		for(int i=0;i<index+1;i++){
			img_stars[i].setBackgroundResource(R.drawable.icon_evaluate_selected);
		}
	}
	
	
	private void showShare(String shareTittle,String shareText,String commentText,String tagetUrl) {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		//oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(shareTittle);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(tagetUrl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(shareText);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/yckx/yckx_screen_temp.png");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(tagetUrl);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment(commentText);
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(shareTittle);
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(tagetUrl);

		// 启动分享GUI
		oks.show(this);
	}

	private int page=0;
	private int size=20;
	/**获取评论列表数据*/
	private void getHistoryEvolution(final String companyId){
		new Thread(new Runnable() {//测试id:id=12
			@Override
			public void run() {///comment/list/{page}/{size}/{targetOwnerId}
				String url=WEBInterface.COMPANYHISTORY_COMMENT_URL+page+"/"+size+"/"+companyId;
				String token=PrefrenceConfig.getUserMessage(TouristCommentActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpGet(url, token, null);
				Log.d("yckx", ""+respose);
				if(respose!=null){
					JSONObject jSONObject;
					try {
						jSONObject = new JSONObject(respose);
						String code=jSONObject.getString("code");
						if(code.equals("2")){
							Message msg=new Message();
							msg.what=TOKEN_EORRO;
							mHandler.sendMessage(msg);
							return;
						}else{
							list=ParseJSONUtils.parseHistoryCommet(respose);
							if(list!=null){
								//获取数据成功
								if(list.size()==0){
									Message msg=new Message();
									msg.what=LOADHISTORY_OVER;
									mHandler.sendMessage(msg);
								}else{
									Message msg=new Message();
									msg.what=LOADHISTORY_COMMENT_SUCCESS;
									mHandler.sendMessage(msg);
								}

							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}



	@Override
	public void onLoad() {
		page++;
		getHistoryEvolution(businessEntity.getCompanyId());
	}


	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case TOKEN_EORRO:
				ToastUtil.show(getApplicationContext(), "还未登陆，请登录后再评论", 3000);
				break;
			case LOADHISTORY_OVER:
				ToastUtil.show(getApplicationContext(), "已加载完成", 3000);
				break;
			case LOADHISTORY_COMMENT_SUCCESS:
				adapter.clearAdapter();
				adapter.addDataToAdapter(list);
				adapter.notifyDataSetChanged();
				//加载完成
				listview.loadComplete();
				break;
			}
		};
	};




	class HistoryEvolutionAdapter extends BaseAdapter2<HistoryCommentEntity>{

		private ImageLoader imageLoader;
		public HistoryEvolutionAdapter(Context context,ImageLoader imageLoader) {
			super(context);
			this.imageLoader=imageLoader;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup arg2) {
			ViewHoder viewHolder;
			if(contentView==null){
				contentView=layoutInflater.inflate(R.layout.row_historical_comment,null);
				viewHolder=new ViewHoder(contentView);
				contentView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHoder) contentView.getTag();
			}

			HistoryCommentEntity historyCommentEntity=getItem(position);
			//imageLoader.displayImage(WE, imageView)
			viewHolder.tv_commenterName.setText(historyCommentEntity.getCreateUserPhone());
			//设置评分星级
			String mScore=historyCommentEntity.getCommentScore();
			if(mScore!=null&&!mScore.equals("")){
				Integer Iscore=Integer.parseInt(mScore);
				for(int i=0;i<Iscore;i++){
					viewHolder.img_stars[i].setBackgroundResource(R.drawable.icon_evaluate_selected);
				}
			}
			viewHolder.tv_commentTime.setText(TimeUtils.millsToDateTime(historyCommentEntity.getCommentCreateTime()));
			viewHolder.tv_commentTxt.setText(historyCommentEntity.getCommentContent());
			String img1=historyCommentEntity.getImg1();
			if(img1!=null&&!img1.equals("")){
				viewHolder.img_commentImgs[0].setVisibility(View.VISIBLE);
				imageLoader.displayImage(WEBInterface.INDEXIMGURL+img1, viewHolder.img_commentImgs[0]);
			}else{
				viewHolder.img_commentImgs[0].setVisibility(View.GONE);
			}
			String img2=historyCommentEntity.getImg1();
			if(img2!=null&&!img2.equals("")){
				viewHolder.img_commentImgs[1].setVisibility(View.VISIBLE);
				imageLoader.displayImage(WEBInterface.INDEXIMGURL+img2, viewHolder.img_commentImgs[1]);
			}else{
				viewHolder.img_commentImgs[1].setVisibility(View.GONE);
			}
			String img3=historyCommentEntity.getImg1();
			if(img3!=null&&!img3.equals("")){
				viewHolder.img_commentImgs[2].setVisibility(View.VISIBLE);
				imageLoader.displayImage(WEBInterface.INDEXIMGURL+img3, viewHolder.img_commentImgs[2]);
			}else{
				viewHolder.img_commentImgs[2].setVisibility(View.GONE);
			}
			//以后要修改，图片的展示，多余三张时的操作，赞、评价的个数
			return contentView;
		}

		class ViewHoder{
			public CircleImageView img_head;
			public TextView tv_commenterName;
			public ImageView img_commenterSex;
			public ImageView[] img_stars=new ImageView [5];
			public TextView tv_commentTime;
			public TextView tv_commentTxt;
			public ImageView[] img_commentImgs=new ImageView[3];
			public LinearLayout layout_commentImgMore;
			public TextView tv_commentImgCount;
			public TextView tv_commentPraiseCount,tv_commentRecommetCount;
			public ViewHoder(View view){
				img_head=(CircleImageView) view.findViewById(R.id.img_head);
				tv_commenterName=(TextView) view.findViewById(R.id.tv_commenterName);
				img_commenterSex=(ImageView) view.findViewById(R.id.img_commenterSex);
				img_stars[0]=(ImageView) view.findViewById(R.id.img_star1);
				img_stars[1]=(ImageView) view.findViewById(R.id.img_star2);
				img_stars[2]=(ImageView) view.findViewById(R.id.img_star3);
				img_stars[3]=(ImageView) view.findViewById(R.id.img_star4);
				img_stars[4]=(ImageView) view.findViewById(R.id.img_star5);
				tv_commentTime=(TextView) view.findViewById(R.id.tv_commentTime);
				tv_commentTxt=(TextView) view.findViewById(R.id.tv_commentTxt);
				img_commentImgs[0]=(ImageView) view.findViewById(R.id.img_commentImg1);
				img_commentImgs[1]=(ImageView) view.findViewById(R.id.img_commentImg2);
				img_commentImgs[2]=(ImageView) view.findViewById(R.id.img_commentImg3);
				layout_commentImgMore=(LinearLayout) view.findViewById(R.id.layout_commentImgMore);
				tv_commentImgCount=(TextView) view.findViewById(R.id.tv_commentImgCount);
				tv_commentPraiseCount=(TextView) view.findViewById(R.id.tv_commentPraiseCount);
				tv_commentRecommetCount=(TextView) view.findViewById(R.id.tv_commentRecommetCount);
			}
		}

	}
}
