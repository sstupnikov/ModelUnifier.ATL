package org.ipiran.unifier.utils;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class InputFileLoader{
	
	private static final Logger log = Logger.getLogger(InputFileLoader.class);
	
	private IResource fileToLoad = null;
	private Resource resource = null;
	private EObject eobject = null;
	String fileNameWithoutExt = null;
	String fileExt = null;
		
	public String getFileNameWithoutExt() {
		return fileNameWithoutExt;
	}

	public String getFileExt() {
		return fileExt;
	}
	
	public Resource getResource() {
		return resource;
	}

	public EObject getEObject() {
		return eobject;
	}

	public InputFileLoader(IResource fileToLoad) {
		this.fileToLoad = fileToLoad;
	}
	
	public boolean load() {
		
		if (fileToLoad != null) {

			InputFileNameProcessor nameProcessor = new InputFileNameProcessor(fileToLoad.getName());
			if (nameProcessor.check()) {
				fileNameWithoutExt = nameProcessor.getFileNameWithoutExt();
				fileExt = nameProcessor.getFileExt();
			} else {
				return false;
			}	
			
			resource = loadResourceFromFile(fileToLoad.getLocation().toOSString());
			eobject = loadEObjectFromResource();
			
			
		} else {		
			log.warn("File to load is null");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Loads file's contents into emf resource. 
	 * If all is ok, resource with contents returned
	 */
	private Resource loadResourceFromFile(String osString) {
		ResourceSet rset = new ResourceSetImpl();		
		rset.getResourceFactoryRegistry().getProtocolToFactoryMap().put("*", new XMIResourceFactoryImpl());
		
		log.info("File to load: " + osString);
		
		try {
			Resource res = rset.getResource(URI.createFileURI(osString), true);			
			res.load(null);
			return res;
		} catch (Exception ex) {
			log.warn("Can not load resource from file: " + osString);
			log.info(ex.toString());
		}
		
		return null;
	}
	
	private EObject loadEObjectFromResource(){		
		if(resource != null){
			eobject = resource.getContents().get(0); 						
		}
		
		return eobject;
	}


	
}