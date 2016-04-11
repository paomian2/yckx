package com.brother.yckx.adapter.fragmnet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brother.BaseAdapter2;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.map.NativeLocationActivity;
import com.brother.yckx.model.BusinessEntity;
public class RBAdapter extends BaseAdapter2<BusinessEntity> {

	private Context mcontext;
	private ImageView[] img_evaluation=new ImageView[5];

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



	public RBAdapter(Context context) {
		super(context);
		mcontext=context;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		if(arg1==null){
			arg1=layoutInflater.inflate(R.layout.row_rb,null);
		}
		TextView isSelect=(TextView) arg1.findViewById(R.id.isSelect);
		TextView conpaneyName=(TextView) arg1.findViewById(R.id.commaneyName);//公司名称
		TextView carParkStatus=(TextView) arg1.findViewById(R.id.parkCount);//停车位数量
		TextView tv_parkPrice=(TextView) arg1.findViewById(R.id.tv_parkPrice);//停车价格
		TextView tv_ditance=(TextView) arg1.findViewById(R.id.tv_ditance);//距离
		TextView tv_score=(TextView)arg1.findViewById(R.id.tv_score);
		//评论星星
		img_evaluation[0]=(ImageView)arg1.findViewById(R.id.img_evluation1);
		img_evaluation[1]=(ImageView)arg1.findViewById(R.id.img_evluation2);
		img_evaluation[2]=(ImageView)arg1.findViewById(R.id.img_evluation3);
		img_evaluation[3]=(ImageView)arg1.findViewById(R.id.img_evluation4);
		img_evaluation[4]=(ImageView)arg1.findViewById(R.id.img_evluation5);

		final BusinessEntity entity=getItem(position);
		conpaneyName.setText(""+entity.getCompanyName());
		String totalCount=entity.getTotalCount();
		String spaceCount=entity.getSpaceCount();
		String carParkSttus_str=totalCount+"/"+spaceCount+"";
		carParkStatus.setText(carParkSttus_str);
		tv_ditance.setText("" + entity.getDistance());
		//设置评论星星
		if(entity.getAvgScore()!=null&&!entity.getAvgScore().equals("")){
			int score=Integer.parseInt(entity.getAvgScore());
			for (int i=0;i<score;i++){
				img_evaluation[i].setImageResource(R.drawable.test_evaluate_selected);
			}
		}

		final LinearLayout layout_daohang=(LinearLayout)arg1.findViewById(R.id.layout_daohang);
		layout_daohang.setTag(position);
		layout_daohang.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int clickPoinsition=(Integer)layout_daohang.getTag();
				BusinessEntity clickEntity=getItem(clickPoinsition);
				Intent intent=new Intent(context,NativeLocationActivity.class);
				intent.putExtra("NativeLocationActivity", clickEntity);
			}
		});

		return arg1;
	}

	
	public interface RBAdapterOncickListener{
        public void onRBAdapterOncickListener(Activity fromPage, Class<?> toPage, BusinessEntity sendBsEntity);
	}

}
