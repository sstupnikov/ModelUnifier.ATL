package org.ipiran.unifier.editors;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xml.type.impl.XMLTypeFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.ModelRegistry.RegistrationCard;
import org.ipiran.unifier.utils.InputFileLoader;

public abstract class Registrar extends FormEditor {
	
	private static final Logger log = Logger.getLogger(Registrar.class); 
	
	private Resource resource = null;
	private IPath modelPath = null;	

	public abstract RegistrationCard getModel(); 
	public abstract void setModel(RegistrationCard model) throws WrongRegistrarInputException;
	
	public abstract String getTitle();
	
	public IPath getModelPath(){
		return modelPath;
	}	
	
	public Resource getResource(){
		return resource;
	}
	
	@Override
	protected void setInput(IEditorInput input){
		super.setInput(input);
				
		if(input instanceof IFileEditorInput){
			IFile file = null;

			try {
				file = ((IFileEditorInput) input).getFile();													
			} catch (Exception ex) {
				log.error("Exception during file loading", ex);
				return;
			}
			
			InputFileLoader fileLoader = new InputFileLoader(file);
			if (fileLoader.load()) {
				resource = fileLoader.getResource();
				if(resource == null) 
					log.error("Input resource is null.");
				
				try {
					setModel((RegistrationCard)fileLoader.getEObject());
				} catch (WrongRegistrarInputException e) {
					log.error("Wrong Registrar Input");
					e.printStackTrace();
				}
				
				if(getModel() != null){
					createFilesForModel();
				} 					
				else{
					log.error("Input model is null.");
				}
									
				modelPath = file.getFullPath();
				if(modelPath == null){
					log.error("Input file path is null.");
					this.setPartName(getTitle());
				}
				
				this.setPartName(getTitle() + " - " + modelPath.toOSString());
				
			}			
		}
		else{
			log.error("Wrong input for " + getTitle() + ".");
		}		
	}

	protected abstract void createFilesForModel();	
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		//log.info("ModelRegistrar.doSave");				
		
		commitPages(true);
								
		//log.info(this.isDirty());		
										
		putExtensionToRegistry();
		Resource resource = (new ResourceSetImpl()).createResource(URI.createFileURI(modelPath.toOSString()));
		resource.getContents().add(getModel());									
		
		// Save the content.
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		this.editorDirtyStateChanged();				
	}

	protected abstract void putExtensionToRegistry();
	
	protected void commitPages(boolean onSave){
		super.commitPages(onSave);

		if (pages != null) {
			for (int i = 0; i < pages.size(); i++) {
				Object page = pages.get(i);
				if (page instanceof IFormPage) {
					IFormPage fpage = (IFormPage)page;
					fpage.doSave(null);
				}
			}
		}			
	}

	@Override
	public void doSaveAs() {
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}		
}
