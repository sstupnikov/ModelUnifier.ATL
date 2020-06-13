package org.ipiran.unifier.wizards;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.ipiran.unifier.utils.Utils;

public abstract class NewSampleWizard extends Wizard implements INewWizard {
	private static final Logger log = Logger.getLogger(NewSampleWizard.class);
		
	protected NewSampleWizardPage mainPage;	
	
	protected String unificationProjectName;
	protected boolean cancelled;
	
	public NewSampleWizard(String unificationProjectName){
		this.unificationProjectName = unificationProjectName;
		this.cancelled = false;
		
		this.setWindowTitle(getWindowTitle());		
	}

	public abstract String getWindowTitle();	
	protected abstract String getMainPageTitle();
	protected abstract String getMainPageDesc();	
	
	public void addPages() {
		super.addPages();
		
		mainPage = new NewSampleWizardPage("NewSamplePage", unificationProjectName); 
		mainPage.setTitle(getMainPageTitle());
		mainPage.setDescription(getMainPageDesc());
		this.addPage(mainPage);				
	}	
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public boolean performCancel(){
		cancelled = true;
		
		return super.performCancel();
	}
	
	@Override
	public boolean performFinish() {
		if(Utils.isFilenameValid(mainPage.getSampleName())){
			return true;
		} else{
			return false;
		}
	}
	
	public String getSampleName(){
		if(mainPage == null){
			return null; 
		} else{
			return mainPage.getSampleName();
		}
	}

	public boolean isCancelled(){
		return cancelled;
	}
}
