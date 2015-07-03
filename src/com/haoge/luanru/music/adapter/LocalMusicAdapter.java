package com.haoge.luanru.music.adapter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoge.luanru.R;
import com.haoge.luanru.app.LuanruApplication;
import com.haoge.luanru.baseAdapter.BaseListAdapter2;
import com.haoge.luanru.baseAdapter.ViewHolder;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.fragment.OnlineMusicFragment;
import com.haoge.luanru.music.util.ArtworkUtils;
import com.haoge.luanru.music.util.FileUtils;
import com.haoge.luanru.music.util.ImageDownLoader;

public class LocalMusicAdapter extends BaseListAdapter2 {
	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	public static final int HANDLER_IMAGE_LOAD_SUCCESS_2 = 0;
	private LayoutInflater mInflater;
	private AbsListView mAbsView;
	private Context mContext;
	private List<Music> mls;
	private LuanruApplication app;
	private OnlineMusicFragment mOnlineMusicFragment;
	// 声明一个工作线程 用于批量加载图片
	private Thread workThread;
	/**
	 * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
	 */
	private LruCache<String, Bitmap> mMemoryCache;
	private boolean isLoop = true;
	// 声明任务队列
	private List<ImageLoadTask> tasks = new ArrayList<ImageLoadTask>();
	// 声明Handler 设置Bitmap
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLER_IMAGE_LOAD_SUCCESS_2:
				// 获取ImageLoadTask
				ImageLoadTask task = (ImageLoadTask) msg.obj;
				// 给相应的ImageView设置Bitmap
				ImageView iv = (ImageView) mAbsView.findViewWithTag(task.path);
				if (iv != null) {
					iv.setImageBitmap(task.bitmap);
				}
				break;
			}
		}
	};

	/**
	 * Image 下载器
	 */
	private ImageDownLoader mImageDownLoader;

	public LocalMusicAdapter(Context context, List<Music> list,AbsListView lbs) {
		super(context, list);
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mImageDownLoader = new ImageDownLoader(context);
		this.mls = list;
		this.mAbsView=lbs;
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int mCacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {
			// 必须重写此方法，来测量Bitmap的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes() * value.getHeight();
			}
		};
		workThread = new Thread() {
			public void run() {
				// 轮循任务队列
				while (isLoop) {
					if (!tasks.isEmpty()) {
						// 取出任务对象 执行图片下载任务
						ImageLoadTask task = tasks.remove(0);
						Bitmap bitmap= loadBitmap(task);
						// 设置到相应的ImageView中
						task.bitmap = bitmap;
						Message msg = new Message();
						msg.what = HANDLER_IMAGE_LOAD_SUCCESS_2;
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
	class ImageLoadTask {
		String path;
		int _id;
		int albumpicId;
		Bitmap bitmap;
	}
	/**
	 * 添加Bitmap到内存缓存
	 * 
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null && bitmap != null) {
			mMemoryCache.put(key, bitmap);
		}
	}
	/**
	 * 从内存缓存中获取一个Bitmap
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapFromMemCache(String key) {
		// 替换Url中非字母和非数字的字符，这里比较重要，因为我们用Url作为文件名，比如我们的Url
		// 是Http://xiaanming/abc.jpg;用这个作为图片名称，系统会认为xiaanming为一个目录，
		// 我们没有创建此目录保存文件就会保存

		return mMemoryCache.get(key);
	}


	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_lv_music, null);
		}
		ImageView ivAlbum = ViewHolder.get(convertView, R.id.ivAlbum);
		TextView tvName = ViewHolder.get(convertView, R.id.tvName);
		// tvName.setText(text);
		TextView tvSinger = ViewHolder.get(convertView, R.id.tvSinger);
		TextView tvAuthor = ViewHolder.get(convertView, R.id.tvAuthor);
		TextView tvDuration = ViewHolder.get(convertView, R.id.tvDuration);
		Music m = (Music) mlist.get(position);
		tvName.setText(m.getName());
		tvAuthor.setText(m.getAuthor());
		tvSinger.setText(m.getSinger());
		tvDuration.setText(m.getDurationtime());
		// 设置imageView的tag 用于获取了bitmap后调用imageView.setImageBitmap()
		ivAlbum.setTag(m.getL_albumpic());
		// setImageView(m.getAlbumpic(), ivAlbum);
		// 向任务队列中添加一个任务
		ImageLoadTask task = new ImageLoadTask();
		task.path = m.getL_albumpic();
		task._id = m.get_id();
		task.albumpicId=m.getAlbumpicId();
		tasks.add(task);
		notifyWorkThread();
		return convertView;
	}
	private Bitmap loadBitmap(ImageLoadTask task){
		Bitmap bitmap =getBitmapFromMemCache(task.path);
		if (bitmap != null) {
			return bitmap;
		}
		 bitmap = FileUtils.loadBitmap(task.path, 50, 50);
		if (bitmap != null) {
			addBitmapToMemoryCache(task.path, bitmap);
			return bitmap;
		} else {
			bitmap=ArtworkUtils.getArtwork(mContext, task._id, task.albumpicId,false);
			if(bitmap != null){
				addBitmapToMemoryCache(task.path, bitmap);
				return bitmap;
			}else{
				return bitmap=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_album_background);
			}
			
		}
	}
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
