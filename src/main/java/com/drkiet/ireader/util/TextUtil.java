package com.drkiet.ireader.util;

public class TextUtil {

	public static String underline(int text) {
		return new StringBuilder("<u>").append(text).append("</u>").toString();
	}
}
