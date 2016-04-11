package com.brother.yckx.control.activity.owner;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.brother.BaseActivity;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.adapter.activity.HistoricalEvaluationAdapter;
import com.brother.yckx.model.HistoryCommentEntity;
import com.brother.yckx.view.LoadListView;
import com.brother.yckx.view.LoadListView.ILoadListener;

public class HistoricalEvaluationActivity extends BaseActivity implements ILoadListener{
	private LoadListView pingjia_listview;
	private HistoricalEvaluationAdapter adapter;
	
	private List<HistoryCommentEntity> list;
	/**获取历史评论数据成功*/
	private final int LOADHISTORY_COMMENT_SUCCESS=10;
	/**评论内容加载完成*/
    private final int LOADHISTORY_OVER=104;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historical_evaluation);
		setActionBar(R.string.historical_evaluation_title, R.drawable.btn_homeasup_default, NULL_ID, listenr);
		pingjia_listview=(LoadListView) findViewById(R.id.listview);
		pingjia_listview.setInterface(this);
		adapter=new HistoricalEvaluationAdapter(this);
		pingjia_listview.setAdapter(adapter);
		String userId=PrefrenceConfig.getUserMessage(HistoricalEvaluationActivity.this).getUserId();
		getHistoryEvolution(userId);
	}
	
	
	private View.OnClickListener listenr=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(arg0.getId()==R.id.iv_action_left){
				HistoricalEvaluationActivity.this.finish();
			}
		}
	};
	
	private Handler mHandler=new Handler(){
        public void handleMessage(Message msg) {
        	switch (msg.what) {
			case LOADHISTORY_COMMENT_SUCCESS:
				adapter.addDataToAdapter(list);
				adapter.notifyDataSetChanged();
				pingjia_listview.loadComplete();
				break;
			case LOADHISTORY_OVER://加载完成
				pingjia_listview.loadComplete();
				ToastUtil.show(getApplicationContext(), "加载完成",2000);
				break;
			}
        };
	};
	
	
	
	private int page=0;
	private int size=5;
	/**获取评论列表数据*/
	private void getHistoryEvolution(final String userId){
		new Thread(new Runnable() {//测试id:id=12
			@Override
			public void run() {///comment/list/{page}/{size}/{targetOwnerId}
				String url=WEBInterface.HISTORYEVOLOTION_URL+page+"/"+size+"/"+userId;
				String token=PrefrenceConfig.getUserMessage(HistoricalEvaluationActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpGet(url, token, null);
				Log.d("yckx", "评价数据:"+respose);
				list=ParseJSONUtils.parseHistoryCommet(respose);
				if(list!=null){
					//获取数据成功
					if(list.size()==0){
						Message msg=new Message();
						msg.what=LOADHISTORY_OVER;
						mHandler.sendMessage(msg);
					}else{
						Message msg=new Message();
						msg.what=LOADHISTORY_COMMENT_SUCCESS;
						mHandler.sendMessage(msg);
					}
					
				}
			}
		}).start();
	}
	
	/**加载更多*/
	@Override
	public void onLoad() {
		page++;
		String userId=PrefrenceConfig.getUserMessage(HistoricalEvaluationActivity.this).getUserId();
		getHistoryEvolution(userId);
		
	}

}
