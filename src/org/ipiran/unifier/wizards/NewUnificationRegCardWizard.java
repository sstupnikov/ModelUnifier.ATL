package org.ipiran.unifier.wizards;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.ipiran.unifier.FileExtension;

public class NewUnificationRegCardWizard extends NewFileWizard {

	private static final Logger log = Logger.getLogger(NewUnificationRegCardWizard.class); 			

	protected static final String WINDOW_TITLE = "New Unification Registration Card";
	protected static final String TEMPLATE_PATH = "uni_template.uni";
	protected static final String PAGE_TITLE = "Unification registration card";
	protected static final String PAGE_DESC = "This wizard creates a new unification registration card file with *.uni extension that can be opened by Unification Registration Card Editor";
	protected static final FileExtension EXTENSION = FileExtension.UNI;
	protected static final String DEFAULT_FILE_NAME = "new_card.uni";


	public NewUnificationRegCardWizard() {
		super();		
		this.setWindowTitle(WINDOW_TITLE);
	}
	
	public void addPages() {
		page = new NewFileWizardPage(
				selection,
				PAGE_TITLE,
				PAGE_DESC,
				EXTENSION,
				DEFAULT_FILE_NAME
				);
		addPage(page);
	}

	protected InputStream openContentStream() {
		return this.getClass().getResourceAsStream(TEMPLATE_PATH);
	}	
	
	

}
