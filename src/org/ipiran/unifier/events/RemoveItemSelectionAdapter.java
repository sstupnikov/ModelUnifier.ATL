package org.ipiran.unifier.events;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.ipiran.unifier.ModelRegistry.Document;
import org.ipiran.unifier.parts.TableFormPart;

public class RemoveItemSelectionAdapter extends TableWithFixedListSelectionAdapter {
	private static final Logger log = Logger.getLogger(RemoveItemSelectionAdapter.class);

	protected TableViewer viewer = null;
	
	public RemoveItemSelectionAdapter(TableFormPart part, List items, TableViewer viewer){
		super(part, items);
		
		this.viewer = viewer;
	}

	
	public void widgetSelected(SelectionEvent e) {
		log.info("Remove item button pressed.");
		
		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		if(sel.getFirstElement() != null){
			items.remove(sel.getFirstElement());
			part.markDirty();
		}				
	}

}
