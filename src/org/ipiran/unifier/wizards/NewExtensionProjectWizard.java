package org.ipiran.unifier.wizards;

import org.apache.log4j.Logger;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelRegistry.ExtensionReg;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.ModelRegistry.ModelRegistryFactory;
import org.ipiran.unifier.ModelRegistry.RegistrationCard;
import org.ipiran.unifier.utils.ModelRegistryUtils;

public class NewExtensionProjectWizard extends NewProjectWizard {
	private static final Logger log = Logger.getLogger(NewExtensionProjectWizard.class);
	
	
	protected static final String WINDOW_TITLE = "New Extension Project";
	protected static final String PAGE_TITLE = "Extension Project";
	protected static final String PAGE_DESC = "This wizard creates a new extension project and " +
			"a new extension registration card file with *.erc extension " +
			"that can be opened by Model Extension Card Editor.";

	protected static final FileExtension EXTENSION = FileExtension.ERC;	

			
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
		ExtensionReg card = null;
		
		card = 	ModelRegistryFactory.eINSTANCE.createExtensionReg();
		card.setShortTitle(this.mainPage.getProjectName());
		ModelRegistryUtils.createFilesForModel(card);

		return card;
	}

	@Override
	protected void createProjectContent() {
	}

}
