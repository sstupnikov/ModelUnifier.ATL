package org.ipiran.unifier.events;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.ipiran.unifier.utils.Utils;

public class BrowseFileSelectionAdapter extends SelectionAdapter {
	
	private static final Logger log = Logger.getLogger(BrowseFileSelectionAdapter.class);

	private IAdaptable root;
	private Text text;
	
	public BrowseFileSelectionAdapter(Text text, IAdaptable root){
		super();
		
		if(root == null){
			this.root = Utils.getWorkspaceRoot();
		} else{
			this.root = root;
		}
		
		this.text = text;
	}

	public BrowseFileSelectionAdapter(Text text){
		super();
		
		if(root == null){
			this.root = Utils.getWorkspaceRoot();
		} else{
			this.root = root;
		}
		
		this.text = text;
	}	
	
	public void widgetSelected(SelectionEvent e) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ResourceSelectionDialog rsd = new ResourceSelectionDialog(shell, root, "Select a file:");
		rsd.setTitle("Open");
		rsd.open();		
		
		Object[] selected = rsd.getResult(); 
        if(selected != null &&  selected.length != 0){
        	// Get first selected file
        	if(selected[0] instanceof IFile){
        		text.setText(((IFile)selected[0]).getFullPath().toOSString());
        	}
        }
	}
	
}
