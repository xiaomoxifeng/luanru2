package com.haoge.luanru.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	public static String getTimeString(long timemillis) {
		return new SimpleDateFormat("mm:ss", Locale.CHINA).format(new Date(
				timemillis));
	}

	public static String getDateString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
}
