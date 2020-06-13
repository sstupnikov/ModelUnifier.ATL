package org.ipiran.unifier.wizards;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea.IErrorMessageReporter;
import org.eclipse.ui.internal.ide.filesystem.FileSystemConfiguration;
import org.eclipse.ui.internal.ide.filesystem.FileSystemSupportRegistry;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.utils.Utils;

public class NewProjectWizardPage extends WizardPage {
	private static final Logger log = Logger.getLogger(NewProjectWizardPage.class);

	private static final String DEFAULT_INITIAL_PROJECT_NAME = "New_Project";
    private static final int SIZING_TEXT_FIELD_WIDTH = 400;
    private static final String WORKSPACE_PATH = Utils.getWorkspacePath().toOSString();
	
    // widgets
    private Text projectNameField;
    private Text fileText;
    private Text locationText;
    
    private String initialProjectFieldValue;
    private FileExtension extension;

    
    
    private Listener nameModifyListener = new Listener() {
        public void handleEvent(Event e) {
            boolean valid = validatePage();
            setPageComplete(valid); 
            
            fileText.setText(getProjectName() + "." + extension.toString());
            locationText.setText(WORKSPACE_PATH + File.separator + getProjectName());
        }
    };


    public NewProjectWizardPage(String pageName, FileExtension ext){
    	super(pageName);
    	this.extension = ext;

    	setPageComplete(false);	    
	    this.setInitialProjectName(DEFAULT_INITIAL_PROJECT_NAME);    	
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
    

        initializeDialogUnits(parent);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(composite,
                IIDEHelpContextIds.NEW_PROJECT_WIZARD_PAGE);

        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createControlsGroup(composite);
		
        setPageComplete(validatePage());
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
        Dialog.applyDialogFont(composite);
    }
    
    

    /**
     * Creates the project name specification controls.
     *
     * @param parent the parent composite
     */
    private final Composite createControlsGroup(Composite parent) {
        GridData gd;
    	
    	// project specification group
        Composite projectGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // new project label
        Label projectLabel = new Label(projectGroup, SWT.NONE);
        projectLabel.setText("Project name:");
        projectLabel.setFont(parent.getFont());

        // new project name entry field
        projectNameField = new Text(projectGroup, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.widthHint = SIZING_TEXT_FIELD_WIDTH;
        projectNameField.setLayoutData(gd);
        projectNameField.setFont(parent.getFont());
                
        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialProjectFieldValue != null) {
			projectNameField.setText(initialProjectFieldValue);
		}
        projectNameField.addListener(SWT.Modify, nameModifyListener);
        
                
        // Location label and field
        Label locationLabel = new Label(projectGroup, SWT.NONE);
        locationLabel.setText("Location:");
        locationLabel.setFont(parent.getFont());        

        locationText = new Text(projectGroup, SWT.BORDER);
        locationText.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        locationText.setLayoutData(gd);
        locationText.setFont(parent.getFont());
        locationText.setText(WORKSPACE_PATH);
        
        // Registration card file label and field
        Label fileLabel = new Label(projectGroup, SWT.NONE);
        fileLabel.setText("Registration card file:");
        fileLabel.setFont(parent.getFont());        

        fileText = new Text(projectGroup, SWT.BORDER);
        fileText.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fileText.setLayoutData(gd);
        fileText.setFont(parent.getFont());
        fileText.setText(this.getProjectName());
        
        projectNameField.setText(this.initialProjectFieldValue);
        
        return projectGroup;
    }

    
    /**
    /**
     * Returns the current project location URI as entered by 
     * the user, or <code>null</code> if a valid project location
     * has not been entered.
     *
     * @return the project location URI, or <code>null</code>
     * @since 3.2
     */
    public URI getLocationURI() {
    	// Null means default location (workspace).
    	return null;
    }
    
    /**
	 * Creates a project resource handle for the current project name field
	 * value. The project handle is created relative to the workspace root.
	 * <p>
	 * This method does not create the project resource; this is the
	 * responsibility of <code>IProject::create</code> invoked by the new
	 * project resource wizard.
	 * </p>
	 * 
	 * @return the new project resource handle
	 */
    public IProject getProjectHandle() {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName());
    }

    /**
     * Returns the current project name as entered by the user, or its anticipated
     * initial value.
     *
     * @return the project name, its anticipated initial value, or <code>null</code>
     *   if no project name is known
     */
    public String getProjectName() {
        if (projectNameField == null) {
			return initialProjectFieldValue;
		}

        return getProjectNameFieldValue();
    }

    /**
     * Returns the value of the project name field
     * with leading and trailing spaces removed.
     * 
     * @return the project name in the field
     */
    private String getProjectNameFieldValue() {
        if (projectNameField == null) {
			return ""; 
		}

        return projectNameField.getText().trim();
    }

    /**
     * Sets the initial project name that this page will use when
     * created. The name is ignored if the createControl(Composite)
     * method has already been called. Leading and trailing spaces
     * in the name are ignored.
     * Providing the name of an existing project will not necessarily 
     * cause the wizard to warn the user.  Callers of this method 
     * should first check if the project name passed already exists 
     * in the workspace.
     * 
     * @param name initial project name for this page
     * 
     * @see IWorkspace#validateName(String, int)
     * 
     */
    public void setInitialProjectName(String name) {
        if (name == null) {
			initialProjectFieldValue = null;
		} else {
            initialProjectFieldValue = name.trim();
        }
    }

  
    /**
     * Returns whether this page's controls currently all contain valid 
     * values.
     *
     * @return <code>true</code> if all controls are valid, and
     *   <code>false</code> if at least one is invalid
     */
    protected boolean validatePage() {
        IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();

        String projectFieldContents = getProjectNameFieldValue();
        if (projectFieldContents.equals("")) { //$NON-NLS-1$
            setErrorMessage(null);
            setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectNameEmpty);
            return false;
        }

        IStatus nameStatus = workspace.validateName(projectFieldContents, IResource.PROJECT);
        if (!nameStatus.isOK()) {
            setErrorMessage(nameStatus.getMessage());
            return false;
        }

        IProject handle = getProjectHandle();
        if (handle.exists()) {
            setErrorMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectExistsMessage);
            return false;
        }
                
        setErrorMessage(null);
        setMessage(null);
        return true;
    }

    /*
     * see @DialogPage.setVisible(boolean)
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
			projectNameField.setFocus();
		}
    }


}

