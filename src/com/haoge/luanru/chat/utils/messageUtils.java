package com.haoge.luanru.chat.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import com.haoge.luanru.chat.entity.ChatMessage;
import com.haoge.luanru.chat.entity.ChatMessage.Type;
import com.haoge.luanru.chat.entity.Result;
import com.haoge.luanru.utils.FastJsonUtils;
import com.haoge.luanru.utils.GlobalConsts;
import com.haoge.luanru.utils.HttpUtil;

public class messageUtils {
	public static ChatMessage  setMessage(String message) {
		ChatMessage chatMessage = new ChatMessage();
		HttpResponse resp;
		try {
			String url =setParams(message);
			resp = HttpUtil.send(HttpUtil.METHOD_GET, url, null);
			HttpEntity entity = resp.getEntity();
			String jsonString=EntityUtils.toString(entity, "utf-8");
			Result result=FastJsonUtils.createJsonBean(jsonString, Result.class);
			chatMessage.setMsg(result.getText());
		} catch (Exception e) {
			chatMessage.setMsg("服务器繁忙，请稍候再试");
		}
		chatMessage.setDate(new Date());
		chatMessage.setType(Type.INCOMING);
		return chatMessage;
	}
	private static String setParams(String msg) {
		String url = "";
		try {
			url = GlobalConsts.ROBOT_URL + "?key=" + GlobalConsts.ROBOT_API_KEY + "&info=" + URLEncoder.encode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}
