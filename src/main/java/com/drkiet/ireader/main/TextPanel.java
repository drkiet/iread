package com.drkiet.ireader.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import org.elasticsearch.common.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.handler.BookHandler;
import com.drkiet.ireader.handler.ReaderListener;
import com.drkiettran.text.ReadingTextManager;
import com.drkiettran.text.model.Page;
import com.drkiettran.text.model.SearchResult;

public class TextPanel extends JPanel {
	private static final Logger LOGGER = LoggerFactory.getLogger(TextPanel.class);

	public static final String LINE_INFO = " *** line: ";
	public static final int DEFAULT_DISPLAYING_FONT_SIZE = 60;
	public static final int DEFAULT_TEXT_AREA_FONT_SIZE = 18;
	public static final int SMALLEST_DISPLAYING_FONT_SIZE = 20;
	public static final int SMALLEST_TEXT_AREA_FONT_SIZE = 10;
	public static final int LARGEST_DISPLAYING_FONT_SIZE = 100;
	public static final int LARGEST_TEXT_AREA_FONT_SIZE = 32;
	public static final long serialVersionUID = -825536523977292110L;

	private ContentTextPane textPane;
	private JLabel displayingWordLabel;
	private String readingText = null;
	private JLabel infoLabel;
	private String htmlFontName = "Candara";
	private int displayingWordFontSize = DEFAULT_DISPLAYING_FONT_SIZE;
	private ReadingTextManager rtm = null;
	private String searchText;
	public String[] THE_ARTICLES = { "the", "an", "a" };
	public List<String> ARTICLES = Arrays.asList(THE_ARTICLES);
	private BookHandler bookHandler;

	private boolean isReading = false;

	private TextTimerTask textTimerTask = null;
	private Timer timer = null;

	private int speedWpm;

	/**
	 * Construct a Text Panel that includes labels Top, Bottom and text pane in the
	 * middle for displaying reading text
	 * 
	 */
	public TextPanel() {
		arrangeFixedComponents();
		setBorder();
		arrangeLayout();
	}

	/**
	 * Assign a BookHandler object.
	 * 
	 * @param bookHandler
	 */
	public void setBookHandler(BookHandler bookHandler) {
		LOGGER.info("Displaying {}", bookHandler.getBookName());
		this.bookHandler = bookHandler;
		rtm = bookHandler.getReadTextManager();
		displayText(rtm);
	}

	/**
	 * Set reading speed word per minute.
	 * 
	 * @param speedWpm
	 */
	public void setSpeed(Integer speedWpm) {
		this.speedWpm = speedWpm;
	}

	/**
	 * Set command listner.
	 * 
	 * @param readerListener
	 */
	public void setReaderListener(ReaderListener readerListener) {
		LOGGER.info("Set listener ...");
		textPane.setListener(readerListener);
	}

	/**
	 * Pause/Unpause speed reader.
	 * 
	 */
	public void pauseReading() {
		textPane.pauseReading();
	}

	/**
	 * Stop speed reader.
	 * 
	 */
	public void stopReading() {
		if (textTimerTask != null) {
			timer.cancel();
			textTimerTask.cancel();
			timer = null;
			textTimerTask = null;
		}
		isReading = false;
		textPane.stopReading();
	}

	/**
	 * Start a speed reader with word per minute value.
	 * 
	 * @param speedWpm
	 */
	public void startReading(int speedWpm) {
		if (!isReading && rtm != null) {
			this.speedWpm = speedWpm;
			LOGGER.info("Start reading ... {} wpm", speedWpm);
			textPane.startReading(speedWpm);
			startTimer(speedWpm);
		}
	}

	public void startReadingAt(int speedWpm) {
		if (!isReading && rtm != null) {
			this.speedWpm = speedWpm;
			LOGGER.info("Start reading at ... {} wpm", speedWpm);
			textPane.startReadingAt(speedWpm);
			startTimer(speedWpm);
		}
	}

	/**
	 * Check of speed reader completes the page.
	 * 
	 * @return
	 */
	public boolean isDoneReading() {
		return textPane.isDoneReading();
	}

	/**
	 * Skip/unskip articles by speed reader.
	 * 
	 * @param skipArticle
	 */
	public void skipArticle(boolean skipArticle) {
	}

	/**
	 * Read current word.
	 */
	public void readWord() {
		textPane.readWord();
		displayingWordLabel.setText(textPane.getWordToRead());
		repaint();
	}

	/**
	 * Display text with new reading text manager.
	 * 
	 * @param rtm
	 */
	public void displayText(ReadingTextManager rtm) {
		this.rtm = rtm;
		if (rtm != null) {
			textPane.displayText(rtm);
			updateInfoLabel();
		}
	}

	/**
	 * Search text on page, on the entire book.
	 * 
	 * @param searchText
	 * @return
	 */
	public String search(String searchText) {
		this.searchText = searchText;
		StringBuilder searchResult = new StringBuilder("Search result: <br>Current Page #: ");
		searchResult.append(bookHandler.getCurrentPage().getPageNumber());
		searchResult.append("<br>");
		searchResult.append(textPane.search(searchText));
		searchResult.append(searchBook(searchText));
		return searchResult.toString();
	}

	public void previousPage() {
		stopReading();
		Page page = bookHandler.previousPage();
		if (page != null) {
			displayPageText(page);
		}
	}

	public void nextPage() {
		stopReading();
		Page page = bookHandler.nextPage();
		if (page != null) {
			displayPageText(page);
		}
	}

	/**
	 * Start Timer task.
	 * 
	 * @param speedWpm
	 */
	private void startTimer(int speedWpm) {
		textTimerTask = new TextTimerTask();
		textTimerTask.register(this);
		timer = new Timer();
		timer.schedule(textTimerTask, 0, (60 * 1000) / speedWpm);
		isReading = true;
	}

	private void arrangeFixedComponents() {
		displayingWordLabel = new JLabel();
		displayingWordLabel.setFont(new Font(htmlFontName, Font.PLAIN, displayingWordFontSize));
		displayingWordLabel.setHorizontalAlignment(JLabel.CENTER);

		infoLabel = new JLabel();
		infoLabel.setFont(new Font("Candara", Font.PLAIN, 14));
		infoLabel.setForeground(Color.BLUE);
		textPane = new ContentTextPane(rtm);
	}

	private void arrangeLayout() {
		setLayout(new BorderLayout());
		add(new JScrollPane(textPane), BorderLayout.CENTER);
		add(displayingWordLabel, BorderLayout.NORTH);
		add(infoLabel, BorderLayout.SOUTH);
	}

	private void setBorder() {
		Border innerBorder = BorderFactory.createTitledBorder("Reader");
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));
	}

	private void updateInfoLabel() {
		int mins2Read = rtm.getTotalWords() / speedWpm + 1;
		String info = String.format("File: %s - Page: %d of %d - Words: %d (< %d minutes)", bookHandler.getBookName(),
				bookHandler.getCurrentPage().getPageNumber(), bookHandler.getPageCount(), rtm.getTotalWords(),
				mins2Read);
		infoLabel.setText(info);
	}

	private String searchBook(String searchText) {
		StringBuilder sb = new StringBuilder("<br><b>Book Result</b>: <br>");
		int curPageNum = bookHandler.getCurrentPageNumber();

		for (int pageNumber = 1; pageNumber <= bookHandler.getPageCount(); pageNumber++) {
			bookHandler.setPageNo(pageNumber);
			ReadingTextManager rtm = bookHandler.getCurrentPage().getRtm();
			List<Integer> indices = new ArrayList<Integer>();

			for (String searchWord : searchText.toLowerCase().split(" ")) {
				for (int index = 0; index < rtm.getTotalWords(); index++) {
					if (rtm.getWords().get(index).getOriginalWord().toLowerCase().contains(searchWord)) {
						indices.add(index);
					}
				}
			}
			if (!indices.isEmpty()) {
				sb.append("<br>Page #: ").append(pageNumber).append("<br>");
				for (Integer index : indices) {
					sb.append(rtm.getWords().get(index).getOriginalWord()).append(' ');
				}
			}
		}

		bookHandler.setPageNo(curPageNum);
		return sb.toString();
	}

	private void displayPageText(Page page) {
		rtm = page.getRtm();

		readingText = rtm.getReadingText();

		if (emptyReadingText()) {
			textPane.setText("*** PAGE IS EMPTY! ***");
		} else {
			displayText(rtm);
		}

		if (!Strings.isNullOrEmpty(searchText)) {
			search(searchText);
		}

		updateInfoLabel();
		repaint();
	}

	private boolean emptyReadingText() {
		for (int idx = 0; idx < readingText.length(); idx++) {
			if (Character.isAlphabetic(readingText.charAt(idx)) || Character.isDigit(readingText.charAt(idx))) {
				return false;
			}
		}
		return true;
	}

	public void goTo(int gotoPageNo) {
		bookHandler.setPageNo(gotoPageNo);
		displayPageText(bookHandler.getCurrentPage());
	}

	public String getHighlightedText() {
		return textPane.getHighlightedText();
	}

	public String getWordAtCaret() {
		return textPane.getWordAtCaret();
	}

}
