package com.haoge.luanru.utils;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class FastJsonUtils {
	public FastJsonUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String createJsonString(Object object) {
		String jsonString = JSON.toJSONString(object);
		return jsonString;
	}

	public static <T> T createJsonBean(String jsonString, Class<T> cls) {
		T t = JSON.parseObject(jsonString, cls);
		return t;
	}

	public static <T> List<T> createJsonToListBean(String jsonString,
			Class<T> cls) {
		List<T> list = null;
		list = JSON.parseArray(jsonString, cls);
		return list;
	}

	/**
	 * @param jsonString
	 * @return
	 */
	public static List<Map<String, Object>> createJsonToListMap(
			String jsonString) {
		List<Map<String, Object>> list2 = JSON.parseObject(jsonString,
				new TypeReference<List<Map<String, Object>>>() {
				});
		return list2;
	}
}
