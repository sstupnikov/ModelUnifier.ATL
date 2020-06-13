package org.ipiran.unifier.wizards;

import java.io.IOException;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.ui.internal.wizards.newresource.ResourceMessages;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelRegistry.ModelRegistryFactory;
import org.ipiran.unifier.ModelRegistry.RegistrationCard;
import org.ipiran.unifier.ModelRegistry.UnificationReg;
import org.ipiran.unifier.utils.ModelRegistryUtils;

public class NewUnificationProjectWizard extends NewProjectWizard {
	private static final Logger log = Logger.getLogger(NewUnificationProjectWizard.class);

	protected static final String WINDOW_TITLE = "New Unification Project";
	protected static final String PAGE_TITLE = "Unification Project";
	protected static final String PAGE_DESC = "This wizard creates a new unification project and " +
			"a new unification registration card file with *.uni extension " +
			"that can be opened by Unification Registration Card Editor.";

	protected static final FileExtension EXTENSION = FileExtension.UNI;	

			
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
		UnificationReg card = null;
		
		card = 	ModelRegistryFactory.eINSTANCE.createUnificationReg();
		ModelRegistryUtils.createFilesForModel(card);

		return card;
	}

	@Override
	protected void createProjectContent() throws CoreException {
		/*
		IFolder refSampleFolder = getNewProject().getFolder(new Path("refinement_samples"));
		if(!refSampleFolder.exists()){
			refSampleFolder.create(true, true, null);
		}
		
		IFolder entSampleFolder = getNewProject().getFolder(new Path("entailment_samples"));
		if(!entSampleFolder.exists()){
			entSampleFolder.create(true, true, null);
		}
		*/
	}

}
