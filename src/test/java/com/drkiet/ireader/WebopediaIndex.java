package com.drkiet.ireader;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebopediaIndex {
	public static final Logger LOGGER = LoggerFactory.getLogger(WebopediaIndex.class);
	public static final String CS_INDEX_FOLDER = "D:/Teaching/2019/HarrisburgU/cs-idx/";
	public static final String WEBOPEDIA_INDEX_FILENAME = "webopedia.idx";
	public static final String WEBOPEDIA_MAIN_URL = "https://www.webopedia.com";
	public static final String TERMS_XPATH = "//a[contains(@href,'TERM/%s')]";

	@Test
	public void gettingIndex() {
		WebDriver driver = new HtmlUnitDriver();
		List<String> terms = new ArrayList<String>();

		for (char c = 'A'; c <= 'Z'; c++) {
			StringBuilder sb = new StringBuilder(WEBOPEDIA_MAIN_URL).append("/TERM/").append(c);
			driver.get(sb.toString());
			List<WebElement> weTerms = driver.findElements(By.xpath(String.format(TERMS_XPATH, String.valueOf(c))));

			for (WebElement weTerm : weTerms) {
				String href = weTerm.getAttribute("href");
				if (href.toLowerCase().contains("footer")) {
					break;
				}
				terms.add(makeEntry(weTerm.getText(), href));
			}

		}

		for (int i = 0; i <= 9; i++) {
			StringBuilder sb = new StringBuilder(WEBOPEDIA_MAIN_URL).append("/TERM/").append(i);
			driver.get(sb.toString());
			List<WebElement> weTerms = driver.findElements(By.xpath(String.format(TERMS_XPATH, String.valueOf(i))));

			for (WebElement weTerm : weTerms) {
				String href = weTerm.getAttribute("href");
				if (href.toLowerCase().contains("footer")) {
					break;
				}
				terms.add(makeEntry(weTerm.getText(), href));
			}
		}

		LOGGER.info("{} terms as shown here:\n", terms.size());
		for (String term : terms) {
			LOGGER.info("{}", term);
		}

		Collections.sort(terms);
		writeCSIndexFile(this.CS_INDEX_FOLDER + this.WEBOPEDIA_INDEX_FILENAME, terms);
	}

	private String makeEntry(String text, String href) {
		StringBuilder sb = new StringBuilder(text).append(": ").append(href);
		return sb.toString();
	}

	private void writeCSIndexFile(String fileName, List<String> lines) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			for (String line : lines) {
				bw.write(line);
				bw.write("\n");
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("*** ERROR *** {}", e);
		} catch (IOException e) {
			LOGGER.error("*** ERROR *** {}", e);
		}
	}
}
