package com.haoge.luanru.app;

import com.haoge.luanru.music.fragment.MusicFragment;
import com.haoge.luanru.music.util.ImageDownLoader;

import android.app.Application;

public class LuanruApplication extends Application {

private MusicFragment mMusicFragment;

public MusicFragment getmMusicFragment() {
	return mMusicFragment;
}

public void setmMusicFragment(MusicFragment mMusicFragment) {
	mMusicFragment.getMusics();
	this.mMusicFragment = mMusicFragment;
}
private ImageDownLoader musicImageDownLoader;

public ImageDownLoader getMusicImageDownLoader() {
	return musicImageDownLoader;
}

public void setMusicImageDownLoader(ImageDownLoader musicImageDownLoader) {
	this.musicImageDownLoader = musicImageDownLoader;
}

}
