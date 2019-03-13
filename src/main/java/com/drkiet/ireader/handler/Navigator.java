package com.drkiet.ireader.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.main.TextPanel;

public class Navigator {
	private static final Logger LOGGER = LoggerFactory.getLogger(Navigator.class);

	private TextPanel textPanel;
	private int speedWpm;

	public Navigator(TextPanel textPanel, int speedWpm) {
		this.textPanel = textPanel;
		this.speedWpm = speedWpm;
	}

	public void processToolbar(Command cmd) {
		switch (cmd) {
		case NEXT_PAGE:
			textPanel.nextPage();
			break;

		case PREVIOUS_PAGE:
			textPanel.previousPage();
			break;

		case START_READING:
			LOGGER.info("Start reading ... {} wpm", speedWpm);
			textPanel.startReading(speedWpm);
			break;

		case PAUSE_READING:
			LOGGER.info("Pause reading ... {} wpm", speedWpm);
			textPanel.pauseReading();
			break;

		case STOP_READING:
			LOGGER.info("Stop reading ... {} wpm", speedWpm);
			textPanel.stopReading();
			break;

		case START_READING_AT:
			LOGGER.info("Stop reading ... {} wpm", speedWpm);
			textPanel.startReadingAt(speedWpm);
			break;

		default:
			break;
		}
	}

	public void setSpeedWpm(Integer speedWpm) {
		this.speedWpm = speedWpm;
	}

}
