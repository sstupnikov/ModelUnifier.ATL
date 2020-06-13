package org.ipiran.unifier.utils;

import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.events.BrowseFileSelectionAdapter;
import org.ipiran.unifier.events.NewFileSelectionAdapter;
import org.ipiran.unifier.events.OpenHyperlinkAdapter;
import org.ipiran.unifier.parts.ButtonFormPart;
import org.ipiran.unifier.parts.TableFormPart;
import org.ipiran.unifier.parts.TextFormPart;

public class FormsUtils {

	public static final int DEFAULT_SPAN = 4; 
	public static final int MIN_SPAN = 1;
	public static final int LABEL_WIDTH_HINT = 10;
	
	public enum Buttons{ 
		NULL { public String toString() { return "NULL"; } },
		NEW { public String toString() { return "New"; } },
		CREATE { public String toString() { return "Create"; } },
		GENERATE { public String toString() { return "Generate"; } }
	}	
	
	// Create section to put into <span>-column grid layout.
	// Create and return section part.
	// Create 4-column client grid layout. 	
	public static SectionPart createSection(IManagedForm managedForm, Composite parent, String title, String desc, int extSpan, int intSpan, boolean fillVertical){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;

		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE | Section.DESCRIPTION);
		section.setText(title);
		section.setDescription(desc);				
		section.setExpanded(true);
		section.marginHeight = 5;
		if(fillVertical){
			gd = new GridData(GridData.FILL_BOTH);
		} else{
			gd = new GridData(GridData.FILL_HORIZONTAL);
		}
		gd.horizontalSpan = extSpan;
		section.setLayoutData(gd);		
		
		SectionPart part = new SectionPart(section);
		managedForm.addPart(part);		

		Composite client = toolkit.createComposite(section, SWT.FILL | SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = intSpan;
		layout.marginHeight = 5;
		layout.marginWidth = 0;
		client.setLayout(layout);
		toolkit.paintBordersFor(client);
		
		section.setClient(client);		
		
		return part;
	}
	
	
	public static SectionPart createSection(IManagedForm managedForm, Composite parent, String title, String desc, int extSpan, int intSpan){
		return createSection(managedForm, parent, title, desc, extSpan, intSpan, false);
	}
	
	public static SectionPart createSection(IManagedForm managedForm, Composite parent, String title, String desc, int extSpan){
		return createSection(managedForm, parent, title, desc, extSpan, DEFAULT_SPAN);
	}
	
	public static SectionPart createSection(IManagedForm managedForm, Composite parent, String title, String desc){
		return createSection(managedForm, parent, title, desc, DEFAULT_SPAN, DEFAULT_SPAN);
	}	
	
	// Create controls to put into 4-column grid layout as a row.
	// Controls are: Hyperlink, Text, [Button], Browse Button
	public static Text createLinkTextButtonsInGrid(
			IManagedForm managedForm,  
			Composite parent, 
			String linkText, 
			String linkToolTipText,
			int textSpan,
			boolean textIsEditable,
			Set<FileExtension> newExtensions,
			Buttons button,
			String additionalButtonToolTip,
			SelectionListener additionalButtonListener
			)
	{		
		assert (textSpan == 1) || (textSpan == 2 && button == Buttons.NULL);
		
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;

		// Create hyperlink
		Hyperlink link = toolkit.createHyperlink(parent, linkText, SWT.NULL);
		link.setToolTipText(linkToolTipText);

		// Create text
		Text text = FormsUtils.createTextInGrid(managedForm, parent, textSpan, textIsEditable); 
								
		link.addHyperlinkListener(new OpenHyperlinkAdapter(text));

		// Create additional button
		switch(button){
			case NULL:
				break;
			case NEW:
				createNewButtonInGrid(managedForm, parent, newExtensions, text, additionalButtonToolTip);
				break;
			case CREATE:
			case GENERATE:
				Button generate = createButtonInGrid(managedForm, parent, button.toString(), additionalButtonToolTip);	
				if(additionalButtonListener != null){
					generate.addSelectionListener(additionalButtonListener);
				}
				break;
		}
		
		// Create browse button
		createBrowseButtonInGrid(managedForm, parent, text);
		
		return text;
	}
	
	// Create a hyperlink observing text
	public static Hyperlink createHyperlink(IManagedForm managedForm,  Composite parent, String linkText, String toolTipText, Text observedText){
		FormToolkit toolkit = managedForm.getToolkit();
				
		Hyperlink link = toolkit.createHyperlink(parent, linkText, SWT.NULL);
		link.setToolTipText(toolTipText);
		link.addHyperlinkListener(new OpenHyperlinkAdapter(observedText));
		
		return link;
	}
	
	// Create horizontally filled text to be put in grid layout.
	public static Text createTextInGrid(IManagedForm managedForm,  Composite parent, int span, int heightHint, boolean editable){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;

		Text text = toolkit.createText(parent, "");		
		text.setEditable(editable);				
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = span;
		if(heightHint > 0){
			gd.heightHint = heightHint;
		}
		
		text.setLayoutData(gd);		

		final TextFormPart part = new TextFormPart(text);
		managedForm.addPart(part);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				part.markDirty();			
			}
		});
				
		return text;
	}

	public static Text createScrollableTextInGrid(IManagedForm managedForm,  Composite parent, int span, int heightHint, boolean editable){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;

		Text text = toolkit.createText(parent, "", SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = span;
		gd.heightHint = heightHint;
		if(heightHint > 0){
			gd.heightHint = heightHint;
		}
		
		text.setLayoutData(gd);

		final TextFormPart part = new TextFormPart(text);
		managedForm.addPart(part);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				part.markDirty();
			}
		});				
		
		return text;
	}
	
	public static Text createTextInGrid(IManagedForm managedForm, Composite parent, int span, boolean editable){
		return createTextInGrid(managedForm, parent, span, 0, editable);
	}
	
	public static Text createTextInGrid(IManagedForm managedForm,  Composite parent, boolean editable){
		return createTextInGrid(managedForm, parent, 1, editable);
	}
	
	
	
	public static Button createBrowseButtonInGrid(IManagedForm managedForm,  Composite parent, Text text){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;		
		
		Button browseButton = toolkit.createButton(parent, "...", SWT.PUSH); 		
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalAlignment = GridData.FILL;		
		browseButton.setLayoutData(gd);
		browseButton.addSelectionListener(new BrowseFileSelectionAdapter(text));		
		
		return browseButton;		
	}		
	
	
	public static Button createNewButtonInGrid(IManagedForm managedForm,  Composite parent, Set<FileExtension> exts, Text text, String toolTip){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;
		
		Button button = toolkit.createButton(parent, "New", SWT.PUSH); 		
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalAlignment = GridData.FILL;
		button.setLayoutData(gd);		
		button.setToolTipText(toolTip);
		button.addSelectionListener(new NewFileSelectionAdapter(text, exts));		
		
		return button;		
	}		
	
	public static Button createButtonInGrid(IManagedForm managedForm, Composite parent, String title, String toolTip){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;

		Button button = toolkit.createButton(parent, title, SWT.PUSH); 
		button.setToolTipText(toolTip);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalAlignment = GridData.FILL;
		button.setLayoutData(gd);				
		
		return button;
	}
	
	public static Button createCheckButtonInGrid(IManagedForm managedForm, Composite parent, String title, String toolTip, int span){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;		
		
		Button button = toolkit.createButton(parent, title, SWT.CHECK);
		button.setToolTipText(toolTip);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalAlignment = GridData.FILL;
		gd.horizontalSpan = span;
		button.setLayoutData(gd);
		final ButtonFormPart buttonPart = new ButtonFormPart(button);
		managedForm.addPart(buttonPart);		
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				buttonPart.markDirty();				
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		return button;
	}

	public static Button createCheckButtonInGrid(IManagedForm managedForm, Composite parent, String title, String toolTip){
		return createCheckButtonInGrid(managedForm, parent, title, toolTip, DEFAULT_SPAN);
	}
	
	
	// Create grid layout to put various controls into.
	public static GridLayout createStandardGridLayout(int numColumns){
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		layout.marginLeft = 5;
		layout.marginRight = 5;
		layout.marginBottom  = 10;
		layout.marginTop  = 10;		
		
		return layout;
	}

	public static GridLayout createStandardGridLayout(){
		return createStandardGridLayout(MIN_SPAN);
	}	

	public static GridLayout createCompactGridLayout(int numColumns){
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		layout.marginLeft = 5;
		layout.marginRight = 5;
		layout.marginBottom  = 5;
		layout.marginTop  = 0;		
		
		return layout;
	}	

	public static GridLayout createNoMarginGridLayout(int numColumns){
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginBottom  = 0;
		layout.marginTop  = 0;		
		
		return layout;
	}	
	
	
	// Create table wrap layout to put various controls into.
	public static TableWrapLayout createStandardTableWrapLayout(int numColumns){
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = numColumns;
		layout.leftMargin = 5;
		layout.rightMargin = 5;
		layout.bottomMargin  = 10;
		layout.topMargin  = 10;		
		
		return layout;
	}

	public static TableWrapLayout createStandardTableWrapLayout(){
		return createStandardTableWrapLayout(MIN_SPAN);
	}

	public static Composite createCompositeInGrid(IManagedForm managedForm, Composite parent, int horSpan){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;
		
		Composite block = toolkit.createComposite(parent);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = horSpan;
		block.setLayoutData(gd);
		block.setLayout(FormsUtils.createNoMarginGridLayout(DEFAULT_SPAN));		
		
		return block;
	}

	public static Composite createCompositeInGrid(IManagedForm managedForm, Composite parent){
		return createCompositeInGrid(managedForm, parent, MIN_SPAN);
	}	
	
	public static Label createEmptyLabel(IManagedForm managedForm,  Composite parent){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;		
		
		Label label = toolkit.createLabel(parent, "");		
		gd = new GridData();
		gd.horizontalAlignment = SWT.CENTER;
		gd.widthHint = LABEL_WIDTH_HINT;
		label.setLayoutData(gd);
		
		return label;
	}	

	public static Section createTreeNodeSectionInGrid(IManagedForm managedForm,  Composite parent, String title, int clientSpan){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;
		
		Section section = toolkit.createSection(parent, Section.TREE_NODE);
		section.setText(title);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = DEFAULT_SPAN;
		section.setLayoutData(gd);
		section.setExpanded(true);
		Composite comp = toolkit.createComposite(section);
		section.setClient(comp);		
		comp.setLayout(FormsUtils.createCompactGridLayout(clientSpan));
		
		return section;
	}
	
	
	public static Label createImageLabelInGrid(IManagedForm managedForm,  Composite parent, Image image){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;		
		
		Label label = toolkit.createLabel(parent, "");
		label.setImage(image);
		gd = new GridData();
		gd.horizontalAlignment = SWT.CENTER;
		gd.verticalAlignment = SWT.CENTER;
		//gd.widthHint = LABEL_WIDTH_HINT;
		label.setLayoutData(gd);
		
		return label;		
	}
	
	public static Label createLabelInGrid(IManagedForm managedForm,  Composite parent, String title, int horSpan){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;		

		Label label = toolkit.createLabel(parent, title);		
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalSpan = horSpan;
		label.setLayoutData(gd);		
		label.setForeground(Utils.getBlueColor());
		
		return label;
	}
	
	public static Label createHorizontalSeparatorInGrid(IManagedForm managedForm,  Composite parent, int horSpan){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;		
		
		Label sepLabel = toolkit.createSeparator(parent, SWT.SEPARATOR | SWT.HORIZONTAL);		
		gd = new GridData(GridData.FILL_HORIZONTAL);		
		gd.horizontalSpan = horSpan;		
		sepLabel.setLayoutData(gd);			
		
		return sepLabel;
	} 
	
	public static TableFormPart createTableInGrid(IManagedForm managedForm,  Composite parent, int heightHint, int widthHint, int horSpan, int verSpan){
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd;	
		
		Table table = toolkit.createTable(parent, SWT.NULL);		
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = heightHint;
		gd.widthHint = widthHint;	
		gd.horizontalSpan = horSpan;
		gd.verticalSpan = verSpan;
		table.setLayoutData(gd);
		final TableFormPart tablePart = new TableFormPart(table);
		managedForm.addPart(tablePart);	
		
		return tablePart;
	}
	
	public static ToolBarManager createSectionToolBarManager(Section section, Action action){
		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
	    ToolBar toolbar = toolBarManager.createControl(section);
	    
	    final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
	    toolbar.setCursor(handCursor);
	    // Cursor needs to be explicitly disposed
	    toolbar.addDisposeListener(new DisposeListener() {
	        public void widgetDisposed(DisposeEvent e) {
	            if ((handCursor != null) && (handCursor.isDisposed() == false)) {
	                handCursor.dispose();
	            }
	        }
	    });

	    // Add action to the tool bar
	    toolBarManager.add(action);

	    toolBarManager.update(true);
	    section.setTextClient(toolbar);	
	    
	    return toolBarManager;
	}
}

