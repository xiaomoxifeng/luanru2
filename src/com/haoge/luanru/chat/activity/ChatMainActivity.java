package com.haoge.luanru.chat.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.haoge.luanru.R;
import com.haoge.luanru.baseActivity.SlidingFragment;
import com.haoge.luanru.chat.adpter.ChatMessageAdapter;
import com.haoge.luanru.chat.adpter.ChatMessageAdapter2;
import com.haoge.luanru.chat.entity.ChatMessage;
import com.haoge.luanru.chat.entity.ChatMessage.Type;
import com.haoge.luanru.chat.utils.messageUtils;
import com.haoge.luanru.utils.GlobalConsts;
import com.qq.wx.voice.recognizer.VoiceRecognizer;
import com.qq.wx.voice.recognizer.VoiceRecognizerListener;
import com.qq.wx.voice.recognizer.VoiceRecognizerResult;
import com.qq.wx.voice.recognizer.VoiceRecordState;
import com.qq.wx.voice.recognizer.VoiceRecognizerResult.Word;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ChatMainActivity extends SlidingFragment implements VoiceRecognizerListener{
	private ListView mMsgs;
	private ChatMessageAdapter mAdapter;
	private List<ChatMessage> mDatas;
	private EditText mInputMsg;
	private Button mSendMsg;
	private ImageView mIvVoice;
	private String mRecognizerResult;
	//表示目前所处的状态 0:空闲状态，可进行识别； 1：正在进行录音; 2：者处于语音识别; 3：处于取消状态
	private int mRecoState = 0;
	int mInitSucc = 0;
	//定时器
		private Timer frameTimer;
		private TimerTask frameTask;
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			//等待接收，子线程完成数据 的返回
			ChatMessage fromMessage = (ChatMessage) msg.obj;
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
		mIvVoice.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (0 == mRecoState) {
					preInitVoiceRecognizer();
					if (0 == startRecognizer()) {
					int ret = VoiceRecognizer.shareInstance().cancel();
						//Log.d(TAG, "cancel ret = " + ret);

						mRecoState = 1;
						//mCancelBtn.setBackgroundResource(R.drawable.recogcancel);
						//mCancelBtn.setEnabled(true);
					}
				}
				else if (1 == mRecoState) {
					VoiceRecognizer.shareInstance().stop();
					mIvVoice.setEnabled(false);
				}
			}
		});
	}

	private void initDatas() {
		mDatas = new ArrayList<ChatMessage>();
		mDatas.add(new ChatMessage("您好，小乱为您服务",Type.INCOMING,new Date()));
		mAdapter = new ChatMessageAdapter(this, mDatas);
		mMsgs.setAdapter(mAdapter);
	
	}

	private void initView() {
		mMsgs = (ListView) findViewById(R.id.id_lv_chat);
		mInputMsg = (EditText) findViewById(R.id.id_input_msg);
		mSendMsg = (Button) findViewById(R.id.id_send_msg);
		mIvVoice = (ImageView)findViewById(R.id.ivVoice);
	}
	private int startRecognizer() {
		if (0 == VoiceRecognizer.shareInstance().start()) {
//			mStartRecordTv.setText("语音已开启，请说话…");
			Toast.makeText(this, "语音已开启，请说话…", Toast.LENGTH_LONG).show();
			return 0;
		}
//		mStartRecordTv.setText("启动失败");
		Toast.makeText(this, "启动失败", Toast.LENGTH_LONG).show();
//		mCompleteBtn.setEnabled(true);
		return -1;
		
	}
	private void preInitVoiceRecognizer() {
		//setSilentTime参数单位为微秒：1秒=1000毫秒
		VoiceRecognizer.shareInstance().setSilentTime(1000);
		VoiceRecognizer.shareInstance().setListener(this);
		//VoiceRecognizer.shareInstance().setDomain("113.108.82.81", 8080);
		//VoiceRecognizer.shareInstance().setDomain("newsso.map.qq.com", 8080, "newsso.map.qq.com:8080");
		//VoiceRecognizer.shareInstance().setUri("/voice/fetchinfo");
		//VoiceRecognizer.shareInstance().setGetPureRes(true);
		
		mInitSucc = VoiceRecognizer.shareInstance().init(this, GlobalConsts.WEIXIN_API_KEY);
		if (mInitSucc != 0) {
			Toast.makeText(this, "初始化失败",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onGetError(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetResult(VoiceRecognizerResult result) {
		// TODO Auto-generated method stub
		cancelTask();

		mRecognizerResult = "";

		//if (1 == result.type) {
		//	Log.d(TAG, "result = " + new String(result.httpRes));
		//}

		if (result != null && result.words != null) {
			int wordSize = result.words.size();
			StringBuilder results = new StringBuilder();
			for (int i = 0; i<wordSize; ++i) {
				Word word = (Word) result.words.get(i);
				if (word != null && word.text != null) {
					results.append("\r\n");
					results.append(word.text.replace(" ", ""));
				}
			}
			results.append("\r\n");
			mRecognizerResult = results.toString();
		}
//		mStartRecordTv.setText(mRecognizerResult);
//		setCancelBtn(false);
//		setStartBtn(true);
		mRecoState = 0;
	}

	@Override
	public void onGetVoiceRecordState(VoiceRecordState arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVolumeChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	public void cancelTask() {
		if (null != frameTask) {
			frameTask.cancel();
		}
		if (null != frameTimer) {
			frameTimer.cancel();
		}
	}
}
