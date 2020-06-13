package org.ipiran.unifier.editors;

import org.apache.log4j.Logger;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormEditor;

public class ExtensionRegistrarPage extends AbstractModelRegistrarPage {
	
	private static final Logger log = Logger.getLogger(ExtensionRegistrarPage.class);	
	
	private static final String PAGE_TITLE = "Extension Syntax and Semantics";
	private static final String TAB_TITLE = "Extension Registration Card";	
	
	private ExtensionRegistrarMasterDetailsBlock block;		

	public ExtensionRegistrarPage(FormEditor editor) throws WrongRegistrarInputException {
		super(editor, "first", TAB_TITLE); 
		block = new ExtensionRegistrarMasterDetailsBlock(this);	
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
