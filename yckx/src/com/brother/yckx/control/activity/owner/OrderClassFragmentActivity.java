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
 * �����б��ڶ���(����ʹ��)
 * 
 * */
public class OrderClassFragmentActivity extends BaseActivity implements FragmentItemOnClickListener{

	private YCKXClassGroupFragment ycxkClassGroupFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderclass_fragment_activity);
		// ��ʼ����Fragment(Ĭ����ʾ������Fragment)
		initFragmentObj();
	}


	private void initFragmentObj() {
		ycxkClassGroupFragment=new YCKXClassGroupFragment();
		smartFragmentReplace(R.id.contentID, ycxkClassGroupFragment);
	}


	/** ��ǰActivity������Fragment����click�¼��ļ����ص�,���ü���������BaseFragment��onAttach�Ѵ��� */
	@Override
	public void onFragmentItemClick(String tag, int eventTagID, Object data) {
		// �Ǵ�HNewsGroupFragmentNews�����Ļص�����,�������������б��ĳһ��
		// �����Ǵ�HCollectFragment�����Ļص�����,�������������б��ĳһ��HNewsGroupFragmentNews
		if (tag.equals(YCKXClassFragmentOrders.class.getSimpleName())) {
			//switch (eventTagID) {
			/*	case R.id.tag_hnewsgroup_enternews:
					case R.id.tag_hcollect_enternews:
						NewslistInfo info = (NewslistInfo) data;*/
			// �������������BrowseActivity
			Bundle bundle = new Bundle();
			//String key = NewslistInfo.class.getSimpleName();
			//bundle.putSerializable(key, info);
			//openActivity(BrowseActivity.class, bundle);
			//	break;
			//}
		}
	}

}
