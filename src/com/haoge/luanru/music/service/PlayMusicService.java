package com.haoge.luanru.music.service;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.haoge.luanru.app.LuanruApplication;
import com.haoge.luanru.music.activity.MusicMainActivity;
import com.haoge.luanru.music.activity.PlayMusicActivity;
import com.haoge.luanru.music.fragment.LocalMusicFragment;
import com.haoge.luanru.music.fragment.MusicFragment;
import com.haoge.luanru.music.fragment.OnlineMusicFragment;
import com.haoge.luanru.music.util.MusicBroadcastActions;

public class PlayMusicService extends Service implements MusicBroadcastActions {
	// 判断player是否在运行
	public static boolean runFlag = false;
	private MusicFragment mMusicFragment;
	private LuanruApplication app;
	private int currentMusicPosition;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		runFlag = true;
		app = (LuanruApplication) getApplication();
	
		// m=app.getmMusic();
		// m=(Music) intent.getSerializableExtra("music");
		init();
		initReceiver();
		new ProgressThread().start();
		// 为什么放在activity的create和
		//sendBroadcast(new Intent(ACTVITY_PLAY_OR_PAUSE));
	}

	/**
	 * 音乐播放器
	 */
	private MediaPlayer player;
	/**
	 * 歌曲暂停的位置
	 */
	private int pausePosition;
	/**
	 * 广播接收者
	 */
	private InnerReceiver receiver;

	/**
	 * 初始化广播接收者
	 */
	private void initReceiver() {
		receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTVITY_PLAY_OR_PAUSE);
		filter.addAction(ACTVITY_UODATE_PROGRESS);
		filter.addAction(ACTVITY_ITEM);
		filter.addAction(ACTVITY_PRE);
		filter.addAction(ACTVITY_NEXT);
		registerReceiver(receiver, filter);
	}

	/**
	 * 初始化
	 */
	private void init() {
		// getMusicList();
		// 初始化播放器
		player = new MediaPlayer();
		// 为播放器配置OnPreparedListener
		player.setOnPreparedListener(new PlayerOnPreparedListener());
		player.setOnErrorListener(new PlayerOnErrorListener());
		player.setOnCompletionListener(new PlayerOnCompletionListener());
	}

	/**
	 * 广播接收者
	 */
	private final class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			mMusicFragment=app.getmMusicFragment();
			if(mMusicFragment instanceof LocalMusicFragment){
				System.out.println("mMusicFragment instanceof LocalMusicFragment____________");
			}else if(mMusicFragment instanceof OnlineMusicFragment){
				System.out.println("mMusicFragment instanceof OnlineMusicFragment_______________");
			}
			if (ACTVITY_PLAY_OR_PAUSE.equals(intent.getAction())) {
				// 界面上点击了播放按钮
				// 但是，不确定是播放还是暂停
				System.out.println(currentMusicPosition+"--------ACTVITY_PLAY_OR_PAUSE");
				
				if (player.isPlaying()) {
					pause();
					Intent i1 = new Intent();
					i1.setAction(SERVICE_PLAYER_PAUSED);
					sendBroadcast(i1);
				} else {
					play();
					Intent i2 = new Intent();
					i2.setAction(SERVICE_PLAYER_PLAYING);
					sendBroadcast(i2);
				}
			} else if (ACTVITY_UODATE_PROGRESS.equals(intent.getAction())) {
				// 进度条最大值
				int max = 100;
				// 拖动后的进度百分比（因为该进度条设置为max=100）
				int progress = intent.getIntExtra(SEEKBAR, 0);
				// 歌曲的时长
				int duration = player.getDuration();
				// 应该播放的位置，即跳到?分?秒再播放
				int playPosition = duration * progress / max;
				// 播放器播放指定位置
				player.seekTo(playPosition);
			} else if (ACTVITY_ITEM.equals(intent.getAction())) {
				//currentMusicPosition；
				if(intent.getIntExtra("Loaclmusic", -1)!=-1){
					currentMusicPosition=intent.getIntExtra("Loaclmusic", -1);
					System.out.println(currentMusicPosition+"--------ACTVITY_PLAY_OR_PAUSE");
				}else if(intent.getIntExtra("OnLineMusic", -1)!=-1){
					currentMusicPosition=intent.getIntExtra("OnLineMusic", -1);
				}
				play();
			}else if(ACTVITY_NEXT.equals(intent.getAction())){
				currentMusicPosition++;
				if (currentMusicPosition >= mMusicFragment.getMusics().size()) {
					currentMusicPosition = 0;
				}
				play();
			}else if(ACTVITY_PRE.equals(intent.getAction())){
				currentMusicPosition--;
				if (currentMusicPosition <0) {
					currentMusicPosition = mMusicFragment.getMusics().size() - 1;
				}
				play();
			}
		}

		/**
		 * 播放器：播放
		 */
		public void play() {
			try {
				player.reset();
				if(MusicMainActivity.onlineMusicFlag){
					player.setDataSource(mMusicFragment.getMusics()
							.get(currentMusicPosition).getMusicpath().getFileUrl(mMusicFragment.getContext()));
				}else{
					player.setDataSource(mMusicFragment.getMusics()
							.get(currentMusicPosition)
							.getL_musicpath());
				}
				player.prepareAsync();
				// 因为已经使用OnPreparedListener，则无需在此调用start()
				// player.start();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 播放器：暂停
	 */
	private void pause() {
		if (player.isPlaying()) {
			pausePosition = player.getCurrentPosition();
			player.pause();
		}
	}

	private final class PlayerOnPreparedListener implements
			MediaPlayer.OnPreparedListener {

		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mp.seekTo(pausePosition);
			pausePosition = 0;
			mp.start();
		}

	}

	private final class PlayerOnCompletionListener implements
			MediaPlayer.OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub

			currentMusicPosition++;
			if (currentMusicPosition >= mMusicFragment
					.getMusics().size()) {
				currentMusicPosition = 0;
			}
			sendBroadcast(new Intent().setAction(SERVICE_PLAYER_COMPLETION));
			sendBroadcast(new Intent(ACTVITY_ITEM));
		}
	}

	private final class PlayerOnErrorListener implements
			MediaPlayer.OnErrorListener {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
		// 取消广播接收者的注册
		unregisterReceiver(receiver);
		runFlag = false;
		super.onDestroy();
	}

	private final class ProgressThread extends Thread {
		@Override
		public void run() {
			while (true) {
				if (player != null && player.isPlaying()) {
					Intent intent = new Intent();
					intent.setAction(SERVICE_UPDATE_PROGRESS);
					intent.putExtra(EXTRA_MUSIC_CURRENT_POSITION,
							player.getCurrentPosition());
					intent.putExtra(EXTRA_MUSIC_DURATION, player.getDuration());
					sendBroadcast(intent);
				}
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
