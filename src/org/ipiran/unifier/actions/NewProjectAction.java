package org.ipiran.unifier.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.IWorkbenchWizard;
import org.ipiran.unifier.ModelUnifierPlugin;

public abstract class NewProjectAction implements IWorkbenchWindowActionDelegate {

	private static final Logger log = Logger.getLogger(NewProjectAction.class); 	

	protected IWorkbenchWindow window;
	
	@Override
	public void run(IAction action) {
		openDialog();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
		
	protected void openDialog(){
		IWorkbenchWizard wizard = getWizard(); 
		
		wizard.init(window.getWorkbench(), null);
	    WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
	    dialog.setDefaultImage(ModelUnifierPlugin.getDefault().getImage(ModelUnifierPlugin.IMG_PERSPECTIVE));
	    dialog.create();
	    dialog.open();
	    

	}
	
	protected abstract INewWizard getWizard();
}
