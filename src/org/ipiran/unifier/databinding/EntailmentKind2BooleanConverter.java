package org.ipiran.unifier.databinding;

import org.eclipse.core.databinding.conversion.Converter;
import org.ipiran.unifier.EntailmentKind;

public class EntailmentKind2BooleanConverter extends Converter {

	public EntailmentKind2BooleanConverter() {
		super(EntailmentKind.class, Boolean.class);
	}

	@Override
	public Object convert(Object fromObject) {
		if (fromObject != null){
			EntailmentKind value = (EntailmentKind)fromObject;
			switch(value){
			case POSITIVE:
				return Boolean.TRUE; 
			case NEGATIVE:
				return Boolean.FALSE;
			default: 
				return Boolean.TRUE;
			}			
		} else {
			return Boolean.TRUE;
		}		
	}

}
