package org.ipiran.unifier.events;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.m2m.atl.adt.ui.wizard.atlproject.AtlProjectCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelUnifierPlugin;
import org.ipiran.unifier.utils.LinkOpener;
import org.ipiran.unifier.utils.NonLegalResourcePathException;
import org.ipiran.unifier.utils.Utils;
import org.ipiran.unifier.wizards.NewAbstractSyntaxFileWizard;
import org.ipiran.unifier.wizards.NewFileWizard;
import org.ipiran.unifier.wizards.NewFileWizardDefaultImpl;
import org.ipiran.unifier.wizards.NewModelRegCardWizard;
import org.emftext.sdk.ui.wizards.*;

public class NewFileSelectionAdapter extends SelectionAdapter {
	private static final Logger log = Logger.getLogger(NewFileSelectionAdapter.class); 			

	private Text text;
	private IPath path;
	private String name;
	private Set<FileExtension> extensions;
	
	public NewFileSelectionAdapter(Text text, IPath path, String name, Set<FileExtension> exts){
		super();

		this.text = text;		
		this.path = path;
		
		if(name == null || name.trim().compareTo("") == 0)
			this.name = "Model";
		else
			this.name = name;
		
		this.extensions = exts;
	}

	public NewFileSelectionAdapter(Text text, Set<FileExtension> exts){
		super();

		this.text = text;				
		this.extensions = exts;
	}
	
	
	public void widgetSelected(SelectionEvent e) {
		log.info("New file selected.");
		
		//if(name == null) log.info("Null name");	else log.info(name);
		//if(extension == null) log.info("Null ext"); 	else log.info(extension);
		//if(path == null) log.info("Null path");
				
		if (extensions.size() == 1 && extensions.contains(FileExtension.ATL)) {
			openNewWizardDialog(new AtlProjectCreator());
			text.setText("");
		} else if (extensions.size() == 1 && extensions.contains(FileExtension.CS)) {
			openNewWizardDialog(new NewProjectWizard());			
		} else {
			openFileDialog();
			text.setText("");
		}	
	}

	private void openNewWizardDialog(IWorkbenchWizard wizard){
		wizard.init(Utils.getWorkbench(), null);
	    WizardDialog dialog = new WizardDialog(Utils.getShell(), wizard);
	    dialog.create();
	    dialog.open();				
	}
	
	
	private void openFileDialog(){
        String chosenLocalpath = Utils.openFileInWorkspace(extensions); 
        
        if(chosenLocalpath != null){
			text.setText(chosenLocalpath);
			
			try {				
				LinkOpener opener = new LinkOpener(chosenLocalpath);
				
				// For Ecore files it is required to create a file with a specific template
				if (extensions.contains(FileExtension.ECORE) && extensions.size() == 1) {
					opener.openNewFileInEditor(FileLocator.openStream(ModelUnifierPlugin.getDefault().getBundle(), 
							new Path("resources/abstract_syntax_template.ecore"), false));    
				}
				// Other files may be opened empty.
				else{
					opener.openNewFileInEditor();
				}				
			} catch (CoreException ex) {
				log.error("New file " + path + " opening failed.");
				ex.printStackTrace();
			} catch (IOException ex) {
				log.error("New file " + path + " opening failed.");
				ex.printStackTrace();
			} catch (NonLegalResourcePathException ex) {
				log.error(ex.getMessage());
				ex.printStackTrace();
			}
        }
        
	}
}
