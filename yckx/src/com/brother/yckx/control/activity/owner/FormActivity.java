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
	private String[] names = { "ȫ��", "������", "�ɵ���", "��ϴ��", "������", "�����", "��ȡ��", "������" };
	private Handler mHandler;
	/**��ǰҳ��ı�ļ�����*/
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
		// ���ù�������
		int selectColorId = R.color.tab_top_text_2;
		int unSelectColorId = R.color.tab_top_text_1;
		tabs.setOnTransitionListener(new OnTransitionTextListener().setColorId(this, selectColorId, unSelectColorId));

		viewPager.setOffscreenPageLimit(2);
		indicatorViewPager = new IndicatorViewPager(tabs, viewPager);
		//ҳ���л�����
		indicatorViewPager.setOnIndicatorPageChangeListener(new OnIndicatorPageChangeListener(){
			public void onIndicatorPageChange(int preItem, int currentItem) {
				currentPageWatchListener.onCurrentPageWatchListener(currentItem);
			}});
		
		inflate = LayoutInflater.from(getApplicationContext());
		indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		
		progressBar=(ProgressBar) findViewById(R.id.progressBar);
		layout_showOrder=(LinearLayout) findViewById(R.id.layout_showOrder);
		//�ȼ����������ݣ�֮���ٽ������ݷ���
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
	
	
	/**һ�����ڸ�fragment����ͨ�ŵĽӿ�,������ǰ�ǵڼ���ҳ��*/
	public  interface CurrentPageWatchListener{
		public void onCurrentPageWatchListener(int currenPage);
	}

	/**���ü������ķ���*/
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
