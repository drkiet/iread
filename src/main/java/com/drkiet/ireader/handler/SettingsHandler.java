package com.drkiet.ireader.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.main.FormPanel;
import com.drkiet.ireader.main.LoggingPanel;
import com.drkiet.ireader.reference.ReferencesFrame;
import com.drkiet.ireader.util.TextUtil;

/**
 * This class manages all events of the formPanel where all the settings occur.
 * 
 * @author ktran
 *
 */
public class SettingsHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingsHandler.class);
	private static final Object HTML_BREAK = "<br>";

	private String selectedBookName;
	private FormPanel formPanel;
	private String selectedReference;
	private BookHandler bookHandler;
	private ReferenceHandler refHandler;
	private String searchText;
	private LoggingPanel loggingPanel;
	
	public SettingsHandler(FormPanel formPanel, LoggingPanel loggingPanel) {
		this.formPanel = formPanel;
		this.loggingPanel = loggingPanel;
	}

	public void processForm(Command cmd) {
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

		case GOTO_PAGE:
			break;
		case SEARCH:
			this.searchText = formPanel.getSearchText();
			break;
		case SEARCH_NEXT:
			break;
		default:
			break;
		}
	}

	private void openSelectedReference() {
		refHandler = new ReferenceHandler(loggingPanel);
		refHandler.openSelectedReference(selectedReference);
		logReferenceHandler();
	}

	private void logReferenceHandler() {
		StringBuilder sb = new StringBuilder();
		sb.append("Reference: ").append(selectedReference).append(" has ");
		sb.append(TextUtil.underline(refHandler.getIndexedWordCount())).append(" indexed words").append(HTML_BREAK);
		loggingPanel.info(sb.toString());
	}

	private void openSelectedBook() {
		bookHandler = new BookHandler();
		bookHandler.openSelectedBook(selectedBookName);
		logBookHandlerInfo();
	}

	private void logBookHandlerInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Book: ").append(bookHandler.getBookName()).append(" has ");
		sb.append(TextUtil.underline(bookHandler.getPageCount())).append(" pages").append(HTML_BREAK);
		loggingPanel.info(sb.toString());
	}

	public BookHandler getBookHandler() {
		return bookHandler;
	}

	public ReferenceHandler getReferenceHandler() {
		return refHandler;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.formPanel.setSearchText(searchText);
		this.searchText = searchText;
	}
}
