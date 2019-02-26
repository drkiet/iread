package com.drkiet.ireader.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.dictionary.DictionaryFrame;
import com.drkiet.ireader.main.LoggingPanel;
import com.drkiet.ireader.util.WebHelper;
import com.drkiettran.text.util.CommonUtils;

public class DictionaryHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryHandler.class);
	private DictionaryFrame dictionaryFrame;
	private LoggingPanel loggingPanel;
	private String[] topics;

	public DictionaryHandler(LoggingPanel loggingPanel) {
		this.loggingPanel = loggingPanel;
		dictionaryFrame = new DictionaryFrame();
		if (getTopics() != null) {
			topics = getTopics().split(" ");
		} else {
			topics = new String[] { "" };
		}
	}

	public void getDefinition(String term) {
		LOGGER.info("Get definition {}", term);
		StringBuilder sb = new StringBuilder(WebHelper.getDefinitionForWord(term));
		String[] singleTerms = term.split(" ");

		if (singleTerms.length > 1) {
			sb.append("<br>~~~<br><br>");
			for (String singleTerm : singleTerms) {
				sb.append(CommonUtils.getDefinitionForWord(singleTerm)).append("<br><br>");
			}
		}

		for (String topic : topics) {
			switch (topic.toLowerCase()) {
			case "acc":
				sb.append(WebHelper.getAccDefinitionForWord(term));
				break;
			case "cs":
				sb.append("<br><br>~~~<br>");
				sb.append(WebHelper.getDefinitionForWordInCS(term));
				break;
			case "fin":
				sb.append("<br><br>~~~<br>");
				sb.append(WebHelper.getDefinitionForWordInFinance(term));
				break;
			default:
				break;
			}
		}
		dictionaryFrame.displayDefinition(sb.toString());
	}

	private String getTopics() {
		return System.getProperty("ireader.topics");
	}

}
