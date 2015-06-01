package com.haoge.luanru;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.haoge.luanru.music.activity.MusicMainActivity;

public class MenuRightFragment extends Fragment {
	//右面的Menu还未实现
	private View mView;
	private ListView mCategories;
	private List<String> mDatas = Arrays.asList("扫描歌曲","列表循环","换背景","睡眠","设置");
	private ListAdapter mAdapter;

	// private SimpleAdapter sAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (mView == null) {
			mView = inflater.inflate(R.layout.left_menu, container, false);
			mCategories = (ListView) mView
					.findViewById(R.id.id_listview_categories);
			mAdapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, mDatas);
			mCategories.setAdapter(mAdapter);
			mCategories.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String name = mDatas.get(position);
					Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT)
							.show();
				
				}

			});
		}

		return mView;

	}

}
