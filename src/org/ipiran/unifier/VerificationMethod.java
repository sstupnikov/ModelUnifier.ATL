package org.ipiran.unifier;

import java.util.ArrayList;
import java.util.List;

public enum VerificationMethod{
	REFINEMENT { public String toString() { return "Refinement"; } },
	ENTAILMENT { public String toString() { return "Entailment"; } };
	
	public static String[] toStringArray(){
        List<String> items = new ArrayList<String>();
        for(VerificationMethod item: VerificationMethod.values()){
        	items.add(item.toString());
        }
        String[] itemsArray = new String[items.size()];  
        items.toArray(itemsArray);
        
        return itemsArray;
	}
	
	public static VerificationMethod stringToValue(String str){
		VerificationMethod value = null;
		
		for(VerificationMethod item: VerificationMethod.values()){
			if(item.toString().compareTo(str) == 0)
				return item;
		}
		
		return value;
	}
}
