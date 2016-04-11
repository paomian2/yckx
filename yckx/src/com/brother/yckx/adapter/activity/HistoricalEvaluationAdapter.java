package com.brother.yckx.adapter.activity;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brother.BaseAdapter2;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.TimeUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.HistoricalEvaluationActivity;
import com.brother.yckx.model.HistoricalEvaluationEntity;
import com.brother.yckx.model.HistoryCommentEntity;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.image.CacheImageView;

public class HistoricalEvaluationAdapter extends BaseAdapter2<HistoryCommentEntity>{

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}


	public HistoricalEvaluationAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(arg1==null){
			arg1=layoutInflater.inflate(R.layout.row_history_comment, null);
		}

		CircleImageView userImg=(CircleImageView) arg1.findViewById(R.id.userImg);
		//美护师现在回复layout
		LinearLayout washer_reply_do_layout=(LinearLayout) arg1.findViewById(R.id.washer_reply_do_layout);

		//美护师历史回复layout
		LinearLayout wahser_replay_layout=(LinearLayout) arg1.findViewById(R.id.wahser_replay_layout);


		TextView tv_userPhone=(TextView) arg1.findViewById(R.id.tv_userPhone);
		TextView tv_commentTime=(TextView) arg1.findViewById(R.id.tv_commentTime);
		TextView tv_commentContent=(TextView) arg1.findViewById(R.id.tv_commentContent);
		TextView tv_serviceProducetName=(TextView) arg1.findViewById(R.id.tv_serviceProducetName);
		TextView tv_serviceTime=(TextView) arg1.findViewById(R.id.tv_serviceTime);
		TextView tv_replyContent=(TextView) arg1.findViewById(R.id.replyContent);

		CacheImageView img1=(CacheImageView) arg1.findViewById(R.id.img1);
		CacheImageView img2=(CacheImageView) arg1.findViewById(R.id.img2);
		CacheImageView img3=(CacheImageView) arg1.findViewById(R.id.img3);

		HistoryCommentEntity entity=getItem(arg0);
		String createPhone=entity.getCreateUserPhone();
		String commentCreateTime=entity.getCommentCreateTime();
		if(commentCreateTime!=null&&!commentCreateTime.equals("")){
			commentCreateTime=TimeUtils.millsToDateTime(commentCreateTime);
		}
		String commentContent=entity.getCommentContent();
		String productName=entity.getProductName();
		String orderServiceTime=entity.getOrderTime();
		if(orderServiceTime!=null&&!orderServiceTime.equals("")){
			orderServiceTime=TimeUtils.millsToDateTime(orderServiceTime);
		}
		String replyContent=entity.getReplyContent();
		String mImg1=entity.getImg1();
		String mImg2=entity.getImg2();
		String mImg3=entity.getImg3();

		//用户给美护师打分的星级
		ImageView startImages[]=new ImageView[5];
		startImages[0]=(ImageView) arg1.findViewById(R.id.starImge1);
		startImages[1]=(ImageView) arg1.findViewById(R.id.starImge2);
		startImages[2]=(ImageView) arg1.findViewById(R.id.starImge3);
		startImages[3]=(ImageView) arg1.findViewById(R.id.starImge4);
		startImages[4]=(ImageView) arg1.findViewById(R.id.starImge5);
		//设置用户给美护师打的星级
		Integer washerScore=Integer.parseInt(entity.getCommentScore());
		for(int i=0;i<washerScore;i++){
			startImages[i].setBackgroundResource(R.drawable.icon_evaluate_selected);
		}

		//判断美护师现在是否可以回复,以前不曾回复，并且现在的时间离用户回复的时间不能超过三天
		if(PrefrenceConfig.getUserMessage(context).getUserRole().equals("washer")){
			//美护师才能回复
			if(replyContent.equals("")){
				//当前的时间戳
				Date todateDate=new Date();
				long todateTime = todateDate.getTime();
				long long_commentCreateTime=Long.parseLong(entity.getCommentCreateTime());
				long betweenDays = (long)((todateTime-long_commentCreateTime) / (1000 * 60 * 60 *24) + 0.5);
				if(betweenDays<4){
					//ToastUtil.show(getApplicationContext(), "只能预约未来3天", 2000);
					washer_reply_do_layout.setVisibility(View.VISIBLE);
				}else{
					washer_reply_do_layout.setVisibility(View.GONE);
				}
			}else{
				washer_reply_do_layout.setVisibility(View.GONE);
			}
		}else{
			washer_reply_do_layout.setVisibility(View.GONE);
		}

		tv_userPhone.setText(createPhone);
		tv_commentTime.setText(commentCreateTime);
		tv_commentContent.setText(commentContent);
		tv_serviceProducetName.setText(productName);
		tv_serviceTime.setText(orderServiceTime);
		if(!replyContent.equals("")){
			wahser_replay_layout.setVisibility(View.VISIBLE);
			tv_replyContent.setText(replyContent);
		}else{
			wahser_replay_layout.setVisibility(View.GONE);
		}

		if(!mImg1.equals("")){
			img1.setImageUrl(WEBInterface.INDEXIMGURL+mImg1);
			img1.setVisibility(View.VISIBLE);
		}else{
			img1.setVisibility(View.GONE);
		}
		if(!mImg2.equals("")){
			img2.setImageUrl(WEBInterface.INDEXIMGURL+mImg2);
			img2.setVisibility(View.VISIBLE);
		}else{
			img2.setVisibility(View.GONE);
		}
		if(!mImg3.equals("")){
			img3.setImageUrl(WEBInterface.INDEXIMGURL+mImg3);
			img3.setVisibility(View.VISIBLE);
		}else{
			img3.setVisibility(View.GONE);
		}

		//美护师回复用户
		final EditText ev_washerRepale=(EditText) arg1.findViewById(R.id.ev_washerRepale);
		final TextView washerReplySend=(TextView) arg1.findViewById(R.id.washerReplySend);
		//ev_washerRepale.setTag(arg0, ev_washerRepale.getText().toString().trim());
		washerReplySend.setTag(arg0);
		washerReplySend.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						int position=(Integer)washerReplySend.getTag();
						//	String replyContent=(String) ev_washerRepale.getTag(position);
						String replyContent=ev_washerRepale.getText().toString().trim();
						String token=PrefrenceConfig.getUserMessage(context).getUserToken();
						String commentId=getItem(position).getCommentId();
						HashMap<String,String> hasp=new HashMap<String, String>();
						hasp.put("content", replyContent);
						String host=WEBInterface.WASHERCOMMENTREPLY+commentId;
                        String respose=ApacheHttpUtil.httpPost(host, token, hasp);
                        Log.d("yckx","美护师回复评论返回的网络数据:"+respose);
                        try {
							JSONObject jSONObject=new JSONObject(respose);
							String code=jSONObject.getString("code");
							if(code.equals("0")){
								Intent intent=new Intent(context,HistoricalEvaluationActivity.class);
								context.startActivity(intent);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        
					}
				}).start();


			}});
		return arg1;
	}

}
