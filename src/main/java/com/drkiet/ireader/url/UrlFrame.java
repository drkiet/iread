package com.drkiet.ireader.url;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.util.ScreenPositions;

public class UrlFrame extends JFrame {

	private static final long serialVersionUID = 4689102811730742079L;
	private UrlPanel urlPanel;
	private UrlToolbarPanel urlToolbarPanel;
	private String word = "";

	public UrlFrame() {
		setLayout(new BorderLayout());
		setTitle("Browsing URLs");
		setSize(600, 500);
		urlPanel = new UrlPanel();
		urlToolbarPanel = new UrlToolbarPanel();

		urlToolbarPanel.setReaderListener((Command cmd) -> {
			switch (cmd) {

			case LARGER_TEXT_FONT:
//				makeLargerFont();
				break;

			case SMALLER_TEXT_FONT:
//				makeSmallerFont();
				break;

			default:
				break;
			}
		});

		add(urlToolbarPanel, BorderLayout.NORTH);
		add(urlPanel, BorderLayout.CENTER);
		setLocation(ScreenPositions.getBottomWest((int) getSize().getWidth(), (int) getSize().getHeight()));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public void setUrl(String url) {
		urlPanel.setUrl(url);
	}
}
