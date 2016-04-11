package com.brother.yckx.control.activity.owner;

import android.os.Bundle;
import com.brother.BaseActivity;
import com.brother.BaseFragment.FragmentItemOnClickListener;
import com.brother.yckx.R;
import com.brother.yckx.control.frgament.YCKXClassFragmentOrders;
import com.brother.yckx.control.frgament.YCKXClassGroupFragment;

/**
 * @author lxz
 * 
 * 订单列表表第二版(测试使用)
 * 
 * */
public class OrderClassFragmentActivity extends BaseActivity implements FragmentItemOnClickListener{

	private YCKXClassGroupFragment ycxkClassGroupFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderclass_fragment_activity);
		// 初始化各Fragment(默认显示订单组Fragment)
		initFragmentObj();
	}


	private void initFragmentObj() {
		ycxkClassGroupFragment=new YCKXClassGroupFragment();
		smartFragmentReplace(R.id.contentID, ycxkClassGroupFragment);
	}


	/** 当前Activity下其它Fragment反馈click事件的监听回调,设置监听代码在BaseFragment的onAttach已处理 */
	@Override
	public void onFragmentItemClick(String tag, int eventTagID, Object data) {
		// 是从HNewsGroupFragmentNews反馈的回调数据,当单击了新闻列表的某一项
		// 或者是从HCollectFragment反馈的回调数据,当单击了新闻列表的某一项HNewsGroupFragmentNews
		if (tag.equals(YCKXClassFragmentOrders.class.getSimpleName())) {
			//switch (eventTagID) {
			/*	case R.id.tag_hnewsgroup_enternews:
					case R.id.tag_hcollect_enternews:
						NewslistInfo info = (NewslistInfo) data;*/
			// 进入新闻浏览的BrowseActivity
			Bundle bundle = new Bundle();
			//String key = NewslistInfo.class.getSimpleName();
			//bundle.putSerializable(key, info);
			//openActivity(BrowseActivity.class, bundle);
			//	break;
			//}
		}
	}

}
