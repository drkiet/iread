package com.drkiet.ireader.main;

import java.util.TimerTask;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Establishing an asynchronous timer awaken periodically until the reading is
 * done.
 * 
 * @author Kiet T. Tran, Ph.D (c) 2019
 *
 */
public class TextTimerTask extends TimerTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(TextTimerTask.class);
	private ContentTextPane contentTextPane;
	private TextPanel textPanel;

	@Override
	public void run() {
		textPanel.readWord();
		if (textPanel.isDoneReading()) {
			LOGGER.info("*** Reading is completed! ***");
			cancel();
		}
	}

	public void register(TextPanel textPanel) {
		LOGGER.info("Reading timer starts ...");
		this.textPanel = textPanel;
	}

}
