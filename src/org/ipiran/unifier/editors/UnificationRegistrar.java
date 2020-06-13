package org.ipiran.unifier.editors;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.ModelRegistry.RegistrationCard;
import org.ipiran.unifier.ModelRegistry.UnificationReg;
import org.ipiran.unifier.utils.ModelRegistryUtils;

public class UnificationRegistrar extends Registrar {

	private static final Logger log = Logger.getLogger(UnificationRegistrar.class);

	protected static final String TITLE = "Unification Registrar";	
	protected UnificationReg model = null;
	
	
	@Override
	public RegistrationCard getModel() {
		return model;
	}

	@Override
	public void setModel(RegistrationCard model) throws WrongRegistrarInputException {
		if(model instanceof UnificationReg){
			this.model = (UnificationReg)model;	
		} 
		else{
			throw new WrongRegistrarInputException("Unification registrar input model is not of type UnificationReg.");
		}		
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	protected void putExtensionToRegistry() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(FileExtension.UNI.toString(), new XMIResourceFactoryImpl());				
	}

	@Override
	protected void createFilesForModel() {
		//ModelRegistryUtils.createFilesForModel(model);
		ModelRegistryUtils.createFilesForEObject(model);
	}

	@Override
	protected void addPages() {
		try {
			addPage(new UnificationRegistrarPage(this));
		}
		catch (PartInitException e) {
			e.printStackTrace();
		} 
		catch (WrongRegistrarInputException e) {
		
			log.error("Wrong Model Registrar input.");
			e.printStackTrace();
		}				
	} 

	
	
	

}
