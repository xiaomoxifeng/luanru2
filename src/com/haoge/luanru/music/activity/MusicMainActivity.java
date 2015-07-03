package com.haoge.luanru.music.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haoge.luanru.R;
import com.haoge.luanru.baseActivity.SlidingFragment;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.fragment.ILoadMusicListenter;
import com.haoge.luanru.music.fragment.LocalMusicFragment;
import com.haoge.luanru.music.fragment.OnlineMusicFragment;
import com.haoge.luanru.music.service.PlayMusicService;
import com.haoge.luanru.music.util.ArtworkUtils;
import com.haoge.luanru.music.util.FileUtils;
import com.haoge.luanru.music.util.MusicBroadcastActions;
import com.haoge.luanru.music.util.ImageDownLoader;
import com.haoge.luanru.music.util.ImageDownLoader.onImageLoaderListener;
import com.haoge.luanru.music.view.AlwaysMarqueeTextView;
import com.haoge.luanru.utils.DateUtils;
public class MusicMainActivity extends SlidingFragment implements
		MusicBroadcastActions,ILoadMusicListenter {
	/**
	 * View Pager，容纳多个Fragment的容器
	 */
	private ViewPager vpPager;
	private InnerReceiver receiver;
	private ImageView albumpic;
	private ProgressBar sb;
	public  int currentMusicPosition;
	private ImageButton play, pause, next;
	private AlwaysMarqueeTextView title, singer;
	private TextView currnt, duration;
	private InnerStatePagerAdapter adapter;
	private Intent mIntent;
	public static boolean onlineMusicFlag = false;
	private List<Music> musics;
	private ImageDownLoader mImageDownLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置标题栏的标题
		initView();
		initReceiver();
	}

	private void initView() {
		setTitle("音乐世界里别乱跑");
		// 设置是否能够使用ActionBar来滑动
		setSlidingActionBarEnabled(true);
		// 设置是否显示Home图标按钮
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.music_main);
		initSlidingMenu();
		vpPager = (ViewPager) findViewById(R.id.vpPager);
		adapter = new InnerStatePagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapter);
		vpPager.setOnPageChangeListener(new InnerOnPageChangeListener());
		vpPager.setCurrentItem(0);
		title = (AlwaysMarqueeTextView) findViewById(R.id.musicname_tv2);
		singer = (AlwaysMarqueeTextView) findViewById(R.id.singer2);
		currnt = (TextView) findViewById(R.id.tv_music_current_position2);
		duration = (TextView) findViewById(R.id.tv_music_duration2);
		play = (ImageButton) findViewById(R.id.btn_play2);
		pause = (ImageButton) findViewById(R.id.btn_pause2);
		albumpic = (ImageView) findViewById(R.id.iv_albumpic2);
		next = (ImageButton) findViewById(R.id.btn_playNext2);
		sb = (ProgressBar) findViewById(R.id.playback_seekbar2);
		InnerButtonOnClickListener buttonOnClickLitener = new InnerButtonOnClickListener();
		albumpic.setOnClickListener(buttonOnClickLitener);
		play.setOnClickListener(buttonOnClickLitener);
		play.setOnClickListener(buttonOnClickLitener);
		next.setOnClickListener(buttonOnClickLitener);
		pause.setOnClickListener(buttonOnClickLitener);
		// 启动Service
		Intent i = new Intent(this, PlayMusicService.class);
		startService(i);
		
		
	}

	/**
	 * 给fregment回调
	 * 
	 * @param result
	 */
	public void getMusics(List<Music> result) {
		this.musics = result;
	}

	public void getCurrentMusicPosition(int postion) {
		this.currentMusicPosition = postion;
	}

	private final class InnerOnPageChangeListener implements
			ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case 0:
				sendBroadcast(new Intent().setAction(ACTVITY_SET_LOCAL_APP));
				onlineMusicFlag = false;
				break;
			case 1:
				sendBroadcast(new Intent().setAction(ACTVITY_SET_ONLINE_APP));
				onlineMusicFlag = true;
				break;
			}

		}

	}

	/**
	 * ViewPager适配器
	 * 
	 * @author apple
	 */
	private final class InnerStatePagerAdapter extends
			FragmentStatePagerAdapter {

		public InnerStatePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int location) {
			Fragment fragment = null;
			switch (location) {
			case 0:
				fragment= new LocalMusicFragment().setMusicInterface(MusicMainActivity.this);
				break;
			case 1:
				fragment = new OnlineMusicFragment().setMusicInterface(MusicMainActivity.this);
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}
	}

	/**
	 * 注册广播接收者
	 */
	private void initReceiver() {
		receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(SERVICE_PLAYER_PAUSED);
		filter.addAction(SERVICE_PLAYER_PLAYING);
		filter.addAction(SERVICE_UPDATE_PROGRESS);
		filter.addAction(SERVICE_PLAYER_COMPLETION);
		filter.addAction(SERVICE_REFRESH);
		registerReceiver(receiver, filter);
	}

	/**
	 * 广播接收者
	 */
	private final class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (SERVICE_PLAYER_PLAYING.equals(intent.getAction())) {
				// 播放时
				play.setVisibility(View.VISIBLE);
				pause.setVisibility(View.GONE);
			} else if (SERVICE_PLAYER_PAUSED.equals(intent.getAction())) {
				// 播放暂停时
				play.setVisibility(View.GONE);
				pause.setVisibility(View.VISIBLE);
				// }
			} else if (SERVICE_UPDATE_PROGRESS.equals(intent.getAction())) {
				// Service要求更新进度
				int currentPosition = intent.getIntExtra(
						EXTRA_MUSIC_CURRENT_POSITION, 0);
				int durationpost = intent.getIntExtra(EXTRA_MUSIC_DURATION, 0);
				int progress = currentPosition * 100 / durationpost;
				currnt.setText(DateUtils.getTimeString(currentPosition));
				duration.setText(DateUtils.getTimeString(durationpost));
				sb.setProgress(progress);
			} else if (SERVICE_PLAYER_COMPLETION.equals(intent.getAction())) {
				currentMusicPosition++;
				if (currentMusicPosition >= musics.size()) {
					currentMusicPosition = 0;
				}
				refresh();
				//区分完成和刷新
			}else if(SERVICE_REFRESH.equals(intent.getAction())){
				if(intent.getIntExtra(SERVICE_REFRESH, -1)!=-1){
					currentMusicPosition=intent.getIntExtra(SERVICE_REFRESH, -1);
				}
				refresh();
			}
		}
	}

	/**
	 * 按钮监听器
	 * 
	 * @author apple
	 * 
	 */
	private final class InnerButtonOnClickListener implements
			View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_play2:
				// 点击播放按钮时
				System.out.println("btn_play2");
				mIntent = new Intent(ACTVITY_PLAY_OR_PAUSE);
				sendBroadcast(mIntent);
				refresh();
				break;
			case R.id.btn_pause2:
				// 点击播放按钮时
				System.out.println("R.id.btn_pause2");
				mIntent = new Intent(ACTVITY_PLAY_OR_PAUSE);
				sendBroadcast(mIntent);
				break;
			case R.id.btn_playNext2:
				// 点击播放按钮时
				currentMusicPosition++;
				if (currentMusicPosition >= musics.size()) {
					currentMusicPosition = 0;
				}
				refresh();
				mIntent = new Intent(ACTVITY_NEXT);
				sendBroadcast(mIntent);
				break;
			case R.id.iv_albumpic2:
				// 点击播放页面
				Intent i = new Intent(MusicMainActivity.this,
						PlayMusicActivity.class);
				if (!onlineMusicFlag) {
					i.putExtra("Loaclmusic", currentMusicPosition);
				} else {
					i.putExtra("OnLineMusic", currentMusicPosition);
				}
				startActivity(i);
				break;
			}
		}
	}

	/**
	 * 刷新
	 */
	private void refresh() {
		title.setText(musics.get(currentMusicPosition).getName());
		singer.setText(musics.get(currentMusicPosition).getSinger());
		if (onlineMusicFlag) {
			this.mImageDownLoader = new ImageDownLoader(MusicMainActivity.this);
			mImageDownLoader.downloadImage(musics.get(currentMusicPosition)
					.getAlbumpic().getFileUrl(MusicMainActivity.this),
					new onImageLoaderListener() {
						@Override
						public void onImageLoader(Bitmap bitmap, String url) {
							// TODO Auto-generated method stub
							if (albumpic != null && bitmap != null) {
								albumpic.setImageBitmap(bitmap);
							}
						}
					});
		} else {
			Bitmap	bitmap=	FileUtils.loadBitmap(
					musics.get(currentMusicPosition).getL_albumpic(), 100, 100);
			if(bitmap!=null){
				albumpic.setImageBitmap(bitmap);
			}else{
				bitmap=ArtworkUtils.getArtwork(MusicMainActivity.this, musics.get(currentMusicPosition).get_id(), musics.get(currentMusicPosition).getAlbumpicId(),false);
				if(bitmap != null){
					albumpic.setImageBitmap(bitmap);
				}else{
					albumpic.setImageResource(R.drawable.img_album_background);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	//public void setMusicInterface(ILoadMusicListener iLoadMusicListener){
//		this.mLoadMusicListener = iLoadMusicListener;
//		return this;
//	}
	//回调接口
//	public interface ILoadMusicListener{
//		public void onLoadMusic(int position);
//		public void onLoadMusics(List<Music> musics);
//	}

	@Override
	public void onLoadMusic(int position) {
		// TODO Auto-generated method stub
	this.currentMusicPosition = position;
	}

	@Override
	public void onLoadMusics(List<Music> result) {
		// TODO Auto-generated method stub
		this.musics = result;
	}


}
