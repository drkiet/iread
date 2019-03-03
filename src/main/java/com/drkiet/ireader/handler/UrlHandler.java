package com.drkiet.ireader.handler;

import com.drkiet.ireader.main.LoggingPanel;
import com.drkiet.ireader.url.UrlFrame;
import com.drkiet.ireader.util.WebHelper;

public class UrlHandler {

	private LoggingPanel loggingPanel;
	private String clickedUrl;
	private int textPaneFontSize = 5;
	private String textPaneFont = "Candara";
	private UrlFrame urlFrame = new UrlFrame();

	public UrlHandler(LoggingPanel loggingPanel) {
		this.loggingPanel = loggingPanel;
	}

	public void setUrl(String clickedUrl) {
		this.clickedUrl = clickedUrl;
		urlFrame.setUrl(clickedUrl);
	}

}
