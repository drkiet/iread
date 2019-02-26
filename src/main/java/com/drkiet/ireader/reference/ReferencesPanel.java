package com.drkiet.ireader.reference;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.handler.ReaderListener;
import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.reference.page.ReferencePageFrame;

public class ReferencesPanel extends JPanel {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferencesPanel.class);

	private static final long serialVersionUID = -8244136736985618463L;
	public static final int SMALLEST_TEXT_AREA_FONT_SIZE = 3;
	public static final int LARGEST_TEXT_AREA_FONT_SIZE = 7;
	private static final String FONT_BEGIN = "<font size=\"%d\" face=\"%s\">";
	private static final Object FONT_END = "</font>";

	private JTextPane referencesPane;
	private int textPaneFontSize = 5;
	private String textPaneFont = "Candara";
	private int selectedPageNumber;

	private ReaderListener listener;

	public ReferencesPanel() {
		referencesPane = new JTextPane();
		referencesPane.setCaretPosition(0);
		referencesPane.setCaretColor(Color.WHITE);
		referencesPane.setContentType("text/html");

		setLayout(new BorderLayout());
		add(new JScrollPane(referencesPane), BorderLayout.CENTER);
		referencesPane.addMouseListener(getMouseListner());
		setBorder("References");
	}

	public void setText(String displayText) {
		displayText(displayText);
	}

	private MouseListener getMouseListner() {
		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (referencesPane.getSelectedText() != null) {
					String selectedText = referencesPane.getSelectedText().trim();
					if (NumberUtils.isNumber(selectedText)) {
						selectedPageNumber = Integer.valueOf(selectedText);
						listener.invoke(Command.GOTO_PAGE);
						LOGGER.info("Selected page #: {}", selectedPageNumber);
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		};
	}

	private void displayText(String displayingText) {
		StringBuilder sb = new StringBuilder(String.format(FONT_BEGIN, textPaneFontSize, textPaneFont));
		sb.append(displayingText).append(FONT_END);
		referencesPane.setText(sb.toString());
		referencesPane.setCaretPosition(0);
	}

	private void setBorder(String fileName) {
		Border innerBorder = BorderFactory.createTitledBorder(fileName);
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));
	}

	public int getSelectedPageNumber() {
		return selectedPageNumber;
	}

	public void setListener(ReaderListener listener) {
		this.listener = listener;
	}

}
