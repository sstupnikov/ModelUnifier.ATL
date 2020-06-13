package org.ipiran.unifier.editors;

import org.apache.log4j.Logger;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormEditor;

public class ModelRegistrarPage extends AbstractModelRegistrarPage {
	
	private static final Logger log = Logger.getLogger(ModelRegistrarPage.class);	
	
	private static final String PAGE_TITLE = "Model Syntax and Semantics";
	private static final String TAB_TITLE = "Model Registration Card";
	
	private MasterDetailsBlock block;	

	public ModelRegistrarPage(FormEditor editor) throws WrongRegistrarInputException {
		super(editor, "first", TAB_TITLE); 
		block = new ModelRegistrarMasterDetailsBlock(this);		
	}

	@Override
	public MasterDetailsBlock getBlock() {
		return block;
	}

	@Override
	public String getPageTitle() {
		return PAGE_TITLE;
	}

}
