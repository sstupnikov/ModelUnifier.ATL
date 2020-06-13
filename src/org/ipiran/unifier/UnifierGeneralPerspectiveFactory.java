package org.ipiran.unifier;

import org.apache.log4j.Logger;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class UnifierGeneralPerspectiveFactory implements IPerspectiveFactory {
	private static final Logger log = Logger.getLogger(UnifierGeneralPerspectiveFactory.class); 	
	

	@Override
	public void createInitialLayout(IPageLayout layout) {

		// Editors are placed for free.
        String editorArea = layout.getEditorArea();

        // Place project explorer to left of editor area.
        IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, (float) 0.3, editorArea);
        left.addView(IPageLayout.ID_PROJECT_EXPLORER);
		

	}

}
