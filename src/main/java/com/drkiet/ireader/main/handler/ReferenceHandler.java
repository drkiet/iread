package com.drkiet.ireader.main.handler;

import com.drkiet.ireader.util.FileHelper;
import com.drkiet.search.DocumentSearch;

public class ReferenceHandler {

	private DocumentSearch ds;

	public void openSelectedReference(String selectedReference) {
		ds = DocumentSearch.getInstance(FileHelper.getFQRefencesFileName(selectedReference),
				FileHelper.getWorkspaceFolder());
	}

}
