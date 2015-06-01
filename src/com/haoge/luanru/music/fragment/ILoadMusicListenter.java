package com.haoge.luanru.music.fragment;

import java.util.List;

import com.haoge.luanru.music.entity.Music;

public interface ILoadMusicListenter {
	public void onLoadMusic(int position);
	public void onLoadMusics(List<Music> musics);
}
