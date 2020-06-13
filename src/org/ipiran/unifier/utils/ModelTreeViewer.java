package org.ipiran.unifier.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.ipiran.unifier.FileExtension;
import org.ipiran.unifier.ModelUnifierPlugin;
import org.ipiran.unifier.ModelRegistry.ExtensionReg;
import org.ipiran.unifier.ModelRegistry.ModelReg;

public class ModelTreeViewer {
	
	private static final Logger log = Logger.getLogger(ModelTreeViewer.class);
	
	private static final String UNDEFINED = "UNDEFINED"; 
	private static final Image IMG_MODEL = ModelUnifierPlugin.getDefault().getImage(ModelUnifierPlugin.IMG_MODEL);
	private static final Image IMG_EXTENSION = ModelUnifierPlugin.getDefault().getImage(ModelUnifierPlugin.IMG_EXTENSION);	
	
	private String editingExtensionPath; 
	
	private boolean modelSelected = false;
	private String selectedModelPath;
	
	
	public ModelTreeViewer(String editingExtensionPath){
		this.editingExtensionPath = editingExtensionPath;
		log.info("editingExtensionPath: " + editingExtensionPath);
	}
	
	
	// Window class
	private class ModelTreeWindow extends ApplicationWindow{
		private Button ok;
		private Button cancel;
		
		public ModelTreeWindow(Shell parentShell) {
			super(parentShell);
		    setBlockOnOpen(true);
		    this.setShellStyle(SWT.APPLICATION_MODAL | SWT.CLOSE);
		    open();
		}

		protected void configureShell(Shell shell) {
			super.configureShell(shell);
			//shell.setSize(400, 400);
			shell.setText("Models and Extensions in the Workspace");			
			shell.setImage(ModelUnifierPlugin.getDefault().getImage(ModelUnifierPlugin.IMG_PERSPECTIVE));
		}

		protected Control createContents(Composite parent) {		
			GridData gd;
			GridLayout layout;
			
			Composite composite = new Composite(parent, SWT.NONE);
			layout = FormsUtils.createCompactGridLayout(2);
			composite.setLayout(layout);

			// Description
			Label select = new Label(composite, SWT.LEFT);
			select.setText("Select a model or an extension to be the extended model.");
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			select.setLayoutData(gd);
			
			// Viewer
			gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			final TreeViewer viewer = new TreeViewer(composite);
			viewer.getTree().setLayoutData(gd);
			viewer.setContentProvider(new ModelTreeContentProvider());
			viewer.setLabelProvider(new ModelTreeLabelProvider());
			viewer.setInput(createTreeInput());
			viewer.expandAll();
			viewer.addSelectionChangedListener(new ISelectionChangedListener() {				
				@Override
				public void selectionChanged(SelectionChangedEvent event) {					
					if (!event.getSelection().isEmpty() && event.getSelection() instanceof IStructuredSelection) {												
						IStructuredSelection selection = (IStructuredSelection)event.getSelection();
						ModelNode node = (ModelNode)selection.getFirstElement();
						
						if(node != null){
							ok.setEnabled(true);
							selectedModelPath = node.localPath;
						} else{
							return;
						}
					} else{
						return;
					}					
				}
			});

			// Agenda
			Label imgModelLabel = new Label(composite, SWT.LEFT);
			imgModelLabel.setImage(IMG_MODEL);
			Label modelLabel = new Label(composite, SWT.LEFT);
			modelLabel.setText("Model");

			Label imgExtensionLabel = new Label(composite, SWT.LEFT);
			imgExtensionLabel.setImage(IMG_EXTENSION);
			Label extensionLabel = new Label(composite, SWT.LEFT);
			extensionLabel.setText("Extension");
			
			// Buttons
			Composite buttons = new Composite(composite, SWT.RIGHT_TO_LEFT);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			buttons.setLayoutData(gd);
			
			RowLayout rowLayout = new RowLayout();
			rowLayout.wrap = true;
			rowLayout.pack = false;
			rowLayout.spacing = 3;
			
			buttons.setLayout(rowLayout);

			cancel = new Button(buttons, SWT.CENTER);
			cancel.setText("Cancel");
			cancel.addSelectionListener(new CancelButtonSelectionListener(this));
						
			ok = new Button(buttons, SWT.CENTER);						
			ok.setText("OK");
			ok.setEnabled(false);
			ok.addSelectionListener(new OkButtonSelectionListener(this));			
			
			return composite;
		}		
	}
	
	private class OkButtonSelectionListener implements SelectionListener{
		private Window window;
		
		public OkButtonSelectionListener(Window window){
			this.window = window;
		}
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			modelSelected = true;			
			window.close();
		}				
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	}	
	
	private class CancelButtonSelectionListener implements SelectionListener{
		private Window window;
		
		public CancelButtonSelectionListener(Window window){
			this.window = window;
		}
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			modelSelected = false;			
			window.close();
		}				
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	}	
	
	
	// Node Class
	class ModelNode{
		public ModelReg card;
		public File file;
		public String localPath;
		
		public ModelNode extend;
		public Set<ModelNode> extendedBy;
		
		public ModelNode(){
			extendedBy = new HashSet<ModelNode>();
		};
	}
	
	// Content and Label Providers
	class ModelTreeContentProvider implements ITreeContentProvider {
		public Object[] getChildren(Object obj){
			ModelNode node = (ModelNode)obj;			
			return node.extendedBy.toArray();
		}

		public Object getParent(Object obj){
			ModelNode node = (ModelNode)obj;			
			return node.extend;
		}

		public boolean hasChildren(Object obj){
			ModelNode node = (ModelNode)obj;			
			
			if(node.extendedBy != null && node.extendedBy.size() != 0){
				return true;
			} else{
				return false;
			}
		}

		public Object[] getElements(Object obj) {
			ModelNode node = (ModelNode)obj;
			return node.extendedBy.toArray();			
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object obj1, Object obj2) {
		}
	}

	class ModelTreeLabelProvider implements ILabelProvider {
		private List listeners;


		public ModelTreeLabelProvider() {
			listeners = new ArrayList();
		}

		public void dispose() {
		}		
		
		public Image getImage(Object obj) {
			ModelNode node = (ModelNode)obj;
			if(node.card != null){ 
			    if(node.card instanceof ExtensionReg){
					return IMG_EXTENSION;
				} else{
					return IMG_MODEL;
				}
			} else{
				return null;
			}
		}

		public String getText(Object obj) {
			ModelNode node = (ModelNode)obj;
			
			if(node.card != null && node.card.getShortTitle() != null){
				return node.card.getShortTitle();
			} else{
				return UNDEFINED;
			}			
		}

		public boolean isLabelProperty(Object obj, String str) {
			return false;
		}

		public void addListener(ILabelProviderListener listener) {
			listeners.add(listener);
		}		
		
		public void removeListener(ILabelProviderListener listener) {
			listeners.remove(listener);
		}
	}
	
	// ModelTreeViewer methods	
	public void openWindow(){
		ModelTreeWindow window = new ModelTreeWindow(Utils.getShell());
	}
	
	public ModelNode createTreeInput(){
		ModelNode root = new ModelNode();
		String[] extensions =  {FileExtension.ERC.toString(), FileExtension.MRC.toString()};
		Map localPath2Model = new HashMap<String, ModelReg>();
		String workspacePath = Utils.getWorkspacePath().toOSString();
		
		Collection<File> modelFiles = FileUtils.listFiles(new File(workspacePath), extensions, true); 
		
		for(File file: modelFiles){
			String localPath = file.getAbsolutePath().substring(workspacePath.length());
			log.info("Model local path: " + localPath);
			
			// Do not add editing extension to the tree.
			if(localPath.compareTo(editingExtensionPath) != 0){
				ModelNode node = createModelNode(file);				
				localPath2Model.put(localPath, node);
			}
		}
		
		for(Object path: localPath2Model.keySet()){
			ModelNode node = (ModelNode)localPath2Model.get(path);
						
			if(node.card != null){
				// Select root nodes - models, not extensions.
				if(!(node.card instanceof ExtensionReg)){
					root.extendedBy.add(node);
				} 
				// For every extension create parent-child associations.
				else{					
					ExtensionReg ext = (ExtensionReg)node.card;
					
					// Add extension with empty extended model to roots.
					if(ext.getExtendedModel() != null){
						if(ext.getExtendedModel().getReference() == null){
							root.extendedBy.add(node);
						} else{
							log.info("Extended model path: " + ext.getExtendedModel().getReference());
							ModelNode parent = (ModelNode)localPath2Model.get(ext.getExtendedModel().getReference()); 
							if(parent != null){
								node.extend = parent;
								parent.extendedBy.add(node);
							}
						}
					}
				}
			}			
		} 
		
		return root;
	}
	
	public ModelNode createModelNode(File file){
		String workspacePath = Utils.getWorkspacePath().toOSString();
		
		ModelNode node = new ModelNode();
		
		node.file = file;
		node.localPath = file.getAbsolutePath().substring(workspacePath.length());
		
		// Load resource
		Resource res = null;
		ResourceSet rset = new ResourceSetImpl();		
		rset.getResourceFactoryRegistry().getProtocolToFactoryMap().put("*", new XMIResourceFactoryImpl());				
		try {
			res = rset.getResource(URI.createFileURI(file.getAbsolutePath()), true);			
			res.load(null);			
		} catch (Exception ex) {
			log.warn("Can not load resource from file: " + file.getAbsolutePath());
			log.info(ex.toString());
		}
		
		// Get model
		EObject eobject = res.getContents().get(0);
		if(eobject != null && eobject instanceof ModelReg){
			node.card = (ModelReg)eobject;
			log.info("Model loaded: " + node.card.getShortTitle());
		}		
		
		return node;
	}
	
	public String getSelectedModelPath(){
		return selectedModelPath;
	}
	
	public boolean isModelSelected(){
		return modelSelected;
	}
}
