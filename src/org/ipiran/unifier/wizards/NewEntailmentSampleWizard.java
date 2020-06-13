package org.ipiran.unifier.wizards;

import org.apache.log4j.Logger;

public class NewEntailmentSampleWizard extends NewSampleWizard {	
	private static final Logger log = Logger.getLogger(NewEntailmentSampleWizard.class);	
	
	protected static final String WINDOW_TITLE = "New Entailment Sample";
	protected static final String PAGE_TITLE = "Entailment Sample";
	protected static final String PAGE_DESC = "";	

	public NewEntailmentSampleWizard(String unificationProjectName) {
		super(unificationProjectName);
	}

	@Override
	public String getWindowTitle() {
		return WINDOW_TITLE;
	}

	@Override
	protected String getMainPageTitle() {
		return PAGE_TITLE;
	}

	@Override
	protected String getMainPageDesc() {
		return PAGE_DESC;
	}

}
