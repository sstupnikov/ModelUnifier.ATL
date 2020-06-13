package org.ipiran.unifier.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.INewWizard;
import org.ipiran.unifier.ModelUnifierPlugin;
import org.ipiran.unifier.wizards.NewExtensionProjectWizard;
import org.ipiran.unifier.wizards.NewExtensionRegCardWizard;
import org.ipiran.unifier.wizards.NewModelRegCardWizard;

public class NewExtensionProjectAction extends NewProjectAction {

	private static final Logger log = Logger.getLogger(NewExtensionProjectAction.class); 	
	
	@Override
	protected INewWizard getWizard() {
		return new NewExtensionProjectWizard();
	}
	

}
