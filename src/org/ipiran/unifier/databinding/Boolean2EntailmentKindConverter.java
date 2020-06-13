package org.ipiran.unifier.databinding;

import org.eclipse.core.databinding.conversion.Converter;
import org.ipiran.unifier.EntailmentKind;

public class Boolean2EntailmentKindConverter extends Converter {

	public Boolean2EntailmentKindConverter() {
		super(Boolean.class, EntailmentKind.class);
	}

	@Override
	public Object convert(Object fromObject) {		
		if (fromObject != null){
			boolean value = (Boolean)fromObject;
			
			if(value == Boolean.TRUE){
				return EntailmentKind.POSITIVE;
			} else
			if(value == Boolean.FALSE){
				return EntailmentKind.NEGATIVE;
			} else{
				return EntailmentKind.POSITIVE;
			}
		} else {
			return EntailmentKind.POSITIVE;
		}				
	}
}
