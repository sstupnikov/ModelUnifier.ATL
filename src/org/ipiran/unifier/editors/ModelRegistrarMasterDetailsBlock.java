package org.ipiran.unifier.editors;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.ipiran.unifier.ModelRegistry.ExtensionReg;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.ModelRegistry.RegistrationCard;

public class ModelRegistrarMasterDetailsBlock extends
		AbstractModelRegistrarMasterDetailsBlock {

	private static final Logger log = Logger.getLogger(ModelRegistrarMasterDetailsBlock.class); 			

	private ModelReg card;	
		
	public ModelRegistrarMasterDetailsBlock(FormPage page) throws WrongRegistrarInputException {				
		super(page);
		
		RegistrationCard card = ((Registrar)page.getEditor()).getModel(); 
		if(card instanceof ModelReg){
			this.card = (ModelReg)card;	
		} 
		else{
			throw new WrongRegistrarInputException("Model registrar input model is not of type ModelReg.");
		}		
	}

	@Override
	protected ModelReg getModel() {
		return card;
	}

	@Override
	protected void createAdditionalControls(IManagedForm managedForm, Composite parent) {
		// DO NOTHING		
	}

}
