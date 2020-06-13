package org.ipiran.unifier.editors;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.actions.OpenFileAction;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelRole;
import org.ipiran.unifier.ModelUnifierPlugin;
import org.ipiran.unifier.ModelRegistry.Document;
import org.ipiran.unifier.ModelRegistry.File;
import org.ipiran.unifier.ModelRegistry.ModelRegistryPackage;
import org.ipiran.unifier.ModelRegistry.RefinementSampleReg;
import org.ipiran.unifier.ModelRegistry.SampleReg;
import org.ipiran.unifier.editors.AbstractModelRegistrarMasterDetailsBlock.MasterLabelProvider;
import org.ipiran.unifier.events.AddFileSelectionAdapter;
import org.ipiran.unifier.events.OpenFileSelectionAdapter;
import org.ipiran.unifier.events.RemoveFileSelectionAdapter;
import org.ipiran.unifier.events.RemoveItemSelectionAdapter;
import org.ipiran.unifier.parts.TableFormPart;
import org.ipiran.unifier.utils.FormsUtils;
import org.ipiran.unifier.utils.FormsUtils.Buttons;

public class RefinementSampleDetailsPage extends SampleDetailsPage {
	private static final Logger log = Logger.getLogger(RefinementSampleDetailsPage.class);

	protected final static int GLUING_TEXT_HEIGHT_HINT = 100;
	protected final static int FILE_TABLE_HEIGHT_HINT = 60;
	protected final static int FILE_TABLE_WIDTH_HINT = 100;	
	
	//protected RefinementSampleReg sample; 
	
	// Fields for data binding	
	protected Text sourceConcreteSpec;
	protected Text sourceAbstractSpec;
	protected Table sourceFormalSpec;
	
	protected Text targetConcreteSpec;
	protected Text targetAbstractSpec;
	protected Table targetFormalSpec;
	
	protected Text proof;
	protected Text gluing;
	
	protected TableViewer sourceSemanticsViewer;
	protected TableViewer targetSemanticsViewer;
	
	
	
	
	
	public RefinementSampleDetailsPage(EMFDataBindingContext bindingContext, IObservableValue master) {
		super(bindingContext, master);	
	}
	
	class SpecFileLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			if(obj instanceof File){
				return (new Path(((File)obj).getReference())).lastSegment();
			}
			else		
				return obj.toString();
		}
		public Image getColumnImage(Object obj, int index) {
			return ModelUnifierPlugin.getDefault().getImage(ModelUnifierPlugin.IMG_FILE_OBJ);
		}
	}	
	
	@Override
	protected Section createVerificationSection(Composite parent) {
		FormToolkit toolkit = mform.getToolkit();
		Set<FileExtension> exts;	

		Section section = FormsUtils.createSection(mform, parent, "Sample Refinement Verification", 
				"This section describes verification of refinement of " +
				"target sample AMN-specification by source sample AMN-specification.").getSection();			
		Composite client = (Composite)section.getClient();	

		createSourceSubsection(client);
		
		createTargetSubsection(client);		

		FormsUtils.createHorizontalSeparatorInGrid(mform, client, FormsUtils.DEFAULT_SPAN);		
		
		// Gluing
		FormsUtils.createLabelInGrid(mform, client, "Gluing invariant:", FormsUtils.DEFAULT_SPAN);
		gluing = FormsUtils.createScrollableTextInGrid(mform, client, FormsUtils.DEFAULT_SPAN, GLUING_TEXT_HEIGHT_HINT, true);		
		
		// Proof
		exts = new HashSet<FileExtension>();
		exts.add(FileExtension.TXT);
		exts.add(FileExtension.PDF);
		proof = FormsUtils.createLinkTextButtonsInGrid(mform, client, "Proof:", 
				"Formal proof that the semantic specification of the source sample refines the semantic specification of the target sample.",
				1, false, exts, FormsUtils.Buttons.NEW,
				"Create a new file to put a formal proof in.", 
				null);	
		
		
		return section;
	}

	private Section createSourceSubsection(Composite parent) {
		FormToolkit toolkit = mform.getToolkit();
		GridData gd;	
		Set<FileExtension> exts;		
		
		Section section = FormsUtils.createTreeNodeSectionInGrid(mform, parent, "Source specifications", FormsUtils.DEFAULT_SPAN);
		Composite client = (Composite)section.getClient();

		exts = new HashSet<FileExtension>(); 
		exts.add(FileExtension.ALL);
		
		sourceConcreteSpec = FormsUtils.createLinkTextButtonsInGrid(mform, client, "Concrete:", 
				"Source sample concrete specification.", 1, false, exts, Buttons.NEW, 
				"Create new concrete specification.",
				null);
		
		sourceAbstractSpec = FormsUtils.createLinkTextButtonsInGrid(mform, client, "Abstract:", 
				"Source sample abstract specification.", 2, false, null, Buttons.NULL, null, null);		
		
		this.sourceSemanticsViewer = this.createLabelTableAndButtons(client, ModelRole.SOURCE);
		
		return section;
	}

	private Section createTargetSubsection(Composite parent) {
		FormToolkit toolkit = mform.getToolkit();
		GridData gd;	
		Set<FileExtension> exts;		
		
		exts = new HashSet<FileExtension>(); 
		exts.add(FileExtension.ALL);		
		
		Section section = FormsUtils.createTreeNodeSectionInGrid(mform, parent, "Target specifications", FormsUtils.DEFAULT_SPAN);
		Composite client = (Composite)section.getClient();
		
		targetAbstractSpec = FormsUtils.createLinkTextButtonsInGrid(mform, client, "Abstract:", 
				"Target sample abstract specification.", 1, false, exts, Buttons.GENERATE, 
				"Generate target specification using source-to-target ATL-transformation.",
				null);

		
		targetConcreteSpec = FormsUtils.createLinkTextButtonsInGrid(mform, client, "Concrete:", 
				"Target sample concrete specification.", 2, false, null, Buttons.NULL, null, null);
		
		this.targetSemanticsViewer = this.createLabelTableAndButtons(client, ModelRole.TARGET);
		
		return section;
	}
	
	private TableViewer createLabelTableAndButtons(Composite parent, ModelRole modelKind){
		FormsUtils.createLabelInGrid(mform, parent, "AMN representation:", FormsUtils.DEFAULT_SPAN);
		
		// Semantic spec table
		final TableFormPart tablePart = FormsUtils.createTableInGrid(mform, parent, 
				this.FILE_TABLE_HEIGHT_HINT, this.FILE_TABLE_WIDTH_HINT, 3, 4);
		Table table = tablePart.getTable();
		
		// Table viewer
		TableViewer viewer = new TableViewer(table);
		viewer.setLabelProvider(new SpecFileLabelProvider());	
		viewer.setContentProvider(new ObservableListContentProvider());

		Button generate = FormsUtils.createButtonInGrid(mform, parent, "Generate", 
				"Generate semantic AMN-specifications from abstract sample " +
				"specification using semantic ATL-transformation of the " +
				modelKind.toString() +	" model.");
				
		Button add = FormsUtils.createButtonInGrid(mform, parent, "Add", "Add AMN-specification file (*.mch, *.ref).");				
		Button remove = FormsUtils.createButtonInGrid(mform, parent, "Remove", "Remove AMN-specification file.");		
		
		Button open = FormsUtils.createButtonInGrid(mform, parent, "Open", "Open AMN-specification file in text editor.");
		open.addSelectionListener(new OpenFileSelectionAdapter(viewer));
		
		Set<FileExtension> exts = new HashSet<FileExtension>();
		exts.add(FileExtension.MCH);
		exts.add(FileExtension.REF);
				
		switch(modelKind){
		case SOURCE:
			add.addSelectionListener(new AddFileSelectionAdapter(tablePart, master, ModelRole.SOURCE, exts));
			remove.addSelectionListener(new RemoveFileSelectionAdapter(tablePart, master, ModelRole.SOURCE, viewer));			
			break;
		case TARGET:
			add.addSelectionListener(new AddFileSelectionAdapter(tablePart, master, ModelRole.TARGET, exts));
			remove.addSelectionListener(new RemoveFileSelectionAdapter(tablePart, master, ModelRole.TARGET, viewer));			
			break;
		}
		
		return viewer;
	}


	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		/*Object obj = master.getValue();
		if (obj instanceof RefinementSampleReg){
			sample = (RefinementSampleReg)obj;			
		} else {
			throw new WrongMasterValueTypeException("Master value is not of type RefinementSampleReg."); 
		}
		*/
	
		log.info("Selection changed.");
	}
	
	
	@Override
	protected void createBindings() {
		log.info("Refinement Sample: creating bindings.");
		
		super.createBindings();
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(sourceConcreteSpec), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.REFINEMENT_SAMPLE_REG__CONCRETE_SOURCE_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(sourceAbstractSpec), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.REFINEMENT_SAMPLE_REG__ABSTRACT_SOURCE_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(targetConcreteSpec), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.REFINEMENT_SAMPLE_REG__CONCRETE_TARGET_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(targetAbstractSpec), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.REFINEMENT_SAMPLE_REG__ABSTRACT_TARGET_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));					
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(proof), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.REFINEMENT_SAMPLE_REG__PROOF,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observeDetail(master));	
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(gluing), 
				EMFProperties.value(ModelRegistryPackage.Literals.REFINEMENT_SAMPLE_REG__GLUING).observeDetail(master));		
		

		
		sourceSemanticsViewer.setInput(EMFObservables.observeDetailList(Realm.getDefault(), 
				this.master, ModelRegistryPackage.Literals.REFINEMENT_SAMPLE_REG__SOURCE_SEM_SPEC));
		
		targetSemanticsViewer.setInput(EMFObservables.observeDetailList(Realm.getDefault(), 
				this.master, ModelRegistryPackage.Literals.REFINEMENT_SAMPLE_REG__TARGET_SEM_SPEC));

	}

}
