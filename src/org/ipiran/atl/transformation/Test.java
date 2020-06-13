package org.ipiran.atl.transformation;

import java.io.IOException;

import org.eclipse.m2m.atl.common.ATLExecutionException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.ipiran.atl.transformation.synthesis2amn.Synthesis2AMNWithExternalProperties;

public class Test {

	public static void main(String[] args) {
		try {
			
			//runWithExternalProperties();
			
			runFamilies2Persons();
			
		} catch (ATLCoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ATLExecutionException e) {
			e.printStackTrace();
		}
	}	
	
	private static void runWithExternalProperties() throws IOException, ATLExecutionException, ATLCoreException{
		AtlTransformationWithExternalProperties runner1 = new AtlTransformationWithExternalProperties("Families2Persons\\AtlTransformation.properties", "Families", "Persons");
		runner1.loadTransformSave("Families2Persons\\sample-Families.xmi", "Families2Persons\\sample-Persons1.xmi");
		
		Synthesis2AMNWithExternalProperties runner2 = new Synthesis2AMNWithExternalProperties("org.ipiran.atl.transformation\\src\\org\\ipiran\\atl\\transformation\\synthesis2amn\\Synthesis2AMNTransformation.properties", "Synthesis", "AMN");
		runner2.loadTransformSave("Synthesis2AMN\\Sample\\agency.xmi", "Synthesis2AMN\\Sample\\agency-amn.xmi");					
	}
	
	private static void runFamilies2Persons() throws ATLExecutionException, ATLCoreException, IOException{
		TransformationPropertiesFactory factory = new TransformationPropertiesFactory();
		
		factory.addTransformationModule("Families2Persons\\atlTransformation.asm");
		factory.addMetamodel("Families", "Families2Persons\\Families.ecore");
		factory.addMetamodel("Persons", "Families2Persons\\Persons.ecore");
		factory.addInMetamodelName("Families");
		factory.addOutMetamodelName("Persons");
		
		Transformation runner = new LocalTransformation(factory.getProperties());
		runner.loadTransformSave("Families2Persons\\sample-Families.xmi", "Families2Persons\\sample-Persons1.xmi");
	}
}
