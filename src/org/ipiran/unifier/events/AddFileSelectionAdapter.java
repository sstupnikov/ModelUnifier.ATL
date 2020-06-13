package org.ipiran.unifier.events;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.events.SelectionEvent;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelRole;
import org.ipiran.unifier.ModelRegistry.Document;
import org.ipiran.unifier.ModelRegistry.File;
import org.ipiran.unifier.ModelRegistry.ModelRegistryFactory;
import org.ipiran.unifier.ModelRegistry.RefinementSampleReg;
import org.ipiran.unifier.editors.WrongMasterValueTypeException;
import org.ipiran.unifier.parts.TableFormPart;
import org.ipiran.unifier.utils.Utils;

public class AddFileSelectionAdapter extends ModifyMasterSelectionAdapter {
	private static final Logger log = Logger.getLogger(AddFileSelectionAdapter.class);	

	protected Set<FileExtension> extensions;
	
	public AddFileSelectionAdapter(TableFormPart part, IObservableValue master, ModelRole role, Set<FileExtension> extensions) {
		super(part, master, role);
		
		this.extensions = extensions;				
	}

	public void widgetSelected(SelectionEvent e) {
		ModelRegistryFactory factory = ModelRegistryFactory.eINSTANCE;
		File file = factory.createFile();
				
		if (!(master.getValue() instanceof RefinementSampleReg)){
			throw new WrongMasterValueTypeException("Master value is not of type RefinementSampleReg.");
		}
		
		RefinementSampleReg sample = (RefinementSampleReg)master.getValue();		

		String chosenFilePath = Utils.openFileInWorkspace(extensions);		
		
		if (chosenFilePath != null && (new java.io.File(Utils.getWorkspacePath().toOSString() + chosenFilePath)).exists()){
			file.setReference(chosenFilePath);		
									
			switch(role){
			case SOURCE:
				sample.getSourceSemSpec().add(file);
				break;
			case TARGET:
				sample.getTargetSemSpec().add(file);
				break;
			}
			
			part.markDirty();
		} else {
			log.info("File " + chosenFilePath + " does not exist.");
		}
		
	}

}
