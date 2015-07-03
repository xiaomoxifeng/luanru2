package com.haoge.luanru.baseAdapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public abstract class BaseListAdapter<E> extends BaseAdapter {
	public static final int HANDLER_IMAGE_LOAD_SUCCESS = 0;
	public List<E> mlist;
	public Context mContext;
	public LayoutInflater mInflater;
	public AbsListView mListView;
	// 声明任务队列
	public List<ImageLoadTask> tasks = new ArrayList<ImageLoadTask>();
	// 声明一个工作线程 用于批量加载图片
	private Thread workThread;
	private boolean isLoop = true;
	// 声明缓存区
	private Map<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();
	// 声明Handler 设置Bitmap
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLER_IMAGE_LOAD_SUCCESS:
				// 获取ImageLoadTask
				ImageLoadTask task = (ImageLoadTask) msg.obj;
				// 给相应的ImageView设置Bitmap
				ImageView iv = (ImageView) mListView.findViewWithTag(task.path);
				if (iv != null) {
					iv.setImageBitmap(task.bitmap);
				}
				break;
			}
		};
	};
	public class ImageLoadTask {
		public String path;
		public Bitmap bitmap;
	}

	/**
	 * 需要加载带图片的listView
	 * @param context
	 * @param list
	 * @param lv
	 */
	public BaseListAdapter(Context context, List<E> list, AbsListView lv) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mlist = list;
		mInflater = LayoutInflater.from(mContext);
		mListView = lv;
		workThread = new Thread() {
			public void run() {
				// 轮循任务队列
				while (isLoop) {
					if (!tasks.isEmpty()) {
						// 取出任务对象 执行图片下载任务
						ImageLoadTask task = tasks.remove(0);
						Bitmap bitmap = loadBitmap(task);
						// 设置到相应的ImageView中
						task.bitmap = bitmap;
						Message msg = new Message();
						msg.what = HANDLER_IMAGE_LOAD_SUCCESS;
						msg.obj = task;
						handler.sendMessage(msg);
					} else {
						// 工作线程等待
						synchronized (workThread) {
							try {
								workThread.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		};
		workThread.start();
	}
public BaseListAdapter(Context context, List<E> list) {
	// TODO Auto-generated constructor stub
	this.mContext = context;
	this.mlist = list;
	mInflater = LayoutInflater.from(mContext);
}

	public Bitmap loadBitmap(ImageLoadTask task) {
		// 先从缓存中获取 看有没有下载过
		SoftReference<Bitmap> ref = cache.get(task.path);
		if (ref != null && ref.get() != null) {
			Log.i("info", "从缓存中读取的图片" + task.path);
			return ref.get();
		}
		Bitmap bitmap = bindbitmap(task);
		cache.put(task.path, new SoftReference<Bitmap>(bitmap));
		return bitmap;
	}

	public abstract Bitmap bindbitmap(ImageLoadTask task);

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
		convertView = bindView(position, convertView, parent);
		return convertView;
	};

	public abstract View bindView(int position, View convertView,
			ViewGroup parent);
	// 唤醒工作线程继续干活
	public void notifyWorkThread() {
		synchronized (workThread) {
			workThread.notify();
		}
	}

	// 停止工作线程 isLoop改为false
	public void stopWorkThread() {
		this.isLoop = false;
	}
}

