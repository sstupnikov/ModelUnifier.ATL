package org.ipiran.unifier.events;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.ide.IDE;
import org.ipiran.unifier.utils.LinkOpener;

public class OpenHyperlinkAdapter extends HyperlinkAdapter {
	private static final Logger log = Logger.getLogger(OpenHyperlinkAdapter.class); 	
	
	private Text text;
		
	public OpenHyperlinkAdapter(Text text){
		super();
		
		this.text = text;
	}
	
	
	public void linkActivated(HyperlinkEvent e) {
		//log.info("Link " + text.getText() + " activated."); //$NON-NLS-1$
		
		String path = text.getText();
		
		try {
				(new LinkOpener(path)).openLinkInBrowserOrEditor();
		} catch (Exception ex) {
			log.error("Browser or editor opening failed.");
			ex.printStackTrace();
		}
			
	}
}
