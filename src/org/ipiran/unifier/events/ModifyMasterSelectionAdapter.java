package org.ipiran.unifier.events;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.ipiran.unifier.ModelRole;
import org.ipiran.unifier.parts.TableFormPart;

import sun.util.logging.resources.logging;

public abstract class ModifyMasterSelectionAdapter extends TableSelectionAdapter {
	private static final Logger log = Logger.getLogger(ModifyMasterSelectionAdapter.class);
	
	protected IObservableValue master;
	protected ModelRole role;
	
	ModifyMasterSelectionAdapter(TableFormPart part, IObservableValue master, ModelRole role) {
		super(part);
		
		this.master = master;
		this.role = role;
	}

}
