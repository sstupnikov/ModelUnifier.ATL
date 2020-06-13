package org.ipiran.unifier.wizards;

import org.apache.log4j.Logger;

public class NewRefinementSampleWizard extends NewSampleWizard {
	private static final Logger log = Logger.getLogger(NewRefinementSampleWizard.class);
	
	protected static final String WINDOW_TITLE = "New Refinement Sample";
	protected static final String PAGE_TITLE = "Refinement Sample";
	protected static final String PAGE_DESC = "";	

	
	public NewRefinementSampleWizard(String unificationProjectName) {
		super(unificationProjectName);
		// TODO Auto-generated constructor stub
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
