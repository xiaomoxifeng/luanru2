package com.haoge.luanru.chat.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.haoge.luanru.R;
import com.haoge.luanru.baseActivity.SlidingFragment;
import com.haoge.luanru.chat.adpter.ChatMessageAdapter;
import com.haoge.luanru.chat.adpter.ChatMessageAdapter2;
import com.haoge.luanru.chat.entity.ChatMessage;
import com.haoge.luanru.chat.entity.ChatMessage.Type;
import com.haoge.luanru.chat.utils.messageUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChatMainActivity extends SlidingFragment {
	private ListView mMsgs;
	private ChatMessageAdapter mAdapter;
	private List<ChatMessage> mDatas;
	private EditText mInputMsg;
	private Button mSendMsg;
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			//等待接收，子线程完成数据 的返回
			ChatMessage fromMessage = (ChatMessage) msg.obj;
			System.out.println(fromMessage.getMsg()+"fromMessage");
			mDatas.add(fromMessage);
			mAdapter.notifyDataSetChanged();
			mMsgs.setSelection(mDatas.size() - 1);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("乱聊天");
		// 设置是否能够使用ActionBar来滑动
		setSlidingActionBarEnabled(true);
		// 设置是否显示Home图标按钮
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.chat_main);
		initSlidingMenu();
		initView();
		initDatas();
		initListener();
	}

	private void initListener() {
		mSendMsg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String toMsg = mInputMsg.getText().toString();
				if (TextUtils.isEmpty(toMsg)) {
					Toast.makeText(ChatMainActivity.this, "发送消息不能为空！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				ChatMessage toMessage = new ChatMessage();
				toMessage.setDate(new Date());
				toMessage.setMsg(toMsg);
				toMessage.setType(Type.OUTCOMING);
				mDatas.add(toMessage);
				mMsgs.setSelection(mDatas.size() - 1);
				mInputMsg.setText("");
				new Thread(){
					public void run() {
						ChatMessage fromMessage=messageUtils.setMessage(toMsg);
						System.out.println(fromMessage.getMsg()+"ThreadfromMessage");
						Message m = Message.obtain();
						m.obj = fromMessage;
						mHandler.sendMessage(m);
					};
				}.start();
				
			}
		});
	}

	private void initDatas() {
		mDatas = new ArrayList<ChatMessage>();
		mDatas.add(new ChatMessage("您好，小乱为为您服务",Type.INCOMING,new Date()));
		mAdapter = new ChatMessageAdapter(this, mDatas);
		mMsgs.setAdapter(mAdapter);
	}

	private void initView() {
		mMsgs = (ListView) findViewById(R.id.id_lv_chat);
		mInputMsg = (EditText) findViewById(R.id.id_input_msg);
		mSendMsg = (Button) findViewById(R.id.id_send_msg);
	}
}
