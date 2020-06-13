package org.ipiran.unifier.wizards;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.utils.Utils;

public class NewModelRegCardWizard extends NewFileWizard {
	private static final Logger log = Logger.getLogger(NewModelRegCardWizard.class); 			

	protected static final String WINDOW_TITLE = "New Model Registration Card";
	protected static final String TEMPLATE_PATH = "mrc_template.mrc";
	protected static final String PAGE_TITLE = "Model registration card";
	protected static final String PAGE_DESC = "This wizard creates a new model registration card file with *.mrc extension that can be opened by Model Registration Card Editor";
	protected static final FileExtension EXTENSION = FileExtension.MRC;
	protected static final String DEFAULT_FILE_NAME = "new_card.mrc";


	public NewModelRegCardWizard() {
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