package com.brother.yckx.control.activity.owner;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.BaseAdapter2;
import com.brother.utils.GlobalServiceUtils;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.utils.net.JavaHttpUtil;
import com.brother.utils.net.StreamUtil;
import com.brother.utils.parse.ParseJSONUtils;
import com.brother.yckx.R;
import com.brother.yckx.model.CardEntity;
import com.brother.yckx.view.image.CacheImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * ����������
 * */
public class GarageManagerActivity extends BaseActivity{
	private ListView cardListview;
	private List<CardEntity> list;
	private CardManagerAdapter adapter;
	/**��ȡ�����б����ݳɹ�*/
	private final int GETCARLISTSUCCESS=0;
	/**tokenֵʧЧ*/
	private final int TOKENERROR=2;
	/**�޸ĳ����ɹ�*/
	private final int MODIFYCARSUCESSED=1;
	/**ɾ�������ɹ�*/
	private final int DELETECARSUCCESS=3;
	
	private final int RESULTCODE_ADDCARD_UPDATE=30;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_garage_manager);
		setActionBar(R.string.carManagerTitle, R.drawable.btn_homeasup_default, R.drawable.icon_default_add, listener);
		cardListview=(ListView) findViewById(R.id.cardManagerListView);
		adapter=new CardManagerAdapter(this,imageLoader);
		cardListview.setAdapter(adapter);
		searchCarListInbackground();
		cardListview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent(GarageManagerActivity.this,CarDetailedActivity.class);
				CardEntity entity=(CardEntity) arg0.getItemAtPosition(arg2);
				//�����¸�ҳ������ӻ����޸�
				GlobalServiceUtils.setGloubalServiceSession("add_modifay", "modifyCar");
				 GlobalServiceUtils.getInstance().setGlobalServiceCarMessage(entity);
				 startActivityForResult(intent, RESULTCODE_ADDCARD_UPDATE);
			}});
	}


	private void searchCarListInbackground(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(GarageManagerActivity.this).getUserToken();
				String userId=PrefrenceConfig.getUserMessage(GarageManagerActivity.this).getUserId();
				String url=WEBInterface.SEARCH_CARLIST+userId;
				String respose=ApacheHttpUtil.httpGet(url, token, null);
				Log.d("yckx", "-->>searchCarListInbackground()�õ��ĳ����б�����"+respose);
				//��������
				list=ParseJSONUtils.parseCarList(respose);
				if(list!=null){
					//��Ĭ�ϵĴ洢���ļ���
					for(int i=0;i<list.size();i++){
						if(list.get(i).isDefault()){
							PrefrenceConfig.setCarMessage(GarageManagerActivity.this,list.get(i));
						}
					}
				}
				Message msg=new Message();
				msg.what=GETCARLISTSUCCESS;
				mHandler.sendMessage(msg);
			}
		}).start();
	}


	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==GETCARLISTSUCCESS){
				adapter.clearAdapter();
				adapter.addDataToAdapter(list);
				adapter.notifyDataSetChanged();
			}
			if(msg.what==MODIFYCARSUCESSED){
				//ˢ��ҳ��
				CardEntity entity=(CardEntity) msg.obj;
				PrefrenceConfig.setCarMessage(GarageManagerActivity.this, entity);
				adapter.clearAdapter();
				searchCarListInbackground();
			}
			if(msg.what==DELETECARSUCCESS){
				//ˢ��ҳ��
				adapter.clearAdapter();
				searchCarListInbackground();
				ToastUtil.show(getApplicationContext(), "ɾ���ɹ�", 2000);
			}
			if(msg.what==TOKENERROR){
				Intent intent=new Intent(GarageManagerActivity.this,LoginActivity.class);
				startActivity(intent);
				GarageManagerActivity.this.finish();
			}
		};
	};

	private View.OnClickListener listener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.iv_action_right:
				//��ӳ���
				Intent intent=new Intent(GarageManagerActivity.this,SearCarMessageActivity.class);
				GlobalServiceUtils.setGloubalServiceSession("add_modifay", "add");
				startActivity(intent);
				/*Intent intent=new Intent(GarageManagerActivity.this,CarDetailedActivity.class);
				intent.putExtra("AddCarActivity", "SearCarMessageActivity");
				startActivityForResult(intent, RESULTCODE_ADDCARD_UPDATE);*/
				break;
			case R.id.iv_action_left:
				GarageManagerActivity.this.finish();
				break;
			}

		}
	};

	class CardManagerAdapter extends BaseAdapter2<CardEntity>{

		private ImageLoader imageLoader;
		private DisplayImageOptions options;
		
		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}
		
		//��������
		/*@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}*/
		
		public CardManagerAdapter(Context context,ImageLoader imageLoader) {
			super(context);
			this.imageLoader=imageLoader;
			/**
			 * ����
			 */
			options = new DisplayImageOptions.Builder()
						.showImageForEmptyUri(R.drawable.logo)
						.showImageOnFail(R.drawable.logo)
						.resetViewBeforeLoading(true)
						.cacheOnDisc(true)
						.imageScaleType(ImageScaleType.EXACTLY)
						.bitmapConfig(Bitmap.Config.RGB_565)
						.displayer(new FadeInBitmapDisplayer(300))
						.build();
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(arg1==null){
				arg1=layoutInflater.inflate(R.layout.row_activity_cardmanager, null);
			}
			ImageView  carImg=(ImageView ) arg1.findViewById(R.id.cardImg);
			TextView carNo=(TextView) arg1.findViewById(R.id.cardNo);
			TextView carName=(TextView) arg1.findViewById(R.id.carName);
			TextView carColor=(TextView) arg1.findViewById(R.id.carColor);
			final CardEntity carEntity=getItem(arg0);
			//carImg.setImageUrl();
			//carImg.setImageUrl(WEBInterface.INDEXIMGURL+carEntity.getCarImage());
			imageLoader.displayImage(WEBInterface.INDEXIMGURL+carEntity.getCarImage(), carImg);
			carName.setText(carEntity.getCarBrand());
			carNo.setText(carEntity.getCarNum());
			carColor.setText(carEntity.getCarColor());
			/*if(carEntity.isDefault()){
				setDef.setImageResource(R.drawable.icon_tick_selected);
				//д���ļ���
				PrefrenceConfig.setCarMessage(GarageManagerActivity.this,carEntity);
			}else{
				setDef.setImageResource(R.drawable.icon_tick_normal);
			}
			setDef.setTag(arg0);
			setDef.setOnClickListener(new OnClickListener(){
				@SuppressLint("NewApi") @Override
				public void onClick(View arg0) {
					ToastUtil.show(getApplicationContext(), "����Ĭ��", 2000);
					//�޸ĳ�����Ϣ	
					// Dialog dialog=new Dialog(context, R.style.dialog);
					AlertDialog.Builder dialog=new AlertDialog.Builder(context,R.style.dialog);
					dialog.setTitle("����"+(list.get((Integer)setDef.getTag()).getCarBrand()));
					dialog.setPositiveButton("��", new android.content.DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							CardEntity entity=list.get((Integer) setDef.getTag());
							entity.setDefault(true);
							//�ϴ�������
							updateCarMessage(entity);
						}});
					dialog.setNegativeButton("��", null);
					dialog.show();

				}});
			final ImageView editor=(ImageView) arg1.findViewById(R.id.editor);
			editor.setTag(arg0);
			editor.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					GlobalServiceUtils msu=GlobalServiceUtils.getInstance();
					msu.setGlobalServiceCarMessage(list.get((Integer)editor.getTag()));
					Intent intent=new Intent(GarageManagerActivity.this,AddCarActivity.class);
					intent.putExtra("AddCarActivity", "modifyCar");
					startActivityForResult(intent,RESULTCODE_ADDCARD_UPDATE);
				}});
			final ImageView delete=(ImageView) arg1.findViewById(R.id.delete);
			delete.setTag(arg0);
			delete.setOnClickListener(new OnClickListener(){
				@SuppressLint("NewApi") @Override
				public void onClick(View arg0) {
					AlertDialog.Builder dialog=new AlertDialog.Builder(context,R.style.dialog);
					dialog.setTitle("ɾ��"+(list.get((Integer)setDef.getTag()).getCarBrand()));
					dialog.setPositiveButton("��", new android.content.DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							deleteCar((list.get((Integer)delete.getTag()).getId()));
						}});
					dialog.setNegativeButton("��", null);
					dialog.show();
				}});*/
			return arg1;
		}

	}

	/**�޸ó�����Ϣ*/
	private void updateCarMessage(final CardEntity entity){
		final HashMap<String, String> hasmp=new HashMap<String, String>();
		hasmp.put("id", entity.getId());
		hasmp.put("carNum", entity.getCarNum());
		hasmp.put("province", entity.getProvince());
		hasmp.put("carBrand", entity.getCarBrand());
		hasmp.put("isDefault", entity.isDefault()+"");
		hasmp.put("carColor", entity.getCarColor());
		hasmp.put("carImage", entity.getCarImage());
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(GarageManagerActivity.this).getUserToken();
				String respose = null;
				try {
					respose = ApacheHttpUtil.httpPost2(WEBInterface.ADD_OR_MODIFY_CAR_URL, token, new StringEntity(new JSONObject(hasmp).toString(),"UTF-8"));
					//��������
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					Message msg=new Message();
					if(code.equals("0")){
						msg.what=MODIFYCARSUCESSED;
						msg.obj=entity;
						mHandler.sendMessage(msg);
					}
					if(code.equals("2")){
						//tokenʧЧ
						msg.what=TOKENERROR;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("-->>updateCarMessage()�޸ĳ���������������"+respose);
			}
		}).start();
	}

	/**ɾ��ָ���ĳ�����Ϣ*/
	private void deleteCar(final String id){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url=WEBInterface.DELETECAR+id;
				String token=PrefrenceConfig.getUserMessage(GarageManagerActivity.this).getUserToken();
				try {
					String respose=ApacheHttpUtil.httpPost(url, token, null);
					System.out.println("--->>deleteCar()ɾ���������ص�����"+respose);
					JSONObject jsonObject=new JSONObject(respose);
					String code=jsonObject.getString("code");
					if(code.equals("0")){
						//�ɹ�
						Message msg=new Message();
						msg.what=DELETECARSUCCESS;
						mHandler.sendMessage(msg);
					}
					if(code.equals("2")){
						Message msg=new Message();
						msg.what=TOKENERROR;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub  RESULTCODE_ADDCARD_UPDATE
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULTCODE_ADDCARD_UPDATE){
			//ˢ�½���
			Log.d("yckx", data.toString());
			searchCarListInbackground();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent intent=new Intent(this,OrderDetaileActivity.class);
			intent.putExtra("carMessage", "������Ϣ");
			this.setResult(0, intent);
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
