package org.ipiran.unifier.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.INewWizard;
import org.ipiran.unifier.ModelUnifierPlugin;
import org.ipiran.unifier.wizards.NewModelRegCardWizard;
import org.ipiran.unifier.wizards.NewUnificationProjectWizard;
import org.ipiran.unifier.wizards.NewUnificationRegCardWizard;

public class NewUnificationProjectAction extends NewProjectAction {

	private static final Logger log = Logger.getLogger(NewUnificationProjectAction.class); 		
	
	@Override
	protected INewWizard getWizard() {
		return new NewUnificationProjectWizard();
	}	

}
