package org.ipiran.atl.transformation;

import java.util.Properties;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

public class TransformationPropertiesFactory {
	
	private Properties properties;
	
	public TransformationPropertiesFactory(){
		properties = new Properties();
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		
		setDefaultProperties();
	}
	
	private void setDefaultProperties(){				
		properties.setProperty("options.supportUML2Stereotypes", "false");
		properties.setProperty("options.printExecutionTime", "false");
		properties.setProperty("options.OPTION_CONTENT_TYPE", "false");
		properties.setProperty("options.allowInterModelReferences", "false");
		properties.setProperty("options.step", "false");		
	}
	
	public void addBundle(String bundleID){
		properties.setProperty("bundle", bundleID);
	}
	
	public void addMetamodel(String metamodelName, String metamodelPath){
		properties.setProperty("metamodels." + metamodelName, metamodelPath);
	}
	
	public void addInMetamodelName(String metamodelName){
		properties.setProperty("inMetamodelName", metamodelName);		
	} 

	public void addOutMetamodelName(String metamodelName){
		properties.setProperty("outMetamodelName", metamodelName);		
	} 
	
	public void addTransformationModule(String modulePath){
		properties.setProperty("modules", modulePath);		
	} 

	public void addLibrary(String libraryName, String libraryPath){
		properties.setProperty("libraries." + libraryName, libraryPath);
	}
	
	public void addProperty(String propertyName, String propertyValue){
		properties.setProperty(propertyName, propertyValue);
	}
	
	public Properties getProperties(){
		return properties;
	}
}
