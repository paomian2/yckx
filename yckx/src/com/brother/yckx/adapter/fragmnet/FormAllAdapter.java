package com.brother.yckx.adapter.fragmnet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brother.BaseAdapter2;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.brother.yckx.model.FormDetalisEntity;
import com.brother.yckx.view.CircleImageView;

public class FormAllAdapter extends BaseAdapter2<FormDetalisEntity>{

	public FormAllAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(arg1==null){
			arg1=layoutInflater.inflate(R.layout.row_frgment_formdetails, null);
		}
		ImageView leagueNameImg=(ImageView) arg1.findViewById(R.id.leagueNameImg);
		TextView leagueName=(TextView) arg1.findViewById(R.id.leagueName);
		CircleImageView productImg=(CircleImageView) arg1.findViewById(R.id.productImg);
		TextView productName=(TextView) arg1.findViewById(R.id.productName);
		TextView productTime=(TextView) arg1.findViewById(R.id.productTime);
		TextView productPrice=(TextView) arg1.findViewById(R.id.productPrice);
		TextView productCard=(TextView) arg1.findViewById(R.id.productCard);
		TextView productPhone=(TextView) arg1.findViewById(R.id.productPhone);
		TextView productStatus=(TextView) arg1.findViewById(R.id.productStatus);
		TextView actualPay=(TextView) arg1.findViewById(R.id.actualPay);
		//FormDetalisEntity entity=new FormDetalisEntity(leagueNameImg, leagueName, productImg, productName, productTime, productPrice, productCard, productPhone, productStatus, actualPay);
		FormDetalisEntity entity=getItem(arg0);
		leagueNameImg.setImageDrawable(entity.getLeagueNameImg());
		leagueName.setText(entity.getLeagueName());
		productImg.setImageDrawable(entity.getProductImg());
		productName.setText(entity.getProductCard());
		productTime.setText(entity.getProductTime());
		productPrice.setText(entity.getProductPrice());
		productCard.setText(entity.getProductCard());
		productPhone.setText(entity.getProductPhone());
		productStatus.setText(entity.getProductStatus());
		actualPay.setText(entity.getActualPay());
		
		//
		TextView evaluate_atonce=(TextView) arg1.findViewById(R.id.evaluate_atonce);
		TextView sendVoice=(TextView) arg1.findViewById(R.id.sendVoice); 
		TextView callPhone=(TextView) arg1.findViewById(R.id.callPhone); 
		TextView cancelProduce=(TextView) arg1.findViewById(R.id.cancelProduce);
		
		evaluate_atonce.setOnClickListener(new Listener(arg0));
		sendVoice.setOnClickListener(new Listener(arg0));
		callPhone.setOnClickListener(new Listener(arg0));
		cancelProduce.setOnClickListener(new Listener(arg0));
		return arg1;
	}
	
	private class Listener implements View.OnClickListener{
		private int position=-1;
        public Listener(int position){
        	this.position=position;
        }
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.evaluate_atonce:
				ToastUtil.show(context, "立即评论"+position, 3000);
				break;
			case R.id.sendVoice:
				ToastUtil.show(context, "发送语音"+position, 3000);
				break;
			case R.id.callPhone:
				ToastUtil.show(context, "拨打电话"+position, 3000);
				break;
			case R.id.cancelProduce:
				ToastUtil.show(context, "取消订单"+position, 3000);
				break;
			}
		}}

}
