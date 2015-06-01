package com.haoge.luanru.music.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.utils.HttpUtil;
import com.haoge.luanru.utils.NumCores;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

public class ImageDownLoader {
	/**
	 * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
	 */
	private LruCache<String, Bitmap> mMemoryCache;
	
	/**
	 * 操作文件相关类对象的引用
	 */
	 private FileUtils fileUtils;
	/**
	 * 下载Image的线程池
	 */
	private ExecutorService mImageThreadPool = null;

	public ImageDownLoader(Context context) {
		// TODO Auto-generated constructor stub
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
		 fileUtils = new FileUtils(context);
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

	public Bitmap downloadImage(final String url,
			final onImageLoaderListener listener) {
		// 替换Url中非字母和非数字的字符，这里比较重要，因为我们用Url作为文件名，比如我们的Url
		// 是Http://xiaanming/abc.jpg;用这个作为图片名称，系统会认为xiaanming为一个目录，
		// 我们没有创建此目录保存文件就会保存
		final String subUrl = url.replaceAll("[^\\w]", "");
		Bitmap bitmap = showCacheBitmap(subUrl);
		if (bitmap != null) {
			listener.onImageLoader(bitmap, url);
			return bitmap;
		} else {
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					listener.onImageLoader((Bitmap) msg.obj, url);
				}
			};
			getThreadPool().execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Bitmap bitmap2 =null;
					try {
						HttpResponse resp = HttpUtil.send(HttpUtil.METHOD_GET,
								url, null);
						HttpEntity entity = resp.getEntity();
						byte[] bytes = EntityUtils.toByteArray(entity);
						// 调用工具类 获取一个50*50的Bitmap对象
						 bitmap2 = FileUtils.loadBitmap(bytes, 50, 50);
						Message msg = handler.obtainMessage();
						msg.obj = bitmap2;
						handler.sendMessage(msg);
						// 保存在SD卡或者手机目录
					 fileUtils.savaBitmap(subUrl, bitmap2);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 将Bitmap 加入内存缓存
					addBitmapToMemoryCache(subUrl, bitmap2);
				}
			});
		}
		return null;
	}
	/**
	 * 获取Bitmap, 内存中没有就去手机或者sd卡中获取，这一步在getView中会调用，比较关键的一步
	 * @param url
	 * @return
	 */
	public Bitmap showCacheBitmap(String url) {
		// TODO Auto-generated method stub
		if(getBitmapFromMemCache(url)!=null){
			return getBitmapFromMemCache(url);
		}else if (fileUtils.isFileExists(url) && fileUtils.getFileSize(url) != 0){
			Bitmap bitmap = fileUtils.getBitmap(url);
			//将Bitmap 加入内存缓存
			addBitmapToMemoryCache(url, bitmap);
			return bitmap;
		}
		
		return null;
	}

	/**
	 * 异步下载图片的回调接口
	 */
	public interface onImageLoaderListener {
		void onImageLoader(Bitmap bitmap, String url);
	}

	/**
	 * 获取线程池的方法，因为涉及到并发的问题，我们加上同步锁
	 * 
	 * @return
	 */
	public ExecutorService getThreadPool() {
		if (mImageThreadPool == null) {
			synchronized (ExecutorService.class) {
				if (mImageThreadPool == null) {
					// 根据手机是几核的来决定线程池中线程的个数
					mImageThreadPool = Executors.newFixedThreadPool(NumCores
							.getNumCores());
				}
			}
		}
		return mImageThreadPool;

	}
	/**
	 * 取消正在下载的任务
	 */
	public synchronized void cancelTask() {
		if(mImageThreadPool != null){
			mImageThreadPool.shutdownNow();
			mImageThreadPool = null;
		}
	}
}
