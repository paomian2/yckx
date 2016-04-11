package com.brother.yckx.control.activity.washer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.utils.view_def.listview.pulltorefresh.RefreshTime;
import com.brother.utils.view_def.listview.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView;
import com.brother.utils.view_def.listview.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.IXListViewListener;
import com.brother.utils.view_def.listview.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.OnMenuItemClickListener;
import com.brother.utils.view_def.listview.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.OnSwipeListener;
import com.brother.utils.view_def.listview.swipemenulistview.SwipeMenu;
import com.brother.utils.view_def.listview.swipemenulistview.SwipeMenuCreator;
import com.brother.utils.view_def.listview.swipemenulistview.SwipeMenuItem;
import com.brother.yckx.R;
import com.brother.yckx.adapter.activity.NoticeAdapter;
import com.brother.yckx.control.activity.owner.FormDetailActivity;
import com.brother.yckx.model.MessageEntity;
import com.brother.yckx.model.NoticeEntity;
public class WasherNoticeListActivity extends BaseActivity implements IXListViewListener{
	
	private PullToRefreshSwipeMenuListView listView;
	private NoticeAdapter adapter;
	private Handler mhanler;
	private final int LOAD_MESSAGE_LIST_SUCCESS=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		setActionBar(R.string.noticeTitle, R.drawable.btn_homeasup_default, NULL_ID, listener);
		
		listView=(PullToRefreshSwipeMenuListView) findViewById(R.id.listView);
		adapter=new NoticeAdapter(this);
		listView.setAdapter(adapter);
		mhanler=new Handler();
		setListViewSetting();
		doInbackgound();
	}
	
	private int page=0;
	private int size=4;
	
	private void doInbackgound(){
		new Thread(new Runnable() {
			@Override
			public void run() {//message/list/{page}/{size}
				String host=WEBInterface.MESSAGE_URL+page+"/"+size;
				String token=PrefrenceConfig.getUserMessage(WasherNoticeListActivity.this).getUserToken();
				String respose=ApacheHttpUtil.httpGet(host, token, null);
				Log.d("yckx", ""+respose);
				if(respose!=null){
					List<MessageEntity> list=ParseJSONUtils.parseOwnMessageList(respose);
					Message msg=new Message();
					msg.what=LOAD_MESSAGE_LIST_SUCCESS;
					msg.obj=list;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_MESSAGE_LIST_SUCCESS:
				List<MessageEntity> list=(List<MessageEntity>) msg.obj;
				adapter.addDataToAdapter(list);
				adapter.notifyDataSetChanged();
				//
				listView.stopRefresh();
		        listView.stopLoadMore();
				break;

			default:
				break;
			}
		};
	};
	
	private void setListViewSetting(){
		
		//设置Listview下来刷新，侧滑属性
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		// step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("详情");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                menu.addMenuItem(openItem);
            }
        };
        
     // set creator
        listView.setMenuCreator(creator);

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                //ApplicationInfo item = mAppList.get(position);
                switch (index) {
                case 0:
                    //打开订单详情
                	if(adapter.getItem(position).getLinkId().equals("")){
                		//订单超额
                		ToastUtil.show(getApplicationContext(), "未处理订单已达上限", 2000);
                		return;
                	}
                	GlobalServiceUtils.setGloubalServiceSession("FormDetailActivity", "NoticeListActivity");
                	GlobalServiceUtils.setGloubalServiceSession("orderId",adapter.getItem(position).getLinkId());
                	Intent intent=new Intent(WasherNoticeListActivity.this,FormDetailActivity.class);
                	intent.putExtra("orderId", adapter.getItem(position).getLinkId());
                	startActivity(intent);
                    break;
                }
            }
        });

        // set SwipeListener
        listView.setOnSwipeListener(new OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // other setting
        // listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
	}
	
	
	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			WasherNoticeListActivity.this.finish();
		}
	};
	
	private void onLoad() {
        listView.setRefreshTime(RefreshTime.getRefreshTime(getApplicationContext()));
        /**重新加载数据*/
        page++;
        doInbackgound();
    }

    public void onRefresh() {
        mhanler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                RefreshTime.setRefreshTime(getApplicationContext(), df.format(new Date()));
                onLoad();
            }
        }, 2000);
    }

    public void onLoadMore() {
    	mhanler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 2000);
    }
	
	 private int dp2px(int dp) {
	        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	    }

}
