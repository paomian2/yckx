package com.brother.yckx.control.activity.owner;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brother.BaseActivity;
import com.brother.BaseAdapter2;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.logic.ToastUtil;
import com.brother.yckx.R;
import com.brother.yckx.model.db.AssetsDBManager;
import com.brother.yckx.model.db.CarClassInfo;
import com.brother.yckx.model.db.DBRead;

public class SearCarMessageActivity extends BaseActivity{
	private String tempText;
	private EditText ev_carSearch;
	private ListView carlist;
	private CarlistAdapter adapter;
	private LinearLayout main_layout;
	private ImageView returnBack;
	private TextView searchOk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search_car_message);
		main_layout=(LinearLayout) findViewById(R.id.main_layout);
		//检查是否存在数据库
		if(!DBRead.isExistsCardbFile()){//不存在则加载
			try {
				// 将本地项目中的Assets/db/commonnum.db文件复制写出到 DBRead.telFile文件中
				AssetsDBManager.copyAssetsFileToFile(getApplicationContext(), "db/carsplist.db", DBRead.carFile);
			} catch (IOException e) {
				Toast.makeText(this, "初始化车辆信息数据库异常", Toast.LENGTH_SHORT).show();
			}
		}
		returnBack=(ImageView) findViewById(R.id.return_back);
		returnBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				SearCarMessageActivity.this.finish();

			}});

		searchOk=(TextView) findViewById(R.id.search_OK);
		searchOk.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String evString=ev_carSearch.getText().toString().trim();
				if(evString.equals("")){
					ToastUtil.show(getApplicationContext(), "请选择车辆系列", 2000);
					return;
				}else{
					String comFrom=(String) GlobalServiceUtils.getGloubalServiceSession("add_modifay");
					if(comFrom.equals("add")){
						Intent intent=new Intent(SearCarMessageActivity.this,CarDetailedActivity.class);
						GlobalServiceUtils.setGloubalServiceSession("carName", evString);
						startActivity(intent);
						SearCarMessageActivity.this.finish();
					}
					if(comFrom.equals("modifyCar")){
						Intent intent=new Intent(SearCarMessageActivity.this,CarDetailedActivity.class);
						GlobalServiceUtils.setGloubalServiceSession("carName", evString);
						SearCarMessageActivity.this.setResult(1, intent);//3为resultCode
						SearCarMessageActivity.this.finish();
					}
					
				}

			}});

		ev_carSearch=(EditText) findViewById(R.id.tv_carSearch);
		ev_carSearch.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ev_carSearch.setInputType(InputType.TYPE_CLASS_PHONE);
				ev_carSearch.setFocusableInTouchMode(true);
				ev_carSearch.requestFocus();
				InputMethodManager inputManager = (InputMethodManager)ev_carSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ev_carSearch, 0);
				main_layout.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener(){
							@Override
							public void onGlobalLayout()
							{
								int heightDiff = main_layout.getRootView().getHeight() - main_layout.getHeight();
								if (heightDiff > 100)
								{ // 说明键盘是弹出状态
									//ToastUtil.show(getApplicationContext(), "弹出", 2000);
								} else{
									ev_carSearch.setInputType(InputType.TYPE_NULL);
									ev_carSearch.setFocusableInTouchMode(false);
									//ToastUtil.show(getApplicationContext(), "隐藏", 2000);
								}
							}
						});
			}});

		ev_carSearch.addTextChangedListener(watcher);
		carlist=(ListView) findViewById(R.id.carlist);
		adapter=new CarlistAdapter(this);
		carlist.setAdapter(adapter);
		adapter.addDataToAdapter(DBRead.readCardbClasslist());
		adapter.notifyDataSetChanged();
		carlist.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				CarClassInfo info=adapter.getItem(arg2);
				tempText=info.getName();
				ev_carSearch.setText(tempText);
			}});
	}


	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			//操作数据库
			ArrayList<CarClassInfo> list=DBRead.readCardbClasslistForKey(arg0.toString().trim());
			adapter.clearAdapter();
			adapter.addDataToAdapter(list);
			adapter.notifyDataSetChanged();

		}

	};



	class CarlistAdapter extends BaseAdapter2<CarClassInfo>{

		public CarlistAdapter(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1==null){
				arg1=layoutInflater.inflate(R.layout.row_carlist, null);
			}

			String carMsg=getItem(arg0).getName();
			TextView tv_carName=(TextView) arg1.findViewById(R.id.carItem);
			tv_carName.setText(""+carMsg);
			return arg1;
		}}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_BACK){
			String evString=ev_carSearch.getText().toString().trim();
			Intent intent=new Intent(SearCarMessageActivity.this,CarDetailedActivity.class);
			intent.putExtra("AddCarActivity", evString);
			GlobalServiceUtils.setGloubalServiceSession("carName", evString);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}






}
