package org.ipiran.unifier.editors;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.ipiran.unifier.ModelRegistry.ExtensionReg;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.ModelRegistry.ModelRegistryPackage;
import org.ipiran.unifier.ModelRegistry.RegistrationCard;
import org.ipiran.unifier.events.BrowseFileSelectionAdapter;
import org.ipiran.unifier.events.OpenHyperlinkAdapter;
import org.ipiran.unifier.parts.TextFormPart;
import org.ipiran.unifier.utils.ModelTreeViewer;
import org.ipiran.unifier.utils.Utils;

public class ExtensionRegistrarMasterDetailsBlock extends
		AbstractModelRegistrarMasterDetailsBlock {

	private static final Logger log = Logger.getLogger(ExtensionRegistrarMasterDetailsBlock.class); 			

	private ExtensionReg card;	
	
	protected Text extendedModelText;
	
	public ExtensionRegistrarMasterDetailsBlock(FormPage page) throws WrongRegistrarInputException {
		super(page);
				
		RegistrationCard edCard = ((Registrar)page.getEditor()).getModel();  
		if(edCard instanceof ExtensionReg){
			card = (ExtensionReg)edCard;
		}
		else{
			throw new WrongRegistrarInputException("Extension registrar input model is not of type ExtensionReg.");
		}
		
	}

	@Override
	protected ModelReg getModel() {
		return card;
	}

	@Override
	protected void createAdditionalControls(IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();	
		GridData gd;

		Hyperlink link = toolkit.createHyperlink(parent, "Extended Model:", SWT.NULL);
		link.setToolTipText("A model or extension to be further extended by this extension.");		
		
		extendedModelText = toolkit.createText(parent, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		extendedModelText.setLayoutData(gd);
		extendedModelText.setEditable(false);
		link.addHyperlinkListener(new OpenHyperlinkAdapter(extendedModelText));
		final TextFormPart extendedModelPart = new TextFormPart(extendedModelText);
		managedForm.addPart(extendedModelPart);
		extendedModelText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				extendedModelPart.markDirty();
			}
		});		
		
		Button browseButton = toolkit.createButton(parent, "...", SWT.PUSH);
		//browseButton.addSelectionListener(new BrowseFileSelectionAdapter(extendedModelText));
		browseButton.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				ModelTreeViewer viewer = new ModelTreeViewer(((Registrar)page.getEditor()).getModelPath().toOSString());
				viewer.openWindow();
				if(viewer.isModelSelected()){
					extendedModelText.setText(viewer.getSelectedModelPath());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	protected void createDataBindings(){
		super.createDataBindings();
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(extendedModelText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.EXTENSION_REG__EXTENDED_MODEL,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(getModel()));			
	}	
	
}
