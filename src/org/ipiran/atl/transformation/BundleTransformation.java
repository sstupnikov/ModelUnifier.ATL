package org.ipiran.atl.transformation;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

public class BundleTransformation extends Transformation {
	private static final Logger log = Logger.getLogger(BundleTransformation.class);
	
	
	public BundleTransformation(Properties properties) {
		super(properties);
	}

	@Override
	protected String getMetamodelUri(String metamodelName) throws IOException {
		String pathInBundle = properties.getProperty("metamodels." + metamodelName);		
		return getFileURL(pathInBundle).getPath();
	}

	@Override
	protected URL getFileURL(String fileName) throws IOException {
		String bundle = properties.getProperty("bundle");
		URL bundleURL = Platform.getBundle(bundle).getEntry(fileName);
		log.info("Bundle URL: " + bundleURL); 
		
		URL url = FileLocator.toFileURL(bundleURL);
		log.info(url);
		return url;		
	}

}
