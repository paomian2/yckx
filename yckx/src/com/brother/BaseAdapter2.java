package com.brother;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 基础适配器, 根据集合内的数据，进行适配
 * 
 * @author yuanc
 * 
 */
public abstract class BaseAdapter2<E> extends BaseAdapter{
	// 当前适配器内的数据集合 (当前适配器适配工作只认此集合)
	private ArrayList<E> adapterDatas = new ArrayList<E>();
	protected LayoutInflater layoutInflater;// 只是一个布局加载器
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

	public void addDataToAdapter(E e) {// 添加数据到当前适配器集合
		if (e != null) {
			adapterDatas.add(e);
		}
	}

	public void addDataToAdapter(List<E> e) {// 添加数据到当前适配器集合
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

	public void removeDataFromAdapter(int index) { // 删除当前适配器集合内数据
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