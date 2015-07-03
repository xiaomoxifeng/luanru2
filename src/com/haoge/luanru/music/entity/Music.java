package com.haoge.luanru.music.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;


public class Music extends BmobObject implements Serializable {
	
	private int _id;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}

	//歌曲名称
	private String name;
	//歌手
	private String singer;
	//作词
	private String author;
	public String getL_albumpic() {
		return L_albumpic;
	}
	public void setL_albumpic(String l_albumpic) {
		L_albumpic = l_albumpic;
	}
	public String getL_musicpath() {
		return L_musicpath;
	}
	public void setL_musicpath(String l_musicpath) {
		L_musicpath = l_musicpath;
	}

	//作曲
	private String composer;
	//专辑
	private String album;
	//时长
	private String durationtime;
	//线上的的图片
	private BmobFile albumpic;
	//线上的音乐
	private BmobFile musicpath;
	//本地数据库用的
	private String L_albumpic;
	private String L_musicpath;
	//本地contentResovele
	private int albumpicId;
	
	public int getAlbumpicId() {
		return albumpicId;
	}
	public void setAlbumpicId(int albumpicId) {
		this.albumpicId = albumpicId;
	}
	public Music() {
		// TODO Auto-generated constructor stub
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getComposer() {
		return composer;
	}
	public void setComposer(String composer) {
		this.composer = composer;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public BmobFile getAlbumpic() {
		return albumpic;
	}
	public void setAlbumpic(BmobFile albumpic) {
		this.albumpic = albumpic;
	}
	public BmobFile getMusicpath() {
		return musicpath;
	}
	public void setMusicpath(BmobFile musicpath) {
		this.musicpath = musicpath;
	}
	public String getDurationtime() {
		return durationtime;
	}
	public void setDurationtime(String durationtime) {
		this.durationtime = durationtime;
	}
	public Music(int _Id, String name, String singer, String author,
			String composer, String album, int albumpicId, String L_albumpic, String L_musicpath,
			String durationtime) {
		super();
		this._id=_Id;
		this.name = name;
		this.singer = singer;
		this.author = author;
		this.composer = composer;
		this.album = album;
		this.albumpicId = albumpicId;
		this.L_albumpic = L_albumpic;
		this.L_musicpath = L_musicpath;
		this.durationtime = durationtime;
	}
	public Music( String name, String singer, String author,
			String composer, String album, String L_albumpic, String L_musicpath,
			String durationtime) {
		super();
		this.name = name;
		this.singer = singer;
		this.author = author;
		this.composer = composer;
		this.album = album;
		this.L_albumpic = L_albumpic;
		this.L_musicpath = L_musicpath;
		this.durationtime = durationtime;
	}


	@Override
	public String toString() {
		return this.name;
	}

}
