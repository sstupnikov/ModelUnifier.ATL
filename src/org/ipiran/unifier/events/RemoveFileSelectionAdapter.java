package org.ipiran.unifier.events;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionEvent;
import org.ipiran.unifier.ModelRole;
import org.ipiran.unifier.ModelRegistry.RefinementSampleReg;
import org.ipiran.unifier.editors.WrongMasterValueTypeException;
import org.ipiran.unifier.parts.TableFormPart;

public class RemoveFileSelectionAdapter extends ModifyMasterSelectionAdapter {
	private static final Logger log = Logger.getLogger(RemoveFileSelectionAdapter.class);	

	protected TableViewer viewer = null;	
	
	public RemoveFileSelectionAdapter(TableFormPart part, IObservableValue master, ModelRole role, TableViewer viewer) {
		super(part, master, role);
		
		this.viewer = viewer;		
	}
	
	public void widgetSelected(SelectionEvent e) {
		log.info("Remove item button pressed.");

		if (!(master.getValue() instanceof RefinementSampleReg)){
			throw new WrongMasterValueTypeException("Master value is not of type RefinementSampleReg.");
		}
		
		RefinementSampleReg sample = (RefinementSampleReg)master.getValue();				
		
		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		if(sel.getFirstElement() != null){		
			
			switch(role){
			case SOURCE:
				sample.getSourceSemSpec().remove(sel.getFirstElement());
				break;
			case TARGET:
				sample.getTargetSemSpec().remove(sel.getFirstElement());
				break;
			}			
			
			part.markDirty();
		}				
	}	
}
