package com.drkiet.ireader.main.handler;

import com.drkiet.ireader.util.FileHelper;
import com.drkiettran.text.TextApp;
import com.drkiettran.text.model.Document;

public class BookHandler {
	private Document document;

	public void openSelectedBook(String bookName) {
		TextApp textApp = new TextApp();
		document = textApp.getPages(FileHelper.getFQContentFileName(bookName));
		document.setBookFileName(bookName);
	}
}
