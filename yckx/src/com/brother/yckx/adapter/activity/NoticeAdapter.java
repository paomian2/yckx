package com.brother.yckx.adapter.activity;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.brother.BaseAdapter2;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.TimeUtils;
import com.brother.yckx.R;
import com.brother.yckx.model.MessageEntity;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.image.CacheImageView;

public class NoticeAdapter extends BaseAdapter2<MessageEntity>{

	public NoticeAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(arg1==null){
			arg1=layoutInflater.inflate(R.layout.row_activity_notice, null);
		}
		final CacheImageView noticeImg=(CacheImageView) arg1.findViewById(R.id.noticeImg);
		TextView noticeType=(TextView) arg1.findViewById(R.id.noticeType);
		TextView noticeTime=(TextView) arg1.findViewById(R.id.time);
		TextView noticeTxt=(TextView) arg1.findViewById(R.id.notice_txt);
		MessageEntity entity=getItem(arg0);
		noticeImg.setImageUrl(WEBInterface.INDEXIMGURL+getItem(arg0).getSenderEntity().getUserImageUrl());
		noticeType.setText(entity.getTittle());
		noticeTime.setText(TimeUtils.millsToDateTime2(""+entity.getCreateTime()));
		noticeTxt.setText(entity.getMsg());
		return arg1;
	}
	
	

}
