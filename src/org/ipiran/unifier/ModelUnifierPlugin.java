package org.ipiran.unifier;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ModelUnifierPlugin extends AbstractUIPlugin {
	private static final Logger log = Logger.getLogger(ModelUnifierPlugin.class); 	
	

	// The plug-in ID
	public static final String PLUGIN_ID = "Unifier";

	// The shared instance
	private static ModelUnifierPlugin plugin;

	
	// Plugin images
	public static final String IMG_DOC = "document"; 
	public static final String IMG_FILE_OBJ = "file_obj";	
	public static final String IMG_HORIZONTAL = "horizontal"; 
	public static final String IMG_MODEL_REGISTRAR = "model_registrar"; 
	public static final String IMG_PERSPECTIVE = "perspective"; 
	public static final String IMG_VERTICAL = "vertical";
	public static final String IMG_NEW_UNIFIER = "new_unifier"; 
	public static final String IMG_REF = "refinement"; 
	public static final String IMG_ENT = "entailment";	
	public static final String IMG_LEFT_ARROW = "left_arrow";	
	public static final String IMG_REFRESH = "refresh";	
	public static final String IMG_UNIFIER_48 = "unifier_48";
	public static final String IMG_UNIFIER_64 = "unifier_64";
	public static final String IMG_MODEL = "model";
	public static final String IMG_EXTENSION = "extension";	
	
	/**
	 * The constructor
	 */
	public ModelUnifierPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ModelUnifierPlugin getDefault() {
		return plugin;
	}

	protected void initializeImageRegistry(ImageRegistry registry) {
		registerImage(registry, IMG_PERSPECTIVE, "Unifier-icon-16.png"); 
		registerImage(registry, IMG_MODEL_REGISTRAR, "ModelRegistrar-icon-16.png");
		registerImage(registry, IMG_DOC, "Document-icon-16.png");
		registerImage(registry, IMG_HORIZONTAL, "th_horizontal.gif"); 
		registerImage(registry, IMG_VERTICAL, "th_vertical.gif"); 
		registerImage(registry, IMG_FILE_OBJ, "file_obj.gif"); 		
		registerImage(registry, IMG_NEW_UNIFIER, "NewUnifier-icon-16.png"); 
		registerImage(registry, IMG_REF, "Letter-R-gold-icon.png");		
		registerImage(registry, IMG_ENT, "Letter-E-dg-icon.png");		
		registerImage(registry, IMG_LEFT_ARROW, "left-arrow-32.png");
		registerImage(registry, IMG_REFRESH, "nav_refresh.gif");		
		registerImage(registry, IMG_UNIFIER_48, "Unifier-icon-48.png");		
		registerImage(registry, IMG_UNIFIER_64, "Unifier-icon-64.png");		
		registerImage(registry, IMG_MODEL, "model.png");		
		registerImage(registry, IMG_EXTENSION, "extension.png");		
		
	}

	private void registerImage(ImageRegistry registry, String key, String fileName) {
		try {
			IPath path = new Path("icons/" + fileName); //$NON-NLS-1$
			URL url = find(path);
			if (url!=null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(key, desc);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public Image getImage(String key) {
		return getImageRegistry().get(key);
	}
	public ImageDescriptor getImageDescriptor(String key) {
		return getImageRegistry().getDescriptor(key);
	}
	
}
