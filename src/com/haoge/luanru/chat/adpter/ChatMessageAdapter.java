package com.haoge.luanru.chat.adpter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoge.luanru.R;
import com.haoge.luanru.baseAdapter.BaseListAdapter2;
import com.haoge.luanru.baseAdapter.ViewHolder;
import com.haoge.luanru.chat.entity.ChatMessage;
import com.haoge.luanru.chat.entity.ChatMessage.Type;
import com.haoge.luanru.utils.DateUtils;

public class ChatMessageAdapter extends BaseListAdapter2 {
	private List<ChatMessage> mDatas;
	private LayoutInflater mInflater;
	TextView mMsg;
	TextView mDate;

	public ChatMessageAdapter(Context context, List<ChatMessage> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
		this.mDatas = list;
		this.mInflater = LayoutInflater.from(context);
	}

//	@Override
//	public View bindView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		ChatMessage chatMessage = mDatas.get(position);
//		if (convertView == null) {
//			if (getItemViewType(position) == 0) {
//				convertView = mInflater.inflate(R.layout.item_from_msg, null);
//				// final ImageView ivAlbum = ViewHolder.get(convertView, R.id.);
//				 mDate = ViewHolder.get(convertView, R.id.id_from_date);
//				 mMsg = ViewHolder.get(convertView,
//						R.id.id_from_msg_info);
//			}else{
//				convertView = mInflater.inflate(R.layout.item_to_msg, null);
//				// final ImageView ivAlbum = ViewHolder.get(convertView, R.id.);
//				 mDate = ViewHolder.get(convertView, R.id.id_to_date);
//				 mMsg = ViewHolder.get(convertView,
//						R.id.id_to_msg_info);
//			}
//		}
//		mDate.setText(DateUtils.getDateString(chatMessage.getDate()));
//		mMsg.setText(chatMessage.getMsg());
//	
//		return convertView;
//	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMessage chatMessage = mDatas.get(position);
		ViewHolder viewHolder = null;
		if(convertView == null) {
			//通过ItemType设置不同的布局
			if(getItemViewType(position) == 0) {
				convertView = mInflater.inflate(R.layout.item_from_msg, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.mDate =  (TextView) convertView.findViewById(R.id.id_from_date);
				viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_from_msg_info);
			} else {
				convertView = mInflater.inflate(R.layout.item_to_msg, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.mDate =  (TextView) convertView.findViewById(R.id.id_to_date);
				viewHolder.mMsg = (TextView) convertView.findViewById(R.id.id_to_msg_info);
			}
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//设置数据
		viewHolder.mDate.setText(DateUtils.getDateString(chatMessage.getDate()));
		viewHolder.mMsg.setText(chatMessage.getMsg());
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		ChatMessage chatMessage = mDatas.get(position);
		if (chatMessage.getType() == Type.INCOMING) {
			return 0;
		}
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	private final class ViewHolder {
		TextView mDate;
		TextView mMsg;
	}
	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
