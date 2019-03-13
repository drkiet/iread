package com.drkiet.ireader.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.handler.DictionaryHandler;
import com.drkiet.ireader.handler.Navigator;
import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.handler.SettingsHandler;
import com.drkiet.ireader.handler.TextHandler;
import com.drkiet.ireader.handler.UrlHandler;
import com.drkiet.ireader.util.FileHelper;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -6071539847363274514L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MainFrame.class);

	private TextPanel textPanel = new TextPanel();
	private Toolbar toolbar = new Toolbar();
	private FormPanel formPanel = new FormPanel();
	private LoggingPanel loggingPanel = new LoggingPanel();
	private JMenuBar menubar = new JMenuBar();
	private SettingsHandler settingsHandler = null;
	private TextHandler textHandler = null;
	private Navigator navigator;
	private DictionaryHandler dictionaryHandler;
	private UrlHandler urlHandler;

	public MainFrame() throws IOException {
		super("Intelligent Reader");
		LOGGER.info("Intelligent Reader Program begins ...");
		manageMenubar();
		manageListeners();
		manageLayout();
		textPanel.setSpeed(formPanel.getSpeedWpm());
	}

	private void manageMenubar() {
		LOGGER.info("Managing Menubar");
		JMenu menu = new JMenu("File");
		JMenuItem menuItem = new JMenuItem("Exit");
		menuItem.addActionListener((event) -> System.exit(0));
		menu.add(menuItem);
		menubar.add(menu);

		menu = new JMenu("Help");
		menuItem = new JMenuItem("About");
		menuItem.addActionListener((event) -> displayAbout(event));
		menu.add(menuItem);
		menubar.add(menu);
		setJMenuBar(menubar);
	}

	public void manageListeners() {
		textHandler = new TextHandler(textPanel, loggingPanel);
		settingsHandler = new SettingsHandler(formPanel, loggingPanel);
		dictionaryHandler = new DictionaryHandler(loggingPanel);
		urlHandler = new UrlHandler(loggingPanel);
		navigator = new Navigator(textPanel, formPanel.getSpeedWpm());

		dictionaryHandler.setUrlHandler(urlHandler);
		formPanel.setReaderListener((Command cmd) -> {
			settingsHandler.processForm(cmd);
			processSettings(cmd);
		});

		textPanel.setReaderListener((Command cmd) -> {
			textHandler.processText(cmd);
			processText(cmd);
		});

		toolbar.setReaderListener((Command cmd) -> {
			navigator.processToolbar(cmd);
		});
	}

	private void processText(Command cmd) {
		switch (cmd) {
		case POPUP_SEARCH:
			if (textHandler.getSearchText() != null) {
				settingsHandler.setSearchText(textHandler.getSearchText());
			}
			break;
		case GET_DEFINITION:
			if (textHandler.getSearchText() != null) {
				dictionaryHandler.getDefinition(textHandler.getSearchText());
			}
			break;
		default:
			break;
		}
	}

	private void processSettings(Command cmd) {
		switch (cmd) {
		case SET_SPEED_WPM:
			LOGGER.info("Changed speed to {}", settingsHandler.getSpeedWpm());
			navigator.setSpeedWpm(settingsHandler.getSpeedWpm());
			break;

		case OPEN_SELECTED_BOOK:
			openSelectedBook();
			break;

		case OPEN_SELECTED_REFERENCE:
			textHandler.setRefenceHandler(settingsHandler.getReferenceHandler());
			settingsHandler.getReferenceHandler().setDictionaryHandler(dictionaryHandler);
			break;

		case GOTO_PAGE:
			gotoPage();
			break;

		case SEARCH:
			search();
			break;

		case SEARCH_NEXT:
			break;

		default:
			break;
		}
	}

	private void openSelectedBook() {
		textPanel.setBookHandler(settingsHandler.getBookHandler());
	}

	private void gotoPage() {
		textHandler.gotoPage(formPanel.getSelectedPageNumber());
	}

	private void search() {
		textHandler.searchText(settingsHandler.getSearchText());
		if (settingsHandler.getReferenceHandler() != null) {
			settingsHandler.getReferenceHandler().searchText(settingsHandler.getSearchText());
		}
	}

	private void manageLayout() {
		LOGGER.info("Managing Layout");
		setLayout(new BorderLayout());
		add(formPanel, BorderLayout.WEST);
		add(toolbar, BorderLayout.NORTH);
		add(textPanel, BorderLayout.CENTER);
		add(loggingPanel, BorderLayout.SOUTH);

		setSize(800, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void displayAbout(ActionEvent event) {
		JTextPane textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setForeground(Color.BLACK);
		textPane.setBackground(Color.WHITE);

		UIManager.put("OptionPane.background", Color.white);
		UIManager.put("Panel.background", Color.white);

		textPane.setText(FileHelper.loadTextFileIntoString("/About.html"));
		JOptionPane.showMessageDialog(this, textPane, "About Accounting Reader", JOptionPane.INFORMATION_MESSAGE);
	}

}
