package org.ipiran.unifier.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.ipiran.unifier.ModelUnifierPlugin;
import org.ipiran.unifier.wizards.NewModelProjectWizard;
import org.ipiran.unifier.wizards.NewModelRegCardWizard;

public class NewModelProjectAction extends  NewProjectAction {

	private static final Logger log = Logger.getLogger(NewModelProjectAction.class);
	
	@Override
	protected INewWizard getWizard() {
		return new NewModelProjectWizard();
	} 
				
	
}
