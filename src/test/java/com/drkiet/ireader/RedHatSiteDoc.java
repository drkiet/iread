package com.drkiet.ireader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class RedHatSiteDoc {
	public static final String DOC_SITE = "https://docs.openshift.com/container-platform/3.11/welcome/index.html";
	public static final String OUTPUT_FILE = "C:/tmp/rh-os-311.txt";

	private BufferedWriter bw = null;

	@Before
	public void setup() throws IOException {
		File output = new File(OUTPUT_FILE);
		if (output.exists()) {
			output.delete();
		}

		bw = new BufferedWriter(new FileWriter(OUTPUT_FILE));
	}

	@After
	public void teardown() throws IOException {
		if (bw != null) {
			bw.close();
		}
	}

	private void write(String text) {

		try {
			bw.write(text);
			bw.write('\n');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void getDoc() {
		WebDriver driver = new HtmlUnitDriver();
		driver.get(DOC_SITE);
		List<WebElement> links = driver.findElements(By.xpath("//ul[contains(@class,'nav-sidebar')]//a"));
		List<String> hrefs = new ArrayList<String>();

		links.stream().filter(link -> !link.getAttribute("href").contains("javascript")).forEach(link -> {
			hrefs.add(link.getAttribute("href"));
		});

		write("~~~ Links = " + hrefs.size());
		hrefs.stream().forEach(href -> {
			System.out.println("href: " + href);
			write("~~~ LINK: " + href);
			driver.get(href);
			List<WebElement> divs = driver.findElements(By.xpath("//div[contains(@class,'main')]//div"));
			for (WebElement div : divs) {
//				System.out.println(div.getText());
				write(div.getText());
			}
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}
}
