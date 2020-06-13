package org.ipiran.unifier.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.internal.resources.ICoreConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.ide.IDE;
import org.ipiran.unifier.FileExtension;

/* Open URI in browser or 
 * open a file (located in current workspace) in an editor corresponded to file extension.
 */

public class LinkOpener {

	private static final Logger log = Logger.getLogger(LinkOpener.class);	
	
	private String spath;
	private IFile ifile; 
	
	public LinkOpener(String spath) throws NonLegalResourcePathException{	
		Path ppath = new Path(spath);
		
		if (ppath.segmentCount() < ICoreConstants.MINIMUM_FILE_SEGMENT_LENGTH){
			throw new NonLegalResourcePathException("Path must include project and resource name: " + ppath.toString());
		}
		
		this.spath = spath;
		this.ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(ppath);
	}	
	
	public void openLinkInBrowserOrEditor() throws CoreException, IOException{
		try {
			URL url = new URL(spath);				
			IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser(IWorkbenchBrowserSupport.AS_EDITOR, null, spath, "");
			browser.openURL(url);				
		} catch (MalformedURLException ex) {								    		    
			if(ifile.exists()){
				openEditor();
			}
			else{
				openNewFileInEditor();
			}
		} 				
	}	

	public String getFileContents(){
		String result = "";
		
		if(ifile.exists()){
			try {
				result = Utils.convertStreamToString(ifile.getContents());
			} catch (CoreException cex) {
				log.error("Getting contents from file failed: " + ifile.toString());
				cex.printStackTrace();
			} catch (IOException ioex) {
				log.error("Reading from file failed: " + ifile.toString());
				ioex.printStackTrace();
			}
		}
		
		return result;
	}
	
	public void openNewFileInEditor() throws CoreException, IOException{
		IPath workspaceDirectory = ResourcesPlugin.getWorkspace().getRoot().getLocation(); 
		IPath fullPath = workspaceDirectory.append(ifile.getFullPath());		
				
		File file = new File(fullPath.toOSString());
		file.createNewFile();		
		
		if(!ifile.exists())
			ifile.create(new FileInputStream(file), true, null);

		openEditor();
	}			
	
	public void openNewFileInEditor(InputStream template) throws CoreException, IOException{
		if(!ifile.exists()){
			ifile.create(template, true, null);
		}
		else{
			ifile.setContents(template, true, false, null);
		}

		openEditor();
	}
	
	public void openEditor() throws PartInitException{
		IFileStore fileStore = EFS.getLocalFileSystem().getStore(ifile.getLocationURI());
	    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();				 

	    IDE.openEditorOnFileStore(page, fileStore);		
	    
	}
	
}
