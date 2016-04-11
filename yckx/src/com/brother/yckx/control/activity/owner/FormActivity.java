package com.brother.yckx.control.activity.owner;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.BaseFragment.FragmentItemOnClickListener;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.FormDetailActivity.RefreshOnclickListner;
import com.brother.yckx.control.frgament.MoreFragment;
import com.brother.yckx.model.OrderListEntity;
import com.shizhefei.view.indicator.FragmentListPageAdapter;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.OnIndicatorPageChangeListener;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

public class FormActivity extends BaseActivity implements FragmentItemOnClickListener{
	private IndicatorViewPager indicatorViewPager;
	private LayoutInflater inflate;
	private ViewPager viewPager;
	private ScrollIndicatorView  tabs;
	private ProgressBar progressBar;
	private LinearLayout layout_showOrder;
	private String[] names = { "全部", "待付款", "派单中", "清洗中", "待评价", "已完成", "已取消", "已作废" };
	private Handler mHandler;
	/**当前页面改变的监听器*/
	private CurrentPageWatchListener currentPageWatchListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form);
		setActionBar(R.string.formTitle, R.drawable.btn_homeasup_default, NULL_ID, listener);
		mHandler = new Handler();
		viewPager=(ViewPager) findViewById(R.id.viewPager);
		tabs=(ScrollIndicatorView) findViewById(R.id.tabs_viewPager);
		tabs.setScrollBar(new ColorBar(this, Color.RED, 5));
		// 设置滚动监听
		int selectColorId = R.color.tab_top_text_2;
		int unSelectColorId = R.color.tab_top_text_1;
		tabs.setOnTransitionListener(new OnTransitionTextListener().setColorId(this, selectColorId, unSelectColorId));

		viewPager.setOffscreenPageLimit(2);
		indicatorViewPager = new IndicatorViewPager(tabs, viewPager);
		//页面切换监听
		indicatorViewPager.setOnIndicatorPageChangeListener(new OnIndicatorPageChangeListener(){
			public void onIndicatorPageChange(int preItem, int currentItem) {
				currentPageWatchListener.onCurrentPageWatchListener(currentItem);
			}});
		
		inflate = LayoutInflater.from(getApplicationContext());
		indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		
		progressBar=(ProgressBar) findViewById(R.id.progressBar);
		layout_showOrder=(LinearLayout) findViewById(R.id.layout_showOrder);
		//先加载网络数据，之后再进行数据访问
	}


	private int size = 8;
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
			MoreFragment fragment = new MoreFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(MoreFragment.INTENT_INT_INDEX, position);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			return FragmentListPageAdapter.POSITION_NONE;
		}

	};

	@Override
	public void onFragmentItemClick(String tag, int eventTagID, Object data) {
		// TODO Auto-generated method stub

	}
	
	
	/**一个用于跟fragment进行通信的接口,监听当前是第几个页面*/
	public  interface CurrentPageWatchListener{
		public void onCurrentPageWatchListener(int currenPage);
	}

	/**设置监听器的方法*/
	public void setCurrentPageWatchListner(CurrentPageWatchListener currentPageWatchListener){
		this.currentPageWatchListener=currentPageWatchListener;
	}
	
	
	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(arg0.getId()==R.id.iv_action_left){
				FormActivity.this.finish();
			}
           
		}
	};
	
	
}
