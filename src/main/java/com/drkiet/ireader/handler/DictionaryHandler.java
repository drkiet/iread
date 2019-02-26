package com.drkiet.ireader.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.dictionary.DictionaryFrame;
import com.drkiet.ireader.main.LoggingPanel;

public class DictionaryHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryHandler.class);
	private DictionaryFrame dictionaryFrame;
	private LoggingPanel loggingPanel;

	public DictionaryHandler(LoggingPanel loggingPanel) {
		this.loggingPanel = loggingPanel;
		dictionaryFrame = new DictionaryFrame();
	}

	public void getDefinition(String term) {
		LOGGER.info("Get definition {}", term);
		dictionaryFrame.displayDefinition(term);
	}
}
