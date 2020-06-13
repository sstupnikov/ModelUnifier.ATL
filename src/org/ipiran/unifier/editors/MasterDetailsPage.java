package org.ipiran.unifier.editors;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public abstract class MasterDetailsPage extends FormPage {

	private static final Logger log = Logger.getLogger(MasterDetailsPage.class); 	
	
	
	public MasterDetailsPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();		
		
		ScrolledForm form = managedForm.getForm();		
		form.setText(getPageTitle());		
		toolkit.decorateFormHeading(form.getForm());

		
		getBlock().createContent(managedForm);				
	}			
	
	public abstract MasterDetailsBlock getBlock();	
	public abstract String getPageTitle();	
	
}
