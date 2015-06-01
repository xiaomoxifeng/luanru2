package com.haoge.luanru.music.fragment;

import java.util.ArrayList;
import java.util.List;

import com.haoge.luanru.R;
import com.haoge.luanru.app.LuanruApplication;
import com.haoge.luanru.music.activity.MusicMainActivity;
import com.haoge.luanru.music.activity.PlayMusicActivity;
import com.haoge.luanru.music.adapter.OnlineMusicAdapter;
import com.haoge.luanru.music.adapter.LocalMusicAdapter;
import com.haoge.luanru.music.biz.QueryLocalBiz;
import com.haoge.luanru.music.dao.MusicDao;
import com.haoge.luanru.music.dao.MusicDaoImp;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.service.DeleteFileService;
import com.haoge.luanru.music.util.MusicBroadcastActions;
import com.haoge.luanru.music.view.LoadListView;
import com.haoge.luanru.music.view.LoadListView.ILoadListener;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class LocalMusicFragment extends Fragment implements MusicBroadcastActions,MusicFragment,ILoadListener {
	private LoadListView lvMusics;
	private  List<Music> musics,loadMusic;
	private LocalMusicAdapter adapter;
	private MusicDao musicDao;
	private boolean isFirst = true;
	private Music m;
	private LuanruApplication app;
	private Intent mIntent;
	private static int currentMusicPosition;
	private int lastPosition;

	private ILoadMusicListenter mLoadMusicListener;
	//为了可以new出连着写
	public LocalMusicFragment LocalMusicFragment() {
		return LocalMusicFragment.this;
	}
	public  List<Music> getMusics() {
		return musics;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_music_local, null);
		lvMusics = (LoadListView) v.findViewById(R.id.lv_music_local);
		lvMusics.setInterface(this);
		musicDao = new MusicDaoImp(getActivity());
		loadMusic= new ArrayList<Music>();
		DaoBiz();
		initReceiver();
		app=(LuanruApplication) getActivity().getApplication();
		lvMusics.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mLoadMusicListener.onLoadMusic(position);
				mIntent = new Intent(ACTVITY_ITEM);
				mIntent.putExtra("Loaclmusic", position);
				getActivity().sendBroadcast(mIntent);
				getActivity().sendBroadcast(new Intent().setAction(SERVICE_REFRESH));
			}
		
		});
		lvMusics.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				m = musics.get(position);
				AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
				b.setItems(new String[] { "播放", "删除" }, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// 执行下载操作 启动service
						if (which == 0) {
							Intent i = new Intent(getActivity(),
									PlayMusicActivity.class);
							Bundle mBundle = new Bundle();
							//mBundle.putSerializable("Loaclmusic", m);
							i.putExtra("Loaclmusic", position);
							i.putExtras(mBundle);
							startActivity(i);
						} else if (which == 1) {
							Intent i = new Intent(getActivity(),
									DeleteFileService.class);
							Bundle mBundle = new Bundle();
							mBundle.putSerializable("m", m);
							i.putExtras(mBundle);
							getActivity().startService(i);
						}
					}
				});
				b.create().show();
				return false;
			}

		});
		return v;
	}

	/**
	 * 注册广播接收者
	 */
	private void initReceiver() {
		InnerReceiver receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(REFRESH_LOCAL_MUSICS);
		filter.addAction(SERVICE_DELETE_FILE);
		filter.addAction(ACTVITY_SET_LOCAL_APP);
		getActivity().registerReceiver(receiver, filter);
	}

	/**
	 * 广播接收者
	 */
	private final class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (REFRESH_LOCAL_MUSICS.equals(intent.getAction())) {
				// 异步刷新
				DaoBiz();
			} else if (SERVICE_DELETE_FILE.equals(intent.getAction())) {
				int i = musicDao.deleteMusic(m);
				if (i != -1) {
					Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT)
							.show();
					// 异步刷新
					DaoBiz();
				}
			}else if(ACTVITY_SET_LOCAL_APP.equals(intent.getAction())){
				System.out.println("ACTVITY_SET_LOCAL_APP");
				setApp();
			}
		}
	}

	/**
	 * 异步刷新
	 */
	private void DaoBiz() {
		new QueryLocalBiz(this).execute();
	}

	// 更新ListView 设置Adapter
	public void updateListView(List<Music> result) {
		this.musics = result;
		setApp();
		loadMusic.clear();
		for (lastPosition = 0; lastPosition < (result.size()<10?result.size():10); lastPosition++) {
			this.loadMusic.add(musics.get(lastPosition));
		}
		showListView(loadMusic);
	}

	private void setApp() {
		mLoadMusicListener.onLoadMusics(musics);
		app.setmMusicFragment(LocalMusicFragment.this);
	}
	public Fragment setMusicInterface(ILoadMusicListenter iLoadMusicListener){
		this.mLoadMusicListener = iLoadMusicListener;
		return this;
	}
	

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return getActivity();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
	// 用于显示
	private void showListView(List<Music> loadMusic) {
		if (adapter == null) {
			adapter = new LocalMusicAdapter(getActivity(), this.loadMusic, lvMusics);
			lvMusics.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}
	// 获取更多数据
	public void getMoreMusics() {
		int i = 0;
		for (i = lastPosition; i < (lastPosition = lastPosition + 2 > musics
				.size() ? musics.size() : lastPosition + 2); i++) {
			System.out.println(lastPosition
					+ "lastPositionlastPositionlastPosition");
			this.loadMusic.add(musics.get(i));
		}
		lastPosition = i;
	}
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getMoreMusics();
				showListView(loadMusic);
				lvMusics.loadComplete();
			}
		}, 2000);
	}
	
}
