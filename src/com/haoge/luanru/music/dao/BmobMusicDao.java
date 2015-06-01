package com.haoge.luanru.music.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.fragment.MusicFragment;
import com.haoge.luanru.music.fragment.OnlineMusicFragment;
import com.haoge.luanru.music.util.MusicBroadcastActions;

public class BmobMusicDao  implements MusicDao,MusicBroadcastActions {

private List<Music> lv;
private MusicFragment mFragment;
private BmobQuery<Music> query ;
	public BmobMusicDao(MusicFragment mFragment) {
	super();
	this.mFragment = mFragment;
	query= new BmobQuery<Music>();
}

	@Override
	public List<Music> findAllMusic() {
		// TODO Auto-generated method stub
		query.findObjects(mFragment.getContext(),new FindListener<Music>() {
			@Override
			public void onSuccess(List<Music> arg0) {
				mFragment.updateListView(arg0);
			}						
			@Override
			public void onError(int arg0, String arg1) {
				System.out.println("BmobMusicDaoerror");
				// TODO Auto-generated method stub
			}
		});
		if(lv==null){
			return new ArrayList<Music>();
		}
		return lv;
	}

	@Override
	public int deleteMusic(Music music) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long addMusic(Music music) {
		// TODO Auto-generated method stub
		return 0;
	}

}
