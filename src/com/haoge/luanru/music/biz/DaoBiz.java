package com.haoge.luanru.music.biz;

import java.util.List;

import com.haoge.luanru.music.dao.MusicDao;
import com.haoge.luanru.music.dao.MusicDaoImp;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.fragment.LocalMusicFragment;
import android.os.AsyncTask;

public class DaoBiz extends AsyncTask<String, String, List<Music>> {
	private LocalMusicFragment fragment;
	private MusicDao dao;

	public DaoBiz(LocalMusicFragment fragment) {
		this.fragment = fragment;
		dao = new MusicDaoImp(fragment.getActivity());
	}

	@Override
	protected List<Music> doInBackground(String... params) {
		//查询本地所有数据
		return  dao.findAllMusic();
	}
	@Override
	protected void onPostExecute(List<Music> result) {
		// TODO Auto-generated method stub
		fragment.updateListView(result);
	}

}
