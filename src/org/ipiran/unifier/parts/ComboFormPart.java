package org.ipiran.unifier.parts;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.forms.AbstractFormPart;

public class ComboFormPart extends AbstractFormPart {
	private static final Logger log = Logger.getLogger(ComboFormPart.class); 		
	
	private Combo combo;
	
	public ComboFormPart(Combo combo){
		this.combo = combo;
	}
	
	public Combo getCombo(){
		return combo;
	}
}
