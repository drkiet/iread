package com.drkiet.ireader.reference.page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import javax.swing.text.Highlighter.Highlight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.handler.ReaderListener;
import com.drkiet.ireader.handler.ReaderListener.Command;

public class ReferencePagePanel extends JPanel {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferencePagePanel.class);

	private static final long serialVersionUID = -8244136736985618463L;
	public static final int SMALLEST_TEXT_AREA_FONT_SIZE = 3;
	public static final int LARGEST_TEXT_AREA_FONT_SIZE = 7;
	private static final String FONT_BEGIN = "<font size=\"%d\" face=\"%s\">";
	private static final Object FONT_END = "</font>";

	private JTextPane referencePagePane;
	private int textPaneFontSize = 5;
	private String textPaneFont = "Candara";

	private ReaderListener listener;

	private String highlightedText;

	public ReferencePagePanel(String refName) {
		referencePagePane = new JTextPane();
		referencePagePane.setCaretPosition(0);
		referencePagePane.setCaretColor(Color.WHITE);
		referencePagePane.setContentType("text/html");

		setLayout(new BorderLayout());
		add(new JScrollPane(referencePagePane), BorderLayout.CENTER);
		referencePagePane.addMouseListener(getMouseListner());
		setBorder("Reference Content");

	}

	private void getSelectedText() {
		highlightedText = referencePagePane.getSelectedText();
		if (highlightedText != null) {
			highlightedText.trim();
		}
	}

	private MouseListener getMouseListner() {
		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
//				String word = getWordAtCaret(referencePagePane);
//				if (SwingUtilities.isRightMouseButton(e)) {
//					LOGGER.info("definition for: {}", word);
////					displayDefinitions(word);
//				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					doPopup(e);
				}
			}

			private void doPopup(MouseEvent e) {
				PopUpDemo menu = new PopUpDemo();
				menu.show(e.getComponent(), e.getX(), e.getY());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				getSelectedText();
				if (e.isPopupTrigger()) {
					doPopup(e);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (referencePagePane.getSelectedText() != null) { // See if they selected something
					referencePagePane.getSelectedText().trim();
				}
			}

		};
	}

	private static String getWordAtCaret(JTextComponent tc) {
		try {
			int caretPosition = tc.getCaretPosition();
			int start = Utilities.getWordStart(tc, caretPosition);
			int end = Utilities.getWordEnd(tc, caretPosition);
			return tc.getText(start, end - start);
		} catch (BadLocationException e) {
			System.err.println(e);
		}

		return null;
	}

	private void setBorder(String fileName) {
		Border innerBorder = BorderFactory.createTitledBorder(fileName);
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));
	}

	public void setText(String pageText) {
		StringBuilder sb = new StringBuilder(String.format(FONT_BEGIN, textPaneFontSize, textPaneFont));
		sb.append(pageText).append(FONT_END);
		referencePagePane.setText(sb.toString());
		referencePagePane.setCaretPosition(0);
	}

	public void setListener(ReaderListener listener) {
		this.listener = listener;
	}

	class PopUpDemo extends JPopupMenu implements ActionListener {
		private static final long serialVersionUID = -21613387050518902L;
		JMenuItem item;

		public PopUpDemo() {
			item = new JMenuItem("Search");
			item.addActionListener(this);
			add(item);

			item = new JMenuItem("Dictionary");
			item.addActionListener(this);
			add(item);

			item = new JMenuItem("Help");
			item.addActionListener(this);
			add(item);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			LOGGER.info("Selected: {}", item.getText());

			switch (item.getText()) {
			case "Search":
				listener.invoke(Command.POPUP_SEARCH);
				break;
			case "Dictionary":
				listener.invoke(Command.GET_DEFINITION);
				break;
			case "Help":
				listener.invoke(Command.GET_HELP);
				break;
			default:
				break;
			}
		}
	}

	public String getHighlightedText() {
		return highlightedText;
	}
}
