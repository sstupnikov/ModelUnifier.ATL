package org.ipiran.unifier.wizards;

import org.apache.log4j.Logger;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.ModelRegistry.ModelRegistryFactory;
import org.ipiran.unifier.ModelRegistry.RegistrationCard;
import org.ipiran.unifier.ModelRegistry.UnificationReg;
import org.ipiran.unifier.utils.ModelRegistryUtils;

public class NewModelProjectWizard extends NewProjectWizard {
	private static final Logger log = Logger.getLogger(NewModelProjectWizard.class);
	
	protected static final String WINDOW_TITLE = "New Model Project";
	protected static final String PAGE_TITLE = "Model Project";
	protected static final String PAGE_DESC = "This wizard creates a new model project and " +
			"a new model registration card file with *.mrc extension " +
			"that can be opened by Model Registration Card Editor.";

	protected static final FileExtension EXTENSION = FileExtension.MRC;	

			
	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return WINDOW_TITLE;
	}
	
	@Override
	protected String getMainPageTitle() {
		return PAGE_TITLE;
	}

	@Override
	protected String getMainPageDesc() {
		return PAGE_DESC;
	}	
	
	@Override
	protected FileExtension getRegCardFileExtension() {
		return EXTENSION;
	}	
	
	@Override
	protected RegistrationCard createRegistrationCard() {
		ModelReg card = null;
		
		card = 	ModelRegistryFactory.eINSTANCE.createModelReg();
		card.setShortTitle(this.mainPage.getProjectName());
		ModelRegistryUtils.createFilesForModel(card);

		return card;
	}

	@Override
	protected void createProjectContent() {				
	}
	
}
