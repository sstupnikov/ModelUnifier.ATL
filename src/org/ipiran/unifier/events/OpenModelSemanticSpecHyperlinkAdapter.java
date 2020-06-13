package org.ipiran.unifier.events;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.utils.InputFileLoader;
import org.ipiran.unifier.utils.LinkOpener;
import org.ipiran.unifier.utils.NonLegalResourcePathException;

public class OpenModelSemanticSpecHyperlinkAdapter extends HyperlinkAdapter {
	private static final Logger log = Logger.getLogger(OpenModelSemanticSpecHyperlinkAdapter.class);
		
	protected Text text;
	protected ModelReg card;
	
	public OpenModelSemanticSpecHyperlinkAdapter(Text text){
		this.text = text;
	}
	
	public void linkActivated(HyperlinkEvent e) {								
		card = loadModelFormFile();
		if(card != null){
			try {
				(new LinkOpener(card.getSemanticSpec().getReference())).openEditor();
			} catch (PartInitException ex) {
				log.error("Editor opening failed.");
				ex.printStackTrace();
			} catch (NonLegalResourcePathException ex) {
				log.error(ex.getMessage());
				ex.printStackTrace();
			}					
		}			    
	}				

	private ModelReg loadModelFormFile(){
		IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(text.getText()));								
		InputFileLoader fileLoader = new InputFileLoader(ifile);
		if (fileLoader.load()) {
			if(fileLoader.getEObject() instanceof ModelReg){
				card = (ModelReg)fileLoader.getEObject();
			} else{
				log.error("Loaded model is not of type ModelReg: " + ifile.getName());
			}					
		} else{
			log.error("Failed loading resource: " + ifile.getName());
		}		
		
		return card;
	}
	
	
}
