package com.brother.yckx.control.activity.washer;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.utils.PrefrenceConfig;
import com.brother.utils.WEBInterface;
import com.brother.utils.logic.ToastUtil;
import com.brother.utils.net.ApacheHttpUtil;
import com.brother.yckx.R;
import com.brother.yckx.model.UserEntity;

/**
 * ϴ��ʦ���°��
 * */
public class WasherPunchActivity extends BaseActivity{
	private TextView tv_status,tv_workStatus_after,wendan_show;
	/**�򿨳ɹ�*/
	private final int DAKA_SUCCESS=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_punche);
		setActionBar(R.string.daka_title , R.drawable.btn_homeasup_default, NULL_ID, actionListener);
		String mstatus=PrefrenceConfig.getUserMessage(WasherPunchActivity.this).getUserOpStatus_mhs();
		if(mstatus==null){
			mstatus="outwork";
		}
		tv_status=(TextView) findViewById(R.id.workStatus);
		tv_workStatus_after=(TextView) findViewById(R.id.workStatus_after);
		if(mstatus.equals("outwork")){
			tv_status.setText("�°�");
			tv_workStatus_after.setText("�ϰ�");
		}else if(mstatus.equals("work")){
			tv_status.setText("�ϰ�");
			tv_workStatus_after.setText("�°�");
		}else{
			tv_status.setText("�°�");
			tv_workStatus_after.setText("�ϰ�");
		}

		//��ʾ�İ�48, 74
		wendan_show=(TextView) findViewById(R.id.wendan_show);
		CharSequence mWenAn=getText(R.string.wenan1); 
		Log.d("yckx",mWenAn.length()+"����");
		SpannableString spannableString1 = new SpannableString(mWenAn); 
		spannableString1.setSpan(new ClickableSpan(){  
			
			@Override
			public void updateDrawState(android.text.TextPaint ds) {
				ds.setUnderlineText(false);//���ò�Ҫ�»��߻���
			};
            @Override  
            public void onClick(View widget) {
                  ToastUtil.show(getApplicationContext(), "�����XXX", 2000);
            }  //mWenAn.length()
        }, 48, 62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
        spannableString1.setSpan(new ForegroundColorSpan(Color.RED),48, 62,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        wendan_show.setText(spannableString1);  
        wendan_show.setHighlightColor(Color.TRANSPARENT); //���õ�������ɫΪ͸���������һֱ���ָ���
        wendan_show.setMovementMethod(LinkMovementMethod.getInstance());//���Ч��
		
		findViewById(R.id.btn_daka).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				work();
			}});
	}

	/**��*/
	private void work(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token=PrefrenceConfig.getUserMessage(WasherPunchActivity.this).getUserToken();
				String url=WEBInterface.WASHERWORK;
				String respose=ApacheHttpUtil.httpPost(url, token, null);
				Log.d("yckx",respose+"");
				try {
					JSONObject jSONObject=new JSONObject(respose);
					String code=jSONObject.getString("code");
					if(code.equals("0")){
						String opStatus=PrefrenceConfig.getUserMessage(WasherPunchActivity.this).getUserOpStatus_mhs();
						UserEntity entity=PrefrenceConfig.getUserMessage(WasherPunchActivity.this);
						if(opStatus==null||opStatus.equals("outwork")){
							entity.setUserOpStatus_mhs("work");
						}else if(opStatus.equals("work")){
							entity.setUserOpStatus_mhs("outwork");
						}
						PrefrenceConfig.setLoginUserMessage(WasherPunchActivity.this,entity);
						Message msg=new Message();
						msg.what=DAKA_SUCCESS;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}


	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DAKA_SUCCESS:
				//����ui
				String opstatus=PrefrenceConfig.getUserMessage(WasherPunchActivity.this).getUserOpStatus_mhs();
				if(opstatus.equals("outwork")){
					tv_status.setText("�°�");
					tv_workStatus_after.setText("�ϰ�");
				}else if(opstatus.equals("work")){
					tv_status.setText("�ϰ�");
					tv_workStatus_after.setText("�°�");
				}else{
					tv_status.setText("�°�");
					tv_workStatus_after.setText("�ϰ�");
				}
				break;

			default:
				break;
			}
		};
	};


	private View.OnClickListener actionListener=new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(arg0.getId()==R.id.iv_action_left){
				WasherPunchActivity.this.finish();
			}

		}
	};

}
