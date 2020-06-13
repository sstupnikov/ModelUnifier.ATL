package org.ipiran.unifier.parts;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.AbstractFormPart;

public class TableFormPart extends AbstractFormPart {
	private static final Logger log = Logger.getLogger(TextFormPart.class); 		
	
	private Table table;
	
	public TableFormPart(Table table){
		this.table = table;
	}
	
	public Table getTable(){
		return table;
	}
}
