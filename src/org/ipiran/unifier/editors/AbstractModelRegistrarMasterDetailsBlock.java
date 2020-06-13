package org.ipiran.unifier.editors;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelUnifierPlugin;
import org.ipiran.unifier.ModelRegistry.ModelRegistryFactory;
import org.ipiran.unifier.ModelRegistry.ModelRegistryPackage;
import org.ipiran.unifier.ModelRegistry.Document;
import org.ipiran.unifier.ModelRegistry.ModelReg;
import org.ipiran.unifier.ModelRegistry.impl.DocumentImpl;
import org.ipiran.unifier.events.AddDocumentSelectionAdapter;
import org.ipiran.unifier.events.AddEObjectSelectionAdapter;
import org.ipiran.unifier.events.BrowseFileSelectionAdapter;
import org.ipiran.unifier.events.NewFileSelectionAdapter;
import org.ipiran.unifier.events.OpenHyperlinkAdapter;
import org.ipiran.unifier.events.RemoveItemSelectionAdapter;
import org.ipiran.unifier.parts.TableFormPart;
import org.ipiran.unifier.parts.TextFormPart;
import org.ipiran.unifier.utils.FormsUtils;
import org.ipiran.unifier.utils.Utils;

public abstract class AbstractModelRegistrarMasterDetailsBlock extends RegistrarMasterDetailsBlock {
	private static final Logger log = Logger.getLogger(AbstractModelRegistrarMasterDetailsBlock.class); 		
	
	public enum Master{
		SYNTAX, SEMANTICS
	} 
	
	protected final static int DOC_TABLE_HEIGHT_HINT = 60;
	protected final static int DOC_TABLE_WIDTH_HINT = 100;
	
	protected IDetailsPageProvider docDetailsPageProvider;	
	protected Composite formBody;	
	
	// Tables and viewers
	protected Table syntaxDocTable;
	protected Table semanticsDocTable;	
	protected TableViewer syntaxDocViewer;
	protected TableViewer semanticsDocViewer;	
	
	// Fields for data binding
	protected Text shortTitleText;
	protected Text fullTitleText;	
	protected Text abstractSyntaxText;
	protected Text concreteSyntaxText;	
	protected Text semanticsText;	
	protected Text transformation;
	protected Text semanticSpec;
 	
	public AbstractModelRegistrarMasterDetailsBlock(FormPage page) {
		super(page);
	}
	

	protected abstract ModelReg getModel();
	
	public TableViewer getSyntaxDocViewer(){
		return syntaxDocViewer;
	}

	public TableViewer getSemanticsDocViewer(){
		return semanticsDocViewer;
	}
		
	
	
	class SyntaxMasterContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			return getModel().getSyntax().toArray();
		}
		public void dispose() {
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class SemanticsMasterContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			return getModel().getSemantics().toArray();
		}
		public void dispose() {
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}	
	
	class MasterLabelProvider extends LabelProvider	implements	ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			if(obj instanceof Document){
				return ((Document)obj).getTitle();
			}
			else		
				return obj.toString();
		}
		public Image getColumnImage(Object obj, int index) {
			return ModelUnifierPlugin.getDefault().getImage(ModelUnifierPlugin.IMG_FILE_OBJ);
		}
	}

	class DocumentDetailsPageProvider implements IDetailsPageProvider {
		private IDetailsPage synDocPage;
		private IDetailsPage semDocPage;		
		
		DocumentDetailsPageProvider(IDetailsPage synDocPage, IDetailsPage semDocPage){
			this.synDocPage = synDocPage;
			this.semDocPage = semDocPage;
		}		

		@Override
		public Object getPageKey(Object object) {
			Document doc = (Document)object;
			
			if(getModel().getSyntax().contains(doc)){
				return Master.SYNTAX;
			}
			else{
				return Master.SEMANTICS;
			}			
		}

		@Override
		public IDetailsPage getPage(Object key) {
			Master master = (Master)key;
			
			switch(master){
				case SYNTAX:
					return synDocPage;
				case SEMANTICS:
					return semDocPage;
			}							
			return null;
		}
		
	}
	
	protected void createMasterPart(final IManagedForm managedForm,	Composite parent) {										
		FormToolkit toolkit = managedForm.getToolkit();		
		GridData gd;
		
		ScrolledForm form = toolkit.createScrolledForm(parent);
		formBody = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		
		formBody.setLayout(FormsUtils.createStandardGridLayout(3));				
				
		// Short title
		Label shortTitleLabel = toolkit.createLabel(formBody, "Short Title:");
		shortTitleLabel.setForeground(Utils.getBlueColor());
		shortTitleText = FormsUtils.createTextInGrid(managedForm, formBody, 2, true);
		shortTitleText.setEnabled(false);
		
		// Full title
		Label fullTitleLabel = toolkit.createLabel(formBody, "Full Title:");
		fullTitleLabel.setForeground(Utils.getBlueColor());		
		fullTitleText = FormsUtils.createTextInGrid(managedForm, formBody, 2, true);

		// Create additional controls
		createAdditionalControls(managedForm, formBody);
		
		// SYNTAX
		createSyntaxSection(managedForm, formBody);
		
		// SEMANTICS
		createSemanticsSection(managedForm, formBody);	
		
		// DATA BINDINGS
		createDataBindings();		 		
	}
	
	protected abstract void createAdditionalControls(IManagedForm managedForm, Composite parent);

	private void createSyntaxSection(final IManagedForm managedForm, Composite parent){
		Set<FileExtension> exts;
		
		// Syntax section part
		SectionPart syntaxSectionPart =  FormsUtils.createSection(managedForm, parent, 
				"Syntax", "Add syntax description documents.", 3, FormsUtils.DEFAULT_SPAN, true);
		
		// Syntax client
		Composite syntaxClient = createDocumentTable(managedForm, syntaxSectionPart, Master.SYNTAX);
		
		// Abstract Syntax 
		exts = new HashSet<FileExtension>();
		exts.add(FileExtension.ECORE);
		abstractSyntaxText = createLinkTextNewBrowse(managedForm, syntaxSectionPart, syntaxClient, 
				"Abstract Syntax:", "Model abstract syntax in Ecore (*.ecore)", exts,
				"Create new abstract syntax file in Ecore.");

		// Concrete Syntax
		exts = new HashSet<FileExtension>();
		exts.add(FileExtension.CS);
		concreteSyntaxText = createLinkTextNewBrowse(managedForm, syntaxSectionPart, syntaxClient, 
				"Concrete Syntax:", "Model concrete syntax for EMFText (*.cs)", exts,
				"Open new EMFText project to construct a concrete model syntax. Old link to concrete syntax will be rmoved.");
		
	}

	
	private Composite createDocumentTable(final IManagedForm managedForm, SectionPart part, final Master master){
		FormToolkit toolkit = managedForm.getToolkit();
		TableWrapData td;
		GridData gd;

		Section parent = part.getSection();
		Composite client = (Composite)part.getSection().getClient();
		
		// Document table
		final TableFormPart docTablePart = FormsUtils.createTableInGrid(managedForm, client, 
				this.DOC_TABLE_HEIGHT_HINT, this.DOC_TABLE_WIDTH_HINT, 3, 2);
		Table docTable = docTablePart.getTable();
		
		// Table viewer
		TableViewer viewer = new TableViewer(docTable);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				//log.info("Master selectionChanged");
				managedForm.fireSelectionChanged(docTablePart, event.getSelection());
			}
		});		
		viewer.setLabelProvider(new MasterLabelProvider());
		viewer.setContentProvider(new ObservableListContentProvider());
		
		// Add doc button		
		Button addDoc = FormsUtils.createButtonInGrid(managedForm, client, "Add", "Add new document.");
				
		// Add doc button		
		Button removeDoc = FormsUtils.createButtonInGrid(managedForm, client, "Remove", "Remove document.");						

		switch(master){
			case SYNTAX: 
				//log.info("createDocumentTable syntax");	
				syntaxDocTable = docTable;
				syntaxDocViewer = viewer;	
				addDoc.addSelectionListener(new AddDocumentSelectionAdapter(docTablePart, getModel().getSyntax()));
				removeDoc.addSelectionListener(new RemoveItemSelectionAdapter(docTablePart, getModel().getSyntax(), viewer));
				break;
			case SEMANTICS: 
				//log.info("createDocumentTable semantics");
				semanticsDocTable = docTable;				
				semanticsDocViewer = viewer;
				addDoc.addSelectionListener(new AddDocumentSelectionAdapter(docTablePart, getModel().getSemantics()));
				removeDoc.addSelectionListener(new RemoveItemSelectionAdapter(docTablePart, getModel().getSemantics(), viewer));
				break;
		}				
				
		// return
		return client;
	}
	
	private Text createLinkTextNewBrowse(
			final IManagedForm managedForm, 
			final SectionPart part, 
			Composite parent, 
			final String linkText, 
			String toolTipText, 
			Set<FileExtension> exts,
			String newButtonTooltip)
	{
		FormToolkit toolkit = managedForm.getToolkit();
				
		Hyperlink link = toolkit.createHyperlink(parent, linkText, SWT.NULL);
		link.setToolTipText(toolTipText);
				
		Text text = FormsUtils.createTextInGrid(managedForm, parent, false); 
		
		link.addHyperlinkListener(new OpenHyperlinkAdapter(text));		
								
		FormsUtils.createNewButtonInGrid(managedForm, parent, exts, text, newButtonTooltip);
		
		FormsUtils.createBrowseButtonInGrid(managedForm, parent, text);
				
		return text;		
	}	
		
	private void createSemanticsSection(final IManagedForm managedForm, Composite parent){
		Set<FileExtension> exts;
		
		// Semantics section part
		SectionPart semanticsSectionPart = FormsUtils.createSection(managedForm, parent, 
				"Semantics", "Add semantics description documents.", 3, FormsUtils.DEFAULT_SPAN, true);		
		
		// Semantics client
		Composite semanticsClient = createDocumentTable(managedForm, semanticsSectionPart, Master.SEMANTICS);
		
		// Verbal Semantics
		exts = new HashSet<FileExtension>();
		exts.add(FileExtension.TXT);
		semanticsText = createLinkTextNewBrowse(
				managedForm, 
				semanticsSectionPart, 
				semanticsClient, 
				"Semantic mapping:", 
				"Model mapping into the formal model (AMN) expressed in natural language (*.txt, *.pdf).", 
				exts,
				"Create new text file decribing the model mapping into the formal model (AMN) using natural language.");
		
		// Semantic Transformation
		exts = new HashSet<FileExtension>();
		exts.add(FileExtension.ATL);
		transformation = createLinkTextNewBrowse(
				managedForm, 
				semanticsSectionPart, 
				semanticsClient, 
				"Semantic Transformation:",
				"Model ATL-transformation (*.atl) into the formal model (AMN).",
				exts,
				"Create new ATL project to construct an ATL-transformation of the model into the AMN. Old link to transformation will be rmoved.");
		
		// Semantic Transformation
		exts = new HashSet<FileExtension>();
		exts.add(FileExtension.REF);
		this.semanticSpec = createLinkTextNewBrowse(
				managedForm, 
				semanticsSectionPart, 
				semanticsClient, 
				"Semantic Specification:",
				"Model semantics expressed as a specification (*.ref) in the formal model (AMN).",
				exts,
				"Create new AMN-specification.");
		
	}
	
	protected void createDataBindings(){

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(shortTitleText), 
				EMFProperties.value(ModelRegistryPackage.Literals.MODEL_REG__SHORT_TITLE).observe(getModel()));

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(fullTitleText), 
				EMFProperties.value(ModelRegistryPackage.Literals.MODEL_REG__TITLE).observe(getModel()));		

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(abstractSyntaxText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.MODEL_REG__ABSTRACT_SYNTAX,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(getModel()));		
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(concreteSyntaxText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.MODEL_REG__CONCRETE_SYNTAX,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(getModel()));		
				
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(semanticsText), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.MODEL_REG__SEMANTIC_MAPPING,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(getModel()));					
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(transformation), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.MODEL_REG__SEMANTIC_TRANSFORMATION,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(getModel()));		

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(this.semanticSpec), 
				EMFProperties.value(FeaturePath.fromList(
						ModelRegistryPackage.Literals.MODEL_REG__SEMANTIC_SPEC,
						ModelRegistryPackage.Literals.FILE__REFERENCE)).observe(getModel()));				
		
		// syntaxDocViewer.setContentProvider(new SyntaxMasterContentProvider());
		//syntaxDocViewer.setContentProvider(new ObservableListContentProvider());		
		syntaxDocViewer.setInput(EMFObservables.observeList(getModel(), ModelRegistryPackage.Literals.MODEL_REG__SYNTAX));

		//semanticsDocViewer.setContentProvider(new SemanticsMasterContentProvider());
		//semanticsDocViewer.setContentProvider(new ObservableListContentProvider());
		semanticsDocViewer.setInput(EMFProperties.list(ModelRegistryPackage.Literals.MODEL_REG__SEMANTICS).observe(getModel()));
		
		
		
	}
	
	
	protected void registerPages(DetailsPart detailsPart) {
		//log.info("Page registered.");

		DocumentDetailsPage synDocPage = new DocumentDetailsPage(bindingContext, ViewersObservables.observeSingleSelection(syntaxDocViewer));
		DocumentDetailsPage semDocPage = new DocumentDetailsPage(bindingContext, ViewersObservables.observeSingleSelection(semanticsDocViewer));
		
		
		this.docDetailsPageProvider = new DocumentDetailsPageProvider(synDocPage, semDocPage);						
		
		detailsPart.setPageProvider(this.docDetailsPageProvider);
	}


}
