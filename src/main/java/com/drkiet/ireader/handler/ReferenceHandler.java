package com.drkiet.ireader.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.main.LoggingPanel;
import com.drkiet.ireader.reference.ReferencesFrame;
import com.drkiet.ireader.reference.page.ReferencePageFrame;
import com.drkiet.ireader.util.FileHelper;
import com.drkiet.search.DocumentSearch;

public class ReferenceHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceHandler.class);

	private DocumentSearch ds;
	private ReferencesFrame referencesFrame = null;
	private ReferencePageFrame referencePageFrame = null;
	private BookHandler bookHandler;
	private LoggingPanel loggingPanel;
	private List<Integer> pageNumbersList;
	private String searchText;

	private HashMap<Integer, Integer> pageNumbersMap;

	private DictionaryHandler dictionaryHandler;

	public ReferenceHandler(LoggingPanel loggingPanel) {
		this.loggingPanel = loggingPanel;
	}

	public void openSelectedReference(String selectedReference) {
		ds = DocumentSearch.getInstance(FileHelper.getFQRefencesFileName(selectedReference),
				FileHelper.getWorkspaceFolder());

		if (referencesFrame != null) {
			referencesFrame.dispose();
		}

		if (referencePageFrame != null) {
			referencePageFrame.dispose();
		}

		bookHandler = new BookHandler();
		referencesFrame = new ReferencesFrame(selectedReference);
		referencePageFrame = new ReferencePageFrame(selectedReference);
		referencesFrame.setListener(new ReferenceListener());
		referencePageFrame.setListener(new ReferencePageListener());

		bookHandler.openSelectedBook(selectedReference);
	}

	public int getIndexedWordCount() {
		return ds.getWordsInDocument().size();
	}

	public void searchText(String searchText) {
		LOGGER.info("Searching ... {}", searchText);
		this.searchText = searchText;
		referencesFrame.setText(search(searchText));
		if (!pageNumbersList.isEmpty()) {
			referencePageFrame.setText(makeRefPage(pageNumbersList.get(0)));
		}
	}

	private String search(String searchText) {
		pageNumbersMap = searchReferences(searchText);
		pageNumbersList = new ArrayList<Integer>();
		pageNumbersList.addAll(pageNumbersMap.keySet());

		Collections.sort(pageNumbersList);
		StringBuilder sb = new StringBuilder("<b>").append(searchText);

		if (pageNumbersList.isEmpty()) {
			sb.append(":</b><br>*** no reference ***");
		} else {
			sb.append("</b> is founded on these pages: <br>");
		}

		for (Integer pageNumber : pageNumbersList) {

			if (pageNumbersMap.get(pageNumber) > 1) {
				boolean exactMatchFound = exactMatchFound(searchText, pageNumbersMap, pageNumber);

				if (exactMatchFound) {
					sb.append("<b>");
				}

				sb.append(pageNumber);

				if (exactMatchFound) {
					sb.append("<sup>").append(pageNumbersMap.get(pageNumber)).append("</sup>");
				} else {
					sb.append("<sub>").append(pageNumbersMap.get(pageNumber)).append("</sub>");
				}

				if (exactMatchFound) {
					sb.append("</b>");
				}
			} else {
				sb.append(pageNumber);
			}
			sb.append(", ");
		}
		LOGGER.info("text: {}", sb);
		return sb.toString();
	}

	private boolean exactMatchFound(String searchText, HashMap<Integer, Integer> pageNumbersMap, Integer pageNumber) {
		bookHandler.setPageNo(pageNumber);
		return bookHandler.getCurrentPage().getRtm().getText().toLowerCase().contains(searchText.toLowerCase());
	}

	private HashMap<Integer, Integer> searchReferences(String searchText) {
		LOGGER.info("Searching references ... {}", searchText);
		String[] searchWords = searchText.split(" ");
		HashMap<Integer, Integer> foundPageNumbers = new HashMap<Integer, Integer>();

		for (String searchWord : searchWords) {
			Iterator<Integer> pageNumbers = ds.search(searchWord);
			while (pageNumbers.hasNext()) {
				Integer pageNumber = pageNumbers.next();
				Integer count = foundPageNumbers.get(pageNumber);
				count = count == null ? 1 : count + 1;
				foundPageNumbers.put(pageNumber, count);
				LOGGER.info("{}:{}", pageNumber, count);
			}
		}
		loggingPanel.info(String.format("%s found in %s pages of reference book.", searchText, foundPageNumbers.size()));
		return foundPageNumbers;
	}

	private String makeRefPage(int pageNumber) {
		bookHandler.setPageNo(pageNumber);
		StringBuilder sb = new StringBuilder("<b>Page ").append(pageNumber).append("</b>:<br>");
		sb.append("<p>").append(getHighlightedPage()).append("</p>");
		return sb.toString();
	}

	private String getHighlightedPage() {
		String highlightedPage = bookHandler.getCurrentPage().getRtm().getText().replaceAll("\n", "<br>");

		String[] highlightedWords = searchText.split(" ");

		for (String highlightedWord : highlightedWords) {
			List<Integer> startHighlightedLocs = getStartHighlightedLocs(highlightedPage, highlightedWord);
			highlightedPage = highlightThePage(highlightedPage, startHighlightedLocs, highlightedWord.length());
			highlightedPage = highlightedPage.replaceAll(highlightedWord,
					String.format("<b><u>%s</u></b>", highlightedWord));
		}
//		LOGGER.info("highlightedPage: {}", highlightedPage);
		return highlightedPage;
	}

	private List<Integer> getStartHighlightedLocs(String highlightedPage, String highlightedWord) {
		List<Integer> startHighlightedLocs = new ArrayList<Integer>();
		String lowercasePage = highlightedPage.toLowerCase();
		String lowercaseWord = highlightedWord.toLowerCase();
		int startIndex = 0;
		while (startIndex >= 0) {
			startIndex = lowercasePage.indexOf(lowercaseWord, startIndex);
			if (startIndex >= 0) {
				startHighlightedLocs.add(startIndex);
				startIndex += highlightedWord.length();
			}
		}
		return startHighlightedLocs;
	}

	private String highlightThePage(String highlightedPage, List<Integer> startHighlightedLocs, int length) {
		StringBuilder sb = new StringBuilder();
		int startIdx = 0;

		for (Integer startHighlightedIndex : startHighlightedLocs) {
			sb.append(highlightedPage.substring(startIdx, startHighlightedIndex));
			sb.append("<b><u>");
			sb.append(highlightedPage.substring(startHighlightedIndex, startHighlightedIndex + length));
			sb.append("</u></b>");
			startIdx = startHighlightedIndex + length;
		}

		if (startIdx < highlightedPage.length()) {
			sb.append(highlightedPage.substring(startIdx));
		}
		return sb.toString();
	}

	class ReferenceListener implements ReaderListener {
		@Override
		public void invoke(Command cmd) {
			switch (cmd) {
			case GOTO_PAGE:
				if (!pageNumbersList.isEmpty()) {
					referencePageFrame.setText(makeRefPage(referencesFrame.getSelectedPageNumber()));
				}
				break;
			}
		}

	}

	class ReferencePageListener implements ReaderListener {
		@Override
		public void invoke(Command cmd) {
			switch (cmd) {
			case NEXT_PAGE:
				bookHandler.nextPage();
				referencePageFrame.setText(makeRefPage(bookHandler.getCurrentPageNumber()));
				break;

			case PREVIOUS_PAGE:
				bookHandler.previousPage();
				referencePageFrame.setText(makeRefPage(bookHandler.getCurrentPageNumber()));
				break;

			case NEXT_FIND:
				nextFindPage();
				break;

			case PREVIOUS_FIND:
				previousFindPage();
				break;

			case NEXT_EXACT_MATCH:
				nextExactMatchPage();
				break;

			case PREVIOUS_EXACT_MATCH:
				previousExactMatchPage();
				break;
			case POPUP_SEARCH:
				if (referencePageFrame.getHighlightedText() != null) {
					searchText(referencePageFrame.getHighlightedText());
				}
				break;
			case GET_DEFINITION:
				if (referencePageFrame.getHighlightedText() != null) {
					dictionaryHandler.getDefinition(referencePageFrame.getHighlightedText());
				}
				break;
			default:
				break;
			}
		}

	}

	private void previousExactMatchPage() {
		Integer pageNumber = bookHandler.getCurrentPageNumber();
		Integer currentPageNumber = bookHandler.getCurrentPageNumber();

		for (int idx = pageNumbersList.size() - 1; idx >= 0; idx--) {
			if (pageNumbersList.get(idx) < currentPageNumber) {
				currentPageNumber = pageNumbersList.get(idx);
				if (exactMatchFound(searchText, currentPageNumber)) {
					pageNumber = currentPageNumber;
					bookHandler.setPageNo(pageNumber);
					referencePageFrame.setText(makeRefPage(pageNumber));
					break;
				}
			}
		}
	}

	private void nextExactMatchPage() {
		Integer pageNumber = bookHandler.getCurrentPageNumber();
		Integer currentPageNumber = bookHandler.getCurrentPageNumber();

		for (int idx = 0; idx < pageNumbersList.size(); idx++) {
			if (pageNumbersList.get(idx) > currentPageNumber) {
				currentPageNumber = pageNumbersList.get(idx);
				if (exactMatchFound(searchText, currentPageNumber)) {
					pageNumber = currentPageNumber;
					bookHandler.setPageNo(pageNumber);
					referencePageFrame.setText(makeRefPage(pageNumber));
					break;
				}
			}
		}
	}

	public void previousFindPage() {
		Integer pageNumber = bookHandler.getCurrentPageNumber();
		for (int idx = pageNumbersList.size() - 1; idx >= 0; idx--) {
			if (pageNumbersList.get(idx) < pageNumber) {
				pageNumber = pageNumbersList.get(idx);
				break;
			}
		}
		bookHandler.setPageNo(pageNumber);
		referencePageFrame.setText(makeRefPage(pageNumber));
	}

	public void nextFindPage() {
		Integer pageNumber = bookHandler.getCurrentPageNumber();
		for (int idx = 0; idx < pageNumbersList.size(); idx++) {
			if (pageNumbersList.get(idx) > pageNumber) {
				pageNumber = pageNumbersList.get(idx);
				break;
			}
		}
		bookHandler.setPageNo(pageNumber);
		referencePageFrame.setText(makeRefPage(pageNumber));
	}

	private boolean exactMatchFound(String text, Integer pageNumber) {
		return pageNumbersMap.get(pageNumber) == text.split(" ").length && exactMatchFoundOnPage(pageNumber, text);
	}

	private boolean exactMatchFoundOnPage(Integer pageNumber, String searchText) {
		bookHandler.setPageNo(pageNumber);
		return bookHandler.getCurrentPage().getRtm().getText().toLowerCase().contains(searchText.toLowerCase());
	}

	public void setDictionaryHandler(DictionaryHandler dictionaryHandler) {
		this.dictionaryHandler = dictionaryHandler;
	}

}
