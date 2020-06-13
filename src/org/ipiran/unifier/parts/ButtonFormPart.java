package org.ipiran.unifier.parts;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.forms.AbstractFormPart;

public class ButtonFormPart extends AbstractFormPart {
	private static final Logger log = Logger.getLogger(ButtonFormPart.class); 		
	
	private Button button;
	
	public ButtonFormPart(Button button){
		this.button = button;
	}
	
	public Button getButton(){
		return button;
	}		
}