package com.drkiet.ireader.dictionary;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.util.ScreenPositions;

public class DictionaryFrame extends JFrame {

	private static final long serialVersionUID = 4689102811730742079L;
	private DictionaryPanel definitionPanel;
	private DictionaryToolbarPanel definitionToolbarPanel;
	private String word = "";

	public DictionaryFrame() {
		setLayout(new BorderLayout());
		setTitle("Definitions");
		setSize(600, 500);
		definitionPanel = new DictionaryPanel();
		definitionToolbarPanel = new DictionaryToolbarPanel();

		definitionToolbarPanel.setReaderListener((Command cmd) -> {
			switch (cmd) {

			case LARGER_TEXT_FONT:
				makeLargerFont();
				break;

			case SMALLER_TEXT_FONT:
				makeSmallerFont();
				break;

			default:
				break;
			}
		});

		add(definitionToolbarPanel, BorderLayout.NORTH);
		add(definitionPanel, BorderLayout.CENTER);
		setLocation(ScreenPositions.getBottomEast((int) getSize().getWidth(), (int) getSize().getHeight()));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void makeSmallerFont() {
		definitionPanel.setSmallerText();
	}

	private void makeLargerFont() {
		definitionPanel.setLargerText();
	}

	public void displayDefinition(String word) {
		if (this.word.equalsIgnoreCase(word)) {
			return;
		}

		this.word = word;
		definitionPanel.displayDefinition(word.toLowerCase());
	}

}
