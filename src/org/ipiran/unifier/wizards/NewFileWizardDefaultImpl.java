package org.ipiran.unifier.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Text;
import org.ipiran.unifier.FileExtension;

public class NewFileWizardDefaultImpl extends NewFileWizard {

	protected String windowTitle = "Open File";
	protected String templatePath;
	protected FileExtension extension;
	
	
	protected NewFileWizardPage page;	
	
	public NewFileWizardDefaultImpl() {
		super();		
	}

	public NewFileWizardDefaultImpl(FileExtension extension, Text text){
		this.extension = extension;
	}

	@Override
	public NewFileWizardPage getPage(){
		return page;
	}
	
	public void addPages() {
		page = new NewFileWizardPage(selection, extension);
		addPage(page);
	}

	@Override
	protected InputStream openContentStream() {
		if(templatePath != null && templatePath.trim().compareTo("") != 0){
			return this.getClass().getResourceAsStream(templatePath);
		}
		else{
			return new ByteArrayInputStream("".getBytes());
		}	
	}
	
}
