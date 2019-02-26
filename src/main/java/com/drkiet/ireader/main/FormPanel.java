package com.drkiet.ireader.main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiet.ireader.handler.ReaderListener;
import com.drkiet.ireader.handler.ReaderListener.Command;
import com.drkiet.ireader.util.FileHelper;

public class FormPanel extends JPanel {
	private static final Logger LOGGER = LoggerFactory.getLogger(FormPanel.class);

	private static final long serialVersionUID = 3506596135223108382L;
	private JLabel bookLabel;
	private JComboBox<String> booksCombo;
	private JButton openBookButton;

	private JLabel referenceLabel;
	private JComboBox<String> referenceCombo;
	private JButton openReferenceButton;

	private JLabel speedLabel;
	private JTextField speedField;
	private JButton setSpeedButton;
	private Integer speedWpm = 200;

	private JLabel skipArticleLabel;
	private JCheckBox skipArticleCheckBox;

	private JLabel searchTextLabel;
	private JTextField searchTextField;
	private JButton searchButton;
	private JButton searchNextButton;

	private ReaderListener readerListener;
	private JLabel gotoTextLabel;
	private JTextField gotoTextField;
	private JButton gotoButton;

	public Integer getSpeedWpm() {
		return speedWpm;
	}

	public FormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 250;
		setPreferredSize(dim);
		makeComboBoxes();
		makeComponents();

		setListeners();

		Border innerBorder = BorderFactory.createTitledBorder("Settings");
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));

		layoutComponents();
	}

	public void setListeners() {
		setSpeedButton.addActionListener((ActionEvent actionEvent) -> {
			speedWpm = Integer.valueOf(speedField.getText());
		});

		booksCombo.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.SELECT_BOOK);
		});

		referenceCombo.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.SELECT_REFERENCE);
		});

		openBookButton.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.OPEN_SELECTED_BOOK);
		});

		openReferenceButton.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.OPEN_SELECTED_REFERENCE);
		});

		searchButton.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.SEARCH);
		});

		searchNextButton.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.SEARCH_NEXT);
		});

		gotoButton.addActionListener((ActionEvent actionEvent) -> {
			readerListener.invoke(Command.GOTO_PAGE);
		});

	}

	public void makeComponents() {
		speedLabel = new JLabel("Speed (wpm): ");
		speedField = new JTextField(10);
		searchTextLabel = new JLabel("Text");
		searchTextField = new JTextField(10);
		gotoTextLabel = new JLabel("Page No.");
		gotoTextField = new JTextField(10);
		speedField.setText("" + speedWpm);
		skipArticleLabel = new JLabel("Skip articles:");

		skipArticleCheckBox = new JCheckBox();
		setSpeedButton = new JButton("Set Speed");
		openBookButton = new JButton("Open Book");
		openReferenceButton = new JButton("Open Ref.");
		searchButton = new JButton("Search");
		searchNextButton = new JButton("Next");
		gotoButton = new JButton("Go to");
	}

	public void makeComboBoxes() {
		bookLabel = new JLabel("Select a book: ");
		booksCombo = new JComboBox<String>();
		booksCombo.setPrototypeDisplayValue("default text here");

		List<String> bookNames = getBookNames();

		for (String fileName : bookNames) {
			booksCombo.addItem(fileName);
		}

		ComboboxToolTipRenderer renderer = new ComboboxToolTipRenderer();
		booksCombo.setRenderer(renderer);
		booksCombo.setSelectedIndex(0);
		renderer.setTooltips(bookNames);

		// Reference books.
		bookNames = getReferenceNames();
		referenceLabel = new JLabel("Select a Ref.: ");
		referenceCombo = new JComboBox<String>();
		referenceCombo.setPrototypeDisplayValue("default text here");

		for (String fileName : bookNames) {
			referenceCombo.addItem(fileName);
		}

		renderer = new ComboboxToolTipRenderer();
		referenceCombo.setRenderer(renderer);
		referenceCombo.setSelectedIndex(0);
		renderer.setTooltips(bookNames);
	}

	public void setReaderListener(ReaderListener readerListener) {
		this.readerListener = readerListener;
	}

	public void setFileName(String selectedFile) {
		booksCombo.setSelectedItem(selectedFile);
	}

	public String getSearchText() {
		return searchTextField.getText();
	}

	public int getSelectedPageNumber() {
		if (!gotoTextField.getText().trim().isEmpty()) {
			return Integer.valueOf(gotoTextField.getText());
		}
		return -1;
	}

	public String getSelectedBookName() {
		return (String) booksCombo.getSelectedItem();
	}

	public boolean skipArticle() {
		LOGGER.info("skip articles ... {}", skipArticleCheckBox.isSelected());
		return skipArticleCheckBox.isSelected();
	}

	public String getSelectedReference() {
		return (String) referenceCombo.getSelectedItem();
	}

	public void setSearchText(String searchText) {
		searchTextField.setText(searchText);
	}

	private List<String> getReferenceNames() {
		return FileHelper.getFileNames(FileHelper.getReferenceFolder());
	}

	private List<String> getBookNames() {
		return FileHelper.getFileNames(FileHelper.getContentFolder());
	}

	private void layoutComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		//// FIRST ROW /////////////
		gc.gridy = 0;

		// Always do the following to avoid future confusion :)
		// Speed
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(speedLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(speedField, gc);

		//// next row /////////////
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = .2;

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(setSpeedButton, gc);

		// Always do the following to avoid future confusion :)
		// Skip Articles
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 0);
		add(skipArticleLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(skipArticleCheckBox, gc);

		// Always do the following to avoid future confusion :)
		// Book Name:
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(bookLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(booksCombo, gc);

		//// next row /////////////
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = .2;

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(openBookButton, gc);

		// Always do the following to avoid future confusion :)
		// Reference Book Name:
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(referenceLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(referenceCombo, gc);

		//// next row /////////////
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = .2;

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(openReferenceButton, gc);

		// Always do the following to avoid future confusion :)
		// Search Text:
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(searchTextLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(searchTextField, gc);

		//// next row /////////////
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = .2; // 5;

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(searchButton, gc);

		//// next row /////////////
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = .2;

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(searchNextButton, gc);

		// Always do the following to avoid future confusion :)
		// Goto page:
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		add(gotoTextLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		add(gotoTextField, gc);

		//// next row /////////////
		gc.gridy++;
		gc.weightx = 1;
		gc.weighty = 5;

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(gotoButton, gc);
	}

}
