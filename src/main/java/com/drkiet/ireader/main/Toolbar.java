package com.drkiet.ireader.main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.drkiet.ireader.handler.ReaderListener;
import com.drkiet.ireader.handler.ReaderListener.Command;

public class Toolbar extends JPanel implements ActionListener {
	private static final long serialVersionUID = 7885784738573268447L;
	private JButton startReadingButton;
	private JButton stopReadingButton;
	private AbstractButton pauseReadingButton;
	private JButton startReadingAtCaretButton;
	private JButton largerTextFontButton;
	private JButton smallerTextFontButton;
	private JButton previousPageButton;
	private AbstractButton nextPageButton;
	private AbstractButton largerWordFontButton;
	private JButton smallerWordFontButton;
	private JButton helpPictureButton;
	private com.drkiet.ireader.handler.ReaderListener readerListener;

	public Toolbar() {
		setBorder(BorderFactory.createEtchedBorder());
		startReadingButton = new JButton();
		startReadingButton.setIcon(createIcon("/icons/Play16.gif"));
		startReadingButton.setToolTipText("Start/Continue");

		stopReadingButton = new JButton();
		stopReadingButton.setIcon(createIcon("/icons/Stop16.gif"));
		stopReadingButton.setToolTipText("Pause");

		pauseReadingButton = new JButton();
		pauseReadingButton.setIcon(createIcon("/icons/Pause16.gif"));
		pauseReadingButton.setToolTipText("Reset");

		startReadingAtCaretButton = new JButton();
		startReadingAtCaretButton.setIcon(createIcon("/icons/StepForward16.gif"));
		startReadingAtCaretButton.setToolTipText("Start at Cursor");

		largerTextFontButton = new JButton();
		largerTextFontButton.setIcon(createIcon("/icons/Up16.gif"));
		largerTextFontButton.setToolTipText("Larger text size");

		smallerTextFontButton = new JButton();
		smallerTextFontButton.setIcon(createIcon("/icons/Down16.gif"));
		smallerTextFontButton.setToolTipText("Smaller text size");

		previousPageButton = new JButton();
		previousPageButton.setIcon(createIcon("/icons/Previous16.gif"));
		previousPageButton.setToolTipText("Previous Chapter");

		nextPageButton = new JButton();
		nextPageButton.setIcon(createIcon("/icons/Next16.gif"));
		nextPageButton.setToolTipText("Next Chapter");

		largerWordFontButton = new JButton();
		largerWordFontButton.setIcon(createIcon("/icons/ZoomIn16.gif"));
		largerWordFontButton.setToolTipText("Larger displaying word size");

		smallerWordFontButton = new JButton();
		smallerWordFontButton.setIcon(createIcon("/icons/ZoomOut16.gif"));
		smallerWordFontButton.setToolTipText("Smaller display word size");

		helpPictureButton = new JButton();
		helpPictureButton.setIcon(createIcon("/icons/Help16.gif"));
		helpPictureButton.setToolTipText("Navigation Help");

		startReadingButton.addActionListener(this);
		stopReadingButton.addActionListener(this);
		pauseReadingButton.addActionListener(this);
		startReadingAtCaretButton.addActionListener(this);
		largerTextFontButton.addActionListener(this);
		smallerTextFontButton.addActionListener(this);
		largerWordFontButton.addActionListener(this);
		smallerWordFontButton.addActionListener(this);
		previousPageButton.addActionListener(this);
		nextPageButton.addActionListener(this);
		helpPictureButton.addActionListener(this);

		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(startReadingButton);
		add(startReadingAtCaretButton);
		add(stopReadingButton);
		add(pauseReadingButton);
		add(largerTextFontButton);
		add(smallerTextFontButton);
		add(previousPageButton);
		add(nextPageButton);
		add(largerWordFontButton);
		add(smallerWordFontButton);
		add(helpPictureButton);
	}

	private ImageIcon createIcon(String path) {
		URL url = getClass().getResource(path);

		if (url == null) {
			System.err.println("Unable to load image: " + path);
		}
		return new ImageIcon(url);
	}

	@Override
	public void actionPerformed(ActionEvent toolbarEvent) {
		ToolbarEvent event = new ToolbarEvent(toolbarEvent);

		if (event.isNextPage()) {
			readerListener.invoke(Command.NEXT_PAGE);
		} else if (event.isPreviousPage()) {
			readerListener.invoke(Command.PREVIOUS_PAGE);
		} else if (event.isStartReading()) {
			readerListener.invoke(Command.START_READING);
		} else if (event.isStartReadingAt()) {
			readerListener.invoke(Command.START_READING_AT);
		} else if (event.isStopReading()) {
			readerListener.invoke(Command.STOP_READING);
		} else if (event.isPauseReading()) {
			readerListener.invoke(Command.PAUSE_READING);
		}
	}

	public void setReaderListener(ReaderListener readerListener) {
		this.readerListener = readerListener;
	}

	private class ToolbarEvent {
		private ActionEvent event;

		public ToolbarEvent(ActionEvent event) {
			this.event = event;
		}

		public boolean isPauseReading() {
			return event.getSource() == pauseReadingButton;
		}

		public boolean isStopReading() {
			return event.getSource() == stopReadingButton;
		}

		public boolean isStartReadingAt() {
			return event.getSource() == startReadingAtCaretButton;
		}

		public boolean isStartReading() {
			return event.getSource() == startReadingButton;
		}

		public boolean isPreviousPage() {
			return event.getSource() == previousPageButton;
		}

		public boolean isNextPage() {
			return event.getSource() == nextPageButton;
		}
	}
}
