package org.ipiran.unifier.wizards;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IIdentifier;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.dialogs.WizardNewProjectReferencePage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.ide.IDEInternalPreferences;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.StatusUtil;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.internal.wizards.newresource.ResourceMessages;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelUnifierPlugin;
import org.ipiran.unifier.ModelRegistry.ModelRegistryFactory;
import org.ipiran.unifier.ModelRegistry.RegistrationCard;
import org.ipiran.unifier.ModelRegistry.UnificationReg;
import org.ipiran.unifier.utils.LinkOpener;
import org.ipiran.unifier.utils.NonLegalResourcePathException;


public abstract class NewProjectWizard extends BasicNewResourceWizard  implements INewWizard {
	private static final Logger log = Logger.getLogger(NewProjectWizard.class);
	
	public static final String WIZARD_ID = "org.ipiran.unifier.wizards.NewProject"; //$NON-NLS-1$

	private static String WINDOW_PROBLEMS_TITLE = ResourceMessages.NewProject_errorOpeningWindow;
	
	protected NewProjectWizardPage mainPage;

	// cache of newly-created project
	private IProject newProject;
	
	public NewProjectWizard() {
		IDialogSettings workbenchSettings = IDEWorkbenchPlugin.getDefault().getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection("NewProjectWizard");
		if (section == null) {
			section = workbenchSettings.addNewSection("NewProjectWizard");
		}
		setDialogSettings(section);
		
		this.setWindowTitle(getWindowTitle());
	}

	public abstract String getWindowTitle();
	protected abstract String getMainPageTitle();
	protected abstract String getMainPageDesc();	
	protected abstract FileExtension getRegCardFileExtension();
	protected abstract RegistrationCard createRegistrationCard();
	protected abstract void createProjectContent() throws CoreException;
	
	public void addPages() {
		super.addPages();
		
		mainPage = new NewProjectWizardPage("NewProjectPage", getRegCardFileExtension()); 
		mainPage.setTitle(getMainPageTitle());
		mainPage.setDescription(getMainPageDesc());
		this.addPage(mainPage);				
	}

	/**
	 * Creates a new project resource with the selected name.
	 * <p>
	 * In normal usage, this method is invoked after the user has pressed Finish
	 * on the wizard; the enablement of the Finish button implies that all
	 * controls on the pages currently contain valid values.
	 * </p>
	 * <p>
	 * Note that this wizard caches the new project once it has been
	 * successfully created; subsequent invocations of this method will answer
	 * the same project resource without attempting to create it again.
	 * </p>
	 * 
	 * @return the created project resource, or <code>null</code> if the
	 *         project was not created
	 */
	private IProject createNewProject() {
		if (newProject != null) {
			return newProject;
		}

		// get a project handle
		final IProject newProjectHandle = mainPage.getProjectHandle();

		// get a project descriptor
		URI location = mainPage.getLocationURI();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription description = workspace.newProjectDescription(newProjectHandle.getName());
		description.setLocationURI(location);

		log.info("Project name: " + newProjectHandle.getName());
		//log.info("Location: " + location.toString());
		
		// create the new project operation
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				CreateProjectOperation op = new CreateProjectOperation(description, ResourceMessages.NewProject_windowTitle);
				try {
					op.execute(monitor, WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
				} catch (ExecutionException e) {
					throw new InvocationTargetException(e);
				}
			}
		};

		// run the new project creation operation
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			log.error("Project creation failed.");
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			log.error("Project creation failed.");
			e.printStackTrace();
			return null;
		}

		newProject = newProjectHandle;

		return newProject;
	}

	/**
	 * Returns the newly created project.
	 * 
	 * @return the created project, or <code>null</code> if project not
	 *         created
	 */
	public IProject getNewProject() {
		return newProject;
	}

	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		super.init(workbench, currentSelection);
		setNeedsProgressMonitor(true);
	}

	protected void initializeDefaultPageImageDescriptor() {
		ImageDescriptor desc = ModelUnifierPlugin.getDefault().getImageDescriptor(ModelUnifierPlugin.IMG_UNIFIER_64);
		setDefaultPageImageDescriptor(desc);
	}


	public boolean performFinish() {
		createNewProject();

		if (newProject == null) {
			return false;
		}
				
		try {
			saveRegistrationCard();
			openRegistrationCardInEditor();
			createProjectContent();
		} catch (IOException e) {
			log.error("Reg Card saving failed.");
			e.printStackTrace();
			return false;
		} catch (PartInitException e) {
			log.error("Reg card opening failed.");
			e.printStackTrace();
		} catch (NonLegalResourcePathException e) {
			log.error("Reg card opening failed.");
			e.printStackTrace();
		} catch (CoreException e) {
			log.error("Project content creation failed.");
			e.printStackTrace();
		}							
		
		//selectAndReveal(newProject);

		return true;
	}

	protected String getCardFilePath(){
		return 
			IPath.SEPARATOR + mainPage.getProjectName() + 
			IPath.SEPARATOR + mainPage.getProjectName() + "." +
			getRegCardFileExtension().toString();
	}	
			
	protected void saveRegistrationCard() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(getRegCardFileExtension().toString(), new XMIResourceFactoryImpl());
		Resource resource = (new ResourceSetImpl()).createResource(org.eclipse.emf.common.util.URI.createFileURI(getCardFilePath()));
		resource.getContents().add(createRegistrationCard());											
		resource.save(Collections.EMPTY_MAP);
	}	
	
	protected void openRegistrationCardInEditor() throws NonLegalResourcePathException, PartInitException{
		LinkOpener opener;
		
		opener = new LinkOpener(getCardFilePath());
		opener.openEditor();
	}

}
