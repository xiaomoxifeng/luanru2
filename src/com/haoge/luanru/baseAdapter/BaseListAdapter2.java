package com.haoge.luanru.baseAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.haoge.luanru.music.entity.Music;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
public abstract class BaseListAdapter2<E> extends BaseAdapter {
	private LayoutInflater mInflater;
	public List<E> mlist;
	public BaseListAdapter2(Context context, List<E> list) {
		// TODO Auto-generated constructor stub
		mInflater.from(context);
		this.mlist=list;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView =bindView (position, convertView,  parent);
		// 绑定内部点击监听
		addInternalClickListener(convertView, position, mlist.get(position));
		return convertView;
	}
	public abstract View bindView (int position, View convertView, ViewGroup parent);
	// adapter中的内部点击事件
	public Map<Integer, onInternalClickListener> canClickItem;

	private void addInternalClickListener(final View itemV, final Integer position,final Object valuesMap) {
		if (canClickItem != null) {
			for (Integer key : canClickItem.keySet()) {
				View inView = itemV.findViewById(key);
				final onInternalClickListener inviewListener = canClickItem.get(key);
				if (inView != null && inviewListener != null) {
					inView.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							inviewListener.OnClickListener(itemV, v, position,
									valuesMap);
						}
					});
				}
			}
		}
	}
	//设置每个item内部的触发事件监听NB
	public void setOnInViewClickListener(Integer key,
			onInternalClickListener onClickListener) {
		if (canClickItem == null)
			canClickItem = new HashMap<Integer, onInternalClickListener>();
		canClickItem.put(key, onClickListener);
	}
	public interface onInternalClickListener {
		public void OnClickListener(View parentV, View v, Integer position,
				Object values);
	}
}
