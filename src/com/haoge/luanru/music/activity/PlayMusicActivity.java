package com.haoge.luanru.music.activity;

import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.haoge.luanru.R;
import com.haoge.luanru.app.LuanruApplication;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.fragment.MusicFragment;
import com.haoge.luanru.music.service.PlayMusicService;
import com.haoge.luanru.music.util.ArtworkUtils;
import com.haoge.luanru.music.util.FileUtils;
import com.haoge.luanru.music.util.MusicBroadcastActions;
import com.haoge.luanru.music.util.ImageDownLoader;
import com.haoge.luanru.music.util.ImageDownLoader.onImageLoaderListener;
import com.haoge.luanru.utils.DateUtils;

public class PlayMusicActivity extends Activity implements MusicBroadcastActions {
	private TextView title, currnt, duration;
	private ImageView albumpic;
	private SeekBar sb;
	private ImageView play, pre, next;
	private List<Music> musics;
	public static int currentMusicPosition;
	private MusicFragment mMusicFragment;
	private Intent mIntent;
	private LuanruApplication app;
	private ImageDownLoader mImageDownLoader;
	/**
	 * 广播接收者
	 */
	private InnerReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getIntent();
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
		// sendBroadcast(new Intent(ACTVITY_PLAY_OR_PAUSE));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 初始化and注册广播，写在这个周期里的原因是必须保证注册于反注册广播的成对出现
		initReceiver();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 取消广播接收者的注册
		unregisterReceiver(receiver);
		Intent mintent = new Intent(SERVICE_REFRESH);
		mintent.putExtra("SERVICE_REFRESH", currentMusicPosition);
		sendBroadcast(mintent);
	}

	/**
	 * 初始化and刷新
	 */
	private void init() {
		app = (LuanruApplication) getApplication();
		mMusicFragment = app.getmMusicFragment();
		if (getIntent().getIntExtra("Loaclmusic", -1) != -1) {
			musics = mMusicFragment.getMusics();
			currentMusicPosition = getIntent().getIntExtra("Loaclmusic", -1);
		} else if (getIntent().getIntExtra("OnLineMusic", -1) != -1) {
			musics = mMusicFragment.getMusics();
			currentMusicPosition = getIntent().getIntExtra("OnLineMusic", -1);
		}
		setContentView(R.layout.activity_play_music);
		title = (TextView) findViewById(R.id.tv_title);
		currnt = (TextView) findViewById(R.id.tv_music_current_position);
		duration = (TextView) findViewById(R.id.tv_music_duration);
		play = (ImageView) findViewById(R.id.iv_play);
		albumpic = (ImageView) findViewById(R.id.iv_albumpic);
		pre = (ImageView) findViewById(R.id.iv_pre);
		next = (ImageView) findViewById(R.id.iv_next);
		sb = (SeekBar) findViewById(R.id.seekBar1);
		refresh();
		// 为按钮配置监听器
		InnerButtonOnClickListener buttonOnClickLitener = new InnerButtonOnClickListener();
		sb.setOnSeekBarChangeListener(new SeekBarChangeListener());
		play.setOnClickListener(buttonOnClickLitener);
		next.setOnClickListener(buttonOnClickLitener);
		pre.setOnClickListener(buttonOnClickLitener);

	}

	/**
	 * 刷新
	 */
	private void refresh() {
		title.setText(musics.get(currentMusicPosition).getName());
		if (MusicMainActivity.onlineMusicFlag) {
			this.mImageDownLoader = new ImageDownLoader(mMusicFragment.getContext());
			mImageDownLoader.downloadImage(musics.get(currentMusicPosition).getAlbumpic().getFileUrl(mMusicFragment.getContext()),
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
				bitmap=ArtworkUtils.getArtwork(PlayMusicActivity.this, musics.get(currentMusicPosition).get_id(), musics.get(currentMusicPosition).getAlbumpicId(),false);
				if(bitmap != null){
					albumpic.setImageBitmap(bitmap);
				}else{
					albumpic.setImageResource(R.drawable.img_album_background);
				}
			}
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
		registerReceiver(receiver, filter);
	}

	/**
	 * 广播接收者
	 */
	private final class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (SERVICE_PLAYER_PAUSED.equals(intent.getAction())) {
				// 播放暂停时
				play.setImageResource(R.drawable.icon_pause_normal);
			} else if (SERVICE_PLAYER_PLAYING.equals(intent.getAction())) {
				// 播放时
				play.setImageResource(R.drawable.play);
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
			case R.id.iv_play:
				// 点击播放按钮时
				System.out.println("iv_play");
				mIntent = new Intent(ACTVITY_PLAY_OR_PAUSE);
				sendBroadcast(mIntent);
				break;
			case R.id.iv_next:
				// 点击播放按钮时
				currentMusicPosition++;
				if (currentMusicPosition >= musics.size()) {
					currentMusicPosition = 0;
				}
				refresh();
				mIntent = new Intent(ACTVITY_NEXT);
				sendBroadcast(mIntent);
				break;
			case R.id.iv_pre:
				// 点击播放按钮时
				currentMusicPosition--;
				if (currentMusicPosition < 0) {
					currentMusicPosition = musics.size() - 1;
				}
				refresh();
				mIntent = new Intent(ACTVITY_PRE);
				sendBroadcast(mIntent);
				break;
			}
		}
	}

	private final class SeekBarChangeListener implements
			OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			sendBroadcast(new Intent().setAction(ACTVITY_UODATE_PROGRESS)
					.putExtra(SEEKBAR, seekBar.getProgress()));
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// stopService(new Intent(this,PlayMusicService.class));
	}
}
