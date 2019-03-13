package com.drkiet.ireader.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.util.FileHelper;
import com.drkiettran.text.ReadingTextManager;
import com.drkiettran.text.TextApp;
import com.drkiettran.text.model.Document;
import com.drkiettran.text.model.Page;

public class BookHandler implements Serializable {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookHandler.class);

	private static final long serialVersionUID = 900551273635014690L;
	private Document document;

	public void openSelectedBook(String fileName) {
		String altFileName = FileHelper.getAltFileName(fileName, ".txt");
		File altFile = new File(altFileName);
		TextApp textApp = new TextApp();

		if (altFile.exists()) {
			fileName = altFileName;
		}

		LOGGER.info("READING from {}", fileName);
		document = textApp.getPages(fileName);
		document.setBookFileName(fileName);

		if (!altFile.exists()) {
			LOGGER.info("*** WRITING to text file {}", altFileName);
			BufferedWriter bw = openFileWriter(altFileName);
			int pageNo = 1;
			Iterator<Page> pages = document.getPages();

			while (pages.hasNext()) {
				StringBuilder sb = new StringBuilder("~~~ Page ").append(pageNo++).append('\n');
				sb.append(pages.next().getRtm().getText()).append('\n');
				sb.trimToSize();
				writerToFile(bw, sb.toString());
			}
			closeFileWriter(bw);
		}
	}

	private void closeFileWriter(BufferedWriter bw) {
		try {
			bw.close();
		} catch (IOException e) {
			LOGGER.error("*** ERROR {}", e);
		}
	}

	private void writerToFile(BufferedWriter bw, String pageText) {
		try {
			bw.write(pageText);
			bw.flush();
		} catch (IOException e) {
			LOGGER.error("*** ERROR {}", e);
		}
	}

	private BufferedWriter openFileWriter(String altFileName) {
		try {
			return new BufferedWriter(new FileWriter(altFileName));
		} catch (IOException e) {
			LOGGER.error("*** ERROR {}", e);
		}
		return null;
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
