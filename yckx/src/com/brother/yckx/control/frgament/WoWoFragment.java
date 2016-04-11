package com.brother.yckx.control.frgament;
import java.util.ArrayList;
import java.util.List;
import com.brother.BaseAdapter2;
import com.brother.utils.Contants;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.HomeActivity;
import com.brother.yckx.model.b2b.B2BUser;
import com.brother.yckx.view.CircleImageView;
import com.brother.yckx.view.HorizontalScrollViewAdapter;
import com.brother.yckx.view.MyHorizontalScrollView;
import com.brother.yckx.view.MyHorizontalScrollView.CurrentImageChangeListener;
import com.brother.yckx.view.MyHorizontalScrollView.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class WoWoFragment extends Fragment{
	private View view;
	private EditText ev_wowo_search;
	private ImageView img_wowo_search;
	private ImageView img_wowo_add;
	private CircleImageView[] img_recomment=new CircleImageView[5];
	private TextView[] recomment_name=new TextView[5];
	private GridView gridView;
	private WoWoGridViewAdapter wowoAdapter;
	//gridview数据
	private List<B2BUser> b2blist=new ArrayList<B2BUser>();
	private PublishSelectPicPopupWindow menuWindow;
	//横向的scrollview
	private MyHorizontalScrollView mHorizontalScrollView;
	private HorizontalScrollViewAdapter mAdapter;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_wowo, container, false);
		initview();
		return view;
	}
	
	private void initview(){
		//actionbar
		ev_wowo_search=(EditText) view.findViewById(R.id.ev_wowo_search);
		img_wowo_search=(ImageView) view.findViewById(R.id.img_wowo_search);
		img_wowo_add=(ImageView) view.findViewById(R.id.img_wowo_add);
        //actionbar事件
		img_wowo_search.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ToastUtil.show(getActivity(), ""+ev_wowo_search.getText().toString().trim(), 2000);
			}});
		img_wowo_add.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// 实例化SelectPicPopupWindow
				menuWindow = new PublishSelectPicPopupWindow(getActivity(),itemsOnClick);
				// 显示窗口
				menuWindow.showAtLocation(img_wowo_add,
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}});
		

		//推荐会员
		img_recomment[0]=(CircleImageView) view.findViewById(R.id.img_recomment_1);//fragment_wowo
		img_recomment[1]=(CircleImageView) view.findViewById(R.id.img_recomment_2);
		img_recomment[2]=(CircleImageView) view.findViewById(R.id.img_recomment_3);
		img_recomment[3]=(CircleImageView) view.findViewById(R.id.img_recomment_4);
		img_recomment[4]=(CircleImageView) view.findViewById(R.id.img_recomment_5);
		//推荐会员测试数据
		B2BUser b2b1=new B2BUser("123", "推荐的猪的世界1", "url", "1", "business", "0");
		B2BUser b2b2=new B2BUser("124", "推荐的猪的世界2", "url", "1", "business", "0");
		B2BUser b2b3=new B2BUser("125", "推荐的猪的世界3", "url", "1", "business", "0");
		B2BUser b2b4=new B2BUser("126", "推荐的猪的世界4", "url", "1", "business", "0");
		B2BUser b2b5=new B2BUser("127", "推荐的猪的世界5", "url", "0", "unbusiness", "1");
		
		recomment_name[0]=(TextView) view.findViewById(R.id.recomment_name1);
		recomment_name[1]=(TextView) view.findViewById(R.id.recomment_name2);
		recomment_name[2]=(TextView) view.findViewById(R.id.recomment_name3);
		recomment_name[3]=(TextView) view.findViewById(R.id.recomment_name4);
		recomment_name[4]=(TextView) view.findViewById(R.id.recomment_name5);
		
		recomment_name[0].setText(b2b1.getName());
		recomment_name[1].setText(b2b2.getName());
		recomment_name[2].setText(b2b3.getName());
		recomment_name[3].setText(b2b4.getName());
		recomment_name[4].setText(b2b5.getName());
		
		//新原型
		final List<Integer> mDatas=new ArrayList<Integer>();//读取资源图片
		//测试专用
		final List<String> mStringDatas=new ArrayList<String>();
		for(int i=0;i<Contants.IMAGES.length;i++){
			mStringDatas.add(Contants.IMAGES[i]);
		}
		
		mHorizontalScrollView = (MyHorizontalScrollView) view.findViewById(R.id.id_horizontalScrollView);
		mAdapter = new HorizontalScrollViewAdapter(getActivity(), mStringDatas,((HomeActivity)getActivity()).imageLoader);
		//添加滚动回调
		mHorizontalScrollView
				.setCurrentImageChangeListener(new CurrentImageChangeListener()
				{
					@Override
					public void onCurrentImgChanged(int position,
							View viewIndicator)
					{
						//mImg.setImageResource(mDatas.get(position));
						/*viewIndicator.setBackgroundColor(Color
								.parseColor("#AA024DA4"));*/
						//滚动到当前的图片
					}
				});
		//添加点击回调
		mHorizontalScrollView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onClick(View view, int position)
			{
				//mImg.setImageResource(mDatas.get(position));
				//view.setBackgroundColor(Color.parseColor("#AA024DA4"));
				//点击当前的图片
			}
		});
		//设置适配器
		mHorizontalScrollView.initDatas(mAdapter);
		//mHorizontalScrollView.setTimerSwitcher();
		
		
		//全部会员
		gridView=(GridView) view.findViewById(R.id.gridView);
		wowoAdapter=new WoWoGridViewAdapter(getActivity());
		gridView.setAdapter(wowoAdapter);
		getlistdata();
		wowoAdapter.addDataToAdapter(b2blist);
		wowoAdapter.notifyDataSetChanged();
	}
	
	
	/**测试数据*/
	private void getlistdata(){
		//                        id     name     img   sex     是否是商家       该商家是否认证
		B2BUser b2b1=new B2BUser("123", "猪的世界1", "url", "1", "business", "0");
		B2BUser b2b2=new B2BUser("124", "猪的世界2", "url", "1", "business", "0");
		B2BUser b2b3=new B2BUser("125", "猪的世界3", "url", "1", "business", "0");
		B2BUser b2b4=new B2BUser("126", "猪的世界4", "url", "1", "business", "0");
		B2BUser b2b5=new B2BUser("127", "猪的世界5", "url", "0", "unbusiness", "1");
		B2BUser b2b6=new B2BUser("128", "猪的世界6", "url", "1", "business", "0");
		B2BUser b2b7=new B2BUser("129", "猪的世界7", "url", "1", "business", "0");
		B2BUser b2b8=new B2BUser("130", "猪的世界8", "url", "1", "business", "0");
		B2BUser b2b9=new B2BUser("131", "猪的世界9", "url", "1", "business", "0");
		
		b2blist.clear();
		b2blist.add(b2b1);
		b2blist.add(b2b2);
		b2blist.add(b2b3);
		b2blist.add(b2b4);
		b2blist.add(b2b5);
		b2blist.add(b2b6);
		b2blist.add(b2b7);
		b2blist.add(b2b8);
		b2blist.add(b2b9);
		
	}
	
	//
	class WoWoGridViewAdapter extends BaseAdapter2<B2BUser>{
		public WoWoGridViewAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			//存储当前view
			ViewHoler viewHoler=null;
			if(arg1==null){
				arg1=layoutInflater.inflate(R.layout.row_wowo_fragment,null);
				viewHoler=new ViewHoler(arg1);
				arg1.setTag(viewHoler);
			}else{
				viewHoler=(ViewHoler) arg1.getTag();
			}
			//获取数据，并添加到view上
			B2BUser b2bUser=getItem(arg0);
			//viewHoler.circleImageView.setImageBitmap(bm);//设置头像
			viewHoler.name.setText(b2bUser.getName());
			//设置性别
			if(b2bUser.getSex().equals("0")){
				viewHoler.sex.setBackgroundResource(R.drawable.icon_defalut_sex_0);
			}else{
				viewHoler.sex.setBackgroundResource(R.drawable.icon_defalut_sex_1);
			}
			//是否是商家
			if(b2bUser.getUserType().equals("business")){//是商家
				//是否已经认证
				viewHoler.tv_business_mark.setVisibility(View.VISIBLE);
				viewHoler.business_indentify.setVisibility(View.VISIBLE);
				//该商家是否已经认证
				if(b2bUser.getUserBusinessIdentify().equals("1")){
					viewHoler.business_indentify.setBackgroundResource(R.drawable.icon_defalut_unidentification);
				}else{
					viewHoler.business_indentify.setBackgroundResource(R.drawable.icon_defalut_identification);
				}
			}else{
				viewHoler.tv_business_mark.setVisibility(View.INVISIBLE);
				viewHoler.business_indentify.setVisibility(View.INVISIBLE);
			}
			
			return arg1;
		}
		
		//holderview
		class ViewHoler{
			ImageView circleImageView;
			TextView name;
			ImageView sex;
			TextView tv_business_mark;
			ImageView business_indentify;
			public ViewHoler(View holderView){
				circleImageView=(ImageView) holderView.findViewById(R.id.circleImageView1);
				name=(TextView) holderView.findViewById(R.id.name);
				sex=(ImageView) holderView.findViewById(R.id.sex);
				tv_business_mark=(TextView) holderView.findViewById(R.id.tv_business_mark);
				business_indentify=(ImageView) holderView.findViewById(R.id.business_indentify);
			}
		}
	}
	
	
	//---------------泡泡窗口  start---------//
	/**泡泡窗口*/
	class PublishSelectPicPopupWindow extends PopupWindow{
		private ImageView btnCancel;
		private View mMenuView;
		public PublishSelectPicPopupWindow(Activity context,OnClickListener itemsOnClick){
			super(context);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.row_dialog_wowo_popu, null);
			btnCancel=(ImageView) mMenuView.findViewById(R.id.btnCancel);
			btnCancel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					dismiss();
				}});
			//设置SelectPicPopupWindow的View
			this.setContentView(mMenuView);
			//设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.FILL_PARENT);
			//设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.FILL_PARENT);
			//设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			//设置SelectPicPopupWindow弹出窗体动画效果
			this.setAnimationStyle(R.style.popwin_anim_style);
			//实例化一个ColorDrawable颜色为半透明0xb0000000
			ColorDrawable dw = new ColorDrawable(0);
			//设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(dw);
			//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
			mMenuView.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					int height = mMenuView.findViewById(R.id.layout_main).getTop();
					int y=(int) event.getY();
					if(event.getAction()==MotionEvent.ACTION_UP){
						if(y<height){
							dismiss();
						}
					}				
					return true;
				}
			});

		}
	}
	
	/**popu窗口点击事件的监听*/
	private View.OnClickListener itemsOnClick=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			
		}
	};
	
}
