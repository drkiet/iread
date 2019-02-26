package com.drkiet.ireader.dictionary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.reference.ReferencesFrame;
import com.drkiet.ireader.reference.ReferencesPanel;
import com.drkiet.ireader.util.FileHelper;
import com.drkiet.ireader.util.FontHelper;
import com.drkiet.ireader.util.WebHelper;
import com.drkiettran.text.util.CommonUtils;

public class DictionaryPanel extends JPanel {
	private static final long serialVersionUID = 8683655130181562963L;
	public static final String FONT_BEGIN = "<font size=\"%d\" face=\"%s\">";
	public static final String FONT_END = "</font>";

	private String definition = null;
	private Integer textPaneFontSize = 4;
	private String textPaneFont = "Candara";
	private String highlightedText = "";

	private Object dictDefinitions;
	private ContentTermPane contentTermPane;

	public DictionaryPanel() {
		contentTermPane = new ContentTermPane();
		setLayout(new BorderLayout());
		add(new JScrollPane(contentTermPane), BorderLayout.CENTER);
		contentTermPane.addMouseListener(getMouseListner());
		setBorder();
	}

	public void displayDefinition(String definition) {
		this.definition = definition;
		displayDefinition();
	}

	public void displayDefinition() {
		StringBuilder sb = new StringBuilder("<html>");
		sb.append(String.format(FONT_BEGIN, textPaneFontSize, textPaneFont));
		sb.append(definition);
		sb.append(FONT_END);
		contentTermPane.setText(sb.toString());
		contentTermPane.setCaretPosition(0);
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryPanel.class);

	private MouseListener getMouseListner() {

		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String urlClicked = null;
				List<Element> contentsWithUrls = new ArrayList<Element>();

				for (Element el : contentTermPane.getDocument().getRootElements()) {
					findContentsWithUrls(el, contentsWithUrls);
				}

				int caretPos = contentTermPane.getCaretPosition();
				Element foundEl = null;

				for (Element el : contentsWithUrls) {
					LOGGER.info("{} - {}, {}", caretPos, el.getStartOffset(), el.getEndOffset());

					if (caretPos >= el.getStartOffset() && caretPos <= el.getEndOffset()) {
						foundEl = el;
						break;
					}
				}

				if (foundEl == null) {
					return;
				}

				Enumeration<?> attrs = foundEl.getAttributes().getAttributeNames();

				while (attrs.hasMoreElements()) {
					Object attrName = attrs.nextElement();
					if ("a".equals(attrName.toString())) {
						String href = foundEl.getAttributes().getAttribute(attrName).toString();
						urlClicked = href.substring(href.indexOf("=") + 1);
						break;
					}
				}
				LOGGER.info("urlClicked: {}", urlClicked);
//				referencesFrame.setText(urlClicked.trim());

			}

			private void findContentsWithUrls(Element el, List<Element> contentsWithUrls) {
				if (el.getElementCount() > 0) {
					for (int idx = 0; idx < el.getElementCount(); idx++) {
						findContentsWithUrls(el.getElement(idx), contentsWithUrls);
					}
				}
				Enumeration<?> attrs = el.getAttributes().getAttributeNames();

				while (attrs.hasMoreElements()) {
					Object attrName = attrs.nextElement();
					if ("a".equals(attrName.toString())) {
						contentsWithUrls.add(el);
						LOGGER.info("FOUND -- {}: {}", attrName, el.getAttributes().getAttribute(attrName));
						break;
					}
					LOGGER.info("-- {}: {}", attrName, el.getAttributes().getAttribute(attrName));
				}
			}

			@Override
			public void mousePressed(MouseEvent event) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				for (Element element : contentTermPane.getDocument().getRootElements()) {
					printElement(element);
				}

				try {
					int len = contentTermPane.getSelectionEnd() - contentTermPane.getSelectionStart();
					highlightedText = contentTermPane.getDocument().getText(contentTermPane.getSelectionStart(), len)
							.trim();
					LOGGER.info("HIGHLIGHTED: {}", highlightedText);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			private void printElement(Element el) {
				LOGGER.info(">> offset {}({}, {})", el.getName(), el.getStartOffset(), el.getEndOffset());
				Enumeration<?> attrs = el.getAttributes().getAttributeNames();

				while (attrs.hasMoreElements()) {
					Object attrName = attrs.nextElement();
					LOGGER.info("-- {}: {}", attrName, el.getAttributes().getAttribute(attrName));
				}

				if (el.getElementCount() == 0) {
					return;
				}

				for (int idx = 0; idx < el.getElementCount(); idx++) {
					printElement(el.getElement(idx));
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

	private void setBorder() {
		Border innerBorder = BorderFactory.createTitledBorder("Definition");
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));
	}

	public void setSmallerText() {
		if (textPaneFontSize > ReferencesPanel.SMALLEST_TEXT_AREA_FONT_SIZE) {
			this.textPaneFontSize--;
		}
		displayDefinition();
		repaint();
	}

	public void setLargerText() {
		if (textPaneFontSize < ReferencesPanel.LARGEST_TEXT_AREA_FONT_SIZE) {
			this.textPaneFontSize++;
		}
		displayDefinition();

		repaint();
	}

}
