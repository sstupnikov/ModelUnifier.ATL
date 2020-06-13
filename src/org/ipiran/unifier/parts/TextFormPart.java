package org.ipiran.unifier.parts;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.AbstractFormPart;

public class TextFormPart extends AbstractFormPart {
	private static final Logger log = Logger.getLogger(TextFormPart.class); 		
	
	private Text text;
	
	public TextFormPart(Text text){
		this.text = text;
	}
	
	public Text getText(){
		return text;
	}
}
