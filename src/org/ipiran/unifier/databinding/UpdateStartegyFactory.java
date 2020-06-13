package org.ipiran.unifier.databinding;

import org.eclipse.emf.databinding.EMFUpdateValueStrategy;

public class UpdateStartegyFactory {
	public static EMFUpdateValueStrategy entailmentKind2Boolean(){
		EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy();
		strategy.setConverter(new EntailmentKind2BooleanConverter());
		
		return strategy;
	}
	
	public static EMFUpdateValueStrategy boolean2EntailmentKind(){
		EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy();
		strategy.setConverter(new Boolean2EntailmentKindConverter());
		
		return strategy;		
	}
	
	public static EMFUpdateValueStrategy filePath2FileContents(){
		EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy();
		strategy.setConverter(new FilePath2FileContentsConverter());
		
		return strategy;		
	}
	
}
