package com.drkiet.ireader.handler;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.main.FormPanel;
import com.drkiet.ireader.main.LoggingPanel;
import com.drkiet.ireader.reference.ReferencesFrame;
import com.drkiet.ireader.util.FileHelper;
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
	private Integer speedWpm;

	public SettingsHandler(FormPanel formPanel, LoggingPanel loggingPanel) {
		this.formPanel = formPanel;
		this.loggingPanel = loggingPanel;
	}

	public void processForm(Command cmd) {
		switch (cmd) {
		case SET_SPEED_WPM:
			speedWpm = formPanel.getSpeedWpm();
			LOGGER.info("Changed speed to {} ...", speedWpm);
			break;

		case SELECT_BOOK:
			selectedBookName = getSelectedFileName(formPanel.getSelectedBookName());
			LOGGER.info("Selected book {}", selectedBookName);
			loggingPanel.info(selectedBookName + " is selected book.");
			break;

		case OPEN_SELECTED_BOOK:
			if (selectedBookName == null) {
				selectedBookName = formPanel.getSelectedBookName();
				loggingPanel.info(selectedBookName + " is open.");
			}
			openSelectedBook();
			break;

		case SELECT_REFERENCE:
			selectedReference = getSelectedFileName(formPanel.getSelectedReference());
			LOGGER.info("Selected reference {}", selectedReference);
			loggingPanel.info(selectedReference + " is selected reference.");
			break;

		case OPEN_SELECTED_REFERENCE:
			if (selectedReference == null) {
				selectedReference = formPanel.getSelectedReference();
				loggingPanel.info(selectedReference + " is referenced.");
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

	private String getSelectedFileName(String fileName) {
		fileName = FileHelper.getFQContentFileName(fileName);
		String altFileName = FileHelper.getAltFileName(fileName, ".txt");
		return (new File(altFileName).exists()) ? altFileName : fileName;
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

	public Integer getSpeedWpm() {
		return speedWpm;
	}
}
