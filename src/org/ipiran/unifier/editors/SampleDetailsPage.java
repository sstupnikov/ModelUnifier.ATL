package org.ipiran.unifier.editors;

import java.util.Set;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelRegistry.ModelRegistryPackage;
import org.ipiran.unifier.ModelRegistry.SampleReg;
import org.ipiran.unifier.utils.FormsUtils;

public abstract class SampleDetailsPage extends DataBindingDetailsPage {
	
	public SampleDetailsPage(EMFDataBindingContext bindingContext,	IObservableValue master) {
		super(bindingContext, master);
	}

	// Fields for data binding
	protected Text sampleTitleText;
	protected Button isVerifiedCheckButton;
	
	
	
	public void createContents(Composite parent) {
		super.createContents(parent);
		
		FormToolkit toolkit = mform.getToolkit();				
		
		parent.setLayout(FormsUtils.createStandardGridLayout());				
		
		createGeneralInfoSection(parent);
		
		createVerificationSection(parent);
		
		createBindings();		
	}
	

	private Section createGeneralInfoSection(Composite parent) {
		FormToolkit toolkit = mform.getToolkit();

		Section infoSection = FormsUtils.createSection(mform, parent, "Sample General Information", 
				"This section describes general information about this sample.").getSection();			
		Composite client = (Composite)infoSection.getClient();

		Label sampleTitleLabel = toolkit.createLabel(client, "Title:");
		sampleTitleText = FormsUtils.createTextInGrid(mform, client, FormsUtils.DEFAULT_SPAN - 1, true);
		sampleTitleText.setEnabled(false);
		
		isVerifiedCheckButton = FormsUtils.createCheckButtonInGrid(mform, client, "Sample is verified",
				"This check box states wether sample is formally verified or not.", FormsUtils.DEFAULT_SPAN);
		
		return infoSection;
	}
	
	@Override
	protected void createBindings() {
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(sampleTitleText), 
				EMFProperties.value(ModelRegistryPackage.Literals.SAMPLE_REG__TITLE).observeDetail(master));		

		bindingContext.bindValue(WidgetProperties.selection().observe(isVerifiedCheckButton), 
				EMFProperties.value(ModelRegistryPackage.Literals.SAMPLE_REG__IS_VERIFIED).observeDetail(master));		
	}
	
	protected abstract Section createVerificationSection(Composite parent);

}
