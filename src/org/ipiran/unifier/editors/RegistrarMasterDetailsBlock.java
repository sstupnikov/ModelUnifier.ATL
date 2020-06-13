package org.ipiran.unifier.editors;

import org.apache.log4j.Logger;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.ipiran.unifier.ModelUnifierPlugin;

public abstract class RegistrarMasterDetailsBlock extends MasterDetailsBlock {
	private static final Logger log = Logger.getLogger(RegistrarMasterDetailsBlock.class);
	
	protected FormPage page;
	protected EMFDataBindingContext bindingContext;

	public RegistrarMasterDetailsBlock(FormPage page) {
		this.page = page;			
		this.bindingContext = new EMFDataBindingContext();					
	}

	protected void createToolBarActions(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		
		Action haction = new Action("hor", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.HORIZONTAL);
				form.reflow(true);
			}
		};
		haction.setChecked(true);
		haction.setToolTipText("Horizontal orientation"); 
		haction.setImageDescriptor(ModelUnifierPlugin.getDefault().getImageRegistry().getDescriptor(ModelUnifierPlugin.IMG_HORIZONTAL));
		Action vaction = new Action("ver", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		
		vaction.setChecked(false);
		vaction.setToolTipText("Vertical orientation"); 
		vaction.setImageDescriptor(ModelUnifierPlugin.getDefault().getImageRegistry().getDescriptor(ModelUnifierPlugin.IMG_VERTICAL));
		form.getToolBarManager().add(haction);
		form.getToolBarManager().add(vaction);
	}	
	
	protected abstract void createDataBindings();	
}
