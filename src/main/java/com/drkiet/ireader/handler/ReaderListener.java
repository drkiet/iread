package com.drkiet.ireader.handler;

public interface ReaderListener {
	public enum Command {
		START, STOP, RESET, RESTART, LOAD, LOAD_REF, START_AT, SMALLER_TEXT_FONT, LARGER_TEXT_FONT, SMALLER_WORD_FONT,
		LARGER_WORD_FONT, HELP_PICTURE, BROWSE, GOTO, SELECT_TRANSLATION,

		/** MATCH **/
		PREVIOUS_EXACT_MATCH, NEXT_EXACT_MATCH, NEXT_FIND, PREVIOUS_FIND,

		/** Reading **/
		START_READING, START_READING_AT, STOP_READING, PAUSE_READING,

		/** Navigation **/
		PREVIOUS_PAGE, NEXT_PAGE, GOTO_PAGE, GOTO_URL,

		/** Searching **/
		SEARCH, POPUP_SEARCH, SEARCH_NEXT, GET_DEFINITION, GET_HELP,

		/** Managing files **/
		SELECT_BOOK, OPEN_SELECTED_BOOK, SELECT_REFERENCE, OPEN_SELECTED_REFERENCE,

		/** Set speed **/
		SET_SPEED_WPM
	};

	void invoke(Command cmd);
}
