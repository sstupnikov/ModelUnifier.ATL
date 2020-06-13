package org.ipiran.unifier.events;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.ipiran.unifier.ModelRegistry.Document;
import org.ipiran.unifier.ModelRegistry.EntailmentSampleReg;
import org.ipiran.unifier.ModelRegistry.ModelRegistryFactory;
import org.ipiran.unifier.ModelRegistry.RefinementSampleReg;
import org.ipiran.unifier.ModelRegistry.SampleReg;
import org.ipiran.unifier.parts.TableFormPart;
import org.ipiran.unifier.utils.ModelRegistryUtils;
import org.ipiran.unifier.utils.Utils;
import org.ipiran.unifier.wizards.NewEntailmentSampleWizard;
import org.ipiran.unifier.wizards.NewRefinementSampleWizard;

public class AddRefSampleSelectionAdapter extends AddEObjectSelectionAdapter {

	public AddRefSampleSelectionAdapter(TableFormPart part, List<SampleReg> samples, IProject uniProject) {
		super(part, samples);
		this.project = uniProject;		
	}

	@Override
	protected EObject createItem() {
		RefinementSampleReg sample = null;

		NewRefinementSampleWizard wizard = new NewRefinementSampleWizard(project.getName()); 	
		Utils.openWizardDialog(wizard);		
		
		if(!wizard.isCancelled()){
			ModelRegistryFactory factory = ModelRegistryFactory.eINSTANCE;
			sample = factory.createRefinementSampleReg();
			sample.setTitle(wizard.getSampleName());

			ModelRegistryUtils.createFilesForEObject(sample);
			
			Utils.createFolderInProject(project, wizard.getSampleName());
		}		
				
		return sample;							
	}

}
