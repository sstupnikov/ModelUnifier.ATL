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

public class ModelRegistrar extends Registrar {
	
	private static final Logger log = Logger.getLogger(ModelRegistrar.class); 

	protected static final String TITLE = "Model Registrar";	
	protected ModelReg model = null;
	
	public ModelReg getModel() {
		return model;
	}

	@Override
	public void setModel(RegistrationCard model) throws WrongRegistrarInputException {
		if(model instanceof ModelReg){
			this.model = (ModelReg)model;	
		} 
		else{
			throw new WrongRegistrarInputException("Model registrar input model is not of type ModelReg.");
		}
	}

	@Override
	public String getTitle() {
		return TITLE;
	}
	
	@Override
	protected void addPages() {
		try {
			addPage(new ModelRegistrarPage(this));
		}
		catch (PartInitException e) {
			e.printStackTrace();
		} catch (WrongRegistrarInputException e) {
			log.error("Wrong Model Registrar input.");
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
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(FileExtension.MRC.toString(), new XMIResourceFactoryImpl());		
	}	
	
}
