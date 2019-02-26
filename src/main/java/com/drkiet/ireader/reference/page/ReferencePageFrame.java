package com.drkiet.ireader.reference.page;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import com.drkiet.ireader.handler.ReaderListener;
import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.util.FileHelper;
import com.drkiet.ireader.util.ScreenPositions;

public class ReferencePageFrame extends JFrame {

	private static final long serialVersionUID = 4689102811730742079L;
	private ReferencePagePanel referencePagePanel;
	private ReferencePageToolbarPanel referencePageToolbarPanel;
	private ReaderListener listener;

	public ReferencePageFrame(String refName) {
		setLayout(new BorderLayout());
		setTitle("Reference Contents: " + refName);
		setSize(600, 500);
		referencePagePanel = new ReferencePagePanel(FileHelper.getFQRefencesFileName(refName));
		referencePageToolbarPanel = new ReferencePageToolbarPanel();

		referencePageToolbarPanel.setReaderListener((Command cmd) -> {
			listener.invoke(cmd);
		});

		add(referencePageToolbarPanel, BorderLayout.NORTH);
		add(referencePagePanel, BorderLayout.CENTER);
		setLocation(ScreenPositions.getCenterEast((int) getSize().getWidth(), (int) getSize().getHeight()));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public void setText(String pageText) {
		referencePagePanel.setText(pageText);
	}

	public void setListener(ReaderListener listener) {
		this.listener = listener;
		referencePagePanel.setListener(listener);
	}

	public String getHighlightedText() {
		return referencePagePanel.getHighlightedText();
	}
}
