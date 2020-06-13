package org.ipiran.unifier.events;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionAdapter;
import org.ipiran.unifier.parts.TableFormPart;

public class TableSelectionAdapter extends SelectionAdapter{
	private static final Logger log = Logger.getLogger(TableSelectionAdapter.class);

	protected TableFormPart part;

	TableSelectionAdapter(TableFormPart part){
		super();
		
		this.part = part;
	}


}
