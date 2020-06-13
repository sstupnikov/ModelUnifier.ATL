package org.ipiran.atl.transformation;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class LocalTransformation extends Transformation {

	public LocalTransformation(Properties properties) {
		super(properties);
	}
	
	protected String getMetamodelUri(String metamodelName) {
		return properties.getProperty("metamodels." + metamodelName);
	}

	protected URL getFileURL(String fileName) throws IOException {
		return (new File(fileName)).toURI().toURL();
	}
	

}
