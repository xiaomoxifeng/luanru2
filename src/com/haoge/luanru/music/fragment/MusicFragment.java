package com.haoge.luanru.music.fragment;

import java.util.List;

import com.haoge.luanru.music.entity.Music;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface MusicFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);
	 public void updateListView(List<Music> result);
	 public Context getContext();
	 public List<Music> getMusics(); 
}
