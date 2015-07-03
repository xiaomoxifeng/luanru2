package com.haoge.luanru.music.service;

import java.util.List;

import com.haoge.luanru.music.dao.BmobMusicDao;
import com.haoge.luanru.music.entity.Music;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class LoadMusicService extends IntentService{
private Context mContext;
	public LoadMusicService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public LoadMusicService(Context mContext) {
		super("");
		System.out.println("LoadMusicService");
		this.mContext=mContext;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
//		// TODO Auto-generated method stub
//		//BmobMusicDao mBmobMusicDao = new BmobMusicDao(mContext); 
//		System.out.println("onHandleIntent");
//		List<Music> lv=mBmobMusicDao.findAllMusic();
//		System.out.println(lv.size()+"LoadMusicService");
	}

}
