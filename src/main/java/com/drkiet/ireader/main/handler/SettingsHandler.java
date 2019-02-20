package com.drkiet.ireader.main.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.main.FormPanel;
import com.drkiet.ireader.main.ReaderListener;
import com.drkiet.ireader.main.ReaderListener.Command;
import com.drkiet.ireader.util.FileHelper;
import com.drkiettran.text.TextApp;

public class SettingsHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingsHandler.class);
	private String selectedBookName;
	private FormPanel formPanel;
	private String selectedReference;
	private BookHandler bookHandler;
	private ReferenceHandler refHandler;

	public void processForm(FormPanel formPanel, Command cmd) {
		this.formPanel = formPanel;

		switch (cmd) {
		case SELECT_BOOK:
			selectedBookName = formPanel.getSelectedBookName();
			LOGGER.info("Selected book {}", selectedBookName);
			break;

		case OPEN_SELECTED_BOOK:
			if (selectedBookName == null) {
				selectedBookName = formPanel.getSelectedBookName();
			}
			openSelectedBook();
			break;

		case SELECT_REFERENCE:
			selectedReference = formPanel.getSelectedReference();
			LOGGER.info("Selected reference {}", selectedReference);
			break;

		case OPEN_SELECTED_REFERENCE:
			if (selectedBookName == null) {
				selectedBookName = formPanel.getSelectedReference();
			}
			openSelectedReference();
			break;

//		case LOAD:
//			loadSelectedBook();
//			break;
//
//		case LOAD_REF:
//			loadSelectedRefBook();
//			break;

//		case SEARCH:
//			search();
//			break;
//
//		case NEXT_FIND:
//			nextFind();
//			break;
//
//		case GOTO:
//			goToPage(formPanel.getGotoPageNo());
//			break;

		default:
			break;
		}
	}

	private void openSelectedReference() {
		refHandler = new ReferenceHandler();
		refHandler.openSelectedReference(selectedReference);
	}

	private void openSelectedBook() {
		bookHandler = new BookHandler();
		bookHandler.openSelectedBook(selectedBookName);
	}

	public void loadSelectedBook() {
//		document = formPanel.getDocument();
//		bookName = formPanel.getSelectedBookName();
//		infoPanel.setBookName(bookName);
//		StringBuilder sb = new StringBuilder();
//
//		if (document != null) {
//			textPanel.loadTextFromFile(document);
//			formPanel.enableSearch();
//			formPanel.enableGoto();
//
//			// @formatter:off
//			sb.append('\n')
//			  .append(bookName)
//			  .append(" is loaded successfully!\n")
//			  .append("This book has ")
//			  .append(document.getPageCount())
//			  .append(" pages.\n");
//			// @formatter:on
//			infoPanel.addText(sb.toString());
//
//		} else {
//			textPanel.resetReading();
//			sb.append('\n').append(bookName).append(" fails to load!\n");
//			infoPanel.addText(sb.toString());
//		}
//		searchText = "";
	}
}
