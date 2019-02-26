package com.drkiet.ireader.reference;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.drkiet.ireader.handler.ReaderListener;
import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.util.ScreenPositions;

public class ReferencesFrame extends JFrame {
	private static final long serialVersionUID = 4689102811730742079L;
	private ReferencesPanel referencesPanel;
	private ReferencesToolbarPanel referencesToolbarPanel;
	private ReaderListener listener;
	private String selectedReference;

	public ReferencesFrame(String selectedReference) {
		this.selectedReference = selectedReference;
		setLayout(new BorderLayout());
		setTitle("References: " + selectedReference);
		setSize(600, 500);
		referencesPanel = new ReferencesPanel();
		referencesToolbarPanel = new ReferencesToolbarPanel();
		referencesToolbarPanel.setReaderListener((Command cmd) -> {
			switch (cmd) {

			default:
				break;
			}
		});

		add(referencesToolbarPanel, BorderLayout.NORTH);
		add(referencesPanel, BorderLayout.CENTER);
		setLocation(ScreenPositions.getTopEast((int) getSize().getWidth(), (int) getSize().getHeight()));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public void setText(String displayText) {
		referencesPanel.setText(displayText);
	}

	public int getSelectedPageNumber() {
		return referencesPanel.getSelectedPageNumber();
	}

	public void setListener(ReaderListener listener) {
		this.listener = listener;
		referencesPanel.setListener(listener);
	}
}
