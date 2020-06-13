package org.ipiran.unifier.utils;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

public class InputFileNameProcessor {

	private static final Logger log = Logger.getLogger(InputFileNameProcessor.class);
	
	private static final String SEPARATOR = "\\."; 
	
	private String fileName = null;
	private String fileNameWithoutExt = null;
	private String fileExt = null;
	
	public String getFileNameWithoutExt() {
		return fileNameWithoutExt;
	}

	public String getFileExt() {
		return fileExt;
	}

	
	public InputFileNameProcessor(String fileName) {
		this.fileName = fileName;
		
		try {			 
			String[] nameAndExtension = fileName.split(SEPARATOR);
			fileNameWithoutExt = nameAndExtension[0];
			fileExt = nameAndExtension[1];															
		} catch (Exception ex) {
			log.warn("Exception during file name and it's extension processing", ex);
		}				
	}
	
	public boolean check() {	
		if (fileNameWithoutExt == null || fileExt == null || fileNameWithoutExt.length() == 0 || fileExt.length() == 0) {
			log.warn("Check error for file: " + fileName);
			MessageDialog.openError(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"File name error", "File name does not look like NAME.EXT");
			return false;
		} else{
			return true;	
		}				
	}
	
}
