package com.bangninjia.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	/**
	 * 格式化日期
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String parseLongDate(Long milliseconds) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		Date date = new Date(milliseconds);

		return format.format(date);

	}

}
