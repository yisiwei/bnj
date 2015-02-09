package com.bangninjia.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	/**
	 * 验证是否是手机号
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		// String telRegex = "[1][358]\\d{9}";
		String telRegex = "^((13[0-9])|(15[^4,\\D])|(18[0,3,5-9]))\\d{8}$";
		Pattern p = Pattern.compile(telRegex);
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * 验证密码不能为纯数字或纯字母
	 * 
	 * @param password
	 * @return
	 */
	public static boolean checkPassword(String password) {
		if (Pattern.matches("^[0-9]*$", password)
				|| Pattern.matches("^[a-z]*$", password)
				|| Pattern.matches("^[A-Z]*$", password)) {
			return true;
		}
		return false;
	}

}
