package org.ipiran.unifier.wizards;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;
import org.ipiran.unifier.utils.Utils;

public class NewSampleWizardPage extends WizardPage{
	private static final Logger log = Logger.getLogger(NewSampleWizardPage.class);
	
	private static final String DEFAULT_SAMPLE_NAME = "New_Sample";
	
	protected Text sampleNameText;
	protected String sampleName = DEFAULT_SAMPLE_NAME; 
	protected Text locationText;
	protected String unificationProjectName;
	
	protected NewSampleWizardPage(String pageName, String unificationProjectName) {
		super(pageName);
		this.unificationProjectName = unificationProjectName;
		
	}

	
    private ModifyListener nameModifyListener = new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
			sampleName = sampleNameText.getText().trim();
			
            boolean valid = validatePage();
            setPageComplete(valid); 
            
            locationText.setText(File.separator + unificationProjectName + File.separator + getSampleName());
		}
    };
	
	
	@Override
	public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);

        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createControlsGroup(composite);        

        setPageComplete(validatePage());        
        
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
		
	}

	
	private Composite createControlsGroup(Composite parent) {
        GridData gd;
    	
    	// project specification group
        Composite group = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // new project label
        Label sampleLabel = new Label(group, SWT.NONE);
        sampleLabel.setText("Sample name:");

        // new project name entry field
        sampleNameText = new Text(group, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        sampleNameText.setLayoutData(gd);                
        sampleNameText.setText(DEFAULT_SAMPLE_NAME);
                
        // Location label and field
        Label locationLabel = new Label(group, SWT.NONE);
        locationLabel.setText("Location:");
        locationLabel.setFont(parent.getFont());        

        locationText = new Text(group, SWT.BORDER);
        locationText.setEnabled(false);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        locationText.setLayoutData(gd);
        locationText.setText(File.separator + unificationProjectName + File.separator + DEFAULT_SAMPLE_NAME);        

        sampleNameText.addModifyListener(nameModifyListener);        
        
        return group;
		
	}

	public String getSampleName(){
		return sampleName;
	}
	
    protected boolean validatePage() {
        if (!Utils.isFilenameValid(getSampleName())) { 
            setErrorMessage(null);
            setMessage("Invalid sample name.");
            return false;
        }
                
        setErrorMessage(null);
        setMessage(null);
        return true;
    }
	
}
