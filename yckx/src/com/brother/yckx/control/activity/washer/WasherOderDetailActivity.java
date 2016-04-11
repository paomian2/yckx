package com.brother.yckx.control.activity.washer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.BaseFragment.FragmentItemOnClickListener;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.brother.yckx.control.frgament.MoreFragment_FormDetail;
import com.brother.yckx.control.frgament.WasherOrderDetailFragment;
import com.brother.yckx.model.OrderListEntity;
import com.brother.yckx.model.QiangDanListEntity;
import com.shizhefei.view.indicator.FragmentListPageAdapter;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.IndicatorViewPager.OnIndicatorPageChangeListener;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
/**
 * (订单处理，订单内容)
 * 
 * */
public class WasherOderDetailActivity extends BaseActivity implements FragmentItemOnClickListener{
	
	private IndicatorViewPager indicatorViewPager;
	private LayoutInflater inflate;
	private ViewPager viewPager;
	private ScrollIndicatorView  tabs;
	private String[] names = { "美护师处理", "订单内容"};
	private String comFrom="";
	private OnPageChangeListener pageChangeListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_washerorder_detail);
		setActionBar(R.string.dingdan_chuli, R.drawable.btn_homeasup_default, NULL_ID, actionListener);
		comFrom=getIntent().getStringExtra("WasherOderDetailActivity");
		GlobalServiceUtils.setGloubalServiceSession("WasherOderDetailActivity_comFrom", comFrom);
		viewPager=(ViewPager) findViewById(R.id.viewPager);
		tabs=(ScrollIndicatorView) findViewById(R.id.tabs_viewPager);
		tabs.setScrollBar(new ColorBar(this, Color.RED, 5));
		showFragmentLayout();
	}
	
	
	private void showFragmentLayout(){
		int selectColorId = R.color.tab_top_text_2;
		int unSelectColorId = R.color.tab_top_text_1;
		tabs.setOnTransitionListener(new OnTransitionTextListener().setColorId(this, selectColorId, unSelectColorId));
		viewPager.setOffscreenPageLimit(2);
		indicatorViewPager = new IndicatorViewPager(tabs, viewPager);
		inflate = LayoutInflater.from(getApplicationContext());
		indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		indicatorViewPager.setOnIndicatorPageChangeListener(new OnIndicatorPageChangeListener(){
			@Override
			public void onIndicatorPageChange(int preItem, int currentItem) {
				if(pageChangeListener!=null){
					pageChangeListener.onPagechane(currentItem);
				}
			}});
	}

	@Override
	public void onFragmentItemClick(String tag, int eventTagID, Object data) {
		// TODO Auto-generated method stub
		
	}
	
	
	private int size =2;
	private class MyAdapter extends IndicatorFragmentPagerAdapter {

		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return size;
		}

		@Override
		public View getViewForTab(int position, View convertView, ViewGroup container) {
			if (convertView == null) {
				convertView = inflate.inflate(R.layout.tab_top, container, false);
			}
			TextView textView = (TextView) convertView;
			textView.setText(names[position % names.length]);
			textView.setPadding(20, 0, 20, 0);
			return convertView;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			WasherOrderDetailFragment fragment = new WasherOrderDetailFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(MoreFragment_FormDetail.INTENT_INT_INDEX, position);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			return FragmentListPageAdapter.POSITION_NONE;
		}

	};
	
	
	/**监听页面的改变*/
	public interface OnPageChangeListener{
		public void onPagechane(int currentPage);
	}
	
	/**设置监听器*/
	public void setOnPageChangeListener(OnPageChangeListener pageChangeListener){
		this.pageChangeListener=pageChangeListener;
	}
	
	private View.OnClickListener actionListener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(arg0.getId()==R.id.iv_action_left){
				finish();
			}
			
		}
	};

}
