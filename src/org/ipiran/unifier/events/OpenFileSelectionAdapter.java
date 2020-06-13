package org.ipiran.unifier.events;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.PartInitException;
import org.ipiran.unifier.ModelRegistry.File;
import org.ipiran.unifier.utils.LinkOpener;
import org.ipiran.unifier.utils.NonLegalResourcePathException;

public class OpenFileSelectionAdapter extends SelectionAdapter {
	private static final Logger log = Logger.getLogger(OpenFileSelectionAdapter.class);
	
	protected TableViewer viewer;
	
	public OpenFileSelectionAdapter(TableViewer viewer){
		this.viewer = viewer;
	}
	
	public void widgetSelected(SelectionEvent e) {
		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		if(sel.getFirstElement() != null && sel.getFirstElement() instanceof File){	
			File file = (File)sel.getFirstElement();
			String path = file.getReference();
						
			try {
				(new LinkOpener(path)).openEditor();
			} catch (PartInitException ex) {
				log.error("File " + path + " opening failed.");
				ex.printStackTrace();
			} catch (NonLegalResourcePathException ex) {
				log.error(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}
