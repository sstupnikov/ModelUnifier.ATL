package org.ipiran.unifier.events;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.ipiran.unifier.ModelRegistry.Document;
import org.ipiran.unifier.parts.TableFormPart;

public abstract class TableWithFixedListSelectionAdapter extends TableSelectionAdapter {
	private static final Logger log = Logger.getLogger(TableWithFixedListSelectionAdapter.class);

	protected List items;	
	
	// Required for Add and Remove Sample Adapters. Store project containing Sample folder.
	protected IProject project;
	
	TableWithFixedListSelectionAdapter(TableFormPart part, List items){
		super(part);
		
		this.items = items;
	}
	

}
