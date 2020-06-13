package org.ipiran.unifier.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.ipiran.unifier.ModelRegistry.EntailmentSampleReg;
import org.ipiran.unifier.ModelRegistry.ExtensionReg;
import org.ipiran.unifier.ModelRegistry.File;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.ModelRegistry.ModelRegistryFactory;
import org.ipiran.unifier.ModelRegistry.RefinementSampleReg;
import org.ipiran.unifier.ModelRegistry.SampleReg;
import org.ipiran.unifier.ModelRegistry.UnificationReg;

public class ModelRegistryUtils {
	private static final Logger log = Logger.getLogger(ModelRegistryUtils.class);

	private static ModelRegistryFactory factory = ModelRegistryFactory.eINSTANCE;
	
	// Deprecated
	public static void createFilesForModel(ModelReg model){				
		if(model.getAbstractSyntax() == null)
			model.setAbstractSyntax(factory.createFile());
		if(model.getConcreteSyntax() == null)
			model.setConcreteSyntax(factory.createFile());
		if(model.getSemanticMapping() == null)
			model.setSemanticMapping(factory.createFile());
		if(model.getSemanticTransformation() == null)
			model.setSemanticTransformation(factory.createFile());
		if(model.getSemanticSpec() == null)
			model.setSemanticSpec(factory.createFile());				
	}

	// Deprecated	
	public static void createFilesForModel(ExtensionReg model){
		createFilesForModel((ModelReg)model);
		
		if(model.getExtendedModel() == null)
			model.setExtendedModel(factory.createFile());				
		
	}

	// Deprecated	
	public static void createFilesForModel(UnificationReg model){	
		if(model.getSource() == null)
			model.setSource(factory.createFile());		
		if(model.getTarget() == null)
			model.setTarget(factory.createFile());				
		if(model.getSimilarities() == null)
			model.setSimilarities(factory.createFile());		
		if(model.getProof() == null)
			model.setProof(factory.createFile());		
		if(model.getMapping() == null)
			model.setMapping(factory.createFile());
		if(model.getTransformationS2T() == null)
			model.setTransformationS2T(factory.createFile());
		if(model.getTransformationT2S() == null)
			model.setTransformationT2S(factory.createFile());		
		
	}

	// Deprecated	
	public static void createFilesForModel(RefinementSampleReg model){	
		if(model.getAbstractSourceSpec() == null)
			model.setAbstractSourceSpec(factory.createFile());		
		if(model.getAbstractTargetSpec() == null)
			model.setAbstractTargetSpec(factory.createFile());		
		if(model.getConcreteSourceSpec() == null)
			model.setConcreteSourceSpec(factory.createFile());		
		if(model.getConcreteTargetSpec() == null)
			model.setConcreteTargetSpec(factory.createFile());		
		if(model.getProof() == null)
			model.setProof(factory.createFile());		
	}	

	// For every null <object>'s reference of type <File>
	// create an instance of <File>.
	public static void createFilesForEObject(EObject object){
		log.info("createFilesForEObject: " + object.eClass().getName());
		
		for(EReference ref : object.eClass().getEAllReferences()) {			
			if(ref.getEReferenceType().getName().compareTo("File") == 0
				&& ref.getUpperBound() == 1
				&& object.eGet(ref) == null){
				log.info("Reference: " + ref.getName());
				object.eSet(ref, factory.createFile());	
				assert object.eGet(ref) != null;
			}
		}		
	}		
	
	public static List<EntailmentSampleReg> getEntailmentSamples(List<SampleReg> items){
		List<EntailmentSampleReg> result = new ArrayList<EntailmentSampleReg>();
		
		for(SampleReg sample: items){
			if(sample instanceof EntailmentSampleReg){
				result.add((EntailmentSampleReg)sample);
			}
		}
		
		return result;
	}

	public static List<RefinementSampleReg> getRefinementSamples(List<SampleReg> items){
		List<RefinementSampleReg> result = new ArrayList<RefinementSampleReg>();
		
		for(SampleReg sample: items){
			if(sample instanceof RefinementSampleReg){
				result.add((RefinementSampleReg)sample);
			}
		}
		
		return result;
	}

}
