package com.brother.yckx.control.activity.owner;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brother.BaseActivity;
import com.brother.yckx.R;

public class CompanyIntroductionActivity extends BaseActivity{
	
	private LinearLayout layout_return;
	private TextView companyName;
	private TextView tv_companyIntroduction;
	private TextView companyPhone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_introduction);
		//������һҳ
		layout_return=(LinearLayout) findViewById(R.id.layout_return);
		layout_return.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}});
		//����actionbar�й�˾������
		companyName=(TextView) findViewById(R.id.companyName);
		//���ù�˾���
		tv_companyIntroduction=(TextView) findViewById(R.id.tv_companyIntroduction);
		String instroduction="��˹�ؾƵ������ߡ����������Ƶꡱ�����й���������ټѾƵꡱ�����й��Ƶ���Ʒ��󽱾Ƶꡱ��������������Ʒ�ơ���" +
				"����������θ���Ӫ��ҵ���ȶ�����ἰ��ҵ�����ľ�ѡ���������Ƶ꼯�š��Ƶ���á��ţ��׺ͣ�" +
				"��ʱ�С�����Ʒ���ԡ�һ�����֡�һ�����ȡ�һ���顢һ�����ȡ�һ����͡��� һ��Ϊ�����ĵ����У���ΪƷ�����ţ�5A��ֵ������" +
				"Ϊ���ڷ�æ������;�У�ע������ε�������ѧ���ṩ�ͷ����ĵ��þӿռ䡣��˹�ؾƵ꼯�������ھ�ѡ���������Ƶ�Ŀ����뾭Ӫ��" +
				"Ŀǰ������������Ӫ���ڽ���ǩԼ�Ƶ��Ѹ������졢�������㣨���人���˲������ڡ����������֡���ۡ����ݡ����֡���ɫ���ȵ�����30����ص���У���Ч��Ա��������100���ˡ�" +
				"δ��5���ڣ�������ΪƷ�ƹ������ġ�����Ϊ�����Ƶ���Ӫ֧�����ģ��̾͡������������֡��人������Ϊ���Ĵ���3Сʱ��������Ȧ��ս�Բ��֣�" +
				"�Թ����������������ɹ�ۡ����֡����ݡ����졢��ɽͨ���ݣ��Ͼ����ݡ�������տ�����麣����ɽ�����ڣ���ȡ��ɫ���ӳء����������졢�������ƹ�" +
				"�������ݡ����֡����ݡ����������������޽���������ս�������У�����2018��Ƶ������ﵽ500�����ϣ��Ը��ܶ��������ǣ���ɴӴ�����Ʒ����ȫ������Ʒ�Ƶ�ת�䣬" +
				"�����й�������������ѡ���������Ƶ��һƷ�ƣ�";
		tv_companyIntroduction.setText(instroduction);
		//���ù�˾������
		companyPhone=(TextView) findViewById(R.id.companyPhone);
		companyPhone.setText("400-100");
	}

}
