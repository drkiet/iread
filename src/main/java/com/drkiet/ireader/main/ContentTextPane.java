package com.drkiet.ireader.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Highlighter.HighlightPainter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.handler.ReaderListener;
import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.util.FileHelper;
import com.drkiettran.text.ReadingTextManager;
import com.drkiettran.text.model.SearchResult;
import com.drkiettran.text.model.Word;

/**
 * This class is responsible for the following behaviors:
 * 
 * <code>
 * 1. Displaying a page of text on screen.
 * 2. Allow speed reading through the displaying text.
 *    - Start, Stop, and Pause.
 * </code>
 * 
 * @author ktran
 *
 */
public class ContentTextPane extends JTextPane {
	private static final long serialVersionUID = -9146996627216852346L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentTextPane.class);
	public static final String TIP_TEXT = "<html><p><font color=\"%s\" size=\"%d\" face=\"%s\">%d '%s's found on page"
			+ "</font></p></html>";

	private int clickedWordPositionIdx = 0;
	private String highlightedText;
	private int htmlFontSize = 5;
	private String wordAtMousePos;
	private String htmlFontName = "Candara";
	private ReadingTextManager rtm;
	private List<Integer[]> wordPositionsList;
	private int curReadingIdx;
	private boolean isReading = false;
	private Integer curReadingCaret;
	private boolean disableTooltip = false;
	private boolean pauseReading;
	private String wordToRead;
	private int leftIdxForWordAtCaret;
	private int rightIdxForWordAtCaret;
	private String wordAtCaret;

	List<Object> highlightedWords = new ArrayList<Object>();
	private ReaderListener listener;

	/**
	 * Construct a ContentTextPane object.
	 * 
	 * @param rtm
	 */
	public ContentTextPane(ReadingTextManager rtm) {
		super();
		this.rtm = rtm;
		setCaretPosition(0);
		setCaretColor(Color.WHITE);
		setContentType("text/html");
		setText(FileHelper.loadTextFileIntoString("/About.html"));
		MouseProcessor mouseProcessor = new MouseProcessor();
		addMouseListener(mouseProcessor);
		addMouseMotionListener(mouseProcessor);
	}

	public void setListener(ReaderListener readerListener) {
		this.listener = readerListener;
	}

	public String getHighlightedText() {
		return highlightedText;
	}

	public String getWordAtCaret() {
		return wordAtCaret;
	}

	public void startReading(int speedWpm) {
		if (!isReading) {
			LOGGER.info("Start reading ... {} wpm", speedWpm);
			startReadingAt(speedWpm, 0);
		}
	}

	public void startReadingAt(int speedWpm) {
		if (!isReading) {
			LOGGER.info("Start reading at ... {} wpm", speedWpm);
			startReadingAt(speedWpm, clickedWordPositionIdx);
		}
	}

	public void stopReading() {
		isReading = false;
		disableTooltip = false;
	}

	public void pauseReading() {
		pauseReading = !pauseReading;
	}

	private void startReadingAt(int speedWpm, int startReadingIdx) {
		disableTooltip = true;
		LOGGER.info("Start reading at word position {} with {} wpm", startReadingIdx, speedWpm);
		setCaret(new FancyCaret());
		curReadingIdx = startReadingIdx;
		isReading = true;
		disableTooltip = true;
	}

	public String getWordToRead() {
		return wordToRead;
	}

	/**
	 * Displaying text on screen
	 * 
	 * @param rtm
	 */
	public void displayText(ReadingTextManager rtm) {
		this.rtm = rtm;
		Arrays.asList(rtm.getReadingText().split("\n"));
		StringBuilder sb = new StringBuilder(
				String.format("<font size=\"%d\" face=\"%s\">", htmlFontSize, htmlFontName));
		sb.append(rtm.getReadingText().replaceAll("\n", "<br>"));
		sb.append("</font>");
		setText(sb.toString());

		try {
			identifyWordPositions();
		} catch (BadLocationException e) {
			LOGGER.error("*** ERROR ***:\n{}", e);
		} catch (Exception e) {
			LOGGER.error("*** ERROR ***:\n{}", e);
		}
		LOGGER.info("displaying text: \n{}", getText());
	}

	public void readWord() {
		if (pauseReading) {
			return;
		}

		wordToRead = getWord();
		LOGGER.info("wordtoread: {}", wordToRead);

		if (wordToRead == null || wordToRead.isEmpty()) {
			isReading = false;
			return;
		}

		setCaretPosition(curReadingCaret);
		unhighlightWords();
		highlightWords(wordPositionsList.get(curReadingIdx)[0],
				wordPositionsList.get(curReadingIdx)[0] + wordPositionsList.get(curReadingIdx)[1] - 1, Color.pink);
		curReadingIdx++;
		repaint();
	}

	private String getWord() {
		if (curReadingIdx < wordPositionsList.size()) {
			try {
				String nextWord = getText(wordPositionsList.get(curReadingIdx)[0],
						wordPositionsList.get(curReadingIdx)[1]);
				curReadingCaret = wordPositionsList.get(curReadingIdx)[0];
				return nextWord;
			} catch (BadLocationException e) {
				LOGGER.error("*** ERROR ***:\n{}", e);
			}
		}
		return null;
	}

	public boolean isDoneReading() {
		return !isReading;
	}

	public String search(String searchText) {
		List<Integer> indices = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();

		for (String searchWord : searchText.toLowerCase().split(" ")) {
			for (int index = 0; index < rtm.getTotalWords(); index++) {
				if (rtm.getWords().get(index).getOriginalWord().toLowerCase().contains(searchWord)) {
					indices.add(index);
				}
			}
		}

		unhighlightWords();
		disableTooltip = true;
		Collections.sort(indices);

		for (Integer index : indices) {
			Integer[] posLen = wordPositionsList.get(index);
			highlightWords(posLen[0], posLen[0] + posLen[1], Color.YELLOW);
			sb.append(rtm.getWords().get(index).getOriginalWord()).append(' ');
		}

		if (sb.length() == 0) {
			sb.append(searchText).append("*** NOT FOUND ***");
		}

		return sb.toString();
	}

	/**
	 * Locating starting point of each words on display.
	 * 
	 * @throws Exception
	 */
	private void identifyWordPositions() throws Exception {
		setCaretPosition(0);
		int idx = getCaretPosition();
		wordPositionsList = new ArrayList<Integer[]>();

		for (Word word : rtm.getWords()) {
			String wordText = word.getOriginalWord().replace("\n", "").replace("\r", "");
			while (idx < getText().length()) {
				if (getText(idx, wordText.length()).equals(wordText)) {
					wordPositionsList.add(new Integer[] { idx, wordText.length() });
					idx += (wordText.length());
					break;
				}
				idx++;
			}
		}
		if (wordPositionsList.size() != rtm.getTotalWords()) {
			throw new Exception("** NOT FOUND all words ***");
		}
		LOGGER.info("Found {} out of {} words", wordPositionsList.size(), rtm.getTotalWords());
	}

	private String getWordAtCaretPosition() {
		int caret = getCaretPosition();

		Character ch = null;

		for (leftIdxForWordAtCaret = caret; leftIdxForWordAtCaret > 0; leftIdxForWordAtCaret--) {
			ch = getCharAt(leftIdxForWordAtCaret);
			if (ch == '-' || Character.isAlphabetic(ch)) {
				continue;
			}
			break;
		}

		if (!Character.isAlphabetic(ch)) {
			leftIdxForWordAtCaret++;
		}

		for (rightIdxForWordAtCaret = caret; rightIdxForWordAtCaret < getText().length(); rightIdxForWordAtCaret++) {
			ch = getCharAt(rightIdxForWordAtCaret);
			if (ch == '-' || Character.isAlphabetic(ch)) {
				continue;
			}
			break;
		}

		String wordAtCaret = getString(leftIdxForWordAtCaret, rightIdxForWordAtCaret);

		if (wordAtCaret != null) {
			wordAtCaret.trim();
		}

		return wordAtCaret;
	}

	private Character getCharAt(int offset) {
		try {
			return getText(offset, 1).charAt(0);
		} catch (BadLocationException e) {
			LOGGER.error("*** ERROR ***:\n{}", e);
		}
		return null;
	}

	/**
	 * Get a text string between two positions.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	private String getString(int left, int right) {
		if (left < right) {
			try {
				return getText(left, right - left);
			} catch (BadLocationException e) {
				LOGGER.error("*** ERROR ***:\n{}", e);
			}
		}
		return null;
	}

	private void unhighlightWords() {
		Highlighter hl = getHighlighter();
		new DefaultHighlighter.DefaultHighlightPainter(null);

		for (Object highlightedWord : highlightedWords) {
			hl.removeHighlight(highlightedWord);
		}
	}

	private void highlightWords(int p0, int p1, Color hiliteColor) {
		Highlighter hl = getHighlighter();
		HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(hiliteColor);

		try {
			highlightedWords.add(hl.addHighlight(p0, p1, painter));
		} catch (BadLocationException e) {
			LOGGER.error("*** ERROR ***:\n{}", e);
		}
	}

	/**
	 * Processing the tool tip for mouse movement
	 * 
	 * @param event
	 */
	private void processToolTip(MouseEvent event) {
		if (disableTooltip) {
			return;
		}

		if (rtm != null) {
			int viewToModel = viewToModel(event.getPoint());

			if (viewToModel != -1) {
				setCaretPosition(viewToModel(event.getPoint()));
				wordAtMousePos = getWordAtCaretPosition();

				if (wordAtMousePos != null) {
					SearchResult sr = rtm.search(wordAtMousePos);
					String tip = String.format(TIP_TEXT, "BLACK", 5, "Candara", sr.getNumberMatchedWords(),
							wordAtMousePos);
					setToolTipText(tip);
					unhighlightWords();
					for (Integer[] posLen : wordPositionsList) {
						try {
							if (getText(posLen[0], posLen[1] - 1).toLowerCase()
									.contains(wordAtMousePos.toLowerCase())) {
								highlightWords(posLen[0], posLen[0] + posLen[1] - 1, Color.green);
							}
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					repaint();
				}
			}
		}

	}

	/**
	 * Processing mouse event internal class. Keep the main code clean.
	 * 
	 * @author ktran
	 *
	 */
	class MouseProcessor implements MouseListener, MouseMotionListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			wordAtCaret = getWordAtCaretPosition();
			if (wordAtCaret != null) {
				highlightedText = null;
				clickedWordPositionIdx = getClickedWordPositionIdx();
				unhighlightWords();
				highlightWords(leftIdxForWordAtCaret, rightIdxForWordAtCaret, Color.green);
				disableTooltip = true;
				LOGGER.info("carrot = {}; word@caret: {} ", getCaretPosition(), wordAtCaret);
			} else {
				disableTooltip = false;
			}
		}

		private int getClickedWordPositionIdx() {
			int prevPos = 0;

			for (int idx = 0; idx < wordPositionsList.size(); idx++) {
				Integer[] posLen = wordPositionsList.get(idx);
				if (getCaretPosition() > prevPos && getCaretPosition() < posLen[0]) {
					return idx;
				}
				prevPos = posLen[0];
			}

			return wordPositionsList.size() - 1;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (getSelectedText() != null) {
				for (Highlight hilite : getHighlighter().getHighlights()) {
					LOGGER.info("highlighted: {}, {}", hilite.getStartOffset(), hilite.getEndOffset());
					unhighlightWords();
					highlightWords(hilite.getStartOffset(), hilite.getEndOffset(), Color.green);
				}
				highlightedText = getSelectedText().trim();
				wordAtCaret = null;
				disableTooltip = true;
				LOGGER.info("highlighted text: {}", highlightedText);
			} else {
				disableTooltip = false;
			}

			if (e.isPopupTrigger()) {
				doPopup(e);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			processToolTip(e);
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

}
