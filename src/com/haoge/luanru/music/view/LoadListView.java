package com.haoge.luanru.music.view;

import com.haoge.luanru.R;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class LoadListView extends ListView implements OnScrollListener {
	// 底部布局
	View footer;
	// 总
	int totalItemCount;
	// 最后
	int lastVisibleItem;
	private  ILoadListener mLoadListener;
	boolean isLoading;

	public LoadListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 添加底部布局
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.footer_layout, null);
		footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
		this.addFooterView(footer);
		this.setOnScrollListener(this);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (totalItemCount == lastVisibleItem
				&& scrollState == SCROLL_STATE_IDLE) {
			if (!isLoading) {
				footer.findViewById(R.id.load_layout).setVisibility(
						View.VISIBLE);
				mLoadListener.onLoad();
			}

		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.lastVisibleItem = firstVisibleItem + visibleItemCount;
		this.totalItemCount = totalItemCount;

	}
	public void setInterface(ILoadListener iLoadListener){
		this.mLoadListener = iLoadListener;
	}
	//通知listView加载完毕
	public void loadComplete(){
		isLoading =false;
		footer.findViewById(R.id.load_layout).setVisibility(
				View.GONE);
		
	}
	
	//加载更多数据的回调接口
	public interface ILoadListener{
		public void onLoad();
	}

}
