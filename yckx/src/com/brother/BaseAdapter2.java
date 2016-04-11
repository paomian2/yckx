package com.brother;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * ����������, ���ݼ����ڵ����ݣ���������
 * 
 * @author yuanc
 * 
 */
public abstract class BaseAdapter2<E> extends BaseAdapter{
	// ��ǰ�������ڵ����ݼ��� (��ǰ���������乤��ֻ�ϴ˼���)
	private ArrayList<E> adapterDatas = new ArrayList<E>();
	protected LayoutInflater layoutInflater;// ֻ��һ�����ּ�����
	protected Context context;

	public BaseAdapter2(Context context) {
		this.context = context;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ArrayList<E> getDataFromAdapter() {
		return adapterDatas;
	}

	public void clearAdapter() {
		adapterDatas.clear();
	}

	public void addDataToAdapter(E e) {// ������ݵ���ǰ����������
		if (e != null) {
			adapterDatas.add(e);
		}
	}

	public void addDataToAdapter(List<E> e) {// ������ݵ���ǰ����������
		if (e != null) {
			adapterDatas.addAll(e);
		}
	}
	public void addDataToAdapterHead(List<E> datas) {
		if (datas != null) {
			adapterDatas.addAll(0, datas);
		}
	}
	
	public void addDataToAdapterEnd(List<E> datas) {
		if (datas != null) {
			adapterDatas.addAll(adapterDatas.size(), datas);
		}
	}

	public void setDataToAdapter(List<E> e) {
		adapterDatas.clear();
		if (e != null) {
			adapterDatas.addAll(e);
		}
	}

	public void removeDataFromAdapter(E e) {
		adapterDatas.remove(e);
	}

	public void removeDataFromAdapter(int index) { // ɾ����ǰ����������������
		adapterDatas.remove(index);
	}

	@Override
	public int getCount() {
		return adapterDatas.size();
	}

	@Override
	public E getItem(int position) {
		// TODO Auto-generated method stub
		return adapterDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}