package org.ipiran.unifier.editors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.m2m.atl.common.ATLExecutionException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.ide.IDE;
import org.ipiran.atl.transformation.BundleTransformation;
import org.ipiran.atl.transformation.LocalTransformation;
import org.ipiran.atl.transformation.Transformation;
import org.ipiran.atl.transformation.TransformationPropertiesFactory;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelUnifierPlugin;
import org.ipiran.unifier.ModelRegistry.Document;
import org.ipiran.unifier.ModelRegistry.EntailmentSampleReg;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.ModelRegistry.ModelRegistryPackage;
import org.ipiran.unifier.ModelRegistry.RefinementSampleReg;
import org.ipiran.unifier.ModelRegistry.RegistrationCard;
import org.ipiran.unifier.ModelRegistry.SampleReg;
import org.ipiran.unifier.ModelRegistry.UnificationReg;
import org.ipiran.unifier.ModelRegistry.impl.EntailmentSampleRegImpl;
import org.ipiran.unifier.ModelRegistry.impl.RefinementSampleRegImpl;
import org.ipiran.unifier.editors.AbstractModelRegistrarMasterDetailsBlock.MasterLabelProvider;
import org.ipiran.unifier.events.AddEntSampleSelectionAdapter;
import org.ipiran.unifier.events.AddRefSampleSelectionAdapter;
import org.ipiran.unifier.events.BrowseFileSelectionAdapter;
import org.ipiran.unifier.events.NewFileSelectionAdapter;
import org.ipiran.unifier.events.OpenHyperlinkAdapter;
import org.ipiran.unifier.events.OpenModelSemanticSpecHyperlinkAdapter;
import org.ipiran.unifier.events.RemoveItemSelectionAdapter;
import org.ipiran.unifier.events.RemoveSampleSelectionAdapter;
import org.ipiran.unifier.parts.ButtonFormPart;
import org.ipiran.unifier.parts.ComboFormPart;
import org.ipiran.unifier.parts.TableFormPart;
import org.ipiran.unifier.parts.TextFormPart;
import org.ipiran.unifier.utils.FormsUtils;
import org.ipiran.unifier.utils.InputFileLoader;
import org.ipiran.unifier.utils.LinkOpener;
import org.ipiran.unifier.utils.ModelRegistryUtils;
import org.ipiran.unifier.utils.Utils;
import org.ipiran.unifier.VerificationMethod;

public class UnificationMasterDetailsBlock extends RegistrarMasterDetailsBlock {
	private static final Logger log = Logger.getLogger(UnificationMasterDetailsBlock.class);
	
	protected final static int[] DEFAULT_CHILD_WEIGHTS_FOR_ENTAILMENT_PAGE = new int[]{1, 2};
	protected final static int[] DEFAULT_CHILD_WEIGHTS_FOR_REFINEMENT_PAGE = new int[]{1, 1};	
	protected final static int GLUING_TEXT_HEIGHT_HINT = 150;
	protected final static int SAMPLES_TABLE_HEIGHT_HINT = 100;
	protected final static int SAMPLES_TABLE_WIDTH_HINT = 60;	
	
	protected UnificationReg card;
	
	// Fields for data binding
	protected Text sourceModel;
	protected Text targetModel;
	protected Text similarities;
	protected Text mapping;
	protected Text transformationS2T;
	protected Text transformationT2S;	
	protected Button isVerified;
	protected Text gluing;
	protected Text proof;
	
	protected Table samplesTable;
	protected TableViewer samplesTableViewer;
	
	//protected VerificationMethod sampleVerificationMethod;
	
	public TableViewer getSamplesViewer(){
		return samplesTableViewer;
	}
	
	public UnificationMasterDetailsBlock(FormPage page) throws WrongRegistrarInputException {
		super(page);
		
		RegistrationCard card = ((Registrar)page.getEditor()).getModel(); 
		if(card instanceof UnificationReg){
			this.card = (UnificationReg)card;	
		} 
		else{
			throw new WrongRegistrarInputException("Unification registrar input model is not of type UnificationReg.");
		}				
	}

	class MasterLabelProvider extends LabelProvider	implements	ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			if(obj instanceof SampleReg){
				return ((SampleReg)obj).getTitle();
			}
			else		
				return obj.toString();
		}
		public Image getColumnImage(Object obj, int index) {
			if(obj instanceof EntailmentSampleReg){
				return ModelUnifierPlugin.getDefault().getImage(ModelUnifierPlugin.IMG_ENT);
			}
			else		
			if(obj instanceof RefinementSampleReg){
				return ModelUnifierPlugin.getDefault().getImage(ModelUnifierPlugin.IMG_REF);
			}
			return null;
		}
	}	
	
	@Override
	protected void createMasterPart(IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();		
		GridData gd;
		Set<FileExtension> exts;
		
		ScrolledForm form = toolkit.createScrolledForm(parent);		
		Composite formBody = form.getBody();
		
		formBody.setLayout(FormsUtils.createStandardGridLayout(FormsUtils.DEFAULT_SPAN));				
				
		// Source model
		exts = new HashSet<FileExtension>(); 
		exts.add(FileExtension.MRC);
		exts.add(FileExtension.ERC);
		sourceModel = FormsUtils.createLinkTextButtonsInGrid(managedForm, formBody,
				"Source Model:", "Source model to be unified.", 2, false, exts,
				FormsUtils.Buttons.NULL, null, null);

		// Target model
		targetModel = FormsUtils.createLinkTextButtonsInGrid(managedForm, formBody,
				"Target Model:", "Target model or extension serving as the canonical model.",
				2, false, exts, FormsUtils.Buttons.NULL, null, null);

		// Similarities
		exts = new HashSet<FileExtension>();
		exts.add(FileExtension.SIM);
		similarities = FormsUtils.createLinkTextButtonsInGrid(managedForm, formBody,
				"Similarities:", "Similarities between elements of source and target models.",
				1, false, exts, FormsUtils.Buttons.CREATE, 
				"Establish similarities between elements of source and target models.", null);								
		
		// Mapping section
		createMappingSection(managedForm, formBody);
		
		// Model Refinement
		createModelRefinementSection(managedForm, formBody);
		
		
		// Samples section
		createSamplesSection(managedForm, formBody);
		
		
		this.createDataBindings();
	}	
	
	private Combo createVerificationMethodCombo(IManagedForm managedForm, Composite parent){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;		
				
		Label verMethodLabel = toolkit.createLabel(parent, "Sample verification method:");
		verMethodLabel.setForeground(Utils.getBlueColor());
		final Combo verMethodCombo = new Combo(parent, SWT.READ_ONLY);
		gd = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gd.horizontalSpan = 3;
		verMethodCombo.setLayoutData(gd);

		verMethodCombo.setItems(VerificationMethod.toStringArray());
		assert verMethodCombo.getItemCount() > 0;
		/*
		this.sampleVerificationMethod = VerificationMethod.REFINEMENT;		
		verMethodCombo.setText(this.sampleVerificationMethod.toString());
		verMethodCombo.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				sampleVerificationMethod = VerificationMethod.stringToValue(verMethodCombo.getText());
				log.info("Method: " + sampleVerificationMethod);
			}			
		});
		*/	
		
		return verMethodCombo;
	}
	
	private Section createMappingSection(IManagedForm managedForm, Composite parent){
		Set<FileExtension> exts;		

		// Section
		Section mapSection = FormsUtils.createSection(managedForm, parent, "Mapping and transformation", 
				"Mapping and transformation between source and target models.").getSection();		
		Composite client = (Composite)mapSection.getClient();
		
		// S2T Mapping
		exts = new HashSet<FileExtension>();
		exts.add(FileExtension.TXT);
		exts.add(FileExtension.PDF);
		mapping = FormsUtils.createLinkTextButtonsInGrid(managedForm,	client,
				"S2T Mapping: ", "Source to target model mapping described in natural language.",
				1, false, exts, FormsUtils.Buttons.NEW,
				"Create new file describing source to target model mapping using natural language.", null);

		// S2T Transformation
		exts = new HashSet<FileExtension>();
		exts.add(FileExtension.ATL);
		transformationS2T = FormsUtils.createLinkTextButtonsInGrid(managedForm, client,
				"S2T Transformation: ",	"Source to target model ATL-transformation.", 
				1, false, exts,	FormsUtils.Buttons.GENERATE,
				"Generate a template of source-to-target model ATL-transformation on the basis of similarities.",
				s2tTemplateGenerationListener);

		// S2T Transformation
		transformationT2S = FormsUtils.createLinkTextButtonsInGrid(managedForm, client, 
				"T2S Transformation: ",	"Target to source model ATL-transformation.",
				1, false, exts,	FormsUtils.Buttons.GENERATE,
				"Generate a template of target-to-source model ATL-transformation on the basis of source-to-target transformation.",
				null);
		
		
		return mapSection;		
	}

	private SelectionListener s2tTemplateGenerationListener = new SelectionListener(){
		@Override
		public void widgetSelected(SelectionEvent e) {
			TransformationPropertiesFactory factory = new TransformationPropertiesFactory();
			
			factory.addBundle("org.ipiran.unifier");
			factory.addTransformationModule("transformations\\TTC.asm");
			factory.addMetamodel("Similarities", "model\\Similarities.ecore");
			factory.addMetamodel("ATL", "model\\ATL.ecore");
			factory.addInMetamodelName("Similarities");
			factory.addOutMetamodelName("ATL");
			
			Transformation runner = new BundleTransformation(factory.getProperties());
			try {
				runner.loadTransformSave(similarities.getText(), "tr.ecore");
			} catch (ATLExecutionException ex) {
				log.info("ATL transformation failed.");
				ex.printStackTrace();
			} catch (ATLCoreException ex) {
				log.info("ATL transformation failed.");				
				ex.printStackTrace();
			} catch (IOException ex) {
				log.info("ATL transformation failed.");				
				ex.printStackTrace();
			}						
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}		
	};
	
	private Section createModelRefinementSection(IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;
		Set<FileExtension> exts;

		// Section
		final SectionPart refSectionPart = FormsUtils.createSection(managedForm, parent, "Source-by-Target Model Refinement", 
				"Refinement of target model semantic AMN-specification by source model semantic AMN-specification.");
		Section refSection = refSectionPart.getSection();
		Composite client = (Composite)refSection.getClient();
		
		// Source semantics
		Hyperlink sourceSemanticsLink = toolkit.createHyperlink(client, "Source model AMN semantics", SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = FormsUtils.DEFAULT_SPAN;
		sourceSemanticsLink.setLayoutData(gd);
		
		sourceSemanticsLink.addHyperlinkListener(new OpenModelSemanticSpecHyperlinkAdapter(sourceModel));

		// Target semantics
		Hyperlink targetSemanticsLink = toolkit.createHyperlink(client, "Target model AMN semantics", SWT.NULL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = FormsUtils.DEFAULT_SPAN;
		targetSemanticsLink.setLayoutData(gd);
		targetSemanticsLink.addHyperlinkListener(new OpenModelSemanticSpecHyperlinkAdapter(targetModel));
		
		// Gluing
		Label gluingLabel = FormsUtils.createLabelInGrid(managedForm, client, "Gluing invariant:", FormsUtils.DEFAULT_SPAN);
		gluing = FormsUtils.createScrollableTextInGrid(managedForm, client, FormsUtils.DEFAULT_SPAN, GLUING_TEXT_HEIGHT_HINT, true);
		
		// Proof
		exts = new HashSet<FileExtension>();
		exts.add(FileExtension.TXT);
		exts.add(FileExtension.PDF);
		proof = FormsUtils.createLinkTextButtonsInGrid(managedForm, client, "Proof:", 
				"Formal proof that the semantic specification of the source model refines the semantic specification of the target model.",
				1, false, exts, FormsUtils.Buttons.NEW,
				"Create a new file to put a formal proof in.", 
				null);	
		
		// "Refinement is verified" check		
		this.isVerified = FormsUtils.createCheckButtonInGrid(managedForm, client, "Refinement is verified",
				"This check box states wether refinement of target model by source model is verified or not."); 		
		
		return refSection;
	}
	
	private Section createSamplesSection(final IManagedForm managedForm, Composite parent){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;
		
		// Section
		SectionPart sampleSectionPart = FormsUtils.createSection(managedForm, parent, "Samples", 
				"Samples intended to verify the mapping of the source model into the target model.",
				FormsUtils.DEFAULT_SPAN, FormsUtils.DEFAULT_SPAN, true); 
		Section sampleSection = sampleSectionPart.getSection();		
		Composite client = (Composite)sampleSection.getClient();	
		
		// Table
		samplesTable = toolkit.createTable(client, SWT.NULL);		
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = SAMPLES_TABLE_HEIGHT_HINT;
		gd.widthHint = SAMPLES_TABLE_WIDTH_HINT;	
		gd.horizontalSpan = 3;
		gd.verticalSpan = 3;
		samplesTable.setLayoutData(gd);
		final TableFormPart sampleTablePart = new TableFormPart(samplesTable);
		managedForm.addPart(sampleTablePart);
		
		// Viewer
		samplesTableViewer = new TableViewer(samplesTable);
		samplesTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				//log.info("Master selectionChanged");
				managedForm.fireSelectionChanged(sampleTablePart, event.getSelection());
				setChildWeights(samplesTableViewer);
			}
		});	
		samplesTableViewer.setContentProvider(new ObservableListContentProvider());
		samplesTableViewer.setLabelProvider(new MasterLabelProvider());
				
		// Add ref sample button		
		Button addRefSample = FormsUtils.createButtonInGrid(managedForm, client, "Add Refinement Sample", "Add new refinement sample.");
		addRefSample.addSelectionListener(new AddRefSampleSelectionAdapter(sampleTablePart, card.getSamples(), Utils.getCurrentProject(page)));

		// Add ent sample button		
		Button addEntSample = FormsUtils.createButtonInGrid(managedForm, client, "Add Entailment Sample", "Add new entailment sample.");
		addEntSample.addSelectionListener(new AddEntSampleSelectionAdapter(sampleTablePart, card.getSamples(), Utils.getCurrentProject(page)));		
		
		// Add doc button		
		Button removeSample = FormsUtils.createButtonInGrid(managedForm, client, "Remove", "Remove selected sample.");
		removeSample.addSelectionListener(new RemoveSampleSelectionAdapter(sampleTablePart, card.getSamples(), samplesTableViewer, Utils.getCurrentProject(page)));		
		
		return sampleSection;
	}
			
	protected void setChildWeights(Viewer viewer){		
		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		Object obj = sel.getFirstElement(); 
		
		if(obj != null){
			if(obj instanceof RefinementSampleReg){
				this.sashForm.setWeights(this.DEFAULT_CHILD_WEIGHTS_FOR_REFINEMENT_PAGE);				
			}
			if(obj instanceof EntailmentSampleReg){
				this.sashForm.setWeights(this.DEFAULT_CHILD_WEIGHTS_FOR_ENTAILMENT_PAGE);
			}
			
		}							
	}
	
	@Override
	protected void createDataBindings() {

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(sourceModel), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.UNIFICATION_REG__SOURCE,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(card));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(targetModel), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.UNIFICATION_REG__TARGET,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(card));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(similarities), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.UNIFICATION_REG__SIMILARITIES,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(card));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(mapping), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.UNIFICATION_REG__MAPPING,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(card));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(transformationS2T), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.UNIFICATION_REG__TRANSFORMATION_S2T,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(card));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(transformationT2S), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.UNIFICATION_REG__TRANSFORMATION_T2S,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(card));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(proof), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.UNIFICATION_REG__PROOF,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(card));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(gluing), 
				EMFProperties.value(ModelRegistryPackage.Literals.UNIFICATION_REG__GLUING).observe(card));
												
		bindingContext.bindValue(WidgetProperties.selection().observe(isVerified), 
				EMFProperties.value(ModelRegistryPackage.Literals.UNIFICATION_REG__IS_VERIFIED).observe(card));
						
		samplesTableViewer.setInput(EMFObservables.observeList(card, ModelRegistryPackage.Literals.UNIFICATION_REG__SAMPLES));		
	}	
	
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		EntailmentSampleDetailsPage entPage = new EntailmentSampleDetailsPage(
				bindingContext, ViewersObservables.observeSingleSelection(samplesTableViewer));							
		detailsPart.registerPage(EntailmentSampleRegImpl.class, entPage);	
		
		RefinementSampleDetailsPage refPage = new RefinementSampleDetailsPage(
				bindingContext, ViewersObservables.observeSingleSelection(samplesTableViewer));							
		detailsPart.registerPage(RefinementSampleRegImpl.class, refPage);			
	}
	
}
