package com.haoge.luanru.music.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

import com.haoge.luanru.chat.utils.messageUtils;
import com.haoge.luanru.music.db.DatabaseConst;
import com.haoge.luanru.music.db.DatabaseHelper;
import com.haoge.luanru.music.db.MusicDaoUtils;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.utils.DateUtils;
import com.haoge.luanru.utils.GlobalConsts;

public class MusicDaoImp implements MusicDao, DatabaseConst {
	public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
	public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟
	Uri mAudioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
	private Context context;
	private ContentResolver res;
	private MusicDaoUtils mMusicDaoUtils;

	public MusicDaoImp(Context context) {
		super();
		this.context = context;
		mMusicDaoUtils=new MusicDaoUtils(context);
		res = context.getContentResolver();
	}

	@Override
	public List<Music> findAllMusic() {
		// TODO Auto-generated method stub
		List<Music> list =new ArrayList<Music>();
		if(mMusicDaoUtils.hasData()){
			list=mMusicDaoUtils.getMusicInfo();
		}else{
			 list = contentResolverQuery();
			 mMusicDaoUtils.saveMusicInfo(list);
			 list=mMusicDaoUtils.getMusicInfo();
		}
		
		return list;
	}

	private List<Music> contentResolverQuery() {
		StringBuilder mSelection = new StringBuilder();
		mSelection.append( AudioColumns.SIZE + " > " + FILTER_SIZE);
		mSelection.append(" and " + AudioColumns.DURATION + " > " + FILTER_DURATION);
		List<Music> list = new ArrayList<Music>();
		Cursor cursor = res.query(mAudioUri,
				null, mSelection.toString(), null, null);
		while (cursor.moveToNext()) {
			Music m = new Music(
					cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)),
					cursor.getString(cursor.getColumnIndex(AudioColumns.TITLE)),
					cursor.getString(cursor.getColumnIndex(AudioColumns.ARTIST)),
					cursor.getString(cursor
							.getColumnIndex(AudioColumns.COMPOSER)),
					cursor.getString(cursor
							.getColumnIndex(AudioColumns.COMPOSER)),
					cursor.getString(cursor.getColumnIndex(AudioColumns.ALBUM)),
					cursor.getInt(cursor.getColumnIndex(AudioColumns.ALBUM_ID)),
					cursor.getString(cursor.getColumnIndex(AudioColumns.ALBUM_KEY)),
					cursor.getString(cursor.getColumnIndex(AudioColumns.DATA)),
					DateUtils.getTimeString(cursor.getInt(cursor
							.getColumnIndex(AudioColumns.DURATION))));
			list.add(m);
		}
		return list;
	}

	@Override
	public int deleteMusic(Music music) {
		// TODO Auto-generated method stub
		int result = mMusicDaoUtils.deleteMusic(music);
		return result;
	}

	@Override
	public long addMusic(Music music) {
		//contentResolverInsert(music);
		return	mMusicDaoUtils.addMusic(music);
	}
//如何正确的向系统中插入一条音频
	private void contentResolverInsert(Music music) {
		ContentValues values = new ContentValues();
	
		//values.put(AudioColumns.DURATION, music.getDurationtime());
		values.put(AudioColumns.TITLE, music.getName());
		values.put(AudioColumns.ARTIST, music.getSinger());
		values.put(AudioColumns.COMPOSER, music.getComposer());
		values.put(AudioColumns.DATA, GlobalConsts.EXTERNALSTORAGE + "/音乐/"
				+ music.getMusicpath().getFilename());
		values.put(AudioColumns.ALBUM_KEY,GlobalConsts.EXTERNALSTORAGE + "/图片/"
				+ music.getAlbumpic().getFilename());
		res.insert(mAudioUri, values);
		//res.notifyChange(uri, observer)
	}


}
