package org.ipiran.unifier.wizards;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.ipiran.unifier.FileExtension;

public class NewExtensionRegCardWizard extends NewFileWizard {

	private static final Logger log = Logger.getLogger(NewExtensionRegCardWizard.class); 			

	protected static final String WINDOW_TITLE = "New Extension Registration Card";
	protected static final String TEMPLATE_PATH = "erc_template.erc";
	protected static final String PAGE_TITLE = "Extension registration card";
	protected static final String PAGE_DESC = "This wizard creates a new Extension registration card file with *.erc extension that can be opened by Extension Registration Card Editor";
	protected static final FileExtension EXTENSION = FileExtension.ERC;
	protected static final String DEFAULT_FILE_NAME = "new_card.erc";


	public NewExtensionRegCardWizard() {
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
