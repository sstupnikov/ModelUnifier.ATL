package org.ipiran.unifier.events;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.ipiran.unifier.ModelRegistry.Document;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.ModelRegistry.ModelRegistryFactory;
import org.ipiran.unifier.parts.TableFormPart;

public abstract class AddEObjectSelectionAdapter extends TableWithFixedListSelectionAdapter {
	private static final Logger log = Logger.getLogger(RemoveItemSelectionAdapter.class);
	
	public AddEObjectSelectionAdapter(TableFormPart part, List items){
		super(part, items);
	}
		
	public void widgetSelected(SelectionEvent e) {
		log.info("Add item button pressed.");
		
		EObject obj = createItem(); 
		
		if (obj != null){
			items.add(obj);
			part.markDirty();
		}						
	}

	// Returns null if item creation failed. 
	protected abstract EObject createItem();	
}
