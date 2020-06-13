package org.ipiran.unifier.editors;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.ui.PartInitException;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelRegistry.ExtensionReg;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.ModelRegistry.RegistrationCard;
import org.ipiran.unifier.utils.ModelRegistryUtils;

public class ExtensionRegistrar extends Registrar {
	
	private static final Logger log = Logger.getLogger(ExtensionRegistrar.class); 

	private static final String TITLE = "Extension Registrar";
	
	private ExtensionReg model = null;
	
	@Override
	public ExtensionReg getModel() {
		return model;
	}

	@Override
	public void setModel(RegistrationCard model) throws WrongRegistrarInputException {
		if(model instanceof ExtensionReg){
			this.model = (ExtensionReg)model;	
		} 
		else{
			throw new WrongRegistrarInputException("Extension registrar input model is not of type ExtensionReg.");
		}
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	protected void addPages() {
		try {
			addPage(new ExtensionRegistrarPage(this));
		}
		catch (PartInitException e) {
			e.printStackTrace();
		} catch (WrongRegistrarInputException e) {
			log.error("Wrong input for extension registrar.");
			e.printStackTrace();
		}		
	}

	@Override
	protected void createFilesForModel() {
		//ModelRegistryUtils.createFilesForModel(model);
		ModelRegistryUtils.createFilesForEObject(model);
	}

	@Override
	protected void putExtensionToRegistry() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(FileExtension.ERC.toString(), new XMIResourceFactoryImpl());		
	}	
	
}
