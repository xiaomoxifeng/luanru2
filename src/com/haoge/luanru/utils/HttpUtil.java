package com.haoge.luanru.utils;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.params.CoreConnectionPNames;

/** 发送http请求的工具类 */
public class HttpUtil {
	public static final int METHOD_GET = 0;
	public static final int METHOD_POST = 1;

	public static HttpResponse send(int method, String uri,
			List<NameValuePair> pairs) throws Exception {
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);// 连接时间
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);// 数据传输时间
		HttpUriRequest request = null;
		switch (method) {
		case METHOD_GET:
			request = new HttpGet(uri);
			break;
		case METHOD_POST:
			HttpPost post = new HttpPost(uri);
			// pairs.add(new BasicNameValuePair
			// ("name", ""));
			post.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			request = post;
			break;
		}
		HttpResponse resp = client.execute(request);
		return resp;

	}
}
