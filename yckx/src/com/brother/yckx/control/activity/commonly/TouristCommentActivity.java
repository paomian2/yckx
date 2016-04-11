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
	
	//listview�ı�ͷ
	private View headerView;
    //�����Ǽ�
	private ImageView[] img_stars=new ImageView[5];
	//��������ѡ��ؼ�
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
		//��������ѡ��ؼ�
		tv_selectPrice=(TextView) headerView.findViewById(R.id.tv_selectPrice);
		tv_selectService=(TextView) headerView.findViewById(R.id.tv_selectService);
		tv_selectEnvironment=(TextView) headerView.findViewById(R.id.tv_selectEnvironment);
		tv_selectComeAgain=(TextView) headerView.findViewById(R.id.tv_selectComeAgain);
		tv_selectPrice.setOnClickListener(commentContentListener);
		tv_selectService.setOnClickListener(commentContentListener);
		tv_selectEnvironment.setOnClickListener(commentContentListener);
		tv_selectComeAgain.setOnClickListener(commentContentListener);
		//�ύѡ�������
		Button btn_commit=(Button) headerView.findViewById(R.id.btn_commit);
		btn_commit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(mSelectContent.equals("")){
					ToastUtil.show(getApplicationContext(), "��ѡ����Ӧ����������", 3000);
				}else{//�Ժ�ֻ��������Ȧ
					//����ShareSDK,,String shareTittle,String shareText,String commentText,String tagetUrl
					String shareTittle=businessEntity.getCompanyName();
					String shareText="�ҵķ���XXX";
					String commentText="�ҵ�����"+businessEntity.getCompanyName()+mSelectContent+"������Ҫ��������һ�Ұɡ�";
					String tagetUrl="WEBInterface.YCKX_APK_DOWNLOAD_URL";
					showShare(shareTittle,shareText,commentText,tagetUrl);
				}
			}
			
		});
	}

	/**ѡ�������Ǽ��ļ����¼�*/
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
	
	/**��������ѡ�������**/
	private View.OnClickListener commentContentListener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			resetSelectContentStatus();
			switch (arg0.getId()) {
			case R.id.tv_selectPrice:
				mSelectContent="ֵ������۸�";//���񳬺�,������,�´��һ���
				//ev_background_green2
				tv_selectPrice.setBackgroundResource(R.drawable.ev_background_green2);
				break;
			case R.id.tv_selectService:
				mSelectContent="���񳬺�";
				tv_selectService.setBackgroundResource(R.drawable.ev_background_green2);
				break;
			case R.id.tv_selectEnvironment:
				mSelectContent="������";
				tv_selectEnvironment.setBackgroundResource(R.drawable.ev_background_green2);
				break;
			case R.id.tv_selectComeAgain:
				mSelectContent="�´��һ���";
				tv_selectComeAgain.setBackgroundResource(R.drawable.ev_background_green2);
				break;
			}
			
		}
	}; 
	
	/**����ѡ�����ݿؼ���ѡ��״̬*/
	private void resetSelectContentStatus(){
		tv_selectPrice.setBackgroundResource(R.drawable.test_rectangle);
		tv_selectService.setBackgroundResource(R.drawable.test_rectangle);
		tv_selectEnvironment.setBackgroundResource(R.drawable.test_rectangle);
		tv_selectComeAgain.setBackgroundResource(R.drawable.test_rectangle);
	}
	
	/**���������Ǽ�*/
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
		//�ر�sso��Ȩ
		oks.disableSSOWhenAuthorize(); 

		// ����ʱNotification��ͼ�������  2.5.9�Ժ�İ汾�����ô˷���
		//oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(shareTittle);
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl(tagetUrl);
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText(shareText);
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		oks.setImagePath("/sdcard/yckx/yckx_screen_temp.png");//ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl(tagetUrl);
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment(commentText);
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(shareTittle);
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl(tagetUrl);

		// ��������GUI
		oks.show(this);
	}

	private int page=0;
	private int size=20;
	/**��ȡ�����б�����*/
	private void getHistoryEvolution(final String companyId){
		new Thread(new Runnable() {//����id:id=12
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
								//��ȡ���ݳɹ�
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
				ToastUtil.show(getApplicationContext(), "��δ��½�����¼��������", 3000);
				break;
			case LOADHISTORY_OVER:
				ToastUtil.show(getApplicationContext(), "�Ѽ������", 3000);
				break;
			case LOADHISTORY_COMMENT_SUCCESS:
				adapter.clearAdapter();
				adapter.addDataToAdapter(list);
				adapter.notifyDataSetChanged();
				//�������
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
			//���������Ǽ�
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
			//�Ժ�Ҫ�޸ģ�ͼƬ��չʾ����������ʱ�Ĳ������ޡ����۵ĸ���
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
