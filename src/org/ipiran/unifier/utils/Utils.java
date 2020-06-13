package org.ipiran.unifier.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormPage;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelUnifierPlugin;
import org.ipiran.unifier.editors.Registrar;

public class Utils {
	private static final Logger log = Logger.getLogger(Utils.class); 		
	
	public static String getExtension(File f){
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
	
		if (i > 0 && i < s.length() - 1)
			ext = s.substring(i+1).toLowerCase();
	
		if(ext == null)
			return "";
		
		return ext;
	}	
	
	public static Display getDisplay() {
		Display display = Display.getCurrent();
		
		//may be null if outside the UI thread
		if (display == null)
			display = Display.getDefault();
		
		return display;		
   }

	public static IWorkspaceRoot getWorkspaceRoot(){
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	public static IPath getWorkspacePath(){
		return ResourcesPlugin.getWorkspace().getRoot().getLocation();		
	}
	
	public static Shell getShell(){
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	
	public static IWorkbench getWorkbench(){
		return PlatformUI.getWorkbench();
	}
	

	// COLORS
	public static Color getSkyBlueColor(){
		return new Color(Utils.getDisplay(), 135, 206, 235);
	}
	
	public static Color getRoyalBlueColor(){
		return new Color(Utils.getDisplay(), 65, 105, 225);
	}

	public static Color getBlueColor(){
		return new Color(Utils.getDisplay(), 0, 0, 100);
	}
	
	// Returns null if chosen file is located outside the workspace.
	public static String openFileInWorkspace(Set<FileExtension> extensions){
		String workspacePath = Utils.getWorkspacePath().toOSString(); 
		String chosenLocalPath = null;
		
		FileDialog fd = new FileDialog(Utils.getShell(), SWT.OPEN);
        fd.setText("Open file in the workspace");
        fd.setFilterPath(workspacePath);                
       
        // Set extension filter
        List<String> exts = new ArrayList<String>();
        for(FileExtension ext: extensions){
        	exts.add("*." + ext.toString());
        }
        String[] filterExt = new String[exts.size()];  
        exts.toArray(filterExt);          		
                
        fd.setFilterExtensions(filterExt);
        
        String selected = fd.open();        
        if(selected != null){
        	log.info("Selected: " + selected);
        	log.info("workspacePath: " + workspacePath);
        	if(selected.startsWith(workspacePath)){
        		chosenLocalPath = selected.substring(workspacePath.length());        		
        	}
        	else{
        		MessageDialog.openWarning(Utils.getShell(), "Warning", "Chosen file is located outside the workspace.");
        	}        	
        }    
        
        return chosenLocalPath;
	}
	
	
	public static String convertStreamToString(InputStream is)	throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {        
			return "";
		}
	}
	
	public static IProject getCurrentProject(FormPage page){
		
		IPath path = ((Registrar)page.getEditor()).getModelPath();
		log.info("Model path: " + path.toOSString());
		IFile ifile = Utils.getWorkspaceRoot().getFile(path);
		log.info("IFile exists: " + ifile.exists());
		IProject iproject = ifile.getProject(); 
		log.info("IProject: " + iproject.getName());

		return iproject;
	}
	
	public static boolean isFilenameValid(String fileName) {
	    final File aFile = new File(fileName);
	    boolean isValid = true;
	    try {
	        if (aFile.createNewFile()) {
	            aFile.delete();
	        }
	    } catch (IOException e) {
	        isValid = false;
	    }
	    return isValid;		
	}
	
	public static void openWizardDialog(IWorkbenchWizard wizard){
		wizard.init(PlatformUI.getWorkbench(), null);
	    WizardDialog dialog = new WizardDialog(getShell(), wizard);
	    dialog.setDefaultImage(ModelUnifierPlugin.getDefault().getImage(ModelUnifierPlugin.IMG_PERSPECTIVE));
	    dialog.create();
	    dialog.open();		
	}
	
	public static void createFolderInProject(IProject project, String folderName){
		if(project != null && isFilenameValid(folderName)){
			IFolder folder = project.getFolder(folderName);
			if(folder != null && !folder.exists()){
				try {
					folder.create(true, true, null);
				} catch (CoreException e) {
					log.error("Creation of a folder " + folderName + " in a project " + project.getName() + " failed.");
					e.printStackTrace();
				}
			}
		}		
	}
	
	public static void removeFolderFromProject(IProject project, String folderName){
		log.info("Removing folder.");
		if(project != null && isFilenameValid(folderName)){
			IFolder folder = project.getFolder(folderName);
			if(folder != null && folder.exists()){
				try {
					folder.delete(true, null);	
					log.info("Folder is deleted.");
				} catch (CoreException e) {
					log.error("Deleting of a folder " + folderName + " in a project " + project.getName() + " failed.");
					e.printStackTrace();
				}
			}
		}				
	}
}
