package com.drkiet.ireader.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.dictionary.DictionaryFrame;
import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.main.LoggingPanel;
import com.drkiet.ireader.util.FileHelper;
import com.drkiet.ireader.util.WebHelper;
import com.drkiettran.text.util.CommonUtils;

public class DictionaryHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryHandler.class);
	private DictionaryFrame dictionaryFrame;
	private LoggingPanel loggingPanel;
	private String[] topics;
	private UrlHandler urlHandler;

	public DictionaryHandler(LoggingPanel loggingPanel) {
		this.loggingPanel = loggingPanel;
		dictionaryFrame = new DictionaryFrame();

		if (FileHelper.getTopics() != null) {
			topics = FileHelper.getTopics().split(" ");
		} else {
			topics = new String[] { "" };
		}

		for (String topic : topics) {
			if (topic.equals("cs")) {
				loadWebopedia();
			}
		}

		dictionaryFrame.setReaderListener((Command cmd) -> {
			switch (cmd) {
			case GOTO_URL:
				urlHandler.setUrl(dictionaryFrame.getClickedUrl());
				break;
			default:
				break;
			}
		});
	}

	private HashMap<String, Integer> hash = new HashMap<String, Integer>();
	private List<String> csTerms = new ArrayList<String>();
	
	private void loadWebopedia() {
 
		try (BufferedReader br = new BufferedReader(new FileReader(FileHelper.getWebopediaFileName()))) {
			String line;
			int lineIdx = 0;
			Character lastFirstChar = 'a';
			
			while ((line = br.readLine()) != null) {
				int index = line.indexOf(": ");
				String term = line.substring(0, index);
				csTerms.add(line);
				Character firstChar = Character.toLowerCase(term.charAt(0));
				hash.put(term.toLowerCase(), lineIdx);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("*** ERROR *** {}", e);
		} catch (IOException e) {
			LOGGER.error("*** ERROR *** {}", e);
		}
	}

	public void getDefinition(String term) {
		StringBuilder sb = new StringBuilder();
		getRegularDefinition(sb, term);

		for (String topic : topics) {
			switch (topic.toLowerCase()) {
			case "acc":
				getAccountingDefinition(sb, term);
				break;
			case "cs":
				getCSDefinition(sb, term);
				break;
			case "fin":
				getFinancialDefinition(sb, term);
				break;
			default:
				break;
			}
		}
		dictionaryFrame.displayDefinition(sb.toString());
	}

	private void getFinancialDefinition(StringBuilder sb, String term) {
		LOGGER.info("Get accounting definition {}", term);
		sb.append("<br>~~~ <b><u>Financial</u></b> ~~~<br>");
		sb.append(WebHelper.getDefinitionForWordInFinance(term));
		String[] singleTerms = term.split(" ");

		if (singleTerms.length > 1) {
			sb.append("<br>~~~ Financial ~~~<br>");
			for (String singleTerm : singleTerms) {
				sb.append(WebHelper.getDefinitionForWordInFinance(singleTerm)).append("<br><br>");
			}
		}
	}

	private void getCSDefinition(StringBuilder sb, String term) {
		LOGGER.info("Get accounting definition {}", term);
		sb.append("<br>~~~ <b><u>Computer Science</u></b> ~~~<br>");
		sb.append(WebHelper.getDefinitionForWordInCS(term));
		String[] singleTerms = term.split(" ");

		if (singleTerms.length > 1) {
			sb.append("<br>~~~ Computer Science ~~~<br>");
			for (String singleTerm : singleTerms) {
				sb.append(WebHelper.getDefinitionForWordInCS(singleTerm)).append("<br><br>");
			}
		}
	}

	private void getAccountingDefinition(StringBuilder sb, String term) {
		LOGGER.info("Get accounting definition {}", term);
		sb.append("<br><br>~~~ <b><u>Accounting</u></b> ~~~<br>");
		sb.append(WebHelper.getAccDefinitionForWord(term));
		String[] singleTerms = term.split(" ");

		if (singleTerms.length > 1) {
			sb.append("<br>~~~ Accounting ~~~<br>");
			for (String singleTerm : singleTerms) {
				sb.append(WebHelper.getAccDefinitionForWord(singleTerm)).append("<br><br>");
			}
		}

	}

	public void getRegularDefinition(StringBuilder sb, String term) {
		LOGGER.info("Get regular definition {}", term);
		sb.append(WebHelper.getDefinitionForWord(term));
		String[] singleTerms = term.split(" ");

		if (singleTerms.length > 1) {
			sb.append("<br>~~~ Regular ~~~<br>");
			for (String singleTerm : singleTerms) {
				sb.append(CommonUtils.getDefinitionForWord(singleTerm)).append("<br><br>");
			}
		}
	}

	public void setUrlHandler(UrlHandler urlHandler) {
		this.urlHandler = urlHandler;
	}

}
