package org.ipiran.unifier.events;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionEvent;
import org.ipiran.unifier.ModelRegistry.SampleReg;
import org.ipiran.unifier.parts.TableFormPart;
import org.ipiran.unifier.utils.Utils;

public class RemoveSampleSelectionAdapter extends RemoveItemSelectionAdapter {

	public RemoveSampleSelectionAdapter(TableFormPart part, List items, TableViewer viewer, IProject project) {
		super(part, items, viewer);
		this.project = project;
	}

	public void widgetSelected(SelectionEvent e) {
		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		if(sel.getFirstElement() != null && (sel.getFirstElement() instanceof SampleReg)){
			SampleReg sample = (SampleReg)sel.getFirstElement();
			
			Utils.removeFolderFromProject(project, sample.getTitle());
		}		
		
		super.widgetSelected(e);				
	}
	
}
