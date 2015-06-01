package com.haoge.luanru.music.service;

import java.io.File;

import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.util.MusicBroadcastActions;

import android.app.IntentService;
import android.content.Intent;

public class DeleteFileService extends IntentService implements
		MusicBroadcastActions {
private	boolean flag =false;

	public DeleteFileService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public DeleteFileService() {
		super("");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Music music = (Music) intent.getSerializableExtra("m");
		if(new File(music.getL_albumpic()).exists()){
			 flag = new File(music.getL_albumpic()).delete();
		}else{
			flag=true;
		}
		if (flag) {
			if(new File(music.getL_musicpath()).exists()){
				flag = new File(music.getL_musicpath()).delete();
			}else{
				flag = false;
			}
			
		}
		if (flag) {
			sendBroadcast(new Intent().setAction(SERVICE_DELETE_FILE));
		}
	}

}
