package com.haoge.luanru.music.db;

public interface DatabaseConst {
	/**
	 * 数据库名称
	 */
	String DB_NAME = "musics.db";
	/**
	 * 数据库版本号
	 */
	int DB_VERSION = 1;
	/**
	 * 表名
	 */
	String TABLE_NAME = "musics";
	/**
	 * 字段：ID
	 */
	String FIELD_ID = "_id";
	String FIELD_NAME = "name";
	String FIELD_SINGER = "singer";
	String FIELD_AUTHOR = "author";
	String FIELD_COMPOSER = "composer";
	String FIELD_ALBUM = "album";
	String FIELD_ALBUMPIC = "albumpic";
	String FIELD_MUSICPATH = "musicpath";
	String FIELD_DURATIONTIME = "durationtime";
	String FIELD_ALBUM_ID ="album_id";
	String FIELD_CONTENT_ID="content_id";

	/**
	 * 查询依据：根据ID查询
	 */
	int SELECT_BY_ID = 1;
	/**
	 * 查询依据：根据EN字段查询
	 */
	int SELECT_BY_ENGLISH = 2;
}
