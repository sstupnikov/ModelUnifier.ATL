package org.ipiran.unifier.editors;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.editor.IFormPage;

public class UnificationRegistrarPage extends MasterDetailsPage {

	private static final Logger log = Logger.getLogger(UnificationRegistrarPage.class);	
	
	private static final String PAGE_TITLE = "Unification Data";
	private static final String TAB_TITLE = "Unification Registration Card";
	

	private UnificationMasterDetailsBlock block;	
	
	public UnificationRegistrarPage(FormEditor editor) throws WrongRegistrarInputException {
		super(editor, "first", TAB_TITLE); 
		block = new UnificationMasterDetailsBlock(this);
	}

	@Override
	public MasterDetailsBlock getBlock() {
		return block;
	}

	@Override
	public String getPageTitle() {
		return PAGE_TITLE;
	}
	
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		
		block.getSamplesViewer().refresh();					
	}		
	
}
