package com.brother.yckx.control.activity.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.SubPoiItem;
import com.brother.BaseActivity;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.MyBitmapUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.brother.yckx.control.activity.owner.HomeActivity;

public class POIGJZSearchActivity extends BaseActivity implements OnPoiSearchListener, TextWatcher, InputtipsListener{
	private String fileName = "/sdcard/yckx/yckx_maobili_temp.png";
	//高斯模糊图片
	private ImageView gaosiImage;
	private String city = "";
	private PoiListAdapter mpoiadapter;
	private ListView mPoiSearchList;
	private ImageView mSearchbtn;
	private AutoCompleteTextView mKeywordText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poigjzsearch);
		findViewById(R.id.btn_return).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				GlobalServiceUtils.setGloubalServiceSession("mapStyle", "location");
				Intent intent=new Intent(POIGJZSearchActivity.this,HomeActivity.class);
				startActivity(intent);
				POIGJZSearchActivity.this.finish();
			}});
		gaosiImage=(ImageView) findViewById(R.id.gaosiImage);
		//test
		//Bitmap gasi=MyBitmapUtils.getBitmapFromResourse(POIGJZSearchActivity.this,R.drawable.icon_gaosiimage_demo);
		//重sdcard中获取截图
		Bitmap gasi=MyBitmapUtils.getBitmapFromSDCard(fileName);
		Bitmap maobili=null;
		if(gasi!=null){
			maobili=MyBitmapUtils.fastblur(gasi, 10);
		}else{
			ToastUtil.show(getApplicationContext(), "gasi的bitmap为空",2000);
		}
		if(maobili!=null){
			gaosiImage.setImageBitmap(maobili);
		}else{
			ToastUtil.show(getApplicationContext(), "毛玻璃bitmap为空",2000);
		}
		 mPoiSearchList = (ListView)findViewById(R.id.listView);
	        mKeywordText = (AutoCompleteTextView)findViewById(R.id.keyword);
	        mKeywordText.addTextChangedListener(this);
	        mSearchbtn = (ImageView)findViewById(R.id.search_btn);
	        mSearchbtn.setOnClickListener(new OnClickListener() {          
	            @Override
	            public void onClick(View v) {
	                String keyword = mKeywordText.getText().toString();
	                poi_Search(keyword);   
	            }
	        });
	        poi_Search("");
	        //点击获取当前经纬度返回定位
	        mPoiSearchList.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					PoiItem poiItem=(PoiItem) arg0.getAdapter().getItem(arg2);
					//String mstr=poiItem.getLatLonPoint().getLatitude()+"---"+poiItem.getLatLonPoint().getLongitude();
					//ToastUtil.show(getApplicationContext(), mstr, 20000);
				    //保存搜索到的经纬度
					GlobalServiceUtils.setGloubalServiceSession("searchLat", poiItem.getLatLonPoint().getLatitude());
					GlobalServiceUtils.setGloubalServiceSession("searchLng", poiItem.getLatLonPoint().getLongitude());
					GlobalServiceUtils.setGloubalServiceSession("searchAdress", poiItem.getTitle());
					GlobalServiceUtils.setGloubalServiceSession("mapStyle", "searchPOI");
					Intent intent=new Intent(POIGJZSearchActivity.this,HomeActivity.class);
					startActivity(intent);
					POIGJZSearchActivity.this.finish();
				}});
	}
	
	
	private void poi_Search(String str){
        PoiSearch.Query mPoiSearchQuery = new PoiSearch.Query(str, "", city);  
        mPoiSearchQuery.requireSubPois(true);   //true 搜索结果包含POI父子关系; false 
        mPoiSearchQuery.setPageSize(10);
        mPoiSearchQuery.setPageNum(0);
        PoiSearch poiSearch = new PoiSearch(this,mPoiSearchQuery);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();        
    }

    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        if (rCode == 0) {
            List<PoiItem> poiItems = new ArrayList<PoiItem>();
            poiItems.add(item);
            mpoiadapter=new PoiListAdapter(this, poiItems);
            mPoiSearchList.setAdapter(mpoiadapter);
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == 0) {
            if (result != null ) {
                List<PoiItem> poiItems = result.getPois();
                mpoiadapter=new PoiListAdapter(this, poiItems);
                mPoiSearchList.setAdapter(mpoiadapter);                    
            }                      
        }        
    }

    @Override
    public void afterTextChanged(Editable s) {
        
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        
    }
    
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
        
    }

    @Override
	public void onGetInputtips(List<Tip> tipList, int rCode) {
    	if (rCode == 0) {
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            mKeywordText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        }
	}
    
    
    /**适配器*/
    class PoiListAdapter extends BaseAdapter{
        private Context ctx;
        private List<PoiItem> list;

        public PoiListAdapter(Context context, List<PoiItem> poiList) {
            this.ctx = context;
            this.list = poiList;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(ctx, R.layout.listview_item, null);
                holder.poititle = (TextView) convertView
                        .findViewById(R.id.poititle);
               // holder.subpois = (GridView) convertView.findViewById(R.id.listview_item_gridview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PoiItem item = list.get(position);
            holder.poititle.setText(item.getTitle());
            /*if (item.getSubPois().size() > 0) {
                List<SubPoiItem> subPoiItems = item.getSubPois();
               // SubPoiAdapter subpoiAdapter=new SubPoiAdapter(ctx, subPoiItems); 
              //  holder.subpois.setAdapter(subpoiAdapter); 
            }*/
            return convertView;
        }
        private class ViewHolder {
            TextView poititle;
           // GridView subpois;
        }

    }
    
    
    class SubPoiAdapter extends BaseAdapter {

        private Context ctx;
        private List<SubPoiItem> list; 
        
        public SubPoiAdapter(Context context, List<SubPoiItem> poiList) {
            this.ctx = context;
            this.list = poiList;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(ctx, R.layout.gridview_item, null);
                holder.poititle = (TextView) convertView
                        .findViewById(R.id.gridview_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            
            SubPoiItem item = list.get(position);
            holder.poititle.setText(item.getSubName());
            
            return convertView;
        }
        private class ViewHolder {
            TextView poititle;
        }
    }
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		GlobalServiceUtils.setGloubalServiceSession("mapStyle", "location");
			Intent intent=new Intent(POIGJZSearchActivity.this,HomeActivity.class);
			startActivity(intent);
			POIGJZSearchActivity.this.finish();
    	}
    	return super.onKeyDown(keyCode, event);
    }
}
