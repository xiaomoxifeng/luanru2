package com.haoge.luanru.music.biz;

import java.util.List;

import android.os.AsyncTask;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.fragment.MusicFragment;

public class MusicBiz extends AsyncTask<String, String, List<Music>> {
	private MusicFragment fragment;
	private  List<Music> lvMusic;
	public MusicBiz(MusicFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	protected List<Music> doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
//		
			BmobQuery<Music> query = new BmobQuery<Music>();
			query.findObjects(fragment.getContext(), new FindListener<Music>() {
		      
				@Override
		        public void onSuccess(List<Music> lv) {
		            // TODO Auto-generated method stub
		    
		        	Toast.makeText(fragment.getContext(), "查询成功：共"+lv.size()+"条数据。", Toast.LENGTH_SHORT).show();
		            //toast("查询成功：共"+object.size()+"条数据。");
		        	
		        lvMusic=lv;
		        }
		        @Override
		        public void onError(int code, String msg) {
		            // TODO Auto-generated method stub
		        	Toast.makeText(fragment.getContext(), "查询失败："+msg, Toast.LENGTH_SHORT).show();
		           // toast("查询失败："+msg);
		        }
		       
		        
		});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// onPostExecute(Result
	// result)：当doInBackground()完成后，系统会自动调用onPostExecute()方法，并将doInBackground方法返回的值传给该方法。
	@Override
	protected void onPostExecute(List<Music> result) {
		// TODO Auto-generated method stub
		// 更新UI
		fragment.updateListView(result);
	}

}
