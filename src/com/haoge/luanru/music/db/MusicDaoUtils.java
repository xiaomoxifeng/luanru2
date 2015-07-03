package com.haoge.luanru.music.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.utils.DateUtils;
import com.haoge.luanru.utils.GlobalConsts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AudioColumns;
import android.util.Log;

public class MusicDaoUtils implements DatabaseConst{
	private Context mContext;
	
	public MusicDaoUtils(Context context) {
		this.mContext = context;
		// 实例化helper
	}
	
	public void saveMusicInfo(List<Music> list) {
		SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
		for (Music info : list) {
			info.getL_albumpic();
			ContentValues values = new ContentValues();
			values.put(FIELD_ALBUM, info.getAlbum());
			values.put(FIELD_AUTHOR, info.getAuthor());
			values.put(FIELD_COMPOSER, info.getComposer());
			values.put(FIELD_ALBUM_ID, info.getAlbumpicId());
			values.put(FIELD_CONTENT_ID, info.get_id());
			values.put(FIELD_DURATIONTIME, info.getDurationtime());
			values.put(FIELD_MUSICPATH, info.getL_musicpath());
			values.put(FIELD_ALBUMPIC, info.getL_albumpic());
			values.put(FIELD_NAME, info.getName());
			values.put(FIELD_SINGER, info.getSinger());
			db.insert(TABLE_NAME, null, values);
		}
	}
	
	public List<Music> getMusicInfo() {
		SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
		List<Music> list = new ArrayList<Music>();
		String sql = "select * from " + TABLE_NAME;
		Cursor cursor = db.rawQuery(sql, null);
		int colums = cursor.getColumnCount();
		while(cursor.moveToNext()) {
			Map<String, Object > map = new HashMap<String, Object>();
			for (int i = 0; i < colums; i++) {
				String cols_name = cursor.getColumnName(i);
				String cols_value = cursor.getString(cursor
						.getColumnIndex(cols_name));
				if (cols_value == null) {
					cols_value = "";
				}
				map.put(cols_name, cols_value);
			}
			Music m = new Music(
					Integer.parseInt(map.get(FIELD_CONTENT_ID).toString()==""?"0":map.get(FIELD_CONTENT_ID).toString()),
					map.get(FIELD_NAME).toString(),
					map.get(FIELD_SINGER).toString(),
					map.get(FIELD_AUTHOR).toString(),
					map.get(FIELD_COMPOSER).toString(),
					map.get(FIELD_ALBUM).toString(),
					Integer.parseInt(map.get(FIELD_ALBUM_ID).toString()==""?"0":map.get(FIELD_ALBUM_ID).toString()),
					map.get(FIELD_ALBUMPIC).toString(),
					map.get(FIELD_MUSICPATH).toString(),
					map.get(FIELD_DURATIONTIME).toString());
			list.add(m);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 数据库中是否有数据
	 * @return
	 */
	public boolean hasData() {
		SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
		String sql = "select count(*) from " + TABLE_NAME;
		Cursor cursor = db.rawQuery(sql, null);
		boolean has = false;
		if(cursor.moveToFirst()) {
			int count = cursor.getInt(0);
			if(count > 0) {
				has = true;
			}
		}
		cursor.close();
		return has;
	}
	
	public int getDataCount() {
		SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
		String sql = "select count(*) from " + TABLE_NAME;
		Cursor cursor = db.rawQuery(sql, null);
		int count = 0;
		if(cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}
		return count;
	}
	public long addMusic(Music music) {
		// 执行数据库操作的对象
		long result = -1;
		Music m = findOneMusic(music);
		if (m.toString() == null) {
			SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
//			db.beginTransaction();
			// 插入数据时SQL语句需要的参数，即字段列表和值列表
			ContentValues values = new ContentValues();
			values.put(FIELD_ALBUM, music.getAlbum());
			values.put(FIELD_ALBUMPIC, GlobalConsts.EXTERNALSTORAGE + "/图片/"
					+ music.getAlbumpic().getFilename());
			values.put(FIELD_AUTHOR, music.getAuthor());
			values.put(FIELD_COMPOSER, music.getComposer());
			values.put(FIELD_DURATIONTIME, music.getDurationtime());
			values.put(FIELD_MUSICPATH, GlobalConsts.EXTERNALSTORAGE + "/音乐/"
					+ music.getMusicpath().getFilename());
			values.put(FIELD_NAME, music.getName());
			values.put(FIELD_SINGER, music.getSinger());
			// 执行
			result = db.insert(TABLE_NAME, null, values);
//			db.setTransactionSuccessful();
//			db.endTransaction();
			// 关闭
			// 返回结果
			return result;
		}
		return result;
	}
	
	public Music findOneMusic(Music music) {
		Music m = new Music();
		// 执行数据库操作的对象
		SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
		// 查询条件
		String selection;
		// 结果
		String[] selectionArgs = null;
		selection = FIELD_ID + "=?";
		//selectionArgs = new String[] { music.getId() + "" };
		Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs,
				null, null, null);
		int colums = cursor.getColumnCount();
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < colums; i++) {
				String cols_name = cursor.getColumnName(i);
				String cols_value = cursor.getString(cursor
						.getColumnIndex(cols_name));
				if (cols_value == null) {
					cols_value = "";
				}
				map.put(cols_name, cols_value);
			}
			m = new Music(map.get(FIELD_NAME).toString(), 
					map.get(FIELD_SINGER).toString(),map.get(FIELD_AUTHOR).toString(), 
					map.get(FIELD_COMPOSER).toString(),map.get(FIELD_ALBUM).toString(), 
					map.get(FIELD_ALBUMPIC).toString(),
					map.get(FIELD_MUSICPATH).toString(),
					map.get(FIELD_DURATIONTIME).toString());
		}
		// 关闭
		// 返回
		return m;
	}
	public int deleteMusic(Music music) {
		// TODO Auto-generated method stub
		// 执行数据库操作的对象
		SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
		// 结果
		int result = -1;
		// where子句，不需要where关键字
		String whereClause = FIELD_NAME + "=?";
		// where子句中?匹配的值把?鬟^?淼?id??成String
		String[] whereArgs = new String[] { music.getName() + "" };
		// 执行删除
		result = db.delete(TABLE_NAME, whereClause, whereArgs);
		// 关闭
		// 返回

		return result;
	}
}
