package org.ipiran.unifier.events;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.ipiran.unifier.ModelRegistry.Document;
import org.ipiran.unifier.ModelRegistry.ModelRegistryFactory;
import org.ipiran.unifier.parts.TableFormPart;

public class AddDocumentSelectionAdapter extends AddEObjectSelectionAdapter {

	public AddDocumentSelectionAdapter(TableFormPart part, List<Document> docs){
		super(part, docs);
	}	
	
	@Override
	protected EObject createItem() {
		ModelRegistryFactory factory = ModelRegistryFactory.eINSTANCE;
		Document doc = factory.createDocument();
		doc.setTitle("new_doc");
		
		return doc;
	}

}
