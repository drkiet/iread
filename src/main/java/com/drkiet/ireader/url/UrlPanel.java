package com.drkiet.ireader.url;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
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

import com.drkiet.ireader.util.WebHelper;

public class UrlPanel extends JPanel {
	private static final long serialVersionUID = -7286531138604994466L;
	public static final String FONT_BEGIN = "<font size=\"%d\" face=\"%s\">";
	public static final String FONT_END = "</font>";

	private JTextPane urlPane;
	private Integer textPaneFontSize = 5;
	private String highlightedText = "";
	private String urlContent;
	private String lastUrl = "";

	public UrlPanel() {
		urlPane = new JTextPane();
		urlPane.setCaretPosition(0);
		urlPane.setCaretColor(Color.WHITE);
		urlPane.setContentType("text/html");

		setLayout(new BorderLayout());
		add(new JScrollPane(urlPane), BorderLayout.CENTER);
		urlPane.addMouseListener(getMouseListner());
		setBorder();
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlPanel.class);

	private MouseListener getMouseListner() {

		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String clickedUrl = null;
				List<Element> contentsWithUrls = new ArrayList<Element>();

				for (Element el : urlPane.getDocument().getRootElements()) {
					findContentsWithUrls(el, contentsWithUrls);
				}

				int caretPos = urlPane.getCaretPosition();
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
						LOGGER.info(">>> FOUND -- {}: {}", attrName, foundEl.getAttributes().getAttribute(attrName));
						LOGGER.info(">>> FOUND -- {}: {}", attrName, href);
						clickedUrl = href.substring(href.indexOf("=") + 1).trim().toLowerCase();
						break;
					}
				}

				if (!clickedUrl.startsWith("http")) {
					if (clickedUrl.startsWith("//")) {
						clickedUrl = "http:" + clickedUrl;
						lastUrl = clickedUrl;
					} else {
						clickedUrl = lastUrl + clickedUrl;
					}
				} else {
					lastUrl = clickedUrl;
				}
				LOGGER.info("urlClicked: {} / lastUrl: {}", clickedUrl, lastUrl);
				urlContent = WebHelper.getUrlContent(clickedUrl, 5, "Candara");
				displayUrlContent();
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
				for (Element element : urlPane.getDocument().getRootElements()) {
					printElement(element);
				}

				try {
					int len = urlPane.getSelectionEnd() - urlPane.getSelectionStart();
					highlightedText = urlPane.getDocument().getText(urlPane.getSelectionStart(), len).trim();
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

	public void setUrlContent(String urlContent) {
		this.urlContent = urlContent;
		displayUrlContent();
	}

	private void displayUrlContent() {
//		LOGGER.info("RAW:\n{}", urlContent);
		urlPane.setText(urlContent);
		urlPane.setCaretPosition(0);
	}

	public void setUrl(String url) {
		try {
			URL myUrl = new URL(url);
			lastUrl = String.format("%s://%s:%d/", myUrl.getProtocol(), myUrl.getHost(), myUrl.getPort());
		} catch (MalformedURLException e) {
			LOGGER.error("*** ERROR *** {}", e);
		}

		urlContent = WebHelper.getUrlContent(url, 5, "Candara");
		displayUrlContent();
	}

}
