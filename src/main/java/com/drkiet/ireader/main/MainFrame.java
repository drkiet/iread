package com.drkiet.ireader.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.handler.TextHandler;
import com.drkiet.ireader.main.ReaderListener.Command;
import com.drkiet.ireader.main.handler.SettingsHandler;
import com.drkiet.ireader.util.FileHelper;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -6071539847363274514L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MainFrame.class);

	private TextPanel textPanel = new TextPanel();
	private Toolbar toolbar = new Toolbar();
	private FormPanel formPanel = new FormPanel();
	private JFileChooser fileChooser = new JFileChooser();
	private InfoPanel infoPanel = new InfoPanel();
	private JMenuBar menubar = new JMenuBar();
	private SettingsHandler sh = new SettingsHandler();
	private TextHandler th = new TextHandler();

	public MainFrame() throws IOException {
		super("Intelligent Reader");
		LOGGER.info("Intelligent Reader Program begins ...");
		manageMenubar();
		manageListeners();
		manageLayout();
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
		formPanel.setReaderListener((Command cmd) -> {
			sh.processForm(formPanel, cmd);
		});

		textPanel.setReaderListener((Command cmd) -> {
			th.processText(textPanel, cmd);
		});
//
//		textPanel.setMainFrame(this);
//		textPanel.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				processMouseClickedInTextPanel();
//			}
//
//		});
//
//		toolbar.setReaderListener((Command cmd) -> {
//			processButtonsOnToolbar(cmd);
//		});
	}

	public void manageLayout() {
		LOGGER.info("Managing Layout");
		setLayout(new BorderLayout());
		add(formPanel, BorderLayout.WEST);
		add(toolbar, BorderLayout.NORTH);
		add(textPanel, BorderLayout.CENTER);
		add(infoPanel, BorderLayout.SOUTH);

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
