package org.ipiran.unifier.editors;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

public abstract class DataBindingDetailsPage implements IDetailsPage {
	private static final Logger log = Logger.getLogger(DataBindingDetailsPage.class);	

	protected IManagedForm mform;
	protected EMFDataBindingContext bindingContext;		
	protected IObservableValue master;
	

	public DataBindingDetailsPage(EMFDataBindingContext bindingContext, IObservableValue master){
		if(bindingContext == null){
			throw new IllegalArgumentException("Null <master> argument in DataBindingDetailsPage constructor.");
		}
		if(master == null){
			throw new IllegalArgumentException("Null <bindingContext> argument in DataBindingDetailsPage constructor.");
		}
		
		this.bindingContext = bindingContext;
		this.master = master;				
	}
	
	
	@Override
	public void initialize(IManagedForm form) {		
		this.mform = form;
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void commit(boolean onSave) {
	}

	@Override
	public boolean setFormInput(Object input) {
		return false;
	}

	@Override
	public void setFocus() {
	}

	@Override
	public boolean isStale() {
		return false;
	}

	@Override
	public void refresh() {
	}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		/*IStructuredSelection ssel = (IStructuredSelection)selection;
		log.info("DocumentDetailsPage: selection changed.");				
		if (ssel.size()==1) {
			input = (Document)ssel.getFirstElement();
		}
		else{
			input = null;
		}
		*/
		
		//Object obj = master.getValue();
		//log.info("Selection changed, master class: " + obj.getClass());		
	}

	@Override
	public void createContents(Composite parent) {		
	}

	protected abstract void createBindings();
	
}
