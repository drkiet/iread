package com.drkiet.ireader.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.main.LoggingPanel;
import com.drkiet.ireader.main.TextPanel;

public class TextHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(TextHandler.class);
	private TextPanel textPanel;
	private LoggingPanel loggingPanel;
	private ReferenceHandler refHandler = null;
	private String searchText;

	public TextHandler(TextPanel textPanel, LoggingPanel loggingPanel) {
		this.textPanel = textPanel;
		this.loggingPanel = loggingPanel;
	}

	public void processText(Command cmd) {
		switch (cmd) {
		case POPUP_SEARCH:
			popupSearch();
			break;
		case GET_DEFINITION:
			popupSearch();
			break;
		default:
			break;
		}

	}

	public String getSearchText() {
		return searchText;
	}

	private void popupSearch() {
		searchText = null;
		if (textPanel.getHighlightedText() == null) {
			searchText = textPanel.getWordAtCaret();
		} else {
			searchText = textPanel.getHighlightedText();
		}

		LOGGER.info("Popup Search ... {} ", searchText);

		if (searchText != null) {
			searchText(searchText);
			if (refHandler != null) {
				refHandler.searchText(searchText);
			}
		}
	}

	public void gotoPage(int go2PageNo) {
		textPanel.goTo(go2PageNo);
	}

	public void searchText(String searchText) {
		loggingPanel.info(textPanel.search(searchText));
	}

	public void setRefenceHandler(ReferenceHandler refHandler) {
		this.refHandler = refHandler;
	}

}
