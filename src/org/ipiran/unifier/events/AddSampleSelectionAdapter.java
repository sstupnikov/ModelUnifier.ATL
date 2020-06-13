package org.ipiran.unifier.events;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.ipiran.unifier.VerificationMethod;
import org.ipiran.unifier.ModelRegistry.ModelRegistryFactory;
import org.ipiran.unifier.ModelRegistry.RefinementSampleReg;
import org.ipiran.unifier.ModelRegistry.SampleReg;
import org.ipiran.unifier.parts.TableFormPart;

public class AddSampleSelectionAdapter extends AddEObjectSelectionAdapter  {

	protected VerificationMethod method;
	
	public AddSampleSelectionAdapter(TableFormPart part, List<SampleReg> items, VerificationMethod method) {
		super(part, items);
		
		this.method = method;
	}

	@Override
	protected EObject createItem() {
		ModelRegistryFactory factory = ModelRegistryFactory.eINSTANCE;
		
		SampleReg sample = null;
		
		switch(method){
			case REFINEMENT:
				sample = factory.createRefinementSampleReg();
				sample.setTitle("new_ref_sample");
				break;
			case ENTAILMENT:
				sample = factory.createEntailmentSampleReg();
				sample.setTitle("new_ent_sample");
				break;
		}
						
		return sample;
	}


}
