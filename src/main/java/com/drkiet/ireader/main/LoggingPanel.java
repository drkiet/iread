package com.drkiet.ireader.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingPanel extends JPanel {
	private static final long serialVersionUID = -5647385749461169568L;
	public final static String HTML_TEMPLATE = "<br><font color=\"%s\" size=\"%d\" face=\"%s\">%s</font>";

	private JLabel fileNameLabel;
	private JTextPane textPane;
	protected Integer selectedPageNo = null;
	private String messageText = "";
	private StringBuilder displayingText = new StringBuilder();

	public Integer getSelectedPageNo() {
		return selectedPageNo;
	}

	public LoggingPanel() {
		arrangeFixedComponents();
		makeTextArea();
		setBorder();
		arrangeLayout();
	}

	private void arrangeLayout() {
		setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.getViewport().setPreferredSize(new Dimension(100, 100));
		add(scrollPane, BorderLayout.CENTER);
		add(fileNameLabel, BorderLayout.SOUTH);
	}

	private void setBorder() {
		Border innerBorder = BorderFactory.createTitledBorder("Results");
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));

	}

	private void makeTextArea() {
		textPane = new JTextPane();
		textPane.setCaretPosition(0);
		textPane.setCaretColor(Color.white);
		textPane.setContentType("text/html");
		textPane.setText("");

		textPane.addMouseListener(getMouseListner());
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingPanel.class);

	private boolean isNumber(String selectedPageNoStr) {
		for (int idx = 0; idx < selectedPageNoStr.length(); idx++) {
			if (!Character.isDigit(selectedPageNoStr.charAt(idx))) {
				return false;
			}
		}
		return true;
	}

	private MouseListener getMouseListner() {

		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String selectedPageNoStr = textPane.getSelectedText();
				if (selectedPageNoStr != null && isNumber(selectedPageNoStr)) {
					selectedPageNo = Integer.valueOf(selectedPageNoStr);
//					mainFrame.goToPage(selectedPageNo);
				}
				LOGGER.info("1. text: '{}'", textPane.getSelectedText());

			}

			@Override
			public void mousePressed(MouseEvent e) {
				LOGGER.info("2. text: '{}'", textPane.getSelectedText());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				LOGGER.info("3. text: '{}'", textPane.getSelectedText());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				LOGGER.info("4. text: '{}'", textPane.getSelectedText());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		};
	}

	private void arrangeFixedComponents() {
		fileNameLabel = new JLabel("file:");
		fileNameLabel.setFont(new Font("Candara", Font.PLAIN, 12));
	}

	public void setFileName(String fileName) {
		fileNameLabel.setText("file: " + fileName);
	}

	public void addText(String text) {
		messageText = new StringBuilder(messageText).append("<br>").append(text).append("<br>").toString();
		textPane.setText(messageText);
	}

	public void clearText() {
		messageText = "";
		textPane.setText(messageText);
	}

	public void info(String text) {
		StringBuilder sb = new StringBuilder();
		sb.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss - "));
		sb.append(text);
		displayingText.append(String.format(HTML_TEMPLATE, "BLUE", 4, "Candara", sb.toString()));
		textPane.setText(displayingText.toString());
	}
}
