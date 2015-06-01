package com.haoge.luanru.music.adapter;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoge.luanru.R;
import com.haoge.luanru.app.LuanruApplication;
import com.haoge.luanru.baseAdapter.BaseListAdapter2;
import com.haoge.luanru.baseAdapter.ViewHolder;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.fragment.MusicFragment;
import com.haoge.luanru.music.fragment.OnlineMusicFragment;
import com.haoge.luanru.music.util.ArtworkUtils;
import com.haoge.luanru.music.util.FileUtils;
import com.haoge.luanru.music.util.ImageDownLoader;
import com.haoge.luanru.music.util.ImageDownLoader.onImageLoaderListener;

public class LocalMusicAdapter extends BaseListAdapter2 {
	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	private LayoutInflater mInflater;
	private AbsListView mAbsView;
	private Context mContext;
	private List<Music> mls;
	private LuanruApplication app;
	private OnlineMusicFragment mOnlineMusicFragment;

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
		
	}

	// 监听下滑事件


		/**
		 * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，在没有就开启线程去下载
		 * 
		 * @param mFirstVisibleItem
		 * @param mVisibleItemCount
		 */
		private void showImage(int Count) {
			// TODO Auto-generated method stub
			Bitmap bitmap = null;
			for (int i = 0; i < Count; i++) {
	
			}
		};


	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_lv_music, null);
		}
		final ImageView ivAlbum = ViewHolder.get(convertView, R.id.ivAlbum);
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
		//ivAlbum.setTag(m.getAlbumpic().getFileUrl(mContext));
		// setImageView(m.getAlbumpic(), ivAlbum);
		String path = m.getL_albumpic();
		Bitmap bitmap = FileUtils.loadBitmap(path, 50, 50);
		if (bitmap != null) {
			ivAlbum.setImageBitmap(bitmap);
		} else {
			bitmap=ArtworkUtils.getArtwork(mContext, m.get_id(), m.getAlbumpicId(),false);
			if(bitmap != null){
				ivAlbum.setImageBitmap(bitmap);
			}else{
				ivAlbum.setImageResource(R.drawable.img_album_background);
			}
			
		}
		return convertView;
	}

}
