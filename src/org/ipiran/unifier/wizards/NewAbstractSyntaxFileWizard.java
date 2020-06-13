package org.ipiran.unifier.wizards;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.ipiran.unifier.FileExtension;

public class NewAbstractSyntaxFileWizard extends NewFileWizard {

	private static final Logger log = Logger.getLogger(NewAbstractSyntaxFileWizard.class); 			

	protected static final String WINDOW_TITLE = "New Model Abstract Syntax File";
	protected static final String TEMPLATE_PATH = "abstract_syntax_tamplate.ecore";
	protected static final String PAGE_TITLE = "Model abstract syntax file";
	protected static final String PAGE_DESC = "This wizard creates a new abstract syntax file with *.ecore extension that can be opened by Ecore editor.";
	protected static final FileExtension EXTENSION = FileExtension.ECORE;
	protected static final String DEFAULT_FILE_NAME = "new_abstract_syntax.ecore";


	public NewAbstractSyntaxFileWizard() {
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
