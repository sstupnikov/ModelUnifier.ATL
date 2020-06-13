package org.ipiran.unifier.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.ipiran.unifier.editors.FormEditorInput;

public abstract class OpenFormEditorAction implements
		IWorkbenchWindowActionDelegate {

	private static final Logger log = Logger.getLogger(OpenFormEditorAction.class); 	
	
	private IWorkbenchWindow window;
	
	
	protected void openEditor(String inputName, String editorId) {
		openEditor(new FormEditorInput(inputName), editorId);
	}
	
	protected void openEditor(IEditorInput input, String editorId) {
		IWorkbenchPage page = window.getActivePage();
		try {
			page.openEditor(input, editorId);
		} catch (PartInitException e) {
			log.error(e);
		}
	}
	
	protected IWorkbenchWindow getWindow() {
		return window;
	}
	/**
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
	/**
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}
	/**
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
