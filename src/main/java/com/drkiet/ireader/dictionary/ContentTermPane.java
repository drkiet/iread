package com.drkiet.ireader.dictionary;

import java.awt.Color;

import javax.swing.JTextPane;

public class ContentTermPane extends JTextPane {
	private static final long serialVersionUID = -2747449872529288250L;

	public ContentTermPane() {
		setCaretPosition(0);
		setCaretColor(Color.WHITE);
		setContentType("text/html");
	}
}
