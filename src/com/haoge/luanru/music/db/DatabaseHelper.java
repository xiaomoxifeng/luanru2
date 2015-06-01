package com.haoge.luanru.music.db;




import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper implements
		DatabaseConst {
	private static DatabaseHelper mHelper;
	private static SQLiteDatabase mDb;
	public DatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table " + TABLE_NAME + "(" + FIELD_ID
				+ " integer primary key autoincrement," + FIELD_NAME
				+ " varchar(30) ," + FIELD_SINGER
				+ " varchar(30)  ," + FIELD_AUTHOR
				+ " varchar(30) ," + FIELD_COMPOSER
				+ " varchar(30) ," + FIELD_ALBUM
				+ " varchar(30) ," + FIELD_ALBUM_ID
				+ " integer ," + FIELD_CONTENT_ID
				+ " integer ," + FIELD_ALBUMPIC
				+ " test," + FIELD_MUSICPATH
				+ " test," + FIELD_DURATIONTIME
				+ " varchar(30) )";
		Log.d("", "sql -> " + sql);
		db.execSQL(sql);
	}
	public static DatabaseHelper getHelper(Context context) {
		if(mHelper == null) {
			mHelper = new DatabaseHelper(context);
		}
		return mHelper;
	}
	public static SQLiteDatabase getInstance(Context context) {
		if (mDb == null) {
			mDb = getHelper(context).getWritableDatabase();
		}
		return mDb;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	public void deleteTables(Context context) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DB_NAME, null, null);
	}
}
