package com.haoge.luanru.music.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.app.IntentService;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.haoge.luanru.R;
import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.fragment.OnlineMusicFragment;
import com.haoge.luanru.music.util.MusicBroadcastActions;
import com.haoge.luanru.utils.GlobalConsts;
import com.haoge.luanru.utils.HttpUtil;

public class MusicDownloadService extends IntentService implements
		MusicBroadcastActions {
	public MusicDownloadService() {
		super("wh");
	}

	public MusicDownloadService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	// 工作线程中执行
	// 下载文件操作 使用通知提示用户
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		// 获取Parcelable对象
		OnlineMusicFragment.PICFALG = false;
		//Music music = (Music) intent.getParcelableExtra("m");
		String httpPath=intent.getStringExtra("path");
		String fileName = "音乐/"+intent.getStringExtra("fileName");
		// 音乐
	
		try {
			HttpResponse resp = HttpUtil.send(HttpUtil.METHOD_GET, httpPath,
					null);
			HttpEntity entity = resp.getEntity();
			// 文件总大小
			long total = entity.getContentLength();
			// 获取输入流
			InputStream is = entity.getContent();
			byte[] buffer = new byte[300 * 1024];
			int length = 0;// 从流中读取数据的长度
			long current = 0;
			// 定义文件输出流
			File file = new File(GlobalConsts.EXTERNALSTORAGE,
					fileName);
			if (file.exists()) {
				Toast.makeText(this, "文件已经存在", Toast.LENGTH_SHORT).show();
				return;
			}
			// 判断file父目录是否存在
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			clear();
			sendNotification("音乐下载", "开始下载。。。", "音乐开始下载..");
			while ((length = is.read(buffer)) != -1) {
				current += length;
				// 计算当前的下载进度
				// current=1000 total 4534534
				String text = Math.floor((double) current / total * 100) + "%";
				// 把数据存入sd卡
				fos.write(buffer, 0, length);
				// 发通知
				sendNotification("音乐下载", "当前下载进度 : " + text, "");
				String musicPath = Environment
						.getExternalStoragePublicDirectory("乱入的世界")
						+ fileName;
			}
			fos.flush();
			fos.close();
			clear();
			sendNotification("音乐下载", "下载完成", "下载完成");
			sendBroadcast(new Intent().setAction(SERVICE_DOWN_MUSIC));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void clear() {
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(1);
	}

	private void sendNotification(String title, String text, String ticker) {
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Builder builder = new Builder(this);
		builder.setContentText(text)
				.setContentTitle(title)
				.setLargeIcon(
						BitmapFactory.decodeResource(getResources(),
								R.drawable.ic_launcher))
				.setSmallIcon(R.drawable.ic_launcher).setSubText("")
				.setTicker(ticker).setWhen(System.currentTimeMillis());
		Notification n = builder.build();
		// 3.发送
		manager.notify(1, n);
	}
}
