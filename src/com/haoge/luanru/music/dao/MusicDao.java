package com.haoge.luanru.music.dao;

import java.util.List;

import android.database.Cursor;

import com.haoge.luanru.music.entity.Music;

public interface MusicDao {
	public List<Music> findAllMusic();

	public int deleteMusic(Music music);

	public long addMusic(Music music);



}
