package org.ipiran.unifier.editors;

import java.util.HashSet;
import java.util.Set;

import javax.swing.text.StyleConstants.ColorConstants;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.ipiran.unifier.EntailmentKind;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelUnifierPlugin;
import org.ipiran.unifier.VerificationMethod;
import org.ipiran.unifier.ModelRegistry.EntailmentSampleReg;
import org.ipiran.unifier.ModelRegistry.ModelRegistryPackage;
import org.ipiran.unifier.ModelRegistry.RefinementSampleReg;
import org.ipiran.unifier.ModelRegistry.SampleReg;
import org.ipiran.unifier.databinding.UpdateStartegyFactory;
import org.ipiran.unifier.parts.ComboFormPart;
import org.ipiran.unifier.utils.FormsUtils;
import org.ipiran.unifier.utils.LinkOpener;
import org.ipiran.unifier.utils.NonLegalResourcePathException;
import org.ipiran.unifier.utils.Utils;

public class EntailmentSampleDetailsPage extends SampleDetailsPage {
	private static final Logger log = Logger.getLogger(EntailmentSampleDetailsPage.class);
	
	protected static int SUBSECTION_SPAN = 3; 
	protected static int CONCRETE_SPECIFICATION_CONTENTS_HEIGHT_HINT = 70;
	
	
	//protected EntailmentSampleReg sample;
	
	// Fields for data binding
	
	// Source/target, conclusion/premise, concrete/abstract, path/contents
	protected Text sourceConcreteConclusionText;
	protected Text sourceConcreteConclusionContentsText;	
	protected Text sourceAbstractConclusionText;
	protected Text sourceConcretePremiseText;	
	protected Text sourceConcretePremiseContentsText;	
	protected Text sourceAbstractPremiseText;	
	protected Text targetConcreteConclusionText;
	protected Text targetConcreteConclusionContentsText;
	protected Text targetAbstractConclusionText;
	protected Text targetConcretePremiseText;	
	protected Text targetConcretePremiseContentsText;
	protected Text targetAbstractPremiseText;	

	Combo entailmentKindCombo;
	ComboViewer entailmentKindComboViewer;
	
	
	
	public EntailmentSampleDetailsPage(EMFDataBindingContext bindingContext, IObservableValue master) {
		super(bindingContext, master);
	}
	
	@Override
	protected Section createVerificationSection(Composite parent) {		
		Section section = FormsUtils.createSection(mform, parent, "Sample Entailment Verification", 
				"This section describes a verification of preserving the sample entailment by the source-to-target model mapping. " +
				"Positive (negative) entaiment of a source conclusion by a source premise " +
				"has to be preserved on a target conclusion and a premise.").getSection();			
		Composite client = (Composite)section.getClient();
		
		createRefreshAction(section);
		
		createSourceSubsection(client);
		
		createTargetSubsection(client);
				
		return section;
	}

	private Action createRefreshAction(Section section){
	    Action refresh = new Action(){
	    	public void run(){
	    		refreshConcreteSpec();
	    	}
	    };
	    refresh.setImageDescriptor(ModelUnifierPlugin.getDefault().getImageDescriptor(ModelUnifierPlugin.IMG_REFRESH));
	    refresh.setToolTipText("Refresh source and target concrete specifications on form from respective files.");
		
	    FormsUtils.createSectionToolBarManager(section, refresh);	    

	    return refresh;
	}
	
	private void refreshConcreteSpec(){
		log.info("refreshConcreteSpec");
		
		LinkOpener op;
		
		// Clear text fields.
		sourceConcreteConclusionContentsText.setText("");
		sourceConcretePremiseContentsText.setText("");
		targetConcreteConclusionContentsText.setText("");
		targetConcretePremiseContentsText.setText("");
		
		// Get specs form files.
		try {
			op = new LinkOpener(sourceConcreteConclusionText.getText());
			sourceConcreteConclusionContentsText.setText(op.getFileContents());
		} catch (NonLegalResourcePathException e) {
			log.warn("sourceConcreteConclusionText: " + e.getMessage());
			//e.printStackTrace();
		}
		
		try {
			op = new LinkOpener(sourceConcretePremiseText.getText());
			sourceConcretePremiseContentsText.setText(op.getFileContents());
		} catch (NonLegalResourcePathException e) {
			log.warn("sourceConcretePremiseText: " + e.getMessage());
			//e.printStackTrace();
		}

		try {
			op = new LinkOpener(targetConcreteConclusionText.getText());
			targetConcreteConclusionContentsText.setText(op.getFileContents());
		} catch (NonLegalResourcePathException e) {
			log.warn("targetConcreteConclusionText: " + e.getMessage());
			//e.printStackTrace();
		}
		
		try {
			op = new LinkOpener(targetConcretePremiseText.getText());
			targetConcretePremiseContentsText.setText(op.getFileContents());		
		} catch (NonLegalResourcePathException e) {
			log.warn("targetConcretePremiseText: " + e.getMessage());
			//e.printStackTrace();
		}
	}
	
	private void refreshConcreteSpec(Text pathText, Text contentsText){
		// Clear contents field.
		contentsText.setText("");
		
		// Get specs form file.
		try {
			LinkOpener op = new LinkOpener(pathText.getText());
			contentsText.setText(op.getFileContents());
		} catch (NonLegalResourcePathException e) {
			log.warn(contentsText.getText() + ": " + e.getMessage());
			//e.printStackTrace();
		}	
	}
	
	private Section createSourceSubsection(Composite parent){
		FormToolkit toolkit = mform.getToolkit();
		GridData gd;	
		Set<FileExtension> exts;		
		
		Section section = FormsUtils.createTreeNodeSectionInGrid(mform, parent, "Source specifications", SUBSECTION_SPAN);		
				
		Composite client = (Composite)section.getClient();						
		createSubsectionHeading(client);
				
		// Conclusion
		Composite conclusionComposite = FormsUtils.createCompositeInGrid(mform, client);
		exts = new HashSet<FileExtension>(); 
		exts.add(FileExtension.ALL);
		sourceConcreteConclusionText = FormsUtils.createLinkTextButtonsInGrid(mform, conclusionComposite, "Concrete:", 
				"Source concrete conclusion specification.", 1, false, exts, FormsUtils.Buttons.NEW, 
				"Create new source concrete conclusion specification.", null);
		sourceConcreteConclusionContentsText = FormsUtils.createTextInGrid(mform, conclusionComposite, 
				FormsUtils.DEFAULT_SPAN, CONCRETE_SPECIFICATION_CONTENTS_HEIGHT_HINT, false);
		sourceConcreteConclusionText.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				refreshConcreteSpec(sourceConcreteConclusionText, sourceConcreteConclusionContentsText);
			}
		});				
		sourceAbstractConclusionText = FormsUtils.createLinkTextButtonsInGrid(mform, conclusionComposite, "Abstract:", 
				"Source abstract conclusion specification.", 2, false, null, FormsUtils.Buttons.NULL, null, null);
		
		FormsUtils.createImageLabelInGrid(mform, client, ModelUnifierPlugin.getDefault().getImage(ModelUnifierPlugin.IMG_LEFT_ARROW));
						
		// Premise
		Composite premiseComposite = FormsUtils.createCompositeInGrid(mform, client);
		exts = new HashSet<FileExtension>(); 
		exts.add(FileExtension.ALL);
		sourceConcretePremiseText = FormsUtils.createLinkTextButtonsInGrid(mform, premiseComposite, "Concrete:", 
				"Source concrete premise specification.", 1, false, exts, FormsUtils.Buttons.NEW, 
				"Create new source concrete premise specification.", null);
		sourceConcretePremiseContentsText = FormsUtils.createTextInGrid(mform, premiseComposite, 
				FormsUtils.DEFAULT_SPAN, CONCRETE_SPECIFICATION_CONTENTS_HEIGHT_HINT, false);
		sourceConcretePremiseText.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				refreshConcreteSpec(sourceConcretePremiseText, sourceConcretePremiseContentsText);
			}
		});						
		sourceAbstractPremiseText = FormsUtils.createLinkTextButtonsInGrid(mform, premiseComposite, "Abstract:", 
				"Source abstract premise specification.", 2, false, null, FormsUtils.Buttons.NULL, null, null);
		
		
		// EntailmentKind
		Composite entKindComposite = FormsUtils.createCompositeInGrid(mform, client, SUBSECTION_SPAN);
		createEntailmentKindCombo(entKindComposite);
		
		return section;
	} 

	private void createSubsectionHeading(Composite parent){
		FormToolkit toolkit = mform.getToolkit();
		GridData gd;		

		Label conclusionLabel = toolkit.createLabel(parent, "Conclusion");		
		conclusionLabel.setForeground(Utils.getBlueColor());
		gd = new GridData(GridData.FILL_HORIZONTAL);	
		gd.horizontalAlignment = SWT.CENTER;
		conclusionLabel.setLayoutData(gd);

		FormsUtils.createEmptyLabel(mform, parent);
		
		Label premiseLabel = toolkit.createLabel(parent, "Premise");		
		premiseLabel.setForeground(Utils.getBlueColor());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = SWT.CENTER;		
		premiseLabel.setLayoutData(gd);
		
		FormsUtils.createHorizontalSeparatorInGrid(mform, parent, SUBSECTION_SPAN);
	}  

	private ComboFormPart createEntailmentKindCombo(Composite parent){
		FormToolkit toolkit = mform.getToolkit();
		GridData gd;		
				
		Label entKindLabel = toolkit.createLabel(parent, "Entailment kind:");
		entKindLabel.setForeground(Utils.getBlueColor());
		entailmentKindCombo  = new Combo(parent, SWT.READ_ONLY);
		gd = new GridData(GridData.BEGINNING, GridData.BEGINNING, true, false);
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = SUBSECTION_SPAN - 1;
		entailmentKindCombo.setLayoutData(gd);

		entailmentKindComboViewer = new ComboViewer(entailmentKindCombo);
		entailmentKindComboViewer.setContentProvider(new ArrayContentProvider());
		entailmentKindComboViewer.setLabelProvider(new LabelProvider());
		entailmentKindComboViewer.setInput(EntailmentKind.values());
		//entailmentKindCombo.setText("positive");
		
		final ComboFormPart entKindFormPart = new ComboFormPart(entailmentKindCombo);
		mform.addPart(entKindFormPart);
		entailmentKindCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				entKindFormPart.markDirty();			
			}
		});		
		
		return entKindFormPart;
	}	

	private Section createTargetSubsection(Composite parent){
		FormToolkit toolkit = mform.getToolkit();
		GridData gd;	
		Set<FileExtension> exts;		
		
		Section section = FormsUtils.createTreeNodeSectionInGrid(mform, parent, "Target specifications", SUBSECTION_SPAN);
		Composite client = (Composite)section.getClient();
				
		createSubsectionHeading(client);
				
		// Conclusion
		Composite conclusionComposite = FormsUtils.createCompositeInGrid(mform, client);
		exts = new HashSet<FileExtension>(); 
		exts.add(FileExtension.ALL);
		targetAbstractConclusionText = FormsUtils.createLinkTextButtonsInGrid(mform, conclusionComposite, "Abstract:", 
				"Target abstract conclusion specification.", 1, false, exts, FormsUtils.Buttons.GENERATE, 
				"Generate target conclusion using source-to-target ATL-transformation.",
				null);
		targetConcreteConclusionText = FormsUtils.createLinkTextButtonsInGrid(mform, conclusionComposite, "Concrete:", 
				"Target concrete conclusion specification.", 2, false, null, FormsUtils.Buttons.NULL, null, null);
		targetConcreteConclusionContentsText = FormsUtils.createTextInGrid(mform, conclusionComposite, 
				FormsUtils.DEFAULT_SPAN, CONCRETE_SPECIFICATION_CONTENTS_HEIGHT_HINT, false);
		targetConcreteConclusionText.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				refreshConcreteSpec(targetConcreteConclusionText, targetConcreteConclusionContentsText);
			}
		});								
		
		FormsUtils.createImageLabelInGrid(mform, client, ModelUnifierPlugin.getDefault().getImage(ModelUnifierPlugin.IMG_LEFT_ARROW));
						
		// Premise
		Composite premiseComposite = FormsUtils.createCompositeInGrid(mform, client);
		exts = new HashSet<FileExtension>(); 
		exts.add(FileExtension.ALL);
		targetAbstractPremiseText = FormsUtils.createLinkTextButtonsInGrid(mform, premiseComposite, "Abstract:", 
				"Target abstract premise specification.", 1, false, exts, FormsUtils.Buttons.GENERATE,
				"Generate target premise using source-to-target ATL-transformation.",
				null);
		targetConcretePremiseText = FormsUtils.createLinkTextButtonsInGrid(mform, premiseComposite, "Concrete:", 
				"Target concrete premise specification.", 2, false, null, FormsUtils.Buttons.NULL, null, null);
		targetConcretePremiseContentsText = FormsUtils.createTextInGrid(mform, premiseComposite, 
				FormsUtils.DEFAULT_SPAN, CONCRETE_SPECIFICATION_CONTENTS_HEIGHT_HINT, false);
		targetConcretePremiseText.addModifyListener(new ModifyListener() {			
			@Override
			public void modifyText(ModifyEvent e) {
				refreshConcreteSpec(targetConcretePremiseText, targetConcretePremiseContentsText);
			}
		});										
						
		return section;
	} 
	
	
	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		super.selectionChanged(part, selection);

		/*
		Object obj = master.getValue();
		if (obj instanceof EntailmentSampleReg){
			sample = (EntailmentSampleReg)obj;
		} else {
			throw new WrongMasterValueTypeException("Master value is not of type EntailmentSampleReg."); 
		}
		*/
		
	}
	
	@Override
	protected void createBindings() {
		super.createBindings();
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(sourceConcreteConclusionText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.ENTAILMENT_SAMPLE_REG__SOURCE_CONCLUSION_CONCRETE_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(sourceAbstractConclusionText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.ENTAILMENT_SAMPLE_REG__SOURCE_CONCLUSION_ABSTRACT_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(sourceConcretePremiseText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.ENTAILMENT_SAMPLE_REG__SOURCE_PREMISE_CONCRETE_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(sourceAbstractPremiseText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.ENTAILMENT_SAMPLE_REG__SOURCE_PREMISE_ABSTRACT_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(targetConcreteConclusionText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.ENTAILMENT_SAMPLE_REG__TARGET_CONCLUSION_CONCRETE_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(targetAbstractConclusionText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.ENTAILMENT_SAMPLE_REG__TARGET_CONCLUSION_ABSTRACT_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(targetConcretePremiseText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.ENTAILMENT_SAMPLE_REG__TARGET_PREMISE_CONCRETE_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(targetAbstractPremiseText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.ENTAILMENT_SAMPLE_REG__TARGET_PREMISE_ABSTRACT_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					


		bindingContext.bindValue(ViewerProperties.singleSelection().observe(entailmentKindComboViewer), 
				EMFProperties.value(ModelRegistryPackage.Literals.ENTAILMENT_SAMPLE_REG__POSITIVE).observeDetail(master),
				UpdateStartegyFactory.entailmentKind2Boolean(),
				UpdateStartegyFactory.boolean2EntailmentKind());		
				
	}
	
}
