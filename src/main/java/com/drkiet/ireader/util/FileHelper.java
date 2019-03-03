package com.drkiet.ireader.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

import com.drkiettran.text.TextApp;

public class FileHelper {
	public static String loadTextFileIntoString(String fileName) {
		try (InputStream is = TextApp.class.getResourceAsStream(fileName)) {
			StringBuilder sb = new StringBuilder();

			for (;;) {
				int c = is.read();
				if (c < 0) {
					break;
				}
				sb.append((char) c);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String loadFaqsFile() {

		try (InputStream is = TextApp.class.getResourceAsStream("/Faqs.md")) {
			Reader in = new InputStreamReader(is);
			Writer out = new StringWriter();
			Markdown md = new Markdown();
			try {
				md.transform(in, out);
			} catch (ParseException e) {
				e.printStackTrace();
				return "*** ERROR ***";
			}
			return out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

	public static List<String> getFileNames(String folderName) {
		LOGGER.info("accreader.folder: {}", folderName);
		File folder = new File(folderName);

		if (!folder.isDirectory()) {
			return null;
		}

		return getFileNames(folder);

	}

	private static List<String> getFileNames(File file) {
		LOGGER.info("get file names: {}", file.getName());
		List<String> fileNames = new ArrayList<String>();
		if (!file.isDirectory()) {
			fileNames.add(file.getName());
			return fileNames;
		}

		for (File f : file.listFiles()) {
			fileNames.addAll(getFileNames(f));
		}

		return fileNames;
	}

	public static List<String> getTermsFromHtml() {
		List<String> definedTerms = new ArrayList<String>();

		String html = loadTextFileIntoString("/terms.html");
		Document doc = Jsoup.parse(html);
		List<Element> nodes = doc.getAllElements();
		for (int idx = 0; idx < nodes.size(); idx++) {
			if ("h3".equalsIgnoreCase(nodes.get(idx).tagName()) && !nodes.get(idx).text().trim().isEmpty()) {
				String term = nodes.get(idx).text();
				String definition = "*** UNKNOWN ***";

				while (idx < nodes.size()) {
					if ("p".equalsIgnoreCase(nodes.get(idx).tagName())) {
						definition = nodes.get(idx).text();
						break;
					}
					idx++;
				}

				LOGGER.info("*** {}: {}", term, definition);
				definedTerms.add(String.format("*** %s: %s", term, definition));
			}

		}
		return definedTerms;
	}

	public static String getReferenceFolder() {
		return System.getProperty("ireader.reference");
	}

	public static String getWebopediaFileName() {
		return System.getProperty("ireader.webopedia");
	}
	
	public static String getTopics() {
		return System.getProperty("ireader.topics");
	}

	public static String getFQFileName(String bookName) {
		return String.format("%s%s%s", getReferenceFolder(), File.separator, bookName);
	}

	public static String getFQRefencesFileName(String refName) {
		return getFQFileName(refName);
	}

	public static String getContentFolder() {
		return System.getProperty("ireader.content");
	}

	public static String getWorkspaceFolder() {
		return System.getProperty("ireader.index");
	}

	public static HashMap<String, String> loadDictionary(String path, String fileName) throws IOException {
		HashMap<String, String> dict = new HashMap<String, String>();
		List<String> lines = Files.readAllLines(Paths.get(path, fileName), Charset.forName("ISO-8859-1"));
		int count = 1;

		for (String line : lines) {
			if (line.trim().isEmpty()) {
				continue;
			}

			int index = line.indexOf(":");
			String key = line.substring(0, index).toLowerCase();
			String value = line.substring(index + 1).trim();
			if (dict.get(key) != null) {
				LOGGER.info("*** COLLISION ON {} ***", key);
				key += count++;
			}
			dict.put(key, value);
		}

		return dict;
	}

	public static String getFQContentFileName(String bookName) {
		return String.format("%s%s%s", getContentFolder(), File.separator, bookName);
	}
}
