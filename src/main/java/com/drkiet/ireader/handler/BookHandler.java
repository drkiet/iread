package com.drkiet.ireader.handler;

import java.io.Serializable;

import com.drkiet.ireader.util.FileHelper;
import com.drkiettran.text.ReadingTextManager;
import com.drkiettran.text.TextApp;
import com.drkiettran.text.model.Document;
import com.drkiettran.text.model.Page;

public class BookHandler implements Serializable {
	private static final long serialVersionUID = 900551273635014690L;
	private Document document;

	public void openSelectedBook(String bookName) {
		TextApp textApp = new TextApp();
		document = textApp.getPages(FileHelper.getFQContentFileName(bookName));
		document.setBookFileName(bookName);
	}

	public ReadingTextManager getReadTextManager() {
		return document.getCurrentPage().getRtm();
	}

	public int getCurrentPageNumber() {
		return document.getCurrentPageNumber();
	}

	public int getPageCount() {
		return document.getPageCount();
	}

	public Page getCurrentPage() {
		return document.getCurrentPage();
	}

	public Page previousPage() {
		return document.previousPage();
	}

	public Page nextPage() {
		return document.nextPage();
	}

	public void setPageNo(int pageNo) {
		document.setPageNo(pageNo);
	}

	public String getBookName() {
		return document.getBookFileName();
	}

}
