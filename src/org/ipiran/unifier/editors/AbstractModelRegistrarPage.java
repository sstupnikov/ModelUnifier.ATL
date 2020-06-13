package org.ipiran.unifier.editors;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormEditor;

public abstract class AbstractModelRegistrarPage extends MasterDetailsPage {

	private static final Logger log = Logger.getLogger(AbstractModelRegistrarPage.class);	
	
	public AbstractModelRegistrarPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		
		MasterDetailsBlock block = getBlock();
		
		if(block instanceof AbstractModelRegistrarMasterDetailsBlock){
			AbstractModelRegistrarMasterDetailsBlock modelBlock = (AbstractModelRegistrarMasterDetailsBlock)block;
			
			modelBlock.getSyntaxDocViewer().refresh();		
			modelBlock.getSemanticsDocViewer().refresh();			
		}
		else{
			log.warn("MasterDetailsBlock is not AbstractModelRegistrarMasterDetailsBlock (syntax and semantics docs viewers are not presented).");
		}
	}	
	
}
