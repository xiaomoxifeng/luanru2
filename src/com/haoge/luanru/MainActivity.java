package com.haoge.luanru;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.haoge.luanru.chat.activity.ChatMainActivity;
import com.haoge.luanru.music.activity.MusicMainActivity;
import com.haoge.luanru.music.util.FileUtils;
import com.haoge.luanru.music.view.CircleMenuLayout;
import com.haoge.luanru.music.view.CircleMenuLayout.OnMenuItemClickListener;

public class MainActivity extends Activity {
	private FileUtils fileUtils;
	private Intent mIntent;
	private CircleMenuLayout mCircleMenuLayout;

	private String[] mItemTexts = new String[] { "乱音乐", "乱视频", "乱事儿",
			"乱聊儿", "乱画儿", "乱设置" };
	private int[] mItemImgs = new int[] { R.drawable.home1,
			R.drawable.home2, R.drawable.home3,
			R.drawable.home4, R.drawable.home5,
			R.drawable.home6 };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置标题栏的标题
		setTitle("乱入2222222的世界呀！");
		fileUtils = new FileUtils(this);
		// 设置是否能够使用ActionBar来滑动

		// 设置是否显示Home图标按钮
		setContentView(R.layout.activity_main);
		mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
		mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);

		mCircleMenuLayout.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			
			@Override
			public void itemClick(View view, int pos)
			{
				Toast.makeText(MainActivity.this, mItemTexts[pos],
						Toast.LENGTH_SHORT).show();
				if(pos==0){
					mIntent = new Intent(MainActivity.this,
							MusicMainActivity.class);
					startActivity(mIntent);
				}else if(pos==3){
					mIntent = new Intent(MainActivity.this,
							ChatMainActivity.class);
					startActivity(mIntent);
				}
					
			}
			
			@Override
			public void itemCenterClick(View view)
			{
				Toast.makeText(MainActivity.this,
						"后期做",
						Toast.LENGTH_SHORT).show();
				
			}
		});
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add("删除手机中图片缓存");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			fileUtils.deleteFile();
			Toast.makeText(getApplication(), "清空缓存成功", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
