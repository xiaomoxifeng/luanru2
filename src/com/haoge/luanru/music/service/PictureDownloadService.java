package com.haoge.luanru.music.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.fragment.OnlineMusicFragment;
import com.haoge.luanru.music.util.FileUtils;
import com.haoge.luanru.music.util.MusicBroadcastActions;
import com.haoge.luanru.utils.GlobalConsts;
import com.haoge.luanru.utils.HttpUtil;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class PictureDownloadService extends IntentService implements
		MusicBroadcastActions {

	public PictureDownloadService() {
		super("wh");
	}

	public PictureDownloadService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		// 获取Parcelable对象
		OnlineMusicFragment.MUSICFALG = false;
		String httpPath2=intent.getStringExtra("path");
		String fileName = "图片/"+intent.getStringExtra("fileName");
		try {
			HttpResponse resp2 = HttpUtil.send(HttpUtil.METHOD_GET, httpPath2,
					null);
			HttpEntity entity2 = resp2.getEntity();
			// 获取输入流
			InputStream is = entity2.getContent();
			// 定义文件输出流
			File file2 = new File(GlobalConsts.EXTERNALSTORAGE,
					fileName);
			Bitmap bt = BitmapFactory.decodeStream(is);
			FileUtils.save(file2, bt);
			//FileUtils.s
			String picPath = Environment
					.getExternalStoragePublicDirectory("乱入的世界")
					+fileName;
			sendBroadcast(new Intent().setAction(SERVICE_DOWN_PIC));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
