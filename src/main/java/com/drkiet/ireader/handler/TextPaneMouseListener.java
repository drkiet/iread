package com.drkiet.ireader.handler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiettran.text.ReadingTextManager;
import com.drkiettran.text.model.Word;

public class TextPaneMouseListener implements MouseListener, MouseMotionListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneMouseListener.class);
	private JTextPane textPane;
	private Word selectedWord;
	private ReadingTextManager rtm;
	private String highlightedText;

	public TextPaneMouseListener(JTextPane textPane, ReadingTextManager rtm) {
		LOGGER.info("Constructing MouseHandler");
		this.textPane = textPane;
		this.rtm = rtm;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		LOGGER.info("Mouse is clicked!");
		SwingUtilities.isLeftMouseButton(e);
		SwingUtilities.isRightMouseButton(e);
		SwingUtilities.isMiddleMouseButton(e);
		int caretPos = textPane.getCaretPosition();
		LOGGER.info("carrot = {}; ", caretPos);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (textPane.getSelectedText() != null) { // See if they selected something
			highlightedText = textPane.getSelectedText().trim();
			LOGGER.info("highlighted text: {}", highlightedText);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
