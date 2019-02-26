package com.drkiet.ireader.util;

public class FontHelper {
	public static final String FONT_SIZE_FACE = "<font size=\"%d\" face=\"%s\">";

	public static final String[] START_TAGS = { "<h1", "<h2", "<h3", "<p", "<li", "<a" },
			END_TAGS = { "</h1>", "</h2>", "</h3>", "</p>", "</li>", "</a>" };

	public static String updateFont(String text, int fontSize, String font) {
		String startFont = String.format(FONT_SIZE_FACE, fontSize, font);

		for (int idx = 0; idx < START_TAGS.length; idx++) {
			text = text.replaceAll(START_TAGS[idx], startFont + START_TAGS[idx]);
			text = text.replaceAll(END_TAGS[idx], END_TAGS[idx] + "</font>");
		}

		return text;
	}

	public static String insertFont(String text) {
		String startFont = FONT_SIZE_FACE;

		for (int idx = 0; idx < START_TAGS.length; idx++) {
			text = text.replaceAll(START_TAGS[idx], startFont + START_TAGS[idx]);
			text = text.replaceAll(END_TAGS[idx], END_TAGS[idx] + "</font>");
		}

		return text;
	}
}
