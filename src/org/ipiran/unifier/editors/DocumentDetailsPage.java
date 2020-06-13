package org.ipiran.unifier.editors;


import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.ipiran.unifier.ModelRegistry.Document;
import org.ipiran.unifier.ModelRegistry.ModelRegistryPackage;
import org.ipiran.unifier.databinding.DateTimeObservableValue;
import org.ipiran.unifier.events.BrowseFileSelectionAdapter;
import org.ipiran.unifier.events.OpenHyperlinkAdapter;
import org.ipiran.unifier.utils.FormsUtils;
import org.ipiran.unifier.utils.Utils;

public class DocumentDetailsPage extends DataBindingDetailsPage {
	private static final Logger log = Logger.getLogger(DocumentDetailsPage.class);	
		
	private SectionPart docPart;	
	private Text titleText;
	private Text publisherText;
	private Text linkText;	
	private Text authorText;	
	private Hyperlink link;
	private DateTime date;

	
	public DocumentDetailsPage(EMFDataBindingContext bindingContext, IObservableValue master){	
		super(bindingContext, master);
	}
	
	
	@Override
	public void createContents(Composite parent) {
		super.createContents(parent);
		
		//log.info("DocumentDetailsPage.createContents");						
		FormToolkit toolkit = mform.getToolkit();				
		TableWrapData td;
		GridData gd;
		
		//parent.setLayout(FormsUtils.createStandardTableWrapLayout());	
		parent.setLayout(FormsUtils.createStandardGridLayout());
		
		SectionPart docSectionPart = FormsUtils.createSection(mform, parent, "Document Details", "Add document details.", 1, 4);
		Section docSection = docSectionPart.getSection();
		Composite client = (Composite)docSection.getClient();												
		
		// Details widgets: Title	
		titleText = createLabelText(client, "Title:");
		
		// Details widgets: Author
		authorText = createLabelText(client, "Authors:");					
		
		// Details widgets: Publisher
		publisherText = createLabelText(client, "Publisher:");	
		
		// Details widgets: Date
		Label dateLabel = toolkit.createLabel(client, "Date:");
		dateLabel.setForeground(Utils.getBlueColor());				
		date = new DateTime(client, SWT.DATE | SWT.DROP_DOWN);
		date.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				docPart.markDirty();
			}
		});
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 3;
		date.setLayoutData(gd);		
		
		//  Details widgets: Link
		linkText = FormsUtils.createLinkTextButtonsInGrid(mform, client, "Link:", 
				"Link to the document.", 2, true, null, FormsUtils.Buttons.NULL, null, null);
		
		//Create bindings
		createBindings();
	}	

	private Text createLabelText(Composite parent, String labelText){
		FormToolkit toolkit = mform.getToolkit();		
		
		Label titleLabel = toolkit.createLabel(parent, labelText);
		titleLabel.setForeground(Utils.getBlueColor());		
		Text text = FormsUtils.createTextInGrid(mform, parent, 3, true); 
						
		return text;
	} 
		
	protected void createBindings(){
		//log.info("Create detail bindings.");
		
		//if(titleText == null) log.info("Doc title is null.");
		//if(master == null) log.info("Master is null");

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(titleText), 
				EMFProperties.value(ModelRegistryPackage.Literals.DOCUMENT__TITLE).observeDetail(master));				
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(publisherText), 
				EMFProperties.value(ModelRegistryPackage.Literals.DOCUMENT__PUBLISHER).observeDetail(master));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(authorText), 
				EMFProperties.value(ModelRegistryPackage.Literals.DOCUMENT__AUTHORS).observeDetail(master));
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(linkText), 
				EMFProperties.value(ModelRegistryPackage.Literals.FILE__REFERENCE).observeDetail(master));				
		bindingContext.bindValue(new DateTimeObservableValue(date), 
				EMFProperties.value(ModelRegistryPackage.Literals.DOCUMENT__DATE).observeDetail(master));
				
		
		//log.info("Detail bindings are set.");
	}
	
}
