package org.ipiran.atl.transformation.synthesis2amn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.m2m.atl.common.ATLExecutionException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;
import org.ipiran.atl.transformation.TransformationPropertiesFactory;

public class Synthesis2AMN {

	private Properties properties;
	
	protected IModel inModel;	
	protected IModel outModel;	
		

	public Synthesis2AMN() throws IOException {
		TransformationPropertiesFactory factory = new TransformationPropertiesFactory();

		factory.addMetamodel("Synthesis", "Synthesis\\Synthesis.ecore");
		factory.addMetamodel("AMN", "Synthesis2AMN\\Metamodels\\AMN.ecore");
		factory.addInMetamodelName("Synthesis");
		factory.addOutMetamodelName("AMN");
		factory.addTransformationModule("Synthesis2AMN\\Transformations\\Synthesis2AMN.atl");
		factory.addProperty("amn2text", "Synthesis2AMN\\Transformations\\AMN2TextTest.asm");
		
		properties = factory.getProperties();
	}
	
	
	public void loadTransformSave(String inModelPath, String outModelPath) throws ATLExecutionException, ATLCoreException, IOException{
			loadModels(inModelPath);
			doAtlTransformation(new NullProgressMonitor());
			saveModels(outModelPath);
	}	
	
	/**
	 * Load the input and input/output models, initialize output models.
	 * 
	 * @param inModelPath
	 *            the IN model path
	 * @throws ATLCoreException
	 *             if a problem occurs while loading models
	 *
	 * @generated
	 */
	protected void loadModels(String inModelPath) throws ATLCoreException {
		ModelFactory factory = new EMFModelFactory();
		IInjector injector = new EMFInjector();
	 	IReferenceModel familiesMetamodel = factory.newReferenceModel();
		injector.inject(familiesMetamodel, getMetamodelUri(properties.getProperty("inMetamodelName")));
	 	IReferenceModel personsMetamodel = factory.newReferenceModel();
		injector.inject(personsMetamodel, getMetamodelUri(properties.getProperty("outMetamodelName")));
		this.inModel = factory.newModel(familiesMetamodel);
		injector.inject(inModel, inModelPath);
		this.outModel = factory.newModel(personsMetamodel);
	}
	
	/**
	 * Save the output and input/output models.
	 * 
	 * @param outModelPath
	 *            the OUT model path
	 * @throws ATLCoreException
	 *             if a problem occurs while saving models
	 *
	 * @generated
	 */
	public void saveModels(String outModelPath) throws ATLCoreException {
		IExtractor extractor = new EMFExtractor();
		extractor.extract(outModel, outModelPath);
	}

	/**
	 * Transform the models.
	 * 
	 * @param monitor
	 *            the progress monitor
	 * @throws ATLCoreException
	 *             if an error occurs during models handling
	 * @throws IOException
	 *             if a module cannot be read
	 * @throws ATLExecutionException
	 *             if an error occurs during the execution
	 *
	 * @generated
	 */
	public void doAtlTransformation(IProgressMonitor monitor) throws ATLCoreException, IOException, ATLExecutionException {
		// Launch Synthesis2AMN
		ILauncher synthesis2amnLauncher = new EMFVMLauncher();
		Map<String, Object> launcherOptions = getOptions();
		synthesis2amnLauncher.initialize(launcherOptions);
		synthesis2amnLauncher.addInModel(inModel, "IN", properties.getProperty("inMetamodelName"));
		synthesis2amnLauncher.addOutModel(outModel, "OUT", properties.getProperty("outMetamodelName"));
		synthesis2amnLauncher.launch("run", monitor, launcherOptions, (Object[]) getModulesList());
		
		// Launch AMN2Text
		ILauncher amn2textLauncher = new EMFVMLauncher();
		amn2textLauncher.initialize(launcherOptions);
		amn2textLauncher.addInModel(outModel, "IN", properties.getProperty("outMetamodelName"));
		amn2textLauncher.addLibrary("AMN2String", getLibraryAsStream("AMN2String"));		
		amn2textLauncher.launch("run", monitor, launcherOptions, getAmn2TextAsStream());				
	}
	
	/**
	 * Returns an Array of the module input streams, parameterized by the
	 * property file.
	 * 
	 * @return an Array of the module input streams
	 * @throws IOException
	 *             if a module cannot be read
	 *
	 * @generated
	 */
	protected InputStream[] getModulesList() throws IOException {
		InputStream[] modules = null;
		String modulesList = properties.getProperty("Synthesis2AMNTransformation.modules");
		if (modulesList != null) {
			String[] moduleNames = modulesList.split(",");
			modules = new InputStream[moduleNames.length];
			for (int i = 0; i < moduleNames.length; i++) {
				String asmModulePath = new Path(moduleNames[i].trim()).removeFileExtension().addFileExtension("asm").toString();
				modules[i] = getFileURL(asmModulePath).openStream();
			}
		}
		return modules;
	}
	
	/**
	 * Returns the URI of the given metamodel, parameterized from the property file.
	 * 
	 * @param metamodelName
	 *            the metamodel name
	 * @return the metamodel URI
	 *
	 * @generated
	 */
	protected String getMetamodelUri(String metamodelName) {
		return properties.getProperty("Synthesis2AMNTransformation.metamodels." + metamodelName);
	}

	protected InputStream getAmn2TextAsStream() throws IOException {
		return getFileURL(properties.getProperty("Synthesis2AMNTransformation.amn2text")).openStream();
	}	
	
	/**
	 * Returns the file name of the given library, parameterized from the property file.
	 * 
	 * @param libraryName
	 *            the library name
	 * @return the library file name
	 *
	 * @generated
	 */
	protected InputStream getLibraryAsStream(String libraryName) throws IOException {
		return getFileURL(properties.getProperty("Synthesis2AMNTransformation.libraries." + libraryName)).openStream();
	}
	
	/**
	 * Returns the options map, parameterized from the property file.
	 * 
	 * @return the options map
	 *
	 * @generated
	 */
	protected Map<String, Object> getOptions() {
		Map<String, Object> options = new HashMap<String, Object>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			if (entry.getKey().toString().startsWith("Synthesis2AMNTransformation.options.")) {
				options.put(entry.getKey().toString().replaceFirst("AtlTransformation.options.", ""), 
				entry.getValue().toString());
			}
		}
		return options;
	}
	
	/**
	 * Finds the file in the plug-in. Returns the file URL.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the file URL
	 * @throws IOException
	 *             if the file doesn't exist
	 * 
	 * @generated
	 */
	protected static URL getFileURL(String fileName) throws IOException {
		return (new File(fileName)).toURI().toURL();
	}
	
	
}
